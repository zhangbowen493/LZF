package com.wanda.pay.activity;

import java.util.HashMap;
import java.util.Map;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.passguard.PassGuardEdit;

import com.umeng.update.UmengUpdateAgent;
import com.wanda.pay.R;
import com.wanda.pay.R.string;
import com.wanda.pay.app.WDApplication;
import com.wanda.pay.bean.Constant_data;
import com.wanda.pay.bean.UserBean;
import com.wanda.pay.dialog.CustomProgressDialog;
import com.wanda.pay.util.ApiClient;
import com.wanda.pay.util.AppUtils;
import com.wanda.pay.util.ImmersedStatusbarUtils;
import com.wanda.pay.util.JSONParsing;
import com.wanda.pay.util.LogUtil;
import com.wanda.pay.util.Result;
import com.wanda.pay.util.SPUtils;
import com.wanda.pay.util.ScreenManager;
import com.wanda.pay.util.StringUtils;
import com.wanda.pay.util.ToastUtil;
import com.wanda.pay.util.Tools;

/**
 * 登录页面
 * 
 * @author kevin
 * 
 */
public class LoginActivity extends BaseActivity implements IMVCActivity,
OnClickListener {

	private Context mContext = this;

	private EditText _LEDT_Phone;
	private PassGuardEdit _LEDT_Password;
	private Button _LEBTN_Back;
	private TextView _LTV_Title;
	private LoginTask loginTask;
	private String _LoginName;
	private String _Password;
	public UserBean mUserBean;
	private WDApplication mApplication;
	private Boolean isCheck;
	private TextView _LTV_Version;
	private CheckLoginTask checkLoginTask;
	private boolean isExit;
	private String _LoginType;
	private TextView _LTV_Other_Login;
	private RelativeLayout _LRL_Wanda_Login;
	private RelativeLayout _LRL_Other_Login;

	// private String _Action_str;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		ImmersedStatusbarUtils.initBeforeSetContentView(this,null);
		setContentView(R.layout.activity_login);
		WDApplication.getInstance().addActivity(this);
		init();
		initView();
		refresh();
		/** 启动友盟自动升级检测 */
		UmengUpdateAgent.update(this);
	}
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		String phone = getIntent().getStringExtra("success_phone");

		if(Tools.isMobile(phone)){
			_LEDT_Phone.setText(phone);
		}
	}
