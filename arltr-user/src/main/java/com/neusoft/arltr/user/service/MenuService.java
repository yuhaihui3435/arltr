package com.neusoft.arltr.user.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import com.neusoft.arltr.common.constant.CacheKey;
import com.neusoft.arltr.common.entity.user.Menu;
import com.neusoft.arltr.common.entity.user.Role;
import com.neusoft.arltr.common.entity.user.User;
import com.neusoft.arltr.user.repository.MenuRepository;

@Service
@Primary
public class MenuService {
	@Autowired
	private MenuRepository menuRepository;
	 @Cacheable(value = CacheKey.USER_MENU, key = "#user.toString()")
	public List<Menu> getMenusOfUser(User user) {
		// 没有配置角色的用户返回空列表
		if (user.getRoles() == null || user.getRoles().size() == 0) {
			return new ArrayList<Menu>();
		}
		// 角色id查询条件
		Integer[] roleIds = new Integer[user.getRoles().size()];
		int idx = 0;
		for (Role role : user.getRoles()) {
			roleIds[idx] = role.getId();
			idx++;
		}

		return getMenusOfRole(roleIds);
	}

	public List<Menu> getMenusOfRole(Integer... roleIds) {

		// 获取导航菜单列表
		List<Menu> navigators = menuRepository.findMenusOfRoles(0,roleIds);

		for (Menu navigator : navigators) {

			// 获取根节点菜单
			List<Menu> menus = menuRepository.findMenusOfRoles(navigator.getId(), roleIds);

			for (Menu menu : menus) {

				// 获取叶子节点菜单
				List<Menu> items = menuRepository.findMenusOfRoles(menu.getId(), roleIds);

				if (items != null) {
					items.forEach(item -> {
						//item.setRoles(null);
						item.setChildren(null);
					});

					menu.setChildren(items);
				}

				//menu.setRoles(null);
			}

			navigator.setChildren(menus);
			//navigator.setRoles(null);
		}

		return navigators;
	}
	
	public List<Menu> getMenuList(){
		 return (List<Menu>) menuRepository.findAll();
	}
	public Menu getMenu(Integer id) {
		
		return menuRepository.findOne(id);
	}
}
