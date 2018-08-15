package com.neusoft.arltr.sysnonym.controller;

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
import com.neusoft.arltr.common.entity.synonym.Synonym;
import com.neusoft.arltr.common.entity.user.User;
import com.neusoft.arltr.common.service.SynonymService;
import com.neusoft.arltr.common.service.SysService;
/**
 * 同义词 Controller
 * @author wuxl
 *
 */
@Controller
public class SynonymDetailController {
	@Autowired
	SynonymService synonymService;
	@Autowired
	SysService sysService;

	/**
	 * 保存同议词信息
	 * 	
	 */
	@PostMapping("/synonym/detail/insert")
	@ResponseBody
	public RespBody<Synonym> saveInfor(@RequestBody Synonym synonym){
		return synonymService.save(synonym);
	};
	/**
	 * 新建同义词页面方法
	 * @param model
	 * @return
	 */
	@GetMapping("/synonym/detail/new")
	public String detailNew(Model model,HttpServletRequest request) {

		User user = (User)request.getSession().getAttribute(SessionKey.USER); 
		//设置修改人为当前登录人
		Synonym synonym = new Synonym();
		synonym.setCreateBy(user.getId());
		synonym.setCreateByName(user.getEmployeeName());
		synonym.setUpdateBy(user.getId());
		synonym.setUpdateByName(user.getEmployeeName());
		Date curDate = new Date();
		synonym.setCreateAt(curDate);
		synonym.setUpdateAt(curDate);
		model.addAttribute("synonym", synonym);

		return "synonym/synonym_detail";
	}
	/**
	 * 打开修改同义词详情页面
	 * @param id
	 * @param model
	 * @return
	 */
	@GetMapping("/synonym/detail/{id}")
	public String detailInfor(@PathVariable("id") Integer id,Model model,HttpServletRequest request) {
		
//		User user = sysService.getUser(1).getBody(); 
		User user = (User)request.getSession().getAttribute(SessionKey.USER); 
		Synonym synonym=synonymService.getOne(id).getBody();
		Date curDate = new Date();
		synonym.setUpdateAt(curDate);
		synonym.setUpdateBy(user.getId());
		synonym.setUpdateByName(user.getEmployeeName());
		model.addAttribute("synonym",synonym);
		
		return "synonym/synonym_detail";
	}

}
