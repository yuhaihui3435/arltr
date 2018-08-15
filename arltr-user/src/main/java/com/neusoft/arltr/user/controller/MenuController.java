package com.neusoft.arltr.user.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.neusoft.arltr.common.base.RespBody;
import com.neusoft.arltr.common.entity.user.Menu;
import com.neusoft.arltr.common.entity.user.Role;
import com.neusoft.arltr.user.repository.MenuRepository;
import com.neusoft.arltr.user.repository.RoleRepository;

/**
 * 角色管理控制器
 * 
 * @author lishuang
 *
 */
@RestController
@RequestMapping("/user")
public class MenuController {
	@Autowired
	MenuRepository menuRepository;
	@Autowired
	RoleRepository roleRepository;
	
	/**
	* 获取全部菜单
	* 
	* @param id 主键
	* @return 返回信息
	*/
	@PostMapping("/menus/{parentId}")
	public RespBody<List<Menu>> getMenus(@PathVariable("parentId") Integer parentId,@RequestBody Role role){
		RespBody<List<Menu>> resp=new RespBody<List<Menu>>();
		List<Menu> menus=menuRepository.findByParentId(parentId);
		getTree(menus,role);
		resp.setBody(menus);
        return resp;
	}
	
	private void getTree(List<Menu> menus,Role role){
		for (Menu menu1 : menus) {
			menu1.setSelected(false);
			if(menu1.getChildren() != null && menu1.getChildren().size() > 0){
				getTree(menu1.getChildren(),role);
				for (Menu menu : role.getMenus()) {
					if(menu1.getId()==menu.getId()){
						menu.setSelected(true);
						break;
					}
				}
			}else{
				for (Menu menu2 : role.getMenus()) {
					if(menu2.getId()==menu1.getId()){
						menu1.setSelected(true);
						break;
					}
				}
			}
		}
	} 
}
