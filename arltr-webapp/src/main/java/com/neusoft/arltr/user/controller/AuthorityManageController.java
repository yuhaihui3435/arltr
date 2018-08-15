package com.neusoft.arltr.user.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.neusoft.arltr.common.base.RespBody;
import com.neusoft.arltr.common.entity.user.SysAuth;
import com.neusoft.arltr.common.entity.user.User;
import com.neusoft.arltr.common.service.SysService;

/**
 *  访问权限管理Controller
 * @author chifch
 *
 */
@Controller
public class AuthorityManageController {
@Autowired
private SysService sysService;
/**
 * 页面入口
 * @return
 */
@GetMapping("/user/sysauth/list")
public String getpath(){
	return "user/authority_manage";
}
/**
 * 查看访问权限列表
 * @return
 */
@GetMapping("/user/sysauth/sysauthlist")
@ResponseBody
public List<SysAuth> getsysauth(){
	return sysService.getSysAuth().getBody();

}
/**
 * 根据权限Id查看有该权限的人
 * @param id 权限id
 * @return
 */
@PostMapping("/user/sysauth/usersysauth")
@ResponseBody
public RespBody<List<User>> getUserSysAuth(@RequestParam(value="id") Integer id){
	return sysService.getUserSysAuth(id);
}
/**
 * 保存访问权限方法
 * @param id 权限id
 * @param list 人员id集合
 * @return
 */
@PostMapping("/user/sysauth/save")
@ResponseBody
public RespBody<Object> save(@RequestParam(value="id") Integer id,@RequestParam(value="useridlist") List<Integer> list){
	return sysService.save(id, list);
}
//@PostMapping("user/sysauth/sysauthall")
//@ResponseBody
//public RespBody<List<SysAuth>> gettest(@RequestParam(value="id") Integer id){
//	return sysService.getAll(id);
//}
}
