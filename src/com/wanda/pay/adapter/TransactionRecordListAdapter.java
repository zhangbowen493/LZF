package com.wanda.pay.adapter;

import java.util.ArrayList;

import com.wanda.pay.R;
import com.wanda.pay.R.color;
import com.wanda.pay.R.dimen;
import com.wanda.pay.R.string;
import com.wanda.pay.bean.BankBean;
import com.wanda.pay.bean.TransactionRecordBean;
import com.wanda.pay.util.TimerTools;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class TransactionRecordListAdapter extends BaseAdapter {

	private Context mContext;
	private ArrayList<TransactionRecordBean> mData;
	private LayoutInflater inflater;
	
	
	public TransactionRecordListAdapter(Context context, ArrayList<TransactionRecordBean> data) {
		super();
		this.mContext = context;
		this.mData = data;
		inflater = LayoutInflater.from(mContext);
	}
	public void setDate(ArrayList<TransactionRecordBean> data){
		this.mData = data;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if(mData!=null){
			return mData.size();
		}
		return 0;
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		if(mData!=null){
			return mData.get(arg0);
		}
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		if(arg1==null){
			holder = new ViewHolder();
			arg1 = inflater.inflate(R.layout.item_transaction_records,arg2,false );
			holder._TV_Type = (TextView) arg1.findViewById(R.id.item_tv_record_transaction2_type);
			holder._TV_Money = (TextView) arg1.findViewById(R.id.item_tv_record_transaction2_money);
			holder._TV_Time = (TextView) arg1.findViewById(R.id.item_tv_record_transaction2_time);
			holder._TV_State = (TextView) arg1.findViewById(R.id.item_tv_record_transaction2_state);
			arg1.setTag(holder);
		}else{
			holder = (ViewHolder) arg1.getTag();
		}
		TransactionRecordBean bean = mData.get(arg0);
			
		long date2long = TimerTools.date2long(bean.getTradeTime(), mContext.getResources().getString(string.yyyyMMddHHmmss));
		SpannableString ss1=new SpannableString(TimerTools.getWeek(date2long)+"\n"+TimerTools.getDateFormat(date2long+"",mContext.getResources().getString(string.mm_dd)));
		ss1.setSpan(new AbsoluteSizeSpan((int) mContext.getResources().getDimension(dimen.font_size_11)), 2, ss1.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		holder._TV_Time.setText(ss1);
		
		holder._TV_State.setText("\n"+bean.getState());
		
		String type="";
		String sign=" ";
		Drawable drawable=null;
		switch (bean.getTradeTypeInt()) {
		case 12:
			drawable = mContext.getResources().getDrawable(R.drawable.icon_chongzhi);
			type="充值";
			sign="+";
			break;
		case 13:
			drawable = mContext.getResources().getDrawable(R.drawable.icon_tixian);
			type="提现";
			sign="-";
			break;
		case 15:
			drawable = mContext.getResources().getDrawable(R.drawable.icon_zhuanzhang);
			type="转账";
			if(bean.getmTransFerFlag().equals("1")){
				sign="+";
			}else{
				sign="-";
			}
			break;
		case 3:
			drawable = mContext.getResources().getDrawable(R.drawable.icon_qita);
			type="其他";
			sign="+";
			break;

		default:
			drawable = mContext.getResources().getDrawable(R.drawable.icon_qita);
			type="其他";
			sign="-";
			break;
		}
		
		holder._TV_Type.setText(type);
		 if(drawable!=null){
			 drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());  
			 holder._TV_Type.setCompoundDrawables(null, drawable, null, null); 
		 }
		 
		 SpannableString ss2=new SpannableString(sign+bean.getTradeAmount()+"\n"+bean.getmMerName());
		 ss2.setSpan(new AbsoluteSizeSpan((int) mContext.getResources().getDimension(dimen.font_size_14)), bean.getTradeAmount().length()+1, ss2.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		 ss2.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(color.gray_tv_item_trsr)), bean.getTradeAmount().length()+1, ss2.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		 holder._TV_Money.setText(ss2);
		return arg1;
	}
	
	private class ViewHolder{
		TextView _TV_Type;
		TextView _TV_Money;
		TextView _TV_Time;
		TextView _TV_State;
		
		
	}

}
