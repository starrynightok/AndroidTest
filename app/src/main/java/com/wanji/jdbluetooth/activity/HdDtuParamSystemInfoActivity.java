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
import android.widget.Button;
import android.widget.EditText;

import com.wanji.jdbluetooth.R;
import com.wanji.jdbluetooth.beans.HDDTU;
import com.wanji.jdbluetooth.bluetooth.BluetoothClient;
import com.wanji.jdbluetooth.global.GlobalVariables;
import com.wanji.jdbluetooth.tools.CommonFunctions;
import com.wanji.jdbluetooth.tools.Package;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/5/10.
 */

public class HdDtuParamSystemInfoActivity extends AppCompatActivity {
    @Bind(R.id.programver_activity_hddtu_param_systeminfo)
    EditText mSoftwareVer;
    @Bind(R.id.hardwarever_activity_hddtu_param_systeminfo)
    EditText mHardwareVer;
    @Bind(R.id.serialno_activity_hddtu_param_systeminfo)
    EditText mSerialNo;
    @Bind(R.id.phoneno_activity_hddtu_param_systeminfo)
    EditText mPhoneNo;
    @Bind(R.id.getsysteminfo_activity_dtuhd_param_systeminfo)
    Button mGetSystemInfo;
    @Bind(R.id.setsysteminfo_activity_dtuhd_param_systeminfo)
    Button mSetSystemInfo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dtuhd_param_systeminfo);
        ButterKnife.bind(this);

        initView();

        //蓝牙收数广播
        BluetoothClient bc = BluetoothClient.getCurInstance();
        if(bc != null){
            //设置解析方式
            bc.setParseType(GlobalVariables.PACKAGE_PARSE_TYPE_DTU_HD);
        }
        GlobalVariables.localBroadcastManager = LocalBroadcastManager.getInstance(this);
        IntentFilter mSocketFilter = new IntentFilter();
        mSocketFilter.addAction(GlobalVariables.SOCKET_BUFFER_BROADCAST);
        //注册广播
        GlobalVariables.localBroadcastManager.registerReceiver(mSocketReceiver, mSocketFilter);
    }

    private void initView() {
        mSetSystemInfo.setEnabled(false);//禁用设置按钮

        Bundle bundle = getIntent().getExtras();
        mSoftwareVer.setText(bundle.getString("SoftwareVer"));
        mHardwareVer.setText(bundle.getString("HardwareVer"));
        mSerialNo.setText(bundle.getString("SerialNo"));
        mPhoneNo.setText(bundle.getString("PhoneID"));
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
                    Log.d("HdDtuParamSystemInfo", "获取全部参数");
                    //参数装载
                    ShowParam(value);
                }else if(value.contains("AT+SETPARAM") && value.contains("AT+SAVEPARAM\r\n\r\nOK")){
                    //返回：参数设置成功
                    Log.d("HdDtuParamSystemInfo", "设置参数成功");
                    CommonFunctions.Msg(HdDtuParamSystemInfoActivity.this, "设置成功");
                }else if(value.contains("AT+SETPARAM") && value.contains("AT+SAVEPARAM\r\n\r\nERROR")){
                    //返回：参数设置失败
                    Log.d("HdDtuParamSystemInfo", "设置参数失败");
                    CommonFunctions.Msg(HdDtuParamSystemInfoActivity.this, "设置失败");
                }
            }
        }
    };

    /**
     * 参数显示
     * @param value
     */
    private void ShowParam(String value) {
        HDDTU dtu = new HDDTU();

        //系统信息
        String strTmp = value.substring(value.indexOf("SPHV=") + 5);//硬件版本
        dtu.getHDDTUParam().HardwareVer = strTmp.substring(0, strTmp.indexOf("\r\n"));
        strTmp = value.substring(value.indexOf("SPSV=") + 5);//软件版本
        dtu.getHDDTUParam().SoftwareVer = strTmp.substring(0, strTmp.indexOf("\r\n"));
        strTmp = value.substring(value.indexOf("SPPID=") + 6);//序列号
        dtu.getHDDTUParam().SerialNo = strTmp.substring(0, strTmp.indexOf("\r\n"));
        strTmp = value.substring(value.indexOf("OPPDTUI=") + 8);//身份识别码
        dtu.getHDDTUParam().PhoneID = strTmp.substring(0, strTmp.indexOf("\r\n"));

        mHardwareVer.setText(dtu.getHDDTUParam().HardwareVer);
        mSoftwareVer.setText(dtu.getHDDTUParam().SoftwareVer);
        mSerialNo.setText(dtu.getHDDTUParam().SerialNo);
        mPhoneNo.setText(dtu.getHDDTUParam().PhoneID);

        mSetSystemInfo.setEnabled(true);//启用设置按钮
        CommonFunctions.Msg(this, "查询成功");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(GlobalVariables.localBroadcastManager != null) {
            GlobalVariables.localBroadcastManager.unregisterReceiver(mSocketReceiver);
        }
    }

    @OnClick({R.id.getsysteminfo_activity_dtuhd_param_systeminfo, R.id.setsysteminfo_activity_dtuhd_param_systeminfo})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.getsysteminfo_activity_dtuhd_param_systeminfo:
                BluetoothClient bc = BluetoothClient.getCurInstance();
                if (bc == null) {
                    CommonFunctions.Msg(HdDtuParamSystemInfoActivity.this, "请连接蓝牙");
                } else {
                    byte[] buf = Package.getAllParam_DtuHD();
                    if (bc.sendData(buf)) {
                        ;
                    } else {
                        CommonFunctions.Msg(HdDtuParamSystemInfoActivity.this, "发送失败");
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
                                    CommonFunctions.Msg(HdDtuParamSystemInfoActivity.this, "请连接蓝牙");
                                } else {
                                    HashMap<String, String> hm = new HashMap<>();
                                    hm.put("OPPDTUI", mPhoneNo.getText().toString());
                                    byte[] buf = Package.setDtuParam(hm);
                                    if (bcSet.sendData(buf)) {
                                        ;
                                    } else {
                                        CommonFunctions.Msg(HdDtuParamSystemInfoActivity.this, "发送失败");
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
