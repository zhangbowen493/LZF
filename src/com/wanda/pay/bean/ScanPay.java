package com.wanda.pay.bean;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Parcelable;
import android.text.TextUtils;

import com.sitech.oncon.barcode.core.StringUtils;
import com.wanda.pay.util.Arith;
/**
 * 扫码支付实体类
 * @author kevin
 *
 */
public class ScanPay implements Serializable{

	String imageUrl;
	String cardNumber;
	String phoneNumber;
	String bankName;
	String protocolNo;
	String bankCode;
	String bankCardNumber;
	
	
	public String getBankCardNumber() {
		return bankCardNumber;
	}
	public void setBankCardNumber(String bankCardNumber) {
		this.bankCardNumber = bankCardNumber;
	}
	public String getBankCode() {
		return bankCode;
	}
	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}
	public String getProtocolNo() {
		return protocolNo;
	}
	public void setProtocolNo(String protocolNo) {
		this.protocolNo = protocolNo;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public String getCardNumber() {
		return cardNumber;
	}
	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	
	public static ArrayList<ScanPay> fromJsonToCardList(JSONArray jsonArray) throws JSONException {
		if(jsonArray == null) return null;
		ArrayList<ScanPay> datalist = new ArrayList<ScanPay>();
		if(jsonArray == null) return null;
		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject json = jsonArray.optJSONObject(i);
			if(json == null) return null;
			ScanPay data = new ScanPay();
			String phone = json.optString("expandAttribute");
			data.setPhoneNumber(phone);
			String bankCardNo = json.optString("bankCardNo");
			data.setBankCardNumber(bankCardNo);
			String bankName = json.optString("bankName");
			data.setBankName(bankName+"("+bankCardNo.substring(bankCardNo.length()-4, bankCardNo.length())+")");

			data.setProtocolNo(json.optString("protocolNo"));
			
			data.setBankCode(json.optString("bankCode"));
			
			String imageUrl = json.optString("paramName");
			data.setImageUrl(imageUrl);
			datalist.add(data);
		}
		return datalist;
	}


	
	String goodsUrl;
	String goodsName;
	String goodsMoney;
	String goodsGetMoneyName;
	String cardBandUrl;
	String cardBandNumber;

	public String getGoodsUrl() {
		return goodsUrl;
	}
	public void setGoodsUrl(String goodsUrl) {
		this.goodsUrl = goodsUrl;
	}
	public String getGoodsName() {
		return goodsName;
	}
	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}
	public String getGoodsMoney() {
		return goodsMoney;
	}
	public void setGoodsMoney(String goodsMoney) {
		this.goodsMoney = goodsMoney;
	}
	public String getGoodsGetMoneyName() {
		return goodsGetMoneyName;
	}
	public void setGoodsGetMoneyName(String goodsGetMoneyName) {
		this.goodsGetMoneyName = goodsGetMoneyName;
	}
	public String getCardBandUrl() {
		return cardBandUrl;
	}
	public void setCardBandUrl(String cardBandUrl) {
		this.cardBandUrl = cardBandUrl;
	}
	public String getCardBandNumber() {
		return cardBandNumber;
	}
	public void setCardBandNumber(String cardBandNumber) {
		this.cardBandNumber = cardBandNumber;
	}

	public static ScanPay fromJson(JSONObject jsonData) throws JSONException {
		if(jsonData == null) return null;
		ScanPay data = new ScanPay();
		String goodsName = jsonData.optString("s_goods_name")+"  "+jsonData.optString("s_goods_desc");
		data.setGoodsName(goodsName);
		DecimalFormat decimalFormat = new DecimalFormat("0.00");//构造方法的字符格式这里如果小数不足2位,会以0补足.
		String balince = decimalFormat.format(Arith.div(jsonData.optInt("s_amount"), 100));
		String goodsMoney = "¥"+balince;
		data.setGoodsMoney(goodsMoney);
		String goodsgetMoneyName = "收款方: "+jsonData.optString("receiver");
		data.setGoodsGetMoneyName(goodsgetMoneyName);

		String goodsUrl = jsonData.optString("key1");
		data.setGoodsUrl(goodsUrl);
		String cardUrl = jsonData.optString("key4");
		data.setCardBandUrl(cardUrl);
		return data;
	}
}
