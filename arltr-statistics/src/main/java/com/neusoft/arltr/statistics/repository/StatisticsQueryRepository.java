package com.neusoft.arltr.statistics.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.neusoft.arltr.common.entity.statistics.UserQueryLogs;

/**
 * 用户查询记录数据仓库
 * @author wuxl
 *
 */
public interface StatisticsQueryRepository extends CrudRepository<UserQueryLogs, Integer>,JpaSpecificationExecutor<UserQueryLogs> {
	/**
	 * 查询统计中按角色和用户分组的记录数
	 * @param queryKeyWord
	 * @return
	 */
	@Query(value = "select * from (select t.*,rownum rn from (SELECT user_name,key_word,COUNT(*) search_num,user_id FROM  "
			  +" user_query_logs WHERE (user_id=?2 or 0=?2) AND (user_id IN "
			  +" (SELECT u.user_id FROM arltr_user.user_role u WHERE u.role_id=?1) or 0=?1) "
			  +" AND (user_id IN (SELECT u.id FROM arltr_user.user_t u WHERE u.employee_org=?3) or'-1'=?3) "
			  +" GROUP BY user_name,key_word,user_id ORDER BY user_name,key_word) t) "
			  +" WHERE RN BETWEEN ?4 AND ?5",nativeQuery = true)
	public List<Object> findAllViewGroupUser(int role,int user,String org,int start,int end);
	/**
	 * 查询统计中按角色和用户分组的总数
	 * @param queryKeyWord
	 * @return
	 */
	@Query(value = " SELECT COUNT(t.num) FROM (SELECT user_name,key_word,COUNT(*) num,user_id FROM "
			+" user_query_logs WHERE (user_id=?2 or 0=?2) AND (user_id IN "
			+" (SELECT u.user_id FROM arltr_user.user_role u WHERE u.role_id=?1) or 0=?1) "
			+" AND (user_id IN (SELECT u.id FROM arltr_user.user_t u WHERE u.employee_org=?3) or'-1'=?3) "
			+" GROUP BY user_name,key_word,user_id) t",nativeQuery = true)
	public int findAllViewGroupUserCount(int role,int user,String org);
	
	@Query(value = "select count(m.userId) as sarchNum, m.keyWord, m.keyWordPinyin, m.keyWordAbbre from UserQueryLogs m where m.userId = ?1 group by m.keyWord, m.keyWordPinyin, m.keyWordAbbre")
	public List<UserQueryLogs> findByUserId(Integer userId);

}
