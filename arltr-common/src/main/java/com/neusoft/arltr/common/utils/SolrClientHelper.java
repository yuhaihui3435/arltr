/**
 * 
 */
package com.neusoft.arltr.common.utils;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * solr客户端工具类
 * 
 * @author zhanghaibo
 *
 */
@Component
public class SolrClientHelper {
	
	@Value("${spring.data.solr.host}")
	private  String solrServerUrl;
	
	@Value("${solr.core.name}")
	private String coreName;
	
	private static final String SYNONYM_MANAGE_URI = "/schema/analysis/synonyms/chinese";
	private static final String DICTIONARY_ADD_URI = "/analyzer/dictionary/words?action=ADD";
	private static final String DICTIONARY_REMOVE_URI = "/analyzer/dictionary/words?action=REMOVE";
	private static final String RELOAD_URI = "/admin/cores?action=RELOAD&core=";

	/**
	 * 保存同义词
	 * 
	 * @param synonym 同义词，格式：{词条 : [同义词1, 同义词2, ...]}
	 */
	public  void saveSynonym(Map<String, Object> synonym) {
		
		HttpClientUtils.doPost(getSolrBaseUrl() + SYNONYM_MANAGE_URI, synonym);
		
		reload();
	}
	
	/**
	 * 删除同义词词条
	 * 
	 * @param words 要删除的同义词
	 */
	public void removeSynonym(String... words) {
		
		for (String word : words) {
			
			String deleteUrl = getSolrBaseUrl() + SYNONYM_MANAGE_URI + "/" + word;
			HttpClientUtils.doDelete(deleteUrl);
		}
		
		reload();
	}
	
	/**
	 * 保存词库词条
	 * 
	 * @param words 词库词条
	 */
	public void saveLexiconWordList(String... words) {
		
		Map<String, Object> wordList = new HashMap<String, Object>();
		
		wordList.put("wordList", words);
		
		HttpClientUtils.doPost(getSolrBaseUrl() + DICTIONARY_ADD_URI, wordList);
	}
	
	/**
	 * 删除词库词条
	 * 
	 * @param words 词库词条
	 */
	public void removeLexiconWordList(String... words) {
		
		Map<String, Object> wordList = new HashMap<String, Object>();
		
		wordList.put("wordList", words);
		
		HttpClientUtils.doPost(getSolrBaseUrl() + DICTIONARY_REMOVE_URI, wordList);
	}
	
	/**
	 * 重启solr
	 */
	public void reload() {
		HttpClientUtils.doPost(solrServerUrl + RELOAD_URI + coreName);
	}
	
	private String getSolrBaseUrl() {
		
		return solrServerUrl + "/" + coreName;
	}
}
