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

public class ControlParamsActivity extends AppCompatActivity {

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.control_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        BluetoothClient bc = BluetoothClient.getCurInstance();
        AlertDialog.Builder builder;
        if (bc == null) {
            CommonFunctions.Msg(ControlParamsActivity.this, "请连接蓝牙");
        } else {
            byte[] buf;
            switch (item.getItemId()) {
                case R.id.control_params_query_item:
                    buf = Package.getPackage_01();
                    if (bc.sendData(buf)) {
                        ;
                    } else {
                        CommonFunctions.Msg(ControlParamsActivity.this, "发送失败");
                    }
                    break;
                case R.id.control_params_set_item:
                    builder = new AlertDialog.Builder(ControlParamsActivity.this);
                    builder.setTitle("提示");
                    builder.setMessage("确定设置？");
                    builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            try {
                                byte[] buf = Package.getPackage_02_New(getControls());
                                BluetoothClient bc = BluetoothClient.getCurInstance();
                                if (bc.sendData(buf)) {
                                    ;
                                } else {
                                    CommonFunctions.Msg(ControlParamsActivity.this, "发送失败");
                                }
                            } catch (Exception ex) {
                                CommonFunctions.Msg(ControlParamsActivity.this, "参数异常");
                            }
                        }
                    });
                    builder.setNegativeButton("否", null);
                    builder.show();
                    break;
                case R.id.control_params_restart_item:
                    builder = new AlertDialog.Builder(ControlParamsActivity.this);
                    builder.setTitle("提示");
                    builder.setMessage("确定重启？");
                    builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            byte[] buf = Package.getPackage_03();
                            BluetoothClient bc = BluetoothClient.getCurInstance();
                            if (bc.sendData(buf)) {
                                ;
                            } else {
                                CommonFunctions.Msg(ControlParamsActivity.this, "发送失败");
                            }
                        }
                    });
                    builder.setNegativeButton("否", null);
                    builder.show();
                    break;
                case R.id.control_params_reset_item:
                    builder = new AlertDialog.Builder(ControlParamsActivity.this);
                    builder.setTitle("提示");
                    builder.setMessage("确定初始化？");
                    builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            byte[] buf = Package.getPackage_04();
                            BluetoothClient bc = BluetoothClient.getCurInstance();
                            if (bc.sendData(buf)) {
                                ;
                            } else {
                                CommonFunctions.Msg(ControlParamsActivity.this, "发送失败");
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
        setContentView(R.layout.activity_control_param);

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
        SpinnerData c = new SpinnerData("0", "正装");
        lst.add(c);
        c = new SpinnerData("1", "侧装");
        lst.add(c);
        ArrayAdapter<SpinnerData> Adapter = new ArrayAdapter<SpinnerData>(ControlParamsActivity.this,
                android.R.layout.simple_spinner_item, lst);
        Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner sp = (Spinner) findViewById(R.id.new_installType);
        sp.setAdapter(Adapter);

        lst = new ArrayList<SpinnerData>();
        c = new SpinnerData("1", "4800");
        lst.add(c);
        c = new SpinnerData("2", "9600");
        lst.add(c);
        c = new SpinnerData("3", "38400");
        lst.add(c);
        c = new SpinnerData("4", "57600");
        lst.add(c);
        c = new SpinnerData("5", "115200");
        lst.add(c);
        Adapter = new ArrayAdapter<SpinnerData>(ControlParamsActivity.this,
                android.R.layout.simple_spinner_item, lst);
        Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp = (Spinner) findViewById(R.id.new_baudRate);
        sp.setAdapter(Adapter);

        lst = new ArrayList<SpinnerData>();
        c = new SpinnerData("0", "关闭");
        lst.add(c);
        c = new SpinnerData("1", "打开");
        lst.add(c);
        Adapter = new ArrayAdapter<SpinnerData>(ControlParamsActivity.this,
                android.R.layout.simple_spinner_item, lst);
        Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp = (Spinner) findViewById(R.id.new_dog);
        sp.setAdapter(Adapter);

        lst = new ArrayList<SpinnerData>();
        c = new SpinnerData("0", "打开");
        lst.add(c);
        c = new SpinnerData("1", "关闭");
        lst.add(c);
        Adapter = new ArrayAdapter<SpinnerData>(ControlParamsActivity.this,
                android.R.layout.simple_spinner_item, lst);
        Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp = (Spinner) findViewById(R.id.new_singleVehSwitch);
        sp.setAdapter(Adapter);

        lst = new ArrayList<SpinnerData>();
        c = new SpinnerData("0", "交调");
        lst.add(c);
        c = new SpinnerData("1", "车检器");
        lst.add(c);
        Adapter = new ArrayAdapter<SpinnerData>(ControlParamsActivity.this,
                android.R.layout.simple_spinner_item, lst);
        Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp = (Spinner) findViewById(R.id.new_devType);
        sp.setAdapter(Adapter);

        lst = new ArrayList<SpinnerData>();
        c = new SpinnerData("0", "国道");
        lst.add(c);
        c = new SpinnerData("1", "高速");
        lst.add(c);
        Adapter = new ArrayAdapter<SpinnerData>(ControlParamsActivity.this,
                android.R.layout.simple_spinner_item, lst);
        Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp = (Spinner) findViewById(R.id.new_laneType);
        sp.setAdapter(Adapter);

        lst = new ArrayList<SpinnerData>();
        c = new SpinnerData("1", "1车道");
        lst.add(c);
        c = new SpinnerData("2", "2车道");
        lst.add(c);
        c = new SpinnerData("3", "3车道");
        lst.add(c);
        c = new SpinnerData("4", "4车道");
        lst.add(c);
        c = new SpinnerData("5", "5车道");
        lst.add(c);
        Adapter = new ArrayAdapter<SpinnerData>(ControlParamsActivity.this,
                android.R.layout.simple_spinner_item, lst);
        Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp = (Spinner) findViewById(R.id.new_leftLaneNum);
        sp.setAdapter(Adapter);

        lst = new ArrayList<SpinnerData>();
        c = new SpinnerData("1", "1车道");
        lst.add(c);
        c = new SpinnerData("2", "2车道");
        lst.add(c);
        c = new SpinnerData("3", "3车道");
        lst.add(c);
        c = new SpinnerData("4", "4车道");
        lst.add(c);
        c = new SpinnerData("5", "5车道");
        lst.add(c);
        Adapter = new ArrayAdapter<SpinnerData>(ControlParamsActivity.this,
                android.R.layout.simple_spinner_item, lst);
        Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp = (Spinner) findViewById(R.id.new_rightLaneNum);
        sp.setAdapter(Adapter);

        lst = new ArrayList<SpinnerData>();
        c = new SpinnerData("0", "无线");
        lst.add(c);
        c = new SpinnerData("1", "有限");
        lst.add(c);
        Adapter = new ArrayAdapter<SpinnerData>(ControlParamsActivity.this,
                android.R.layout.simple_spinner_item, lst);
        Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp = (Spinner) findViewById(R.id.new_netType);
        sp.setAdapter(Adapter);

        lst = new ArrayList<SpinnerData>();
        c = new SpinnerData("0", "不使能");
        lst.add(c);
        c = new SpinnerData("1", "使能");
        lst.add(c);
        Adapter = new ArrayAdapter<SpinnerData>(ControlParamsActivity.this,
                android.R.layout.simple_spinner_item, lst);
        Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp = (Spinner) findViewById(R.id.new_sdCard);
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
                if (buf[2] == 0x01) {
                    try{
                        HashMap<String, String> hm = Package.parsePackage_01_New(buf);
                        setControls(hm);
                        CommonFunctions.Msg(ControlParamsActivity.this, "查询成功");
                    }catch(Exception e){
                        e.printStackTrace();
                        CommonFunctions.Msg(ControlParamsActivity.this, "查询失败");
                    }

                } else if (buf[2] == 0x02) {
                    if (Package.parsePackage_02(buf)) {
                        CommonFunctions.Msg(ControlParamsActivity.this, "设置成功");
                    } else {
                        CommonFunctions.Msg(ControlParamsActivity.this, "设置失败");
                    }
                } else if (buf[2] == 0x03) {
                    if (Package.parsePackage_03(buf)) {
                        CommonFunctions.Msg(ControlParamsActivity.this, "重启成功");
                    } else {
                        CommonFunctions.Msg(ControlParamsActivity.this, "重启失败");
                    }
                } else if (buf[2] == 0x04) {
                    if (Package.parsePackage_04(buf)) {
                        CommonFunctions.Msg(ControlParamsActivity.this, "初始化成功");
                    } else {
                        CommonFunctions.Msg(ControlParamsActivity.this, "初始化失败");
                    }
                }
            }
        }
    };

    private void setControls(HashMap<String, String> hm) {
        EditText et = (EditText) findViewById(R.id.new_versionId);
        et.setText(hm.get("new_versionId"));
        Spinner sp = (Spinner) findViewById(R.id.new_installType);
        CommonFunctions.setSpinnerItemSelectedByValue(sp, hm.get("new_installType"));
        sp = (Spinner) findViewById(R.id.new_baudRate);
        CommonFunctions.setSpinnerItemSelectedByValue(sp, hm.get("new_baudRate"));
        sp = (Spinner) findViewById(R.id.new_dog);
        CommonFunctions.setSpinnerItemSelectedByValue(sp, hm.get("new_dog"));
        et = (EditText) findViewById(R.id.new_verticalLaserLeftHeight);
        et.setText(hm.get("new_verticalLaserLeftHeight"));
        et = (EditText) findViewById(R.id.new_verticalLaserRightHeight);
        et.setText(hm.get("new_verticalLaserRightHeight"));
        et = (EditText) findViewById(R.id.new_tiltLaserLeftHeight);
        et.setText(hm.get("new_tiltLaserLeftHeight"));
        et = (EditText) findViewById(R.id.new_tiltLaserRightHeight);
        et.setText(hm.get("new_tiltLaserRightHeight"));
        et = (EditText) findViewById(R.id.new_laserPosSpace);
        et.setText(hm.get("new_laserPosSpace"));
        et = (EditText) findViewById(R.id.new_tiltLaserAngle);
        et.setText(hm.get("new_tiltLaserAngle"));
        et = (EditText) findViewById(R.id.new_laneWidth);
        et.setText(hm.get("new_laneWidth"));
        et = (EditText) findViewById(R.id.new_isolationRightWidth);
        et.setText(hm.get("new_isolationRightWidth"));
        et = (EditText) findViewById(R.id.new_isolationLeftWidth);
        et.setText(hm.get("new_isolationLeftWidth"));
        et = (EditText) findViewById(R.id.new_powerRestartCount);
        et.setText(hm.get("new_powerRestartCount"));
        et = (EditText) findViewById(R.id.new_dogRestartCount);
        et.setText(hm.get("new_dogRestartCount"));
        et = (EditText) findViewById(R.id.new_highStart);
        et.setText(hm.get("new_highStart"));
        et = (EditText) findViewById(R.id.new_laserHorizontalPos);
        et.setText(hm.get("new_laserHorizontalPos"));
        et = (EditText) findViewById(R.id.new_verticalCenterPos);
        et.setText(hm.get("new_verticalCenterPos"));
        et = (EditText) findViewById(R.id.new_tiltCenterPos);
        et.setText(hm.get("new_tiltCenterPos"));
        et = (EditText) findViewById(R.id.new_startPos);
        et.setText(hm.get("new_startPos"));
        et = (EditText) findViewById(R.id.new_endPos);
        et.setText(hm.get("new_endPos"));
        sp = (Spinner) findViewById(R.id.new_singleVehSwitch);
        CommonFunctions.setSpinnerItemSelectedByValue(sp, hm.get("new_singleVehSwitch"));
        sp = (Spinner) findViewById(R.id.new_devType);
        CommonFunctions.setSpinnerItemSelectedByValue(sp, hm.get("new_devType"));
        sp = (Spinner) findViewById(R.id.new_laneType);
        CommonFunctions.setSpinnerItemSelectedByValue(sp, hm.get("new_laneType"));
        sp = (Spinner) findViewById(R.id.new_leftLaneNum);
        CommonFunctions.setSpinnerItemSelectedByValue(sp, hm.get("new_leftLaneNum"));
        sp = (Spinner) findViewById(R.id.new_rightLaneNum);
        CommonFunctions.setSpinnerItemSelectedByValue(sp, hm.get("new_rightLaneNum"));
        sp = (Spinner) findViewById(R.id.new_netType);
        CommonFunctions.setSpinnerItemSelectedByValue(sp, hm.get("new_netType"));
        sp = (Spinner) findViewById(R.id.new_sdCard);
        CommonFunctions.setSpinnerItemSelectedByValue(sp, hm.get("new_sdCard"));
    }

    private HashMap<String, String> getControls() {
        HashMap<String, String> hm = new HashMap<String, String>();
        EditText et = (EditText) findViewById(R.id.new_versionId);
        hm.put("new_versionId", et.getText().toString());
        Spinner sp = (Spinner) findViewById(R.id.new_installType);
        hm.put("new_installType", ((SpinnerData) sp.getSelectedItem()).getValue());
        sp = (Spinner) findViewById(R.id.new_baudRate);
        hm.put("new_baudRate", ((SpinnerData) sp.getSelectedItem()).getValue());
        sp = (Spinner) findViewById(R.id.new_dog);
        hm.put("new_dog", ((SpinnerData) sp.getSelectedItem()).getValue());
        et = (EditText) findViewById(R.id.new_verticalLaserLeftHeight);
        hm.put("new_verticalLaserLeftHeight", et.getText().toString());
        et = (EditText) findViewById(R.id.new_verticalLaserRightHeight);
        hm.put("new_verticalLaserRightHeight", et.getText().toString());
        et = (EditText) findViewById(R.id.new_tiltLaserLeftHeight);
        hm.put("new_tiltLaserLeftHeight", et.getText().toString());
        et = (EditText) findViewById(R.id.new_tiltLaserRightHeight);
        hm.put("new_tiltLaserRightHeight", et.getText().toString());
        et = (EditText) findViewById(R.id.new_laserPosSpace);
        hm.put("new_laserPosSpace", et.getText().toString());
        et = (EditText) findViewById(R.id.new_tiltLaserAngle);
        hm.put("new_tiltLaserAngle", et.getText().toString());
        et = (EditText) findViewById(R.id.new_laneWidth);
        hm.put("new_laneWidth", et.getText().toString());
        et = (EditText) findViewById(R.id.new_isolationRightWidth);
        hm.put("new_isolationRightWidth", et.getText().toString());
        et = (EditText) findViewById(R.id.new_isolationLeftWidth);
        hm.put("new_isolationLeftWidth", et.getText().toString());
        et = (EditText) findViewById(R.id.new_powerRestartCount);
        hm.put("new_powerRestartCount", et.getText().toString());
        et = (EditText) findViewById(R.id.new_dogRestartCount);
        hm.put("new_dogRestartCount", et.getText().toString());
        et = (EditText) findViewById(R.id.new_highStart);
        hm.put("new_highStart", et.getText().toString());
        et = (EditText) findViewById(R.id.new_laserHorizontalPos);
        hm.put("new_laserHorizontalPos", et.getText().toString());
        et = (EditText) findViewById(R.id.new_verticalCenterPos);
        hm.put("new_verticalCenterPos", et.getText().toString());
        et = (EditText) findViewById(R.id.new_tiltCenterPos);
        hm.put("new_tiltCenterPos", et.getText().toString());
        et = (EditText) findViewById(R.id.new_startPos);
        hm.put("new_startPos", et.getText().toString());
        et = (EditText) findViewById(R.id.new_endPos);
        hm.put("new_endPos", et.getText().toString());
        sp = (Spinner) findViewById(R.id.new_singleVehSwitch);
        hm.put("new_singleVehSwitch", ((SpinnerData) sp.getSelectedItem()).getValue());
        sp = (Spinner) findViewById(R.id.new_devType);
        hm.put("new_devType", ((SpinnerData) sp.getSelectedItem()).getValue());
        sp = (Spinner) findViewById(R.id.new_laneType);
        hm.put("new_laneType", ((SpinnerData) sp.getSelectedItem()).getValue());
        sp = (Spinner) findViewById(R.id.new_leftLaneNum);
        hm.put("new_leftLaneNum", ((SpinnerData) sp.getSelectedItem()).getValue());
        sp = (Spinner) findViewById(R.id.new_rightLaneNum);
        hm.put("new_rightLaneNum", ((SpinnerData) sp.getSelectedItem()).getValue());
        sp = (Spinner) findViewById(R.id.new_netType);
        hm.put("new_netType", ((SpinnerData) sp.getSelectedItem()).getValue());
        sp = (Spinner) findViewById(R.id.new_sdCard);
        hm.put("new_sdCard", ((SpinnerData) sp.getSelectedItem()).getValue());

        return hm;
    }

    public void control_params_query(View v)
    {
        BluetoothClient bc = BluetoothClient.getCurInstance();
        if (bc == null) {
            CommonFunctions.Msg(ControlParamsActivity.this, "请连接蓝牙");
        } else {
            byte[] buf = Package.getPackage_01();
            if (bc.sendData(buf)) {
                ;
            } else {
                CommonFunctions.Msg(ControlParamsActivity.this, "发送失败");
            }
        }
    }

    public void control_params_set(View v)
    {
        BluetoothClient bc = BluetoothClient.getCurInstance();
        if (bc == null) {
            CommonFunctions.Msg(ControlParamsActivity.this, "请连接蓝牙");
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(ControlParamsActivity.this);
            builder.setTitle("提示");
            builder.setMessage("确定设置？");
            builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    try {
                        byte[] buf = Package.getPackage_02_New(getControls());
                        BluetoothClient bc = BluetoothClient.getCurInstance();
                        if (bc.sendData(buf)) {
                            ;
                        } else {
                            CommonFunctions.Msg(ControlParamsActivity.this, "发送失败");
                        }
                    } catch (Exception ex) {
                        CommonFunctions.Msg(ControlParamsActivity.this, "参数异常");
                    }
                }
            });
            builder.setNegativeButton("否", null);
            builder.show();
        }
    }

    public void control_params_restart(View v)
    {
        BluetoothClient bc = BluetoothClient.getCurInstance();
        if (bc == null) {
            CommonFunctions.Msg(ControlParamsActivity.this, "请连接蓝牙");
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(ControlParamsActivity.this);
            builder.setTitle("提示");
            builder.setMessage("确定重启？");
            builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    byte[] buf = Package.getPackage_03();
                    BluetoothClient bc = BluetoothClient.getCurInstance();
                    if (bc.sendData(buf)) {
                        ;
                    } else {
                        CommonFunctions.Msg(ControlParamsActivity.this, "发送失败");
                    }
                }
            });
            builder.setNegativeButton("否", null);
            builder.show();
        }
    }

    public void control_params_reset(View v)
    {
        BluetoothClient bc = BluetoothClient.getCurInstance();
        if (bc == null) {
            CommonFunctions.Msg(ControlParamsActivity.this, "请连接蓝牙");
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(ControlParamsActivity.this);
            builder.setTitle("提示");
            builder.setMessage("确定初始化？");
            builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    byte[] buf = Package.getPackage_04();
                    BluetoothClient bc = BluetoothClient.getCurInstance();
                    if (bc.sendData(buf)) {
                        ;
                    } else {
                        CommonFunctions.Msg(ControlParamsActivity.this, "发送失败");
                    }
                }
            });
            builder.setNegativeButton("否", null);
            builder.show();
        }
    }
}
