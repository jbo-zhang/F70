package com.hwatong.btphone.ui;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.hwatong.btphone.CallLog;
import com.hwatong.btphone.Contact;

/**
 * 特用于本应用需要点击listVew item弹出拨号按钮 注意:不要在外部调用 setOnItemClickListener
 * 方法，否则原来的点击事件会被覆盖
 * 
 * @author zxy time:2017年6月20日
 * 
 */
public class PopItemButtonListView extends ListView {

	protected View mItemView;// 被点击的ItemView
	protected boolean mItemClickEnable = true;// 设置Item view点击是否有效

	private boolean animationStop = true;

	public PopItemButtonListView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		setOnItemClickListener(mItemClickListener);
	}

	public PopItemButtonListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setOnItemClickListener(mItemClickListener);
	}

	public PopItemButtonListView(Context context) {
		super(context);
		setOnItemClickListener(mItemClickListener);
	}

	private NoDoubleItemClickListener mItemClickListener = new NoDoubleItemClickListener() {

		@Override
		public void onItemClickImp(AdapterView<?> parent, View view,
				int position, long id) {
			if (!mItemClickEnable) {
				return;
			}

			Object object = getItemAtPosition(position);
			String number = null;
			if (object instanceof Contact) {
				number = ((Contact) object).number;
			} else if (object instanceof CallLog) {
				number = ((CallLog) object).number;
			}
			if (TextUtils.isEmpty(number)) {
				return;
			}

			ViewHolder holder = (ViewHolder) view.getTag();
			if (holder == null) {
				return;
			}

			if (mItemView == null && animationStop) {
				showButton(holder);
				mItemView = view;
			} else if (mItemView == view && animationStop) {
				hideButton(holder);
			} else {
				//hideCurrentItemBtn();
				showButton(holder);
				mItemView = view;
			}

		}
	};

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			if (mItemView != null && animationStop) {
				if (ev.getY() > mItemView.getY()
						&& ev.getY() < (mItemView.getY() + mItemView
								.getHeight())) {
					// 表示触摸在当前View内, 让onClick收起
					return true;
				} else {
					hideCurrentItemBtn();
					mItemView = null;
					return true;
				}
			}
			break;

		case MotionEvent.ACTION_MOVE:
			// 触摸到当前View也不可以滑动
			if (mItemView != null) {
				//hideCurrentItemBtn();
				return true;
			}
			break;
		case MotionEvent.ACTION_CANCEL:
		case MotionEvent.ACTION_UP:

			break;
		}
		return super.onTouchEvent(ev);
	}

	public void setItemClickEnable(boolean itemClickEnable) {
		this.mItemClickEnable = itemClickEnable;
		hideCurrentItemBtn();
	}

	public synchronized void hideCurrentItemBtn() {
		if (mItemView != null) {
			hideButton((ViewHolder) mItemView.getTag());
		}
	}
	
	//private long mDuration = 100;
	private long mDuration = 0;
	
	public void setAnimationDuration(long duration) {
//		this.mDuration = duration;
		//外部接口无效化，目的是去掉动画效果。
		this.mDuration = 0;
	}

	protected synchronized void showButton(ViewHolder holder) {
		if (holder == null || holder.mTvNumber == null
				|| holder.mBtnDial == null)
			return;
		holder.mBtnDial.setVisibility(View.VISIBLE);
		float orignalX = holder.mTvNumber.getX();
		float endX = orignalX - holder.mBtnDial.getMeasuredWidth() + 10;
		ObjectAnimator ofFloatX = ObjectAnimator.ofFloat(holder.mTvNumber, "X",
				orignalX, endX);
		ofFloatX.setDuration(mDuration);
		ofFloatX.addListener(new AnimatorListener() {

			@Override
			public void onAnimationStart(Animator arg0) {
				animationStop = false;
			}

			@Override
			public void onAnimationRepeat(Animator arg0) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onAnimationEnd(Animator arg0) {
				animationStop = true;
			}

			@Override
			public void onAnimationCancel(Animator arg0) {
				// TODO Auto-generated method stub
			}
		});

		// ObjectAnimator ofFloatAlpha = ObjectAnimator.ofFloat(holder.mBtnDial,
		// "Alpha", 0.0f, 1.0f);
		ObjectAnimator ofFloatAlpha = ObjectAnimator.ofFloat(holder.mBtnDial,
				"X", orignalX + holder.mTvNumber.getWidth() + 30, endX
						+ holder.mTvNumber.getWidth() + 10);
		ofFloatAlpha.setDuration(mDuration);
		ofFloatAlpha.start();
		ofFloatX.start();
	}

	protected synchronized void hideButton(ViewHolder holder) {
		if (holder == null || holder.mTvNumber == null
				|| holder.mBtnDial == null)
			return;
		float orignalX = holder.mTvNumber.getX();
		float endX = orignalX + holder.mBtnDial.getMeasuredWidth() - 10;

		ObjectAnimator ofFloatX = ObjectAnimator.ofFloat(holder.mTvNumber, "X",
				orignalX, endX);
		// ObjectAnimator ofFloatAlpha = ObjectAnimator.ofFloat(holder.mBtnDial,
		// "Alpha", 1.0f, 0.0f);
		ObjectAnimator ofFloatAlpha = ObjectAnimator.ofFloat(holder.mBtnDial,
				"X", orignalX + holder.mTvNumber.getWidth() + 30, endX
						+ holder.mTvNumber.getWidth() + 30);
		ofFloatX.setDuration(mDuration);
		ofFloatAlpha.setDuration(mDuration);

		ofFloatX.addListener(new AnimatorListener() {

			@Override
			public void onAnimationStart(Animator arg0) {
				animationStop = false;
			}

			@Override
			public void onAnimationRepeat(Animator arg0) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onAnimationEnd(Animator arg0) {
				//mItemView = null;
				animationStop = true;
			}

			@Override
			public void onAnimationCancel(Animator arg0) {
				// TODO Auto-generated method stub
			}
		});

		ofFloatX.start();
		ofFloatAlpha.start();
		
		mItemView = null;
	}
	
}
