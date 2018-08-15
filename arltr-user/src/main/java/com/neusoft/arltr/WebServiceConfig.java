package com.neusoft.arltr;

import javax.xml.ws.Endpoint;

import org.apache.cxf.Bus;
import org.apache.cxf.jaxws.EndpointImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.neusoft.arltr.user.ws.MdmTransferService;

@Configuration
public class WebServiceConfig {
	
	@Autowired
	Bus bus;
	
	@Autowired
	MdmTransferService mdmTransferService;
	
    @Bean
    public Endpoint tdm() {
        EndpointImpl endpoint = new EndpointImpl(bus, mdmTransferService);
        endpoint.publish("/transfer/mdm");
        return endpoint;
    }

}
