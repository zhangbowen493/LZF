package com.wanda.pay.activity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;

import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore.Images;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.wanda.pay.R;
import com.wanda.pay.app.WDApplication;
import com.wanda.pay.bean.Constant_data;
import com.wanda.pay.dialog.ProgressBarDialog;
import com.wanda.pay.util.AlertDialogUtils;
import com.wanda.pay.util.BitmapUtils;
import com.wanda.pay.util.ImmersedStatusbarUtils;
import com.wanda.pay.util.NetUtils;
/**
 * 实名认证模块
 * @author Luckydog
 *
 */
public class RealNameCertificationActivity extends BaseActivity implements
		OnClickListener, IMVCActivity {
	private Bitmap[] photos;
	private Button btn_authentication_submit;
	protected AuthenticationTask authenticationTask;
	private ImageView tv_photo_up;
	private ImageView tv_photo_down;
	private ImageView tv_photo_handle;
	public static Bitmap bm_shiming;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_realnamecertification);
		ImmersedStatusbarUtils.initAfterSetContentView(this, null);
		WDApplication.getInstance().addActivity(this);
		if (savedInstanceState != null) {
			photos = (Bitmap[]) savedInstanceState.get("photos");
			Log.i("photos", "" + (photos == null));
		}
		if (photos == null) {
			photos = new Bitmap[3];
		}
		initView();
		WDApplication.getInstance().mScreenManager.printAllActivitys();
	}

	@Override
	public void onClick(View view) {
		int requestCode = 0;
		switch (view.getId()) {
		// 身份证正面
		case R.id.tv_photo_up:
			requestCode = 1;
			break;
		// 身份证反面
		case R.id.tv_photo_down:
			requestCode = 2;
			break;
		// 手持身份证正面
		case R.id.tv_photo_handle:
			requestCode = 3;
			break;

		default:
			break;
		}
		if (requestCode == 0)
			return;

		// 相册
		Intent galleryIntent = new Intent(Intent.ACTION_PICK);
		galleryIntent.setDataAndType(Images.Media.EXTERNAL_CONTENT_URI,
				"image/*");

		// 自定义相机
		Intent cameraIntent = new Intent(this, CameraOrCropActivity.class);
		cameraIntent.putExtra("requestCole", requestCode);

		Intent intent = Intent.createChooser(galleryIntent, "选择照片");
		intent.putExtra(Intent.EXTRA_INITIAL_INTENTS,
				new Intent[] { cameraIntent });

		WDApplication.isBackGround=true;
		startActivityForResult(intent, requestCode);

	}

	@Override
	public void init() {
		// TODO Auto-generated method stub

	}

	@Override
	public void initView() {
		TextView _BCTV_Title = (TextView) findViewById(R.id.activity_title_tv_name);
		_BCTV_Title.setText("实名认证");

		tv_photo_up= (ImageView) findViewById(R.id.tv_photo_up);
		tv_photo_up.setOnClickListener(this);
		tv_photo_down= (ImageView) findViewById(R.id.tv_photo_down);
		tv_photo_down.setOnClickListener(this);
		tv_photo_handle= (ImageView) findViewById(R.id.tv_photo_handle);
		tv_photo_handle.setOnClickListener(this);
		btn_authentication_submit = (Button) findViewById(R.id.btn_act_photo_commit);
		btn_authentication_submit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (photos[0] != null && photos[1] != null && photos[2] != null) {
					// 判断网络状态
					if (NetUtils
							.isConnected(RealNameCertificationActivity.this)) {
						// 有网络
						if (NetUtils.isWifi(RealNameCertificationActivity.this)) {
							submit();
						} else {
							AlertDialogUtils.show(
									RealNameCertificationActivity.this,
									"移动网络提醒", "现在正使用移动网络，是否上传？", "取消",
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											// 取消
											dialog.dismiss();
										}
									}, null, null, "确定",
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											// 上传
											submit();
										}
									});
						}
					} else {
						// 没有网络连接，打开网络设置
						AlertDialogUtils.show(
								RealNameCertificationActivity.this, "无网络连接",
								"无网络连接，是否去设置网络？", "取消",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										// 取消
										dialog.dismiss();
									}
								}, null, null, "确定",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										// 打开网络
										NetUtils.openSetting(RealNameCertificationActivity.this);
									}
								});
					}
				} else {
					Toast.makeText(RealNameCertificationActivity.this,
							"还有照片没有添加", Toast.LENGTH_SHORT).show();
					;
				}
			}
		});

	}

	/**
	 * 上传图片
	 */
	private void submit() {
		btn_authentication_submit.setEnabled(false);
		// 提交实名认证
		authenticationTask = new AuthenticationTask();
		authenticationTask.execute();
	}

	@Override
	public void refresh(Object... param) {
		// TODO Auto-generated method stub

	}

	@Override
	public void MyBack() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		if(resultCode==RESULT_OK && requestCode!=0){
			//图库选择返回的
			Uri uri = data.getData();
			ContentResolver cr = getContentResolver();
			Cursor c = cr.query(uri, new String[]{Images.Media.DATA}, null, null, null);
			c.moveToNext();
			String path = c.getString(0);
			c.close();
			
			int recode=0;
			switch (requestCode) {
			case 1:
				recode=4;
				break;
			case 2:
				recode=5;
				break;
			case 3:
				Bitmap bitmap=BitmapUtils.getCompressBitmapByPath(path, Constant_data.PHOTO_WIDTH, Constant_data.PHOTO_SCALE, Constant_data.PHOTO_SIZE);
				//恢复旋转的图片
				int degree=BitmapUtils.readPictureDegree(path);
				bitmap=BitmapUtils.rotaingImageView(degree, bitmap);
				
				tv_photo_handle.setImageBitmap(bitmap);
				photos[2]=bitmap;
				return;
				
			default:
				break;
			}
			Intent intent=new Intent(this,CameraOrCropActivity.class);
			intent.putExtra("path", path);
			startActivityForResult(intent, recode);
			
		}else if(resultCode == Constant_data.RESULTCODE_CROP){
			//裁剪图片返回
			//Bitmap bitmap=data.getParcelableExtra("bitmap");
			Bitmap bitmap=bm_shiming;
			switch (requestCode) {
			case 4:
				tv_photo_up.setImageBitmap(bitmap);
				photos[0]=bitmap;
				break;
			case 5:
				tv_photo_down.setImageBitmap(bitmap);
				photos[1]=bitmap;
				break;
			case 6:
				tv_photo_handle.setImageBitmap(bitmap);
				photos[2]=bitmap;
				break;
				
			default:
				break;
			}
		}else if(resultCode == Constant_data.RESULTCODE_CAMERA){
			//拍照返回
			//Bitmap bitmap=data.getParcelableExtra("bitmap");
			Bitmap bitmap=bm_shiming;
			switch (requestCode) {
			case 1:
				tv_photo_up.setImageBitmap(bitmap);
				photos[0]=bitmap;
				break;
			case 2:
				tv_photo_down.setImageBitmap(bitmap);
				photos[1]=bitmap;
				break;
			case 3:
				tv_photo_handle.setImageBitmap(bitmap);
				photos[2]=bitmap;
				break;
				
			default:
				break;
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
		Log.i("photos", Arrays.toString(photos));
	}
	
	
	 
	 @Override
	protected void onDestroy() {
		if(bm_shiming!=null){
			bm_shiming.recycle();
			bm_shiming=null;
		}
		if(authenticationTask!=null){
			authenticationTask.cancel(true);
		}
		for(Bitmap bm:photos){
			if(bm!=null){
				bm.recycle();
				bm=null;
			}
		}
		WDApplication.isBackGround=false;
		super.onDestroy();
		Log.i("authentication", "ondestroy");
	}
	 @Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		if(photos[0]!=null || photos[1]!=null || photos[2]!=null)
			outState.putParcelableArray("photos", photos);
		Log.i("authentication", "onSaveInstanceState");
	}
	
	private class AuthenticationTask extends
			AsyncTask<String, Integer, Boolean> {

		@Override
		protected Boolean doInBackground(String... params) {
			for (int i = 0; i < photos.length; i++) {
				OutputStream stream = new ByteArrayOutputStream();
				photos[i].compress(CompressFormat.JPEG, 100, stream);
				for (int j = 0; j < 101; j++) {
					publishProgress(i + 1, j);
					try {
						Thread.sleep(30);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				try {
					stream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			return null;
		}

		@Override
		protected void onPreExecute() {
			ProgressBarDialog.showProgressBarDialog(
					RealNameCertificationActivity.this, "正在上传");
			super.onPreExecute();
		}

		@Override
		protected void onPostExecute(Boolean result) {

			ProgressBarDialog.setMessage("上传完成" + photos.length + "/"
					+ photos.length);
			ProgressBarDialog.setProgress(100);
			ProgressBarDialog.hideProgressBarDialog();

			btn_authentication_submit.setEnabled(true);
			super.onPostExecute(result);
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			ProgressBarDialog
					.setMessage("上传" + values[0] + "/" + photos.length);
			ProgressBarDialog.setProgress(values[1]);
			super.onProgressUpdate(values);
		}

	}
}
