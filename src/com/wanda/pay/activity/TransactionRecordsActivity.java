package com.wanda.pay.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.wanda.pay.R;
import com.wanda.pay.R.string;
import com.wanda.pay.adapter.TransactionRecordListAdapter;
import com.wanda.pay.app.WDApplication;
import com.wanda.pay.bean.Constant_data;
import com.wanda.pay.bean.TransactionRecordBean;
import com.wanda.pay.bean.UserBean;
import com.wanda.pay.dialog.CustomProgressDialog;
import com.wanda.pay.util.ApiClient;
import com.wanda.pay.util.ImmersedStatusbarUtils;
import com.wanda.pay.util.JSONParsing;
import com.wanda.pay.util.Result;
import com.wanda.pay.util.SPUtils;
import com.wanda.pay.util.TimerTools;
import com.wanda.pay.util.ToastUtil;
import com.wanda.pay.view.SingleLayoutListView.OnLoadMoreListener;
import com.wanda.pay.view.SingleLayoutListView.OnRefreshListener;

/**
 * 交易记录查询明细
 * 
 * @author liuzhipeng
 * 
 */
public class TransactionRecordsActivity extends BaseActivity implements
IMVCActivity, OnClickListener ,OnRefreshListener, OnLoadMoreListener , OnItemClickListener{

	private Context mContext = this;

	private int _Page = 1;
	private int _PageSize = 20;
	private String _TradeType ;

	private ListView _TRSLLV_List;

	private TextView _TRSRTV_Month;
	private ImageView _TRSRIV_Rili;

	/** 是否是刷新 */
	private boolean isRefresh = true;
	private ArrayList<TransactionRecordBean> recordBeans = new ArrayList<TransactionRecordBean>() ;
	private ArrayList<TransactionRecordBean> recordBeansTemp ;//= new ArrayList<TransactionRecordBean>();

	private TransactionRecordListAdapter listAdapter;
	/**列表请求Task*/
	private GetRecordListTask recordListTask;

	private String _SearchState = "3";

	private TextView _ACOTV_Title;

	private String _EndTime_Str_Request;

	private String _StartTime_Str_Request;
	/**过去5月开始时间数组**/
	private long[] _StartTime=new long[5];
	/**过去5月结束时间数组**/
	private long[] _EndTime=new long[5];

	private PopupWindow popupWindow;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.activity_transation_records);
		WDApplication.getInstance().addActivity(this);

		View topView = findViewById(R.id.act_top);
		ImmersedStatusbarUtils.initAfterSetContentView(this, null);

		initView();
		init();
		refresh();
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if(recordListTask!=null){
			recordListTask.cancel(true);
		}
	}

	@Override
	public void init() {

		_SearchState =  getIntent().getStringExtra("SearchState");
		_TradeType = getIntent().getStringExtra("tradeType");

		int month=TimerTools.getMonth();
		int year=TimerTools.getYear();
		_StartTime[0]=TimerTools.firstDay(year, month);
		_EndTime[0]=TimerTools.LastDay(year, month);
		for(int i=1;i<5;i++){
			if(--month==0){
				month=12;
				year--;
			}
			_StartTime[i]=TimerTools.firstDay(year, month);
			_EndTime[i]=TimerTools.LastDay(year, month);
		}

		getRecordList(0);
	}


	@Override
	public void initView() {

		_ACOTV_Title = (TextView) findViewById(R.id.activity_title_tv_name);
		_TRSRTV_Month=(TextView) findViewById(R.id.act_tv_transation_month);
		_TRSRIV_Rili=(ImageView) findViewById(R.id.act_iv_transation_rili);
		findViewById(R.id.act_rl_transation_rili).setOnClickListener(this);;

		findViewById(R.id.activity_title_btn_back).setOnClickListener(this);

		_TRSLLV_List=(ListView) findViewById(R.id.act_lv_transation_recodes);
		_TRSLLV_List.setOnItemClickListener(this);
		//		_TRSLLV_List.setOnRefreshListener(this);
		//		_TRSLLV_List.setOnLoadListener(this);
		//		_TRSLLV_List.setCanLoadMore(true);
		//		_TRSLLV_List.setCanRefresh(true);
		//		_TRSLLV_List.setAutoLoadMore(true);
		//		_TRSLLV_List.setMoveToFirstItemAfterRefresh(true);

		listAdapter = new TransactionRecordListAdapter(mContext, recordBeans);
		_TRSLLV_List.setAdapter(listAdapter);

	}

	@Override
	public void refresh(Object... param) {
		// TODO Auto-generated method stub
		if("0".equals(_SearchState)){
			_ACOTV_Title.setText("未完成交易");
		}else{
			_ACOTV_Title.setText("查询明细");
		}

	}

	@Override
	public void MyBack() {
		// TODO Auto-generated method stub
		TransactionRecordsActivity.this.finish();
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

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.activity_title_btn_back:
			//返回
			MyBack();
			break;
		case R.id.act_rl_transation_rili:
			showPopupWindow();
			break;
		case R.id.pop_tv_transaction2_1:
			getRecordList(0);
			break;
		case R.id.pop_tv_transaction2_2:
			getRecordList(1);
			break;
		case R.id.pop_tv_transaction2_3:
			getRecordList(2);
			break;
		case R.id.pop_tv_transaction2_4:
			getRecordList(3);
			break;
		case R.id.pop_tv_transaction2_5:
			getRecordList(4);
			break;

		default:
			break;
		}
	}

	/**
	 * 弹出popupwindow
	 */
	private void showPopupWindow() {
		View view=LayoutInflater.from(this).inflate(R.layout.popupwindow_transaction_records, null);
		view.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);

		popupWindow=new PopupWindow(view,view.getMeasuredWidth(),view.getMeasuredHeight());
		popupWindow.setFocusable(true);
		popupWindow.setBackgroundDrawable(new BitmapDrawable());

		TextView tv1=(TextView) view.findViewById(R.id.pop_tv_transaction2_1);
		tv1.setText(TimerTools.getDateFormat(_StartTime[0]+"", getResources().getString(string.yyyy_mm)));
		tv1.setOnClickListener(this);
		TextView tv2=(TextView) view.findViewById(R.id.pop_tv_transaction2_2);
		tv2.setText(TimerTools.getDateFormat(_StartTime[1]+"", getResources().getString(string.yyyy_mm)));
		tv2.setOnClickListener(this);
		TextView tv3=(TextView) view.findViewById(R.id.pop_tv_transaction2_3);
		tv3.setText(TimerTools.getDateFormat(_StartTime[2]+"", getResources().getString(string.yyyy_mm)));
		tv3.setOnClickListener(this);
		TextView tv4=(TextView) view.findViewById(R.id.pop_tv_transaction2_4);
		tv4.setText(TimerTools.getDateFormat(_StartTime[3]+"", getResources().getString(string.yyyy_mm)));
		tv4.setOnClickListener(this);
		TextView tv5=(TextView) view.findViewById(R.id.pop_tv_transaction2_5);
		tv5.setText(TimerTools.getDateFormat(_StartTime[4]+"", getResources().getString(string.yyyy_mm)));
		tv5.setOnClickListener(this);

		popupWindow.showAsDropDown(_TRSRIV_Rili, 0, 0);
	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		isRefresh = true;
		_Page = 1;
		getRecordList();
	}

	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
		isRefresh = false;
		_Page++;
		getRecordList();
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		TransactionRecordBean bean=(TransactionRecordBean) listAdapter.getItem(arg2);
		Intent intent=new Intent(this,TransactionDetailActivity.class);
		intent.putExtra("bean", bean);
		startActivity(intent);
	}
	/**
	 * 获取交易记录
	 */
	private void getRecordList() {

		CustomProgressDialog.showProgressDialog(mContext, "loading");
		recordListTask = new GetRecordListTask();
		recordListTask.execute();
	}
	/**
	 * 获取交易记录
	 * @param index 过去5月数组下标
	 */
	private void getRecordList(int index) {
		_EndTime_Str_Request = TimerTools.getDateFormat(_EndTime[index]+"", getResources().getString(string.yyyyMMddHHmmss));
		_StartTime_Str_Request = TimerTools.getDateFormat(_StartTime[index]+"", getResources().getString(string.yyyyMMddHHmmss));
		if(popupWindow!=null){
			popupWindow.dismiss();
			popupWindow=null;
		}
		_TRSRTV_Month.setText(TimerTools.getDateFormat(_StartTime[index]+"", getResources().getString(string.mm_yyyy)));

		CustomProgressDialog.showProgressDialog(mContext, "loading");
		recordListTask = new GetRecordListTask();
		recordListTask.execute();
	}
	/**
	 * 获取交易列表
	 * @author kevin
	 *
	 */
	private class GetRecordListTask extends AsyncTask<String, Void, Boolean> {
		String strError;
		JSONObject jsonData;
		Exception exception;
		private Result recordListResult;

		@Override
		protected Boolean doInBackground(String... params) {
			try {
				UserBean userBean = WDApplication.getInstance().getUserBean();
				if(userBean!=null){
					String _LoginNameType = SPUtils.getString(mContext, Constant_data.sp_key_userNameType, "1");
					String _LoginName = SPUtils.getString(mContext, Constant_data.sp_key_loginName, "");
					Map<String, Object> pramsMap = new HashMap<String, Object>();

					pramsMap.put("userNameType", _LoginNameType);
					if (_TradeType!=null) pramsMap.put("tradeType", _TradeType);
					//pramsMap.put("tradeType", "11");
					pramsMap.put("customerNo", userBean.getmUserNO()); //用户号
					pramsMap.put("userName", _LoginName);
					pramsMap.put("startTime", _StartTime_Str_Request+"");
					if(!_SearchState.equals("0")) pramsMap.put("endTime", _EndTime_Str_Request+"");
					pramsMap.put("page", _Page+"");
					pramsMap.put("pageSize", _PageSize+"");
					pramsMap.put("state", _SearchState);	//交易状态

					recordListResult = ApiClient.GetRecordList(mContext, pramsMap);
				}
			} catch (Exception e) {
				e.printStackTrace();
				exception = e;
			}
			return recordListResult != null;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			CustomProgressDialog.hideProgressDialog();
			if(exception != null ){
				Toast.makeText(getApplicationContext(), "网络异常，请重试!", Toast.LENGTH_LONG).show();
				return;
			}
			if(result){
				if(Constant_data.HTTP_SUCCESS.equals(recordListResult.coder)){
					recordBeansTemp = JSONParsing.GetRecordListparsing(recordListResult.jsonBodyArray);
					if(isRefresh){
						recordBeans.clear();
						recordBeans.addAll(recordBeansTemp);
					}else{
						recordBeans.addAll(recordBeansTemp);
					}
					Collections.sort(recordBeans, new Comparator<TransactionRecordBean>() {

						@Override
						public int compare(TransactionRecordBean lhs,
								TransactionRecordBean rhs) {
							long result=Long.parseLong(rhs.getTradeTime())-Long.parseLong(lhs.getTradeTime());
							if(result>0){
								return 1;
							}else if(result<0){
								return -1;
							}
							return 0;
						}
					});
					listAdapter.notifyDataSetChanged();


				}else{
					strError = recordListResult.msg;
					ToastUtil.showShort(mContext, strError);
				}
			}else{
				ToastUtil.showShort(mContext, "接收数据出错");
			}

			super.onPostExecute(result);
		}

		@Override
		protected void onCancelled() {
			recordListTask = null;
			super.onCancelled();
		}
	}

}
