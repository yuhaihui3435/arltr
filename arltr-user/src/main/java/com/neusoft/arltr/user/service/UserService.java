/**
 * 
 */
package com.neusoft.arltr.user.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.neusoft.arltr.common.entity.user.User;
import com.neusoft.arltr.user.repository.UserRepository;
import com.neusoft.arltr.user.ws.MdmUser;

/**
 *
 *
 */
@Service
public class UserService {

	@Autowired
	UserRepository userRepository;
	
	/**
	 * 转换MDM用户数据并持久化
	 * 
	 * @param mdmUsers MDM用户数据
	 *
	 */
	public void saveMdmUsers(List<MdmUser> mdmUsers) {
		
		Date now = new Date();
		
		for (MdmUser mdmUser : mdmUsers) {
			
			User user = userRepository.findByUserNameOrEmployeeNo(mdmUser.getIdentityNo(), mdmUser.getMdCode());
			
			if (user == null) {
				user = new User();
				user.setCreateAt(now);
			}
			
			user.setUpdateAt(now); // 更新时间
			user.setEmployeeName(mdmUser.getName()); // 姓名
			user.setEmployeeNo(mdmUser.getMdCode()); // 用户主数据编号
			user.setUserName(mdmUser.getIdentityNo()); // 身份证号作为登录用户名
			user.setEmployeeOrg(mdmUser.getDeptCode()); // 组织机构主数据编号
			user.setEmployeeTitle("3"); // 职务
			user.setEmployeeToken(mdmUser.getId()); // id
			user.setSecurityClass(Short.parseShort(mdmUser.getPsnSecretLevelCode())); // 密级
			user.setEndFlag(mdmUser.getEndFlag());  //是否离职
			
			userRepository.save(user);
		}
	}
}
