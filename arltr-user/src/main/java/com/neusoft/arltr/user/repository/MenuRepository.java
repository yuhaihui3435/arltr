package com.neusoft.arltr.user.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.neusoft.arltr.common.entity.user.Menu;
/**
 * 菜单数据仓库接口
 * 
 * @author cuilw
 *
 */
public interface MenuRepository extends PagingAndSortingRepository<Menu, Integer> {
	@Query(value = "select distinct m.* "
			+ "from menu m,  role_menu mr "
			+ "where  m.parent_id = ?1 and mr.role_id in (?2) and mr.menu_id = m.id order by m.disp_no" , nativeQuery = true)
	public List<Menu> findMenusOfRoles(Integer parentMenuId,Integer... roleIds);
	
	public List<Menu> findByParentId(Integer parentId);
}
