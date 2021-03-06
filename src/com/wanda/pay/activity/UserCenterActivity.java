package com.wanda.pay.activity;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.wanda.pay.R;
import com.wanda.pay.R.string;
import com.wanda.pay.app.WDApplication;
import com.wanda.pay.bean.Constant_data;
import com.wanda.pay.bean.UserBean;
import com.wanda.pay.dialog.CustomProgressDialog;
import com.wanda.pay.util.ApiClient;
import com.wanda.pay.util.JSONParsing;
import com.wanda.pay.util.Result;
import com.wanda.pay.util.SPUtils;
import com.wanda.pay.util.ToastUtil;
import com.wanda.pay.util.Tools;
/**
 * 个人信息
 * @author kevin
 *
 */
public class UserCenterActivity extends BaseActivity implements IMVCActivity ,OnClickListener{

	public static UserCenterActivity instance = null;
	private Context mContext = this;
	private TextView _UCTV_Account;
	private TextView _UCTV_Name;
	private TextView _UCTV_RealState;
	private UserBean mUserBean;
	private GetUserInfoTask getUserInfoTask;
	private TextView _UCTV_balance; //余额s
	private WDApplication application;
	private String LoginName;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_center);
		WDApplication.getInstance().addActivity(this);
		instance = this;
		application = WDApplication.getInstance();
		initView();
		init();
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if(getUserInfoTask!=null){
			getUserInfoTask.cancel(true);
		}
	}
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		if(application.isRefreshUserInfo)
			init();
		super.onResume();
	}
	@Override
	public void init() {
		// TODO Auto-generated method stub
		GetUserInfo();
	}


	@Override
	public void initView() {
		// TODO Auto-generated method stub

		_UCTV_Account = (TextView) findViewById(R.id.act_tv_user_account);
		_UCTV_Name = (TextView) findViewById(R.id.act_tv_user_name);
		_UCTV_RealState = (TextView) findViewById(R.id.act_tv_user_real_name_state);
		_UCTV_balance = (TextView) findViewById(R.id.tv_act_uc_balance);



		findViewById(R.id.btn_act_uc_top_up).setOnClickListener(this);//充值
		findViewById(R.id.btn_act_uc_draw_money).setOnClickListener(this);//提现
		findViewById(R.id.btn_act_uc_transfer_account).setOnClickListener(this);//转账
		findViewById(R.id.act_rl_user_login_password).setOnClickListener(this);
		findViewById(R.id.act_rl_user_pay_password).setOnClickListener(this);
		findViewById(R.id.act_rl_user_bank_card).setOnClickListener(this);


		TextView _BCTV_Title = (TextView) findViewById(R.id.activity_title_tv_name);
		_BCTV_Title.setText(getResources().getString(string.user_center));

		findViewById(R.id.activity_title_btn_back).setOnClickListener(this);
	}

	@Override
	public void refresh(Object... param) {
		// TODO Auto-generated method stub
		String _RealNmaeLV = mUserBean.getmRealNmaeLV();
		String _RealNameLv = Tools.RealNameLevel(_RealNmaeLV);

		_UCTV_RealState.setText(_RealNameLv);
		_UCTV_Name.setText(mUserBean.getmRealName());
		_UCTV_Account.setText(mUserBean.getmMoblePhoneNo());
		DecimalFormat decimalFormat = new DecimalFormat("0.00");//构造方法的字符格式这里如果小数不足2位,会以0补足.
		_UCTV_balance.setText("余额："+decimalFormat.format(mUserBean.getmBlance()));
		Log.i("tag", "余额："+decimalFormat.format(mUserBean.getmBlance()));
	}

	@Override
	public void MyBack() {
		// TODO Auto-generated method stub
		finish();
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		// 返回键
		if (keyCode == KeyEvent.KEYCODE_BACK){
			MyBack();
		}
		return super.onKeyDown(keyCode, event);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.act_rl_user_login_password:
			//登录密码管理
			Intent intent = new Intent(mContext,
					SetPasswordActivity.class);
			intent.putExtra("loginName", LoginName);
			intent.putExtra("AccessType", 5);
			startActivity(intent);
			break;
		case R.id.act_rl_user_pay_password:
			//支付密码管理
			Intent intent2 = new Intent(mContext,
					SetPasswordActivity1.class);
			intent2.putExtra("AccessType", 2);
			startActivity(intent2);
			break;
		case R.id.act_rl_user_bank_card:
			//银行卡密码管理
			Intent intent1 = new Intent(mContext,
					BankCenterActivity.class);
			startActivity(intent1);
			break;
		case R.id.activity_title_btn_back:
			//返回
			MyBack();
			break;
		case R.id.btn_act_uc_top_up:
			//充值
			JumpTopUp();
			break;
		case R.id.btn_act_uc_draw_money:
			//提现
			JumpDrawMoney();
			break;
		case R.id.btn_act_uc_transfer_account:
			//转账
			JumpTransferAccount();
			break;
		default:
			break;
		}
	}
	/**
	 * 转账跳转
	 */
	private void JumpTransferAccount() {
		// TODO Auto-generated method stub
		Intent intent = new Intent(mContext, FinancialTransferAccountActivity.class);
		startActivity(intent);
	}
	/**
	 * 提现跳转
	 */
	private void JumpDrawMoney() {
		// TODO Auto-generated method stub
		Intent intent = new Intent(mContext, FinancialDrawMoneyActivity.class);
		startActivity(intent);
	}
	/**
	 * 充值跳转
	 */
	private void JumpTopUp() {
		// TODO Auto-generated method stub
		Intent intent = new Intent(mContext, FinancialTopUpActivity.class);
		startActivity(intent);
	}
	/**
	 * 获取个人信息
	 */
	private void GetUserInfo() {
		// TODO Auto-generated method stub
		CustomProgressDialog.showProgressDialog(mContext, "loading");
		getUserInfoTask = new GetUserInfoTask();
		getUserInfoTask.execute();
	}

	/**
	 * 获取个人信息
	 * @author kevin
	 *
	 */
	private class GetUserInfoTask extends AsyncTask<String , Void, Boolean>{
		String strError;
		JSONObject jsonData;
		Exception exception;
		private Result GetUserResult;

		@Override
		protected Boolean doInBackground(String... prams) {
			// TODO Auto-generated method stub
			try {
				Map<String, Object> pramsMap = new HashMap<String, Object>();
				String _UserNameType = SPUtils.getString(mContext, Constant_data.sp_key_userNameType, "1");
				String _LoginName = SPUtils.getString(mContext, Constant_data.sp_key_loginName, "");
				if(Constant_data.LoginType_Mobile.equals(_UserNameType)){
					pramsMap.put("loginType", "2");
				}else if(Constant_data.LoginType_Email.equals(_UserNameType)){
					pramsMap.put("loginType", "3");
				}else{
					pramsMap.put("loginType", "5");
				}
				pramsMap.put("loginName", _LoginName);

				GetUserResult = ApiClient.GetUserInfo(mContext, pramsMap);
			} catch (Exception e) {
				e.printStackTrace();
				exception = e;
			}
			return GetUserResult != null;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			CustomProgressDialog.hideProgressDialog();
			if(exception != null ){
				Toast.makeText(getApplicationContext(), getResources().getString(string.http_error_anomalies), Toast.LENGTH_LONG).show();
				return;
			}
			if(result){
				if(Constant_data.HTTP_SUCCESS.equals(GetUserResult.coder)){
					mUserBean = JSONParsing.GetUserInfo(GetUserResult.jsonBodyObject);
					if(mUserBean!=null){
						WDApplication.getInstance().setUserBean(mUserBean);
						application.isRefreshUserInfo = false;
						refresh();
					}
				}else{
					strError = GetUserResult.msg;
					ToastUtil.showShort(mContext, strError);
				}
			}else{
				ToastUtil.showShort(mContext, getResources().getString(string.http_error_anomalies));
			}
			super.onPostExecute(result);
		}

		@Override
		protected void onCancelled() {
			getUserInfoTask = null;
			super.onCancelled();
		}

	}

}
