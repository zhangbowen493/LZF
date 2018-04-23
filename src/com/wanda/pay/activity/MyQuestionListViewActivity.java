package com.wanda.pay.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.wanda.pay.R;
import com.wanda.pay.adapter.QuestionsAdapter;
import com.wanda.pay.app.WDApplication;
import com.wanda.pay.util.ImmersedStatusbarUtils;

/**
 * 密保问题列表
 * 
 * @author Luckydog
 * 
 */
public class MyQuestionListViewActivity extends BaseActivity implements IMVCActivity,
		OnClickListener,OnItemClickListener {
	private ListView QuestionsList;
	private QuestionsAdapter adapter;
	private ArrayList<String> data = new ArrayList<String>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_question_listview);
		ImmersedStatusbarUtils.initAfterSetContentView(this, null);
		WDApplication.getInstance().addActivity(this);
		init();
		initView();
		
	}


	@Override
	public void init() {
		if(data==null){
			data = new ArrayList<String>();
		}
		data.clear();
		data.add(0, "你的出生地？");
		data.add(1, "你父亲的生日是？");
		data.add(2, "你母亲的生日是？");
		data.add(3, "你配偶的生日是多少？");
		data.add(4, "你孩子的名字是什么？");
		data.add(5, "你小学学校的名称是？");
		data.add(6, "你中学学校的名称是？");
		data.add(7, "你初中班主任的名字是？");
		data.add(8, "你高中班主任的名字是？");
		data.add(9, "你大学辅导员的名字是？");
		data.add(10, "你的大学学号是？");
		data.add(11, "你的最喜爱的游戏是？");
		
	}

	@Override
	public void initView() {
		TextView _RCTV_Title = (TextView) findViewById(R.id.activity_title_tv_name);
		_RCTV_Title.setText("密保问题列表");
		findViewById(R.id.activity_title_btn_back).setOnClickListener(this);
		QuestionsList =  (ListView) findViewById(R.id.lv_question);
		QuestionsList.setOnItemClickListener(this);
		adapter=new QuestionsAdapter(this, data);
		QuestionsList.setAdapter(adapter);
	}

	@Override
	public void refresh(Object... param) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.activity_title_btn_back:
			//返回
			MyBack();
			break;
		default:
			break;
		}
	
		
	}

	@Override
	public void MyBack() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		setResult(912, getIntent().putExtra("number", position));
		this.finish();
	}

}
