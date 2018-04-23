package com.wanda.pay.bean;

import java.io.Serializable;
import java.text.DecimalFormat;

import com.wanda.pay.util.Arith;
import com.wanda.pay.util.StringUtils;

/**
 * 交易记录
 * 
 * @author kevin
 * 
 */
public class TransactionRecordBean implements Serializable{
	// 1. 交易流水号 journalNo 20 String N
	private String journalno;
	// 2. 交易类型 tradeType 5 String Y 交易代码 交易名称
	// 12 充值
	// 12001 银行卡充值
	// 12002 充值卡充值
	// 11 支付
	// 11001 账户支付
	// 11004 绑定支付
	// 15001 转账
	// 39 退款
	// 78004 话费卡充值
	// 12005 快捷支付充值
	// 15002 前向转账
	// 11003 直连网银支付
	// 11002 标准网银支付
	// 11013 页面快捷支付
	// 11014 绑定快捷支付
	// 11015 无磁无密支付
	// 11021 话费支付
	// 31101 商户单笔退款
	// 31102 商户批量退款
	// 31103 统一直连退款 13014 提现
	private String tradeType;
	// 3. 交易时间 tradeTime 14 String Y yyyyMMddHHmmss格式表示。
	private String tradeTime;
	// 4. 交易金额 tradeAmount 19 String Y 单位分
	private String tradeAmount;
	// 5. 交易状态 state 1 String Y 0 交易进行中; 1 交易成功; 2 交易失败;
	private String state;
	// 6. 交易渠道 tradeChnl 2 String Y 00 定时渠道
	// 01 SMS渠道
	// 02 WWW渠道
	// 03 WEB门户渠道
	// 04 IVR渠道
	// 05 POS渠道
	// 06 收单机构
	// 07 WAP渠道
	// 98 日终处理渠道
	// 99 未指定
	private String mTradeChnl;
	// 7. 商品名称 goodsName 64 String N
	private String mGoodsName;
	// 8. 商户名 merName 64 String N
	private String mMerName;
	// 9. 商户订单号 merOrderNo 50 String N
	private String mMerOrderNo;
	// 10. 转入转出标志 transferFlag 1 String N 1表示转入，2表示转出
	private String mTransFerFlag;
	// 11. 转账对方用户名 transferPartner 64 String N
	private String mTransFerPartner;
	// 12. 退款申请时间 refundReqTime 14 String N yyyyMMddHHmmss格式表示。
	private String mRefundReqTime;
	// 13. 退款金额 refundAmount 19 String N
	private String mRefundAmount;
	// 14. 充值银行 depositBank 64 String N 充值银行名称
	private String mDepositBank;

	// 15. 充值卡卡号 expandAttribute Map N 充值卡卡号后四位：
	// [key:：“CARDID”
	// Value：”充值卡卡号后四位”]
	// 16. 提现银行卡号 expandAttribute Map N 提现银行卡号后四位：
	// [key:：“WITHDRAW_CARDNO”
	// Value：”提现银行卡号后四位”]
	// 17. 充值银行卡号 expandAttribute Map N 充值银行卡号后四位：
	// [key:：“BANKCARDNO”
	// Value：”提现银行卡号后四位”]
	// 18. 备注 expandAttribute Map N 备注：
	// [key:：“COMMENTS”
	// Value：”备注”]

	public String getJournalno() {
		return journalno;
	}

	public void setJournalno(String journalno) {
		this.journalno = journalno;
	}

