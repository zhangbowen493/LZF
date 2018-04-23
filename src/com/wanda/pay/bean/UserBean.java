package com.wanda.pay.bean;

import java.text.DecimalFormat;

import com.wanda.pay.util.Arith;

/**
 * 用户信息
 * @author kevin
 *
 */
public class UserBean {
	private String mUserID;
//	0.   logintoken (用于超时验证)
	private String mLoginToken;
//	1.		用户号	userNo	20	String	N	
	private String mUserNO ; 
//	2.		真实姓名	realName	20	String	N	
	private String mRealName ;
//	3.		用户状态	userStatus	1	String	N	用户状态:0-预开户,1-正常状态，9-销户状态
	private String mUserStatus;
//	4.		手机号码	mobileNo	20	String	N	
	private String mMoblePhoneNo;
//	5.		邮箱	email	64	String	N	
	private String mEmail;
//	6.		字符串登录名	loginName	64	String	N	个性登录名
	private String mNickLoginName;
//	7.		实名级别	realNameLevel	1	String	N	实名级别:1 强实名,2 弱实名 ,3 非实名,4-实名审核中
	private String mRealNmaeLV;
//	8.		注册状态	regStatus	1	String	Y	0: 未注册 1：已注册
	private String mRegStatus;
//	9.		用户注册日期	regDate	8	String	N	注册日期；格式：yyyymmdd
	private String mRegDate;
//	10.		性别	sex	1	String	N	用户性别
	private String mSex;
//	11.		证件类型	IDType		String	N	证件类型
	private String mIDType;
//	12.		证件号码	IDNo		String	N	证件号码
	private String mIDNo;
//	13.		固定电话	fixPhomeno		String	N	固定电话
	private String mFixPhoneNo;
//	14.		联系地址	catAddress		String	N	联系地址
	private String mCatAddress;
//	15.		邮编	postCode		String	N	邮政编码
	private String mPostCode;
//	16.		资金止付状态	fundStopPayState		String	N	资金止付状态：0 -未止付 1-止付
	private String mFundStopPayState;
	// 账户余额
	private double mBlance = 0.00;
	
	public double getmBlance() {
		
		return mBlance;
	}
	public void setmBlance(double mBlance) {
		this.mBlance = mBlance;
	}
	public String getmLoginToken() {
		return mLoginToken;
	}
	public void setmLoginToken(String mLoginToken) {
		this.mLoginToken = mLoginToken;
	}
	public String getmUserNO() {
		return mUserNO;
	}
	public void setmUserNO(String mUserNO) {
		this.mUserNO = mUserNO;
	}
	public String getmRealName() {
		return mRealName;
	}
	public void setmRealName(String mRealName) {
		this.mRealName = mRealName;
	}
	public String getmUserStatus() {
		return mUserStatus;
	}
	public void setmUserStatus(String mUserStatus) {
		this.mUserStatus = mUserStatus;
	}
	public String getmMoblePhoneNo() {
		return mMoblePhoneNo;
	}
	public void setmMoblePhoneNo(String mMoblePhoneNo) {
		this.mMoblePhoneNo = mMoblePhoneNo;
	}
	public String getmEmail() {
		return mEmail;
	}
	public void setmEmail(String mEmail) {
		this.mEmail = mEmail;
	}
	public String getmNickLoginName() {
		return mNickLoginName;
	}
	public void setmNickLoginName(String mNickLoginName) {
		this.mNickLoginName = mNickLoginName;
	}
	public String getmRealNmaeLV() {
		return mRealNmaeLV;
	}
	public void setmRealNmaeLV(String mRealNmaeLV) {
		this.mRealNmaeLV = mRealNmaeLV;
	}
	public String getmRegStatus() {
		return mRegStatus;
	}
	public void setmRegStatus(String mRegStatus) {
		this.mRegStatus = mRegStatus;
	}
	public String getmRegDate() {
		return mRegDate;
	}
	public void setmRegDate(String mRegDate) {
		this.mRegDate = mRegDate;
	}
	public String getmSex() {
		return mSex;
	}
	public void setmSex(String mSex) {
		this.mSex = mSex;
	}
	public String getmIDType() {
		return mIDType;
	}
	public void setmIDType(String mIDType) {
		this.mIDType = mIDType;
	}
	public String getmIDNo() {
		return mIDNo;
	}
	public void setmIDNo(String mIDNo) {
		this.mIDNo = mIDNo;
	}
	public String getmFixPhoneNo() {
		return mFixPhoneNo;
	}
	public void setmFixPhoneNo(String mFixPhoneNo) {
		this.mFixPhoneNo = mFixPhoneNo;
	}
	public String getmCatAddress() {
		return mCatAddress;
	}
	public void setmCatAddress(String mCatAddress) {
		this.mCatAddress = mCatAddress;
	}
	public String getmPostCode() {
		return mPostCode;
	}
	public void setmPostCode(String mPostCode) {
		this.mPostCode = mPostCode;
	}
	public String getmFundStopPayState() {
		return mFundStopPayState;
	}
	public void setmFundStopPayState(String mFundStopPayState) {
		this.mFundStopPayState = mFundStopPayState;
	}
	public String getmUserID() {
		return mUserID;
	}
	public void setmUserID(String mUserID) {
		this.mUserID = mUserID;
	}



}
