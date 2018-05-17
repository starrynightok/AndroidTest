package com.wanji.jdbluetooth.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.wanji.jdbluetooth.R;
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

public class RestartInfoActivity extends AppCompatActivity {

    ArrayList<HashMap<String, Object>> restartinfoListListData = new ArrayList<HashMap<String, Object>>();
    ListView restartinfoList;
    SimpleAdapter listItemAdapter;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.restartinfo_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        BluetoothClient bc = BluetoothClient.getCurInstance();
        AlertDialog.Builder builder;
        if (bc == null) {
            CommonFunctions.Msg(RestartInfoActivity.this, "请连接蓝牙");
        } else {
            byte[] buf;
            switch (item.getItemId()) {
                case R.id.restartinfo_query_item:
                    buf = Package.getPackage_07();
                    if (bc.sendData(buf)) {
                        ;
                    } else {
                        CommonFunctions.Msg(RestartInfoActivity.this, "发送失败");
                    }
                    break;
                default:
            }
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restartinfo);

        restartinfoList = (ListView)findViewById(R.id.restartinfo);
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
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("restartinfo_time", "--重启时间--");
        map.put("restartinfo_code", "--重启类型--");
        restartinfoListListData.add(map);

        //适配器
        listItemAdapter = new SimpleAdapter(RestartInfoActivity.this,
                restartinfoListListData,
                R.layout.item_restartinfo,
                new String[]{"restartinfo_time", "restartinfo_code"},
                new int[]{R.id.restartinfo_time, R.id.restartinfo_code});
        restartinfoList.setAdapter(listItemAdapter);
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
                    if (buf[2] == 0x07) {
                        List<String[]> restartInfo = Package.parsePackage_07(buf);
                        setControls(restartInfo);
                        CommonFunctions.Msg(RestartInfoActivity.this, "查询成功");
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    CommonFunctions.Msg(RestartInfoActivity.this, "操作失败");
                }
            }
        }
    };

    private void setControls(List<String[]> restartInfo) {
        restartinfoListListData.clear();
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("restartinfo_time", "--重启时间--");
        map.put("restartinfo_code", "--重启类型--");
        restartinfoListListData.add(map);
        //存储数据的数组列表
        for (int i = 0; i < restartInfo.size(); i++) {
            map = new HashMap<String, Object>();
            map.put("restartinfo_time", restartInfo.get(i)[0]);
            map.put("restartinfo_code", restartInfo.get(i)[1]);
            restartinfoListListData.add(map);
        }
        //适配器
        listItemAdapter = new SimpleAdapter(RestartInfoActivity.this,
                restartinfoListListData,
                R.layout.item_restartinfo,
                new String[]{"restartinfo_time", "restartinfo_code"},
                new int[]{R.id.restartinfo_time, R.id.restartinfo_code});
        restartinfoList.setAdapter(listItemAdapter);
    }

    public void restartinfo_query(View v)
    {
        BluetoothClient bc = BluetoothClient.getCurInstance();
        if (bc == null) {
            CommonFunctions.Msg(RestartInfoActivity.this, "请连接蓝牙");
        } else {
            byte[] buf = Package.getPackage_07();
            if (bc.sendData(buf)) {
                ;
            } else {
                CommonFunctions.Msg(RestartInfoActivity.this, "发送失败");
            }
        }
    }
}
