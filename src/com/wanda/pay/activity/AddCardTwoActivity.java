package com.wanda.pay.activity;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.PopupWindow;
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
import com.wanda.pay.util.ImmersedStatusbarUtils;
import com.wanda.pay.util.Result;
import com.wanda.pay.util.StringUtils;
import com.wanda.pay.util.ToastUtil;
import com.wanda.pay.util.Tools;
import com.wanda.pay.util.URLs;
/**
 * 添加银行卡-银行卡信息校验
 * @author kevin
 *s-works
 */
public class AddCardTwoActivity extends BaseActivity implements IMVCActivity , OnClickListener{

	private Context mContext = this;

	private TextView _ACTWTV_Name;		//姓名
	private TextView _ACTWTV_CardNumber;	//银行卡号
	private TextView _ACTWTV_Type;		//银行名称 + 卡类型
	private TextView _ACTWTV_IDCardNO;		//身份证号
	private EditText _ACTWEDT_CheckCod; // 短信随机金额
	//	private CheckBox _ACTWCB_agreement;	//同意条款
	private Button _ACTWBTN_Next;		//下一步
	//	private TextView _ACTWTV_Agreement;		//服务条款
	private PassGuardEdit _ACTWEDT_PayPassword;	//支付密码

	private String _Name;		//开卡姓名
	private String _CardNO;		//卡号
	private String _CardType;		//卡类型
	private String _Phone;			//预留手机号
	private String _IdcardNO;	//身份证号
	private String _BankCoder;	//银行代码
	private String _BankName;	//银行名称
	private String _CheckPhone;	//	预留手机号
	private String _SysNo;	//系统编号
	private String _ProtocolNo;	//协议号
	private String _RedirectBankURL;	//跳转到银行URL 注意：只有在E-付通签约的时候才会有的
	private String _CheckCode;
	private String _PayPassword;
	private SignedInfoConReqTask conReqTask;

	private String _BankDesc; //银行描述

