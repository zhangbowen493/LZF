package com.wanda.pay.activity;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.wanda.pay.R;
import com.wanda.pay.R.string;
import com.wanda.pay.app.WDApplication;
import com.wanda.pay.bean.BankCardBean;
import com.wanda.pay.bean.Constant_data;
import com.wanda.pay.bean.UserBean;
import com.wanda.pay.dialog.CustomProgressDialog;
import com.wanda.pay.util.ApiClient;
import com.wanda.pay.util.ImmersedStatusbarUtils;
import com.wanda.pay.util.Result;
import com.wanda.pay.util.ToastUtil;
import com.wanda.pay.util.Tools;

/**
 * 银行卡明细管理
 * @author kevin
 *
 */
public class BankCardManageActivity extends BaseActivity implements
IMVCActivity , OnClickListener{

	private Context mContext = this;

	private Button _BCBTN_Login;

	private String _ProtocolNo;	//协议号

	private CancelSignInfoReqTask cancelSignInfoReqTask;

	private BankCardBean _BankCardBean;

	private TextView _BCTV_BankName;

	private TextView _BCTV_BankType;
	private TextView _BCTV_BankNumber;
	private TextView _BCTV_BankInfo;

	private ImageView _BCIV_BankIcon;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bank_card_manage);
		ImmersedStatusbarUtils.initAfterSetContentView(this, null);
		WDApplication.getInstance().addActivity(this);
		init();
		initView();
		refresh();
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if(cancelSignInfoReqTask!=null)
			cancelSignInfoReqTask.cancel(true);
	}

	@Override
	public void init() {
		//		_ProtocolNo = getIntent().getStringExtra("protocolNo");
		Bundle bundle = new Bundle();
		bundle =  this.getIntent().getExtras();
		_BankCardBean = (BankCardBean) bundle.getSerializable("bankCardBean");

	}

	@Override
	public void initView() {
		_BCTV_BankName = (TextView) findViewById(R.id.act_tv_card_mange_bank_name);
		_BCTV_BankInfo = (TextView) findViewById(R.id.act_tv_card_mange_bank_info);
		_BCTV_BankType = (TextView) findViewById(R.id.act_tv_card_mange_bank_type);
		_BCTV_BankNumber = (TextView) findViewById(R.id.act_tv_card_mange_bank_number);
		_BCIV_BankIcon =  (ImageView) findViewById(R.id.act_iv_card_mange_bank_icon);

		TextView _BCTV_Title = (TextView) findViewById(R.id.activity_title_tv_name);
		_BCTV_Title.setText("管理银行卡");

		findViewById(R.id.activity_title_btn_back).setOnClickListener(this);
		findViewById(R.id.act_btn_card_mange_bank_del).setOnClickListener(this);
		_BCBTN_Login = (Button) findViewById(R.id.activity_title_btn_login);
		_BCBTN_Login.setVisibility(View.GONE);
	}

	@Override
	public void refresh(Object... param) {
		// TODO Auto-generated method stub
		if(_BankCardBean!=null){
			_BCTV_BankName.setText(_BankCardBean.get_BankName());
			_BCTV_BankType.setText("储蓄卡");
			//_BCTV_BankType.setText(_BankCardBean.get_CardTypeCode());
			String bankCardNumber = _BankCardBean.get_BankCardNumber();
			_BCTV_BankNumber.setText("尾号"+bankCardNumber.substring(bankCardNumber.length()-4, bankCardNumber.length()));
			//_BCTV_BankNumber.setText(Tools.bankCardChange(_BankCardBean.get_BankCardNumber(),4));
			_BCTV_BankInfo.setText(_BankCardBean.get_RealName());
		}
	}

	@Override
	public void MyBack() {
		// TODO Auto-generated method stub
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
			MyBack();
			break;
		case R.id.act_btn_card_mange_bank_del:
			//删除
			showMyDialog(getResources().getString(string.pop_del_card_str));
			break;
		default:
			break;
		}

	}

	private void showMyDialog(String str) {
		final Dialog mDialog = new Dialog(mContext, R.style.myDilog);
		mDialog.setContentView(R.layout.dialog_layout);
		Button sure = (Button) mDialog.findViewById(R.id.dialog_btn_ok);
		Button back = (Button) mDialog.findViewById(R.id.dialog_btn_cancel);
		TextView _DialogTV = (TextView) mDialog
				.findViewById(R.id.dialog_tv_msg);
		_DialogTV.setText(str);
		mDialog.setCanceledOnTouchOutside(false);

		sure.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				delCard();
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
	 * 删除银行卡
	 */
	private void delCard() {
		// TODO Auto-generated method stub
		CustomProgressDialog.showProgressDialog(mContext, "loading");
		cancelSignInfoReqTask = new CancelSignInfoReqTask();
		cancelSignInfoReqTask.execute();
	}
	/**
	 * 解约申请
	 * @author kevin
	 *
	 */
	private class CancelSignInfoReqTask extends AsyncTask<String , Void, Boolean>{
		String strError;
		JSONObject jsonData;
		Exception exception;
		private Result ReqResult;

		@Override
		protected Boolean doInBackground(String... prams) {
			// TODO Auto-generated method stub
			try {
				UserBean userBean = WDApplication.getInstance().getUserBean();
				if(userBean!=null){
					Map<String, Object> pramsMap = new HashMap<String, Object>();
					pramsMap.put("protocolNo", _BankCardBean.get_ProtocolNo()); 	//协议号
					pramsMap.put("customerNo", userBean.getmUserNO()); 				//用户号
					pramsMap.put("sigProCode", _BankCardBean.get_ConPayProCode()); 	//签约快捷支付产品编码
					ReqResult = ApiClient.CancelSignInfoReq(mContext, pramsMap);
				}
			} catch (Exception e) {
				e.printStackTrace();
				exception = e;
			}
			return ReqResult != null;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			CustomProgressDialog.hideProgressDialog();
			if(exception != null ){
				Toast.makeText(getApplicationContext(), getResources().getString(string.http_error_anomalies), Toast.LENGTH_LONG).show();
				return;
			}

			if(result){
				if(Constant_data.HTTP_SUCCESS.equals(ReqResult.coder)){
					ToastUtil.showShort(mContext, "银行卡删除成功！");
					BankCenterActivity.isRefresh = true;
					startActivity(new Intent(BankCardManageActivity.this, BankCenterActivity.class));
				}else{
					strError = ReqResult.msg;
					ToastUtil.showShort(mContext, strError);
				}
			}else{
				ToastUtil.showShort(mContext, getResources().getString(string.http_error_anomalies));
			}
			super.onPostExecute(result);
		}

		@Override
		protected void onCancelled() {
			cancelSignInfoReqTask = null;
			super.onCancelled();
		}
	}

}
