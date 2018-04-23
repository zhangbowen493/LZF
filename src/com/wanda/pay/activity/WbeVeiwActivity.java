package com.wanda.pay.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.wanda.pay.R;
import com.wanda.pay.app.WDApplication;

public class WbeVeiwActivity extends BaseActivity implements OnClickListener {

	private WebView _WEBview;
	private String _UrlExtra;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_web_view);
		WDApplication.getInstance().addActivity(this);
		initData();
		initview();
	}

	private void initData() {
		// TODO Auto-generated method stub
		Intent intent = getIntent();

		_UrlExtra = intent.getStringExtra("url");
	}

	private void initview() {
		// TODO Auto-generated method stub
		TextView _ACOTV_Title = (TextView) findViewById(R.id.activity_title_tv_name);
		_ACOTV_Title.setText("服务协议");

		findViewById(R.id.activity_title_btn_back).setOnClickListener(this);

		_WEBview = (WebView) findViewById(R.id.act_web_view);

		WebSettings webSettings = _WEBview.getSettings();

		webSettings.setUseWideViewPort(true);// 设置此属性，可任意比例缩放
		webSettings.setLoadWithOverviewMode(true);

		webSettings.setJavaScriptEnabled(true); // 允许运行js
		webSettings.setBuiltInZoomControls(true);

		webSettings.setUseWideViewPort(false); // 将图片调整到适合webview的大小

		webSettings.setSupportZoom(true); // 支持缩放

		webSettings.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN); // 支持内容重新布局

		webSettings.supportMultipleWindows(); // 多窗口

		webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK); // 关闭webview中缓存

		webSettings.setAllowFileAccess(true); // 设置可以访问文件

		webSettings.setNeedInitialFocus(true); // 当webview调用requestFocus时为webview设置节点

		webSettings.setBuiltInZoomControls(true); // 设置支持缩放

		webSettings.setJavaScriptCanOpenWindowsAutomatically(true); // 支持通过JS打开新窗口

		webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小

		webSettings.setLoadsImagesAutomatically(true); // 支持自动加载图片

		_WEBview.loadUrl(_UrlExtra);
		_WEBview.setWebViewClient(webViewClient);
		_WEBview.setWebChromeClient(webChromeClient);
	}

	private WebViewClient webViewClient = new WebViewClient() {
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);

			return true;
		};

		public void onReceivedSslError(WebView view,
				android.webkit.SslErrorHandler handler,
				android.net.http.SslError error) {
			handler.proceed();// 接受证书
		};
	};

	private WebChromeClient webChromeClient = new WebChromeClient() {

	};

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK) && _WEBview.canGoBack()) {
			_WEBview.goBack();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.activity_title_btn_back:
			WbeVeiwActivity.this.finish();
			break;

		default:
			break;
		}
	}

}
