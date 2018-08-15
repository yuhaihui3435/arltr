/***********************************************************************
 * Module:  User.java
 * Author:  zhb_1204
 * Purpose: Defines the Class User
 ***********************************************************************/

package com.neusoft.arltr.common.entity.user;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.Data;

/**
 * 用户实体类
 * 
 * @author zhanghaibo
 */
@Entity
@Data
@Table(name = "USER_T")
public class User {

	@ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
	@JoinTable(name = "user_role",
	joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
	inverseJoinColumns = {@JoinColumn(name = "role_id", referencedColumnName ="id")})
	private Set<Role> roles = new HashSet<Role>();
	
	@ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
	@JoinTable(name = "user_sys_auth",
	joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
	inverseJoinColumns = {@JoinColumn(name = "sys_auth_id", referencedColumnName ="id")})
	private Set<SysAuth> sysAuths = new HashSet<SysAuth>();
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "employee_org", referencedColumnName = "code", insertable = false, updatable = false)
	private Orgnazation orgnazation;
	
	
	/** id */
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="userseq")
	@SequenceGenerator(name="userseq",sequenceName="USER_SEQ",allocationSize=1)
	private Integer id;
	/** 登录账号 */
	private java.lang.String userName;
	/** 职工编号 */
	private java.lang.String employeeNo;
	/** 职工姓名 */
	private java.lang.String employeeName;
	/** 职务 */
	private java.lang.String employeeTitle;
	@Transient
	@EnumerationValueName(type="TITLE")
	/** 职务显示值 */
	private java.lang.String employeeTitleName;
	/** 所属组织机构 */
	@Column(name = "employee_org")
	private java.lang.String employeeOrg;
	/** 密级 */
	private short securityClass;
	@Transient
	@EnumerationValueName(type="SECURITY")
	/**密级显示值**/
	private String securityClassName;
	/** 用户标示 */
	private java.lang.String employeeToken;
	/** 创建时间 */
	private java.util.Date createAt;
	/** 更新时间 */
	private java.util.Date updateAt;
	/** 是否离职(0-在职，1-离职) */
	private String endFlag;

	/** 用户角色 */
	public Set<Role> getRoles() {
		return roles;
	}

	/** 用户角色 */
	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	public Orgnazation getOrgnazation() {
		return orgnazation;
	}

	public void setOrgnazation(Orgnazation orgnazation) {
		this.orgnazation = orgnazation;
	}
	
	/** 组织机构 */
	public Set<SysAuth> getSysAuths() {
		return sysAuths;
	}

	/** 用户系统权限 */
	public void setSysAuths(Set<SysAuth> sysAuths) {
		this.sysAuths = sysAuths;
	}

	/** id */
	public Integer getId() {
		return id;
	}

	/** id */
	public void setId(Integer id) {
		this.id = id;
	}

	/** 登录账号 */
	public java.lang.String getUserName() {
		return userName;
	}

	/** 登录账号 */
	public void setUserName(java.lang.String userName) {
		this.userName = userName;
	}

	/** 职工编号 */
	public java.lang.String getEmployeeNo() {
		return employeeNo;
	}

	/** 职工编号 */
	public void setEmployeeNo(java.lang.String employeeNo) {
		this.employeeNo = employeeNo;
	}

	/** 职工姓名 */
	public java.lang.String getEmployeeName() {
		return employeeName;
	}

	/** 职工姓名 */
	public void setEmployeeName(java.lang.String employeeName) {
		this.employeeName = employeeName;
	}

	/** 职务 */
	public java.lang.String getEmployeeTitle() {
		return employeeTitle;
	}

	/** 职务 */
	public void setEmployeeTitle(java.lang.String employeeTitle) {
		this.employeeTitle = employeeTitle;
	}

	/** 所属组织机构 */
	public java.lang.String getEmployeeOrg() {
		return employeeOrg;
	}

	/** 所属组织机构 */
	public void setEmployeeOrg(java.lang.String employeeOrg) {
		this.employeeOrg = employeeOrg;
	}

	/** 密级 */
	public short getSecurityClass() {
		return securityClass;
	}

	/** 密级 */
	public void setSecurityClass(short securityClass) {
		this.securityClass = securityClass;
	}

	/** 用户标示 */
	public java.lang.String getEmployeeToken() {
		return employeeToken;
	}

	/** 用户标示 */
	public void setEmployeeToken(java.lang.String employeeToken) {
		this.employeeToken = employeeToken;
	}

	/** 创建时间 */
	public java.util.Date getCreateAt() {
		return createAt;
	}

	/** 创建时间 */
	public void setCreateAt(java.util.Date createAt) {
		this.createAt = createAt;
	}

	/** 更新时间 */
	public java.util.Date getUpdateAt() {
		return updateAt;
	}

	/** 更新时间 */
	public void setUpdateAt(java.util.Date updateAt) {
		this.updateAt = updateAt;
	}

	public String getSecurityClassName() {
		return securityClassName;
	}

	public void setSecurityClassName(String securityClassName) {
		this.securityClassName = securityClassName;
	}

	public java.lang.String getEmployeeTitleName() {
		return employeeTitleName;
	}

	public void setEmployeeTitleName(java.lang.String employeeTitleName) {
		this.employeeTitleName = employeeTitleName;
	}
	/** 是否离职(0-在职，1-离职) */
	public String getEndFlag() {
		return endFlag;
	}
	/** 是否离职(0-在职，1-离职) */
	public void setEndFlag(String endFlag) {
		this.endFlag = endFlag;
	}

}