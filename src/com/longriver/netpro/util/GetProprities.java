package com.longriver.netpro.util;

public class GetProprities {
	
	public static Configur PaginationConfig = new Configur("config/properties/PaginationConfig.properties");
	public static Configur paramsConfig = new Configur("config/properties/paramsConfig.properties");
	public static Configur AsdlConfig = new Configur("config/properties/asdlConfig.properties");
	//基本没什么用
	public static String getMQAddress(){
		return "tcp://"+PaginationConfig.getProperty("mqAdd")+":61616";
	}
	
}
