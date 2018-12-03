package com.longriver.netpro.common.sohu;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.ByteArrayOutputStream;  
import java.io.IOException;  
import java.io.UnsupportedEncodingException;  
import java.security.MessageDigest;    
public class BaiduScript {
	
	
  
	public static String getUid(String pubkey,String pw) throws ScriptException, NoSuchMethodException, IOException{
		ScriptEngineManager sem = new ScriptEngineManager();
		ScriptEngine engine = sem.getEngineByName("javascript");
		String jsFileName = "c:\\baidu.js";   // 读取js文件   
		Object result  ;
		FileReader reader = new FileReader(jsFileName);   // 执行指定脚本   
		engine.eval(reader); 
		if(engine instanceof Invocable) {    
			Invocable invoke = (Invocable)engine;    // 调用merge方法，并传入两个参数    

			// c = merge(2, 3);    

			result = ((Invocable)engine).invokeFunction("rsa111",pubkey, pw);

			System.out.println("result = " + result);   
			}else{
				result = new String("");
			}

			reader.close();  
						
		return (String)result;
		
	}
	
}
