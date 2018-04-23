package com.wanda.pay.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;

import cn.passguard.PassGuardEdit;

import com.wanda.pay.R;
import com.wanda.pay.R.string;
import com.wanda.pay.activity.AddCardOneActivity;
import com.wanda.pay.activity.wanDa_Pay_Activity;
import com.wanda.pay.app.WDApplication;
import com.wanda.pay.bean.Constant_data;
import com.wanda.pay.bean.ScanPay;
import com.wanda.pay.dialog.CustomProgressDialog;
import com.wanda.pay.util.ApiClient;
import com.wanda.pay.util.Result;
import com.wanda.pay.util.SPUtils;
import com.wanda.pay.util.StringUtils;
import com.wanda.pay.util.ToastUtil;
import com.wanda.pay.util.Tools;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.PopupWindow.OnDismissListener;

public class ScanPayMoneyFragment extends Fragment {
	
	public static boolean payAddCard = false;
	public static boolean payAddSuccess = false;
	
	public static final int AddCardCode = 1;
	public static  boolean isNeedBack = false;
	private FPayListener mPayListener;
	private View view;
	private wanDa_Pay_Activity activity;
	private WDApplication mApplication;

	private PassGuardEdit _LEDT_Password;
	private GetCardInfTask mGetCardInfTask;
	private PayMoneyTask mPayMoneyTask;
	private LinearLayout mChoiceBankLayout;
	private PopupWindow popupWindow;
	private int screeWidth;
	private ArrayList<ScanPay> mCardDataList = new ArrayList<ScanPay>();
	private ScanPay mScanpayData;
	private TextView mGoodsName, mGoodsMoney, mGoodsGetMoneyName,
			mBankCardNumber;
	private ImageView mGoodImage, mBankCardImage;
	private boolean popWindowIsShow = false;
	private TextView mJiantou;
	private String mScanInformation;
	private int bankPosition = 0; // 选择银行卡的下标
	private QueryProductInfo mQueryProductInfo;
	

	public interface FPayListener{
		void FPayBackListener(String status, String msg ,int code);
		void FPayAddCardListener();
		void FPayFinish();
	}
	public void setmPayListener(FPayListener mPayListener) {
		this.mPayListener = mPayListener;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			 ViewGroup container,  Bundle savedInstanceState) {
		activity = (wanDa_Pay_Activity) getActivity();
		
		//解析布局
		view = inflater
				.inflate(R.layout.fragment_scan_paymoney, container, false);
		
		Bundle arguments = getArguments();
		
		mScanInformation = arguments.getString("code_url");
		
		if (view != null) { // 如果view不为空
			initView();
		}
		return view;
	}
	

	private void initView() {
		// TODO Auto-generated method stub
		TextView _BCTV_Title = (TextView) view.findViewById(R.id.activity_title_tv_name);
		_BCTV_Title.setText(getResources().getString(string.uset_scan_pay));
		view.findViewById(R.id.activity_title_btn_back).setOnClickListener(new OnClickListener() {
			
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(mPayListener!=null){
					mPayListener.FPayFinish();
				}
			}
		});

