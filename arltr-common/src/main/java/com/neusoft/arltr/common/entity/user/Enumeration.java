/***********************************************************************
 * Module:  Enumeration.java
 * Author:  zhb_1204
 * Purpose: Defines the Class Enumeration
 ***********************************************************************/

package com.neusoft.arltr.common.entity.user;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * 枚举表实体类
 * 
 * @author zhanghaibo
 */
@Entity
@Data
@Table(name = "management_enumeration")
public class Enumeration {
	@Id
	private Integer id;
	/** 类型 */
	private java.lang.String type;
	/** 名称 */
	private java.lang.String typeName;
	/** 值编码 */
	private java.lang.String value;
	/** 值名称 */
	private java.lang.String valueName;
	/** 是否默认 */
	private java.lang.String isDefault = "0";
	/** 父值编码 */
	private java.lang.String parentValue = "-1";
	/** 是否可用 */
	private java.lang.String active = "1";
	/** 显示序号 */
	private Integer orderNo;
	/** 备注 */
	private java.lang.String remark;

	/** ID */
	public Integer getId() {
		return id;
	}

	/** ID */
	public void setId(Integer id) {
		this.id = id;
	}

	/** 类型 */
	public java.lang.String getType() {
		return type;
	}

	/** 类型 */
	public void setType(java.lang.String type) {
		this.type = type;
	}

	/** 名称 */
	public java.lang.String getTypeName() {
		return typeName;
	}

	/** 名称 */
	public void setTypeName(java.lang.String typeName) {
		this.typeName = typeName;
	}

	/** 值编码 */
	public java.lang.String getValue() {
		return value;
	}

	/** 值编码 */
	public void setValue(java.lang.String value) {
		this.value = value;
	}

	/** 值名称 */
	public java.lang.String getValueName() {
		return valueName;
	}

	/** 值名称 */
	public void setValueName(java.lang.String valueName) {
		this.valueName = valueName;
	}

	/** 是否默认 */
	public java.lang.String getIsDefault() {
		return isDefault;
	}

	/** 是否默认 */
	public void setIsDefault(java.lang.String isDefault) {
		this.isDefault = isDefault;
	}

	/** 父值编码 */
	public java.lang.String getParentValue() {
		return parentValue;
	}

	/** 父值编码 */
	public void setParentValue(java.lang.String parentValue) {
		this.parentValue = parentValue;
	}

	/** 是否可用 */
	public java.lang.String getActive() {
		return active;
	}

	/** 是否可用 */
	public void setActive(java.lang.String active) {
		this.active = active;
	}

	/** 显示序号 */
	public Integer getOrderNo() {
		return orderNo;
	}

	/** 显示序号 */
	public void setOrderNo(Integer orderNo) {
		this.orderNo = orderNo;
	}

	/** 备注 */
	public java.lang.String getRemark() {
		return remark;
	}

	/** 备注 */
	public void setRemark(java.lang.String remark) {
		this.remark = remark;
	}

}