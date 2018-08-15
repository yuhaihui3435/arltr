/**
 * 
 */
package com.neusoft.arltr.user.controller;

import java.util.HashSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.neusoft.arltr.common.base.RespBody;
import com.neusoft.arltr.common.entity.user.Menu;
import com.neusoft.arltr.common.entity.user.Role;
import com.neusoft.arltr.common.service.SysService;
import com.neusoft.arltr.common.utils.TreeNode;
import com.neusoft.arltr.common.utils.TreeNodeBuilder;
import com.neusoft.arltr.common.utils.TreeNodeUtil;


/**
 * 角色授权控制器
 * 
 * @author pei.j
 *
 */
@Controller
@RequestMapping("/user/menu/authorization")
public class MenuAuthorizationListController {

	private static final String VIEW = "user/menu_authorization_list";
	
	@Autowired
	SysService sysService;
	
	/**
	 * 页面加载
	 */
	@GetMapping("")
	public String index(Model model) {
		
		return VIEW;
	}
	
	/**
	 * 获取角色列表
	 */
	@PostMapping("/role")
	@ResponseBody
	public List<Role> getMenuTree() {
		List<Role> roleList=sysService.getUserRoleList().getBody();
		return roleList;
	}
	
	/**
	 * 获取角色菜单树
	 * 
	 * @param id 角色id
	 */
	
	@GetMapping("/{id}/menus/tree")
	@ResponseBody
	public List<TreeNode> getRolesOfMenu(@PathVariable Integer id) {
		
		Role role= sysService.getMenusByRoleId(id).getBody();
		List<Menu> menus=sysService.findByParentId(0,role).getBody();
		
		return TreeNodeUtil.entityTreeToNodeTree(menus, true,new TreeNodeBuilder<Menu>() {
			
			@Override
			public TreeNode createTreeNode(Menu entity) {
				
				TreeNode node=new TreeNode();
		        node.setId(String.valueOf(entity.getId()));
		        node.setPid(String.valueOf(entity.getParentId()));
		        node.setText(entity.getName());
				node.setChecked(entity.isSelected());
					
		        return node;
			}
		}
	);
	}
	
	/**
	 * 保存角色菜单
	 * 
	 * @param id 角色id
	 * @param roles 角色菜单列表
	 */
	@PostMapping("/{id}/roles/save")
	@ResponseBody
	public RespBody<String> saveMenuRoles(@PathVariable Integer id, @RequestBody HashSet<Menu> menus) {
		
		sysService.saveRoleMenus(id, menus);
		
		return new RespBody<String>();
	}
	
}
