package com.rcc.test.utils;


import com.rcc.test.base.HttpResponseBean;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.*;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.AuthSchemes;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.nio.charset.UnsupportedCharsetException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by DK on 2016/11/1.
 */
public class HttpUtils {
    private final static Logger log = LoggerFactory.getLogger(HttpUtils.class);
    private static PoolingHttpClientConnectionManager cm;
    private static RequestConfig requestConfig;
    private final static int DEFAULT_TIMEOUT = 240000;
    private String emptyStr = "";
    private static CloseableHttpClient httpClient;
    private HttpHost proxy;
    private static SSLConnectionSocketFactory socketFactory;

    private String requestEncoding = "UTF-8";
    private String responseEncoding = "UTF-8";
    private Map<String, String> headers;
    private Boolean autoRedirect = true;
    private long sleepTime = 50;
    private final String jsonContentType = "application/json;charset=UTF-8";

    private static ArrayList<NameValuePair> covertParams2NVPS(Map<String, String> params) {

        ArrayList<NameValuePair> pairs = new ArrayList<>();
        for (Map.Entry<String, String> param : params.entrySet()) {
            pairs.add(new BasicNameValuePair(param.getKey(), String.valueOf(param.getValue())));
        }
        return pairs;
    }

    /**
     * 设置信任自定义的证书
     *
     * @param keyStorePath 密钥库路径
     * @param keyStorepass 密钥库密码
     * @return
     */
    private static void enableCustomVerifySSL(String keyStorePath, String keyStorepass) {
        FileInputStream instream = null;
        try {
            SSLContext sc;
            KeyStore trustStore;
            trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            instream = new FileInputStream(new File(keyStorePath));
            trustStore.load(instream, keyStorepass.toCharArray());
            // 相信自己的CA和所有自签名的证书
            sc = SSLContexts.custom().loadTrustMaterial(trustStore, new TrustSelfSignedStrategy()).build();
            socketFactory = new SSLConnectionSocketFactory(sc, NoopHostnameVerifier.INSTANCE);
        } catch (KeyStoreException | IOException | NoSuchAlgorithmException | CertificateException | KeyManagementException e) {
            log.error("enableCustomVerifySSL() error:", e);
        } finally {
            try {
                if (instream != null) {
                    instream.close();
                }
            } catch (IOException e) {
                log.error("IOException:", e);
            }
        }
    }

    /**
     * 忽略证书
     */
    private static void ignoreVerifySSL() {
        try {
            TrustManager manager = new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] x509Certificates, String s)
                        throws CertificateException {
                    //
                }

