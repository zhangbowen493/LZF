package com.wanda.pay.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.wanda.pay.R;
import com.wanda.pay.bean.Constant_data;

import cn.passguard.PassGuardEdit;
import cn.passguard.doAction;
import android.content.Context;
import android.text.InputType;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

/**
 * 
 * @author kevin
 * 
 */
public class Tools {

	public static String encryptionPassword(Context context, String password) {

		return password;
	}

	// 全局数组
	private final static String[] strDigits = { "0", "1", "2", "3", "4", "5",
			"6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };

	/**
	 * 字符串md5加密
	 * 
	 * @param context
	 * @param str
	 * @return
	 */
	public static String encryptionMD5(String strObj) {
		LogUtil.i("Tools", "strObj:" + strObj);
		String resultString = null;
		try {
			resultString = new String(strObj);
			MessageDigest md = MessageDigest.getInstance("MD5");
			// md.digest() 该函数返回值为存放哈希值结果的byte数组
			resultString = byteToString(md.digest(strObj.getBytes()));
		} catch (NoSuchAlgorithmException ex) {
			ex.printStackTrace();
		}
		LogUtil.i("Tools", "resultString:" + resultString);
		return resultString;
	}

	// 转换字节数组为16进制字串
	private static String byteToString(byte[] bByte) {
		StringBuffer sBuffer = new StringBuffer();
		for (int i = 0; i < bByte.length; i++) {
			sBuffer.append(byteToArrayString(bByte[i]));
		}
		return sBuffer.toString();
	}

	// 返回形式为数字跟字符串
	private static String byteToArrayString(byte bByte) {
		int iRet = bByte;
		// System.out.println("iRet="+iRet);
		if (iRet < 0) {
			iRet += 256;
		}
		int iD1 = iRet / 16;
		int iD2 = iRet % 16;
		return strDigits[iD1] + strDigits[iD2];
	}

	/**
	 * 字符串中间隐藏
	 * 
	 * @param no
	 *            字符串
	 * @param number
	 *            首尾保留位数
	 * @return
	 */
	public static String bankCardChange(String no, int number) {
		if (StringUtils.isEmpty(no)) {
			return "";
		}
		String head = "";
		String s = "";
		String foot = "";
		if (no.length() > 8 && number > 0 && number < no.length() / 2) {
			head = no.substring(0, number);
			foot = no.substring(no.length() - number, no.length());
			for (int i = 0; i < no.length() - (number * 2); i++) {
				s += "*";
			}
			return head + s + foot;
		} else {
			return no;
		}
	}

	/**
	 * 密码复杂度验证
	 * 
	 * @param s
	 * @return
	 */
	public final static boolean checkPassword(String s) {
		if (s != null && !"".equals(s.trim()))
			return s.matches("^(?=.{6,16})(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])[0-9a-zA-Z]*$");
		else
			return false;
	}

	/**
	 * 手机号验证
	 * 
	 * @param str
	 * @return 验证通过返回true
	 */
	public static boolean isMobile(String str) {
		if (StringUtils.isEmpty(str)) {
			return false;
		}
		Pattern p = null;
		Matcher m = null;
		boolean b = false;
		p = Pattern.compile("^[1][3,4,5,6,7,8][0-9]{9}$"); // 验证手机号
		m = p.matcher(str);
		b = m.matches();
		return b;
	}

	/**
	 * URL验证
	 * 
	 * @param str
	 * @return 验证通过返回true
	 */
	public static boolean isURL(String str) {
		if (StringUtils.isEmpty(str)) {
			return false;
		}
		Pattern p = null;
		Matcher m = null;
		boolean b = false;
		p = Pattern.compile("[a-zA-Z]+:/[^s]*"); // 验证手机号
		m = p.matcher(str);
		b = m.matches();
		return b;
	}

	/**
	 * 是否是数字判断
	 * 
	 * @param s
	 * @return
	 */
	public final static boolean isNumeric(String s) {
		if (s != null && !"".equals(s.trim()))
			return s.matches("^[0-9]+(.[0-9]{1,2})?$");
		else
			return false;
	}

	/**
	 * 邮箱地址判断合法 E-mail地址： 1. 必须包含一个并且只有一个符号“@” 2. 第一个字符不得是“@”或者“.” 3.
	 * 不允许出现“@.”或者.@ 4. 结尾不得是字符“@”或者“.” 5. 允许“@”前的字符中出现“＋” 6. 不允许“＋”在最前面，或者“＋@”
	 * 
	 * @param str
	 * @return 验证通过返回true
	 */
	public static boolean isEmail(String str) {
		if (!StringUtils.isEmpty(str)) {
			return false;
		}
		String check = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
		Pattern regex = Pattern.compile(check);
		Matcher matcher = regex.matcher(str);
		return matcher.matches();
	}

	/**
	 * 用户名类型 1.手机 2 邮箱 3 别名
	 * 
	 * @param str
	 * @return
	 */
	public static String LoginType(String str) {
		String type = "1";
		if (StringUtils.isEmpty(str)) {
			return type;
		}
		if (isMobile(str)) {
			type = "1";
		} else if (isEmail(str)) {
			type = "2";
		} else {
			type = "3";
		}
		return type;
	}

	/**
	 * 实名认证级别
	 * 
	 * @param _RealNmaeLV
	 *            实名级别: 4-实名审核中 3 非实名 2 弱实名 1 强实名
	 * 
	 * @return
	 */
	public static String RealNameLevel(String _RealNmaeLV) {
		// TODO Auto-generated method stub
		String realNameLV = "未认证";
		if ("1".equals(_RealNmaeLV)) {
			realNameLV = "强实名";
		} else if ("2".equals(_RealNmaeLV)) {
			realNameLV = "弱实名 ";
		} else if ("4".equals(_RealNmaeLV)) {
			realNameLV = "实名审核中";
		}
		return realNameLV;
	}

	/**
	 * 验证身份账号合法性
	 * 
	 * @param idNo
	 * @return
	 */
	public final static boolean isIDNo(String idNo) {
		if (StringUtils.isEmpty(idNo)) {
			return false;
		}
		// 定义判别用户身份证号的正则表达式（要么是15位，要么是18位，最后一位可以为字母）
		Pattern idNumPattern = Pattern
				.compile("(\\d{14}[0-9a-zA-Z])|(\\d{17}[0-9a-zA-Z])");
		// 通过Pattern获得Matcher
		Matcher idNumMatcher = idNumPattern.matcher(idNo);
		// 判断用户输入是否为身份证号
		if (idNumMatcher.matches()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 验证银行卡
	 * 
	 * @param CardNo
	 * @return
	 */
	public final static boolean isCardNo(String CardNo) {
		if (CardNo == null || "".equals(CardNo)) {
			return false;
		}
		// 定义判别银行卡号的正则表达式（要么是16位，要么是19位）
		Pattern idNumPattern = Pattern.compile("^(\\d{16}|\\d{19})$");
		// 通过Pattern获得Matcher
		Matcher idNumMatcher = idNumPattern.matcher(CardNo);
		// 判断用户输入是否为银行卡号
		if (idNumMatcher.matches()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 初始化安全控件 C) 输出接口 1. getOutput1，获取用户输入数据的加密密文（AES），用Base64编码。 2.
	 * getOutput2，获取用户输入数据的MD5哈希值。 3. getOutput3，获取用户输入数据的长度。 4.
	 * checkMatch，返回boolean值
	 * ，根据setMatchRegex函数所设置的正则表达式尝试匹配当前输入框内容，匹配返回true，否则false
	 * 。若未用setMatchRegex函数设置正则，则返回true。 5.
	 * getPassLevel，返回一个包含两个int的数组，int[0]返回当前输入框内容的组成结构： 完全为空，返回0。
	 * 仅有数字，字符或特殊符号为1。例：”1234””abcd”“%#@!” 有两种组合返回2。例:”12bd”“12@#”“ab@#”
	 * 有三种组合返回3。例：”1@b”“1@2ab3”
	 * Int[1]返回判断当前输入框内容是否为简单密码。若内容为8位以下且是连续字符则为弱密码，返回1。否则并非弱密码，返回0。 6.
	 * isKeyBoardShowing，根据当时键盘是否显示返回boolean值，true为正在显示，false为没有显示。
	 * 
	 * @param v
	 *            密码输入框控件
	 * @param MatchStr
	 *            输入限制 正则表达式
	 */
	public static void initPassGuard(final PassGuardEdit v, String MatchStr) {

		PassGuardEdit.setLicense(Constant_data.PassGuardEditLicense);
		v.setCipherKey("abcdefghijklmnopqrstuvwxyz123456"); // 设置随机字符串。此串用于加密
		v.setEncrypt(true); // 是否加密
		v.setButtonPress(true); // 设置键盘的按键状态。传入true为有按键状态，false为没有。默认构造false
		// _LEDT_Password.setMaxLength(6); //设置键盘输入的最大长度，传入大于0的数字，默认限制为100字
		v.useNumberPad(false); // 表示此键盘是否只使用数字键盘。true为仅使用数字键盘，false为使用字母和数字全键盘（默认）。
		v.setButtonPressAnim(true);
		v.setWatchOutside(true); // 设置是否点击键盘外部后关闭键盘。
		v.setLongClickable(false);
		v.EditTextAlwaysShow(false); // 设置键盘上自带输入框是否显示，true为显示输入框，false为不显示（默认）。
		v.setReorder(PassGuardEdit.KEY_CHAOS_SWITCH_VIEW);// 设置键盘是否乱序。有三种模式：0/KEY_NONE_CHAOS，默认不乱序。1/KEYCHAOS_SWITCH_VIEW，初始化后只乱一次。2/KEY_CHAOS_PRESS_KEY，每次点击键盘是乱序一次。
		v.setInputType(InputType.TYPE_NULL);
		v.setInputRegex("[a-zA-Z0-9@_\\.]");
		if (null != MatchStr) {
			v.setMatchRegex(MatchStr);
		}
		doAction action = new doAction() {
			public void doActionFunction() {
				// Toast.makeText(
				// getApplicationContext(),
				// _LEDT_Password.isKeyBoardShowing() ? "KeyBoardShow"
				// : "KeyBoardHide", Toast.LENGTH_LONG).show();
			}
		};

		v.setShowPassword(true); // 密码输入先显示后再隐藏
		v.setKeyBoardShowAction(action);
		v.setKeyBoardHideAction(action);
		v.initPassGuardKeyBoard(); // 初始化键盘。必须在设置完键盘属性后调用一次，以使键盘能被正常调用

		// _LEDT_Password.clear(); //调用后清空密码框。

	}

	/**
	 * dp转换为px
	 * 
	 * @param context
	 * @param dpValue
	 * @return
	 */
	public static int dipTopx(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/**
	 * px转换为dp
	 * 
	 * @param context
	 * @param pxValue
	 * @return
	 */
	public static int pxTodip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) ((pxValue - 0.5f) / scale);
	}
}
