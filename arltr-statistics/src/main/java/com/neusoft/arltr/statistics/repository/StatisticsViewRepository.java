package com.neusoft.arltr.statistics.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.neusoft.arltr.common.entity.statistics.UserViewLogs;

/**
 * 用户查看记录数据仓库
 * @author wuxl
 *
 */
public interface StatisticsViewRepository extends CrudRepository<UserViewLogs, Integer>,JpaSpecificationExecutor<UserViewLogs> {

	@Query(value = "SELECT * FROM ( "
			  +"SELECT t.*,ROWNUM RN FROM ( "
			  +" SELECT query_key_word,doc_title,COUNT(*) querycount "
			  +" FROM user_view_logs GROUP BY query_key_word , doc_title "
			  +" HAVING query_key_word  LIKE '%'||?1||'%' ) t "
			  +" ORDER BY t.query_key_word,t.querycount ) WHERE RN BETWEEN ?2 AND ?3 " ,nativeQuery = true) 
	public List<Object> findAllViewGroupWord(String queryKeyWord, int start,int end);
	/**
	 * 查询统计中按关键字分组的记录数
	 * @param queryKeyWord
	 * @return
	 */
	@Query(value = " SELECT COUNT(t.query_key_word) FROM ( "
			+" SELECT query_key_word,doc_title,COUNT(*) querycount"
			+" FROM user_view_logs GROUP BY query_key_word , doc_title "
			+" HAVING query_key_word  LIKE '%'||?1||'%' "
			+ " ) t ",nativeQuery = true) 
	public int findAllViewGroupWordCount(String queryKeyWord);
	/**
	 * 查询统计中按用户和关键字分组的记录数
	 * @param queryKeyWord
	 * @return
	 */
	@Query(value = "SELECT * FROM (SELECT T.*,ROWNUM RN FROM(  "
			  +"SELECT doc_title,doc_location,COUNT(*) num FROM user_view_logs "
			  +" WHERE (user_id=?1 or 0=?1) AND (query_key_word=?2 or 'nodata'=?2 )"
			  +"  GROUP BY doc_title,doc_location order by doc_title,doc_location "
			  +" )t )  WHERE RN BETWEEN ?3 AND ?4 ",nativeQuery = true)
	public List<Object> findAllViewUserDocument(int userId,String word,int start,int end);
	/**
	 * 查询统计中按用户和关键字分组的总数
	 * @param queryKeyWord
	 * @return
	 */
	@Query(value = "SELECT COUNT(t.num) FROM  "
			  +" (SELECT doc_title,doc_location,COUNT(*) num FROM user_view_logs "
			  +"  WHERE (user_id=?1 or 0=?1) AND (query_key_word=?2 or 'nodata'=?2 ) "
			  +" GROUP BY doc_title,doc_location) t",nativeQuery = true)
	public int findAllViewUserDocumentCount(int userId,String word);
	/**
	 * 根据用户id获取用户查询历史数据列表
	 * @param userId
	 * @return
	 */
	@Query(value = "select any_value(id) id,key_word,any_value(query_at) query_at,any_value(user_id) user_id,any_value(user_name) user_name"
			+ " from user_query_logs t "
			+ "where user_id=?1 group by key_word order by query_at desc ",nativeQuery = true)
	public List<Object[]> getListHistoryOfUser(int userId);
	/**
	 * 获取热度搜索词的前五条
	 * @return
	 */
	@Query(value = "SELECT  KEY_WORD, NUM FROM(SELECT KEY_WORD, NUM, ROW_NUMBER() OVER(ORDER BY NUM DESC) RN"
			+ " FROM (SELECT KEY_WORD, COUNT(1) NUM FROM USER_QUERY_LOGS GROUP BY KEY_WORD  ORDER BY NUM DESC)) WHERE rn BETWEEN 1 AND 5 ",nativeQuery = true)
	public List<Object[]> getHotListTopFive();
}
