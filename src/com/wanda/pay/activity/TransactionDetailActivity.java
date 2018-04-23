package com.wanda.pay.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.wanda.pay.R;
import com.wanda.pay.R.string;
import com.wanda.pay.app.WDApplication;
import com.wanda.pay.bean.TransactionRecordBean;
import com.wanda.pay.util.TimerTools;
/**
 * 交易记录账户详情
 * @author liuzhipeng
 *
 */
public class TransactionDetailActivity extends BaseActivity implements IMVCActivity, OnClickListener{

	private TextView _ACOTV_Title;
	private TextView _TRSDTV_Money;
	private TextView _TRSDTV_State;
	private TextView _TRSDTV_Account_Type;
	private TextView _TRSDTV_Account;
	private TextView _TRSDTV_Way_Type;
	private TextView _TRSDTV_Way;
	private TextView _TRSDTV_Time_Type;
	private TextView _TRSDTV_Time;
	
	private TransactionRecordBean bean;
	private ImageView _TRSDIV_Way_Type;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_transation_detail);
		WDApplication.getInstance().addActivity(this);
		initView();
		init();
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		_ACOTV_Title.setText("交易详情");
		bean=(TransactionRecordBean) getIntent().getSerializableExtra("bean");
		
		long date2long = TimerTools.date2long(bean.getTradeTime(), getResources().getString(string.yyyyMMddHHmmss));
		_TRSDTV_Time.setText(TimerTools.getDateFormat(date2long+"", getResources().getString(string.yyyy_MM_dd_HH_mm_ss)));
		
		String type="";
		String sign=" ";
		String account=WDApplication.getInstance().getUserBean().getmMoblePhoneNo();
		switch (bean.getTradeTypeInt()) {
		case 11:
			type="消费";
			sign="-";
			account=bean.getmMerName();
			break;
		case 12:
			type="充值";
			sign="+";
			break;
		case 13:
			type="提现";
			sign="-";
			break;
		case 15:
			type="转账";
			if(bean.getmTransFerFlag().equals("1")){
				//转入
				sign="+";
				account=bean.getmTransFerPartner();
			}else{
				//转出
				sign="-";
			}
			break;
		case 3:
			type="退款";
			sign="+";
			break;

		default:
			type=bean.getTradeType();
			sign="-";
			break;
		}
		_TRSDTV_Account_Type.setText(bean.getTradeTypeInt()==11?"商户":type+"账户");
		_TRSDTV_Account.setText(account);
		_TRSDTV_Money.setText(sign+bean.getTradeAmount());
		if(bean.getTradeTypeInt()==15){
			_TRSDTV_Way_Type.setText("收款账户");
			_TRSDIV_Way_Type.setImageResource(R.drawable.icon_shoukuanzhanghu);
		}else{
			_TRSDTV_Way_Type.setText(type+"方式");
		}
		_TRSDTV_Way.setText(bean.getTradeWay());
		_TRSDTV_State.setText(type+bean.getState().substring(2));
	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
		_ACOTV_Title = (TextView) findViewById(R.id.activity_title_tv_name);
		findViewById(R.id.activity_title_btn_back).setOnClickListener(this);
		
		_TRSDTV_Money=(TextView) findViewById(R.id.act_tv_transaction_detail2_money);
		_TRSDTV_State=(TextView) findViewById(R.id.act_tv_transaction_detail2_state);
		_TRSDTV_Account_Type=(TextView) findViewById(R.id.act_tv_transaction_detail2_account_type);
		_TRSDTV_Account=(TextView) findViewById(R.id.act_tv_transaction_detail2_account);
		_TRSDTV_Way_Type=(TextView) findViewById(R.id.act_tv_transaction_detail2_way_type);
		_TRSDTV_Way=(TextView) findViewById(R.id.act_tv_transaction_detail2_way);
		_TRSDTV_Time_Type=(TextView) findViewById(R.id.act_tv_transaction_detail2_time_type);
		_TRSDTV_Time=(TextView) findViewById(R.id.act_tv_transaction_detail2_time);
		
		_TRSDIV_Way_Type=(ImageView) findViewById(R.id.act_iv_transaction_detail2_way_type);
	}

	@Override
	public void refresh(Object... param) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void MyBack() {
		// TODO Auto-generated method stub
		finish();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.activity_title_btn_back:
			MyBack();
			break;

		default:
			break;
		}
	}

}
