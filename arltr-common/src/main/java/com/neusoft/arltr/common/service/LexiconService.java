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
import org.springframework.web.multipart.MultipartFile;

import com.neusoft.arltr.common.base.ListPage;
import com.neusoft.arltr.common.base.RespBody;
import com.neusoft.arltr.common.entity.lexicon.Lexicon;

/**
 * 词库服务接口类
 * 
 * @author zhanghaibo
 *
 */
@FeignClient("arltr-lexicon")
public interface LexiconService {

	/**
	* 条件查询（分页）
	* 
	* @param pageNumber 当前页数
	* @param pageSize 每页显示数据
	* @param condition 查询条件（实体类）
	* @return 分页数据
	*/
	@PostMapping("/lexicon/query")
	public RespBody<ListPage> query(@RequestParam(value = "page", defaultValue = "1") Integer pageNumber, @RequestParam(value = "rows", defaultValue = "10") Integer pageSize, @RequestParam(value="order",defaultValue="desc") String order,@RequestParam(value="sort",defaultValue="updateAt") String sort, @RequestBody Lexicon condition);
	
	/**
	* 根据id获取词条
	* 
	* @param id 主键
	* @return 词条实体类
	*/
	@GetMapping("/lexicon/{id}")
	public RespBody<Lexicon> getOne(@PathVariable("id") Integer id);
	
	/**
	* 保存词条
	* 
	* @param entity 词条实体类
	* @return 返回信息
	*/
	@PostMapping("/lexicon/save")
	public RespBody<Lexicon> save(@RequestBody Lexicon entity);
	
	/**
	* 保存词条
	* 
	* @param entity 词条实体类
	* @return 返回信息
	*/
	@PostMapping("/lexicon/saveall")
	@Async
	public RespBody<String> saveAll(@RequestBody List<Lexicon> entities);
	
	/**
	* 删除词条
	* 
	* @param id 主键
	* @return 返回信息
	*/
	@PostMapping("/lexicon/{id}/remove")
	public RespBody<String> remove(@PathVariable("id") Integer id);
	
	/**
	* 批量删除词条
	* 
	* @param entities 实体list
	* @return 返回信息
	*/
	@PostMapping("/lexicon/remove/multiple")
	public RespBody<String> removeAll(@RequestBody List<Lexicon> entities);
	@PostMapping("/lexicon/fileupload")
	public RespBody<String> getFileUploadTestMethod(@RequestParam("files[]") MultipartFile[] files);
}
