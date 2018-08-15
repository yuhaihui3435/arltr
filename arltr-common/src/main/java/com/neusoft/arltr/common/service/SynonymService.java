/**
 * 
 */
package com.neusoft.arltr.common.service;

import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.neusoft.arltr.common.base.ListPage;
import com.neusoft.arltr.common.base.RespBody;
import com.neusoft.arltr.common.entity.synonym.Synonym;

/**
 * 智能语义服务接口类
 * 
 * @author zhanghaibo
 *
 */
@FeignClient("arltr-synonym")
public interface SynonymService {

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
	@PostMapping("/synonym/query")
	public RespBody<ListPage> query(@RequestParam(value = "page", defaultValue = "1") Integer pageNumber, @RequestParam(value = "rows", defaultValue = "10") Integer pageSize,@RequestParam(value="order",defaultValue="desc") String order,@RequestParam(value="sort",defaultValue="updateAt") String sort, @RequestBody Synonym condition);

	/**
	* 根据id获取同义词
	* 
	* @param id 主键
	* @return 同义词实体类
	*/
	@GetMapping("/synonym/{id}")
	public RespBody<Synonym> getOne(@PathVariable("id") Integer id);
	
	/**
	* 保存同义词
	* 
	* @param entity 同义词实体类
	* @return 返回信息
	*/
	@PostMapping("/synonym/save")
	public RespBody<Synonym> save(@RequestBody Synonym entity);
	
	/**
	* 批量保存同义词
	* 
	* @param entity 同义词实体类
	* @return 返回信息
	*/
	@PostMapping("/synonym/saveall")
	@Async
	public RespBody<String> saveAll(@RequestBody List<Synonym> entities);
	
	/**
	* 删除同义词
	* 
	* @param id 主键
	* @return 返回信息
	*/
	@PostMapping("/synonym/{id}/remove")
	public RespBody<String> remove(@PathVariable("id") Integer id);
	
	/**
	* 批量删除同义词
	* 
	* @param entities 实体list
	* @return 返回信息
	*/
	@PostMapping("/synonym/remove/multiple")
	public RespBody<String> removeAll(@RequestBody List<Synonym> entities);
}
