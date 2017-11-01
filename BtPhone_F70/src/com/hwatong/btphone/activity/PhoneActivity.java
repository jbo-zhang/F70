package com.hwatong.btphone.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.hwatong.btphone.activity.base.BaseActivity;
import com.hwatong.btphone.bean.CallLog;
import com.hwatong.btphone.iview.ITBoxUpdateView;
import com.hwatong.btphone.presenter.TBoxPresenter;
import com.hwatong.btphone.ui.DrawableTextView;
import com.hwatong.btphone.ui.R;
import com.hwatong.btphone.util.L;
import com.hwatong.btphone.util.Utils;

/**
 * 主界面
 * 
 * @author zxy zjb time:2017年5月25日
 * 
 */
public class PhoneActivity extends BaseActivity implements ITBoxUpdateView{

	private static final String thiz = PhoneActivity.class.getSimpleName();

	private DrawableTextView mTvDial;

	private DrawableTextView mTvContacts;

	private DrawableTextView mTvCallLog;

	private ImageButton mBtnReturn;

	private TextView tvTip;

	int totalY;

	@Override
	protected void initView() {
		mTvDial = (DrawableTextView) findViewById(R.id.dtv_dial);
		mTvDial.setOnClickListener(this);

		mTvContacts = (DrawableTextView) findViewById(R.id.dtv_contacts);
		mTvContacts.setOnClickListener(this);

		mTvCallLog = (DrawableTextView) findViewById(R.id.dtv_call_log);
		mTvCallLog.setOnClickListener(this);

		mBtnReturn = (ImageButton) findViewById(R.id.btn_return);
		mBtnReturn.setOnClickListener(this);

		tvTip = (TextView) findViewById(R.id.tv_tip);

		// 默认为false
		showBtConnected(false);

		tBoxPresenter = new TBoxPresenter(this);
		tBoxPresenter.initTboxService(this);
		
	}

	/**
	 * 四指手势
	 */
	private boolean sended = false;

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			// 手势启动升级界面
			case 456:
				L.d(thiz, "msg 456");
				Intent intent = new Intent();
				intent.setAction("android.intent.action.SYSTEM_UPDATE_SETTINGS");
				if (intent.resolveActivity(getPackageManager()) != null) {
					startActivity(intent);
				} else {
					Toast.makeText(PhoneActivity.this, "没有升级应用", Toast.LENGTH_SHORT).show();
				}
				break;

			case 321:
				//Toast.makeText(PhoneActivity.this, "触发tbox升级", Toast.LENGTH_SHORT).show();

