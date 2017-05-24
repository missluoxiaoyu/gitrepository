package com.caiex.account.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Md5Util {
	
	 /**  
     * 生成md5编码字符串.  
     * @param str 源字符串  
     * @param charset 编码方式  
     * @return  
     *  
     */  
    public static String md5(String str,String charset) {   
        if (str == null)   
            return null;   
        char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',   
                'a', 'b', 'c', 'd', 'e', 'f' };   
           
        MessageDigest md5MessageDigest = null;   
        byte[] md5Bytes = null;   
        char md5Chars[] = null;   
        byte[] strBytes = null;   
        try {   
            strBytes = str.getBytes(charset);   
            md5MessageDigest = MessageDigest.getInstance("MD5");   
            md5MessageDigest.update(strBytes);   
            md5Bytes = md5MessageDigest.digest();   
            int j = md5Bytes.length;   
            md5Chars = new char[j * 2];   
            int k = 0;   
            for (int i = 0; i < j; i++) {   
                byte md5Byte = md5Bytes[i];   
                md5Chars[k++] = hexDigits[md5Byte >>> 4 & 0xf];   
                md5Chars[k++] = hexDigits[md5Byte & 0xf];   
            }
            String value = new String(md5Chars);
            if(value!=null){
            	value = value.toLowerCase();
            }
            return value;
        } catch (NoSuchAlgorithmException e) {   
            //Log.output(e.toString(), Log.STD_ERR);   
            return null;   
        } catch (UnsupportedEncodingException e) {   
            //Log.output(e.toString(), Log.STD_ERR);   
            return null;
        } finally {
            md5MessageDigest = null;
            strBytes = null;
            md5Bytes = null;
        }
    }
    
    /**  
     * 生成md5编码字符串. 
     * @param str 源字符串  
     * @param utf8 编码方式  
     * @return  
     *  
     */  
    public static String md5(String str) {
        return   md5(str, "UTF-8");
    }
}
