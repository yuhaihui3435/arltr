/**
 * 
 */
package com.neusoft.arltr.common.service;

import java.util.List;
import java.util.Map;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.neusoft.arltr.common.base.ListPage;
import com.neusoft.arltr.common.base.RespBody;
import com.neusoft.arltr.common.entity.statistics.UserQueryLogs;
import com.neusoft.arltr.common.entity.statistics.UserViewLogs;

/**
 * 统计服务接口类
 * 
 * @author zhanghaibo
 *
 */
@FeignClient("arltr-statistics")
public interface StatisticsService {
	/**
	 * 添加用户查看记录方法
	 * @param entity 用户查看记录实体
	 * @return
	 */
	@PostMapping("/statistics/saveview")
	@Async
	public  RespBody<UserViewLogs> saveUserView(@RequestBody UserViewLogs entity);
	/**
	 * 添加用户查询记录方法
	 * @param keyWord 查询关键词
	 * @param userId  用户ID
	 * @param userName 用户名
	 * @return
	 */
	@PostMapping("/statistics/savequery")
	@Async
	public  RespBody<UserQueryLogs> saveUserQuery(@RequestParam(value="keyWord",defaultValue="") String keyWord,
			@RequestParam(value="userId",defaultValue="0") Integer userId,
			@RequestParam(value="userName",defaultValue="") String userName);
	
	
	/**
	* 条件查询（分页）
	* 
	* @param pageNumber 当前页数
	* @param pageSize 每页显示数据
	* @param condition 查询条件（实体类）
	* @return 分页数据
	*/
	@PostMapping("/statistics/queryfrequency")
	public RespBody<ListPage> queryFrequency(@RequestParam(value = "page", defaultValue = "1") Integer pageNumber, 
			@RequestParam(value = "rows", defaultValue = "10") Integer pageSize,
			@RequestBody UserViewLogs condition);
	
	/**
	* 用户搜索条件查询（分页）
	* 
	* @param pageNumber 当前页数
	* @param pageSize 每页显示数据
	* @param condition 查询条件（实体类）
	* @return 分页数据
	*/
	@PostMapping("/statistics/usersearch")
	public RespBody<ListPage> userQuery(@RequestParam(value = "page", defaultValue = "1") Integer pageNumber, 
			@RequestParam(value = "rows", defaultValue = "10") Integer pageSize,
			@RequestBody UserQueryLogs condition);
	
	/**
	* 用户文档查阅详情条件查询（分页）
	* 
	* @param pageNumber 当前页数
	* @param pageSize 每页显示数据
	* @param condition 查询条件（实体类）
	* @return 分页数据
	*/
	@PostMapping("/statistics/userDocumentsearch")
	public RespBody<ListPage> userDocumentQuery(@RequestParam(value = "page", defaultValue = "1") Integer pageNumber, 
			@RequestParam(value = "rows", defaultValue = "10") Integer pageSize,
			@RequestBody UserViewLogs condition);
	
	/**
	 * 按查询条件查询数据热度
	 * @return
	 */
	@GetMapping("/statistics/hot/data")
	public RespBody<Map<String,Object>> getDataHotByType(@RequestParam("type") String type);
	
	/**
	 * 按条件查询(详细)数据热度
	 * @return
	 */
	@GetMapping("/statistics/hot/data/detail")
	public RespBody<Map<String,Object>> getDataHotByTypeDetail(@RequestParam("type") String type);
	/**
	 * 根据用户id获取用户查询历史数据列表
	 * @param userId
	 * @return
	 */
	@GetMapping("/statistics/querylisthistory")
	public RespBody<List<Object>> getListHistoryOfUser(@RequestParam(value="userId",defaultValue="0") Integer userId);
	/**
	 * 获取前五位搜索热词
	 * @return
	 */
	@GetMapping("/statistics/queryhistorytopfive")
	public RespBody<Map<String,Integer>> getHotListTopFive();
}
