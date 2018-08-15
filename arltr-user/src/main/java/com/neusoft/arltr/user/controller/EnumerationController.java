/**
 * 
 */
package com.neusoft.arltr.user.controller;

import java.util.List;

import org.apache.cxf.endpoint.Client;
import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.neusoft.arltr.common.base.RespBody;
import com.neusoft.arltr.common.entity.user.Enumeration;
import com.neusoft.arltr.common.entity.user.User;
import com.neusoft.arltr.common.exception.BusinessException;
import com.neusoft.arltr.user.repository.EnumerationRepository;
import com.neusoft.arltr.user.repository.UserRepository;

/**
 * 枚举控制器
 * 
 * @author zhanghaibo
 *
 */
@RestController
public class EnumerationController {

	@Autowired
	EnumerationRepository enumerationRepository;
	
	@PostMapping("/enumeration/list")
	public RespBody<List<Enumeration>> getEnumerationList(@RequestParam("type") String type){
		RespBody<List<Enumeration>> resp=new RespBody<List<Enumeration>>();
		List<Enumeration> list=enumerationRepository.getEnumerationList(type);
		resp.setBody(list);
		return resp;
	}
	
	@GetMapping("/wtest")
	public RespBody<Object> wsTest() throws Exception {
		
		String xml = "<?xml version=\"1.0\" encoding=\"gb2312\"?>				" +
				"	<root>				" +
				"		<mdtype>User</mdtype>			" +
				"		<data>			" +
				"			<User>		" +
				"				<xh>9999</xh>	" +
				"				<code>0001A110000000000T4H</code>	" +
				"				<endFlagName>否</endFlagName>	" +
				"				<endFlagCode>0</endFlagCode>	" +
				"				<id>0001A110000000000T4H</id>	" +
				"				<fcFlag>0</fcFlag>	" +
				"				<identityNo>123</identityNo>	" +
				"				<name>二丫</name>	" +
				"				<genderName>女</genderName>	" +
				"				<genderCode>2</genderCode>	" +
				"				<userName>古雷</userName>	" +
				"				<psnSecretLevelName>重要</psnSecretLevelName>	" +
				"				<psnSecretLevelCode>80</psnSecretLevelCode>	" +
				"				<deptCode>B000001173</deptCode>	" +
				"				<mdCode>D000004740</mdCode>	" +
				"			</User>		" +
				"		</data>			" +
				"	</root>				";

		JaxWsDynamicClientFactory dcf = JaxWsDynamicClientFactory.newInstance();
		Client client = dcf.createClient("http://localhost:9001/services/transfer/mdm?wsdl");
		Object[] res = client.invoke("transfer", xml);
		
		RespBody<Object> resp = new RespBody<Object>();
		
		resp.setBody(res);
		
		return resp;
	}
	
	@GetMapping("/owtest")
	public RespBody<Object> wsTesto() throws Exception {
		
		String xml = "<?xml version=\"1.0\" encoding=\"gb2312\"?>				" +
				"	<root>				" +
				"		<mdtype>Organization</mdtype>			" +
				"		<data>			" +
				"			<Organization>		" +
				"				<id>0001A110000000000T4H</id>	" +
				"				<fcFlag>0</fcFlag>	" +
				"				<xh>9999</xh>	" +
				"				<orgCode>1001A110000000000IP3</orgCode>	" +
				"				<mdCode>B000001145</mdCode>	" +
				"				<orgName>206所</orgName>	" +
				"			</Organization>		" +
				"		</data>			" +
				"	</root>				";

		JaxWsDynamicClientFactory dcf = JaxWsDynamicClientFactory.newInstance();
		Client client = dcf.createClient("http://localhost:9001/services/transfer/mdm?wsdl");
		Object[] res = client.invoke("transfer", xml);
		
		RespBody<Object> resp = new RespBody<Object>();
		
		resp.setBody(res);
		
		return resp;
	}
}
