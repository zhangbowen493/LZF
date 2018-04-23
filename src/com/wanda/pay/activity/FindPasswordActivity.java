package com.wanda.pay.activity;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.wanda.pay.R;
import com.wanda.pay.app.WDApplication;
import com.wanda.pay.bean.Constant_data;
import com.wanda.pay.bean.HandlerConstant;
import com.wanda.pay.bean.UserBean;
import com.wanda.pay.dialog.CustomProgressDialog;
import com.wanda.pay.util.ApiClient;
import com.wanda.pay.util.ImmersedStatusbarUtils;
import com.wanda.pay.util.JSONParsing;
import com.wanda.pay.util.Result;
import com.wanda.pay.util.SPUtils;
import com.wanda.pay.util.StringUtils;
import com.wanda.pay.util.ToastUtil;
import com.wanda.pay.util.Tools;

/**
 * 密码找回
 * 
 * @author kevin intent 携带访问类型 GoType 1- 登录密码找回 2 - 支付密码找回
 */
public class FindPasswordActivity extends BaseActivity implements IMVCActivity,
OnClickListener {


	private Context mContext = FindPasswordActivity.this;
	/** 访问目的 1-登录密码 2-找回支付密码 */
	private int _GoType;
	// 密保问题一
	private TextView _FPTV_Question_1;
	// 密保问题二
	private TextView _FPTV_Question_2;
	// 密保问题一答案
	private EditText _FPPEDT_Solution_1;
	// 密保问题二答案
	private EditText _FPPEDT_Solution_2;
	/** 帐号 */
	private String _Accounts;
	/** 帐号类型 */
	private String _AccountsType;

	private GetQuestionsTask questionsTask;
	private String _QwdQuestionFst;
	private String _QwdQuestionSec;
	private String _Solution_1;
	private String _Solution_2;
	private String _UserId;
	String _AccountType = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_find_pay_password);
		ImmersedStatusbarUtils.initAfterSetContentView(this, null);
		WDApplication.getInstance().addActivity(this);
		init();
		initView();
		refresh();
	}

	@Override
	public void init() {
		_GoType = getIntent().getIntExtra("GoType", 1);
		_Accounts = getIntent().getStringExtra("accounts");

		if(StringUtils.isEmpty(_Accounts)){
			_Accounts = SPUtils.getString(mContext, Constant_data.sp_key_loginName, "");
		}
		_AccountsType = Tools.LoginType(_Accounts);

		CustomProgressDialog.showProgressDialog(mContext, "loading");
		questionsTask = new GetQuestionsTask();
		questionsTask.execute();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (questionsTask!=null && !questionsTask.isCancelled()) {
			questionsTask.cancel(true);
		}
		if (checkReqTask!=null&&!checkReqTask.isCancelled()) {
			checkReqTask.cancel(true);
		}
	}

	@Override
	public void initView() {
		_FPTV_Question_1 =  (TextView) findViewById(R.id.act_tv_fp_question_1);
		_FPPEDT_Solution_1 = (EditText) findViewById(R.id.act_edt_find_pay_solution_1);
		_FPTV_Question_2 = (TextView) findViewById(R.id.act_tv_fp_question_2);
		_FPPEDT_Solution_2 = (EditText) findViewById(R.id.act_edt_find_pay_solution_2);

		TextView _FPPTV_Title = (TextView) findViewById(R.id.activity_title_tv_name);
		_FPPTV_Title.setText("填写密保问题");

		findViewById(R.id.activity_title_btn_back).setOnClickListener(this);
		findViewById(R.id.act_btn_find_pay_get).setOnClickListener(this);
	}

	@Override
	public void refresh(Object... param) {
		// TODO Auto-generated method stub

	}

	@Override
	public void MyBack() {
		// TODO Auto-generated method stub
		FindPasswordActivity.this.finish();
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
		case R.id.activity_title_btn_back:
			// 返回
			MyBack();
			break;
		case R.id.act_btn_find_pay_get:
			_Solution_1 = _FPPEDT_Solution_1.getText().toString().trim();
			_Solution_2 = _FPPEDT_Solution_2.getText().toString().trim();
			if (StringUtils.isEmpty(_Solution_1)
					|| StringUtils.isEmpty(_Solution_2)) {
				ToastUtil.showShort(mContext, "请填写密保问题！");
				return;
			}
			CustomProgressDialog.showProgressDialog(mContext, "loading");
			checkReqTask = new PwdQuestionCheckReqTask();
			checkReqTask.execute();

			break;

		default:
			break;
		}
	}


	/**
	 * 获取密保问题
	 * 
	 * @author kevin
	 * 
	 */
	private class GetQuestionsTask extends AsyncTask<String, Void, Boolean> {
		String strError;
		JSONObject jsonData;
		Exception exception;
		private Result QuestionsResult;


		@Override
		protected Boolean doInBackground(String... prams) {
			// TODO Auto-generated method stub
			try {
				Map<String, Object> pramsMap = new HashMap<String, Object>();
				if ("1".equals(_AccountsType)) {
					_AccountType = "0";
				} else if ("2".equals(_AccountsType)) {
					_AccountType = "1";
				} else {
					_AccountType = "2";
				}
				pramsMap.put("sysType", "1"); // 系统类型
				pramsMap.put("accountType", _AccountType); // 0：手机。1.邮箱，2：别名用户
				pramsMap.put("accountNo", _Accounts); // 用户注册账号
				pramsMap.put("pwdType", _GoType + ""); // 密码类型:1-登录密码,2-支付密码

				QuestionsResult = ApiClient.PwdQuestionQueryReq(mContext,
						pramsMap);
			} catch (Exception e) {
				e.printStackTrace();
				exception = e;
			}
			return QuestionsResult != null;
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
				if (Constant_data.HTTP_SUCCESS.equals(QuestionsResult.coder)) {
					Map<String, String> reqMap = JSONParsing
							.Question(QuestionsResult.jsonBodyObject);
					if (reqMap != null) {
						_UserId = reqMap.get("userId");
						_QwdQuestionFst = reqMap.get("pwdQuestionFst");
						_QwdQuestionSec = reqMap.get("pwdQuestionSec");
						setQuestions();
					}
				} else {
					strError = QuestionsResult.msg;
					ToastUtil.showShort(mContext, strError);
				}
			} else {
				ToastUtil.showShort(mContext, "接收数据出错");
			}
			super.onPostExecute(result);
		}

		@Override
		protected void onCancelled() {
			questionsTask = null;
			super.onCancelled();
		}

	}

	String str1 = "";
	String str2 = "";

	private PwdQuestionCheckReqTask checkReqTask;

	public void setQuestions() {
		// TODO Auto-generated method stub
		String question1 = str1 + _QwdQuestionFst;
		SpannableString sb1 = new SpannableString(question1);
		sb1.setSpan(new ForegroundColorSpan(Color.BLACK), str1.length() ,
				sb1.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
		_FPTV_Question_1.setText(sb1);

		String question2 = str2 + _QwdQuestionSec;
		SpannableString sb2 = new SpannableString(question2);
		sb2.setSpan(new ForegroundColorSpan(Color.BLACK), str2.length() ,
				sb2.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
		_FPTV_Question_2.setText(sb2);
	}

	/**
	 * 签约申请
	 * 
	 * @author kevin
	 * 
	 */
	private class PwdQuestionCheckReqTask extends AsyncTask<String, Void, Boolean> {
		String strError;
		JSONObject jsonData;
		Exception exception;
		private Result CheckResult;

		@Override
		protected Boolean doInBackground(String... prams) {
			// TODO Auto-generated method stub
			try {
				Map<String, Object> pramsMap = new HashMap<String, Object>();

				pramsMap.put("sysType", "1"); // 系统类型
				pramsMap.put("operId", _UserId); // 用户ID
				pramsMap.put("pwdType", _GoType + ""); // 密码类型:1-登录密码,2-支付密码   
				pramsMap.put("pwdQuestionFst", _QwdQuestionFst); // 密码问题1
				pramsMap.put("pwdAnsweFst", Tools.encryptionMD5(_Solution_1)); // 密码问题答案1 MD5标准加密后传入
				pramsMap.put("pwdQuestionSec", _QwdQuestionSec); // 密码问题2
				pramsMap.put("pwdAnswerSec", Tools.encryptionMD5(_Solution_2)); // 密码问题答案2 MD5标准加密后传入

				CheckResult = ApiClient.PwdQuestionCheck(mContext,
						pramsMap);
			} catch (Exception e) {
				e.printStackTrace();
				exception = e;
			}
			return CheckResult != null;
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
				if (Constant_data.HTTP_SUCCESS.equals(CheckResult.coder)) {
					Map<String, String> reqMap = JSONParsing
							.QuestionCheck(CheckResult.jsonBodyObject);
					if (reqMap != null) {
						String _UserId = reqMap.get("userId");
						String _QwdFindResult = reqMap.get("pwdFindResult");
						if("YES".equals(_QwdFindResult)){
							Intent intent = new Intent(mContext, SetPasswordActivity.class);
							intent.putExtra("userId", _UserId);
							intent.putExtra("accountNo", _Accounts);
							intent.putExtra("accountType", _AccountType);
							if (_GoType == 2) {
								intent.putExtra("AccessType", 3);
							} else {
								intent.putExtra("AccessType", 4);
							}
							startActivity(intent);
							FindPasswordActivity.this.finish();
						}else{
							ToastUtil.showShort(mContext, "密码找回失败!");
						}
					}
				} else {
					strError = CheckResult.msg;
					ToastUtil.showShort(mContext, strError);
				}
			} else {
				ToastUtil.showShort(mContext, "接收数据出错");
			}
			super.onPostExecute(result);
		}

		@Override
		protected void onCancelled() {
			checkReqTask = null;
			super.onCancelled();
		}

	}
}
