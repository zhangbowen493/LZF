package com.wanda.pay.activity;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.FeatureInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.Size;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.wanda.pay.R;
import com.wanda.pay.app.WDApplication;
import com.wanda.pay.bean.Constant_data;
import com.wanda.pay.util.BitmapUtils;
import com.wanda.pay.util.Tools;
import com.wanda.pay.view.CropImageView;
import com.wanda.pay.view.CropImageView.TakePictureCallback;

public class CameraOrCropActivity extends Activity implements OnClickListener,
		Callback {

	private SurfaceView mSurfaceView;
	private CropImageView mCropImageView;
	private ImageView iv_cor_quit;
	private ImageView iv_cor_ok;
	private Camera camera;
	private Parameters parameters;
	/** 是否拍照模式 */
	private boolean isCamera;
	/** 图片路径 */
	private String path;
	/** 是否打开闪光灯 */
	private boolean isOpenLight = false;
	private ImageView mOpenLightView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 没有标题
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);// 设置全屏
		setContentView(R.layout.activity_camera_or_crop);
		WDApplication.getInstance().addActivity(this);

		initView();
		init();
		// 初始化闪光灯
		if (isCamera)
			initLight();
	}

	private void initLight() {
		PackageManager pm = getPackageManager();
		FeatureInfo[] features = pm.getSystemAvailableFeatures();
		for (FeatureInfo f : features) {
			if (PackageManager.FEATURE_CAMERA_FLASH.equals(f.name)) { // 判断设备是否支持闪光灯
				mOpenLightView.setVisibility(View.VISIBLE);
			}
		}
	}

	private void initView() {
		mSurfaceView = (SurfaceView) findViewById(R.id.surfaceView_COC_camera);
		mCropImageView = (CropImageView) findViewById(R.id.cropimage_COC_crop);
		iv_cor_quit = (ImageView) findViewById(R.id.iv_COC_quit);
		iv_cor_ok = (ImageView) findViewById(R.id.iv_COC_ok);
		mOpenLightView = (ImageView) findViewById(R.id.iv_COC_light);
	}

	private void init() {
		iv_cor_ok.setOnClickListener(this);
		iv_cor_quit.setOnClickListener(this);
		mOpenLightView.setOnClickListener(this);

		path = getIntent().getStringExtra("path");
		if (path != null) {
			// 裁剪图片
			isCamera = false;
			Bitmap bitmap = BitmapFactory.decodeFile(path);
			// 处理图片角度
			int degree = BitmapUtils.readPictureDegree(path);
			Log.i("图片角度", degree + "");
			bitmap = BitmapUtils.rotaingImageView(degree, bitmap);
			mCropImageView.setDrawable(new BitmapDrawable(getResources(),
					bitmap), Constant_data.PHOTO_HEIGHT,
					Constant_data.PHOTO_WIDTH);
			// 设置裁剪框可以移动和改变大小
			mCropImageView.setChangeAndMove(true, true);
		} else {
			// 拍照
			isCamera = true;
			mSurfaceView.setVisibility(View.VISIBLE);
			mSurfaceView.getHolder().setType(
					SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
			// mSurfaceView.getHolder().setFixedSize(mSurfaceView.getWidth(),
			// mSurfaceView.getHeight()); //设置Surface分辨率
			mSurfaceView.getHolder().setKeepScreenOn(true);// 屏幕常亮
			mSurfaceView.getHolder().addCallback(this);// 为SurfaceView的句柄添加一个回调函数
			// 设置裁剪框不可以移动不可以改变大小
			mCropImageView.setChangeAndMove(false, false);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_COC_quit:
			finish();
			break;
		case R.id.iv_COC_ok:
			getPicture();
			break;
		case R.id.iv_COC_light:
			openLight();
			break;

		default:
			break;
		}
	}

	/**
	 * 是否打开闪光灯
	 */
	private void openLight() {
		// TODO Auto-generated method stub
		isOpenLight = !isOpenLight;
		try {
			if (isOpenLight) {
				if (null == camera) {
					camera = Camera.open();
				}

				Camera.Parameters parameters = camera.getParameters();
				parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
				camera.setParameters(parameters);
				camera.startPreview();

				// mOpenLightView.setBackgroundResource(R.drawable.flash_open);
			} else {
				Camera.Parameters parameters = camera.getParameters();
				parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
				camera.setParameters(parameters);
				camera.startPreview();

				// mOpenLightView.setBackgroundResource(R.drawable.flash_can_open);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	private void getPicture() {
		// TODO Auto-generated method stub
		if (isCamera) {
			mCropImageView.takePicture(new TakePictureCallback() {

				@Override
				public void getPictrue(Bitmap bitmap) {
					Intent intent = new Intent();
					RealNameCertificationActivity.bm_shiming = bitmap;
					setResult(Constant_data.RESULTCODE_CAMERA, intent);
					finish();

				}
			});
		} else {
			// 处理图片旋转角度
			Bitmap bitmap = mCropImageView.getCropImage();
			// int degree=BitmapUtils.readPictureDegree(path);
			// bitmap=BitmapUtils.rotaingImageView(degree, bitmap);
			// Log.i("图片大小 2",
			// "b width="+bitmap.getWidth()+" b height="+bitmap.getHeight()+" b size="+bitmap.getRowBytes()*bitmap.getHeight());

			Intent intent = new Intent();
			RealNameCertificationActivity.bm_shiming = bitmap;
			setResult(Constant_data.RESULTCODE_CROP, intent);
			finish();
		}

	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		try {
			parameters = camera.getParameters();
			parameters.setPictureFormat(PixelFormat.JPEG);
			// parameters.setPictureSize(mView.getWidth(), mView.getHeight());
			// // 部分定制手机，无法正常识别该方法。
			parameters.setJpegQuality(100);
			parameters.setFlashMode(parameters.FLASH_MODE_AUTO);

			// 设置对焦 自动对焦或手动对焦
			List<String> focuseMode = parameters.getSupportedFocusModes();
			if (focuseMode.contains(parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
				parameters
						.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);// 1连续对焦
				camera.cancelAutoFocus();// 2如果要实现连续的自动对焦，这一句必须加上
			} else {
				parameters.setFlashMode(parameters.FOCUS_MODE_AUTO);
			}

			// 图片选择角度，三星手机不支持此方法
			// parameters.setRotation(getPreviewDegree(MainActivity.this));
			// Log.i("图片旋转角度", ""+getPreviewDegree(MainActivity.this));

			//
			List<Size> SupportedPreviewSizes = parameters
					.getSupportedPreviewSizes();// 获取支持预览照片的尺寸
			List<Size> supportedPictureSizes = parameters
					.getSupportedPictureSizes();// 获取支持保存图片的尺寸

			// 选择最合适的预览图尺寸
			DisplayMetrics dm = new DisplayMetrics();
			getWindowManager().getDefaultDisplay().getMetrics(dm);
			// 竖屏
			Size size = getOptimalPreviewSize(SupportedPreviewSizes,
					dm.heightPixels, dm.widthPixels);
			// 横屏
			// Size
			// size=getOptimalPreviewSize(SupportedPreviewSizes,dm.widthPixels,
			// dm.heightPixels);

			parameters.setPreviewSize(size.width, size.height);

			// Size size2=getOptimalPreviewSize(supportedPictureSizes,
			// dm.heightPixels,dm.widthPixels);
			// parameters.setPictureSize(size2.width, size2.height);

			camera.setParameters(parameters);
			camera.startPreview();
			// camera.cancelAutoFocus();// 2如果要实现连续的自动对焦，这一句必须加上

			// 设置surface大小适应预览
			// float scale=size.width*1f/size.height;
			// mSurfaceView.setLayoutParams(new
			// RelativeLayout.LayoutParams(dm.widthPixels, (int)
			// (dm.widthPixels*scale)));
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		try {
			camera = Camera.open(); // 打开摄像头
			camera.setPreviewDisplay(holder); // 设置用于显示拍照影像的SurfaceHolder对象

			// 相机选择角度
			camera.setDisplayOrientation(90);
			camera.startPreview(); // 开始预览

			if (getIntent().getIntExtra("requestCole", 0) == 3) {
				// 手持身份证相
				mCropImageView.setCamera(camera,
						Tools.pxTodip(this, mSurfaceView.getWidth()),
						Tools.pxTodip(this, mSurfaceView.getHeight()), true);
			} else {
				mCropImageView.setCamera(camera, Constant_data.float_width,
						Constant_data.float_height, false);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		if (camera != null) {
			camera.release(); // 释放照相机
			camera = null;
		}
	}

	/**
	 * 获取最适合屏幕的照片 尺寸
	 * 
	 * @param sizes
	 * @param w
	 *            要适应的宽
	 * @param h
	 *            要适应的高
	 * @return
	 */
	private static Size getOptimalPreviewSize(List<Size> sizes, int w, int h) {
		final double ASPECT_TOLERANCE = 0.1;
		double targetRatio = (double) w / h;
		if (sizes == null)
			return null;

		Size optimalSize = null;
		double minDiff = Double.MAX_VALUE;

		int targetHeight = h;

		// Try to find an size match aspect ratio and size
		for (Size size : sizes) {
			double ratio = (double) size.width / size.height;
			if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE)
				continue;
			if (Math.abs(size.height - targetHeight) < minDiff) {
				optimalSize = size;
				minDiff = Math.abs(size.height - targetHeight);
			}
		}

		// Cannot find the one match the aspect ratio, ignore the requirement
		if (optimalSize == null) {
			minDiff = Double.MAX_VALUE;
			for (Size size : sizes) {
				if (Math.abs(size.height - targetHeight) < minDiff) {
					optimalSize = size;
					minDiff = Math.abs(size.height - targetHeight);
				}
			}
		}
		return optimalSize;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (isCamera) {
			if (keyCode == KeyEvent.KEYCODE_CAMERA
					|| keyCode == KeyEvent.KEYCODE_SEARCH) {
				getPicture();
				return true;
			} else if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
				mCropImageView.setZoom(mCropImageView.getZoom() - 10);
				return true;
			} else if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
				mCropImageView.setZoom(mCropImageView.getZoom() + 10);
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}

}
