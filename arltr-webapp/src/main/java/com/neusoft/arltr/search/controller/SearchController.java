/**
 *
 */
package com.neusoft.arltr.search.controller;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import antlr.StringUtils;
import com.neusoft.arltr.common.entity.user.Role;
import org.apache.jena.atlas.lib.StrUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.neusoft.arltr.common.base.RespBody;
import com.neusoft.arltr.common.constant.SessionKey;
import com.neusoft.arltr.common.entity.search.Result;
import com.neusoft.arltr.common.entity.statistics.UserViewLogs;
import com.neusoft.arltr.common.entity.user.User;
import com.neusoft.arltr.common.service.IndexingService;
import com.neusoft.arltr.common.service.SearchService;
import com.neusoft.arltr.common.service.StatisticsService;
import com.neusoft.arltr.common.service.SysService;
import org.springframework.web.servlet.ModelAndView;

/**
 * 搜索控制器
 *
 *
 *
 */
@Controller
@SessionAttributes({SessionKey.USER})
public class SearchController {

	@Autowired
	SearchService searchService;

	@Autowired
	StatisticsService statisticsService;

	@Autowired
	SysService sysService;

	@Autowired
	IndexingService indexingService;



	/**
	 * 搜索主页面
	 */
	@GetMapping("")
	public String index() throws Exception {

		return "search_index";
	}

	/**
	 * 搜索结果页面跳转
	 *
	 * @param query 查询关键字
	 */
	@GetMapping("/search")
	public String resultList(String query, Model model) {

		if (query == null || query.isEmpty()) {
			return "search_index";
		}

		model.addAttribute("query", query);

		return "search_result";
	}

	/**
	 * 搜索结果页面跳转
	 *
	 * @param query 查询关键字
	 * @param pageNum 页码
	 * @param pageSize 页长
	 * @param queryField 查询字段
	 * @param orderby 排序字段
	 *
	 * @return 查询结果（高亮）分页内容
	 */
	@PostMapping("/search/query")
	@ResponseBody
	public RespBody<Map<String, Object>> query(String keyword,
			@RequestParam(defaultValue = "*") String source,
			@RequestParam(defaultValue = "0") Integer pageNum,
			@RequestParam(defaultValue = "10") Integer pageSize,
			@RequestParam(defaultValue = "1") Integer queryField,
			@RequestParam(defaultValue = "1") Integer orderby,
			@ModelAttribute(SessionKey.USER) User user) throws Exception {


		statisticsService.saveUserQuery(keyword, user.getId(), user.getEmployeeName());

		return searchService.query(keyword, String.valueOf(user.getSecurityClass()), source, pageNum, pageSize, queryField, orderby);
	}

	/**
	 * 打开快照
	 * @param id
	 * @param queryKey 查询关键词
	 * @param model
	 * @return 网页快照页面
	 */
	@GetMapping("/snapshoot")
	public String getSnapshoot(String id,String queryKey,Model model){
		List<Result> resultList = this.searchService.getSnapshoot(id).getBody();
		String content = "";
		Date date = new Date();
		if(resultList != null && resultList.size()>0){
			content = resultList.get(0).getCache();
			date = resultList.get(0).getLastModified();
		}
		model.addAttribute("queryKey", queryKey);
		model.addAttribute("content", content);
		model.addAttribute("snatchDate", date);
		return "webpage_snapshoot";
	}

	/**
	 * 查看结果页面
	 *
	 * @param query 查询关键字
	 * @throws IOException
	 */
	@GetMapping("/search/view")
	public String viewResult(String url, String wd, String title, Integer source, String originalId, String docId, @ModelAttribute(SessionKey.USER) User user, HttpServletResponse response) throws IOException {

		UserViewLogs userViewLog = new UserViewLogs();
		userViewLog.setDocLocation(url);
		userViewLog.setDocTitle(title);
		userViewLog.setQueryKeyWord(wd);
		userViewLog.setUserId(user.getId());
		userViewLog.setUserName(user.getEmployeeName());

		statisticsService.saveUserView(userViewLog);

		// 更新文档score
		indexingService.plusDocScore(docId);

		// 访问权限验证
		if (!sysService.isUserAuthorized(source, originalId, user).getBody()) {
			return "redirect:/user/login/errorrefuse";
		}

		response.sendRedirect(url + "&verifyCode=" + user.getUserName());

		return null;
	}

	/**
	 * 获取输入提示
	 *
	 * @param keyword 关键字
	 *
	 * @return RespBody<List<String>> 提示信息
	 */
	@GetMapping("/search/suggestion")
	@ResponseBody
	public RespBody<List<String>> getSuggestion(String keyword) {

		return searchService.getSuggestion(keyword);
	}


	/**
	 *yhh add 为其他系统提供搜索api
	 * @param token 用户身份，工号
	 * @param query 查询关键字
	 *
	 */
	@RequestMapping(value = "/api/query",method = {RequestMethod.GET,RequestMethod.POST})
	public String queryForOther(HttpServletRequest request, String token, String query) throws IOException {
		User user = sysService.getUserOfUserIdOrOtherId(token).getBody();
		if(null != user){

			request.getSession().setAttribute(com.neusoft.arltr.constant.SessionKey.USER, user);

			request.getSession().setAttribute(com.neusoft.arltr.constant.SessionKey.IS_ADMIN, false);

			for (Role role : user.getRoles()) {
				if (role.getId() == 1) {
					request.getSession().setAttribute(com.neusoft.arltr.constant.SessionKey.IS_ADMIN, true);
					break;
				}
			}
			request.setAttribute("query",query);
			return "submitApiQuery";

		}else{
			return "redirect:/user/login/errorrefuse";
		}

	}

}
