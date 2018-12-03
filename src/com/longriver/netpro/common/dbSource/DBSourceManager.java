package com.longriver.netpro.common.dbSource;

import org.apache.log4j.Logger;


public class DBSourceManager {
	private static Logger logger = Logger.getLogger(DBSourceManager.class);
	/** ==================== DBSource key ==================== **/
	/** public **/
	public static String publicDBSource = "public";
	
	/**
	 * 公共库数据源
	 */
	public static void setPublicDB(){
		logger.info(publicDBSource);
		DataSourceHandle.setDataSourceType(publicDBSource);
	}
	/**
	 * 将数据源改为默认的数据源
	 */
	public static void setDefaultDB(){
		setPublicDB();
	}
}

