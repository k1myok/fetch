package com.longriver.netpro.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;

/**
 * @项目名称：网评员管理系统
 * @类名称：DateUtil
 * @类描述：时间工具类
 * @创建人：
 * @创建时间：2012-09-07
 * @修改人：
 * @修改时间：
 * @备注：
 */
public class DateUtil {
	public static String time1 = "yyyyMMddHHmmssSSS";
	/**
	 * 时间戳
	 * @return string 时间戳
	 */
	public static String getCurrentTimeMillis() {
		long time = System.currentTimeMillis();
		Date date = new Date(time);
		SimpleDateFormat sdf = new SimpleDateFormat(time1);
		return sdf.format(date);
	}
	/**
	 * 时间戳
	 * @return string 时间戳
	 */
	public static String getCurrentDate() {
		long time = System.currentTimeMillis();
		Date date = new Date(time);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		return sdf.format(date);
	}
	
	/**
	 * 时间 获得当前时间
	 */
	public static String getCurrentTime() {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(date);
	}
	/**
	 * 获得某个日期 前N天的时间
	 * @return
	 */
	public static String getCurrentTimeForDay(String dateString,int day) {
		String d = subDate(dateString,day);
		return d + " 23:59:59";
	}
	/**
	 * 截取日期月份
	 * @param 日期格式 2012-09-07
	 * @return string 1209
	 */
	public static String dateForMonth(String date) {
		if (date == null) {
			return null;
		}
		String date_sub = date.replaceAll("-", "");
		return date_sub.substring(2, 6);
	}
	public static String getNowDate(int i){
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
		SimpleDateFormat df2 = new SimpleDateFormat("yyyyMMdd");//设置日期格式
		if(i==0){
			return df.format(new Date());
		}else{
			return df2.format(new Date());
		}
	}
	/**
	 * 得到系统日期的n天前或n天后的日期 - String
	 */
	public static String getSubCurrentDate(int n) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, n);
		Date date = calendar.getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String dateTime = sdf.format(date);
		return dateTime;
	}
	
	/**
	 * 得到当前时间时间
	 * @param 时间格式
	 * @return string 按格式返回当前时间
	 */
	public static String getCurrentDataTime(String format) {
		long time = System.currentTimeMillis();
		Date date = new Date(time);
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(date);
	}
	
	/**
	 * 取得当前日期 前后n天的日期
	 * n = -7 前7天
	 * n = 7 后7天
	 */
	public static String subDate(String dateString, int n) {
		Calendar calendar = Calendar.getInstance();
		Date date_old = toDate(dateString, "yyyy-MM-dd");
		calendar.setTime(date_old);
		calendar.add(Calendar.DATE, n);
		Date date = calendar.getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(date);
	}
	/**
	 * 取得某个时间 前后n天的日期时间
	 * 2014-12-06 12:20:01--2014-12-08 12:20:01
	 * n = -7 前7天
	 * n = 7 后7天
	 */
	public static String getTimeForDays(String dateString, int n) {
		Calendar calendar = Calendar.getInstance();
		Date date_old = toDate(dateString, "yyyy-MM-dd HH:mm:ss");
		calendar.setTime(date_old);
		calendar.add(Calendar.DATE, n);
		Date date = calendar.getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(date);
	}
	// 增加或减少天数  
    public static Date addDay(Date date, int num) {  
        Calendar startDT = Calendar.getInstance();  
        startDT.setTime(date);  
        startDT.add(Calendar.DAY_OF_MONTH, num);  
        return startDT.getTime();  
    }  
	/**
	 * 字符串转换成 Date 
	 * 
	 * @param dateStr 字符串(yyyyMMdd)
	 * @param formatType format 
	 * @return Date java.util.Date日期
	 */
	public static Date toDate(String dateStr, String formatType) {
		Date date = null;
		try {
			date = new SimpleDateFormat(formatType).parse(dateStr);
		} catch (ParseException e) {
			return date;
		}
		return date;
	}
	
	/**
	 * 两个日期之间相隔天数的共通
	 * @param dateFrom 开始时间
	 * @param dateEnd　结束时间
	 * @return　天数
	 */
	public static int getDaysBetweenTwoDates(String dateFrom, String dateEnd) {
		Date dtFrom = toDate(dateFrom, "yyyy-MM-dd");
		Date dtEnd = toDate(dateEnd, "yyyy-MM-dd");
		
		long begin = dtFrom.getTime();
		long end = dtEnd.getTime();
		long inter = end - begin;
		if (inter < 0) {
			inter = inter * (-1);
		}
		long dateMillSec = 24 * 60 * 60 * 1000;

		Long dateCnt = inter / dateMillSec;

		long remainder = inter % dateMillSec;

		if (remainder != 0) {
			dateCnt++;
		}
		return dateCnt.intValue();
	}
	/**
	 * 取得当前日期 前后n月的日期
	 * n = -7 前7个月
	 * n = 7 后7个月
	 */
	public static String subMonth(String dateString, int n) {
		Calendar calendar = Calendar.getInstance();
		Date date_old = toDate(dateString, "yyyy-MM-dd");
		calendar.setTime(date_old);
		calendar.add(Calendar.MONTH, n);
		Date date = calendar.getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(date);
	}
	/**
	 * 取得当前日期 前后n小时的日期
	 * n = -7 前7小时
	 * n = 7 后7小时
	 */
	public static String newDateNewsForHour(int h,String format) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.HOUR_OF_DAY, h);
		Date date = calendar.getTime();
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		String dateTime = sdf.format(date);
		return dateTime;
	}
	/**
	 * 取得XX日期 前后n小时的日期
	 * n = -7 前7小时
	 * n = 7 后7小时
	 */
	public static String subHour(String dateString, int n) {
		Calendar calendar = Calendar.getInstance();
		Date date_old = toDate(dateString, "yyyy-MM-dd HH:mm:ss");
		calendar.setTime(date_old);
		calendar.add(Calendar.HOUR_OF_DAY, n);
		Date date = calendar.getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(date);
	}
	/**
	 * 取得XX日期 前后n分钟的日期
	 * n = -7 前7分钟
	 * n = 7 后7分
	 */
	public static String subMine(String dateString, int n) {
		Calendar calendar = Calendar.getInstance();
		Date date_old = toDate(dateString, "yyyy-MM-dd HH:mm:ss");
		calendar.setTime(date_old);
		calendar.add(Calendar.MINUTE, n);
		Date date = calendar.getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(date);
	}	
	/**
	 * 把毫秒转换成时长，小时、分钟、秒
	 * 
	 * @param millis
	 * @param format
	 * @return
	 */
	public static String formatLongToTimeStr(Long millis, String format) {
		String str = "";
		long hour = 0;
		long minute = 0;
		long second = 0;

		second = millis / 1000;
		if (second > 60) {
			minute = second / 60;
			second = second % 60;
		}
		if (minute > 60) {
			hour = minute / 60;
			minute = minute % 60;
		}
		if (hour > 0) {
			str = StringUtils.replaceEach(format, new String[] { "HH", "mm",
					"ss" }, new String[] { String.format("%0$02d", hour),
					String.format("%0$02d", minute),
					String.format("%0$02d", second) });
		} else if (hour == 0 && minute > 0) {
			format = "mm" + StringUtils.substringAfter(format, "mm");
			str = StringUtils.replaceEach(format, new String[] { "mm", "ss" },
					new String[] { String.format("%0$02d", minute),
							String.format("%0$02d", second) });
		} else if (hour == 0 && minute == 0) {
			format = "ss" + StringUtils.substringAfter(format, "ss");
			str = StringUtils.replace(format, "ss", String.format("%0$02d",
					second));
		}
		return str;
	}
	/**
	 * 得到年 / 月 / 日的数组
	 */
	public static String[] getDateArray(String date) {
		if(date == null) {
			return null;
		}
		return date.split("-");
	}
	/**
	 * 得到年 / 月 / 日的数组
	 */
	public static String getDateArray2(String date) {
		if(date == null) {
			return null;
		}
		String[] d = date.split(" ");
		String dd[] = d[0].split("-");
		return Integer.parseInt(dd[1])+"月"+dd[2]+"日";
	}
	/**
	 * 得到月查询的每一天
	 */
	public static String[] getMonthEveryDay(String start_param,String end) {
		String start = start_param == null ? subMonth(end, -1) : start_param;
		String[] start_array = getDateArray(start);
		int year = Integer.valueOf(start_array[0]);
	    int month = Integer.valueOf(start_array[1]);
	    int day = Integer.valueOf(start_array[2]);
	    int count = Integer.valueOf(getDaysBetweenTwoDates(start, end));
	    String[] day_array = new String[count + 1];
	    Calendar calendar = Calendar.getInstance();
	    for(int i = 0;i<=count;i++) {
		    calendar.clear();
		    calendar.set(year, month-1, day);       
			calendar.add(Calendar.DATE, i);
			Date date = calendar.getTime();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String dateTime = sdf.format(date);
			day_array[i] = dateTime;
	    }
		return day_array;
	}
	/**
	 * 得到系统日期的n天前或n天后的日期 - String
	 * 
	 * @param n
	 *            相差天数
	 * @return
	 */
	public static String newDate(int n) {
		return DateFormatUtils.format(DateUtils.addDays(new Date(), n),
				"yyyy-MM-dd");
	}
	
	/**
	 * 截取月日（04-18）
	 * @param date
	 * @return
	 */
	public static String dateForMonthDay(String date) {
		if (date == null) {
			return null;
		}
		return date.substring(5);
	}
	/**
	 * 两个日期之间月相隔时间（2011-10-21 ~ 2011-12-20 = 2011-10-01 ~ 2011-11-30）
	 * @param dateFrom 开始时间
	 * @param dateEnd　结束时间
	 * @return　天数
	 */
	public static int getDaysBetweenForMonth(String dateFrom, String dateEnd) {
		Date dtFrom = toDate(dateFrom, "yyyy-MM-dd");
		Date dtEnd = toDate(dateEnd, "yyyy-MM-dd");
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(dtFrom);
		Calendar calendar2 = Calendar.getInstance();
		calendar2.setTime(dtEnd);
		
		addMonthDayMethod(calendar,calendar2);
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		return getDaysBetweenTwoDates(sdf.format(calendar.getTime()),sdf.format(calendar2.getTime()));
	}
	
	/**
	 * 两个日期之间月相隔月份
	 * @param dateFrom 开始时间
	 * @param dateEnd　结束时间
	 * @return　天数
	 */
	public static int getMonthsBetween(String dateFrom, String dateEnd) {
		Date dtFrom = toDate(dateFrom, "yyyy-MM-dd");
		Date dtEnd = toDate(dateEnd, "yyyy-MM-dd");
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(dtFrom);
		Calendar calendar2 = Calendar.getInstance();
		calendar2.setTime(dtEnd);
		
		addMonthDayMethod(calendar,calendar2);
		
		int numyear = calendar2.get(Calendar.YEAR) - calendar.get(Calendar.YEAR);
		int nummonth = calendar2.get(Calendar.MONTH) - calendar.get(Calendar.MONTH);
		
		return numyear * 12 + nummonth;
	}
	
	/**
	 * 得到系统日期的n天前或n天后的日期 - String
	 * @param format yyyy-MM-dd
	 */
	public static String newsForHour(Calendar calendar) {
		Date date = calendar.getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String dateTime = sdf.format(date);
		return dateTime;
	}
	
	/**
	 * 计算日期月份方法
	 */
	public static void addMonthDayMethod(Calendar calendar,Calendar calendar2) {
		if(calendar2.get(Calendar.DATE) >= calendar.get(Calendar.DATE)) {
			calendar2.add(Calendar.MONTH, 1);
		}
		calendar.set(Calendar.DATE, 1);
		calendar2.set(Calendar.DATE, 1);
		calendar2.add(Calendar.DATE, -1);
	}
	/**
	 * 获得当前系统小时
	 * @return
	 */
	public static int getNowHour(){
		Calendar c = Calendar.getInstance();
		int hour = c.get(Calendar.HOUR_OF_DAY);
		return hour;
	}
	/**
	 * 获得周
	 * @return string 获得周
	 */
	public static int getDateWeek(String day, String format) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(toDate(day, format));
		int week = calendar.get(Calendar.DAY_OF_WEEK);
		week = week - 1;
		if(week == 0) {
			week = 7;
		}
		return week;
	}
	/**
	 * 将秒转化成时间		
	 * @param mill
	 * @return
	 */
	public static String mill2Date(long mill){
		Date date = new Date(mill*1000);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return format.format(date);
	}
	public static String jointStartTime(String startTime) {
		String start = " 00:00:00";
		if (startTime != null && !startTime.equals("")) {
			startTime += start;
		}
		return startTime;
	}
	
	public static String jointEndTime(String endTime) {
		String end = " 23:59:59";
		if (endTime != null && !endTime.equals("")) {
			endTime += end;
		}
		return endTime;
	}
	/**
	 * long 转化成日期
	 * @param endTime
	 * @return
	 */
	public static String long2Date(String longStr) {
		//1452577921, 1452577920, 1452577879
		longStr = longStr+"000";
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(Long.parseLong(longStr));
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(c.getTime());
	}
	/**
	 * long 转化成日期
	 * @param endTime
	 * @return
	 */
	public static String long2Date2(String longStr) {
		//1452577921, 1452577920, 1452577879
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(Long.parseLong(longStr));
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(c.getTime());
	}
	//获得两个时间相差分钟
	public static long getDateTimeMin(String startTime,String endTime,String c){
		SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		long nd = 1000*24*60*60;//一天的毫秒数
		long nh = 1000*60*60;//一小时的毫秒数
		long nm = 1000*60;//一分钟的毫秒数
		long ns = 1000;//一秒钟的毫秒数
		long diff = 0;
		try {
			diff = sd.parse(endTime).getTime() - sd.parse(startTime).getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		long time = 0l;
		if(c.equals("day"))
			time = diff/nd;//计算差多少天
		else if(c.equals("hour"))
			time = diff%nd/nh;//计算差多少小时
		else if(c.equals("min"))
			time = diff%nd%nh/nm;//计算差多少分钟
		else
			time= diff%nd%nh%nm/ns;//计算差多少秒//输出结果
		
		return time;
	}
	public static boolean isValidDate(String str) {
	       	boolean convertSuccess=true;
	        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	        try {
	           format.parse(str);
	        } catch (ParseException e) {
	            convertSuccess=false;
	       } 
	       return convertSuccess;
	}
	public static boolean volidateDate(String checkValue) { 
        String eL= "^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))";        
        Pattern p = Pattern.compile(eL);         
        Matcher m = p.matcher(checkValue);         
        boolean flag = m.matches();
		return flag;
	}
	/**
	  * 年月日转化
	  * @param pTime
	  * @return
	  */
	 public static String String2DateStr(String date){
		 try {
			if(date.contains("年")){
				 date = date.replaceAll("年", "-");
			 }else if(date.lastIndexOf("-")<5){
				 Calendar calendar = Calendar.getInstance();
				 date = calendar.get(Calendar.YEAR)+"-"+date;
			 }
			  date = date.replaceAll("月", "-");
			  date = date.replaceAll("日", "");
			  date = date.trim()+":00";
			  SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			  Date date_old = toDate(date, "yyyy-MM-dd HH:mm:ss");
			  return sdf.format(date_old);
		} catch (RuntimeException e) {
			System.out.println("解析时间失败");
			return getCurrentTime();
		}
	 }
	public static void main(String arg[]){
		

//		String endTime = DateUtil.getCurrentTime();

		String endTime = DateUtil.getCurrentTime();
		System.out.println(subMine(endTime,-30));;

//		String starttime = "2015-03-24 14:58:19";
//		endTime = "2015-03-25 15:08:19";
//		System.out.println(isValidDate("---"));
//		System.out.println(getNowTime("2015-05-28 14:18:19",getCurrentTime()));
//		System.out.println(getDaysBetweenTwoDates(starttime, endTime));
//		System.out.println(long2Date("1452577920"));
		
//		Calendar calendar = Calendar.getInstance();
//		System.out.println(long2Date("1467963009"));
//		System.out.println(new Date().getTime());
//		System.out.println(String2DateStr("8月18日 10:55"));
	}
}