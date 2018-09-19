/**
 * 
 */
package com.neusoft.arltr.interceptor;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.neusoft.arltr.common.entity.user.Menu;
import com.neusoft.arltr.common.entity.user.User;
import com.neusoft.arltr.common.service.SysService;
import com.neusoft.arltr.constant.SessionKey;

/**
 * 权限验证拦截器
 * 
 *
 *
 */
public class AuthenticatingInterceptor implements HandlerInterceptor {
	private static Logger logger = Logger.getLogger(AuthenticatingInterceptor.class);
	@Autowired
	SysService sysService;
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		logger.info("URI:" + request.getServletPath() + " [" + request.getMethod() + "] Begin");
		if (request.getParameter(SessionKey.NAV_ID) != null) {
			request.getSession().setAttribute(SessionKey.NAV_ID, request.getParameter(SessionKey.NAV_ID));
			if(request.getParameter(SessionKey.NAV_ID).equals("2")){//权限管理一级目录，默认选择一个菜单
				request.getSession().setAttribute(SessionKey.MENU_ID, 8);
				request.getSession().setAttribute(SessionKey.ITEM_ID, 10);
			}else if (request.getParameter(SessionKey.NAV_ID).equals("4")){//统计分析一级目录，默认选择用户搜索统计
				request.getSession().setAttribute(SessionKey.MENU_ID, 14);
				request.getSession().setAttribute(SessionKey.ITEM_ID, 15);
			}else{
				request.getSession().setAttribute(SessionKey.MENU_ID, 0);
				request.getSession().setAttribute(SessionKey.ITEM_ID, 0);
			}
		}
		
		if (request.getParameter(SessionKey.ITEM_ID) != null) {
			
			request.getSession().setAttribute(SessionKey.ITEM_ID, request.getParameter(SessionKey.ITEM_ID));
			
			Integer menuId = Integer.parseInt(request.getParameter(SessionKey.ITEM_ID));
			
			if (menuId > 0) {
				Menu currentMenu = sysService.getMenu(menuId);
				
				if (currentMenu != null) {
					request.getSession().setAttribute(SessionKey.MENU_ID, currentMenu.getParentId());
				}
			}
		}
		//TODO 
//		if (request.getSession().getAttribute(SessionKey.USER) == null) {
//			User user = sysService.getUser(2).getBody();
//			request.getSession().setAttribute(SessionKey.USER, user);
//		}
		
		if (request.getSession().getAttribute(SessionKey.USER) != null) {
			return true;
		}
//		response.sendRedirect("/user/login/validate/test45678");
		response.sendRedirect("/user/login/errorrefuse");
		return false;
	}
	
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView)
			throws Exception {
		// 菜单显示控制，加载用户可以浏览的导航菜单，左侧菜单
		if (modelAndView != null && request.getSession().getAttribute(SessionKey.USER) != null) {
			User user = (User) request.getSession().getAttribute(SessionKey.USER);
			// 获取登录用户的菜单树
			List<Menu> menus = sysService.getMenusOfUser(user.getId());
			
			modelAndView.addObject("commonNavigators", menus);
			// 设置默认导航菜单为第一个导航菜单
			if (request.getSession().getAttribute(SessionKey.NAV_ID) == null || 
					Integer.parseInt(request.getSession().getAttribute(SessionKey.NAV_ID).toString()) == 0) {
				
				if (menus.size() > 0) {
					String defaultUri="";
					if(!"/".equals(request.getRequestURI()) && !request.getRequestURI().equals("/search")
							&& !request.getRequestURI().equals("/snapshoot")){
						request.getSession().setAttribute(SessionKey.NAV_ID, menus.get(0).getId());
						defaultUri = menus.get(0).getUri();
						
						if (defaultUri == null || defaultUri.isEmpty()) {
							defaultUri = "/";
						}
						response.sendRedirect(defaultUri);
					}
					
				} else {
					request.getSession().setAttribute(SessionKey.NAV_ID, 0);
				}
			} 
			
			if (request.getSession().getAttribute(SessionKey.ITEM_ID) == null) {
				request.getSession().setAttribute(SessionKey.ITEM_ID, 0);
			}
			
			if (request.getSession().getAttribute(SessionKey.MENU_ID) == null) {
				request.getSession().setAttribute(SessionKey.MENU_ID, 0);
			}
		}
		logger.info("URI:" + request.getServletPath() + " [" + request.getMethod() + "] Finished");
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception e)
			throws Exception {
		
	}

}
