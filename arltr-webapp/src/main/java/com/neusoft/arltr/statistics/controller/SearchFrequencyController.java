package com.neusoft.arltr.statistics.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.neusoft.arltr.common.base.ListPage;
import com.neusoft.arltr.common.base.RespBody;
import com.neusoft.arltr.common.entity.statistics.UserViewLogs;
import com.neusoft.arltr.common.service.StatisticsService;
/**
 * 搜索频次统计 Controller
 * @author wuxl
 *
 */
@Controller
public class SearchFrequencyController {
	@Autowired
	StatisticsService statisticsService;
	/**
	 * 动态查询根据查询条件
	 * @return
	 */
	@PostMapping("/statistics/frequencydata")
	@ResponseBody
	public ListPage data(@RequestParam(value = "page", defaultValue = "1") Integer pageNumber,
			@RequestParam(value = "rows", defaultValue = "10") Integer pageSize, 
			UserViewLogs queryParam) {
		 RespBody<ListPage> data=statisticsService.queryFrequency(pageNumber, pageSize, queryParam);
		 return data.getBody();
	}
	
	/**
	 * 列表页面入口方法
	 * @param model
	 * @return
	 */
	@GetMapping("/statistics/frequencylist")
	public String list(Model model) {
		return "statistics/search_frequency";
	}
	/**
	 * 根据用户id获取用户查询历史数据列表
	 * @param userId
	 * @return
	 */
	@GetMapping("/statistics/querylisthistory")
	@ResponseBody
	public RespBody<List<Object>> getListHistoryOfUser(@RequestParam(value="userId",defaultValue="0") Integer userId){
		return statisticsService.getListHistoryOfUser(userId);
	}
	/**
	 * 获取前五位搜索热词
	 * @return
	 */
	@GetMapping("/statistics/queryhistorytopfive")
	@ResponseBody
	public RespBody<Map<String,Integer>> getHotListTopFive(){
		return statisticsService.getHotListTopFive();
	}
}
