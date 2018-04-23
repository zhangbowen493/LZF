package com.wanda.pay.util;

/**
 * 设置用户电话中间四位为星号   “****”
 * @author Luckydog
 *
 */
public class PhoneNumberUtil {

	public static String getNumber(String str){
		return str.substring(0,3)+"****"+str.substring(7,str.length());
		
	}
}
