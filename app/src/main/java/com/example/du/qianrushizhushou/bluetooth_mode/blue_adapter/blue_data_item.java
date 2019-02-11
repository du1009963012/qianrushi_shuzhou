package com.example.du.qianrushizhushou.bluetooth_mode.blue_adapter;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;

public class blue_data_item {
    private BluetoothDevice device;

    public BluetoothAdapter getBluetoothAdapter() {
        return bluetoothAdapter;
    }

    public void setBluetoothAdapter(BluetoothAdapter bluetoothAdapter) {
        this.bluetoothAdapter = bluetoothAdapter;
    }

    private BluetoothAdapter bluetoothAdapter;
    public BluetoothServerSocket getBluetoothServerSocket() {
        return bluetoothServerSocket;
    }

    public void setBluetoothServerSocket(BluetoothServerSocket bluetoothServerSocket) {
        this.bluetoothServerSocket = bluetoothServerSocket;
    }

    public BluetoothSocket getBluetoothSocket() {
        return bluetoothSocket;
    }

    public void setBluetoothSocket(BluetoothSocket bluetoothSocket) {
        this.bluetoothSocket = bluetoothSocket;
    }

    private BluetoothServerSocket bluetoothServerSocket;
    private BluetoothSocket bluetoothSocket;
    public BluetoothDevice getDevice() {
        return device;
    }

    public void setDevice(BluetoothDevice device) {
        this.device = device;
    }
}
