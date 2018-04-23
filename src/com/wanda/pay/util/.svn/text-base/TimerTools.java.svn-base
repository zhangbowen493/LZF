package com.wanda.pay.util;

/**
 * 时间long值 格式化
 */

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

public class TimerTools {

	public TimerTools() {
		super();
	}

	// Date d = new Date();
	// d.setTime(fromtime * 1000);
	// SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss",
	// Locale.CHINA);
	// sdf.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
	public static long oneMonth = monthDays() * 24 * 60 * 60;

	// 返回当前年份
	public static int getYear() {
		Date date = new Date();
		String year = new SimpleDateFormat("yyyy").format(date);
		return Integer.parseInt(year);
	}

	// 返回当前月份
	public static int getMonth() {
		Date date = new Date();
		String month = new SimpleDateFormat("MM").format(date);
		return Integer.parseInt(month);
	}

	/**
	 * 得到指定月的天数
	 * */
	public static int getMonthLastDay(int year, int month) {
		Calendar a = Calendar.getInstance();
		a.set(Calendar.YEAR, year);
		a.set(Calendar.MONTH, month - 1);
		a.set(Calendar.DATE, 1);// 把日期设置为当月第一天
		a.roll(Calendar.DATE, -1);// 日期回滚一天，也就是最后一天
		int maxDate = a.get(Calendar.DATE);
		return maxDate;
	}

	/**
	 * 当月多少天
	 * 
	 * @return
	 */
	public static int monthDays() {
		Calendar a = Calendar.getInstance();
		a.set(Calendar.DATE, 1);// 把日期设置为当月第一天
		a.roll(Calendar.DATE, -1);// 日期回滚一天，也就是最后一天
		int maxDate = a.get(Calendar.DATE);
		return maxDate;
	}

	/**
	 * 本月第一天
	 * @return
	 */
	public static long MonthOfDay() {
		// 本月的第一天
		Calendar calendar = new GregorianCalendar();
		calendar.set(Calendar.DATE, 1);
		calendar.set(Calendar.HOUR_OF_DAY, -1);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		return calendar.getTimeInMillis() +1000;
	}

	/**
	 * 本月的最后一天
	 * @return
	 */
	public static long MonthLastDay() {
		Calendar calendar = Calendar.getInstance();
		 calendar.set(Calendar.DAY_OF_MONTH,calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		 calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		return calendar.getTimeInMillis() ;
	}
	/**
	 * 现在
	 * @return
	 */
	public static long now() {
		// 现在
		Calendar calendar = new GregorianCalendar();
		return calendar.getTimeInMillis() ;
	}
	/**
	 * 判断是否是今天
	 * @param time	long值时间
	 * @return
	 */
	public static Boolean isToday(String time){
		if(time!=null){
			String now = now()+"";
			String dateFormat = getDateFormat(now, "yyyy-mm-dd");
			String dateFormat2 = getDateFormat(time, "yyyy-mm-dd");
			if(dateFormat.equals(dateFormat2)){
				return true;
			}else{
				return false;
			}
		}else{
			return false;
		}
	}

	/**
	 * 将2013年9月11日 转换为long类型 秒值
	 * 
	 * @param date
	 * @param dateType
	 *            日期格式
	 * @return
	 */

	public static long date2long(String date, String dateType) {
		// String sDt = "08-3-6 21时";
		// SimpleDateFormat sdf= new SimpleDateFormat("yy-M-d HH时");
		String sDt = date;
		SimpleDateFormat sdf = new SimpleDateFormat(dateType);
		Date dt2;
		try {
			dt2 = sdf.parse(sDt);
			// 继续转换得到秒数的long型
			long lTime = dt2.getTime() ;
			return lTime;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * 返回当前时间，String类型的毫秒值 a2 2013-6-7 String
	 * 
	 * @return
	 */
	public static String getdatatimeString() {
		Date d = new Date();
		long longtime = d.getTime() ;
		String s = String.valueOf(longtime);
		return s;
	}

	/**
	 * 得到当前时间，返回值是格式化后的字符串 a2 2013-6-7 String
	 * 
	 * @return
	 */
	public static String getDateFormat() {
		Date d = new Date();
		long longtime = d.getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		return sdf.format(longtime);

	}

	/**
	 * 制定月的第一天第一秒
	 * 
	 * @param year
	 * @param month
	 * @return
	 */
	public static long firstDay(int year, int month) {
		// 本月的第一天第一秒
		Calendar calendar = new GregorianCalendar();
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, month - 1);
		calendar.set(Calendar.DATE, 1);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		// System.out.println(":::" + calendar.getTimeInMillis() / 1000);
		return calendar.getTimeInMillis() ;
	}

	/**
	 * 制定月最后一秒
	 * 
	 * @param year
	 * @param month
	 * @return
	 */
	public static long LastDay(int year, int month) {
		// 本月的最后一秒
		Calendar calendar = new GregorianCalendar();
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, month);
		calendar.set(Calendar.DATE, 1);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, -1);
		// System.out.println(":::" + calendar.getTimeInMillis() / 1000);
		return calendar.getTimeInMillis() ;
	}

	/**
	 * 将秒值转换为 日期
	 * 
	 * @param time
	 *            秒值
	 * @param type
	 *            日期格式
	 * @return
	 */
	public static String getDateFormat(String time, String type) {
		if (time != null) {

			long longtime = Long.parseLong(time );
			SimpleDateFormat sdf = new SimpleDateFormat(type);
			return sdf.format(longtime);
		}
		return "";
	}

	/**
	 * 返回当前时间，long类型的毫秒值 a2 2013-6-7 Long
	 * 
	 * @return
	 */
	public static Long getdatatimelong() {
		Date d = new Date();
		long longtime = d.getTime();
		return longtime;
	}

	
	/**
	 * 距离某日多少天
	 * @param bzrq
	 * @return
	 */
	public static long getBzTime(String bzrq)  {
		if(bzrq == null){
			return 0;
		}
		  long syDay = Long.parseLong(bzrq)-getdatatimelong(); 
		  long syts = syDay/60/60/1000/24;//获取今天到指定日期剩余天数
		  return syts;
	  }
	
	/**
	  * 判断当前日期是星期几
	  * 
	  * @param  time     设置的需要判断的时间 
	  *  
	  * @return week 返回周几
	  */
	 public static String getWeek(long time) {
	  String Week = "周";

	  Calendar c = Calendar.getInstance();
	  c.setTime(new Date(time));
	  
	  switch (c.get(Calendar.DAY_OF_WEEK)) {
		case 1:
			Week += "日";
			break;
		case 2:
			Week += "一";
			break;
		case 3:
			Week += "二";
			break;
		case 4:
			Week += "三";
			break;
		case 5:
			Week += "四";
			break;
		case 6:
			Week += "五";
			break;
		case 7:
			Week += "六";
			break;
	
		default:
			break;
		}

	  return Week;
	 }
}
