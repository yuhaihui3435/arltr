package com.neusoft.arltr.common.utils;

import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.springframework.http.HttpEntity;


public class HttpClientUtils {

    /**
     * post请求
     * @param url
     * @param formParams
     * @return
     */
    public static String doPost(String url, Map<String, Object> formParams) {
    	
        if (MapUtils.isEmpty(formParams)) {
            return doPost(url);
        }

        try {
            return RestClient.getClient().postForObject(url, formParams, String.class);
        } catch (Exception e) {
        	throw new RuntimeException("HTTP请求失败[URL: " + url + "]");
        }
    }

    /**
     * post请求
     * @param url
     * @return
     */
    public static String doPost(String url) {
        try {
            return RestClient.getClient().postForObject(url, HttpEntity.EMPTY, String.class);
        } catch (Exception e) {
        	throw new RuntimeException("HTTP请求失败[URL: " + url + "]");
        }

    }
    
    /**
     * delete请求
     * @param url
     * @return
     */
    public static void doDelete(String url) {
        try {
            RestClient.getClient().delete(url);
        } catch (Exception e) {
        	throw new RuntimeException("HTTP请求失败[URL: " + url + "]");
        }

    }

    /**
     * get请求
     * @param url
     * @return
     */
    public static String doGet(String url) {
        try {
            return RestClient.getClient().getForObject(url, String.class);
        } catch (Exception e) {
        	throw new RuntimeException("HTTP请求失败[URL: " + url + "]");
        }
    }

}
