package com.wanda.pay.activity;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.passguard.PassGuardEdit;

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
import com.wanda.pay.util.StringUtils;
import com.wanda.pay.util.ToastUtil;
import com.wanda.pay.util.Tools;

/**
 * 设置支付密码 
 * 
 * @author kevin
 * 
 *         intent 携带访问类型 AccessType 1-设置密码 2-修改支付密码 3- 重置支付密码 4 - 重置登录密码 5-修改登录密码
 */
public class SetPasswordActivity1 extends BaseActivity implements
IMVCActivity, OnClickListener {

	private Context mContext = this;

	private int _AccessType;
	private TextView _ACTWTV_Title;
	private PassGuardEdit _SPPEDT_OldPassword;
	private PassGuardEdit _SPPEDT_AgainPassword;
	private PassGuardEdit _SPPEDT_Password;

	private String _Old_Password;

	private String _Password;

	private String _Again_Password;

	private TextView _SPPTV_Find;
	/** 修改密码类型 1:登录密码 2:支付密码*/
	private String _operationType ="1";

	private PasswordModifyTask modifyTask;

	private LoginOutTask loginOutTask;

	private String _UserID;
	private String _LoginName;
	private TextView _LEDT_Phone;
	private RetrievePwdSaveReqTask pwdSaveReqTask;
	private RelativeLayout act_photo_top;
	private String _AcctountType;
	private RelativeLayout activity_photo_set_top1;
	private RelativeLayout _SPPRL_CheckCode;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_set_pay_password);
		WDApplication.getInstance().addActivity(this);
		init();
		initView();
		initData();
		refresh();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if(modifyTask!=null)
			modifyTask.cancel(true);
		if(loginOutTask!=null)
			loginOutTask.cancel(true);
		if(pwdSaveReqTask!=null)
			pwdSaveReqTask.cancel(true);
	}
	@Override
	public void init() {
		_AccessType = getIntent().getIntExtra("AccessType", 1);
		_UserID = getIntent().getStringExtra("userId");
		_AcctountType = getIntent().getStringExtra("accountType");

	}

	private void initData() {
		// TODO Auto-generated method stub
		_LoginName= getIntent().getStringExtra("loginName");
		_LEDT_Phone.setText(_LoginName);
	}
	@Override
	public void initView() {
		// TODO Auto-generated method stub

		_SPPRL_CheckCode = (RelativeLayout) findViewById(R.id.activity_rl_set_top);

		_SPPEDT_OldPassword = (PassGuardEdit) findViewById(R.id.act_edt_set_pay_password_old);
		_LEDT_Phone=(TextView) findViewById(R.id.activity_tet_photo_pay);
		_SPPEDT_Password = (PassGuardEdit) findViewById(R.id.act_edt_set_pay_password);
		_SPPEDT_AgainPassword = (PassGuardEdit) findViewById(R.id.act_edt_set_pay_password_again);
		act_photo_top=(RelativeLayout) findViewById(R.id.activity_photo_set_top);
		activity_photo_set_top1=(RelativeLayout) findViewById(R.id.activity_photo_set_top1);
		_ACTWTV_Title = (TextView) findViewById(R.id.activity_title_tv_name);

		_SPPTV_Find = (TextView) findViewById(R.id.act_tv_set_find_password);
		_SPPTV_Find.setOnClickListener(this);
		findViewById(R.id.activity_title_btn_back).setOnClickListener(this);
		findViewById(R.id.act_btn_set_pay_password_setting).setOnClickListener(
				this);

		Tools.initPassGuard(_SPPEDT_OldPassword,null);
		Tools.initPassGuard(_SPPEDT_Password,Constant_data.passwordMatchStr);
		Tools.initPassGuard(_SPPEDT_AgainPassword,Constant_data.passwordMatchStr);
	}


	@Override
	public void refresh(Object... param) {
		// TODO Auto-generated method stub
		if (_AccessType == 1) {
			_ACTWTV_Title.setText("设置支付密码");
			_SPPEDT_OldPassword.setVisibility(View.GONE);
		} else if(_AccessType == 2){
			_ACTWTV_Title.setText("修改支付密码");
			_SPPEDT_OldPassword.setVisibility(View.VISIBLE);
			_SPPRL_CheckCode.setVisibility(View.VISIBLE);
			_SPPEDT_OldPassword.setHint("请填写原支付密码");
			act_photo_top.setVisibility(View.GONE);
			_SPPTV_Find.setVisibility(View.VISIBLE);
			activity_photo_set_top1.setVisibility(View.VISIBLE);
			_operationType="2";
		}else if(_AccessType == 3){
			_ACTWTV_Title.setText("重置支付密码");
			_SPPRL_CheckCode.setVisibility(View.VISIBLE);
			act_photo_top.setVisibility(View.GONE);
			activity_photo_set_top1.setVisibility(View.GONE);
			_SPPEDT_OldPassword.setVisibility(View.VISIBLE);
			_SPPEDT_OldPassword.setHint("请填写短信验证码");
		}else if(_AccessType == 4){
			_ACTWTV_Title.setText("重置登录密码");
			_SPPRL_CheckCode.setVisibility(View.VISIBLE);
			_SPPEDT_OldPassword.setVisibility(View.VISIBLE);
			activity_photo_set_top1.setVisibility(View.GONE);
			act_photo_top.setVisibility(View.GONE);
			_SPPEDT_OldPassword.setHint("请填写短信验证码");
		}else if(_AccessType ==5){
			_ACTWTV_Title.setText("修改登录密码");
			_SPPEDT_OldPassword.setVisibility(View.VISIBLE);
			act_photo_top.setVisibility(View.GONE);
			activity_photo_set_top1.setVisibility(View.VISIBLE);
			_SPPEDT_OldPassword.setHint("请填写原登录密码");
			_SPPTV_Find.setVisibility(View.GONE);
			_operationType = "1";
		}
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
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			MyBack();
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.act_tv_set_find_password:
			// 找回密码
			Intent intent = new Intent(mContext, FindPasswordActivity.class);
			intent.putExtra("GoType", 2);
			String _LoginName = SPUtils.getString(mContext, Constant_data.sp_key_loginName, "");
			intent.putExtra("accounts", _LoginName);

			startActivity(intent);
			SetPasswordActivity1.this.finish();

			break;
		case R.id.activity_title_btn_back:
			// 返回
			MyBack();
			break;
		case R.id.act_btn_set_pay_password_setting:
			_Old_Password = "";
			_Password = _SPPEDT_Password.getOutput2();
			_Again_Password = _SPPEDT_AgainPassword.getOutput2();
			if (_AccessType == 2 ) {
				_Old_Password = _SPPEDT_OldPassword.getOutput2();
				if (StringUtils.isEmpty(_Old_Password)) {
					ToastUtil.showShort(mContext, "请输入原始支付密码！");
					return;
				}
			}if(_AccessType == 5){
				_Old_Password = _SPPEDT_OldPassword.getOutput2();
				if (StringUtils.isEmpty(_Old_Password)) {
					ToastUtil.showShort(mContext, "请输入原始登录密码！");
					return;
				}
			}else if(_AccessType == 3 || _AccessType ==4){
				_SPPEDT_OldPassword.setVisibility(View.GONE);

			}
			if (StringUtils.isEmpty(_Password)||!_SPPEDT_Password.checkMatch()) {
				ToastUtil.showShort(mContext, "请输入六位或以上密码，并且同时包含数字和大小写字母！");
				return;
			}
			if (StringUtils.isEmpty(_Again_Password)) {
				ToastUtil.showShort(mContext, "请再次输入密码！");
				return;
			}
			if (!_Again_Password.equals(_Password)) {
				ToastUtil.showShort(mContext, "两次密码输入不一致！");
				return;
			}
			if ((_AccessType == 2 ||_AccessType == 5) && _Old_Password.equals(_Password)) {
				ToastUtil.showShort(mContext, "新密码与原密码一致 无需修改！");
				return;
			}

			setPayPassword(_AccessType, _Old_Password, _Password);

			break;

		default:
			break;
		}
	}

	/**
	 * 设置支付密码
	 * 
	 * @param accessType
	 *            访问类型 1-设置 2 修改
	 * @param old_Password
	 *            原密码
	 * @param password
	 *            新密码
	 */
	private void setPayPassword(int accessType, String old_Password,
			String password) {
		// TODO Auto-generated method stub
		CustomProgressDialog.showProgressDialog(mContext, "loading");
		if (accessType == 1) {
			//msg.obj = "支付密码设置成功";
		} else if(accessType == 2){
			//msg.obj = "支付密码修改成功";
			PasswordModify();
		}else if(accessType == 3){
			//msg.obj = "支付密码重置成功";
			RetrievePwdSaveReq("2",password);
		}else if(accessType == 4){
			//msg.obj = "登录密码重置成功";
			RetrievePwdSaveReq("1",password);
		}else if(accessType == 5){
			//msg.obj = "登录密码修改成功";
			PasswordModify();
		}
	}
	/**
	 * 重置密码
	 * @param pwdType	密码类型
	 * @param passWord	密码
	 */
	private void RetrievePwdSaveReq(String pwdType,String passWord) {
		CustomProgressDialog.showProgressDialog(mContext, "loading");
		pwdSaveReqTask = new RetrievePwdSaveReqTask();
		pwdSaveReqTask.execute(_UserID,pwdType,passWord,_AcctountType);
	}

	/**
	 * 修改密码
	 */
	private void PasswordModify() {
		// TODO Auto-generated method stub
		CustomProgressDialog.showProgressDialog(mContext, "loading");
		modifyTask = new PasswordModifyTask();
		modifyTask.execute();
	}

	/**
	 * 登出
	 */
	private void Logout() {
		// TODO Auto-generated method stub  _Again_Password
		CustomProgressDialog.showProgressDialog(mContext, "loading");
		loginOutTask = new LoginOutTask();
		loginOutTask.execute();
	}

	/**
	 * 修改密码
	 * @author kevin
	 *
	 */
	private class PasswordModifyTask extends AsyncTask<String, Integer, Boolean>{
		String strError;
		Exception exception;
		private Result modifyResult;

		@Override
		protected Boolean doInBackground(String... prams) {
			// TODO Auto-generated method stub
			try {
				UserBean userBean = WDApplication.getInstance().getUserBean();
				if(userBean!=null){
					Map<String, Object> pramsMap = new HashMap<String, Object>();
					String _LoginName = SPUtils.getString(mContext, Constant_data.sp_key_loginName, "");

					pramsMap.put("operationType", _operationType); // 密码类型	operationType
					pramsMap.put("loginName", _LoginName);	//登录名	loginName
					pramsMap.put("customerNo", userBean.getmUserNO());//客户号	
					pramsMap.put("oldPassword", Tools.encryptionPassword(mContext, _Old_Password));//旧密码	
					pramsMap.put("newPassword", Tools.encryptionPassword(mContext, _Password));//新密码

					modifyResult = ApiClient.PasswordModify(mContext, pramsMap);
				}
			} catch (Exception e) {
				e.printStackTrace();
				exception = e;
			}
			return modifyResult != null;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			CustomProgressDialog.hideProgressDialog();
			if(exception != null ){
				Toast.makeText(getApplicationContext(), getResources().getString(string.http_error_anomalies), Toast.LENGTH_LONG).show();
				return;
			}
			if(result){
				if(Constant_data.HTTP_SUCCESS.equals(modifyResult.coder)){
					if(_AccessType ==5){
						Logout();
					}else{
						ToastUtil.showLong(mContext, "支付密码修改成功！");
						SetPasswordActivity1.this.finish();
					}
				}else{
					strError = modifyResult.msg;
					ToastUtil.showShort(mContext, strError);
				}
			}else{
				ToastUtil.showShort(mContext, getResources().getString(string.http_error_anomalies));
			}
			super.onPostExecute(result);
		}

		@Override
		protected void onCancelled() {
			modifyTask = null;
			super.onCancelled();
		}
	}
	/**
	 * 用户登出
	 * @author kevin
	 *
	 */
	private class LoginOutTask extends AsyncTask<String , Void, Boolean>{
		String strError;
		JSONObject jsonData;
		Exception exception;
		private Result loginResult;

		@Override
		protected Boolean doInBackground(String... prams) {
			// TODO Auto-generated method stub
			try {
				String _LoginName = SPUtils.getString(mContext,
						Constant_data.sp_key_loginName, "");
				Map<String, Object> pramsMap = new HashMap<String, Object>();

				pramsMap.put("operationType", "1");
				pramsMap.put("loginType", "1");
				pramsMap.put("isLogin", "0");
				pramsMap.put("loginName",_LoginName);
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
			if(exception != null ){
				Toast.makeText(getApplicationContext(), getResources().getString(string.http_error_anomalies), Toast.LENGTH_LONG).show();
				return;
			}
			if(result){
				if(Constant_data.HTTP_SUCCESS.equals(loginResult.coder)){
					SPUtils.put(mContext, Constant_data.IS_LOGIN, false);
					SPUtils.clear(mContext);
					WDApplication.getInstance().setUserBean(null);
					ToastUtil.showShort(mContext, "登录密码已修改，请重新登录！");
					Intent intent = new Intent(mContext, LoginActivity.class);
					intent.putExtra("isExit", true);
					startActivity(intent);
					SetPasswordActivity1.this.finish();
					if(MainViewPagerActivity.instance!=null)
						MainViewPagerActivity.instance.finish();
					if(UserCenterActivity.instance!=null)
						UserCenterActivity.instance.finish();
				}else{
					strError = loginResult.msg;
					ToastUtil.showShort(mContext, strError);
				}
			}else{
				ToastUtil.showShort(mContext, getResources().getString(string.http_error_anomalies));
			}
			super.onPostExecute(result);
		}

		@Override
		protected void onCancelled() {
			loginOutTask = null;
			super.onCancelled();
		}

	}

	/**
	 * 修改密码
	 * @author kevin
	 *prams[0] userID [1] 密码类型 [2] 新密码 [3]账户类型
	 */
	private class RetrievePwdSaveReqTask extends AsyncTask<String, Integer, Boolean>{
		String strError;
		Exception exception;
		private Result PwdSaveResult;
		private String pwdType;
		@Override
		protected Boolean doInBackground(String... prams) {
			pwdType = prams[1];
			try {
				Map<String, Object> pramsMap = new HashMap<String, Object>();
				pramsMap.put("operId", prams[0]); // 用户ID
				pramsMap.put("pwdType", prams[1]);	//密码类型:1-登录密码,2-支付密码
				pramsMap.put("newPassword", prams[2]);//新密码
				pramsMap.put("accountType", prams[3]);//账户类型
				PwdSaveResult = ApiClient.RetrievePwdSaveReq(mContext, pramsMap);

			} catch (Exception e) {
				e.printStackTrace();
				exception = e;
			}
			return PwdSaveResult != null;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			CustomProgressDialog.hideProgressDialog();
			if(exception != null ){
				Toast.makeText(getApplicationContext(), getResources().getString(string.http_error_anomalies), Toast.LENGTH_LONG).show();
				return;
			}
			if(result){

				if(Constant_data.HTTP_SUCCESS.equals(PwdSaveResult.coder)){
					Map<String, String> reqMap = JSONParsing
							.PwdSave(PwdSaveResult.jsonBodyObject);
					if("YES".equals(reqMap.get("pwdResetResult"))){
						if("1".equals(pwdType)){
							//登录
							ToastUtil.showLong(mContext, "登录密码重置成功！");
							SetPasswordActivity1.this.finish();
						}else if("2".equals(pwdType)){
							//支付
							ToastUtil.showLong(mContext, "支付密码重置成功！");
							SetPasswordActivity1.this.finish();
						}
					}else{
						if("1".equals(pwdType)){
							//登录
							ToastUtil.showLong(mContext, "登录密码重置失败！");
							SetPasswordActivity1.this.finish();
						}else if("2".equals(pwdType)){
							//支付
							ToastUtil.showLong(mContext, "支付密码重置失败！");
							SetPasswordActivity1.this.finish();
						}
					}
				}else{
					strError = PwdSaveResult.msg;
					ToastUtil.showShort(mContext, strError);
				}
			}else{
				ToastUtil.showShort(mContext, getResources().getString(string.http_error_anomalies));
			}
			super.onPostExecute(result);
		}

		@Override
		protected void onCancelled() {
			pwdSaveReqTask = null;
			super.onCancelled();
		}
	}

}
