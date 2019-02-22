package com.rcc.test.base;

import java.io.Serializable;
import java.util.Map;

/** 
 * http 响应bean 
 * @author YangWenlong
 * @date 2017年7月13日
 */
public class HttpResponseBean implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public HttpResponseBean(){
        
    }
    /** 
     * 返回报文内容
     */
    private String responseBody;
    /**
     * http响应码
     */
    private int statusCode;
    
    /**
     * http响应头
     */
    private Map<String,String> headers;
    
    /**
     * @return the responseBody
     */
    public String getResponseBody() {
        return responseBody;
    }
    /**
     * @param responseBody the responseBody to set
     */
    public void setResponseBody(String responseBody) {
        this.responseBody = responseBody;
    }
    /**
     * @return the statusCode
     */
    public int getStatusCode() {
        return statusCode;
    }
    /**
     * @param statusCode the statusCode to set
     */
    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }
    /**
     * @return the headers
     */
    public Map<String, String> getHeaders() {
        return headers;
    }
    /**
     * @param headers the headers to set
     */
    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }
   
}
