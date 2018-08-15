package com.neusoft.arltr.search.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.solr.core.query.result.HighlightPage;
import org.springframework.data.solr.repository.Boost;
import org.springframework.data.solr.repository.Highlight;
import org.springframework.data.solr.repository.Query;
import org.springframework.data.solr.repository.SolrCrudRepository;

import com.neusoft.arltr.common.entity.search.Result;

public interface SearchRepository extends SolrCrudRepository<Result, String> {
	
	@Query(value="(title:?0 OR content:?1 OR author:?1 OR titlePinyin:*?0* OR titleAbbre:?0* OR authorPinyin:*?1* OR authorAbbre:?1 OR summary:*?0* OR extend:*?0*) AND classification:[* TO ?2] AND source:?3", defType = "edismax")
	@Highlight(prefix = "<em>", postfix = "</em>")
	public HighlightPage<Result> findByCondition(@Boost(2) String title,String content, Integer classification, String source, Pageable pageable);
	
	@Query(value="(title:?0 OR titlePinyin:*?0* OR titleAbbre:?0*) AND classification:[* TO ?1] AND source:?2", defType = "edismax")
	@Highlight(prefix = "<em>", postfix = "</em>")
	public HighlightPage<Result> findByTitle(String kw, Integer classification, String source, Pageable pageable);

	@Query(value="(author:?0 OR authorPinyin:*?0* OR authorAbbre:?0*) AND classification:[* TO ?1] AND source:?2", defType = "edismax")
	@Highlight(prefix = "<em>", postfix = "</em>")
	public HighlightPage<Result> findByAuthor(String kw, Integer classification, String source, Pageable pageable);
	
	@Query(value="(title:?0 OR  author:?0 OR titlePinyin:*?0* OR titleAbbre:?0* OR authorPinyin:*?0* OR authorAbbre:?0) AND classification:[* TO ?1] AND source:?2 ", defType = "edismax")
	@Highlight(prefix = "<em>", postfix = "</em>")
	public HighlightPage<Result> findByTitleOrAuthor(String kw, Integer classification, String source, Pageable pageable);
	
	// 不含拼音查询
	@Query(value="(title:?0 OR content:?1 OR author:?1 OR summary:*?0* OR extend:*?0*) AND classification:[* TO ?2] AND source:?3 ", defType = "edismax")
	@Highlight(prefix = "<em>", postfix = "</em>")
	public HighlightPage<Result> findByConditionWithoutPy(@Boost(2) String title,String content, Integer classification, String source, Pageable pageable);
	
	@Query(value="(title:?0) AND classification:[* TO ?1] AND source:?2", defType = "edismax")
	@Highlight(prefix = "<em>", postfix = "</em>")
	public HighlightPage<Result> findByTitleWithoutPy(String kw, Integer classification, String source, Pageable pageable);

	@Query(value="(author:?0) AND classification:[* TO ?1] AND source:?2", defType = "edismax")
	@Highlight(prefix = "<em>", postfix = "</em>")
	public HighlightPage<Result> findByAuthorWithoutPy(String kw, Integer classification, String source, Pageable pageable);
	
	@Query(value="(title:?0 OR  author:?0) AND classification:[* TO ?1] AND source:?2 ", defType = "edismax")
	@Highlight(prefix = "<em>", postfix = "</em>")
	public HighlightPage<Result> findByTitleOrAuthorWithoutPy(String kw, Integer classification, String source, Pageable pageable);
	
	@Query(value="suggest:*?0*",fields={"title"})
	public List<Result> findBySuggest(String kw, Pageable pageable);
	
	public List<Result> findById(String id);
}
