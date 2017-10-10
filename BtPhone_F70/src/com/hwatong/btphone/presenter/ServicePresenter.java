package com.hwatong.btphone.presenter;

import java.util.ArrayList;
import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.Context;
import android.os.Bundle;

import com.hwatong.btphone.app.BtPhoneApplication;
import com.hwatong.btphone.bean.CallLog;
import com.hwatong.btphone.bean.Contact;
import com.hwatong.btphone.constants.Constant;
import com.hwatong.btphone.imodel.IBTPhoneModel;
import com.hwatong.btphone.iview.IServiceView;
import com.hwatong.btphone.iview.IUIView;
import com.hwatong.btphone.model.HwatongModel;
import com.hwatong.btphone.model.NForeModel;
import com.hwatong.btphone.model.OldHwatongModel;

public class ServicePresenter implements IUIView, IBTPhoneModel{

	private IServiceView iServiceView;
	
	private IBTPhoneModel iModel = new HwatongModel(this);

	public ServicePresenter(IServiceView iServiceView) {
		this.iServiceView = iServiceView;
	}
	
	@Override
	public void showConnected() {
		BtPhoneApplication.getInstance().notifyMsg(Constant.MSG_SHOW_CONNECTED);
	}


	@Override
	public void showDisconnected() {
		BtPhoneApplication.getInstance().notifyMsg(Constant.MSG_SHOW_DISCONNECTED);
	}


	@Override
	public void showComing(CallLog callLog) {
		if(!isDialForground()) {
			iServiceView.showWindow(callLog);
		} else {
			BtPhoneApplication.getInstance().notifyMsg(Constant.MSG_SHOW_COMING, callLog);
		}
	}


	@Override
	public void showCalling(CallLog callLog) {
		BtPhoneApplication.getInstance().notifyMsg(Constant.MSG_SHOW_CALLING, callLog);
	}


	@Override
	public void showTalking(CallLog callLog) {
		if(!isDialForground()) {
			iServiceView.hideWindow();
		} else {
			BtPhoneApplication.getInstance().notifyMsg(Constant.MSG_SHOW_TALKING, callLog);
		}
	}


	@Override
	public void showHangUp(CallLog callLog) {
		BtPhoneApplication.getInstance().notifyMsg(Constant.MSG_SHOW_HANG_UP, callLog);
	}


	@Override
	public void showReject(CallLog callLog) {
		if(!isDialForground()) {
			iServiceView.hideWindow();
		} else {
			BtPhoneApplication.getInstance().notifyMsg(Constant.MSG_SHOW_REJECT, callLog);
		}
	}


	@Override
	public void updateBooks(ArrayList<Contact> list) {
//		Bundle bundle = new Bundle();
//		bundle.putParcelableArrayList("books", list);
		BtPhoneApplication.getInstance().notifyMsg(Constant.MSG_UPDATE_BOOKS);
	}


	@Override
	public void updateMissedLogs(ArrayList<CallLog> list) {
//		Bundle bundle = new Bundle();
//		bundle.putParcelableArrayList("missed_logs", list);
		BtPhoneApplication.getInstance().notifyMsg(Constant.MSG_UPDATE_MISSED_LOGS);
	}


	@Override
	public void updateDialedLogs(ArrayList<CallLog> list) {
//		Bundle bundle = new Bundle();
//		bundle.putParcelableArrayList("dialed_logs", list);
		BtPhoneApplication.getInstance().notifyMsg(Constant.MSG_UPDATE_DIALED_LOGS);
	}


	@Override
	public void updateReceivedLogs(ArrayList<CallLog> list) {
//		Bundle bundle = new Bundle();
//		bundle.putParcelableArrayList("received_logs", list);
		BtPhoneApplication.getInstance().notifyMsg(Constant.MSG_UPDATE_RECEIVED_LOGS);
	}


	@Override
	public void updateAllLogs(ArrayList<CallLog> list) {
//		Bundle bundle = new Bundle();
//		bundle.putParcelableArrayList("logs", list);
		BtPhoneApplication.getInstance().notifyMsg(Constant.MSG_UPDATE_ALL_LOGS);
	}


	@Override
	public void showBooksLoadStart() {
		BtPhoneApplication.getInstance().notifyMsg(Constant.MSG_SHOW_BOOKS_LOAD_START);
	}


	@Override
	public void showBooksLoading() {
		BtPhoneApplication.getInstance().notifyMsg(Constant.MSG_SHOW_BOOKS_LOADING);		
	}


