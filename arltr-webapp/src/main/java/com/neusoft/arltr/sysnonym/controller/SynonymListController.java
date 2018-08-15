package com.neusoft.arltr.sysnonym.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.neusoft.arltr.common.base.ListPage;
import com.neusoft.arltr.common.base.RespBody;
import com.neusoft.arltr.common.entity.synonym.Synonym;
import com.neusoft.arltr.common.service.SynonymService;
/**
 * 同义词 Controller
 * @author wuxl
 *
 */
@Controller
public class SynonymListController {
	@Autowired
	SynonymService synonymService;
	/**
	 * 动态查询根据查询条件
	 * @return
	 */
	@PostMapping("/synonym/list/data")
	@ResponseBody
	public ListPage data(@RequestParam(value = "page", defaultValue = "1") Integer pageNumber,
			@RequestParam(value = "rows", defaultValue = "10") Integer pageSize, 
			@RequestParam(value="order",defaultValue="desc") String order,
			@RequestParam(value="sort",defaultValue="updateAt") String sort,Synonym queryParam) {
		 RespBody<ListPage> data=synonymService.query(pageNumber, pageSize, order, sort, queryParam);
		 return data.getBody();
	}
	/**
	 * 删除航空词汇的信息单条删除
	 * 	
	 */
	@PostMapping("/synonym/remove/{id}")
	@ResponseBody
	public RespBody<String> remove(@PathVariable("id") Integer id){
		return synonymService.remove(id);
	};
	
	/**
	 * 批量删除航空词汇的信息
	 * 	
	 */
	@PostMapping("/synonym/multiple")
	@ResponseBody
	public RespBody<String> removeAll(@RequestBody List<Synonym> synonym){
		return synonymService.removeAll(synonym);
	};
	/**
	 * 列表页面入口方法
	 * @param model
	 * @return
	 */
	@GetMapping("/synonym/list")
	public String list(Model model) {

		return "synonym/synonym_list";
	}

}
