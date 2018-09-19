package com.neusoft.arltr.indexing.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.neusoft.arltr.common.entity.indexing.PdmDocInfoFail;
import com.neusoft.arltr.indexing.repository.PdmDocInfoFailRepository;
import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.neusoft.arltr.common.base.ListPage;
import com.neusoft.arltr.common.base.RespBody;
import com.neusoft.arltr.common.entity.indexing.CronTask;
import com.neusoft.arltr.common.entity.indexing.DataImportLogs;
import com.neusoft.arltr.common.entity.search.Result;
import com.neusoft.arltr.common.entity.user.User;
import com.neusoft.arltr.common.exception.BusinessException;
import com.neusoft.arltr.common.model.indexing.ScheduleJob;
import com.neusoft.arltr.indexing.repository.IndexingRepository;
import com.neusoft.arltr.indexing.repository.SolrRepository;
import com.neusoft.arltr.indexing.service.CronTaskService;
import com.neusoft.arltr.indexing.service.IndexingService;
import com.neusoft.arltr.indexing.service.QuartzConfigService;
/**
 * 索引维护控制类
 * @author wuxl
 *
 */
@RestController
@RequestMapping("/indexing")
public class IndexingController {

	private static final Logger logger = Logger.getLogger(IndexingController.class.getName());
	
	/**任务类型——手动*/
	private static final short INDEX_TASK_TYPE_MANUAL = 2;
	
	@Autowired
	IndexingRepository indexingRepository;
	
	@Autowired
	CronTaskService cronTaskService;
	
	@Autowired
	QuartzConfigService quartzConfigService;
	
	@Autowired
	IndexingService indexingService;
	
	@Autowired
	SolrRepository solrRepository;

	@Autowired
	PdmDocInfoFailRepository pdmDocInfoFailRepository;
	
	/**
	* 条件查询（分页）
	* 
	* @param pageNumber 当前页数
	* @param pageSize 每页显示数据
	* @param order 升降排序
	* @param sort  排序字段
	* @param condition 查询条件（实体类）
	* @return 分页数据
	*/
	@PostMapping("/query")
	public RespBody<ListPage> query(@RequestParam(value = "page", defaultValue = "1") Integer pageNumber, @RequestParam(value = "rows", defaultValue = "10") Integer pageSize,@RequestParam(value="order",defaultValue="desc") String order,@RequestParam(value="sort",defaultValue="updateAt") String sort, @RequestBody DataImportLogs condition){
		
		Order viewOrder;
		if("asc".equals(order)){
			viewOrder=new Order(Sort.Direction.ASC,sort);
		}else{
			viewOrder=new Order(Sort.Direction.DESC,sort);
		}
		
		Sort sorts=new Sort(viewOrder, new Sort.Order(Sort.Direction.DESC, sort));
		Pageable pageable =new PageRequest(pageNumber-1,pageSize,sorts);
		
		Specification<DataImportLogs> specification = new Specification<DataImportLogs>() {
			@Override
			public Predicate toPredicate(Root<DataImportLogs> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				//采集类型
				Path<Short> importType = root.get("importType");
				//任务类型
				Path<Short> taskType = root.get("taskType");
				//任务状态
				Path<Short> taskState = root.get("taskState");
				//开始时间
				Path<Date> startTime = root.get("startTime");
				
				List<Predicate> list = new ArrayList<Predicate>();
				//采集类型
				if (condition.getImportType() != 0 ) {
					list.add(cb.equal(importType, condition.getImportType()));
				}
				//任务类型
				if (condition.getTaskType() != 0 ) {
					list.add(cb.equal(taskType, condition.getTaskType()));
				}
				//任务状态
				if (condition.getTaskState() != 0 ) {
					list.add(cb.equal(taskState, condition.getTaskState()));
				}
				//开始时间
				Calendar cal2 = Calendar.getInstance(); 
				if(condition.getStartTime()!=null){
					cal2.setTime(condition.getStartTime());
				}

				if (condition.getStartTime() != null && cal2.get(Calendar.YEAR)!=1900) {
					list.add(cb.greaterThanOrEqualTo(startTime, condition.getStartTime()));
				}
				Predicate[] p = new Predicate[list.size()];
				return cb.and(list.toArray(p));		
			}
		};

		RespBody<ListPage> resbody = new RespBody<ListPage>();
		Page<DataImportLogs>  respage = indexingRepository.findAll(specification,pageable);
		ListPage plist = new ListPage(respage);
		resbody.setBody(plist);
		
		return resbody;
	}
	/**
	* 根据id获取详细信息
	* 
	* @param id 主键
	* @return 索引维护实体类
	*/
	@GetMapping("/{id}")
	public RespBody<DataImportLogs> getOne(@PathVariable("id") Integer id){
		RespBody<DataImportLogs> resInfor = new RespBody<DataImportLogs>();
		DataImportLogs infor = indexingRepository.findOne(id);
		if(infor == null){
			throw new BusinessException("索引维护（" + id + "）不存在！");
		}
		resInfor.setBody(infor);
		return resInfor;
	}
	
