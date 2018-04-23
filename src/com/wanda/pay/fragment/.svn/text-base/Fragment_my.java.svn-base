package com.wanda.pay.fragment;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.wanda.pay.R;
import com.wanda.pay.R.string;
import com.wanda.pay.activity.FinancialDrawMoneyActivity;
import com.wanda.pay.activity.FinancialTopUpActivity;
import com.wanda.pay.activity.FinancialTopUpActivity1;
import com.wanda.pay.activity.FinancialTransferAccountActivity;
import com.wanda.pay.activity.IMVCActivity;
import com.wanda.pay.activity.LoginActivity;
import com.wanda.pay.activity.RealNameCertificationActivity;
import com.wanda.pay.activity.SetNameActivity;
import com.wanda.pay.activity.SetPasswordActivity;
import com.wanda.pay.bean.Constant_data;
import com.wanda.pay.bean.UserBean;
import com.wanda.pay.dialog.CustomProgressDialog;
import com.wanda.pay.util.ApiClient;
import com.wanda.pay.activity.SetPasswordActivity1;
import com.wanda.pay.app.WDApplication;
import com.wanda.pay.util.PhoneNumberUtil;
import com.wanda.pay.util.Result;
import com.wanda.pay.util.SPUtils;
import com.wanda.pay.util.ToastUtil;
import com.wanda.pay.util.Tools;

