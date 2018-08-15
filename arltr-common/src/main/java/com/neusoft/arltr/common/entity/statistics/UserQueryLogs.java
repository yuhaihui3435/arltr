/***********************************************************************
 * Module:  UserQueryLogs.java
 * Author:  zhb_1204
 * Purpose: Defines the Class UserQueryLogs
 ***********************************************************************/

package com.neusoft.arltr.common.entity.statistics;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Transient;

import lombok.Data;

/**
 * 用户查询记录实体类
 * 
 * @author zhanghaibo
 */
@Entity
@Data
public class UserQueryLogs {
	/** id */
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="statisticsseq")
	@SequenceGenerator(name="statisticsseq",sequenceName="STATISTICS_SEQ",allocationSize=1)
	private Integer id;
	/** 查询关键词 */
	private java.lang.String keyWord;
	/** 查询关键词全拼 */
	private java.lang.String keyWordPinyin;
	/** 查询关键词缩写 */
	private java.lang.String keyWordAbbre;
	/** 查询时间 */
	private java.util.Date queryAt;
	/** 用户id */
	private Integer userId;
	/** 用户名 */
	private java.lang.String userName;
	/** 搜索次数 */
	@Transient
	private Integer sarchNum;

	/** id */
	public Integer getId() {
		return id;
	}

	/** id */
	public void setId(Integer id) {
		this.id = id;
	}

	/** 查询关键词 */
	public java.lang.String getKeyWord() {
		return keyWord;
	}

	/** 查询关键词 */
	public void setKeyWord(java.lang.String keyWord) {
		this.keyWord = keyWord;
	}

	/** 查询关键词全拼 */
	public java.lang.String getKeyWordPinyin() {
		return keyWordPinyin;
	}

	/** 查询关键词全拼 */
	public void setKeyWordPinyin(java.lang.String keyWordPinyin) {
		this.keyWordPinyin = keyWordPinyin;
	}

	/** 查询关键词缩写 */
	public java.lang.String getKeyWordAbbre() {
		return keyWordAbbre;
	}

	/** 查询关键词缩写 */
	public void setKeyWordAbbre(java.lang.String keyWordAbbre) {
		this.keyWordAbbre = keyWordAbbre;
	}

	/** 查询时间 */
	public java.util.Date getQueryAt() {
		return queryAt;
	}

	/** 查询时间 */
	public void setQueryAt(java.util.Date queryAt) {
		this.queryAt = queryAt;
	}

	/** 用户id */
	public Integer getUserId() {
		return userId;
	}

	/** 用户id */
	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	/** 用户名 */
	public java.lang.String getUserName() {
		return userName;
	}

	/** 用户名 */
	public void setUserName(java.lang.String userName) {
		this.userName = userName;
	}

	/** 搜索次数 */
	public Integer getSarchNum() {
		return sarchNum;
	}

	/** 搜索次数 */
	public void setSarchNum(Integer sarchNum) {
		this.sarchNum = sarchNum;
	}

}