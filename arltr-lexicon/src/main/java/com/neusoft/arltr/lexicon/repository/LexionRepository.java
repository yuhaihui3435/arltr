package com.neusoft.arltr.lexicon.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import com.neusoft.arltr.common.entity.lexicon.Lexicon;

/**
 * 词库管理数据仓库接口
 * 
 * @author lishuang
 *
 */
public interface LexionRepository extends CrudRepository<Lexicon, Integer> ,JpaSpecificationExecutor<Lexicon>{

}
