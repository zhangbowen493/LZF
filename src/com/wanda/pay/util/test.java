package com.wanda.pay.util;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.wanda.pay.activity.BaseActivity;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.Context;
import android.test.AndroidTestCase;
import junit.framework.TestSuite;

public class test extends AndroidTestCase {
	
	public void sptest(){
		SPUtils.put(mContext, "key", "kdsa");
		SPUtils.put(mContext, "key1", 1);
		SPUtils.put(mContext, "key2", true);
		SPUtils.put(mContext, "key3", 1.4);
		
		String key = (String) SPUtils.getString(mContext, "key", "");
//		String key1 = (String) SPUtils.get(mContext, "key1", 0);
//		String key2 = (String) SPUtils.get(mContext, "key2", false);
//		String key3 = (String) SPUtils.get(mContext, "key3", 0.0);
		
		LogUtil.i("key="+key );//+" key1="+key1+" key2="+key2+" key3="+key3);
		
	}

	public void test(){
		String s = "{\"head\": {\"a\":\"g\"},\"body\": {}}";
		
		JSONObject jsonObject;
		try {
			jsonObject = new JSONObject(s);
			String optString = jsonObject.optString("body");
			
			if(optString != null){
				JSONObject jsonObject2 = new JSONObject(optString);
				
					LogUtil.e("");
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	public void testq(){
		JSONObject jsonData;
		try {
			jsonData = new JSONObject("{\"head\":{\"operationSys\":\"204\",\"channelType\":\"08\",\"reqSysDate\":\"20150612082032\",\"identifiCation\":\"ZF0001\",\"operationCode\":\"UserNameVerifySendReq\",\"expandAttribute\":\"\"},\"tail\":{\"s_mac\":\"MAC\"},\"body\":{}}");
			Result result = JSONParsing.NoReturnDataRequest(jsonData);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	public  void basne(){
		boolean mobile = Tools.isMobile("186");
		System.out.println(mobile);
		LogUtil.e("================"+mobile);
	}
	
	public boolean isRunningForeground() {
		String packageName = getPackageName(mContext);
		String topActivityClassName = getTopActivityName(mContext);
		if (packageName != null && topActivityClassName != null
				&& topActivityClassName.startsWith(packageName)&& !topActivityClassName.endsWith("LoginActivity")) {
			return true;
		} else {
			return false;
		}
	}

	public String getTopActivityName(Context context) {
		String topActivityClassName = null;
		ActivityManager activityManager = (ActivityManager) (context
				.getSystemService(android.content.Context.ACTIVITY_SERVICE));
		// android.app.ActivityManager.getRunningTasks(int maxNum)
		// int maxNum--->The maximum number of entries to return in the list
		// 即最多取得的运行中的任务信息(RunningTaskInfo)数量
		List runningTaskInfos = activityManager.getRunningTasks(1);
		if (runningTaskInfos != null) {
			// ComponentName f = runningTaskInfos.get(0).topActivity;
			RunningTaskInfo taskInfo = (RunningTaskInfo) runningTaskInfos
					.get(0);
			ComponentName f = taskInfo.topActivity;
			topActivityClassName = f.getClassName();
		}
		// 按下Home键盘后 topActivityClassName=com.android.launcher2.Launcher
		return topActivityClassName;
	}

	public String getPackageName(Context context) {
		String packageName = context.getPackageName();
		return packageName;
	}
	
}
