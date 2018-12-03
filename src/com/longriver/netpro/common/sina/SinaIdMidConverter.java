package com.longriver.netpro.common.sina;

public class SinaIdMidConverter {
	
	private static String str62keys = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
	
	public static void main(String args[]){
//		String mid = idToMid("3520617028999724");
//		System.out.println(mid);
		String id = midToId("2418432711");
		System.out.println(id);
	}
	
	public static String idToMid(String si){
		String mid = "";
		for (int i = si.length() - 7; i > -7; i = i - 7){
			int startIdex = i < 0 ? 0 : i;
			int len = i < 0 ? si.length() % 7 : 7;
			String strTemp = si.substring(startIdex, startIdex + len);
			mid = intToStr62(Integer.valueOf(strTemp)) + mid;
		}
		return mid;
	}
	
	public static String midToId(String mid){
		String id = "";
		for (int i = mid.length() - 4; i > -4; i = i - 4){
			int offset1 = i < 0 ? 0 : i;
			int len = i < 0 ? mid.length() % 4 : 4;
			String str = mid.substring(offset1, offset1 + len);
			
			str = str62toInt(str);
			if (offset1 > 0){
				 while (str.length() < 7){
					 str = "0" + str;
				 }
			}
			id = str + id;
		}
		return id;
	}
	
	private static String str62toInt(String str){
		int i64 = 0;
		for (int i = 0; i < str.length(); i++){
			int vi = (int)Math.pow(62, (str.length() - i - 1));
			char t = str.charAt(i);
			i64 += vi * getInt10(String.valueOf(t));
		}
		return String.valueOf(i64);
	}
	
	private static int getInt10(String key){
		return str62keys.indexOf(key);
	}
	
	private static String intToStr62(int si){
		String s62 = "";
		int r = 0;
		while(si != 0){
			r = si % 62;
			s62 = get62Key(r) + s62;
			si = si / 62;
		}
		return s62;
	}
	
	private static String get62Key(int si){
		if (si < 0 || si > 61){
			return "";
		}
		return str62keys.substring(si, si + 1);
	}

}
