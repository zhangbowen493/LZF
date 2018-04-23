package com.wanda.pay.activity;

import java.util.Map;

import com.wanda.pay.R;
import com.wanda.pay.app.WDApplication;
import com.wanda.pay.bean.Constant_data;
import com.wanda.pay.fragment.AddCardOneFragment;
import com.wanda.pay.fragment.AddCardOneFragment.FAddCardOneBtnClickListener;
import com.wanda.pay.fragment.AddCardTwoFragment;
import com.wanda.pay.fragment.AddCardTwoFragment.FAddCardTwoBtnClickListener;
import com.wanda.pay.fragment.LoginFragment;
import com.wanda.pay.fragment.ScanPayMoneyFragment;
import com.wanda.pay.fragment.LoginFragment.FLoginBtnClickListener;
import com.wanda.pay.fragment.ScanPayMoneyFragment.FPayListener;
import com.wanda.pay.util.LogUtil;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentManager.OnBackStackChangedListener;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

/**
 * Fragment从Activity获取数据 MyFragment1 fragment1 = new MyFragment1(); Bundle
 * bundle1 = new Bundle(); bundle1.putString("id", "Activity发送给MyFragment1的数据");
 * fragment1.setArguments(bundle1); transaction.replace(R.id.left, fragment1,
 * "left");
 * 
 * @author Administrator
 * 
 */
public class wanDa_Pay_Activity extends FragmentActivity implements OnClickListener {
	private FragmentTransaction beginTransaction;
	//当前显示页面 0 登陆 1 支付 2 添加银行卡一 3添加银行卡二
	private int showingUI = 0;
	
	public void setShowingUI(int showingUI) {
		this.showingUI = showingUI;
	}

	// 订单url
	private String mCode_URL;
	// 登陆令牌
	private String mLogin_Key;
	// 登录页面
	private Fragment loginFragment;
	// 支付页面
	private Fragment mScanPayMoneyFragment;
	//添加银行卡一页面
	private Fragment addCardOneFragment;
	//添加银行卡二页面
	private AddCardTwoFragment addCardTwoFragment;
	
	FLoginBtnClickListener loginBtnClickListener = new FLoginBtnClickListener() {
		@Override
		public void onFLoginBackClick(boolean b) {
			// TODO Auto-generated method stub 返回

		}
		@Override
		public void onFLoginToPayClick(String... prams) {
			// TODO Auto-generated method stub
			loadingScanPayFragment();
		}
	};
	FPayListener payListener = new FPayListener() {
		@Override
		public void FPayFinish() {
			// TODO Auto-generated method stub
			showMyDialog();
		}
		@Override
		public void FPayBackListener(String status, String msg, int code) {
			// TODO Auto-generated method stub
			LogUtil.i("LIANPAY", "status = " + status + " msg = " + msg
					+ " code =" + code);

			Intent intent = new Intent();
			if ("true".equals(status)) {
				intent.putExtra("payStatus", true);
			} else {
				intent.putExtra("payStatus", false);
			}
			intent.putExtra("payMsg", msg);
			intent.putExtra("result_code", code);
			setResult(RESULT_OK, intent);
			wanDa_Pay_Activity.this.finish();
		}
		@Override
		public void FPayAddCardListener() {
			// TODO Auto-generated method stub 加载添加银行卡
			
			loadAddCardOne();
		}
		
	};
	FAddCardOneBtnClickListener addCardOneBtnClickListener = new FAddCardOneBtnClickListener() {
		@Override
		public void onFAddCardOneBackClick(boolean b) {
			// TODO Auto-generated method stub
			wanDa_Pay_Activity.this.onBackPressed();
			beginTransaction= fragmentManager.beginTransaction();
			beginTransaction.remove(addCardOneFragment);
			beginTransaction.commit();
			addCardOneFragment = null;
		}
		@Override
		public void onFAddCardOneToTwoClick(Map<String, String> map) {
			// TODO Auto-generated method stub
			loadAddCardTwo(map);
		}
	};
	
