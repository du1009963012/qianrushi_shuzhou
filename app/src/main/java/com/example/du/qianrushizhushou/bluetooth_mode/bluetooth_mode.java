package com.example.du.qianrushizhushou.bluetooth_mode;
import com.example.du.qianrushizhushou.bluetooth_mode.blue_device_chaungdi.blue_device_adapter;
import com.example.du.qianrushizhushou.bluetooth_mode.connect.*;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.du.qianrushizhushou.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Set;
import java.util.UUID;

/**
 * A simple {@link Fragment} subclass.
 */
public class bluetooth_mode extends Fragment {
    private boolean DBUG=true;
    private View view;

    private boolean clict=false;
    private FloatingActionButton blue_jiemian;
    private BluetoothAdapter mBluetoothAdapter;
    private Switch jinzhi_zhuanghuan;
    private Switch zhucong_shezhi_Switch;
    private Switch dingshi_send_switch;
    private Button qingkong_button;
    private Button send_data_button;
    private EditText data_jieshou;
    private EditText data_fasong;
    private BluetoothDevice device;
    private BluetoothSocket bluetoothSocket;
    private BluetoothServerSocket bluetoothServerSocket;
    private blue_setting_content blueSettingContent_fragment;
    private InputStream inputStream;
    private OutputStream outputStream;

    private final int Bluetooth_Read_buffer=1;
    private final int Bluetooth_write_buffer=2;

    private AcceptThread mAcceptThread;
    private ConnectThread mConnectThread;


    private final UUID MY_UUID=UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private final String NAME="blue_sever";



    public Handler handler_blue_main=new Handler(){
        public void handleMessage(Message message) {
            super.handleMessage(message);
            switch (message.what) {
                case Constant.MSG_GOT_DATA:
                   // Toast.makeText(getContext(),"MSG_GOT_DATA",Toast.LENGTH_SHORT).show();
                    data_jieshou.append(message.obj.toString());
           //         showToast("data:" + String.valueOf(message.obj));
                break;
                case Constant.MSG_ERROR:
                    Toast.makeText(getContext(),"发生异常:"+message.obj.toString(),Toast.LENGTH_SHORT).show();
                    clict=false;
              //  showToast("error:" + String.valueOf(message.obj));
                break;
                case Constant.MSG_CONNECTED_TO_SERVER:
                    Toast.makeText(getContext(),"连接到服务端成功",Toast.LENGTH_SHORT).show();
                    clict=true;
                   // showToast("连接到服务端");
                    break;
                case Constant.MSG_GOT_A_CLINET:
                    Toast.makeText(getContext(),"客户端连接成功",Toast.LENGTH_SHORT).show();
                    clict=true;
                    //showToast("找到服务端");
                    break;
            }
        }
    };
    public bluetooth_mode() {
        // Required empty public constructor
    }
    @Subscribe
    public void Evenbus_blue(blue_device_adapter blue_data){
        Log.d("dbug","EvenBus收到数据");
       // device_flag=true;
        this.device=blue_data.getMdevice();
        this.mBluetoothAdapter=blue_data.getMadapter();
        if(!zhucong_shezhi_Switch.isChecked()){
            Log.d("dbug","客户端线程启动");
            if (mConnectThread != null) {
                mConnectThread.cancel();
            }
            mConnectThread = new ConnectThread(device,mBluetoothAdapter, handler_blue_main);
            mConnectThread.start();
        }

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mBluetoothAdapter=BluetoothAdapter.getDefaultAdapter();
    }
    private void initUI(){
        /*****************
         * 显示方式改变：
         * 显示：字符、16进制
         * ***************************/
        jinzhi_zhuanghuan=view.findViewById(R.id.jinzhi_switch);
        jinzhi_zhuanghuan.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            }
        });
        data_fasong=view.findViewById(R.id.fasong_data);
        zhucong_shezhi_Switch=view.findViewById(R.id.sever_or_client);
        zhucong_shezhi_Switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                /*******************
                 * 说明：true为服务器模式，false为客户端模式
                 *
                 * **********************************/
                if(mBluetoothAdapter.isEnabled()){
                    if(isChecked){
                        mAcceptThread = new AcceptThread(mBluetoothAdapter, handler_blue_main);
                        mAcceptThread.start();
                    }else{
                        if(device==null){
                            Toast.makeText(getContext(),"客户端模式下，没有连接的设备。请重新选择！",Toast.LENGTH_LONG).show();
                        }else{
                            if(mConnectThread==null){

                            }else{

                            }
                        }
                    }
                }else{
                    Toast.makeText(getContext()," 请打开蓝牙！",Toast.LENGTH_SHORT).show();
                    zhucong_shezhi_Switch.setChecked(false);
                }

            }
        });
        qingkong_button=view.findViewById(R.id.qingkong_button);
        send_data_button=view.findViewById(R.id.send_data_button);
        /***************
         * 数据发送
         *
         * ****************************/
        send_data_button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(clict){
                    String text;
                    byte[] msg=null;
                    text=data_fasong.getText().toString();
                    msg=text.getBytes();
                    int action=event.getAction();
                    switch(action){
                        case MotionEvent.ACTION_DOWN:{
                            if(zhucong_shezhi_Switch.isChecked()){
                                //服务器模式
                                mAcceptThread.sendData(msg);
                            }else{
                                //客户端模式
                                mConnectThread.sendData(msg);
                            }
                        }break;
                        case MotionEvent.ACTION_UP:{
                            if(zhucong_shezhi_Switch.isChecked()){
                                //服务器模式
                                // mAcceptThread.sendData(null);
                                //客户端模式
                            }else{

                            }
                        }break;
                    }
                }else{
                    Toast.makeText(getContext(),"设备未连接！",Toast.LENGTH_LONG).show();
                }


                return false;
            }
        });
        dingshi_send_switch=view.findViewById(R.id.dingshi_fasong_switch);
        dingshi_send_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(clict){
                    if(isChecked){

                    }else{

                    }
                }else{
                    Toast.makeText(getContext(),"请打开蓝牙",Toast.LENGTH_SHORT).show();
                    dingshi_send_switch.setChecked(false);
                }
            }
        });
        data_jieshou=view.findViewById(R.id.data_jiehsou);

        blue_jiemian=view.findViewById(R.id.blue_switch);
        blue_jiemian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(blueSettingContent_fragment==null)blueSettingContent_fragment=new blue_setting_content();
                blueSettingContent_fragment.show(getFragmentManager(),"blue_setting");
            }
        });
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment_bluetooth_mode, container, false);

        EventBus.getDefault().register(this);
        initUI();

        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        return view;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);

    }

    @Override
    public void onResume() {
        super.onResume();
        if(device!=null){

        }


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        clict=false;
        handler_blue_main.removeMessages(Constant.MSG_GOT_DATA);
        handler_blue_main.removeMessages(Constant.MSG_CONNECTED_TO_SERVER);
        handler_blue_main.removeMessages(Constant.MSG_ERROR);
        handler_blue_main.removeMessages(Constant.MSG_FINISH_LISTENING);
        handler_blue_main.removeMessages(Constant.MSG_GOT_A_CLINET);
        handler_blue_main.removeMessages(Constant.MSG_START_LISTENING);
        EventBus.getDefault().unregister(this);
      if (mAcceptThread != null) {
           mAcceptThread.cancel();
      }
       if (mConnectThread != null) {
         mConnectThread.cancel();
       }
    }


    /*************************
     * 建立蓝牙套接字，实现服务器端
     * 时间：2018.12.3
     * *************************/


}
