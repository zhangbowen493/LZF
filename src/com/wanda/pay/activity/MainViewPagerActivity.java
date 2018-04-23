package com.wanda.pay.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.wanda.pay.R;
import com.wanda.pay.R.string;
import com.wanda.pay.app.WDApplication;
import com.wanda.pay.bean.Constant_data;
import com.wanda.pay.bean.UserBean;
import com.wanda.pay.dialog.CustomProgressDialog;
import com.wanda.pay.fragment.Fragment_apply;
import com.wanda.pay.fragment.Fragment_main;
import com.wanda.pay.fragment.Fragment_my;
import com.wanda.pay.fragment.Fragment_server;
import com.wanda.pay.util.ApiClient;
import com.wanda.pay.util.GetUserInfoUtil;
import com.wanda.pay.util.ImmersedStatusbarUtils;
import com.wanda.pay.util.JSONParsing;
import com.wanda.pay.util.Result;
import com.wanda.pay.util.SPUtils;
import com.wanda.pay.util.ToastUtil;
import com.wonders.pay.viewpager.PayViewPager;


public class MainViewPagerActivity extends FragmentActivity {

	private List<Fragment> list;
	private PayViewPager viewpager;
	private Button btn_pay;
	//	private Button btn_server;
	private Button btn_apply;
	private Button btn_my;
	private LoginOutTask loginOutTask;
	Context mContext = this;
	private GetUserInfoUtil getUserInfoTask;
	private UserBean mUserBean;
	private WDApplication application;
	public static MainViewPagerActivity instance=null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ImmersedStatusbarUtils.initBeforeSetContentView(this,null);
		setContentView(R.layout.wonders_activity_home);
		WDApplication.getInstance().addActivity(this);
		if (instance == null) {
			instance = this;
		}
		application = WDApplication.getInstance();
		setViews();
		setListeners();
		setAdapter();
		GetUserInfo();
		btn_pay.setSelected(true);
		WDApplication.getInstance().setActicity(this);
	}

	private void setListeners() {
		MenuButtonOnClickListener listener = new MenuButtonOnClickListener();
		btn_pay.setOnClickListener(listener);
		//		btn_server.setOnClickListener(listener);
		btn_apply.setOnClickListener(listener);
		btn_my.setOnClickListener(listener);
	}

	/**
	 * 获取个人信息
	 */
	private void GetUserInfo() {
		// TODO Auto-generated method stub
		//CustomProgressDialog.showProgressDialog(getActivity(), "loading");
		getUserInfoTask = new GetUserInfoUtil(this);
		getUserInfoTask.execute();
	}

	private void setAdapter() {
		MyPagerAdapter adapter = new MyPagerAdapter(getSupportFragmentManager());
		list = new ArrayList<Fragment>();
		list.add(new Fragment_main());
		list.add(new Fragment_apply());
		list.add(new Fragment_my());
		viewpager.setAdapter(adapter);
	}

	private class MyPagerAdapter extends FragmentPagerAdapter {

		public MyPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			return list.get(position);
		}

		@Override
		public int getCount() {
			return list.size();
		}
	}

	public class MenuButtonOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btn_pay:
				selectMenuItem(btn_pay);
				viewpager.setCurrentItem(0);
				break;
				//			case R.id.btn_server:
				//				selectMenuItem(btn_server);
				//				viewpager.setCurrentItem(1);
				//				break;
			case R.id.btn_apply:
				selectMenuItem(btn_apply);
				viewpager.setCurrentItem(1);
				break;
			case R.id.btn_my:
				selectMenuItem(btn_my);
				viewpager.setCurrentItem(2);
				break;
			}
		}
	}

	private void setViews() {
		viewpager = (PayViewPager) findViewById(R.id.payViewPager);
		btn_pay = (Button) findViewById(R.id.btn_pay);
		//		btn_server = (Button) findViewById(R.id.btn_server);
		btn_apply = (Button) findViewById(R.id.btn_apply);
		btn_my = (Button) findViewById(R.id.btn_my);
	}

	private void selectMenuItem(Button ib) {
		btn_pay.setSelected(false);
		//		btn_server.setSelected(false);
		btn_apply.setSelected(false);
		btn_my.setSelected(false);
		ib.setSelected(true);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK )  
		{ 
			showLogOut();
		}
		return false;
	}
	/**
	 * 提示去登录
	 */
	private void showLogOut() {
		final Dialog mDialog = new Dialog(mContext, R.style.myDilog);
		mDialog.setContentView(R.layout.dialog_layout);
		Button sure = (Button) mDialog.findViewById(R.id.dialog_btn_ok);
		Button back = (Button) mDialog.findViewById(R.id.dialog_btn_cancel);
		TextView _DialogTV = (TextView) mDialog
				.findViewById(R.id.dialog_tv_msg);
		_DialogTV.setText("您将要登出当前帐号？");
		mDialog.setCanceledOnTouchOutside(false);

		sure.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ExtitAPP();
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
	 * APP登出
	 */
	private void ExtitAPP() {
		// TODO Auto-generated method stub
		CustomProgressDialog.showProgressDialog(mContext, "loading");
		loginOutTask = new LoginOutTask();
		loginOutTask.execute();
	}
	/**
	 * 用户登出
	 * 
	 * @author kevin
	 * 
	 */
	private class LoginOutTask extends AsyncTask<String, Void, Boolean> {
		String strError;
		Exception exception;
		private Result loginResult;

		@Override
		protected Boolean doInBackground(String... prams) {
			// TODO Auto-generated method stub
			try {
				UserBean userBean = WDApplication.getInstance().getUserBean();
				if (userBean != null) {
					String _LoginNameType = SPUtils.getString(mContext,
							Constant_data.sp_key_userNameType, "1");
					String _LoginName = SPUtils.getString(mContext,
							Constant_data.sp_key_loginName, "");

					Map<String, Object> pramsMap = new HashMap<String, Object>();

					pramsMap.put("operationType", "1");
					pramsMap.put("loginType", _LoginNameType);
					pramsMap.put("isLogin", "0");
					pramsMap.put("loginName", _LoginName);

					loginResult = ApiClient.Login(mContext, pramsMap);

				}

			} catch (Exception e) {
				e.printStackTrace();
				exception = e;
			}
			return loginResult != null;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			CustomProgressDialog.hideProgressDialog();
			if (exception != null) {
				Toast.makeText(mContext,
						getResources().getString(string.http_error_anomalies),
						Toast.LENGTH_LONG).show();
				return;
			}
			if (result) {
				if (Constant_data.HTTP_SUCCESS.equals(loginResult.coder)) {

					WDApplication.getInstance().setUserBean(null);
					SPUtils.clear(mContext);
					Intent intent = new Intent(mContext, LoginActivity.class);
					intent.putExtra("isExit", true);
					startActivity(intent);
					MainViewPagerActivity.this.finish();
				} else {
					strError = loginResult.msg;
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
			loginOutTask = null;
			super.onCancelled();
		}

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if(getUserInfoTask!=null){
			getUserInfoTask.cancel(true);
		}
	}
}
