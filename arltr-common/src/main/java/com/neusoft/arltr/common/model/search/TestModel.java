/**
 * 
 */
package com.neusoft.arltr.common.model.search;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * @author zhanghaibo
 *
 */
public class TestModel {

	@NotEmpty
	private String name;
	@NotEmpty
	private String code;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
}
