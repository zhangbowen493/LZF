package com.wonders.pay.viewpager;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class PayViewPager extends ViewPager {
	private boolean isCanScroll = true;

	public PayViewPager(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public PayViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public void setScanScroll(boolean isCanScroll) {
		this.isCanScroll = isCanScroll;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		/* return false;//super.onTouchEvent(arg0); */
		if (isCanScroll)
			return false;
		else
			return super.onTouchEvent(event);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent event) {
		if (isCanScroll)
			return false;
		else
			return super.onInterceptTouchEvent(event);
	}

	@Override
	public void setCurrentItem(int item, boolean smoothScroll) {
		super.setCurrentItem(item, smoothScroll);
	}

	@Override
	public void setCurrentItem(int item) {
		super.setCurrentItem(item);
	}
}
