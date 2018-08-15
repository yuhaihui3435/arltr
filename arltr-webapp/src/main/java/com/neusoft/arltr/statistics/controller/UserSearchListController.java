package com.neusoft.arltr.statistics.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.neusoft.arltr.common.base.ListPage;
import com.neusoft.arltr.common.base.RespBody;
import com.neusoft.arltr.common.entity.statistics.UserQueryLogs;
import com.neusoft.arltr.common.entity.statistics.UserViewLogs;
import com.neusoft.arltr.common.entity.user.Role;
import com.neusoft.arltr.common.entity.user.User;
import com.neusoft.arltr.common.service.StatisticsService;
import com.neusoft.arltr.common.service.SysService;

/**
 * 用户搜索统计控制器
 * 
 * @author lishuang
 *
 */
@Controller
public class UserSearchListController {

	@Autowired
	StatisticsService statisticsService;
	@Autowired
	SysService sysService;
	
	/**
	 * 页面加载
	 * @return
	 */
	@GetMapping("/statistics/userlist")
	public String  getUserlist(){
		return "statistics/usersearch_list";
	}
	
	/**
	* 条件查询（分页）
	* @param pageNumber 当前页数
	* @param pageSize 每页显示数据
	* @param queryParam 查询条件（实体类）
	* @return 分页数据
	*/
	@PostMapping("/statistics/usersearch")
	@ResponseBody
	public ListPage data(@RequestParam(value = "page", defaultValue = "1") Integer pageNumber,
			@RequestParam(value = "rows", defaultValue = "10") Integer pageSize,UserQueryLogs queryParam) {
		 RespBody<ListPage> data=statisticsService.userQuery(pageNumber,pageSize,queryParam);
		 return data.getBody();
	}
	
	/**
	 * 获取角色下拉列表
	 */
	@PostMapping("/statistics/usersearch/role")
	@ResponseBody
	public List<Role> getrolelist() {
		List<Role> roleList=sysService.getUserRoleList().getBody();
		return roleList;
	}
	/**
	 * 获取角色下拉列表
	 */
	@PostMapping("/statistics/usersearch/user")
	@ResponseBody
	public List<User> getroleUserlist(@RequestParam(value="id") Integer id) {
		List<User> userList=sysService.getRoleRelaUser(id).getBody();
		return userList;
	}
	
	/**
	* 条件查询（分页）
	* @param pageNumber 当前页数
	* @param pageSize 每页显示数据
	* @param queryParam 查询条件（实体类）
	* @return 分页数据
	*/
	@PostMapping("/statistics/userDocumentsearch")
	@ResponseBody
	public ListPage data(@RequestParam(value = "page", defaultValue = "1") Integer pageNumber,
			@RequestParam(value = "rows", defaultValue = "10") Integer pageSize,UserViewLogs queryParam) {
		 RespBody<ListPage> data=statisticsService.userDocumentQuery(pageNumber,pageSize,queryParam);
		 return data.getBody();
	}
}
