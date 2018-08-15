package com.neusoft.arltr.synonym.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import com.neusoft.arltr.common.entity.synonym.Synonym;

/**
 * 同义词数据仓库
 * @author wuxl
 *
 */
public interface SynonymRepository extends CrudRepository<Synonym, Integer>,JpaSpecificationExecutor<Synonym> {

}
