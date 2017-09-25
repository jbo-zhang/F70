package com.hwatong.aircondition;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.canbus.ACStatus;
import android.canbus.IACStatusListener;
import android.canbus.ICanbusService;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.hwatong.statusbarinfo.aidl.IStatusBarInfo;

public class MainActivity extends Activity implements OnClickListener,
		OnTouchListener {

	private static final String TAG = "kongtiao";
	protected static final boolean DBG = false;

	private ICanbusService mCanbusService;

	private ImageButton mBtnReturn;

	private com.hwatong.aircondition.VerticalSeekBar mLeftTempSeekBar;
	/**
	 * @ViewIOC(id = R.id.tv_show_left_temp) private TextView mTvLeftTemp;
	 */
	// --吹风模式
	private ImageView mIvBlowerUp;

	private ImageView mIvBlowerFront;

	private ImageView mIvBlowerDown;
	// --吹风模式

	// ---风速加减
	private ImageView[] mWindLevels;

	private TextView mTvWindSwitch;

	private ImageView mIvWindLevel1;

	private ImageView mIvWindLevel2;

	private ImageView mIvWindLevel3;

	private ImageView mIvWindLevel4;

	private ImageView mIvWindLevel5;

	private ImageView mIvWindLevel6;

	private ImageView mIvWindLevel7;
	// ---风速加减

	private TextView mTvFrontDefrost;

	private TextView mTvRearDefrost;

	private TextView mTvAc;

	private TextView mTvLoop;

	private TextView mTvRearSwitch;
	
	private static final int DISAPPEAR_DELAY = 10000;

	private int[] drawableIds = new int[] { R.drawable.wind_level_1,
			R.drawable.wind_level_2, R.drawable.wind_level_3,
			R.drawable.wind_level_4, R.drawable.wind_level_5,
			R.drawable.wind_level_6, R.drawable.wind_level_7 };

	// private float mLeftTemp = 16f;

	private static final int MSG_AC_STATUS_RECEIVED = 0;

	@SuppressLint("HandlerLeak")
	private final Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_AC_STATUS_RECEIVED:
				handleACStatusReceived();
				break;
			case 20:
				MainActivity.this.finish();
				break;
			}
		}
	};

	/**
	 * 
	 * @param img
	 * @return
	 */
	public Bitmap toturn(Bitmap bitmap) {
		Matrix matrix = new Matrix();
		// 设置旋转角度
		matrix.setRotate(90);
		// 重新绘制Bitmap
		bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
				bitmap.getHeight(), matrix, true);
		return bitmap;
	}

	private TempThumbDrable tempThumbDrable;

	public static Bitmap drawableToBitmap(Drawable drawable) {

		Bitmap bitmap = Bitmap.createBitmap(

		drawable.getIntrinsicWidth(),

		drawable.getIntrinsicHeight(),

		drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888

		: Bitmap.Config.RGB_565);

		Canvas canvas = new Canvas(bitmap);

		// canvas.setBitmap(bitmap);

		drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
				drawable.getIntrinsicHeight());

		drawable.draw(canvas);

		return bitmap;

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		initView();

		mWindLevels = new ImageView[] { mIvWindLevel1, mIvWindLevel2,
				mIvWindLevel3, mIvWindLevel4, mIvWindLevel5, mIvWindLevel6,
				mIvWindLevel7 };

		mLeftTempSeekBar.setEnabled(true);
		/* 设置32是为了除了二可以得到float型 才会有0.5的出现 */
		mLeftTempSeekBar.setMax(32);

		tempThumbDrable = new TempThumbDrable(BitmapFactory.decodeResource(
				getResources(), R.drawable.seekbar_pointer));
		tempThumbDrable
				.setTemp(mLeftTempSeekBar.getProgress() / 2.0 + 16 + "℃");
		mLeftTempSeekBar.setThumb(new BitmapDrawable(
				toturn(drawableToBitmap(tempThumbDrable))));

		mLeftTempSeekBar
				.setUpDownListener(new com.hwatong.aircondition.VerticalSeekBar.UpDownListener() {

					@Override
					public void onTouch(int progress) {

					}
				});

		mLeftTempSeekBar
				.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

					@Override
					public void onStopTrackingTouch(SeekBar arg0) {

					}

					@Override
					public void onStartTrackingTouch(SeekBar arg0) {

					}

					@Override
					public void onProgressChanged(SeekBar arg0, int progress,
							boolean fromUser) {
						tempThumbDrable = new TempThumbDrable(BitmapFactory
								.decodeResource(getResources(),
										R.drawable.seekbar_pointer));
						tempThumbDrable.setTemp(progress / 2.0 + 16 + "℃");
						mLeftTempSeekBar.setThumb(new BitmapDrawable(
								toturn(drawableToBitmap(tempThumbDrable))));
						Log.d(TAG, "fromUser = " + fromUser);
						//if(fromUser) {
							sendACKeyEvent("温度", 10, (int) (progress + 32));
						//}
					}
				});

		mCanbusService = ICanbusService.Stub.asInterface(ServiceManager
				.getService("canbus"));
		try {
			mCanbusService.addACStatusListener(mACStatusListener);

		} catch (RemoteException e) {
			e.printStackTrace();
		}

		handleACStatusReceived();

	}

	private void initView() {
		mBtnReturn = (ImageButton) findViewById(R.id.btn_return);
		mBtnReturn.setOnClickListener(this);

		mLeftTempSeekBar = (VerticalSeekBar) findViewById(R.id.seekbar_left_temp);

		// 除雾（向上吹风）,平行吹风，向下吹风
		mIvBlowerUp = (ImageView) findViewById(R.id.iv_blower_up);
		mIvBlowerUp.setOnClickListener(this);

		mIvBlowerFront = (ImageView) findViewById(R.id.iv_blower_front);
		mIvBlowerFront.setOnClickListener(this);

		mIvBlowerDown = (ImageView) findViewById(R.id.iv_blower_down);
		mIvBlowerDown.setOnClickListener(this);
		
		// 风速 1~7
		mTvWindSwitch = (TextView) findViewById(R.id.btn_wind_switch);
		mTvWindSwitch.setOnClickListener(this);

		mIvWindLevel1 = (ImageView) findViewById(R.id.btn_wind_level_1);
		mIvWindLevel1.setOnClickListener(this);

		mIvWindLevel2 = (ImageView) findViewById(R.id.btn_wind_level_2);
		mIvWindLevel2.setOnClickListener(this);

		mIvWindLevel3 = (ImageView) findViewById(R.id.btn_wind_level_3);
		mIvWindLevel3.setOnClickListener(this);

		mIvWindLevel4 = (ImageView) findViewById(R.id.btn_wind_level_4);
		mIvWindLevel4.setOnClickListener(this);

		mIvWindLevel5 = (ImageView) findViewById(R.id.btn_wind_level_5);
		mIvWindLevel5.setOnClickListener(this);

		mIvWindLevel6 = (ImageView) findViewById(R.id.btn_wind_level_6);
		mIvWindLevel6.setOnClickListener(this);

		mIvWindLevel7 = (ImageView) findViewById(R.id.btn_wind_level_7);
		mIvWindLevel7.setOnClickListener(this);

		// 模式：前部，后部，空调，内循环，后空调
		mTvFrontDefrost = (TextView) findViewById(R.id.btn_front_defrost);
		mTvFrontDefrost.setOnClickListener(this);

		mTvRearDefrost = (TextView) findViewById(R.id.btn_rear_defrost);
		mTvRearDefrost.setOnClickListener(this);

		mTvAc = (TextView) findViewById(R.id.btn_ac);
		mTvAc.setOnClickListener(this);

		mTvLoop = (TextView) findViewById(R.id.btn_loop);
		mTvLoop.setOnClickListener(this);

		mTvRearSwitch = (TextView) findViewById(R.id.btn_rear);
		mTvRearSwitch.setOnClickListener(this);
	}

	boolean mStoped = false;
	boolean mPopMode = false;

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		Log.d(TAG, "onNewIntent " + mStoped);
		if (mStoped) {
			setIntent(intent);
		}
	};

	@Override
	protected void onStart() {
		super.onStart();
		mStoped = false;
		Log.d(TAG, "onStart");
	}

	@Override
	protected void onResume() {
		super.onResume();
		Intent t = getIntent();
		Log.d(TAG, "onResume");
		if (t.hasExtra("mode")) {
			String mode = t.getStringExtra("mode");
			if ("pop".equals(mode)) {
				mPopMode = true;
				mHandler.removeMessages(20);
				mHandler.sendEmptyMessageDelayed(20, DISAPPEAR_DELAY);
			}
		}
		// 绑定状态栏服务
		bindService(new Intent("com.remote.hwatong.statusinfoservice"),
				mStatusBarConnection, BIND_AUTO_CREATE);
		
		syncStatusBar();
		
	};
	
	protected IStatusBarInfo iStatusBarInfo;
	private void syncStatusBar() {
		if(iStatusBarInfo != null) {
			try {
				iStatusBarInfo.setCurrentPageName("air_conditioning");
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		} else {
			Intent intent = new Intent();
			intent.setAction("com.remote.hwatong.statusinfoservice");
			bindService(intent, mConn2, BIND_AUTO_CREATE);
		}
	}
	
	protected ServiceConnection mConn2 = new ServiceConnection() {

		@Override
		public void onServiceDisconnected(ComponentName arg0) {
			iStatusBarInfo = null;
		}

		@Override
		public void onServiceConnected(ComponentName arg0, IBinder binder) {
			iStatusBarInfo = IStatusBarInfo.Stub.asInterface(binder);
			try {
				if (iStatusBarInfo != null)
					Log.d(TAG, "setCurrentPageName air_conditioning");
					iStatusBarInfo.setCurrentPageName("air_conditioning");
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
	};
	
	

	@Override
	public void onClick(View v) {
		if (mPopMode) {
			mHandler.removeMessages(20);
			mHandler.sendEmptyMessageDelayed(20, DISAPPEAR_DELAY);
		}

		switch (v.getId()) {

		case R.id.iv_blower_up:
			Log.d(TAG, "blower_up");
			sendACKeyEvent("向上吹风", 4, v.getVisibility() == View.VISIBLE ? 0 : 1);
			//v.setVisibility(v.getVisibility() == View.VISIBLE ? View.INVISIBLE : View.VISIBLE);
			break;
		case R.id.iv_blower_front:
			Log.d(TAG, "blower_front");
			sendACKeyEvent("平行吹风", 5, v.getVisibility() == View.VISIBLE ? 0 : 1);
			//v.setVisibility(v.getVisibility() == View.VISIBLE ? View.INVISIBLE : View.VISIBLE);
			break;
		case R.id.iv_blower_down:
			Log.d(TAG, "blower_down");
			sendACKeyEvent("向下吹风", 6, v.getVisibility() == View.VISIBLE ? 0 : 1);
			//v.setVisibility(v.getVisibility() == View.VISIBLE ? View.INVISIBLE : View.VISIBLE);
			break;

		case R.id.btn_return:
			finish();
			break;

		case R.id.btn_wind_level_1:
		case R.id.btn_wind_level_2:
		case R.id.btn_wind_level_3:
		case R.id.btn_wind_level_4:
		case R.id.btn_wind_level_5:
		case R.id.btn_wind_level_6:
		case R.id.btn_wind_level_7:
			int level = Integer.parseInt((String) v.getTag());
			sendACKeyEvent("风速", 3, level);
			//onWindLevel(level);
			break;
			
			
		case R.id.btn_ac:
			sendACKeyEvent("ac", 1, v.isSelected() ? 0 : 1);
			//v.setSelected(!v.isSelected());
			break;
		case R.id.btn_front_defrost:
			sendACKeyEvent("前除霜", 0, v.isSelected() ? 0 : 1);
			//v.setSelected(!v.isSelected());
			break;
		case R.id.btn_rear_defrost:
			sendACKeyEvent("后除霜", 2, v.isSelected() ? 0 : 1);
			//v.setSelected(!v.isSelected());
			break;
		case R.id.btn_wind_switch:
			sendACKeyEvent("风速开关", 3, 0);
			//mTvWindSwitch
			//.setText(getString(R.string.text_wind_off).equals(mTvWindSwitch.getText().toString()) ? R.string.text_wind_on
			//		: R.string.text_wind_off);
			//onWindLevel(getString(R.string.text_wind_off).equals(mTvWindSwitch.getText().toString()) ? 0 : 1);
			break;
		case R.id.btn_rear:
			sendACKeyEvent("后空调开关按下", 7, v.isSelected() ? 0 : 1);
			//v.setSelected(!v.isSelected());
			break;
		case R.id.btn_loop:// 内外循环
			sendACKeyEvent("内外循环", 9, v.isSelected() ? 0 : 1);
			//v.setSelected(!v.isSelected());
			break;

		default:
			break;
		}
	}

	/**
	 * 弃用,改为onClickListener
	 */
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		int status = -1;
		status = event.getAction() == MotionEvent.ACTION_DOWN ? 1 : event.getAction() == MotionEvent.ACTION_UP ? 0 : status;
		if (status == -1)
			return true;

		switch (v.getId()) {
		case R.id.iv_blower_up:
			Log.d(TAG, "blower_up");
		case R.id.iv_blower_front:
			Log.d(TAG, "blower_front");
		case R.id.iv_blower_down:
			Log.d(TAG, "blower_down");
			Point point = new Point((int) event.getRawX(), (int) event.getRawY());
			//touchBlowerMode(v.getId(), status, point);
			break;
		case R.id.btn_ac:
			sendACKeyEvent("ac", 1, status);
			break;
		case R.id.btn_front_defrost:
			sendACKeyEvent("前除霜", 0, status);
			break;
		case R.id.btn_rear_defrost:
			sendACKeyEvent("后除霜", 2, status);
			break;
		case R.id.btn_wind_switch:
			sendACKeyEvent("风速开关", 3, 0);
			break;
		case R.id.btn_rear:
			sendACKeyEvent("后空调开关按下", 7, status);
			break;
		case R.id.btn_loop:// 内外循环
			sendACKeyEvent("内外循环", 9, status);
			break;

		default:
			break;
		}
		return false;
	}

	/* 摄氏度和华氏度转换 */
	public float tempExchange(float temp) {
		float left = 0;
		if (temp >= 16 && temp <= 32) {
			left = temp * 9 / 5 + 32;
		} else {
			left = (temp - 32) * 5 / 9;

		}
		return left;
	}

	private void sendACKeyEvent(String tag, int code, int status) {
		Log.d(TAG, tag + ":(" + code + "," + status + ")");
		sendACKeyEvent(code, status);
	}

	/**
	 * 发送到 CanBus
	 * @param code
	 * @param status
	 */
	private void sendACKeyEvent(int code, int status) {
		try {
			mCanbusService.writeACControl(code, status);
		} catch (RemoteException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 数据反馈
	 */
	private final IACStatusListener mACStatusListener = new IACStatusListener.Stub() {

		@Override
		public void onReceived(ACStatus arg0) throws RemoteException {
			if (DBG)
				Log.d(TAG, "onReceived: " + arg0);
			mHandler.removeMessages(MSG_AC_STATUS_RECEIVED);
			mHandler.sendEmptyMessage(MSG_AC_STATUS_RECEIVED);
		}
	};
	
	public void modeWindNull() {
		mIvBlowerUp.setVisibility(View.INVISIBLE);
		mIvBlowerFront.setVisibility(View.INVISIBLE);
		mIvBlowerDown.setVisibility(View.INVISIBLE);
	}

	/**
	 * 风速等级：1-7
	 * 
	 * @param level
	 */

	private void onWindLevel(int level) {
		if (level > mWindLevels.length)
			level = 0;

		for (int i = 0; i < level; i++) {
			mWindLevels[i].setImageAlpha(255);
		}
		for (int i = level; i < mWindLevels.length; i++) {
			mWindLevels[i].setImageAlpha(0);
		}
	}
	
	private ACStatus mACStatus;

	/**
	 * 获取最新状态，设置界面
	 */
	private void handleACStatusReceived() {

		ACStatus status = null;
		try {
			status = mCanbusService.getLastACStatus(getPackageName());

		} catch (RemoteException e) {
			e.printStackTrace();
			status = null;
		}

		if (status != null) {
			if (mACStatus == null || !mACStatus.equals(status)) { // 如果按钮状态为空,切按钮状态改变

				if (mACStatus == null)
					mACStatus = new ACStatus();
				mACStatus.set(status);

				Log.e(TAG, "handleACStatusReceived: mAPLStatus = " + mACStatus);

				// 左边温度
				int value = status.getStatus1() & 0xFF;
				value = value < 32 ? 32 : value > 64 ? 64 : value;
				mLeftTempSeekBar.setProgress(value - 32);

				float temp = (float) value * 0.5f;
				tempThumbDrable.setTemp(temp + "℃");
				Log.e("temp", "" + temp);

				// if (temp != mLeftTemp) { mLeftTemp = temp;
				// onTempChange(temp); }

				// 风速
				int valuewind = status.getStatus2() & 0x0F;
				onWindLevel(valuewind);
				
				// 风速开关
				int windOnOff = status.getStatus2() & 0x0F;
				mTvWindSwitch
						.setText(windOnOff == 0x00 ? R.string.text_wind_off
								: R.string.text_wind_on);

				// 后空调开关
				int rear = status.getStatus3() & 0x03;
				mTvRearSwitch.setSelected(rear == 0x01);

				// 内外循环模式 询问下ON/OFF 分别代表哪个循环
				int loop = status.getStatus4() & 0x03;
				mTvLoop.setSelected(loop == 0x01);

				// AC
				int ac = status.getStatus5() & 0x03;
				mTvAc.setSelected(ac == 0x01);

				// Air Front 前除霜UI变化
				int frontDefrost = status.getStatus6() & 0x03;
				mTvFrontDefrost.setSelected(frontDefrost == 0x01);

				// Air Rear 后除霜UI变化
				int rearDefrost = status.getStatus12() & 0x03;
				mTvRearDefrost.setSelected(rearDefrost == 0x01);

				//吹风模式
				modeWindNull();
				if ((status.getStatus8() & 0x03) == 0x01) {
					mIvBlowerUp.setVisibility(View.VISIBLE);
				}

				if ((status.getStatus9() & 0x03) == 0x01) {
					mIvBlowerFront.setVisibility(View.VISIBLE);
				}

				if ((status.getStatus10() & 0x03) == 0x01) {
					mIvBlowerDown.setVisibility(View.VISIBLE);
				}
			}
		}

	}

	private IStatusBarInfo mStatusBarInfo; // 状态栏左上角信息
	private ServiceConnection mStatusBarConnection = new ServiceConnection() {

		@Override
		public void onServiceDisconnected(ComponentName name) {
			mStatusBarInfo = null;
		}

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			mStatusBarInfo = IStatusBarInfo.Stub.asInterface(service);
			try {
				if (mStatusBarInfo != null) {
					mStatusBarInfo.setCurrentPageName("launcher");
				}
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
	};

	@Override
	protected void onPause() {
		Log.d(TAG, "onPause");
		mStatusBarInfo = null;
		unbindService(mStatusBarConnection);
		super.onPause();
	}

	@Override
	protected void onStop() {
		super.onStop();
		mStoped = true;
		Log.d(TAG, "onStop");
	}

	@Override
	protected void onDestroy() {
		try {
			mCanbusService.removeACStatusListener(mACStatusListener);
		} catch (RemoteException e) {
			e.printStackTrace();
		}

		mHandler.removeMessages(MSG_AC_STATUS_RECEIVED);
		mHandler.removeCallbacksAndMessages(null);
		super.onDestroy();
	}
}