	@Override
	public void showBooksLoaded(boolean succeed, int reason) {
		BtPhoneApplication.getInstance().notifyMsg(Constant.MSG_SHOW_BOOKS_LOADED, succeed ? 1 : 0 , reason);		
	}


	@Override
	public void syncBooksAlreadyLoad() {
		BtPhoneApplication.getInstance().notifyMsg(Constant.MSG_SYNC_BOOKS_ALREADY_LOAD);
	}


	@Override
	public void showLogsLoadStart() {
		BtPhoneApplication.getInstance().notifyMsg(Constant.MSG_SHOW_LOGS_LOAD_START);
	}


	@Override
	public void showLogsLoading() {
		BtPhoneApplication.getInstance().notifyMsg(Constant.MSG_SHOW_LOGS_LOADING);		
	}


	@Override
	public void showLogsLoaded(boolean succeed, int reason) {
		BtPhoneApplication.getInstance().notifyMsg(Constant.MSG_SHOW_LOGS_LOADED, succeed ? 1 : 0 , reason);		
	}


	@Override
	public void syncLogsAlreadyLoad() {
		BtPhoneApplication.getInstance().notifyMsg(Constant.MSG_SYNC_LOGS_ALREADY_LOAD);		
	}


	@Override
	public void showMicMute(boolean isMute) {
		BtPhoneApplication.getInstance().notifyMsg(Constant.MSG_SHOW_MIC_MUTE, isMute ? 1 : 0);
	}


	@Override
	public void showSoundTrack(boolean isCar) {
		BtPhoneApplication.getInstance().notifyMsg(Constant.MSG_SHOW_SOUND_TRACK, isCar ? 1 : 0);
	}
	
	@Override
	public void showIdel() {
		BtPhoneApplication.getInstance().notifyMsg(Constant.MSG_SHOW_IDEL);
	}

	
//---------------------------------华丽的分割线----------------------------------------

	
	@Override
	public void link(Context context) {
		iModel.link(context);
	}


	@Override
	public void unlink(Context context) {
		iModel.unlink(context);
	}
	
	@Override
	public void sync() {
		iModel.sync();
	}
	

	@Override
	public void dial(String number) {
		iModel.dial(number);
	}


	@Override
	public void pickUp() {
		iModel.pickUp();
	}


	@Override
	public void hangUp() {
		iModel.hangUp();
	}


	@Override
	public void reject() {
		iModel.reject();
	}


	@Override
	public void dtmf(char code) {
		iModel.dtmf(code);
	}


	@Override
	public void toggleMic() {
		iModel.toggleMic();
	}


	@Override
	public void toggleTrack() {
		iModel.toggleTrack();
	}


	@Override
	public void loadBooks() {
		iModel.loadBooks();
	}
	
	@Override
	public void loadLogs() {
		iModel.loadLogs();
	}


	@Override
	public void loadMissedLogs() {
		iModel.loadMissedLogs();
	}


	@Override
	public void loadDialedLogs() {
		iModel.loadDialedLogs();
	}

	@Override
	public void loadReceivedLogs() {
		iModel.loadReceivedLogs();
	}
	
	protected boolean isDialForground() {
		ActivityManager am = (ActivityManager) BtPhoneApplication.getInstance().getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> cn = am.getRunningTasks(1);
		RunningTaskInfo taskInfo = cn.get(0);
		ComponentName name = taskInfo.topActivity;
		if ("com.hwatong.btphone.activity.DialActivity".equals(name.getClassName())
				|| "com.hwatong.btphone.activity.CallLogActivity".equals(name.getClassName())
				|| "com.hwatong.btphone.activity.ContactsListActivity".equals(name.getClassName())
				|| "com.hwatong.btphone.activity.PhoneActivity".equals(name.getClassName())) {
			return true;
		}
		return false;
	}

	@Override
	public ArrayList<Contact> getBooks() {
		return iModel.getBooks();
	}

	@Override
	public ArrayList<CallLog> getLogs() {
		return iModel.getLogs();
	}

	@Override
	public ArrayList<CallLog> getMissedLogs() {
		return iModel.getMissedLogs();
	}

	@Override
	public ArrayList<CallLog> getReceivedLogs() {
		return iModel.getReceivedLogs();
	}

	@Override
	public ArrayList<CallLog> getDialedLogs() {
		return iModel.getDialedLogs();
	}

	
}
