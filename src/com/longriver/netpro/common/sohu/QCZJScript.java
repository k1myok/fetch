package com.longriver.netpro.common.sohu;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class QCZJScript {
	
	private static String jsFileName = "c:\\qczj.js";
	
	public static void main(String args[]){
		QCZJScript st02 = new QCZJScript();
		String str = "";
		//347E941672AC4007F4CADA3AD44F4835
		try {
			str = st02.getUid("");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ScriptException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(str);
	}
	
	public String getUid(String pw) throws ScriptException, NoSuchMethodException, IOException{
		ScriptEngineManager sem = new ScriptEngineManager();
		ScriptEngine engine = sem.getEngineByName("JavaScript");
		Object result  ;
		FileReader reader = new FileReader(jsFileName);   // 执行指定脚本   
		engine.eval(reader); 
		if(engine instanceof Invocable) {    
			Invocable invoke = (Invocable)engine;    // 调用merge方法，并传入两个参数    

			// c = merge(2, 3);    

			result = ((Invocable)engine).invokeFunction("hex_md5", pw);

			System.out.println("result = " + result);   
			}else{
				result = new String("");
			}

			reader.close();  
						
		return (String)result;
		
	}
	
}
