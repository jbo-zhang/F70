package com.hwatong.btphone.model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.TimerTask;
import java.util.TreeSet;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.SystemClock;
import android.sax.StartElementListener;
import android.text.TextUtils;
import android.util.SparseArray;

import com.hwatong.btphone.CallStatus;
import com.hwatong.btphone.ICallback;
import com.hwatong.btphone.IService;
import com.hwatong.btphone.bean.CallLog;
import com.hwatong.btphone.bean.Contact;
import com.hwatong.btphone.constants.BtPhoneDef;
import com.hwatong.btphone.constants.PhoneState;
import com.hwatong.btphone.imodel.IBTPhoneModel;
import com.hwatong.btphone.iview.IUIView;
import com.hwatong.btphone.util.L;
import com.hwatong.btphone.util.TimerTaskUtil;
import com.hwatong.btphone.util.Utils;

public class OldHwatongModel /* implements IBTPhoneModel */ {

//	private static final String thiz = OldHwatongModel.class.getSimpleName();
//	
//	/**
//	 * View接口
//	 */
//	public IUIView iView;
//
//	/**
//	 * 三个通话类型列表
//	 */
//	private SparseArray<ArrayList<CallLog>> mCallLogMap = new SparseArray<ArrayList<CallLog>>(3);
//
//	/**
//	 * 所有通话记录列表
//	 */
//	private ArrayList<CallLog> mAllCallLogList = new ArrayList<CallLog>();
//	
//	/**
//	 * 所有通讯录列表
//	 */
//	private ArrayList<Contact> mContacts = new ArrayList<Contact>();
//
//	/**
//	 * 通讯录列表 去重 排序
//	 */
//	private TreeSet<Contact> mContactSet = new TreeSet<Contact>();
//
//	/**
//	 * 蓝牙服务Action
//	 */
//	private static final String ACTION_BT_SERVICE = "com.hwatong.btphone.service";
//
//	/**
//	 * 通讯录加载中
//	 */
//	private boolean booksLoading = false;
//	
//	/**
//	 * 通话记录加载中
//	 */
//	private boolean logsLoading = false;
//	
//	/**
//	 * 锁对象
//	 */
//	private Object lock = new Object();
//	
//	/**
//	 * framework层 蓝牙电话服务 代理对象
//	 */
//	private IService iService;
//
//	/**
//	 * 通话状态
//	 */
//	private PhoneState phoneState = PhoneState.IDEL;
//	
//	/**
//	 * 当前CallLog
//	 */
//	private CallLog currentCall;
//	
//	/**
//	 * 声音通道
//	 */
//	private boolean isCar;
//
//	/**
//	 * 麦克风状态
//	 */
//	private boolean isMute;
//	
//	
//	public OldHwatongModel(IUIView iView) {
//		this.iView = iView;
//		mCallLogMap.put(CallLog.TYPE_CALL_IN, new ArrayList<CallLog>());
//		mCallLogMap.put(CallLog.TYPE_CALL_OUT, new ArrayList<CallLog>());
//		mCallLogMap.put(CallLog.TYPE_CALL_MISS, new ArrayList<CallLog>());
//	}
//	
//	@Override
//	public void link(Context context) {
//		L.d(thiz, "link()");
//		context.bindService(new Intent(ACTION_BT_SERVICE), mBtSdkConn, Context.BIND_AUTO_CREATE);
//	}
//
//	@Override
//	public void unlink(Context context) {
//		L.d(thiz, "unlink()");
//		context.unbindService(mBtSdkConn);
//	}
//	
//	
//	@Override
//	public void sync() {
//		L.d(thiz, "sync()");
//		if(iService != null) {
//			try {
//				if(iService.isHfpConnected()) {
//					//同步连接状态
//					iView.showConnected();
//					
//					//同步通讯录下载状态
//					if(booksLoading) {
//						iView.showBooksLoading();
//					} else {
//						iView.syncBooksAlreadyLoad();
//					}
//					
//					//同步通话记录下载状态
//					if(logsLoading) {
//						iView.showLogsLoading();
//					} else {
//						iView.syncLogsAlreadyLoad();
//					}
//					
//					//同步通讯录
//					iView.updateBooks(mContacts);
//					
//					//同步通话记录
//					iView.updateAllLogs(mAllCallLogList);
//					iView.updateDialedLogs(mCallLogMap.get(CallLog.TYPE_CALL_OUT));
//					iView.updateMissedLogs(mCallLogMap.get(CallLog.TYPE_CALL_MISS));
//					iView.updateReceivedLogs(mCallLogMap.get(CallLog.TYPE_CALL_IN));
//					
//					//同步通话状态
//					if(phoneState == PhoneState.TALKING) {
//						iView.showTalking(currentCall);
//					} else if(phoneState == PhoneState.INCOMING) {
//						iView.showComing(currentCall);
//					} else if(phoneState == PhoneState.OUTGOING) {
//						iView.showCalling(currentCall);
//					} else {
//						iView.showIdel();
//					}
//					
//					//同步麦克风
//					iView.showMicMute(isMute);
//					
//					//同步声音通道
//					iView.showSoundTrack(isCar);
//					
//					
//				} else {
//					iView.showDisconnected();
//				}
//				
//			} catch (RemoteException e) {
//				e.printStackTrace();
//			}
//		} else {
//			iView.showDisconnected();
//		}
//	}
//	
//
//	@Override
//	public void dial(String number) {
//		L.d(thiz, "dial() number = " + number);
//		if(iService != null) {
//			try {
//				iService.phoneDial(number.trim());
//			} catch (RemoteException e) {
//				e.printStackTrace();
//			}
//		}
//	}
//
//	@Override
//	public void pickUp() {
//		L.d(thiz, "pickUp()");
//		if(iService != null) {
//			try {
//				iService.phoneAnswer();
//			} catch (RemoteException e) {
//				e.printStackTrace();
//			}
//		}
//	}
//
//	@Override
//	public void hangUp() {
//		L.d(thiz, "hangUp()");
//		if(iService != null) {
//			try {
//				iService.phoneFinish();
//			} catch (RemoteException e) {
//				e.printStackTrace();
//			}
//		}
//	}
//
//	@Override
//	public void reject() {
//		L.d(thiz, "reject()");
//		if(iService != null) {
//			try {
//				iService.phoneReject();
//			} catch (RemoteException e) {
//				e.printStackTrace();
//			}
//		}
//	}
//
//	@Override
//	public void dtmf(char code) {
//		L.d(thiz, "dtmf() code = " + code);
//		if(iService != null) {
//			try {
//				iService.phoneTransmitDTMFCode(code);
//			} catch (RemoteException e) {
//				e.printStackTrace();
//			}
//		}
//	}
//
//	@Override
//	public void toggleMic() {
//		L.d(thiz, "toggleMic()");
//		if(iService != null) {
//			try {
//				L.d(thiz, "toggleMic() 1");
//				iService.phoneMicOpenClose();
//				L.d(thiz, "toggleMic() 2");
//				isMute = !isMute;
//				iView.showMicMute(isMute);
//				L.d(thiz, "toggleMic() 3 ");
//			} catch (RemoteException e) {
//				e.printStackTrace();
//			}
//		}
//		
//		
//		isMute = !isMute;
//		iView.showMicMute(isMute);
//		L.d(thiz, "toggleMic() 4");
//	}
//
//	@Override
//	public void toggleTrack() {
//		L.d(thiz, "toggleTrack()");
//		if(iService != null) {
//			try {
//				iService.phoneTransfer();
//			} catch (RemoteException e) {
//				e.printStackTrace();
//			}
//		}
//	}
//
//	@Override
//	public void loadBooks() {
//		L.d(thiz, "loadBooks()");
//		if(booksLoading) {
//			return;
//		}
//		if(iService != null) {
//			try {
//				boolean result = iService.phoneBookStartUpdate();
//				if(result) {
//					cleanList();
//					booksLoading = true;
//					logsLoading = true;
//					iView.showBooksLoadStart();
//					new Thread(new Runnable() {
//						@Override
//						public void run() {
//							SystemClock.sleep(500);
//							iView.showBooksLoading();
//						}
//					}).start();
//				} else {
//					booksLoading = false;
//					iView.showBooksLoaded(false, BtPhoneDef.PBAP_DOWNLOAD_REJECT);
//				}
//			} catch (RemoteException e) {
//				e.printStackTrace();
//			}
//		}
//	}
//
//	@Override
//	public void loadLogs() {
//		L.d(thiz, "loadLogs()");
//	}
//
//	@Override
//	public void loadMissedLogs() {
//		L.d(thiz, "loadMissedLogs()");
//		loadLogsByType(com.hwatong.btphone.CallLog.TYPE_CALL_MISS);
//	}
//
//	@Override
//	public void loadDialedLogs() {
//		L.d(thiz, "loadDialedLogs()");
//		loadLogsByType(com.hwatong.btphone.CallLog.TYPE_CALL_OUT);
//	}
//
//	@Override
//	public void loadReceivedLogs() {
//		L.d(thiz, "loadReceivedLogs()");
//		loadLogsByType(com.hwatong.btphone.CallLog.TYPE_CALL_IN);
//	}
//	
//	private void loadLogsByType(String type) {
//		L.d(thiz, "loadLogsByType() type = " + type);
//		if(logsLoading) {
//			return;
//		}
//		if(iService != null) {
//			try {
//				boolean result = iService.phoneBookStartUpdate();
//				if(result) {
//					logsLoading = true;
//					cleanList();
//					iView.showLogsLoadStart();
//					new Thread(new Runnable() {
//						@Override
//						public void run() {
//							SystemClock.sleep(500);
//							iView.showLogsLoading();
//						}
//					}).start();
//				} else {
//					logsLoading = false;
//					iView.showLogsLoaded(false, BtPhoneDef.PBAP_DOWNLOAD_REJECT);
//				}
//			} catch (RemoteException e) {
//				e.printStackTrace();
//			}
//		}
//	}
//	
//
//	// framework层bt服务连接
//	private ServiceConnection mBtSdkConn = new ServiceConnection() {
//
//		@Override
//		public void onServiceDisconnected(ComponentName name) {
//			L.d(thiz, "IService disconnected");
//			iService = null;
//		}
//
//		@Override
//		public void onServiceConnected(ComponentName name, IBinder service) {
//			L.d(thiz, "IService connected");
//			iService = IService.Stub.asInterface(service);
//			try {
//				if (iService != null) {
//					iService.registerCallback(mBtCallback);
//					if (iService.isHfpConnected()) {
//						loadBooks();
//					}
//				}
//			} catch (RemoteException e) {
//				e.printStackTrace();
//			}
//		}
//	};
//	
//	
//	private void cleanList() {
//		synchronized (lock) {
//			mContactSet.clear();
//			mCallLogMap.get(CallLog.TYPE_CALL_OUT).clear();
//			mCallLogMap.get(CallLog.TYPE_CALL_IN).clear();
//			mCallLogMap.get(CallLog.TYPE_CALL_MISS).clear();
//			mAllCallLogList.clear();
//		}
//	}
//	
//	/**
//	 * Framework回调
//	 */
//	private ICallback mBtCallback = new ICallback.Stub() {
//
//		
//		@Override
//		public void onRingStop() throws RemoteException {
//			// TODO Auto-generated method stub
//			L.d(thiz, "onRingStop");
//		}
//		
//		@Override
//		public void onRingStart() throws RemoteException {
//			// TODO Auto-generated method stub
//			L.d(thiz, "onRingStart");
//		}
//		
//		@Override
//		public void onPhoneBook(String type, String name, String number)
//				throws RemoteException {
//			L.d(thiz, "onPhoneBook type= " + type + " name= " + name + " number= " + number);
//			if (TextUtils.isEmpty(number) || !number.matches("^\\d*")) {
//				return;
//			}
//			name = name.replaceAll(" +", "");
//			number = number.replaceAll(":", "");
//			String[] strs = Utils.getPinyinAndFirstLetter(name);
//			Contact contact = new Contact(name, number,strs[0], strs[1]);
//			mContactSet.add(contact);
//			if(mContactSet.size() % 30 == 0) {
//				mContacts = new ArrayList<Contact>(mContactSet);
//				iView.updateBooks(mContacts);
//			}
//		}
//		
//		@Override
//		public void onHfpRemote() throws RemoteException {
//			L.d(thiz, "onHfpRemote");
//			iView.showSoundTrack(true);
//			isCar = true;
//		}
//		
//		@Override
//		public void onHfpLocal() throws RemoteException {
//			L.d(thiz, "onHfpLocal");
//			iView.showSoundTrack(false);
//			isCar = false;
//		}
//		
//		@Override
//		public void onHfpDisconnected() throws RemoteException {
//			L.d(thiz, "onHfpDisconnected");
//			cleanList();
//			iView.showDisconnected();
//		}
//		
//
//		@Override
//		public void onHfpConnected() throws RemoteException {
//			L.d(thiz, "onHfpConnected");
//			loadBooks();
//			loadLogs();
//			iView.showConnected();
//		}
//		
//		@Override
//		public void onContactsChange() throws RemoteException {
//			L.d(thiz, "onContactsChange");
//			List<com.hwatong.btphone.Contact> contactList = iService.getContactList();
//			L.d(thiz, "onContactsChange contactList : " + contactList.size());
//		}
//		
//		@Override
//		public void onCalllogChange(String type) throws RemoteException {
//			L.d(thiz, "onCalllogChange type = " + type);
//			List<com.hwatong.btphone.CallLog> calllogList = iService.getCalllogList(type);
//			L.d(thiz, "onCalllogChange calllogList : " + calllogList.size() + " " + calllogList.get(0));
//			
//			//来电
//			if(com.hwatong.btphone.CallLog.TYPE_CALL_IN.equals(type)) {
//				
//			//漏接
//			} else if(com.hwatong.btphone.CallLog.TYPE_CALL_MISS.equals(type)) {
//				
//			//去电
//			} else if(com.hwatong.btphone.CallLog.TYPE_CALL_OUT.equals(type)) {
//				
//			}
//		}
//		
//		@Override
//		public void onCalllog(String type, String name, String number, String date) throws RemoteException {
//			L.d(thiz, "onCalllog type= " + type + " name= " + name + " number= " + number+ " date= " + date);
//			name = name.replaceAll(" +", "");
//			number = number.replaceAll(":", "");
//			int typeInt = Integer.parseInt(type);
//			
//			CallLog callLog = new CallLog(typeInt, name, number, date);
//			
//			ArrayList<CallLog> callLogs = mCallLogMap.get(typeInt);
//			if (callLogs == null) {
//				callLogs = new ArrayList<CallLog>();
//				mCallLogMap.put(typeInt, callLogs);
//			}
//			callLogs.add(callLog);
//		}
//		
//		@Override
//		public void onCallStatusChanged() throws RemoteException {
//			L.d(thiz, "onCallStatusChanged");
//
//			if (iService != null && iService.isHfpConnected()) {
//				CallStatus callStatus = iService.getCallStatus();
//
//				//闲置状态
//				if (CallStatus.PHONE_CALL_NONE.equals(callStatus.status)) {
//					if(phoneState == PhoneState.TALKING || phoneState == PhoneState.OUTGOING) {
//						iView.showHangUp(currentCall);
//					} else if(phoneState == PhoneState.INCOMING) {
//						iView.showReject(currentCall);
//					}
//					phoneState = PhoneState.IDEL;
//					currentCall = null;
//					
//					TimerTaskUtil.cancelTimer("update_duration");
//					
//				//拨打状态
//				} else if (CallStatus.PHONE_CALLING.equals(callStatus.status)) {
//					currentCall = getCallLogFromCallStatus(CallLog.TYPE_CALL_OUT, callStatus);
//					
//					iView.showCalling(currentCall);
//					
//					phoneState = PhoneState.OUTGOING;
//					
//					
//				//来电状态
//				} else if (CallStatus.PHONE_COMING.equals(callStatus.status)) {
//					currentCall = getCallLogFromCallStatus(CallLog.TYPE_CALL_IN, callStatus);
//					
//					iView.showComing(currentCall);
//					
//					phoneState = PhoneState.INCOMING;
//					
//				//通话状态
//				} else if (CallStatus.PHONE_TALKING.equals(callStatus.status)) {
//					if(currentCall != null) {
//						currentCall.duration = 0;
//						
//						iView.showTalking(currentCall);
//						
//						TimerTaskUtil.startTimer("update_duration", 0, 1000, new TimerTask() {
//							
//							@Override
//							public void run() {
//								currentCall.duration += 1000;
//								iView.showTalking(currentCall);
//							}
//						});
//					}
//					
//					phoneState = PhoneState.TALKING;
//				}
//			}
//		}
//
//		@Override
//		public void onCalllogDone() throws RemoteException {
//			//更新通话记录
//			for (int i = 0; i < mCallLogMap.size(); i++) {
//				mAllCallLogList.addAll(mCallLogMap.get(mCallLogMap.keyAt(i)));
//			}
//			Collections.sort(mAllCallLogList, new CallLog.CallLogComparator());
//			
//			iView.showLogsLoaded(true, 0);
//			
//			iView.updateAllLogs(mAllCallLogList);
//			iView.updateMissedLogs(mCallLogMap.get(CallLog.TYPE_CALL_MISS));
//			iView.updateDialedLogs(mCallLogMap.get(CallLog.TYPE_CALL_OUT));
//			iView.updateReceivedLogs(mCallLogMap.get(CallLog.TYPE_CALL_IN));	
//			
//			
//			logsLoading = false;
//		}
//
//		@Override
//		public void onConnected() throws RemoteException {
//			// TODO Auto-generated method stub
//			
//		}
//
//		@Override
//		public void onDisconnected() throws RemoteException {
//			// TODO Auto-generated method stub
//			
//		}
//
//		
//		@Override
//		public void onPhoneBookDone() throws RemoteException {
//			
//			//更新通话记录
//			mContacts = new ArrayList<Contact>(mContactSet);
//			iView.updateBooks(new ArrayList<Contact>(mContactSet));
//			iView.showBooksLoaded(true, 0);
//			
//			
//			//更新通话记录
//			for (int i = 0; i < mCallLogMap.size(); i++) {
//				mAllCallLogList.addAll(mCallLogMap.get(mCallLogMap.keyAt(i)));
//			}
//			Collections.sort(mAllCallLogList, new CallLog.CallLogComparator());
//			
//			iView.showLogsLoaded(true, 0);
//
//			iView.updateAllLogs(mAllCallLogList);
//			iView.updateMissedLogs(mCallLogMap.get(CallLog.TYPE_CALL_MISS));
//			iView.updateDialedLogs(mCallLogMap.get(CallLog.TYPE_CALL_OUT));
//			iView.updateReceivedLogs(mCallLogMap.get(CallLog.TYPE_CALL_IN));
//			
//			
//			new Thread(new Runnable() {
//				@Override
//				public void run() {
//					SystemClock.sleep(1000);
//					iView.syncLogsAlreadyLoad();
//					iView.syncBooksAlreadyLoad();
//				}
//			}).start();
//			
//			booksLoading = false;
//			logsLoading = false;
//			
//		}
//
//		@Override
//		public void onSimDone() throws RemoteException {
//			
//		}
//	};
//	
//	private CallLog getCallLogFromCallStatus(int type, CallStatus callStatus) {
//		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		CallLog log = new CallLog(type, callStatus.name, callStatus.number, df.format(new Date()));
//		return log;
//	}
//
//	@Override
//	public ArrayList<Contact> getBooks() {
//		L.d(thiz, "getBooks mContacts.size : " + mContacts.size());
//		return mContacts;
//	}
//
//	@Override
//	public ArrayList<CallLog> getLogs() {
//		L.d(thiz, "getLogs mAllCallLogList.size : " + mAllCallLogList.size());
//		return mAllCallLogList;
//	}
//
//	@Override
//	public ArrayList<CallLog> getMissedLogs() {
//		L.d(thiz, "getMissedLogs mCallLogMap.get(CallLog.TYPE_CALL_MISS).size : " + mCallLogMap.get(CallLog.TYPE_CALL_MISS).size());
//		return mCallLogMap.get(CallLog.TYPE_CALL_MISS);
//	}
//
//	@Override
//	public ArrayList<CallLog> getReceivedLogs() {
//		L.d(thiz, "getReceivedLogs mCallLogMap.get(CallLog.TYPE_CALL_IN).size : " + mCallLogMap.get(CallLog.TYPE_CALL_IN).size());
//		return mCallLogMap.get(CallLog.TYPE_CALL_IN);
//	}
//
//	@Override
//	public ArrayList<CallLog> getDialedLogs() {
//		L.d(thiz, "getDialedLogs mCallLogMap.get(CallLog.TYPE_CALL_OUT).size : " + mCallLogMap.get(CallLog.TYPE_CALL_OUT).size());
//		return mCallLogMap.get(CallLog.TYPE_CALL_OUT);
//	}

}
