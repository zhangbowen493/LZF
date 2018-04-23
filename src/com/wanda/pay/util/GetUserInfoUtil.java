package com.wanda.pay.util;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.Toast;

import com.wanda.pay.R.string;
import com.wanda.pay.app.WDApplication;
import com.wanda.pay.bean.Constant_data;
import com.wanda.pay.bean.UserBean;
import com.wanda.pay.dialog.CustomProgressDialog;
/**
 * 异步获取用户信息，并将信息保存于application中
 * @author Luckydog
 *
 */
public class GetUserInfoUtil extends AsyncTask<String , Void, Boolean>{
		public static final int USERBEAN = 1;
		String strError;
		Exception exception;
		private Result GetUserResult;
		private Context mContext;
		private UserBean mUserBean;

		public GetUserInfoUtil(Context mContext) {
			super();
			this.mContext = mContext;
		}

		@Override
		protected Boolean doInBackground(String... prams) {
			// TODO Auto-generated method stub
			try {
				Map<String, Object> pramsMap = new HashMap<String, Object>();
				String _UserNameType = SPUtils.getString(mContext, Constant_data.sp_key_userNameType, "1");
				String _LoginName = SPUtils.getString(mContext, Constant_data.sp_key_loginName, "");
				if(Constant_data.LoginType_Mobile.equals(_UserNameType)){
					pramsMap.put("loginType", "2");
				}else if(Constant_data.LoginType_Email.equals(_UserNameType)){
					pramsMap.put("loginType", "3");
				}else{
					pramsMap.put("loginType", "5");
				}
				pramsMap.put("loginName", _LoginName);

				GetUserResult = ApiClient.GetUserInfo(mContext, pramsMap);
			} catch (Exception e) {
				e.printStackTrace();
				exception = e;
			}
			return GetUserResult != null;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			CustomProgressDialog.hideProgressDialog();
			if(exception != null ){
				Toast.makeText(mContext, mContext.getResources().getString(string.http_error_anomalies), Toast.LENGTH_LONG).show();
				return;
			}
			if(result){
				if(Constant_data.HTTP_SUCCESS.equals(GetUserResult.coder)){
					mUserBean = JSONParsing.GetUserInfo(GetUserResult.jsonBodyObject);
					if(mUserBean!=null){
						Handler handler=new Handler();
						handler.sendEmptyMessage(USERBEAN);
						//增加一个线程锁，通知mainActivity已经拿到数据
//						synchronized (WDApplication.getInstance()) {
							WDApplication.getInstance().setUserBean(mUserBean);
							Intent intent = new Intent("com.wonders.MUSERBEAN_REFRESH");
							LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
							//LogUtil.d("获得余额时间"+System.currentTimeMillis());
							WDApplication.getInstance().isRefreshUserInfo = false;
//				            }
//				            notifyAll();
					}
				}else{
					strError = GetUserResult.msg;
					ToastUtil.showShort(mContext, strError);
				}
			}else{
				ToastUtil.showShort(mContext, mContext.getResources().getString(string.http_error_anomalies));
			}
			super.onPostExecute(result);
		}

		@Override
		protected void onCancelled() {
			
			super.onCancelled();
		}

	
}