	public String getTradeType() {
		String type = "";
		if ("12".equals(tradeType)) {
			type = "充值";
		} else if ("12001".equals(tradeType)) {
			type = "银行卡充值";
		} else if ("12002".equals(tradeType)) {
			type = "充值卡充值";
		} else if ("11".equals(tradeType)) {
			type = "支付";
		} else if ("11001".equals(tradeType)) {
			type = "账户支付";
		} else if ("11004".equals(tradeType)) {
			type = "绑定支付";
		} else if ("15001".equals(tradeType)) {
			type = "转账";
		} else if ("39".equals(tradeType)) {
			type = "退款";
		} else if ("78004".equals(tradeType)) {
			type = "话费卡充值";
		} else if ("12005".equals(tradeType)) {
			type = "快捷支付充值";
		} else if ("15002".equals(tradeType)) {
			type = "前向转账";
		} else if ("11003".equals(tradeType)) {
			type = "直连网银支付";
		} else if ("11002".equals(tradeType)) {
			type = "标准网银支付";
		} else if ("11013".equals(tradeType)) {
			type = "页面快捷支付";
		} else if ("11014".equals(tradeType)) {
			type = "绑定快捷支付";
		} else if ("11015".equals(tradeType)) {
			type = "无磁无密支付";
		} else if ("11021".equals(tradeType)) {
			type = "话费支付";
		} else if ("31101".equals(tradeType)) {
			type = "商户单笔退款";
		} else if ("31102".equals(tradeType)) {
			type = "商户批量退款";
		} else if ("31103".equals(tradeType)) {
			type = "统一直连退款";
		} else if ("13014".equals(tradeType)) {
			type = " 提现";
		} else if ("11016".equals(tradeType)){
			type="";
		}

		return type;
	}
	/**
	 * 12 充值
		11 支付
		15 转账   
		3   退款       39
		78004    话费卡充值  12
		31101	商户单笔退款
		31102	商户批量退款
		31103	统一直连退款
		13    提现    13014
	 * @return 交易记录类型
	 */
	public int getTradeTypeInt() {
		int type = -1;
		if ("12".equals(tradeType)) {
			type = 12;
		} else if ("12001".equals(tradeType)) {
			type = 12;
		} else if ("12002".equals(tradeType)) {
			type = 12;
		} else if ("11".equals(tradeType)) {
			type = 11;
		} else if ("11001".equals(tradeType)) {
			type = 11;
		} else if ("11004".equals(tradeType)) {
			type = 11;
		} else if ("15001".equals(tradeType)) {
			type = 15;
		} else if ("39".equals(tradeType)) {
			type = 3;
		} else if ("78004".equals(tradeType)) {
			type = 12;
		} else if ("12005".equals(tradeType)) {
			type = 12;
		} else if ("15002".equals(tradeType)) {
			type = 15;
		} else if ("11003".equals(tradeType)) {
			type = 11;
		} else if ("11002".equals(tradeType)) {
			type = 11;
		} else if ("11013".equals(tradeType)) {
			type = 11;
		} else if ("11014".equals(tradeType)) {
			type = 11;
		} else if ("11015".equals(tradeType)) {
			type = 11;
		} else if ("11021".equals(tradeType)) {
			type = 11;
		} else if ("31101".equals(tradeType)) {
			type = 3;
		} else if ("31102".equals(tradeType)) {
			type = 3;
		} else if ("31103".equals(tradeType)) {
			type = 3;
		} else if ("13014".equals(tradeType)) {
			type = 13;
		} else if ("11016".equals(tradeType)){
			type=11;
		}
		
		return type;
	}
	/**
	 *12 充值
		12001 银行卡充值
		12002 充值卡充值
	 11 支付
		11001 账户支付
		11004 绑定支付
		15001 转账
		39 退款
		78004    话费卡充值
		12005	快捷支付充值
		15002	前向转账
		11003	直连网银支付
		11002	标准网银支付
		11013	页面快捷支付
		11014	绑定快捷支付
		11015	无磁无密支付
		11021	话费支付
		31101	商户单笔退款
		31102	商户批量退款
		31103	统一直连退款
		13014    提现
	 * @return
	 */
	public String getTradeWay(){
		String way = getTradeType();
		if ("12".equals(tradeType)) {
		} else if ("12001".equals(tradeType)) {
			way = getmDepositBank()+"";
		} else if ("12002".equals(tradeType)) {
		} else if ("11".equals(tradeType)) {
		} else if ("11001".equals(tradeType)) {
		} else if ("11004".equals(tradeType)) {
		} else if ("15001".equals(tradeType)) {
			way = getmTransFerPartner().substring(getmTransFerPartner().indexOf("=")+1, getmTransFerPartner().length()-1);
		} else if ("39".equals(tradeType)) {
		} else if ("78004".equals(tradeType)) {
		} else if ("12005".equals(tradeType)) {
			way = getmDepositBank()+"";
		} else if ("15002".equals(tradeType)) {
		} else if ("11003".equals(tradeType)) {
		} else if ("11002".equals(tradeType)) {
		} else if ("11013".equals(tradeType)) {
		} else if ("11014".equals(tradeType)) {
		} else if ("11015".equals(tradeType)) {
		} else if ("11021".equals(tradeType)) {
		} else if ("31101".equals(tradeType)) {
		} else if ("31102".equals(tradeType)) {
		} else if ("31103".equals(tradeType)) {
		} else if ("13014".equals(tradeType)) {
			way = getmDepositBank()+"";
		} else if ("11016".equals(tradeType)){
			way = getmDepositBank()+"";
		}
		return way;
	}

