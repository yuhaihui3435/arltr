package com.neusoft.arltr.mask.controller;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.neusoft.arltr.common.base.ListPage;
import com.neusoft.arltr.common.base.RespBody;
import com.neusoft.arltr.common.entity.mask.MaskWord;
import com.neusoft.arltr.common.service.MaskService;

/** 
* 类功能描述:
* @author 作者: 
* @version 创建时间：2017年6月26日 下午1:01:02 
*  
*/
@Controller
public class MaskListController {

	@Autowired
	MaskService maskService;
	/**
	* 查询屏蔽词
	* 
	* @param entity 屏蔽词实体类
	* @return 返回信息
	*/
	@PostMapping("/mask/query")
	@ResponseBody
	public ListPage query(@RequestParam(value = "page", defaultValue = "1") Integer pageNumber,
			@RequestParam(value="order",defaultValue="desc") String order,
			@RequestParam(value="sort",defaultValue="updateAt")String sort,
			@RequestParam(value = "rows", defaultValue = "10") Integer pageSize, 
			MaskWord condition){
		 RespBody<ListPage> query = maskService.query(pageNumber, order, sort, pageSize, condition);
		return query.getBody();
	}
	
	@GetMapping("/mask/list")
	public String list(Model model) {
		return "mask/mask_list";
	}
	
//	/**
//	* 获取所有屏蔽词
//	* 
//	* @return 所有屏蔽词
//	*/
//	@GetMapping("/mask/all")
//	@ResponseBody
//	public ListPage getAll(){
//		RespBody<List<MaskWord>> query = maskService.getAll();
//		return null;
//	}
//	
	/**
	* 根据id获取屏蔽词
	* 
	* @param id 主键
	* @return 屏蔽词实体类
	 * @throws ParseException 
	*/
	@GetMapping("/mask/{id}")
	public String getOne(@PathVariable("id") Integer id ,Model model) throws ParseException{
		MaskWord mw = maskService.getOne(id).getBody();
		Date curDate = new Date();
		mw.setUpdateAt(curDate);
//		SimpleDateFormat ss = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		Date c = mw.getCreateAt();
//		Date u = mw.getUpdateAt();
//		mw.setCreateAt(ss.format(c));
//		mw.setUpdateAt(ss.format(u));
		model.addAttribute("mask", mw);
		return "mask/mask_detail";
	}
	

	
	/**
	* 删除屏蔽词
	* 
	* @param id 主键
	* @return 返回信息
	*/
	@PostMapping("/mask/{id}/remove")
	@ResponseBody
	public RespBody<String> remove(@PathVariable("id") Integer id){
		
		return maskService.remove(id);
	}
	
	/**
	* 批量删除屏蔽词
	* 
	* @param entities 实体list
	* @return 返回信息
	*/
	@PostMapping("/mask/remove/multiple")
	@ResponseBody
	public RespBody<String> removeAll(@RequestBody List<MaskWord> entities){
		
		return maskService.removeAll(entities);
	}
	
	
}
