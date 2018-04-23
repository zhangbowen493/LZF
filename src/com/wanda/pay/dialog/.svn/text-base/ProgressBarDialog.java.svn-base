package com.wanda.pay.dialog;


import com.wanda.pay.R;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

public class ProgressBarDialog extends Dialog{
	private static ProgressBarDialog progressBarDialog;

	public ProgressBarDialog(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	public ProgressBarDialog(Context context, int customprogressdialog) {
		// TODO Auto-generated constructor stub
		super(context, customprogressdialog);
	}
	
	public static void setMessage(String message){
		if(progressBarDialog!=null){
			TextView textView=(TextView) progressBarDialog.findViewById(R.id.tv_pbd_message);
			if(textView!=null){
				textView.setText(message);
			}
		}
	}
	public static void setProgress(int progress){
		if(progressBarDialog!=null){
			TextView textView=(TextView) progressBarDialog.findViewById(R.id.tv_pbd_progress);
			ProgressBar progressBar=(ProgressBar) progressBarDialog.findViewById(R.id.progressBar1);
			if(textView!=null && progressBar!=null){
				textView.setText(progress+"%");
				Log.i("progress", textView.isShown()+"");
				progressBar.setMax(100);
				progressBar.setProgress(progress);
			}
		}
	}

	public static ProgressBarDialog createDialog(Context context) {
		progressBarDialog = new ProgressBarDialog(context, R.style.CustomProgressDialog);
		progressBarDialog.setContentView(R.layout.view_progress_bar_dialog);
		progressBarDialog.getWindow().getAttributes().gravity = Gravity.CENTER;
		progressBarDialog.setCanceledOnTouchOutside(false);
		return progressBarDialog;
	}
	
	public static ProgressBarDialog showProgressBarDialog(Context context, String message) {
		if (progressBarDialog == null) {
			progressBarDialog = createDialog(context);
			setMessage(message);
			setProgress(0);
		}
		progressBarDialog.show();
		return progressBarDialog;
	}
	
	public static void hideProgressBarDialog() {
		if (progressBarDialog != null) {
			progressBarDialog.dismiss();
			progressBarDialog = null;
		}
	}

	@Override
	public void onBackPressed() {
		if (progressBarDialog != null) {
			progressBarDialog.dismiss();
			progressBarDialog = null;
		}
	}

}
