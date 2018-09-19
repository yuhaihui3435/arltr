/***********************************************************************
 * Module:  Lexicon.java
 * Author:  zhb_1204
 * Purpose: Defines the Class Lexicon
 ***********************************************************************/

package com.neusoft.arltr.common.entity.lexicon;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

import lombok.Data;

/** 航天词库实体类
 * 
 *  */
@Entity
@Data
public class Lexicon {
   /** id */
   @Id 
   @GeneratedValue(strategy=GenerationType.SEQUENCE,generator="lexiconseq")
   @SequenceGenerator(name="lexiconseq",sequenceName="LEXICON_SEQ",allocationSize=1)
   private Integer id;
   /** 词条 */
   private java.lang.String word;
   /** 创建时间 */
   private java.util.Date createAt;
   /** 创建者 */
   private Integer createBy;
   /** 创建者名称 */
   private java.lang.String createByName;
   /** 更新时间 */
   private java.util.Date updateAt;
   /** 更新者 */
   private Integer updateBy;
   /** 更新者名称 */
   private java.lang.String updateByName;

   /** id */
   public Integer getId() {  
     return id;  
   }
   /** id */
   public void setId(Integer id) {  
      this.id = id;  
   } 
   /** 词条 */
   public java.lang.String getWord() {  
     return word;  
   }
   /** 词条 */
   public void setWord(java.lang.String word) {  
      this.word = word;  
   } 
   /** 创建时间 */
   public java.util.Date getCreateAt() {  
     return createAt;  
   }
   /** 创建时间 */
   public void setCreateAt(java.util.Date createAt) {  
      this.createAt = createAt;  
   } 
   /** 创建者 */
   public Integer getCreateBy() {  
     return createBy;  
   }
   /** 创建者 */
   public void setCreateBy(Integer createBy) {  
      this.createBy = createBy;  
   } 
   /** 创建者名称 */
   public java.lang.String getCreateByName() {  
     return createByName;  
   }
   /** 创建者名称 */
   public void setCreateByName(java.lang.String createByName) {  
      this.createByName = createByName;  
   } 
   /** 更新时间 */
   public java.util.Date getUpdateAt() {  
     return updateAt;  
   }
   /** 更新时间 */
   public void setUpdateAt(java.util.Date updateAt) {  
      this.updateAt = updateAt;  
   } 
   /** 更新者 */
   public Integer getUpdateBy() {  
     return updateBy;  
   }
   /** 更新者 */
   public void setUpdateBy(Integer updateBy) {  
      this.updateBy = updateBy;  
   } 
   /** 更新者名称 */
   public java.lang.String getUpdateByName() {  
     return updateByName;  
   }
   /** 更新者名称 */
   public void setUpdateByName(java.lang.String updateByName) {  
      this.updateByName = updateByName;  
   }

}