package com.wanda.pay.bean;

import java.util.HashMap;
import java.util.Map;

/**
 * 项目常量
 * @author kevin
 *
 */
public class Constant_data {
	
	public static final String PassGuardEditLicense = "aVNYc2Z6OVgrMzlVUlRhL3k5ZDRUSGFkS0o1Ky9DMmdhdHJMdVRCNDhldldWR01yazNORGp4dVNUSXlEVUVCd1Z5aGlVbU1ScXRCTHMrMFpwVHM4MzhxbWk1aXJwOG9tMHlBaHhkRVE2UFFpcXp0S002S2V6N0p5L1JBeEMvbFlLaVE0bDZ1eVZnbnBEaE5LQXFiV3QvbS9YYlJRRUJOelR2dFdlZ3NQTmEwPXsiaWQiOjAsInR5cGUiOiJwcm9kdWN0IiwicGFja2FnZSI6WyJjb20ud2FuZGEucGF5Il0sImFwcGx5bmFtZSI6WyLpk77mlK/ku5giXSwicGxhdGZvcm0iOjJ9";
	
	public static final String passwordMatchStr ="^(?=.{6,16})(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])[0-9a-zA-Z]*$";
	/**http请求成功code*/
	public static final String HTTP_SUCCESS = "99999999";
	/**http请求成功code 针对银行卡签约流程 签约成功解约再签约 */
	public static final String HTTP_SUCCESS_FOR_ADDCARD = "99999998";
	/**用户类型常量  手机号*/
	public static final String LoginType_Mobile = "1";
	/**用户类型常量  邮箱*/
	public static final String LoginType_Email = "2";
	/**用户类型常量  昵称*/
	public static final String LoginType_Nick = "3";
	
	
	/**verifyCodeType 验证码类型 08:用户统一注册手机验证码*/
	public static final String verifyCodeType = "08";

	public static final String sp_key_loginName = "loginName";
	/**sp文件loginTokenKey*/
	public static final String sp_key_userNameType = "userNameType";
	/**sp文件loginTokenKey*/
	public static final String sp_key_verifyCodeType = "verifyCodeType";
	/**sp文件loginTokenKey*/
	public static final String sp_key_loginToken = "login_token";
	/**是否登录*/
	public static final String IS_LOGIN = "isLogin";
	/**版本名称*/
	public static final String Version_name ="version_name";
	/**手机imei号*/
	public static final String Phone_imei ="phone_imei";
	/**SharedPreferences 的字段名称 -- 是否是从后台回啦*/
	public static final String ISCURRENTRUNNINGFOREGROUND = "isCurrentRunningForeground";
	
	
	/**调用支付结果返回标志 - 成功*/
	public static final int PAY_SUCCESS = 99999999;
	/**调用支付结果返回标志 - 取消*/
	public static final int PAY_CANCEL = 0;
	/**调用支付结果返回标志 - 失败*/
	public static final int PAY_FILE = -1;
	
	/**
	 * 交易记录查询类型
	 * 3  退款
	 * 11  支付
	 * 12  充值
	 * 13  提现
	 * 15  转账
	 */
	public static final String FINANCIAL_ALL=null;
	public static final String FINANCIAL_DRAW_BACK="3";
	public static final String FINANCIAL_PAY_MONEY="11";
	public static final String FINANCIAL_TOP_UP="12";
	public static final String FINANCIAL_DRAW_MONEY="13";
	public static final String FINANCIAL_TRANSFER_ACCOUNT="15";
	
	/**拍照返回结果码*/
	public static int RESULTCODE_CAMERA=1;
	/**裁剪返回结果码*/
	public static int RESULTCODE_CROP=2;
	
	/**实名认证图片大小*/
	public static int PHOTO_SIZE=1024;
	/**实名认证图片高度*/
	public static int PHOTO_HEIGHT=450;
	/**实名认证图片宽度*/
	public static int PHOTO_WIDTH=800;
	/**实名认证图片长宽比*/
	public static float PHOTO_SCALE=PHOTO_HEIGHT*1f/PHOTO_WIDTH;
	/**裁剪框宽度*/
	public static int float_width=270;
	/**裁剪框高度*/
	public static int float_height=480;
}
