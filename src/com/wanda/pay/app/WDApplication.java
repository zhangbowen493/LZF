package com.wanda.pay.app;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.app.Application;
import android.telephony.TelephonyManager;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.wanda.pay.R;
import com.wanda.pay.activity.MainViewPagerActivity;
import com.wanda.pay.bean.Constant_data;
import com.wanda.pay.bean.UserBean;
import com.wanda.pay.util.LogUtil;
import com.wanda.pay.util.SPUtils;
import com.wanda.pay.util.ScreenManager;

public class WDApplication extends Application {
	/** passguard.jar libpassguard.so x86文件夹 加载代码 文件加载到内存 */
	static {
		System.loadLibrary("PassGuard");
	}

	/** passguard.jar 加载代码 文件加载到内存 结束 */

	public static boolean isAddCard = false;
	public static boolean isAddSuccess = false;
	public static boolean isBackGround = false;

	// 运用list来保存们每一个activity是关键
	private List<Activity> mList = new LinkedList<Activity>();
	private UserBean userBean;
	// 为了实现每次使用该类时不创建新的对象而创建的静态对象
	private static WDApplication instance;
	public ScreenManager mScreenManager;// 堆栈管理工具
	public static InputStream inputStream;

	public boolean isRefreshUserInfo = false;
	private MainViewPagerActivity acticity;

	@Override
	public void onCreate() {
		super.onCreate();
		// CrashHandler.getInstance().init(getApplicationContext());
		if (instance == null) {
			instance = this;
		}
		String imei = (String) SPUtils.get(this, Constant_data.Phone_imei, "");
		if ("".equals(imei)) {
			TelephonyManager tm = (TelephonyManager) this
					.getSystemService(this.TELEPHONY_SERVICE);
			String phoneimei = tm.getDeviceId();
			if (phoneimei != null) {
				SPUtils.put(getApplicationContext(), Constant_data.Phone_imei,
						phoneimei);
			}
		}

		try {
			inputStream = getAssets().open("ca.crt");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		setActicity(acticity);

		DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.icon_stub_newskin) // 设置图片下载期间显示的图片
				.showImageForEmptyUri(R.drawable.icon_empty_newskin) // 设置图片Uri为空或是错误的时候显示的图片
				.showImageOnFail(R.drawable.icon_error_newskin) // 设置图片加载或解码过程中发生错误显示的图片
				.cacheInMemory(true) // 设置下载的图片是否缓存在内存中
				.cacheOnDisc(true) // 设置下载的图片是否缓存在SD卡中
				// .displayer(new RoundedBitmapDisplayer(20)) // 设置成圆角图片
				.build(); // 创建配置过得DisplayImageOption对象

		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				getApplicationContext())
				.defaultDisplayImageOptions(defaultOptions)
				.threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				.discCacheFileNameGenerator(new Md5FileNameGenerator())
				.tasksProcessingOrder(QueueProcessingType.LIFO).build();
		ImageLoader.getInstance().init(config);
	}

	public void setActicity(MainViewPagerActivity activity) {
		this.acticity = activity;
	}

	public MainViewPagerActivity getActicity() {
		return acticity;
	}

	// 实例化一次
	public synchronized static WDApplication getInstance() {
		if (null == instance) {
			instance = new WDApplication();
		}
		return instance;
	}

	public UserBean getUserBean() {
		return userBean;
	}

	public void setUserBean(UserBean userBean) {
		this.userBean = userBean;
	}

	// add Activity
	public void addActivity(Activity activity) {
		mList.add(activity);
	}

	// 关闭每一个list内的activity
	public void exit() {
		try {
			for (Activity activity : mList) {
				if (activity != null)
					activity.finish();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			System.exit(0);
		}
	}

	// 杀进程
	public void onLowMemory() {
		super.onLowMemory();
		System.gc();
	}
}
