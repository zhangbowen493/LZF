package com.wanda.pay.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.wanda.pay.bean.Constant_data;

public class ApiClient extends BaseClient {

	private static final String REQUEST_KEY = "jsonparam";

	/**
	 * 拼接url和方法名
	 * 
	 * @param methodName
	 * @return
	 */
	private static String getUrl(String methodName) {
		return URLs.URL_MAIN + methodName + ".do?";
	}

	private static JSONObject postsJSONObject(String url,
			Map<String, Object> params, Map<String, File> files,
			Context mContext) throws Exception {
		Map<String, String> maps = new HashMap<String, String>();
		if (params != null) {
			Set<Entry<String, Object>> set = params.entrySet();
			Iterator<Entry<String, Object>> it = set.iterator();
			while (it.hasNext()) {
				Map.Entry<String, Object> entry = it.next();
				maps.put(entry.getKey(), entry.getValue().toString());
			}
		}

		//增加图片上传 2016.6.7
		if(files!=null){
			//ByteArrayOutputStream baos = new ByteArrayOutputStream();  
			
		}
		
		
		String data = _httpsPost(url, maps, mContext);
		String responseBody = URLDecoder.decode(data, "utf-8");

		String json = responseBody.replaceAll("\\x0a|\\x0d", "");
		LogUtil.i("json   " + json);
		JSONObject jsonObject = null;
		try {
			if (!StringUtils.isEmpty(json)) {
				jsonObject = new JSONObject(json);
			}
		} catch (JSONException e) {
			e.printStackTrace();
			throw e;
		}
		return jsonObject;

	}

	// private static JSONObject postJSONObject(String url,
	// Map<String, Object> params, Map<String, File> files)
	// throws Exception {
	// Map<String, String> maps = new HashMap<String, String>();
	// if (params != null) {
	// Set<Entry<String, Object>> set = params.entrySet();
	// Iterator<Entry<String, Object>> it = set.iterator();
	// while (it.hasNext()) {
	// Map.Entry<String, Object> entry = it.next();
	// maps.put(entry.getKey(), entry.getValue().toString());
	// }
	// }
	//
	// String data = _httpPost(url, maps);
	// String responseBody = URLDecoder.decode(data, "gbk");
	//
	// String json = responseBody.replaceAll("\\x0a|\\x0d", "");
	// JSONObject jsonObject = null;
	// try {
	// jsonObject = new JSONObject(json);
	// } catch (JSONException e) {
	// e.printStackTrace();
	// throw e;
	// }
	// return jsonObject;
	//
	// }

	/**
	 * 将参数map转化成一个参数为key的map
	 * 
	 * @param params
	 *            参数map
	 * @param jsonMark
	 *            方法名
	 * @param key
	 *            json串key名字
	 * @return
	 */
	public static Map<String, Object> dealMap(Context context,
			Map<String, Object> params, String jsonMark) {
		Map<String, Object> param = new HashMap<String, Object>();
		String str;
		String result = getJsonString(context, params, jsonMark);
		LogUtil.i(result);
		try {
			str = URLEncoder.encode(result, "utf-8");
			param.put(REQUEST_KEY, str);
		} catch (UnsupportedEncodingException e) {
			param.put(REQUEST_KEY, result);
			e.printStackTrace();
		}
		return param;
	}

	/**
	 * 获取请求Json数据 包括请求头，体，尾
	 * 
	 * @param methodName
	 * @return
	 */
	public static String getJsonString(Context context,
			Map<String, Object> params, String methodName) {
		StringBuffer jsonHead = new StringBuffer();
		String bodyJson = JSONUtils.getJsonString(params);
		long cuuTime = System.currentTimeMillis();
		String imei = (String) SPUtils.get(context, Constant_data.Phone_imei,
				"");
		String onlyString = StringUtils.MD5(String.valueOf(cuuTime) + imei); // 唯一表示
		String time = TimeUtils
				.getTime(cuuTime, TimeUtils.DEFAULT_DATE_FORMAT2);
		jsonHead.append("{\"head\":{\"identifiCation\":\"");
		jsonHead.append(onlyString);
		jsonHead.append("\",\"reqSysDate\":\"");
		jsonHead.append(time);
		jsonHead.append("\",\"operationCode\":\"");
		jsonHead.append(methodName);
		jsonHead.append("\",\"operationSys\":\"");
		jsonHead.append("211");
		jsonHead.append("\",\"channelType\":\"");
		jsonHead.append("8");
		jsonHead.append("\",\"expandAttribute\":[]");
		jsonHead.append("");
		jsonHead.append("}");
		jsonHead.append(",\"body\":");
		jsonHead.append(bodyJson);
		jsonHead.append(",\"tail\":{\"s_mac\":\"MAC\"}");
		jsonHead.append("}");

		return jsonHead.toString();
	}

