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

public class ThresholdParamActivity extends AppCompatActivity {

    final String[] items = {"空白帧数 ", "长高系数", "异常报警", "车型阈值"};
    int yourChoice;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.threshold_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        BluetoothClient bc = BluetoothClient.getCurInstance();
        AlertDialog.Builder builder;
        if (bc == null) {
            CommonFunctions.Msg(ThresholdParamActivity.this, "请连接蓝牙");
        } else {
            byte[] buf;
            switch (item.getItemId()) {
                case R.id.threshold_param_query_item:
                    showQueryDialog();
                    break;
                case R.id.threshold_param_set_item:
                    showSetDialog();
                    break;
                default:
            }
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_threshold_param);

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
        SpinnerData c = new SpinnerData("170", "自动上传");
        lst.add(c);
        c = new SpinnerData("187", "不上传");
        lst.add(c);
        ArrayAdapter<SpinnerData> Adapter = new ArrayAdapter<SpinnerData>(ThresholdParamActivity.this,
                android.R.layout.simple_spinner_item, lst);
        Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner sp = (Spinner) findViewById(R.id.disconnNet);
        sp.setAdapter(Adapter);

        lst = new ArrayList<SpinnerData>();
        c = new SpinnerData("170", "自动上传");
        lst.add(c);
        c = new SpinnerData("187", "不上传");
        lst.add(c);
        Adapter = new ArrayAdapter<SpinnerData>(ThresholdParamActivity.this,
                android.R.layout.simple_spinner_item, lst);
        Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp = (Spinner) findViewById(R.id.exWave);
        sp.setAdapter(Adapter);

        lst = new ArrayList<SpinnerData>();
        c = new SpinnerData("0", "高度优先");
        lst.add(c);
        c = new SpinnerData("1", "轴数优先");
        lst.add(c);
        Adapter = new ArrayAdapter<SpinnerData>(ThresholdParamActivity.this,
                android.R.layout.simple_spinner_item, lst);
        Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp = (Spinner) findViewById(R.id.first);
        sp.setAdapter(Adapter);

