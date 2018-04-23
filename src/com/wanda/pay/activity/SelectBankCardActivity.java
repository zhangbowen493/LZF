package com.wanda.pay.activity;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.wanda.pay.R;
import com.wanda.pay.app.WDApplication;
import com.wanda.pay.bean.ScanPay;
import com.wanda.pay.util.ToastUtil;
	/*
 *@作者: Administrator
 *@版本: 
 *@version create time：2016-1-27 下午2:36:31
 */
public class SelectBankCardActivity extends BaseActivity implements
		OnClickListener, OnItemClickListener {

	private Context mContext;
	private ListView sbkLV_cardlist;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_select_bank_card);
		WDApplication.getInstance().addActivity(this);
		mContext = this;
		initView();
		initData();
	}

	private void initView() {
		// TODO Auto-generated method stub
		
		sbkLV_cardlist = (ListView) findViewById(R.id.lv_act_sbc);
		
		TextView _BCTV_Title = (TextView) findViewById(R.id.activity_title_tv_name);
		_BCTV_Title.setText("选择银行卡");

		findViewById(R.id.activity_title_btn_back).setOnClickListener(this);
		
	}

	private void initData() {
		// TODO Auto-generated method stub
		ArrayList<ScanPay>  cardList= (ArrayList<ScanPay>) getIntent().getSerializableExtra("cardList");
		
		DialogAdapter dialogAdapter = new DialogAdapter(mContext, cardList);
		
		sbkLV_cardlist.setAdapter(dialogAdapter);
		sbkLV_cardlist.setOnItemClickListener(this);
		
	}
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		setResult(999, new Intent().putExtra("number", arg2));
		SelectBankCardActivity.this.finish();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.activity_title_btn_back:
			//返回
			MyBack();
			break;

		default:
			break;
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


