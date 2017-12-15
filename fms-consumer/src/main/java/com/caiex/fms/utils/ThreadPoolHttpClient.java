package com.caiex.fms.utils;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;


public class ThreadPoolHttpClient {

	private static Logger logger = LogManager.getLogger(ThreadPoolHttpClient.class); 
	private static PoolingHttpClientConnectionManager poolConnManager = null;
	private final int maxTotalPool = Runtime.getRuntime().availableProcessors() * 100;
	private final int maxConPerRoute = 20;
	
	private final int connectionRequestTimeout = 50000;
	
	private final int socketTimeout = 10000;
	
	private final int connectTimeout = 10000;

	private static class ThreadPoolHttpClientHolder{
		private static final ThreadPoolHttpClient instance = new  ThreadPoolHttpClient();
	}
	
	private ThreadPoolHttpClient(){
		if(poolConnManager == null){
			init();
		}
	}
	
	public static final ThreadPoolHttpClient getInstance(){
		return ThreadPoolHttpClientHolder.instance;
	}
	
	public void init() {
		
		SSLContext sslcontext = null;
		try {
			// 需要通过以下代码声明对https连接支持
			sslcontext = SSLContexts.custom()
					.loadTrustMaterial(null, new TrustSelfSignedStrategy(){
						public boolean isTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {  
		                    return true;  
		                }  
					})
					.build();
			
			/*SSLContext sslcontext = SSLContexts.custom()
					.loadTrustMaterial(null, new TrustSelfSignedStrategy())
					.build();*/
			HostnameVerifier hostnameVerifier = SSLConnectionSocketFactory.getDefaultHostnameVerifier();//SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;SSLConnectionSocketFactory.getDefaultHostnameVerifier();//SSLConnectionSocketFactory.getDefaultHostnameVerifier();  //ALLOW_ALL_HOSTNAME_VERIFIER
			SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
					sslcontext, hostnameVerifier);
			
			Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder
					.<ConnectionSocketFactory> create()
					.register("http",
							PlainConnectionSocketFactory.getSocketFactory())
					.register("https", sslsf).build();
			// 初始化连接管理器
			poolConnManager = new PoolingHttpClientConnectionManager(
					socketFactoryRegistry);
			// Increase max total connection to 200
			poolConnManager.setMaxTotal(maxTotalPool);
			// Increase default max connection per route to 20
			poolConnManager.setDefaultMaxPerRoute(maxConPerRoute);
		
		}catch (KeyManagementException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (KeyStoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	// 获取连接
	public  CloseableHttpClient getConnection() {
		 RequestConfig requestConfig = RequestConfig.custom().setConnectionRequestTimeout(connectionRequestTimeout)  
	                .setConnectTimeout(connectTimeout).setSocketTimeout(socketTimeout).build();  
	        CloseableHttpClient httpClient = HttpClients.custom()  
	                    .setConnectionManager(poolConnManager).setDefaultRequestConfig(requestConfig).build();  
	        if(poolConnManager != null && poolConnManager.getTotalStats() != null){  
	            logger.info("now client pool "+poolConnManager.getTotalStats().toString());  
	        }  

		return httpClient;
	}

	// 发送请求 url为请求url，paramsMap为请求参数
    public String post(String url, Map<String, String> paramsMap)  
    {  
       // Boolean respCode = true;  
        String resopnse = null;  
        //参数检测  
        if(url==null || "".equals(url))  
        	return null;  
        
        HttpPost httpPost = new HttpPost(url);  
        try {  
              
            List <NameValuePair> nvps = new ArrayList <NameValuePair>();  
            Set<String> keySet = paramsMap.keySet();
            Iterator<String> it = keySet.iterator();
            for(;it.hasNext(); ){
            	String paramName = it.next();
            	nvps.add(new BasicNameValuePair(paramName, paramsMap.get(paramName)));  
            }
            
            httpPost.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8")); 
            
            logger.info(paramsMap.get("agentid") + " " + paramsMap.get("tkId") + " " + paramsMap.get("sign") + " " + " " + paramsMap.get("status") + System.currentTimeMillis() + " 开始发送 请求：url "+url);  
            
            CloseableHttpClient client = getConnection();
            CloseableHttpResponse response = client.execute(httpPost);  
            int status = response.getStatusLine().getStatusCode();  
            HttpEntity entity =  response.getEntity();
            
            if (status >= 200 && status < 300) {  
                if(entity != null)  {  
                    resopnse = EntityUtils.toString(entity,"utf-8");  
                }  
                
                logger.info(paramsMap.get("tkId") + "   " + System.currentTimeMillis() + " 接收响应：url " + url + " status=" + status + "   response = "+JSON.parse(resopnse)); 
               // return entity != null ? resopnse : null;  
            } else {   
                httpPost.abort();  
             //   respCode = false;
                logger.info(paramsMap.get("tkId") + "   " +System.currentTimeMillis() + " 接收响应：url "+url+" status="+status+" resopnse="+EntityUtils.toString(entity,"utf-8"));  
                logger.error("Unexpected response status: " + status);
                //    throw new ClientProtocolException("Unexpected response status: " + status);  
            }  
        } catch (Exception e) {  
        	//logger.warn(paramsMap.get("tkId") + " url = " + url + ExceptionUtils.getErrorInfoFromException(e));
            httpPost.abort();  
            return null;
        //    respCode = false;
        }   
        return resopnse;  
    }  
    
    
    public String get(String url, Map<String, String> paramsMap){
    	//Boolean respCode = true;  
   	  	String resopnse = null;  
    	InputStream ins = null;
        //参数检测  
        if(url==null || "".equals(url))  
          return null;
        
        HttpGet httpGet = null;
        try {  
        	url += "?";
             Set<String> keySet = paramsMap.keySet();
             Iterator<String> it = keySet.iterator();
             for(;it.hasNext(); ){
             	String paramName = it.next();
             	url += paramName+"="+paramsMap.get(paramName)+"&";
             }
            url = url.substring(0, url.length()-1);
            
            logger.info(paramsMap.get("tkId") + "   "+ System.currentTimeMillis() + " 开始发送 请求：url "+url);  
            
            httpGet = new HttpGet(url);
            CloseableHttpClient client = getConnection();
            CloseableHttpResponse response = client.execute(httpGet);  
            
            int status = response.getStatusLine().getStatusCode();  
            HttpEntity entity =  response.getEntity();
            
            if (status >= 200 && status < 300) {  
                  if(entity != null)  {  
                      resopnse = EntityUtils.toString(entity,"utf-8");  
                  }  
                  logger.info(paramsMap.get("tkId") + "   "+" 接收响应：url="+url+" status="+status+"   response = "+JSON.parse(resopnse));  
            } else {   
            	httpGet.abort();  
                logger.info(paramsMap.get("tkId") + "   " +System.currentTimeMillis() + " 接收响应：url "+url+" status="+status+" resopnse="+EntityUtils.toString(entity,"utf-8"));  
                throw new ClientProtocolException("Unexpected response status: " + status);  
            }  
        } catch (Exception e) {  
        	//logger.warn(paramsMap.get("tkId") + " url = " + url + ExceptionUtils.getErrorInfoFromException(e));
        	httpGet.abort();  
        	//respCode = false;
        } finally {
            try {
                if (ins != null) {
                 ins.close();
                 ins = null;
               }
              } catch (IOException e1) {
            	     e1.printStackTrace();
               }
        }
        return resopnse;
    }
    
    
    
	public static void main(String[] args) {
		Map<String, String> map = new HashMap<String,String>();
	/*	map.put("tkId", "101");
	//	map.put("sellTime", "2015-04-21 04:19:39");
	//	map.put("totalInv", "20");
		map.put("agentid", "101");
		map.put("uid", "555489");
		map.put("bonus", "100");
		map.put("investInfo", "FSGL>10>201512151996*HAD|X@1.15");*/
		//ThreadPoolHttpClient.getInstance().post("https://services.caiex.com/oltp-api/price/getPriceParam", map);
		map.put("tkId", "DD20161121210227100002");
		map.put("tradePrice","1225.6");
		map.put("status", "true");
		ThreadPoolHttpClient.getInstance().post("https://www.baidu.com", map);  //http://pay.dididapiao.com/oltpNotify/sendTrade
		
		
	/*	map.put("tid", "1631");
		map.put("tradePrice", "6.76");
		map.put("status", "false");
		ThreadPoolHttpClient.getInstance().post("http://www.caiex.com/caiexWx/orderTrade.do", map);*/
		// 开启通知渠道商交易结果的线程
		/*ThreadPool.getInstance().getExecutorService()
				.execute(new Runnable() {
					public void run() {
						// "http://192.168.1.111:8080/oltp-home/saveOrder";
						Map<String, String> map = new HashMap<String, String>();
						map.put("tid", ""+ ticket.getTid());
						map.put("tradePrice", ticket.getTotal()+"");
						map.put("status", "true");

						String url = getAgentUrl(agentInfoDao, ticket.getAgent_id());
						logger.info("推送结果的url = "+url );
						if(url != null && !"".equals(url)){
							ThreadPoolHttpClient.getInstance().post(url, map);
						}else{
							logger.info("渠道商推送交易结果的url不存在  agentid = "+ ticket.getAgent_id());
						}
						
					}
				});*/
	
	}
}