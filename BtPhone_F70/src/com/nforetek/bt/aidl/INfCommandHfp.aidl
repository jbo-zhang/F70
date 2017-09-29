/** 
 * nForeTek HFP Commands Interface for Android 4.3
 *
 * Copyright (C) 2011-2019  nForeTek Corporation
 *
 * Zeus 0.2.0 on 20141112
 * @author KC Huang	<kchuang@nforetek.com>
 * @author Piggy	<piggylee@nforetek.com>
 * @version 0.2.0
 * 
 */

package com.nforetek.bt.aidl;

import com.nforetek.bt.aidl.INfCallbackHfp;
import com.nforetek.bt.aidl.NfHfpClientCall;

/**
 * The API interface is for HandsFree profile (HFP).
 * <br>UI program may use these specific APIs to access to nFore service.
 * <br>The naming principle of API in this doc is as follows,
 *		<blockquote><b>setXXX()</b> : 	set attributes to specific functions of nFore service.
 *		<br><b>reqXXX()</b> : 				request nFore service to implement specific function. It is an Asynchronized mode.
 *		<br><b>isXXX()</b> : 				check the current status from nFore service. It is a Synchronized mode.
 *		<br><b>getXXX()</b> : 				get the current result from nFore service. It is a Synchronized mode.</blockquote>
 *
 * <p>The constant variables in this Doc could be found and referred by importing
 * 		<br><blockquote>com.nforetek.bt.res.NfDef</blockquote>
 * <p>with prefix NfDef class name. Ex : <code>NfDef.DEFAULT_ADDRESS</code>
 *
 * <p>Valid Bluetooth hardware addresses must be in a format such as "00:11:22:33:AA:BB".
 *
 * @see INfCallbackHfp
 */
 
 interface INfCommandHfp {

 	/** 
	 * Check if HFP service is ready.
	 */
	boolean isHfpServiceReady();

	/** 
	 * Register callback functions for HFP.
	 * <br>Call this function to register callback functions for HFP.
	 * <br>Allow nFore service to call back to its registered clients, which is usually the UI application.
	 *
	 * @param cb callback interface instance.
	 * @return true to indicate the operation is successful, or false erroneous.
	 */ 
    boolean registerHfpCallback(INfCallbackHfp cb);
    
	/** 
	 * Remove callback functions from HFP.
     * <br>Call this function to remove previously registered callback interface for HFP.
     * 
     * @param cb callback interface instance.
	 * @return true to indicate the operation is successful, or false erroneous.
     */ 
    boolean unregisterHfpCallback(INfCallbackHfp cb);

	/** 
	 * Get current connection state of the remote device.
	 * 
	 * @return current state of HFP profile service.
	 */ 
    int getHfpConnectionState();

    /** 
	 * Check if local device is HFP connected with remote device.
	 *
	 * @return true to indicate HFP is connected, or false disconnected.
	 */
    boolean isHfpConnected();

    /** 
	 * Get the Bluetooth hardware address of HFP connected remote device.
	 *
	 * @return Bluetooth hardware address as string if there is a connected HFP device, or 
	 * <code>DEFAULT_ADDRESS</code> 00:00:00:00:00:00.
	 */ 
    String getHfpConnectedAddress();

    /** 
	 * Get the current audio state of HFP connected remote device.
	 * 
	 * @return current HFP audio state.
	 */ 
    int getHfpAudioConnectionState();

	/** 
	 * Request to connect HFP to the remote device.
	 * <p>This is an asynchronous call: it will return immediately, and clients should register and implement callback function
	 * {@link INfCallbackHfp#onHfpStateChanged onHfpStateChanged} to be notified of subsequent profile state changes.
	 *
	 * @param address valid Bluetooth MAC address.
	 * @return true to indicate the operation is successful, or false erroneous.
	 */ 
    boolean reqHfpConnect(String address);
    
	/** 
	 * Request to disconnect HFP to the remote device.
	 * <p>This is an asynchronous call: it will return immediately, and clients should register and implement callback function 
	 * {@link INfCallbackHfp#onHfpStateChanged onHfpStateChanged} to be notified of subsequent profile state changes.
	 *
	 * @param address valid Bluetooth MAC address.
	 * @return true to indicate the operation is successful, or false erroneous.
	 */ 
    boolean reqHfpDisconnect(String address);

    /** 
	 * Get the signal strength of HFP connected remote device.
	 *	 	 
	 * @return the current signal strength of remote device.	 
	 */
    int getHfpRemoteSignalStrength();

	/** 
	 * Get the active phone number information of HFP connected remote device.
	 *	 	 
	 * @return the phone number information list of remote device.	 
	 */
    List<NfHfpClientCall> getHfpCallList();
	
    /** 
	 * Check if the HFP connected remote device is on roaming.
	 *	 	 
	 * @return true to indicate the remote device is current on roaming.	 
	 */ 
    boolean isHfpRemoteOnRoaming();

    /** 
	 * Get the battery indicator of HFP connected remote device.
	 *
	 * @return the current battery indicator of remote device.
	 */ 
    int getHfpRemoteBatteryIndicator();

    /** 
	 * Check if the HFP connected remote device has telecom service.
	 *	 	 
	 * @param address valid Bluetooth MAC address.
	 * @return true to indicate remote device has telecom service.
	 */ 
    boolean isHfpRemoteTelecomServiceOn();

    /** 
	 * Check if the voice dial of HFP connected remote device is activated.
	 *
	 * @param address valid Bluetooth MAC address of connected device.	 
	 * @return true to indicate voice dial is activated for remote device.
	 */ 
    boolean isHfpRemoteVoiceDialOn();

	/** 
	 * Request HFP connected remote device to dial a call with given phone number.
	 * <p>This is an asynchronous call: it will return immediately, and clients should register and implement callback functions
	 * {@link INfCallbackHfp#onHfpCallChanged onHfpCallChanged} and {@link INfCallbackHfp#onHfpAudioStateChanged onHfpAudioStateChanged}
	 * to be notified of subsequent profile state changes.
	 *
	 * @param number the outgoing call phone number.
	 * @return true to indicate the operation is successful, or false erroneous.
	 */ 
    boolean reqHfpDialCall(String number);    
    
	/** 
	 * Request HFP connected remote device to re-dial the last outgoing call.
	 * <p>This is an asynchronous call: it will return immediately, and clients should register and implement callback functions
	 * {@link INfCallbackHfp#onHfpCallChanged onHfpCallChanged} and {@link INfCallbackHfp#onHfpAudioStateChanged onHfpAudioStateChanged}
	 * to be notified of subsequent profile state changes.
	 *
	 * @return true to indicate the operation is successful, or false erroneous.
	 */ 
    boolean reqHfpReDial();
    
	/** 
	 * Request HFP connected remote device to do memory dialing.
	 * <p>This is an asynchronous call: it will return immediately, and clients should register and implement callback functions
	 * {@link INfCallbackHfp#onHfpCallChanged onHfpCallChanged} and {@link INfCallbackHfp#onHfpAudioStateChanged onHfpAudioStateChanged}
	 * to be notified of subsequent profile state changes.
	 *
 	 * @param index the memory index on mobile phone. The phone number with the memory index will be dialed out, for example: 1-9
	 * @return true to indicate the operation is successful, or false erroneous.
	 */ 
    boolean reqHfpMemoryDial(String index);         
    
	/** 
	 * Request HFP connected remote device to answer the incoming call.
	 * <p>This is an asynchronous call: it will return immediately, and clients should register and implement callback functions
	 * {@link INfCallbackHfp#onHfpCallChanged onHfpCallChanged} and {@link INfCallbackHfp#onHfpAudioStateChanged onHfpAudioStateChanged}
	 * to be notified of subsequent profile state changes.
	 *
	 * @param flag action policy while accepting a call. Possible values are
     *		<br>CALL_ACCEPT_NONE					(int) 0 
     * 		<br>If have a incoming call, setting this flag will pickup the call.
     * 		<br>If have multi call, this flag will merge calls to multi party state.
     *
	 * 		<br>CALL_ACCEPT_HOLD					(int) 1
	 * 		<br>This flag will hold current call and answer the hold call or incoming call.
	 *
	 * 		<br>CALL_ACCEPT_TERMINATE				(int) 2
	 * 		<br>This flag will termintate current call and answer the hold call or incoming call.
	 * 
	 * @return true if the command is sent successfully
	 */ 
    boolean reqHfpAnswerCall(int flag);
    
	/** 
	 * Request HFP connected remote device to reject the incoming call.
	 * <p>This is an asynchronous call: it will return immediately, and clients should register and implement callback functions
	 * {@link INfCallbackHfp#onHfpCallChanged onHfpCallChanged} and {@link INfCallbackHfp#onHfpAudioStateChanged onHfpAudioStateChanged}
	 * to be notified of subsequent profile state changes.
	 *
	 * @return true if the command is sent successfully
	 */ 
    boolean reqHfpRejectIncomingCall();
    
	/** 
	 * Request HFP connected remote device to terminate the ongoing call.
	 * <p>This is an asynchronous call: it will return immediately, and clients should register and implement callback functions
	 * {@link INfCallbackHfp#onHfpCallChanged onHfpCallChanged} and {@link INfCallbackHfp#onHfpAudioStateChanged onHfpAudioStateChanged}
	 * to be notified of subsequent profile state changes.
	 *
	 * @return true if the command is sent successfully
	 */ 
    boolean reqHfpTerminateCurrentCall();
    
	/** 
	 * Request HFP connected remote device to send the DTMF.
	 * <p>Due to the compatibility, please request this API with single DTMF number each time.
	 * <p>Avoid requesting with serial DTMF numbers. 
	 *
 	 * @param number DTMF number.
 	 * @return true if the command is sent successfully
	 */ 
    boolean reqHfpSendDtmf(String number);
    
	/** 
	 * Request HFP connected remote device to transfer the audio to Carkit.
	 * <p>This is an asynchronous call: it will return immediately, and clients should register and implement callback function
	 * {@link INfCallbackHfp#onHfpAudioStateChanged onHfpAudioStateChanged} to be notified of subsequent state changes.
	 *
	 * @return true if the command is sent successfully	 
	 */
    boolean reqHfpAudioTransferToCarkit();

    /** 
	 * Request HFP connected remote device to transfer the audio to Phone.
	 * <p>This is an asynchronous call: it will return immediately, and clients should register and implement callback function
	 * {@link INfCallbackHfp#onHfpAudioStateChanged onHfpAudioStateChanged} to be notified of subsequent state changes.
	 *
	 * @return true if the command is sent successfully	 
	 */
    boolean reqHfpAudioTransferToPhone();
    
	/** 
	 * Get the network operator of HFP connected remote device.
	 *	 	 
	 * @return network operator
	 */ 
    String getHfpRemoteNetworkOperator();
    
	/** 
	 * Get the subscriber number of HFP connected remote device.
	 *	 	 
	 * @return subscriber number
	 */ 
    String getHfpRemoteSubscriberNumber();

    /**
	 * Request HFP connected remote device to do the voice dialing.
	 * <p>This is an asynchronous call: it will return immediately, and clients should register and implement callback functions
	 * {@link INfCallbackHfp#onHfpCallChanged onHfpCallChanged} and {@link INfCallbackHfp#onHfpAudioStateChanged onHfpAudioStateChanged}
	 * to be notified of subsequent profile state changes.
	 * 
 	 * @param enable true to start the voice dialing.
	 * @return true to indicate the operation is successful, or false erroneous.
	 */
    boolean reqHfpVoiceDial(boolean enable);

	/**
	 * Stop sending HFP stream data to audio track.
	 */
	void pauseHfpRender();

	/**
	 * Start sending HFP stream data to audio track.
	 */
	void startHfpRender();

	/**
	 * Check if mic is mute.
	 */
	boolean isHfpMicMute();

	/**
	 * Request HFP Mute Mic during call
	 * @param mute true to mute the microphone
	 */
	void muteHfpMic(boolean mute);

	/**
	 * Check if remote device support in-band ringtone.
     * Only available when HFP connected.
	 *
	 * @return true to indicate remote device support in-band ringtone.
	 */
	boolean isHfpInBandRingtoneSupport();
	
 }
