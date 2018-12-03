package com.longriver.netpro.common.dbSource;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * 动态数据源类
 * 这个类必须继承AbstractRoutingDataSource，
 * 且实现方法 determineCurrentLookupKey，该方法返回一个Object，一般是返回字符串
 * @author Administrator
 *
 */
public class DynamicDataSource extends AbstractRoutingDataSource {

	@Override
	protected Object determineCurrentLookupKey() {
		// 在进行DAO操作前，通过上下文环境变量，获得数据源的类型
		return DataSourceHandle.getDataSourceType();
	}
}