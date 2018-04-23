package com.wanda.pay.fragment;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import cn.passguard.PassGuardEdit;

import com.wanda.pay.R;
import com.wanda.pay.R.string;
import com.wanda.pay.activity.SetPayPasswordActivity;
import com.wanda.pay.activity.wanDa_Pay_Activity;
import com.wanda.pay.app.WDApplication;
import com.wanda.pay.bean.Constant_data;
import com.wanda.pay.bean.UserBean;
import com.wanda.pay.dialog.CustomProgressDialog;
import com.wanda.pay.util.ApiClient;
import com.wanda.pay.util.JSONParsing;
import com.wanda.pay.util.LogUtil;
import com.wanda.pay.util.Result;
import com.wanda.pay.util.SPUtils;
import com.wanda.pay.util.StringUtils;
import com.wanda.pay.util.ToastUtil;
import com.wanda.pay.util.Tools;

public class LoginFragment extends Fragment implements OnClickListener {

	private FLoginBtnClickListener mLoginBtnClickListener;

	public interface FLoginBtnClickListener {
		void onFLoginBackClick(boolean b);

		void onFLoginToPayClick(String... prams);
	}

	public void setmLoginBtnClickListener(
			FLoginBtnClickListener mLoginBtnClickListener) {
		this.mLoginBtnClickListener = mLoginBtnClickListener;
	}

	private View view;
	private wanDa_Pay_Activity activity;
	private EditText _LEDT_Phone;
	private PassGuardEdit _LEDT_Password;
	private TextView _LTV_Title;
	private TextView _LTV_Version;
	private Button _LEBTN_Back;
	private String _LoginName;
	private String _Password;
	private LoginTask loginTask;
	private YunLoginTask yunLoginTask;
	private String _LoginType;
	private String mLogin_key;
	private CheckLoginTask checkLoginTask;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		activity = (wanDa_Pay_Activity) getActivity();
		Bundle bundle = getArguments();
		mLogin_key = bundle.getString("login_key");

		// 解析布局
		view = inflater.inflate(R.layout.fragment_login, container, false);

		if (view != null) { // 如果view不为空
			initView();
		}
		return view;

	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		activity.setShowingUI(0);
		
