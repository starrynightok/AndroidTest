package com.wanji.jdbluetooth.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.wanji.jdbluetooth.R;
import com.wanji.jdbluetooth.bluetooth.BluetoothClient;
import com.wanji.jdbluetooth.global.GlobalVariables;
import com.wanji.jdbluetooth.tools.CommonFunctions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class SearchActivity extends AppCompatActivity {

    public static final int MY_PERMISSION_REQUEST_CONSTANT = 0;
    public static boolean btn_bluetooth_flag = true;
    ProgressDialog dia_bluetooth;
    ArrayList<HashMap<String, Object>> listBluetoothListData = new ArrayList<HashMap<String, Object>>();
    ListView bluetoothList;
    SimpleAdapter listItemAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        //6.0以后的如果需要利用本机查找周围的wifi和蓝牙设备，需要在配置文件中申请两个权限ACCESS_COARSE_LOCATION ACCESS_FINE_LOCATION
        if (Build.VERSION.SDK_INT >= 6.0) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSION_REQUEST_CONSTANT);
        }
        bluetoothList = (ListView) findViewById(R.id.bluetooths);
        bluetoothList.setOnItemClickListener(new ItemClick());

        //蓝牙广播
        IntentFilter filter = new IntentFilter();
        //发现设备
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        //设备连接状态改变
        filter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        //蓝牙设备状态改变
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        //蓝牙设备开始搜索
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        //蓝牙设备搜索结束
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        registerReceiver(mBluetoothReceiver, filter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mBluetoothReceiver);//注销广播
    }

    /*
     * item上的OnClick事件
     */
    public final class ItemClick implements AdapterView.OnItemClickListener {

        public void onItemClick(AdapterView<?> parent, View arg1, int position, long id) {
            if (BluetoothClient.getCurInstance() != null) {
                CommonFunctions.Msg(SearchActivity.this, "请断开蓝牙");
                return;
            }
            GlobalVariables.gCurBluetoothDevice = GlobalVariables.gListBluetoothDevice.get((int) id);
            try {
                dia_bluetooth = ProgressDialog.show(SearchActivity.this, "提示", "正在连接，请稍后...");
                BluetoothSocket socket = null;
                GlobalVariables.gCurSdk = Build.VERSION.SDK_INT;
                if (GlobalVariables.gCurSdk >= 10) {
                    socket = GlobalVariables.gCurBluetoothDevice.createInsecureRfcommSocketToServiceRecord(GlobalVariables.MY_UUID);
                } else {
                    socket = GlobalVariables.gCurBluetoothDevice.createRfcommSocketToServiceRecord(GlobalVariables.MY_UUID);
                }
                BluetoothClient bc = BluetoothClient.getInstance(_handler, socket, getApplicationContext());
                bc.connSocket();
            } catch (Exception ex) {
                CommonFunctions.Msg(SearchActivity.this, "连接蓝牙异常");
            }
        }
    }

    /**
     * 蓝牙按钮点击事件
     *
     * @param v
     */
    public void bluetoothClick(View v) {

        if (btn_bluetooth_flag) {
            GlobalVariables.gBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            if (GlobalVariables.gBluetoothAdapter == null) {
                CommonFunctions.Msg(SearchActivity.this, "当前设备不支持蓝牙");
                return;
            }
            if (!GlobalVariables.gBluetoothAdapter.isEnabled()) {
                CommonFunctions.Msg(SearchActivity.this, "请开启蓝牙");
                return;
            }


            GlobalVariables.gBluetoothAdapter.startDiscovery();

            //定时器延迟一段时间停止搜索蓝牙
            final Timer timer = new Timer();
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    GlobalVariables.gBluetoothAdapter.cancelDiscovery();
                    timer.cancel();
                }
            };
            timer.schedule(task, 5000, 1000);

            Button btn = (Button) findViewById(R.id.btn_bluetooth);
            btn_bluetooth_flag = false;
            btn.setText("断开蓝牙");
        } else {
            GlobalVariables.gListBluetoothDevice.clear();
            listBluetoothListData.clear();
            if (listItemAdapter != null) {
                listItemAdapter.notifyDataSetChanged();
                bluetoothList.setAdapter(listItemAdapter);
            }
            BluetoothClient bc = BluetoothClient.getCurInstance();
            if (bc != null) {
                bc.disconnSocket();
            }
            Button btn = (Button) findViewById(R.id.btn_bluetooth);
            btn_bluetooth_flag = true;
            btn.setText("搜索蓝牙");
        }
    }

    /**
     * 注册蓝牙广播
     */
    private BroadcastReceiver mBluetoothReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {//每扫描到一个设备，系统都会发送此广播。
                //获取蓝牙设备
                BluetoothDevice scanDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
              //  if(scanDevice.getName() != null) {
                    GlobalVariables.gListBluetoothDevice.add(scanDevice);
             //   }
            } else if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
                dia_bluetooth = ProgressDialog.show(SearchActivity.this, "提示", "搜索中...");
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                dia_bluetooth.cancel();
                CommonFunctions.Msg(SearchActivity.this, "搜索完毕");

                //存储数据的数组列表
                for (int i = 0; i < GlobalVariables.gListBluetoothDevice.size(); i++) {
                    HashMap<String, Object> map = new HashMap<String, Object>();
                    map.put("bluetooth_image", R.drawable.png_bluetooth);
                    map.put("bluetooth_name", "设备名称：" + GlobalVariables.gListBluetoothDevice.get(i).getName());
                    map.put("bluetooth_mac", "MAC地址：" + GlobalVariables.gListBluetoothDevice.get(i).getAddress());
                    listBluetoothListData.add(map);
                }
                //适配器
                listItemAdapter = new SimpleAdapter(SearchActivity.this,
                        listBluetoothListData,
                        R.layout.item_bluetooth,
                        new String[]{"bluetooth_image", "bluetooth_name", "bluetooth_mac"},
                        new int[]{R.id.bluetooth_image, R.id.bluetooth_name, R.id.bluetooth_mac});
                bluetoothList.setAdapter(listItemAdapter);
            }
        }
    };

    Handler _handler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            // 刷新信息
            try {
                String msgId = msg.getData().getString("msgId");
                if ("0".equals(msgId)) {//连接成功
                    CommonFunctions.Msg(SearchActivity.this, msg.getData().getString("msgContent").toString());
                    dia_bluetooth.cancel();
                    Intent i = new Intent(SearchActivity.this, MainActivity.class);
                    startActivity(i);
                } else if ("1".equals(msgId)) {//连接失败
                    CommonFunctions.Msg(SearchActivity.this, msg.getData().getString("msgContent").toString());
                    dia_bluetooth.cancel();
                    if (BluetoothClient.getCurInstance() != null) {
                        BluetoothClient.getCurInstance().disconnSocket();
                    }
                } else if ("2".equals(msgId))//断开连接
                {
                    CommonFunctions.Msg(SearchActivity.this, msg.getData().getString("msgContent").toString());
                } else if ("3".equals(msgId))//重连成功
                {
                    CommonFunctions.Msg(SearchActivity.this, msg.getData().getString("msgContent").toString());
                }
            } catch (Exception e) {
            }
        }
    };

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {

        switch (requestCode) {
            case MY_PERMISSION_REQUEST_CONSTANT: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //permission granted!
                }
                return;
            }
        }
    }

    public void onBackPressed() {
        new AlertDialog.Builder(this).setTitle("确定退出吗？")
                .setIcon(android.R.drawable.ic_dialog_info)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 点击“确认”后的操作
                        SearchActivity.this.finish();

                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 点击“返回”后的操作,这里不设置没有任何操作
                    }
                }).show();
    }
}
