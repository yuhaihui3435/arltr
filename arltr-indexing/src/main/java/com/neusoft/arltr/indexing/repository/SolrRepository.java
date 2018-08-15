package com.neusoft.arltr.indexing.repository;

import org.springframework.data.solr.repository.SolrCrudRepository;

import com.neusoft.arltr.common.entity.search.Result;

/**
 * Solr仓库
 * 
 * @author zhanghaibo
 */
public interface SolrRepository extends SolrCrudRepository<Result, String> {
	
	public Result findByOriginalIdAndSource(String originalId, Integer source);
}
