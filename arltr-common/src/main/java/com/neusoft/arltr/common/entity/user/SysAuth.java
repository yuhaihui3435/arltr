/***********************************************************************
 * Module:  SysAuth.java
 * Author:  zhb_1204
 * Purpose: Defines the Class SysAuth
 ***********************************************************************/

package com.neusoft.arltr.common.entity.user;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Transient;

import lombok.Data;

/** 系统权限实体类
 * 
 * @author zhanghaibo */
@Entity
@Data
public class SysAuth {
   /** id */
   @Id 
   @GeneratedValue(strategy=GenerationType.SEQUENCE,generator="userseq")
   @SequenceGenerator(name="userseq",sequenceName="USER_SEQ",allocationSize=1)
   private Integer id;
   /** 系统权限名称 */
   private java.lang.String name;
   @Transient
   private Integer parentId;
   @Transient
   private List<SysAuth> children = new ArrayList<SysAuth>();

   /** id */
   public Integer getId() {
      return id;
   }
      /** id */
   public void setId(Integer id) {
      this.id = id;
   }
   /** 系统权限名称 */
   public java.lang.String getName() {
      return name;
   }
      /** 系统权限名称 */
   public void setName(java.lang.String name) {
      this.name = name;
   }
	public Integer getParentId() {
		return parentId;
	}
	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}
	public List<SysAuth> getChildren() {
		return children;
	}
	public void setChildren(List<SysAuth> children) {
		this.children = children;
	}

}