package com.neusoft.arltr.common.utils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.neusoft.arltr.common.base.SerializableObject;
/**
 * easyui树节点
 * 
 * @author zhanghaibo
 */
public class TreeNode extends SerializableObject implements Serializable {
	
	private static final long serialVersionUID = 776880759926892246L;
	
	private String id;
	private String state="";
	private String url;
	private String pid;
	private String text;
	private String iconCls;
	private boolean checked=false;
	private Map<String,Object> attributes;
	private List<TreeNode> children;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getIconCls() {
		return iconCls;
	}
	public void setIconCls(String iconCls) {
		this.iconCls = iconCls;
	}
	public boolean isChecked() {
		return checked;
	}
	public void setChecked(boolean checked) {
		this.checked = checked;
	}
	public Map<String, Object> getAttributes() {
		return attributes;
	}
	public void setAttributes(Map<String, Object> attributes) {
		this.attributes = attributes;
	}
	public List<TreeNode> getChildren() {
		return children;
	}
	public void setChildren(List<TreeNode> children) {
		this.children = children;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getPid() {
		return pid;
	}
	public void setPid(String pid) {
		this.pid = pid;
	}
	
	public void addAttribute(String key, Object value) {
		
		if (this.attributes == null) {
			this.attributes = new HashMap<String, Object>();
		}
		
		this.attributes.put(key, value);
	}

}
