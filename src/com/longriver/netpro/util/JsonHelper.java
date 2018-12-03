package com.longriver.netpro.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.longriver.netpro.webview.entity.FetchTaskRiceverBean;
import com.longriver.netpro.webview.entity.TaskGuideBean;

public class JsonHelper {
	
	private static Logger logger = Logger.getLogger(JsonHelper.class);

	public static void main(String[] args) {
//		String json = "{'items':[{'cid':1,'crontab':'0 0/20 10-11 8 5 ?','id':1500,type:'sina.status','ifid':0,'taskid':908,'url':'http://www.hao123.com/'}],'totalRecords':1}";
//		FetchTaskRiceverBean jh = (FetchTaskRiceverBean)parseJson2Object(json,FetchTaskRiceverBean.class);
//		System.out.println(jh.getId()+"---"+jh.getTotalRecords());
//		List<FetchTaskRiceverBean> list = jh.getRlist();
//		for(int i=0;i<list.size();i++){
//			System.out.println(list.get(i).getType()+"---"+list.get(i).getCrontab());
//		}
		
		String json1 = "{'account':3,'accountInfo':[{'areaid':3,'corpus':'当我第一次知道要拍传奇广告的时候，其实我是......是拒绝的！','hostIp':'192.168.1.11','hostName':'123','hostPassword':'123','hostPort':80,'id':1,'nick':'测试001','password':'234'},{'areaid':2,'corpus':'我跟游戏厂商讲，我拒绝','hostIp':'61.135.169.125','hostName':'吃饭v','hostPassword':'1234','hostPort':56,'id':16,'nick':'123','password':'123','url':'http://weibo.com/u/123'}],'address':'http://www.baidu.com/s','category':2,'corpus':0,'id':42,'name':'测试json','number':10,'spaceTimeEnd':2,'spaceTimeStart':1,'startTime':'2015-05-08 10:04:37.0'}";
		TaskGuideBean jh1 = (TaskGuideBean)parseJson2Object(json1,TaskGuideBean.class);
		System.out.println(jh1.getId()+"---"+jh1.getAccount()+"---"+jh1.getAddress());
		List<TaskGuideBean> list1 = jh1.getRlist();
		for(int i=0;i<list1.size();i++){
			System.out.println(list1.get(i).getPassword()+"---"+list1.get(i).getNick());
		}
	}
	/**
	 * 浠巎son瀛楃涓蹭腑瑙ｆ瀽鍑簀ava瀵硅薄
	 * json1={"name":"ll","age":"20","sex":"鐢?"}
	 * @param jsonStr
	 * @param clazz
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Object parseJson2Object(String jsonStr,Class clazz){
		return JSON.toJavaObject(JSON.parseObject(jsonStr), clazz);
	}
	/**
	 * json=[{"name":"ll","age":"20","sex":"鐢?"}]
	 * @param jsonStr
	 * @return
	 */
	public static List<FetchTaskRiceverBean> parseJson2Object_2(String jsonStr){
		JSONArray array = JSONArray.parseArray(jsonStr);
		List<FetchTaskRiceverBean> list = new ArrayList<FetchTaskRiceverBean>();
		for(int i = 0; i < array.size();i++){
			JSONObject jobj = (JSONObject)array.get(i);
			FetchTaskRiceverBean b = JSON.toJavaObject(jobj,FetchTaskRiceverBean.class);
			list.add(b);
		}
		return list;
	}
	/**
	 * json=[{"name":"ll","age":"20","sex":"1"}]
	 * @param jsonStr
	 * @return
	 */
	public static List<TaskGuideBean> parseJson2GuideObject_2(String jsonStr){
		JSONArray array = JSONArray.parseArray(jsonStr);
		List<TaskGuideBean> list = new ArrayList<TaskGuideBean>();
		for(int i = 0; i < array.size();i++){
			JSONObject jobj = (JSONObject)array.get(i);
			TaskGuideBean b = JSON.toJavaObject(jobj,TaskGuideBean.class);
			list.add(b);
		}
		return list;
	}
	/**
	 * 杈撳嚭Ext鐨凷tore鏍煎紡鐨凧SON锛堢敤浜嶨ridPanel锛?
	 * @param response
	 * @param obj
	 * @throws IOException
	 */
	public static String printJsonListLocal(List<?> items, long totalRecords) {
		GridStore store = new GridStore();
		store.setTotalRecords(totalRecords);
		store.setItems(items);
		String json = JSON.toJSONString(store);
		return json;
	}
	/**
	 * 杈撳嚭Ext鐨凷tore鏍煎紡鐨凧SON锛堢敤浜嶨ridPanel锛?
	 * @param response
	 * @param obj
	 * @throws IOException
	 */
	public static void printJsonList(HttpServletResponse response, List<?> items, long totalRecords) {
		GridStore store = new GridStore();
		store.setTotalRecords(totalRecords);
		store.setItems(items);
		String json = JSON.toJSONString(store);
		response.setContentType("text/json;charset=UTF-8");
		try {
			logger.debug(json);
			response.getWriter().println(json);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 杈撳嚭Ext鐨凷tore鏍煎紡鐨凧SON锛堢敤浜巘ree锛?
	 * @param response
	 * @param obj
	 * @throws IOException
	 */
	public static void printJsonListfilerStr(HttpServletResponse response, List<?> items, long totalRecords,String filter) {
		GridStore store = new GridStore();
		store.setTotalRecords(totalRecords);
		store.setItems(items);
		String json = JSON.toJSONString(store);
		response.setContentType("text/json;charset=UTF-8");
		try {
			json = json.replace(filter, "");
			response.getWriter().println(json);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 杈撳嚭瀵硅薄JSON
	 * @param response
	 * @param obj
	 * @throws IOException
	 */
	public static void printJsonObject(HttpServletResponse response,Object obj) {
		String json = JSON.toJSONString(obj);
		response.setContentType("text/html;charset=UTF-8");
		try {
			logger.debug(json);
			response.getWriter().println(json);
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
	/**
	 * 涓嶅甫鎬绘暟鐨刯son
	 * @param response
	 * @param items
	 * @param totalRecords
	 */
	public static void printJsonList_chartdata(HttpServletResponse response, List<?> items, long totalRecords) {
		GridStore store = new GridStore();
		store.setTotalRecords(totalRecords);
		store.setItems(items);
		String json = JSON.toJSONString(store);
		int st = json.indexOf("[");
		int end = json.lastIndexOf("]");
		json = json.substring(st, end+1);
		response.setContentType("text/json;charset=UTF-8");
		try {
			//logger.debug(json);
			response.getWriter().println(json);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 输出对象JSON
	 * @param response
	 * @param obj
	 * @throws IOException
	 */
	public static String Object2Json(Object obj) {
		String json = JSON.toJSONString(obj);
		logger.info(json);	
		return json;
	}
}

class GridStore {
	private long totalRecords;
	private List<?> items;
	public long getTotalRecords() {
		return totalRecords;
	}
	public void setTotalRecords(long totalRecords) {
		this.totalRecords = totalRecords;
	}
	public List<?> getItems() {
		return items;
	}
	public void setItems(List<?> items) {
		this.items = items;
	}
}