	FAddCardTwoBtnClickListener addCardTwoBtnClickListener = new FAddCardTwoBtnClickListener() {
		
		@Override
		public void onFAddCardTwoSuccess(String... prams) {
			// TODO Auto-generated method stub
			beginTransaction= fragmentManager.beginTransaction();
			beginTransaction.remove(addCardOneFragment);
			beginTransaction.remove(addCardTwoFragment);
			beginTransaction.replace(R.id.act_fragment_wanda_pay,
				mScanPayMoneyFragment, "scan_pay");
			beginTransaction.commit();
		}
		
		@Override
		public void onFAddCardTwoBackClick(boolean b) {
			// TODO Auto-generated method stub
			wanDa_Pay_Activity.this.onBackPressed();
			beginTransaction= fragmentManager.beginTransaction();
			beginTransaction.remove(addCardTwoFragment);
			beginTransaction.commit();
			addCardTwoFragment = null;
		}
	};
	private FragmentManager fragmentManager;
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_wanda_pay);
		WDApplication.getInstance().addActivity(this);
		fragmentManager = getSupportFragmentManager();
		
		initData();
		
	}

	

	private void initData() {
		mLogin_Key = getIntent().getStringExtra("session_token");
		mCode_URL = getIntent().getStringExtra("pay_code_url");
		
		
		loginFragment = new LoginFragment();
		
		mScanPayMoneyFragment = new ScanPayMoneyFragment();
		addCardOneFragment = new AddCardOneFragment();
		addCardTwoFragment = new AddCardTwoFragment();

		loadLogin();

	}

	/**
	 * 加载登陆页面
	 */
	private void loadLogin() {
		if(loginFragment==null){
			loginFragment = new LoginFragment();
		}
		Bundle bundle1 = new Bundle();
		bundle1.putString("login_key", mLogin_Key);
		loginFragment.setArguments(bundle1);

		((LoginFragment) loginFragment).setmLoginBtnClickListener(loginBtnClickListener);
		beginTransaction = fragmentManager.beginTransaction();
		// 添加Fragment
		beginTransaction.add(R.id.act_fragment_wanda_pay, loginFragment);
		// 提交事务
		beginTransaction.commit();
	}

	

	/**
	 * 加载支付页面
	 */
	private void loadingScanPayFragment() {
		if(mScanPayMoneyFragment==null){
			mScanPayMoneyFragment = new ScanPayMoneyFragment();
		}
		
		Bundle bundle1 = new Bundle();
		bundle1.putString("code_url", mCode_URL);
		mScanPayMoneyFragment.setArguments(bundle1);
		((ScanPayMoneyFragment) mScanPayMoneyFragment).setmPayListener(payListener);
		beginTransaction = fragmentManager.beginTransaction();
		// 添加Fragment
		beginTransaction.replace(R.id.act_fragment_wanda_pay,
				mScanPayMoneyFragment, "scan_pay");
		// 提交事务
		beginTransaction.commit();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}
	//加载添加银行卡第一页
	private void loadAddCardOne() {
		if(addCardOneFragment==null){
			addCardOneFragment = new AddCardOneFragment();
		}
		
		((AddCardOneFragment) addCardOneFragment)
				.setmLoginBtnClickListener(addCardOneBtnClickListener);
		beginTransaction = fragmentManager.beginTransaction();
		beginTransaction.addToBackStack("scan_pay");
		// 添加Fragment
		beginTransaction.replace(R.id.act_fragment_wanda_pay,
				addCardOneFragment, "addone");
		// 提交事务
		beginTransaction.commit();
	}
	/**
	 * 加载添加银行卡二界面
	 * @param map
	 */
	protected void loadAddCardTwo(Map<String, String> map) {
		// TODO Auto-generated method stub
		if(addCardTwoFragment==null){
			addCardTwoFragment = new AddCardTwoFragment();
		}
		Bundle bundle1 = new Bundle();
		bundle1.putString("http_reqcode", map.get("http_reqcode"));
		bundle1.putString("name", map.get("name"));
		bundle1.putString("id_card", map.get("id_card"));
		bundle1.putString("bank_code", map.get("bank_code"));
		bundle1.putString("bank_name", map.get("bank_name"));
		bundle1.putString("check_phone", map.get("check_phone"));
		bundle1.putString("cardNO", map.get("cardNO"));
		bundle1.putString("card_type", map.get("card_type"));
		bundle1.putString("sysNO", map.get("sysNO"));
		bundle1.putString("protocolNo", map.get("protocolNo"));
		bundle1.putString("redirectBankURL", map.get("redirectBankURL"));
		bundle1.putString("bankDesc", map.get("bankDesc"));
		bundle1.putString("bankType", map.get("bankType"));
		
		addCardTwoFragment.setArguments(bundle1);
		addCardTwoFragment.setmAddTwoBtnClickListener(addCardTwoBtnClickListener);
		beginTransaction = fragmentManager.beginTransaction();
		beginTransaction.show(addCardTwoFragment);
		beginTransaction.addToBackStack("addone");
		// 添加Fragment
		beginTransaction.add(R.id.act_fragment_wanda_pay,
						addCardTwoFragment, "addtwo");
		// 提交事务
		beginTransaction.commit();
		
	
	}
	

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		// 返回键
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if(showingUI == 0 || showingUI == 1){
				showMyDialog();
			}else if(showingUI==2 ){
				showingUI = 1;
				beginTransaction= fragmentManager.beginTransaction();
				beginTransaction.remove(addCardOneFragment);
				beginTransaction.commit();
				addCardOneFragment = null;
			}else if(showingUI==3){
				showingUI = 2;
				beginTransaction= fragmentManager.beginTransaction();
				beginTransaction.remove(addCardTwoFragment);
				beginTransaction.commit();
				addCardTwoFragment = null;
			}
			
		}
		return super.onKeyDown(keyCode, event);
	}
	

	private void showMyDialog() {
		final Dialog mDialog = new Dialog(wanDa_Pay_Activity.this,
				R.style.myDilog);
		mDialog.setContentView(R.layout.dialog_layout);
		Button sure = (Button) mDialog.findViewById(R.id.dialog_btn_ok);
		Button back = (Button) mDialog.findViewById(R.id.dialog_btn_cancel);
		TextView _DialogTV = (TextView) mDialog
				.findViewById(R.id.dialog_tv_msg);
		_DialogTV.setText("您确定要放弃本次支付？");
		mDialog.setCanceledOnTouchOutside(false);
		sure.setText("放弃");
		sure.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				Intent data = new Intent();
				data.putExtra("payStatus", false);
				data.putExtra("payMsg", "用户放弃支付！");
				data.putExtra("result_code", Constant_data.PAY_CANCEL);

				setResult(RESULT_OK, data);
				wanDa_Pay_Activity.this.finish();

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

}