@Override
protected void onRestart() {
	super.onRestart();
	
}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		Intent intent = getIntent();
		// _Action_str = intent.getAction();

		isCheck = intent.getBooleanExtra("isCheck",false);
		isExit = intent.getBooleanExtra("isExit",false);
		//		if(!isCheck){
		//			String mLoginToken = (String) SPUtils.getString(mContext, Constant_data.sp_key_loginToken, "");
		//			if (!StringUtils.isEmpty(mLoginToken) && !isExit) {
		//				checkLoginTask = new CheckLoginTask();
		//				checkLoginTask.execute(mLoginToken);
		//				CustomProgressDialog.showProgressDialog(mContext, "用户登录中。。。");
		//			}
		//		}
	}

	@Override
	public void initView() {

		_LRL_Wanda_Login = (RelativeLayout) findViewById(R.id.act_rl_login_wanda_login);

		//		_LRL_Other_Login = (RelativeLayout) findViewById(R.id.act_rl_login_other_login);

		_LEDT_Phone = (EditText) findViewById(R.id.activity_login_edt_phone);
		_LEDT_Password = (PassGuardEdit) findViewById(R.id.activity_login_edt_password);

		_LTV_Title = (TextView) findViewById(R.id.activity_title_tv_name);
		_LTV_Title.setText("登录");
		//		_LTV_Version = (TextView) findViewById(R.id.act_tv_version);

		_LTV_Other_Login = (TextView) findViewById(R.id.act_tv_other_loginstyle);
		//		_LTV_Other_Login.setOnClickListener(this);
		findViewById(R.id.activity_login_btn_login).setOnClickListener(this);
		findViewById(R.id.activity_login_btn_registered).setOnClickListener(
				this);
		findViewById(R.id.activity_login_btn_find_password).setOnClickListener(
				this);
		_LEBTN_Back = (Button) findViewById(R.id.activity_title_btn_back);
		_LEBTN_Back.setOnClickListener(this);

		Tools.initPassGuard(_LEDT_Password,"^.{6,}$");

		String versionName = AppUtils.getVersionName(mContext);
		//		_LTV_Version.setText("当前版本号："+versionName+URLs.url_des);

		_LEDT_Phone.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
				// TODO Auto-generated method stub
				_LEDT_Password.clear();
			}
			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {}
			@Override
			public void afterTextChanged(Editable arg0) {}
		});

		sla0.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation arg0) {}
			@Override
			public void onAnimationRepeat(Animation arg0) {}
			@Override
			public void onAnimationEnd(Animation arg0) {
				// TODO Auto-generated method stub
				if(_LRL_Wanda_Login.getVisibility() == View.VISIBLE){
					ShowOtherLogin();
					_LRL_Wanda_Login.setAnimation(null);
					_LRL_Other_Login.startAnimation(sla1);
				}else{
					ShowWandaLogin();
					_LRL_Other_Login.setAnimation(null);
					_LRL_Wanda_Login.startAnimation(sla1);
				}
			}
		});
		sla0.setDuration(500);
		sla1.setDuration(500);
	}

	@Override
	public void refresh(Object... param) {

	}

	@Override
	public void MyBack() {
//		mApplication = WDApplication.getInstance();
//		mApplication.mScreenManager = ScreenManager.getScreenManager();
//		mApplication.mScreenManager.popAlls();
		WDApplication.getInstance().exit();
	}

	private void showMyDialog() {
		final Dialog mDialog = new Dialog(mContext, R.style.myDilog);
		mDialog.setContentView(R.layout.dialog_layout);
		Button sure = (Button) mDialog.findViewById(R.id.dialog_btn_ok);
		Button back = (Button) mDialog.findViewById(R.id.dialog_btn_cancel);
		TextView _DialogTV = (TextView) mDialog
				.findViewById(R.id.dialog_tv_msg);
		_DialogTV.setText(getResources().getString(string.app_exit_prompt));
		mDialog.setCanceledOnTouchOutside(false);

		sure.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				MyBack();
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

	private ScaleAnimation sla0 = 
			new ScaleAnimation(1, 0, 1, 1,
					Animation.RELATIVE_TO_PARENT, 0.5f, Animation.RELATIVE_TO_PARENT,
					0.5f);
	private ScaleAnimation sla1 = 
			new ScaleAnimation(0, 1, 1, 1,
					Animation.RELATIVE_TO_PARENT, 0.5f, Animation.RELATIVE_TO_PARENT,
					0.5f);
	private void ShowWandaLogin(){
		_LTV_Other_Login.setText("健康云帐号登录");
		_LRL_Wanda_Login.setVisibility(View.VISIBLE);
		_LRL_Other_Login.setVisibility(View.INVISIBLE);
	}
	private void ShowOtherLogin(){
		_LTV_Other_Login.setText("易支付帐号登录");
		_LRL_Wanda_Login.setVisibility(View.INVISIBLE);
		_LRL_Other_Login.setVisibility(View.VISIBLE);
	}



	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		//登录方式切换去除 2016.4.13
		case R.id.act_tv_other_loginstyle:

			if(_LRL_Wanda_Login.getVisibility() == View.VISIBLE){
				_LRL_Wanda_Login.startAnimation(sla0);
			}else{
				_LRL_Other_Login.startAnimation(sla0);
			}

			break;
		case R.id.activity_login_btn_find_password:
			// 找回密码

			//			ToastUtil.showShort(mContext, "敬请期待！");
			_LoginName = _LEDT_Phone.getText().toString();
			if (StringUtils.isEmpty(_LoginName) ) {
				ToastUtil.showShort(mContext, "请输入帐号！");
				return;
			}
			Intent intent = new Intent(mContext,
					FindPasswordActivity.class);
			intent.putExtra("accounts", _LoginName);
			intent.putExtra("GoType", 1);
			startActivity(intent);
			break;
		case R.id.activity_login_btn_login:
			// 登录		
//			Intent intent3=new Intent(this, AddCardTwoActivity.class);
//			startActivity(intent3);
			
			Login();
			break;
		case R.id.activity_login_btn_registered:
			// 去注册

			String _Phone_toR = _LEDT_Phone.getText().toString();
			Intent toRegist = new Intent(this,
					RegisteredInputPhoneActivity.class);
			if (!StringUtils.isEmpty(_Phone_toR)) {
				toRegist.putExtra("Phone", _Phone_toR);
			}

			startActivity(toRegist);
			break;
		case R.id.activity_title_btn_back:
			// 返回
			// showExitPrompt();
			//			showMyDialog();
			MyBack();
			break;
		default:
			break;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		// 返回键
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			// showExitPrompt();
			//			showMyDialog();
			MyBack();
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * 登录
	 * 
	 * @param password
	 *            密码
	 * @param phone
	 *            帐号
	 */
	private void Login() {
		_LoginName = _LEDT_Phone.getText().toString();
		_Password = _LEDT_Password.getOutput2(); // 获取用户输入数据的加密密文（AES），用Base64编码。
		_LoginType = Tools.LoginType(_LoginName);
		if(Constant_data.LoginType_Mobile.equals(_LoginType)){
			if (StringUtils.isEmpty(_LoginName) || !Tools.isMobile(_LoginName)) {
				ToastUtil.showShort(mContext, "请输入正确手机号！");
				return;
			}
		}else if(Constant_data.LoginType_Email.equals(_LoginType)){
			if (StringUtils.isEmpty(_LoginName) || !Tools.isEmail(_LoginName)) {
				ToastUtil.showShort(mContext, "请输入正确邮箱地址！");
				return;
			}
		}else{
			if (StringUtils.isEmpty(_LoginName) ) {
				ToastUtil.showShort(mContext, "请输入帐号！");
				return;
			}
		}
		if (StringUtils.isEmpty(_Password)) {
			ToastUtil.showShort(mContext, "请输入登录密码！");
			return;
		}

		CustomProgressDialog.showProgressDialog(mContext, "loading");
		loginTask = new LoginTask();
		loginTask.execute();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (loginTask != null)
			loginTask.cancel(true);
	}

	/**
	 * 登录
	 * 
	 * @author kevin
	 */
	private class LoginTask extends AsyncTask<String, Void, Boolean> {
		String strError;
		Exception exception;
		private Result loginResult;
		@Override
		protected Boolean doInBackground(String... prams) {
			try {
				Map<String, Object> pramsMap = new HashMap<String, Object>();

				pramsMap.put("operationType", "1");
				pramsMap.put("loginType", _LoginType);
				pramsMap.put("loginName", _LoginName);
				pramsMap.put("isLogin", "1");
				pramsMap.put("loginPwdType", "1");
				pramsMap.put("loginPwd",
						Tools.encryptionPassword(mContext, _Password));

				loginResult = ApiClient.Login(mContext, pramsMap);
			} catch (Exception e) {
				e.printStackTrace();
				exception = e;
			}
			return loginResult != null;
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
				if (Constant_data.HTTP_SUCCESS.equals(loginResult.coder)) {
					mUserBean = JSONParsing
							.GetUserInfo(loginResult.jsonBodyObject);
					if (mUserBean != null) {
						WDApplication.getInstance().setUserBean(mUserBean);
						SPUtils.put(mContext, Constant_data.sp_key_loginToken, mUserBean.getmLoginToken());
						SPUtils.put(mContext, Constant_data.sp_key_userNameType, _LoginType);
						SPUtils.put(mContext, Constant_data.sp_key_loginName, _LoginName);
						Intent intent;
						if(!isCheck){
							intent = new Intent(mContext, MainViewPagerActivity.class);
							intent.putExtra("loginName", _LoginName);
							startActivity(intent);
							overridePendingTransition(R.anim.zoomin, R.anim.zoomout); 
						}
						LoginActivity.this.finish();
					}
				} else {
					strError = loginResult.msg;
					ToastUtil.showShort(mContext, strError);
				}
			} else {
				ToastUtil.showShort(mContext,
						getResources().getString(string.http_error_anomalies));
			}
			super.onPostExecute(result);
		}

		@Override
		protected void onCancelled() {
			loginTask = null;
			super.onCancelled();
		}
	}
	/**
	 * 用户验证登录
	 * @author Administrator
	 *
	 */
	private class CheckLoginTask extends AsyncTask<String, Void, Boolean> {
		String strError;
		Exception exception;
		private Result loginResult;

		@Override
		protected Boolean doInBackground(String... prams) {
			// TODO Auto-generated method stub
			try {
				Map<String, Object> pramsMap = new HashMap<String, Object>();
				pramsMap.put("loginToken", prams[0]);
				loginResult = ApiClient.CheckLogin(mContext,
						pramsMap);
			} catch (Exception e) {
				e.printStackTrace();
				exception = e;
			}
			return loginResult != null;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			CustomProgressDialog.hideProgressDialog();
			if (exception != null) {
				Toast.makeText(getApplicationContext(), "网络异常，请重试!",
						Toast.LENGTH_LONG).show();
				return;
			}
			if (result) {
				if (Constant_data.HTTP_SUCCESS.equals(loginResult.coder)) {
					//					loginResult.jsonBodyObject
					Map<String, String> checkLoinTime = JSONParsing.CheckLoinTime(loginResult.jsonBodyObject);

					if("NO".equals(checkLoinTime.get("isLoginTimeOut"))){
						mUserBean = JSONParsing
								.GetUserInfo(loginResult.jsonBodyObject);
						if (mUserBean != null) {
							WDApplication.getInstance().setUserBean(mUserBean);
							SPUtils.put(mContext, Constant_data.sp_key_loginToken, mUserBean.getmLoginToken());
							String mLoginName = mUserBean.getmNickLoginName();
							SPUtils.put(mContext, Constant_data.sp_key_loginName, mLoginName);
							String loginType = Tools.LoginType(mLoginName);
							SPUtils.put(mContext, Constant_data.sp_key_userNameType, loginType);
							Intent intent = new Intent(mContext, MyQuestionListViewActivity.class);
							startActivity(intent);
							LoginActivity.this.finish();
						}

					}
				} 
			} 
			super.onPostExecute(result);
		}

		@Override
		protected void onCancelled() {
			checkLoginTask = null;
			super.onCancelled();
		}

	}

}