	/**
	 * 查询定时任务
	 * @author ye.yy
	 * @return RespBody<CronTask> 定时任务实体类
	 */
	@GetMapping("/timer")
	public RespBody<CronTask> getTask(){
		RespBody<CronTask> resInfor = new RespBody<CronTask>();
		CronTask cronTask = this.cronTaskService.getCronTask();
		if(cronTask != null){
			resInfor.setBody(cronTask);
		}
		return resInfor;
	}
	
	/**
	 * 保存定时任务
	 * @author ye.yy
	 * @param cronTask 定时任务信息
	 * @return RespBody<CronTask> 定时任务实体类
	 */
	@PostMapping("/timer/save")
	public RespBody<CronTask> saveCronTask(@RequestBody CronTask cronTask){
		RespBody<CronTask> resInfor = new RespBody<CronTask>();
		CronTask cronTaskNew = this.cronTaskService.saveCronTask(cronTask);
		resInfor.setBody(cronTaskNew);
		short importType = cronTaskNew.getImportType();
		String jobName = importType == 1 ? "索引全量更新" : "索引增量更新";
		//设置任务属性
		ScheduleJob scheduleJob = new ScheduleJob();
		scheduleJob.setJobName(jobName);
		scheduleJob.setJobGroup(String.valueOf(importType));
		scheduleJob.setCronExpression(this.quartzConfigService.configCronExpression(cronTaskNew));
		//启动调度
		this.quartzConfigService.startQuartz(scheduleJob);
		return resInfor;
	}
	
	/**
	 * 手动更新确定采集类型
	 * @param importType 采集类型
	 * @param user 执行者
	 * @return RespBody<Object>
	 */
	@PostMapping("/manualupdate/confirm")
	public RespBody<DataImportLogs> confirmManualType(@RequestParam("importType") String importType,@RequestBody User user){
		RespBody<DataImportLogs> resInfor = new RespBody<DataImportLogs>();
		DataImportLogs dataImportLogs = this.indexingService.insertData(INDEX_TASK_TYPE_MANUAL, Short.parseShort(importType), null, user);
		resInfor.setBody(dataImportLogs);
		return resInfor;
	}
	
	/**
	 * 增加文档评分
	 * @param id 文档id
	 * 
	 * @return RespBody<String>
	 */
	@PostMapping("/doc/score/plus")
	public RespBody<String> plusDocScore(String id) {
		
		Result doc = solrRepository.findOne(id);
		
		doc.setScore(doc.getScore() + 1);
		
		solrRepository.save(doc);
		
		return new RespBody<String>();
	}

	/**
	 *
	 * @return
	 */
	@PostMapping("/timer/fullAmountSave")
	@ResponseBody
	public RespBody<String> saveFullAmountTask(@RequestParam("sDate") String sDate,@RequestParam("eDate") String eDate,@RequestParam("taskDate") String taskDate,@RequestParam("taskTime") String taskTime){
		ScheduleJob scheduleJob = new ScheduleJob();
		scheduleJob.setJobName("索引全量更新");
		scheduleJob.setJobGroup(String.valueOf(1));
		String[] taskTimes=taskTime.split(":");
		String[] taskDates=taskDate.split("-");
		String cronStr="00 "+taskTimes[1]+" "+taskTimes[0]+" "+taskDates[2]+" "+taskDates[1]+" ? "+taskDates[0];
		logger.info("索引全量更新计划任务时间格式为："+cronStr);
		scheduleJob.setCronExpression(cronStr);
		scheduleJob.setDesc(sDate+"|"+eDate);//设置任务内容
		//启动调度
		this.quartzConfigService.startQuartz(scheduleJob);
		RespBody respBody=new RespBody();
		respBody.setMsg("全量更新计划任务设置成功");
		return respBody;
	}