	private String _BankType;	//银行签约方式
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_card_two);
		ImmersedStatusbarUtils.initAfterSetContentView(this, null);
		WDApplication.getInstance().addActivity(this);
		init();
		initView();
		refresh();
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if(conReqTask!=null)
			conReqTask.cancel(true);
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		Intent intent = getIntent();
		_HttpReqCode = intent.getStringExtra("http_reqcode");
		_Name = intent.getStringExtra("name");
		_CardNO = intent.getStringExtra("cardNO");
		_CardType = intent.getStringExtra("card_type");
		_IdcardNO = intent.getStringExtra("id_card");
		_BankCoder = intent.getStringExtra("bank_code");
		_BankName = intent.getStringExtra("bank_name");
		_CheckPhone = intent.getStringExtra("check_phone");
		_SysNo = intent.getStringExtra("sysNO");
		_ProtocolNo = intent.getStringExtra("protocolNo");
		_RedirectBankURL = intent.getStringExtra("redirectBankURL");
		_BankDesc = intent.getStringExtra("bankDesc");
		_BankType = intent.getStringExtra("bankType");


	}

	@Override
	public void initView() {
		_ACTWTV_Name = (TextView) findViewById(R.id.act_tv_add_card_two_name);
		_ACTWTV_IDCardNO = (TextView) findViewById(R.id.act_tv_add_card_two_id_card_number);
		_ACTWTV_Type = (TextView) findViewById(R.id.act_tv_add_card_two_card_type);
		_ACTWTV_CardNumber = (TextView) findViewById(R.id.act_tv_add_card_two_card_number);
		_ACTWEDT_CheckCod = (EditText) findViewById(R.id.act_edt_add_card_two_check_code);
		_ACTWEDT_CheckCod.addTextChangedListener(CheckWatcher);
		_ACTWEDT_PayPassword = (PassGuardEdit) findViewById(R.id.act_edt_add_card_two_pay_password);
		//		_ACTWCB_agreement = (CheckBox) findViewById(R.id.act_cb_add_card_two_agreed);
		//		_ACTWTV_Agreement = (TextView) findViewById(R.id.act_tv_add_card_two_service_agreement);
		TextView _ACTWTV_Title = (TextView) findViewById(R.id.activity_title_tv_name);
		_ACTWTV_Title.setText("安全验证");
		findViewById(R.id.act_ibtn_add_two_instructions).setOnClickListener(this);
		Tools.initPassGuard(_ACTWEDT_PayPassword,"^.{6,}$");
		findViewById(R.id.activity_title_btn_back).setOnClickListener(this);
		_ACTWBTN_Next = (Button) findViewById(R.id.act_btn_add_card_two_next);
		//		_ACTWBTN_Next.setEnabled(true);
		_ACTWBTN_Next.setOnClickListener(this);
		//		_ACTWTV_Agreement.setText(Html.fromHtml("<u>"+getResources().getString(string.service_agreement)+"</u>"));
		//		_ACTWTV_Agreement.setOnClickListener(this);
		//		_ACTWCB_agreement.setOnCheckedChangeListener(new OnCheckedChangeListener() {
		//			@Override
		//			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
		//				// TODO Auto-generated method stub
		//				if(arg1){
		//					_ACTWBTN_Next.setEnabled(true);
		//				}else{
		//					_ACTWBTN_Next.setEnabled(false);
		//				}
		//			}
		//		});
	}

	private TextWatcher CheckWatcher = new TextWatcher() {

		@Override
		public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
			// TODO Auto-generated method stub

		}

		@Override
		public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {
			// TODO Auto-generated method stub

		}

		@Override
		public void afterTextChanged(Editable arg0) {
			// TODO Auto-generated method stub
			if(arg0.length()==1 && "0".equals(arg0.toString())){
				arg0.clear();
			}
		}
	};

	private String _HttpReqCode;

	@Override
	public void refresh(Object... param) {
		// TODO Auto-generated method stub

		_ACTWTV_Name.setText(_Name);
		_ACTWTV_CardNumber.setText(_CardNO);
		String type = "储蓄卡";
		if("A02".equals(_CardType)){
			type = "信用卡";
		}
		_ACTWTV_Type.setText(_BankName+" "+type);
		_ACTWTV_IDCardNO.setText(_IdcardNO);

	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		if(Constant_data.HTTP_SUCCESS_FOR_ADDCARD.equals(_HttpReqCode)){
			showMyDialog(getResources().getString(string.pop_again_str));
		}

	}


	private void showMyDialog(String str) {
		final Dialog mDialog = new Dialog(mContext, R.style.myDilog);
		mDialog.setContentView(R.layout.dialog_prompt_layout);
		Button sure = (Button) mDialog.findViewById(R.id.dialog_btn_ok);
		TextView _DialogTV = (TextView) mDialog
				.findViewById(R.id.dialog_tv_msg);
		_DialogTV.setText(str);
		mDialog.setCanceledOnTouchOutside(false);

		sure.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mDialog.isShowing())
					mDialog.dismiss();
			}
		});

		Window window = mDialog.getWindow();
		window.setGravity(Gravity.CENTER); // 此处可以设置dialog显示的位置
		window.setWindowAnimations(R.style.mystyle); // 添加动画mDialog.
		mDialog.setCanceledOnTouchOutside(true);
		mDialog.show();
	}

	@Override
	public void MyBack() {
		// TODO Auto-generated method stub
		WDApplication.getInstance().mScreenManager.popAllActivitys("AddCardTwoActivity");
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
		case R.id.act_btn_add_card_two_next:
			_CheckCode = _ACTWEDT_CheckCod.getText().toString().trim();
			_PayPassword = _ACTWEDT_PayPassword.getOutput2();
			if(StringUtils.isEmpty(_CheckCode)){
				ToastUtil.showShort(mContext, "请输入随机金额！");
				return;
			}
			if(StringUtils.isEmpty(_PayPassword)){
				ToastUtil.showShort(mContext, "请输入支付密码！");
				return;
			}
			CheckAddCard(_CheckCode,_PayPassword);

			break;
			//		case R.id.act_tv_add_card_two_service_agreement:
			//			//服务协议
			//
			//			Intent intent = new Intent(mContext, WbeVeiwActivity.class);
			//			intent.putExtra("url", URLs.URL_MAIN+URLs.pay_url);
			//			startActivity(intent);
			//			//			startActivity(new Intent(mContext, ServiceAgreementActivity.class));
			//			break;
		case R.id.act_ibtn_add_two_instructions:
			//随机金额提醒
			showPopupWindow(_ACTWEDT_CheckCod,getResources().getString(string.pop_defult_str));
			break;
		default:
			break;
		}
	}
	/**
	 * 说明随机金额来源
	 * 
	 * 系统已给您的卡号转账一笔随机小额资金，请查询该交易金额作为验证凭证（通过短信通知或者网银）。
	 */
	private void showPopupWindow(View view , String str) {

		int width = getWindowManager().getDefaultDisplay().getWidth();
		int height=findViewById(R.id.act_ibtn_add_two_instructions).getHeight();
		// 一个自定义的布局，作为显示的内容
		View contentView = LayoutInflater.from(mContext).inflate(
				R.layout.pop_window,null);

		TextView pop_showtv = (TextView) contentView.findViewById(R.id.pop_tv);
		pop_showtv.setText(str);

		final PopupWindow popupWindow = new PopupWindow(contentView,LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		popupWindow.setWidth((int)(width*0.5));
		popupWindow.setTouchable(true);
		popupWindow.setFocusable(true);  
		popupWindow.setOutsideTouchable(true); 
		popupWindow.setTouchInterceptor(new OnTouchListener() {
			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				// TODO Auto-generated method stub
				// 这里如果返回true的话，touch事件将被拦截
				// 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
				return false;
			}
		});
		// 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
		// 我觉得这里是API的一个bug
		popupWindow.setBackgroundDrawable(new BitmapDrawable() );
		// 设置好参数之后再show
		popupWindow.showAtLocation(view, Gravity.RIGHT, 0, height*2);
		//popupWindow.showAsDropDown(view, 0, 0);
		//	        popupWindow.showAtLocation(view, Gravity.VERTICAL_GRAVITY_MASK, 0, 0);

	}


	/**
	 * 获取随机金额
	 * @param phone		手机号码
	 * @param payPassword 
	 */
	private void CheckAddCard(String phone, String payPassword) {
		// TODO Auto-generated method stub
		CustomProgressDialog.showProgressDialog(mContext, "loading");
		conReqTask = new SignedInfoConReqTask();
		conReqTask.execute();
	}

	/**
	 * 签约确认
	 * @author kevin
	 *
	 */
	private class SignedInfoConReqTask extends AsyncTask<String , Void, Boolean>{
		String strError;
		JSONObject jsonData;
		Exception exception;
		private Result ReqResult;
		@Override
		protected Boolean doInBackground(String... prams) {
			// TODO Auto-generated method stub
			try {
				UserBean userBean = WDApplication.getInstance().getUserBean();
				if(userBean!=null){
					Map<String, Object> pramsMap = new HashMap<String, Object>();
					pramsMap.put("protocolNo", _ProtocolNo); 	//协议号
					pramsMap.put("customerNo", userBean.getmUserNO()); //用户号
					pramsMap.put("status", ""); 	//状态
					pramsMap.put("desc", "");	//描述
					pramsMap.put("payPassWord", _PayPassword); //支付密码
					pramsMap.put("mobieVeriCode", _CheckCode);	//手机随机金额
					pramsMap.put("sysNo", _SysNo);		//请求系统编码
					pramsMap.put("userName", _Name);		//用户名
					ReqResult = ApiClient.SignedInfoConReq(mContext, pramsMap);
				}
			} catch (Exception e) {
				e.printStackTrace();
				exception = e;
			}
			return ReqResult != null;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			CustomProgressDialog.hideProgressDialog();
			if(exception != null ){
				Toast.makeText(getApplicationContext(), getResources().getString(string.http_error_anomalies), Toast.LENGTH_LONG).show();
				return;
			}
			if(result){
				if(Constant_data.HTTP_SUCCESS.equals(ReqResult.coder)){
					//					ToastUtil.showShort(mContext, "银行卡添加成功！");
					Intent intent = new Intent(mContext, AddBankCardSuccessfuActivity.class);
					startActivity(intent);
					BankCenterActivity.isRefresh = true;
					//					if(WDApplication.isAddCard){
					//						WDApplication.isAddSuccess=true;
					//						WDApplication.getInstance().isRefreshUserInfo = true;
					//						WDApplication.getInstance().mScreenManager.popAllActivitys("AddCardOneActivity","AddCardTwoActivity");
					//					}else{
					//						WDApplication.isAddSuccess=true;
					//						startActivity(new Intent(AddCardTwoActivity.this, BankCenterActivity.class));
					//					}
				}else{
					strError = ReqResult.msg;
					ToastUtil.showShort(mContext, strError);
				}
			}else{
				ToastUtil.showShort(mContext, getResources().getString(string.http_error_anomalies));
			}
			super.onPostExecute(result);
		}

		@Override
		protected void onCancelled() {
			conReqTask = null;
			super.onCancelled();
		}

	}

}
