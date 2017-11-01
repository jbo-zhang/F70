package com.hwatong.btphone.model;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.TimerTask;
import java.util.TreeSet;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.RemoteException;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;

import com.hwatong.btphone.CallStatus;
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
import com.nforetek.bt.aidl.INfCallbackBluetooth;
import com.nforetek.bt.aidl.INfCallbackHfp;
import com.nforetek.bt.aidl.INfCallbackPbap;
import com.nforetek.bt.aidl.INfCommandBluetooth;
import com.nforetek.bt.aidl.INfCommandHfp;
import com.nforetek.bt.aidl.INfCommandPbap;
import com.nforetek.bt.aidl.NfHfpClientCall;
import com.nforetek.bt.aidl.NfPbapContact;
import com.nforetek.bt.res.NfDef;

public class NForeModel implements IBTPhoneModel {
	private static final String thiz = NForeModel.class.getSimpleName();

	/**
	 * View接口
	 */
	public IUIView iView;

	/**
	 * 三个通话类型列表
	 */
	private SparseArray<ArrayList<CallLog>> mCallLogMap = new SparseArray<ArrayList<CallLog>>(3);

	/**
	 * 所有通话记录列表
	 */
	private ArrayList<CallLog> mAllCallLogList = new ArrayList<CallLog>();

	/**
	 * 所有通讯录列表
	 */
	private ArrayList<Contact> mContacts = new ArrayList<Contact>();

	/**
	 * 通讯录列表 去重 排序
	 */
	private TreeSet<Contact> mContactSet = new TreeSet<Contact>();

	/**
	 * 蓝牙服务Action
	 */
	private static final String ACTION_BT_SERVICE = "com.hwatong.btphone.service";

	/**
	 * 通讯录加载中
	 */
	private boolean booksLoading = false;

	/**
	 * 通话记录加载中
	 */
	private boolean logsLoading = false;

	/**
	 * 锁对象
	 */
	private Object lock = new Object();

	/**
	 * 通话状态
	 */
	private PhoneState phoneState = PhoneState.IDEL;

	/**
	 * 当前CallLog
	 */
	private CallLog currentCall;

	/**
	 * 声音通道
	 */
	private boolean isCar;

	/**
	 * 麦克风状态
	 */
	private boolean isMute;

	/**
	 * 蓝牙地址
	 */
	private String targetAddress = NfDef.DEFAULT_ADDRESS;

	/**
	 * 需要下载的字段
	 */
	private int mProperty = NfDef.PBAP_PROPERTY_MASK_FN | NfDef.PBAP_PROPERTY_MASK_N | NfDef.PBAP_PROPERTY_MASK_TEL
			| NfDef.PBAP_PROPERTY_MASK_VERSION | NfDef.PBAP_PROPERTY_MASK_ADR | NfDef.PBAP_PROPERTY_MASK_EMAIL
			| NfDef.PBAP_PROPERTY_MASK_PHOTO;

	public NForeModel(IUIView iView) {
		this.iView = iView;
		mCallLogMap.put(CallLog.TYPE_CALL_IN, new ArrayList<CallLog>());
		mCallLogMap.put(CallLog.TYPE_CALL_OUT, new ArrayList<CallLog>());
		mCallLogMap.put(CallLog.TYPE_CALL_MISS, new ArrayList<CallLog>());
	}

	private INfCommandHfp mCommandHfp;
	private INfCommandPbap mCommandPbap;
	private INfCommandBluetooth mCommandBluetooth;

	@Override
	public void link(Context context) {
		context.bindService(new Intent(NfDef.CLASS_SERVICE_BLUETOOTH), this.mConnection, Context.BIND_AUTO_CREATE);
		context.bindService(new Intent(NfDef.CLASS_SERVICE_HFP), this.mConnection, Context.BIND_AUTO_CREATE);
		context.bindService(new Intent(NfDef.CLASS_SERVICE_PBAP), this.mConnection, Context.BIND_AUTO_CREATE);
	}