	/**
	 * pdm文档同步失败记录查询
	 * @param pageNumber
	 * @param pageSize
	 * @param order
	 * @param sort
	 * @param condition uAt 更新时间，failState 失败状态，docId 文档id，docTitle 文档标题
	 * @return
	 */
	@PostMapping("/pdm/fail/query")
	public RespBody<ListPage> pmdFailQuery(@RequestParam(value = "page", defaultValue = "1") Integer pageNumber, @RequestParam(value = "rows", defaultValue = "10") Integer pageSize,@RequestParam(value="order",defaultValue="desc") String order,@RequestParam(value="sort",defaultValue="uAt") String sort, @RequestBody PdmDocInfoFail condition) {

		Order viewOrder;
		if("asc".equals(order)){
			viewOrder=new Order(Sort.Direction.ASC,sort);
		}else{
			viewOrder=new Order(Sort.Direction.DESC,sort);
		}

		Sort sorts=new Sort(viewOrder, new Sort.Order(Sort.Direction.DESC, sort));
		Pageable pageable =new PageRequest(pageNumber-1,pageSize,sorts);

		Specification<PdmDocInfoFail> specification = new Specification<PdmDocInfoFail>() {
			@Override
			public Predicate toPredicate(Root<PdmDocInfoFail> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Path<Integer> failState=root.get("failState");
				Path<String> docId=root.get("docId");
				Path<String> docTitle=root.get("docTitle");
				Path<Date> uAt=root.get("uAt");

				List<Predicate> list = new ArrayList<Predicate>();

				if (condition.getFailState()!=null&& condition.getFailState()!= 0 ) {
					list.add(cb.equal(failState, condition.getFailState()));
				}
				//时间查询
				Calendar cal2 = Calendar.getInstance();
				if(condition.getStartTime()!=null){
					cal2.setTime(condition.getStartTime());
				}
				System.out.println(cal2.get(Calendar.YEAR));
				if (condition.getStartTime() != null && cal2.get(Calendar.YEAR)!=1900) {
					list.add(cb.greaterThanOrEqualTo(uAt, condition.getStartTime()));
				}
				if(condition.getEndTime()!=null){
					cal2.setTime(condition.getEndTime());
				}
				if (condition.getEndTime() != null && cal2.get(Calendar.YEAR)!=1900) {
					list.add(cb.lessThanOrEqualTo(uAt, condition.getEndTime()));
				}

				if(StringUtils.isNotBlank(condition.getDocId())){
					list.add(cb.equal(docId,condition.getDocId()));
				}

				if(StringUtils.isNotBlank(condition.getDocTitle())){
					list.add(cb.equal(docTitle,condition.getDocTitle()));
				}

				Predicate[] p = new Predicate[list.size()];
				return cb.and(list.toArray(p));

			}
		};
		RespBody<ListPage> resbody = new RespBody<ListPage>();
		Page<PdmDocInfoFail>  respage = pdmDocInfoFailRepository.findAll(specification,pageable);
		ListPage plist = new ListPage(respage);
		resbody.setBody(plist);
		return resbody;
	}
	
