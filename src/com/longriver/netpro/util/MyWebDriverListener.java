package com.longriver.netpro.util;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.events.AbstractWebDriverEventListener;

public class MyWebDriverListener extends AbstractWebDriverEventListener {
	/**
	* @author Selenium Monster
	*/
	@Override
	public void onException(Throwable arg0, WebDriver arg1) {
		System.out.println("There is an exception in the script, please find the below error description" );
		arg0.printStackTrace();
	}
	
}
