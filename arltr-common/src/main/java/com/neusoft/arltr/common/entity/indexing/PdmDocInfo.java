/***********************************************************************
 * Module:  PdmDocInfo.java
 * Author:  zhb_1204
 * Purpose: Defines the Class PdmDocInfo
 ***********************************************************************/

package com.neusoft.arltr.common.entity.indexing;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Data;

/** PDM文档信息表实体类
 * 
 * @author zhanghaibo */
@Entity
@Data
@Table(name ="PDM_DOC_INFO")
public class PdmDocInfo {
   @Id 
   @GeneratedValue(strategy=GenerationType.SEQUENCE,generator="pdiseq")
   @SequenceGenerator(name="pdiseq",sequenceName="PDI_SEQ",allocationSize=1)
   private  Integer id;
   /** 文档标识 */
   private  java.lang.String documentId;
   /** 文档标题 */
   private  java.lang.String documentTitle;
   /** 文档添加时间 */
   private  java.lang.String documentDate;
   /** 文档作者 */
   private  java.lang.String author;
   /** 文档密级 */
   private  java.lang.String secretLevel;
   /** 文档版本号 */
   private  java.lang.String versionSno;
   /** 详情URL */
   private  java.lang.String url;
   /** 文件全路径 */
   private  java.lang.String filePath;
   /** 文档类型 */
   private String fileType;
   /** 创建时间 */
   private  java.util.Date createAt;
   /** 数据状态（0：未处理，1：已获取文件路径，2：已建立索引） */
   private  Integer dataState;
   /** 文档上下文 */
   private String documentCon;
   /** 文档阶段标记 */
   private String documentPhase;
   /** 文档小类 */
   private String fileSmallType;
   /** 文档专业方向 */
   private String majorOri;
   /** 文档专业类别 */
   private String majorType;
   /** 文档编制单位 */
   private String documentDept;
   /** 关键词 */
   private String keyWord;
   /** 摘要 */
   private String digest;
   /** 责任单位 */
   private String documentResponDept;

   /** id */
   public Integer getId() {
      return id;
   }   
   /** id */
   public void setId(Integer id) {
      this.id = id;
   }
   /** 文档标识 */
   public java.lang.String getDocumentId() {
      return documentId;
   }   
   /** 文档标识 */
   public void setDocumentId(java.lang.String documentId) {
      this.documentId = documentId;
   }
   /** 文档标题 */
   public java.lang.String getDocumentTitle() {
      return documentTitle;
   }   
   /** 文档标题 */
   public void setDocumentTitle(java.lang.String documentTitle) {
      this.documentTitle = documentTitle;
   }
   /** 文档添加时间 */
   public java.lang.String getDocumentDate() {
      return documentDate;
   }   
   /** 文档添加时间 */
   public void setDocumentDate(java.lang.String documentDate) {
      this.documentDate = documentDate;
   }
   /** 文档作者 */
   public java.lang.String getAuthor() {
      return author;
   }   
   /** 文档作者 */
   public void setAuthor(java.lang.String author) {
      this.author = author;
   }
   /** 文档密级 */
   public java.lang.String getSecretLevel() {
      return secretLevel;
   }   
   /** 文档密级 */
   public void setSecretLevel(java.lang.String secretLevel) {
      this.secretLevel = secretLevel;
   }
   /** 文档版本号 */
   public java.lang.String getVersionSno() {
      return versionSno;
   }   
   /** 文档版本号 */
   public void setVersionSno(java.lang.String versionSno) {
      this.versionSno = versionSno;
   }
   /** 详情URL */
   public java.lang.String getUrl() {
      return url;
   }   
   /** 详情URL */
   public void setUrl(java.lang.String url) {
      this.url = url;
   }
   /** 文件全路径 */
   public java.lang.String getFilePath() {
      return filePath;
   }   
   /** 文件全路径 */
   public void setFilePath(java.lang.String filePath) {
      this.filePath = filePath;
   }
   /** 创建时间 */
   public java.util.Date getCreateAt() {
      return createAt;
   }   
   /** 创建时间 */
   public void setCreateAt(java.util.Date createAt) {
      this.createAt = createAt;
   }
   /** 数据状态 */
   public Integer getDataState() {
      return dataState;
   }   
   /** 数据状态 */
   public void setDataState(Integer dataState) {
      this.dataState = dataState;
   }
	/**
	 * @return the fileType
	 */
	public String getFileType() {
		return fileType;
	}
	/**
	 * @param fileType the fileType to set
	 */
	public void setFileType(String fileType) {
		this.fileType = fileType;
	}
	/** 文档上下文 */
	public String getDocumentCon() {
		return documentCon;
	}
	/** 文档上下文 */
	public void setDocumentCon(String documentCon) {
		this.documentCon = documentCon;
	}
	/** 文档阶段标记 */
	public String getDocumentPhase() {
		return documentPhase;
	}
	/** 文档阶段标记 */
	public void setDocumentPhase(String documentPhase) {
		this.documentPhase = documentPhase;
	}
	/** 文档小类 */
	public String getFileSmallType() {
		return fileSmallType;
	}
	/** 文档小类 */
	public void setFileSmallType(String fileSmallType) {
		this.fileSmallType = fileSmallType;
	}
	/** 文档专业方向 */
	public String getMajorOri() {
		return majorOri;
	}
	/** 文档专业方向 */
	public void setMajorOri(String majorOri) {
		this.majorOri = majorOri;
	}
	/** 文档专业类别 */
	public String getMajorType() {
		return majorType;
	}
	/** 文档专业类别 */
	public void setMajorType(String majorType) {
		this.majorType = majorType;
	}
	/** 文档编制单位 */
	public String getDocumentDept() {
		return documentDept;
	}
	/** 文档编制单位 */
	public void setDocumentDept(String documentDept) {
		this.documentDept = documentDept;
	}
	/** 关键词 */
	public String getKeyWord() {
		return keyWord;
	}
	/** 关键词 */
	public void setKeyWord(String keyWord) {
		this.keyWord = keyWord;
	}
	/** 摘要 */
	public String getDigest() {
		return digest;
	}
	/** 摘要 */
	public void setDigest(String digest) {
		this.digest = digest;
	}
	/** 责任单位 */
	public String getDocumentResponDept() {
		return documentResponDept;
	}
	/** 责任单位 */
	public void setDocumentResponDept(String documentResponDept) {
		this.documentResponDept = documentResponDept;
	}
	
	
}