public class Fragment_my extends Fragment implements IMVCActivity{
	private TextView tv_phonenumber_top;
	private TextView tv_balance;
	private TextView tv_name;
	private TextView tv_phonenumber_middle;
	private TextView tv_realName_state;
	private ImageView iv_head;
	private View view;
	private LoginOutTask loginOutTask;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.wonders_fragment_my, null);
		initView();
		setListeners(view);
		refresh();
		return view;
	}

	private void setListeners(View view) {
		FragmentMyOnClickListener listener=new FragmentMyOnClickListener();
		view.findViewById(R.id.btn_recharge).setOnClickListener(listener);
		view.findViewById(R.id.btn_transfer).setOnClickListener(listener);
		view.findViewById(R.id.btn_deposit).setOnClickListener(listener);
		view.findViewById(R.id.ll_name).setOnClickListener(listener);
		view.findViewById(R.id.ll_phonenumber).setOnClickListener(listener);
		view.findViewById(R.id.ll_certification).setOnClickListener(listener);
		view.findViewById(R.id.ll_login_pwd).setOnClickListener(listener);
		view.findViewById(R.id.ll_pay_pwd).setOnClickListener(listener);
		view.findViewById(R.id.ll_out_).setOnClickListener(listener);
	}

	public class FragmentMyOnClickListener implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btn_recharge:
				//充值
				Intent intent5 = new Intent(getActivity(), FinancialTopUpActivity1.class);
				startActivity(intent5);
				getActivity().overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
				break;
			case R.id.btn_transfer:
				//转账
				Intent intent7 = new Intent(getActivity(), FinancialTransferAccountActivity.class);
				startActivity(intent7);
				getActivity().overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
				break;
			case R.id.btn_deposit:
				//提现
				Intent intent6 = new Intent(getActivity(), FinancialDrawMoneyActivity.class);
				startActivity(intent6);
				getActivity().overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
				break;
			case R.id.ll_name:
				//昵称
				Intent intent_name = new Intent(getActivity(), SetNameActivity.class);
				startActivity(intent_name);
				getActivity().overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
				break;
			case R.id.ll_phonenumber:
				break;
			case R.id.ll_certification:
				//实名认证
				Intent intent_certification = new Intent(getActivity(), RealNameCertificationActivity.class);
				startActivity(intent_certification);
				getActivity().overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
				break;
			case R.id.ll_login_pwd:
				// 登录密码管理
				Intent intent3 = new Intent(getActivity(), SetPasswordActivity.class);
				intent3.putExtra("AccessType", 5);
				startActivity(intent3);
				getActivity().overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
				break;
			case R.id.ll_pay_pwd:
				// 支付密码管理
				Intent intent2 = new Intent(getActivity(), SetPasswordActivity1.class);
				intent2.putExtra("AccessType", 2);
				startActivity(intent2);
				getActivity().overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
				break;
			case R.id.ll_out_:
				// 退出账户
				showLogOut();
				break;

			}
		}
	}
	/**
	 * 提示去登录
	 */
	private void showLogOut() {
		final Dialog mDialog = new Dialog(getActivity(), R.style.myDilog);
		mDialog.setContentView(R.layout.dialog_layout);
		Button sure = (Button) mDialog.findViewById(R.id.dialog_btn_ok);
		Button back = (Button) mDialog.findViewById(R.id.dialog_btn_cancel);
		TextView _DialogTV = (TextView) mDialog
				.findViewById(R.id.dialog_tv_msg);
		_DialogTV.setText("您将要登出当前帐号？");
		mDialog.setCanceledOnTouchOutside(false);

		sure.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ExtitAPP();
				if (mDialog.isShowing())
					mDialog.dismiss();
			}
		});
		back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mDialog.isShowing())
					mDialog.dismiss();
			}
		});
		Window window = mDialog.getWindow();
		window.setGravity(Gravity.CENTER); // 此处可以设置dialog显示的位置
		window.setWindowAnimations(R.style.mystyle); // 添加动画
		mDialog.show();
	}
	/**
	 * APP登出
	 */
	private void ExtitAPP() {
		// TODO Auto-generated method stub
		CustomProgressDialog.showProgressDialog(getActivity(), "loading");
		loginOutTask = new LoginOutTask();
		loginOutTask.execute();
	}
	/**
	 * 用户登出
	 * 
	 * @author kevin
	 * 
	 */
	private class LoginOutTask extends AsyncTask<String, Void, Boolean> {
		String strError;
		Exception exception;
		private Result loginResult;

		@Override
		protected Boolean doInBackground(String... prams) {
			// TODO Auto-generated method stub
			try {
				UserBean userBean = WDApplication.getInstance().getUserBean();
				if (userBean != null) {
					String _LoginNameType = SPUtils.getString(getActivity(),
							Constant_data.sp_key_userNameType, "1");
					String _LoginName = SPUtils.getString(getActivity(),
							Constant_data.sp_key_loginName, "");

					Map<String, Object> pramsMap = new HashMap<String, Object>();

					pramsMap.put("operationType", "1");
					pramsMap.put("loginType", _LoginNameType);
					pramsMap.put("isLogin", "0");
					pramsMap.put("loginName", _LoginName);

					loginResult = ApiClient.Login(getActivity(), pramsMap);

				}

			} catch (Exception e) {
				e.printStackTrace();
				exception = e;
			}
			return loginResult != null;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			CustomProgressDialog.hideProgressDialog();
			if (exception != null) {
				Toast.makeText(getActivity(),
						getResources().getString(string.http_error_anomalies),
						Toast.LENGTH_LONG).show();
				return;
			}
			if (result) {
				if (Constant_data.HTTP_SUCCESS.equals(loginResult.coder)) {

					WDApplication.getInstance().setUserBean(null);
					SPUtils.clear(getActivity());
					Intent intent = new Intent(getActivity(), LoginActivity.class);
					intent.putExtra("isExit", true);
					startActivity(intent);
					WDApplication.getInstance().getActicity().finish();
				} else {
					strError = loginResult.msg;
					ToastUtil.showShort(getActivity(), strError);
				}
			} else {
				ToastUtil.showShort(getActivity(),
						getResources().getString(string.http_error_anomalies));
			}
			super.onPostExecute(result);
		}

		@Override
		protected void onCancelled() {
			loginOutTask = null;
			super.onCancelled();
		}

	}

	
	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initView() {
		tv_balance=(TextView)view.findViewById(R.id.tv_money_balance);
		tv_name=(TextView)view.findViewById(R.id.tv_name);
		tv_phonenumber_middle=(TextView)view.findViewById(R.id.tv_phonenumber);
		tv_phonenumber_top=(TextView)view.findViewById(R.id.tv_phonenumber_top);
		iv_head=(ImageView) view.findViewById(R.id.iv_head);
		tv_realName_state=(TextView)view.findViewById(R.id.tv_prove2);
	}

	@Override
	public void refresh(Object... param) {
		String _RealNmaeLV = WDApplication.getInstance().getUserBean().getmRealNmaeLV();
		String _RealNameLv = Tools.RealNameLevel(_RealNmaeLV);
		
		DecimalFormat decimalFormat = new DecimalFormat("0.00");//构造方法的字符格式这里如果小数不足2位,会以0补足.
		tv_balance.setText(decimalFormat.format(WDApplication.getInstance().getUserBean().getmBlance()));
		tv_name.setText(WDApplication.getInstance().getUserBean().getmRealName());
		tv_phonenumber_middle.setText(PhoneNumberUtil.getNumber(WDApplication.getInstance().getUserBean().getmMoblePhoneNo()));
		tv_phonenumber_top.setText(PhoneNumberUtil.getNumber(WDApplication.getInstance().getUserBean().getmMoblePhoneNo()));
		tv_realName_state.setText(_RealNameLv);
	}

	@Override
	public void MyBack() {
		// TODO Auto-generated method stub
		
	}
}
