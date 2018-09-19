/**
 * 
 */
package com.neusoft.arltr.sample.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.solr.core.query.result.HighlightPage;
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
import com.neusoft.arltr.common.entity.search.Result;
import com.neusoft.arltr.common.exception.BusinessException;
import com.neusoft.arltr.common.model.search.TestModel;
import com.neusoft.arltr.common.service.SearchService;
import com.neusoft.arltr.common.service.SysService;
import com.neusoft.arltr.common.utils.SolrClientHelper;

/**
 *
 *
 */
@Controller
public class SampleController {

	@Autowired
	SearchService searchService;
	
	@Autowired
	SysService sysService;
	
	@Autowired
	SolrClientHelper solrClientHelper;
	
//	@GetMapping("/s")
//	public String idx() throws Exception {
//
//		return "search_result";
//	}
//	
//	@GetMapping("/search")
//	public String search(String query, 
//			@RequestParam(defaultValue = "0") Integer pageNum, 
//			@RequestParam(defaultValue = "10") Integer pageSize, Model model) {
//		
//		if (query == null || query.isEmpty()) {
//			return "index";
//		}
//		
////		HighlightPage<Result> result = searchRepository.findByTitleOrContent(query, query, new PageRequest(pageNum, pageSize));
////		
//		model.addAttribute("query", query);
//		return "search_result";
//	}
//	
//	@PostMapping("/q")
//	@ResponseBody
//	public RespBody<Map<String, Object>> q(String keyword) throws Exception {
//
//		return searchService.query(keyword);
//	}
	
	@GetMapping("/sample/exception")
	@ResponseBody
	public RespBody<String> exception(Integer cmd) throws Exception {

		if (cmd == 1) {
			TestModel tm = new TestModel();
			tm.setCode("aaa");
			
			searchService.postSomeThing(tm);
			
			return null;
		}
		
		if (cmd == 2) {
			throw new Exception("A o");
		}
		
		return new RespBody<String>();
	}
	
	@PostMapping("/p")
	@ResponseBody
	public RespBody<String> postSome(@RequestBody String[] o) {
		
		RespBody<String> resp = new RespBody<String>();
		
		solrClientHelper.saveLexiconWordList(o);
		
		return resp;
	} 
	
	@PostMapping("/r")
	@ResponseBody
	public RespBody<String> rm(@RequestBody String[] o) {
		
		RespBody<String> resp = new RespBody<String>();
		
		solrClientHelper.removeLexiconWordList(o);
		
		return resp;
	} 
	
	@GetMapping("/user1/{id}")
	public String testPage(@PathVariable Integer id, Model model) {
		
		model.addAttribute("msg", "Hello! " + sysService.getUser(id).getBody().getEmployeeName());
		
		return "test";
	}
	
	@GetMapping("/sample/list")
	public String list(Model model) {
		return "samples/list_sample";
	}
	
	@GetMapping("/sample/get")
	@ResponseBody
	public RespBody<String> get(Integer cmd) throws Exception {

		RespBody<String> resp = new RespBody<String>();
		
		resp.setBody("it's ok.");
		
		if (cmd == 1) {
			
			throw new Exception("发生系统异常");
		}
		
		if (cmd == 2) {
			
			throw new RuntimeException("发生运行时系统异常");
		}
		
		if (cmd == 3) {
			
			throw new BusinessException("发生业务异常");
		}
		
		return resp;
	}
	
	@PostMapping("/sample/post")
	@ResponseBody
	public RespBody<Map<String, Object>> post(@RequestBody Map<String, Object> param, Integer cmd) throws Exception {

		RespBody<Map<String, Object>> resp = new RespBody<Map<String, Object>>();
		
		resp.setBody(param);
		
		if (cmd == 1) {
			
			throw new Exception("发生系统异常");
		}
		
		if (cmd == 2) {
			
			throw new RuntimeException("发生运行时系统异常");
		}
		
		if (cmd == 3) {
			
			throw new BusinessException("发生业务异常");
		}
		
		return resp;
	}
	
	@GetMapping("/sample/list/data")
	@ResponseBody
	public ListPage data(@RequestParam(value = "page", defaultValue = "1") Integer pageNumber,
			@RequestParam(value = "rows", defaultValue = "10") Integer pageSize, Map<String, Object> queryParam) {
		
		List<Map<String, String>> data = new ArrayList<Map<String, String>>();
		
		for (int i =0 ; i < 15; i++) {
			
			Map<String, String> rec = new HashMap<String, String>();
			
			rec.put("r1", "rec" + i + "-1");
			rec.put("r2", "rec" + i + "-2");
			rec.put("r3", "rec" + i + "-3");
			
			data.add(rec);
		}
		
		Page<Map<String, String>> page = new PageImpl<Map<String, String>>(data.subList(pageNumber-1, pageNumber * pageSize), new PageRequest(pageNumber-1, pageSize), data.size());
		
		return new ListPage(page);
	}
	
}