        lst = new ArrayList<SpinnerData>();
        c = new SpinnerData("1", "一类交调");
        lst.add(c);
        c = new SpinnerData("2", "二类交调");
        lst.add(c);
        Adapter = new ArrayAdapter<SpinnerData>(ThresholdParamActivity.this,
                android.R.layout.simple_spinner_item, lst);
        Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp = (Spinner) findViewById(R.id.threType);
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
                    if (buf[2] == 0x34) {
                        Object obj = Package.parsePackage_34(buf);
                        if (obj instanceof HashMap) {
                            setControls_0((HashMap<String, String>) obj);
                            CommonFunctions.Msg(ThresholdParamActivity.this, "查询成功");
                        } else {
                            if ((boolean) obj) {
                                CommonFunctions.Msg(ThresholdParamActivity.this, "设置成功");
                            } else {
                                CommonFunctions.Msg(ThresholdParamActivity.this, "设置失败");
                            }
                        }
                    } else if (buf[2] == 0x32) {
                        Object obj = Package.parsePackage_32(buf);
                        if (obj instanceof HashMap) {
                            setControls_1((HashMap<String, String>) obj);
                            CommonFunctions.Msg(ThresholdParamActivity.this, "查询成功");
                        } else {
                            if ((boolean) obj) {
                                CommonFunctions.Msg(ThresholdParamActivity.this, "设置成功");
                            } else {
                                CommonFunctions.Msg(ThresholdParamActivity.this, "设置失败");
                            }
                        }
                    } else if (buf[2] == 0x28) {
                        Object obj = Package.parsePackage_28(buf);
                        if (obj instanceof HashMap) {
                            setControls_2((HashMap<String, String>) obj);
                            CommonFunctions.Msg(ThresholdParamActivity.this, "查询成功");
                        } else {
                            if ((boolean) obj) {
                                CommonFunctions.Msg(ThresholdParamActivity.this, "设置成功");
                            } else {
                                CommonFunctions.Msg(ThresholdParamActivity.this, "设置失败");
                            }
                        }
                    } else if (buf[2] == 0x14) {
                        HashMap<String, String> hm = Package.parsePackage_14(buf);
                        setControls_3(hm);
                        CommonFunctions.Msg(ThresholdParamActivity.this, "查询成功");
                    } else if (buf[2] == 0x15) {
                        if (Package.parsePackage_15(buf)) {
                            CommonFunctions.Msg(ThresholdParamActivity.this, "设置成功");
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    CommonFunctions.Msg(ThresholdParamActivity.this, "操作失败");
                }
            }
        }
    };

    private void setControls_0(HashMap<String, String> hm) {
        EditText et = (EditText) findViewById(R.id.slowEmptyFrame);
        et.setText(hm.get("slowEmptyFrame"));
        et = (EditText) findViewById(R.id.peopleEmptyFrame);
        et.setText(hm.get("peopleEmptyFrame"));
        et = (EditText) findViewById(R.id.normalEmptyFrame);
        et.setText(hm.get("normalEmptyFrame"));
    }

    private void setControls_1(HashMap<String, String> hm) {
        EditText et = (EditText) findViewById(R.id.verticalLenRate);
        et.setText(hm.get("verticalLenRate"));
        et = (EditText) findViewById(R.id.tiltLenRate);
        et.setText(hm.get("tiltLenRate"));
        et = (EditText) findViewById(R.id.verticalSpeedRate);
        et.setText(hm.get("verticalSpeedRate"));
        et = (EditText) findViewById(R.id.tiltSpeedRate);
        et.setText(hm.get("tiltSpeedRate"));
    }

    private void setControls_2(HashMap<String, String> hm) {
        Spinner sp = (Spinner) findViewById(R.id.disconnNet);
        CommonFunctions.setSpinnerItemSelectedByValue(sp, hm.get("disconnNet"));
        sp = (Spinner) findViewById(R.id.exWave);
        CommonFunctions.setSpinnerItemSelectedByValue(sp, hm.get("exWave"));
    }

    private void setControls_3(HashMap<String, String> hm) {
        EditText et = (EditText) findViewById(R.id.xxcLenThre);
        et.setText(hm.get("xxcLenThre"));
        et = (EditText) findViewById(R.id.dxcLenThre);
        et.setText(hm.get("dxcLenThre"));
        et = (EditText) findViewById(R.id.xxcHeightThre);
        et.setText(hm.get("xxcHeightThre"));
        et = (EditText) findViewById(R.id.dkcLenThre);
        et.setText(hm.get("dkcLenThre"));
        et = (EditText) findViewById(R.id.dkcHeightThre);
        et.setText(hm.get("dkcHeightThre"));
        et = (EditText) findViewById(R.id.jzxHeightMax);
        et.setText(hm.get("jzxHeightMax"));
        et = (EditText) findViewById(R.id.jzxHeightMin);
        et.setText(hm.get("jzxHeightMin"));
        Spinner sp = (Spinner) findViewById(R.id.first);
        CommonFunctions.setSpinnerItemSelectedByValue(sp, hm.get("first"));
        sp = (Spinner) findViewById(R.id.threType);
        CommonFunctions.setSpinnerItemSelectedByValue(sp, hm.get("threType"));
    }

    private HashMap<String, String> getControls_0() {
        HashMap<String, String> hm = new HashMap<String, String>();
        EditText et = (EditText) findViewById(R.id.slowEmptyFrame);
        hm.put("slowEmptyFrame", et.getText().toString());
        et = (EditText) findViewById(R.id.peopleEmptyFrame);
        hm.put("peopleEmptyFrame", et.getText().toString());
        et = (EditText) findViewById(R.id.normalEmptyFrame);
        hm.put("normalEmptyFrame", et.getText().toString());
        return hm;
    }

    private HashMap<String, String> getControls_1() {
        HashMap<String, String> hm = new HashMap<String, String>();
        EditText et = (EditText) findViewById(R.id.verticalLenRate);
        hm.put("verticalLenRate", et.getText().toString());
        et = (EditText) findViewById(R.id.tiltLenRate);
        hm.put("tiltLenRate", et.getText().toString());
        et = (EditText) findViewById(R.id.verticalSpeedRate);
        hm.put("verticalSpeedRate", et.getText().toString());
        et = (EditText) findViewById(R.id.tiltSpeedRate);
        hm.put("tiltSpeedRate", et.getText().toString());
        return hm;
    }

    private HashMap<String, String> getControls_2() {
        HashMap<String, String> hm = new HashMap<String, String>();
        Spinner sp = (Spinner) findViewById(R.id.disconnNet);
        hm.put("disconnNet", ((SpinnerData) sp.getSelectedItem()).getValue());
        sp = (Spinner) findViewById(R.id.exWave);
        hm.put("exWave", ((SpinnerData) sp.getSelectedItem()).getValue());
        return hm;
    }

    private HashMap<String, String> getControls_3() {
        HashMap<String, String> hm = new HashMap<String, String>();
        EditText et = (EditText) findViewById(R.id.xxcLenThre);
        hm.put("xxcLenThre", et.getText().toString());
        et = (EditText) findViewById(R.id.dxcLenThre);
        hm.put("dxcLenThre", et.getText().toString());
        et = (EditText) findViewById(R.id.xxcHeightThre);
        hm.put("xxcHeightThre", et.getText().toString());
        et = (EditText) findViewById(R.id.dkcLenThre);
        hm.put("dkcLenThre", et.getText().toString());
        et = (EditText) findViewById(R.id.dkcHeightThre);
        hm.put("dkcHeightThre", et.getText().toString());
        et = (EditText) findViewById(R.id.jzxHeightMax);
        hm.put("jzxHeightMax", et.getText().toString());
        et = (EditText) findViewById(R.id.jzxHeightMin);
        hm.put("jzxHeightMin", et.getText().toString());
        Spinner sp = (Spinner) findViewById(R.id.first);
        hm.put("first", ((SpinnerData) sp.getSelectedItem()).getValue());
        sp = (Spinner) findViewById(R.id.threType);
        hm.put("threType", ((SpinnerData) sp.getSelectedItem()).getValue());
        return hm;
    }

    private void showQueryDialog() {

        yourChoice = 0;
        AlertDialog.Builder singleChoiceDialog =
                new AlertDialog.Builder(ThresholdParamActivity.this);
        singleChoiceDialog.setTitle("请选择需要查询的参数");
        // 第二个参数是默认选项，此处设置为0
        singleChoiceDialog.setSingleChoiceItems(items, 0,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        yourChoice = which;
                    }
                });
        singleChoiceDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        BluetoothClient bc = BluetoothClient.getCurInstance();
                        if (yourChoice == 0) {
                            byte[] buf = Package.getPackage_34(0xCC, null);
                            if (bc.sendData(buf)) {
                                ;
                            } else {
                                CommonFunctions.Msg(ThresholdParamActivity.this, "发送失败");
                            }
                        } else if (yourChoice == 1) {
                            byte[] buf = Package.getPackage_32(0xCC, null);
                            if (bc.sendData(buf)) {
                                ;
                            } else {
                                CommonFunctions.Msg(ThresholdParamActivity.this, "发送失败");
                            }
                        } else if (yourChoice == 2) {
                            byte[] buf = Package.getPackage_28(0xCC, null);
                            if (bc.sendData(buf)) {
                                ;
                            } else {
                                CommonFunctions.Msg(ThresholdParamActivity.this, "发送失败");
                            }
                        } else if (yourChoice == 3) {
                            byte[] buf = Package.getPackage_14();
                            if (bc.sendData(buf)) {
                                ;
                            } else {
                                CommonFunctions.Msg(ThresholdParamActivity.this, "发送失败");
                            }
                        }
                    }
                });
        singleChoiceDialog.show();
    }

    private void showSetDialog() {

        yourChoice = 0;
        AlertDialog.Builder singleChoiceDialog =
                new AlertDialog.Builder(ThresholdParamActivity.this);
        singleChoiceDialog.setTitle("请选择需要设置的参数");
        // 第二个参数是默认选项，此处设置为0
        singleChoiceDialog.setSingleChoiceItems(items, 0,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        yourChoice = which;
                    }
                });
        singleChoiceDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        BluetoothClient bc = BluetoothClient.getCurInstance();
                        if (yourChoice == 0) {

                            try {
                                byte[] buf = Package.getPackage_34(0xDD, getControls_0());
                                if (bc.sendData(buf)) {
                                    ;
                                } else {
                                    CommonFunctions.Msg(ThresholdParamActivity.this, "发送失败");
                                }
                            } catch (Exception ex) {
                                CommonFunctions.Msg(ThresholdParamActivity.this, "参数异常");
                            }
                        } else if (yourChoice == 1) {

                            try {
                                byte[] buf = Package.getPackage_32(0xDD, getControls_1());
                                if (bc.sendData(buf)) {
                                    ;
                                } else {
                                    CommonFunctions.Msg(ThresholdParamActivity.this, "发送失败");
                                }
                            } catch (Exception ex) {
                                CommonFunctions.Msg(ThresholdParamActivity.this, "参数异常");
                            }
                        } else if (yourChoice == 2) {

                            try {
                                byte[] buf = Package.getPackage_28(0xDD, getControls_2());
                                if (bc.sendData(buf)) {
                                    ;
                                } else {
                                    CommonFunctions.Msg(ThresholdParamActivity.this, "发送失败");
                                }
                            } catch (Exception ex) {
                                CommonFunctions.Msg(ThresholdParamActivity.this, "参数异常");
                            }
                        } else if (yourChoice == 3) {

                            try {
                                byte[] buf = Package.getPackage_15(getControls_3());
                                if (bc.sendData(buf)) {
                                    ;
                                } else {
                                    CommonFunctions.Msg(ThresholdParamActivity.this, "发送失败");
                                }
                            } catch (Exception ex) {
                                CommonFunctions.Msg(ThresholdParamActivity.this, "参数异常");
                            }
                        }
                    }
                });
        singleChoiceDialog.show();
    }

    public void emptyFrame_query(View v) {
        BluetoothClient bc = BluetoothClient.getCurInstance();
        byte[] buf = Package.getPackage_34(0xCC, null);
        if (bc.sendData(buf)) {
            ;
        } else {
            CommonFunctions.Msg(ThresholdParamActivity.this, "发送失败");
        }
    }

    public void emptyFrame_set(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ThresholdParamActivity.this);
        builder.setTitle("提示");
        builder.setMessage("确定设置？");
        builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                BluetoothClient bc = BluetoothClient.getCurInstance();
                try {
                    byte[] buf = Package.getPackage_34(0xDD, getControls_0());
                    if (bc.sendData(buf)) {
                        ;
                    } else {
                        CommonFunctions.Msg(ThresholdParamActivity.this, "发送失败");
                    }
                } catch (Exception ex) {
                    CommonFunctions.Msg(ThresholdParamActivity.this, "参数异常");
                }
            }
        });
        builder.setNegativeButton("否", null);
        builder.show();
    }

    public void rate_query(View v) {
        BluetoothClient bc = BluetoothClient.getCurInstance();
        byte[] buf = Package.getPackage_32(0xCC, null);
        if (bc.sendData(buf)) {
            ;
        } else {
            CommonFunctions.Msg(ThresholdParamActivity.this, "发送失败");
        }
    }

    public void rate_set(View v) {

        AlertDialog.Builder builder = new AlertDialog.Builder(ThresholdParamActivity.this);
        builder.setTitle("提示");
        builder.setMessage("确定设置？");
        builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                BluetoothClient bc = BluetoothClient.getCurInstance();
                try {
                    byte[] buf = Package.getPackage_32(0xDD, getControls_1());
                    if (bc.sendData(buf)) {
                        ;
                    } else {
                        CommonFunctions.Msg(ThresholdParamActivity.this, "发送失败");
                    }
                } catch (Exception ex) {
                    CommonFunctions.Msg(ThresholdParamActivity.this, "参数异常");
                }
            }
        });
        builder.setNegativeButton("否", null);
        builder.show();
    }

    public void ex_alarm_query(View v) {
        BluetoothClient bc = BluetoothClient.getCurInstance();
        byte[] buf = Package.getPackage_28(0xCC, null);
        if (bc.sendData(buf)) {
            ;
        } else {
            CommonFunctions.Msg(ThresholdParamActivity.this, "发送失败");
        }
    }

    public void ex_alarm_set(View v) {

        AlertDialog.Builder builder = new AlertDialog.Builder(ThresholdParamActivity.this);
        builder.setTitle("提示");
        builder.setMessage("确定设置？");
        builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                BluetoothClient bc = BluetoothClient.getCurInstance();
                try {
                    byte[] buf = Package.getPackage_28(0xDD, getControls_2());
                    if (bc.sendData(buf)) {
                        ;
                    } else {
                        CommonFunctions.Msg(ThresholdParamActivity.this, "发送失败");
                    }
                } catch (Exception ex) {
                    CommonFunctions.Msg(ThresholdParamActivity.this, "参数异常");
                }
            }
        });
        builder.setNegativeButton("否", null);
        builder.show();
    }

    public void thre_query(View v) {
        BluetoothClient bc = BluetoothClient.getCurInstance();
        byte[] buf = Package.getPackage_14();
        if (bc.sendData(buf)) {
            ;
        } else {
            CommonFunctions.Msg(ThresholdParamActivity.this, "发送失败");
        }
    }

    public void thre_set(View v) {

        AlertDialog.Builder builder = new AlertDialog.Builder(ThresholdParamActivity.this);
        builder.setTitle("提示");
        builder.setMessage("确定设置？");
        builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                BluetoothClient bc = BluetoothClient.getCurInstance();
                try {
                    byte[] buf = Package.getPackage_15(getControls_3());
                    if (bc.sendData(buf)) {
                        ;
                    } else {
                        CommonFunctions.Msg(ThresholdParamActivity.this, "发送失败");
                    }
                } catch (Exception ex) {
                    CommonFunctions.Msg(ThresholdParamActivity.this, "参数异常");
                }
            }
        });
        builder.setNegativeButton("否", null);
        builder.show();
    }
}
