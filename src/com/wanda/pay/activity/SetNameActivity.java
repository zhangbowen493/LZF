package com.wanda.pay.activity;

import com.wanda.pay.R;
import com.wanda.pay.app.WDApplication;
import com.wanda.pay.util.ImmersedStatusbarUtils;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class SetNameActivity extends BaseActivity implements
OnClickListener,IMVCActivity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setname);
		ImmersedStatusbarUtils.initAfterSetContentView(this, null);
		WDApplication.getInstance().addActivity(this);
		initView();
		
	}
	
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initView() {
		TextView _BCTV_Title = (TextView) findViewById(R.id.activity_title_tv_name);
		_BCTV_Title.setText("设置昵称");
		
	}

	@Override
	public void refresh(Object... param) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void MyBack() {
		// TODO Auto-generated method stub
		
	}

}