				tBoxPresenter.updateTbox();
				break;
			default:
				break;
			}
		};
	};

	private void showTboxUpdateDialog(String fileName) {
        final AlertDialog.Builder normalDialog = new AlertDialog.Builder(this);
        normalDialog.setTitle("TBox升级");
        normalDialog.setMessage("找到文件 " + fileName +" ,确定升级TBox吗?");
        normalDialog.setPositiveButton("确定", 
            new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            	tBoxPresenter.confirmUpdate();
            }
        });
        normalDialog.setNegativeButton("关闭", 
            new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //...To-do
            }
        });
        // 显示
        normalDialog.show();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		tBoxPresenter.unbindTbox(this);
	}
	

	private TBoxPresenter tBoxPresenter;

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// 系统升级
		if (event.getPointerCount() >= 3) {
			if (!sended) {
				handler.sendEmptyMessageDelayed(456, 5000);
				sended = true;
			}
		} else {
			handler.removeMessages(456);
			sended = false;
		}

		// tbox升级
		switch (event.getAction()) {
		case MotionEvent.ACTION_MOVE:
			if (event.getX() > 1000) {
				totalY += event.getY();
				L.d(thiz, "totalY : " + totalY);
				if (totalY > (200 * 100)) {
					handler.sendEmptyMessage(321);
					totalY = 0;
				}
			}
			break;
		case MotionEvent.ACTION_UP:
			handler.removeMessages(321);
			totalY = 0;
			break;
		default:
			break;
		}

		return super.onTouchEvent(event);
	}

	private void showBtConnected(boolean connected) {
		L.d(thiz, "onViewStateChange enabled : " + connected);

		if (mTvDial.isEnabled() != connected) {
			// 字体颜色
			int colorId = connected ? R.color.activity_tab_textcolor : R.color.activity_tab_textcolor_gray;
			int color = getResources().getColor(colorId);

			// 拨号
			Drawable drawable = getResources()
					.getDrawable(connected ? R.drawable.icon_dial : R.drawable.icon_dial_gray);
			Utils.setTextViewGray(mTvDial, connected, new Drawable[] { null, drawable, null, null }, color);

			// 通讯录
			Drawable drawable2 = getResources().getDrawable(
					connected ? R.drawable.icon_calllog : R.drawable.icon_calllog_gray);
			Utils.setTextViewGray(mTvCallLog, connected, new Drawable[] { null, drawable2, null, null }, color);

			// 通话记录
			Drawable drawable3 = getResources().getDrawable(
					connected ? R.drawable.icon_contacts : R.drawable.icon_contacts_gray);
			Utils.setTextViewGray(mTvContacts, connected, new Drawable[] { null, drawable3, null, null }, color);

			// 连接提示
			// tvTip.setText(connected ? "" :
			// getString(R.string.bt_not_connect));
		}
	}

	@Override
	protected void doClick(View v) {
		switch (v.getId()) {
		case R.id.dtv_dial:
			L.d(thiz, "onclick dial!");
			Utils.gotoActivity(this, DialActivity.class);
			break;
		case R.id.dtv_contacts:
			L.d(thiz, "onclick contacts!");
			Utils.gotoActivity(this, ContactsListActivity.class);
			break;
		case R.id.dtv_call_log:
			L.d(thiz, "onclick calllog!");
			Utils.gotoActivity(this, CallLogActivity.class);
			break;
		case R.id.btn_return:
			L.d(thiz, "onclick finish!");
			toHome();
			finish();
			break;

		default:
			break;
		}
	}

	/**
	 * to home!
	 */
	private void toHome() {
		Intent intent = new Intent("com.hwatong.launcher.MAIN");
		try {
			startActivity(intent);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected int getLayoutId() {
		return R.layout.activity_btphone;
	}

	@Override
	protected String getPageName() {
		return "btphone_home";
	}

	@Override
	public void showConnected() {
		showBtConnected(true);
	}

	@Override
	public void showDisconnected() {
		showBtConnected(false);
	}

	@Override
	public void showComing(CallLog callLog) {
		Utils.gotoDialActivity(this, callLog);
	}

	@Override
	public void showCalling(CallLog callLog) {
		Utils.gotoDialActivity(this, callLog);
	}

	@Override
	public void showHangUp(CallLog callLog) {
		L.d(thiz, "showHangUp");
	}

	
	//------------tbox升级相关--------------
	@Override
	public void showConfirmDialog(String fileName) {
		showTboxUpdateDialog(fileName);
	}

	@Override
	public void showCopyProgress(final long percent) {
		runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				showToast(PhoneActivity.this, "复制进度：" + percent + "%");
			}
		});
	}

	@Override
	public void showUpdateResult(int result, final String info) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(PhoneActivity.this, info, Toast.LENGTH_SHORT).show();
			}
		});
	}
	
	private Toast mToast;
	public void showToast(Context context, String msg) {
		if (mToast != null) {
			mToast.setText(msg);
			mToast.setDuration(Toast.LENGTH_SHORT);
		} else {
			mToast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
		}
		mToast.show();
	}
	
	@Override
	public void copyEnd() {
		tBoxPresenter.startUpdate();
	}
	
	@Override
	public void showNoFiles() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(PhoneActivity.this, "没有找到tbox升级文件", Toast.LENGTH_SHORT).show();
			}
		});
	}
	
	
	@Override
	public void showUpdateStart() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(PhoneActivity.this, "开始升级", Toast.LENGTH_SHORT).show();
			}
		});
		
	}
	

}
