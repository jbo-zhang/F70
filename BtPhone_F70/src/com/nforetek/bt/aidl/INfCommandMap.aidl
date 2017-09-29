/** 
 * nForeTek MAP Commands Interface for Android 4.3
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

import com.nforetek.bt.aidl.INfCallbackMap;

/**
 * The API interface is for Message Access Profile (MAP).
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
 * @see INfCallbackMap
 */
 
 interface INfCommandMap {

 	/** 
	 * Check if MAP service is ready.
	 */
	boolean isMapServiceReady();
	/** 
	 * Register callback functions for MAP.
	 * <br>Call this function to register callback functions for MAP.
	 * <br>Allow nFore service to call back to its registered clients, which is usually the UI application.
	 *
	 * @param cb callback interface instance.
	 * @return true to indicate the operation is successful, or false erroneous.
	 */ 
    boolean registerMapCallback(INfCallbackMap cb);   
    
	/** 
	 * Remove callback functions from MAP.
     * <br>Call this function to remove previously registered callback interface for MAP.
     * 
     * @param cb callback interface instance.
	 * @return true to indicate the operation is successful, or false erroneous.
     */ 
    boolean unregisterMapCallback(INfCallbackMap cb);    
    
	/** 
	 * Request to download all messages from remote device.
	 * <br>This is an asynchronous call: it will return immediately, and clients should register and implement callback functions 
	 * {@link INfCallbackMap#onMapStateChanged onMapStateChanged} to be notified of subsequent profile state changes.
	 * And {@link INfCallbackMap#retMapDownloadedMessage retMapDownloadedMessage} to be notified of downloaded messages.
	 *
	 * @param address valid Bluetooth MAC address.
	 * @param folder which folder message download from. Possible values are
     *		<br>FOLDER_STRUCTURE_INBOX					(int) 0
	 * 		<br>FOLDER_STRUCTURE_SENT					(int) 1
	 * @param isContentDownload
	 * <p><value>=false, download message list only
	 * <p><value>=true, download all messages including the contents, but this will set all messages to "read"
	 * @return true to indicate the operation is successful, or false erroneous.
	 */ 	    
    boolean reqMapDownloadAllMessages(String address, int folder, boolean isContentDownload);    
    
    /**
	 * Request to download single message from remote device.
	 * 
	 * <br>This is an asynchronous call: it will return immediately, and clients should register and implement callback function
	 * {@link INfCallbackMap#onMapStateChanged onMapStateChanged} to be notified of subsequent result.
	 * And {@link INfCallbackMap#retMapDownloadedMessage retMapDownloadedMessage} to be notified of downloaded message.
	 *
	 * @param address valid Bluetooth MAC address.	
	 * @param folder which folder message download from. Possible values are
     *		<br>FOLDER_STRUCTURE_INBOX					(int) 0
	 * 		<br>FOLDER_STRUCTURE_SENT					(int) 1
	 * @param handle MAP handle
	 * @return true to indicate the operation is successful, or false erroneous.
	 */
	boolean reqMapDownloadSingleMessage(String address, int folder, String handle);

	/** 
	 * Request to download all messages from remote device and save to local database.
	 * <br>This is an asynchronous call: it will return immediately, and clients should register and implement callback functions 
	 * {@link INfCallbackMap#onMapStateChanged onMapStateChanged} to be notified of subsequent profile state changes.
	 *
	 * @param address valid Bluetooth MAC address.
	 * @param folder which folder message download from. Possible values are
     *		<br>FOLDER_STRUCTURE_INBOX					(int) 0
	 * 		<br>FOLDER_STRUCTURE_SENT					(int) 1
	 * @param notifyFreq defines the callback frequency.
	 * <p><value>=0 all messages would be downloaded first, and inserted to database. Only one callback would be invocated.
	 * <p><value>>0 Callbacks would be invocated every "notifyFreq" messages have been downloaded and inserted to database. 
	 * @param isContentDownload
	 * <p><value>=false, download message list only
	 * <p><value>=true, download all messages including the contents, but this will set all messages to "read"
	 * @return true to indicate the operation is successful, or false erroneous.
	 */ 	    
    boolean reqMapDownloadAllMessagesToDatabase(String address, int folder, boolean isContentDownload);    
    
    /**
	 * Request to download single message from remote device and save to local database.
	 * 
	 * This is an asynchronous call: it will return immediately, and clients should register and implement callback function
	 * {@link INfCallbackMap#onMapStateChanged onMapStateChanged} to be notified of subsequent result.
	 *
	 * @param address valid Bluetooth MAC address.	
	 * @param folder which folder message download from. Possible values are
     *		<br>FOLDER_STRUCTURE_INBOX					(int) 0
	 * 		<br>FOLDER_STRUCTURE_SENT					(int) 1
	 * @param handle MAP handle
	 * @return true to indicate the operation is successful, or false erroneous.		 
	 */
	boolean reqMapDownloadSingleMessageToDatabase(String address, int folder, String handle);
     
    /**
	 * Request to register notification when there is new message on remote device with given Bluetooth hardware address.
	 * Valid Bluetooth hardware addresses must be in a format such as "00:11:22:33:AA:BB".
	 * <br>This is an asynchronous call: it will return immediately, and clients should register and implement callback functions 
	 * {@link INfCallbackMap#onMapStateChanged onMapStateChanged} to be notified if register notification success.
	 * and implement callback function {@link INfCallbackMap#onMapNewMessageReceivedEvent onMapNewMessageReceivedEvent}
	 * , {@link INfCallbackMap#onMapMemoryAvailableEvent onMapMemoryAvailableEvent}
	 * , {@link INfCallbackMap#onMapMessageSendingStatusEvent onMapMessageSendingStatusEvent}
	 * , {@link INfCallbackMap#onMapMessageDeliverStatusEvent onMapMessageDeliverStatusEvent}
	 * , {@link INfCallbackMap#onMapMessageShiftedEvent onMapMessageShiftedEvent}
	 * , {@link INfCallbackMap#onMapMessageDeletedEvent onMapMessageDeletedEvent}
	 * to be notified of receivced new message.
	 *
	 * @param address valid Bluetooth MAC address.
	 * @param downloadNewMessage if true, download the new message, including sender and message contents; if false, only notification will be sent
	 */
    boolean reqMapRegisterNotification(String address, boolean downloadNewMessage);
    
    /**
	 * Request to unregister new message notification on remote device with given Bluetooth hardware address.
	 * Valid Bluetooth hardware addresses must be in a format such as "00:11:22:33:AA:BB". 
	 *
	 * <br>This is an asynchronous call: it will return immediately, and clients should register and implement callback functions 
	 * {@link INfCallbackMap#onMapStateChanged onMapStateChanged} to be notified if unregister notification success.
	 * @param address valid Bluetooth MAC address.
	 */
    void reqMapUnregisterNotification(String address); 
    
	/** 
	 * Return true if the new message notification is registered on device with given address.
	 *
	 * @param address valid Bluetooth MAC address.	 
	 * @return true if registered.
	 */ 
    boolean isMapNotificationRegistered(String address);

    /**
	 * Request to interrupt the ongoing download on remote device.
	 * 
	 * Clients should register and implement callback function {@link INfCallbackMap#onMapStateChanged onMapStateChanged} to be notified of subsequent result.    
	 *
	 * @param address valid Bluetooth MAC address.
	 * @return true if really try to interrupt.
	 */
    boolean reqMapDownloadInterrupt(String address);

	/**
	 * Request to check if database is available for query
	 * Client should register and implement {@link INfCallbackMap#retMapDatabaseAvailable retMapDatabaseAvailable} 
	 * to be notified when database is available.
	 *
	 * @param address valid Bluetooth MAC address.	 
	 */ 
    void reqMapDatabaseAvailable();

    /**
	 * Request to delete MAP data by specific address
	 * Client should register and implement {@link INfCallbackMap#retMapDeleteDatabaseByAddressCompleted retMapDeleteDatabaseByAddressCompleted} 
	 * to be notified when database is available.
	 *
	 * @param address valid Bluetooth MAC address.	 
	 */
	void reqMapDeleteDatabaseByAddress(String address);

    /**
	 * Request to clean database of MAP.
	 * This is an asynchronous call: it will return immediately, and clients should register and implement callback function
	 * {@link INfCallbackMap#retMapCleanDatabaseCompleted retMapCleanDatabaseCompleted} to be notified when database has been cleaned.	 
	 */ 
	void reqMapCleanDatabase();

	/** 
	 * Request for the current download state of remote connected MAP device with given Bluetooth hardware address.
	 * Valid Bluetooth hardware addresses must be in a format such as "00:11:22:33:AA:BB".	 
	 *
	 * @param address valid Bluetooth MAC address of connected device.	 
	 * @return current state of MAP profile service.
	 */ 
    int getMapCurrentState(String address);

    /** 
	 * Request for the current register state of remote connected MAP device with given Bluetooth hardware address.
	 * Valid Bluetooth hardware addresses must be in a format such as "00:11:22:33:AA:BB".	 
	 *
	 * @param address valid Bluetooth MAC address of connected device.	 
	 * @return current state of MAP profile service.
	 */ 
    int getMapRegisterState(String address);
    
    /**
	 * Request to send message on remote device.
	 * 
	 * Clients should register and implement callback function {@link INfCallbackMap#retMapSendMessageCompleted retMapSendMessageCompleted} to be notified of subsequent result.    
	 *
	 * @param address valid Bluetooth MAC address.
	 * @param message send context.
	 * @param target phone number of target.
	 */
    boolean reqMapSendMessage(String address, String message, String target);
    
    /**
	 * Request to delete message on remote device.
	 * 
	 * Clients should register and implement callback function {@link INfCallbackMap#retMapDeleteMessageCompleted retMapDeleteMessageCompleted} to delete a message in remote device.
	 * Suggest that the message handle should be updated by downloading message listing before deleting a message.
	 *
	 * @param address valid Bluetooth MAC address.
	 * @param folder which folder message download from. Possible values are
     *		<br>FOLDER_STRUCTURE_INBOX					(int) 0
	 * 		<br>FOLDER_STRUCTURE_SENT					(int) 1
	 * @param handle MAP handle
	 */
    boolean reqMapDeleteMessage(String address, int folder, String handle);
    
    /**
	 * Request to change read status of message.
	 * 
	 * Clients should register and implement callback function {@link INfCallbackMap#retMapChangeReadStatusCompleted retMapChangeReadStatusCompleted} to modify a read status in remote device.    
	 *
	 * @param address valid Bluetooth MAC address.
	 * @param folder which folder message download from. Possible values are
     *		<br>FOLDER_STRUCTURE_INBOX					(int) 0
	 * 		<br>FOLDER_STRUCTURE_SENT					(int) 1
	 * @param handle MAP handle
	 * @param isReadStatus that "true" (=read) or "false" (=unread) for the "readStatus" indicator
	 */
    boolean reqMapChangeReadStatus(String address, int folder, String handle, boolean isReadStatus);

    /**
	 * Set MAP download notify frequency. Will set to default value when ServiceManager restart.
	 * Default value is 0 means don't callback download notify. For example, if frequency is set to 5, every 5 contacts onPbapDownloadNofity will be notified.
	 *
	 * <br>Clients should register and implement callback function {@link INfCallbackMap#onMapDownloadNotify onMapDownloadNotify} to be notified of subsequent result.
	 * Callback will be invoked if below commands are issued     
	 * {@link INfCommandMap#reqMapDownloadAllMessages reqMapDownloadAllMessages} or
	 * {@link INfCommandMap#reqMapDownloadAllMessagesToDatabase reqMapDownloadAllMessagesToDatabase}
	 *
	 * @param frequency define the callback frequency.
	 * <p><value>=0 all messages would be downloaded first, and inserted to database. Only one callback would be invocated.
	 * <p><value>>0 Callbacks would be invocated every "frequency" messages have been downloaded and inserted to database. 
	 *
	 * @return true to indicate the operation is successful, or false erroneous.
	 */
    boolean setMapDownloadNotify(int frequency);
}