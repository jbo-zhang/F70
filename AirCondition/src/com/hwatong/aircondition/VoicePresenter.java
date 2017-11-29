package com.hwatong.aircondition;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

/**
 * 用于接受语音命令
 * @author zxy time:2017年11月29日
 *
 */
public class VoicePresenter {
	
	private IVoiceView iView;
	private VoiceBroadcast voiceBroadcast;
	
	private static final String CLOSE_ACTION = "";
	
	public VoicePresenter(IVoiceView iView, Context context){
		this.iView = iView;
		voiceBroadcast = new VoiceBroadcast();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(CLOSE_ACTION);
		context.registerReceiver(voiceBroadcast, intentFilter);
	}
	
	private class VoiceBroadcast extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			if(CLOSE_ACTION.equals(intent.getAction())) {
				iView.close();
			}
		}
	}
	
	public void unregisterBroadcast(Context context) {
		context.unregisterReceiver(voiceBroadcast);
	}
}
