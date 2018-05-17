package com.wanji.jdbluetooth.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.wanji.jdbluetooth.R;
import com.wanji.jdbluetooth.bluetooth.BluetoothClient;
import com.wanji.jdbluetooth.global.GlobalVariables;
import com.wanji.jdbluetooth.tools.CommonFunctions;
import com.wanji.jdbluetooth.tools.Package;

import java.util.HashMap;

/**
 * Created by admin on 2018/2/9.
 */

public class NetParamsActivity extends AppCompatActivity {

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.net_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        BluetoothClient bc = BluetoothClient.getCurInstance();
        AlertDialog.Builder builder;
        if (bc == null) {
            CommonFunctions.Msg(NetParamsActivity.this, "请连接蓝牙");
        } else {
            byte[] buf;
            switch (item.getItemId()) {
                case R.id.net_params_query_item:
                    buf = Package.getPackage_36(0xCC, null);
                    if (bc.sendData(buf)) {
                        ;
                    } else {
                        CommonFunctions.Msg(NetParamsActivity.this, "发送失败");
                    }
                    break;
                case R.id.net_params_set_item:
                    builder = new AlertDialog.Builder(NetParamsActivity.this);
                    builder.setTitle("提示");
                    builder.setMessage("确定设置？");
                    builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            try {
                                byte[] buf = Package.getPackage_36(0xDD, getControls());
                                BluetoothClient bc = BluetoothClient.getCurInstance();
                                if (bc.sendData(buf)) {
                                    ;
                                } else {
                                    CommonFunctions.Msg(NetParamsActivity.this, "发送失败");
                                }
                            } catch (Exception ex) {
                                CommonFunctions.Msg(NetParamsActivity.this, "参数异常");
                            }
                        }
                    });
                    builder.setNegativeButton("否", null);
                    builder.show();
                    break;
                default:
            }
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_net_param);

        initView();

        //蓝牙收数广播
        BluetoothClient bc = BluetoothClient.getCurInstance();
        if (bc != null)//设置解析方式
        {
            bc.setParseType(0);
        }
        GlobalVariables.localBroadcastManager = LocalBroadcastManager.getInstance(this);
        IntentFilter mSocketFilter = new IntentFilter();
        mSocketFilter.addAction(GlobalVariables.SOCKET_BUFFER_BROADCAST);
        GlobalVariables.localBroadcastManager.registerReceiver(mSocketReceiver, mSocketFilter);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        GlobalVariables.localBroadcastManager.unregisterReceiver(mSocketReceiver);//注销广播
    }

    /**
     * 初始化界面
     */
    public void initView() {

    }


    /**
     * 蓝牙收数接收器
     */
    private BroadcastReceiver mSocketReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (GlobalVariables.SOCKET_BUFFER_BROADCAST.equals(action)) {
                byte[] buf = intent.getByteArrayExtra(GlobalVariables.SOCKET_BUFFER_DATA);
                try{
                    if (buf[2] == 0x36) {
                        Object obj = Package.parsePackage_36(buf);
                        if (obj instanceof HashMap) {
                            setControls((HashMap<String, String>) obj);
                            CommonFunctions.Msg(NetParamsActivity.this, "查询成功");
                        } else {
                            if ((boolean) obj) {
                                CommonFunctions.Msg(NetParamsActivity.this, "设置成功");
                            }
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    CommonFunctions.Msg(NetParamsActivity.this, "操作失败");
                }
            }
        }
    };

    private void setControls(HashMap<String, String> hm) {
        EditText et = (EditText) findViewById(R.id.new_verticalLaserIp);
        et.setText(hm.get("new_verticalLaserIp"));
        et = (EditText) findViewById(R.id.new_verticalLaserPort);
        et.setText(hm.get("new_verticalLaserPort"));
        et = (EditText) findViewById(R.id.new_tiltLaserIp);
        et.setText(hm.get("new_tiltLaserIp"));
        et = (EditText) findViewById(R.id.new_tiltLaserPort);
        et.setText(hm.get("new_tiltLaserPort"));
        et = (EditText) findViewById(R.id.new_controlIp);
        et.setText(hm.get("new_controlIp"));
        et = (EditText) findViewById(R.id.new_controlMask);
        et.setText(hm.get("new_controlMask"));
        et = (EditText) findViewById(R.id.new_controlPort);
        et.setText(hm.get("new_controlPort"));
        et = (EditText) findViewById(R.id.new_controlNetGate);
        et.setText(hm.get("new_controlNetGate"));
        et = (EditText) findViewById(R.id.new_controlMac);
        et.setText(hm.get("new_controlMac"));
        et = (EditText) findViewById(R.id.new_verticalLaserDisconnCount);
        et.setText(hm.get("new_verticalLaserDisconnCount"));
        et = (EditText) findViewById(R.id.new_tiltLaserDisconnCount);
        et.setText(hm.get("new_tiltLaserDisconnCount"));
        et = (EditText) findViewById(R.id.new_verticalLaserValidCount);
        et.setText(hm.get("new_verticalLaserValidCount"));
        et = (EditText) findViewById(R.id.new_tiltLaserValidCount);
        et.setText(hm.get("new_tiltLaserValidCount"));
        et = (EditText) findViewById(R.id.new_exCode);
        et.setText(hm.get("new_exCode"));
    }

    private HashMap<String, String> getControls() {
        HashMap<String, String> hm = new HashMap<String, String>();
        EditText et = (EditText) findViewById(R.id.new_verticalLaserIp);
        hm.put("new_verticalLaserIp", et.getText().toString());
        et = (EditText) findViewById(R.id.new_verticalLaserPort);
        hm.put("new_verticalLaserPort", et.getText().toString());
        et = (EditText) findViewById(R.id.new_tiltLaserIp);
        hm.put("new_tiltLaserIp", et.getText().toString());
        et = (EditText) findViewById(R.id.new_tiltLaserPort);
        hm.put("new_tiltLaserPort", et.getText().toString());
        et = (EditText) findViewById(R.id.new_controlIp);
        hm.put("new_controlIp", et.getText().toString());
        et = (EditText) findViewById(R.id.new_controlMask);
        hm.put("new_controlMask", et.getText().toString());
        et = (EditText) findViewById(R.id.new_controlPort);
        hm.put("new_controlPort", et.getText().toString());
        et = (EditText) findViewById(R.id.new_controlNetGate);
        hm.put("new_controlNetGate", et.getText().toString());
        et = (EditText) findViewById(R.id.new_controlMac);
        hm.put("new_controlMac", et.getText().toString());
        et = (EditText) findViewById(R.id.new_verticalLaserDisconnCount);
        hm.put("new_verticalLaserDisconnCount", et.getText().toString());
        et = (EditText) findViewById(R.id.new_tiltLaserDisconnCount);
        hm.put("new_tiltLaserDisconnCount", et.getText().toString());
        et = (EditText) findViewById(R.id.new_verticalLaserValidCount);
        hm.put("new_verticalLaserValidCount", et.getText().toString());
        et = (EditText) findViewById(R.id.new_tiltLaserValidCount);
        hm.put("new_tiltLaserValidCount", et.getText().toString());
        et = (EditText) findViewById(R.id.new_exCode);
        hm.put("new_exCode", et.getText().toString());
        return hm;
    }

    public void net_params_query(View v)
    {
        BluetoothClient bc = BluetoothClient.getCurInstance();
        if (bc == null) {
            CommonFunctions.Msg(NetParamsActivity.this, "请连接蓝牙");
        } else {
            byte[] buf = Package.getPackage_36(0xCC, null);
            if (bc.sendData(buf)) {
                ;
            } else {
                CommonFunctions.Msg(NetParamsActivity.this, "发送失败");
            }
        }
    }

    public void net_params_set(View v)
    {
        BluetoothClient bc = BluetoothClient.getCurInstance();
        if (bc == null) {
            CommonFunctions.Msg(NetParamsActivity.this, "请连接蓝牙");
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(NetParamsActivity.this);
            builder.setTitle("提示");
            builder.setMessage("确定设置？");
            builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    try {
                        byte[] buf = Package.getPackage_36(0xDD, getControls());
                        BluetoothClient bc = BluetoothClient.getCurInstance();
                        if (bc.sendData(buf)) {
                            ;
                        } else {
                            CommonFunctions.Msg(NetParamsActivity.this, "发送失败");
                        }
                    } catch (Exception ex) {
                        CommonFunctions.Msg(NetParamsActivity.this, "参数异常");
                    }
                }
            });
            builder.setNegativeButton("否", null);
            builder.show();
        }
    }
}
