package com.neusoft.arltr.indexing.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.neusoft.arltr.common.constant.Constant;
import com.neusoft.arltr.common.entity.indexing.CronTask;
import com.neusoft.arltr.common.entity.indexing.DataImportLogs;
import com.neusoft.arltr.common.entity.indexing.PdmDocInfo;
import com.neusoft.arltr.common.entity.indexing.PdmDocInfoFail;
import com.neusoft.arltr.common.entity.search.Result;
import com.neusoft.arltr.common.entity.user.User;
import com.neusoft.arltr.indexing.model.Project;
import com.neusoft.arltr.indexing.repository.IndexingRepository;
import com.neusoft.arltr.indexing.repository.PdmDocInfoRepository;
import com.neusoft.arltr.indexing.repository.SolrRepository;
import com.neusoft.arltr.indexing.utils.SolrDataConverter;
import com.neusoft.arltr.indexing.ws.client.PdmClient;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service
public class IndexingService {

	private static final Logger logger = Logger.getLogger(IndexingService.class.getName());
	
	private static final int OPT_TYPE_NEW = 1;
	private static final int OPT_TYPE_UPD = 2;
	private static final int OPT_TYPE_DEL = 3;
	
	/**任务类型——自动*/
	private static final short INDEX_TASK_TYPE_AUTO = 1;
	
	/**任务状态——进行中*/
	private static final short INDEX_TASK_STATE_DOING = 1;
	
	/**任务状态——已完成*/
	private static final short INDEX_TASK_STATE_DONE = 2;
	
	/**任务状态——异常退出*/
	private static final short INDEX_TASK_STATE_EXCEPTION = 3;
	
	/**采集类型——全量*/
	private static final short INDEX_IMPORT_TYPE_ALL = 1;
	
	/**数据状态：未处理 */
	private static final int DATA_STATE_UNDISPOSED = 0;
	
	/**数据状态：已获取文件路径 */
	private static final int DATA_STATE_PATHGETED = 1;
	//yhh add pdm系统提供的连接有可能出现无效的情况，增加数据状态 连接无效
	/**数据状态： 路径无效**/
	public static final int DATA_STATE_INVALIDPATH=3;

//	public static final List<Integer> DATA_STATE_INVALID=new ArrayList<>();
//
//	{
//		DATA_STATE_INVALID.add(DATA_STATE_INVALIDPATH);
//		DATA_STATE_INVALID.add(PdmService.DATA_STATE_INDEX_FAIL);
//		DATA_STATE_INVALID.add(DATA_STATE_UNDISPOSED);
//
//
//	}
	
	@Autowired
	SolrRepository solrRepository;
	
	@Autowired
	IndexingRepository indexingRepository;
	
	@Autowired
	PdmClient pdmClient;
	
	@Autowired
	PdmDocInfoRepository pdmDocInfoRepository;
	
	@Autowired
	PdmService pdmService;

	@Autowired
	PdmDocInfoFailService pdmDocInfoFailService;
	
	private StringBuffer errorInfo;
	
	@Async
	public void createIndexOfTdmObject(Project object) {
		
		Result doc = null;
		
		switch (object.getOptType()) {
		case OPT_TYPE_NEW :
			
			doc = SolrDataConverter.convert(object);
			solrRepository.save(doc);
			break;
		case OPT_TYPE_UPD :
			
			doc = solrRepository.findByOriginalIdAndSource(object.getId(), Constant.SOURCE_TDM);
			
			if (doc == null) {
				doc = SolrDataConverter.convert(object);
			} else {
				SolrDataConverter.convert(object, doc);
			}
			
			solrRepository.save(doc);
			break;
		case OPT_TYPE_DEL :
			doc = solrRepository.findByOriginalIdAndSource(object.getId(), Constant.SOURCE_TDM);
			if (doc != null) {
				solrRepository.delete(doc);
			}
			break;
		}
	}
	
	@Async
	public void createIndexOfPdm(DataImportLogs task,Date date) {
		
		logger.info("===========PDM数据索引处理 START============");
		
		errorInfo = new StringBuffer();
		
		try {
			
			// 获取文档列表
			readDocList(task,date);


				// 获取文档的文件路径并持久化
			readPathOfDocFile();

				// 建立文档的solr索引
			createIndexOfPdmDoc();

			
		} catch (Exception e) {
			this.appendErrorInfo("PDM数据同步错误【" + e.getMessage() + "】");
		}
		
		if (this.errorInfo.length() > 0) {
			task.setTaskState(INDEX_TASK_STATE_EXCEPTION);
			task.setTaskInfo(task.getTaskInfo().replace("进行中",this.errorInfo.toString()));
		} else {
			task.setTaskState(INDEX_TASK_STATE_DONE);
			task.setTaskInfo(task.getTaskInfo().replace("进行中","PDM数据同步已完成"));
		}
		
		task.setEndTime(new Date());
		this.indexingRepository.save(task);
		
		logger.info("===========PDM数据索引处理 END============");
	}
	
