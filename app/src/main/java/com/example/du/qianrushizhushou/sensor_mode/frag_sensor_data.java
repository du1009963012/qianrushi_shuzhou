package com.example.du.qianrushizhushou.sensor_mode;


import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.du.qianrushizhushou.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * A simple {@link Fragment} subclass.
 */
public class frag_sensor_data extends Fragment implements SensorEventListener {
    private View view;
    private Sensor_jihe sensor_data;
    private TextView textView;
    private ImageButton back_button;
    private SensorManager mSensorManager;
    public frag_sensor_data() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.frag_sensor_data, container, false);
        EventBus.getDefault().register(this);
        textView=view.findViewById(R.id.sensor_mode_data);
        back_button=view.findViewById(R.id.back_button);
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
        mSensorManager= (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        mSensorManager.registerListener(this,sensor_data.getSensor(),SensorManager.SENSOR_DELAY_NORMAL);
        return view;

    }
    @Subscribe(threadMode = ThreadMode.MAIN,sticky = true)
    public void EvenBus_Msg(Sensor_jihe sensorJihe){
        Log.d("dbug", "EvenBus_Msg:数据收取 "+sensorJihe.getSensor_name());
        this.sensor_data=sensorJihe;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mSensorManager.unregisterListener(this);
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        String sensor_str;
        sensor_str="名称："+event.sensor.getName()+"\n";
        sensor_str+="类型代号："+event.sensor.getType()+"\n";
       // sensor_str+="类型名称："+event.sensor.getStringType()+"\n";
        sensor_str+="供应商:"+event.sensor.getVendor()+"\n";
     //   sensor_str+="最大采集频率："+event.sensor.getMaxDelay()+"\n";
        sensor_str+="最小采集频率："+event.sensor.getMinDelay()+"\n";
        sensor_str+="功耗："+event.sensor.getPower()+"\n";
        sensor_str+="版本："+event.sensor.getVersion()+"\n";
        sensor_str+="分辨率："+event.sensor.getResolution()+"\n";
        //     sensor_str+="传感器输入模式："+event.sensor.getReportingMode()+"\n";
        for(int i=0;i<event.values.length;i++){
            sensor_str+="参数"+i+"："+event.values[i]+"\n";
        }
        //  Log.d("sensor_data", "onSensorChanged: "+sensor_str);
        textView.setText(sensor_str);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
