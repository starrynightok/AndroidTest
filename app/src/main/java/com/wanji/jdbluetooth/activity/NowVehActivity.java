package com.wanji.jdbluetooth.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.wanji.jdbluetooth.R;
import com.wanji.jdbluetooth.bluetooth.BluetoothClient;
import com.wanji.jdbluetooth.global.GlobalVariables;
import com.wanji.jdbluetooth.tools.CommonFunctions;
import com.wanji.jdbluetooth.tools.Package;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by admin on 2018/2/9.
 */

public class NowVehActivity extends AppCompatActivity {

    ArrayList<HashMap<String, Object>> nowvehListListData = new ArrayList<HashMap<String, Object>>();
    ListView nowvehList;
    SimpleAdapter listItemAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nowveh);

        nowvehList = (ListView) findViewById(R.id.nowveh);
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
        map.put("nowveh_time", "接收时间");
        map.put("nowveh_lane_type_speed", "车道/车型/车速(km/h)");
        map.put("nowveh_len_width_height", "长/宽/高(mm)");
        nowvehListListData.add(map);

        //适配器
        listItemAdapter = new SimpleAdapter(NowVehActivity.this,
                nowvehListListData,
                R.layout.item_nowveh,
                new String[]{"nowveh_time", "nowveh_lane_type_speed", "nowveh_len_width_height"},
                new int[]{R.id.nowveh_time, R.id.nowveh_lane_type_speed, R.id.nowveh_len_width_height});
        nowvehList.setAdapter(listItemAdapter);
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
                    if (buf[2] == 0x09) {
                        HashMap<String, String> hm = Package.parsePackage_09(buf);
                        setControls(hm);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    };

    private void setControls(HashMap<String, String> hm) {
        HashMap<String,Object> newAdd = new HashMap<String,Object>();
        newAdd.put("nowveh_time",hm.get("nowveh_time"));
        newAdd.put("nowveh_lane_type_speed",hm.get("nowveh_lane_type_speed"));
        newAdd.put("nowveh_len_width_height",hm.get("nowveh_len_width_height"));
        nowvehListListData.add(1,newAdd);
        listItemAdapter.notifyDataSetChanged();
    }
}
