package com.wanda.pay.bean;
/**
 * 银行实体类
 * @author kevin
 *
 */
public class BankBean {
	
	//银行名称
	private String bankName;
//	1	银行编码	bankCode	20	String	Y	
	private String bankCode;
//	2	银行LOGO	bankLogo	50	String	Y	
	private String bankLogo;
//	3	卡种	cardTypeCode	3	String	Y	目前只有储蓄卡
	private String cardTypeCode;
//	4	快捷支付产品编码	productCode	16	String	Y
	public String productCode;
//	5	快捷支付产品描述	productDesc	30	String	Y	
	public String productDesc;
//	6	签约方式	signType	1	String	Y	1:网银签约	2:手机口令 3:支付签约
	public String signType;
//	7   银行ID 
	public String bankID;
	
	
	public String getBankID() {
		return bankID;
	}
	public void setBankID(String bankID) {
		this.bankID = bankID;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public String getBankCode() {
		return bankCode;
	}
	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}
	public String getBankLogo() {
		return bankLogo;
	}
	public void setBankLogo(String bankLogo) {
		this.bankLogo = bankLogo;
	}
	public String getCardTypeCode() {
		return cardTypeCode;
	}
	public void setCardTypeCode(String cardTypeCode) {
		this.cardTypeCode = cardTypeCode;
	}
	public String getProductCode() {
		return productCode;
	}
	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}
	public String getProductDesc() {
		return productDesc;
	}
	public void setProductDesc(String productDesc) {
		this.productDesc = productDesc;
	}
	public String getSignTypeForMark() {
		return signType;
	}
	public String getSignType() {
		String s = "" ;
//		1:网银签约	2:手机口令 3:支付签约
		if("1".equals(signType)){
			s = "网银签约";
		}else if("2".equals(signType)){
			s = "手机口令";
		}else if("3".equals(signType)){
			s = "支付签约";
		}
		return s;
	}
	public void setSignType(String signType) {
		this.signType = signType;
	}
	
	

}