	private void readDocList(DataImportLogs task,Date date) throws Exception {
		
		String dataXml = "";

		
		// 从PDM接口获取文档列表
//		if (task.getImportType() == INDEX_IMPORT_TYPE_ALL) {
//			dataXml = pdmClient.getDataXML(null);
//		} else {
			dataXml = pdmClient.getDataXML(date);
//		}
		
		// 解析接口返回值并持久化
		parseDataXml(dataXml);
	}
	
	private void parseDataXml(String xml) throws Exception {
		
		logger.info("===========PDM文档列表数据解析 START============");
		logger.info("解析前数据：" + xml);
		ObjectMapper objectMapper = new ObjectMapper();
		PdmDocInfo pdmDocInfo=null;
		try {
			
			StringReader read = new StringReader(xml);
	        InputSource source = new InputSource(read);
	        
	        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();  
	        DocumentBuilder db = dbf.newDocumentBuilder();  
	        Document document = db.parse(source);
	        
	        NodeList dNodes = document.getChildNodes();
	        
	        for (int i = 0; i < dNodes.getLength(); i++) {

	        	Node dNode = dNodes.item(i);
	        	
	        	if ("root".equalsIgnoreCase(dNode.getNodeName())) {
	        		
	        		NodeList rNodes = dNode.getChildNodes();
	        		
	        		for (int j = 0; j < rNodes.getLength(); j++) {
	        			
	        			Node rNode = rNodes.item(j);
	        			
	        			if ("data".equalsIgnoreCase(rNode.getNodeName())) {
	        				
	        				NodeList pNodes = rNode.getChildNodes();
	        				
	        				for (int k = 0; k < pNodes.getLength(); k++) {
	        					
	        					Node pNode = pNodes.item(k);
	        					
	        					if ("documentInfo".equalsIgnoreCase(pNode.getNodeName())) {
	        						
	        						PdmDocInfo doc = new PdmDocInfo();
	        						
	        						NodeList attributes = pNode.getChildNodes();
	        						
	        						for (int s = 0; s < attributes.getLength(); s++) {
	        							
	        							Node attribute = attributes.item(s);
	        							
	        							if (attribute.getNodeType() == Node.ELEMENT_NODE) { 
	        								
	        								PropertyDescriptor pd = new PropertyDescriptor(attribute.getNodeName(), doc.getClass());
	        								pd.getWriteMethod().invoke(doc, attribute.getTextContent());
	        							}
	        						}
	        						
	        						logger.info("解析后数据：" + objectMapper.writeValueAsString(doc));
	        						
	        						if ("URLData".equals(doc.getFileType())) {
	        							doc.setDataState(DATA_STATE_PATHGETED);
	        						} else {
	        							doc.setDataState(DATA_STATE_UNDISPOSED);
	        						}
									//yhh add  从pdm获取到的同步数据中，如果之前曾经处理过的文档数据，本次针对这个数据进行更新处理。
									pdmDocInfo=pdmDocInfoRepository.findByDocumentIdAndVersionSno(doc.getDocumentId(),doc.getVersionSno());
									if(null!=pdmDocInfo)
											doc.setId(pdmDocInfo.getId());
									doc.setCreateAt(new Date());
	        						pdmDocInfoRepository.save(doc);
	        					}
	        				}
	        			}
	        		}
	        	}
	        }
		} catch (Exception e) {
			logger.error("PDM文档列表数据解析出错");
			logger.error(e.getMessage(), e.getCause());
			e.printStackTrace();
			this.appendErrorInfo("PDM文档数据同步阶段出错,无法获取到增量更新文档元数据【" + e.getLocalizedMessage() + "】");
			throw e;
		}
		logger.info("===========PDM文档列表数据解析 END============");
	}
	