		mChoiceBankLayout = (LinearLayout) view.findViewById(R.id.scan_choice_bank_layout);
		mChoiceBankLayout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mCardDataList != null && !mCardDataList.isEmpty()) {
					if (bankPosition == -1) {
						showAddCardDialog();
					} else {
						if (popupWindow != null && popupWindow.isShowing()) {
							popupWindow.dismiss();
						} else {
							popupWindow
									.setAnimationStyle(R.style.main_popwin_anim_style);
							popupWindow.showAsDropDown(v, -v.getWidth(), 0);
							popWindowIsShow = !popWindowIsShow;
							if (popWindowIsShow) {
								mJiantou.setBackgroundResource(R.drawable.jiantou_up);
							} else {
								mJiantou.setBackgroundResource(R.drawable.jiantou_down);
							}
						}
					}
				} else
					showAddCardDialog();
			}
		});
		mGoodsName = (TextView) view.findViewById(R.id.scan_goods_name);
		mGoodsMoney = (TextView) view.findViewById(R.id.scan_goods_money);
		mGoodsGetMoneyName = (TextView) view.findViewById(R.id.scan_goods_getmoney_name);
		mBankCardNumber = (TextView) view.findViewById(R.id.scan_cardbank_number);
		mJiantou = (TextView) view.findViewById(R.id.scan_cardbank_jiantou);

		mGoodImage = (ImageView) view.findViewById(R.id.scan_goods_imageview);
		mBankCardImage = (ImageView) view.findViewById(R.id.scan_cardbank_imageview);

		view.findViewById(R.id.scan_sure_pay_button).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						if (mCardDataList != null && !mCardDataList.isEmpty()) {
							if (mCardDataList.size() == 0 || bankPosition == -1) {
								showAddCardDialog();
							} else {
								if(mScanpayData!=null){
									showInputPasswordDialog();
								}else{
									ToastUtil.showShort(activity, "订单信息获取失败，请重新支付！");
									if(mPayListener!=null){
										mPayListener.FPayFinish();
									}
								}
							}
						} else
							showAddCardDialog();
					}
				});
		
		mApplication = WDApplication.getInstance();	
		
		screeWidth = activity.getWindowManager().getDefaultDisplay().getWidth();

		initMorePopwindow();
		
		mQueryProductInfo = new QueryProductInfo();
		isQueryProduct = false;
		mQueryProductInfo.execute(mScanInformation);

		mGetCardInfTask = new GetCardInfTask();
		isGetCardSuccess = false;
		mGetCardInfTask.execute();
		CustomProgressDialog.showProgressDialog(activity, "loading",
				mGetCardInfTask);
		
	}
	
	@Override
	public void onDetach() {
		// TODO Auto-generated method stub
		if (mGetCardInfTask != null)
			mGetCardInfTask.cancel(true);
		if (mPayMoneyTask != null)
			mPayMoneyTask.cancel(true);
		super.onDetach();
		 payAddCard = false;
		 payAddSuccess = false;
	}



	private void showAddCardDialog() {
		final Dialog mDialog = new Dialog(activity, R.style.myDilog);
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
//				startActivity(new Intent(activity,
//						AddCardOneActivity.class));
				
				if(mPayListener!=null){
					mPayListener.FPayAddCardListener();
				}
				
				 payAddCard= true;
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
		
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		activity.setShowingUI(1);
		if( payAddCard &&  payAddSuccess){
			CustomProgressDialog.showProgressDialog(activity, "loading");
			 payAddCard = false;
			 payAddSuccess = false;
			isGetCardSuccess = false;
			mGetCardInfTask = new GetCardInfTask();
			mGetCardInfTask.execute(mScanInformation);
		}else{
			if (mCardDataList != null && !mCardDataList.isEmpty()) {
				initMorePopwindow();
			}
			if (mCardDataList.size() > 0) {
				bankPosition = 0;
				mBankCardNumber.setText(mCardDataList.get(0)
						.getBankName());
				mBankProtocolNo = mCardDataList.get(0)
						.getProtocolNo();
			}
		}
		super.onResume();
	}
	/**是否隐藏加载页面*/
	private boolean isGetCardSuccess = false;
	private boolean isQueryProduct = false;
	/**
	 * 查询二维码中商品信息
	 * @author kevin
	 *
	 */
	private class QueryProductInfo extends AsyncTask<String, Void, Boolean> {
		Result dataInf;
		Exception exception;

		@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
			try {
				Map<String, Object> param = new HashMap<String, Object>();
				param.put("s_gatewayjnl_no", params[0]); // 二维码支付流水号
				dataInf = ApiClient.getScanPayInformation(
						activity, param);

			} catch (Exception e) {
				e.printStackTrace();
				exception = e;
			}
			return dataInf != null;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			// TODO Auto-generated method stub
			isQueryProduct = true;
			if(isGetCardSuccess && isQueryProduct){
				CustomProgressDialog.hideProgressDialog();
			}
			if (exception != null) {
				Toast.makeText(activity,
						getResources().getString(string.http_error_anomalies),
						Toast.LENGTH_LONG).show();
				return;
			}
			if (result) {
				if (Constant_data.HTTP_SUCCESS.equals(dataInf.coder)) {
					try {
						mScanpayData = ScanPay.fromJson(dataInf.jsonBodyObject);

						if (mScanpayData != null) {
							mGoodsName.setText(mScanpayData.getGoodsName());
							mGoodsMoney.setText(mScanpayData.getGoodsMoney());
							mGoodsGetMoneyName.setText(mScanpayData
									.getGoodsGetMoneyName());

						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {
					ToastUtil.showShort(activity, dataInf.msg);
				}
			} else {
				Toast.makeText(activity,
						getResources().getString(string.http_error_anomalies),
						Toast.LENGTH_LONG).show();

			}
			super.onPostExecute(result);
		}

		@Override
		protected void onCancelled() {
			mQueryProductInfo= null;
			super.onCancelled();
		}
	}

	
protected String mBankProtocolNo;
	/**
	 * 查询已添加银行卡
	 * @author kevin
	 *
	 */
	private class GetCardInfTask extends AsyncTask<String, Void, Boolean> {
//		Result dataInf;
		Result dataCardInf;
		Exception exception;

		@Override
		protected Boolean doInBackground(String... params) {
			mCardDataList.clear();
			try {
//				Map<String, Object> param = new HashMap<String, Object>();
//				param.put("s_gatewayjnl_no", params[0]); // 二维码支付流水号
//				// dataInf.jsoBnodyObject = new
//				// JSONObject("{\"s_goods_name\":\"脉动\",\"s_goods_desc\":\"550ml清热解毒 止暑爽口\",\"s_amount\":\"450\"}");
//				dataInf = ApiClient.getScanPayInformation(
//						ScanPayMoneyActivity.this, param);
				String _LoginName = SPUtils.getString(activity,
						Constant_data.sp_key_loginName, "");
				Map<String, Object> param1 = new HashMap<String, Object>();
				param1.put("customerNo", mApplication.getUserBean()
						.getmUserNO()); // 用户编号
				param1.put("sysNo", "1"); // 请求系统编码
				param1.put("loginName", _LoginName);// 用户登录名
				param1.put("status", "1"); // 1查询正在签约和已经签约的银行卡 2查询成功绑定的银行卡
											// 3查询默认的银行卡
				// dataCardInf.jsonBodyArray = new
				// JSONArray("[{\"customerNo\":\"111111111111111\",\"cardTypeCode\":\"中国工商银行储蓄卡\",\"bankCardNo\":\"6215114284123621\",\"expandAttribute\":\"18530125462\"},{“customerNo\":\"22222222222222\",\"cardTypeCode\":\"中国工商银行信用卡\",\"bankCardNo\":\"62151142854512\",\"expandAttribute\":\"18541453261\"},{“customerNo\":\"3333333333333\",\"cardTypeCode\":\"中国招商银行信用卡\",\"bankCardNo\":\"621511428798542\",\"expandAttribute\":\"13895263452\"}]");
				dataCardInf = ApiClient.getIsSuccessBankCard(
						activity, param1);

//				mScanpayData = ScanPay.fromJson(dataInf.jsonBodyObject);
				

			} catch (Exception e) {
				e.printStackTrace();
				exception = e;
			}
			return dataCardInf != null;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			isGetCardSuccess = true;
			if(isGetCardSuccess && isQueryProduct){
				CustomProgressDialog.hideProgressDialog();
			}
			if (exception != null) {
				Toast.makeText(activity,
						getResources().getString(string.http_error_anomalies),
						Toast.LENGTH_LONG).show();
				return;
			}
			if (result) {
				if (Constant_data.HTTP_SUCCESS.equals(dataCardInf.coder)) {
					try {
						mCardDataList = ScanPay
								.fromJsonToCardList(dataCardInf.jsonBodyArray);
						if (mCardDataList != null && !mCardDataList.isEmpty()) {
							initMorePopwindow();
						}
						if (mCardDataList.size() > 0) {
							bankPosition = 0;
							mBankCardNumber.setText(mCardDataList.get(0)
									.getBankName());
							mBankProtocolNo = mCardDataList.get(0).getProtocolNo();
						}
						
						initMorePopwindow();
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {
					ToastUtil.showShort(activity, dataCardInf.msg);
				}
			} else {
				Toast.makeText(activity,
						getResources().getString(string.http_error_anomalies),
						Toast.LENGTH_LONG).show();

			}
			super.onPostExecute(result);
		}

		@Override
		protected void onCancelled() {
			mGetCardInfTask = null;
			super.onCancelled();
		}
	}

	private class PayMoneyTask extends AsyncTask<String, Void, Boolean> {
		Result resuleInf;
		Exception exception;

		@Override
		protected Boolean doInBackground(String... params) {
			try {
				String _LoginNameType = SPUtils.getString(activity, Constant_data.sp_key_userNameType, "1");
				String _LoginName = SPUtils.getString(activity, Constant_data.sp_key_loginName, "");
				Map<String, Object> param = new HashMap<String, Object>();

//				网银网关流水号	a	24	s_gatewayjnl_no	网银网关流水号
				param.put("s_gatewayjnl_no", mScanInformation); // 网银网关流水号
//				用户登录名	a	32	s_login_name	用户登录名
				param.put("s_login_name", _LoginName); // 用户登录名
//				用户登录方式	n	1	l_login_type	1:手机号登录 2:邮箱登录3:昵称登录
				param.put("l_login_type", _LoginNameType);
//				用户PIN	a	32	s_zpk_pin	经过安全控件加密的PIN（安全控件未到位前使用明文传输）
				param.put("s_zpk_pin", params[0]); // 密码
//				银行协议号确认方式	n	1	l_agrno_confirm_type	1:银行协议号，直接根据银行协议号确认（这里使用这个）  2:银行卡号/账号，根据银行卡号或账号确认银行绑定协议号 9:默认绑定协议，根据默认银行绑定信息确认银行确认
				param.put("l_agrno_confirm_type", "1"); // 银行协议号确认方式
//				银行协议号	a	30	s_kpay_agrno	当银行协议确认方式是1时必填，其他无意义
				param.put("s_kpay_agrno", mBankProtocolNo); // 银行协议号
//				银行卡号	a	32	s_bank_acct_no	当银行协议确认方式是2时必填，其他无意义
				param.put("s_bank_acct_no", ""); 
//				合作银行号	a	16	s_cobank_code	当银行协议确认方式是2时必填，其他无意义
				param.put("s_cobank_code", ""); 
				
				

				resuleInf = ApiClient
						.payMoney(activity, param);

				// resuleInf ="{\"status\":\"err\",\"info\":\"支付密码错误\"}";
				// resuleInf ="{\"status\":\"ok\"}";
			} catch (Exception e) {
				e.printStackTrace();
				exception = e;
			}
			return resuleInf != null;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			CustomProgressDialog.hideProgressDialog();
			if (exception != null) {
//				if(mPayListener!=null){
//					mPayListener.FPayBackListener("false",getResources().getString(string.http_error_anomalies),Constant_data.PAY_FILE);
//				}
				ToastUtil.showLong(activity, getResources().getString(string.http_error_anomalies));
				return;
			}
			if (result) {
				if (Constant_data.HTTP_SUCCESS.equals(resuleInf.coder)) {
					if(mPayListener!=null){
						mPayListener.FPayBackListener("true","支付成功！",Constant_data.PAY_SUCCESS);
					}
				} else {
					ToastUtil.showLong(activity, resuleInf.msg);
				}
			} else {
				ToastUtil.showLong(activity, getResources().getString(string.http_error_anomalies));
			}
			super.onPostExecute(result);
		}

		@Override
		protected void onCancelled() {
			mPayMoneyTask = null;
			super.onCancelled();
		}
	}

	private void initMorePopwindow() {
		ListView listview = null;
		LayoutInflater inflater = LayoutInflater.from(activity);
		View popupView = inflater.inflate(R.layout.scan_popwindow_layout, null);

		listview = (ListView) popupView
				.findViewById(R.id.scan_choicecard_pop_listview);
		popupView.findViewById(R.id.scan_listview_addcard_button)
				.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						bankPosition = -1;
//						startActivity(new Intent(
//								activity,
//								AddCardOneActivity.class));
						if(mPayListener!=null){
							mPayListener.FPayAddCardListener();
						}
						 payAddCard = true;
						popupWindow.dismiss();
					}
				});

		popupWindow = new PopupWindow(popupView, screeWidth,
				LayoutParams.WRAP_CONTENT);
		popupWindow.setFocusable(true);
		popupWindow.setOutsideTouchable(true);
		popupWindow.setBackgroundDrawable(new ColorDrawable());

		popupWindow.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss() {
				popWindowIsShow = false;
				mJiantou.setBackgroundResource(R.drawable.jiantou_down);
			}
		});
		listview.setDividerHeight(0);
		listview.setAdapter(new DialogAdapter(activity, mCardDataList));
		listview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				mBankCardNumber.setText(mCardDataList.get(arg2).getBankName());
				mBankProtocolNo = mCardDataList.get(arg2).getProtocolNo();
				bankPosition = arg2;
				popupWindow.dismiss();
			}
		});
		if (mCardDataList == null || mCardDataList.isEmpty()) {
			mBankCardNumber.setText("添加银行卡");
		}
	}

	

	private void showInputPasswordDialog() {
		final Dialog mDialog = new Dialog(activity, R.style.passwordDilog);
		mDialog.setContentView(R.layout.scan_input_password_layout);
		Button sure = (Button) mDialog
				.findViewById(R.id.other_dialog_listview_sure);
		Button back = (Button) mDialog
				.findViewById(R.id.other_dialog_listview_back);
		TextView phoneView = (TextView) mDialog
				.findViewById(R.id.input_password_phone);
		EditText money = (EditText) mDialog
				.findViewById(R.id.input_password_goods_money);
		_LEDT_Password = (PassGuardEdit) mDialog
				.findViewById(R.id.input_password_input);
		Tools.initPassGuard(_LEDT_Password,null);
		mDialog.setCanceledOnTouchOutside(false);
		if (bankPosition != -1) {
			// String phoneStr =
			// mCardDataList.get(bankPosition).getPhoneNumber();
			String phoneStr = mApplication.getUserBean().getmMoblePhoneNo();
			String phonePass = phoneStr.substring(0, 3)
					+ "****"
					+ phoneStr.substring(phoneStr.length() - 4,
							phoneStr.length());
			phoneView.setText(phonePass);
			money.setText(mScanpayData.getGoodsMoney().replace("¥", "") + "元");
		}

		sure.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String passStr = _LEDT_Password.getOutput2();
				if (StringUtils.isEmpty(passStr)) {
					ToastUtil.showLong(activity, "密码不能为空");
					return;
				}
				CustomProgressDialog.showProgressDialog(activity, "正在支付...");
				mPayMoneyTask = new PayMoneyTask();
				mPayMoneyTask.execute(passStr);
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
		mDialog.show();
	}

	private class DialogAdapter extends BaseAdapter {
		private ArrayList<ScanPay> dataList;
		private Context context;
		private LayoutInflater inflater;
		private viewHolder mHodler;

		public DialogAdapter(Context con, ArrayList<ScanPay> list) {
			this.context = con;
			this.dataList = list;
			inflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			return dataList.size();
		}

		@Override
		public Object getItem(int position) {
			return dataList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ScanPay data = dataList.get(position);

			if (convertView == null) {
				convertView = inflater.inflate(
						R.layout.scan_choicecard_pop_listview_item, null);
				mHodler = new viewHolder();
				mHodler.bankImage = (ImageView) convertView
						.findViewById(R.id.scan_cardbank_imageview_item);
				mHodler.cardNumber = (TextView) convertView
						.findViewById(R.id.scan_cardbank_number_item);
				convertView.setTag(mHodler);

			} else {
				mHodler = (viewHolder) convertView.getTag();
			}

			// mHodler.bankImage.seti
			mHodler.cardNumber.setText(data.getBankName());

			return convertView;
		}

		class viewHolder {
			ImageView bankImage;
			TextView cardNumber;

		}
	}
}