	private ServiceConnection mConnection = new ServiceConnection() {
		public void onServiceConnected(android.content.ComponentName className, android.os.IBinder service) {
			// HFP
			if (className.equals(new ComponentName(NfDef.PACKAGE_NAME, NfDef.CLASS_SERVICE_HFP))) {
				L.d(thiz, "ComponentName(" + NfDef.CLASS_SERVICE_HFP + ")");
				mCommandHfp = INfCommandHfp.Stub.asInterface(service);
				if (mCommandHfp == null) {
					L.d(thiz, "mCommandHfp is null!!");
					return;
				}
				dumpClassMethod(mCommandHfp.getClass());
				try {
					mCommandHfp.registerHfpCallback(mCallbackHfp);
				} catch (RemoteException e) {
					e.printStackTrace();
				}

				// PBAP
			} else if (className.equals(new ComponentName(NfDef.PACKAGE_NAME, NfDef.CLASS_SERVICE_PBAP))) {
				L.d(thiz, "ComponentName(" + NfDef.CLASS_SERVICE_PBAP + ")");
				mCommandPbap = INfCommandPbap.Stub.asInterface(service);
				if (mCommandPbap == null) {
					L.d(thiz, "mCommandPbap is null !!");
					return;
				}
				dumpClassMethod(mCommandPbap.getClass());
				try {
					mCommandPbap.registerPbapCallback(mCallbackPbap);
					loadBooks();
					loadLogs();
					
				} catch (RemoteException e) {
					e.printStackTrace();
				}

				// BlUETOOTH
			} else if (className.equals(new ComponentName(NfDef.PACKAGE_NAME, NfDef.CLASS_SERVICE_BLUETOOTH))) {
				L.d(thiz, "ComponentName(" + NfDef.CLASS_SERVICE_BLUETOOTH + ")");
				mCommandBluetooth = INfCommandBluetooth.Stub.asInterface(service);
				if (mCommandBluetooth == null) {
					L.d(thiz, "mCommandBluetooth is null !!");
					return;
				}
				dumpClassMethod(mCommandBluetooth.getClass());
				try {
					mCommandBluetooth.registerBtCallback(mCallbackBluetooth);
				} catch (RemoteException e) {
					e.printStackTrace();
				}
			}
		};

		public void onServiceDisconnected(android.content.ComponentName className) {
			L.d(thiz, "ready onServiceDisconnected: " + className);
			if (className.equals(new ComponentName(NfDef.PACKAGE_NAME, NfDef.CLASS_SERVICE_HFP))) {
				mCommandHfp = null;
			} else if (className.equals(new ComponentName(NfDef.PACKAGE_NAME, NfDef.CLASS_SERVICE_PBAP))) {
				mCommandPbap = null;
			} else if (className.equals(new ComponentName(NfDef.PACKAGE_NAME, NfDef.CLASS_SERVICE_BLUETOOTH))) {
				mCommandBluetooth = null;
			}
			L.d(thiz, "end onServiceDisconnected");
		};
	};

	private void dumpClassMethod(Class c) {
		for (Method method : c.getDeclaredMethods()) {
			L.d(thiz, "Method name: " + method.getName());
		}
	}

