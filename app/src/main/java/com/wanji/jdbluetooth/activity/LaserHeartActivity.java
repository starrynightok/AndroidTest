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
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.wanji.jdbluetooth.R;
import com.wanji.jdbluetooth.beans.SpinnerData;
import com.wanji.jdbluetooth.bluetooth.BluetoothClient;
import com.wanji.jdbluetooth.global.GlobalVariables;
import com.wanji.jdbluetooth.tools.CommonFunctions;
import com.wanji.jdbluetooth.tools.Package;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by admin on 2018/2/9.
 */

public class LaserHeartActivity extends AppCompatActivity {

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.laser_heart_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        BluetoothClient bc = BluetoothClient.getCurInstance();
        AlertDialog.Builder builder;
        if (bc == null) {
            CommonFunctions.Msg(LaserHeartActivity.this, "请连接蓝牙");
        } else {
            byte[] buf;
            switch (item.getItemId()) {
                case R.id.laser_heart_query_item:
                    try {
                        EditText et = (EditText) findViewById(R.id.mLaserIp);
                        String ip = et.getText().toString();
                        et = (EditText) findViewById(R.id.mLaserPort);
                        int port = Integer.parseInt(et.getText().toString());
                        buf = Package.getPackage_61(0x5F,ip, port);
                        if (bc.sendData(buf)) {
                            ;
                        } else {
                            CommonFunctions.Msg(LaserHeartActivity.this, "发送失败");
                        }
                    } catch (Exception ex) {
                        CommonFunctions.Msg(LaserHeartActivity.this, "目标激光器IP或端口错误");
                    }
                    break;
                case R.id.laser_heart_set_item:
                    builder = new AlertDialog.Builder(LaserHeartActivity.this);
                    builder.setTitle("提示");
                    builder.setMessage("确定设置？");
                    builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            try {
                                EditText et = (EditText) findViewById(R.id.mLaserIp);
                                String ip = et.getText().toString();
                                et = (EditText) findViewById(R.id.mLaserPort);
                                int port = Integer.parseInt(et.getText().toString());
                                Spinner sp = (Spinner) findViewById(R.id.heart);
                                byte[] buf = Package.getPackage_61(Integer.parseInt(((SpinnerData) sp.getSelectedItem()).getValue()), ip, port);
                                BluetoothClient bc = BluetoothClient.getCurInstance();
                                if (bc.sendData(buf)) {
                                    ;
                                } else {
                                    CommonFunctions.Msg(LaserHeartActivity.this, "发送失败");
                                }
                            } catch (Exception ex) {
                                CommonFunctions.Msg(LaserHeartActivity.this, "目标激光器IP或端口错误");
                            }
                        }
                    });
                    builder.setNegativeButton("否", null);
                    builder.show();
                    break;
                case R.id.laser_reset_query_item:
                    try {
                        EditText et = (EditText) findViewById(R.id.mLaserIp);
                        String ip = et.getText().toString();
                        et = (EditText) findViewById(R.id.mLaserPort);
                        int port = Integer.parseInt(et.getText().toString());
                        buf = Package.getPackage_56(ip, port);
                        if (bc.sendData(buf)) {
                            ;
                        } else {
                            CommonFunctions.Msg(LaserHeartActivity.this, "发送失败");
                        }
                    } catch (Exception ex) {
                        CommonFunctions.Msg(LaserHeartActivity.this, "目标激光器IP或端口错误");
                    }
                    break;
                case R.id.laser_reset_clear_item:
                    builder = new AlertDialog.Builder(LaserHeartActivity.this);
                    builder.setTitle("提示");
                    builder.setMessage("确定清零？");
                    builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            try {
                                EditText et = (EditText) findViewById(R.id.mLaserIp);
                                String ip = et.getText().toString();
                                et = (EditText) findViewById(R.id.mLaserPort);
                                int port = Integer.parseInt(et.getText().toString());
                                Spinner sp = (Spinner) findViewById(R.id.heart);
                                byte[] buf = Package.getPackage_57(ip, port);
                                BluetoothClient bc = BluetoothClient.getCurInstance();
                                if (bc.sendData(buf)) {
                                    ;
                                } else {
                                    CommonFunctions.Msg(LaserHeartActivity.this, "发送失败");
                                }
                            } catch (Exception ex) {
                                CommonFunctions.Msg(LaserHeartActivity.this, "目标激光器IP或端口错误");
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
        setContentView(R.layout.activity_laser_heart);

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
        List<SpinnerData> lst = new ArrayList<SpinnerData>();
        SpinnerData c = new SpinnerData("82", "关闭");
        lst.add(c);
        c = new SpinnerData("81", "开启");
        lst.add(c);
        ArrayAdapter<SpinnerData> Adapter = new ArrayAdapter<SpinnerData>(LaserHeartActivity.this,
                android.R.layout.simple_spinner_item, lst);
        Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner sp = (Spinner) findViewById(R.id.heart);
        sp.setAdapter(Adapter);
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
                    if (buf.length > 20 && buf[11] == (byte) 0xB1) {
                        Object obj = Package.parsePackage_B1(buf);
                        if (obj instanceof HashMap) {
                            setControls_0((HashMap<String, String>) obj);
                            CommonFunctions.Msg(LaserHeartActivity.this, "查询成功");
                        } else {
                            if ((boolean) obj) {
                                CommonFunctions.Msg(LaserHeartActivity.this, "设置成功");
                            } else {
                                CommonFunctions.Msg(LaserHeartActivity.this, "设置失败");
                            }
                        }
                    } else if (buf.length > 20 && buf[11] == (byte) 0xA6) {
                        HashMap<String, String> hm = Package.parsePackage_A6(buf);
                        setControls_1(hm);
                        CommonFunctions.Msg(LaserHeartActivity.this, "查询成功");
                    } else if (buf.length > 20 && buf[11] == (byte) 0xA7) {
                        if (Package.parsePackage_A7(buf)) {
                            CommonFunctions.Msg(LaserHeartActivity.this, "清零成功");
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    CommonFunctions.Msg(LaserHeartActivity.this, "操作失败");
                }
            }
        }
    };

    private void setControls_0(HashMap<String, String> hm) {
        Spinner sp = (Spinner) findViewById(R.id.heart);
        CommonFunctions.setSpinnerItemSelectedByValue(sp, hm.get("heart"));
    }

    private void setControls_1(HashMap<String, String> hm) {
        EditText et = (EditText) findViewById(R.id.softResetCount);
        et.setText(hm.get("softResetCount"));
        et = (EditText) findViewById(R.id.independentResetCount);
        et.setText(hm.get("independentResetCount"));
        et = (EditText) findViewById(R.id.windowResetCount);
        et.setText(hm.get("windowResetCount"));
        et = (EditText) findViewById(R.id.powerResetCount);
        et.setText(hm.get("powerResetCount"));
        et = (EditText) findViewById(R.id.nrstResetCount);
        et.setText(hm.get("nrstResetCount"));
    }

    public void laser_heart_query(View v)
    {
        BluetoothClient bc = BluetoothClient.getCurInstance();
        try {
            EditText et = (EditText) findViewById(R.id.mLaserIp);
            String ip = et.getText().toString();
            et = (EditText) findViewById(R.id.mLaserPort);
            int port = Integer.parseInt(et.getText().toString());
            byte[] buf = Package.getPackage_61(0x5F,ip, port);
            if (bc.sendData(buf)) {
                ;
            } else {
                CommonFunctions.Msg(LaserHeartActivity.this, "发送失败");
            }
        } catch (Exception ex) {
            CommonFunctions.Msg(LaserHeartActivity.this, "目标激光器IP或端口错误");
        }
    }

    public void laser_heart_set(View v)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(LaserHeartActivity.this);
        builder.setTitle("提示");
        builder.setMessage("确定设置？");
        builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                try {
                    EditText et = (EditText) findViewById(R.id.mLaserIp);
                    String ip = et.getText().toString();
                    et = (EditText) findViewById(R.id.mLaserPort);
                    int port = Integer.parseInt(et.getText().toString());
                    Spinner sp = (Spinner) findViewById(R.id.heart);
                    byte[] buf = Package.getPackage_61(Integer.parseInt(((SpinnerData) sp.getSelectedItem()).getValue()), ip, port);
                    BluetoothClient bc = BluetoothClient.getCurInstance();
                    if (bc.sendData(buf)) {
                        ;
                    } else {
                        CommonFunctions.Msg(LaserHeartActivity.this, "发送失败");
                    }
                } catch (Exception ex) {
                    CommonFunctions.Msg(LaserHeartActivity.this, "目标激光器IP或端口错误");
                }
            }
        });
        builder.setNegativeButton("否", null);
        builder.show();
    }

    public void laser_reset_query(View v)
    {
        BluetoothClient bc = BluetoothClient.getCurInstance();
        try {
            EditText et = (EditText) findViewById(R.id.mLaserIp);
            String ip = et.getText().toString();
            et = (EditText) findViewById(R.id.mLaserPort);
            int port = Integer.parseInt(et.getText().toString());
            byte[] buf = Package.getPackage_56(ip, port);
            if (bc.sendData(buf)) {
                ;
            } else {
                CommonFunctions.Msg(LaserHeartActivity.this, "发送失败");
            }
        } catch (Exception ex) {
            CommonFunctions.Msg(LaserHeartActivity.this, "目标激光器IP或端口错误");
        }
    }

    public void laser_reset_clear(View v)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(LaserHeartActivity.this);
        builder.setTitle("提示");
        builder.setMessage("确定清零？");
        builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                try {
                    EditText et = (EditText) findViewById(R.id.mLaserIp);
                    String ip = et.getText().toString();
                    et = (EditText) findViewById(R.id.mLaserPort);
                    int port = Integer.parseInt(et.getText().toString());
                    Spinner sp = (Spinner) findViewById(R.id.heart);
                    byte[] buf = Package.getPackage_57(ip, port);
                    BluetoothClient bc = BluetoothClient.getCurInstance();
                    if (bc.sendData(buf)) {
                        ;
                    } else {
                        CommonFunctions.Msg(LaserHeartActivity.this, "发送失败");
                    }
                } catch (Exception ex) {
                    CommonFunctions.Msg(LaserHeartActivity.this, "目标激光器IP或端口错误");
                }
            }
        });
        builder.setNegativeButton("否", null);
        builder.show();
    }
}
