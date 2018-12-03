package com.longriver.netpro.common.sohu;

import java.io.FileReader;
import java.io.IOException;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
public class XiciScript {
	
	public static void main(String[] args) {
		String t = "1127968";
		String tt = "246853833";
		String tst = "呵呵大";
		String sessionID = "F8E324B8BC5AAECE";
		try {
			getH2(sessionID,t,tt,tst);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (ScriptException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 获得发帖时候的h的值
	 * @param cookie
	 * @param lBdId 页面中有
	 * @param DocId 链接中有
	 * @param text 发帖的内容
	 * @return
	 * @throws ScriptException
	 * @throws NoSuchMethodException
	 * @throws IOException
	 */
	public static String getH2(String cookie,String lBdId,String DocId,String text) throws ScriptException, NoSuchMethodException, IOException{
		ScriptEngineManager sem = new ScriptEngineManager();
		ScriptEngine engine = sem.getEngineByName("javascript");
		String jsFileName = "c:\\fjs\\pub_noad.js";   // 读取js文件   
		Object result  ;
		FileReader reader = new FileReader(jsFileName);   // 执行指定脚本   
		engine.eval(reader); 
		if(engine instanceof Invocable) {    
			Object p = ((Invocable)engine).invokeFunction("P",text);
			String hh = lBdId+'-'+DocId+'-'+p;

			result = ((Invocable)engine).invokeFunction("H2",hh);

			System.out.println("result = " + result);   
			}else{
				result = new String("");
			}

			reader.close();  
						
		return (String)result;
		
	}
	
}
