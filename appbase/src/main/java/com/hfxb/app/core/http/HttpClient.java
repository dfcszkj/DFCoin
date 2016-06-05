package com.hfxb.app.core.http;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.cookie.Cookie;
import org.apache.http.cookie.CookieSpecProvider;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.cookie.BestMatchSpecFactory;
import org.apache.http.impl.cookie.BrowserCompatSpecFactory;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HttpClient {

	private static final Logger logger = LoggerFactory.getLogger(HttpClient.class);
	
    private final String _DEFLAUT_CHARSET = "utf-8";
    private int timeout = 30*1000;
    
    private CloseableHttpClient httpClient;
    
	// 设置超时，毫秒级别
    private RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(timeout).setConnectTimeout(timeout).build();
    // 设置cookie策略
    private BasicCookieStore cookieStore = new BasicCookieStore();
	
	public HttpClient(){init();}
	
    public HttpClient(int timeout) {
    	this.timeout = timeout;
    	init();
    }
    
    private void init(){
        try {
            RegistryBuilder.<CookieSpecProvider> create().register(CookieSpecs.BEST_MATCH,
                            new BestMatchSpecFactory()).register(CookieSpecs.BROWSER_COMPATIBILITY, new BrowserCompatSpecFactory()).build();
            RequestConfig globalConfig = RequestConfig.custom().setCookieSpec("easy").build();
            httpClient = HttpClients.custom().setDefaultRequestConfig(globalConfig)
			                    .setDefaultCookieStore(cookieStore).build();
        } catch (Exception e) {
            logger.error("create httpclient error", e);
        }
    }
 
    public void setTimeOut(int socketTimeOut, int connectTimeOut) {
        requestConfig = RequestConfig.custom().setSocketTimeout(socketTimeOut).setConnectTimeout(connectTimeOut).build();
    }
 
    public String get(String url) {
        return get(url, _DEFLAUT_CHARSET);
    }
 
    public String get(String url, String charset) {
        return get(url, null, charset);
    }
    
    public String get(String url,Map<String, String> headers) {
    	return get(url,headers, _DEFLAUT_CHARSET);
    }
 
    public String get(String url,Map<String, String> headers, String charset) {
        HttpClientContext context = HttpClientContext.create();
        context.setCookieStore(cookieStore);
        String useCharset = charset;
        if (charset == null) {
            useCharset = _DEFLAUT_CHARSET;
        }
        HttpGet httpGet = new HttpGet(url);
        if (headers != null) {
            for (String key : headers.keySet()) {
                httpGet.setHeader(key, headers.get(key));
            }
        }
        httpGet.setConfig(requestConfig);
        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(httpGet, context);
            HttpEntity entity = response.getEntity();
            String result = EntityUtils.toString(entity, useCharset);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        	try {
				if (response != null) {
					response.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
        }
        return null;
    }
 
    public String post(String url, Map<String, String> params) {
        return post(url, params, null, null);
    }
 
    public String post(String url, Map<String, String> params,
            Map<String, String> headers) {
        return post(url, params, headers, null);
    }
 
    public String post(String url, Map<String, String> params, String charset) {
        return post(url, params, null, charset);
    }
 
    public String post(String url, Map<String, String> params,
            Map<String, String> headers, String charset) {
        HttpClientContext context = HttpClientContext.create();
        context.setCookieStore(cookieStore);
        String useCharset = charset;
        if (charset == null) {
            useCharset = _DEFLAUT_CHARSET;
        }
        CloseableHttpResponse response = null;
        String result = null;
        try {
        	logger.debug("post url: " + url);
            HttpPost httpPost = new HttpPost(url);
            if (headers != null) {
                for (String key : headers.keySet()) {
                    httpPost.setHeader(key, headers.get(key));
                }
            }
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            if (params != null) {
                for (String key : params.keySet()) {
                    nvps.add(new BasicNameValuePair(key, params.get(key)));
                }
            }
    		
    		httpPost.setEntity(new UrlEncodedFormEntity(nvps, charset));
            httpPost.setConfig(requestConfig);
            
            response = httpClient.execute(httpPost, context);
            HttpEntity entity = response.getEntity();
            result = EntityUtils.toString(entity, useCharset);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        	if(response != null) {
        		try {
					response.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
        	}
        }
        return result;
    }
 
    public String getCookie(String key) {
        List<Cookie> cookies = cookieStore.getCookies();
        if (cookies != null) {
            for (Cookie c : cookies) {
                if (c.getName().equals(key)) {
                    return c.getValue();
                }
            }
        }
        return null;
    }
 
    public void showCookies() {
        List<Cookie> cookies = cookieStore.getCookies();
        if (cookies != null) {
            for (Cookie c : cookies) {
            	logger.debug("cookie=name:{}\tvalue:{}\tdomain:{}\texpires:{}\tpath:{}\tversion:{}",
            			new Object[]{c.getName(), c.getValue(), c.getDomain(), c.getExpiryDate(), c.getPath(), c.getVersion()});
            }
        }
    }
    
}
