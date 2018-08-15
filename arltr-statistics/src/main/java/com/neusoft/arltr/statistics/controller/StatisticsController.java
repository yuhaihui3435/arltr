package com.neusoft.arltr.statistics.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.neusoft.arltr.common.base.ListPage;
import com.neusoft.arltr.common.base.RespBody;
import com.neusoft.arltr.common.entity.statistics.UserQueryLogs;
import com.neusoft.arltr.common.entity.statistics.UserViewLogs;
import com.neusoft.arltr.common.utils.ChineseToPinyin;
import com.neusoft.arltr.statistics.repository.DataHotRepository;
import com.neusoft.arltr.statistics.repository.StatisticsQueryRepository;
import com.neusoft.arltr.statistics.repository.StatisticsViewRepository;
/**
 * 索引维护控制类
 * @author wuxl
 *
 */
@RestController
@RequestMapping("/statistics")
public class StatisticsController {
	@Autowired
	StatisticsQueryRepository statisticsQueryRepository;
	@Autowired
	StatisticsViewRepository statisticsViewRepository;
	@Autowired
	DataHotRepository dataHotRepository;
	/**
	* 保存用户查看记录
	* 
	* @param entity 用户查看记录实体类
	* @return 返回信息
	*/
	@PostMapping("/saveview")
	public RespBody<UserViewLogs> saveView(@RequestBody UserViewLogs entity){
		
		entity.setViewAt(new Date());
		
		statisticsViewRepository.save(entity);
		
		return new RespBody<UserViewLogs>();
	}
	/**
	* 保存用户查询记录
	* 
	* @param entity 用户查询记录实体类
	* @return 返回信息
	*/
	@PostMapping("/savequery")
	public RespBody<UserQueryLogs> saveQuery(@RequestParam(value="keyWord",defaultValue="") String keyWord,
			@RequestParam(value="userId",defaultValue="0") Integer userId,
			@RequestParam(value="userName",defaultValue="") String userName){
		
		UserQueryLogs entity = new UserQueryLogs();
		
		entity.setKeyWord(keyWord);
		entity.setQueryAt(new Date());
		entity.setUserId(userId);
		entity.setUserName(userName);
		entity.setKeyWordPinyin(ChineseToPinyin.getPinYin(entity.getKeyWord()));
		entity.setKeyWordAbbre(ChineseToPinyin.getPinYinAbbre(entity.getKeyWord()));

		statisticsQueryRepository.save(entity);
		
		return new RespBody<UserQueryLogs>();
	}
	
	/**
	* 条件查询（分页）
	* 
	* @param pageNumber 当前页数
	* @param pageSize 每页显示数据
	* @param condition 查询条件（实体类）
	* @return 分页数据
	*/
	@PostMapping("/queryfrequency")
	public RespBody<ListPage> queryFrequency(@RequestParam(value = "page", defaultValue = "1") Integer pageNumber, 
			@RequestParam(value = "rows", defaultValue = "10") Integer pageSize,
			@RequestBody UserViewLogs condition){

		RespBody<ListPage> resbody = new RespBody<ListPage>();
		
		//一共有多少条数据 分页用
		String word = condition.getQueryKeyWord();
		word = word == null ? "" : word.trim();
		int count =  statisticsViewRepository.findAllViewGroupWordCount(word);
		int startnum = pageSize * (pageNumber - 1) + 1;
		int endnum = pageSize * pageNumber;
		List<Object>  tempList = statisticsViewRepository.findAllViewGroupWord(word, startnum, endnum);
//		List<Object>  tempList = statisticsViewRepository.findAllViewGroupWord(word, startnum,pageSize);
		tempList = tempList==null ? new ArrayList<Object>():tempList;
		List<UserViewLogs>  resList = new ArrayList<UserViewLogs>();
		for (int i = 0;i<tempList.size();i++) {
			Object[] obj = (Object[])tempList.get(i);
			UserViewLogs utemp = new UserViewLogs();
			utemp.setQueryKeyWord((String)obj[0]);
			utemp.setDocTitle((String)obj[1]);
			utemp.setQuerycount(Integer.parseInt(String.valueOf(obj[2])));
			resList.add(utemp);
		}

		Pageable pageable =new PageRequest(pageNumber-1,pageSize);
		Page<UserViewLogs> respage = new PageImpl<UserViewLogs>(resList,pageable,count);

		ListPage plist = new ListPage(respage);
		resbody.setBody(plist);
		
		return resbody;
	}
	
