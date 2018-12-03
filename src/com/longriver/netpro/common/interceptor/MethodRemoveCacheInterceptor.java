package com.longriver.netpro.common.interceptor;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
/**
 * <b>function:</b> 缓存方法拦截器核心代码 
 * @author lilei
 * @createDate 2012-7-2 下午06:05:34
 * @file MethodRemoveCacheInterceptor.java
 * @email lilei1929@163.com
 * @version 1.0
 */
public class MethodRemoveCacheInterceptor implements MethodInterceptor, InitializingBean{
	
    private static final Logger logger = Logger.getLogger(MethodRemoveCacheInterceptor.class);
    private Cache cache;
    public void setCache(Cache cache) {
        this.cache = cache;
    }
	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		
		String targetName = invocation.getThis().getClass().getName();
        String methodName = invocation.getMethod().getName();
        Object[] arguments = invocation.getArguments();
        String cacheKey = getCacheKey(targetName, methodName, arguments);
        Element element = null;
        synchronized (this) {
            element = cache.get(cacheKey);
            if (element == null) {
            	logger.info(cacheKey + "加入到缓存： " + cache.getName());
                // 调用实际的方法
                invocation.proceed();
                cache.removeAll();
                logger.info("remove all, cacheName="+cache.getName());
            } else {
            	logger.info(cacheKey + "使用缓存： " + cache.getName());
            }
        }
		return 1;
	}
	private String getCacheKey(String targetName, String methodName, Object[] arguments) {
        StringBuffer sb = new StringBuffer();
        sb.append(targetName).append(".").append(methodName);
        if ((arguments != null) && (arguments.length != 0)) {
            for (int i = 0; i < arguments.length; i++) {
                sb.append(".").append(arguments[i]);
            }
        }
        return sb.toString();
    }
	@Override
	public void afterPropertiesSet() throws Exception {
		logger.info(cache + " A cache is required. Use setCache(Cache) to provide one.");
	}

}
