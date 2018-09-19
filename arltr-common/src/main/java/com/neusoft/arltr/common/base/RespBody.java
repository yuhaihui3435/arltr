package com.neusoft.arltr.common.base;

/**
 * JSON返回数据容器
 * 
 *
 *
 */
public class RespBody<T> {

	/** JSON返回码：成功 */
	public static final int SUCCESS = 1000;
	/** JSON返回码：业务错误 */
	public static final int BUSINESS_ERROR = 1001;
	/** JSON返回码：系统错误 */
	public static final int SYS_ERROR = 1002;
	
	/** 返回消息 */
	private String msg;

	/** 返回码 */
	private int code;
	
	/** 返回内容 */
	private T body = null;
	
	/**
	 * JSON返回数据容器（默认返回成功状态码）
	 * 
	 */
	public RespBody() {

		this.code = SUCCESS;
		this.msg = "";
	}

	/**
	 * JSON返回数据容器
	 * 
	 * @param msg 返回消息
	 * @param code 返回码
	 *
	 */
	public RespBody(String msg, int code) {
		
		this.code = code;
		this.msg = msg;
	}
	
	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public T getBody() {
		return body;
	}

	public void setBody(T content) {
		this.body = content;
	}
}
