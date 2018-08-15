package com.neusoft.arltr.user.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import com.neusoft.arltr.common.base.RespBody;
import com.neusoft.arltr.common.entity.user.Role;
import com.neusoft.arltr.common.entity.user.User;
import com.neusoft.arltr.common.service.SysService;
import com.neusoft.arltr.constant.SessionKey;

@Controller
@RequestMapping("/user/login")
@SessionAttributes({SessionKey.USER, SessionKey.NAV_ID, SessionKey.MENU_ID, SessionKey.ITEM_ID})
public class LoginController {
	private static Logger logger = Logger.getLogger(LoginController.class);
	@Autowired
	SysService sysService;
	/**
	 * 根据用户id验证是否有权登录
	 * @throws IOException 
	 */
	@GetMapping("/validate/{userid}")
	public void login(HttpServletRequest request,HttpServletResponse response,@PathVariable String userid) throws IOException {
		User user = sysService.getUserOfUserIdOrOtherId(userid).getBody();
		if(null != user){
			
			request.getSession().setAttribute(SessionKey.USER, user);
			
			request.getSession().setAttribute(SessionKey.IS_ADMIN, false);
			
			for (Role role : user.getRoles()) {
				if (role.getId() == 1) {
					request.getSession().setAttribute(SessionKey.IS_ADMIN, true);
					break;
				}
			}
			
			response.sendRedirect("/");
			//return "redirect:/";
		}else{
			response.sendRedirect("/user/login/errorrefuse");
		}
		
	}
	@GetMapping("/errorrefuse")
	public String showErrorRefuse(){
		return "error_refuse";
	}
	@PostMapping("/clearsesssion")
	@ResponseBody
	public RespBody<String> clearSesssion(@ModelAttribute(SessionKey.USER) User user, SessionStatus sessionStatus){
//		logger.info("===================================");
		sessionStatus.setComplete();
		return new RespBody<String>();
	}
}
