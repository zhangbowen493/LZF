package com.wanda.pay.util;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface.OnClickListener;

public class AlertDialogUtils {

	public static Dialog show(Context mContext,String title,String message,String negative,OnClickListener negativeListener,
			String neutral,OnClickListener neutralListener,String positive,OnClickListener positiveListener ){
		AlertDialog.Builder builder=new AlertDialog.Builder(mContext);
		if(title!=null)builder.setTitle(title);
		if(message!=null)builder.setMessage(message);
		if(negative!=null)builder.setNegativeButton(negative, negativeListener);
		if(neutral!=null)builder.setNeutralButton(neutral, neutralListener);
		if(positive!=null)builder.setPositiveButton(positive, positiveListener);
		return builder.show();
	}
}
