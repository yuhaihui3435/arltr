package com.neusoft.arltr.indexing.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.neusoft.arltr.common.entity.indexing.DataImportLogs;
import com.neusoft.arltr.common.entity.user.Enumeration;
import com.neusoft.arltr.common.service.IndexingService;
import com.neusoft.arltr.common.service.SysService;

/**
 * 索引维护详情Controller
 * @author ye.yy
 *
 */
@Controller
@RequestMapping("/indexing")
public class IndexingDetailController {
	
	@Autowired
	IndexingService indexingService;
	
	@Autowired
	SysService sysService;
	
	/**
	 * 根据主键，查看索引维护详情
	 * @param id 主键
	 * @param model
	 * @return 索引维护详情页面
	 */
	@GetMapping("/{id}")
	public String getIndexingDetail(@PathVariable Integer id,Model model){
		DataImportLogs dataImportLogs = this.indexingService.getOne(id).getBody();
		List<Enumeration> taskTypeList = this.sysService.getListByType("INDEX_TASK_TYPE").getBody();
		List<Enumeration> importTypeList = this.sysService.getListByType("INDEX_IMPORT_TYPE").getBody();
		List<Enumeration> taskStateList = this.sysService.getListByType("INDEX_TASK_STATE").getBody();
		model.addAttribute("dataImportLogs", dataImportLogs);
		model.addAttribute("taskTypeList", taskTypeList);
		model.addAttribute("importTypeList", importTypeList);
		model.addAttribute("taskStateList", taskStateList);
		return "indexing/indexing_detail";
	}
	
}
