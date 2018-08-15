package com.neusoft.arltr.indexing.ws.client;

import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.namespace.QName;
import javax.xml.rpc.ParameterMode;
import javax.xml.rpc.ServiceException;

import org.apache.axis.Constants;
import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.axis.soap.SOAPConstants;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/** 
 * PDM WebService客户端 
 */
@Component
public class PdmClient {

	private static final Logger logger = Logger.getLogger(PdmClient.class.getName());
	
	@Value("${com.neusoft.pdm.url}")
	private String url;

	@Value("${com.neusoft.pdm.ns}")
    private String ns;
	
	@Value("${com.neusoft.pdm.username}")
    private String username;
	
	@Value("${com.neusoft.pdm.password}")
    private String password;
	
	Service service = new Service();
	
	/** 
	 * 根据日期得到文档列表
	 * 
	 * @param date 日期
	 * @return xml文档
	 */
	public String getDataXML(Date date) {
		
		logger.info("=====开始获取PDM文档列表【"+ date + "】=====");
		
		String d = "ALL";
		
		if (date != null) {
			SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd"); 
			d = df.format(date);
		}
		
		String paramXml = "<root><date>" + d + "</date></root>";
		
		String result = "";
		
		try {
			result = (String) invoke("getDataXML", new String[] {"arg0"},
			        new QName[] {Constants.XSD_STRING}, new Object[] {paramXml}, Constants.XSD_STRING);
			
			logger.info("PDM文档列表返回结果：" + result);
			
		} catch (Exception e) {
			logger.error("调用PDM文档列表接口出错");
			logger.error(e.getMessage(), e.getCause());
			e.printStackTrace();
		}
		
		logger.info("=====获取PDM文档列表结束【"+ date + "】=====");
		
		return result;
	}
	
	/** 
	 * 获取PDM文档实体路径
	 * 
	 * @param documentId 文档标识
	 * @param versionSno 版本号
	 * 
	 * @return xml文档
	 */
	public String getDocumentApplicationData(String documentId, String versionSno) {
		
		logger.info("=====开始获取PDM文档实体路径【" + documentId + "@" + versionSno + "】=====");
		
		String paramXml =  "<root>" +
										"	<data>" +
										"		<documentInfo>" +
										"			<documentId>" + documentId + "</documentId>" +
										"			<versionSno>" + versionSno + "</versionSno>" +
										"		</documentInfo>" +
										"	</data>" +
										"</root>";
		
		String result = "";
		
		try {
			result = (String) invoke("getDocumentApplicationData", new String[] {"arg0"},
			        new QName[] {Constants.XSD_STRING}, new Object[] {paramXml}, Constants.XSD_STRING);
			
			logger.info("PDM文档实体路径返回结果：" + result);
			
		} catch (Exception e) {
			logger.error("调用PDM文档实体路径接口出错");
			logger.error(e.getMessage(), e.getCause());
			e.printStackTrace();
		}
		
		logger.info("=====获取PDM文档实体路径结束【" + documentId + "@" + versionSno + "】=====");
		
		return result;
	}
	
	/**
     * 调用核心服务的WebService
     * 
     * @param methodName
     *            方法名称
     * @param parameterNames
     *            参数名称数组
     * @param parameterTypes
     *            参数类型数组
     * @param parameters
     *            参数值数组
     * @param returnType
     *            返回值类型
     * @return 返回值
     * @throws ServiceException
     * @throws MalformedURLException
     * @throws RemoteException
     */
    private String invoke(String methodName, String[] parameterNames, QName[] parameterTypes, Object[] parameters,
            QName returnType) throws ServiceException, MalformedURLException, RemoteException  {

        Call call = (Call) service.createCall();
        call.setUsername(username);// 设置用户名
        call.setPassword(password);// 设置密码
        call.setUseSOAPAction(true);
        call.setSOAPVersion(SOAPConstants.SOAP11_CONSTANTS);
        call.setOperationName(new QName(ns, methodName));
        call.setTargetEndpointAddress(new URL(url));
        for (int i = 0; i < parameterNames.length && i < parameterTypes.length; i++) {
            call.addParameter(parameterNames[i], parameterTypes[i], ParameterMode.IN);
        }
        // 设置返回值类型
        if (returnType != null) {
            call.setReturnType(returnType);
        } else {
            call.setReturnType(Constants.XSD_STRING);
        }
        
        return (String) call.invoke(parameters);
    }
    
}
