package com.neusoft.arltr.statistics.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.neusoft.arltr.common.entity.statistics.UserQueryLogs;

/**
 * 数据热度数据仓库
 * @author pei.j
 *
 */
public interface DataHotRepository extends CrudRepository<UserQueryLogs, Integer>,JpaSpecificationExecutor<UserQueryLogs> {
	
	/**
	 *  按7天查询数据热度
	 * @return
	 */
	@Query(value = "select * from (select TRIM(key_word),count(*) num from user_query_logs "
			  +" WHERE to_char(trunc(sysdate - 7),'YYYY-MM-DD') <= to_char(query_at, 'YYYY-MM-DD') "
			  +" GROUP BY TRIM(key_word) order by num desc ) where rownum <= 10",nativeQuery = true)
	public List<Object[]> findDataHotByDays();
	
	/**
	 *  按1个月查询数据热度
	 * @return
	 */
	@Query(value = "select * from (select TRIM(key_word),count(*) num from user_query_logs "
			  +" WHERE to_char(add_months(sysdate,-1),'YYYY-MM-DD') <= to_char(query_at, 'YYYY-MM-DD') "
			  +" GROUP BY TRIM(key_word) order by num desc ) where rownum <= 10",nativeQuery = true)
	public List<Object[]> findDataHotByOneMonth();
	
	/**
	 *  按3个月查询数据热度
	 * @return
	 */
	@Query(value = "select * from (select TRIM(key_word),count(*) num from user_query_logs "
			  +" WHERE to_char(add_months(sysdate,-3),'YYYY-MM-DD') <= to_char(query_at, 'YYYY-MM-DD') "
			  +" GROUP BY TRIM(key_word) order by num desc) where rownum <= 10",nativeQuery = true)
	public List<Object[]> findDataHotByThreeMonths();
	
	/**
	 *  按6个月查询数据热度
	 * @return
	 */
	@Query(value = "select * from (select TRIM(key_word),count(*) num from user_query_logs "
			  +" WHERE to_char(add_months(sysdate,-6),'YYYY-MM-DD') <= to_char(query_at, 'YYYY-MM-DD') "
			  +" GROUP BY TRIM(key_word) order by num desc) where rownum <= 10 ",nativeQuery = true)
	public List<Object[]> findDataHotBySixMonths();
	
	/**
	 *  按7天查询(详细)数据热度
	 * @return
	 */
	@Query(value = "SELECT rq,nvl(uu.kw,'') keyword,nvl(uu.cc,0) num FROM( "
			+" SELECT to_char(sysdate,'MM-DD') rq FROM DUAL UNION "
			+" SELECT to_char(sysdate-1,'MM-DD') rq FROM DUAL UNION "
			+" SELECT to_char(sysdate-2,'MM-DD') rq FROM DUAL UNION "
			+" SELECT to_char(sysdate-3,'MM-DD') rq FROM DUAL UNION "
			+" SELECT to_char(sysdate-4,'MM-DD') rq FROM DUAL UNION "
			+" SELECT to_char(sysdate-5,'MM-DD') rq FROM DUAL UNION "
			+" SELECT to_char(sysdate-6,'MM-DD') rq FROM DUAL) date_table "
			+" LEFT JOIN (SELECT t.da,max(kw) kw,max(tt.cc) cc FROM( "
			+" SELECT da,max(cc) ma FROM( "
			+" SELECT to_char(query_at, 'MM-DD') da,trim(key_word),count(*) cc FROM "
			+" user_query_logs t WHERE to_char(trunc(sysdate - 7), 'MM-DD') < to_char(query_at, 'MM-DD') "
			+" GROUP BY trim(key_word),to_char(query_at, 'MM-DD')) a GROUP BY da) t, "
			+" (SELECT to_char(query_at, 'MM-DD') da,trim(key_word) kw,count(*) cc FROM user_query_logs t "
			+" WHERE to_char(trunc(sysdate - 7), 'MM-DD') < to_char(query_at, 'MM-DD') GROUP BY trim(key_word),"
			+" to_char(query_at, 'MM-DD') ) tt where t.da=tt.da and t.ma=tt.cc "
			+ "GROUP BY t.da) uu ON date_table.rq = uu.da ORDER BY rq",nativeQuery = true) 
	public List<Object[]> findDataHotByDaysDetail();
	
