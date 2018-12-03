package com.longriver.netpro.common.ifeng;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

public class IFengScript {
	
	public static String getUid() throws Exception{
		ScriptEngineManager sem = new ScriptEngineManager();
		ScriptEngine se = sem.getEngineByName("JavaScript");
		Object result = se.eval("function xx(){"
			+ "var b=(new Date).getTime(),a=\"\",c=\"\",a=(2147483648*Math.random()|0).toString(36),c=Math.round(1E4*Math.random());"
			+ "return b+\"_\"+a+c;"
			+ "}");
		result = ((Invocable)se).invokeFunction("xx");
		return (String) result;
	}
	
	public static String getVjuids(String cookie) throws Exception{
		ScriptEngineManager sem = new ScriptEngineManager();
		ScriptEngine se = sem.getEngineByName("JavaScript");
		Object result = se.eval("function vjVisitorID(s) {"
						+ "	var A = vjHash(s).toString(16);"
						+ "	var B = new Date();"
						+ "	return A + \".\" + B.getTime().toString(16) + \".\" + Math.random().toString(16);"
						+ "}"
						+ "function vjHash(C) {"
						+ "	if (!C || C == \"\") {"
						+ "		return 0;"
						+ "	}"
						+ "	var B = 0;"
						+ "	for (var A = C.length - 1; A >= 0; A--) {"
						+ "		var D = parseInt(C.charCodeAt(A));"
						+ "		B = (B << 5) + B + D;"
						+ "	}"
						+ "	return B;"
						+ "}"
						+ "function xx(s){"
						+ "	return escape(vjVisitorID(s));"
						+ "}");
		result = ((Invocable)se).invokeFunction("xx", cookie);
		return (String) result;
	}
	
	public static String getVjlast() throws Exception{
		ScriptEngineManager sem = new ScriptEngineManager();
		ScriptEngine se = sem.getEngineByName("JavaScript");
		Object result = se.eval("function vjGetTimestamp(A) {"
						+ "	return Math.round(A / 1000);"
						+ "}"
						+ "function xx(){"
						+ "	var F = new Date();"
						+ "	var E = vjGetTimestamp(F.getTime()).toString();"
						+ "	return E + \".\" + E + \".30\";"
						+ "}");
		result = ((Invocable)se).invokeFunction("xx");
		return (String) result;
	}

}
