package com.neusoft.arltr.user.ws;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;

/**
 * 接收MDM数据的WebService
 * 
 * @author zhanghaibo
 *
 */
@WebService(targetNamespace = "http://service.arltr.neusoft.com/", name = "ArltrMdmTransfer")
public interface MdmTransferService {

//	/**
//	 * MDM用户数据同步接口
//	 *	
//	 *	@param xml MDM数据报文
//	 *  @return 状态码（1000：成功，1001：失败）
//	 */
//	@WebResult(name = "return", targetNamespace = "")
//	public String userTransfer(@WebParam(name="xml") String xml);
//	
//	/**
//	 * MDM组织机构数据同步接口
//	 *	
//	 *	@param xml MDM数据报文
//	 *  @return 状态码（1000：成功，1001：失败）
//	 */
//	@WebResult(name = "return", targetNamespace = "")
//	public String orgTransfer(@WebParam(name="xml") String xml);
	
	/**
	 * MDM数据同步接口
	 *	
	 *	@param xml MDM数据报文
	 *  @return 状态码（1000：成功，1001：失败）
	 */
	@WebResult(name = "msg", targetNamespace = "http://service.arltr.neusoft.com/")
    @RequestWrapper(localName = "mdmTransferRequest",
                    targetNamespace = "http://service.arltr.neusoft.com/",
                    className = "com.neusoft.ds.butler.ws.MdmTransferRequest")
    @WebMethod(action = "urn:transfer")
    @ResponseWrapper(localName = "mdmTransferResponse",
                     targetNamespace = "http://service.arltr.neusoft.com/",
                     className = "com.neusoft.ds.butler.ws.MdmTransferResponse")
	public String transfer(@WebParam(targetNamespace="http://service.arltr.neusoft.com/", name="xml") String xml);
}
