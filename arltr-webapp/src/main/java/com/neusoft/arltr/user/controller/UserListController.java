package com.neusoft.arltr.user.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.neusoft.arltr.common.base.ListPage;
import com.neusoft.arltr.common.base.RespBody;
import com.neusoft.arltr.common.entity.user.Enumeration;
import com.neusoft.arltr.common.entity.user.User;
import com.neusoft.arltr.common.service.SysService;

@Controller
public class UserListController {
	@Autowired
	private SysService sysService;
/**
 * 页面入口
 * @return
 */
@GetMapping("/user/list")
public String getpath(){
	return "user/user_list";
}
/**
 * 用户查询方法
 * @param pageNumber
 * @param pageSize
 * @param user
 * @return
 */
@PostMapping("/user/query")
@ResponseBody
public ListPage search(@RequestParam(value = "page", defaultValue = "1") Integer pageNumber,
			@RequestParam(value = "rows", defaultValue = "10") Integer pageSize,@RequestParam(value="order",defaultValue="desc") String order,User user){
	 RespBody<ListPage> pagelist=sysService.search(pageNumber, pageSize,order, user);
	 return pagelist.getBody();
}
/**
 * 职务下拉列表
 * @return
 */
@GetMapping("/user/enumeration")
@ResponseBody
public List<Enumeration> getEnumeration(){
	 List<Enumeration> enumeration=sysService.getListByType("TITLE").getBody();
	 return enumeration;
}
}