                @Override
                public void checkServerTrusted(X509Certificate[] x509Certificates, String s)
                        throws CertificateException {
                    //
                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }
            };
            SSLContext context = SSLContext.getInstance("TLS");// SSLv3
            context.init(null, new TrustManager[]{manager}, null);
            socketFactory = new SSLConnectionSocketFactory(context, NoopHostnameVerifier.INSTANCE);
        } catch (NoSuchAlgorithmException e) {
            log.error("NoSuchAlgorithmException:", e);
        } catch (KeyManagementException e) {
            log.error("KeyManagementException:", e);
        }
    }

    private void initHttpClient() {
        if (requestConfig == null) {
            requestConfig = RequestConfig.custom().setSocketTimeout(DEFAULT_TIMEOUT).setConnectTimeout(DEFAULT_TIMEOUT)
                    .setConnectionRequestTimeout(DEFAULT_TIMEOUT).setCookieSpec(CookieSpecs.STANDARD_STRICT)
                    .setRedirectsEnabled(false).setExpectContinueEnabled(true)
                    .setTargetPreferredAuthSchemes(Arrays.asList(AuthSchemes.NTLM, AuthSchemes.DIGEST))
                    .setProxyPreferredAuthSchemes(Arrays.asList(AuthSchemes.BASIC)).build();
        }

        if (cm == null) {
            ignoreVerifySSL();
            Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                    .register("http", PlainConnectionSocketFactory.INSTANCE).register("https", socketFactory).build();
            cm = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
            cm.setMaxTotal(500);// 整个连接池最大连接数
            cm.setDefaultMaxPerRoute(5);// 每路由最大连接数，默认值是2
        }

        reSetHeader();

		/*
         * DefaultHttpRequestRetryHandler dhr = new
		 * DefaultHttpRequestRetryHandler(3, true); httpClient =
		 * HttpClients.custom().setConnectionManager(cm).setRetryHandler(dhr)
		 * .build();
		 */
        if (httpClient == null) {
            httpClient = HttpClients.custom().setConnectionManager(cm).build();
        }


    }

    public void reSetHeader() {
        headers = new HashMap<>();
        headers.put("Accept",
                "text/html,application/xhtml+xml,application/xml;application/json;q=0.9,image/webp,*/*;q=0.8");
        headers.put("Accept-Encoding", "gzip, deflate, sdch, br");
        headers.put("Accept-Language", "zh-CN,zh;q=0.8");
        headers.put("Upgrade-Insecure-Requests", "1");
        headers.put("User-Agent",
                "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/52.0.2743.116 Safari/537.36");
    }

    public void addHeaders(Map<String, String> headersMap) {
        headers.putAll(headersMap);
    }

    private HttpResponseBean getResponse(HttpRequestBase request) {
        for (Map.Entry<String, String> param : headers.entrySet()) {
            request.addHeader(param.getKey(), String.valueOf(param.getValue()));
        }
        // log.debug("http 请求 header:{}",headers);
        request.setConfig(requestConfig);
        CloseableHttpResponse response = null;
        try {
            if (proxy == null) {
                response = httpClient.execute(request);
                return getHttpResponseBean(response);
            } else {
                response = httpClient.execute(proxy, request);
                return getHttpResponseBean(response);
            }

        } catch (ClientProtocolException | HttpHostConnectException e) {
            log.error("HttpUtils getResult error:", e);
        } catch (IOException e) {
            log.error("HttpUtils getResult IOException:", e);
        } finally {
            request.releaseConnection();
            try {
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                log.error("IOException:", e);
            }
        }
        return new HttpResponseBean();
    }

    private HttpResponseBean getHttpResponseBean(HttpResponse httpResponse) {
        HttpResponseBean httpResponseBean = new HttpResponseBean();
        Header[] headers = httpResponse.getAllHeaders();
        Map<String, String> headersMap = new HashMap<>();
        if (headers != null) {
            for (int i = 0; i < headers.length; i++) {
                Header header = headers[i];
                headersMap.put(header.getName(), header.getValue());
            }
        }
        httpResponseBean.setHeaders(headersMap);
        httpResponseBean.setStatusCode(httpResponse.getStatusLine().getStatusCode());
        String responseBody = "";
        try {
            responseBody = EntityUtils.toString(httpResponse.getEntity(), responseEncoding);
        } catch (ParseException e) {
        } catch (IOException e) {
        }
        httpResponseBean.setResponseBody(responseBody);
        return httpResponseBean;
    }

    private String getResult(HttpRequestBase request) {
        for (Map.Entry<String, String> param : headers.entrySet()) {
            request.addHeader(param.getKey(), String.valueOf(param.getValue()));
        }
        log.debug("请求headers: {}", headers);
        request.setConfig(requestConfig);
        CloseableHttpResponse response = null;
        try {
            if (proxy == null) {
                response = httpClient.execute(request);
                Thread.sleep(sleepTime);
            } else {
                response = httpClient.execute(proxy, request);
                Thread.sleep(sleepTime);
            }
            int statusCode = response.getStatusLine().getStatusCode();

            if (statusCode == HttpStatus.SC_OK) {
                return EntityUtils.toString(response.getEntity(), responseEncoding);
            } else {
                String result = EntityUtils.toString(response.getEntity(), responseEncoding);
                return "HTTPSTATUS_ERROR:" + statusCode + "::::" + result;
            }
        } catch (ClientProtocolException | HttpHostConnectException | InterruptedException e) {
            log.error("HttpUtils getResult error:", e);
        } catch (IOException e) {
            log.error("HttpUtils getResult IOException:", e);
        } finally {
            request.releaseConnection();
            try {
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                log.error("IOException:", e);
            }
        }
        return emptyStr;
    }

    public HttpUtils() {
        initHttpClient();
    }

    public void setProxy(String host, int port) {
        proxy = new HttpHost(host, port);
    }

    public void enableFidder() {
        //proxy = new HttpHost("127.0.0.1", 8888);
    }

    public void disableProxy() {
        proxy = null;
    }

    public String doGet(String url) {
        HttpGet httpGet = new HttpGet(url);
        return getResult(httpGet);
    }

    public String doGetMap(String url, Map<String, String> params) {
        String result = "";
        try {
            URIBuilder ub = new URIBuilder();
            ub.setPath(url);

            ArrayList<NameValuePair> pairs = covertParams2NVPS(params);
            ub.setParameters(pairs);

            HttpGet httpGet = new HttpGet(ub.build());
            result = getResult(httpGet);
        } catch (URISyntaxException e) {
            log.error("URISyntaxException:", e);
        }
        return result;
    }

    public String doPost(String url) {
        HttpPost httpPost = new HttpPost(url);
        return getResult(httpPost);
    }

    public String doPostMap(String url, Map<String, String> params) {
        return doPostMap(url, params, requestEncoding);
    }

    public HttpResponseBean doPostMapResponse(String url, Map<String, String> params) {
        return doPostMapResponse(url, params, requestEncoding);
    }

    public HttpResponseBean doPostMapResponse(String url, Map<String, String> params, String encoding) {
        HttpResponseBean result = new HttpResponseBean();
        String encodingCharset = requestEncoding;
        if (StringUtils.isNotBlank(encoding)) {
            encodingCharset = encoding;
        }
        try {
            HttpPost httpPost = new HttpPost(url);
            ArrayList<NameValuePair> pairs = covertParams2NVPS(params);
            log.debug("请求接口：{}，报文：{}", url, pairs);
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(pairs, encodingCharset);

            entity.setContentEncoding(encoding);
            httpPost.setEntity(entity);
            result = getResponse(httpPost);
        } catch (UnsupportedEncodingException e) {
            log.error("UnsupportedEncodingException:", e);
        }
        return result;
    }

    public String doPostMap(String url, Map<String, String> params, String encoding) {
        String result = "";
        String encodingCharset = requestEncoding;
        if (StringUtils.isNotBlank(encoding)) {
            encodingCharset = encoding;
        }
        try {
            HttpPost httpPost = new HttpPost(url);
            ArrayList<NameValuePair> pairs = covertParams2NVPS(params);
            log.debug("请求接口：{}，报文：{}", url, pairs);
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(pairs, encodingCharset);

            entity.setContentEncoding(encoding);
            httpPost.setEntity(entity);
            result = getResult(httpPost);
        } catch (UnsupportedEncodingException e) {
            log.error("UnsupportedEncodingException:", e);
        }
        return result;
    }

    public String doPostStr(String url, String postStr) {
        String result = "";

        try {
            HttpPost httpPost = new HttpPost(url);
            StringEntity stringEntity = new StringEntity(postStr, requestEncoding);// 解决中文乱码问题
            stringEntity.setContentEncoding(requestEncoding);
            stringEntity.setContentType("application/x-www-form-urlencoded");
            httpPost.setEntity(stringEntity);
            result = getResult(httpPost);
        } catch (UnsupportedCharsetException e) {
            log.error("UnsupportedCharsetException:", e);
        }

        return result;
    }

    public String doPostJson(String url, String jsonStr) {
        String result = "";
        try {
            HttpPost httpPost = new HttpPost(url);
            StringEntity stringEntity = new StringEntity(jsonStr, requestEncoding);// 解决中文乱码问题
            stringEntity.setContentEncoding(requestEncoding);
            stringEntity.setContentType(jsonContentType);
            httpPost.setEntity(stringEntity);
            result = getResult(httpPost);
        } catch (Exception e) {
            log.error("Exception:", e);
        }
        return result;

    }

    public String doPostUrl(String url, Map<String, String> params) {
        String result = "";
        try {
            URIBuilder ub = new URIBuilder();
            ub.setPath(url);

            ArrayList<NameValuePair> pairs = covertParams2NVPS(params);
            ub.setParameters(pairs);
            String jsonStr = JsonUtils.objtoJson(params);
            StringEntity stringEntity = new StringEntity(jsonStr, requestEncoding);// 解决中文乱码问题
            HttpPost httpPost = new HttpPost(ub.build());

            stringEntity.setContentEncoding(requestEncoding);
            stringEntity.setContentType(jsonContentType);
            httpPost.setHeader("Content-Type", jsonContentType);
            result = getResult(httpPost);
        } catch (URISyntaxException e) {
            log.error("URISyntaxException:", e);
        }
        return result;
    }

    public HttpResponseBean doPostMapResponseAlphaj(String url, Map<String, String> params, String encoding) {
        HttpResponseBean result = new HttpResponseBean();
        String encodingCharset = requestEncoding;
        if (StringUtils.isNotBlank(encoding)) {
            encodingCharset = encoding;
        }
        try {
            URIBuilder ub = new URIBuilder();
            ub.setPath(url);

            ArrayList<NameValuePair> pairs = covertParams2NVPS(params);
            ub.setParameters(pairs);
            String jsonStr = JsonUtils.objtoJson(params);
            StringEntity stringEntity = new StringEntity(jsonStr, encodingCharset);// 解决中文乱码问题
            HttpPost httpPost = new HttpPost(ub.build());

            stringEntity.setContentEncoding(requestEncoding);
            stringEntity.setContentType(jsonContentType);
            httpPost.setHeader("Content-Type", jsonContentType);
            result = getResponse(httpPost);
        } catch (URISyntaxException e) {
        }
        return result;
    }

    /**
     * 获取ip
     **/
    public static String getClientIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_CLUSTER_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_FORWARDED");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_VIA");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("REMOTE_ADDR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    public static String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("X-Real-IP");
        if (null != ip && !"".equals(ip.trim())
                && !"unknown".equalsIgnoreCase(ip)) {
            return ip;
        }
        ip = request.getHeader("X-Forwarded-For");
        if (null != ip && !"".equals(ip.trim())
                && !"unknown".equalsIgnoreCase(ip)) {
            // get first ip from proxy ip
            int index = ip.indexOf(',');
            if (index != -1) {
                return ip.substring(0, index);
            } else {
                return ip;
            }
        }
        return request.getRemoteAddr();
    }

    /**
     * @param ipSection 192.168.1.1-192.168.1.10
     * @param ip
     * @return
     */
    public static boolean ipIsValid(String ipSection, String ip) {
        if (ipSection == null){
            throw new NullPointerException("IP段不能为空！");
        }
        if (ip == null){
            throw new NullPointerException("IP不能为空！");
        }

        if (ipSection.indexOf("*") >= 0) {
            ipSection = (ipSection.replace("*", "0") + "-" + ipSection.replace("*", "255"));
        }

        int count = 0;
        int start = 0;
        while (ip.indexOf(".", start) >= 0 && start < ip.length()) {
            count++;
            start = ip.indexOf(".", start) + ".".length();
        }

        ipSection = ipSection.trim();
        ip = ip.trim();
        final String regxIp = "((25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]\\d|\\d)\\.){" + count + "}(25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]\\d|\\d)";
        final String regxIpb = regxIp + "\\-" + regxIp;
        if (!ipSection.matches(regxIpb) || !ip.matches(regxIp)){
            return false;
        }
        int idx = ipSection.indexOf('-');
        String[] sips = ipSection.substring(0, idx).split("\\.");
        String[] sipe = ipSection.substring(idx + 1).split("\\.");
        String[] sipt = ip.split("\\.");
        long ips = 0L, ipe = 0L, ipt = 0L;

        for (int i = 0; i < (count + 1); ++i) {
            ips = ips << 8 | Integer.parseInt(sips[i]);
            ipe = ipe << 8 | Integer.parseInt(sipe[i]);
            ipt = ipt << 8 | Integer.parseInt(sipt[i]);
        }
        if (ips > ipe) {
            long t = ips;
            ips = ipe;
            ipe = t;
        }
        return ips <= ipt && ipt <= ipe;
    }

    /**
     * 获取浏览器信息
     * @param request
     * @return
     */
    public static String getBrowserName(HttpServletRequest request) {
        String agent = request.getHeader("User-Agent").toLowerCase();
        if (agent.indexOf("msie 7") > 0) {
            return "ie7";
        } else if (agent.indexOf("msie 8") > 0) {
            return "ie8";
        } else if (agent.indexOf("msie 9") > 0) {
            return "ie9";
        } else if (agent.indexOf("msie 10") > 0) {
            return "ie10";
        } else if (agent.indexOf("msie") > 0) {
            return "ie";
        } else if (agent.indexOf("chrome") > 0) {
            return "chrome";
        } else if (agent.indexOf("opera") > 0) {
            return "opera";
        } else if (agent.indexOf("firefox") > 0) {
            return "firefox";
        } else if (agent.indexOf("webkit") > 0) {
            return "webkit";
        } else if (agent.indexOf("gecko") > 0 && agent.indexOf("rv:11") > 0) {
            return "ie11";
        } else {
            return "Others";
        }
    }
}
