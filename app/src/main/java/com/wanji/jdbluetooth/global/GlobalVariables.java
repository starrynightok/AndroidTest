package com.wanji.jdbluetooth.global;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.support.v4.content.LocalBroadcastManager;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by admin on 2018/2/8.
 */

public class GlobalVariables {
    public static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    public static LocalBroadcastManager localBroadcastManager;
    public static final String SOCKET_BUFFER_BROADCAST = "com.wanji.jdbluetooth.BluetoothClient.BROADCAST";
    public static final String SOCKET_BUFFER_DATA = "com.wanji.jdbluetooth.BluetoothClient.DATA";

    public static final int PACKAGE_PARSE_TYPE_CONTROL = 0;
    public static final int PACKAGE_PARSE_TYPE_LASER = 1;
    public static final int PACKAGE_PARSE_TYPE_DTU_HD = 2;

    public static final int SOCKET_SEND_DATA_LENGTH = 256;

    public static BluetoothAdapter gBluetoothAdapter = null;
    public static List<BluetoothDevice> gListBluetoothDevice = new ArrayList<BluetoothDevice>();
    public static BluetoothDevice gCurBluetoothDevice = null;
    public static int gCurSdk = 0;
}
