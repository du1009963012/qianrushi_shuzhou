package com.example.du.qianrushizhushou.bluetooth_mode;


import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.du.qianrushizhushou.R;
import com.example.du.qianrushizhushou.bluetooth_mode.blue_adapter.blue_adapter;
import com.example.du.qianrushizhushou.bluetooth_mode.blue_adapter.blue_data_item;
import com.example.du.qianrushizhushou.bluetooth_mode.blue_device_chaungdi.blue_device_adapter;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;


/**
 * A simple {@link Fragment} subclass.
 */
public class blue_setting_content extends DialogFragment {
    private View view;
    private boolean DBUG=true;
    private boolean flag=true;
    private List<blue_data_item> bind_list_data=new ArrayList<>();
    private List<blue_data_item> binded_list_data=new ArrayList<>();
    private blue_adapter bind_list_adapter,binded_list_adapter;
    private BluetoothAdapter mbluetoothAdapter;
    private BluetoothDevice bluetoothDevice;
    private BluetoothSocket bluetoothSocket;
    private BluetoothServerSocket bluetoothServerSocket;
    private RecyclerView bind_list_RV;
    private blue_data_item Even_blue_data;
    private Button upDevice_btn;
    public blue_setting_content() {
        // Required empty public constructor
    }
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch(msg.what){
                case 1: Toast.makeText(getDialog().getContext(),"配对成功",Toast.LENGTH_SHORT).show();
                        if(DBUG)Log.d("dbug","handleMessage:步骤一");
                        blue_device_adapter dataItem=new blue_device_adapter();
                        dataItem.setMadapter(mbluetoothAdapter);
                        dataItem.setMdevice((BluetoothDevice) msg.obj);
                        EventBus.getDefault().post(dataItem);
                        getDialog().cancel();
                break;

            }

        }

    };


    private final BroadcastReceiver mRecevier=new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                String action=intent.getAction();
                if(BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)){//开始搜寻周围设备
                    if(DBUG){
                    Log.d("dbug","开始搜寻");
                    Toast.makeText(getActivity(),"开始搜寻。。。",Toast.LENGTH_SHORT).show();
                    }
                    bind_list_data.clear();
                 }

                if(BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)){//搜寻完成
                    if(DBUG){
                         Log.d("dbug","搜索完成！");
                         if(flag){
                         Toast.makeText(getActivity(),"搜索完成",Toast.LENGTH_SHORT).show();
                             bind_list_adapter=new blue_adapter(bind_list_data);
                             bind_list_RV.setAdapter(bind_list_adapter);
                             bind_list_adapter.setOnItemClickListener(new blue_adapter.OnItemClickListener() {
                                 @Override
                                 public void onItemClick(View view, final int position) {
                                     Log.d("dbug",bind_list_data.get(position).getDevice().getName());
                                     if(mbluetoothAdapter.isDiscovering())mbluetoothAdapter.cancelDiscovery();
                                    if(bluetoothDevice==null)bluetoothDevice=mbluetoothAdapter.getRemoteDevice(bind_list_data.get(position).getDevice().getAddress());
//
                                     Method createBondMethos= null;
                                     try {
                                         createBondMethos = bluetoothDevice.getClass().getMethod("createBond");
                                         Boolean returnValue= null;
                                         try {
                                             returnValue = (Boolean)createBondMethos.invoke(bluetoothDevice);
                                         } catch (IllegalAccessException e) {
                                             e.printStackTrace();
                                         } catch (InvocationTargetException e) {
                                             e.printStackTrace();
                                         }
                                         returnValue.booleanValue();

                                         Toast.makeText(getDialog().getContext(),"配对当中，请等待。。。",Toast.LENGTH_SHORT).show();
                                         new Thread(){


                                             Message  message=handler.obtainMessage(1,bind_list_data.get(position).getDevice());
                                              @Override
                                             public void run() {
                                                 while(true){
                                                     if(bluetoothDevice.getBondState()==BluetoothDevice.BOND_BONDED)break;
                                                 }
                                                 message.sendToTarget();
                                             }
                                         }.start();

                                     } catch (NoSuchMethodException e) {
                                         e.printStackTrace();
                                     }

                                 }
                             });
                         }
                     }

                }
                if(BluetoothDevice.ACTION_FOUND.equals(action)){
                    BluetoothDevice device=intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    blue_data_item dataItem;
                    if(DBUG){
                        Log.d("dbug","找到设备："+device.getName());
                        if(flag)
                        Toast.makeText(getActivity(),"找到设备："+device.getName(),Toast.LENGTH_SHORT).show();
                    }

                      if(device.getBondState()!=BluetoothDevice.BOND_BONDED){
                            dataItem=new blue_data_item();
                           dataItem.setDevice(device);
                          bind_list_data.add(dataItem);
                       }

                }

             }

    };
    private void blue_updata(){
        mbluetoothAdapter.startDiscovery();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        bind_list_data.clear();
        binded_list_data.clear();
        if(DBUG) Log.d("dbug","onAttch");
        checkBluetoothAndLocationPermission();
        mbluetoothAdapter=BluetoothAdapter.getDefaultAdapter();
        if(mbluetoothAdapter==null){
            Toast.makeText(getActivity(),"此设备不支持蓝牙",Toast.LENGTH_LONG).show();

            return;
        }
        if(!mbluetoothAdapter.isEnabled()){
            mbluetoothAdapter.enable();
//            Intent intent=new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
//            startActivity(intent);
        }

        while(BluetoothAdapter.getDefaultAdapter().getState()!=BluetoothAdapter.STATE_ON);

        IntentFilter filter=new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        requireActivity().registerReceiver(mRecevier,filter);
        filter=new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        requireActivity().registerReceiver(mRecevier,filter);
        filter=new IntentFilter(BluetoothDevice.ACTION_FOUND);
        requireActivity().registerReceiver(mRecevier,filter);
        if(mbluetoothAdapter.isDiscovering())mbluetoothAdapter.cancelDiscovery();
        mbluetoothAdapter.startDiscovery();


    }

    @TargetApi(Build.VERSION_CODES.M)
    private void checkBluetoothAndLocationPermission(){
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
            return;
        }
        if(getActivity().checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED||getActivity().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION},1);
        }
    }
    /*****************
     * 动态权限获取
     * *****************************/
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1: {
                // 如果请求被取消，则结果数组为空。
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.i("tag","同意申请");
                } else {
                    Log.i("tag","拒绝申请");
                }
                return;
            }
        }


    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (DBUG)Log.d("dbug","create");


    }

    @Override
    public void onStart() {
        super.onStart();
        flag=true;
        if (DBUG)Log.d("dbug","onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        if (DBUG)Log.d("dbug","onResume");


    }

    @Override
    public void onPause() {
        super.onPause();
        flag=false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        flag=false;
       // getActivity().unregisterReceiver(mRecevier);
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        handler.removeMessages(1);
       getContext().unregisterReceiver(mRecevier);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        flag=true;
        if (DBUG)Log.d("dbug","onCreateDialog");
        final AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
        builder.setTitle("设备搜索");
        LayoutInflater inflater=getActivity().getLayoutInflater();
        view=inflater.inflate(R.layout.fragment_blue_setting_content,null);
        builder.setView(view);

        RecyclerView binded_list_RV=view.findViewById(R.id.binded_blueDevice_list);
        bind_list_RV=view.findViewById(R.id.bind_blueDevice_list);
        bind_list_RV.setLayoutManager(new LinearLayoutManager(getActivity()));
        bind_list_RV.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.HORIZONTAL));
      //  bind_list_RV.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL));
        binded_list_RV.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.HORIZONTAL));
     //   binded_list_RV.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL));
        upDevice_btn=view.findViewById(R.id.updata_drvice);
        upDevice_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                blue_updata();

            }
        });

        /************************
         * 设备数据填充
         * ********************/
        Set<BluetoothDevice> pairedDevices=mbluetoothAdapter.getBondedDevices();
        if(pairedDevices.size()>0){
            blue_data_item blueDataItem;
            for(BluetoothDevice device:pairedDevices){
                blueDataItem=new blue_data_item();
                blueDataItem.setDevice(device);
                binded_list_data.add(blueDataItem);
            }
        }
        binded_list_RV.setLayoutManager(new LinearLayoutManager(getActivity()));
        binded_list_adapter=new blue_adapter(binded_list_data);
        binded_list_RV.setAdapter(binded_list_adapter);

        binded_list_adapter.setOnItemClickListener(new blue_adapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                blue_device_adapter blueDeviceAdapter=new blue_device_adapter();
                Toast.makeText(getDialog().getContext(),binded_list_data.get(position).getDevice().getName(),Toast.LENGTH_SHORT).show();
                blueDeviceAdapter.setMadapter(mbluetoothAdapter);
                blueDeviceAdapter.setMdevice(binded_list_data.get(position).getDevice());
                EventBus.getDefault().post(blueDeviceAdapter);
                if(DBUG)Log.d("dbug","发送设备1");
                getDialog().cancel();
            }
        });

        return builder.create();
    }


}
