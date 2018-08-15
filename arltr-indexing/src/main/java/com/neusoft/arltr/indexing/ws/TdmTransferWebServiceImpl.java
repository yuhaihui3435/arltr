/**
 * 
 */
package com.neusoft.arltr.indexing.ws;

import java.util.List;

import javax.jws.WebService;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.neusoft.arltr.common.base.RespBody;
import com.neusoft.arltr.common.exception.BusinessException;
import com.neusoft.arltr.indexing.model.Project;
import com.neusoft.arltr.indexing.service.IndexingService;
import com.neusoft.arltr.indexing.utils.WebServiceUtils;

/**
 * TDM数据同步WebService
 * 
 * @author zhanghaibo
 */
@WebService(serviceName = "ArltrTdmTransferService", portName = "ArltrTdmTransferPort",
						targetNamespace = "http://service.arltr.neusoft.com/",
						endpointInterface = "com.neusoft.arltr.indexing.ws.TdmTransferWebService")
@Component
public class TdmTransferWebServiceImpl implements TdmTransferWebService {

	private static final Logger logger = Logger.getLogger(TdmTransferWebServiceImpl.class.getName());
	
	@Autowired
	IndexingService indexingService;
	
	@Override
	public RespBody<String> tdmTransfer(List<Project> data) {
		logger.info("===========TDM数据同步 START============");
		try {
			logger.info("数据量：" + data.size() + "条");
			for (Project project : data) {
				indexingService.createIndexOfTdmObject(project);
			}
		} catch (Exception e) {
			logger.error("TDM数据同步发生错误", e);
			throw new BusinessException("TDM数据同步发生错误");
		}
		
		logger.info("===========TDM数据同步 END=============");
		return new RespBody<String>();
	}

	@Override
	public String UserRightValidate(String strUserKey, String strProjectId) {
		logger.info("===========TDM权限验证模拟接口 START============");
		
		if ("2".equals(strProjectId)) {
			return "0";
		}
		logger.info("===========TDM权限验证模拟接口 END=============");
		return "1";
	}

	@Override
	public String tdmTransferAlternate(String xml) {
		logger.info("===========TDM数据同步候补接口 START============");
		
		logger.info("解析前数据：" + xml);
		
		try {
			
			List<Project> projects = WebServiceUtils.xml2Pojo(xml);
			
			ObjectMapper objectMapper = new ObjectMapper();
			logger.info("解析后数据：" + objectMapper.writeValueAsString(projects));
			
			for (Project project : projects) {
				indexingService.createIndexOfTdmObject(project);
			}
			
		} catch (Exception e) {
			logger.error(e);
			return "fail";
		}
		
		logger.info("===========TDM数据同步候补接口 END============");
		return "success";
	}

}
