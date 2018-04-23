package com.wanda.pay.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import cn.passguard.PassGuardEdit;

import com.wanda.pay.R;
import com.wanda.pay.R.string;
import com.wanda.pay.app.WDApplication;
import com.wanda.pay.bean.BankCardBean;
import com.wanda.pay.bean.Constant_data;
import com.wanda.pay.bean.ScanPay;
import com.wanda.pay.dialog.CustomProgressDialog;
import com.wanda.pay.util.ApiClient;
import com.wanda.pay.util.Result;
import com.wanda.pay.util.SPUtils;
import com.wanda.pay.util.ToastUtil;
import com.wanda.pay.util.Tools;

/*
 *@作者: Administrator
 *@版本: 
 *@version create time：2016-1-27 上午11:10:04
 */
public class FinancialTopUpActivity extends BaseActivity implements
OnClickListener {

	private GetCardInfTask mGetCardInfTask;
	private WDApplication mApplication;
	protected String mBankProtocolNo;
	private Context mContext;
	private ArrayList<ScanPay> mCardDataList = new ArrayList<ScanPay>();
	private int bankPosition;
	protected boolean popWindowIsShow;
	private EditText ftuEDT_Money;
	private PassGuardEdit ftuEDT_PayPassword;
	private TextView ftuTV_BankCard;
	private TextView ftuTv_bankname;
	private TextView ftuTv_bankweihao;
//	private RelativeLayout Rela_click;
	private ImageView Image_src;
	private String mMoney;
	private String mPayWD;
	private QuickRechargeTask mRechargeTask;

	private int mCommitMoney = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_financial_top_up);
		WDApplication.getInstance().addActivity(this);
		mContext = this;
		mApplication = WDApplication.getInstance();
		initView();
		initData();
	}

	private void initView() {
		ftuEDT_Money = (EditText) findViewById(R.id.edt_act_ftu_money);
		ftuEDT_PayPassword = (PassGuardEdit) findViewById(R.id.edt_act_ftu_pay_password);
		ftuTV_BankCard = (TextView) findViewById(R.id.tv_act_ftu_bankcard_number);
		ftuTv_bankname=(TextView) findViewById(R.id.tv_act_ftu_bankcard_tianjia);
		ftuTv_bankweihao=(TextView) findViewById(R.id.tv_act_weihao_number);
//		Rela_click=(RelativeLayout) findViewById(R.id.scan_choice_bank_layout);
		TextView _BCTV_Title = (TextView) findViewById(R.id.activity_title_tv_name);
		Image_src=(ImageView) findViewById(R.id.Image_src);
		_BCTV_Title.setText("帐户充值");
//		Rela_click.setOnClickListener(this);
		findViewById(R.id.activity_title_btn_back).setOnClickListener(this);
		findViewById(R.id.btn_act_ftu_commit).setOnClickListener(this);
		ftuTV_BankCard.setOnClickListener(this);
		Tools.initPassGuard(ftuEDT_PayPassword, "^.{6,}$");
		ftuEDT_Money.addTextChangedListener(moneyWatcher);
		ftuEDT_Money.setText("");
	}




	private void initData() {
		mBlance = mApplication.getUserBean().getmBlance();
		getCardList();
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if(mApplication.isAddSuccess)
			getCardList();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.activity_title_btn_back:
			MyBack();
			break;
		case R.id.btn_act_ftu_commit:
			TopUpCommit();
			break;
		case R.id.tv_act_ftu_bankcard_tianjia:
			// 选择银行卡
			SelcetBankCard();
			break;
		default:
			break;
		}
	}

	private void SelcetBankCard() {
		// TODO Auto-generated method stub
		if (mCardDataList != null && !mCardDataList.isEmpty()) {
			if (mCardDataList.size() == 0 || bankPosition == -1) {
				ftuTV_BankCard.setVisibility(View.VISIBLE);
				showAddCardDialog();
			} else {
				//去选择页面
				JumpBankList();
			}
		} else {
			ftuTV_BankCard.setVisibility(View.VISIBLE);
			showAddCardDialog();
		}
	}
	private void showAddCardDialog() {
		final Dialog mDialog = new Dialog(mContext, R.style.myDilog);
		mDialog.setContentView(R.layout.dialog_layout);
		Button sure = (Button) mDialog.findViewById(R.id.dialog_btn_ok);
		Button back = (Button) mDialog.findViewById(R.id.dialog_btn_cancel);
		TextView _DialogTV = (TextView) mDialog
				.findViewById(R.id.dialog_tv_msg);
		_DialogTV.setText("您还没有添加银行卡，是否现在添加?");
		mDialog.setCanceledOnTouchOutside(false);
		sure.setText("是");
		back.setText("否");
		sure.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				bankPosition = -1;
				startActivity(new Intent(mContext, AddCardOneActivity.class));
				WDApplication.isAddCard = true;

				if (mDialog.isShowing())
					mDialog.dismiss();
			}
		});
		back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mDialog.isShowing())
					mDialog.dismiss();
			}
		});
		Window window = mDialog.getWindow();
		window.setGravity(Gravity.CENTER); // 此处可以设置dialog显示的位置
		window.setWindowAnimations(R.style.mystyle); // 添加动画
		mDialog.show();
	}
	/**
	 * 跳转到列表选择页
	 */
	private void JumpBankList() {
		// TODO Auto-generated method stub
		Intent intent = new Intent(mContext, SelectBankCardActivity.class);
		intent.putExtra("cardList", mCardDataList);
		startActivityForResult(intent, 900);
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);

		if(requestCode==900 && resultCode == 999){
			int number = data.getIntExtra("number", 0);
			ftuTv_bankname.setText(mCardDataList.get(number).getBankName());
			mBankProtocolNo = mCardDataList.get(number).getProtocolNo();
			mBankCode = mCardDataList.get(number).getBankCode();
			mBankCardNumber = mCardDataList.get(number).getBankCardNumber();
			bankPosition = number;
		}

	}

	/**
	 * 后退接口
	 */
	private void MyBack() {
		// TODO Auto-generated method stub
		finish();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		// 返回键
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			MyBack();
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		if(mGetCardInfTask!=null)
			mGetCardInfTask.cancel(true);
		if(mRechargeTask!=null)
			mRechargeTask.cancel(true);
		super.onDestroy();
	}

	/**
	 * 获取已签约银行卡列表
	 */
	private void getCardList() {
		// TODO Auto-generated method stub
		mGetCardInfTask = new GetCardInfTask();
		mGetCardInfTask.execute();
		CustomProgressDialog.showProgressDialog(mContext, "loading",
				mGetCardInfTask);
	}

	/**
	 * 提交充值表单
	 */
	private void TopUpCommit() {
		// TODO Auto-generated method stub
		mMoney = ftuEDT_Money.getText().toString();
		mPayWD = ftuEDT_PayPassword.getOutput2();

		if(bankPosition==-1){
			showAddCardDialog();
			return;
		}

		if(!ftuEDT_PayPassword.checkMatch()){
			ToastUtil.showLong(mContext, "请输入正确的支付密码！");
			return;
		}
		if(Tools.isNumeric(mMoney)){
			mCommitMoney = (int) (Double.parseDouble(mMoney)*100);
		}else{
			ToastUtil.showLong(mContext, "请输入正确金额！");
			return;
		}
		if(mCommitMoney<1){
			ToastUtil.showLong(mContext, "请输入正确金额！");
			return;
		}

		mRechargeTask = new QuickRechargeTask();
		mRechargeTask.execute();
		CustomProgressDialog.showProgressDialog(mContext, "loading",
				mRechargeTask);

	}
	/**
	 * 充值
	 * @author Administrator
	 *
	 */
	private class QuickRechargeTask extends AsyncTask<String, Void, Boolean>{

		Result TopUp;
		Exception exception;
		//		用户身份识别标志		1	userIdentifyType	2:支付系统用户号3:商户绑定协议号4:支付系统登录名
		//		支付系统用户号	a	16	userNo	当用户身份识别标志为2时必填，其他无意义
		//		用户登录名	n	32	loginName	当用户身份识别标志为4时必填，其他无意义
		//		用户登录方式	a	1	loginType	当用户身份识别标志为4时必填，其他无意义1:手机号登录2:邮箱登录3:昵称登录
		//		协议号确认方式	a	1	agrnoConfirmType	1:快捷协议号，直接根据快捷协议号确认2:银行卡号/账号，根据银行卡号或账号确认银行绑定协议号9:默认绑定协议，根据默认银行绑定信息确认银行确认
		//		银行协议号	a	30	kpayAgrno	当银行协议确认方式是1时必填，其他无意义
		//		银行卡号	a	32	bankAcctNo	当银行协议确认方式是2时必填，其他无意义
		//		合作银行号	a	16	cobankCode	当银行协议确认方式是2时必填，其他无意义
		//		充值金额	a	10	amount	订单金额，单位：分
		//		支付密码			pawd	加密后
		@Override
		protected Boolean doInBackground(String... arg0) {
			// TODO Auto-generated method stub

			Map<String, Object> param1 = new HashMap<String, Object>();
			param1.put("userIdentifyType", "2");
			param1.put("userNo", mApplication.getUserBean().getmUserNO()); // 用户号
			param1.put("agrnoConfirmType", "1");//1:快捷协议号，直接根据快捷协议号确认
			param1.put("kpayAgrno", mBankProtocolNo); // 银行协议号
			param1.put("amount", ""+mCommitMoney); // 金额
			param1.put("pawd", mPayWD); // 支付密码

			try {
				TopUp = ApiClient.QuickRecharge(mContext, param1);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return TopUp != null;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			// TODO Auto-generated method stub

			CustomProgressDialog.hideProgressDialog();
			if (exception != null) {
				Toast.makeText(getApplicationContext(),
						getResources().getString(string.http_error_anomalies),
						Toast.LENGTH_LONG).show();
				return;
			}
			if(result){
				if(Constant_data.HTTP_SUCCESS.equals(TopUp.coder)){
					Intent intent = new Intent(mContext, DisplayingResultsActivity.class);
					intent.putExtra("fromwhere", "FinancialTopUpActivity");
					intent.putExtra("showinfo", "链支付签约银行卡快捷充值申请提交成功！");
					startActivity(intent);
					mApplication.isRefreshUserInfo = true;
					FinancialTopUpActivity.this.finish();
				}else{
					String strError = TopUp.msg;
					ToastUtil.showShort(mContext, strError);
				}
			}else{
				ToastUtil.showShort(mContext, getResources().getString(string.http_error_anomalies));
			}

			super.onPostExecute(result);
		}

		@Override
		protected void onCancelled() {
			// TODO Auto-generated method stub
			mRechargeTask = null;
			super.onCancelled();
		}
	}

	/**
	 * 查询已添加银行卡
	 * 
	 * @author kevin
	 * 
	 */
	private class GetCardInfTask extends AsyncTask<String, Void, Boolean> {
		// Result dataInf;
		Result dataCardInf;
		Exception exception;

		@Override
		protected Boolean doInBackground(String... params) {
			mCardDataList.clear();
			try {

				String _LoginName = SPUtils.getString(mContext,
						Constant_data.sp_key_loginName, "");
				Map<String, Object> param1 = new HashMap<String, Object>();
				param1.put("customerNo", mApplication.getUserBean()
						.getmUserNO()); // 用户编号
				param1.put("sysNo", "1"); // 请求系统编码
				param1.put("loginName", _LoginName);// 用户登录名
				param1.put("status", "1"); // 1查询正在签约和已经签约的银行卡 2查询成功绑定的银行卡
				// 3查询默认的银行卡
				dataCardInf = ApiClient.getIsSuccessBankCard(mContext, param1);

			} catch (Exception e) {
				e.printStackTrace();
				exception = e;
			}
			return dataCardInf != null;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			CustomProgressDialog.hideProgressDialog();
			if (exception != null) {
				Toast.makeText(getApplicationContext(),
						getResources().getString(string.http_error_anomalies),
						Toast.LENGTH_LONG).show();
				return;
			}
			if (result) {
				if (Constant_data.HTTP_SUCCESS.equals(dataCardInf.coder)) {
					try {
						mCardDataList = ScanPay
								.fromJsonToCardList(dataCardInf.jsonBodyArray);
						if (mCardDataList != null && mCardDataList.size() > 0) {
							bankPosition = 0;
							//显示隐藏银行名称、银行图标、银行尾号
							ftuTV_BankCard.setVisibility(View.GONE);
							ftuTv_bankname.setVisibility(View.VISIBLE);
							ftuTv_bankweihao.setVisibility(View.VISIBLE);
							Image_src.setVisibility(View.VISIBLE);
							ftuTv_bankname.setText(mCardDataList.get(0)
									.getBankName());
							mBankProtocolNo = mCardDataList.get(0)
									.getProtocolNo();
						} else {
							bankPosition = -1;
							//显示隐藏银行名称、银行图标、银行尾号
							ftuTv_bankname.setVisibility(View.GONE);
							ftuTv_bankweihao.setVisibility(View.GONE);
							Image_src.setVisibility(View.GONE);
							ftuTV_BankCard.setVisibility(View.VISIBLE);
							ftuTV_BankCard.setText("请添加银行卡");
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {
					ToastUtil.showShort(mContext, dataCardInf.msg);
				}
			} else {
				Toast.makeText(getApplicationContext(),
						getResources().getString(string.http_error_anomalies),
						Toast.LENGTH_LONG).show();

			}
			super.onPostExecute(result);
		}

		@Override
		protected void onCancelled() {
			mGetCardInfTask = null;
			super.onCancelled();
		}
	}
	TextWatcher moneyWatcher = new TextWatcher() {    
		private boolean isChanged = false;    
		@Override    
		public void onTextChanged(CharSequence s, int start, int before,    
				int count) { }    
		@Override    
		public void beforeTextChanged(CharSequence s, int start, int count,    
				int after) { }    
		@Override    
		public void afterTextChanged(Editable s) {    
			// TODO Auto-generated method stub    
			if (isChanged) {// ----->如果字符未改变则返回    
				return;    
			}    
			String str = s.toString();    

			isChanged = true;    
			String cuttedStr = str;    
			/* 删除字符串中的dot */    
			for (int i = str.length() - 1; i >= 0; i--) {    
				char c = str.charAt(i);    
				if ('.' == c) {    
					cuttedStr = str.substring(0, i) + str.substring(i + 1);    
					break;    
				}    
			}    
			/* 删除前面多余的0 */    
			int NUM = cuttedStr.length();   
			int zeroIndex = -1;  
			for (int i = 0; i < NUM - 2; i++) {    
				char c = cuttedStr.charAt(i);    
				if (c != '0') {    
					zeroIndex = i;  
					break;  
				}else if(i == NUM - 3){  
					zeroIndex = i;  
					break;  
				}  
			}    
			if(zeroIndex != -1){  
				cuttedStr = cuttedStr.substring(zeroIndex);  
			}  
			/* 不足3位补0 */    
			if (cuttedStr.length() < 3) {    
				cuttedStr = "0" + cuttedStr;    
			}    
			/* 加上dot，以显示小数点后两位 */    
			cuttedStr = cuttedStr.substring(0, cuttedStr.length() - 2)    
					+ "." + cuttedStr.substring(cuttedStr.length() - 2);    

			ftuEDT_Money.setText(cuttedStr);    

			ftuEDT_Money.setSelection(cuttedStr.length());    
			isChanged = false;    
		}    
	};
	private String mBankCode;
	private String mBankCardNumber;
	private double mBlance;
}
