package com.longriver.netpro.util;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;

public class WebDriverUtil {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	//判断是否存在包含期望关键字的元素
	public static boolean isContentAppeared(WebDriver driver,String content) {  
		boolean status = false;  
		try {  
			driver.findElement(By.xpath("//*[contains(.,'" + content + "')]"));  
			System.out.println(content + " is appeard!");  
			status = true;  
			} catch (NoSuchElementException e) {  
				status = false;  
				System.out.println("'" + content + "' doesn't exist!");  
		}  
		return status;  
	}

}