	/**
	 *  按1个月查询(详细)数据热度
	 * @return
	 */
	@Query(value = "SELECT rq,nvl(uu.kw,'') keyword,nvl(uu.cc,0) num FROM( "
			+" SELECT to_char(sysdate,'iw') rq FROM DUAL UNION "
			+" SELECT to_char(to_char(sysdate,'iw')-1) rq FROM DUAL UNION "
			+" SELECT to_char(to_char(sysdate,'iw')-2) rq FROM DUAL UNION "
			+" SELECT to_char(to_char(sysdate,'iw')-3) rq FROM DUAL "
			+" ) date_table "
			+" LEFT JOIN (SELECT t.da,max(kw) kw,max(tt.cc) cc FROM( "
			+" SELECT da,max(cc) ma FROM( "
			+" SELECT to_char(query_at, 'iw') da,trim(key_word),count(*) cc FROM "
			+" user_query_logs t WHERE to_char(sysdate - interval '30' day,'YYYY-MM-DD') < to_char(query_at, 'iw') "
			+" GROUP BY trim(key_word),to_char(query_at, 'iw')) a GROUP BY da) t, "
			+" (SELECT to_char(query_at, 'iw') da,trim(key_word) kw,count(*) cc FROM user_query_logs t "
			+" WHERE to_char(sysdate - interval '30' day,'YYYY-MM-DD') < to_char(query_at, 'iw') GROUP BY trim(key_word),"
			+" to_char(query_at, 'iw') ) tt where t.da=tt.da and t.ma=tt.cc "
			+ "GROUP BY t.da) uu ON date_table.rq = uu.da ORDER BY rq",nativeQuery = true) 
	public List<Object[]> findDataHotByOneMonthDetail();
	
	/**
	 *  按3个月查询(详细)数据热度
	 * @return
	 */
	@Query(value = "SELECT rq,nvl(uu.kw,'') keyword,nvl(uu.cc,0) num FROM( "
			+" SELECT to_char(add_months(sysdate,0),'YYYY-MM') rq FROM DUAL UNION "
			+" SELECT to_char(add_months(sysdate,-1),'YYYY-MM') rq FROM DUAL UNION "
			+" SELECT to_char(add_months(sysdate,-2),'YYYY-MM') rq FROM DUAL "
			+" ) date_table "
			+" LEFT JOIN (SELECT t.da,max(kw) kw,max(tt.cc) cc FROM( "
			+" SELECT da,max(cc) ma FROM( "
			+" SELECT to_char(query_at, 'YYYY-MM') da,trim(key_word),count(*) cc FROM "
			+" user_query_logs t WHERE to_char(add_months(sysdate,-3),'YYYY-MM') <= to_char(query_at, 'YYYY-MM') "
			+" GROUP BY trim(key_word),to_char(query_at, 'YYYY-MM')) a GROUP BY da) t, "
			+" (SELECT to_char(query_at, 'YYYY-MM') da,trim(key_word) kw,count(*) cc FROM user_query_logs t "
			+" WHERE to_char(add_months(sysdate,-3),'YYYY-MM') <= to_char(query_at, 'YYYY-MM') GROUP BY trim(key_word),"
			+" to_char(query_at, 'YYYY-MM') ) tt where t.da=tt.da and t.ma=tt.cc "
			+ "GROUP BY t.da) uu ON date_table.rq = uu.da ORDER BY rq",nativeQuery = true) 
	public List<Object[]> findDataHotByThreeMonthsDetail();
	
	/**
	 *  按6个月查询(详细)数据热度
	 * @return
	 */
	@Query(value = "SELECT rq,nvl(uu.kw,'') keyword,nvl(uu.cc,0) num FROM( "
			+" SELECT to_char(add_months(sysdate,0),'YYYY-MM') rq FROM DUAL UNION "
			+" SELECT to_char(add_months(sysdate,-1),'YYYY-MM') rq FROM DUAL UNION "
			+" SELECT to_char(add_months(sysdate,-2),'YYYY-MM') rq FROM DUAL UNION "
			+" SELECT to_char(add_months(sysdate,-3),'YYYY-MM') rq FROM DUAL UNION "
			+" SELECT to_char(add_months(sysdate,-4),'YYYY-MM') rq FROM DUAL UNION "
			+" SELECT to_char(add_months(sysdate,-5),'YYYY-MM') rq FROM DUAL "
			+" ) date_table "
			+" LEFT JOIN (SELECT t.da,max(kw) kw,max(tt.cc) cc FROM( "
			+" SELECT da,max(cc) ma FROM( "
			+" SELECT to_char(query_at, 'YYYY-MM') da,trim(key_word),count(*) cc FROM "
			+" user_query_logs t WHERE to_char(add_months(sysdate,-6),'YYYY-MM') <= to_char(query_at, 'YYYY-MM') "
			+" GROUP BY trim(key_word),to_char(query_at, 'YYYY-MM') )a GROUP BY da) t, "
			+" (SELECT to_char(query_at, 'YYYY-MM') da,trim(key_word) kw,count(*) cc FROM user_query_logs t "
			+" WHERE to_char(add_months(sysdate,-6),'YYYY-MM') <= to_char(query_at, 'YYYY-MM') GROUP BY trim(key_word),"
			+" to_char(query_at, 'YYYY-MM') ) tt where t.da=tt.da and t.ma=tt.cc "
			+ "GROUP BY t.da) uu ON date_table.rq = uu.da ORDER BY rq",nativeQuery = true) 
	public List<Object[]> findDataHotBySixMonthsDetail();
}