	/**
	* 用户查询条件查询（分页）
	* 
	* @param pageNumber 当前页数
	* @param pageSize 每页显示数据
	* @param condition 查询条件（实体类）
	* @return 分页数据
	*/
	@PostMapping("/usersearch")
	public RespBody<ListPage> userQuery(@RequestParam(value = "page", defaultValue = "1") Integer pageNumber, 
			@RequestParam(value = "rows", defaultValue = "10") Integer pageSize,
			@RequestBody UserQueryLogs condition){

		RespBody<ListPage> resbody = new RespBody<ListPage>();
		
		//一共有多少条数据 分页用
		Integer role = condition.getId();
		role = role == null ? 0 : role;
		Integer user = condition.getUserId();
		user = user == null ? 0 : user;
		String orgId=condition.getUserName();
		orgId = orgId == null ? "-1" : orgId;
		int count =  statisticsQueryRepository.findAllViewGroupUserCount(role,user,orgId);
		int startnum = pageSize * (pageNumber - 1) + 1;
		int endnum = pageSize * pageNumber;
		List<Object>  tempList = statisticsQueryRepository.findAllViewGroupUser(role,user,orgId,startnum, endnum);
//		List<Object>  tempList = statisticsQueryRepository.findAllViewGroupUser(role,user,orgId,startnum,pageSize);
		tempList = tempList==null ? new ArrayList<Object>():tempList;
		List<UserQueryLogs>  resList = new ArrayList<UserQueryLogs>();
		for (int i = 0;i<tempList.size();i++) {
			Object[] obj = (Object[])tempList.get(i);
			UserQueryLogs utemp = new UserQueryLogs();
			utemp.setUserName((String)obj[0]);
			utemp.setKeyWord((String)obj[1]);
			utemp.setSarchNum(Integer.parseInt(String.valueOf(obj[2])));
			utemp.setUserId(Integer.parseInt(String.valueOf(obj[3])));
			resList.add(utemp);
		}
        Pageable pageable =new PageRequest(pageNumber-1,pageSize);
		Page<UserQueryLogs> respage = new PageImpl<UserQueryLogs>(resList,pageable,count);
		ListPage plist = new ListPage(respage);
		resbody.setBody(plist);
		return resbody;
	}
	
	/**
	* 用户查询文档详情条件查询（分页）
	* 
	* @param pageNumber 当前页数
	* @param pageSize 每页显示数据
	* @param condition 查询条件（实体类）
	* @return 分页数据
	*/
	@PostMapping("/userDocumentsearch")
	public RespBody<ListPage> userDocumentQuery(@RequestParam(value = "page", defaultValue = "1") Integer pageNumber, 
			@RequestParam(value = "rows", defaultValue = "10") Integer pageSize,
			@RequestBody UserViewLogs condition){

		RespBody<ListPage> resbody = new RespBody<ListPage>();
		
		//一共有多少条数据 分页用
		Integer userId = condition.getUserId();
		userId = userId == null ? 0 : userId;
		String word = condition.getQueryKeyWord();
		word = word == null ? "nodata" : word.trim();
		int count =  statisticsViewRepository.findAllViewUserDocumentCount(userId,word);
		int startnum = pageSize * (pageNumber - 1) + 1;
		int endnum = pageSize * pageNumber;
		List<Object>  tempList = statisticsViewRepository.findAllViewUserDocument(userId,word,startnum, endnum);
//		List<Object>  tempList = statisticsViewRepository.findAllViewUserDocument(userId,word,startnum,pageSize);
		tempList = tempList==null ? new ArrayList<Object>():tempList;
		List<UserViewLogs>  resList = new ArrayList<UserViewLogs>();
		for (int i = 0;i<tempList.size();i++) {
			Object[] obj = (Object[])tempList.get(i);
			UserViewLogs utemp = new UserViewLogs();
			utemp.setDocTitle((String)obj[0]);
			utemp.setDocLocation((String)obj[1]);
			utemp.setQuerycount(Integer.parseInt(String.valueOf(obj[2])));
			resList.add(utemp);
		}
        Pageable pageable =new PageRequest(pageNumber-1,pageSize);
		Page<UserViewLogs> respage = new PageImpl<UserViewLogs>(resList,pageable,count);
		ListPage plist = new ListPage(respage);
		resbody.setBody(plist);
		return resbody;
	}
	
