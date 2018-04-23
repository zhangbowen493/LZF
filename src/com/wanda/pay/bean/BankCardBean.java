package com.wanda.pay.bean;

import java.io.Serializable;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 银行卡实体类
 * @author kevin
 *
 */
public class BankCardBean implements Serializable{
	
//	1	银行卡名称	bankName	15	String	Y	
	private String _BankName ; 		
//	1	用户号	customerNo	15	String	Y	
	private String _CustomerNo;
//	2	请求系统编码	sysNo	3	String	Y	
	private String _SysNo;
//	3	卡种	cardTypeCode	3	String	Y	
	private String _CardTypeCode;
//	4	银行卡卡号	bankCardNo	20	String	Y	
	private String _BankCardNumber;
//	5	银行卡LOGO	logo	50	String	Y	
	private String _Logo;
//	6	协议号	protocolNo	30	String	Y	
	private String _ProtocolNo;
//	7	签约状态	status	1	String	Y	
	private String _Status;
//	8	是否支持在线解约	ifOnlineTer	1	String	Y	
	private String _IfOnlineTer;
//	9	产品编码	ConPayProCode	5	String	Y	
	private String _ConPayProCode;
//	10	产品名称	ProductName	50	String	Y	
	private String _ProductName;
//	11	是否为默认	IfDefault	1	String	Y	
	private String _IfDefault;
//	12	真实姓名	realName	10	String	Y	
	private String _RealName;
//	13	身份证号码	identityCard	20	String 	Y	
	private String _IdentityCard;
//	14	收单机构编码	acqCode	20	String	Y	
	private String _AcqCode;
//	15	是否支持充值	ifRecharge	1	String	Y	
	private String _IfRecharge;
//	16	是否支持签约结果查询	isSupSignCek	1	String	Y	
	private String _IsSupsignCek;
//	17	签约方式	signType	1	String	Y	1:网银签约 2:手机口令
	private String _SignType;
	
	
	public String get_CustomerNo() {
		return _CustomerNo;
	}
	public void set_CustomerNo(String _CustomerNo) {
		this._CustomerNo = _CustomerNo;
	}
	public String get_SysNo() {
		return _SysNo;
	}
	public void set_SysNo(String _SysNo) {
		this._SysNo = _SysNo;
	}
	public String get_CardTypeCode() {
		return _CardTypeCode;
	}
	public void set_CardTypeCode(String _CardTypeCode) {
		this._CardTypeCode = _CardTypeCode;
	}
	public String get_Logo() {
		return _Logo;
	}
	public void set_Logo(String _Logo) {
		this._Logo = _Logo;
	}
	public String get_Status() {
		return _Status;
	}
	public void set_Status(String _Status) {
		this._Status = _Status;
	}
	public String get_IfOnlineTer() {
		return _IfOnlineTer;
	}
	public void set_IfOnlineTer(String _IfOnlineTer) {
		this._IfOnlineTer = _IfOnlineTer;
	}
	public String get_ConPayProCode() {
		return _ConPayProCode;
	}
	public void set_ConPayProCode(String _ConPayProCode) {
		this._ConPayProCode = _ConPayProCode;
	}
	public String get_ProductName() {
		return _ProductName;
	}
	public void set_ProductName(String _ProductName) {
		this._ProductName = _ProductName;
	}
	public String get_IfDefault() {
		return _IfDefault;
	}
	public void set_IfDefault(String _IfDefault) {
		this._IfDefault = _IfDefault;
	}
	public String get_RealName() {
		return _RealName;
	}
	public void set_RealName(String _RealName) {
		this._RealName = _RealName;
	}
	public String get_IdentityCard() {
		return _IdentityCard;
	}
	public void set_IdentityCard(String _IdentityCard) {
		this._IdentityCard = _IdentityCard;
	}
	public String get_AcqCode() {
		return _AcqCode;
	}
	public void set_AcqCode(String _AcqCode) {
		this._AcqCode = _AcqCode;
	}
	public String get_IfRecharge() {
		return _IfRecharge;
	}
	public void set_IfRecharge(String _IfRecharge) {
		this._IfRecharge = _IfRecharge;
	}
	public String get_IsSupsignCek() {
		return _IsSupsignCek;
	}
	public void set_IsSupsignCek(String _IsSupsignCek) {
		this._IsSupsignCek = _IsSupsignCek;
	}
	public String get_SignType() {
		return _SignType;
	}
	public void set_SignType(String _SignType) {
		this._SignType = _SignType;
	}
	public String get_ProtocolNo() {
		return _ProtocolNo;
	}
	public void set_ProtocolNo(String _ProtocolNo) {
		this._ProtocolNo = _ProtocolNo;
	}
	public String get_BankName() {
		return _BankName;
	}
	public void set_BankName(String _BankName) {
		this._BankName = _BankName;
	}
	public String get_BankCardNumber() {
		return _BankCardNumber;
	}
	public void set_BankCardNumber(String _BankCardNumber) {
		this._BankCardNumber = _BankCardNumber;
	}
	
	public static ArrayList<BankCardBean> fromJsonToList(JSONArray jsonArray) throws JSONException {
		if(jsonArray == null) return null;
		ArrayList<BankCardBean> datalist = new ArrayList<BankCardBean>();
		if(jsonArray == null) return null;
		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject json = jsonArray.optJSONObject(i);
			if(json == null) return null;
			BankCardBean data = new BankCardBean();
			data.set_AcqCode(json.optString("acqCode"));
			data.set_BankCardNumber(json.optString("bankCardNo"));
			data.set_BankName(json.optString("bankName"));
			data.set_CardTypeCode(json.optString("cardTypeCode"));
			data.set_ConPayProCode(json.optString("conPayProCode"));
			data.set_CustomerNo(json.optString("customerNo"));
			data.set_IdentityCard(json.optString("identityCard"));
			data.set_IfDefault(json.optString("ifDefault"));
			data.set_IfOnlineTer(json.optString("ifOnlineTer"));
			data.set_IfRecharge(json.optString("ifRecharge"));
			data.set_IsSupsignCek(json.optString("isSupSignCek"));
			data.set_Logo(json.optString("logo"));
			data.set_ProductName(json.optString("productName"));
			data.set_ProtocolNo(json.optString("protocolNo"));
			data.set_RealName(json.optString("realName"));
			data.set_SignType(json.optString("signType"));
			data.set_Status(json.optString("status"));
			data.set_SysNo(json.optString("sysNo"));
			datalist.add(data);
		}
		return datalist;
	}

}
