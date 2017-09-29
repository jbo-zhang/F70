/** 
 * nForeTek HID Commands Interface for Android 4.3
 *
 * Copyright (C) 2011-2019  nForeTek Corporation
 *
 * Zeus 0.6.2 on 20150127
 * @author KC Huang	<kchuang@nforetek.com>
 * @author Piggy	<piggylee@nforetek.com>
 * @version 0.1.6
 * 
 */

package com.nforetek.bt.aidl;

import com.nforetek.bt.aidl.INfCallbackHid;

/**
 * The API interface is for Human Interface Device profile (HID).
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
 * @see INfCallbackHid
 */
 
 interface INfCommandHid {

 	/** 
	 * Check if HID service is ready.
	 */
	boolean isHidServiceReady();

 	/** 
	 * Register callback functions for HID profile.
	 * Call this function to register callback functions for HID profile.
	 * Allow nFore service to call back to its registered clients, which is often UI.
	 *
	 * @param cb : callback interface instance.
	 * @return true to indicate the operation is successful, or false erroneous.
	 */ 
	boolean registerHidCallback(INfCallbackHid cb);
	
	/** 
	 * Remove callback functions for HID profile.
     * Call this function to remove previously registered callback interface for HID profile.
     * 
     * @param cb : callback interface instance.
	 * @return true to indicate the operation is successful, or false erroneous.
     */ 
	boolean unregisterHidCallback(INfCallbackHid cb);
	
	/** 
	 * Request to connect HID to the remote device.
	 * <p>This is an asynchronous call: it will return immediately, and clients should register and implement callback function
	 * {@link INfCallbackHid#onHidStateChanged onHidStateChanged} to be notified of subsequent profile state changes.
	 *
	 * @param address valid Bluetooth MAC address.
	 * @return true to indicate the operation is successful, or false erroneous.
	 */ 	
	boolean reqHidConnect(in String address);
	
	/** 
	 * Request to disconnect HID to the remote device.
	 * <p>This is an asynchronous call: it will return immediately, and clients should register and implement callback function
	 * {@link INfCallbackHid#onHidStateChanged onHidStateChanged} to be notified of subsequent profile state changes.
	 * 
	 * @param address : valid Bluetooth MAC address.
	 * @return true to indicate the operation is successful, or false erroneous.
	 */ 	
	boolean reqHidDisconnect(in String address);
	
	
	/** 
	 * Request to send HID mouse command to the remote device.
	 * 
	 * It should be noticed that the function is available after HID connect success.
	 * The return value is an integer means this function has sent data to the remote device. 0 means fail.
	 * @param button : You should put the correct value to this parameter. For exmple : 0x01 means left button. 0x02 means right button.	
	 *				   Please refers to "USB HID Usage Tables, v1.12, page 67". 		   
	 * @param offset_x : You should put x-direction of mouse offset on this parameter. The range should be in (32768 ~ -32768).
	 *				   The parameter is the relative value of last position.
	 * @param offset_y : You should put y-direction of mouse offset on this parameter. The range should be in (32768 ~ -32768).
	 *				   The parameter is the relative value of last position.
	 * @param wheel : You should put wheel information of mouse offset on this parameter. The range should be in (127 ~ -127).
	 *				   The parameter is the relative value of last position.
	 * @return true to indicate the operation is successful, or false erroneous.
	 */ 
	boolean reqSendHidMouseCommand(int button, int offset_x, int offset_y, int wheel);
	
	
	/** 
	 * Request to send virtual key event to the remote device.
	 * 
	 * It should be noticed that the function is available after HID connect success.
	 * The return value is an integer means this function has sent data to the remote device. 0 means fail.
	 * About the key_1 and key_2 value, please refers to "USB HID Usage Tables, v1.12, page 75-102".
	 * @param key_1 : You should put virtual key command on this parameter. For example, 0x223 means home key. 0x224 means back button.
	 * 					The range should be in (1 ~ 652).
	 * @param key_2 : You should put virtual key command on this parameter. For example, 0x223 means home key. 0x224 means back button.
	 * 					The range should be in (1 ~ 652).
	 * @return true to indicate the operation is successful, or false erroneous.
	 */ 
	boolean reqSendHidVirtualKeyCommand(int key_1, int key_2);
	
	
	/** 
	 * Get current connection state of the remote device.
	 *
	 * @return current state of HID profile service.
	 */
	int getHidConnectionState();
	
	/** 
	 * Check if local device is HID connected with remote device.
	 *
	 * @return true to indicate HID is connected, or false disconnected.
	 */
	boolean isHidConnected();
	
	/** 
	 * Get the Bluetooth hardware address of HID connected remote device.
	 *
	 * @return Bluetooth hardware address as string if there is a connected HID device, or 
	 * <code>DEFAULT_ADDRESS</code> 00:00:00:00:00:00.
	 */
	String getHidConnectedAddress();	

}