	private void readPathOfDocFile() {
		
		List<PdmDocInfo> docList = pdmDocInfoRepository.findByDataState(DATA_STATE_UNDISPOSED);
		
		for (PdmDocInfo doc : docList) {
			
			// 从PDM接口获取文档的文件路径
			String fileXml = pdmClient.getDocumentApplicationData(doc.getDocumentId(), doc.getVersionSno());
			
			try {
				// 解析接口返回值
				String filePath = parseFileXml(fileXml);
				doc.setFilePath(filePath);
				//yhh modify
				//doc.setDataState(DATA_STATE_PATHGETED);
				//如果获取的pdm文件地址为空，将该临时数据状态设置为连接无效
				doc.setDataState(org.apache.commons.lang3.StringUtils.isBlank(filePath)?DATA_STATE_INVALIDPATH:DATA_STATE_PATHGETED);
				//yhh add
				pdmDocInfoRepository.save(doc);
				if(org.apache.commons.lang3.StringUtils.isBlank(filePath)){
					PdmDocInfoFail pdmDocInfoFail=new PdmDocInfoFail();
					pdmDocInfoFail.setDocId(doc.getDocumentId());
					pdmDocInfoFail.setPdmDocInfoId(doc.getId());
					pdmDocInfoFail.setDocFilePath(doc.getFilePath());
					pdmDocInfoFail.setDocUrl(doc.getUrl());
					pdmDocInfoFail.setDocTitle(doc.getDocumentTitle());
					pdmDocInfoFail.setFailReason("PDM文档路径数据为空，获取filePath阶段出错");
					pdmDocInfoFail.setFailState(DATA_STATE_INVALIDPATH);
					pdmDocInfoFailService.addPdmDocInfoFail(pdmDocInfoFail);
				}
			
			} catch (Exception e) {
				logger.info("PDM文档路径数据解析出错：报文为【" + fileXml + "】");
//				this.appendErrorInfo(","+doc.getDocumentId());
				if(this.errorInfo.length()==0||this.errorInfo.indexOf("PDM文档路径失败错误")<0)
					this.appendErrorInfo("PDM文档路径失败错误");
				doc.setDataState(DATA_STATE_INVALIDPATH);
				pdmDocInfoRepository.save(doc);
				//yhh add
				PdmDocInfoFail pdmDocInfoFail=new PdmDocInfoFail();
				pdmDocInfoFail.setDocId(doc.getDocumentId());
				pdmDocInfoFail.setPdmDocInfoId(doc.getId());
				pdmDocInfoFail.setDocFilePath(doc.getFilePath());
				pdmDocInfoFail.setDocUrl(doc.getUrl());
				pdmDocInfoFail.setDocTitle(doc.getDocumentTitle());
				pdmDocInfoFail.setFailReason("PDM文档路径数据解析出错,获取filePath阶段出错【" + e.getLocalizedMessage() + "】");
				pdmDocInfoFail.setFailState(DATA_STATE_INVALIDPATH);
				pdmDocInfoFailService.addPdmDocInfoFail(pdmDocInfoFail);
				//yhh add end
				//continue;
			}
		}
		
		if (docList != null) {
			docList.clear();
		}
	}
	
	private String parseFileXml(String xml) throws Exception {
		
		logger.info("===========PDM文档主数据解析 START============");
		logger.info("解析前数据：" + xml);
//		ObjectMapper objectMapper = new ObjectMapper();
		
		try {
			
			StringReader read = new StringReader(xml);
	        InputSource source = new InputSource(read);
	        
	        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();  
	        DocumentBuilder db = dbf.newDocumentBuilder();  
	        Document document = db.parse(source);
	        
	        NodeList dNodes = document.getChildNodes();
	        
	        for (int i = 0; i < dNodes.getLength(); i++) {

	        	Node dNode = dNodes.item(i);
	        	
	        	if ("root".equalsIgnoreCase(dNode.getNodeName())) {
	        		
	        		NodeList rNodes = dNode.getChildNodes();
	        		
	        		for (int j = 0; j < rNodes.getLength(); j++) {
	        			
	        			Node rNode = rNodes.item(j);
	        			
	        			if ("data".equalsIgnoreCase(rNode.getNodeName())) {
	        				
	        				NodeList pNodes = rNode.getChildNodes();
	        				
	        				for (int k = 0; k < pNodes.getLength(); k++) {
	        					
	        					Node pNode = pNodes.item(k);
	        					
	        					if ("documentInfo".equalsIgnoreCase(pNode.getNodeName())) {
	        						
	        						NodeList attributes = pNode.getChildNodes();
	        						
	        						for (int s = 0; s < attributes.getLength(); s++) {
	        							
	        							Node attribute = attributes.item(s);
	        							
	        							if (attribute.getNodeType() == Node.ELEMENT_NODE && "filePath".equalsIgnoreCase(attribute.getNodeName())) { 
	        								
	        								return attribute.getTextContent();
	        							}
	        						}
	        						
//	        						logger.info("解析后数据：" + objectMapper.writeValueAsString(doc));
//	        						pdmDocInfoRepository.save(doc);
	        					}
	        				}
	        			}
	        		}
	        	}
	        }
		} catch (Exception e) {
			logger.error("PDM文档主数据解析出错");
			logger.error(e.getMessage(), e.getCause());
			e.printStackTrace();
			throw e;
		}
		logger.info("===========PDM文档主数据解析 END============");
		
		return "";
	}
	
