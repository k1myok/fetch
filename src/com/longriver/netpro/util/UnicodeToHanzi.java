package com.longriver.netpro.util;

/**
 * unicode和中文互转
 * @author rhy
 * @date 2018-6-25 下午3:16:04
 * @version V1.0
 */
public class UnicodeToHanzi {
	
	public static void main(String[] args) {
		String result = decodeUnicode("\u7cfb\u7edf\u7e41\u5fd9");
		System.out.println(result);
	}
	//中文转Unicode  
    public static String gbEncoding(final String gbString) {   //gbString = "测试"  
        char[] utfBytes = gbString.toCharArray();   //utfBytes = [测, 试]  
        String unicodeBytes = "";     
        for (int byteIndex = 0; byteIndex < utfBytes.length; byteIndex++) {     
            String hexB = Integer.toHexString(utfBytes[byteIndex]);   //转换为16进制整型字符串  
              if (hexB.length() <= 2) {     
                  hexB = "00" + hexB;     
             }     
             unicodeBytes = unicodeBytes + "\\u" + hexB;     
        }     
        System.out.println("unicodeBytes is: " + unicodeBytes);     
        return unicodeBytes;     
    }  
  //Unicode转中文  
    public static String decodeUnicode(final String dataStr) {     
       int start = 0;     
       int end = 0;     
       final StringBuffer buffer = new StringBuffer();     
       while (start > -1) {     
           end = dataStr.indexOf("\\u", start + 2);     
           String charStr = "";     
           if (end == -1) {     
               charStr = dataStr.substring(start + 2, dataStr.length());     
           } else {     
               charStr = dataStr.substring(start + 2, end);     
           }     
           char letter = (char) Integer.parseInt(charStr, 16); // 16进制parse整形字符串。     
           buffer.append(new Character(letter).toString());     
           start = end;     
       }     
       return buffer.toString();     
    }  
}
