package com.wanda.pay.util;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.wanda.pay.bean.BankBean;
import com.wanda.pay.bean.TransactionRecordBean;
import com.wanda.pay.bean.UserBean;

/**
 * JSON 解析类
 * @author kevin
 *{
"head": {
"identifiCation": "报文唯一标识",
"reqSysDate": "请求系统日期",
"operationCode": "接口编号",
"respCode": "响应码",
"respCodeMemo": "响应信息描述"，
"expandAttribute":"应答扩展字段"
    }, 
    "body": {
      "s_login_name": "用户登录名",
    	  "l_login_type": "用户登录方式",
       "s_zpk_pin": "用户PIN"
}，
"tail": {
"s_mac": "MAC"
}
}

 */
public class JSONParsing {
	
	public static ArrayList<TransactionRecordBean> GetRecordListparsing(JSONArray jsonBodyArray){
		if(jsonBodyArray == null) return null;
		ArrayList<TransactionRecordBean> recordBeansTemp = new ArrayList<TransactionRecordBean>();
		for (int i = 0; i < jsonBodyArray.length(); i++) {
			JSONObject json = jsonBodyArray.optJSONObject(i);
			if(json == null) return null;
			TransactionRecordBean data = new TransactionRecordBean();
			data.setJournalno(json.optString("journalNo"));
			data.setmDepositBank(json.optString("depositBank"));
			data.setmGoodsName(json.optString("goodsName"));
			data.setmMerName(json.optString("merName"));
			data.setmMerOrderNo(json.optString("merOrderNo"));
			data.setmRefundAmount(json.optString("refundAmount"));
			data.setmRefundReqTime(json.optString("refundReqTime"));
			data.setmTradeChnl(json.optString("tradeChnl"));
			data.setmTransFerFlag(json.optString("transferFlag"));
			data.setmTransFerPartner(json.optString("transferPartner"));
			data.setState(json.optString("state"));
			data.setTradeAmount(json.optString("tradeAmount"));
			data.setTradeTime(json.optString("tradeTime"));
			data.setTradeType(json.optString("tradeType"));
			
			recordBeansTemp.add(data);
		}
		return recordBeansTemp;
	}

	/**
	 * 注册
	 * @param jsonData
	 */
	public static Result regist(JSONObject jsonData) {
		// TODO Auto-generated method stub
		Result result = null;
		if(jsonData==null){
			return result;
		}
		JSONObject jsonObject = (JSONObject) jsonData.optJSONObject("head");
		JSONObject jsonObject_body = (JSONObject) jsonData.optJSONObject("body");
		if(jsonObject==null || jsonObject_body == null){
			return result;
		}
		String respCode = (String) jsonObject.opt("respCode");
		String msg = (String) jsonObject.opt("respCodeMemo");
		result = new Result(respCode, msg);
		return result;
	}

	/**
	 * 返回状态数据 携带body 下JSONObject 数据
	 */
	public static Result NoReturnDataRequest(JSONObject jsonData) {
		// TODO Auto-generated method stub
		
		Result result = null;
		if(jsonData==null){
			return result;
		}
		LogUtil.i(jsonData.toString());
		JSONObject jsonObject = (JSONObject) jsonData.optJSONObject("head");
		if(jsonObject==null){
			return result;
		}
		JSONObject optbodyJSONObject = jsonData.optJSONObject("body");
		
		String respCode = (String) jsonObject.opt("respCode");
		String msg = (String) jsonObject.opt("respCodeMemo");
		result = new Result(respCode, msg);
		result.jsonBodyObject = optbodyJSONObject;
		return result;
		
	}
	public static Result NoReturnDataRequestToArray(JSONObject jsonData) {
		// TODO Auto-generated method stub
		Result result = null;
		if(jsonData==null){
			return result;
		}
		LogUtil.i(jsonData.toString());
		JSONObject jsonObject = (JSONObject) jsonData.optJSONObject("head");
		if(jsonObject==null){
			return result;
		}
		JSONArray jsonArray = (JSONArray) jsonData.optJSONArray("body");
		String respCode = (String) jsonObject.opt("respCode");
		String msg = (String) jsonObject.opt("respCodeMemo");
		result = new Result(respCode, msg);
		result.jsonBodyArray = jsonArray;
		return result;
		
	}

	/**
	 * 解析用户数据
	 * @param jsonDataObject
	 * @return
	 */
	public static UserBean GetUserInfo(JSONObject jsonDataObject) {
		// TODO Auto-generated method stub
		if(jsonDataObject==null)
			return null;
		UserBean bean = new UserBean();
		bean.setmCatAddress(jsonDataObject.optString("catAddress"));
		bean.setmEmail(jsonDataObject.optString("email"));
		bean.setmFixPhoneNo(jsonDataObject.optString("fixPhomeno"));
		bean.setmFundStopPayState(jsonDataObject.optString("fundStopPayState"));
		bean.setmIDNo(jsonDataObject.optString("IDNo"));
		bean.setmIDType(jsonDataObject.optString("IDType"));
		bean.setmLoginToken(jsonDataObject.optString("loginToken"));
		bean.setmMoblePhoneNo(jsonDataObject.optString("mobileNo"));
		bean.setmNickLoginName(jsonDataObject.optString("loginName"));
		bean.setmPostCode(jsonDataObject.optString("postCode"));
		bean.setmRealName(jsonDataObject.optString("realName"));
		bean.setmRealNmaeLV(jsonDataObject.optString("realNameLevel"));
		bean.setmRegDate(jsonDataObject.optString("regDate"));
		bean.setmRegStatus(jsonDataObject.optString("regStatus"));
		bean.setmSex(jsonDataObject.optString("sex"));
		bean.setmUserNO(jsonDataObject.optString("userNo"));
		bean.setmUserStatus(jsonDataObject.optString("userStatus"));
		bean.setmBlance(jsonDataObject.optDouble("accBalance")/100);
		
		
		return bean;
	}

