package com.neusoft.arltr.user.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.neusoft.arltr.common.entity.user.SysAuth;
/**
 * 访问权限仓库
 * @author chifch
 *
 */
public interface SysAuthRepository extends CrudRepository<SysAuth, Integer> ,JpaSpecificationExecutor<SysAuth>{
/**
 * 删除该权限的全部人员
 * @param id 权限Id
 */
@Modifying
@Query(value=" delete from user_sys_auth  where sys_auth_id=?1 ",nativeQuery=true)
public void deleteBySysId(Integer id);
/**
 * 增加该权限的全部人员
 * @param userid 用户id
 * @param id 权限id
 * @return
 */
@Modifying
@Query(value="insert into user_sys_auth  values(?1,?2) ",nativeQuery=true)
public int insert(Integer userid,Integer id);
/**
 * 根据用户id获取该用户的权限
 * @param userid 用户id
 * @return
 */
@Query(value="select t.* from sys_auth t , user_sys_auth c where t.id=c.sys_auth_id and c.user_id=?1 ",nativeQuery=true)
public List<SysAuth> getAll(Integer userid);
}
