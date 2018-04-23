package com.wanda.pay.activity;

public interface IMVCActivity {
	/**
	 * 初始化数据
	 */
	void init();// 初始化数据

	/**
	 * 初始化界面控件
	 */
	void initView();// 初始化界面控件

	/**
	 * 刷新 内容 不知道可变参数
	 * @param param
	 */
	void refresh(Object... param);// 刷新 内容 不知道可变参数
	/**
	 * 自定义返回
	 */
	void MyBack();// 自定义返回

}

