/**
 * 
 */
package com.neusoft.arltr.common.service;

import java.util.List;
import java.util.Map;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.neusoft.arltr.common.base.RespBody;
import com.neusoft.arltr.common.entity.search.Result;
import com.neusoft.arltr.common.model.search.TestModel;

/**
 * 实时搜索服务接口类
 * 
 *
 *
 */
@FeignClient("arltr-search")
public interface SearchService {

	@PostMapping("/search/p")
	public RespBody<String> postSomeThing(@RequestBody TestModel model);
	
	/**
	 * 查询方法
	 * 
	 * @param keyword 关键字
	 * @param classification 查询用户的密级
	 * @param pageNum 页码
	 * @param pageSize 页长
	 * @param queryField 查询字段
	 * @param orderby 排序字段
	 * 
	 * @return 查询结果（高亮）
	 */
	@GetMapping("/search/query")
	public RespBody<Map<String, Object>> query(
			@RequestParam("keyword") String keyword,
			@RequestParam(name = "classification", defaultValue = "60") String classification,
			@RequestParam(name = "source", defaultValue = "*") String source,
			@RequestParam(name = "pageNum", defaultValue = "0") Integer pageNum, 
			@RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
			@RequestParam(name = "queryField", defaultValue = "1") Integer queryField,
			@RequestParam(name = "orderby", defaultValue = "1") Integer orderby);
	
	@GetMapping("/search/snapshoot")
	public RespBody<List<Result>> getSnapshoot(@RequestParam("id") String id);
	
	/**
	 * 获取输入提示
	 * 
	 * @param keyword 关键字
	 * 
	 * @return RespBody<List<String>> 提示信息
	 */
	@GetMapping("/search/suggestion")
	public RespBody<List<String>> getSuggestion(@RequestParam("keyword") String keyword);
	
}
