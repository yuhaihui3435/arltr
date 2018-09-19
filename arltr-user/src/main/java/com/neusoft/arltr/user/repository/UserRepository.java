/**
 * 
 */
package com.neusoft.arltr.user.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.neusoft.arltr.common.entity.user.User;

/**
 * 用户表数据仓库接口
 * 
 *
 *
 */
public interface UserRepository extends CrudRepository<User, Integer>,JpaSpecificationExecutor<User> {
	@Query(value="select * from user_t  where (id in (select user_id from user_role where role_id=?2) or 0=?2) and employee_org = ?1 and (end_flag <> '1' or end_flag is null)",nativeQuery=true)
	public List<User> findByOrgCode(String orgCode,Integer roleId);
	@Query(value="select t.* from user_t t,user_sys_auth s where t.id=s.user_id and s.sys_auth_id=?1 and (t.end_flag <> '1' or t.end_flag is null)",nativeQuery=true)
	public List<User> getUserSysAuth(Integer sysId);
	@Query(value="select t.* from user_t t,user_role s where t.id=s.user_id and s.role_id=?1 and (t.end_flag <> '1' or t.end_flag is null)",nativeQuery=true)
	public List<User> getRoleRelaUser(Integer roleId);
	public User findByUserNameOrEmployeeNo(String userName,String employeeNo);

}
