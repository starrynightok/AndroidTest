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

public class ExParamActivity extends AppCompatActivity {

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.ex_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        BluetoothClient bc = BluetoothClient.getCurInstance();
        AlertDialog.Builder builder;
        if (bc == null) {
            CommonFunctions.Msg(ExParamActivity.this, "请连接蓝牙");
        } else {
            byte[] buf;
            switch (item.getItemId()) {
                case R.id.ex_param_query_item:
                    buf = Package.getPackage_22();
                    if (bc.sendData(buf)) {
                        ;
                    } else {
                        CommonFunctions.Msg(ExParamActivity.this, "发送失败");
                    }
                    break;
                case R.id.ex_param_set_item:
                    builder = new AlertDialog.Builder(ExParamActivity.this);
                    builder.setTitle("提示");
                    builder.setMessage("确定设置？");
                    builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            try {
                                byte[] buf = Package.getPackage_23(getControls());
                                BluetoothClient bc = BluetoothClient.getCurInstance();
                                if (bc.sendData(buf)) {
                                    ;
                                } else {
                                    CommonFunctions.Msg(ExParamActivity.this, "发送失败");
                                }
                            } catch (Exception ex) {
                                CommonFunctions.Msg(ExParamActivity.this, "参数异常");
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
        setContentView(R.layout.activity_ex_param);

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
        List<SpinnerData> lst = new ArrayList<SpinnerData>();
        SpinnerData c = new SpinnerData("171", "打开");
        lst.add(c);
        c = new SpinnerData("186", "关闭");
        lst.add(c);
        c = new SpinnerData("188", "强开");
        lst.add(c);
        ArrayAdapter<SpinnerData> Adapter = new ArrayAdapter<SpinnerData>(ExParamActivity.this,
                android.R.layout.simple_spinner_item, lst);
        Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner sp = (Spinner) findViewById(R.id.exSwitch);
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
                if (buf[2] == 0x22) {
                    try{
                        HashMap<String, String> hm = Package.parsePackage_22(buf);
                        setControls(hm);
                        CommonFunctions.Msg(ExParamActivity.this, "查询成功");
                    }catch(Exception e){
                        e.printStackTrace();
                        CommonFunctions.Msg(ExParamActivity.this, "查询失败");
                    }

                } else if (buf[2] == 0x23) {
                    if (Package.parsePackage_23(buf)) {
                        CommonFunctions.Msg(ExParamActivity.this, "设置成功");
                    }
                }
            }
        }
    };

    private void setControls(HashMap<String, String> hm) {
        EditText et = (EditText) findViewById(R.id.packageVehMax);
        et.setText(hm.get("packageVehMax"));
        et = (EditText) findViewById(R.id.nightVehMax);
        et.setText(hm.get("nightVehMax"));
        et = (EditText) findViewById(R.id.exPackageSum);
        et.setText(hm.get("exPackageSum"));
        et = (EditText) findViewById(R.id.exTime);
        et.setText(hm.get("exTime"));
        et = (EditText) findViewById(R.id.exState);
        et.setText(hm.get("exState"));
        et = (EditText) findViewById(R.id.min5VehSum);
        et.setText(hm.get("min5VehSum"));
        et = (EditText) findViewById(R.id.laser1Frame);
        et.setText(hm.get("laser1Frame"));
        et = (EditText) findViewById(R.id.laser2Frame);
        et.setText(hm.get("laser2Frame"));
        Spinner sp = (Spinner) findViewById(R.id.exSwitch);
        CommonFunctions.setSpinnerItemSelectedByValue(sp, hm.get("exSwitch"));
    }

    private HashMap<String,String> getControls()
    {
        HashMap<String, String> hm = new HashMap<String, String>();
        EditText et = (EditText) findViewById(R.id.packageVehMax);
        hm.put("packageVehMax", et.getText().toString());
        et = (EditText) findViewById(R.id.nightVehMax);
        hm.put("nightVehMax", et.getText().toString());
        et = (EditText) findViewById(R.id.exPackageSum);
        hm.put("exPackageSum", et.getText().toString());
        Spinner sp = (Spinner) findViewById(R.id.exSwitch);
        hm.put("exSwitch", ((SpinnerData) sp.getSelectedItem()).getValue());
        return hm;
    }

    public void ex_param_query(View v)
    {
        BluetoothClient bc = BluetoothClient.getCurInstance();
        if (bc == null) {
            CommonFunctions.Msg(ExParamActivity.this, "请连接蓝牙");
        } else {
            byte[] buf = Package.getPackage_22();
            if (bc.sendData(buf)) {
                ;
            } else {
                CommonFunctions.Msg(ExParamActivity.this, "发送失败");
            }
        }
    }

    public void ex_param_set(View v)
    {
        BluetoothClient bc = BluetoothClient.getCurInstance();
        if (bc == null) {
            CommonFunctions.Msg(ExParamActivity.this, "请连接蓝牙");
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(ExParamActivity.this);
            builder.setTitle("提示");
            builder.setMessage("确定设置？");
            builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    try {
                        byte[] buf = Package.getPackage_23(getControls());
                        BluetoothClient bc = BluetoothClient.getCurInstance();
                        if (bc.sendData(buf)) {
                            ;
                        } else {
                            CommonFunctions.Msg(ExParamActivity.this, "发送失败");
                        }
                    } catch (Exception ex) {
                        CommonFunctions.Msg(ExParamActivity.this, "参数异常");
                    }
                }
            });
            builder.setNegativeButton("否", null);
            builder.show();
        }
    }
}
