package com.neusoft.arltr.user.controller;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.neusoft.arltr.common.base.RespBody;
import com.neusoft.arltr.common.entity.user.Enumeration;
import com.neusoft.arltr.common.entity.user.User;
import com.neusoft.arltr.common.service.SysService;

/**
 * 用户管理详情Controller
 * @author pei.j
 *
 */
@Controller
public class UserManageDetailController {
	@Autowired
	SysService sysService;
	
	/**
	 * 用户详情
	 * @param id 用户id
	 * @return
	 */
	@GetMapping("/user/{id}")
	public String  getUserDetail(@PathVariable("id") Integer id,Model model){
		List<Enumeration> enumeration=sysService.getListByType("SECURITY").getBody();
		Collections.reverse(enumeration);
		User user=sysService.getUser(id).getBody();
		model.addAttribute("user", user);
		model.addAttribute("enumeration", enumeration);
		return "user/user_detail";
	}
	
	/**
	* 保存用户信息
	* 
	* @param user 用户实体类
	* @return 返回信息
	*/
	@PostMapping("/user/save")
	@ResponseBody
	public RespBody<User> save(@RequestBody User user){
		User entity=sysService.getUser(user.getId()).getBody();
		entity.setSecurityClass(user.getSecurityClass());
		return sysService.saveUser(entity);
	}

	@GetMapping("/user/securityList")
	@ResponseBody
	public RespBody<List<Enumeration>>  getSecurityList(){
		List<Enumeration> enumeration=sysService.getListByType("SECURITY").getBody();
		RespBody respBody=new RespBody();
		respBody.setBody(enumeration);
		return respBody;
	}

}
