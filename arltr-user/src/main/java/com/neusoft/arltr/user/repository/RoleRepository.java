package com.neusoft.arltr.user.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.neusoft.arltr.common.entity.user.Role;

/**
 * 角色管理数据仓库接口
 * 
 * @author lishuang
 *
 */
public interface RoleRepository  extends CrudRepository<Role, Integer> ,JpaSpecificationExecutor<Role>{
	@Modifying
	@Query(value=" delete from user_role  where role_id=?1 ",nativeQuery=true)
	public void deleteByRoleId(Integer id);
	@Modifying
	@Query(value="insert into user_role  values(?1,?2) ",nativeQuery=true)
	public int insert(Integer userid,Integer id);

}
