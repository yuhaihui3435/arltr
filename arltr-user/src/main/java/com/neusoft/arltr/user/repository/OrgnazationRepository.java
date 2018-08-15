package com.neusoft.arltr.user.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import com.neusoft.arltr.common.entity.user.Orgnazation;

public interface OrgnazationRepository extends CrudRepository<Orgnazation, Integer>,JpaSpecificationExecutor<Orgnazation>{

	public Orgnazation findByCode(String code);
	
	public List<Orgnazation> findByParentOrgCodeOrderByOrgOrder(String parentOrgCode);
}
