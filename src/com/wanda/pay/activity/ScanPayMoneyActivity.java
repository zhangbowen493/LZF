package com.wanda.pay.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;
import android.widget.Toast;
import cn.passguard.PassGuardEdit;

import com.wanda.pay.R;
import com.wanda.pay.R.string;
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

public class ScanPayMoneyActivity extends BaseActivity {

	public static final int AddCardCode = 1;
	public static boolean isNeedBack = false;
	private Context mContext;
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.scan_paymoney_layout);
		WDApplication.getInstance().addActivity(this);
		mContext = this;
		mApplication = WDApplication.getInstance();

		mScanInformation = getIntent().getStringExtra("data");

		findView();
		screeWidth = getWindowManager().getDefaultDisplay().getWidth();

		initMorePopwindow();

		mQueryProductInfo = new QueryProductInfo();
		mQueryProductInfo.execute(mScanInformation);

		mGetCardInfTask = new GetCardInfTask();
		mGetCardInfTask.execute();
		CustomProgressDialog.showProgressDialog(mContext, "loading",
				mGetCardInfTask);
	}

	@Override
	protected void onDestroy() {
		if (mGetCardInfTask != null)
			mGetCardInfTask.cancel(true);
		if (mPayMoneyTask != null)
			mPayMoneyTask.cancel(true);
		super.onDestroy();
		WDApplication.isAddCard = false;
		WDApplication.isAddSuccess = false;
	}

	private void findView() {

		TextView _BCTV_Title = (TextView) findViewById(R.id.activity_title_tv_name);
		_BCTV_Title.setText(getResources().getString(string.uset_scan_pay));
		findViewById(R.id.activity_title_btn_back).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						finish();
					}
				});

		mChoiceBankLayout = (LinearLayout) findViewById(R.id.scan_choice_bank_layout);
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
		mGoodsName = (TextView) findViewById(R.id.scan_goods_name);
		mGoodsMoney = (TextView) findViewById(R.id.scan_goods_money);
		mGoodsGetMoneyName = (TextView) findViewById(R.id.scan_goods_getmoney_name);
		mBankCardNumber = (TextView) findViewById(R.id.scan_cardbank_number);
		mJiantou = (TextView) findViewById(R.id.scan_cardbank_jiantou);

		mGoodImage = (ImageView) findViewById(R.id.scan_goods_imageview);
		mBankCardImage = (ImageView) findViewById(R.id.scan_cardbank_imageview);

		findViewById(R.id.scan_sure_pay_button).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						if (mCardDataList != null && !mCardDataList.isEmpty()) {
							if (mCardDataList.size() == 0 || bankPosition == -1) {
								showAddCardDialog();
							} else {
								if (mScanpayData != null) {
									showInputPasswordDialog();
								} else {
									ToastUtil.showShort(mContext,
											"订单信息获取失败，请重新扫描！");
									ScanPayMoneyActivity.this.finish();
								}
							}
						} else
							showAddCardDialog();
					}
				});
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
	 * 查询二维码中商品信息
	 * 
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
				// dataInf.jsoBnodyObject = new
				// JSONObject("{\"s_goods_name\":\"脉动\",\"s_goods_desc\":\"550ml清热解毒 止暑爽口\",\"s_amount\":\"450\"}");
				dataInf = ApiClient.getScanPayInformation(
						ScanPayMoneyActivity.this, param);

			} catch (Exception e) {
				e.printStackTrace();
				exception = e;
			}
			return dataInf != null;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			// TODO Auto-generated method stub
			if (!isHind) {
				isHind = true;
			} else {
				CustomProgressDialog.hideProgressDialog();
			}
			if (exception != null) {
				Toast.makeText(
						getApplicationContext(),
						getResources().getString(string.http_error_anomalies)
								, Toast.LENGTH_LONG).show();
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
					ToastUtil.showShort(mContext, dataInf.msg);
				}
			} else {
				Toast.makeText(
						getApplicationContext(),
						getResources().getString(string.http_error_anomalies)
								, Toast.LENGTH_LONG).show();

			}
			super.onPostExecute(result);
		}

		@Override
		protected void onCancelled() {
			mQueryProductInfo = null;
			super.onCancelled();
		}
	}

	/** 是否隐藏加载页面 */
	private boolean isHind = false;
	protected String mBankProtocolNo;

	/**
	 * 查询已添加银行卡
	 * 
	 * @author kevin
	 * 
	 */
	private class GetCardInfTask extends AsyncTask<String, Void, Boolean> {
		// Result dataInf;
		Result dataCardInf;
		Exception exception;

		@Override
		protected Boolean doInBackground(String... params) {
			mCardDataList.clear();
			try {

				String _LoginName = SPUtils.getString(mContext,
						Constant_data.sp_key_loginName, "");
				Map<String, Object> param1 = new HashMap<String, Object>();
				param1.put("customerNo", mApplication.getUserBean()
						.getmUserNO()); // 用户编号
				param1.put("sysNo", "1"); // 请求系统编码
				param1.put("loginName", _LoginName);// 用户登录名
				param1.put("status", "1"); // 1查询正在签约和已经签约的银行卡 2查询成功绑定的银行卡
											// 3查询默认的银行卡
				dataCardInf = ApiClient.getIsSuccessBankCard(
						ScanPayMoneyActivity.this, param1);

			} catch (Exception e) {
				e.printStackTrace();
				exception = e;
			}
			return dataCardInf != null;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			if (!isHind) {
				isHind = true;
			} else {
				CustomProgressDialog.hideProgressDialog();
			}
			if (exception != null) {
				Toast.makeText(
						getApplicationContext(),
						getResources().getString(string.http_error_anomalies)
								, Toast.LENGTH_LONG).show();
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
							mBankProtocolNo = mCardDataList.get(0)
									.getProtocolNo();
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {
					ToastUtil.showShort(mContext, dataCardInf.msg);
				}
			} else {
				Toast.makeText(
						getApplicationContext(),
						getResources().getString(string.http_error_anomalies)
								, Toast.LENGTH_LONG).show();

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
				String _LoginNameType = SPUtils.getString(mContext,
						Constant_data.sp_key_userNameType, "1");
				String _LoginName = SPUtils.getString(mContext,
						Constant_data.sp_key_loginName, "");
				Map<String, Object> param = new HashMap<String, Object>();

				// 网银网关流水号 a 24 s_gatewayjnl_no 网银网关流水号
				param.put("s_gatewayjnl_no", mScanInformation); // 网银网关流水号
				// 用户登录名 a 32 s_login_name 用户登录名
				param.put("s_login_name", _LoginName); // 用户登录名
				// 用户登录方式 n 1 l_login_type 1:手机号登录 2:邮箱登录3:昵称登录
				param.put("l_login_type", _LoginNameType);
				// 用户PIN a 32 s_zpk_pin 经过安全控件加密的PIN（安全控件未到位前使用明文传输）
				param.put("s_zpk_pin", params[0]); // 密码
				// 银行协议号确认方式 n 1 l_agrno_confirm_type
				// 1:银行协议号，直接根据银行协议号确认（这里使用这个） 2:银行卡号/账号，根据银行卡号或账号确认银行绑定协议号
				// 9:默认绑定协议，根据默认银行绑定信息确认银行确认
				param.put("l_agrno_confirm_type", "1"); // 银行协议号确认方式
				// 银行协议号 a 30 s_kpay_agrno 当银行协议确认方式是1时必填，其他无意义
				param.put("s_kpay_agrno", mBankProtocolNo); // 银行协议号
				// 银行卡号 a 32 s_bank_acct_no 当银行协议确认方式是2时必填，其他无意义
				param.put("s_bank_acct_no", "");
				// 合作银行号 a 16 s_cobank_code 当银行协议确认方式是2时必填，其他无意义
				param.put("s_cobank_code", "");

				resuleInf = ApiClient
						.payMoney(ScanPayMoneyActivity.this, param);

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
				Toast.makeText(
						getApplicationContext(),
						getResources().getString(string.http_error_anomalies)
								, Toast.LENGTH_LONG).show();
				return;
			}
			if (result) {
				if (Constant_data.HTTP_SUCCESS.equals(resuleInf.coder)) {
					ToastUtil.showShort(mContext, "支付成功");
					ScanPayMoneyActivity.this.finish();
				} else {
					ToastUtil.showShort(mContext, resuleInf.msg);
				}
			} else {
				Toast.makeText(
						getApplicationContext(),
						getResources().getString(string.http_error_anomalies)
								, Toast.LENGTH_LONG).show();

			}
			super.onPostExecute(result);
		}

		@Override
		protected void onCancelled() {
			mPayMoneyTask = null;
			super.onCancelled();
		}
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		if (WDApplication.isAddCard && WDApplication.isAddSuccess) {
			CustomProgressDialog.showProgressDialog(mContext, "loading");
			WDApplication.isAddCard = false;
			WDApplication.isAddSuccess = false;
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

	private void initMorePopwindow() {
		ListView listview = null;
		LayoutInflater inflater = LayoutInflater.from(mContext);
		View popupView = inflater.inflate(R.layout.scan_popwindow_layout, null);

		listview = (ListView) popupView
				.findViewById(R.id.scan_choicecard_pop_listview);
		popupView.findViewById(R.id.scan_listview_addcard_button)
				.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						bankPosition = -1;
						startActivity(new Intent(mContext,
								AddCardOneActivity.class));
						WDApplication.isAddCard = true;
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
		listview.setAdapter(new DialogAdapter(mContext, mCardDataList));
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

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == AddCardCode) {
			CustomProgressDialog.showProgressDialog(mContext, "loading");
			mGetCardInfTask = new GetCardInfTask();
			mGetCardInfTask.execute(mScanInformation);
		}
		super.onActivityResult(requestCode, resultCode, data);
	}


	private void showInputPasswordDialog() {
		final Dialog mDialog = new Dialog(mContext, R.style.passwordDilog);
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
		Tools.initPassGuard(_LEDT_Password, null);
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
					ToastUtil.showLong(mContext, "密码不能为空");
					return;
				}
				CustomProgressDialog.showProgressDialog(mContext, "正在支付...");
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
