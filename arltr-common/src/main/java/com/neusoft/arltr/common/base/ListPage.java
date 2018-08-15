/**
 * 
 */
package com.neusoft.arltr.common.base;

import java.util.HashMap;

import org.springframework.data.domain.Page;

/**
 * datagrid分页数据容器
 * 
 * @author zhanghaibo
 *
 */
public class ListPage extends HashMap<String, Object>  {

	private static final long serialVersionUID = -8734780799143554730L;
	
	public ListPage() {
		
	}
	
	public ListPage(Page<?> listPage) {
		this.put("total", listPage.getTotalElements());
		this.put("rows", listPage.getContent());
		this.put("code", RespBody.SUCCESS);
	}

}
