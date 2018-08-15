package com.neusoft.arltr.user.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.neusoft.arltr.common.base.RespBody;
import com.neusoft.arltr.common.entity.user.SysAuth;
import com.neusoft.arltr.common.entity.user.User;
import com.neusoft.arltr.user.repository.SysAuthRepository;
import com.neusoft.arltr.user.repository.UserRepository;

@RestController
@RequestMapping("/user/sysauth")
public class SysAuthController {
	
//	private static final Logger logger = Logger.getLogger(SysAuthController.class.getName());
	
	@Autowired
	private SysAuthRepository sysauthRepository;
	@Autowired
	private UserRepository userRepository;
	
	@Value("${ws.tdm.url}")
	private String tdmWsUrl;
	
	@Value("${ws.tdm.method}")
	private String tdmWsMethod;
	
//	private static final int TDM = 1;

	/**
	 * 获取访问权限列表
	 * 
	 * @return
	 */
	@PostMapping("/getsysauth")
	public RespBody<List<SysAuth>> getSysAuth() {
		RespBody<List<SysAuth>> rep = new RespBody<List<SysAuth>>();
		List<SysAuth> list = (List<SysAuth>) sysauthRepository.findAll();
		rep.setBody(list);
		return rep;
	}

	/**
	 * 查看具有该权限的人
	 * 
	 * @param id
	 * @return
	 */
	@PostMapping("/usersysauth")
	public RespBody<List<User>> getUserSysAuth(@RequestParam(value = "id") Integer id) {
		RespBody<List<User>> rep = new RespBody<List<User>>();
		List<User> list = userRepository.getUserSysAuth(id);
		rep.setBody(list);
		return rep;
	}

	/**
	 * 访问权限保存方法
	 * 
	 * @param id
	 *            权限ID
	 * @param list
	 *            人员id集合
	 * @return
	 */
	@PostMapping("/save")
	@Transactional
	public RespBody<Object> save(@RequestParam(value = "id") Integer id,
			@RequestParam(value = "useridlist", defaultValue = "0") List<Integer> list) {
		RespBody<Object> rep = new RespBody<Object>();
		sysauthRepository.deleteBySysId(id);
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i) != 0) {
				sysauthRepository.insert(list.get(i), id);
			}
		}
		return rep;
	}

	/**
	 * 根据用户id 返回用户具有的所有权限
	 * 
	 * @param id
	 *            用户id
	 * @return
	 */
	@PostMapping("/sysauthall")
	public RespBody<List<SysAuth>> getAll(@RequestParam(value = "id") Integer id) {
		RespBody<List<SysAuth>> rep = new RespBody<List<SysAuth>>();
		List<SysAuth> list = new ArrayList<SysAuth>();
		list = sysauthRepository.getAll(id);
		rep.setBody(list);
		return rep;
	}
	
	/**
	 * 判断用户是否被授予访问权
	 * 
	 * @param source 访问资源id
	 * @param originalId 源系统id
	 * @param user 当前用户
	 * 
	 * @return true/false
	 */
	@PostMapping("/verification")
	public RespBody<Boolean> isUserAuthorized(Integer source, String originalId
			, @RequestBody User user
			) {
		
		RespBody<Boolean> resp = new RespBody<Boolean>();
		boolean isUserAuthorized = false;
		
		// 内部权限验证
		for (SysAuth sa : user.getSysAuths()) {
			if (sa.getId() == source) {
				isUserAuthorized = true;
				break;
			}
		}
		
//		if (isUserAuthorized) {
//			// 外部权限验证
//			switch (source) {
//			case TDM :
//				JaxWsDynamicClientFactory dcf = JaxWsDynamicClientFactory.newInstance();
//				Client client = dcf.createClient(tdmWsUrl);
//				
//				try {
//					Object[] res = client.invoke(tdmWsMethod, "123", originalId);
//					
//					if ("0".equals(res[0])) {
//						isUserAuthorized = false;
//					}
//				} catch (Exception e) {
//					logger.error(e);
//					throw new BusinessException("调用TDM权限验证接口出错");
//				}
//				break;
//			}
//		}
		
		resp.setBody(isUserAuthorized);
		return resp;
	}

}
