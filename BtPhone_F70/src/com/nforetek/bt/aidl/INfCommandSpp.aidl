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

import com.nforetek.bt.aidl.INfCallbackSpp;

/**
 * The API interface is for Serial Port profile (SPP).
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
 * @see INfCallbackSpp
 */
 
 interface INfCommandSpp {

 	/** 
	 * Check if SPP service is ready.
	 */
	boolean isSppServiceReady();

 	/** 
	 * Register callback functions for SPP profile.
	 * Call this function to register callback functions for SPP profile.
	 * Allow nFore service to call back to its registered clients, which might often be UI application.
	 *
	 * @param cb callback interface instance.
	 * @return true to indicate the operation is successful, or false erroneous.
	 */ 
    boolean registerSppCallback(INfCallbackSpp cb);   
    
	/** 
	 * Remove callback functions for SPP profile.
     * Call this function to remove previously registered callback interface for SPP profile.
     * 
     * @param cb callback interface instance.
	 * @return true to indicate the operation is successful, or false erroneous.
     */ 
    boolean unregisterSppCallback(INfCallbackSpp cb);    
    
	/** 
	 * Request to connect SPP to the remote device.
	 *
	 * This is an asynchronous call: it will return immediately, and clients should register and implement callback function
	 * {@link INfCallbackSpp#onSppStateChanged onSppStateChanged} to be notified of subsequent profile state changes.
	 *
	 * @param address valid Bluetooth MAC address.
	 * @return true to indicate the operation is successful, or false erroneous.
	 */
    boolean reqSppConnect(String address);
    
	/** 
	 * Request to disconnect the connected SPP connection to the remote device.
	 *
	 * This is an asynchronous call: it will return immediately, and clients should register and implement callback function
	 * {@link INfCallbackSpp#onSppStateChanged onSppStateChanged} to be notified of subsequent profile state changes.
	 *
	 * @param address valid Bluetooth MAC address.
	 * @return true to indicate the operation is successful, or false erroneous.
	 */ 
    boolean reqSppDisconnect(String address);
    
	/** 
	 * Request for the hardware Bluetooth address of the remote SPP devices.
	 * For example, "00:11:22:AA:BB:CC".
	 * This is an asynchronous call: it will return immediately, and clients should register and implement callback function
	 * {@link INfCallbackSpp#retSppConnectedDeviceAddressList retSppConnectedDeviceAddressList} to be notified of subsequent result.
	 */
    void reqSppConnectedDeviceAddressList();    
    
	/** 
	 * Check if local device is SPP connected with the remote device. 	 
	 *
	 * @param address valid Bluetooth MAC address.	 
	 * @return true if device with given address is currently connected.
	 */ 
    boolean isSppConnected(String address);    
    
	/** 
	 * Request to send given data to the remote SPP device.
	 * 
	 * Data size should not be greater than 512 bytes each time. 
	 *
	 * @param address valid Bluetooth MAC address of connected device.
	 * @param sppData the data to be sent.
	 */
    void reqSppSendData(String address, in byte[] sppData);
    

}