package com.example.du.qianrushizhushou.sensor_mode;


import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.du.qianrushizhushou.R;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class sensor_fragment extends Fragment implements sensor_rv_adapter.OnItemClickListener {
    private View view;
    private frag_sensor_data sensorFragmentData;
    private SensorManager sensorManager;
    private List<Sensor_jihe> mdata=new ArrayList<>();
    private RecyclerView recyclerView;
    private List<Fragment> fragmentList=new ArrayList<>();
    public sensor_fragment() {
        // Required empty public constructor
    }
    public void init_data(){
        mdata.clear();
        sensorManager=(SensorManager)getContext().getSystemService(Context.SENSOR_SERVICE);
        List<Sensor> sensorsList=sensorManager.getSensorList(Sensor.TYPE_ALL);
        for (Sensor sensor:sensorsList){
            Sensor_jihe sensor_jihe=new Sensor_jihe();
            sensor_jihe.setSensor(sensor);
            sensor_jihe.setSensor_name(sensor.getName());
            Log.d("dbug",sensor_jihe.getSensor_name());
            mdata.add(sensor_jihe);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.sensor_fragment, container, false);
        recyclerView=view.findViewById(R.id.sensor_RV);
        init_data();
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        sensor_rv_adapter adapter=new sensor_rv_adapter();
        adapter.sensor_rv_adapter(mdata);
        adapter.setmItemClickListener(this);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(this.getActivity(),DividerItemDecoration.VERTICAL));
        return view;
    }

    @Override
    public void onItemClick(int position) {
        Sensor_jihe sensor_jihe=mdata.get(position);
        if(sensorFragmentData!=null)sensorFragmentData.onDestroy();
        sensorFragmentData=new frag_sensor_data();
        getActivity().getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.mian_fragment_content,sensorFragmentData).commit();
        Log.d("dbug","onItemClick事件触发");
        EventBus.getDefault().postSticky(sensor_jihe);
        Log.d("dbug","enevtBus发送"+sensor_jihe.getSensor_name());
    }
}