	/*
	 * Hfp callback
	 */
	private INfCallbackHfp mCallbackHfp = new INfCallbackHfp.Stub() {

		@Override
		public void onHfpServiceReady() throws RemoteException {
			L.d(thiz, "onHfpServiceReady()");

		}

		/**
		 * 蓝牙状态变化
		 */
		@Override
		public void onHfpStateChanged(String address, int prevState, int newState) throws RemoteException {
			L.d(thiz, "onHfpStateChanged() " + address + " state: " + prevState + "->" + newState);

			if (newState == NfDef.STATE_CONNECTED) {
				iView.showConnected();

			} else if (newState == NfDef.STATE_READY) {
				iView.showDisconnected();

			} else if (newState == NfDef.STATE_CONNECTING) {

			} else if (newState == NfDef.STATE_NOT_INITIALIZED) {

			}
		}

		/**
		 * 声音通道变化
		 */
		@Override
		public void onHfpAudioStateChanged(String address, int prevState, int newState) throws RemoteException {
			L.d(thiz, "onHfpAudioStateChanged() " + address + " state: " + prevState + "->" + newState);
			String state = newState == NfDef.STATE_CONNECTED ? "At Speaker" : "At Phone";
			isCar = newState == NfDef.STATE_CONNECTED;
			iView.showSoundTrack(isCar);
		}

		@Override
		public void onHfpVoiceDial(String address, boolean isVoiceDialOn) throws RemoteException {
			L.d(thiz, "onHfpVoiceDial() " + address + " isVoiceDialOn: " + isVoiceDialOn);

		}

		@Override
		public void onHfpErrorResponse(String address, int code) throws RemoteException {
			L.d(thiz, "onHfpErrorResponse() " + address + " code: " + code);

		}

		@Override
		public void onHfpRemoteTelecomService(String address, boolean isTelecomServiceOn) throws RemoteException {
			L.d(thiz, "onHfpRemoteTelecomService() " + address + " isTelecomServiceOn: " + isTelecomServiceOn);

		}

		@Override
		public void onHfpRemoteRoamingStatus(String address, boolean isRoamingOn) throws RemoteException {
			L.d(thiz, "onHfpRemoteRoamingStatus() " + address + " isRoamingOn: " + isRoamingOn);

		}

		@Override
		public void onHfpRemoteBatteryIndicator(String address, int currentValue, int maxValue, int minValue)
				throws RemoteException {
			L.d(thiz, "onHfpRemoteBatteryIndicator() " + address + " value: " + currentValue + " (" + minValue + "-"
					+ maxValue + ")");

		}

		@Override
		public void onHfpRemoteSignalStrength(String address, int currentStrength, int maxStrength, int minStrength)
				throws RemoteException {
			L.d(thiz, "onHfpRemoteSignalStrength() " + address + " strength: " + currentStrength + " (" + minStrength
					+ "-" + maxStrength + ")");

		}

		/**
		 * 通话状态变化
		 */
		@Override
		public void onHfpCallChanged(String address, NfHfpClientCall call) throws RemoteException {

			L.d(thiz, "onHfpCallChanged() " + address);
			if (call != null) {
				int callstate = call.getState();
				L.d(thiz, "callstate " + callstate);
				// 来电状态
				if (callstate == NfHfpClientCall.CALL_STATE_INCOMING) {
					currentCall = getCallLogFromCallStatus(CallLog.TYPE_CALL_IN, call);

					iView.showComing(currentCall);

					phoneState = PhoneState.INCOMING;

					// 闲置状态
				} else if (callstate == NfHfpClientCall.CALL_STATE_TERMINATED) {
					if (phoneState == PhoneState.TALKING || phoneState == PhoneState.OUTGOING) {
						iView.showHangUp(currentCall);
					} else if (phoneState == PhoneState.INCOMING) {
						iView.showReject(currentCall);
					}

					currentCall = null;

					TimerTaskUtil.cancelTimer("update_duration");

					phoneState = PhoneState.IDEL;

					// 通话状态
				} else if (callstate == NfHfpClientCall.CALL_STATE_ACTIVE) {
					
					if(currentCall == null) {
						currentCall = getCallLogFromCallStatus(CallLog.TYPE_CALL_OUT, call);
					}
					
					if (currentCall != null) {
						currentCall.duration = 0;

						iView.showTalking(currentCall);

						TimerTaskUtil.startTimer("update_duration", 0, 1000, new TimerTask() {

							@Override
							public void run() {
								currentCall.duration += 1000;
								iView.showTalking(currentCall);
							}
						});
					}
					phoneState = PhoneState.TALKING;

					// 拨打状态
				} else if (callstate == NfHfpClientCall.CALL_STATE_ALERTING) {
					currentCall = getCallLogFromCallStatus(CallLog.TYPE_CALL_OUT, call);

					iView.showCalling(currentCall);

					phoneState = PhoneState.OUTGOING;
				}
			}
		}
	};