		if (!StringUtils.isEmpty(mLogin_key)) {
			yunLoginTask = new YunLoginTask();
			yunLoginTask.execute(mLogin_key);
			CustomProgressDialog.showProgressDialog(activity, "用户登录中。。。");
		}else{
			String mLoginToken = (String) SPUtils.getString(activity, Constant_data.sp_key_loginToken, "");
			if (!StringUtils.isEmpty(mLoginToken) ) {
				checkLoginTask = new CheckLoginTask();
				checkLoginTask.execute(mLoginToken);
				CustomProgressDialog.showProgressDialog(activity, "用户验证登录中。。。");
			}
		}
	}

	public void initView() {
		_LEDT_Phone = (EditText) view
				.findViewById(R.id.activity_login_edt_phone);
		_LEDT_Password = (PassGuardEdit) view
				.findViewById(R.id.activity_login_edt_password);

		_LTV_Title = (TextView) view.findViewById(R.id.activity_title_tv_name);
		_LTV_Title.setText("登录");

		view.findViewById(R.id.activity_login_btn_login).setOnClickListener(
				this);
		_LEBTN_Back = (Button) view.findViewById(R.id.activity_title_btn_back);
		_LEBTN_Back.setOnClickListener(this);

		Tools.initPassGuard(_LEDT_Password,"^.{6,}$");

		_LEDT_Phone.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (s == null || s.length() == 0)
					return;
				StringBuilder sb = new StringBuilder();
				for (int i = 0; i < s.length(); i++) {
					if (i != 3 && i != 8 && s.charAt(i) == ' ') {
						continue;
					} else {
						sb.append(s.charAt(i));
						if ((sb.length() == 4 || sb.length() == 9)
								&& sb.charAt(sb.length() - 1) != ' ') {
							sb.insert(sb.length() - 1, ' ');
						}
					}
				}
				if (!sb.toString().equals(s.toString())) {
					int index = start + 1;
					if (sb.charAt(start) == ' ') {
						if (before == 0) {
							index++;
						} else {
							index--;
						}
					} else {
						if (before == 1) {
							index--;
						}
					}
					_LEDT_Phone.setText(sb.toString());
					_LEDT_Phone.setSelection(index);
				}
				_LEDT_Password.clear();
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub

			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.activity_login_btn_login:
			// 登录
			Login();
			break;
		default:
			break;
		}
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
		_LoginName = _LEDT_Phone.getText().toString().replaceAll(" ", "");
		_Password = _LEDT_Password.getOutput2(); // 获取用户输入数据的加密密文（AES），用Base64编码。
		_LoginType = Tools.LoginType(_LoginName);
		if (Constant_data.LoginType_Mobile.equals(_LoginType)) {
			if (StringUtils.isEmpty(_LoginName) || !Tools.isMobile(_LoginName)) {
				ToastUtil.showShort(activity, "请输入正确手机号！");
				return;
			}
		} else if (Constant_data.LoginType_Email.equals(_LoginType)) {
			if (StringUtils.isEmpty(_LoginName) || !Tools.isEmail(_LoginName)) {
				ToastUtil.showShort(activity, "请输入正确邮箱地址！");
				return;
			}
		} else {
			if (StringUtils.isEmpty(_LoginName)) {
				ToastUtil.showShort(activity, "请输入帐号！");
				return;
			}
		}
		if (StringUtils.isEmpty(_Password)) {
			ToastUtil.showShort(activity, "请输入登录密码！");
			return;
		}

		CustomProgressDialog.showProgressDialog(activity, "loading");
		loginTask = new LoginTask();
		loginTask.execute();
	}

	@Override
	public void onDestroy() {
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
		private UserBean mUserBean;
		private boolean isCheck;

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
						Tools.encryptionPassword(activity, _Password));

				loginResult = ApiClient.Login(activity, pramsMap);
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
				Toast.makeText(
						activity,
						activity.getResources().getString(
								string.http_error_anomalies), Toast.LENGTH_LONG)
						.show();
				return;
			}
			if (result) {
				if (Constant_data.HTTP_SUCCESS.equals(loginResult.coder)) {

					mUserBean = JSONParsing
							.GetUserInfo(loginResult.jsonBodyObject);
					if (mUserBean != null) {
						WDApplication.getInstance().setUserBean(mUserBean);
						SPUtils.put(activity, Constant_data.sp_key_loginToken, mUserBean.getmLoginToken());
						SPUtils.put(activity, Constant_data.sp_key_userNameType, _LoginType);
						SPUtils.put(activity, Constant_data.sp_key_loginName, _LoginName);
						if (mLoginBtnClickListener != null) {
							mLoginBtnClickListener.onFLoginToPayClick();
						}
					}
				} else {
					strError = loginResult.msg;
					ToastUtil.showShort(activity, strError);
				}
			} else {
				ToastUtil.showShort(activity, activity.getResources()
						.getString(string.http_error_anomalies));
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
	 * 
	 * @author Administrator
	 * 
	 */
	private class YunLoginTask extends AsyncTask<String, Void, Boolean> {
		String strError;
		Exception exception;
		private Result loginResult;

		@Override
		protected Boolean doInBackground(String... prams) {
			// TODO Auto-generated method stub
			try {
					Map<String, Object> pramsMap = new HashMap<String, Object>();
					pramsMap.put("session_token", prams[0]);
					loginResult = ApiClient.YunLogin(activity, pramsMap);
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
				Toast.makeText(activity, "网络异常，请重试!", Toast.LENGTH_LONG).show();
				return;
			}
			if (result) {
				if (Constant_data.HTTP_SUCCESS.equals(loginResult.coder)) {
					// loginResult.jsonBodyObject
					LogUtil.i("云账号验证登录:"+loginResult.jsonBodyObject);
					
					Map<String, String> YunUserInfo = JSONParsing.YunLogin(loginResult.jsonBodyObject);
					
//					用户ID	userId	10	String	Y	
//					用户号	userNo	15	String	N	
//					登录名	loginName	64	String	?	
//					账户类型	accountType	1	String	Y	0：手机  1.邮箱
//					用户状态	userStatus	1	String	Y	用户状态:0-预开户,1-正常状态，
//					9-销户状态
//					手机号码	mobileNo	20	String	N	
//					邮箱	email	64	String	N
					WDApplication.getInstance().setUserBean(null);
					SPUtils.clear(activity);
					String userStatus = YunUserInfo.get("userStatus");
					
					if("0".equals(userStatus)){
						
						UserBean mUserBean = new UserBean();
						mUserBean.setmUserID(YunUserInfo.get("userId"));
						mUserBean.setmUserNO(YunUserInfo.get("userNo"));
						mUserBean.setmEmail(YunUserInfo.get("email"));
						mUserBean.setmMoblePhoneNo(YunUserInfo.get("mobileNo"));
						
						WDApplication.getInstance().setUserBean(mUserBean);
						SPUtils.put(activity, Constant_data.sp_key_loginToken, mUserBean.getmLoginToken());
						SPUtils.put(activity, Constant_data.sp_key_userNameType, YunUserInfo.get("accountType"));
						SPUtils.put(activity, Constant_data.sp_key_loginName, YunUserInfo.get("loginName"));
						
						startActivityForResult(new Intent(activity, SetPayPasswordActivity.class), 100);
						
					}else if("1".equals(userStatus)){
						
						UserBean mUserBean = new UserBean();
						mUserBean.setmUserID(YunUserInfo.get("userId"));
						mUserBean.setmUserNO(YunUserInfo.get("userNo"));
						mUserBean.setmEmail(YunUserInfo.get("email"));
						mUserBean.setmMoblePhoneNo(YunUserInfo.get("mobileNo"));
						
						WDApplication.getInstance().setUserBean(mUserBean);
						SPUtils.put(activity, Constant_data.sp_key_loginToken, mUserBean.getmLoginToken());
						SPUtils.put(activity, Constant_data.sp_key_userNameType, YunUserInfo.get("accountType"));
						SPUtils.put(activity, Constant_data.sp_key_loginName, YunUserInfo.get("loginName"));
						
					}else{
						ToastUtil.showLong(activity, "未查询到账户信息！");
					}
					
					
					
//					Map<String, String> checkLoinTime = JSONParsing
//							.CheckLoinTime(loginResult.jsonBodyObject);
//					if ("NO".equals(checkLoinTime.get("isLoginTimeOut"))) {
//						UserBean mUserBean = JSONParsing
//								.GetUserInfo(loginResult.jsonBodyObject);
//						if (mUserBean != null) {
//							WDApplication.getInstance().setUserBean(mUserBean);
//							SPUtils.put(activity,
//									Constant_data.sp_key_loginToken,
//									mUserBean.getmLoginToken());
//							String mLoginName = mUserBean.getmNickLoginName();
//							SPUtils.put(activity,
//									Constant_data.sp_key_loginName, mLoginName);
//							String loginType = Tools.LoginType(mLoginName);
//							SPUtils.put(activity,
//									Constant_data.sp_key_userNameType,
//									loginType);
//							if (mLoginBtnClickListener != null) {
//								mLoginBtnClickListener.onFLoginToPayClick();
//							}
//						}
//					}
				}
			}
			super.onPostExecute(result);
		}
		@Override
		protected void onCancelled() {
			yunLoginTask = null;
			super.onCancelled();
		}
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode==100 && resultCode == Activity.RESULT_OK){
			//设置密码返回 
			if(data!=null){
				String status = data.getStringExtra("status");
				if("SUCCESS".equals(status)){
					
				}else{
					String msg = data.getStringExtra("msg");
					
				}
			}
			
		}
	}

	/**
	 * 用户验证登录
	 * 
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
					loginResult = ApiClient.CheckLogin(activity, pramsMap);
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
				Toast.makeText(activity, "网络异常，请重试!", Toast.LENGTH_LONG).show();
				return;
			}
			if (result) {
				if (Constant_data.HTTP_SUCCESS.equals(loginResult.coder)) {
					// loginResult.jsonBodyObject
					Map<String, String> checkLoinTime = JSONParsing
							.CheckLoinTime(loginResult.jsonBodyObject);
					if ("NO".equals(checkLoinTime.get("isLoginTimeOut"))) {
						UserBean mUserBean = JSONParsing
								.GetUserInfo(loginResult.jsonBodyObject);
						if (mUserBean != null) {
							WDApplication.getInstance().setUserBean(mUserBean);
							SPUtils.put(activity,
									Constant_data.sp_key_loginToken,
									mUserBean.getmLoginToken());
							String mLoginName = mUserBean.getmNickLoginName();
							SPUtils.put(activity,
									Constant_data.sp_key_loginName, mLoginName);
							String loginType = Tools.LoginType(mLoginName);
							SPUtils.put(activity,
									Constant_data.sp_key_userNameType,
									loginType);
							if (mLoginBtnClickListener != null) {
								mLoginBtnClickListener.onFLoginToPayClick();
							}
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
