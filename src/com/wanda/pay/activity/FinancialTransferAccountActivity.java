package com.wanda.pay.activity;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import cn.passguard.PassGuardEdit;

import com.wanda.pay.R;
import com.wanda.pay.R.string;
import com.wanda.pay.app.WDApplication;
import com.wanda.pay.bean.Constant_data;
import com.wanda.pay.dialog.CustomProgressDialog;
import com.wanda.pay.util.ApiClient;
import com.wanda.pay.util.ImmersedStatusbarUtils;
import com.wanda.pay.util.Result;
import com.wanda.pay.util.SPUtils;
import com.wanda.pay.util.ToastUtil;
import com.wanda.pay.util.Tools;

/*
 *@作者: Administrator
 *@版本: 
 *@version create time：2016-1-29 上午9:50:35
 */
public class FinancialTransferAccountActivity extends BaseActivity implements
OnClickListener {

	private Context mContext;
	private WDApplication mApplication;
	private EditText ftaEDT_Money;
	private EditText ftaEDT_ReceiveAccoutn;
	private PassGuardEdit ftaEDT_PayPassword;
	private TextView ftaTV_AcctountInfo;
	private double mBlance;
	private String _LoginName;
	private String mMoney;
	private int mCommitMoney;
	private TextView LoginName;
	private String lognname;
	private TransferAccountTask mTransferAccountTask;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.act_financial_transfer_account);
		ImmersedStatusbarUtils.initAfterSetContentView(this, null);
		WDApplication.getInstance().addActivity(this);
		mContext = this;
		mApplication = WDApplication.getInstance();
		initView();
		initData();
	}
	/**
	 * 
	 */



	private void initView() {
		ftaTV_AcctountInfo = (TextView) findViewById(R.id.tv_act_fta_blance);
		ftaEDT_Money = (EditText) findViewById(R.id.edt_act_fta_money);
		ftaEDT_ReceiveAccoutn = (EditText) findViewById(R.id.edt_act_fta_account_receive);
		ftaEDT_PayPassword = (PassGuardEdit) findViewById(R.id.edt_act_fta_pay_password);
		LoginName=(TextView) findViewById(R.id.tv_act_weihao_number);
		TextView _BCTV_Title = (TextView) findViewById(R.id.activity_title_tv_name);
		_BCTV_Title.setText("转账");
		findViewById(R.id.act_tv_set_find_password).setOnClickListener(this);
		findViewById(R.id.activity_title_btn_back).setOnClickListener(this);
		findViewById(R.id.btn_act_fta_commit).setOnClickListener(this);

		Tools.initPassGuard(ftaEDT_PayPassword, "^.{6,}$");

		ftaEDT_Money.addTextChangedListener(moneyWatcher);
		ftaEDT_Money.setText("0.00");
	}

	private void initData() {
		_LoginName = SPUtils.getString(mContext, Constant_data.sp_key_loginName, "");
		mBlance = mApplication.getUserBean().getmBlance();


		String userInfo = ""+mBlance+" 元";
		ftaTV_AcctountInfo.setText(userInfo);
		LoginName.setText(_LoginName);
	}


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.activity_title_btn_back:
			MyBack();
			break;
		case R.id.btn_act_fta_commit:
			TransferAccount();
			break;
		case R.id.act_tv_set_find_password:
			Intent intent = new Intent(mContext, FindPasswordActivity.class);
			intent.putExtra("GoType", 2);
			String _LoginName = SPUtils.getString(mContext, Constant_data.sp_key_loginName, "");
			intent.putExtra("accounts", _LoginName);
			startActivity(intent);
			this.finish();
		default:
			break;
		}
	}
	/**
	 * 转账
	 */
	private void TransferAccount() {
		// TODO Auto-generated method stub

		mMoney = ftaEDT_Money.getText().toString();
		mReceiveAccoutn = ftaEDT_ReceiveAccoutn.getText().toString().trim();

		if(!ftaEDT_PayPassword.checkMatch()){
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
		if(mCommitMoney>(mBlance*100)){
			ToastUtil.showLong(mContext, "账户余额不足！");
			return;
		}


		mTransferAccountTask = new TransferAccountTask();
		mTransferAccountTask.execute();
		CustomProgressDialog.showProgressDialog(mContext, "loading",
				mTransferAccountTask);

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		if(mTransferAccountTask!=null)
			mTransferAccountTask.cancel(true);
		super.onDestroy();
	}
	/**
	 * 转账
	 * @author Administrator
	 *
	 */
	private class TransferAccountTask extends AsyncTask<String, Void, Boolean>{

		Result TopUp;
		Exception exception;
		@Override
		protected Boolean doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			Map<String, Object> param1 = new HashMap<String, Object>();
			param1.put("myUserNo", mApplication.getUserBean().getmUserNO());//用户号
			param1.put("otherAccount", mReceiveAccoutn); // 转入账号
			param1.put("acctType", "201"); // 提现总金额
			param1.put("amount", ""+mCommitMoney); // 提现总金额
			param1.put("PINPwd", ftaEDT_PayPassword.getOutput2()); // 支付密码
			try {
				TopUp = ApiClient.TransferAccount(mContext, param1);
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
					lognname = SPUtils.getString(mContext,
							Constant_data.sp_key_loginName, "");
					Intent intent = new Intent(mContext, DisplayingResultsActivity2.class);
					intent.putExtra("fromwhere", "FinancialTransferAccountActivity");
//					intent.putExtra("showinfo", "链支付转账申请提交成功！");
					intent.putExtra("amount", mMoney);
					intent.putExtra("otherAccount", mReceiveAccoutn);
					intent.putExtra("loginName",lognname);
					startActivity(intent);
					mApplication.isRefreshUserInfo = true;
					FinancialTransferAccountActivity.this.finish();
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
			mTransferAccountTask = null;
			super.onCancelled();
		}
	}



	TextWatcher moneyWatcher = new TextWatcher() {
		private boolean isChanged = false;

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
		}

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
				} else if (i == NUM - 3) {
					zeroIndex = i;
					break;
				}
			}
			if (zeroIndex != -1) {
				cuttedStr = cuttedStr.substring(zeroIndex);
			}
			/* 不足3位补0 */
			if (cuttedStr.length() < 3) {
				cuttedStr = "0" + cuttedStr;
			}
			/* 加上dot，以显示小数点后两位 */
			cuttedStr = cuttedStr.substring(0, cuttedStr.length() - 2) + "."
					+ cuttedStr.substring(cuttedStr.length() - 2);

			ftaEDT_Money.setText(cuttedStr);

			ftaEDT_Money.setSelection(cuttedStr.length());
			isChanged = false;
		}
	};
	private String mReceiveAccoutn;

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
}