	private void createIndexOfPdmDoc() throws Exception {
		
		List<PdmDocInfo> docList = pdmDocInfoRepository.findByDataState(DATA_STATE_PATHGETED);
		
		List<String> finishedTaskList = Collections.synchronizedList(new ArrayList<String>());
		int i=1;
		for (PdmDocInfo doc : docList) {
			pdmService.createIndexOfPdmDoc(doc, finishedTaskList, errorInfo);
			Thread.currentThread().sleep(1500);
//			控制每三个提交处理，三个处理完再继续处理
//			if(i%2==0) {
//				if(finishedTaskList.size() == docList.size())continue;
//				logger.info("已经发起了二个线程处理，现在开启等待");
//				while (!(finishedTaskList.size()  == i)) {
////					logger.info(finishedTaskList.size());
////					Thread.currentThread().sleep(1000);
//				}
//				logger.info("已经发起的三个线程处理结束，开始下一组线程");
//				Thread.currentThread().sleep(15000);
//			}
//			i++;
		}
		
		if (docList != null) {
			
			while(finishedTaskList.size() < docList.size()) {
				// 等待所有线程结束
			}
			
			docList.clear();
		}
	}
	
	private void appendErrorInfo(String msg) {
		this.errorInfo.append(msg + "\n");
	}
	
	/**
	 * 点击手动更新及设置定时器，向索引维护表插入数据
	 * @param taskType 区分手动及自动
	 * @param importType 手动更新选择的采集类型
	 * @param cronTask 定时器信息
	 * @param user 手动更新的执行者
	 * @return DataImportLogs 索引维护实体
	 */
	@Async
	public DataImportLogs insertData(short taskType,short importType,CronTask cronTask,User user){
		DataImportLogs dataImportLogs = new DataImportLogs();
		Date startTime = new Date();
		dataImportLogs.setStartTime(startTime);
		dataImportLogs.setTaskState(INDEX_TASK_STATE_DOING);
		dataImportLogs.setTaskType(taskType);
		String taskInfo = "";
		if(INDEX_TASK_TYPE_AUTO == taskType){  //表明为定时器
			dataImportLogs.setImportType(cronTask.getImportType());
			dataImportLogs.setExecutor(0);  //设置自动更新的执行者为0
			dataImportLogs.setExecutorName("系统自动更新");
			taskInfo = cronTask.getImportType() == INDEX_IMPORT_TYPE_ALL ? "索引全量更新" : "索引增量更新";
		}else{  //表明为手动更新
			dataImportLogs.setImportType(importType);
			dataImportLogs.setExecutor(user.getId());
			dataImportLogs.setExecutorName(user.getEmployeeName());
			taskInfo = importType == INDEX_IMPORT_TYPE_ALL ? "索引全量更新" : "索引增量更新";
		}
		dataImportLogs.setTaskInfo(taskInfo+"进行中");
		DataImportLogs dataImportLogsNew = this.indexingRepository.save(dataImportLogs);
//		DataImportLogs dataImportLogsUpdate = new DataImportLogs();
//		dataImportLogsNew.setTaskState(INDEX_TASK_STATE_DONE);
//		dataImportLogsNew.setTaskInfo(taskInfo+"已完成");
//		try {
//			Thread.sleep(10000);
//		} catch (Exception e) {
//			e.printStackTrace();
//			dataImportLogsNew.setTaskState(INDEX_TASK_STATE_EXCEPTION);
//			dataImportLogsNew.setTaskInfo(taskInfo+"异常退出");
//		}
//		dataImportLogsNew.setEndTime(new Date());
//		dataImportLogsUpdate = this.indexingRepository.save(dataImportLogsNew);
		
		createIndexOfPdm(dataImportLogsNew,new Date());
		
		return dataImportLogsNew;
	}

	/**
	 *
	 * @param taskType 任务类型
	 * @param user	用户 系统更新时为null
	 * @param date 更新日期
	 * @param taskInfo 任务信息
	 * @return
	 */
//	@Async
	public DataImportLogs fullAmountInsertData(short taskType,User user,String date,String taskInfo){
		DataImportLogs dataImportLogs = new DataImportLogs();

		dataImportLogs.setStartTime(new Date());
		dataImportLogs.setTaskState(INDEX_TASK_STATE_DOING);
		dataImportLogs.setTaskType(taskType);
		dataImportLogs.setImportType((short) 1);
		dataImportLogs.setExecutor(user==null?0:user.getId());
		dataImportLogs.setExecutorName(user==null?"系统自动更新":user.getUserName());
		dataImportLogs.setTaskInfo(taskInfo+"数据日期："+date+"|进行中");
		DataImportLogs dataImportLogsNew = this.indexingRepository.save(dataImportLogs);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		try {
			createIndexOfPdm(dataImportLogsNew, sdf.parse(date));
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return dataImportLogsNew;
	}
	
}
