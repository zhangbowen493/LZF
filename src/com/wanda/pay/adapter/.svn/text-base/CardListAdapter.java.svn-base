package com.wanda.pay.adapter;

import java.util.ArrayList;

import com.wanda.pay.R;
import com.wanda.pay.activity.AddCardOneActivity;
import com.wanda.pay.bean.BankCardBean;
import com.wanda.pay.util.LogUtil;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
/**
 * 银行卡列表适配器
 * @author kevin
 *
 */
public class CardListAdapter extends BaseAdapter {

	private Context mContext;
	private ArrayList<BankCardBean> mData;
	private LayoutInflater inflater;


	public CardListAdapter(Context context, ArrayList<BankCardBean> data) {
		super();
		this.mContext = context;
		this.mData = data;
		inflater = LayoutInflater.from(mContext);
	}

	public void setData(ArrayList<BankCardBean> data){
		this.mData = data;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if(mData!=null){
			return mData.size()+1;
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
		//		if(arg1==null){
		holder = new ViewHolder();
		arg1 = inflater.inflate(R.layout.item_card_bank,arg2,false);
		holder._TV_BankInfo = (TextView) arg1.findViewById(R.id.item_tv_card_info_list);
		holder._TV_BankTypeInfo = (TextView) arg1.findViewById(R.id.item_tv_card_type_list);
		holder._TV_BankNumberInfo = (TextView) arg1.findViewById(R.id.item_tv_card_number_list);
		holder._IV_BankIcon =  (ImageView) arg1.findViewById(R.id.iv_bankicon);
		holder._RL_View = (RelativeLayout) arg1.findViewById(R.id.item_rl_card_info_view);
		holder._RL_AddCard=(RelativeLayout) arg1.findViewById(R.id.rl_add_card);
		arg1.setTag(holder);
		//		}else{
		//			holder = (ViewHolder) arg1.getTag();
		//		}

		if(arg0==mData.size()){
			holder._RL_AddCard.setVisibility(View.VISIBLE);
			holder._TV_BankInfo.setVisibility(View.GONE);
			holder._TV_BankTypeInfo.setVisibility(View.GONE);
			holder._TV_BankNumberInfo.setVisibility(View.GONE);
			holder._IV_BankIcon.setVisibility(View.GONE);
			//			holder._TV_BankInfo.setCompoundDrawables(null, null, null, null);
			holder._RL_View.setVisibility(View.VISIBLE);
			arg1.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					mContext.startActivity(new Intent(mContext, AddCardOneActivity.class));
				}
			});
		}else {
			BankCardBean bankCard = mData.get(arg0);
			String bankName = bankCard.get_BankName();
			String bankType=bankCard.get_CardTypeCode();
			String bankCardNumber = bankCard.get_BankCardNumber();
			String bankLogo=bankCard.get_Logo();
			//holder._IV_BankIcon.setImageBitmap(BitmapFactory.decodeFile(bankLogo));
			holder._TV_BankInfo.setText(bankName);
			holder._TV_BankTypeInfo.setText("储蓄卡");
			//holder._TV_BankTypeInfo.setText(bankType);
			//银行卡序号替换为*号
			StringBuilder str=new StringBuilder();
			for (int i = 0; i < bankCardNumber.length()-4; i++) {
				if(i!=0&&i%4==0)str.append("  ");
				str.append("*");
				if(i==bankCardNumber.length()-5)str.append("  ");
			}
			
			holder._TV_BankNumberInfo.setText(bankCardNumber.replace(bankCardNumber.substring(0, bankCardNumber.length()-4), str));
			//holder._TV_BankNumberInfo.setText(bankCardNumber.substring(bankCardNumber.length()-4, bankCardNumber.length()));
			LogUtil.d("银行LOGO地址"+bankLogo);
			holder._RL_View.setVisibility(View.GONE);
		}


		return arg1;
	}

	private class ViewHolder{
		TextView _TV_BankInfo;
		TextView _TV_BankTypeInfo;
		TextView _TV_BankNumberInfo;
		RelativeLayout _RL_View;
		ImageView _IV_BankIcon;
		//添加银行卡条目
		RelativeLayout _RL_AddCard;
	}

}
