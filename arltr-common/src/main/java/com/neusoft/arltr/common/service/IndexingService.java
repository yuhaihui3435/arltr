/**
 * 
 */
package com.neusoft.arltr.common.service;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.neusoft.arltr.common.base.ListPage;
import com.neusoft.arltr.common.base.RespBody;
import com.neusoft.arltr.common.entity.indexing.CronTask;
import com.neusoft.arltr.common.entity.indexing.DataImportLogs;
import com.neusoft.arltr.common.entity.user.User;

/**
 * 索引维护服务接口类
 * 
 * @author zhanghaibo
 *
 */
@FeignClient("arltr-indexing")
public interface IndexingService {
	/**
	* 条件查询（分页）
	* 
	* @param pageNumber 当前页数
	* @param pageSize 每页显示数据
	* @param order 升降排序
	* @param sort  排序字段
	* @param condition 查询条件（实体类）
	* @return 分页数据
	*/
	@PostMapping("/indexing/query")
	public RespBody<ListPage> query(@RequestParam(value = "page", defaultValue = "1") Integer pageNumber, @RequestParam(value = "rows", defaultValue = "10") Integer pageSize,@RequestParam(value="order",defaultValue="desc") String order,@RequestParam(value="sort",defaultValue="updateAt") String sort, @RequestBody DataImportLogs condition);

	/**
	* 根据id获取同义词
	* 
	* @param id 主键
	* @return 同义词实体类
	*/
	@GetMapping("/indexing/{id}")
	public RespBody<DataImportLogs> getOne(@PathVariable("id") Integer id);
	
	/**
	 * 查询定时任务
	 * @author ye.yy
	 * @return RespBody<CronTask> 定时任务实体类
	 */
	@GetMapping("/indexing/timer")
	public RespBody<CronTask> getTask();
	
	/**
	 * 保存定时任务
	 * @author ye.yy
	 * @param cronTask 定时任务信息
	 * @return RespBody<CronTask> 定时任务实体类
	 */
	@PostMapping("/indexing/timer/save")
	public RespBody<CronTask> saveCronTask(@RequestBody CronTask cronTask);
	
	/**
	 * 手动更新确定采集类型
	 * @param importType 采集类型
	 * @param user 执行者
	 * @return RespBody<Object>
	 */
	@PostMapping("/indexing/manualupdate/confirm")
	public RespBody<DataImportLogs> confirmManualType(@RequestParam("importType") String importType,@RequestBody User user);
	
	/**
	 * 增加文档评分
	 * 
	 * @param id 文档id
	 * @return RespBody<String>
	 */
	@Async
	@PostMapping("/indexing/doc/score/plus")
	public RespBody<String> plusDocScore(@RequestParam("id") String id);
}
