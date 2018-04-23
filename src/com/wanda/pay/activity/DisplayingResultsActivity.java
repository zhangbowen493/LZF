package com.wanda.pay.activity;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.Time;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.wanda.pay.R;
import com.wanda.pay.app.WDApplication;
import com.wanda.pay.bean.ScanPay;
import com.wanda.pay.util.ImmersedStatusbarUtils;
/*
 *@作者: Administrator
 *@版本: 
 *@version create time：2016-1-28 上午11:08:02
 */
public class DisplayingResultsActivity extends BaseActivity implements
OnClickListener {

	private Context mContext;
	private String mFromWhere;
	private String mShowInfo;
	//	private TextView mDRTV_info;
	private ArrayList<ScanPay> mCardDataList = new ArrayList<ScanPay>();
	private TextView mact_money_text;
	private TextView mact_number_text;
	private TextView mact_mode_text;
	private TextView mact_time_text;
	private String mMoney;
	private int bankPosition;
	private String lognname;
	private String number;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_recharge_success);
		ImmersedStatusbarUtils.initAfterSetContentView(this, null);
		WDApplication.getInstance().addActivity(this);
		mContext = this;
		initView();
		initData();
	}
	private void initView() {
		mact_money_text=(TextView) findViewById(R.id.act_money_text);
		mact_number_text=(TextView) findViewById(R.id.act_number_text);
		mact_mode_text=(TextView) findViewById(R.id.act_mode_text);
		mact_time_text=(TextView) findViewById(R.id.act_time_text);
		findViewById(R.id.activity_title_btn_back).setVisibility(View.GONE);
		findViewById(R.id.activity_title_text_right).setVisibility(View.VISIBLE);
		findViewById(R.id.activity_title_text_right).setOnClickListener(this);;
	}
	private void initData() {
		// TODO Auto-generated method stub
		mFromWhere = getIntent().getStringExtra("fromwhere");
		mMoney=getIntent().getStringExtra("amount");
		number=getIntent().getStringExtra("number");
		lognname=getIntent().getStringExtra("loginName");
		mact_money_text.setText(mMoney+"元");
		mact_number_text.setText(lognname);
		mact_mode_text.setText(number);
		//系统时间显示
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String str = format.format(new java.util.Date());
		mact_time_text.setText(str);
	}
	/**
	 * 后退接口
	 */
	private void MyBack() {
		// TODO Auto-generated method stub
		finish();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_act_dra_ok:
			this.finish();
			break;
		case R.id.activity_title_text_right:
			MyBack();
			break;
		default:
			break;
		}
	}

}


