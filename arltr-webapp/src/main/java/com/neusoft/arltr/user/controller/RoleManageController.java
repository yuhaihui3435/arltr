package com.neusoft.arltr.user.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.neusoft.arltr.common.base.RespBody;
import com.neusoft.arltr.common.entity.user.Role;
import com.neusoft.arltr.common.entity.user.User;
import com.neusoft.arltr.common.service.SysService;
import com.neusoft.arltr.common.utils.TreeNode;

/**
 * 角色管理Controller
 * @author lishuang
 *
 */
@Controller
public class RoleManageController {
	@Autowired
	SysService roleService;
	
	
	/**
	 * 页面加载
	 * @return
	 */
	@GetMapping("/user/role/list")
	public String  getMenuDetail(){
		return "user/role_manage";
	}
	
	/**
	 * 角色树
	 * @return
	 */
	@PostMapping("/user/role/tree")
	@ResponseBody
	public List<TreeNode> getMenuTree(){
		return roleService.getUserRoleTree().getBody();
	}
	
	/**
	* 保存角色
	* 
	* @param entity 角色实体类
	* @return 返回信息
	*/
	@PostMapping("/user/role/save")
	@ResponseBody
	public RespBody<Role> save(@RequestBody Role entity, HttpServletRequest request){
		return roleService.save(entity);
	}
	
	/**
	* 删除角色
	* 
	* @param id 主键
	* @return 返回信息
	*/
	@PostMapping("/user/role/remove/{id}")
	@ResponseBody
	public RespBody<String> remove(@PathVariable("id") Integer id){
		return roleService.remove(id);
	}
	
	/**
	 * 人员组织机构树
	 * @return
	 */
	@PostMapping("/user/role/orgusertree")
	@ResponseBody
	public List<TreeNode> getorgUserTree(@RequestParam(value="roleId",defaultValue = "0") Integer roleId){
		return roleService.getOrgUserTree(roleId).getBody();
	}
	/**
	 * 组织机构树
	 * @return
	 */
	@PostMapping("/user/role/orgtree")
	@ResponseBody
	public List<TreeNode> getorgTree(){
		return roleService.getOrgTree().getBody();
	}
	/**
	 * 根据角色Id 获取拥有该角色的用户
	 * @param id 角色ID
	 * @return
	 */
	@PostMapping("/user/role/rolerelauser")
	@ResponseBody
	public RespBody<List<User>> getRoleRelaUser(@RequestParam(value="id") Integer id){
		return roleService.getRoleRelaUser(id);
	}
	/**
	 * 保存角色配置用户方法
	 * @param id 角色ID
	 * @param list 用户ID集合
	 * @return
	 */
	@PostMapping("/user/role/roleusersave")
	@ResponseBody
	public RespBody<Object> save(@RequestParam(value="id") Integer id,@RequestParam(value="useridlist") List<Integer> list){
		return roleService.roleSave(id, list);
	}
	

}
