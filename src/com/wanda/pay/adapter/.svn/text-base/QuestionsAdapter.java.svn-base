package com.wanda.pay.adapter;

import java.util.ArrayList;

import com.wanda.pay.R;
import com.wanda.pay.bean.BankBean;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
/**
 * 密保问题
 * @author Administrator
 *
 */
public class QuestionsAdapter extends BaseAdapter {

	private Context mContext;
	private ArrayList<String> mData;
	private LayoutInflater inflater;
	
	
	public QuestionsAdapter(Context context, ArrayList<String> data) {
		super();
		this.mContext = context;
		this.mData = data;
		inflater = LayoutInflater.from(mContext);
	}
	
	public void setData(ArrayList<String> data){
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
			arg1 = inflater.inflate(R.layout.item_sp_quest,arg2,false );
			holder._TV_quest = (TextView) arg1.findViewById(R.id.item_tv_bank_name_list);
			arg1.setTag(holder);
		}else{
			holder = (ViewHolder) arg1.getTag();
		}
			String quest = mData.get(arg0);
			
			holder._TV_quest.setText(quest);
		
		return arg1;
	}
	
	private class ViewHolder{
		TextView _TV_quest;
	}

}
