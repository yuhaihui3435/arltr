/**
 * 
 */
package com.neusoft.arltr.indexing.service;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Random;

import com.neusoft.arltr.common.entity.indexing.PdmDocInfoFail;
import org.apache.log4j.Logger;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.solr.UncategorizedSolrException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.xml.sax.ContentHandler;

import com.neusoft.arltr.common.constant.Constant;
import com.neusoft.arltr.common.entity.indexing.PdmDocInfo;
import com.neusoft.arltr.common.entity.search.Result;
import com.neusoft.arltr.common.utils.ChineseToPinyin;
import com.neusoft.arltr.indexing.repository.PdmDocInfoRepository;
import com.neusoft.arltr.indexing.repository.SolrRepository;

/**
 * PDM业务Service
 * 
 *
 */
@Service
public class PdmService {
	//yhh add
	private static final Logger logger = Logger.getLogger(PdmService.class.getName());
	
	/**数据状态：已建立索引 */
	private static final int DATA_STATE_INDEXED = 2;
	/** yhh add数据状态：建立索引失败 */
	public static final int DATA_STATE_INDEX_FAIL = 4;
	
	@Autowired
	SolrRepository solrRepository;
	
	@Autowired
	PdmDocInfoRepository pdmDocInfoRepository;

	@Autowired
	PdmDocInfoFailService pdmDocInfoFailService;
	
	@Async
	public void createIndexOfPdmDoc(PdmDocInfo doc, List<String> finishedTaskList, StringBuffer errorInfo) throws Exception {
		
		try {
			Result solrDoc = solrRepository.findByOriginalIdAndSource(doc.getDocumentId(), Constant.SOURCE_PDM);
			
			// 建立索引
			if (solrDoc == null) {
				solrDoc = new Result();
			}
			
			solrDoc.setSource(Constant.SOURCE_PDM);
			solrDoc.setScore(0);
			solrDoc.setResourcename("PDM");
			solrDoc.setContentType("2");
			solrDoc.setOriginalId(doc.getDocumentId());
			solrDoc.setClassification(doc.getSecretLevel());
			solrDoc.setTitle(doc.getDocumentTitle());
			solrDoc.setTitleAbbre(ChineseToPinyin.getPinYinAbbre(solrDoc.getTitle()));
			solrDoc.setTitlePinyin(ChineseToPinyin.getPinYin(solrDoc.getTitle()));
			if ("ApplicationData".equals(doc.getFileType())) {
				solrDoc.setContent(getFileContent(doc.getFilePath()));
			} else {
				solrDoc.setContent(" ");
			}
			solrDoc.setAuthor(doc.getAuthor());
			solrDoc.setAuthorAbbre(ChineseToPinyin.getPinYinAbbre(solrDoc.getAuthor()));
			solrDoc.setAuthorPinyin(ChineseToPinyin.getPinYin(solrDoc.getAuthor()));
//			solrDoc.setCache((doc.getFileContent() != null && doc.getFileContent().length() > 32766) ? doc.getFileContent().substring(0, 32765) : doc.getFileContent());
			solrDoc.setCache(" ");
			solrDoc.setTimestamp(System.currentTimeMillis());
			solrDoc.setLastModified(new Date());
			solrDoc.setUrl(doc.getUrl());
			
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("documentCon", doc.getDocumentCon());
			jsonObject.put("documentPhase", doc.getDocumentPhase());
			jsonObject.put("fileSmallType", doc.getFileSmallType());
			jsonObject.put("majorOri", doc.getMajorOri());
			jsonObject.put("majorType", doc.getMajorType());
			jsonObject.put("documentDept", doc.getDocumentDept());
			jsonObject.put("keyWord", doc.getKeyWord());
			jsonObject.put("documentResponDept", doc.getDocumentResponDept());
			
			String extend = jsonObject.toString();
			solrDoc.setExtend(extend);
			solrDoc.setSummary(doc.getDigest());
			solrRepository.save(solrDoc);
			doc.setDataState(DATA_STATE_INDEXED);

			//yhh add 索引建立完成移除，文档建立失败表中的状态
			pdmDocInfoFailService.removePdmDocInfoFail(doc.getDocumentId());


			//yhh modify 调整到finally里中调用
			//pdmDocInfoRepository.save(doc);
			
		} catch (Exception e) {
			PdmDocInfoFail pdmDocInfoFail=new PdmDocInfoFail();
			pdmDocInfoFail.setDocId(doc.getDocumentId());
			pdmDocInfoFail.setPdmDocInfoId(doc.getId());
			pdmDocInfoFail.setDocFilePath(doc.getFilePath());
			pdmDocInfoFail.setDocUrl(doc.getUrl());
			pdmDocInfoFail.setDocTitle(doc.getDocumentTitle());
			//yhh add 可能出现异常有两种 solr建立索引失败的异常，其他认为是文档路径无效
			if( e instanceof UncategorizedSolrException){
				String errMsg=e.getLocalizedMessage();
				System.out.println(errMsg);
				if(errMsg.indexOf("maxWarmingSearchers=")<0) {
					doc.setDataState(DATA_STATE_INDEX_FAIL);
					pdmDocInfoFail.setFailState(DATA_STATE_INDEX_FAIL);
					pdmDocInfoFail.setFailReason("文件内容获取成功，Solr索引建立失败【"+e.getLocalizedMessage()+"】");
					if(errorInfo.indexOf("文档Solr索引建立失败")<0||errorInfo.length()==0)
						errorInfo.append("文档Solr索引建立失败" );
				}
				logger.info("文档"+doc.getDocumentId()+"文件获取成功，Solr发出警告【"+e.getLocalizedMessage()+"】");

			}else{
				logger.error("发生其他异常，极有可能是文档获取失败【"+e.getLocalizedMessage()+"】");
				pdmDocInfoFail.setFailState(DATA_STATE_INDEX_FAIL);
				pdmDocInfoFail.setFailReason("发生其他异常，极有可能是文档获取失败【"+e.getLocalizedMessage()+"】");
				doc.setDataState(IndexingService.DATA_STATE_INVALIDPATH);
//				errorInfo.append(","+doc.getDocumentTitle() );
				if(errorInfo.indexOf("文档内容获取失败")<0||errorInfo.length()==0)
					errorInfo.append("文档内容获取失败" );
			}
			//将失败的pdm文档信息记录到处理失败记录表中
			pdmDocInfoFailService.addPdmDocInfoFail(pdmDocInfoFail);

		} finally {
			doc.setCreateAt(new Date());
			pdmDocInfoRepository.save(doc);
			finishedTaskList.add("");
		}
	}
	
	private String getFileContent(String filePath) throws Exception {
		
        ContentHandler handler = null;
        InputStream input = null;
        
        try {
            
            Parser parser = new AutoDetectParser();
            Metadata meta = new Metadata();
            
            URL url = new URL(filePath);
    		HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();
            
            input = httpConnection.getInputStream();
            handler = new BodyContentHandler(-1);
            
            parser.parse(input, handler, meta, new ParseContext());
        } catch (Exception e) {
        	//yhh add
			logger.error("解析PDM系统文档内容时出现了异常【："+e.getMessage()+"】");
            e.printStackTrace();
            throw e;
        } finally {
            try {
            	if (input != null) {
            		input.close();
            	}
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        return handler.toString();
    }
}
