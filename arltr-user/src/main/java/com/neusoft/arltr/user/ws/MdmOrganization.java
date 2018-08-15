/**
 * 
 */
package com.neusoft.arltr.user.ws;

/**
 * @author zhanghaibo
 *
 */
public class MdmOrganization {

	/** 流水号 */
	private String id;
	/** 封存标志 */
	private String fcFlag;
	/** 排序 */
	private String xh;
	/** 部门编号 */
	private String orgCode;
	/** 部门主数据编号 */
	private String mdCode;
	/** 部门名称 */
	private String orgName;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getFcFlag() {
		return fcFlag;
	}
	public void setFcFlag(String fcFlag) {
		this.fcFlag = fcFlag;
	}
	public String getXh() {
		return xh;
	}
	public void setXh(String xh) {
		this.xh = xh;
	}
	public String getOrgCode() {
		return orgCode;
	}
	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}
	public String getMdCode() {
		return mdCode;
	}
	public void setMdCode(String mdCode) {
		this.mdCode = mdCode;
	}
	public String getOrgName() {
		return orgName;
	}
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
}
