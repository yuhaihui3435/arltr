package com.neusoft.arltr.common.utils;

import java.io.IOException;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.solr.client.solrj.ResponseParser;
import org.apache.solr.client.solrj.SolrRequest;
import org.apache.solr.client.solrj.SolrServerException;

public class HttpSolrClient extends  org.apache.solr.client.solrj.impl.HttpSolrClient {
    private static final long serialVersionUID = 1973496789131415484L;

    
    public HttpSolrClient(String baseURL,
                          HttpClient client,
                          ResponseParser parser) {
        super(baseURL, client, parser);
    }


    public HttpSolrClient(String baseURL, HttpClient client) {
        super(baseURL, client);
    }


    public HttpSolrClient(String baseURL) {
        super(baseURL);
    }


    @SuppressWarnings("rawtypes")
    @Override
    protected HttpRequestBase createMethod(SolrRequest request,
                                           String collection) throws IOException, SolrServerException {
        String col = (collection != null && baseUrl.endsWith(collection)) ? null : collection;  
        return super.createMethod(request, col);
    }
}
