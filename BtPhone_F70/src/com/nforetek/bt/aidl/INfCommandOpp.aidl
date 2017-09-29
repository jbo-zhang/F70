/** 
 * nForeTek OPP Commands Interface for Android 4.3
 *
 * Copyright (C) 2011-2019  nForeTek Corporation
 *
 * Zeus 0.0.0 on 20150525
 * @author KC Huang	<kchuang@nforetek.com>
 * @author Piggy	<piggylee@nforetek.com>
 * @version 0.0.0
 * 
 */

package com.nforetek.bt.aidl;
import com.nforetek.bt.aidl.INfCallbackOpp;

/**
 * The API interface is for Object Push Profile (OPP).
 * <br>UI program may use these specific APIs to access to nFore service.
 * <br>The naming principle of API in this doc is as follows,
 *		<blockquote><b>setXXX()</b> : 		set attributes to specific functions of nFore service.
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
 * 
 */
 
 interface INfCommandOpp {

 	/** 
	 * Check if Opp service is ready.
	 */
	boolean isOppServiceReady();
	
 	/**
	 * Register callback functions for OPP.
	 * <br>Call this function to register callback functions for OPP.
	 * <br>Allow nFore service to call back to its registered clients, which is usually the UI application.
	 *
	 * @param cb callback interface instance.
	 * @return true to indicate the operation is successful, or false erroneous.
	 */ 
	boolean registerOppCallback(INfCallbackOpp cb);
	
	/** 
	 * Remove callback functions from OPP.
     * <br>Call this function to remove previously registered callback interface for OPP.
     * 
     * @param cb callback interface instance.
	 * @return true to indicate the operation is successful, or false erroneous.
     */ 
	boolean unregisterOppCallback(INfCallbackOpp cb);
	
    /** 
	 * Request to set Opp file storage path.
	 *
	 * @param path is under Environment.getExternalStorageDirectory().
	 * 		<br>For example, if path is set as /nfore, the actual path would be /sdcard/nfore.
	 * 		<br>And path need start with "/", and have to be legal folder name.
	 * 		<br>Default path is under Environment.getExternalStorageDirectory().
	 * 		<br>In general, it's under /sdcard
	 * @return true to indicate the operation is successful, or false erroneous.
	 */ 	
	boolean setOppFilePath(String path);

	/** 
	 * Get Opp file storage path.
	 *
	 * @return Opp file storage path
	 */ 	
	String getOppFilePath();
	
	/** 
	 * Accept to receive file.
     * <br>Call this function to accept file transfer from remote device.
     * 
     * @param accept true or false to accept file transfer.
	 * @return true to indicate the operation is successful, or false erroneous.
     */ 
	boolean reqOppAcceptReceiveFile(boolean accept);
	
	/** 
	 * Accept to receive file.
     * <br>Call this function to accept file transfer from remote device.
     * 
     * @param accept true or false to accept file transfer.
	 * @return true to indicate the operation is successful, or false erroneous.
     */ 
	boolean reqOppInterruptReceiveFile();
	
}