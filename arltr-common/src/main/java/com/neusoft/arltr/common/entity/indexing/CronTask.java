/***********************************************************************
 * Module:  CronTask.java
 * Author:  zhb_1204
 * Purpose: Defines the Class CronTask
 ***********************************************************************/

package com.neusoft.arltr.common.entity.indexing;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

import lombok.Data;

/** 定时任务实体类
 * 
 *  */
@Entity
@Data
public class CronTask {
   /** id */
   @Id 
   @GeneratedValue(strategy=GenerationType.SEQUENCE,generator="indexseq")
   @SequenceGenerator(name="indexseq",sequenceName="INDEX_SEQ",allocationSize=1)
   private Integer id;
   /** 开始时间 */
   private java.util.Date startTime;
   /** 频率 */
   private short frequency;
   /** 创建时间 */
   private java.util.Date createAt;
   /** 创建者 */
   private Integer createBy;
   /** 更新时间 */
   private java.util.Date updateAt;
   /** 更新者 */
   private Integer updateBy;
   /** 创建者名称 */
   private java.lang.String createByName;
   /** 更新者名称 */
   private java.lang.String updateByName;
   /** 采集类型 */
   private short importType;

   /** id */
   public Integer getId() {  
     return id;  
   }
   /** id */
   public void setId(Integer id) {  
      this.id = id;  
   } 
   /** 开始时间 */
   public java.util.Date getStartTime() {
		return startTime;
	}
   /** 开始时间 */
	public void setStartTime(java.util.Date startTime) {
		this.startTime = startTime;
	}
   /** 频率 */
   public short getFrequency() {  
     return frequency;  
   }
   /** 频率 */
   public void setFrequency(short frequency) {  
      this.frequency = frequency;  
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
   /** 创建者名称 */
   public java.lang.String getCreateByName() {  
     return createByName;  
   }
   /** 创建者名称 */
   public void setCreateByName(java.lang.String createByName) {  
      this.createByName = createByName;  
   } 
   /** 更新者名称 */
   public java.lang.String getUpdateByName() {  
     return updateByName;  
   }
   /** 更新者名称 */
   public void setUpdateByName(java.lang.String updateByName) {  
      this.updateByName = updateByName;  
   }
   /** 采集类型 */
	public short getImportType() {
		return importType;
	}
	/** 采集类型 */
	public void setImportType(short importType) {
		this.importType = importType;
	}

}