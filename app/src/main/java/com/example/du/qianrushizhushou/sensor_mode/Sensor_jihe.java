package com.example.du.qianrushizhushou.sensor_mode;

import android.hardware.Sensor;

public class Sensor_jihe {
    public Sensor getSensor() {
        return sensor;
    }

    public void setSensor(Sensor sensor) {
        this.sensor = sensor;
    }

    public String getSensor_name() {
        return sensor_name;
    }

    public void setSensor_name(String sensor_name) {
        this.sensor_name = sensor_name;
    }

    private Sensor sensor;
    private String sensor_name;
}
