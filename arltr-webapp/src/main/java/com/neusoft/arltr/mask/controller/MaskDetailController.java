package com.neusoft.arltr.mask.controller;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.neusoft.arltr.common.base.RespBody;
import com.neusoft.arltr.common.constant.SessionKey;
import com.neusoft.arltr.common.entity.mask.MaskWord;
import com.neusoft.arltr.common.entity.user.User;
import com.neusoft.arltr.common.service.MaskService;

/** 
* 类功能描述:
* @author 作者: 
* @version 创建时间：2017年6月26日 下午1:01:02 
*  
*/
@Controller
public class MaskDetailController {

	@Autowired
	MaskService maskService;
	
	
	/**
	* 保存屏蔽词
	* 
	* @param entity 屏蔽词实体类
	* @return 返回信息
	*/
	@PostMapping("/mask/save")
	@ResponseBody
	public RespBody<MaskWord> save(@RequestBody MaskWord entity){
//		if(entity.getId()==null||entity.getId().equals("")){
//			entity.setCreateAt(new Date());
//			entity.setCreateBy(1);
//			entity.setCreateByName("lvhaiyang");
//			entity.setUpdateAt(new Date());
//			entity.setUpdateBy(1);
//			entity.setUpdateByName("lvhaiyang");
//		}else{
//			entity.setUpdateAt(new Date());
//			entity.setUpdateBy(2);
//			entity.setUpdateByName("wangzhixiang");
//		}
		return maskService.save(entity);
	}
	
	/**
	* 添加屏蔽词
	* 
	* @param entity 屏蔽词实体类
	* @return 返回信息
	*/
	
	@GetMapping("/mask/add")
	public String add(Model model,HttpServletRequest request){
		User user = (User) request.getSession().getAttribute(SessionKey.USER);
		MaskWord mw = new MaskWord();
		Date curDate = new Date();
		mw.setCreateBy(user.getId());
		mw.setUpdateBy(user.getId());
		mw.setCreateByName(user.getEmployeeName());
		mw.setUpdateByName(user.getEmployeeName());
		mw.setCreateAt(curDate);
		mw.setUpdateAt(curDate);
		model.addAttribute("mask",mw);
		return "mask/mask_detail";
	}
	
	
	
}
