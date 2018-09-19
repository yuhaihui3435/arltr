/**
 * 
 */
package com.neusoft.arltr.indexing.ws;

import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;

import com.neusoft.arltr.common.base.RespBody;
import com.neusoft.arltr.indexing.model.Project;

/**
 * 接收各系统数据的WebService
 * 
 *
 *
 */
@WebService(targetNamespace = "http://service.arltr.neusoft.com/", name = "ArltrTdmTransfer")
public interface TdmTransferWebService {

	/**
	 * TDM数据同步接口
	 *	
	 *	@param data TDM数据
	 *  @return 状态码（1000：成功，1001：失败）
	 */
	@WebResult(name = "return", targetNamespace = "")
    @RequestWrapper(localName = "tdmTransferRequest",
                    targetNamespace = "http://service.arltr.neusoft.com/",
                    className = "com.neusoft.arltr.indexing.ws.TdmTransferRequest")
    @WebMethod(action = "urn:tdmTransfer")
    @ResponseWrapper(localName = "tdmTransferResponse",
                     targetNamespace = "http://service.arltr.neusoft.com/",
                     className = "com.neusoft.arltr.indexing.ws.TdmTransferResponse")
	public RespBody<String> tdmTransfer(List<Project> data);
	
	@WebResult(name = "return", targetNamespace = "")
	public String UserRightValidate(@WebParam(name="strUserKey") String strUserKey, @WebParam(name="strProjectId") String strProjectId);
	
	@WebResult(name = "return", targetNamespace = "")
	public String tdmTransferAlternate(@WebParam(name="xml") String xml);
}
