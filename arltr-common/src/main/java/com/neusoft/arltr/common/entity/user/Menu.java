/***********************************************************************
 * Module:  Menu.java
 * Author:  zhb_1204
 * Purpose: Defines the Class Menu
 ***********************************************************************/

package com.neusoft.arltr.common.entity.user;

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
import javax.persistence.Transient;

import lombok.Data;

/** 菜单实体类
 * 
 *  */
@Entity
@Data
public class Menu {
	
//	@ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
//	@JoinTable(name = "role_menu",
//	joinColumns = {@JoinColumn(name = "menu_id", referencedColumnName = "id")},
//	inverseJoinColumns = {@JoinColumn(name = "role_id", referencedColumnName ="id")})
//	private Set<Role> roles = new HashSet<Role>();
	
   /** id */
   @Id 
   @GeneratedValue(strategy=GenerationType.SEQUENCE,generator="userseq")
   @SequenceGenerator(name="userseq",sequenceName="USER_SEQ",allocationSize=1)
   private Integer id;
   /** 菜单名 */
   private java.lang.String name;
   /** 菜单地址 */
   private java.lang.String uri;
   /** 图标 */
   private java.lang.String icon;
   /** 显示序号 */
   private Integer dispNo;
   /** 父菜单id */
   @Column(name="parent_id")
   private Integer parentId;
   
   @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY,orphanRemoval=true)
	@JoinColumn(name="parent_id")
	private List<Menu> children = new ArrayList<Menu>();
   @Transient
   private boolean selected;
   
//   /** 用户角色 */
//	public Set<Role> getRoles() {
//		return roles;
//	}
//
//	/** 用户角色 */
//	public void setRoles(Set<Role> roles) {
//		this.roles = roles;
//	}
	
   public boolean isSelected() {
	return selected;
}
public void setSelected(boolean selected) {
	this.selected = selected;
}
public List<Menu> getChildren() {
	return children;
}
public void setChildren(List<Menu> children) {
	this.children = children;
}
/** id */
   public Integer getId() {
      return id;
   }
      /** id */
   public void setId(Integer id) {
      this.id = id;
   }
   /** 菜单名 */
   public java.lang.String getName() {
      return name;
   }
      /** 菜单名 */
   public void setName(java.lang.String name) {
      this.name = name;
   }
   /** 菜单地址 */
   public java.lang.String getUri() {
      return uri;
   }
      /** 菜单地址 */
   public void setUri(java.lang.String uri) {
      this.uri = uri;
   }
   /** 图标 */
   public java.lang.String getIcon() {
      return icon;
   }
      /** 图标 */
   public void setIcon(java.lang.String icon) {
      this.icon = icon;
   }
   /** 显示序号 */
   public Integer getDispNo() {
      return dispNo;
   }
      /** 显示序号 */
   public void setDispNo(Integer dispNo) {
      this.dispNo = dispNo;
   }
   /** 父菜单id */
   public Integer getParentId() {
      return parentId;
   }
      /** 父菜单id */
   public void setParentId(Integer parentId) {
      this.parentId = parentId;
   }

}