/** 
 * nForeTek AVRCP Commands Interface for Android 4.3
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

import com.nforetek.bt.aidl.INfCallbackAvrcp;

/**
 * The API interface is for Audio Video Remote Control Profile (AVRCP).
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
 * <p>If the command has prefix number 13 or 14, it means the command supports AVRCP 1.3 or 1.4.
 * <br>For example, like {@link #reqAvrcp14GetFolderItems reqAvrcp14GetFolderItems} is means it is available in AVRCP 1.4.
 * @see INfCallbackAvrcp
 */
 
 interface INfCommandAvrcp {

 	/** 
	 * Check if AVRCP service is ready.
	 */
	boolean isAvrcpServiceReady();

 	/** 
	 * Register callback functions for AVRCP.
	 * <br>Call this function to register callback functions for AVRCP.
	 * <br>Allow nFore service to call back to its registered clients, which is usually the UI application.
	 *
	 * @param cb callback interface instance.
	 * @return true to indicate the operation is successful, or false erroneous.
	 */ 
	boolean registerAvrcpCallback(INfCallbackAvrcp cb);
	
	/** 
	 * Remove callback functions from AVRCP.
     * <br>Call this function to remove previously registered callback interface for AVRCP.
     * 
     * @param cb callback interface instance.
	 * @return true to indicate the operation is successful, or false erroneous.
     */ 
	boolean unregisterAvrcpCallback(INfCallbackAvrcp cb);

	/** 
	 * Get current connection state of the remote device.
	 *
	 * @return current state of AVRCP profile service.
	 */ 
	int getAvrcpConnectionState();

    /** 
	 * Check if local device is AVRCP connected with remote device.
	 *
	 * @return true to indicate AVRCP is connected, or false disconnected.
	 */
    boolean isAvrcpConnected();

	/** 
	 * Get the Bluetooth hardware address of AVRCP connected remote device.
	 * 
	 */ 
	String getAvrcpConnectedAddress();

	/** 
	 * Request to connect AVRCP to the remote device.
	 * <br>This is an asynchronous call: it will return immediately, and clients should register and implement callback functions 
	 * {@link INfCallbackAvrcp#onAvrcpStateChanged onAvrcpStateChanged} to be notified of subsequent profile state changes.
	 * 
	 * @param address valid Bluetooth MAC address.
	 * @return true to indicate the operation is successful, or false erroneous.
	 */ 	
	boolean reqAvrcpConnect(String address);
	
	/** 
	 * Request to disconnect AVRCP to the remote device.
	 * <p>This is an asynchronous call: it will return immediately, and clients should register and implement callback functions 
	 * {@link INfCallbackAvrcp#onAvrcpStateChanged onAvrcpStateChanged} to be notified of subsequent profile state changes.
	 * 	 
	 * @param address valid Bluetooth MAC address.
	 * @return true to indicate the operation is successful, or false erroneous.
	 */ 	
	boolean reqAvrcpDisconnect(String address);
	
	/** 
	 * Request if AVRCP 1.3 is supported by remote device.
	 *
	 * <p>The requested address must be the paired device and is connected currently.	 
	 *
	 * @param address valid Bluetooth MAC address of paired and connected AVRCP device.
	 * @return false if the device dose not support version 1.3 or is not connected currently.  
	 */
	boolean isAvrcp13Supported(String address);
	
	/** 
	 * Request if AVRCP 1.4 is supported by remote device.
	 *
	 * <p>The requested address must be the paired device and is connected currently.	 
	 *
	 * @param address valid Bluetooth MAC address of paired and connected AVRCP device.
	 * @return false if the device dose not support version 1.4 or is not connected currently.  
	 */
	boolean isAvrcp14Supported(String address);

	/* ==================================================================================================================================== 
	 * AVRCP v1.0
	 */	
	
	/** 
	 * Request A2DP/AVRCP connected remote device to do the "Play" operation.
	 * <p>This is an asynchronous call: it will return immediately, and clients should register and implement callback function 
	 * {@link INfCallbackA2dp#onA2dpStateChanged onA2dpStateChanged} 
	 * to be notified of subsequent profile state changes.
	 *
	 * @return true to indicate the operation is successful, or false erroneous.
	 */ 	
	boolean reqAvrcpPlay();
	
	/** 
	 * Request A2DP/AVRCP connected remote device to do the "Stop" operation.
	 * <p>This is an asynchronous call: it will return immediately, and clients should register and implement callback function 
	 * {@link INfCallbackA2dp#onA2dpStateChanged onA2dpStateChanged} 
	 * to be notified of subsequent profile state changes.
	 *
	 * @return true to indicate the operation is successful, or false erroneous.
	 */ 	
	boolean reqAvrcpStop();
	
	/** 
	 * Request A2DP/AVRCP connected remote device to do the "Pause" operation.
	 * <p>This is an asynchronous call: it will return immediately, and clients should register and implement callback function 
	 * {@link INfCallbackA2dp#onA2dpStateChanged onA2dpStateChanged} 
	 * to be notified of subsequent profile state changes.
	 *
	 * @return true to indicate the operation is successful, or false erroneous.
	 */ 	
	boolean reqAvrcpPause();
	
	/** 
	 * Request A2DP/AVRCP connected remote device to do the "Forward" operation.
	 * <p>This is an asynchronous call: it will return immediately.
	 *
	 * @return true to indicate the operation is successful, or false erroneous.
	 */ 	
	boolean reqAvrcpForward();
	
	/** 
	 * Request A2DP/AVRCP connected remote device to do the "Backward" operation.
	 * <p>This is an asynchronous call: it will return immediately.
	 *
	 * @return true to indicate the operation is successful, or false erroneous.
	 */ 	
	boolean reqAvrcpBackward();
	
	/** 
	 * Request A2DP/AVRCP connected remote device to do the "Volume Up" operation.
	 * <p>This is an asynchronous call: it will return immediately.
	 *
	 * @return true to indicate the operation is successful, or false erroneous.
	 */ 	
	boolean reqAvrcpVolumeUp();
	
	/** 
	 * Request A2DP/AVRCP connected remote device to do the "Volume Down" operation.
	 * <p>This is an asynchronous call: it will return immediately.
	 *
	 * @return true to indicate the operation is successful, or false erroneous.
	 */	
	boolean reqAvrcpVolumeDown();

	/** 
	 * Request A2DP/AVRCP connected remote device to start the "Fast Forward" operation.
	 * <p>This is an asynchronous call: it will return immediately.
	 *
	 * @return true to indicate the operation is successful, or false erroneous.
	 */		
	boolean reqAvrcpStartFastForward();

	/** 
	 * Request A2DP/AVRCP connected remote device to stop the "Fast Forward" operation.
	 * <p>This is an asynchronous call: it will return immediately.
	 *
	 * @return true to indicate the operation is successful, or false erroneous.
	 */		
	boolean reqAvrcpStopFastForward();

	/** 
	 * Request A2DP/AVRCP connected remote device to start the "Rewind" operation.
	 * <p>This is an asynchronous call: it will return immediately.
	 *
	 * @return true to indicate the operation is successful, or false erroneous.
	 */		
	boolean reqAvrcpStartRewind();

	/** 
	 * Request A2DP/AVRCP connected remote device to stop the "Rewind" operation.
	 * <p>This is an asynchronous call: it will return immediately.
	 *
	 * @return true to indicate the operation is successful, or false erroneous.
	 */		
	boolean reqAvrcpStopRewind();
	
	
	/* ==================================================================================================================================== 
	 * AVRCP v1.3
	 */		
	 
	/** 
	 * Request to get the supported events of capabilities from A2DP/AVRCP connected remote device. 
	 * This is sent by CT to inquire capabilities of the peer device.
	 *
	 * <p>This requests the list of events supported by the remote device. Remote device is expected to respond with all the events supported 
	 * including the mandatory events defined in the AVRCP v1.3 specification.	 
	 *
	 * <p>This is an asynchronous call: it will return immediately, and clients should register and implement callback functions 
	 * {@link INfCallbackAvrcp#retAvrcp13CapabilitiesSupportEvent retAvrcp13CapabilitiesSupportEvent} and 
	 * {@link INfCallbackAvrcp#onAvrcpErrorResponse onAvrcpErrorResponse} 
	 * to be notified of subsequent result.
	 *
	 * @return true to indicate the operation is successful, or false erroneous.
	 */	 
	boolean reqAvrcp13GetCapabilitiesSupportEvent();	 
	 
	/** 
	 * Request to get the supported player application setting attributes from A2DP/AVRCP connected remote device. 
	 *
	 * <p>The list of reserved player application setting attributes is provided in Appendix F of AVRCP v1.3 specification. 
	 * <br>It is expected that a target device may have additional attributes not defined as part of the specification.
	 *
	 * <p>This is an asynchronous call: it will return immediately, and clients should register and implement callback functions 
	 * {@link INfCallbackAvrcp#retAvrcp13PlayerSettingAttributesList retAvrcp13PlayerSettingAttributesList} and 
	 * {@link INfCallbackAvrcp#onAvrcpErrorResponse onAvrcpErrorResponse} 
	 * to be notified of subsequent result.
	 *
	 * @return true to indicate the operation is successful, or false erroneous.
	 */		  
	boolean reqAvrcp13GetPlayerSettingAttributesList();
	 
	/** 
	 * Request to get the set of possible values for the requested player application setting attribute 
	 * from A2DP/AVRCP connected remote device. 
	 *
	 * <p>The list of reserved player application setting attributes and their values are provided in Appendix F of AVRCP v1.3 specification. 
	 * <br>It is expected that a target device may have additional attribute values not defined as part of the specification.
	 *
	 * <p>This is an asynchronous call: it will return immediately, and clients should register and implement callback functions 
	 * {@link INfCallbackAvrcp#retAvrcp13PlayerSettingValuesList retAvrcp13PlayerSettingValuesList} and 
	 * {@link INfCallbackAvrcp#onAvrcpErrorResponse onAvrcpErrorResponse} 
	 * to be notified of subsequent result.
	 *
	 * @param attributeId the requesting attribute ID. Possible values are:
	 * 		<blockquote><b>AVRCP_SETTING_ATTRIBUTE_ID_EQUALIZER</b>	(byte) 0x01
	 *		<br><b>AVRCP_SETTING_ATTRIBUTE_ID_REPEAT_MODE</b>			(byte) 0x02
	 *		<br><b>AVRCP_SETTING_ATTRIBUTE_ID_SHUFFLE</b>				(byte) 0x03
	 *		<br><b>AVRCP_SETTING_ATTRIBUTE_ID_SCAN</b>					(byte) 0x04</blockquote>
	 * @return true to indicate the operation is successful, or false erroneous.
	 */		 
	boolean reqAvrcp13GetPlayerSettingValuesList(byte attributeId);
	 
	/** 
	 * Request to get the current set values from A2DP/AVRCP connected remote device
	 * for the provided player application setting attribute. 
	 * 
	 * <p>This is an asynchronous call: it will return immediately, and clients should register and implement callback functions 
	 * {@link INfCallbackAvrcp#retAvrcp13PlayerSettingCurrentValues retAvrcp13PlayerSettingCurrentValues} and 
	 * {@link INfCallbackAvrcp#onAvrcpErrorResponse onAvrcpErrorResponse} 
	 * to be notified of subsequent result.
	 *
	 * @return true to indicate the operation is successful, or false erroneous.
	 */		 
	boolean reqAvrcp13GetPlayerSettingCurrentValues();
	 
	/** 
	 * Request to set the player application setting of player application setting value on A2DP/AVRCP connected remote device 
	 * for the corresponding defined PlayerApplicationSettingAttribute. 
	 * 
	 * <p>This is an asynchronous call: it will return immediately, and clients should register and implement callback functions 
	 * {@link INfCallbackAvrcp#retAvrcp13SetPlayerSettingValueSuccess retAvrcp13SetPlayerSettingValueSuccess} and 
	 * {@link INfCallbackAvrcp#onAvrcpErrorResponse onAvrcpErrorResponse} 
	 * to be notified of subsequent result.
	 *
	 * @param attributeId the requesting attribute ID. Possible values are:
	 * 		<blockquote><b>AVRCP_SETTING_ATTRIBUTE_ID_EQUALIZER</b>	(byte) 0x01
	 *		<br><b>AVRCP_SETTING_ATTRIBUTE_ID_REPEAT_MODE</b>			(byte) 0x02
	 *		<br><b>AVRCP_SETTING_ATTRIBUTE_ID_SHUFFLE</b>				(byte) 0x03
	 *		<br><b>AVRCP_SETTING_ATTRIBUTE_ID_SCAN</b>					(byte) 0x04</blockquote>
	 * @param valueId the setting value. Possible values are:
	 * 		<blockquote>For <b>AVRCP_SETTING_ATTRIBUTE_ID_EQUALIZER</b></blockquote>
	 *			<blockquote><blockquote><b>AVRCP_SETTING_VALUE_ID_EQUALIZER_OFF</b>	(byte) 0x01
	 *			<br><b>AVRCP_SETTING_VALUE_ID_EQUALIZER_ON</b>						(byte) 0x02</blockquote></blockquote>
	 *		<blockquote>For <b>AVRCP_SETTING_ATTRIBUTE_ID_REPEAT_MODE</b></blockquote>
	 *			<blockquote><blockquote><b>AVRCP_SETTING_VALUE_ID_REPEAT_OFF</b>	(byte) 0x01
	 *			<br><b>AVRCP_SETTING_VALUE_ID_REPEAT_SINGLE</b>						(byte) 0x02
	 *			<br><b>AVRCP_SETTING_VALUE_ID_REPEAT_ALL</b>						(byte) 0x03
	 *			<br><b>AVRCP_SETTING_VALUE_ID_REPEAT_GROUP</b>						(byte) 0x04</blockquote></blockquote>
	 *		<blockquote>For <b>AVRCP_SETTING_ATTRIBUTE_ID_SHUFFLE</b></blockquote>
	 *			<blockquote><blockquote><b>AVRCP_SETTING_VALUE_ID_SHUFFLE_OFF</b>	(byte) 0x01
	 *			<br><b>AVRCP_SETTING_VALUE_ID_SHUFFLE_ALL</b>						(byte) 0x02
	 *			<br><b>AVRCP_SETTING_VALUE_ID_SHUFFLE_GROUP</b>						(byte) 0x03</blockquote></blockquote>
	 *		<blockquote>For <b>AVRCP_SETTING_ATTRIBUTE_ID_SCAN</b></blockquote>
	 *			<blockquote><blockquote><b>AVRCP_SETTING_VALUE_ID_SCAN_OFF</b>		(byte) 0x01
	 *			<br><b>AVRCP_SETTING_VALUE_ID_SCAN_ALL</b>							(byte) 0x02
	 *			<br><b>AVRCP_SETTING_VALUE_ID_SCAN_GROUP</b>						(byte) 0x03</blockquote></blockquote>
	 * @return true to indicate the operation is successful, or false erroneous.
	 */		 
	boolean reqAvrcp13SetPlayerSettingValue(byte attributeId, byte valueId);
	 
	/** 
	 * Request to get the attributes of the element specified in the parameter 
	 * from A2DP/AVRCP connected remote device
	 * 
	 * <p>This is an asynchronous call: it will return immediately, and clients should register and implement callback functions 
	 * {@link INfCallbackAvrcp#retAvrcp13ElementAttributesPlaying retAvrcp13ElementAttributesPlaying} and
	 * {@link INfCallbackAvrcp#onAvrcpErrorResponse onAvrcpErrorResponse} 
	 * to be notified of subsequent result.
	 * 
	 * @return true to indicate the operation is successful, or false erroneous.
	 */		 
	boolean reqAvrcp13GetElementAttributesPlaying();
	 
	/** 
	 * Request to get the status of the currently playing media  
	 * from A2DP/AVRCP connected remote device
	 *
	 * <p>This is an asynchronous call: it will return immediately, and clients should register and implement callback functions 
	 * {@link INfCallbackAvrcp#retAvrcp13PlayStatus retAvrcp13PlayStatus} and
	 * {@link INfCallbackAvrcp#onAvrcpErrorResponse onAvrcpErrorResponse} 
	 * to be notified of subsequent result.
	 *	 
	 * @return true to indicate the operation is successful, or false erroneous.
	 */		 
	boolean reqAvrcp13GetPlayStatus();
	 
	/** 
	 * Request to register with A2DP/AVRCP connected remote device
	 * to receive notifications asynchronously based on specific events occurring. 
	 * 
	 * <p>The events registered would be kept on remote device until another
	 * {@link INfCommandAvrcp#reqAvrcpUnregisterEventWatcher reqAvrcpUnregisterEventWatcher} is called.
	 * 
	 * <p>This is an asynchronous call: it will return immediately, and clients should register and implement callback functions 
	 * <blockquote>
	 * <br>{@link INfCallbackAvrcp#onAvrcp13RegisterEventWatcherSuccess onAvrcp13RegisterEventWatcherSuccess},
	 * <br>{@link INfCallbackAvrcp#onAvrcp13RegisterEventWatcherFail onAvrcp13RegisterEventWatcherFail},
	 *
	 * <br>{@link INfCallbackAvrcp#onAvrcp13EventPlaybackStatusChanged onAvrcp13EventPlaybackStatusChanged},
	 * <br>{@link INfCallbackAvrcp#onAvrcp13EventTrackChanged onAvrcp13EventTrackChanged},
	 * <br>{@link INfCallbackAvrcp#onAvrcp13EventTrackReachedEnd onAvrcp13EventTrackReachedEnd},
	 * <br>{@link INfCallbackAvrcp#onAvrcp13EventTrackReachedStart onAvrcp13EventTrackReachedStart},
	 * <br>{@link INfCallbackAvrcp#onAvrcp13EventPlaybackPosChanged onAvrcp13EventPlaybackPosChanged},
 	 * <br>{@link INfCallbackAvrcp#onAvrcp13EventBatteryStatusChanged onAvrcp13EventBatteryStatusChanged},
 	 * <br>{@link INfCallbackAvrcp#onAvrcp13EventSystemStatusChanged onAvrcp13EventSystemStatusChanged},
 	 * <br>{@link INfCallbackAvrcp#onAvrcp13EventPlayerSettingChanged onAvrcp13EventPlayerSettingChanged},
 	 * <br>v1.4
 	 * <br>{@link INfCallbackAvrcp#onAvrcp14EventNowPlayingContentChanged onAvrcp14EventNowPlayingContentChanged},
 	 * <br>{@link INfCallbackAvrcp#onAvrcp14EventAvailablePlayerChanged onAvrcp14EventAvailablePlayerChanged},
 	 * <br>{@link INfCallbackAvrcp#onAvrcp14EventAddressedPlayerChanged onAvrcp14EventAddressedPlayerChanged},
 	 * <br>{@link INfCallbackAvrcp#onAvrcp14EventUidsChanged onAvrcp14EventUidsChanged},
 	 * <br>{@link INfCallbackAvrcp#onAvrcp14EventVolumeChanged onAvrcp14EventVolumeChanged}, and
	 * <br>{@link INfCallbackAvrcp#onAvrcpErrorResponse onAvrcpErrorResponse} </blockquote>
	 * to be notified of subsequent result.
	 * <br>Each corresponding callback would be invoked once immediately after the event has been registered successfully. 
	 *
	 *  
	 * @param eventId the registering event ID. Possible values are:
	 *		<blockquote><b>AVRCP_EVENT_ID_PLAYBACK_STATUS_CHANGED</b>		(byte) 0x01
	 *		<br><b>AVRCP_EVENT_ID_TRACK_CHANGED</b>							(byte) 0x02
	 *		<br><b>AVRCP_EVENT_ID_TRACK_REACHED_END</b>						(byte) 0x03
	 *		<br><b>AVRCP_EVENT_ID_TRACK_REACHED_START</b>					(byte) 0x04
	 *		<br><b>AVRCP_EVENT_ID_PLAYBACK_POS_CHANGED</b>					(byte) 0x05
	 *		<br><b>AVRCP_EVENT_ID_BATT_STATUS_CHANGED</b>					(byte) 0x06
	 *		<br><b>AVRCP_EVENT_ID_SYSTEM_STATUS_CHANGED</b>					(byte) 0x07
	 *		<br><b>AVRCP_EVENT_ID_PLAYER_APPLICATION_SETTING_CHANGED</b>	(byte) 0x08
	 *		<br>v1.4
	 *		<br><b>AVRCP_EVENT_ID_NOW_PLAYING_CONTENT_CHANGED</b>			(byte) 0x09
	 *		<br><b>AVRCP_EVENT_ID_AVAILABLE_PLAYERS_CHANGED</b>				(byte) 0x0a
	 *		<br><b>AVRCP_EVENT_ID_ADDRESSED_PLAYER_CHANGED</b>				(byte) 0x0b
	 *		<br><b>AVRCP_EVENT_ID_UIDS_CHANGED</b>							(byte) 0x0c
	 *		<br><b>AVRCP_EVENT_ID_VOLUME_CHANGED</b>						(byte) 0x0d</blockquote>		
	 * @param interval the update interval in second. 
	 * <br>This parameter applicable only for <b>AVRCP_EVENT_ID_PLAYBACK_POS_CHANGED</b>. 
	 * For other events, this parameter is <b>ignored</b> !
	 * @return true to indicate the operation is successful, or false erroneous.
	 */		 
	boolean reqAvrcpRegisterEventWatcher(byte eventId, long interval);
	 
	/** 
	 * Request to unregister the specific events with A2DP/AVRCP connected remote device.
	 *
	 * <p>This is an asynchronous call: it will return immediately.
	 *
	 * @param eventId the unregistering event ID. Possible values are
	 *		<blockquote><b>AVRCP_EVENT_ID_PLAYBACK_STATUS_CHANGED</b>		(byte) 0x01
	 *		<br><b>AVRCP_EVENT_ID_TRACK_CHANGED</b>							(byte) 0x02
	 *		<br><b>AVRCP_EVENT_ID_TRACK_REACHED_END</b>						(byte) 0x03
	 *		<br><b>AVRCP_EVENT_ID_TRACK_REACHED_START</b>					(byte) 0x04
	 *		<br><b>AVRCP_EVENT_ID_PLAYBACK_POS_CHANGED</b>					(byte) 0x05
	 *		<br><b>AVRCP_EVENT_ID_BATT_STATUS_CHANGED</b>					(byte) 0x06
	 *		<br><b>AVRCP_EVENT_ID_SYSTEM_STATUS_CHANGED</b>					(byte) 0x07
	 *		<br><b>AVRCP_EVENT_ID_PLAYER_APPLICATION_SETTING_CHANGED</b>	(byte) 0x08
	 *		<br>v1.4	 
	 *		<br><b>AVRCP_EVENT_ID_NOW_PLAYING_CONTENT_CHANGED</b>			(byte) 0x09
	 *		<br><b>AVRCP_EVENT_ID_AVAILABLE_PLAYERS_CHANGED</b>				(byte) 0x0a
	 *		<br><b>AVRCP_EVENT_ID_ADDRESSED_PLAYER_CHANGED</b>				(byte) 0x0b
	 *		<br><b>AVRCP_EVENT_ID_UIDS_CHANGED</b>							(byte) 0x0c
	 *		<br><b>AVRCP_EVENT_ID_VOLUME_CHANGED</b>						(byte) 0x0d</blockquote>		
	 * @return true to indicate the operation is successful, or false erroneous.
	 */		 
	boolean reqAvrcpUnregisterEventWatcher(byte eventId);
	 	 
	/** 
	 * Request A2DP/AVRCP connected remote device to move to the first song in the next group.
	 * 
	 * <p>This is an asynchronous call: it will return immediately.
	 * 
	 * @return true to indicate the operation is successful, or false erroneous.
	 */
 	boolean reqAvrcp13NextGroup();
	 
	/** 
	 * Request A2DP/AVRCP connected remote device to move to the first song in the previous group.
	 *
	 * <p>This is an asynchronous call: it will return immediately.
	 *
	 * @return true to indicate the operation is successful, or false erroneous.
	 */ 	 
	boolean reqAvrcp13PreviousGroup();
		 
	/* ==================================================================================================================================== 
	 * AVRCP v1.4
	 */


	/** 
	 * Request if A2DP/AVRCP connected remote device has browsing channel established. 
	 *
	 * @return true to indicate the remote device has browsing channel.
	 */			 	 	 
	boolean isAvrcp14BrowsingChannelEstablished();
	 
	/** 
	 * Request to inform the A2DP/AVRCP connected remote device of which media player we wishes to control.
	 * <p>The player is selected by its "Player Id".
	 * <br>When the addressed player is changed, whether locally on the remote device or explicitly by us, 
	 * the remote device shall complete notifications following the mechanism described in section 6.9.2 of AVRCP v1.4 specification. 
	 * <p>This is an asynchronous call: it will return immediately, and clients should register and implement callback functions 
	 * {@link INfCallbackAvrcp#retAvrcp14SetAddressedPlayerSuccess retAvrcp14SetAddressedPlayerSuccess} and
	 * {@link INfCallbackAvrcp#onAvrcpErrorResponse onAvrcpErrorResponse} 
	 * to be notified of subsequent result.
	 *
	 * @param playerId the selected player ID of 2 octets. 
	 * @return true to indicate the operation is successful, or false erroneous.
	 */		 
	boolean reqAvrcp14SetAddressedPlayer(int playerId);
	
	/** 
	 * Request the A2DP/AVRCP connected remote device to route browsing commands to which player.
	 *
	 * <p>The player to which AVRCP shall route browsing commands is the browsed player. 
	 * <br>This command shall be sent successfully before any other commands are sent on the browsing channel except 
	 * {@link INfCommandAvrcp#reqAvrcp14GetFolderItems reqAvrcp14GetFolderItems}
	 * in the Media Player List scope. 
	 * <br>If the browsed player has become unavailable this command shall be sent successfully again before further commands are sent on the browsing channel. 
	 * <br>Some players may support browsing only when set as the Addressed Player.
	 * <p>The player is selected by its "Player Id".
	 * <p>This is an asynchronous call: it will return immediately, and clients should register and implement callback functions 
	 * {@link INfCallbackAvrcp#retAvrcp14SetBrowsedPlayerSuccess retAvrcp14SetBrowsedPlayerSuccess} and
	 * {@link INfCallbackAvrcp#onAvrcpErrorResponse onAvrcpErrorResponse}
	 * to be notified of subsequent result.
	 *
	 * @param playerId the selected player ID of 2 octets. 
	 * @return true to indicate the operation is successful, or false erroneous.
	 */				
	boolean reqAvrcp14SetBrowsedPlayer(int playerId);
	
	/** 
	 * Request to retrieve a listing of the contents of a folder on A2DP/AVRCP connected remote device.
	 *
	 * <p>The folder is the representation of available media players, virtual file system, the last searching result, or the playing list.
	 * Should not issue this command to an empty folder.
	 *
	 * <p>This is an asynchronous call: it will return immediately, and clients should register and implement callback functions 
	 * {@link INfCallbackAvrcp#retAvrcp14FolderItems retAvrcp14FolderItems} and
	 * {@link INfCallbackAvrcp#onAvrcpErrorResponse onAvrcpErrorResponse}
	 * to be notified of subsequent result.
	 *
	 * @param scopeId the requesting folder ID. Possible values are:
	 * 		<blockquote><b>AVRCP_SCOPE_ID_MEDIA_PLAYER</b>	(byte) 0x00
	 *		<br><b>AVRCP_SCOPE_ID_VFS</b>						(byte) 0x01
	 *		<br><b>AVRCP_SCOPE_ID_SEARCH</b>					(byte) 0x02
	 *		<br><b>AVRCP_SCOPE_ID_NOW_PLAYING</b>				(byte) 0x03</blockquote>
	 * @return true to indicate the operation is successful, or false erroneous.
	 */	
	boolean reqAvrcp14GetFolderItems(byte scopeId);
	
	/** 
	 * Request to navigate the virtual filesystem on A2DP/AVRCP connected remote device. 
	 * <p>This command allows us to navigate one level up or down in the virtual filesystem.
	 * <p>Uid counters parameter is used to make sure that our uid cache is consistent with what remote device has currently. 
	 * <p>This is an asynchronous call: it will return immediately, and clients should register and implement callback functions 
	 * {@link INfCallbackAvrcp#retAvrcp14ChangePathSuccess retAvrcp14ChangePathSuccess} and
	 * {@link INfCallbackAvrcp#onAvrcpErrorResponse onAvrcpErrorResponse}
	 * to be notified of subsequent result.
	 *
	 * @param uidCounter the value of uid counter we have.
	 * @param uid The UID of the folder to navigate to. This may be retrieved via a 
	 * {@link INfCommandAvrcp#reqAvrcp14GetFolderItems reqAvrcp14GetFolderItems} command. 
	 * <br>If the navigation command is "Folder Up" this field is <b>reserved</b>.	 
	 * @param direction the requesting operation on selested UID. Possible values are:
	 * 		<blockquote><b>AVRCP_FOLDER_DIRECTION_ID_UP</b>		(byte) 0x00
	 *		<br><b>AVRCP_FOLDER_DIRECTION_ID_DOWN</b>			(byte) 0x01</blockquote>
	 * @return true to indicate the operation is successful, or false erroneous.
	 */		
	boolean reqAvrcp14ChangePath(int uidCounter, long uid, byte direction);
	
	/** 
	 * Request to retrieve the metadata attributes for a particular media element item or folder item 
	 * on A2DP/AVRCP connected remote device. 
	 * <p>When the remote device supports this command, we shall use this command and not {@link #reqAvrcp13GetElementAttributesPlaying reqAvrcp13GetElementAttributesPlaying}. 
	 * <br>To retrieve the Metadata for the currently playing track we should register to receive Track Changed Notifications. 
	 * <br>This shall then provide the UID of the currently playing track, which can be used in the scope of the Now Playing folder.
	 * <p>This is an asynchronous call: it will return immediately, and clients should register and implement callback functions 
	 * {@link INfCallbackAvrcp#retAvrcp14ItemAttributes retAvrcp14ItemAttributes} and
	 * {@link INfCallbackAvrcp#onAvrcpErrorResponse onAvrcpErrorResponse}
	 * to be notified of subsequent result.
	 *
	 * @param scopeId the requesting folder ID. Possible values are
	 *		<blockquote><b>AVRCP_SCOPE_ID_VFS</b>		(byte) 0x01
	 *		<br><b>AVRCP_SCOPE_ID_SEARCH</b>			(byte) 0x02
	 *		<br><b>AVRCP_SCOPE_ID_NOW_PLAYING</b>		(byte) 0x03</blockquote>
	 * @param uidCounter the value of uid counter we have.
	 * @param uid The UID of the media element item or folder item to return the attributes for. UID 0 shall not be used.
	 * @return true to indicate the operation is successful, or false erroneous.
	 */			
	boolean reqAvrcp14GetItemAttributes(byte scopeId, int uidCounter, long uid);
				
	/** 
	 * Request A2DP/AVRCP connected remote device to starts playing an item indicated by the UID.
	 * It is routed to the Addressed Player. 
	 * <p>If a UID changed event has happened but not received by local yet, the previous UID counter may be sent. 
	 * In this case a failure status shall be returned.
	 * <p>Request this command with the scope parameter of 
	 *		<blockquote><b>AVRCP_SCOPE_ID_VFS</b>			(byte) 0x01 or 
	 * 		<br><b>AVRCP_SCOPE_ID_SEARCH</b>				(byte) 0x02</blockquote>
	 * shall result in the NowPlaying folder being invalidated. 
	 * <br>The old content may not be valid any more or may contain additional items. 
	 * <p>What is put in the NowPlaying folder depends on both the media player and its state, however the item selected by us shall be included.
	 * <p>Request this command with the scope parameter of 
	 * 		<blockquote><b>AVRCP_SCOPE_ID_NOW_PLAYING</b>	(byte) 0x03</blockquote>
	 * should not change the contents of the NowPlaying Folder, the only effect is that the new item is played.
	 * <p>Never request this command with the scope parameter 
	 * 		<blockquote><b>AVRCP_SCOPE_ID_MEDIA_PLAYER</b>	(byte) 0x00.</blockquote>
	 * <p>This is an asynchronous call: it will return immediately, and clients should register and implement callback functions 
	 * {@link INfCallbackAvrcp#retAvrcp14PlaySelectedItemSuccess retAvrcp14PlaySelectedItemSuccess} and
	 * {@link INfCallbackAvrcp#onAvrcpErrorResponse onAvrcpErrorResponse}
	 * to be notified of subsequent result.
	 *
	 * @param scopeId The scope in which the UID of the media element item or folder item, if supported, is valid. Possible values are
	 *		<blockquote><b>AVRCP_SCOPE_ID_VFS</b>		(byte) 0x01
	 *		<br><b>AVRCP_SCOPE_ID_SEARCH</b>			(byte) 0x02
	 *		<br><b>AVRCP_SCOPE_ID_NOW_PLAYING</b>		(byte) 0x03</blockquote>
	 * @param uidCounter the value of uid counter we have.
	 * @param uid The UID of the media element item or folder item, if supported, to be played.
	 * @return true to indicate the operation is successful, or false erroneous.
	 */		
	boolean reqAvrcp14PlaySelectedItem(byte scopeId, int uidCounter,long uid);
	
	/** 
	 * Request to perform the basic search from the current folder and all folders below the Browsed Players virtual filesystem. 
	 * Regular expressions shall not be supported. 
	 * <br>Search results are valid until
	 * 		<blockquote>Another search request is performed or
	 *		<br>A UIDs changed notification response is received
	 * 		<br>The Browsed player is changed</blockquote>
	 * <p>The search result would contain only media element items.
	 * <br>Searching may not be supported by all players. Furthermore, searching may only be possible on some players 
	 * when they are set as the Addressed Player.
	 * <p>This is an asynchronous call: it will return immediately, and clients should register and implement callback functions 
	 * {@link INfCallbackAvrcp#retAvrcp14SearchResult retAvrcp14SearchResult} and
	 * {@link INfCallbackAvrcp#onAvrcpErrorResponse onAvrcpErrorResponse}
	 * to be notified of subsequent result.
	 *
	 * @param text The string to search on in the specified character set.
	 * @return true to indicate the operation is successful, or false erroneous.
	 */		
	boolean reqAvrcp14Search(in String text);
	
	/** 
	 * Request A2DP/AVRCP connected remote device to add an item indicated by the UID to the NowPlaying queue. 
	 * <p>If a UID changed event has happened but not received by local yet, the previous UID counter may be sent. 
	 * In this case a failure status shall be returned.
	 * <p>Request this command with the scope parameter of 
	 *		<blockquote><b>AVRCP_SCOPE_ID_VFS</b>			(byte) 0x01
	 * 		<br><b>AVRCP_SCOPE_ID_SEARCH</b>				(byte) 0x02 or
	 * 		<br><b>AVRCP_SCOPE_ID_NOW_PLAYING</b>			(byte) 0x03</blockquote>
	 * shall result in the item being added to the NowPlaying folder on media players that support this command.
	 * <p>Never request this command with the scope parameter 
	 * 		<blockquote><b>AVRCP_SCOPE_ID_MEDIA_PLAYER</b>	(byte) 0x00.</blockquote>
	 * This command could be requested with the UID of a Folder Item if the folder is playable. 
	 * <p>The media items of that folder are added to the NowPlaying list, not the folder itself.
	 * <p>This is an asynchronous call: it will return immediately, and clients should register and implement callback functions 
	 * {@link INfCallbackAvrcp#retAvrcp14AddToNowPlayingSuccess retAvrcp14AddToNowPlayingSuccess} and
	 * {@link INfCallbackAvrcp#onAvrcpErrorResponse onAvrcpErrorResponse}
	 * to be notified of subsequent result.
	 *
	 * @param scopeId The scope in which the UID of the media element item or folder item, if supported, is valid. Possible values are
	 *		<blockquote><b>AVRCP_SCOPE_ID_VFS</b>		(byte) 0x01
	 *		<br><b>AVRCP_SCOPE_ID_SEARCH</b>			(byte) 0x02
	 *		<br><b>AVRCP_SCOPE_ID_NOW_PLAYING</b>		(byte) 0x03</blockquote>
	 * @param uidCounter the value of uid counter we have.
	 * @param uid The UID of the media element item or folder item, if supported, to be played.
	 * @return true to indicate the operation is successful, or false erroneous.
	 */		
	boolean reqAvrcp14AddToNowPlaying(byte scopeId, int uidCounter, long uid);	
	
	/** 
	 * By the AVRCP v1.4 specification, this command is used to set an absolute volume to be used by the rendering device. 
	 * This is in addition to the relative volume commands. 
	 * <p>It is expected that the audio sink will perform as the TG role for this command.
	 * <br>As this command specifies a percentage rather than an absolute dB level, the CT should exercise caution when sending this command.
	 * <p>It should be noted that this command is intended to alter the rendering volume on the audio sink. 
	 * <br>It is not intended to alter the volume within the audio stream. The volume level which has actually been set on the TG is returned in the response. 
	 * This is to enable the CT to deal with whatever granularity of volume control the TG provides.
	 * <p>The setting volume is represented in one octet. The top bit (bit 7) is reserved for future definition. 
	 * <br>The volume is specified as a percentage of the maximum. The value 0x0 corresponds to 0%. The value 0x7F corresponds to 100%.
	 * <p>This is an asynchronous call: it will return immediately, and clients should register and implement callback functions 
	 * {@link INfCallbackAvrcp#retAvrcp14SetAbsoluteVolumeSuccess retAvrcp14SetAbsoluteVolumeSuccess} and 
	 * {@link INfCallbackAvrcp#onAvrcpErrorResponse onAvrcpErrorResponse} 
	 * to be notified of subsequent result.
	 *
	 * @param volume the setting volume value of octet from 0x00 to 0x7F.
	 * @return true to indicate the operation is successful, or false erroneous.
	 */		
	boolean reqAvrcp14SetAbsoluteVolume(byte volume);

	boolean reqAvrcpQueryVersion(String address);

 }