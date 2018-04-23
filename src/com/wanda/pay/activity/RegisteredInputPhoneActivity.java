package com.wanda.pay.activity;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.wanda.pay.R;
import com.wanda.pay.R.string;
import com.wanda.pay.app.WDApplication;
import com.wanda.pay.bean.Constant_data;
import com.wanda.pay.bean.HandlerConstant;
import com.wanda.pay.dialog.CustomProgressDialog;
import com.wanda.pay.util.ApiClient;
import com.wanda.pay.util.ImmersedStatusbarUtils;
import com.wanda.pay.util.Result;
import com.wanda.pay.util.StringUtils;
import com.wanda.pay.util.ToastUtil;
import com.wanda.pay.util.Tools;
import com.wanda.pay.util.URLs;
/**
 * 注册页面 输入手机号码
 * @author kevin
 *
 */
public class RegisteredInputPhoneActivity extends BaseActivity implements
IMVCActivity , OnClickListener{
	Handler RCPHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			CustomProgressDialog.hideProgressDialog();
			switch (msg.what) {
			case HandlerConstant.TimeChange:
				if(tempTime>0){
					_RCBTN_GetAgain.setClickable(false);
					String s  = getResources().getString(string.tv_reg_check_again_send);
					String format = String.format(s, tempTime);
					_RCBTN_GetAgain.setText(format);
					tempTime--;
				}else{
					tempTime = 60;
					_RCBTN_GetAgain.setClickable(true);
					_RCBTN_GetAgain.setText("重新获取");
					stopTime();
				}

				break;
			default:
				break;
			}
		};
	};
	int tempTime = 60;
	private Timer timer;
	private TimerTask task;

	private void startTimer(){
		timer = new Timer(true);
		task = new TimerTask() {
			@Override
			public void run() {
				Message msg = RCPHandler.obtainMessage();
				msg.what = HandlerConstant.TimeChange;
				RCPHandler.sendMessage(msg);
			}
		};
		if(timer!=null && task!=null){
			timer.schedule(task, 0, 1000);
		}
	}

	/**
	 * 停止
	 */
	private void stopTime() {
		// TODO Auto-generated method stub
		if(timer!=null){
			timer.cancel();
			timer=null;
		}
		if(task !=null){
			task.cancel();
			task=null;
		}
	}

	private Context mContext = this;

	/**用户注册帐号*/
	private String _Extra_Phone;

	/**帐号输入框*/
	private EditText _RIEDT_Phone;
	/**输入短信验证码*/
	private EditText _RCEDT_Phone_Code;
	/**再次获取短信验证码*/
	private Button _RCBTN_GetAgain;
	private String CheckCode;
	private CheckBox _RCCB;
	private TextView _RCTV_Agreement;
	//	private Button _RCBTN_Regist;
	private GetPhoneCodeTask phoneCodeTask;
	private VerifyPhoneCodeTask verifyPhoneCodeTask;
	private Button _RIBTN_Next;
	private String _LoginNameType;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_regist_input_phone);
		ImmersedStatusbarUtils.initAfterSetContentView(this, null);
		WDApplication.getInstance().addActivity(this);
		init();
		initView();
		refresh();
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		tempTime = 60;
		_RCBTN_GetAgain.setClickable(true);
		_RCBTN_GetAgain.setText("重新获取");
		stopTime();
	}

	@Override
	public void init() {
		_Extra_Phone = getIntent().getStringExtra("Phone");

	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
		_RCTV_Agreement = (TextView) findViewById(R.id.act_tv_regitst_service_agreement);
		_RIEDT_Phone = (EditText) findViewById(R.id.activity_regist_edt_phone);
		_RCEDT_Phone_Code = (EditText) findViewById(R.id.activity_regist_check_edt_check_code);
		_RCBTN_GetAgain = (Button) findViewById(R.id.activity_regist_get_check_btn_again);	
		_RCBTN_GetAgain.setOnClickListener(this);
		_RCTV_Agreement.setOnClickListener(this);
		//		_RCTV_Agreement.setText(Html.fromHtml("<u>"+getResources().getString(string.service_agreement)+"</u>"));

		_RCCB = (CheckBox) findViewById(R.id.act_cb_regitst_agreed);
		_RCCB.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				// TODO Auto-generated method stub
				if(arg1){
					_RIBTN_Next.setEnabled(true);
				}else{
					_RIBTN_Next.setEnabled(false);
				}
			}
		});
		TextView _RITV_Title = (TextView) findViewById(R.id.activity_title_tv_name);
		_RITV_Title.setText("注册");

		_RIBTN_Next = (Button) findViewById(R.id.activity_regist_btn_getcode);
		_RIBTN_Next.setEnabled(false);
		_RIBTN_Next.setOnClickListener(this);
		findViewById(R.id.activity_title_btn_back).setOnClickListener(this);

	}

	@Override
	public void refresh(Object... param) {
		// TODO Auto-generated method stub
		if(!StringUtils.isEmpty(_Extra_Phone)){
			_RIEDT_Phone.setText(_Extra_Phone);
		}
		_RCBTN_GetAgain.setText("获取验证码");
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
		case R.id.activity_regist_btn_getcode:
			VerifyPhoneCode();
			break;
		case R.id.act_tv_regitst_service_agreement:
			//服务协议
			Intent intent = new Intent(mContext, WbeVeiwActivity.class);
			intent.putExtra("url", URLs.URL_MAIN+URLs.regist_url);
			startActivity(intent);
			break;
		case R.id.activity_regist_get_check_btn_again:
			//再次获取短信验证码
			_Extra_Phone = _RIEDT_Phone.getText().toString();
			_LoginNameType = Tools.LoginType(_Extra_Phone);

			if(Constant_data.LoginType_Nick.equals(_LoginNameType)){
				ToastUtil.showShort(mContext, "注册帐号暂时只支持‘手机号’和‘邮箱地址’！");
				return;
			}
			if(Constant_data.LoginType_Mobile.equals(_LoginNameType)){
				if(StringUtils.isEmpty(_Extra_Phone)){
					ToastUtil.showShort(mContext, "请输入十一位手机号");
				}else {
					GetPhoneCoder(_Extra_Phone);
					_RCEDT_Phone_Code.setText("");
				}
			}else if(Constant_data.LoginType_Email.equals(_LoginNameType)){
				if(StringUtils.isEmpty(_Extra_Phone)){
					ToastUtil.showShort(mContext, "请输入正确邮箱地址！");
				}else {
					GetPhoneCoder(_Extra_Phone);
					_RCEDT_Phone_Code.setText("");
				}
			}

			break;

		case R.id.activity_title_btn_back:
			//返回
			MyBack();
			break;
		default:
			break;
		}
	}

	/**
	 * 验证短信验证码
	 */
	private void VerifyPhoneCode() {
		// TODO Auto-generated method stub
		CheckCode = _RCEDT_Phone_Code.getText().toString();
		if(StringUtils.isEmpty(CheckCode)){
			ToastUtil.showShort(mContext, "请输入短信验证码！");
			return;
		}
		CustomProgressDialog.showProgressDialog(mContext, "loading");
		verifyPhoneCodeTask = new VerifyPhoneCodeTask();
		verifyPhoneCodeTask.execute();
	}

	/**
	 * 获取验证码
	 * @param phone		手机号码
	 */
	private void GetPhoneCoder(String phone) {
		// TODO Auto-generated method stub
		CustomProgressDialog.showProgressDialog(mContext, "loading");
		_RIBTN_Next.setEnabled(false);
		phoneCodeTask = new GetPhoneCodeTask();
		phoneCodeTask.execute(phone);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if(phoneCodeTask!=null)
			phoneCodeTask.cancel(true);
		if(verifyPhoneCodeTask!=null)
			verifyPhoneCodeTask.cancel(true);
	}
	/**
	 * 获取短信验证码
	 * @author kevin
	 *
	 */
	private class GetPhoneCodeTask extends AsyncTask<String , Void, Boolean>{
		String strError;
		JSONObject jsonData;
		Exception exception;
		private Result getPhoneCode;

		@Override
		protected Boolean doInBackground(String... prams) {
			// TODO Auto-generated method stub
			try {
				Map<String, Object> pramsMap = new HashMap<String, Object>();

				pramsMap.put("userNameType", _LoginNameType);
				pramsMap.put("userName", _Extra_Phone);
				pramsMap.put("verifyCodeType", Constant_data.verifyCodeType);

				getPhoneCode = ApiClient.registGetPhoneCode(mContext, pramsMap);
			} catch (Exception e) {
				e.printStackTrace();
				exception = e;
			}
			return getPhoneCode!=null;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			CustomProgressDialog.hideProgressDialog();
			if(exception != null ){
				Toast.makeText(getApplicationContext(), getResources().getString(string.http_error_anomalies), Toast.LENGTH_LONG).show();
				return;
			}
			if(result){

				if(!Constant_data.HTTP_SUCCESS.equals(getPhoneCode.coder)) {
					strError = getPhoneCode.msg;
					ToastUtil.showShort(mContext, strError);
				}else{
					ToastUtil.showShort(mContext, "验证码短信已发送至"+_Extra_Phone);
					_RIBTN_Next.setEnabled(true);
					startTimer();
				}
			}else{
				_RIBTN_Next.setEnabled(false);
				ToastUtil.showShort(mContext, getResources().getString(string.http_error_anomalies));
			}
			super.onPostExecute(result);
		}

		@Override
		protected void onCancelled() {
			phoneCodeTask = null;
			super.onCancelled();
		}

	}

	/**
	 * 验证短信验证码
	 * @author kevin
	 *
	 */
	private class VerifyPhoneCodeTask extends AsyncTask<String , Void, Boolean>{
		String strError;
		JSONObject jsonData;
		Exception exception;
		private Result verifyPhoneCode;

		@Override
		protected Boolean doInBackground(String... prams) {
			// TODO Auto-generated method stub
			try {
				Map<String, Object> pramsMap = new HashMap<String, Object>();

				pramsMap.put("userNameType", _LoginNameType);
				pramsMap.put("userName", _Extra_Phone);
				pramsMap.put("randomCode", CheckCode);

				verifyPhoneCode = ApiClient.registVerifyPhoneCode(mContext, pramsMap);

			} catch (Exception e) {
				e.printStackTrace();
				exception = e;
			}
			return verifyPhoneCode!=null;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			CustomProgressDialog.hideProgressDialog();
			if(exception != null ){
				Toast.makeText(getApplicationContext(), getResources().getString(string.http_error_anomalies), Toast.LENGTH_LONG).show();
				return;
			}
			if(result){

				if(!Constant_data.HTTP_SUCCESS.equals(verifyPhoneCode.coder)) {
					strError = verifyPhoneCode.msg;
					ToastUtil.showShort(mContext, strError);
				}else{
					Intent intent = new Intent(mContext, RegisteredCheckPhoneActivity.class);
					intent.putExtra("Phone", _Extra_Phone);
					intent.putExtra("LoginNameType", _LoginNameType);
					startActivity(intent);
				}
			}else{
				ToastUtil.showShort(mContext, getResources().getString(string.http_error_anomalies));
			}
			super.onPostExecute(result);
		}

		@Override
		protected void onCancelled() {
			verifyPhoneCodeTask = null;
			super.onCancelled();
		}

	}

}