	private CallLog getCallLogFromCallStatus(int type, NfHfpClientCall call) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String number = call.getNumber();
		String date = df.format(new Date());
		String name = "";
		synchronized (mContacts) {
			for (int i = 0; i < mContacts.size(); i++) {
				final Contact contact = mContacts.get(i);
				final String number2 = contact.number;
				if (number2.equals(number)) {
					name = contact.name;
					break;
				}
			}
		}
		CallLog log = new CallLog(type, name, number, date);
		return log;
	}

	/*
	 * Pbap Callback
	 */
	private INfCallbackPbap mCallbackPbap = new INfCallbackPbap.Stub() {

		@Override
		public void onPbapServiceReady() throws RemoteException {
			L.d(thiz, "onPbapServiceReady()");
		}

		@Override
		public void onPbapStateChanged(String address, int prevState, int newState, int reason, int counts)
				throws RemoteException {
			if (newState == NfDef.STATE_READY){
				
            }
            switch (reason) {
                case NfDef.REASON_DOWNLOAD_FULL_CONTENT_COMPLETED :
                	//通话记录加载完成
                    if(logsLoading) {
                    	mAllCallLogList.clear();
          				//更新通话记录
          				for (int i = 0; i < mCallLogMap.size(); i++) {
          					mAllCallLogList.addAll(mCallLogMap.get(mCallLogMap.keyAt(i)));
          				}
          				Collections.sort(mAllCallLogList, new CallLog.CallLogComparator());
          				
          				iView.updateAllLogs(mAllCallLogList);
          				iView.updateMissedLogs(mCallLogMap.get(CallLog.TYPE_CALL_MISS));
          				iView.updateDialedLogs(mCallLogMap.get(CallLog.TYPE_CALL_OUT));
          				iView.updateReceivedLogs(mCallLogMap.get(CallLog.TYPE_CALL_IN));
          				
          				showLogsLoadedAndSync(true, reason);
            			
            			logsLoading = false;
                    }
                    
                    //通讯录加载完成
                    if(booksLoading) {
                    	mContacts = new ArrayList<Contact>(mContactSet);
        				iView.updateBooks(mContacts);
        				
        				showBooksLoadedAndSync(true, reason);
            			
            			booksLoading = false;
                    }
                    
                    break;
                case NfDef.REASON_DOWNLOAD_FAILED :
                case NfDef.REASON_DOWNLOAD_TIMEOUT :
                case NfDef.REASON_DOWNLOAD_USER_REJECT:
                	//通话记录加载失败
                	if(logsLoading) {
                		showLogsLoadedAndSync(false, reason);
            			logsLoading = false;
                	}
                	//通讯录加载失败
                	if(booksLoading) {
                		mContacts = new ArrayList<Contact>(mContactSet);
        				iView.updateBooks(mContacts);
        				
        				showBooksLoadedAndSync(false, reason);
            			
            			booksLoading = false;
                	}
                	
                	break;
                default:
                    if (newState == NfDef.STATE_READY) {
                    	
                    }
                    else if (newState == NfDef.STATE_DOWNLOADED) {
                    	
                    }
                    else if (newState == NfDef.STATE_CONNECTED) {
                    	
                    }
                    break;

            }
		}

		/**
		 * 得到通讯录的方法
		 */
		@Override
		public void retPbapDownloadedContact(NfPbapContact nfContact) throws RemoteException {
			L.d(thiz, "retPbapDownloadedContact()");
			Contact[] contacts = getContactsFromNfPbapContact(nfContact);
			for (Contact contact : contacts) {
				mContactSet.add(contact);
			}
			if (mContactSet.size() % 30 == 0) {
				mContacts = new ArrayList<Contact>(mContactSet);
				iView.updateBooks(mContacts);
			}
		}

		/**
		 * 得到通话记录的方法
		 */
		@Override
		public void retPbapDownloadedCallLog(String address, String firstName, String middleName, String lastName,
				String number, int type, String timestamp) throws RemoteException {
			L.d(thiz, "retPbapDownloadedCallLog() " + address + " lastName: " + lastName + " (" + type + ")");

			String name = firstName + middleName + lastName;
			name = name.replaceAll(" +", "");
			number = number.replaceAll(":", "");
			int typeInt = CallLog.TYPE_CALL_MISS;
			switch (type) {
			case NfPbapContact.STORAGE_TYPE_MISSED_CALLS:
				typeInt = CallLog.TYPE_CALL_MISS;
				break;
			case NfPbapContact.STORAGE_TYPE_RECEIVED_CALLS:
				typeInt = CallLog.TYPE_CALL_IN;
				break;
			case NfPbapContact.STORAGE_TYPE_DIALED_CALLS:
				typeInt = CallLog.TYPE_CALL_OUT;
				break;
			}

			CallLog callLog = new CallLog(typeInt, name, number, timestamp);

			ArrayList<CallLog> callLogs = mCallLogMap.get(typeInt);
			if (callLogs == null) {
				callLogs = new ArrayList<CallLog>();
				mCallLogMap.put(typeInt, callLogs);
			}
			callLogs.add(callLog);
		}

		@Override
		public void onPbapDownloadNotify(String address, int storage, int totalContacts, int downloadedContacts)
				throws RemoteException {
			L.d(thiz, "onPbapDownloadNotify() " + address + " storage: " + storage + " downloaded: "
					+ downloadedContacts + "/" + totalContacts);
		}

		@Override
		public void retPbapDatabaseQueryNameByNumber(String address, String target, String name, boolean isSuccess)
				throws RemoteException {
			L.d(thiz, "retPbapDatabaseQueryNameByNumber() " + address + " target: " + target + " name: " + name
					+ " isSuccess: " + isSuccess);
		}

		@Override
		public void retPbapDatabaseQueryNameByPartialNumber(String address, String target, String[] names,
				String[] numbers, boolean isSuccess) throws RemoteException {
			L.d(thiz, "retPbapDatabaseQueryNameByPartialNumber() " + address + " target: " + target + " isSuccess: "
					+ isSuccess);
		}

		@Override
		public void retPbapDatabaseAvailable(String address) throws RemoteException {
			L.d(thiz, "retPbapDatabaseAvailable() " + address);
		}

		@Override
		public void retPbapDeleteDatabaseByAddressCompleted(String address, boolean isSuccess) throws RemoteException {
			L.d(thiz, "retPbapDeleteDatabaseByAddressCompleted() " + address + " isSuccess: " + isSuccess);
		}

		@Override
		public void retPbapCleanDatabaseCompleted(boolean isSuccess) throws RemoteException {
			L.d(thiz, "retPbapCleanDatabaseCompleted() isSuccess: " + isSuccess);
		}

	};

	/*
	 * Bluetooth callback
	 */
	private INfCallbackBluetooth mCallbackBluetooth = new INfCallbackBluetooth.Stub() {

		@Override
		public void onBluetoothServiceReady() throws RemoteException {
			L.d(thiz, "onBluetoothServiceReady()");
		}

		@Override
		public void onAdapterStateChanged(int prevState, int newState) throws RemoteException {
			L.d(thiz, "onAdapterStateChanged() state: " + prevState + "->" + newState);
		}

		@Override
		public void onAdapterDiscoverableModeChanged(int prevState, int newState) throws RemoteException {
			L.d(thiz, "onAdapterDiscoverableModeChanged() state: " + prevState + "->" + newState);
		}

		@Override
		public void onAdapterDiscoveryStarted() throws RemoteException {
			L.d(thiz, "onAdapterDiscoveryStarted()");
		}

		@Override
		public void onAdapterDiscoveryFinished() throws RemoteException {
			L.d(thiz, "onAdapterDiscoveryFinished()");
		}

		@Override
		public void retPairedDevices(int elements, String[] address, String[] name, int[] supportProfile,
				byte[] category) throws RemoteException {
			L.d(thiz, "retPairedDevices() elements: " + elements);
		}

		@Override
		public void onDeviceFound(String address, String name, byte category) throws RemoteException {
			L.d(thiz, "onDeviceFound() " + address + " name: " + name);
		}

		@Override
		public void onDeviceBondStateChanged(String address, String name, int prevState, int newState)
				throws RemoteException {
			L.d(thiz, "onDeviceBondStateChanged() " + address + " name: " + name + " state: " + prevState + "->"
					+ newState);
		}

		@Override
		public void onDeviceUuidsUpdated(String address, String name, int supportProfile) throws RemoteException {
			L.d(thiz, "onDeviceUuidsUpdated() " + address + " name: " + name + " supportProfile: " + supportProfile);
		}

		@Override
		public void onLocalAdapterNameChanged(String name) throws RemoteException {
			L.d(thiz, "onLocalAdapterNameChanged() " + name);
		}

		@Override
		public void onDeviceOutOfRange(String address) throws RemoteException {
			L.d(thiz, "onDeviceOutOfRange() " + address);
		}

		@Override
		public void onDeviceAclDisconnected(String address) throws RemoteException {
			L.d(thiz, "onDeviceAclDisconnected() " + address);

		}

		@Override
		public void onBtRoleModeChanged(int mode) throws RemoteException {
			L.d(thiz, "onBtRoleModeChanged() " + mode);

		}

	};

	@Override
	public void unlink(Context context) {
		try {
			if (mCommandHfp != null) {
				mCommandHfp.unregisterHfpCallback(mCallbackHfp);
			}
			if (mCommandPbap != null) {
				mCommandPbap.unregisterPbapCallback(mCallbackPbap);
			}
			if (mCommandBluetooth != null) {
				mCommandBluetooth.unregisterBtCallback(mCallbackBluetooth);
			}
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		L.d(thiz, "start unbind service");
		context.unbindService(mConnection);
		L.d(thiz, "end unbind service");
	}

	@Override
	public void sync() {
		if(mCommandHfp != null) {
			try {
				if(mCommandHfp.isHfpConnected()) {
					//同步连接状态
					iView.showConnected();
					
					//同步通讯录下载状态
					if(booksLoading) {
						iView.showBooksLoading();
					} else {
						iView.syncBooksAlreadyLoad();
					}
					
					//同步通话记录下载状态
					if(logsLoading) {
						iView.showLogsLoading();
					} else {
						iView.syncLogsAlreadyLoad();
					}
					
					//同步通讯录
					iView.updateBooks(mContacts);
					
					//同步通话记录
					iView.updateAllLogs(mAllCallLogList);
					iView.updateDialedLogs(mCallLogMap.get(CallLog.TYPE_CALL_OUT));
					iView.updateMissedLogs(mCallLogMap.get(CallLog.TYPE_CALL_MISS));
					iView.updateReceivedLogs(mCallLogMap.get(CallLog.TYPE_CALL_IN));
					
					//同步通话状态
					if(phoneState == PhoneState.TALKING) {
						iView.showTalking(currentCall);
					} else if(phoneState == PhoneState.INCOMING) {
						iView.showComing(currentCall);
					} else if(phoneState == PhoneState.OUTGOING) {
						iView.showCalling(currentCall);
					} else {
						iView.showIdel();
					}
					
					//同步麦克风
					isMute = mCommandHfp.isHfpMicMute();
					iView.showMicMute(isMute);
					
					//同步声音通道
					iView.showSoundTrack(isCar);
					
				} else {
					iView.showDisconnected();
				}
				
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		} else {
			iView.showDisconnected();
		}
	}

	@Override
	public void dial(String number) {
		L.d(thiz, "dial() "+ (mCommandHfp == null));
		if (mCommandHfp != null) {
			try {
				mCommandHfp.reqHfpDialCall(number);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void pickUp() {
		L.d(thiz, "pickUp() "+ (mCommandHfp == null));
		if (mCommandHfp != null) {
			try {
				mCommandHfp.reqHfpAnswerCall(NfDef.CALL_ACCEPT_NONE);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void hangUp() {
		L.d(thiz, "hangUp() "+ (mCommandHfp == null));
		if (mCommandHfp != null) {
			try {
				mCommandHfp.reqHfpTerminateCurrentCall();
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void reject() { 
		L.d(thiz, "reject() "+ (mCommandHfp == null));
		if (mCommandHfp != null) {
			try {
				mCommandHfp.reqHfpRejectIncomingCall();
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void dtmf(char code) {
		L.d(thiz, "dtmf code = " + code + "  " + (mCommandHfp == null)) ;
		if (mCommandHfp != null) {
			try {
				mCommandHfp.reqHfpSendDtmf(String.valueOf(code));
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}

	}

	@Override
	public void toggleMic() {
		L.d(thiz, "toggleMic : " + (mCommandHfp == null) + " " +  isMute) ;
		if (mCommandHfp != null) {
			try {
				mCommandHfp.muteHfpMic(!isMute);

				isMute = mCommandHfp.isHfpMicMute();
				iView.showMicMute(isMute);

			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}

	}

	@Override
	public void toggleTrack() {
		L.d(thiz, "toggleTrack : " + (mCommandHfp == null) + " " +  isCar) ;
		if (mCommandHfp != null) {
			try {
				if (isCar) {
					mCommandHfp.reqHfpAudioTransferToPhone();
				} else {
					mCommandHfp.reqHfpAudioTransferToCarkit();
				}
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}

	}

	@Override
	public void loadBooks() {
		L.d(thiz, "loadBooks : " + (mCommandPbap == null) + " " +  booksLoading) ;
		if (booksLoading) {
			return;
		}
		if (mCommandPbap != null) {
			try {
				int storage = NfDef.PBAP_STORAGE_PHONE_MEMORY;
				mCommandPbap.reqPbapDownload(mCommandHfp.getHfpConnectedAddress(), storage, mProperty);
				booksLoading = true;
				clearBooks();
				iView.showBooksLoadStart();
				new Thread(new Runnable() {
					@Override
					public void run() {
						SystemClock.sleep(500);
						iView.showBooksLoading();
					}
				}).start();
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void loadLogs() {
		L.d(thiz, "loadLogs()");
		if (logsLoading) {
			return;
		}
		if (mCommandPbap != null) {
			try {
				L.d(thiz, "loadlogs missedcalls");
				int storage = NfDef.PBAP_STORAGE_MISSED_CALLS;
				mCommandPbap.reqPbapDownload(mCommandHfp.getHfpConnectedAddress(), storage, mProperty);

				L.d(thiz, "loadlogs dialedcalls");
				
				storage = NfDef.PBAP_STORAGE_DIALED_CALLS;
				mCommandPbap.reqPbapDownload(mCommandHfp.getHfpConnectedAddress(), storage, mProperty);

				storage = NfDef.PBAP_STORAGE_RECEIVED_CALLS;
				mCommandPbap.reqPbapDownload(mCommandHfp.getHfpConnectedAddress(), storage, mProperty);

				logsLoading = true;

				iView.showLogsLoadStart();

				new Thread(new Runnable() {
					@Override
					public void run() {
						SystemClock.sleep(500);
						iView.showLogsLoading();
					}
				}).start();

			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void loadMissedLogs() {
		L.d(thiz, "loadMissedLogs : " + (mCommandPbap == null)) ;
		loadLogsByType(NfDef.PBAP_STORAGE_MISSED_CALLS, CallLog.TYPE_CALL_MISS);
	}

	@Override
	public void loadDialedLogs() {
		L.d(thiz, "loadDialedLogs : " + (mCommandPbap == null)) ;
		loadLogsByType(NfDef.PBAP_STORAGE_DIALED_CALLS, CallLog.TYPE_CALL_OUT);
	}

	@Override
	public void loadReceivedLogs() {
		L.d(thiz, "loadReceivedLogs : " + (mCommandPbap == null)) ;
		loadLogsByType(NfDef.PBAP_STORAGE_RECEIVED_CALLS, CallLog.TYPE_CALL_IN);
	}

	private void loadLogsByType(int storage, int typeInt) {
		
		if (logsLoading) {
			return;
		}
		if (mCommandPbap != null) {
			try {
				mCommandPbap.reqPbapDownload(mCommandHfp.getHfpConnectedAddress(), storage, mProperty);

				logsLoading = true;
				clearLogsByType(typeInt);
				iView.showLogsLoadStart();
				new Thread(new Runnable() {
					@Override
					public void run() {
						SystemClock.sleep(500);
						iView.showLogsLoading();
					}
				}).start();

			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public ArrayList<Contact> getBooks() {
		L.d(thiz, "getBooks mContacts.size : " + mContacts.size());
		return mContacts;
	}

	@Override
	public ArrayList<CallLog> getLogs() {
		L.d(thiz, "getLogs mAllCallLogList.size : " + mAllCallLogList.size());
		return mAllCallLogList;
	}

	@Override
	public ArrayList<CallLog> getMissedLogs() {
		L.d(thiz,
				"getMissedLogs mCallLogMap.get(CallLog.TYPE_CALL_MISS).size : "
						+ mCallLogMap.get(CallLog.TYPE_CALL_MISS).size());
		return mCallLogMap.get(CallLog.TYPE_CALL_MISS);
	}

	@Override
	public ArrayList<CallLog> getReceivedLogs() {
		L.d(thiz,
				"getReceivedLogs mCallLogMap.get(CallLog.TYPE_CALL_IN).size : "
						+ mCallLogMap.get(CallLog.TYPE_CALL_IN).size());
		return mCallLogMap.get(CallLog.TYPE_CALL_IN);
	}

	@Override
	public ArrayList<CallLog> getDialedLogs() {
		L.d(thiz,
				"getDialedLogs mCallLogMap.get(CallLog.TYPE_CALL_OUT).size : "
						+ mCallLogMap.get(CallLog.TYPE_CALL_OUT).size());
		return mCallLogMap.get(CallLog.TYPE_CALL_OUT);
	}

	private void clearBooks() {
		mContactSet.clear();
	}

	private void clearAllLogs() {
		mCallLogMap.get(CallLog.TYPE_CALL_OUT).clear();
		mCallLogMap.get(CallLog.TYPE_CALL_IN).clear();
		mCallLogMap.get(CallLog.TYPE_CALL_MISS).clear();
		mAllCallLogList.clear();
	}

	private void clearLogsByType(int type) {
		mCallLogMap.get(type).clear();
	}

	private Contact[] getContactsFromNfPbapContact(NfPbapContact nfContact) {
		String name = nfContact.getFirstName() + nfContact.getMiddleName() + nfContact.getLastName();
		String[] numbers = nfContact.getNumberArray();
		Contact[] contacts = new Contact[numbers.length];
		for (int i = 0; i < numbers.length; i++) {
			if (TextUtils.isEmpty(numbers[i]) || !numbers[i].matches("^\\d*")) {
				continue;
			}
			name = name.replaceAll(" +", "");
			String number = numbers[i].replaceAll(":", "");
			String[] strs = Utils.getPinyinAndFirstLetter(name);
			contacts[i] = new Contact(name, number, strs[0], strs[1]);
		}
		return contacts;
	}
	
	
	private void showBooksLoadedAndSync(boolean succeed, int reason) {
		iView.showBooksLoaded(succeed, reason);
		new Thread(new Runnable() {
			@Override
			public void run() {
				SystemClock.sleep(1000);
				iView.syncBooksAlreadyLoad();
			}
		}).start();
	}
	
	private void showLogsLoadedAndSync(boolean succeed, int reason) {
		iView.showLogsLoaded(succeed, reason);
		new Thread(new Runnable() {
			@Override
			public void run() {
				SystemClock.sleep(1000);
				iView.syncLogsAlreadyLoad();
			}
		}).start();
	}
	
	
	
	
	
	
	

}
