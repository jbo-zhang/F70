/** 
 * nForeTek A2DP Commands Interface for Android 4.3
 *
 * Copyright (C) 2011-2019  nForeTek Corporation
 *
 * Zeus 0.0.0 on 20140606
 * @author KC Huang	<kchuang@nforetek.com>
 * @author Piggy	<piggylee@nforetek.com>
 * @version 0.0.0
 * 
 */

package com.nforetek.bt.aidl;

import com.nforetek.bt.aidl.INfCallbackA2dp;

/**
 * The API interface is for Advanced Audio Distribution Profile (A2DP).
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
 * @see INfCallbackA2dp
 */
 
 interface INfCommandA2dp {

	/** 
	 * Check if A2DP service is ready.
	 */
	boolean isA2dpServiceReady();

 	/** 
	 * Register callback functions for A2DP.
	 * <br>Call this function to register callback functions for A2DP.
	 * <br>Allow nFore service to call back to its registered clients, which is usually the UI application.
	 *
	 * @param cb callback interface instance.
	 * @return true to indicate the operation is successful, or false erroneous.
	 */ 
	boolean registerA2dpCallback(INfCallbackA2dp cb);
	
	/** 
	 * Remove callback functions from A2DP.
     * <br>Call this function to remove previously registered callback interface for A2DP.
     * 
     * @param cb callback interface instance.
	 * @return true to indicate the operation is successful, or false erroneous.
     */ 
	boolean unregisterA2dpCallback(INfCallbackA2dp cb);

	/** 
	 * Get current connection state of the remote device.
	 *
	 * @return current state of A2DP profile service.
	 */ 
	int getA2dpConnectionState();

    /** 
	 * Check if local device is A2DP connected with remote device.
	 *
	 * @return true to indicate A2DP is connected, or false disconnected.
	 */
    boolean isA2dpConnected();

	/** 
	 * Get the Bluetooth hardware address of A2DP connected remote device.
	 *
	 * @return Bluetooth hardware address as string if there is a connected A2DP device, or 
	 * <code>DEFAULT_ADDRESS</code> 00:00:00:00:00:00.
	 */ 
	String getA2dpConnectedAddress();

	/** 
	 * Request to connect A2DP to the remote device.
	 * <br>This is an asynchronous call: it will return immediately, and clients should register and implement callback functions 
	 * {@link INfCallbackA2dp#onA2dpStateChanged onA2dpStateChanged} to be notified of subsequent profile state changes.
	 * 
	 * <p>There is no guarantee that A2DP will be connected and the sequence of state changed callback of profiles! 
	 * <br>This depends on the behavior of connected device.
	 *
	 * @param address valid Bluetooth MAC address.
	 * @return true to indicate the operation is successful, or false erroneous.
	 */ 	
	boolean reqA2dpConnect(String address);
	
	/** 
	 * Request to disconnect A2DP to the remote device.
	 * <p>This is an asynchronous call: it will return immediately, and clients should register and implement callback functions 
	 * {@link INfCallbackA2dp#onA2dpStateChanged onA2dpStateChanged} to be notified of subsequent profile state changes.
	 * 
	 * <p>There is no guarantee that A2DP will be disconnected and the sequence of state changed callback of profiles! 
	 * <br>This depends on the behavior of connected device.
	 *
	 * @param address valid Bluetooth MAC address.
	 * @return true to indicate the operation is successful, or false erroneous.
	 */ 	
	boolean reqA2dpDisconnect(String address);

	/**
	 * Stop send A2DP stream data to audio track.
	 */
	void pauseA2dpRender();

	/**
	 * Start send A2DP stream data to audio track.
	 */
	void startA2dpRender();

	/** 
	 * Request to set the volume of A2DP streaming music locally.
	 * <br>This is an asynchronous call: it will return immediately.
	 *
	 * @param vol volumn level to set. The possible values are from 0.0f to 1.0f.
	 * @return true to indicate the operation is successful, or false erroneous.
	 */ 	
	boolean setA2dpLocalVolume(float vol);
	
	/** 
	 * Request to set the stream type of audio system.
	 * Have to set each time after A2DP is connected.
	 * Default value is AudioManager.STREAM_MUSIC (3)
	 * If need to change stream type during A2DP connected, have to use {@link INfCommandA2dp#pauseA2dpRender pauseA2dpRender} first
	 * and then use {@link INfCommandA2dp#startA2dpRender startA2dpRender} after set stream type.
	 *
	 * <br>This is an asynchronous call: it will return immediately.
	 *
	 * @param type stream type to set.
	 * @return true to indicate the operation is successful, or false erroneous.
	 */ 	
	boolean setA2dpStreamType(int type);
	
	/** 
	 * Get the current stream type of audio system.
	 *
	 * @return current stream type of audio system.
	 */ 	
	int getA2dpStreamType();

 }
