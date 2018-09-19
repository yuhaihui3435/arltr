/***********************************************************************
 * Module:  Role.java
 * Author:  zhb_1204
 * Purpose: Defines the Class Role
 ***********************************************************************/

package com.neusoft.arltr.common.entity.user;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Transient;

import lombok.Data;

/** 角色实体类
 * 
 *  */
@Entity
@Data
public class Role {
   /** id */
   @Id 
   @GeneratedValue(strategy=GenerationType.SEQUENCE,generator="userseq")
   @SequenceGenerator(name="userseq",sequenceName="USER_SEQ",allocationSize=1)
   private Integer id;
   /** 角色名 */
   private java.lang.String name;
   @Transient
   private Integer parentId;
   @Transient
   private List<Role> children = new ArrayList<Role>();
   /** 角色对应菜单列表*/
   @ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
   @JoinTable(name = "role_menu", joinColumns = {
			@JoinColumn(name = "role_id", referencedColumnName = "id") }, inverseJoinColumns = {
					@JoinColumn(name = "menu_id", referencedColumnName = "id") })
   private Set<Menu> menus = new HashSet<Menu>();
   /** 角色对应菜单列表*/
   public Set<Menu> getMenus() {
	return menus;
   }
   /** 角色对应菜单列表*/
	public void setMenus(Set<Menu> menus) {
		this.menus = menus;
	}
	/** id */
   public Integer getId() {
      return id;
   }
      /** id */
   public void setId(Integer id) {
      this.id = id;
   }
   /** 角色名 */
   public java.lang.String getName() {
      return name;
   }
      /** 角色名 */
   public void setName(java.lang.String name) {
      this.name = name;
   }
   
   /** id */
   public Integer getParentId() {
      return parentId;
   }
      /** id */
   public void setParentId(Integer parentId) {
      this.parentId = parentId;
   }
   
   /**
	 * @return the children
	 */
	public List<Role> getChildren() {
		return children;
	}

	/**
	 * @param children the children to set
	 */
	public void setChildren(List<Role> children) {
		this.children = children;
	}

}