	/**
	 * 可用银行卡列表
	 * @param jsonBodyObject
	 * @return
	 */
	public static ArrayList<BankBean> GetUsableBanksInfo(
			JSONArray jsonstr) {
		ArrayList<BankBean> datalist = new ArrayList<BankBean>();
		if(jsonstr == null) return null;
		for (int i = 0; i < jsonstr.length(); i++) {
			JSONObject json = jsonstr.optJSONObject(i);
			if(json == null) return null;
			BankBean data = new BankBean();
				data.setBankCode(json.optString("bankCode"));
				data.setBankLogo(json.optString("bankLogo"));
				data.setBankName(json.optString("bankName"));
				data.setCardTypeCode(json.optString("cardTypeCode"));
				data.setProductCode(json.optString("productCode"));
				data.setProductDesc(json.optString("productDesc"));
				data.setSignType(json.optString("signType"));
				data.setBankID(json.optString("bankId"));
				datalist.add(data);
		}
		return datalist;
	}

	/**
	 * 签约申请
	 * @param jsonBodyObject
	 * @return
	 */
	public static Map<String, String> req(JSONObject jsonBodyObject) {
		// TODO Auto-generated method stub
		if(jsonBodyObject==null)
			return null;
		Map<String, String> map = new HashMap<String, String>();
		map.put("sysNO", jsonBodyObject.optString("sysNO")); //请求系统编码
		map.put("redirectBankURL", jsonBodyObject.optString("redirectBankURL"));  			//跳转到银行的URL
		map.put("protocolNo", jsonBodyObject.optString("protocolNo"));				//协议号
		
		
		return map;
	}

	/**
	 * 登录超时验证
	 * @param jsonBodyObject
	 * @return
	 */
	public static Map<String, String> CheckLoinTime(JSONObject jsonBodyObject) {
		// TODO Auto-generated method stub
		if(jsonBodyObject==null)
			return null;
		Map<String, String> map = new HashMap<String, String>();
		map.put("isLoginTimeOut", jsonBodyObject.optString("isLoginTimeOut")); //是否超时 1 登录等待时间过长0 登录等待时间在可允许范围内
		map.put("lastLoginTime", jsonBodyObject.optString("lastLoginTime"));  //上次登录时间
		
		return map;
	}

	/**
	 * 用户密保问题
	 * @param jsonBodyObject
	 * @return
	 */
	public static Map<String, String> Question(JSONObject jsonBodyObject) {
		// TODO Auto-generated method stub
		if(jsonBodyObject==null)
			return null;
		Map<String, String> map = new HashMap<String, String>();
		map.put("userId", jsonBodyObject.optString("operId")); 
		map.put("pwdQuestionFst", jsonBodyObject.optString("pwdQuestionFst"));  
		map.put("pwdQuestionSec", jsonBodyObject.optString("pwdQuestionSec"));  
		map.put("pwdQuestionTrd", jsonBodyObject.optString("pwdQuestionTrd"));  
		
		return map;
	}

	/**
	 * 密保问题验证
	 * @param jsonBodyObject
	 * @return
	 */
	public static Map<String, String> QuestionCheck(JSONObject jsonBodyObject) {
		// TODO Auto-generated method stub
		if(jsonBodyObject==null)
			return null;
		Map<String, String> map = new HashMap<String, String>();
		map.put("userId", jsonBodyObject.optString("operId")); 				//
		map.put("pwdFindResult", jsonBodyObject.optString("pwdCheckResult"));  //是否成功标识   YES 密码找回成功 NO 密码找回失败

		return map;
	}

	/**
	 * 重置密码
	 * @param jsonBodyObject
	 * @return
	 */
	public static Map<String, String> PwdSave(JSONObject jsonBodyObject) {
		// TODO Auto-generated method stub
		if(jsonBodyObject==null)
			return null;
		Map<String, String> map = new HashMap<String, String>();
		map.put("pwdResetResult", jsonBodyObject.optString("pwdResetResult")); 				//

		return map;
	}

	/**
	 * 云账户验证
	 * @param jsonBodyObject
	 * @return
	 */
	public static Map<String, String> YunLogin(JSONObject jsonBodyObject) {
		// TODO Auto-generated method stub
		if(jsonBodyObject==null)
			return null;
		Map<String, String> map = new HashMap<String, String>();
		
//		用户ID	userId	10	String	Y	
//		用户号	userNo	15	String	N	
//		登录名	loginName	64	String	?	
//		账户类型	accountType	1	String	Y	0：手机  1.邮箱
//		用户状态	userStatus	1	String	Y	用户状态:0-预开户,1-正常状态，
//		9-销户状态
//		手机号码	mobileNo	20	String	N	
//		邮箱	email	64	String	N	
		map.put("userId", jsonBodyObject.optString("userId")); 				//
		map.put("userNo", jsonBodyObject.optString("userNo")); 				//
		map.put("loginName", jsonBodyObject.optString("loginName")); 				//
		map.put("accountType", jsonBodyObject.optString("accountType")); 				//
		map.put("userStatus", jsonBodyObject.optString("userStatus")); 				//
		map.put("mobileNo", jsonBodyObject.optString("mobileNo")); 				//
		map.put("email", jsonBodyObject.optString("email")); 				//

		return map;
	}
}
