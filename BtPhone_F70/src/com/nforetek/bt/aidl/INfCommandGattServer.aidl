/** 
 * nForeTek Gatt Server Commands Interface for Android 4.4.2
 *
 * Copyright (C) 2011-2019  nForeTek Corporation
 *
 * Zeus 3.0.0 on 20160321
 * @author KC Huang <kchuang@nforetek.com>
 * @author Piggy    <piggylee@nforetek.com>
 * @version 3.0.0
 * 
 */

package com.nforetek.bt.aidl;

import com.nforetek.bt.aidl.INfCallbackGattServer;

/**
 * The API interface is for Gatt Server (BLE Peripheral)
 * <br>UI program may use these specific APIs to access to nFore service.
 * <br>The naming principle of API in this doc is as follows,
 *      <blockquote><b>setXXX()</b> :   set attributes to specific functions of nFore service.
 *      <br><b>reqXXX()</b> :               request nFore service to implement specific function. It is an Asynchronized mode.
 *      <br><b>isXXX()</b> :                check the current status from nFore service. It is a Synchronized mode.
 *      <br><b>getXXX()</b> :               get the current result from nFore service. It is a Synchronized mode.</blockquote>
 *
 * <p>The constant variables in this Doc could be found and referred by importing
 *      <br><blockquote>com.nforetek.bt.res.NfDef</blockquote>
 * <p>with prefix NfDef class name. Ex : <code>NfDef.DEFAULT_ADDRESS</code>
 *
 * <p>Valid Bluetooth hardware addresses must be in a format such as "00:11:22:33:AA:BB".
 *
 * @see INfCallbackGattServer
 */
 
 interface INfCommandGattServer {

    /** 
     * Check if Gatt Server service is ready.
     */
    boolean isGattServiceReady();

    /** 
     * Register callback functions for Gatt Server.
     * <br>Call this function to register callback functions for Gatt Server.
     * <br>Allow nFore service to call back to its registered clients, which is usually the UI application.
     *
     * @param cb callback interface instance.
     * @return true to indicate the operation is successful, or false erroneous.
     */ 
    boolean registerGattServerCallback(INfCallbackGattServer cb);

    /** 
     * Remove callback functions from Gatt Server.
     * <br>Call this function to remove previously registered callback interface for Gatt Server.
     * 
     * @param cb callback interface instance.
     * @return true to indicate the operation is successful, or false erroneous.
     */
    boolean unregisterGattServerCallback(INfCallbackGattServer cb);

    /** 
     * Get current connection state of the remote device.
     * 
     * @return current state of Gatt Server profile service.
     */ 
    int getGattServerConnectionState();

    /** 
     * Disconnects an established connection, or cancels a connection attempt
     * currently in progress.
     * <br>This is an asynchronous call: it will return immediately, and clients should register 
     * and implement callback functions {@link INfCallbackGattServer#onGattServerStateChanged onGattServerStateChanged} 
     * to be notified of subsequent profile state changes.
     * 
     * @param address Remote device address
     * @return true to indicate the operation is successful, or false erroneous.
     */ 
    boolean reqGattServerDisconnect(String address);

    /** 
     * Request to add Gatt service with service type and UUID.
     *
     * @param srvcType service type. Possible values are:
     *      <br>GATT_SERVICE_TYPE_PRIMARY                (int) 0
     *      <br>GATT_SERVICE_TYPE_SECONDARY              (int) 1
     * 
     * @param srvcUuid service UUID.
     *
     * @return true to indicate the operation is successful, or false erroneous.
     */ 
    boolean reqGattServerBeginServiceDeclaration(int srvcType, in ParcelUuid srvcUuid); 

    /** 
     * Request to add Gatt characteristic with UUID, properties and permissions with 
     * defined service.
     *
     * @param charUuid characteristic UUID.
     * @param properties Characteristic property. Possible values are:
     *      <br>GATT_CHARACTERISTIC_PROPERTY_BROADCAST              (int) 0x01
     *      <br>Characteristic is broadcastable.
     *
     *      <br>GATT_CHARACTERISTIC_PROPERTY_READ                   (int) 0x02
     *      <br>Characteristic is readable.
     *
     *      <br>GATT_CHARACTERISTIC_PROPERTY_WRITE_NO_RESPONSE      (int) 0x04
     *      <br>Characteristic can be written without response.
     *
     *      <br>GATT_CHARACTERISTIC_PROPERTY_WRITE                  (int) 0x08
     *      <br>Characteristic can be written.
     *
     *      <br>GATT_CHARACTERISTIC_PROPERTY_NOTIFY                 (int) 0x10
     *      <br>Characteristic supports notification
     *
     *      <br>GATT_CHARACTERISTIC_PROPERTY_INDICATE               (int) 0x20
     *      <br>Characteristic supports indication
     *
     *      <br>GATT_CHARACTERISTIC_PROPERTY_SIGNED_WRITE           (int) 0x30
     *      <br>Characteristic supports write with signature
     *
     *      <br>GATT_CHARACTERISTIC_PROPERTY_EXTENDED_PROPS         (int) 0x40
     *      <br>Characteristic has extended properties
     *
     * @param permissions Characteristic permission. Attribute permissions 
     *  are a combination of access permissions, authentication
     *  permissions and authorization permissions. Possible values are:
     *      <br>GATT_CHARACTERISTIC_PERMISSION_READ                 (int) 0x01
     *      <br>Characteristic read permission
     *
     *      <br>GATT_CHARACTERISTIC_PERMISSION_READ_ENCRYPTED        (int) 0x02
     *      <br>Allow encrypted read operations
     *
     *      <br>GATT_CHARACTERISTIC_PERMISSION_READ_ENCRYPTED_MITM    (int) 0x04
     *      <br>Allow reading with man-in-the-middle protection
     *
     *      <br>GATT_CHARACTERISTIC_PERMISSION_WRITE                  (int) 0x10
     *      <br>Characteristic write permission
     *
     *      <br>GATT_CHARACTERISTIC_PERMISSION_WRITE_ENCRYPTED         (int) 0x20
     *      <br>Allow encrypted writes
     *
     *      <br>GATT_CHARACTERISTIC_PERMISSION_WRITE_ENCRYPTED_MITM    (int) 0x40
     *      <br>Allow encrypted writes with man-in-the-middle protection
     *
     *      <br>GATT_CHARACTERISTIC_PERMISSION_WRITE_SIGNED             (int) 0x80
     *      <br>Allow signed write operations
     *
     *      <br>GATT_CHARACTERISTIC_PERMISSION_WRITE_SIGNED_MITM        (int) 0x100
     *      <br>Allow signed write operations with man-in-the-middle protection
     *
     *  For example, if this characteristic could be read/write with authentication permission,
     *  this value could be set as 0x22. (read encrypted and write encrypted)
     *
     * @return true to indicate the operation is successful, or false erroneous.
     */ 
    boolean reqGattServerAddCharacteristic(in ParcelUuid charUuid, int properties,
                           int permissions);

    /** 
     * Request to add Gatt characteristic descriptor with UUID and permissions with 
     * defined characteristic.
     *
     * @param descUuid descriptor UUID.
     * @param permissions Descriptor permission. Attribute descriptor permissions 
     *  are a combination of access permissions, authentication
     *  permissions and authorization permissions. Possible values are:
     *      <br>GATT_DESCRIPTOR_PERMISSION_READ                 (int) 0x01
     *      <br>Descriptor read permission
     *
     *      <br>GATT_DESCRIPTOR_PERMISSION_READ_ENCRYPTED        (int) 0x02
     *      <br>Allow encrypted read operations
     *
     *      <br>GATT_DESCRIPTOR_PERMISSION_READ_ENCRYPTED_MITM    (int) 0x04
     *      <br>Allow reading with man-in-the-middle protection
     *
     *      <br>GATT_DESCRIPTOR_PERMISSION_WRITE                  (int) 0x10
     *      <br>Descriptor write permission
     *
     *      <br>GATT_DESCRIPTOR_PERMISSION_WRITE_ENCRYPTED         (int) 0x20
     *      <br>Allow encrypted writes
     *
     *      <br>GATT_DESCRIPTOR_PERMISSION_WRITE_ENCRYPTED_MITM    (int) 0x40
     *      <br>Allow encrypted writes with man-in-the-middle protection
     *
     *      <br>GATT_DESCRIPTOR_PERMISSION_WRITE_SIGNED             (int) 0x80
     *      <br>Allow signed write operations
     *
     *      <br>GATT_DESCRIPTOR_PERMISSION_WRITE_SIGNED_MITM        (int) 0x100
     *      <br>Allow signed write operations with man-in-the-middle protection
     *
     *  For example, if this characteristic descriptor could be read/write with authentication permission,
     *  this value could be set as 0x22. (read encrypted and write encrypted)
     *
     * @return true to indicate the operation is successful, or false erroneous.
     */ 
    boolean reqGattServerAddDescriptor(in ParcelUuid descUuid, int permissions);

    /** 
     * Finish Service declaration. The service and related characteristics and descriptors 
     * are registered after end service declaration.
     *
     * @return true to indicate the operation is successful, or false erroneous.
     */ 
    boolean reqGattServerEndServiceDeclaration();

    /** 
     * Request to remove registered Gatt service with service type and UUID.
     * <br>This is an asynchronous call: it will return immediately, and clients should register 
     * and implement callback functions {@link INfCallbackGattServer#onGattServerServiceDeleted onGattServerServiceDeleted} 
     * to be notified of services deleted.
     *
     * @param srvcType service type.
     * @param srvcUuid service UUID.
     *
     * @return true to indicate the operation is successful, or false erroneous.
     */ 
    boolean reqGattServerRemoveService(int srvcType, in ParcelUuid srvcUuid);

    /** 
     * Request to remove all registered Gatt services.
     * <br>This is an asynchronous call: it will return immediately, and clients should register 
     * and implement callback functions {@link INfCallbackGattServer#onGattServerServiceDeleted onGattServerServiceDeleted} 
     * to be notified of services deleted.
     *    
     *
     * @return true to indicate the operation is successful, or false erroneous.
     */ 
    boolean reqGattServerClearServices();

    /** 
     * Request to start or finish advertisement broadcast
     * 
     * <br>This is an asynchronous call: it will return immediately, and clients should register 
     * and implement callback functions {@link INfCallbackGattServer#onGattServerStateChanged onGattServerStateChanged} 
     * to be notified of subsequent profile state changes.
     * @param listen start listen or not.
     *
     * @return true to indicate the operation is successful, or false erroneous.
     */ 
    boolean reqGattServerListen(boolean listen);

    /** 
     * Request to send response with read and write characteristic request to Gatt clients.
     *  
     * @param address valid Bluetooth MAC address.
     * @param requestId The ID of the request that was received with the callback
     * @param status The status of the request to be sent to the remote devices
     *               Possible values are:
     *      <br>GATT_STATUS_SUCCESS                (int) 0x00
     *      <br>Operation success.
     *
     *      <br>GATT_STATUS_INVALID_HANDLE         (int) 0x01
     *      <br>Invalid Handle
     *
     *      <br>GATT_STATUS_READ_NOT_PERMIT        (int) 0x02
     *      <br>Read Not Permitted
     *
     *      <br>GATT_STATUS_WRITE_NOT_PERMIT       (int) 0x03
     *      <br>Write Not Permitted
     *
     *      <br>GATT_STATUS_INVALID_PDU            (int) 0x04
     *      <br>Invalid PDU
     *
     *      <br>GATT_STATUS_INSUF_AUTHENTICATION   (int) 0x05
     *      <br>Insufficient Authentication
     *
     *      <br>GATT_STATUS_REQ_NOT_SUPPORTED      (int) 0x06
     *      <br>Request Not Supported
     *
     *      <br>GATT_STATUS_INVALID_OFFSET         (int) 0x07
     *      <br>Invalid Offset
     *
     *      <br>GATT_STATUS_INSUF_AUTHORIZATION    (int) 0x08
     *      <br>Insufficient Authorization
     *
     *      <br>GATT_STATUS_PREPARE_Q_FULL         (int) 0x09
     *      <br>Prepare Queue Full
     *
     *      <br>GATT_STATUS_NOT_FOUND              (int) 0x0a
     *      <br>Attribute Not Found
     *
     *      <br>GATT_STATUS_NOT_LONG               (int) 0x0b
     *      <br>Attribute Not Long
     *
     *      <br>GATT_STATUS_INSUF_KEY_SIZE         (int) 0x0c
     *      <br>Insufficient Encryption Key Size
     *
     *      <br>GATT_STATUS_INVALID_ATTR_LEN       (int) 0x0d
     *      <br>Invalid Attribute Value Length
     *
     *      <br>GATT_STATUS_ERR_UNLIKELY           (int) 0x0e
     *      <br>Unlikely Error
     *
     *      <br>GATT_STATUS_INSUF_ENCRYPTION       (int) 0x0f
     *      <br>Insufficient Encryption
     *
     *      <br>GATT_STATUS_UNSUPPORT_GRP_TYPE     (int) 0x10
     *      <br>Unsupported Group Type.
     *
     *      <br>GATT_STATUS_INSUF_RESOURCE         (int) 0x11
     *      <br>Insufficient Resources
     *
     * @param offset Value offset for partial read/write response
     * @param value The value of the attribute that was read/written (optional)
     *
     * @return true to indicate the operation is successful, or false erroneous.
     */ 
    boolean reqGattServerSendResponse(String address, int requestId,
                      int status, int offset, in byte[] value);

    /** 
     * Request to send characteristic notification values to Gatt clients.
     *  
     * @param address valid Bluetooth MAC address.
     * @param srvcType service type
     * @param srvcUuid service UUID
     * @param charUuid characteristic UUID
     * @param confirm true to request confirmation from the client (indication),
     *                false to send a notification
     * @param value The value to notify remote device.
     *
     * @return true to indicate the operation is successful, or false erroneous.
     */                   
    boolean reqGattServerSendNotification(String address, int srvcType,
                                 in ParcelUuid srvcUuid,
                                 in ParcelUuid charUuid,
                                 boolean confirm, in byte[] value);

    /** 
     * Get GATT added service UUID list.
     *
     * @return Added Gatt service UUID list
     */ 
    List<ParcelUuid> getGattAddedGattServiceUuidList();

    /** 
     * Get GATT added characteristic UUID list.
     *
     * @param srvcUuid service UUID
     *
     * @return Added Gatt characteristic UUID list
     */ 
    List<ParcelUuid> getGattAddedGattCharacteristicUuidList(in ParcelUuid srvcUuid);

    /** 
     * Get GATT added descriptor UUID list.
     *
     * @param srvcUuid service UUID
     * @param charUuid characteristic UUID
     *
     * @return Added Gatt descriptor UUID list
     */ 
    List<ParcelUuid> getGattAddedGattDescriptorUuidList(in ParcelUuid srvcUuid, in ParcelUuid charUuid);
    

}