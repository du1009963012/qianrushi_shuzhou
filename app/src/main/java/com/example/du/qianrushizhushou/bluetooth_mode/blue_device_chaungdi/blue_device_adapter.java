package com.example.du.qianrushizhushou.bluetooth_mode.blue_device_chaungdi;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.Handler;

public class blue_device_adapter {
    public Handler getHandler() {
        return handler;
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    private Handler handler;
    public BluetoothDevice getMdevice() {
        return mdevice;
    }

    public void setMdevice(BluetoothDevice mdevice) {
        this.mdevice = mdevice;
    }

    public BluetoothAdapter getMadapter() {
        return madapter;
    }

    public void setMadapter(BluetoothAdapter madapter) {
        this.madapter = madapter;
    }

    private BluetoothDevice mdevice;
    private BluetoothAdapter madapter;

}