	/**
	 * 按条件查询数据热度
	 * @return
	 */
	@GetMapping("/hot/data")
	public RespBody<Map<String,Object>> getDataHotByType(@RequestParam("type") String type){
		RespBody<Map<String,Object>> resp=new RespBody<Map<String,Object>>();
		Map<String,Object> map=new HashMap<String,Object>();
		List<String> keyword=new ArrayList<String>();
		List<Integer> count=new ArrayList<Integer>();
		if(("7days").equals(type)){
			List<Object[]> list=dataHotRepository.findDataHotByDays();
			Collections.reverse(list);
			for(Object[] o:list){
				 keyword.add(String.valueOf(o[0]));
				 count.add(Integer.parseInt(String.valueOf(o[1])));
			}
		}else if(("1month").equals(type)){
			List<Object[]> list=dataHotRepository.findDataHotByOneMonth();
			Collections.reverse(list);
			for(Object[] o:list){
				 keyword.add(String.valueOf(o[0]));
				 count.add(Integer.parseInt(String.valueOf(o[1])));
			}
		}else if(("3months").equals(type)){
			List<Object[]> list=dataHotRepository.findDataHotByThreeMonths();
			Collections.reverse(list);
			for(Object[] o:list){
				 keyword.add(String.valueOf(o[0]));
				 count.add(Integer.parseInt(String.valueOf(o[1])));
			}
		}else{
			List<Object[]> list=dataHotRepository.findDataHotBySixMonths();
			Collections.reverse(list);
			for(Object[] o:list){
				 keyword.add(String.valueOf(o[0]));
				 count.add(Integer.parseInt(String.valueOf(o[1])));
			}
		}
		map.put("keyword",keyword);
		map.put("count",count);
		resp.setBody(map);
		return resp;
	}
	
	/**
	 *  按条件查询(详细)数据热度
	 * @return
	 */
	@GetMapping("/hot/data/detail")
	public RespBody<Map<String,Object>> getDataHotByTypeDetail(@RequestParam("type") String type){
		RespBody<Map<String,Object>> resp=new RespBody<Map<String,Object>>();
		Map<String,Object> map=new HashMap<String,Object>();
		List<String> time=new ArrayList<String>();
		List<String> keyword=new ArrayList<String>();
		List<Integer> count=new ArrayList<Integer>();
		if(("7days").equals(type)){
			List<Object[]> list=dataHotRepository.findDataHotByDaysDetail();
			for (Object[] o : list) {
				time.add(String.valueOf(o[0]));
				keyword.add(String.valueOf(o[1]));
				count.add(Integer.parseInt(String.valueOf(o[2])));
			}
		}else if(("1month").equals(type)){
			List<Object[]> list=dataHotRepository.findDataHotByOneMonthDetail();
			time.add("第一周");
			time.add("第二周");
			time.add("第三周");
			time.add("第四周");
			for (Object[] o : list) {
				//time.add(String.valueOf(o[0]));
				keyword.add(String.valueOf(o[1]));
				count.add(Integer.parseInt(String.valueOf(o[2])));
			}
		}else if(("3months").equals(type)){
			List<Object[]> list=dataHotRepository.findDataHotByThreeMonthsDetail();
			for (Object[] o : list) {
				time.add(String.valueOf(o[0]));
				keyword.add(String.valueOf(o[1]));
				count.add(Integer.parseInt(String.valueOf(o[2])));
			}
		}else{
			List<Object[]> list=dataHotRepository.findDataHotBySixMonthsDetail();
			for (Object[] o : list) {
				time.add(String.valueOf(o[0]));
				keyword.add(String.valueOf(o[1]));
				count.add(Integer.parseInt(String.valueOf(o[2])));
			}
		}
		map.put("time", time);
		map.put("keywordDetail", keyword);
		map.put("count", count);
		resp.setBody(map);
		return resp;
	}
	/**
	 * 根据用户id获取用户查询历史数据列表
	 * @param userId
	 * @return
	 */
	@GetMapping("/querylisthistory")
	public RespBody<List<UserQueryLogs>> getListHistoryOfUser(@RequestParam(value="userId",defaultValue="0") Integer userId){
		RespBody<List<UserQueryLogs>> resp=new RespBody<List<UserQueryLogs>>();
//		List<Object[]> list = statisticsViewRepository.getListHistoryOfUser(userId);
//		List<String> listreturn = new ArrayList<String>();
//		for(Object[] o : list){
//			listreturn.add(String.valueOf(o[1]));
//		}
//		resp.setBody(listreturn);
		
		List<UserQueryLogs> userQueryLogs = statisticsQueryRepository.findByUserId(userId);
		
		resp.setBody(userQueryLogs);
		
		return resp;
	}
	/**
	 * 获取前五个热点查询
	 * @return
	 */
	@GetMapping("/queryhistorytopfive")
	public RespBody<Map<String,Integer>> getHotListTopFive(){
		RespBody<Map<String, Integer>> resp=new RespBody<Map<String,Integer>>();
		List<Object[]> list = statisticsViewRepository.getHotListTopFive();
		Map<String,Integer> listreturn = new LinkedHashMap<String,Integer>();
		for(Object[] o : list){
			listreturn.put(String.valueOf(o[0]),Integer.valueOf(String.valueOf(o[1])));
		}
		resp.setBody(listreturn);
		return resp;
	}
}
