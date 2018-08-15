package com.neusoft.arltr.statistics.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import com.neusoft.arltr.common.base.RespBody;
import com.neusoft.arltr.common.service.StatisticsService;
/**
 * 数据热度统计 Controller
 * @author pei.j
 *
 */
@Controller
public class DataHotController {
	@Autowired
	StatisticsService statisticsService;
	/**
	 * 动态查询根据查询条件
	 * @return
	 */
	@GetMapping("/statistics/hot/data/{type}")
	@ResponseBody
	public RespBody<Map<String,Object>> data(@PathVariable("type") String type) {
		RespBody<Map<String,Object>> resp=new RespBody<Map<String,Object>>();
		Map<String,Object> map=new HashMap<String,Object>();
			
		Map<String,Object> data=statisticsService.getDataHotByType(type).getBody();
		Map<String,Object> data1=statisticsService.getDataHotByTypeDetail(type).getBody();
		map.put("Top", data);
		map.put("detail", data1);
		resp.setBody(map);
		
		 return resp;
	}
	
	/**
	 * 页面入口
	 * @param model
	 * @return
	 */
	@GetMapping("/statistics/datahot")
	public String list(Model model) {
		return "statistics/data_heat";
	}

}
