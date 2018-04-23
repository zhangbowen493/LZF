package com.wanda.pay.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.wanda.pay.R;
import com.wanda.pay.app.WDApplication;
import com.wanda.pay.util.ImmersedStatusbarUtils;
/**
 * 重置支付密码成功界面
 * @author kevin
 *
 */
public class UpdatePayPwdSuccessfuActivity extends BaseActivity implements
		IMVCActivity , OnClickListener{
	
	private String _Extra_Phone;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_update_pay_pwd_success);
		ImmersedStatusbarUtils.initAfterSetContentView(this, null);
		WDApplication.getInstance().addActivity(this);
		init();
		initView();
		refresh();
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		_Extra_Phone = getIntent().getStringExtra("accounts");
	}

	@Override
	public void initView() {
		
		TextView _RSTV_Title = (TextView) findViewById(R.id.activity_title_tv_name);
		_RSTV_Title.setText("重置支付密码");
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
		Intent intent = new Intent(UpdatePayPwdSuccessfuActivity.this, MainViewPagerActivity.class);
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
