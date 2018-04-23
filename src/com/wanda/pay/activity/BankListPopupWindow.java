package com.wanda.pay.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
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
import android.widget.ListView;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.wanda.pay.R;
import com.wanda.pay.R.string;
import com.wanda.pay.adapter.BankListAdapter;
import com.wanda.pay.app.WDApplication;
import com.wanda.pay.bean.BankBean;
import com.wanda.pay.bean.Constant_data;
import com.wanda.pay.bean.ScanPay;
import com.wanda.pay.bean.UserBean;
import com.wanda.pay.dialog.CustomProgressDialog;
import com.wanda.pay.util.ApiClient;
import com.wanda.pay.util.JSONParsing;
import com.wanda.pay.util.Result;
import com.wanda.pay.util.ToastUtil;

/**
 * 弹出银行卡列表
 * @author Luckydog
 *
 */
public class BankListPopupWindow extends Activity implements IMVCActivity,OnClickListener,OnItemClickListener{  

	private ListView lv_card_list;
	private Context mContext;
	private BankListAdapter adapter;
	private GetUsableBanksTask getUsableBanksTask;
	private ArrayList<BankBean> BankList;

	@Override  
	protected void onCreate(Bundle savedInstanceState) {  
		super.onCreate(savedInstanceState);  
		setContentView(R.layout.popupwindow_bank_list);  
		mContext=this;
		initView();
		init();
		
	}  

	/**
	 * 获取可签约银行列表
	 */
	private void getBankList() {
		// TODO Auto-generated method stub
		CustomProgressDialog.showProgressDialog(mContext, "loading");
		getUsableBanksTask = new GetUsableBanksTask();
		getUsableBanksTask.execute();
	}

	/**
	 * 获取可签约银行
	 * 
	 * @author kevin
	 * 
	 */
	private class GetUsableBanksTask extends AsyncTask<String, Void, Boolean> {
		String strError;
		JSONObject jsonData;
		Exception exception;
		private Result GetUserBanksResult;

		@Override
		protected Boolean doInBackground(String... prams) {
			// TODO Auto-generated method stub
			try {
				UserBean userBean = WDApplication.getInstance().getUserBean();
				if (userBean != null) {
					Map<String, Object> pramsMap = new HashMap<String, Object>();

					pramsMap.put("sysNo", "2");
					pramsMap.put("customerNo", userBean.getmUserNO());

					GetUserBanksResult = ApiClient.GetUsableBanks(mContext,
							pramsMap);
				}
			} catch (Exception e) {
				e.printStackTrace();
				exception = e;
			}
			return GetUserBanksResult != null;
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
				if (Constant_data.HTTP_SUCCESS.equals(GetUserBanksResult.coder)) {
					BankList = JSONParsing
							.GetUsableBanksInfo(GetUserBanksResult.jsonBodyArray);
					if (BankList == null) {
						// 获取可签约银行列表
						ToastUtil.showShort(mContext, "未查询到合作银行!");
					}
					adapter.setData(BankList);
					adapter.notifyDataSetChanged();
				} else {
					strError = GetUserBanksResult.msg;
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
			getUsableBanksTask = null;
			super.onCancelled();
		}

	}
	@Override
	public void init() {
		adapter = new BankListAdapter(mContext, BankList);
		lv_card_list.setAdapter(adapter);
		getBankList();
	}

	@Override
	public void initView() {
		getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		lv_card_list=(ListView) findViewById(R.id.lv_banklist);
		lv_card_list.setDivider(null);//去除listview的下划线  
		lv_card_list.setOnItemClickListener(this);
		//添加按钮监听  
		findViewById(R.id.iv_back).setOnClickListener(this);  
	}

	@Override
	public void refresh(Object... param) {
		adapter.notifyDataSetChanged();
	}


	//实现onTouchEvent触屏函数但点击屏幕时销毁本Activity  
	@Override  
	public boolean onTouchEvent(MotionEvent event){  
		finish();  
		return true;  
	}  

	@Override
	protected void onResume() {
		adapter.notifyDataSetChanged();
		super.onResume();
	}

	public void onClick(View v) {  
		switch (v.getId()) {  
		case R.id.iv_back: 
			finish();
			break;  
		default:  
			break;  
		}  
	}


	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		setResult(998, getIntent().putExtra("number", position));
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
