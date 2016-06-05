/**
 * 
 */
package com.hfxb.app.core.utils;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 * Http相关工具方法
 * @author qq
 *
 */
public class HttpUtils {
	
	/**
	 * 获取访问用户的真实ip（包括用户使用多级代理或者反向代理时的真实ip）
	 * @return
	 */
	public static String getIpAddr(HttpServletRequest request){
	   
       String ip = request.getHeader("x-forwarded-for");
       if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
           ip = request.getHeader("Proxy-Client-IP");
       }
       if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
           ip = request.getHeader("WL-Proxy-Client-IP");
       }
       if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
           ip = request.getRemoteAddr();
       }
       return ip; 
	}

    public static String getAddressByIP(String strIP)
    {
        try
        {
            URL url = new URL( "http://ip.qq.com/cgi-bin/searchip?searchip1=" + strIP);
            URLConnection conn = url.openConnection();
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "GBK"));
            String line = null;
            StringBuffer result = new StringBuffer();
            while((line = reader.readLine()) != null)
            {
                result.append(line);
            }
            reader.close();
            strIP = result.substring(result.indexOf( "该IP所在地为：" ));
            strIP = strIP.substring(strIP.indexOf( "：") + 1);
            return  strIP;
        }
        catch( IOException e)
        {
            return "读取失败";
        }
    }

}
