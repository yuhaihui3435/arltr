/**
 * 
 */
package com.neusoft.arltr.user.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.neusoft.arltr.common.entity.user.Orgnazation;
import com.neusoft.arltr.user.repository.OrgnazationRepository;
import com.neusoft.arltr.user.ws.MdmOrganization;

/**
 *
 *
 */
@Service
public class OrgService {

	@Autowired
	OrgnazationRepository orgnazationRepository;
	
	/**
	 * 转换MDM组织机构数据并持久化
	 * 
	 * @param mdmOrgs MDM组织机构数据
	 *
	 */
	public void saveMdmOrgs(List<MdmOrganization> mdmOrgs) {
		
		Date now = new Date();
		
		for (MdmOrganization mdmOrg : mdmOrgs) {
			
			Orgnazation org = orgnazationRepository.findByCode(mdmOrg.getMdCode());
			
			if (org == null) {
				org = new Orgnazation();
			}
			
			org.setCode(mdmOrg.getMdCode()); // 主数据编码
			org.setName(mdmOrg.getOrgName()); // 名称
			org.setOrgCode(mdmOrg.getOrgCode()); // 部门编码
			org.setOrgOrder(1);
			org.setFcFlag(mdmOrg.getFcFlag()); //是否封存
			
			orgnazationRepository.save(org);
		}
	}
}
