package com.longriver.netpro.util;

import java.io.UnsupportedEncodingException;
import java.util.Random;

/**
 * 随机生成常见的汉字
 * @author lilei
 */
public class GeneChar {
    public static void main(String[] args) {

    	getName();
    	getName();
    	getName();
    	getName();
    }
    public static String getName(){
    	int num = StringUtil.getRandom();
    	String name = "";
    	for (int i = 1; i < num; i++) {
            name += getRandomChar();
        }
    	System.out.println(name);
    	return name;
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
            str = new String(b, "gb2312");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            System.out.println("错误");
        }

        return str.charAt(0);
    }
}
