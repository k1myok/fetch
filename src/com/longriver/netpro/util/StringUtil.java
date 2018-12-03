package com.longriver.netpro.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Random;

public class StringUtil {
	
	public static void main(String args[]){
//		System.out.println(decodeUnicode("\"msg\":\"\u76f8\u540c\u5185\u5bb9\u8bf7\u969410\u5206\u949f\u518d\u8fdb\u884c"));
//		System.out.println(getURLDecoderString("%E7%96%8F%E8%A7%A3%E6%95%B4%E6%B2%BB%E4%BF%83%E6%8F%90%E5%8D%87%E4%B8%93%E9%A1%B9%E8%A1%8C%E5%8A%A8%E8%A6%81%E5%9D%9A%E6%8C%81%E6%B0%91%E6%9C%89%E6%89%80%E5%91%BC%E6%88%91%E6%9C%89%E6%89%80%E5%BA%94&"));
		try {
			System.out.println("----");
			System.out.println(formatString(URLDecoder.decode("http://s.weibo.com/weibo/%23%E7%83%AD%E6%90%9C%E8%A1%A8%E6%83%85%23","utf-8")));
			System.out.println(URLDecoder.decode("http://s.weibo.com/weibo/%23%E7%83%AD%E6%90%9C%E8%A1%A8%E6%83%85%23","utf-8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
     * URL 转码
     *
     * @return String
     * @author lifq
     * @date 2015-3-17 下午04:10:28
     */
    public static String getURLEncoderString(String str) {
        String result = "";
        if (null == str) {
            return "";
        }
        try {
            result = java.net.URLEncoder.encode(str, "gbk");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }
	/**
     * URL 解码
     *
     * @return String
     * @author lifq
     * @date 2015-3-17 下午04:09:51
     */
    public static String getURLDecoderString(String str) {
        String result = "";
        if (null == str) {
            return "";
        }
        try {
            result = java.net.URLDecoder.decode(str, "gbk");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }
	public static int getRandom(){
		int max=7;
        int min=3;
        Random random = new Random();
        int s = random.nextInt(max)%(max-min+1) + min;
        return s;
	}
	public static String randomString(){  
        int length = getRandom();
        StringBuilder s = new StringBuilder(50);  
        Random random = new Random();     
        for( int i = 0; i < length; i ++) {     
            int choice = random.nextInt(2) % 2 == 0 ? 65 : 97; // 取得大写还是小写     
            s.append((char)(choice + random.nextInt(26)));     
        }  
        return s.toString();  
    }  
	public static String randomName(){  
		int length = getRandom();
		StringBuilder s = new StringBuilder(50);  
		for( int i = 0; i < length; i ++) {     
			s.append((char)(getRandomChar()));     
		}  
		return s.toString();  
	}  
	public static String randomQq(){  
		Random random1 = new Random();
		int max=10;
        int min=9;
		int length = random1.nextInt(max)%(max-min+1) + min;
		String s = "";
	    for(int i=0;i<length;i++) {
	    	s += (int)(10*(Math.random()));
	    }
		return s;  
	}  
	public static String encode(String str){
		if(str==null) return str;

		try {
			str = URLEncoder.encode(str,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return str;
	}
	public static String decode(String str){

		if(str==null) return str;

		try {
			str = URLDecoder.decode(str,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return str;
	}
	public static String formatString(String str) {
        if (str != null) {            
        	try {                
        		str = new String(str.getBytes("ISO-8859-1"), "UTF-8");            
        	} catch (Exception e) {}        
        }
        return str;    
    }
	/**
	 * 过滤掉字符串中HTML标签
	 * @param input
	 * @return
	 */
	public static String filterHTMl(String input){
		if(input == null) return input;
		String str = input.replaceAll("<[a-zA-Z]+[1-9]?[^><]*>", "").replaceAll("</[a-zA-Z]+[1-9]?>", "");
		str = str.replaceAll("&nbsp;", "");
		str = str.replace("：", "");
		return str;
	}

	private static char getRandomChar() {
        String str = "";
        int hightPos; //
        int lowPos;

        Random random = new Random();

        hightPos = (176 + Math.abs(random.nextInt(39)));
        lowPos = (161 + Math.abs(random.nextInt(93)));

        byte[] b = new byte[2];
        b[0] = (Integer.valueOf(hightPos)).byteValue();
        b[1] = (Integer.valueOf(lowPos)).byteValue();

        try {
            str = new String(b, "GBK");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            System.out.println("错误");
        }

        return str.charAt(0);
    }

	public static String decodeUnicode(String theString) {      
	    char aChar;      
	     int len = theString.length();      
	    StringBuffer outBuffer = new StringBuffer(len);      
	    for (int x = 0; x < len;) {      
	     aChar = theString.charAt(x++);      
	     if (aChar == '\\') {      
	      aChar = theString.charAt(x++);      
	      if (aChar == 'u') {      
	       // Read the xxxx      
	       int value = 0;      
	       for (int i = 0; i < 4; i++) {      
	        aChar = theString.charAt(x++);      
	        switch (aChar) {      
	        case '0':      
	        case '1':      
	        case '2':      
	        case '3':      
	       case '4':      
	        case '5':      
	         case '6':      
	          case '7':      
	          case '8':      
	          case '9':      
	           value = (value << 4) + aChar - '0';      
	           break;      
	          case 'a':      
	          case 'b':      
	          case 'c':      
	          case 'd':      
	          case 'e':      
	          case 'f':      
	           value = (value << 4) + 10 + aChar - 'a';      
	          break;      
	          case 'A':      
	          case 'B':      
	          case 'C':      
	          case 'D':      
	          case 'E':      
	          case 'F':      
	           value = (value << 4) + 10 + aChar - 'A';      
	           break;      
	          default:      
	           throw new IllegalArgumentException(      
	             "Malformed   \\uxxxx   encoding.");      
	          }      
	   
	        }      
	         outBuffer.append((char) value);      
	        } else {      
	         if (aChar == 't')      
	          aChar = '\t';      
	         else if (aChar == 'r')      
	          aChar = '\r';      
	   
	         else if (aChar == 'n')      
	   
	          aChar = '\n';      
	   
	         else if (aChar == 'f')      
	   
	          aChar = '\f';      
	   
	         outBuffer.append(aChar);      
	   
	        }      
	   
	       } else     
	   
	       outBuffer.append(aChar);      
	   
	      }      
	   
	      return outBuffer.toString();      
	}     
}