	public void setTradeType(String tradeType) {
		this.tradeType = tradeType;
	}

	public String getTradeTime() {
		return tradeTime;
	}

	public void setTradeTime(String tradeTime) {
		this.tradeTime = tradeTime;
	}

	public String getTradeAmount() {
		String money = "0.00元";
		if (tradeAmount != null && !StringUtils.isEmpty(tradeAmount)) {
			double parseDouble = Double.parseDouble(tradeAmount);
			if (parseDouble != 0) {
				DecimalFormat decimalFormat = new DecimalFormat("0.00");//构造方法的字符格式这里如果小数不足2位,会以0补足.
				money = decimalFormat.format(Arith.div(parseDouble, 100)) + "元";
				
//				money = Arith.round(Arith.div(parseDouble, 100), 2) + "元";
			}
		}
		return money;
	}

	public void setTradeAmount(String tradeAmount) {

		this.tradeAmount = tradeAmount;
	}

	public String getState() {
		// 0 交易进行中; 1 交易成功; 2 交易失败;？
		String s = "";
		if ("0".equals(state)) {
			s = "交易处理中";
		} else if ("1".equals(state)) {
			s = "交易成功";
		} else if ("2".equals(state)) {
			s = "交易失败";
		}
		return s;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getmTradeChnl() {
		return mTradeChnl;
	}

	public void setmTradeChnl(String mTradeChnl) {
		this.mTradeChnl = mTradeChnl;
	}

	public String getmGoodsName() {
		return mGoodsName;
	}

	public void setmGoodsName(String mGoodsName) {
		this.mGoodsName = mGoodsName;
	}

	public String getmMerName() {
		return mMerName;
	}

	public void setmMerName(String mMerName) {
		this.mMerName = mMerName;
	}

	public String getmMerOrderNo() {
		return mMerOrderNo;
	}

	public void setmMerOrderNo(String mMerOrderNo) {
		this.mMerOrderNo = mMerOrderNo;
	}

	public String getmTransFerFlag() {
		return mTransFerFlag;
	}

	public void setmTransFerFlag(String mTransFerFlag) {
		this.mTransFerFlag = mTransFerFlag;
	}

	public String getmTransFerPartner() {
		return mTransFerPartner;
	}

	public void setmTransFerPartner(String mTransFerPartner) {
		this.mTransFerPartner = mTransFerPartner;
	}

	public String getmRefundReqTime() {
		return mRefundReqTime;
	}

	public void setmRefundReqTime(String mRefundReqTime) {
		this.mRefundReqTime = mRefundReqTime;
	}

	public String getmRefundAmount() {
		return mRefundAmount;
	}

	public void setmRefundAmount(String mRefundAmount) {
		this.mRefundAmount = mRefundAmount;
	}

	public String getmDepositBank() {
		return mDepositBank;
	}

	public void setmDepositBank(String mDepositBank) {
		this.mDepositBank = mDepositBank;
	}
}
