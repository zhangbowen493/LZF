package com.wanda.pay.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.wanda.pay.R;
import com.wanda.pay.app.WDApplication;
import com.wanda.pay.util.ImmersedStatusbarUtils;
/**
 * 注册页面 输入手机号码
 * @author kevin
 *
 */
public class RegisteredSuccessfuActivity extends BaseActivity implements
IMVCActivity , OnClickListener{

	private String _Extra_Phone;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_regist_success);
		ImmersedStatusbarUtils.initAfterSetContentView(this, null);
		WDApplication.getInstance().addActivity(this);
		init();
		initView();
		refresh();
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		findViewById(R.id.activity_title_btn_back).setVisibility(View.GONE);
		_Extra_Phone = getIntent().getStringExtra("Phone");
	}

	@Override
	public void initView() {

		TextView _RSTV_Title = (TextView) findViewById(R.id.activity_title_tv_name);
		_RSTV_Title.setText("注册");
		findViewById(R.id.activity_title_btn_back).setOnClickListener(this);
		findViewById(R.id.activity_regist_success_btn_gotologin).setOnClickListener(this);
	}

	@Override
	public void refresh(Object... param) {
		// TODO Auto-generated method stub
	}

	@Override
	public void MyBack() {
		// TODO Auto-generated method stub
		Intent intent = new Intent(RegisteredSuccessfuActivity.this, LoginActivity.class);
		intent.putExtra("success_phone", _Extra_Phone);
		startActivity(intent);
		finish();
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
		case R.id.activity_regist_success_btn_gotologin:
			//去登录
			MyBack();
			break;

		default:
			break;
		}
	}

}
