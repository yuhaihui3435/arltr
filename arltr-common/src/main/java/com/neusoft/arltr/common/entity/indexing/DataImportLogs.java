/***********************************************************************
 * Module:  DataImportLogs.java
 * Author:  zhb_1204
 * Purpose: Defines the Class DataImportLogs
 ***********************************************************************/

package com.neusoft.arltr.common.entity.indexing;

import javax.persistence.*;

import com.neusoft.arltr.common.entity.user.EnumerationValueName;

import lombok.Data;

/** 数据采集记录实体类
 * 
 *  */
@Entity
@Data
public class DataImportLogs {
   /** id */
   @Id 
   @GeneratedValue(strategy=GenerationType.SEQUENCE,generator="indexseq")
   @SequenceGenerator(name="indexseq",sequenceName="INDEX_SEQ",allocationSize=1)
   private Integer id;
   /** 开始时间 */
   private java.util.Date startTime;
   /** 结束时间 */
   private java.util.Date endTime;
   /** 任务类型 */
   private short taskType;
   @Transient
   @EnumerationValueName(type="INDEX_TASK_TYPE")
   /** 任务类型名称 */
   private String taskTypeName;
   /** 采集类型 */
   private short importType;
   @Transient
   @EnumerationValueName(type="INDEX_IMPORT_TYPE")
   /** 采集类型名称 */
   private String importTypeName;
   /** 任务状态 */
   private short taskState;
   @Transient
   @EnumerationValueName(type="INDEX_TASK_STATE")
   /** 任务状态名称 */
   private String taskStateName;
   /** 执行者 */
   private Integer executor;
   /** 任务信息 */
//   @Lob
//   @Basic(fetch = FetchType.LAZY)
//   @Column(columnDefinition = "CLOB")
   private java.lang.String taskInfo;
   /** 执行者名称 */
   private java.lang.String executorName;

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
   /** 结束时间 */
   public java.util.Date getEndTime() {  
     return endTime;  
   }
   /** 结束时间 */
   public void setEndTime(java.util.Date endTime) {  
      this.endTime = endTime;  
   } 
   /** 任务类型 */
   public short getTaskType() {  
     return taskType;  
   }
   /** 任务类型 */
   public void setTaskType(short taskType) {  
      this.taskType = taskType;  
   } 
   /** 采集类型 */
   public short getImportType() {  
     return importType;  
   }
   /** 采集类型 */
   public void setImportType(short importType) {  
      this.importType = importType;  
   } 
   /** 任务状态 */
   public short getTaskState() {  
     return taskState;  
   }
   /** 任务状态 */
   public void setTaskState(short taskState) {  
      this.taskState = taskState;  
   } 
   /** 执行者 */
   public Integer getExecutor() {  
     return executor;  
   }
   /** 执行者 */
   public void setExecutor(Integer executor) {  
      this.executor = executor;  
   } 
   /** 任务信息 */
   public java.lang.String getTaskInfo() {  
     return taskInfo;  
   }
   /** 任务信息 */
   public void setTaskInfo(java.lang.String taskInfo) {  
      this.taskInfo = taskInfo;  
   } 
   /** 执行者名称 */
   public java.lang.String getExecutorName() {  
     return executorName;  
   }
   /** 执行者名称 */
   public void setExecutorName(java.lang.String executorName) {  
      this.executorName = executorName;  
   }
   /** 任务类型名称 */
	public String getTaskTypeName() {
		return taskTypeName;
	}
	/** 任务类型名称 */
	public void setTaskTypeName(String taskTypeName) {
		this.taskTypeName = taskTypeName;
	}
	/** 采集类型名称 */
	public String getImportTypeName() {
		return importTypeName;
	}
	/** 采集类型名称 */
	public void setImportTypeName(String importTypeName) {
		this.importTypeName = importTypeName;
	}
	/** 任务状态名称 */
	public String getTaskStateName() {
		return taskStateName;
	}
	/** 任务状态名称 */
	public void setTaskStateName(String taskStateName) {
		this.taskStateName = taskStateName;
	}

}