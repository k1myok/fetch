package com.longriver.netpro.common.dbSource;

/**
 * 改变数据源类
 * @author Administrator
 *
 */
public class DataSourceHandle {
	/**
	 * 线程本地环境
	 */
	@SuppressWarnings("unchecked")
	private static final ThreadLocal contextHolder = new ThreadLocal();

	/**
	 * 设置数据源类型
	 * @param dataSourceType
	 */
	@SuppressWarnings("unchecked")
	public static void setDataSourceType(String dataSourceType) {
		contextHolder.set(dataSourceType);
	}

	/**
	 * 获取数据源类型
	 * @return
	 */
	public static String getDataSourceType() {
		return (String) contextHolder.get();
	}

	/**
	 * 清除数据源类型
	 */
	public static void clearDataSourceType() {
		contextHolder.remove();
	}
}

