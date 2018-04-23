package com.wanda.pay.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.json.JSONObject;

import u.aly.ad;
import android.content.Context;
import android.content.Intent;
import android.content.ClipData.Item;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;
import cn.passguard.PassGuardEdit;
import cn.passguard.doAction;

import com.wanda.pay.R;
import com.wanda.pay.R.string;
import com.wanda.pay.adapter.QuestionsAdapter;
import com.wanda.pay.app.WDApplication;
import com.wanda.pay.bean.Constant_data;
import com.wanda.pay.dialog.CustomProgressDialog;
import com.wanda.pay.util.ApiClient;
import com.wanda.pay.util.ImmersedStatusbarUtils;
import com.wanda.pay.util.JSONParsing;
import com.wanda.pay.util.LogUtil;
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
public class RegisteredCheckPhoneActivity extends BaseActivity implements
IMVCActivity , OnClickListener{
	Context mContext = this;
	/**输入登录密码*/
	private PassGuardEdit _RCEDT_Login_Password;
	/**输入支付密码*/
	private PassGuardEdit _RCEDT_Pay_Password;
	private String _Extra_Phone;
	private TextView _RCTV_Agreement;
	private EditText _RCEDT_NickName;
	private CheckBox _RCCB;
	private Button _RCBTN_Regist;

	private String nickName;
	private String login_password;
	private String pay_password;

	//	private RegistTask registTask;
	//	private PassGuardEdit _RCEDT_Login_Password_Again;
	//	private PassGuardEdit _RCEDT_Pay_Password_Again;
	private String _Extra_LoginNameType;

	//	private Map<Integer, String> _Questions = new HashMap<Integer, String>();
	//	private Spinner _RCSP_Questions_1;
	private EditText _RCEDT_Solution_1;
	//	private Spinner _RCSP_Questions_2;
	private EditText _RCEDT_Solution_2;

	//	private int _RCSP_Questions_1_code= 0;
	//	private String _RCSP_Solution_1="";
	//	private int _RCSP_Questions_2_code = 1;

	//	private String _RCSP_Solution_2="";

	//	private ArrayList<String> QuestionsList1 = new ArrayList<String>();
	//	private ArrayList<String> QuestionsList2 = new ArrayList<String>() ;
	private TextView _RCTV_Login_Password_Title;
	//	private TextView _RCTV_Login_Password_Again_Title;
	private TextView _RCTV_Pay_Password_Title;
	//	private TextView _RCTV_Pay_Password_Again_Title;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_regist_check_phone);
		ImmersedStatusbarUtils.initAfterSetContentView(this, null);
		WDApplication.getInstance().addActivity(this);
		init();
		initView();
		refresh();
	}


	//	@Override
	//	protected void onDestroy() {
	//		// TODO Auto-generated method stub
	//		super.onDestroy();
	//		if(registTask!=null){
	//			registTask.cancel(true);
	//		}
	//	}
	@Override
	public void init() {
		// TODO Auto-generated method stub
		//		if(_Questions==null){
		//			_Questions = new HashMap<Integer, String>();
		//		}
		//		_Questions.clear();
		//		_Questions.put(0, "你的出生地？");
		//		_Questions.put(1, "你父亲的生日是？");
		//		_Questions.put(2, "你母亲的生日是？");
		//		_Questions.put(3, "你配偶的生日是多少？");
		//		_Questions.put(4, "你孩子的名字是什么？");
		//		_Questions.put(5, "你小学学校的名称是？");
		//		_Questions.put(6, "你中学学校的名称是？");
		//		_Questions.put(7, "你初中班主任的名字是？");
		//		_Questions.put(8, "你高中班主任的名字是？");
		//		_Questions.put(9, "你大学辅导员的名字是？");
		//		_Questions.put(10, "你的大学学号是？");
		//		_Questions.put(11, "你的最喜爱的游戏是？");

		_Extra_Phone = getIntent().getStringExtra("Phone");
		_Extra_LoginNameType = getIntent().getStringExtra("LoginNameType");
	}

	@Override
	public void initView() {

		_RCEDT_NickName = (EditText) findViewById(R.id.activity_edt_regist_check_nickname);

		_RCTV_Login_Password_Title = (TextView) findViewById(R.id.activity_edt_regist_check_login_password_title);
		_RCEDT_Login_Password = (PassGuardEdit) findViewById(R.id.activity_edt_regist_check_login_password);
		_RCEDT_Login_Password.addTextChangedListener(loginPasswordWatcher);

		//		_RCTV_Login_Password_Again_Title = (TextView) findViewById(R.id.activity_edt_regist_check_login_password_again_title);
		//		_RCEDT_Login_Password_Again = (PassGuardEdit) findViewById(R.id.activity_edt_regist_check_login_password_again);
		//		_RCEDT_Login_Password_Again.addTextChangedListener(loginPasswordAgainWatcher);

		_RCTV_Pay_Password_Title = (TextView) findViewById(R.id.activity_edt_regist_check_pay_password_title);
		_RCEDT_Pay_Password = (PassGuardEdit) findViewById(R.id.activity_edt_regist_check_pay_password);
		_RCEDT_Pay_Password.addTextChangedListener(payPasswordWatcher);

		//		_RCTV_Pay_Password_Again_Title = (TextView) findViewById(R.id.activity_edt_regist_check_pay_password_again_title);
		//		_RCEDT_Pay_Password_Again = (PassGuardEdit) findViewById(R.id.activity_edt_regist_check_pay_password_again);
		//		_RCEDT_Pay_Password_Again.addTextChangedListener(payPasswordAgainWatcher);

		//		_RCSP_Questions_1 = (Spinner) findViewById(R.id.act_sp_regist_questions_list_1);
		_RCEDT_Solution_1 = (EditText) findViewById(R.id.act_edt_regist_solution_1);

		//		_RCSP_Questions_2 = (Spinner) findViewById(R.id.act_sp_regist_questions_list_2);
		_RCEDT_Solution_2 = (EditText) findViewById(R.id.act_edt_regist_solution_2);

		_RCCB = (CheckBox) findViewById(R.id.act_cb_regitst_agreed);


		_RCTV_Agreement = (TextView) findViewById(R.id.act_tv_regitst_service_agreement);
		TextView _RCTV_Title = (TextView) findViewById(R.id.activity_title_tv_name);
		_RCTV_Title.setText("注册");


		findViewById(R.id.activity_title_btn_back).setOnClickListener(this);
		_RCBTN_Regist = (Button) findViewById(R.id.activity_regist_check_btn_regist);
		_RCBTN_Regist.setOnClickListener(this);

		_RCTV_Agreement.setOnClickListener(this);
		//		_RCTV_Agreement.setText(Html.fromHtml("<u>"+getResources().getString(string.service_agreement)+"</u>"));

		//		_RCCB.setOnCheckedChangeListener(new OnCheckedChangeListener() {
		//			@Override
		//			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
		//				// TODO Auto-generated method stub
		//				if(arg1){
		//					_RCBTN_Regist.setEnabled(true);
		//				}else{
		//					_RCBTN_Regist.setEnabled(false);
		//				}
		//			}
		//		});

		Tools.initPassGuard(_RCEDT_Login_Password,Constant_data.passwordMatchStr);
		//		Tools.initPassGuard(_RCEDT_Login_Password_Again,Constant_data.passwordMatchStr);
		Tools.initPassGuard(_RCEDT_Pay_Password,Constant_data.passwordMatchStr);
		//		Tools.initPassGuard(_RCEDT_Pay_Password_Again,Constant_data.passwordMatchStr);
		//		getQuestions();
		//		adapter1 = new QuestionsAdapter(mContext,QuestionsList1);
		//		_RCSP_Questions_1.setAdapter(adapter1);
		//		_RCSP_Questions_1.setOnItemSelectedListener(qusetListener1);
		//		adapter2 = new QuestionsAdapter(mContext,QuestionsList2);
		//		_RCSP_Questions_2.setAdapter(adapter2);
		//		_RCSP_Questions_2.setOnItemSelectedListener(qusetList2);


	}
	private boolean loginPWDok =false;
	TextWatcher loginPasswordWatcher = new TextWatcher() {
		@Override
		public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

		}
		@Override
		public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {}
		@Override
		public void afterTextChanged(Editable arg0) {
			// TODO Auto-generated method stub
			loginPWDok = _RCEDT_Login_Password.checkMatch();
			if(loginPWDok){
				_RCTV_Login_Password_Title.setTextColor(Color.BLACK);
			}else{
				_RCTV_Login_Password_Title.setTextColor(Color.RED);
			}
			//			if(_RCEDT_Login_Password_Again.getOutput2()!=null)
			//			_RCEDT_Login_Password_Again.clear();
		}
	};
	private boolean loginPWDok_again = false;
	TextWatcher loginPasswordAgainWatcher = new TextWatcher() {
		@Override
		public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

		}
		@Override
		public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {}
		@Override
		public void afterTextChanged(Editable arg0) {
			// TODO Auto-generated method stub
			//			if(_RCEDT_Login_Password.getOutput2()!=null){
			//				loginPWDok_again = _RCEDT_Login_Password.getOutput2().equals(_RCEDT_Login_Password_Again.getOutput2());
			//				if(loginPWDok_again){
			//					_RCTV_Login_Password_Again_Title.setTextColor(Color.GREEN);
			//				}else{
			//					_RCTV_Login_Password_Again_Title.setTextColor(Color.RED);
			//				}
			//			}else{
			//				_RCTV_Login_Password_Again_Title.setTextColor(Color.RED);
			//			}
		}
	};

	private boolean payPWDok = false;
	TextWatcher payPasswordWatcher = new TextWatcher() {
		@Override
		public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) { }
		@Override
		public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) { }
		@Override
		public void afterTextChanged(Editable arg0) {
			// TODO Auto-generated method stub
			payPWDok = _RCEDT_Pay_Password.checkMatch();
			if(payPWDok){
				_RCTV_Pay_Password_Title.setTextColor(Color.BLACK);
			}else{
				_RCTV_Pay_Password_Title.setTextColor(Color.RED);
			}
			//			if(_RCEDT_Pay_Password_Again.getOutput2()!=null)
			//				_RCEDT_Pay_Password_Again.clear();
		}
	};
	private boolean payPWDok_again = false;
	TextWatcher payPasswordAgainWatcher = new TextWatcher() {
		@Override
		public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

		}
		@Override
		public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {}
		@Override
		public void afterTextChanged(Editable arg0) {
			// TODO Auto-generated method stub
			//			if(_RCEDT_Pay_Password.getOutput2()!=null){
			//				payPWDok_again = _RCEDT_Pay_Password.getOutput2().equals(_RCEDT_Pay_Password_Again.getOutput2());
			//				if(payPWDok_again){
			//					_RCTV_Pay_Password_Again_Title.setTextColor(Color.GREEN);
			//				}else{
			//					_RCTV_Pay_Password_Again_Title.setTextColor(Color.RED);
			//				}
			//			}else{
			//				_RCTV_Pay_Password_Again_Title.setTextColor(Color.RED);
			//			}
		}
	};

	private OnItemSelectedListener qusetListener1 = new OnItemSelectedListener() {


		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
			//			String quest = QuestionsList1.get(arg2);
			//			_RCSP_Questions_1_code = getQuestionsKey(quest);
			//			getQuestions();
			//			adapter1.notifyDataSetChanged();
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub

		}
	};
	private OnItemSelectedListener qusetList2 = new OnItemSelectedListener() {

		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
			//			String quest = QuestionsList2.get(arg2);
			//			_RCSP_Questions_2_code = getQuestionsKey(quest);
			//			getQuestions();
			//			adapter2.notifyDataSetChanged();
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub

		}
	};
	//	private QuestionsAdapter adapter1;
	//	private QuestionsAdapter adapter2;


	//	private Integer getQuestionsKey(String str){
	//		if(StringUtils.isEmpty(str))
	//			return -1;
	//		Set set = _Questions.entrySet();         
	//		Iterator i = set.iterator();         
	//		while(i.hasNext()){      
	//			Map.Entry<Integer, String> entry1=(Map.Entry<Integer, String>)i.next();    
	//			if(str.equals(entry1.getValue())){
	//				return entry1.getKey();
	//			}
	//		} 
	//		return -1;
	//	}
	//	private void getQuestions(){
	//		QuestionsList1.clear();
	//		QuestionsList2.clear();

	//		Set set = _Questions.entrySet();         
	//		Iterator i = set.iterator();         
	//		while(i.hasNext()){      
	//		     Map.Entry<Integer, String> entry1=(Map.Entry<Integer, String>)i.next();    
	//		     if(entry1.getKey() != _RCSP_Questions_2_code){
	//		    	 QuestionsList1.add(entry1.getValue());
	//		     }
	//		     if(entry1.getKey() != _RCSP_Questions_1_code){
	//		    	 QuestionsList2.add(entry1.getValue());
	//		     }
	//		}   

	//	}

	@Override
	public void refresh(Object... param) {
		// TODO Auto-generated method stub

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
		case R.id.activity_title_btn_back:
			//返回
			MyBack();
			break;
		case R.id.activity_regist_check_btn_regist:
			Regist();
			Intent intent1 = new Intent(mContext, RegisteredCheckPhoneActivity2.class);
			intent1.putExtra("Phone", _Extra_Phone);
			intent1.putExtra("userNameType", _Extra_LoginNameType);
			intent1.putExtra("userName", _Extra_Phone);
			intent1.putExtra("nickName", nickName);//昵称
			intent1.putExtra("loginPwd", Tools.encryptionPassword(mContext, login_password));//密码
			intent1.putExtra("payPwd", Tools.encryptionPassword(mContext, pay_password));//密码
			startActivity(intent1);
			break;
		case R.id.act_tv_regitst_service_agreement:
			//服务协议
			Intent intent = new Intent(mContext, WbeVeiwActivity.class);
			intent.putExtra("url", URLs.URL_MAIN+URLs.regist_url);
			startActivity(intent);
			break;
		default:
			break;
		}
	}
	/**
	 * 注册
	 */
	private void Regist() {
		// TODO Auto-generated method stub
		if(!loginPWDok){
			ToastUtil.showShort(mContext, "请输入同时包含数字、大写字母和小写字母的登录密码！");
			_RCEDT_Login_Password.setFocusable(true);
			return;
		}
		//		if(!loginPWDok_again){
		//			ToastUtil.showShort(mContext, "请保持两次输入的登陆密码一致！");
		////			_RCEDT_Login_Password_Again.setFocusable(true);
		//			return;
		//		}
		if(!payPWDok){
			ToastUtil.showShort(mContext, "请输入同时包含数字、大写字母和小写字母的支付密码！");
			_RCEDT_Pay_Password.setFocusable(true);
			return;
		}
		//		if(!payPWDok_again){
		//			ToastUtil.showShort(mContext, "请保持两次输入的支付密码一致！");
		////			_RCEDT_Pay_Password_Again.setFocusable(true);
		//			return;
		//		}
		nickName = _RCEDT_NickName.getText().toString();
		login_password = _RCEDT_Login_Password.getOutput2();
		//		String login_password_Again = _RCEDT_Login_Password_Again.getOutput2();
		pay_password = _RCEDT_Pay_Password.getOutput2();
		//		String pay_password_Again = _RCEDT_Pay_Password_Again.getOutput2();
		if(StringUtils.isEmpty(nickName)){
			ToastUtil.showShort(mContext, "请输入个性登录名");
			return;
		}

		//		if(StringUtils.isEmpty(login_password)||!_RCEDT_Login_Password.checkMatch()){
		//			ToastUtil.showShort(mContext, "请输入六位或以上登录密码");
		//			return;
		//		}
		//		if(StringUtils.isEmpty(login_password_Again)){
		//			ToastUtil.showShort(mContext, "请再次输入登录密码");
		//			return;
		//		}
		//		if(!login_password.equals(login_password_Again)){
		//			ToastUtil.showShort(mContext, "登录密码不一致");
		//			return;
		//		}
		//		
		//		if(StringUtils.isEmpty(pay_password)||!_RCEDT_Pay_Password.checkMatch()){
		//			ToastUtil.showShort(mContext, "请输入六位或以上支付密码");
		//			return;
		//		}
		//		if(StringUtils.isEmpty(pay_password_Again)){
		//			ToastUtil.showShort(mContext, "请再次输入支付密码");
		//			return;
		//		}
		//		if(!pay_password.equals(pay_password_Again)){
		//			ToastUtil.showShort(mContext, "支付密码不一致");
		//			return;
		//		}
		if(login_password.equals(pay_password)){
			ToastUtil.showShort(mContext, "请确保登录密码和支付密码不相同！");
			return;
		}

		//		_RCSP_Solution_1 = _RCEDT_Solution_1.getText().toString().trim();
		//		if(StringUtils.isEmpty(_RCSP_Solution_1)){
		//			ToastUtil.showShort(mContext, "请输入密保问题答案！");
		//			return;
		//		}
		//		
		//		_RCSP_Solution_2 = _RCEDT_Solution_2.getText().toString().trim();
		//		if(StringUtils.isEmpty(_RCSP_Solution_2)){
		//			ToastUtil.showShort(mContext, "请输入密保问题答案！");
		//			return;
		//		}
		//		if(_RCSP_Solution_2.equals(_RCSP_Solution_1)){
		//			ToastUtil.showShort(mContext, "请确保两次密保问题答案不一致！");
		//			return;
		//		}

		CustomProgressDialog.showProgressDialog(mContext, "loading");
		//		registTask = new RegistTask();
		//		registTask.execute();
	}



	/**
	 * 验证注册码+注册
	 * @author kevin
	 *
	 */
	//	private class RegistTask extends AsyncTask<String , Void, Boolean>{
	//		String strError;
	//		JSONObject jsonData;
	//		Exception exception;
	//		private Result registResult;

	//		@Override
	//		protected Boolean doInBackground(String... prams) {
	//			// TODO Auto-generated method stub
	//			try {
	//				Map<String, Object> pramsMap = new HashMap<String, Object>();
	//
	//				pramsMap.put("userNameType", _Extra_LoginNameType);
	//				pramsMap.put("userName", _Extra_Phone);
	//				pramsMap.put("nickName", nickName);//昵称
	//				pramsMap.put("loginPwd", Tools.encryptionPassword(mContext, login_password));//密码
	//				pramsMap.put("payPwd", Tools.encryptionPassword(mContext, pay_password));//密码
	//				//				pramsMap.put("pwdQuestionFst", _Questions.get(_RCSP_Questions_1_code));//密保问题1
	//				//				pramsMap.put("pwdAnswerFst", Tools.encryptionMD5(_RCSP_Solution_1));//密保问题1
	//				//				pramsMap.put("pwdQuestionSec", _Questions.get(_RCSP_Questions_2_code));//密保问题2
	//				//				pramsMap.put("pwdAnswerSec", Tools.encryptionMD5(_RCSP_Solution_2));//密保问题2
	//				registResult = ApiClient.regist(mContext, pramsMap);
	//			} catch (Exception e) {
	//				e.printStackTrace();
	//				exception = e;
	//			}
	//			return registResult != null;
	//		}
	//		@Override
	//		protected Boolean doInBackground(String... params) {
	//			// TODO Auto-generated method stub
	//			return registResult != null;
	//		}
	//		@Override
	//		protected void onPostExecute(Boolean result) {
	//			CustomProgressDialog.hideProgressDialog();
	//			if(exception != null ){
	//				Toast.makeText(getApplicationContext(), getResources().getString(string.http_error_anomalies), Toast.LENGTH_LONG).show();
	//				return;
	//			}
	//			if(result){
	//				if(Constant_data.HTTP_SUCCESS.equals(registResult.coder)){
	//					//注册
	//					Intent intent = new Intent(mContext, RegisteredCheckPhoneActivity2.class);
	//					intent.putExtra("Phone", _Extra_Phone);
	//					intent.putExtra("userNameType", _Extra_LoginNameType);
	//					intent.putExtra("userName", _Extra_Phone);
	//					intent.putExtra("nickName", nickName);//昵称
	//					intent.putExtra("loginPwd", Tools.encryptionPassword(mContext, login_password));//密码
	//					intent.putExtra("payPwd", Tools.encryptionPassword(mContext, pay_password));//密码
	//					startActivity(intent);
	//				}else{
	//					strError = registResult.msg;
	//					ToastUtil.showShort(mContext, strError);
	//				}
	//			}else{
	//				ToastUtil.showShort(mContext, getResources().getString(string.http_error_anomalies));
	//			}
	//			super.onPostExecute(result);
	//		}
	//		@Override
	//		protected void onCancelled() {
	//			registTask = null;
	//			super.onCancelled();
	//		}
	//		
	//
	//	}
}
