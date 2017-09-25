package com.hwatong.btphone.presenter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.hwatong.btphone.iview.IReceiverView;
import com.hwatong.btphone.util.L;

public class BroadcastPresenter {
	private VoiceBroadcast voiceBroadcast;
	private static final String CLOSE_ACTION = "com.hwatong.voice.CLOSE_BTMUSIC";
	private static final String thiz = BroadcastPresenter.class.getSimpleName();
	
	private IReceiverView iView;
	
	public BroadcastPresenter(IReceiverView view) {
		this.iView = view;
	}

	public void regVoiceBroadcast(Context context) {
		voiceBroadcast = new VoiceBroadcast();
		IntentFilter filter = new IntentFilter();
		filter.addAction(CLOSE_ACTION);
		context.registerReceiver(voiceBroadcast, filter);
	}
	
	public void unregVoiceBroadcast(Context context) {
		context.unregisterReceiver(voiceBroadcast);
	}
	
	private class VoiceBroadcast extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			L.d(thiz, "onReceive !");
			if(CLOSE_ACTION.equals(intent.getAction())) {
				iView.close();
			}
		}
		
	}
	
}
