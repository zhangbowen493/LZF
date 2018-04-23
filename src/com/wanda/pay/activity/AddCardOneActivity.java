package com.wanda.pay.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import cn.passguard.PassGuardEdit;

import com.wanda.pay.R;
import com.wanda.pay.R.string;
import com.wanda.pay.adapter.BankListAdapter;
import com.wanda.pay.app.WDApplication;
import com.wanda.pay.bean.BankBean;
import com.wanda.pay.bean.Constant_data;
import com.wanda.pay.bean.ScanPay;
import com.wanda.pay.bean.UserBean;
import com.wanda.pay.dialog.CustomProgressDialog;
import com.wanda.pay.util.ApiClient;
import com.wanda.pay.util.ImmersedStatusbarUtils;
import com.wanda.pay.util.JSONParsing;
import com.wanda.pay.util.Result;
import com.wanda.pay.util.StringUtils;
import com.wanda.pay.util.ToastUtil;
import com.wanda.pay.util.Tools;

/**
 * 添加银行卡-用户信息
 * 
 * @author kevin
 * 
 */
public class AddCardOneActivity extends BaseActivity implements IMVCActivity,
OnClickListener, OnItemSelectedListener {

	private Context mContext = this;
	private EditText _ACOEDT_Name;
	private EditText _ACOEDT_CardNO;
	private EditText _ACOEDT_IDCardNO;// 身份证号
	private EditText _ACOEDT_CheckPhone; // 预留手机号
	private TextView _ACOSP_BankList;
	private String _Name;
	private String _CardNO;
	private String _IdcardNO;
	private String _CheckPhone;
	private String _BankCoder;
	private String _BankName;

	private ArrayList<BankBean> BankList;
	private BankListAdapter adapter;
	private GetUsableBanksTask getUsableBanksTask;
	private String _BankCardType; // 卡种
	private SignedInfoAppReqTask appReqTask;
	private String _BankProductCode; // 可签约银行列表 名称 产品编码 签约申请 名称 要签约的快捷支付产品编码
	// sigProCode

	private String _BankID;
	private String text1ID;	
	private String text2ID;	
	private String text3ID;	
	private String _BankDesc;
	private String _BankType;
	private ImageView imageView3;
	private ImageView  imageView4;
	private ImageView imageView6;
	private ImageView imageView5;
	
	
	private WDApplication mApplication;
	protected String mBankProtocolNo;
	private ArrayList<ScanPay> mCardDataList = new ArrayList<ScanPay>();
	private int bankPosition;
	protected boolean popWindowIsShow;
	private EditText ftuEDT_Money;
	private PassGuardEdit ftuEDT_PayPassword;
	private TextView ftuTV_BankCard;
	private TextView ftuTv_bankname;
	private TextView ftuTv_bankweihao;
	private ImageView Image_src;
	private TextView Forgetpwd;
	private String mMoney;
	private String mPayWD;
	private int number;
	private int mCommitMoney = 0;
	private String lognname;
	private String _name;
	private String number1;
	private int list_position=0;
	private BankBean bankBean;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_card_one);
		ImmersedStatusbarUtils.initAfterSetContentView(this, null);
		WDApplication.getInstance().addActivity(this);
		init();
		initView();
		refresh();

	}

	//	private void initColor() {
	//		// TODO Auto-generated method stub
	//		text1ID=_ACOEDT_Name.getText().toString();
	//		text2ID=_ACOEDT_IDCardNO.getText().toString();
	//		text3ID=_ACOEDT_CardNO.getText().toString();
	//
	//		if(text1ID.equals("")||text1ID==null){
	//
	//			imageView3.setImageResource(R.drawable.bank_icon_card);
	//		}else{
	//			imageView3.setImageResource(R.drawable.icon_safe_name);
	//		}
	//
	//	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (getUsableBanksTask != null)
			getUsableBanksTask.cancel(true);
		if (appReqTask != null)
			appReqTask.cancel(true);
	}

	@Override
	public void init() {

	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub

		_ACOEDT_Name = (EditText) findViewById(R.id.act_edt_add_card_one_name);
		_ACOEDT_IDCardNO = (EditText) findViewById(R.id.act_edt_add_card_one_id_card_number);
		_ACOSP_BankList = (TextView) findViewById(R.id.act_sp_add_card_bank_list);
		_ACOEDT_CardNO = (EditText) findViewById(R.id.act_edt_add_card_one_card_number);
		_ACOEDT_CheckPhone = (EditText) findViewById(R.id.act_edt_add_card_one_check_phone);
		imageView3=(ImageView) findViewById(R.id.imageView3);
		imageView4=(ImageView) findViewById(R.id.imageView4);
		imageView6=(ImageView) findViewById(R.id.imageView6);
		imageView5=(ImageView) findViewById(R.id.imageView5);
		TextView _ACOTV_Title = (TextView) findViewById(R.id.activity_title_tv_name);
		_ACOTV_Title.setText("添加银行卡");
		text1ID=_ACOEDT_Name.getText().toString();
		text2ID=_ACOEDT_IDCardNO.getText().toString();
		text3ID=_ACOEDT_CardNO.getText().toString();

		findViewById(R.id.activity_title_btn_back).setOnClickListener(this);
		findViewById(R.id.act_btn_add_card_one_next).setOnClickListener(this);
		//_ACOSP_BankList.setOnItemSelectedListener(this);
		_ACOSP_BankList.setOnClickListener(this);
		//		initColor();
		_ACOEDT_CardNO.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (s == null || s.length() == 0)
					return;
				StringBuilder sb = new StringBuilder();
				for (int i = 0; i < s.length(); i++) {
					if (i != 4 && i != 9 && i != 14 && i != 19
							&& s.charAt(i) == '-') {
						continue;
					} else {
						sb.append(s.charAt(i));
						if ((sb.length() == 5 || sb.length() == 10
								|| sb.length() == 15 || sb.length() == 20)
								&& sb.charAt(sb.length() - 1) != '-') {
							sb.insert(sb.length() - 1, '-');
						}
					}
				}
				if (!sb.toString().equals(s.toString())) {
					int index = start + 1;
					if (sb.charAt(start) == '-') {
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
					_ACOEDT_CardNO.setText(sb.toString());
					_ACOEDT_CardNO.setSelection(index);
				}
				imageView6.setImageResource(R.drawable.icon_safe_write);
			}
			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				imageView5.setImageResource(R.drawable.icon_safe_bank);
				imageView6.setImageResource(R.drawable.bank_icon_bank_number);
			}
			@Override
			public void afterTextChanged(Editable arg0) {
				imageView6.setImageResource(R.drawable.icon_safe_write);
			}
		});
		_ACOEDT_Name.addTextChangedListener(new TextWatcher() {
			//输入中
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				Log.d("TAG","onTextChanged--------------->");  
				imageView3.setImageResource(R.drawable.icon_safe_name);

			}
			//输入前
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				imageView3.setImageResource(R.drawable.bank_icon_card);
				Log.d("TAG","beforeTextChanged--------------->");  
			}
			//输入后
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				imageView3.setImageResource(R.drawable.icon_safe_name);
				Log.d("TAG","afterTextChanged--------------->");  
			}
		});
		_ACOEDT_IDCardNO.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				imageView4.setImageResource(R.drawable.icon_safe_number);
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				imageView4.setImageResource(R.drawable.bank_icon_card_number);
			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				imageView4.setImageResource(R.drawable.icon_safe_number);
			}
		});


	}

	@Override
	public void refresh(Object... param) {
		// TODO Auto-generated method stub
//		adapter = new BankListAdapter(mContext, BankList);
//		_ACOSP_BankList.setAdapter(adapter);
		getBankList();
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
		case R.id.act_sp_add_card_bank_list:
			JumpBankList();
			break;
		case R.id.activity_title_btn_back:
			// 返回
			MyBack();
			break;
		case R.id.act_btn_add_card_one_next:
			_Name = _ACOEDT_Name.getText().toString().trim();
			_CardNO = _ACOEDT_CardNO.getText().toString().trim().replaceAll("-", "");
			_IdcardNO = _ACOEDT_IDCardNO.getText().toString().trim().toUpperCase();// 身份证号
			_CheckPhone = _ACOEDT_CheckPhone.getText().toString().trim();// 预留手机

			if (StringUtils.isEmpty(_Name)) {
				ToastUtil.showShort(mContext, "请输入开卡姓名");
				return;
			}
			if (!Tools.isIDNo(_IdcardNO)){//StringUtils.isEmpty(_IdcardNO)|| _IdcardNO.length()>18) {
				ToastUtil.showShort(mContext, "请输入正确的身份证号");
				return;
			}
			if (!Tools.isCardNo(_CardNO)){//StringUtils.isEmpty(_CardNO)||_CardNO.length()>20) {
				ToastUtil.showShort(mContext, "请输入正确银行卡号");
				return;
			}
			if (StringUtils.isEmpty(_CheckPhone)) {
				ToastUtil.showShort(mContext, "请输入预留手机号");
				return;
			}
			if (StringUtils.isEmpty(_BankName) || _BankCoder == null) {
				ToastUtil.showShort(mContext, "请选择开户银行");
				return;
			}

			ValidationCard(_Name, _CardNO, _IdcardNO, _CheckPhone, _BankCoder,
					_BankName);
			break;

		default:
			break;
		}
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		// TODO Auto-generated method stub
		bankBean = BankList.get(arg2);
		_BankCoder = bankBean.getBankCode();
		_BankName = bankBean.getBankName();
		_BankCardType = bankBean.getCardTypeCode();
		_BankProductCode = bankBean.getProductCode();
		_BankID = bankBean.getBankID();
		_BankDesc = bankBean.getProductDesc();
		_BankType = bankBean.getSignTypeForMark();
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		_BankCoder = null;
		_BankName = null;
	}

	/**
	 * 验证 卡人统一
	 * 
	 * @param name
	 *            开户姓名
	 * @param cardNO
	 *            卡号
	 * @param bankName
	 *            银行名称
	 * @param bankCode
	 *            银行coder
	 * @param checkPhone
	 *            预留手机
	 * @param idcardNO
	 *            银行卡号
	 */
	private void ValidationCard(String name, String cardNO, String idcardNO,
			String checkPhone, String bankCode, String bankName) {
		// TODO Auto-generated method stub
		CustomProgressDialog.showProgressDialog(mContext, "loading");
		appReqTask = new SignedInfoAppReqTask();
		appReqTask.execute();
	}

	/**
	 * 获取可签约银行列表
	 */
	private void getBankList() {
		// TODO Auto-generated method stub
		CustomProgressDialog.showProgressDialog(mContext, "loading");
		getUsableBanksTask = new GetUsableBanksTask();
		getUsableBanksTask.execute();
	}

	/**
	 * 获取可签约银行
	 * 
	 * @author kevin
	 * 
	 */
	private class GetUsableBanksTask extends AsyncTask<String, Void, Boolean> {
		String strError;
		JSONObject jsonData;
		Exception exception;
		private Result GetUserBanksResult;

		@Override
		protected Boolean doInBackground(String... prams) {
			// TODO Auto-generated method stub
			try {
				UserBean userBean = WDApplication.getInstance().getUserBean();
				if (userBean != null) {
					Map<String, Object> pramsMap = new HashMap<String, Object>();

					pramsMap.put("sysNo", "2");
					pramsMap.put("customerNo", userBean.getmUserNO());

					GetUserBanksResult = ApiClient.GetUsableBanks(mContext,
							pramsMap);
				}
			} catch (Exception e) {
				e.printStackTrace();
				exception = e;
			}
			return GetUserBanksResult != null;
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
				if (Constant_data.HTTP_SUCCESS.equals(GetUserBanksResult.coder)) {
					BankList = JSONParsing
							.GetUsableBanksInfo(GetUserBanksResult.jsonBodyArray);
					if (BankList == null) {
						// 获取可签约银行列表
						ToastUtil.showShort(mContext, "未查询到合作银行!");
					}
//					adapter.setData(BankList);
//					adapter.notifyDataSetChanged();
				} else {
					strError = GetUserBanksResult.msg;
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
			getUsableBanksTask = null;
			super.onCancelled();
		}

	}

	/**
	 * 签约申请
	 * 
	 * @author kevin
	 * 
	 */
	private class SignedInfoAppReqTask extends AsyncTask<String, Void, Boolean> {
		String strError;
		JSONObject jsonData;
		Exception exception;
		private Result ReqResult;

		@Override
		protected Boolean doInBackground(String... prams) {
			// TODO Auto-generated method stub
			try {
				UserBean userBean = WDApplication.getInstance().getUserBean();
				if (userBean != null) {
					Map<String, Object> pramsMap = new HashMap<String, Object>();
					pramsMap.put("appType", "2"); // 申请类型  1、随机码验证申请 2、随机金额验证申请

					pramsMap.put("userRealName", _Name); // 真实姓名
					pramsMap.put("customerNo", userBean.getmUserNO()); // 用户号
					pramsMap.put("identity", _IdcardNO); // 身份证号码
					pramsMap.put("bankCardNo", _CardNO); // 银行卡号
					pramsMap.put("cardTypeCode", _BankCardType); // 签约卡种
					pramsMap.put("cobankNo", _BankCoder); // 银行编号
					pramsMap.put("reservedPhone", _CheckPhone); // 预留手机号
					pramsMap.put("mobileNumber", _CheckPhone); // 预留手机号
					pramsMap.put("bankName", _BankName); //
					pramsMap.put("bankId", _BankID); //银行ID
					pramsMap.put("sigProCode", _BankProductCode); // 要签约的快捷支付产品编码
					pramsMap.put("creCardPeriod", ""); // 信用卡有效期
					pramsMap.put("CVV2", ""); // CVV2码
					ReqResult = ApiClient.SignedInfoAppReq(mContext, pramsMap);
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
			if (exception != null) {
				Toast.makeText(getApplicationContext(), "网络异常，请重试!",
						Toast.LENGTH_LONG).show();
				return;
			}

			if (result) {
				if (Constant_data.HTTP_SUCCESS.equals(ReqResult.coder)||Constant_data.HTTP_SUCCESS_FOR_ADDCARD.equals(ReqResult.coder)) {
					Map<String, String> reqMap = JSONParsing
							.req(ReqResult.jsonBodyObject);
					if (reqMap != null) {
						Intent intent = new Intent(mContext,
								AddCardTwoActivity.class);

						intent.putExtra("http_reqcode", ReqResult.coder);
						intent.putExtra("name", _Name);
						intent.putExtra("id_card", _IdcardNO);
						intent.putExtra("bank_code", _BankCoder);
						intent.putExtra("bank_name", _BankName);
						intent.putExtra("check_phone", _CheckPhone);
						intent.putExtra("cardNO", _CardNO);
						intent.putExtra("card_type", _BankCardType);
						intent.putExtra("sysNO", reqMap.get("sysNO"));
						intent.putExtra("protocolNo", reqMap.get("protocolNo"));
						intent.putExtra("redirectBankURL",
								reqMap.get("redirectBankURL"));
						intent.putExtra("bankDesc", _BankDesc);
						intent.putExtra("bankType", _BankType);
						startActivity(intent);
					}
				} else {
					strError = ReqResult.msg;
					ToastUtil.showShort(mContext, strError);
				}
			} else {
				ToastUtil.showShort(mContext, "接收数据出错");
			}
			super.onPostExecute(result);
		}

		@Override
		protected void onCancelled() {
			appReqTask = null;
			super.onCancelled();
		}

	}

	
	private void SelcetBankCard() {
		// TODO Auto-generated method stub
		if (BankList != null && !BankList.isEmpty()) {
			if (BankList.size() == 0 || bankPosition == -1) {
				showAddCardDialog();
			} else {
				//去选择页面
				JumpBankList();
			}
		} else {
			showAddCardDialog();
		}
	}
	private void showAddCardDialog() {
		final Dialog mDialog = new Dialog(mContext, R.style.myDilog);
		mDialog.setContentView(R.layout.dialog_layout);
		Button sure = (Button) mDialog.findViewById(R.id.dialog_btn_ok);
		Button back = (Button) mDialog.findViewById(R.id.dialog_btn_cancel);
		TextView _DialogTV = (TextView) mDialog
				.findViewById(R.id.dialog_tv_msg);
		_DialogTV.setText("您还没有添加银行卡，是否现在添加?");
		mDialog.setCanceledOnTouchOutside(false);
		sure.setText("是");
		back.setText("否");
		sure.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				bankPosition = -1;
				startActivity(new Intent(mContext, AddCardOneActivity.class));
				WDApplication.isAddCard = true;

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
	/**
	 * 跳转到列表选择页
	 */
	private void JumpBankList() {
		// TODO Auto-generated method stub
		Intent intent = new Intent(mContext, BankListPopupWindow.class);
		intent.putExtra("cardList", mCardDataList);
		startActivityForResult(intent, 800);
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);

		if(requestCode==800 && resultCode == 998){
			number = data.getIntExtra("number", 0);
			list_position=number;
			_ACOSP_BankList.setText(BankList.get(number).getBankName());
			bankPosition = number;
			bankBean = BankList.get(bankPosition);
			_BankCoder = bankBean.getBankCode();
			_BankName = bankBean.getBankName();
			_BankCardType = bankBean.getCardTypeCode();
			_BankProductCode = bankBean.getProductCode();
			_BankID = bankBean.getBankID();
			_BankDesc = bankBean.getProductDesc();
			_BankType = bankBean.getSignTypeForMark();
		}

	}
	
}
