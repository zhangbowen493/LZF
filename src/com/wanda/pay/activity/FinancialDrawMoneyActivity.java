package com.wanda.pay.activity;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;

import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
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
import com.wanda.pay.util.ImmersedStatusbarUtils;
import com.wanda.pay.util.LogUtil;
import com.wanda.pay.util.Result;
import com.wanda.pay.util.SPUtils;
import com.wanda.pay.util.ToastUtil;
import com.wanda.pay.util.Tools;

/*提现
 *@作者: Administrator
 *@版本: 
 *@version create time：2016-1-28 下午3:49:14
 */
public class FinancialDrawMoneyActivity extends BaseActivity implements
OnClickListener {

	private FinancialDrawMoneyActivity mContext;
	private WDApplication mApplication;
	private EditText fdmEDT_Money;
	private PassGuardEdit fdmEDT_PayPassword;
	private TextView fdmTV_BankCard;
	private ArrayList<ScanPay> mCardDataList = new ArrayList<ScanPay>();
	private int bankPosition;
	protected boolean popWindowIsShow;

	private int mCommitMoney = 0;
	protected String mBankProtocolNo;
	private GetCardInfTask mGetCardInfTask;
	private String mMoney;
	private String mPayWD;
	private DrawMoneyTask mDrawMoneyTask;
	private String lognname;
	private int number;
	private TextView fdmTV_BankWeiHao;
	private int list_position=0;
	private TextView tv_balance;
	private DecimalFormat decimalFormat;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.act_financial_draw_money);
		ImmersedStatusbarUtils.initAfterSetContentView(this, null);
		WDApplication.getInstance().addActivity(this);
		mContext = this;
		mApplication = WDApplication.getInstance();
		decimalFormat = new DecimalFormat("0.00");
		initView();
		initData();
	}

	public void initView() {
		// TODO Auto-generated method stub
		fdmEDT_Money = (EditText) findViewById(R.id.edt_act_fdm_money);
		fdmEDT_PayPassword = (PassGuardEdit) findViewById(R.id.edt_act_fdm_pay_password);
		fdmTV_BankCard = (TextView) findViewById(R.id.tv_act_ftu_bankcard_number);
		fdmTV_BankWeiHao=(TextView) findViewById(R.id.tv_act_weihao_number);
		tv_balance=(TextView) findViewById(R.id.tv_balance);

		TextView _BCTV_Title = (TextView) findViewById(R.id.activity_title_tv_name);
		_BCTV_Title.setText("余额提现");

		findViewById(R.id.activity_title_btn_back).setOnClickListener(this);
		findViewById(R.id.btn_act_fdm_commit).setOnClickListener(this);
		findViewById(R.id.img_act_ftu_card_jiantou).setOnClickListener(this);
		findViewById(R.id.tv_find_pwd).setOnClickListener(this);

		fdmTV_BankCard.setOnClickListener(this);

		Tools.initPassGuard(fdmEDT_PayPassword, "^.{6,}$");

		fdmEDT_Money.addTextChangedListener(moneyWatcher);
		fdmEDT_Money.setText("0.00");
	}

	private void initData() {
		mBlance = mApplication.getUserBean().getmBlance();
		getCardList();
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (mApplication.isAddSuccess)
			getCardList();
		
		tv_balance.setText("该卡本次最多可转出"+decimalFormat.format(WDApplication.getInstance().getUserBean().getmBlance())+"元");
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.activity_title_btn_back:
			MyBack();
			break;
		case R.id.btn_act_fdm_commit:
			DrawMoneyCommit();
			break;
		case R.id.img_act_ftu_card_jiantou:
			// 选择银行卡
			SelcetBankCard();
			break;
		case R.id.tv_act_ftu_bankcard_number:
			// 选择银行卡
			SelcetBankCard();
			break;
		case R.id.tv_find_pwd:
			// 忘记密码？
			Intent intent = new Intent(mContext, FindPasswordActivity.class);
			intent.putExtra("GoType", 2);
			String _LoginName = SPUtils.getString(mContext, Constant_data.sp_key_loginName, "");
			intent.putExtra("accounts", _LoginName);

			startActivity(intent);
			overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
			FinancialDrawMoneyActivity.this.finish();

			break;
		default:
			break;
		}
	}



	private void SelcetBankCard() {
		// TODO Auto-generated method stub
		if (mCardDataList != null && !mCardDataList.isEmpty()) {
			if (mCardDataList.size() == 0 || bankPosition == -1) {
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
		//Intent intent = new Intent(mContext, SelectBankCardActivity.class);
		Intent intent = new Intent(mContext, SelectBankPopupWindow.class);
		intent.putExtra("cardList", mCardDataList);
		startActivityForResult(intent, 900);
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);

		if(requestCode==900 && resultCode == 999){
			list_position = data.getIntExtra("number", 0);
			String str=mCardDataList.get(list_position).getBankName();
			String bk_name= str.substring(0, str.indexOf("("));
			fdmTV_BankCard.setText(bk_name);
			initweihao();
			mBankProtocolNo = mCardDataList.get(list_position).getProtocolNo();
			bankPosition = list_position;
		}

	}

	/**
	 * 后退接口
	 */
	private void MyBack() {
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
	protected void onDestroy() {
		// TODO Auto-generated method stub
		if(mGetCardInfTask!=null)
			mGetCardInfTask.cancel(true);
		if(mDrawMoneyTask!=null)
			mDrawMoneyTask.cancel(true);
		super.onDestroy();
	}

	/**
	 * 提交提现申请
	 */
	private void DrawMoneyCommit() {
		// TODO Auto-generated method stub
		mMoney = fdmEDT_Money.getText().toString();
		mPayWD = fdmEDT_PayPassword.getOutput2();

		if(bankPosition==-1){
			showAddCardDialog();
			return;
		}

		if(!fdmEDT_PayPassword.checkMatch()){
			ToastUtil.showLong(mContext, "请输入正确的支付密码！");
			return;
		}
		if(Tools.isNumeric(mMoney)){
			mCommitMoney = (int) (Double.parseDouble(mMoney)*100);
		}else{
			ToastUtil.showLong(mContext, "请输入正确金额！");
			return;
		}
		if(mCommitMoney<1){
			ToastUtil.showLong(mContext, "请输入正确金额！");
			return;
		}
		if(mCommitMoney>(mBlance*100)){
			ToastUtil.showLong(mContext, "账户余额不足！");
			return;
		}


		mDrawMoneyTask = new DrawMoneyTask();
		mDrawMoneyTask.execute();
		CustomProgressDialog.showProgressDialog(mContext, "loading",
				mDrawMoneyTask);
	}

	/**
	 * 提现
	 * @author Administrator
	 * 
	 * 支付系统用户号	a	16	userNo	当用户身份识别标志为2时必填，其他无意义
提现总金额	n	19	carryAmount	提出总金额，到账金额+手续费。单位：分
支付密码校验标识	n	1	pawdSign	0：不校验支付密码。1：需校验支付密码。
支付密码	a	32	pawd	支付密码的密文（HEX字符串描述）
银行协议号		30	protectNo	当银行协议确认方式是1时必填，其他无意义
	 *
	 */
	private class DrawMoneyTask extends AsyncTask<String, Void, Boolean>{

		Result TopUp;
		Exception exception;
		@Override
		protected Boolean doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			Map<String, Object> param1 = new HashMap<String, Object>();
			param1.put("userNo", mApplication.getUserBean().getmUserNO());//用户号
			param1.put("carryAmount", ""+mCommitMoney); // 提现总金额
			param1.put("pawdSign", "1"); //支付密码校验标识
			param1.put("protectNo", mBankProtocolNo); // 银行协议号
			param1.put("pawd", mPayWD); // 支付密码
			try {
				TopUp = ApiClient.DrawMoney(mContext, param1);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return TopUp != null;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			// TODO Auto-generated method stub

			CustomProgressDialog.hideProgressDialog();
			if (exception != null) {
				Toast.makeText(getApplicationContext(),
						getResources().getString(string.http_error_anomalies),
						Toast.LENGTH_LONG).show();
				return;
			}
			if(result){
				if(Constant_data.HTTP_SUCCESS.equals(TopUp.coder)){
					lognname = SPUtils.getString(mContext,
							Constant_data.sp_key_loginName, "");
					Intent intent = new Intent(mContext, DisplayingResultsActivity1.class);
					intent.putExtra("fromwhere", "FinancialDrawMoneyActivity");
					//					intent.putExtra("showinfo", "链支付提现申请提交成功！");
					intent.putExtra("carryAmount",mMoney);
					intent.putExtra("loginName", lognname);
					intent.putExtra("number",mCardDataList.get(number).getBankName());
					startActivity(intent);
					mApplication.isRefreshUserInfo = true;
					FinancialDrawMoneyActivity.this.finish();
				}else{
					String strError = TopUp.msg;
					ToastUtil.showShort(mContext, strError);
				}
			}else{
				ToastUtil.showShort(mContext, getResources().getString(string.http_error_anomalies));
			}
			super.onPostExecute(result);
		}
		@Override
		protected void onCancelled() {
			// TODO Auto-generated method stub
			mDrawMoneyTask = null;
			super.onCancelled();
		}
	}


	/**
	 * 获取已签约银行卡列表
	 */
	private void getCardList() {
		// TODO Auto-generated method stub
		mGetCardInfTask = new GetCardInfTask();
		mGetCardInfTask.execute();
		CustomProgressDialog.showProgressDialog(mContext, "loading",
				mGetCardInfTask);
	}

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
				dataCardInf = ApiClient.getIsSuccessBankCard(mContext, param1);

			} catch (Exception e) {
				e.printStackTrace();
				exception = e;
			}
			return dataCardInf != null;
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
				if (Constant_data.HTTP_SUCCESS.equals(dataCardInf.coder)) {
					try {
						mCardDataList = ScanPay
								.fromJsonToCardList(dataCardInf.jsonBodyArray);
						if (mCardDataList != null && mCardDataList.size() > 0) {
							bankPosition = 0;
							findViewById(R.id.Image_src).setVisibility(View.VISIBLE);
							findViewById(R.id.tv_act_ftu_bankcard_number).setVisibility(View.VISIBLE);
							findViewById(R.id.tv_act_weihao_number).setVisibility(View.VISIBLE);
							String str=mCardDataList.get(0).getBankName();
							String bk_name= str.substring(0, str.indexOf("("));
							fdmTV_BankCard.setText(bk_name);
							initweihao();
							mBankProtocolNo = mCardDataList.get(0)
									.getProtocolNo();
						} else {
							bankPosition = -1;
							findViewById(R.id.Image_src).setVisibility(View.GONE);
							findViewById(R.id.tv_act_weihao_number).setVisibility(View.GONE);
							fdmTV_BankCard.setText("添加银行卡");
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {
					ToastUtil.showShort(mContext, dataCardInf.msg);
				}
			} else {
				Toast.makeText(getApplicationContext(),
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
	private void initweihao() {
		// TODO Auto-generated method stub
		String res="";
		String weihao=mCardDataList.get(list_position).getBankName();
		res=test(weihao);
	}
	
	private String test(String res) {
		// TODO Auto-generated method stub
		Pattern p;
		p = Pattern.compile("\\d{4}");//在这里，编译 成一个正则。
		Matcher m;
		m = p.matcher(res);//获得匹配
		res = "";
		
		while(m.find()){ //注意这里，是while不是if
			String wei = m.group();
			fdmTV_BankWeiHao.setText("尾号  "+wei);
		}
		return res;
	}

	TextWatcher moneyWatcher = new TextWatcher() {
		private boolean isChanged = false;

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
		}

		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub
			if (isChanged) {// ----->如果字符未改变则返回
				return;
			}
			String str = s.toString();

			isChanged = true;
			String cuttedStr = str;
			/* 删除字符串中的dot */
			for (int i = str.length() - 1; i >= 0; i--) {
				char c = str.charAt(i);
				if ('.' == c) {
					cuttedStr = str.substring(0, i) + str.substring(i + 1);
					break;
				}
			}
			/* 删除前面多余的0 */
			int NUM = cuttedStr.length();
			int zeroIndex = -1;
			for (int i = 0; i < NUM - 2; i++) {
				char c = cuttedStr.charAt(i);
				if (c != '0') {
					zeroIndex = i;
					break;
				} else if (i == NUM - 3) {
					zeroIndex = i;
					break;
				}
			}
			if (zeroIndex != -1) {
				cuttedStr = cuttedStr.substring(zeroIndex);
			}
			/* 不足3位补0 */
			if (cuttedStr.length() < 3) {
				cuttedStr = "0" + cuttedStr;
			}
			/* 加上dot，以显示小数点后两位 */
			cuttedStr = cuttedStr.substring(0, cuttedStr.length() - 2) + "."
					+ cuttedStr.substring(cuttedStr.length() - 2);

			fdmEDT_Money.setText(cuttedStr);

			fdmEDT_Money.setSelection(cuttedStr.length());
			isChanged = false;
		}
	};
	private double mBlance;
}
