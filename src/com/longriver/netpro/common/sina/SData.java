package com.longriver.netpro.common.sina;

import java.util.HashMap;

@SuppressWarnings("rawtypes")
public class SData extends HashMap{
	
	private static final long serialVersionUID = 1L;

	@Override
	public Object get(Object key){
		return super.get(key);
	}
	
	@SuppressWarnings("unchecked")
	public void set(Object key, Object value){
		super.put(key, value);
	}
	
	public String getString(String key){
		return (String)this.get(key);
	}
	
	@SuppressWarnings("unchecked")
	public void setString(String key, String value){
		super.put(key, value);
	}
	
	public boolean getBoolean(String key){
		return (Boolean) super.get(key);
	}
	
	public int getInt(String key){
		return (Integer) super.get(key);
	}

}
