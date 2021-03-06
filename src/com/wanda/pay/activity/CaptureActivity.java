package com.wanda.pay.activity;

import java.io.IOException;
import java.util.Collection;
import java.util.Hashtable;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.FeatureInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.EncodeHintType;
import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.client.result.ParsedResult;
import com.google.zxing.client.result.ResultParser;
import com.google.zxing.common.HybridBinarizer;
import com.sitech.oncon.barcode.camera.CameraManager;
import com.sitech.oncon.barcode.core.CaptureActivityHandler;
import com.sitech.oncon.barcode.core.FinishListener;
import com.sitech.oncon.barcode.core.InactivityTimer;
import com.sitech.oncon.barcode.core.QRCodeReader;
import com.sitech.oncon.barcode.core.RGBLuminanceSource;
import com.sitech.oncon.barcode.core.TitleView;
import com.sitech.oncon.barcode.core.ViewfinderView;
import com.wanda.pay.R;
import com.wanda.pay.R.string;
import com.wanda.pay.app.WDApplication;
import com.wanda.pay.util.LogUtil;

public final class CaptureActivity extends Activity implements
		SurfaceHolder.Callback {

	private static final String TAG = CaptureActivity.class.getSimpleName();
	private CameraManager cameraManager;
	private CaptureActivityHandler handler;
	private ViewfinderView viewfinderView;
	private Result lastResult;
	private boolean hasSurface;
	private IntentSource source;
	private Collection<BarcodeFormat> decodeFormats;
	private String characterSet;
	private InactivityTimer inactivityTimer;
	private TitleView title;
	// private Button from_gallery;
	private final int from_photo = 010;
	static final int PARSE_BARCODE_SUC = 3035;
	static final int PARSE_BARCODE_FAIL = 3036;
	String photoPath;
	ProgressDialog mProgress;

	public static final int  decode = R.id.decode;
	public static final int decode_failed=R.id.decode_failed;
	public static final int decode_succeeded=R.id.decode_succeeded;
	public static final int launch_product_query=R.id.launch_product_query;
	public static final int quit=R.id.quit;
	public static final int restart_preview=R.id.restart_preview;
	public static final int return_scan_result=R.id.return_scan_result;
	
	private TextView mTitle,mOpenLightView,mOpenLightText;
	private Camera m_Camera;
	private boolean isOpenLight = false;  // 是否打开闪光灯
	private boolean isHaveLight = false;  // 是否有闪光灯
	
	// Dialog dialog;

	enum IntentSource {

		ZXING_LINK, NONE

	}

	Handler barHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case PARSE_BARCODE_SUC:
				// viewfinderView.setRun(false);
				showDialog((String) msg.obj);
				break;
			case PARSE_BARCODE_FAIL:
				// showDialog((String) msg.obj);
				if (mProgress != null && mProgress.isShowing()) {
					mProgress.dismiss();
				}
				new AlertDialog.Builder(CaptureActivity.this)
						.setTitle("提示")
						.setMessage("扫描失败！")
						.setPositiveButton("确定",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										dialog.dismiss();
									}
								}).show();
				break;
			}
			super.handleMessage(msg);
		}

	};

	public ViewfinderView getViewfinderView() {
		return viewfinderView;
	}

	public Handler getHandler() {
		return handler;
	}

	public CameraManager getCameraManager() {
		return cameraManager;
	}

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.capture);
		WDApplication.getInstance().addActivity(this);
		initControl();
		
		PackageManager pm= CaptureActivity.this.getPackageManager();
		FeatureInfo[] features=pm.getSystemAvailableFeatures();
		for(FeatureInfo f : features){
			if(PackageManager.FEATURE_CAMERA_FLASH.equals(f.name)){  //判断设备是否支持闪光灯
				isHaveLight = true;
			mOpenLightView.setClickable(true);
			mOpenLightView.setEnabled(true);

			mOpenLightView.setBackgroundResource(R.drawable.flash_can_open);
			mOpenLightText.setText("开灯");
			}
		}
		
		hasSurface = false;
		inactivityTimer = new InactivityTimer(CaptureActivity.this);

		cameraManager = new CameraManager(getApplication());

		viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);
		viewfinderView.setCameraManager(cameraManager);
		// 为标题和底部按钮添加监听事件
	}
	private void initControl() {
		viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);

		mOpenLightText = (TextView) findViewById(R.id.open_light_text);
		mOpenLightView = (TextView) findViewById(R.id.open_light_view);
		mOpenLightView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(isHaveLight){
					isOpenLight = !isOpenLight;
					if(isOpenLight){
						if ( null == m_Camera){  
							m_Camera = Camera.open();      
						}  

						Camera.Parameters parameters = m_Camera.getParameters();               
						parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);    
						m_Camera.setParameters( parameters );              
						m_Camera.startPreview();

						mOpenLightView.setBackgroundResource(R.drawable.flash_open);
						mOpenLightText.setText("关灯");
					}else{
						Camera.Parameters parameters = m_Camera.getParameters();               
						parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);    
						m_Camera.setParameters( parameters );              
						m_Camera.startPreview();

						mOpenLightView.setBackgroundResource(R.drawable.flash_can_open);
						mOpenLightText.setText("开灯");
					}
				}
			}
		});

		TextView _BCTV_Title = (TextView) findViewById(R.id.activity_title_tv_name);
		_BCTV_Title.setText("扫一扫");
		findViewById(R.id.activity_title_btn_back).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
	public void showDialog(final String msg) {

		if (mProgress != null && mProgress.isShowing()) {
			mProgress.dismiss();
		}
		Intent data = new Intent();
		data.putExtra("msg", msg);
		LogUtil.e("====="+msg);
		setResult(100, data);
		this.finish();

	}


	public String parsLocalPic(String path) {
		String parseOk = null;
		Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
		hints.put(EncodeHintType.CHARACTER_SET, "UTF8");

		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true; // 先获取原大小
		Bitmap bitmap = BitmapFactory.decodeFile(path, options);
		options.inJustDecodeBounds = false; // 获取新的大小
		// 缩放比
		int be = (int) (options.outHeight / (float) 200);
		if (be <= 0)
			be = 1;
		options.inSampleSize = be;
		bitmap = BitmapFactory.decodeFile(path, options);
		int w = bitmap.getWidth();
		int h = bitmap.getHeight();
		
		LogUtil.i(w + "   " + h);
		RGBLuminanceSource source = new RGBLuminanceSource(bitmap);
		BinaryBitmap bitmap1 = new BinaryBitmap(new HybridBinarizer(source));
		QRCodeReader reader2 = new QRCodeReader();
		Result result;
		try {
			result = reader2.decode(bitmap1, hints);
			android.util.Log.i("steven", "result:" + result);
			parseOk = result.getText();

		} catch (NotFoundException e) {
			parseOk = null;
		} catch (ChecksumException e) {
			parseOk = null;
		} catch (FormatException e) {
			parseOk = null;
		}
		return parseOk;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			final Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		android.util.Log.i("steven", "data.getData()" + data);
		if (data != null) {
			mProgress = new ProgressDialog(CaptureActivity.this);
			mProgress.setMessage("正在扫描...");
			mProgress.setCancelable(false);
			mProgress.show();
			final ContentResolver resolver = getContentResolver();
			if (requestCode == from_photo) {
				if (resultCode == RESULT_OK) {
					Cursor cursor = getContentResolver().query(data.getData(),
							null, null, null, null);
					if (cursor.moveToFirst()) {
						photoPath = cursor.getString(cursor
								.getColumnIndex(MediaStore.Images.Media.DATA));
					}
					cursor.close();
					new Thread(new Runnable() {
						@Override
						public void run() {
							Looper.prepare();
							String result = parsLocalPic(photoPath);
							if (result != null) {
								Message m = Message.obtain();
								m.what = PARSE_BARCODE_SUC;
								m.obj = result;
								barHandler.sendMessage(m);
							} else {
								Message m = Message.obtain();
								m.what = PARSE_BARCODE_FAIL;
								m.obj = "扫描失败！";
								barHandler.sendMessage(m);
							}
							Looper.loop();
						}
					}).start();
				}

			}
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		handler = null;
		lastResult = null;
		resetStatusView();
		SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
		SurfaceHolder surfaceHolder = surfaceView.getHolder();
		if (hasSurface) {
			initCamera(surfaceHolder);
		} else {
			surfaceHolder.addCallback(this);
			surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		}
		inactivityTimer.onResume();
		source = IntentSource.NONE;
		decodeFormats = null;
	}

	@Override
	protected void onPause() {
		if (handler != null) {
			handler.quitSynchronously();
			handler = null;
		}
		inactivityTimer.onPause();
		cameraManager.closeDriver();
		if (!hasSurface) {
			SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
			SurfaceHolder surfaceHolder = surfaceView.getHolder();
			surfaceHolder.removeCallback(this);
		}
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		inactivityTimer.shutdown();
		if (mProgress != null) {
			mProgress.dismiss();
		}
		super.onDestroy();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			if ((source == IntentSource.NONE || source == IntentSource.ZXING_LINK)
					&& lastResult != null) {
				restartPreviewAfterDelay(0L);
				return true;
			}
			break;
		case KeyEvent.KEYCODE_VOLUME_DOWN:
			cameraManager.setTorch(false);
			return true;
		case KeyEvent.KEYCODE_VOLUME_UP:
			cameraManager.setTorch(true);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	// 这里初始化界面，调用初始化相机
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		if (holder == null) {
			Log.e(TAG,
					"*** WARNING *** surfaceCreated() gave us a null surface!");
		}
		if (!hasSurface) {
			hasSurface = true;
			initCamera(holder);
		}
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		hasSurface = false;
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {

	}

	private static ParsedResult parseResult(Result rawResult) {
		return ResultParser.parseResult(rawResult);
	}

	// 解析二维码
	public void handleDecode(Result rawResult, Bitmap barcode) {
		inactivityTimer.onActivity();
		playBeepSoundAndVibrate();

		final String resultString = rawResult.getText();
		Intent resultIntent = new Intent();
		Bundle bundle = new Bundle();
		bundle.putString("result", resultString);
		LogUtil.e("===="+resultString);
		resultIntent.putExtras(bundle);
		this.setResult(RESULT_OK, resultIntent);

		finish();
	}
	 private static final long VIBRATE_DURATION = 200L;

	 private void playBeepSoundAndVibrate() {
		 //        if (playBeep && mediaPlayer != null) {
		 //            mediaPlayer.start();
		 //        }
		Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
	    vibrator.vibrate(VIBRATE_DURATION);
	 }
	// 初始化照相机，CaptureActivityHandler解码
	private void initCamera(SurfaceHolder surfaceHolder) {
		if (surfaceHolder == null) {
			throw new IllegalStateException("No SurfaceHolder provided");
		}
		if (cameraManager.isOpen()) {
			Log.w(TAG,
					"initCamera() while already open -- late SurfaceView callback?");
			return;
		}
		try {
			cameraManager.openDriver(surfaceHolder);
			m_Camera =  cameraManager.getCamera();
			if (handler == null) {
				handler = new CaptureActivityHandler(CaptureActivity.this, decodeFormats,
						characterSet, cameraManager);
			}
		} catch (IOException ioe) {
			Log.w(TAG, ioe);
			displayFrameworkBugMessageAndExit();
		} catch (RuntimeException e) {
			Log.w(TAG, "Unexpected error initializing camera", e);
			displayFrameworkBugMessageAndExit();
		}
	}

	private void displayFrameworkBugMessageAndExit() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(getString(R.string.app_name));
		builder.setMessage(getString(R.string.msg_camera_framework_bug));
		builder.setPositiveButton(R.string.confirm, new FinishListener(this));
		builder.setOnCancelListener(new FinishListener(this));
		builder.show();
	}

	public void restartPreviewAfterDelay(long delayMS) {
		if (handler != null) {
			handler.sendEmptyMessageDelayed(R.id.restart_preview, delayMS);
		}
		resetStatusView();
	}

	private void resetStatusView() {
		viewfinderView.setVisibility(View.VISIBLE);
		lastResult = null;
	}

	public void drawViewfinder() {
		viewfinderView.drawViewfinder();
	}

}
