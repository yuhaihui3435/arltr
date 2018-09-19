/**
 * 
 */
package com.neusoft.arltr.common.service;

import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.neusoft.arltr.common.base.ListPage;
import com.neusoft.arltr.common.base.RespBody;
import com.neusoft.arltr.common.entity.mask.MaskWord;

/**
 * 屏蔽服务接口类
 * 
 *
 *
 */
@FeignClient("arltr-mask")
public interface MaskService {

	/**
	* 条件查询（分页）
	* 
	* @param pageNumber 当前页数
	* @param pageSize 每页显示数据
	* @param condition 查询条件（实体类）
	* @return 分页数据
	*/
	@PostMapping("/mask/query")
	public RespBody<ListPage> query(@RequestParam(value = "page", defaultValue = "1") Integer pageNumber,
	@RequestParam(value="order",defaultValue="desc") String order,
	@RequestParam(value="sort",defaultValue="updateAt")String sort,
	@RequestParam(value = "rows", defaultValue = "10") Integer pageSize, 
	MaskWord condition);
	
	/**
	* 获取所有屏蔽词
	* 
	* @return 所有屏蔽词
	*/
	@GetMapping("/mask/all")
	public RespBody<List<MaskWord>> getAll();
	
	/**
	* 根据id获取屏蔽词
	* 
	* @param id 主键
	* @return 屏蔽词实体类
	*/
	@GetMapping("/mask/{id}")
	public RespBody<MaskWord> getOne(@PathVariable("id") Integer id);
	
	/**
	* 保存屏蔽词
	* 
	* @param entity 屏蔽词实体类
	* @return 返回信息
	*/
	@PostMapping("/mask/save")
	public RespBody<MaskWord> save(@RequestBody MaskWord entity);
	
	/**
	* 删除屏蔽词
	* 
	* @param id 主键
	* @return 返回信息
	*/
	@PostMapping("/mask/{id}/remove")
	public RespBody<String> remove(@PathVariable("id") Integer id);
	
	/**
	* 批量删除屏蔽词
	* 
	* @param entities 实体list
	* @return 返回信息
	*/
	@PostMapping("/mask/remove/multiple")
	public RespBody<String> removeAll(@RequestBody List<MaskWord> entities);
	
}
