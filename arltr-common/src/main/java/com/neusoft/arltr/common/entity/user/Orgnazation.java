package com.neusoft.arltr.common.entity.user;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;

import org.hibernate.annotations.Where;

import lombok.Data;

/** 菜单实体类
 * 
 * @author lishuang */
@Entity
@Data
public class Orgnazation implements Serializable{
	
	 /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** id */
	   @Id 
	   @GeneratedValue(strategy=GenerationType.SEQUENCE,generator="userseq")
	   @SequenceGenerator(name="userseq",sequenceName="USER_SEQ",allocationSize=1)
	   private Integer id;
	   /** 部门主数据编号 */
	   private java.lang.String code;
	   /** 名称 */
	   private java.lang.String name;
	   /** 顺序 */
	   private Integer orgOrder;
	   /** 部门编号 */
	   @Column(name = "org_code")
	   private String orgCode;
	   /** 上级部门编号 */
	   @Column(name = "parent_org_code")
	   private String parentOrgCode = "99999";
	   
	   @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	   @JoinColumn(name="parent_org_code" ,referencedColumnName="org_code")
	   @Where(clause = "fc_flag <> '1' or fc_flag is null")
	   private List<Orgnazation> children = new ArrayList<Orgnazation>();
	   /**是否封存(0-显示,1-封存)*/
	   private String fcFlag;
	   
	   /** id */
		public Integer getId() {
			return id;
		}
		/** id */
		public void setId(Integer id) {
			this.id = id;
		}
		/** 部门主数据编号 */
		public java.lang.String getCode() {
			return code;
		}
		/** 部门主数据编号 */
		public void setCode(java.lang.String code) {
			this.code = code;
		}
		
		/** 名称 */
		public java.lang.String getName() {
			return name;
		}
		/** 名称 */
		public void setName(java.lang.String name) {
			this.name = name;
		}
		
		/** 顺序 */
		public Integer getOrgOrder() {
			return orgOrder;
		}
		/** 顺序 */
		public void setOrgOrder(Integer orgOrder) {
			this.orgOrder = orgOrder;
		}
		
		/**
		 * @return the children
		 */
		public List<Orgnazation> getChildren() {
			return children;
		}

		/**
		 * @param children the children to set
		 */
		public void setChildren(List<Orgnazation> children) {
			this.children = children;
		}
		/** 部门编号 */
		public String getOrgCode() {
			return orgCode;
		}
		/** 部门编号 */
		public void setOrgCode(String orgCode) {
			this.orgCode = orgCode;
		}
		/** 上级部门编号 */
		public String getParentOrgCode() {
			return parentOrgCode;
		}
		/** 上级部门编号 */
		public void setParentOrgCode(String parentOrgCode) {
			this.parentOrgCode = parentOrgCode;
		}
		/**是否封存(0-显示,1-封存)*/
		public String getFcFlag() {
			return fcFlag;
		}
		/**是否封存(0-显示,1-封存)*/
		public void setFcFlag(String fcFlag) {
			this.fcFlag = fcFlag;
		}
		
}
