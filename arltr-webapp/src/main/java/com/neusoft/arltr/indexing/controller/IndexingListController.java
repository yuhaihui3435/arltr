package com.neusoft.arltr.indexing.controller;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.neusoft.arltr.common.entity.indexing.PdmDocInfoFail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.neusoft.arltr.common.base.ListPage;
import com.neusoft.arltr.common.base.RespBody;
import com.neusoft.arltr.common.constant.SessionKey;
import com.neusoft.arltr.common.entity.indexing.CronTask;
import com.neusoft.arltr.common.entity.indexing.DataImportLogs;
import com.neusoft.arltr.common.entity.user.Enumeration;
import com.neusoft.arltr.common.entity.user.User;
import com.neusoft.arltr.common.service.IndexingService;
import com.neusoft.arltr.common.service.SysService;
/**
 * 索引维护 Controller
 * @author wuxl
 *
 */
@Controller
public class IndexingListController {
	@Autowired
	IndexingService indexingService;
	
	@Autowired
	SysService sysService;
	
	/**
	 * 动态查询根据查询条件
	 * @return
	 */
	@PostMapping("/indexing/list/data")
	@ResponseBody
	public ListPage data(@RequestParam(value = "page", defaultValue = "1") Integer pageNumber,
			@RequestParam(value = "rows", defaultValue = "10") Integer pageSize, 
			@RequestParam(value="order",defaultValue="desc") String order,
			@RequestParam(value="sort",defaultValue="startTime") String sort,DataImportLogs queryParam) {
		 RespBody<ListPage> data= indexingService.query(pageNumber, pageSize, order, sort, queryParam);
		 ListPage resList = data.getBody();
	     List<Enumeration> importTypeList = sysService.getListByType("INDEX_IMPORT_TYPE").getBody();
		 List<Enumeration> taskTypeList = sysService.getListByType("INDEX_TASK_TYPE").getBody();
		 List<Enumeration> taskStateList = sysService.getListByType("INDEX_TASK_STATE").getBody();
		 //采集类型列表
		 resList.put("importTypeList", importTypeList);
		 //任务类型列表
		 resList.put("taskTypeList", taskTypeList);
		 //任务状态列表
		 resList.put("taskStateList", taskStateList);
		 return resList;
	}
	
	/**
	 * 列表页面入口方法
	 * @return
	 */
	@GetMapping("/indexing/list")
	public String list() {
		return "indexing/indexing_list";
	}
	
	/**
	 * 显示定时器弹出框
	 * @author ye.yy
	 * @param request
	 * @return RespBody<CronTask>
	 */
	@GetMapping("/indexing/timer")
	@ResponseBody
	public RespBody<CronTask> showTimerDetail(HttpServletRequest request){
		User user = (User)request.getSession().getAttribute(SessionKey.USER);
		Date curDate = new Date();
		RespBody<CronTask> resInforReturn = new RespBody<CronTask>();
		CronTask cronTask = null;
		RespBody<CronTask> resInfor = this.indexingService.getTask();
		if(resInfor.getBody() == null){  //表明新建定时任务
			cronTask = new CronTask();
			cronTask.setCreateBy(user.getId());
			cronTask.setCreateByName(user.getEmployeeName());
			cronTask.setCreateAt(curDate);
			cronTask.setUpdateBy(user.getId());
			cronTask.setUpdateByName(user.getEmployeeName());
			cronTask.setUpdateAt(curDate);
		}else{  //表明修改定时任务
			cronTask = this.indexingService.getTask().getBody();
			cronTask.setUpdateBy(user.getId());
			cronTask.setUpdateByName(user.getEmployeeName());
			cronTask.setUpdateAt(curDate);
		}
		resInforReturn.setBody(cronTask);
		return resInforReturn;
	}
	
	/**
	 * 保存定时任务
	 * @author ye.yy
	 * @param cronTask 定时任务信息
	 * @return RespBody<CronTask>
	 */
	@PostMapping("/indexing/timer/save")
	@ResponseBody
	public RespBody<CronTask> saveCronTask(@RequestBody CronTask cronTask){
		return this.indexingService.saveCronTask(cronTask);
	}

	@PostMapping("/indexing/timer/saveFullAmount")
	@ResponseBody
	public RespBody<String> saveFullAmountCronTask(@RequestBody Map<String,String> map){
		RespBody respBody=this.indexingService.saveFullAmountCronTask(map.get("sDate"),map.get("eDate"),map.get("taskDate"),map.get("taskTime"));
		return respBody;
	}

	
	/**
	 * 加载采集类型下拉列表
	 * @return RespBody<List<Enumeration>>
	 */
	@GetMapping("/indexing/importtype/list")
	@ResponseBody
	public RespBody<List<Enumeration>> getImportTypeList(){
		return this.sysService.getListByType("INDEX_IMPORT_TYPE");
	}
	
	/**
	 * 手动更新确定采集类型
	 * @param importType 采集类型
	 * @param request
	 * @return RespBody<DataImportLogs>
	 */
	@PostMapping("/indexing/manualupdate/confirm")
	@ResponseBody
	public RespBody<DataImportLogs> confirmManualType(@RequestBody String importType,HttpServletRequest request){
		User user = (User)request.getSession().getAttribute(SessionKey.USER);
		return this.indexingService.confirmManualType(importType, user);
	}

	@PostMapping("/indexing/pdm/fail/query")
	@ResponseBody
	public ListPage pdmFailQuery(@RequestParam(value = "page", defaultValue = "1") Integer pageNumber,
												 @RequestParam(value = "rows", defaultValue = "10") Integer pageSize,
												 @RequestParam(value="order",defaultValue="desc") String order,
												 @RequestParam(value="sort",defaultValue="uAt") String sort,PdmDocInfoFail queryParam){
		RespBody<ListPage> data= indexingService.pdmFailQuery(pageNumber, pageSize, order, sort, queryParam);
		ListPage resList = data.getBody();
		return resList;
	}


}
