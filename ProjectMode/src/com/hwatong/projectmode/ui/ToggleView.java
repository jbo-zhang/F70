package com.hwatong.projectmode.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.hwatong.projectmode.R;

/**
 * 自定义开关控件
 * 
 * @author hss
 */
public class ToggleView extends View {
	private static final String TAG = "ToogleView";
	private Bitmap sliding_background;
	private Bitmap switch_background;
	private boolean isSliding = false;
	private boolean toggle_state = false;
	private int downX;
	private OnToggleStateChangeListener mToggleStateChangeListener;

	// 构造方法，在xml文件布局的时候，指定了style的时候调用
	public ToggleView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	// 构造方法，在xml文件中布局的时候，没有指定style的时候调用
	public ToggleView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		
		TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.toggle);
		int toggle_switchbackground = ta.getResourceId(R.styleable.toggle_switchBackground, -1);
		int toggle_slidingbackground = ta.getResourceId(R.styleable.toggle_slidingBackground, -1);
		boolean toggle_state = ta.getBoolean(R.styleable.toggle_toggleState, false);
		ta.recycle();  //注意回收
		
		// 在Java代码中 获取到xml中自定义属性对应的值
		Log.i(TAG, "" + toggle_slidingbackground + "  " + toggle_switchbackground);
		// 设置自定义开关的图片
		setToggleSwitchBackground(toggle_switchbackground);
		setToggleSlidingBackground(toggle_slidingbackground);
		setToggleState(toggle_state);
	}

	// 构造方法 在代码中new的时候调用
	public ToggleView(Context context) {
		this(context, null);

	}

	/**
	 * 给滑动的控件设置背景图片
	 * 
	 * @param toggle_slidingbackground
	 *            图片ID
	 */
	private void setToggleSlidingBackground(int toggle_slidingbackground) {
		sliding_background = BitmapFactory.decodeResource(getResources(), toggle_slidingbackground);
	}

	/**
	 * 给背景的控件，设置背景图片
	 * 
	 * @param toggle_switchbackground
	 *            图片ID
	 */
	private void setToggleSwitchBackground(int toggle_switchbackground) {
		switch_background = BitmapFactory.decodeResource(getResources(), toggle_switchbackground);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// 测量控件的大小，设置控件的大小为背景图片的大小
		setMeasuredDimension(switch_background.getWidth(), switch_background.getHeight());
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// 开始画自定义控件，使用canvas对象先把背景图片画上来
		canvas.drawBitmap(switch_background, 0, 0, null);
		if (isSliding) {
			// 如果是滑动状态
			// 控件距离左边的相对距离为：（控件每时每刻的距离自己左上方的焦点的x轴距离）-（控件本身一半的x轴宽度）
			int left = downX - sliding_background.getWidth() / 2;
			// 控件最大的滑动距离（距离左边最大的距离）就是：（背景图片的宽度）-（滑块图片的宽度）
			int rightAlign = switch_background.getWidth() - sliding_background.getWidth();
			// 如果距离左边的距离小于0，，就不让他继续往左边动了
			if (left < 0) {
				left = 0;
			} else if (left > rightAlign) {
				// 如果距离左边的距离》应该距离左边的最大距离，也不让他往右边移动了
				left = rightAlign;
			}
			// 控制好属性之后就可以时时刻刻的跟着画了
			canvas.drawBitmap(sliding_background, left, 0, null);
		} else {
			// 如果不滑动，则根据控件的属性中开关的状态，来画滑块的位置
			if (toggle_state) {
				// 如果开关状态为真，滑块移动到最右边
				int left = switch_background.getWidth() - sliding_background.getWidth();
				canvas.drawBitmap(sliding_background, left, 0, null);
			} else {
				// 如果开关状态为假，滑块移动到最左边
				canvas.drawBitmap(sliding_background, 0, 0, null);
			}
		}
		super.onDraw(canvas);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// 重写触摸事件
		int action = event.getAction();
		switch (action) {
		case MotionEvent.ACTION_DOWN:
			// 开始点击的时候，是否滑动置为真，获取到当前手指的距离
			isSliding = true;
			downX = (int) event.getX();
			break;
		case MotionEvent.ACTION_MOVE:
			downX = (int) event.getX();
			break;
		case MotionEvent.ACTION_UP:
			// 当点击结束的时候将是否滑动记为假，获取到移动的x轴的坐标
			downX = (int) event.getX();
			isSliding = false;
			// 获取到背景图片中间的那个值
			int center = switch_background.getWidth() / 2;

			boolean state = downX > center;
			// 如果先后的状态不相同，则将新的状态赋给成员变量，然后调用监听的方法
			if (toggle_state != state) {
				toggle_state = state;
				if (null != mToggleStateChangeListener) {
					mToggleStateChangeListener.onToggleState(toggle_state);
				}
			}
			break;
		}
		// 调用一次onDraw()方法
		invalidate();
		return true;
	}

	// 给自定义开关控件设置监听的方法
	public void setOnToggleStateLinstener(OnToggleStateChangeListener listen) {
		mToggleStateChangeListener = listen;
	}

	public void setToggleState(boolean b) {
		toggle_state = b;
	}

	// 监听回调接口，方法由实现接口的类实现
	public interface OnToggleStateChangeListener {

		public void onToggleState(boolean state);
	}
}