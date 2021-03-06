package com.wanda.pay.activity;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.umeng.analytics.AnalyticsConfig;
import com.umeng.analytics.MobclickAgent;
import com.wanda.pay.R;
import com.wanda.pay.app.WDApplication;
import com.wanda.pay.bean.Constant_data;
import com.wanda.pay.bean.HandlerConstant;
import com.wanda.pay.util.AppUtils;
import com.wanda.pay.util.ImmersedStatusbarUtils;
import com.wanda.pay.util.SPUtils;
import com.wanda.pay.util.URLs;

public class MainActivity extends BaseActivity {

	Handler MHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case HandlerConstant.MainJump:

				String _SP_Version = (String) SPUtils.get(mContext, Constant_data.Version_name, "0.0");
				if(versionName.equals(_SP_Version)){
					Intent intent = new Intent(mContext, LoginActivity.class);
					startActivity(intent);
				}else{
					SPUtils.put(mContext, Constant_data.Version_name, versionName);
					Intent intent = new Intent(mContext, LoginActivity.class);
					startActivity(intent);
				}
				MainActivity.this.finish();
				break;

			default:
				break;
			}
		};
	};

	Context mContext = this;

	private TextView _MTV_Versiong;

	private String versionName;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_NO_TITLE);  
		ImmersedStatusbarUtils.initBeforeSetContentView(this,null);
		setContentView(R.layout.activity_main);
		WDApplication.getInstance().addActivity(this);
		//		if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.KITKAT){
		//
		//			Window window=getWindow();
		//			window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		//			//透明状态栏
		//			getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		//			//透明导航栏
		//			//	       getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
		//		}
		init();
		MHandler.sendMessageDelayed(MHandler.obtainMessage(HandlerConstant.MainJump), 3000);
	}

	public void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}
	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

	private void init() {
		_MTV_Versiong = (TextView) findViewById(R.id.act_tv_main_version);

		versionName = AppUtils.getVersionName(mContext);
		_MTV_Versiong.setText("当前版本号："+versionName+URLs.url_des);


		/**启动友盟统计*/
		MobclickAgent.updateOnlineConfig( mContext );
		/** 设置是否对日志信息进行加密, 默认false(不加密). */
		AnalyticsConfig.enableEncrypt(false);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		// 返回键
		if (keyCode == KeyEvent.KEYCODE_BACK){

		}
		return super.onKeyDown(keyCode, event);
	}
}
