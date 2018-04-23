package com.wanda.pay.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.FieldPosition;
import java.text.NumberFormat;
import java.text.ParsePosition;

/**
 * 
 * 由于Java的简单类型不能够精确的对浮点数进行运算，这个工具类提供精
 * 确的浮点数运算，包括加减乘除和四舍五入。
 * 
 *
 */
public class Arith{
    //默认除法运算精度
    private static final int DEF_SCALE = 10;
	
    //这个类不能实例化
    private Arith(){
    	
    }
    
    /**
	 * 手势密码-- 两点间的距离
	 * 
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @return
	 */
	public static double distance(double x1, double y1, double x2, double y2) {
		return Math.sqrt(Math.abs(x1 - x2) * Math.abs(x1 - x2)
				+ Math.abs(y1 - y2) * Math.abs(y1 - y2));
	}

	/**
	 * 手势密码-- 计算点a(x,y)的角度
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	public static double pointTotoDegrees(double x, double y) {
		return Math.toDegrees(Math.atan2(x, y));
	}
  
    /**
     * 提供精确的加法运算。
     * @param v1 被加数
     * @param v2 加数
     * @return 两个参数的和
     */
    public static double add(double v1,double v2){
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.add(b2).doubleValue();
    }
    /**
     * 提供精确的减法运算。
     * @param v1 被减数
     * @param v2 减数
     * @return 两个参数的差
     */
    public static double sub(double v1,double v2){
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        NumberFormat numberFormat = new NumberFormat() {
			
			@Override
			public Number parse(String string, ParsePosition position) {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public StringBuffer format(long value, StringBuffer buffer,
					FieldPosition field) {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public StringBuffer format(double value, StringBuffer buffer,
					FieldPosition field) {
				// TODO Auto-generated method stub
				return null;
			}
		};
        return b1.subtract(b2).doubleValue();
        
    } 
    /**
     * 提供精确的乘法运算。
     * @param v1 被乘数
     * @param v2 乘数
     * @return 两个参数的积
     */
    public static double mul(double v1,double v2){
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.multiply(b2).doubleValue();
    }
    
    /**
     * 提供精确的乘法运算。
     * @param v1 被乘数
     * @param v2 乘数
     * @return 两个参数的积
     */
    public static String mulToString(double v1,double v2){
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        DecimalFormat df = new DecimalFormat("0.00");
        String format = df.format(b1.multiply(b2).doubleValue());
        return format;
    }
    
    /**
     * 提供精确的乘法运算。
     * @param v1 被乘数
     * @param v2 乘数
     * @param v3 乘数
     * @return 三个个参数的积
     */
    public static double mul(double v1,double v2,double v3){
    	BigDecimal b1 = new BigDecimal(Double.toString(v1));
    	BigDecimal b2 = new BigDecimal(Double.toString(v2));
    	BigDecimal b3 = new BigDecimal(Double.toString(v3));
    	return (b1.multiply(b2)).multiply(b3).doubleValue();
    }
  
    /**
     * 提供（相对）精确的除法运算，当发生除不尽的情况时，精确到
     * 小数点以后10位，以后的数字四舍五入。
     * @param v1 被除数
     * @param v2 除数
     * @return 两个参数的商
     */
    public static double div(double v1,double v2){
        return div(v1,v2,DEF_SCALE);
    }
  
    /**
     * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指
     * 定精度，以后的数字四舍五入。
     * @param v1 被除数
     * @param v2 除数
     * @param scale 表示表示需要精确到小数点以后几位。
     * @return 两个参数的商
     */
    public static double div(double v1,double v2,int scale){
        if(scale<0){
            throw new IllegalArgumentException(
                "The scale must be a positive integer or zero");
        }
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.divide(b2,scale,BigDecimal.ROUND_HALF_UP).doubleValue();
    }
  
    /**
     * 提供精确的小数位四舍五入处理。
     * @param v 需要四舍五入的数字
     * @param scale 小数点后保留几位
     * @return 四舍五入后的结果
     */
    public static double round(double v,int scale){
        if(scale<0){
            throw new IllegalArgumentException(
                "The scale must be a positive integer or zero");
        }
        BigDecimal b = new BigDecimal(Double.toString(v));
        BigDecimal one = new BigDecimal("1");
        return b.divide(one,scale,BigDecimal.ROUND_HALF_UP).doubleValue();
    }
    /**
     * 判断字符串是否是数字
     * @param str
     * @return
     */
    public static boolean isNum(String str){
    	if("".equals(str)||"null".equals(str)||str==null){
    		return false;
    	}
		return str.matches("^[-+]?(([0-9]+)([.]([0-9]+))?|([.]([0-9]+))?)$");
	}
    
	/**
	 * 数字格式化
	 * @param str
	 * @return
	 */
	public static String LeftPad_Tow_Zero(int str) {
		java.text.DecimalFormat format = new java.text.DecimalFormat("00");
		return format.format(str);
	}
	/**
	 * 金额转换  转换为千  万 和亿为单位
	 * @param s
	 * @return
	 */
	public static String moneyParse(String s) {

		if (isNum(s)) {
			double pdb = Double.parseDouble(s);
			if (pdb / 100000000 > 1) {
				int i = (int) (pdb / 100000000);
				int j = (int) (pdb - i * 100000000);
				String string = String.valueOf(j);
				if (string.length() > 2) {
					CharSequence subSequence = string.subSequence(0, 2);
					return i + "." + subSequence + "亿";
				} else {
					return i + "." + string + "亿";
				}
			} else if (pdb / 10000 > 1) {
				int i = (int) (pdb / 10000);
				int j = (int) (pdb - i * 10000);
				String string = String.valueOf(j);
				if (string.length() > 2) {
					CharSequence subSequence = string.subSequence(0, 2);
					return i + "." + subSequence + "万";
				} else {
					return i + "." + string + "万";
				}
			} else if(pdb%1000 ==0){
				return (int)pdb/1000 +"千";
			}else {
				return s + "元";
			}
		} else {
			return "0元";
		}
	}
	/**
	 * double 指定精确度
	 * @param v
	 * @param scale
	 * @return
	 */
	public static double round(String v,int scale){
		if(isNum(v)){
			double parseDouble = Double.parseDouble(v);
			return round(parseDouble, scale);
		}else{
			return 0.0;
		}
	}
	/**
	 * 将科学计算法转为字符串
	 * @param s
	 * @return
	 */
	public static String DecimalFormat(String s ){
		DecimalFormat df = new DecimalFormat(".000");   
		  Double d = new Double(s);  
		return df.format(d);
	}
}
