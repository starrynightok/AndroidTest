package com.wanji.jdbluetooth.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
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

public class LaserStateActivity extends AppCompatActivity {

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.laser_state_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        BluetoothClient bc = BluetoothClient.getCurInstance();
        if (bc == null) {
            CommonFunctions.Msg(LaserStateActivity.this, "请连接蓝牙");
        } else {
            byte[] buf;
            switch (item.getItemId()) {
                case R.id.laser_state_query_item:
                    try {
                        EditText et = (EditText) findViewById(R.id.laserIp);
                        String ip = et.getText().toString();
                        et = (EditText) findViewById(R.id.laserPort);
                        int port = Integer.parseInt(et.getText().toString());
                        buf = Package.getPackage_5F(ip, port);
                        if (bc.sendData(buf)) {
                            ;
                        } else {
                            CommonFunctions.Msg(LaserStateActivity.this, "发送失败");
                        }
                    } catch (Exception ex) {
                        CommonFunctions.Msg(LaserStateActivity.this, "目标激光器IP或端口错误");
                    }
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
        setContentView(R.layout.activity_laser_state);

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
                    if (buf.length > 20 && buf[11] == (byte)0xAF) {
                        HashMap<String, String> hm = Package.parsePackage_AF(buf);
                        setControls(hm);
                        CommonFunctions.Msg(LaserStateActivity.this, "查询成功");
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    CommonFunctions.Msg(LaserStateActivity.this, "操作失败");
                }
            }
        }
    };

    private void setControls(HashMap<String, String> hm) {
        EditText et = (EditText) findViewById(R.id.laserVersionId);
        et.setText(hm.get("laserVersionId"));
        et = (EditText) findViewById(R.id.countTimeState);
        et.setText(hm.get("countTimeState"));
        et = (EditText) findViewById(R.id.heatState);
        et.setText(hm.get("heatState"));
        et = (EditText) findViewById(R.id.laserLaunchState);
        et.setText(hm.get("laserLaunchState"));
        et = (EditText) findViewById(R.id.emState);
        et.setText(hm.get("emState"));
        et = (EditText) findViewById(R.id.apdTemp);
        et.setText(hm.get("apdTemp"));
        et = (EditText) findViewById(R.id.emTemp);
        et.setText(hm.get("emTemp"));
        et = (EditText) findViewById(R.id.apdHighPress);
        et.setText(hm.get("apdHighPress"));
        et = (EditText) findViewById(R.id.airPress);
        et.setText(hm.get("airPress"));
        et = (EditText) findViewById(R.id.zeroPlusWidth);
        et.setText(hm.get("zeroPlusWidth"));
        et = (EditText) findViewById(R.id.humidity);
        et.setText(hm.get("humidity"));
    }

    public void laser_state_query(View v)
    {
        BluetoothClient bc = BluetoothClient.getCurInstance();
        try {
            EditText et = (EditText) findViewById(R.id.laserIp);
            String ip = et.getText().toString();
            et = (EditText) findViewById(R.id.laserPort);
            int port = Integer.parseInt(et.getText().toString());
            byte[] buf = Package.getPackage_5F(ip, port);
            if (bc.sendData(buf)) {
                ;
            } else {
                CommonFunctions.Msg(LaserStateActivity.this, "发送失败");
            }
        } catch (Exception ex) {
            CommonFunctions.Msg(LaserStateActivity.this, "目标激光器IP或端口错误");
        }
    }
}