	@GetMapping("/wtest")
	@ResponseBody
	public RespBody<Object> wsTest() throws Exception {
		
		String xml = "<?xml version=\"1.0\" encoding=\"utf-8\" ?>	" +
				" <Root>	" +
				"  <Projects>	" +
				"    <Project OptType=\"1\">	" +
				"      <ID IsMust=\"true\" Type=\"String\">4d6c24a1-3211-4637-ae59-e472ffdd2289</ID>	" +
				"      <NAME IsMust=\"true\" Type=\"String\">20170731-1</NAME>	" +
				"      <CODE IsMust=\"true\" Type=\"String\">20170731-1</CODE>	" +
				"      <CREATEDATE IsMust=\"false\" Type=\"DateTime\">2017/7/31 0:00:00</CREATEDATE>	" +
				"      <MANAGER IsMust=\"true\" Type=\"String\">US0000000000000</MANAGER>	" +
				"      <MODELID IsMust=\"true\" Type=\"String\">98794e28-5aec-4714-9bab-e83131f50f86</MODELID>	" +
				"      <STAGE IsMust=\"true\" Type=\"String\">5B189A99E7BF4F448EC2B230E830D312</STAGE>	" +
				"      <TESTCATEGORY IsMust=\"false\" Type=\"String\">TCD20160700002</TESTCATEGORY>	" +
				"      <MODELPRODUCT IsMust=\"false\" Type=\"String\">a2a7e7b7-3c1e-4c07-b7aa-114d27b60fb5</MODELPRODUCT>	" +
				"      <PRODUCT1 IsMust=\"false\" Type=\"String\">1f98e774-a2a4-4cd0-8e3a-6e9bbfc7e0a6</PRODUCT1>	" +
				"      <PRODUCT2 IsMust=\"false\" Type=\"String\">a2a7e7b7-3c1e-4c07-b7aa-114d27b60fb5</PRODUCT2>	" +
				"      <PRODUCT3 IsMust=\"false\" Type=\"String\"></PRODUCT3>	" +
				"      <PRODUCT4 IsMust=\"false\" Type=\"String\"></PRODUCT4>	" +
				"      <PRODUCT5 IsMust=\"false\" Type=\"String\"></PRODUCT5>	" +
				"      <PRODUCT6 IsMust=\"false\" Type=\"String\"></PRODUCT6>	" +
				"      <PRODUCT7 IsMust=\"false\" Type=\"String\"></PRODUCT7>	" +
				"      <PRODUCT8 IsMust=\"false\" Type=\"String\"></PRODUCT8>	" +
				"      <PRODUCT9 IsMust=\"false\" Type=\"String\"></PRODUCT9>	" +
				"      <PRODUCT10 IsMust=\"false\" Type=\"String\"></PRODUCT10>	" +
				"      <OTHERMANAGER IsMust=\"false\" Type=\"String\"></OTHERMANAGER>	" +
				"      <TESTAPARTMENT IsMust=\"false\" Type=\"String\">AOR201609260000001</TESTAPARTMENT>	" +
				"      <TESTPLACE IsMust=\"false\" Type=\"String\">20170731-1</TESTPLACE>	" +
				"      <TASKSOURCE IsMust=\"false\" Type=\"String\">20170731-1</TASKSOURCE>	" +
				"      <TESTBASIS IsMust=\"false\" Type=\"String\">20170731-1</TESTBASIS>	" +
				"      <DATASHORTNAME IsMust=\"false\" Type=\"String\">20170731-1</DATASHORTNAME>	" +
				"      <DATAANALYSISRESUME IsMust=\"false\" Type=\"String\">20170731-1</DATAANALYSISRESUME>	" +
				"      <TECHNIQUEQUESTION IsMust=\"false\" Type=\"String\">20170731-1</TECHNIQUEQUESTION>	" +
				"      <TESTCONCLUSION IsMust=\"false\" Type=\"String\">圆满成功</TESTCONCLUSION>	" +
				"      <CONCLUSIONEXPLAIN IsMust=\"false\" Type=\"String\">20170731-1</CONCLUSIONEXPLAIN>	" +
				"      <INTENTION IsMust=\"false\" Type=\"String\">20170731-1</INTENTION>	" +
				"      <CREATOR IsMust=\"false\" Type=\"String\">US0000000000000</CREATOR>	" +
				"      <URL>http://192.168.0.138:1101/Projects/Detection/ProjectManage/Html/ProjectManage.htm?ProjectId=4d6c24a1-3211-4637-ae59-e472ffdd2289&amp;objType=project&amp;CanEdit=null&amp;UserType=1&amp;ProjectState=null&amp;RD=1&amp;MenuType=Full&amp;FormId=TVW20160705000000001,TVW20160822000000001,TVW20160817000000001&amp;NodeType=project</URL>	" +
				"    </Project>	" +
				"    <Project OptType=\"1\">	" +
				"      <ID IsMust=\"true\" Type=\"String\">APM201706260005</ID>	" +
				"      <NAME IsMust=\"true\" Type=\"String\">T20170626-3</NAME>	" +
				"      <CODE IsMust=\"true\" Type=\"String\">T20170626-3</CODE>	" +
				"      <CREATEDATE IsMust=\"false\" Type=\"DateTime\">2017/6/26 0:00:00</CREATEDATE>	" +
				"      <MANAGER IsMust=\"true\" Type=\"String\">US0000000000000</MANAGER>	" +
				"      <MODELID IsMust=\"true\" Type=\"String\">6e818deb-2c8d-438d-a3b1-cca5a30b124c</MODELID>	" +
				"      <STAGE IsMust=\"true\" Type=\"String\">95225A22F0314F2BBE490A4C0D6C4E90</STAGE>	" +
				"      <TESTCATEGORY IsMust=\"false\" Type=\"String\">TCD20160700002</TESTCATEGORY>	" +
				"      <MODELPRODUCT IsMust=\"false\" Type=\"String\"></MODELPRODUCT>	" +
				"      <PRODUCT1 IsMust=\"false\" Type=\"String\"></PRODUCT1>	" +
				"      <PRODUCT2 IsMust=\"false\" Type=\"String\"></PRODUCT2>	" +
				"      <PRODUCT3 IsMust=\"false\" Type=\"String\"></PRODUCT3>	" +
				"      <PRODUCT4 IsMust=\"false\" Type=\"String\"></PRODUCT4>	" +
				"      <PRODUCT5 IsMust=\"false\" Type=\"String\"></PRODUCT5>	" +
				"      <PRODUCT6 IsMust=\"false\" Type=\"String\"></PRODUCT6>	" +
				"      <PRODUCT7 IsMust=\"false\" Type=\"String\"></PRODUCT7>	" +
				"      <PRODUCT8 IsMust=\"false\" Type=\"String\"></PRODUCT8>	" +
				"      <PRODUCT9 IsMust=\"false\" Type=\"String\"></PRODUCT9>	" +
				"      <PRODUCT10 IsMust=\"false\" Type=\"String\"></PRODUCT10>	" +
				"      <OTHERMANAGER IsMust=\"false\" Type=\"String\">T20170626-3</OTHERMANAGER>	" +
				"      <TESTAPARTMENT IsMust=\"false\" Type=\"String\">AOR201609260000001</TESTAPARTMENT>	" +
				"      <TESTPLACE IsMust=\"false\" Type=\"String\">T20170626-3</TESTPLACE>	" +
				"      <TASKSOURCE IsMust=\"false\" Type=\"String\">T20170626-3</TASKSOURCE>	" +
				"      <TESTBASIS IsMust=\"false\" Type=\"String\">T20170626-3</TESTBASIS>	" +
				"      <DATASHORTNAME IsMust=\"false\" Type=\"String\">T20170626-3</DATASHORTNAME>	" +
				"      <DATAANALYSISRESUME IsMust=\"false\" Type=\"String\"></DATAANALYSISRESUME>	" +
				"      <TECHNIQUEQUESTION IsMust=\"false\" Type=\"String\"></TECHNIQUEQUESTION>	" +
				"      <TESTCONCLUSION IsMust=\"false\" Type=\"String\">未完成</TESTCONCLUSION>	" +
				"      <CONCLUSIONEXPLAIN IsMust=\"false\" Type=\"String\"></CONCLUSIONEXPLAIN>	" +
				"      <INTENTION IsMust=\"false\" Type=\"String\"></INTENTION>	" +
				"      <CREATOR IsMust=\"false\" Type=\"String\">US0000000000000</CREATOR>	" +
				"      <URL>http://192.168.0.138:1101/Projects/Detection/ProjectManage/Html/ProjectManage.htm?ProjectId=APM201706260005&amp;objType=project&amp;CanEdit=null&amp;UserType=1&amp;ProjectState=null&amp;RD=1&amp;MenuType=Full&amp;FormId=TVW20160705000000001,TVW20160822000000001,TVW20160817000000001&amp;NodeType=project</URL>	" +
				"    </Project>	" +
				"  </Projects>	" +
				"</Root>	";

		JaxWsDynamicClientFactory dcf = JaxWsDynamicClientFactory.newInstance();
		Client client = dcf.createClient("http://localhost:9007/services/transfer/tdm?wsdl");
		Object[] res = client.invoke("tdmTransferAlternate", xml);
		
		RespBody<Object> resp = new RespBody<Object>();
		
		resp.setBody(res);
		
		return resp;
	}

}
