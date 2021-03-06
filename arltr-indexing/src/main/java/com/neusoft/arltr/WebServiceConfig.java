package com.neusoft.arltr;

import javax.xml.ws.Endpoint;

import com.neusoft.arltr.indexing.ws.PdmTest;
import org.apache.cxf.Bus;
import org.apache.cxf.jaxws.EndpointImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.neusoft.arltr.indexing.ws.TdmTransferWebService;

@Configuration
public class WebServiceConfig {
	
	@Autowired
	Bus bus;
	
	@Autowired
	TdmTransferWebService tdmTransferWebService;

	@Autowired
    PdmTest pdmTest;
	
    @Bean
    public Endpoint tdm() {
        EndpointImpl endpoint = new EndpointImpl(bus, tdmTransferWebService);
        endpoint.publish("/transfer/tdm");
        return endpoint;
    }

//    @Bean
//    public Endpoint pdm(){
//        EndpointImpl endpoint = new EndpointImpl(bus, pdmTest);
//        endpoint.publish("/data/pdm");
//        return endpoint;
//    }

}
