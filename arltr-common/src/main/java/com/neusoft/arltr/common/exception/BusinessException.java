/**
 * 
 */
package com.neusoft.arltr.common.exception;

/**
 * 业务异常类
 * 
 * @author zhanghaibo
 *
 */
public class BusinessException extends RuntimeException {

	private static final long serialVersionUID = -84459605747375334L;

	public BusinessException(String msg) {
		super(msg);
	}
}
