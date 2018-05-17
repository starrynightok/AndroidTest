package com.wanji.jdbluetooth.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.wanji.jdbluetooth.R;
import com.wanji.jdbluetooth.beans.HDDTU;
import com.wanji.jdbluetooth.beans.SpinnerData;
import com.wanji.jdbluetooth.bluetooth.BluetoothClient;
import com.wanji.jdbluetooth.global.GlobalVariables;
import com.wanji.jdbluetooth.tools.CommonFunctions;
import com.wanji.jdbluetooth.tools.Package;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/5/11.
 */

public class HdDtuParamSerialInfoActivity extends AppCompatActivity {
    @Bind(R.id.BaudRate_activity_hddtu_param_netinfo)
    Spinner mBaudRate;
    @Bind(R.id.Debug_activity_hddtu_param_systeminfo)
    Spinner mDebug;
    @Bind(R.id.RdpType_activity_hddtu_param_systeminfo)
    Spinner mRdpType;
    @Bind(R.id.RdpDownType_activity_hddtu_param_systeminfo)
    Spinner mRdpDownType;
    @Bind(R.id.getsysteminfo_activity_dtuhd_param_systeminfo)
    Button mGetSystemInfo;
    @Bind(R.id.setsysteminfo_activity_dtuhd_param_systeminfo)
    Button mSetSystemInfo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dtuhd_param_serialinfo);
        ButterKnife.bind(this);

        initView();

        //蓝牙收数广播
        BluetoothClient bc = BluetoothClient.getCurInstance();
        if (bc != null) {
            //设置解析方式
            bc.setParseType(GlobalVariables.PACKAGE_PARSE_TYPE_DTU_HD);
        }
        GlobalVariables.localBroadcastManager = LocalBroadcastManager.getInstance(this);
        IntentFilter mSocketFilter = new IntentFilter();
        mSocketFilter.addAction(GlobalVariables.SOCKET_BUFFER_BROADCAST);
        //注册广播
        GlobalVariables.localBroadcastManager.registerReceiver(mSocketReceiver, mSocketFilter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (GlobalVariables.localBroadcastManager != null) {
            GlobalVariables.localBroadcastManager.unregisterReceiver(mSocketReceiver);//注销广播
        }
    }

    private void initView() {
        mSetSystemInfo.setEnabled(false);//禁用设置按钮

        Bundle bundle = getIntent().getExtras();

        List<SpinnerData> lst = new ArrayList<>();
        lst.add(new SpinnerData("0", "115200"));
        lst.add(new SpinnerData("1", "57600"));
        lst.add(new SpinnerData("2", "38400"));
        lst.add(new SpinnerData("3", "19200"));
        lst.add(new SpinnerData("4", "9600"));
        lst.add(new SpinnerData("5", "4800"));
        lst.add(new SpinnerData("6", "2400"));
        lst.add(new SpinnerData("7", "1200"));
        lst.add(new SpinnerData("8", "600"));
        lst.add(new SpinnerData("9", "300"));
        ArrayAdapter<SpinnerData> adapter = new ArrayAdapter<SpinnerData>(HdDtuParamSerialInfoActivity.this,
                android.R.layout.simple_spinner_item, lst);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mBaudRate.setAdapter(adapter);

        lst = new ArrayList<>();
        lst.add(new SpinnerData("0", "OFF"));
        lst.add(new SpinnerData("1", "INFO"));
        lst.add(new SpinnerData("2", "DEBUG"));
        adapter = new ArrayAdapter<SpinnerData>(HdDtuParamSerialInfoActivity.this,
                android.R.layout.simple_spinner_item, lst);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mDebug.setAdapter(adapter);

        lst = new ArrayList<>();
        lst.add(new SpinnerData("0", "DIS"));
        lst.add(new SpinnerData("1", "EN"));
        adapter = new ArrayAdapter<SpinnerData>(HdDtuParamSerialInfoActivity.this,
                android.R.layout.simple_spinner_item, lst);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mRdpType.setAdapter(adapter);

        lst = new ArrayList<>();
        lst.add(new SpinnerData("0", "NONE"));
        lst.add(new SpinnerData("1", "SMS"));
        lst.add(new SpinnerData("2", "GPRS"));
        lst.add(new SpinnerData("3", "SMS+GPRS"));
        adapter = new ArrayAdapter<SpinnerData>(HdDtuParamSerialInfoActivity.this,
                android.R.layout.simple_spinner_item, lst);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mRdpDownType.setAdapter(adapter);
    }

    /**
     * 广播接收器
     */
    private BroadcastReceiver mSocketReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (GlobalVariables.SOCKET_BUFFER_BROADCAST.equals(action)) {
                String value = intent.getStringExtra(GlobalVariables.SOCKET_BUFFER_DATA);//提取数据
                if (value.contains("AT+GETPARAM?")) {
                    //返回：获取全部参数
                    Log.d("HdDtuParamSerialInfo", "获取全部参数");
                    //参数装载
                    ShowParam(value);
                }else if(value.contains("AT+SETPARAM") && value.contains("AT+SAVEPARAM\r\n\r\nOK")){
                    //返回：参数设置成功
                    Log.d("HdDtuParamSerialInfo", "设置参数成功");
                    CommonFunctions.Msg(HdDtuParamSerialInfoActivity.this, "设置成功");
                }else if(value.contains("AT+SETPARAM") && value.contains("AT+SAVEPARAM\r\n\r\nERROR")){
                    //返回：参数设置失败
                    Log.d("HdDtuParamSerialInfo", "设置参数失败");
                    CommonFunctions.Msg(HdDtuParamSerialInfoActivity.this, "设置失败");
                }
            }
        }
    };

    /**
     * 参数显示
     *
     * @param value
     */
    private void ShowParam(String value) {
        HDDTU dtu = new HDDTU();

        //串口配置
        String strTmp = value.substring(value.indexOf("RTUPBR=") + 7);//串口波特率
        dtu.getHDDTUParam().ComBaudRate = strTmp.substring(0, strTmp.indexOf("\r\n"));
        strTmp = value.substring(value.indexOf("OPPDIO=") + 7);//调试信息
        dtu.getHDDTUParam().DebugInfo = strTmp.substring(0, strTmp.indexOf("\r\n"));
        strTmp = value.substring(value.indexOf("RTUDTUP=") + 8);//启用RDP协议
        dtu.getHDDTUParam().RDPSwitch = strTmp.substring(0, strTmp.indexOf("\r\n"));
        strTmp = value.substring(value.indexOf("RTUDTUDOWNP=") + 12);//启用RDP下行协议
        dtu.getHDDTUParam().GPRSProtocal = strTmp.substring(0, strTmp.indexOf("\r\n"));

        CommonFunctions.setSpinnerItemSelectedByText(mBaudRate, dtu.getHDDTUParam().ComBaudRate);
        CommonFunctions.setSpinnerItemSelectedByText(mDebug, dtu.getHDDTUParam().DebugInfo);
        CommonFunctions.setSpinnerItemSelectedByText(mRdpType, dtu.getHDDTUParam().RDPSwitch);
        CommonFunctions.setSpinnerItemSelectedByText(mRdpDownType, dtu.getHDDTUParam().GPRSProtocal);

        mSetSystemInfo.setEnabled(true);//启用设置按钮

        CommonFunctions.Msg(this, "查询成功");
    }

    @OnClick({R.id.getsysteminfo_activity_dtuhd_param_systeminfo, R.id.setsysteminfo_activity_dtuhd_param_systeminfo})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.getsysteminfo_activity_dtuhd_param_systeminfo:
                BluetoothClient bc = BluetoothClient.getCurInstance();
                if (bc == null) {
                    CommonFunctions.Msg(HdDtuParamSerialInfoActivity.this, "请连接蓝牙");
                } else {
                    byte[] buf = Package.getAllParam_DtuHD();
                    if (bc.sendData(buf)) {
                        ;
                    } else {
                        CommonFunctions.Msg(HdDtuParamSerialInfoActivity.this, "发送失败");
                    }
                }
                break;
            case R.id.setsysteminfo_activity_dtuhd_param_systeminfo:
                new AlertDialog.Builder(this)
                        .setTitle("设置")
                        .setMessage("确认设置？")
                        .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                BluetoothClient bcSet = BluetoothClient.getCurInstance();
                                if (bcSet == null) {
                                    CommonFunctions.Msg(HdDtuParamSerialInfoActivity.this, "请连接蓝牙");
                                } else {
                                    HashMap<String, String> hm = new HashMap<>();
                                    hm.put("RTUPBR", mBaudRate.getSelectedItem().toString());
                                    hm.put("OPPDIO", mDebug.getSelectedItem().toString());
                                    hm.put("RTUDTUP", mRdpType.getSelectedItem().toString());
                                    hm.put("RTUDTUDOWNP", mRdpDownType.getSelectedItem().toString());
                                    byte[] buf = Package.setDtuParam(hm);
                                    if (bcSet.sendData(buf)) {
                                        ;
                                    } else {
                                        CommonFunctions.Msg(HdDtuParamSerialInfoActivity.this, "发送失败");
                                    }
                                }
                            }
                        })
                        .setNegativeButton("取消", null)
                        .show();
                break;
        }
    }
}
