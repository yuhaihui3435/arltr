/**
 * 
 */
package com.neusoft.arltr.indexing.utils;

import java.beans.PropertyDescriptor;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.neusoft.arltr.indexing.model.Project;

/**
 * WebService工具类
 * 
 *
 *
 */
public class WebServiceUtils {

	public static List<Project> xml2Pojo(String xml) throws Exception {
		
		List<Project> result = new ArrayList<Project>();
		
		StringReader read = new StringReader(xml);
        InputSource source = new InputSource(read);
        
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();  
        DocumentBuilder db = dbf.newDocumentBuilder();  
        Document document = db.parse(source);
        
        NodeList dNodes = document.getChildNodes();
        
        for (int i = 0; i < dNodes.getLength(); i++) {
        	
        	Node dNode = dNodes.item(i);
        	
        	if ("Root".equalsIgnoreCase(dNode.getNodeName())) {
        		
        		NodeList rNodes = dNode.getChildNodes();
        		
        		for (int j = 0; j < rNodes.getLength(); j++) {
        			
        			Node rNode = rNodes.item(j);
        			
        			if ("Projects".equalsIgnoreCase(rNode.getNodeName())) {
        				
        				NodeList pNodes = rNode.getChildNodes();
    	        		
    	        		for (int k = 0; k < pNodes.getLength(); k++) {
    	        			
    	        			Node pNode = pNodes.item(k);
    	        			
    	        			if ("Project".equalsIgnoreCase(pNode.getNodeName())) {
    	        				
    	        				Project project = new Project();
    	        				
    	        				Node oNode = pNode.getAttributes().getNamedItem("OptType");
    	        				
    	        				project.setOptType(Integer.parseInt(oNode.getTextContent()));
    	        				
    	        				NodeList aNodes = pNode.getChildNodes();
    	        				
    	        				for (int s = 0; s < aNodes.getLength(); s++) {
    	        					
    	        					Node aNode = aNodes.item(s);
    	        					
    	        					if (aNode.getNodeType() == Node.ELEMENT_NODE) { 
    	        						
    	        						PropertyDescriptor pd = new PropertyDescriptor(aNode.getNodeName().toLowerCase(), project.getClass());
    	        						pd.getWriteMethod().invoke(project, aNode.getTextContent());
    	        					}
    	        				}
    	        				
    	        				result.add(project);
    	        			}
    	        		}
    	        		
        			}
        		}
        	}
        	
        }
	        
		return result;
	}
}
