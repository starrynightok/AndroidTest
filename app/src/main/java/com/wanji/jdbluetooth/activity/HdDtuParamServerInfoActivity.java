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
 * Created by Administrator on 2018/5/11.
 */

public class HdDtuParamServerInfoActivity extends AppCompatActivity {
    @Bind(R.id.ServerDomain_activity_hddtu_param_netinfo)
    EditText mServerDomain;
    @Bind(R.id.ServerPort_activity_hddtu_param_systeminfo)
    EditText mServerPort;
    @Bind(R.id.getsysteminfo_activity_dtuhd_param_systeminfo)
    Button mGetSystemInfo;
    @Bind(R.id.setsysteminfo_activity_dtuhd_param_systeminfo)
    Button mSetSystemInfo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dtuhd_param_serverinfo);
        ButterKnife.bind(this);

        initView();

        //蓝牙收数广播
        BluetoothClient bc = BluetoothClient.getCurInstance();
        if (bc != null) {
            bc.setParseType(GlobalVariables.PACKAGE_PARSE_TYPE_DTU_HD);//设置解析方式
        }
        GlobalVariables.localBroadcastManager = LocalBroadcastManager.getInstance(this);
        IntentFilter mSocketFilter = new IntentFilter();
        mSocketFilter.addAction(GlobalVariables.SOCKET_BUFFER_BROADCAST);
        //注册收数广播
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
        mServerDomain.setText(bundle.getString("ManageDscYM"));
        mServerPort.setText(bundle.getString("ManageDscPort"));
    }

    @OnClick({R.id.getsysteminfo_activity_dtuhd_param_systeminfo, R.id.setsysteminfo_activity_dtuhd_param_systeminfo})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.getsysteminfo_activity_dtuhd_param_systeminfo:
                BluetoothClient bc = BluetoothClient.getCurInstance();
                if (bc == null) {
                    CommonFunctions.Msg(HdDtuParamServerInfoActivity.this, "请连接蓝牙");
                } else {
                    byte[] buf = Package.getAllParam_DtuHD();
                    if (bc.sendData(buf)) {
                        ;
                    } else {
                        CommonFunctions.Msg(HdDtuParamServerInfoActivity.this, "发送失败");
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
                                    CommonFunctions.Msg(HdDtuParamServerInfoActivity.this, "请连接蓝牙");
                                } else {
                                    HashMap<String, String> hm = new HashMap<>();
                                    hm.put("WPDOMAIN", mServerDomain.getText().toString());
                                    hm.put("WPDESTPORT", mServerPort.getText().toString());
                                    byte[] buf = Package.setDtuParam(hm);
                                    if (bcSet.sendData(buf)) {
                                        ;
                                    } else {
                                        CommonFunctions.Msg(HdDtuParamServerInfoActivity.this, "发送失败");
                                    }
                                }
                            }
                        })
                        .setNegativeButton("取消", null)
                        .show();
                break;
        }
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
                    Log.d("HdDtuParamServerInfo", "获取全部参数");
                    //参数装载
                    ShowParam(value);
                }else if(value.contains("AT+SETPARAM") && value.contains("AT+SAVEPARAM\r\n\r\nOK")){
                    //返回：参数设置成功
                    Log.d("HdDtuParamServerInfo", "设置参数成功");
                    CommonFunctions.Msg(HdDtuParamServerInfoActivity.this, "设置成功");
                }else if(value.contains("AT+SETPARAM") && value.contains("AT+SAVEPARAM\r\n\r\nERROR")){
                    //返回：参数设置失败
                    Log.d("HdDtuParamServerInfo", "设置参数失败");
                    CommonFunctions.Msg(HdDtuParamServerInfoActivity.this, "设置失败");
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

        //宏电管理平台
        String strTmp = value.substring(value.indexOf("WPDOMAIN=") + 9);//平台域名
        dtu.getHDDTUParam().ManageDscYM = strTmp.substring(0, strTmp.indexOf("\r\n"));
        strTmp = value.substring(value.indexOf("WPIPADDR=") + 9);//平台IP
        dtu.getHDDTUParam().ManageDscIP = strTmp.substring(0, strTmp.indexOf("\r\n"));
        strTmp = value.substring(value.indexOf("WPDESTPORT=") + 11);//平台端口
        dtu.getHDDTUParam().ManageDscPort = strTmp.substring(0, strTmp.indexOf("\r\n"));

        mServerDomain.setText(dtu.getHDDTUParam().ManageDscYM);
        mServerPort.setText(dtu.getHDDTUParam().ManageDscPort);

        mSetSystemInfo.setEnabled(true);//启用设置按钮
        CommonFunctions.Msg(this, "查询成功");
    }
}
