/**
 * 
 */
package com.neusoft.arltr.search.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.solr.client.solrj.util.ClientUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.solr.core.query.result.HighlightEntry;
import org.springframework.data.solr.core.query.result.HighlightEntry.Highlight;
import org.springframework.data.solr.core.query.result.HighlightPage;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.neusoft.arltr.common.base.RespBody;
import com.neusoft.arltr.common.entity.mask.MaskWord;
import com.neusoft.arltr.common.entity.search.Result;
import com.neusoft.arltr.common.service.MaskService;
import com.neusoft.arltr.common.utils.ChineseToPinyin;
import com.neusoft.arltr.search.repository.SearchRepository;

/**
 * 查询控制器
 * 
 *
 *
 */
@RestController
@RequestMapping("/search")
public class SearchController {

	private static final int QF_ALL = 1;
	private static final int QF_TITLE = 2;
	private static final int QF_AUTHOR = 3;
	private static final int QF_TITLE_OR_AUTHOR = 4;
	private static final int SORT_DEFAULT = 1;
	
	@Autowired
	SearchRepository searchRepository;
	
	@Autowired
	MaskService maskService;
	
	/**
	 * 查询方法
	 * 
	 * @param keyword 关键字
	 * @param classification 查询用户的密级
	 * @param pageNum 页码
	 * @param pageSize 页长
	 * @param queryField 查询字段
	 * @param orderby 排序字段
	 * 
	 * @return 查询结果（高亮）
	 */
	@GetMapping("/query")
	public RespBody<HighlightPage<Result>> query(String keyword, 
			@RequestParam(defaultValue = "60") String classification,
			@RequestParam(defaultValue = "*") String source,
			@RequestParam(defaultValue = "0") Integer pageNum, 
			@RequestParam(defaultValue = "10") Integer pageSize,
			@RequestParam(defaultValue = "1") Integer queryField,
			@RequestParam(defaultValue = "1") Integer orderby) {
		
		keyword = ClientUtils.escapeQueryChars(keyword);
		source = ClientUtils.escapeQueryChars(source);
		Pageable pageable;
		
		if (orderby == SORT_DEFAULT) {
			pageable = new PageRequest(pageNum, pageSize);
		} else {
			Sort sort = new Sort(Direction.DESC, "timestamp");
			pageable = new PageRequest(pageNum, pageSize, sort);
		}
		
		HighlightPage<Result> result = null;
		
		switch (queryField) {
		case QF_ALL:
			
			if (ChineseToPinyin.isStrContainsPinyin(keyword)) {
				result = searchRepository.findByCondition(keyword, keyword, Integer.parseInt(classification), source, pageable);
			} else {
				result = searchRepository.findByConditionWithoutPy(keyword, keyword, Integer.parseInt(classification), source, pageable);
			}
			
			break;
		case QF_TITLE:
			
			if (ChineseToPinyin.isStrContainsPinyin(keyword)) {
				result = searchRepository.findByTitle(keyword, Integer.parseInt(classification), source, pageable);
			} else {
				result = searchRepository.findByTitleWithoutPy(keyword, Integer.parseInt(classification), source, pageable);
			}
			
			break;
		case QF_AUTHOR:
			
			if (ChineseToPinyin.isStrContainsPinyin(keyword)) {
				result = searchRepository.findByAuthor(keyword, Integer.parseInt(classification), source, pageable);
			} else {
				result = searchRepository.findByAuthorWithoutPy(keyword, Integer.parseInt(classification), source, pageable);
			}
			
			break;
		case QF_TITLE_OR_AUTHOR:
			
			if (ChineseToPinyin.isStrContainsPinyin(keyword)) {
				result = searchRepository.findByTitleOrAuthor(keyword, Integer.parseInt(classification), source, pageable);
			} else {
				result = searchRepository.findByTitleOrAuthorWithoutPy(keyword, Integer.parseInt(classification), source, pageable);
			}
			
			break;
		}
		
		// 屏蔽处理
		masking(result);
		
		RespBody<HighlightPage<Result>> resp = new RespBody<HighlightPage<Result>>();
		
		resp.setBody(result);
		
		return resp;
	}
	
	/**
	 * 根据id，查询快照
	 * @author ye.yy
	 * @param id 
	 * @return RespBody<List<Result>> 查询结果
	 */
	@GetMapping("/snapshoot")
	public RespBody<List<Result>> getSnapshoot(String id){
		RespBody<List<Result>> resp = new RespBody<List<Result>>();
		List<Result> resultList = this.searchRepository.findById(id);
		resp.setBody(resultList);
		return resp;
	}
	
	/**
	 * 获取输入提示
	 * 
	 * @param keyword 关键字
	 * @param pageNum 页码
	 * @param pageSize 页长
	 * @return RespBody<List<String>> 提示信息
	 */
	@GetMapping("/suggestion")
	public RespBody<List<String>> getSuggestion(String keyword, 
			@RequestParam(defaultValue = "0") Integer pageNum, 
			@RequestParam(defaultValue = "5") Integer pageSize) {
		
		RespBody<List<String>> resp = new RespBody<List<String>>();
		
		List<Result> resultList = searchRepository.findBySuggest(keyword, new PageRequest(pageNum, pageSize));
		
		List<String> suggestion = new ArrayList<String>();
		
		for (Result result : resultList) {
			suggestion.add(result.getTitle());
		}
		resp.setBody(suggestion);
		return resp;
	}
	
	/**
	 * 查询结果屏蔽处理
	 * 
	 * @param result 查询结果 
	 */
	private void masking(HighlightPage<Result> resultList) {
		
		List<MaskWord> maskWords = maskService.getAll().getBody();
		String mwRegex = "";
		
		for (MaskWord mw : maskWords) {
			if (StringUtils.isNotEmpty(mw.getWord())) {
				mwRegex += mw.getWord() + "|";
			}
		}
		
		mwRegex = mwRegex.substring(0, mwRegex.length() - 1);
		
		Pattern mwPattern = Pattern.compile(mwRegex);
		
		if (resultList.getTotalElements() > 0) {
			List<HighlightEntry<Result>>  highlightList = resultList.getHighlighted();
			
			for (int i = 0; i < highlightList.size(); i++) {
				
				List<Highlight> highlights = highlightList.get(i).getHighlights();
				Result result = highlightList.get(i).getEntity();
				boolean isTitleHighlighted = false;
				boolean isContentHighlighted = false;
				
				for (Highlight highlight : highlights) {
					if ("title".equals(highlight.getField().getName())) {
						
						String title = highlight.getSnipplets().get(0);
						title = mwPattern.matcher(title).replaceAll("XX");
						
						highlight.getSnipplets().set(0, title);
						isTitleHighlighted = true;
					} else if ("content".equals(highlight.getField().getName())) {
						isContentHighlighted = true;
						
						if (!"60".equals(result.getClassification())) {
							highlight.getSnipplets().set(0, "");
						}
					}
				}
				
				if (!isTitleHighlighted) {
					result.setTitle(mwPattern.matcher(result.getTitle()).replaceAll("XX"));
				}
				
				if (!isContentHighlighted) {
					if (!"60".equals(result.getClassification())) {
						result.setContent("");
					}
				}
			}
		}
	}
	
}
