/**
 * 
 */
package com.neusoft.arltr.common.entity.search;

import java.util.Date;

import org.apache.solr.client.solrj.beans.Field;
import org.springframework.data.annotation.Id;
import org.springframework.data.solr.core.mapping.Indexed;
import org.springframework.data.solr.core.mapping.SolrDocument;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.neusoft.arltr.common.entity.user.EnumerationValueName;

/**
 * @author zhanghaibo
 *
 */
@SolrDocument(solrCoreName = "arltr_core")
public class Result {

	@Id 
	@Indexed
	private String id;
	@Indexed
	@Field
	private String title;
	@Indexed
	@Field
	private String author;
	@Indexed
	@Field
	private String content;
	@Field
	private String url;
	@Field
	private String cache;
	@Field
	private String summary;
	@Indexed
	@Field
	private String contentType;
	@Indexed
	@Field
	private String resourcename;
	@Indexed
	@Field
	private String classification;
	
	@EnumerationValueName(type = "SECURITY")
	private String classificationName;
	
	@Indexed
	@Field
	private String titlePinyin;
	@Indexed
	@Field
	private String titleAbbre;
	@Indexed
	@Field
	private String authorPinyin;
	@Indexed
	@Field
	private String authorAbbre;
	@Field
	private int score;
	@Indexed
	@Field
	private String originalId;
	@Indexed
	@Field
	private Integer source;
	@Indexed
	@Field
	private long timestamp;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Field
	private Date lastModified;
	@Indexed
	@Field
	private String extend;
	
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	/**
	 * @return the author
	 */
	public String getAuthor() {
		return author;
	}
	/**
	 * @param author the author to set
	 */
	public void setAuthor(String author) {
		this.author = author;
	}
	/**
	 * @return the content
	 */
	public String getContent() {
		return content;
	}
	/**
	 * @param content the content to set
	 */
	public void setContent(String content) {
		this.content = content;
	}
	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}
	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}
	/**
	 * @return the cache
	 */
	public String getCache() {
		return cache;
	}
	/**
	 * @param cache the cache to set
	 */
	public void setCache(String cache) {
		this.cache = cache;
	}

	/**
	 * @return the summary
	 */
	public String getSummary() {
		return summary;
	}
	/**
	 * @param summary the summary to set
	 */
	public void setSummary(String summary) {
		this.summary = summary;
	}
	/**
	 * @return the contentType
	 */
	public String getContentType() {
		return contentType;
	}
	/**
	 * @param contentType the contentType to set
	 */
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	/**
	 * @return the resourcename
	 */
	public String getResourcename() {
		return resourcename;
	}
	/**
	 * @param resourcename the resourcename to set
	 */
	public void setResourcename(String resourcename) {
		this.resourcename = resourcename;
	}
	/**
	 * @return the classification
	 */
	public String getClassification() {
		return classification;
	}
	/**
	 * @param classification the classification to set
	 */
	public void setClassification(String classification) {
		this.classification = classification;
	}
	/**
	 * @return the titlePinyin
	 */
	public String getTitlePinyin() {
		return titlePinyin;
	}
	/**
	 * @param titlePinyin the titlePinyin to set
	 */
	public void setTitlePinyin(String titlePinyin) {
		this.titlePinyin = titlePinyin;
	}
	/**
	 * @return the titleAbbre
	 */
	public String getTitleAbbre() {
		return titleAbbre;
	}
	/**
	 * @param titleAbbre the titleAbbre to set
	 */
	public void setTitleAbbre(String titleAbbre) {
		this.titleAbbre = titleAbbre;
	}
	/**
	 * @return the authorPinyin
	 */
	public String getAuthorPinyin() {
		return authorPinyin;
	}
	/**
	 * @param authorPinyin the authorPinyin to set
	 */
	public void setAuthorPinyin(String authorPinyin) {
		this.authorPinyin = authorPinyin;
	}
	/**
	 * @return the authorAbbre
	 */
	public String getAuthorAbbre() {
		return authorAbbre;
	}
	/**
	 * @param authorAbbre the authorAbbre to set
	 */
	public void setAuthorAbbre(String authorAbbre) {
		this.authorAbbre = authorAbbre;
	}
	/**
	 * @return the score
	 */
	public int getScore() {
		return score;
	}
	/**
	 * @param score the score to set
	 */
	public void setScore(int score) {
		this.score = score;
	}
	public String getOriginalId() {
		return originalId;
	}
	public void setOriginalId(String originalId) {
		this.originalId = originalId;
	}
	public Integer getSource() {
		return source;
	}
	public void setSource(Integer source) {
		this.source = source;
	}
	public String getClassificationName() {
		return classificationName;
	}
	public void setClassificationName(String classificationName) {
		this.classificationName = classificationName;
	}
	public long getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
	public Date getLastModified() {
		return new Date(this.timestamp);
	}
	public void setLastModified(Date lastModified) {
		this.lastModified = lastModified;
	}
	public String getExtend() {
		return extend;
	}
	public void setExtend(String extend) {
		this.extend = extend;
	}
	
}
