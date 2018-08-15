/***********************************************************************
 * Module:  UserViewLogs.java
 * Author:  zhb_1204
 * Purpose: Defines the Class UserViewLogs
 ***********************************************************************/

package com.neusoft.arltr.common.entity.statistics;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Transient;

import lombok.Data;

/** 用户查看记录实体类
 * 
 * @author zhanghaibo */
@Entity
@Data
public class UserViewLogs {
   /** id */
   @Id 
   @GeneratedValue(strategy=GenerationType.SEQUENCE,generator="statisticsseq")
   @SequenceGenerator(name="statisticsseq",sequenceName="STATISTICS_SEQ",allocationSize=1)
   private Integer id;
   /** 文档位置 */
   private java.lang.String docLocation;
   /** 文档标题 */
   private java.lang.String docTitle;
   /** 查询关键词 */
   private java.lang.String queryKeyWord;
   /** 查看时间 */
   private java.util.Date viewAt;
   /** 用户id */
   private Integer userId;
   /** 用户名 */
   private java.lang.String userName;
   /** 查询计数列    */
   @Transient
   private Integer querycount;


   /** id */
   public Integer getId() {  
     return id;  
   }
   /** id */
   public void setId(Integer id) {  
      this.id = id;  
   } 
   /** 文档位置 */
   public java.lang.String getDocLocation() {  
     return docLocation;  
   }
   /** 文档位置 */
   public void setDocLocation(java.lang.String docLocation) {  
      this.docLocation = docLocation;  
   } 
   /** 文档标题 */
   public java.lang.String getDocTitle() {  
     return docTitle;  
   }
   /** 文档标题 */
   public void setDocTitle(java.lang.String docTitle) {  
      this.docTitle = docTitle;  
   } 
   /** 查询关键词 */
   public java.lang.String getQueryKeyWord() {  
     return queryKeyWord;  
   }
   /** 查询关键词 */
   public void setQueryKeyWord(java.lang.String queryKeyWord) {  
      this.queryKeyWord = queryKeyWord;  
   } 
   /** 查看时间 */
   public java.util.Date getViewAt() {  
     return viewAt;  
   }
   /** 查看时间 */
   public void setViewAt(java.util.Date viewAt) {  
      this.viewAt = viewAt;  
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
   /** 查询计数列    */
   public Integer getQuerycount() {
		return querycount;
	}
   /** 查询计数列    */
	public void setQuerycount(Integer querycount) {
		this.querycount = querycount;
	}
}