	/**
	 * 获取订单信息
	 * 
	 * @param context
	 * @param params
	 * @return
	 */
	public static Result getScanPayInformation(Context context,
			Map<String, Object> params) throws Exception {
		params = dealMap(context, params, "QueryOrderinfo");

		String url = null;
		try {
			url = getUrl("order/query/QueryOrderinfo");
			JSONObject str = postsJSONObject(url, params, null, context);
			return JSONParsing.NoReturnDataRequest(str);
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 扫描付款
	 * 
	 * @param context
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public static Result payMoney(Context context, Map<String, Object> params)
			throws Exception {
		params = dealMap(context, params, "PayReq");

		String url = null;
		try {
			url = getUrl("order/pay/PayReq");
			JSONObject str = postsJSONObject(url, params, null, context);
			return JSONParsing.NoReturnDataRequest(str);
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 获取账户成功绑定的银行卡
	 * 
	 * @param context
	 * @param params
	 * @return
	 */
	public static Result getIsSuccessBankCard(Context context,
			Map<String, Object> params) throws Exception {
		params = dealMap(context, params, "SearchSignedBankCardReq");
		String url = null;
		try {
			url = getUrl("user/query/SearchSignedBankCardReq");
			JSONObject str = postsJSONObject(url, params, null, context);
			return JSONParsing.NoReturnDataRequestToArray(str);
		} catch (Exception e) {
			throw e;
		}
	}

	public static Result GetRecordList(Context mContext,
			Map<String, Object> pramsMap) throws Exception {
		Map<String, Object> params = dealMap(mContext, pramsMap,
				"PerTradeRecordQueryReq");
		String url = null;
		try {
			url = getUrl("user/query/PerTradeRecordQueryReq");
			JSONObject str = postsJSONObject(url, params, null, mContext);
			return JSONParsing.NoReturnDataRequestToArray(str);
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 注册 获取短信验证码
	 * 
	 * @param mContext
	 * @param pramsMap
	 *            参数map
	 * @return
	 * @throws Exception
	 */
	public static Result registGetPhoneCode(Context mContext,
			Map<String, Object> pramsMap) throws Exception {
		Map<String, Object> params = dealMap(mContext, pramsMap,
				"UserNameVerifySendReq");
		String url = null;
		try {
			url = getUrl("admin/varify/UserNameVerify");
			JSONObject str = postsJSONObject(url, params, null, mContext);
			return JSONParsing.NoReturnDataRequest(str);
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 注册验证短信验证码
	 * 
	 * @param mContext
	 * @param pramsMap
	 * @return
	 * @throws Exception
	 */
	public static Result registVerifyPhoneCode(Context mContext,
			Map<String, Object> pramsMap) throws Exception {
		// TODO Auto-generated method stub
		Map<String, Object> params = dealMap(mContext, pramsMap,
				"UserNameVerifyReq");
		String url = null;
		try {
			url = getUrl("admin/varify/UserVerifyReq");
			JSONObject str = postsJSONObject(url, params, null, mContext);
			return JSONParsing.NoReturnDataRequest(str);
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 注册
	 * 
	 * @param mContext
	 * @param pramsMap
	 * @return
	 * @throws Exception
	 */
	public static Result regist(Context mContext, Map<String, Object> pramsMap)
			throws Exception {
		// TODO Auto-generated method stub
		Map<String, Object> params = dealMap(mContext, pramsMap,
				"UserRegisterReq");
		String url = null;
		try {
			url = getUrl("user/regist/UserRegisterReq");
			JSONObject str = postsJSONObject(url, params, null, mContext);
			return JSONParsing.NoReturnDataRequest(str);
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 登录
	 * 
	 * @param mContext
	 * @param pramsMap
	 * @return
	 * @throws Exception
	 */
	public static Result Login(Context mContext, Map<String, Object> pramsMap)
			throws Exception {
		// TODO Auto-generated method stub
		Map<String, Object> params = dealMap(mContext, pramsMap, "UserLoginReq");
		String url = null;
		try {
			url = getUrl("user/login/UserLoginReq");
			JSONObject str = postsJSONObject(url, params, null, mContext);
			return JSONParsing.NoReturnDataRequest(str);
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 修改登录、支付密码 （知道原密码）
	 * 
	 * @param mContext
	 * @param pramsMap
	 * @return
	 * @throws Exception
	 */
	public static Result PasswordModify(Context mContext,
			Map<String, Object> pramsMap) throws Exception {
		Map<String, Object> params = dealMap(mContext, pramsMap,
				"PasswordModifyReq");
		String url = null;
		try {
			url = getUrl("user/modify/PasswordModifyReq");
			JSONObject str = postsJSONObject(url, params, null, mContext);
			return JSONParsing.NoReturnDataRequest(str);
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 获取用户信息
	 * 
	 * @param mContext
	 * @param pramsMap
	 * @return
	 * @throws Exception
	 */
	public static Result GetUserInfo(Context mContext,
			Map<String, Object> pramsMap) throws Exception {
		// TODO Auto-generated method stub
		Map<String, Object> params = dealMap(mContext, pramsMap,
				"PasswordModifyReq");
		String url = null;
		try {
			url = getUrl("user/query/UserInfoQueryReq");
			JSONObject str = postsJSONObject(url, params, null, mContext);
			return JSONParsing.NoReturnDataRequest(str);
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 获取可用银行卡列表
	 * 
	 * @param mContext
	 * @param pramsMap
	 * @return
	 * @throws Exception
	 */
	public static Result GetUsableBanks(Context mContext,
			Map<String, Object> pramsMap) throws Exception {
		// TODO Auto-generated method stub
		Map<String, Object> params = dealMap(mContext, pramsMap,
				"SearchSignedBankCardReq");
		String url = null;
		try {
			url = getUrl("user/query/BankAndProductInfoReq");
			JSONObject str = postsJSONObject(url, params, null, mContext);
			return JSONParsing.NoReturnDataRequestToArray(str);
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 签约申请
	 * 
	 * @param mContext
	 * @param pramsMap
	 * @return
	 * @throws Exception
	 */
	public static Result SignedInfoAppReq(Context mContext,
			Map<String, Object> pramsMap) throws Exception {
		// TODO Auto-generated method stub
		Map<String, Object> params = dealMap(mContext, pramsMap,
				"SignedInfoAppReq");
		String url = null;
		try {
			url = getUrl("user/sign/SignedInfoAppReq");
			JSONObject str = postsJSONObject(url, params, null, mContext);
			return JSONParsing.NoReturnDataRequest(str);
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 签约确认
	 * 
	 * @param mContext
	 * @param pramsMap
	 * @return
	 * @throws Exception
	 */
	public static Result SignedInfoConReq(Context mContext,
			Map<String, Object> pramsMap) throws Exception {
		// TODO Auto-generated method stub
		Map<String, Object> params = dealMap(mContext, pramsMap,
				"SignedInfoConReq");
		String url = null;
		try {
			url = getUrl("user/sign/SignedInfoConReq");
			JSONObject str = postsJSONObject(url, params, null, mContext);
			return JSONParsing.NoReturnDataRequest(str);
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 解约申请
	 * 
	 * @param mContext
	 * @param pramsMap
	 * @return
	 * @throws Exception
	 */
	public static Result CancelSignInfoReq(Context mContext,
			Map<String, Object> pramsMap) throws Exception {
		Map<String, Object> params = dealMap(mContext, pramsMap,
				"CancelSignInfoReq");
		String url = null;
		try {
			url = getUrl("user/sign/CancelSignInfoReq");
			JSONObject str = postsJSONObject(url, params, null, mContext);
			return JSONParsing.NoReturnDataRequest(str);
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 验证登录超时
	 * 
	 * @param baseActivity
	 * @param pramsMap
	 * @return
	 * @throws Exception
	 */
	public static Result CheckLogin(Context mContext,
			Map<String, Object> pramsMap) throws Exception {
		// TODO Auto-generated method stub
		Map<String, Object> params = dealMap(mContext, pramsMap,
				"TimeOutVerifyReq");
		String url = null;
		try {
			url = getUrl("user/login/TimeOutVerifyReq");
			JSONObject str = postsJSONObject(url, params, null, mContext);
			return JSONParsing.NoReturnDataRequest(str);
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 获取验证密保问题
	 * 
	 * @param mContext
	 * @param pramsMap
	 * @return
	 * @throws Exception
	 */
	public static Result PwdQuestionQueryReq(Context mContext,
			Map<String, Object> pramsMap) throws Exception {
		// TODO Auto-generated method stub
		Map<String, Object> params = dealMap(mContext, pramsMap,
				"PwdQuestionQueryReq");
		String url = null;
		try {
			url = getUrl("user/pwd/PwdQuestionQueryReq");
			JSONObject str = postsJSONObject(url, params, null, mContext);
			return JSONParsing.NoReturnDataRequest(str);
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 验证密保问题
	 * 
	 * @param mContext
	 * @param pramsMap
	 * @return
	 * @throws Exception
	 */
	public static Result PwdQuestionCheck(Context mContext,
			Map<String, Object> pramsMap) throws Exception {
		// TODO Auto-generated method stub
		Map<String, Object> params = dealMap(mContext, pramsMap,
				"PwdQuestionCheckReq");
		String url = null;
		try {
			url = getUrl("user/pwd/PwdQuestionCheckReq");
			JSONObject str = postsJSONObject(url, params, null, mContext);
			return JSONParsing.NoReturnDataRequest(str);
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 密码重置
	 * 
	 * @param mContext
	 * @param pramsMap
	 * @return
	 * @throws Exception
	 */
	public static Result RetrievePwdSaveReq(Context mContext,
			Map<String, Object> pramsMap) throws Exception {
		Map<String, Object> params = dealMap(mContext, pramsMap,
				"RetrievePwdSaveReq");
		String url = null;
		try {
			url = getUrl("user/pwd/RetrievePwdSaveReq");
			JSONObject str = postsJSONObject(url, params, null, mContext);
			return JSONParsing.NoReturnDataRequest(str);
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 云账号令牌登录
	 * 
	 * @param mContext
	 * @param pramsMap
	 * @return
	 * @throws Exception
	 */
	public static Result YunLogin(Context mContext, Map<String, Object> pramsMap)
			throws Exception {
		// TODO Auto-generated method stub
		Map<String, Object> params = dealMap(mContext, pramsMap,
				"cloudAccountLogin");
		String url = null;
		try {
			url = getUrl("user/query/");
			JSONObject str = postsJSONObject(url, params, null, mContext);
			return JSONParsing.NoReturnDataRequest(str);
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 已签约银行卡快捷充值
	 * 
	 * @param mContext
	 * @param param1
	 * @return
	 * @throws Exception
	 */
	public static Result QuickRecharge(Context mContext,
			Map<String, Object> param1) throws Exception {
		// TODO Auto-generated method stub
		Map<String, Object> params = dealMap(mContext, param1, "directPayReq");
		String url = null;
		try {
			url = getUrl("user/account/quickRecharge");
			JSONObject str = postsJSONObject(url, params, null, mContext);
			return JSONParsing.NoReturnDataRequest(str);
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 提现申请
	 * 
	 * @param mContext
	 * @param param1
	 * @return
	 * @throws Exception
	 */
	public static Result DrawMoney(Context mContext, Map<String, Object> param1)
			throws Exception {
		// TODO Auto-generated method stub
		Map<String, Object> params = dealMap(mContext, param1, "withdrawBusi");
		String url = null;
		try {
			url = getUrl("user/account/withdrawBusi");
			JSONObject str = postsJSONObject(url, params, null, mContext);
			return JSONParsing.NoReturnDataRequest(str);
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 转账
	 * 
	 * @param mContext
	 * @param param1
	 * @return
	 * @throws Exception
	 */
	public static Result TransferAccount(Context mContext,
			Map<String, Object> param1) throws Exception {
		// TODO Auto-generated method stub
		Map<String, Object> params = dealMap(mContext, param1,
				"transferAccount");
		String url = null;
		try {
			url = getUrl("user/account/transferAccount");
			JSONObject str = postsJSONObject(url, params, null, mContext);
			return JSONParsing.NoReturnDataRequest(str);
		} catch (Exception e) {
			throw e;
		}
	}

}
