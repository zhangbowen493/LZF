package com.wanda.pay.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;
import cn.passguard.PassGuardEdit;

import com.wanda.pay.R;
import com.wanda.pay.R.string;
import com.wanda.pay.adapter.QuestionsAdapter;
import com.wanda.pay.app.WDApplication;
import com.wanda.pay.bean.Constant_data;
import com.wanda.pay.dialog.CustomProgressDialog;
import com.wanda.pay.util.ApiClient;
import com.wanda.pay.util.Result;
import com.wanda.pay.util.StringUtils;
import com.wanda.pay.util.ToastUtil;
import com.wanda.pay.util.Tools;
/**
 * 统一云账户信息完善
 * @author Administrator
 *
 */
public class SetPayPasswordActivity extends BaseActivity implements IMVCActivity,OnClickListener{

	private PassGuardEdit _SPEDT_Pay_Password;
	private PassGuardEdit _SPEDT_Pay_Password_Again;
	private Spinner _SPSP_Questions_1;
	private EditText _SPEDT_Solution_1;
	private Spinner _SPSP_Questions_2;
	private EditText _SPEDT_Solution_2;
	private Button _SPBTN_set;
	
	private Context mContext;

	private Map<Integer, String> _Questions = new HashMap<Integer, String>();
	private ArrayList<String> QuestionsList1 = new ArrayList<String>();
	private ArrayList<String> QuestionsList2 = new ArrayList<String>() ;
	private QuestionsAdapter adapter1;
	private QuestionsAdapter adapter2;
	protected Integer _SPSP_Questions_1_code;
	protected Integer _SPSP_Questions_2_code;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_set_pay_password);
		WDApplication.getInstance().addActivity(this);
		mContext = this;
		init();
		initView();
		refresh();
	}
	
	@Override
	public void init() {
		// TODO Auto-generated method stub
		if(_Questions==null){
			_Questions = new HashMap<Integer, String>();
		}
		_Questions.clear();
		_Questions.put(0, "你的出生地？");
		_Questions.put(1, "你父亲的生日是？");
		_Questions.put(2, "你母亲的生日是？");
		_Questions.put(3, "你配偶的生日是多少？");
		_Questions.put(4, "你孩子的名字是什么？");
		_Questions.put(5, "你小学学校的名称是？");
		_Questions.put(6, "你中学学校的名称是？");
		_Questions.put(7, "你初中班主任的名字是？");
		_Questions.put(8, "你高中班主任的名字是？");
		_Questions.put(9, "你大学辅导员的名字是？");
		_Questions.put(10, "你的大学学号是？");
		_Questions.put(11, "你的最喜爱的游戏是？");
	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
		_SPEDT_Pay_Password = (PassGuardEdit) findViewById(R.id.activity_edt_set_check_pay_password);
		_SPEDT_Pay_Password_Again = (PassGuardEdit) findViewById(R.id.activity_edt_set_check_pay_password_again);
		
		_SPSP_Questions_1 = (Spinner) findViewById(R.id.act_sp_set_questions_list_1);
		_SPEDT_Solution_1 = (EditText) findViewById(R.id.act_edt_set_solution_1);
		
		_SPSP_Questions_2 = (Spinner) findViewById(R.id.act_sp_set_questions_list_2);
		_SPEDT_Solution_2 = (EditText) findViewById(R.id.act_edt_set_solution_2);
		
		TextView _SPTV_Title = (TextView) findViewById(R.id.activity_title_tv_name);
		_SPTV_Title.setText("注册");

		
		findViewById(R.id.activity_title_btn_back).setOnClickListener(this);
		_SPBTN_set = (Button) findViewById(R.id.activity_set_check_btn_set);
		_SPBTN_set.setOnClickListener(this);
		
		Tools.initPassGuard(_SPEDT_Pay_Password,Constant_data.passwordMatchStr);
		Tools.initPassGuard(_SPEDT_Pay_Password_Again,Constant_data.passwordMatchStr);
		
		getQuestions();
		adapter1 = new QuestionsAdapter(mContext,QuestionsList1);
		_SPSP_Questions_1.setAdapter(adapter1);
		_SPSP_Questions_1.setOnItemSelectedListener(qusetListener1);
		adapter2 = new QuestionsAdapter(mContext,QuestionsList2);
		_SPSP_Questions_2.setAdapter(adapter2);
		_SPSP_Questions_2.setOnItemSelectedListener(qusetList2);
	}
	private OnItemSelectedListener qusetListener1 = new OnItemSelectedListener() {
		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
			String quest = QuestionsList1.get(arg2);
			_SPSP_Questions_1_code = getQuestionsKey(quest);
			getQuestions();
			adapter1.notifyDataSetChanged();
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
			String quest = QuestionsList2.get(arg2);
			_SPSP_Questions_2_code = getQuestionsKey(quest);
			getQuestions();
			adapter2.notifyDataSetChanged();
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub
			
		}
	};
	private String pay_password;
	private String _RCSP_Solution_1;
	private String _RCSP_Solution_2;
	private SubmitTask submitTask;

	private Integer getQuestionsKey(String str){
		if(StringUtils.isEmpty(str))
		return -1;
		Set set = _Questions.entrySet();         
		Iterator i = set.iterator();         
		while(i.hasNext()){      
			Map.Entry<Integer, String> entry1=(Map.Entry<Integer, String>)i.next();    
			if(str.equals(entry1.getValue())){
				return entry1.getKey();
			}
		} 
		return -1;
	}
	private void getQuestions(){
		QuestionsList1.clear();
		QuestionsList2.clear();
		
		Set set = _Questions.entrySet();         
		Iterator i = set.iterator();         
		while(i.hasNext()){      
		     Map.Entry<Integer, String> entry1=(Map.Entry<Integer, String>)i.next();    
		     if(entry1.getKey() != _SPSP_Questions_2_code){
		    	 QuestionsList1.add(entry1.getValue());
		     }
		     if(entry1.getKey() != _SPSP_Questions_1_code){
		    	 QuestionsList2.add(entry1.getValue());
		     }
		}   
		
	}
	

	@Override
	public void refresh(Object... param) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.activity_title_btn_back:
			//返回
			MyBack();
			break;
		case R.id.activity_set_check_btn_set:
			//设置
			
			Submit();
			break;

		default:
			break;
		}
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


	private void Submit() {
		// TODO Auto-generated method stub
		pay_password = _SPEDT_Pay_Password.getOutput2();
		String pay_password_Again = _SPEDT_Pay_Password_Again.getOutput2();
		
		
		if(StringUtils.isEmpty(pay_password)||!_SPEDT_Pay_Password.checkMatch()){
			ToastUtil.showShort(mContext, "请输入六位或以上密码，并且同时包含数字和大小写字母！");
			return;
		}
		if(StringUtils.isEmpty(pay_password_Again)){
			ToastUtil.showShort(mContext, "请再次输入支付密码");
			return;
		}
		if(!pay_password.equals(pay_password_Again)){
			ToastUtil.showShort(mContext, "支付密码不一致");
			return;
		}
		
		_RCSP_Solution_1 = _SPEDT_Solution_1.getText().toString().trim();
		if(StringUtils.isEmpty(_RCSP_Solution_1)){
			ToastUtil.showShort(mContext, "请输入密保问题答案！");
			return;
		}
		if(_RCSP_Solution_1.length()<2){
			ToastUtil.showShort(mContext, "密保答案过短！");
			return;
		}
		_RCSP_Solution_2 = _SPEDT_Solution_2.getText().toString().trim();
		if(StringUtils.isEmpty(_RCSP_Solution_2)){
			ToastUtil.showShort(mContext, "请输入密保问题答案！");
			return;
		}
		if(_RCSP_Solution_2.length()<2){
			ToastUtil.showShort(mContext, "密保答案过短！");
			return;
		}
		if(_RCSP_Solution_2.equals(_RCSP_Solution_1)){
			ToastUtil.showShort(mContext, "请确保两次密保问题答案不一致！");
			return;
		}
		
		CustomProgressDialog.showProgressDialog(mContext, "loading");
		submitTask = new SubmitTask();
		submitTask.execute();
		
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if(submitTask!=null){
			submitTask.cancel(true);
		}
	}

	@Override
	public void MyBack() {
		// TODO Auto-generated method stub
		Intent intent = new Intent();
		intent.putExtra("status", "UNSUCCESS");
		intent.putExtra("msg", "用户取消");
		setResult(Activity.RESULT_OK,intent);
		SetPayPasswordActivity.this.finish();
	}
	
	/**
	 * 验证注册码+注册
	 * @author kevin
	 *
	 */
	private class SubmitTask extends AsyncTask<String , Void, Boolean>{
		String strError;
		JSONObject jsonData;
		Exception exception;
		private Result registResult;
		
		@Override
		protected Boolean doInBackground(String... prams) {
			// TODO Auto-generated method stub
			try {
				Map<String, Object> pramsMap = new HashMap<String, Object>();
				
				pramsMap.put("userId", WDApplication.getInstance().getUserBean().getmUserID());
				pramsMap.put("payPwd", Tools.encryptionPassword(mContext, pay_password));//密码
				pramsMap.put("pwdQuestionFst",  _Questions.get(_SPSP_Questions_1_code));
				pramsMap.put("pwdAnsweFst", Tools.encryptionMD5(_RCSP_Solution_1));
				pramsMap.put("pwdQuestionSec", _Questions.get(_SPSP_Questions_2_code));
				pramsMap.put("pwdAnswerSec",  Tools.encryptionMD5(_RCSP_Solution_2));
				
				
				
				registResult = ApiClient.regist(mContext, pramsMap);
				
			} catch (Exception e) {
				e.printStackTrace();
				exception = e;
			}
			return registResult != null;
		}
		
		@Override
		protected void onPostExecute(Boolean result) {
			CustomProgressDialog.hideProgressDialog();
			if(exception != null ){
				Toast.makeText(getApplicationContext(), getResources().getString(string.http_error_anomalies), Toast.LENGTH_LONG).show();
				return;
			}
			if(result){
				if(Constant_data.HTTP_SUCCESS.equals(registResult.coder)){
				//成功
					Intent intent = new Intent();
					intent.putExtra("status", "SUCCESS");
					setResult(Activity.RESULT_OK,intent);
					SetPayPasswordActivity.this.finish();
				}else{
					strError = registResult.msg;
					Intent intent = new Intent();
					intent.putExtra("status", "UNSUCCESS");
					intent.putExtra("msg", strError);
					setResult(Activity.RESULT_OK,intent);
					SetPayPasswordActivity.this.finish();
				}
			}else{
				Intent intent = new Intent();
				intent.putExtra("status", "UNSUCCESS");
				intent.putExtra("msg", getResources().getString(string.http_error_anomalies));
				setResult(Activity.RESULT_OK,intent);
				SetPayPasswordActivity.this.finish();
			}
			super.onPostExecute(result);
		}
		
		@Override
		protected void onCancelled() {
			submitTask = null;
			super.onCancelled();
		}
		
	}

}
