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

public class LaserSysActivity extends AppCompatActivity {

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.laser_sys_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        BluetoothClient bc = BluetoothClient.getCurInstance();
        AlertDialog.Builder builder;
        if (bc == null) {
            CommonFunctions.Msg(LaserSysActivity.this, "请连接蓝牙");
        } else {
            byte[] buf;
            switch (item.getItemId()) {
                case R.id.laser_sys_query_item:
                    try {
                        EditText et = (EditText) findViewById(R.id.mLaserIp);
                        String ip = et.getText().toString();
                        et = (EditText) findViewById(R.id.mLaserPort);
                        int port = Integer.parseInt(et.getText().toString());
                        buf = Package.getPackage_54(ip, port);
                        if (bc.sendData(buf)) {
                            ;
                        } else {
                            CommonFunctions.Msg(LaserSysActivity.this, "发送失败");
                        }
                    } catch (Exception ex) {
                        CommonFunctions.Msg(LaserSysActivity.this, "目标激光器IP或端口错误");
                    }
                    break;
                case R.id.laser_sys_set_item:
                    builder = new AlertDialog.Builder(LaserSysActivity.this);
                    builder.setTitle("提示");
                    builder.setMessage("确定设置？");
                    builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            try {
                                EditText et = (EditText) findViewById(R.id.mLaserIp);
                                String ip = et.getText().toString();
                                et = (EditText) findViewById(R.id.mLaserPort);
                                int port = Integer.parseInt(et.getText().toString());
                                byte[] buf = Package.getPackage_53(getControls(), ip, port);
                                BluetoothClient bc = BluetoothClient.getCurInstance();
                                if (bc.sendData(buf)) {
                                    ;
                                } else {
                                    CommonFunctions.Msg(LaserSysActivity.this, "发送失败");
                                }
                            } catch (Exception ex) {
                                CommonFunctions.Msg(LaserSysActivity.this, "目标激光器IP或端口错误");
                            }
                        }
                    });
                    builder.setNegativeButton("否", null);
                    builder.show();

                    break;
                case R.id.laser_sys_restart_item:
                    builder = new AlertDialog.Builder(LaserSysActivity.this);
                    builder.setTitle("提示");
                    builder.setMessage("确定重启？");
                    builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            try {
                                EditText et = (EditText) findViewById(R.id.mLaserIp);
                                String ip = et.getText().toString();
                                et = (EditText) findViewById(R.id.mLaserPort);
                                int port = Integer.parseInt(et.getText().toString());
                                byte[] buf = Package.getPackage_88(ip, port);
                                BluetoothClient bc = BluetoothClient.getCurInstance();
                                if (bc.sendData(buf)) {
                                    ;
                                } else {
                                    CommonFunctions.Msg(LaserSysActivity.this, "发送失败");
                                }
                            } catch (Exception ex) {
                                CommonFunctions.Msg(LaserSysActivity.this, "目标激光器IP或端口错误");
                            }
                        }
                    });
                    builder.setNegativeButton("否", null);
                    builder.show();
                    break;
                case R.id.laser_sys_reset_item:
                    builder = new AlertDialog.Builder(LaserSysActivity.this);
                    builder.setTitle("提示");
                    builder.setMessage("确定初始化？");
                    builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            try {
                                EditText et = (EditText) findViewById(R.id.mLaserIp);
                                String ip = et.getText().toString();
                                et = (EditText) findViewById(R.id.mLaserPort);
                                int port = Integer.parseInt(et.getText().toString());
                                byte[] buf = Package.getPackage_5B(ip, port);
                                BluetoothClient bc = BluetoothClient.getCurInstance();
                                if (bc.sendData(buf)) {
                                    ;
                                } else {
                                    CommonFunctions.Msg(LaserSysActivity.this, "发送失败");
                                }
                            } catch (Exception ex) {
                                CommonFunctions.Msg(LaserSysActivity.this, "目标激光器IP或端口错误");
                            }
                        }
                    });
                    builder.setNegativeButton("否", null);
                    builder.show();
                    break;
                default:
                    break;
            }
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laser_sys);

        initView();

        //蓝牙收数广播
        BluetoothClient bc = BluetoothClient.getCurInstance();
        if (bc != null)//设置解析方式
        {
            bc.setParseType(1);
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
                    if (buf.length > 20 && buf[11] == (byte) 0xA4) {
                        HashMap<String, String> hm = Package.parsePackage_A4(buf);
                        setControls(hm);
                        CommonFunctions.Msg(LaserSysActivity.this, "查询成功");
                    } else if (buf.length > 20 && buf[11] == (byte) 0xA0) {
                        CommonFunctions.Msg(LaserSysActivity.this, "设置失败");
                    } else if (buf.length > 20 && buf[11] == (byte) 0xA3) {
                        CommonFunctions.Msg(LaserSysActivity.this, "设置成功");
                    } else if (buf.length > 20 && buf[11] == (byte) 0xAB) {
                        CommonFunctions.Msg(LaserSysActivity.this, "初始化成功");
                    } else if (buf.length > 20 && buf[11] == (byte) 0x89) {
                        CommonFunctions.Msg(LaserSysActivity.this, "重启成功");
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    CommonFunctions.Msg(LaserSysActivity.this, "操作失败");
                }
            }
        }
    };

    private void setControls(HashMap<String, String> hm) {
        EditText et = (EditText) findViewById(R.id.laserIp);
        et.setText(hm.get("laserIp"));
        et = (EditText) findViewById(R.id.laserPort);
        et.setText(hm.get("laserPort"));
        et = (EditText) findViewById(R.id.serverIp);
        et.setText(hm.get("serverIp"));
        et = (EditText) findViewById(R.id.serverPort);
        et.setText(hm.get("serverPort"));
        et = (EditText) findViewById(R.id.mask);
        et.setText(hm.get("mask"));
        et = (EditText) findViewById(R.id.mac);
        et.setText(hm.get("mac"));
        et = (EditText) findViewById(R.id.gateway);
        et.setText(hm.get("gateway"));
        et = (EditText) findViewById(R.id.circle);
        et.setText(hm.get("circle"));
        et = (EditText) findViewById(R.id.zeroStartPos);
        et.setText(hm.get("zeroStartPos"));
        et = (EditText) findViewById(R.id.zeroEndPos);
        et.setText(hm.get("zeroEndPos"));
        et = (EditText) findViewById(R.id.zeroMin);
        et.setText(hm.get("zeroMin"));
        et = (EditText) findViewById(R.id.scanningPrecision);
        et.setText(hm.get("scanningPrecision"));
        et = (EditText) findViewById(R.id.scanningNum);
        et.setText(hm.get("scanningNum"));
        et = (EditText) findViewById(R.id.scanningStartPos);
        et.setText(hm.get("scanningStartPos"));
        et = (EditText) findViewById(R.id.scanningEndPos);
        et.setText(hm.get("scanningEndPos"));
        et = (EditText) findViewById(R.id.zeroLength);
        et.setText(hm.get("zeroLength"));
        et = (EditText) findViewById(R.id.zeroAvgNum);
        et.setText(hm.get("zeroAvgNum"));
        et = (EditText) findViewById(R.id.apdDecay);
        et.setText(hm.get("apdDecay"));
        et = (EditText) findViewById(R.id.apdBreakdownVoltage);
        et.setText(hm.get("apdBreakdownVoltage"));
        et = (EditText) findViewById(R.id.apdBreakdownTemp);
        et.setText(hm.get("apdBreakdownTemp"));

    }

    private HashMap<String, String> getControls() {
        HashMap<String, String> res = new HashMap<String, String>();
        EditText et = (EditText) findViewById(R.id.laserIp);
        res.put("laserIp", et.getText().toString());
        et = (EditText) findViewById(R.id.laserPort);
        res.put("laserPort", et.getText().toString());
        et = (EditText) findViewById(R.id.serverIp);
        res.put("serverIp", et.getText().toString());
        et = (EditText) findViewById(R.id.serverPort);
        res.put("serverPort", et.getText().toString());
        et = (EditText) findViewById(R.id.mask);
        res.put("mask", et.getText().toString());
        et = (EditText) findViewById(R.id.mac);
        res.put("mac", et.getText().toString());
        et = (EditText) findViewById(R.id.gateway);
        res.put("gateway", et.getText().toString());
        et = (EditText) findViewById(R.id.circle);
        res.put("circle", et.getText().toString());
        et = (EditText) findViewById(R.id.zeroStartPos);
        res.put("zeroStartPos", et.getText().toString());
        et = (EditText) findViewById(R.id.zeroEndPos);
        res.put("zeroEndPos", et.getText().toString());
        et = (EditText) findViewById(R.id.zeroMin);
        res.put("zeroMin", et.getText().toString());
        et = (EditText) findViewById(R.id.scanningPrecision);
        res.put("scanningPrecision", et.getText().toString());
        et = (EditText) findViewById(R.id.scanningNum);
        res.put("scanningNum", et.getText().toString());
        et = (EditText) findViewById(R.id.scanningStartPos);
        res.put("scanningStartPos", et.getText().toString());
        et = (EditText) findViewById(R.id.scanningEndPos);
        res.put("scanningEndPos", et.getText().toString());
        et = (EditText) findViewById(R.id.zeroLength);
        res.put("zeroLength", et.getText().toString());
        et = (EditText) findViewById(R.id.zeroAvgNum);
        res.put("zeroAvgNum", et.getText().toString());
        et = (EditText) findViewById(R.id.apdDecay);
        res.put("apdDecay", et.getText().toString());
        et = (EditText) findViewById(R.id.apdBreakdownVoltage);
        res.put("apdBreakdownVoltage", et.getText().toString());
        et = (EditText) findViewById(R.id.apdBreakdownTemp);
        res.put("apdBreakdownTemp", et.getText().toString());

        return res;
    }

    public void laser_sys_query(View v)
    {
        BluetoothClient bc = BluetoothClient.getCurInstance();
        try {
            EditText et = (EditText) findViewById(R.id.mLaserIp);
            String ip = et.getText().toString();
            et = (EditText) findViewById(R.id.mLaserPort);
            int port = Integer.parseInt(et.getText().toString());
            byte[] buf = Package.getPackage_54(ip, port);
            if (bc.sendData(buf)) {
                ;
            } else {
                CommonFunctions.Msg(LaserSysActivity.this, "发送失败");
            }
        } catch (Exception ex) {
            CommonFunctions.Msg(LaserSysActivity.this, "目标激光器IP或端口错误");
        }
    }

    public void laser_sys_set(View v)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(LaserSysActivity.this);
        builder.setTitle("提示");
        builder.setMessage("确定设置？");
        builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                try {
                    EditText et = (EditText) findViewById(R.id.mLaserIp);
                    String ip = et.getText().toString();
                    et = (EditText) findViewById(R.id.mLaserPort);
                    int port = Integer.parseInt(et.getText().toString());
                    byte[] buf = Package.getPackage_53(getControls(), ip, port);
                    BluetoothClient bc = BluetoothClient.getCurInstance();
                    if (bc.sendData(buf)) {
                        ;
                    } else {
                        CommonFunctions.Msg(LaserSysActivity.this, "发送失败");
                    }
                } catch (Exception ex) {
                    CommonFunctions.Msg(LaserSysActivity.this, "目标激光器IP或端口错误");
                }
            }
        });
        builder.setNegativeButton("否", null);
        builder.show();
    }

    public void laser_sys_restart(View v)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(LaserSysActivity.this);
        builder.setTitle("提示");
        builder.setMessage("确定重启？");
        builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                try {
                    EditText et = (EditText) findViewById(R.id.mLaserIp);
                    String ip = et.getText().toString();
                    et = (EditText) findViewById(R.id.mLaserPort);
                    int port = Integer.parseInt(et.getText().toString());
                    byte[] buf = Package.getPackage_88(ip, port);
                    BluetoothClient bc = BluetoothClient.getCurInstance();
                    if (bc.sendData(buf)) {
                        ;
                    } else {
                        CommonFunctions.Msg(LaserSysActivity.this, "发送失败");
                    }
                } catch (Exception ex) {
                    CommonFunctions.Msg(LaserSysActivity.this, "目标激光器IP或端口错误");
                }
            }
        });
        builder.setNegativeButton("否", null);
        builder.show();
    }

    public void laser_sys_reset(View v)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(LaserSysActivity.this);
        builder.setTitle("提示");
        builder.setMessage("确定初始化？");
        builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                try {
                    EditText et = (EditText) findViewById(R.id.mLaserIp);
                    String ip = et.getText().toString();
                    et = (EditText) findViewById(R.id.mLaserPort);
                    int port = Integer.parseInt(et.getText().toString());
                    byte[] buf = Package.getPackage_5B(ip, port);
                    BluetoothClient bc = BluetoothClient.getCurInstance();
                    if (bc.sendData(buf)) {
                        ;
                    } else {
                        CommonFunctions.Msg(LaserSysActivity.this, "发送失败");
                    }
                } catch (Exception ex) {
                    CommonFunctions.Msg(LaserSysActivity.this, "目标激光器IP或端口错误");
                }
            }
        });
        builder.setNegativeButton("否", null);
        builder.show();
    }
}
