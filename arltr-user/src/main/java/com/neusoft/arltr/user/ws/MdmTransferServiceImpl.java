/**
 * 
 */
package com.neusoft.arltr.user.ws;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.jws.WebService;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.neusoft.arltr.user.service.OrgService;
import com.neusoft.arltr.user.service.UserService;

/**
 * @author zhanghaibo
 *
 */
@WebService(serviceName = "MdmTransferService", portName = "MdmTransferPort",
targetNamespace = "http://service.arltr.neusoft.com/",
endpointInterface = "com.neusoft.arltr.user.ws.MdmTransferService")
@Component
public class MdmTransferServiceImpl implements MdmTransferService {

	private static final Logger logger = Logger.getLogger(MdmTransferServiceImpl.class.getName());
	
	@Autowired
	UserService userService;
	
	@Autowired
	OrgService orgService;
	
//	@Override
	public String userTransfer(String xml) {
		
		logger.info("===========MDM用户数据同步接口 START============");
		logger.info("解析前数据：" + xml);
		
		try {
			
			StringReader read = new StringReader(xml);
	        InputSource source = new InputSource(read);
	        
	        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();  
	        DocumentBuilder db = dbf.newDocumentBuilder();  
	        Document document = db.parse(source);
	        
	        NodeList dNodes = document.getChildNodes();
	        
	        List<MdmUser> userList = new ArrayList<MdmUser>();
	        
	        for (int i = 0; i < dNodes.getLength(); i++) {

	        	Node dNode = dNodes.item(i);
	        	
	        	if ("root".equalsIgnoreCase(dNode.getNodeName())) {
	        		
	        		NodeList rNodes = dNode.getChildNodes();
	        		
	        		for (int j = 0; j < rNodes.getLength(); j++) {
	        			
	        			Node rNode = rNodes.item(j);
	        			
	        			if ("data".equalsIgnoreCase(rNode.getNodeName())) {
	        				
	        				NodeList pNodes = rNode.getChildNodes();
	        				
	        				for (int k = 0; k < pNodes.getLength(); k++) {
	        					
	        					Node pNode = pNodes.item(k);
	        					
	        					if ("User".equalsIgnoreCase(pNode.getNodeName())) {
	        						
	        						MdmUser mdmUser = new MdmUser();
	        						
	        						NodeList attributes = pNode.getChildNodes();
	        						
	        						for (int s = 0; s < attributes.getLength(); s++) {
	        							
	        							Node attribute = attributes.item(s);
	        							
	        							if (attribute.getNodeType() == Node.ELEMENT_NODE) { 
	        								
	        								PropertyDescriptor pd = new PropertyDescriptor(attribute.getNodeName(), mdmUser.getClass());
	        								pd.getWriteMethod().invoke(mdmUser, attribute.getTextContent());
	        							}
	        						}
	        						
	        						userList.add(mdmUser);
	        					}
	        				}
	        			}
	        		}
	        	}
	        }
	        
	        ObjectMapper objectMapper = new ObjectMapper();
			logger.info("解析后数据：" + objectMapper.writeValueAsString(userList));
			
			userService.saveMdmUsers(userList);
	        
		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
			return "failed";
		}
		
		logger.info("===========MDM用户数据同步接口 END============");
		
		return "success";
	}

//	@Override
	public String orgTransfer(String xml) {

		logger.info("===========MDM组织机构数据同步接口 START============");
		logger.info("解析前数据：" + xml);
		
		try {
			
			StringReader read = new StringReader(xml);
	        InputSource source = new InputSource(read);
	        
	        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();  
	        DocumentBuilder db = dbf.newDocumentBuilder();  
	        Document document = db.parse(source);
	        
	        NodeList dNodes = document.getChildNodes();
	        
	        List<MdmOrganization> orgList = new ArrayList<MdmOrganization>();
	        
	        for (int i = 0; i < dNodes.getLength(); i++) {

	        	Node dNode = dNodes.item(i);
	        	
	        	if ("root".equalsIgnoreCase(dNode.getNodeName())) {
	        		
	        		NodeList rNodes = dNode.getChildNodes();
	        		
	        		for (int j = 0; j < rNodes.getLength(); j++) {
	        			
	        			Node rNode = rNodes.item(j);
	        			
	        			if ("data".equalsIgnoreCase(rNode.getNodeName())) {
	        				
	        				NodeList pNodes = rNode.getChildNodes();
	        				
	        				for (int k = 0; k < pNodes.getLength(); k++) {
	        					
	        					Node pNode = pNodes.item(k);
	        					
	        					if ("Organization".equalsIgnoreCase(pNode.getNodeName())) {
	        						
	        						MdmOrganization mdmOrg = new MdmOrganization();
	        						
	        						NodeList attributes = pNode.getChildNodes();
	        						
	        						for (int s = 0; s < attributes.getLength(); s++) {
	        							
	        							Node attribute = attributes.item(s);
	        							
	        							if (attribute.getNodeType() == Node.ELEMENT_NODE) { 
	        								
	        								PropertyDescriptor pd = new PropertyDescriptor(attribute.getNodeName(), mdmOrg.getClass());
	        								pd.getWriteMethod().invoke(mdmOrg, attribute.getTextContent());
	        							}
	        						}
	        						
	        						orgList.add(mdmOrg);
	        					}
	        				}
	        			}
	        		}
	        	}
	        }
	        
	        ObjectMapper objectMapper = new ObjectMapper();
			logger.info("解析后数据：" + objectMapper.writeValueAsString(orgList));
			
			orgService.saveMdmOrgs(orgList);
	        
		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
			return "failed";
		}
		
		logger.info("===========MDM组织机构数据同步接口 END============");
		
		return "success";
	}

