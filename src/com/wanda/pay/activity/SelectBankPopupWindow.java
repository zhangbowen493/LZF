package com.wanda.pay.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.wanda.pay.R;
import com.wanda.pay.app.WDApplication;
import com.wanda.pay.bean.BankCardBean;
import com.wanda.pay.bean.ScanPay;
import com.wanda.pay.util.LogUtil;

/**
 * 选择银行卡列表，从下方弹出
 * @author Luckydog
 *
 */
public class SelectBankPopupWindow extends Activity implements IMVCActivity,OnClickListener,OnItemClickListener{  

	private ListView lv_card_list;
	private ArrayList<ScanPay>  cardList;
	private Context mContext;
	private DialogAdapter dialogAdapter;

	@Override  
	protected void onCreate(Bundle savedInstanceState) {  
		super.onCreate(savedInstanceState);  
		setContentView(R.layout.popupwindow_select_bankcard_list);  
		WDApplication.getInstance().addActivity(this);
		mContext=this;
		initView();
		init();
		
	}  

	@Override
	public void init() {
		cardList= (ArrayList<ScanPay>) getIntent().getSerializableExtra("cardList");
		dialogAdapter = new DialogAdapter(mContext, cardList);
		lv_card_list.setAdapter(dialogAdapter);
	}

	@Override
	public void initView() {
		getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		lv_card_list=(ListView) findViewById(R.id.lv_banklist);
		lv_card_list.setOnItemClickListener(this);
		//添加按钮监听  
		findViewById(R.id.iv_back).setOnClickListener(this);  
		findViewById(R.id.rl_pay_with_newcard).setOnClickListener(this);  
	}

	@Override
	public void refresh(Object... param) {
		// TODO Auto-generated method stub

	}


	//实现onTouchEvent触屏函数但点击屏幕时销毁本Activity  
	@Override  
	public boolean onTouchEvent(MotionEvent event){  
		finish();  
		return true;  
	}  

	@Override
	protected void onResume() {
		dialogAdapter.notifyDataSetChanged();
		LogUtil.d("更新银行卡列表");
		super.onResume();
	}

	public void onClick(View v) {  
		switch (v.getId()) {  
		case R.id.iv_back: 
			finish();
			break;  
		case R.id.rl_pay_with_newcard:    
			Intent intent1 = new Intent(mContext, AddCardOneActivity.class);
			startActivity(intent1);
			overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
			break;  
		default:  
			break;  
		}  
	}


	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		setResult(999, getIntent().putExtra("number", position));
		this.finish();
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

	/**
	 * 银行卡列表的Adapter，展示已绑定银行LOGO+名称
	 * @author Luckydog
	 *
	 */
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
						R.layout.list_select_cards, null);
				mHodler = new viewHolder();
				mHodler.bankImage = (ImageView) convertView
						.findViewById(R.id.iv_bank_pic);
				mHodler.cardNumber = (TextView) convertView
						.findViewById(R.id.tv_bank_name);
				convertView.setTag(mHodler);

			} else {
				mHodler = (viewHolder) convertView.getTag();
			}

			mHodler.bankImage.setImageResource(R.drawable.icon_bank);
			mHodler.cardNumber.setText(data.getBankName());

			return convertView;
		}

		class viewHolder {
			ImageView bankImage;
			TextView cardNumber;
		}
	}

	@Override
	public void MyBack() {
		finish();
	}


}  
