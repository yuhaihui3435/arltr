package com.neusoft.arltr.lexicon.controller;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.neusoft.arltr.common.base.RespBody;
import com.neusoft.arltr.common.constant.SessionKey;
import com.neusoft.arltr.common.entity.lexicon.Lexicon;
import com.neusoft.arltr.common.entity.user.User;
import com.neusoft.arltr.common.service.LexiconService;
import com.neusoft.arltr.common.service.SysService;

/**
 * 航天词汇详情Controller
 * @author lishuang
 *
 */
@Controller
public class LexiconDetailController {
	
	@Autowired
	LexiconService lexiconService;
	@Autowired
	SysService sysService;
	
	/**
	 * 通过id查找词条详细信息
	 * @param id 词条序列
	 * @return
	 */
	@GetMapping("/lexicon/{id}")
	public String getLexiconInfo(@PathVariable("id") Integer id,Model model,HttpServletRequest request){
		User user = (User)request.getSession().getAttribute(SessionKey.USER); 
		Lexicon lexicon=lexiconService.getOne(id).getBody();
		Date curDate = new Date();
		lexicon.setUpdateAt(curDate);
		lexicon.setUpdateBy(user.getId());
		lexicon.setUpdateByName(user.getEmployeeName());
		model.addAttribute("lexicon",lexicon);
		return "lexicon/lexicon_detail";
	}
	
	/**
	 * 航天词条的新建页面
	 */
	@GetMapping("/lexicon/new")
	public String getLexcionNew(Model model,HttpServletRequest request){
		User user = (User)request.getSession().getAttribute(SessionKey.USER);
		Lexicon lexicon = new Lexicon();
		lexicon.setCreateBy(user.getId());
		lexicon.setCreateByName(user.getEmployeeName());
		lexicon.setUpdateBy(user.getId());
		lexicon.setUpdateByName(user.getEmployeeName());
		Date curDate = new Date();
		lexicon.setCreateAt(curDate);
		lexicon.setUpdateAt(curDate);
		model.addAttribute("lexicon", lexicon);
		return "lexicon/lexicon_detail";
	}
	/**
	* 保存词条
	* 
	* @param entity 词条实体类
	* @return 返回信息
	*/
	@PostMapping("/lexicon/save")
	@ResponseBody
	public RespBody<Lexicon> save(@RequestBody Lexicon entity, HttpServletRequest request){
		return lexiconService.save(entity);
	}	
}