	@Override
	public String transfer(String xml) {
		logger.info("===========MDM数据同步接口 START============");
		logger.info("解析前数据：" + xml);
		
		try {
			
			StringReader read = new StringReader(xml);
	        InputSource source = new InputSource(read);
	        
	        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();  
	        DocumentBuilder db = dbf.newDocumentBuilder();  
	        Document document = db.parse(source);
	        
	        NodeList dNodes = document.getChildNodes();
	        
	        List<MdmUser> userList = new ArrayList<MdmUser>();
	        List<MdmOrganization> orgList = new ArrayList<MdmOrganization>();
	        
	        for (int i = 0; i < dNodes.getLength(); i++) {

	        	Node dNode = dNodes.item(i);
	        	
	        	if ("root".equalsIgnoreCase(dNode.getNodeName())) {
	        		
	        		NodeList rNodes = dNode.getChildNodes();
	        		
	        		String dataType = document.getElementsByTagName("mdtype").item(0).getTextContent();
	        		
	        		for (int j = 0; j < rNodes.getLength(); j++) {
	        			
	        			Node rNode = rNodes.item(j);
	        			
	        			if ("data".equalsIgnoreCase(rNode.getNodeName())) {
	        				
	        				NodeList pNodes = rNode.getChildNodes();
	        				
	        				for (int k = 0; k < pNodes.getLength(); k++) {
	        					
	        					Node pNode = pNodes.item(k);
	        					
	        					if ("Organization".equals(dataType) && "Organization".equalsIgnoreCase(pNode.getNodeName())) {
	        						
	        						MdmOrganization mdmOrg = new MdmOrganization();
	        						
	        						NodeList attributes = pNode.getChildNodes();
	        						
	        						for (int s = 0; s < attributes.getLength(); s++) {
	        							
	        							Node attribute = attributes.item(s);
	        							
	        							if (attribute.getNodeType() == Node.ELEMENT_NODE) { 
	        								
	        								try {
	        									PropertyDescriptor pd = new PropertyDescriptor(attribute.getNodeName(), mdmOrg.getClass());
		        								pd.getWriteMethod().invoke(mdmOrg, attribute.getTextContent());
	        								} catch (IntrospectionException e) {
	        									logger.info("未识别字段：" + attribute.getNodeName());
	        								}
	        							}
	        						}
	        						
	        						orgList.add(mdmOrg);
	        					}
	        					
	        					if ("User".equals(dataType) && "User".equalsIgnoreCase(pNode.getNodeName())) {
	        						
	        						MdmUser mdmUser = new MdmUser();
	        						
	        						NodeList attributes = pNode.getChildNodes();
	        						
	        						for (int s = 0; s < attributes.getLength(); s++) {
	        							
	        							Node attribute = attributes.item(s);
	        							
	        							if (attribute.getNodeType() == Node.ELEMENT_NODE) { 
	        								
	        								try {
	        									PropertyDescriptor pd = new PropertyDescriptor(attribute.getNodeName(), mdmUser.getClass());
		        								pd.getWriteMethod().invoke(mdmUser, attribute.getTextContent());
	        								} catch (IntrospectionException e) {
	        									logger.info("未识别字段：" + attribute.getNodeName());
	        								}
	        							}
	        						}
	        						
	        						userList.add(mdmUser);
	        					}
	        				}
	        			}
	        		}
	        	}
	        }
	        
	        ObjectMapper objectMapper = new ObjectMapper();
			logger.info("解析后数据：" + objectMapper.writeValueAsString(orgList));
			logger.info("解析后数据：" + objectMapper.writeValueAsString(userList));
			
			userService.saveMdmUsers(userList);
			orgService.saveMdmOrgs(orgList);
	        
		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
			return "failure";
		}
		
		logger.info("===========MDM数据同步接口 END============");
		
		return "success";
		
	}
}
