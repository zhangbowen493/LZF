package com.wanda.pay.fragment;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import com.wanda.pay.R;
import com.wanda.pay.R.string;
import com.wanda.pay.activity.wanDa_Pay_Activity;
import com.wanda.pay.adapter.BankListAdapter;
import com.wanda.pay.app.WDApplication;
import com.wanda.pay.bean.BankBean;
import com.wanda.pay.bean.Constant_data;
import com.wanda.pay.bean.UserBean;
import com.wanda.pay.dialog.CustomProgressDialog;
import com.wanda.pay.util.ApiClient;
import com.wanda.pay.util.JSONParsing;
import com.wanda.pay.util.Result;
import com.wanda.pay.util.StringUtils;
import com.wanda.pay.util.ToastUtil;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
	/*
	 * 添加银行卡一
 *@作者: Administrator
 *@版本: 
 *@version create time：2016-3-7 下午5:18:20
 */
public class AddCardOneFragment extends Fragment implements OnClickListener ,OnItemSelectedListener {
	
	
	private FAddCardOneBtnClickListener mAddCardOneBtnClickListener;
	private wanDa_Pay_Activity activity;
	private View view;

	public interface FAddCardOneBtnClickListener {
		void onFAddCardOneBackClick(boolean b);

		void onFAddCardOneToTwoClick(Map<String, String> map);
	}

	public void setmLoginBtnClickListener(
			FAddCardOneBtnClickListener mLoginBtnClickListener) {
		this.mAddCardOneBtnClickListener = mLoginBtnClickListener;
	}
	
	private EditText _ACOEDT_Name;
	private EditText _ACOEDT_CardNO;
	private EditText _ACOEDT_IDCardNO;// 身份证号
	private EditText _ACOEDT_CheckPhone; // 预留手机号
	private Spinner _ACOSP_BankList;
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
	private String _BankDesc;
	private String _BankType;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		activity = (wanDa_Pay_Activity) getActivity();
		
		//解析布局
		view = inflater
				.inflate(R.layout.fragment_add_card_one, container, false);
		
		if (view != null) { // 如果view不为空
			initView();
		}
		
		return view;
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		activity.setShowingUI(2);
		adapter = new BankListAdapter(activity, BankList);
		_ACOSP_BankList.setAdapter(adapter);
		getBankList();
	}
	
	public void initView() {
		// TODO Auto-generated method stub

		_ACOEDT_Name = (EditText) view.findViewById(R.id.act_edt_add_card_one_name);
		_ACOEDT_IDCardNO = (EditText) view.findViewById(R.id.act_edt_add_card_one_id_card_number);
		_ACOSP_BankList = (Spinner) view.findViewById(R.id.act_sp_add_card_bank_list);
		_ACOEDT_CardNO = (EditText) view.findViewById(R.id.act_edt_add_card_one_card_number);
		_ACOEDT_CheckPhone = (EditText) view.findViewById(R.id.act_edt_add_card_one_check_phone);

		TextView _ACOTV_Title = (TextView) view.findViewById(R.id.activity_title_tv_name);
		_ACOTV_Title.setText("填写银行卡信息");

		view.findViewById(R.id.activity_title_btn_back).setOnClickListener(this);
		view.findViewById(R.id.act_btn_add_card_one_next).setOnClickListener(this);
		_ACOSP_BankList.setOnItemSelectedListener(this);
		
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
			}
			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {}
			@Override
			public void afterTextChanged(Editable arg0) {}
		});
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
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
				ToastUtil.showShort(activity, "请输入开卡姓名");
				return;
			}
			if (StringUtils.isEmpty(_IdcardNO)|| _IdcardNO.length()>18) {
				ToastUtil.showShort(activity, "请输入正确的身份证号");
				return;
			}
			if (StringUtils.isEmpty(_CardNO)||_CardNO.length()>20) {
				ToastUtil.showShort(activity, "请输入正确银行卡号");
				return;
			}
			if (StringUtils.isEmpty(_CheckPhone)) {
				ToastUtil.showShort(activity, "请输入预留手机号");
				return;
			}
			if (StringUtils.isEmpty(_BankName) || _BankCoder == null) {
				ToastUtil.showShort(activity, "请选择开户银行");
				return;
			}
			
			ValidationCard(_Name, _CardNO, _IdcardNO, _CheckPhone, _BankCoder,
					_BankName);
			break;

		default:
			break;
		}
	}
	
	private void MyBack() {
		// TODO Auto-generated method stub
		if(mAddCardOneBtnClickListener!=null){
			mAddCardOneBtnClickListener.onFAddCardOneBackClick(true);
		}
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		// TODO Auto-generated method stub
		BankBean bankBean = BankList.get(arg2);
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
		CustomProgressDialog.showProgressDialog(activity, "loading");
		appReqTask = new SignedInfoAppReqTask();
		appReqTask.execute();
	}

	/**
	 * 获取可签约银行列表
	 */
	private void getBankList() {
		// TODO Auto-generated method stub
		CustomProgressDialog.showProgressDialog(activity, "loading");
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

					GetUserBanksResult = ApiClient.GetUsableBanks(activity,
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
				Toast.makeText(activity,
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
						ToastUtil.showShort(activity, "未查询到合作银行!");
					}
					adapter.setData(BankList);
					adapter.notifyDataSetChanged();
				} else {
					strError = GetUserBanksResult.msg;
					ToastUtil.showShort(activity, strError);
				}
			} else {
				ToastUtil.showShort(activity,
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
					ReqResult = ApiClient.SignedInfoAppReq(activity, pramsMap);
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
				Toast.makeText(activity, "网络异常，请重试!",
						Toast.LENGTH_LONG).show();
				return;
			}

			if (result) {
				if (Constant_data.HTTP_SUCCESS.equals(ReqResult.coder)||Constant_data.HTTP_SUCCESS_FOR_ADDCARD.equals(ReqResult.coder)) {
					Map<String, String> reqMap = JSONParsing
							.req(ReqResult.jsonBodyObject);
					if (reqMap != null) {
						
						if(mAddCardOneBtnClickListener!=null){
							Map<String, String> map = new HashMap<String, String>();
							map.put("http_reqcode", ReqResult.coder);
							map.put("name", _Name);
							map.put("id_card", _IdcardNO);
							map.put("bank_code", _BankCoder);
							map.put("bank_name", _BankName);
							map.put("check_phone", _CheckPhone);
							map.put("cardNO", _CardNO);
							map.put("card_type", _BankCardType);
							map.put("sysNO", reqMap.get("sysNO"));
							map.put("protocolNo", reqMap.get("protocolNo"));
							map.put("redirectBankURL", reqMap.get("redirectBankURL"));
							map.put("bankDesc", _BankDesc);
							map.put("bankType", _BankType);
							
							mAddCardOneBtnClickListener.onFAddCardOneToTwoClick(map);
						}
						
						
					}
				} else {
					strError = ReqResult.msg;
					ToastUtil.showShort(activity, strError);
				}
			} else {
				ToastUtil.showShort(activity, "接收数据出错");
			}
			super.onPostExecute(result);
		}

		@Override
		protected void onCancelled() {
			appReqTask = null;
			super.onCancelled();
		}

	}

}


