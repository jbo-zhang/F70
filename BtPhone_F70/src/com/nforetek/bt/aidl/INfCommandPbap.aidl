/** 
 * nForeTek A2DP Commands Interface for Android 4.3
 *
 * Copyright (C) 2011-2019  nForeTek Corporation
 *
 * Zeus 0.0.0 on 20140613
 * @author KC Huang	<kchuang@nforetek.com>
 * @author Piggy	<piggylee@nforetek.com>
 * @version 0.0.0
 * 
 */

package com.nforetek.bt.aidl;

import com.nforetek.bt.aidl.INfCallbackPbap;

/**
 * The API interface is for Phone Book Access Profile (PBAP).
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
 * @see INfCallbackPbap
 */
 
 interface INfCommandPbap {

 	/** 
	 * Check if PBAP service is ready.
	 */
	boolean isPbapServiceReady();

	/** 
	 * Register callback functions for PBAP.
	 * <br>Call this function to register callback functions for PBAP.
	 * <br>Allow nFore service to call back to its registered clients, which is usually the UI application.
	 *
	 * @param cb callback interface instance.
	 * @return true to indicate the operation is successful, or false erroneous.
	 */
	boolean registerPbapCallback(INfCallbackPbap cb);
	
	/** 
	 * Remove callback functions from PBAP.
     * <br>Call this function to remove previously registered callback interface for PBAP.
     * 
     * @param cb callback interface instance.
	 * @return true to indicate the operation is successful, or false erroneous.
     */
	boolean unregisterPbapCallback(INfCallbackPbap cb);

	/** 
	 * Get current connection state of the remote device.
	 *
	 * @return current state of PBAP profile service.
	 */
	int getPbapConnectionState();

    /** 
	 * Check if local device is downloading phonebook from remote device.
	 *
	 * @return true to indicate PBAP is downloading, or false disconnected.
	 */
    boolean isPbapDownloading();

	/** 
	 * Get the Bluetooth hardware address of PBAP downloading remote device.
	 *
	 * @return Bluetooth hardware address as string if there is a downloading PBAP device, 
	 * or otherwise <code>DEFAULT_ADDRESS</code> 00:00:00:00:00:00.
	 */
	String getPbapDownloadingAddress();

	/**
	 * Request to download phonebook with vCard from remote device and by pass callback to user.
	 * 
	 * <p>This is an asynchronous call: it will return immediately, and clients should register and implement callback functions
	 * {@link INfCallbackPbap#retPbapDownloadedContact retPbapDownloadedContact} 
	 * and {@link INfCallbackPbap#retPbapDownloadedCallLog retPbapDownloadedCallLog}
	 * and {@link INfCallbackPbap#onPbapStateChanged onPbapStateChanged} to be notified of subsequent result.
	 *
	 * @param address valid Bluetooth MAC address.
	 * @param storage possible storage type are:
	 *		<blockquote><b>PBAP_STORAGE_SIM</b>			(int) 1
	 *		<br><b>PBAP_STORAGE_PHONE_MEMORY</b>		(int) 2
	 *		<br><b>PBAP_STORAGE_SPEEDDIAL</b>			(int) 3
	 *		<br><b>PBAP_STORAGE_FAVORITE</b>			(int) 4
	 *		<br><b>PBAP_STORAGE_MISSED_CALLS</b>		(int) 5
	 *		<br><b>PBAP_STORAGE_RECEIVED_CALLS</b>		(int) 6
	 *		<br><b>PBAP_STORAGE_DIALED_CALLS</b>		(int) 7
	 *		<br><b>PBAP_STORAGE_CALL_LOGS</b>			(int) 8</blockquote>
	 * @param property download property mask. Possible storage type are:
	 *		<blockquote><b>PBAP_PROPERTY_MASK_VERSION</b>	(int) (1&lt;&lt;0)
	 *		<br><b>PBAP_PROPERTY_MASK_FN</b>		(int) (1&lt;&lt;1)
	 *		<br><b>PBAP_PROPERTY_MASK_N</b>			(int) (1&lt;&lt;2)
	 *		<br><b>PBAP_PROPERTY_MASK_PHOTO</b>		(int) (1&lt;&lt;3)
	 *		<br><b>PBAP_PROPERTY_MASK_BDAY</b>		(int) (1&lt;&lt;4)
	 *		<br><b>PBAP_PROPERTY_MASK_ADR</b>		(int) (1&lt;&lt;5)
	 *		<br><b>PBAP_PROPERTY_MASK_LABEL</b>		(int) (1&lt;&lt;6)
	 *		<br><b>PBAP_PROPERTY_MASK_TEL</b>		(int) (1&lt;&lt;7)
	 *		<br><b>PBAP_PROPERTY_MASK_EMAIL</b>		(int) (1&lt;&lt;8)
	 *		<br><b>PBAP_PROPERTY_MASK_MAILER</b>	(int) (1&lt;&lt;9)
	 *		<br><b>PBAP_PROPERTY_MASK_TZ</b>		(int) (1&lt;&lt;10)
	 *		<br><b>PBAP_PROPERTY_MASK_GEO</b>		(int) (1&lt;&lt;11)
	 *		<br><b>PBAP_PROPERTY_MASK_TITLE</b>		(int) (1&lt;&lt;12)
	 *		<br><b>PBAP_PROPERTY_MASK_ROLE</b>		(int) (1&lt;&lt;13)
	 *		<br><b>PBAP_PROPERTY_MASK_LOGO</b>		(int) (1&lt;&lt;14)
	 *		<br><b>PBAP_PROPERTY_MASK_AGENT</b>		(int) (1&lt;&lt;15)
	 *		<br><b>PBAP_PROPERTY_MASK_ORG</b>		(int) (1&lt;&lt;16)
	 *		<br><b>PBAP_PROPERTY_MASK_NOTE</b>		(int) (1&lt;&lt;17)
	 *		<br><b>PBAP_PROPERTY_MASK_REV</b>		(int) (1&lt;&lt;18)
	 *		<br><b>PBAP_PROPERTY_MASK_SOUND</b>		(int) (1&lt;&lt;19)
	 *		<br><b>PBAP_PROPERTY_MASK_URL</b>		(int) (1&lt;&lt;20)
	 *		<br><b>PBAP_PROPERTY_MASK_UID</b>		(int) (1&lt;&lt;21)
	 *		<br><b>PBAP_PROPERTY_MASK_KEY</b>		(int) (1&lt;&lt;22)
	 *		<br><b>PBAP_PROPERTY_MASK_NICKNAME</b>	(int) (1&lt;&lt;23)
	 *		<br><b>PBAP_PROPERTY_MASK_CATEGORIES</b>(int) (1&lt;&lt;24)
	 *		<br><b>PBAP_PROPERTY_MASK_PROID</b>		(int) (1&lt;&lt;25)
	 *		<br><b>PBAP_PROPERTY_MASK_CLASS</b>		(int) (1&lt;&lt;26)
	 *		<br><b>PBAP_PROPERTY_MASK_SORT_STRING</b>(int) (1&lt;&lt;27)
	 *		<br><b>PBAP_PROPERTY_MASK_TIME_STAMP</b>(int) (1&lt;&lt;28)</blockquote>
	 * @return true to indicate the operation is successful, or false erroneous.
	 */
    boolean reqPbapDownload(String address, int storage, int property);

	/**
	 * Request to download phonebook with vCard from remote device and by pass callback to user.
	 * 
	 * <p>This is an asynchronous call: it will return immediately, and clients should register and implement callback functions
	 * {@link INfCallbackPbap#retPbapDownloadedContact retPbapDownloadedContact} 
	 * and {@link INfCallbackPbap#retPbapDownloadedCallLog retPbapDownloadedCallLog}
	 * and {@link INfCallbackPbap#onPbapStateChanged onPbapStateChanged} to be notified of subsequent result.
	 *
	 * @param address valid Bluetooth MAC address.
	 * @param storage possible storage type are:
	 *		<blockquote><b>PBAP_STORAGE_SIM</b>			(int) 1
	 *		<br><b>PBAP_STORAGE_PHONE_MEMORY</b>		(int) 2
	 *		<br><b>PBAP_STORAGE_SPEEDDIAL</b>			(int) 3
	 *		<br><b>PBAP_STORAGE_FAVORITE</b>			(int) 4
	 *		<br><b>PBAP_STORAGE_MISSED_CALLS</b>		(int) 5
	 *		<br><b>PBAP_STORAGE_RECEIVED_CALLS</b>		(int) 6
	 *		<br><b>PBAP_STORAGE_DIALED_CALLS</b>		(int) 7
	 *		<br><b>PBAP_STORAGE_CALL_LOGS</b>			(int) 8</blockquote>
	 * @param property download property mask. Possible storage type are:
	 *		<blockquote><b>PBAP_PROPERTY_MASK_VERSION</b>	(int) (1&lt;&lt;0)
	 *		<br><b>PBAP_PROPERTY_MASK_FN</b>		(int) (1&lt;&lt;1)
	 *		<br><b>PBAP_PROPERTY_MASK_N</b>			(int) (1&lt;&lt;2)
	 *		<br><b>PBAP_PROPERTY_MASK_PHOTO</b>		(int) (1&lt;&lt;3)
	 *		<br><b>PBAP_PROPERTY_MASK_BDAY</b>		(int) (1&lt;&lt;4)
	 *		<br><b>PBAP_PROPERTY_MASK_ADR</b>		(int) (1&lt;&lt;5)
	 *		<br><b>PBAP_PROPERTY_MASK_LABEL</b>		(int) (1&lt;&lt;6)
	 *		<br><b>PBAP_PROPERTY_MASK_TEL</b>		(int) (1&lt;&lt;7)
	 *		<br><b>PBAP_PROPERTY_MASK_EMAIL</b>		(int) (1&lt;&lt;8)
	 *		<br><b>PBAP_PROPERTY_MASK_MAILER</b>	(int) (1&lt;&lt;9)
	 *		<br><b>PBAP_PROPERTY_MASK_TZ</b>		(int) (1&lt;&lt;10)
	 *		<br><b>PBAP_PROPERTY_MASK_GEO</b>		(int) (1&lt;&lt;11)
	 *		<br><b>PBAP_PROPERTY_MASK_TITLE</b>		(int) (1&lt;&lt;12)
	 *		<br><b>PBAP_PROPERTY_MASK_ROLE</b>		(int) (1&lt;&lt;13)
	 *		<br><b>PBAP_PROPERTY_MASK_LOGO</b>		(int) (1&lt;&lt;14)
	 *		<br><b>PBAP_PROPERTY_MASK_AGENT</b>		(int) (1&lt;&lt;15)
	 *		<br><b>PBAP_PROPERTY_MASK_ORG</b>		(int) (1&lt;&lt;16)
	 *		<br><b>PBAP_PROPERTY_MASK_NOTE</b>		(int) (1&lt;&lt;17)
	 *		<br><b>PBAP_PROPERTY_MASK_REV</b>		(int) (1&lt;&lt;18)
	 *		<br><b>PBAP_PROPERTY_MASK_SOUND</b>		(int) (1&lt;&lt;19)
	 *		<br><b>PBAP_PROPERTY_MASK_URL</b>		(int) (1&lt;&lt;20)
	 *		<br><b>PBAP_PROPERTY_MASK_UID</b>		(int) (1&lt;&lt;21)
	 *		<br><b>PBAP_PROPERTY_MASK_KEY</b>		(int) (1&lt;&lt;22)
	 *		<br><b>PBAP_PROPERTY_MASK_NICKNAME</b>	(int) (1&lt;&lt;23)
	 *		<br><b>PBAP_PROPERTY_MASK_CATEGORIES</b>(int) (1&lt;&lt;24)
	 *		<br><b>PBAP_PROPERTY_MASK_PROID</b>		(int) (1&lt;&lt;25)
	 *		<br><b>PBAP_PROPERTY_MASK_CLASS</b>		(int) (1&lt;&lt;26)
	 *		<br><b>PBAP_PROPERTY_MASK_SORT_STRING</b>(int) (1&lt;&lt;27)
	 *		<br><b>PBAP_PROPERTY_MASK_TIME_STAMP</b>(int) (1&lt;&lt;28)</blockquote>
	 * @param startPos download start position.
	 * @param offset download offset.
	 * @return true to indicate the operation is successful, or false erroneous.
	 */
    boolean reqPbapDownloadRange(String address, int storage, int property, int startPos, int offset);

	/**
	 * Request to download phonebook with vCard from remote device and save to local database.
	 * 
	 * <p>This is an asynchronous call: it will return immediately, and clients should register and implement callback functions
	 * {@link INfCallbackPbap#onPbapStateChanged onPbapStateChanged} to be notified of subsequent result.
	 *
	 * @param address valid Bluetooth MAC address.
	 * @param storage possible storage type are:
	 *		<blockquote><b>PBAP_STORAGE_SIM</b>			(int) 1
	 *		<br><b>PBAP_STORAGE_PHONE_MEMORY</b>		(int) 2
	 *		<br><b>PBAP_STORAGE_SPEEDDIAL</b>			(int) 3
	 *		<br><b>PBAP_STORAGE_FAVORITE</b>			(int) 4
	 *		<br><b>PBAP_STORAGE_MISSED_CALLS</b>		(int) 5
	 *		<br><b>PBAP_STORAGE_RECEIVED_CALLS</b>		(int) 6
	 *		<br><b>PBAP_STORAGE_DIALED_CALLS</b>		(int) 7
	 *		<br><b>PBAP_STORAGE_CALL_LOGS</b>			(int) 8</blockquote>	
	 * @param property download property mask. Possible storage type are:
	 *		<blockquote><b>PBAP_PROPERTY_MASK_VERSION</b>	(int) (1&lt;&lt;0)
	 *		<br><b>PBAP_PROPERTY_MASK_FN</b>		(int) (1&lt;&lt;1)
	 *		<br><b>PBAP_PROPERTY_MASK_N</b>			(int) (1&lt;&lt;2)
	 *		<br><b>PBAP_PROPERTY_MASK_PHOTO</b>		(int) (1&lt;&lt;3)
	 *		<br><b>PBAP_PROPERTY_MASK_BDAY</b>		(int) (1&lt;&lt;4)
	 *		<br><b>PBAP_PROPERTY_MASK_ADR</b>		(int) (1&lt;&lt;5)
	 *		<br><b>PBAP_PROPERTY_MASK_LABEL</b>		(int) (1&lt;&lt;6)
	 *		<br><b>PBAP_PROPERTY_MASK_TEL</b>		(int) (1&lt;&lt;7)
	 *		<br><b>PBAP_PROPERTY_MASK_EMAIL</b>		(int) (1&lt;&lt;8)
	 *		<br><b>PBAP_PROPERTY_MASK_MAILER</b>	(int) (1&lt;&lt;9)
	 *		<br><b>PBAP_PROPERTY_MASK_TZ</b>		(int) (1&lt;&lt;10)
	 *		<br><b>PBAP_PROPERTY_MASK_GEO</b>		(int) (1&lt;&lt;11)
	 *		<br><b>PBAP_PROPERTY_MASK_TITLE</b>		(int) (1&lt;&lt;12)
	 *		<br><b>PBAP_PROPERTY_MASK_ROLE</b>		(int) (1&lt;&lt;13)
	 *		<br><b>PBAP_PROPERTY_MASK_LOGO</b>		(int) (1&lt;&lt;14)
	 *		<br><b>PBAP_PROPERTY_MASK_AGENT</b>		(int) (1&lt;&lt;15)
	 *		<br><b>PBAP_PROPERTY_MASK_ORG</b>		(int) (1&lt;&lt;16)
	 *		<br><b>PBAP_PROPERTY_MASK_NOTE</b>		(int) (1&lt;&lt;17)
	 *		<br><b>PBAP_PROPERTY_MASK_REV</b>		(int) (1&lt;&lt;18)
	 *		<br><b>PBAP_PROPERTY_MASK_SOUND</b>		(int) (1&lt;&lt;19)
	 *		<br><b>PBAP_PROPERTY_MASK_URL</b>		(int) (1&lt;&lt;20)
	 *		<br><b>PBAP_PROPERTY_MASK_UID</b>		(int) (1&lt;&lt;21)
	 *		<br><b>PBAP_PROPERTY_MASK_KEY</b>		(int) (1&lt;&lt;22)
	 *		<br><b>PBAP_PROPERTY_MASK_NICKNAME</b>	(int) (1&lt;&lt;23)
	 *		<br><b>PBAP_PROPERTY_MASK_CATEGORIES</b>(int) (1&lt;&lt;24)
	 *		<br><b>PBAP_PROPERTY_MASK_PROID</b>		(int) (1&lt;&lt;25)
	 *		<br><b>PBAP_PROPERTY_MASK_CLASS</b>		(int) (1&lt;&lt;26)
	 *		<br><b>PBAP_PROPERTY_MASK_SORT_STRING</b>(int) (1&lt;&lt;27)
	 *		<br><b>PBAP_PROPERTY_MASK_TIME_STAMP</b>(int) (1&lt;&lt;28)</blockquote>
	 * @return true to indicate the operation is successful, or false erroneous.
	 */
    boolean reqPbapDownloadToDatabase(String address, int storage, int property);

	/**
	 * Request to download phonebook with vCard from remote device and save to local Contacts Provider.
	 * 
	 * <p>This is an asynchronous call: it will return immediately, and clients should register and implement callback functions
	 * {@link INfCallbackPbap#onPbapStateChanged onPbapStateChanged} to be notified of subsequent result.
	 *
	 * @param address valid Bluetooth MAC address.
	 * @param storage possible storage type are:
	 *		<blockquote><b>PBAP_STORAGE_SIM</b>			(int) 1
	 *		<br><b>PBAP_STORAGE_PHONE_MEMORY</b>		(int) 2
	 *		<br><b>PBAP_STORAGE_SPEEDDIAL</b>			(int) 3
	 *		<br><b>PBAP_STORAGE_FAVORITE</b>			(int) 4
	 *		<br><b>PBAP_STORAGE_MISSED_CALLS</b>		(int) 5
	 *		<br><b>PBAP_STORAGE_RECEIVED_CALLS</b>		(int) 6
	 *		<br><b>PBAP_STORAGE_DIALED_CALLS</b>		(int) 7
	 *		<br><b>PBAP_STORAGE_CALL_LOGS</b>			(int) 8</blockquote>
	 * @param property download property mask. Possible storage type are:
	 *		<blockquote><b>PBAP_PROPERTY_MASK_VERSION</b>	(int) (1&lt;&lt;0)
	 *		<br><b>PBAP_PROPERTY_MASK_FN</b>		(int) (1&lt;&lt;1)
	 *		<br><b>PBAP_PROPERTY_MASK_N</b>			(int) (1&lt;&lt;2)
	 *		<br><b>PBAP_PROPERTY_MASK_PHOTO</b>		(int) (1&lt;&lt;3)
	 *		<br><b>PBAP_PROPERTY_MASK_BDAY</b>		(int) (1&lt;&lt;4)
	 *		<br><b>PBAP_PROPERTY_MASK_ADR</b>		(int) (1&lt;&lt;5)
	 *		<br><b>PBAP_PROPERTY_MASK_LABEL</b>		(int) (1&lt;&lt;6)
	 *		<br><b>PBAP_PROPERTY_MASK_TEL</b>		(int) (1&lt;&lt;7)
	 *		<br><b>PBAP_PROPERTY_MASK_EMAIL</b>		(int) (1&lt;&lt;8)
	 *		<br><b>PBAP_PROPERTY_MASK_MAILER</b>	(int) (1&lt;&lt;9)
	 *		<br><b>PBAP_PROPERTY_MASK_TZ</b>		(int) (1&lt;&lt;10)
	 *		<br><b>PBAP_PROPERTY_MASK_GEO</b>		(int) (1&lt;&lt;11)
	 *		<br><b>PBAP_PROPERTY_MASK_TITLE</b>		(int) (1&lt;&lt;12)
	 *		<br><b>PBAP_PROPERTY_MASK_ROLE</b>		(int) (1&lt;&lt;13)
	 *		<br><b>PBAP_PROPERTY_MASK_LOGO</b>		(int) (1&lt;&lt;14)
	 *		<br><b>PBAP_PROPERTY_MASK_AGENT</b>		(int) (1&lt;&lt;15)
	 *		<br><b>PBAP_PROPERTY_MASK_ORG</b>		(int) (1&lt;&lt;16)
	 *		<br><b>PBAP_PROPERTY_MASK_NOTE</b>		(int) (1&lt;&lt;17)
	 *		<br><b>PBAP_PROPERTY_MASK_REV</b>		(int) (1&lt;&lt;18)
	 *		<br><b>PBAP_PROPERTY_MASK_SOUND</b>		(int) (1&lt;&lt;19)
	 *		<br><b>PBAP_PROPERTY_MASK_URL</b>		(int) (1&lt;&lt;20)
	 *		<br><b>PBAP_PROPERTY_MASK_UID</b>		(int) (1&lt;&lt;21)
	 *		<br><b>PBAP_PROPERTY_MASK_KEY</b>		(int) (1&lt;&lt;22)
	 *		<br><b>PBAP_PROPERTY_MASK_NICKNAME</b>	(int) (1&lt;&lt;23)
	 *		<br><b>PBAP_PROPERTY_MASK_CATEGORIES</b>(int) (1&lt;&lt;24)
	 *		<br><b>PBAP_PROPERTY_MASK_PROID</b>		(int) (1&lt;&lt;25)
	 *		<br><b>PBAP_PROPERTY_MASK_CLASS</b>		(int) (1&lt;&lt;26)
	 *		<br><b>PBAP_PROPERTY_MASK_SORT_STRING</b>(int) (1&lt;&lt;27)
	 *		<br><b>PBAP_PROPERTY_MASK_TIME_STAMP</b>(int) (1&lt;&lt;28)</blockquote>
	 * @param startPos download start position.
	 * @param offset download offset.
	 * @return true to indicate the operation is successful, or false erroneous.
	 */
    boolean reqPbapDownloadRangeToDatabase(String address, int storage, int property, int startPos, int offset);

	/**
	 * Request to download phonebook with vCard from remote device and save to local Contacts Provider.
	 * 
	 * <p>This is an asynchronous call: it will return immediately, and clients should register and implement callback functions
	 * {@link INfCallbackPbap#onPbapStateChanged onPbapStateChanged} to be notified of subsequent result.
	 *
	 * @param address valid Bluetooth MAC address.
	 * @param storage possible storage type are:
	 *		<blockquote><b>PBAP_STORAGE_SIM</b>			(int) 1
	 *		<br><b>PBAP_STORAGE_PHONE_MEMORY</b>		(int) 2
	 *		<br><b>PBAP_STORAGE_SPEEDDIAL</b>			(int) 3
	 *		<br><b>PBAP_STORAGE_FAVORITE</b>			(int) 4
	 *		<br><b>PBAP_STORAGE_MISSED_CALLS</b>		(int) 5
	 *		<br><b>PBAP_STORAGE_RECEIVED_CALLS</b>		(int) 6
	 *		<br><b>PBAP_STORAGE_DIALED_CALLS</b>		(int) 7
	 *		<br><b>PBAP_STORAGE_CALL_LOGS</b>			(int) 8</blockquote>
	 * @param property download property mask. Possible storage type are:
	 *		<blockquote><b>PBAP_PROPERTY_MASK_VERSION</b>	(int) (1&lt;&lt;0)
	 *		<br><b>PBAP_PROPERTY_MASK_FN</b>		(int) (1&lt;&lt;1)
	 *		<br><b>PBAP_PROPERTY_MASK_N</b>			(int) (1&lt;&lt;2)
	 *		<br><b>PBAP_PROPERTY_MASK_PHOTO</b>		(int) (1&lt;&lt;3)
	 *		<br><b>PBAP_PROPERTY_MASK_BDAY</b>		(int) (1&lt;&lt;4)
	 *		<br><b>PBAP_PROPERTY_MASK_ADR</b>		(int) (1&lt;&lt;5)
	 *		<br><b>PBAP_PROPERTY_MASK_LABEL</b>		(int) (1&lt;&lt;6)
	 *		<br><b>PBAP_PROPERTY_MASK_TEL</b>		(int) (1&lt;&lt;7)
	 *		<br><b>PBAP_PROPERTY_MASK_EMAIL</b>		(int) (1&lt;&lt;8)
	 *		<br><b>PBAP_PROPERTY_MASK_MAILER</b>	(int) (1&lt;&lt;9)
	 *		<br><b>PBAP_PROPERTY_MASK_TZ</b>		(int) (1&lt;&lt;10)
	 *		<br><b>PBAP_PROPERTY_MASK_GEO</b>		(int) (1&lt;&lt;11)
	 *		<br><b>PBAP_PROPERTY_MASK_TITLE</b>		(int) (1&lt;&lt;12)
	 *		<br><b>PBAP_PROPERTY_MASK_ROLE</b>		(int) (1&lt;&lt;13)
	 *		<br><b>PBAP_PROPERTY_MASK_LOGO</b>		(int) (1&lt;&lt;14)
	 *		<br><b>PBAP_PROPERTY_MASK_AGENT</b>		(int) (1&lt;&lt;15)
	 *		<br><b>PBAP_PROPERTY_MASK_ORG</b>		(int) (1&lt;&lt;16)
	 *		<br><b>PBAP_PROPERTY_MASK_NOTE</b>		(int) (1&lt;&lt;17)
	 *		<br><b>PBAP_PROPERTY_MASK_REV</b>		(int) (1&lt;&lt;18)
	 *		<br><b>PBAP_PROPERTY_MASK_SOUND</b>		(int) (1&lt;&lt;19)
	 *		<br><b>PBAP_PROPERTY_MASK_URL</b>		(int) (1&lt;&lt;20)
	 *		<br><b>PBAP_PROPERTY_MASK_UID</b>		(int) (1&lt;&lt;21)
	 *		<br><b>PBAP_PROPERTY_MASK_KEY</b>		(int) (1&lt;&lt;22)
	 *		<br><b>PBAP_PROPERTY_MASK_NICKNAME</b>	(int) (1&lt;&lt;23)
	 *		<br><b>PBAP_PROPERTY_MASK_CATEGORIES</b>(int) (1&lt;&lt;24)
	 *		<br><b>PBAP_PROPERTY_MASK_PROID</b>		(int) (1&lt;&lt;25)
	 *		<br><b>PBAP_PROPERTY_MASK_CLASS</b>		(int) (1&lt;&lt;26)
	 *		<br><b>PBAP_PROPERTY_MASK_SORT_STRING</b>(int) (1&lt;&lt;27)
	 *		<br><b>PBAP_PROPERTY_MASK_TIME_STAMP</b>(int) (1&lt;&lt;28)</blockquote>
	 * @return true to indicate the operation is successful, or false erroneous.
	 */
    boolean reqPbapDownloadToContactsProvider(String address, int storage, int property);

	/**
	 * Request to download phonebook with vCard from remote device and save to local Contacts Provider.
	 * 
	 * <p>This is an asynchronous call: it will return immediately, and clients should register and implement callback functions
	 * {@link INfCallbackPbap#onPbapStateChanged onPbapStateChanged} to be notified of subsequent result.
	 *
	 * @param address valid Bluetooth MAC address.
	 * @param storage possible storage type are:
	 *		<blockquote><b>PBAP_STORAGE_SIM</b>			(int) 1
	 *		<br><b>PBAP_STORAGE_PHONE_MEMORY</b>		(int) 2
	 *		<br><b>PBAP_STORAGE_SPEEDDIAL</b>			(int) 3
	 *		<br><b>PBAP_STORAGE_FAVORITE</b>			(int) 4
	 *		<br><b>PBAP_STORAGE_MISSED_CALLS</b>		(int) 5
	 *		<br><b>PBAP_STORAGE_RECEIVED_CALLS</b>		(int) 6
	 *		<br><b>PBAP_STORAGE_DIALED_CALLS</b>		(int) 7
	 *		<br><b>PBAP_STORAGE_CALL_LOGS</b>			(int) 8</blockquote>
	 * @param property download property mask. Possible storage type are:
	 *		<blockquote><b>PBAP_PROPERTY_MASK_VERSION</b>	(int) (1&lt;&lt;0)
	 *		<br><b>PBAP_PROPERTY_MASK_FN</b>		(int) (1&lt;&lt;1)
	 *		<br><b>PBAP_PROPERTY_MASK_N</b>			(int) (1&lt;&lt;2)
	 *		<br><b>PBAP_PROPERTY_MASK_PHOTO</b>		(int) (1&lt;&lt;3)
	 *		<br><b>PBAP_PROPERTY_MASK_BDAY</b>		(int) (1&lt;&lt;4)
	 *		<br><b>PBAP_PROPERTY_MASK_ADR</b>		(int) (1&lt;&lt;5)
	 *		<br><b>PBAP_PROPERTY_MASK_LABEL</b>		(int) (1&lt;&lt;6)
	 *		<br><b>PBAP_PROPERTY_MASK_TEL</b>		(int) (1&lt;&lt;7)
	 *		<br><b>PBAP_PROPERTY_MASK_EMAIL</b>		(int) (1&lt;&lt;8)
	 *		<br><b>PBAP_PROPERTY_MASK_MAILER</b>	(int) (1&lt;&lt;9)
	 *		<br><b>PBAP_PROPERTY_MASK_TZ</b>		(int) (1&lt;&lt;10)
	 *		<br><b>PBAP_PROPERTY_MASK_GEO</b>		(int) (1&lt;&lt;11)
	 *		<br><b>PBAP_PROPERTY_MASK_TITLE</b>		(int) (1&lt;&lt;12)
	 *		<br><b>PBAP_PROPERTY_MASK_ROLE</b>		(int) (1&lt;&lt;13)
	 *		<br><b>PBAP_PROPERTY_MASK_LOGO</b>		(int) (1&lt;&lt;14)
	 *		<br><b>PBAP_PROPERTY_MASK_AGENT</b>		(int) (1&lt;&lt;15)
	 *		<br><b>PBAP_PROPERTY_MASK_ORG</b>		(int) (1&lt;&lt;16)
	 *		<br><b>PBAP_PROPERTY_MASK_NOTE</b>		(int) (1&lt;&lt;17)
	 *		<br><b>PBAP_PROPERTY_MASK_REV</b>		(int) (1&lt;&lt;18)
	 *		<br><b>PBAP_PROPERTY_MASK_SOUND</b>		(int) (1&lt;&lt;19)
	 *		<br><b>PBAP_PROPERTY_MASK_URL</b>		(int) (1&lt;&lt;20)
	 *		<br><b>PBAP_PROPERTY_MASK_UID</b>		(int) (1&lt;&lt;21)
	 *		<br><b>PBAP_PROPERTY_MASK_KEY</b>		(int) (1&lt;&lt;22)
	 *		<br><b>PBAP_PROPERTY_MASK_NICKNAME</b>	(int) (1&lt;&lt;23)
	 *		<br><b>PBAP_PROPERTY_MASK_CATEGORIES</b>(int) (1&lt;&lt;24)
	 *		<br><b>PBAP_PROPERTY_MASK_PROID</b>		(int) (1&lt;&lt;25)
	 *		<br><b>PBAP_PROPERTY_MASK_CLASS</b>		(int) (1&lt;&lt;26)
	 *		<br><b>PBAP_PROPERTY_MASK_SORT_STRING</b>(int) (1&lt;&lt;27)
	 *		<br><b>PBAP_PROPERTY_MASK_TIME_STAMP</b>(int) (1&lt;&lt;28)</blockquote>
	 * @param startPos download start position.
	 * @param offset download offset.
	 * @return true to indicate the operation is successful, or false erroneous.
	 */
    boolean reqPbapDownloadRangeToContactsProvider(String address, int storage, int property, int startPos, int offset);

    /**
	 * Request to query the corresponding name by a given phone number from database.
	 *
	 * <p>This is an asynchronous call: it will return immediately, and clients should register and implement callback function
	 * {@link INfCallbackPbap#retPbapDatabaseQueryNameByNumber retPbapDatabaseQueryNameByNumber} to be notified of subsequent result.
	 *
	 * @param address valid Bluetooth MAC address.
     * @param target the phone number given to database query.
     */
	void reqPbapDatabaseQueryNameByNumber(String address, String target);

	/**
	 * Request to querythe corresponding name by a given partial phone number from database.
	 *
	 * <p>This is an asynchronous call: it will return immediately, and clients should register and implement callback function
	 * {@link INfCallbackPbap#retPbapDatabaseQueryNameByPartialNumber retPbapDatabaseQueryNameByPartialNumber} to be notified of subsequent result.
	 *
	 * @param address valid Bluetooth MAC address.
     * @param target the phone number given to database query like number.
     * @param findPosition decide which target position you want to find. Possible value are:
     *		<br>For example: target is 123.
     *		<blockquote><b>SQL_QUERY_FIND_CONTAIN</b>		(int) 0 ex. XXX<b>123</b>XXX
	 *		<br><b>SQL_QUERY_FIND_HEAD</b>					(int) 1 ex. <b>123</b>XXXXXX
	 *		<br><b>SQL_QUERY_FIND_TAIL</b>					(int) 2 ex. XXXXXX<b>123</b></blockquote>
     */
	void reqPbapDatabaseQueryNameByPartialNumber(String address, String target, int findPosition);
	
    /**
	 * Request to check if nFore's PBAP database is available for query.
	 *
	 * <p>This is an asynchronous call: it will return immediately, and clients should register and implement callback function
	 * {@link INfCallbackPbap#retPbapDatabaseAvailable retPbapDatabaseAvailable} to be notified when database is available.
	 *
	 * <p>When nFore's PBAP database is required to access, this command has to be issued in advanced and wait for 
	 * the callback {@link INfCallbackPbap#retPbapDatabaseAvailable retPbapDatabaseAvailable}. Or the database may be crashed!
	 * <br>After nFore's PBAP database is done accessing, the database resource needs to be released. And should <b>never</b> use commands
	 * {@link INfCommandPbap#reqPbapDownloadToDatabase reqPbapDownloadToDatabase}, 
	 * {@link INfCommandPbap#reqPbapDownloadToContactsProvider reqPbapDownloadToContactsProvider},
	 * {@link INfCommandPbap#reqPbapDatabaseQueryNameByNumber reqPbapDatabaseQueryNameByNumber} or 
	 * {@link INfCommandPbap#reqPbapDatabaseQueryNameByPartialNumber reqPbapDatabaseQueryNameByPartialNumber} 
	 * before the database resource is released. 
	 * 
	 * @param address valid Bluetooth MAC address.
	 */
    void reqPbapDatabaseAvailable(String address);

	/**
	 * Request to delete phonebook data of specific address from database.
	 * 
	 * <p>This is an asynchronous call: it will return immediately, and clients should register and implement callback function
	 * {@link INfCallbackPbap#retPbapDeleteDatabaseByAddressCompleted retPbapDeleteDatabaseByAddressCompleted} to be notified when database has been deleted.
	 *
	 * @param address valid Bluetooth MAC address.
	 */
    void reqPbapDeleteDatabaseByAddress(String address);

    /**
	 * Request to clean whole PBAP database.
	 *
	 * <p>This is an asynchronous call: it will return immediately, and clients should register and implement callback function
	 * {@link INfCallbackPbap#retPbapCleanDatabaseCompleted retPbapCleanDatabaseCompleted} to be notified when database has been cleaned.
	 */
    void reqPbapCleanDatabase();

    /**
	 * Request to interrupt the ongoing download from remote device.
	 *
	 * <br>Clients should register and implement callback function {@link INfCallbackPbap#onPbapStateChanged onPbapStateChanged} to be notified of subsequent result.    
	 *
	 * @param address valid Bluetooth MAC address.
	 * @return true if really try to interrupt.
	 */
    boolean reqPbapDownloadInterrupt(String address);

    /**
	 * Set PBAP download notify frequency. Will set to default value when ServiceManager restart.
	 * Default value is 0 means don't callback download notify. For example, if frequency is set to 5, every 5 contacts onPbapDownloadNofity will be notified.
	 *
	 * <br>Clients should register and implement callback function {@link INfCallbackPbap#onPbapDownloadNotify onPbapDownloadNotify} to be notified of subsequent result.
	 * Callback will be invoked if below commands are issued     
	 * {@link INfCommandPbap#reqPbapDownload reqPbapDownload}, 
	 * {@link INfCommandPbap#reqPbapDownloadRange reqPbapDownloadRange},
	 * {@link INfCommandPbap#reqPbapDownloadToDatabase reqPbapDownloadToDatabase},
	 * {@link INfCommandPbap#reqPbapDownloadRangeToDatabase reqPbapDownloadRangeToDatabase},
	 * {@link INfCommandPbap#reqPbapDownloadToContactsProvider reqPbapDownloadToContactsProvider} or 
	 * {@link INfCommandPbap#reqPbapDownloadRangeToContactsProvider reqPbapDownloadRangeToContactsProvider} 
	 *
	 * @param frequency define the callback frequency.
     *
	 * @return true to indicate the operation is successful, or false erroneous.
	 */
    boolean setPbapDownloadNotify(int frequency);
}