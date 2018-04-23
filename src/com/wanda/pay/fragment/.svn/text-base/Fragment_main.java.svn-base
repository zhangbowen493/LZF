package com.wanda.pay.fragment;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wanda.pay.R;
import com.wanda.pay.activity.BankCenterActivity;
import com.wanda.pay.activity.CaptureActivity;
import com.wanda.pay.activity.FinancialDrawMoneyActivity;
import com.wanda.pay.activity.FinancialTopUpActivity1;
import com.wanda.pay.activity.FinancialTransferAccountActivity;
import com.wanda.pay.activity.IMVCActivity;
import com.wanda.pay.activity.SetPasswordActivity;
import com.wanda.pay.activity.SetPasswordActivity1;
import com.wanda.pay.activity.TransactionRecordsActivity;
import com.wanda.pay.app.WDApplication;
import com.wanda.pay.util.GetUserInfoUtil;
import com.wanda.pay.util.LogUtil;
import com.wanda.pay.util.ViewFactory;
import com.wonders.pay.viewpager.ADInfo;
import com.wonders.pay.viewpager.CycleViewPager;
import com.wonders.pay.viewpager.CycleViewPager.ImageCycleViewListener;

public class Fragment_main extends Fragment implements  IMVCActivity,   OnClickListener{
	private View view;
	private List<ImageView> views = new ArrayList<ImageView>();
	private List<ADInfo> infos = new ArrayList<ADInfo>();
	private CycleViewPager cycleViewPager;
	private TextView fragment_main_balance; //余额
	private String lognname;
	private GetUserInfoUtil getUserInfoTask;
	
	
	/**
	 * 轮播图片路径
	 */
	private String[] imageUrls = {"http://pic15.nipic.com/20110722/2912365_092519919000_2.jpg",
	"http://pic.58pic.com/58pic/12/64/27/55U58PICrdX.jpg"};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if(view==null)
			view = inflater.inflate(R.layout.wonders_fragment_main, null);
		
		initView();

		//在fragment中注册广播接收者
		registerReceiver();
		
		//配置图片轮播
		initialize();
		return view;
	}

	private void registerReceiver() {
		LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(getActivity());
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction("com.wonders.MUSERBEAN_REFRESH");
		BroadcastReceiver mItemViewListClickReceiver = new BroadcastReceiver() {
		            @Override
		            public void onReceive(Context context, Intent intent){
		            	notifyInformation();
		            }
		 };
		 broadcastManager.registerReceiver(mItemViewListClickReceiver, intentFilter);
	}

	private void notifyInformation() {
		DecimalFormat decimalFormat = new DecimalFormat("0.00");//构造方法的字符格式这里如果小数不足2位,会以0补足.
		fragment_main_balance.setText(decimalFormat.format(WDApplication.getInstance().getUserBean().getmBlance()));
	}

	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
		//Log.i("tag", "fragment_main销毁");
	}

	/**
	 * 设置图片轮播
	 */
	private void initialize() {

		cycleViewPager = (CycleViewPager)getActivity().getFragmentManager().findFragmentById(R.id.fragment_cycle_viewpager_content);

		if (infos!=null) 			
			infos.clear();
		for(int i = 0; i < imageUrls.length; i ++){
			ADInfo info = new ADInfo();
			info.setUrl(imageUrls[i]);
			infos.add(info);
		}

		if(views!=null)
			views.clear();
		// 将最后一个ImageView添加进来
		views.add(ViewFactory.getImageView(getActivity(), infos.get(infos.size() - 1).getUrl()));
		for (int i = 0; i < infos.size(); i++) {
			views.add(ViewFactory.getImageView(getActivity(), infos.get(i).getUrl()));
		}
		// 将第一个ImageView添加进来
		views.add(ViewFactory.getImageView(getActivity(), infos.get(0).getUrl()));

		// 设置循环，在调用setData方法前调用
		cycleViewPager.setCycle(true);

		// 在加载数据前设置是否循环
		cycleViewPager.setData(views, infos, mAdCycleViewListener);
		//设置轮播
		cycleViewPager.setWheel(true);

		// 设置轮播时间，
		cycleViewPager.setTime(2000);
		//设置圆点指示图标组居中显示，默认靠右
		cycleViewPager.setIndicatorCenter();
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		cycleViewPager.hide();
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		LogUtil.d("执行OnResume()");
		getUserInfoTask = new GetUserInfoUtil(getActivity());
		getUserInfoTask.execute();
		cycleViewPager.start();
		super.onResume();

	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}
	private ImageCycleViewListener mAdCycleViewListener = new ImageCycleViewListener() {

		@Override
		public void onImageClick(ADInfo info, int position, View imageView) {
			if (cycleViewPager.isCycle()) {
				position = position - 1;
			}
		}
	};


	@Override
	public void init() {
		//		lognname=getIntent().getStringExtra("loginName");

	}


	@Override
	public void initView() {
		fragment_main_balance = (TextView) view.findViewById(R.id.tv_fragment_main_money_balance);

		view.findViewById(R.id.rl_main_scan).setOnClickListener(this);
		view.findViewById(R.id.rl_query).setOnClickListener(this);
		view.findViewById(R.id.rl_recharge).setOnClickListener(this);
		view.findViewById(R.id.rl_deposit).setOnClickListener(this);
		view.findViewById(R.id.rl_transfer).setOnClickListener(this);
		view.findViewById(R.id.rl_bankmanager).setOnClickListener(this);
		view.findViewById(R.id.rl_loginpwd).setOnClickListener(this);
		view.findViewById(R.id.rl_paypwd).setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_main_scan:
			// 扫描支付
			Intent intent = new Intent(getActivity(), CaptureActivity.class);
			startActivityForResult(intent, 1);
			break;
		case R.id.rl_query:
			// 交易查询
			Intent intent4 = new Intent(getActivity(),TransactionRecordsActivity.class);
			intent4.putExtra("SearchState", "3");
			startActivity(intent4);
			getActivity().overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
			break;
		case R.id.rl_recharge:
			//充值
			Intent intent5 = new Intent(getActivity(), FinancialTopUpActivity1.class);
			startActivity(intent5);
			getActivity().overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
			break;
		case R.id.rl_deposit:
			//提现
			Intent intent6 = new Intent(getActivity(), FinancialDrawMoneyActivity.class);
			startActivity(intent6);
			getActivity().overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
			break;
		case R.id.rl_transfer:
			//转账
			Intent intent7 = new Intent(getActivity(), FinancialTransferAccountActivity.class);
			startActivity(intent7);
			getActivity().overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
			break;
		case R.id.rl_bankmanager:
			// 银行卡管理
			Intent intent1 = new Intent(getActivity(), BankCenterActivity.class);
			startActivity(intent1);
			getActivity().overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
			break;
		case R.id.rl_loginpwd:
			// 登录密码管理
			Intent intent3 = new Intent(getActivity(), SetPasswordActivity.class);
			intent3.putExtra("AccessType", 5);
			intent3.putExtra("loginName", lognname);
			startActivity(intent3);
			getActivity().overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
			break;
		case R.id.rl_paypwd:
			// 支付密码管理
			Intent intent2 = new Intent(getActivity(), SetPasswordActivity.class);
			intent2.putExtra("AccessType", 2);
			intent2.putExtra("loginName", lognname);
			startActivity(intent2);
			getActivity().overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
			break;

		default:
			break;
		}
	}


	@Override
	public void refresh(Object... param) {

	}

	@Override
	public void MyBack() {
		// TODO Auto-generated method stub

	}

}
