package com.nmedia.util;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;

import javax.servlet.ServletContext;

import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.context.ServletContextAware;

import com.jolbox.bonecp.BoneCPDataSource;

public class SpringUtil implements ApplicationContextAware,ServletContextAware{
	private static Logger logger = Logger.getLogger(SpringUtil.class);
	
	public static final String jmsTemplate = "fetchJmsTemplate"; //
	public static final String publisherJmsTemplate = "publisherJmsTemplate"; 
	public static final String publicDBSource = "publicDBSource"; //本地数据库连接
	//此方法会重新加载spring,故不用
//	public static ApplicationContext ct=new ClassPathXmlApplicationContext("/config/spring/spring-servlet.xml"); ;
	public static ApplicationContext ct;
	public static ServletContext servletContext; 
	//从数据库中获得数据库连接
	public static Connection getSpringBean(String beanId){
		BoneCPDataSource datasource=(BoneCPDataSource)ct.getBean(beanId); 
		Connection conn = null;
		try {
			conn=(Connection) datasource.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return conn;
	}
	public static JmsTemplate getFetchJmsTemplate(){
		return (JmsTemplate)ct.getBean(jmsTemplate);
	}
	public static JmsTemplate getBigVTemplate(){
		return (JmsTemplate)ct.getBean("bigVTemplate");
	}
	public static JmsTemplate getPublisherJmsTemplate(){
		return (JmsTemplate)ct.getBean(publisherJmsTemplate);
	}
	public static Connection getPublicConnection(){
		return getSpringBean("publicDBSource");
	}
	//spring中获得bean
	public static Object getObject(String beanId){
		return ct.getBean(beanId);
	}
	//获得web.xml配置文件参数
	public static String getContextParams(String name){
		return servletContext.getInitParameter(name);
	}
	//获得项目路径
	public static String getRootURI(){
		String key = servletContext.getInitParameter("webAppRootKey");
		return System.getProperty(key);
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Connection conn = getSpringBean(null);
		CallableStatement statement = null;
		String sql = "{call InsertBBSBloger(?,?,?)}";
		try {
			statement = conn.prepareCall(sql);
			statement.setString(1, "陵少");
			statement.setString(2, "http://3g.tianya.cn/user/art_more.jsp");
			statement.registerOutParameter(3, Types.INTEGER);
			statement.execute();
			//输出：
            int webid = statement.getInt(3);
            System.out.println(webid+"===");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	@Override
	public void setApplicationContext(ApplicationContext arg0) throws BeansException {
		SpringUtil.ct = arg0;
	}
	@Override
	public void setServletContext(ServletContext arg0) {
		servletContext = arg0;
	}
}
