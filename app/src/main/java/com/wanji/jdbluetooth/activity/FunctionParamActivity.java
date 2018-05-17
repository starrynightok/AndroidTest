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

public class FunctionParamActivity extends AppCompatActivity {

    final String[] items_query = {"控制器时间 ", "组包方式", "串口波特率", "串口发数", "站点信息", "上传参数", "检定单车", "DTU信息", "车型转换准则", "ADC电压"};
    final String[] items_set = {"系统校时 ", "组包方式", "串口波特率", "串口发数", "站点信息", "上传参数", "检定单车", "DTU信息", "车型转换准则"};
    int yourChoice;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.function_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        BluetoothClient bc = BluetoothClient.getCurInstance();
        AlertDialog.Builder builder;
        if (bc == null) {
            CommonFunctions.Msg(FunctionParamActivity.this, "请连接蓝牙");
        } else {
            byte[] buf;
            switch (item.getItemId()) {
                case R.id.function_param_query_item:
                    showQueryDialog();
                    break;
                case R.id.function_param_set_item:
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
        setContentView(R.layout.activity_function_param);

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
        SpinnerData c = new SpinnerData("172", "正常组包");
        lst.add(c);
        c = new SpinnerData("202", "两控制器组包");
        lst.add(c);
        ArrayAdapter<SpinnerData> Adapter = new ArrayAdapter<SpinnerData>(FunctionParamActivity.this,
                android.R.layout.simple_spinner_item, lst);
        Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner sp = (Spinner) findViewById(R.id.makePackageType);
        sp.setAdapter(Adapter);

        lst = new ArrayList<SpinnerData>();
        c = new SpinnerData("1", "4800");
        lst.add(c);
        c = new SpinnerData("2", "9600");
        lst.add(c);
        c = new SpinnerData("3", "19200");
        lst.add(c);
        c = new SpinnerData("4", "57600");
        lst.add(c);
        c = new SpinnerData("5", "115200");
        lst.add(c);
        Adapter = new ArrayAdapter<SpinnerData>(FunctionParamActivity.this,
                android.R.layout.simple_spinner_item, lst);
        Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp = (Spinner) findViewById(R.id.com1Baud);
        sp.setAdapter(Adapter);

        lst = new ArrayList<SpinnerData>();
        c = new SpinnerData("1", "4800");
        lst.add(c);
        c = new SpinnerData("2", "9600");
        lst.add(c);
        c = new SpinnerData("3", "19200");
        lst.add(c);
        c = new SpinnerData("4", "57600");
        lst.add(c);
        c = new SpinnerData("5", "115200");
        lst.add(c);
        Adapter = new ArrayAdapter<SpinnerData>(FunctionParamActivity.this,
                android.R.layout.simple_spinner_item, lst);
        Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp = (Spinner) findViewById(R.id.com2Baud);
        sp.setAdapter(Adapter);

        lst = new ArrayList<SpinnerData>();
        c = new SpinnerData("170", "开启");
        lst.add(c);
        c = new SpinnerData("187", "关闭");
        lst.add(c);
        Adapter = new ArrayAdapter<SpinnerData>(FunctionParamActivity.this,
                android.R.layout.simple_spinner_item, lst);
        Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp = (Spinner) findViewById(R.id.comSend);
        sp.setAdapter(Adapter);

        lst = new ArrayList<SpinnerData>();
        c = new SpinnerData("193", "COM1");
        lst.add(c);
        c = new SpinnerData("194", "COM2");
        lst.add(c);
        c = new SpinnerData("195", "全响应");
        lst.add(c);
        Adapter = new ArrayAdapter<SpinnerData>(FunctionParamActivity.this,
                android.R.layout.simple_spinner_item, lst);
        Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp = (Spinner) findViewById(R.id.comRep);
        sp.setAdapter(Adapter);

        lst = new ArrayList<SpinnerData>();
        c = new SpinnerData("170", "小号上行,全部上传");
        lst.add(c);
        c = new SpinnerData("187", "小号下行,全部上传");
        lst.add(c);
        c = new SpinnerData("17", "小号上行,只上传上行");
        lst.add(c);
        c = new SpinnerData("34", "小号下行,只上传上行");
        lst.add(c);
        c = new SpinnerData("51", "小号上行,只上传下行");
        lst.add(c);
        c = new SpinnerData("68", "小号下行,只上传下行");
        lst.add(c);
        Adapter = new ArrayAdapter<SpinnerData>(FunctionParamActivity.this,
                android.R.layout.simple_spinner_item, lst);
        Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp = (Spinner) findViewById(R.id.uploadType);
        sp.setAdapter(Adapter);

        lst = new ArrayList<SpinnerData>();
        c = new SpinnerData("170", "关闭");
        lst.add(c);
        c = new SpinnerData("187", "打开");
        lst.add(c);
        Adapter = new ArrayAdapter<SpinnerData>(FunctionParamActivity.this,
                android.R.layout.simple_spinner_item, lst);
        Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp = (Spinner) findViewById(R.id.singleVehSwitch);
        sp.setAdapter(Adapter);

        lst = new ArrayList<SpinnerData>();
        c = new SpinnerData("171", "不添加");
        lst.add(c);
        c = new SpinnerData("186", "添加");
        lst.add(c);
        Adapter = new ArrayAdapter<SpinnerData>(FunctionParamActivity.this,
                android.R.layout.simple_spinner_item, lst);
        Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp = (Spinner) findViewById(R.id.dtuProc);
        sp.setAdapter(Adapter);

        lst = new ArrayList<SpinnerData>();
        c = new SpinnerData("170", "上传");
        lst.add(c);
        c = new SpinnerData("187", "不上传");
        lst.add(c);
        Adapter = new ArrayAdapter<SpinnerData>(FunctionParamActivity.this,
                android.R.layout.simple_spinner_item, lst);
        Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp = (Spinner) findViewById(R.id.mtcRule);
        sp.setAdapter(Adapter);

        lst = new ArrayList<SpinnerData>();
        c = new SpinnerData("170", "转换为小客");
        lst.add(c);
        c = new SpinnerData("187", "不上传");
        lst.add(c);
        c = new SpinnerData("204", "三轮转摩托");
        lst.add(c);
        c = new SpinnerData("221", "转换为小货");
        lst.add(c);
        Adapter = new ArrayAdapter<SpinnerData>(FunctionParamActivity.this,
                android.R.layout.simple_spinner_item, lst);
        Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp = (Spinner) findViewById(R.id.slcRule);
        sp.setAdapter(Adapter);

        lst = new ArrayList<SpinnerData>();
        c = new SpinnerData("170", "开启检测");
        lst.add(c);
        c = new SpinnerData("187", "关闭检测");
        lst.add(c);
        Adapter = new ArrayAdapter<SpinnerData>(FunctionParamActivity.this,
                android.R.layout.simple_spinner_item, lst);
        Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp = (Spinner) findViewById(R.id.xrRule);
        sp.setAdapter(Adapter);

        lst = new ArrayList<SpinnerData>();
        c = new SpinnerData("170", "转为特大");
        lst.add(c);
        c = new SpinnerData("187", "实车上传");
        lst.add(c);
        Adapter = new ArrayAdapter<SpinnerData>(FunctionParamActivity.this,
                android.R.layout.simple_spinner_item, lst);
        Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp = (Spinner) findViewById(R.id.jzxRule);
        sp.setAdapter(Adapter);

        lst = new ArrayList<SpinnerData>();
        c = new SpinnerData("170", "实车上传");
        lst.add(c);
        c = new SpinnerData("187", "转为小货");
        lst.add(c);
        Adapter = new ArrayAdapter<SpinnerData>(FunctionParamActivity.this,
                android.R.layout.simple_spinner_item, lst);
        Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp = (Spinner) findViewById(R.id.tljRule);
        sp.setAdapter(Adapter);

        lst = new ArrayList<SpinnerData>();
        c = new SpinnerData("170", "开启");
        lst.add(c);
        c = new SpinnerData("187", "关闭");
        lst.add(c);
        Adapter = new ArrayAdapter<SpinnerData>(FunctionParamActivity.this,
                android.R.layout.simple_spinner_item, lst);
        Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp = (Spinner) findViewById(R.id.zxhSwitch);
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
                    if (buf[2] == 0x10) {
                        HashMap<String, String> hm = Package.parsePackage_10(buf);
                        setControls_0(hm);
                        CommonFunctions.Msg(FunctionParamActivity.this, "查询成功");
                    } else if (buf[2] == 0x29) {
                        HashMap<String, String> hm = Package.parsePackage_29(buf);
                        setControls_1(hm);
                        CommonFunctions.Msg(FunctionParamActivity.this, "查询成功");
                    } else if (buf[2] == 0x30) {
                        if (Package.parsePackage_30(buf)) {
                            CommonFunctions.Msg(FunctionParamActivity.this, "设置成功");
                        }
                    } else if (buf[2] == 0x31) {
                        Object obj = Package.parsePackage_31(buf);
                        if (obj instanceof HashMap) {
                            setControls_2((HashMap<String, String>) obj);
                            CommonFunctions.Msg(FunctionParamActivity.this, "查询成功");
                        } else {
                            if ((boolean) obj) {
                                CommonFunctions.Msg(FunctionParamActivity.this, "设置成功");
                            } else {
                                CommonFunctions.Msg(FunctionParamActivity.this, "设置失败");
                            }
                        }
                    } else if (buf[2] == 0x35) {
                        Object obj = Package.parsePackage_35(buf);
                        if (obj instanceof HashMap) {
                            setControls_3((HashMap<String, String>) obj);
                            CommonFunctions.Msg(FunctionParamActivity.this, "查询成功");
                        } else {
                            if ((boolean) obj) {
                                CommonFunctions.Msg(FunctionParamActivity.this, "设置成功");
                            } else {
                                CommonFunctions.Msg(FunctionParamActivity.this, "设置失败");
                            }
                        }
                    } else if (buf[2] == 0x12) {
                        HashMap<String, String> hm = Package.parsePackage_12(buf);
                        setControls_4(hm);
                        CommonFunctions.Msg(FunctionParamActivity.this, "查询成功");
                    } else if (buf[2] == 0x20) {
                        HashMap<String, String> hm = Package.parsePackage_20(buf);
                        setControls_5(hm);
                        CommonFunctions.Msg(FunctionParamActivity.this, "查询成功");
                    } else if (buf[2] == 0x21) {
                        if (Package.parsePackage_21(buf)) {
                            CommonFunctions.Msg(FunctionParamActivity.this, "设置成功");
                        }
                    } else if (buf[2] == 0x24) {
                        Object obj = Package.parsePackage_24(buf);
                        if (obj instanceof HashMap) {
                            setControls_6((HashMap<String, String>) obj);
                            CommonFunctions.Msg(FunctionParamActivity.this, "查询成功");
                        } else {
                            if ((boolean) obj) {
                                CommonFunctions.Msg(FunctionParamActivity.this, "设置成功");
                            } else {
                                CommonFunctions.Msg(FunctionParamActivity.this, "设置失败");
                            }
                        }
                    } else if (buf[2] == 0x37) {
                        Object obj = Package.parsePackage_37(buf);
                        if (obj instanceof HashMap) {
                            setControls_7((HashMap<String, String>) obj);
                            CommonFunctions.Msg(FunctionParamActivity.this, "查询成功");
                        } else {
                            if ((boolean) obj) {
                                CommonFunctions.Msg(FunctionParamActivity.this, "设置成功");
                            } else {
                                CommonFunctions.Msg(FunctionParamActivity.this, "设置失败");
                            }
                        }
                    } else if (buf[2] == 0x25) {
                        Object obj = Package.parsePackage_25(buf);
                        if (obj instanceof HashMap) {
                            setControls_8((HashMap<String, String>) obj);
                            CommonFunctions.Msg(FunctionParamActivity.this, "查询成功");
                        } else {
                            if ((boolean) obj) {
                                CommonFunctions.Msg(FunctionParamActivity.this, "设置成功");
                            } else {
                                CommonFunctions.Msg(FunctionParamActivity.this, "设置失败");
                            }
                        }
                    } else if (buf[2] == 0x06) {
                        HashMap<String, String> hm = Package.parsePackage_06(buf);
                        setControls_9(hm);
                        CommonFunctions.Msg(FunctionParamActivity.this, "查询成功");
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    CommonFunctions.Msg(FunctionParamActivity.this, "操作失败");
                }
            }
        }
    };

    private void setControls_0(HashMap<String, String> hm) {
        EditText et = (EditText) findViewById(R.id.controlTime);
        et.setText(hm.get("controlTime"));
    }

    private void setControls_1(HashMap<String, String> hm) {
        Spinner sp = (Spinner) findViewById(R.id.makePackageType);
        CommonFunctions.setSpinnerItemSelectedByValue(sp, hm.get("makePackageType"));
    }

    private void setControls_2(HashMap<String, String> hm) {

        Spinner sp = (Spinner) findViewById(R.id.com1Baud);
        CommonFunctions.setSpinnerItemSelectedByValue(sp, hm.get("com1Baud"));
        sp = (Spinner) findViewById(R.id.com2Baud);
        CommonFunctions.setSpinnerItemSelectedByValue(sp, hm.get("com2Baud"));
    }

    private void setControls_3(HashMap<String, String> hm) {

        Spinner sp = (Spinner) findViewById(R.id.comSend);
        CommonFunctions.setSpinnerItemSelectedByValue(sp, hm.get("comSend"));
        sp = (Spinner) findViewById(R.id.comRep);
        CommonFunctions.setSpinnerItemSelectedByValue(sp, hm.get("comRep"));
    }

    private void setControls_4(HashMap<String, String> hm) {
        EditText et = (EditText) findViewById(R.id.devId);
        et.setText(hm.get("devId"));
        et = (EditText) findViewById(R.id.stationId);
        et.setText(hm.get("stationId"));
    }

    private void setControls_5(HashMap<String, String> hm) {
        Spinner sp = (Spinner) findViewById(R.id.uploadType);
        CommonFunctions.setSpinnerItemSelectedByValue(sp, hm.get("uploadType"));
    }

    private void setControls_6(HashMap<String, String> hm) {
        Spinner sp = (Spinner) findViewById(R.id.singleVehSwitch);
        CommonFunctions.setSpinnerItemSelectedByValue(sp, hm.get("singleVehSwitch"));
    }

    private void setControls_7(HashMap<String, String> hm) {
        Spinner sp = (Spinner) findViewById(R.id.dtuProc);
        CommonFunctions.setSpinnerItemSelectedByValue(sp, hm.get("dtuProc"));
        EditText et = (EditText) findViewById(R.id.zdCenter);
        et.setText(hm.get("zdCenter"));
        et = (EditText) findViewById(R.id.bdCenter);
        et.setText(hm.get("bdCenter"));
    }

    private void setControls_8(HashMap<String, String> hm) {
        Spinner sp = (Spinner) findViewById(R.id.mtcRule);
        CommonFunctions.setSpinnerItemSelectedByValue(sp, hm.get("mtcRule"));
        sp = (Spinner) findViewById(R.id.slcRule);
        CommonFunctions.setSpinnerItemSelectedByValue(sp, hm.get("slcRule"));
        sp = (Spinner) findViewById(R.id.xrRule);
        CommonFunctions.setSpinnerItemSelectedByValue(sp, hm.get("xrRule"));
        sp = (Spinner) findViewById(R.id.jzxRule);
        CommonFunctions.setSpinnerItemSelectedByValue(sp, hm.get("jzxRule"));
        sp = (Spinner) findViewById(R.id.tljRule);
        CommonFunctions.setSpinnerItemSelectedByValue(sp, hm.get("tljRule"));
        sp = (Spinner) findViewById(R.id.zxhSwitch);
        CommonFunctions.setSpinnerItemSelectedByValue(sp, hm.get("zxhSwitch"));
    }

    private void setControls_9(HashMap<String, String> hm) {
        EditText et = (EditText) findViewById(R.id.adcV);
        et.setText(hm.get("adcV"));
    }

    private HashMap<String, String> getControls_1() {
        HashMap<String, String> hm = new HashMap<String, String>();
        Spinner sp = (Spinner) findViewById(R.id.makePackageType);
        hm.put("makePackageType", ((SpinnerData) sp.getSelectedItem()).getValue());
        return hm;
    }

    private HashMap<String, String> getControls_2() {
        HashMap<String, String> hm = new HashMap<String, String>();
        Spinner sp = (Spinner) findViewById(R.id.com1Baud);
        hm.put("com1Baud", ((SpinnerData) sp.getSelectedItem()).getValue());
        sp = (Spinner) findViewById(R.id.com2Baud);
        hm.put("com2Baud", ((SpinnerData) sp.getSelectedItem()).getValue());
        return hm;
    }

    private HashMap<String, String> getControls_3() {
        HashMap<String, String> hm = new HashMap<String, String>();
        Spinner sp = (Spinner) findViewById(R.id.comSend);
        hm.put("comSend", ((SpinnerData) sp.getSelectedItem()).getValue());
        sp = (Spinner) findViewById(R.id.comRep);
        hm.put("comRep", ((SpinnerData) sp.getSelectedItem()).getValue());
        return hm;
    }

    private HashMap<String, String> getControls_4() {
        HashMap<String, String> hm = new HashMap<String, String>();
        EditText et = (EditText) findViewById(R.id.devId);
        hm.put("devId", et.getText().toString());
        et = (EditText) findViewById(R.id.stationId);
        hm.put("stationId", et.getText().toString());
        return hm;
    }

    private HashMap<String, String> getControls_5() {
        HashMap<String, String> hm = new HashMap<String, String>();
        Spinner sp = (Spinner) findViewById(R.id.uploadType);
        hm.put("uploadType", ((SpinnerData) sp.getSelectedItem()).getValue());
        return hm;
    }

    private int getControls_6() {
        Spinner sp = (Spinner) findViewById(R.id.singleVehSwitch);
        return Integer.parseInt(((SpinnerData) sp.getSelectedItem()).getValue());
    }

    private HashMap<String, String> getControls_7() {
        HashMap<String, String> hm = new HashMap<String, String>();
        Spinner sp = (Spinner) findViewById(R.id.dtuProc);
        hm.put("dtuProc", ((SpinnerData) sp.getSelectedItem()).getValue());
        EditText et = (EditText) findViewById(R.id.zdCenter);
        hm.put("zdCenter", et.getText().toString());
        et = (EditText) findViewById(R.id.bdCenter);
        hm.put("bdCenter", et.getText().toString());
        return hm;
    }

    private HashMap<String, String> getControls_8() {
        HashMap<String, String> hm = new HashMap<String, String>();
        Spinner sp = (Spinner) findViewById(R.id.mtcRule);
        hm.put("mtcRule", ((SpinnerData) sp.getSelectedItem()).getValue());
        sp = (Spinner) findViewById(R.id.slcRule);
        hm.put("slcRule", ((SpinnerData) sp.getSelectedItem()).getValue());
        sp = (Spinner) findViewById(R.id.xrRule);
        hm.put("xrRule", ((SpinnerData) sp.getSelectedItem()).getValue());
        sp = (Spinner) findViewById(R.id.jzxRule);
        hm.put("jzxRule", ((SpinnerData) sp.getSelectedItem()).getValue());
        sp = (Spinner) findViewById(R.id.tljRule);
        hm.put("tljRule", ((SpinnerData) sp.getSelectedItem()).getValue());
        sp = (Spinner) findViewById(R.id.zxhSwitch);
        hm.put("zxhSwitch", ((SpinnerData) sp.getSelectedItem()).getValue());
        return hm;
    }

    private void showQueryDialog() {

        yourChoice = 0;
        AlertDialog.Builder singleChoiceDialog =
                new AlertDialog.Builder(FunctionParamActivity.this);
        singleChoiceDialog.setTitle("请选择需要查询的参数");
        // 第二个参数是默认选项，此处设置为0
        singleChoiceDialog.setSingleChoiceItems(items_query, 0,
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
                            byte[] buf = Package.getPackage_10();
                            if (bc.sendData(buf)) {
                                ;
                            } else {
                                CommonFunctions.Msg(FunctionParamActivity.this, "发送失败");
                            }
                        } else if (yourChoice == 1) {
                            byte[] buf = Package.getPackage_29();
                            if (bc.sendData(buf)) {
                                ;
                            } else {
                                CommonFunctions.Msg(FunctionParamActivity.this, "发送失败");
                            }
                        } else if (yourChoice == 2) {
                            byte[] buf = Package.getPackage_31(0xCC, null);
                            if (bc.sendData(buf)) {
                                ;
                            } else {
                                CommonFunctions.Msg(FunctionParamActivity.this, "发送失败");
                            }
                        } else if (yourChoice == 3) {
                            byte[] buf = Package.getPackage_35(0xBB, null);
                            if (bc.sendData(buf)) {
                                ;
                            } else {
                                CommonFunctions.Msg(FunctionParamActivity.this, "发送失败");
                            }
                        } else if (yourChoice == 4) {
                            byte[] buf = Package.getPackage_12();
                            if (bc.sendData(buf)) {
                                ;
                            } else {
                                CommonFunctions.Msg(FunctionParamActivity.this, "发送失败");
                            }
                        } else if (yourChoice == 5) {
                            byte[] buf = Package.getPackage_20();
                            if (bc.sendData(buf)) {
                                ;
                            } else {
                                CommonFunctions.Msg(FunctionParamActivity.this, "发送失败");
                            }
                        } else if (yourChoice == 6) {
                            byte[] buf = Package.getPackage_24(0x02);
                            if (bc.sendData(buf)) {
                                ;
                            } else {
                                CommonFunctions.Msg(FunctionParamActivity.this, "发送失败");
                            }
                        } else if (yourChoice == 7) {
                            byte[] buf = Package.getPackage_37(0xAA, null);
                            if (bc.sendData(buf)) {
                                ;
                            } else {
                                CommonFunctions.Msg(FunctionParamActivity.this, "发送失败");
                            }
                        } else if (yourChoice == 8) {
                            byte[] buf = Package.getPackage_25(0x3C, null);
                            if (bc.sendData(buf)) {
                                ;
                            } else {
                                CommonFunctions.Msg(FunctionParamActivity.this, "发送失败");
                            }
                        } else if (yourChoice == 9) {
                            byte[] buf = Package.getPackage_06();
                            if (bc.sendData(buf)) {
                                ;
                            } else {
                                CommonFunctions.Msg(FunctionParamActivity.this, "发送失败");
                            }
                        }
                    }
                });
        singleChoiceDialog.show();
    }

    private void showSetDialog() {

        yourChoice = 0;
        AlertDialog.Builder singleChoiceDialog =
                new AlertDialog.Builder(FunctionParamActivity.this);
        singleChoiceDialog.setTitle("请选择需要设置的参数");
        // 第二个参数是默认选项，此处设置为0
        singleChoiceDialog.setSingleChoiceItems(items_set, 0,
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
                                byte[] buf = Package.getPackage_11();
                                if (bc.sendData(buf)) {
                                    CommonFunctions.Msg(FunctionParamActivity.this, "发送成功");
                                } else {
                                    CommonFunctions.Msg(FunctionParamActivity.this, "发送失败");
                                }
                            } catch (Exception ex) {
                                CommonFunctions.Msg(FunctionParamActivity.this, "参数异常");
                            }
                        } else if (yourChoice == 1) {

                            try {
                                byte[] buf = Package.getPackage_30(getControls_1());
                                if (bc.sendData(buf)) {
                                    ;
                                } else {
                                    CommonFunctions.Msg(FunctionParamActivity.this, "发送失败");
                                }
                            } catch (Exception ex) {
                                CommonFunctions.Msg(FunctionParamActivity.this, "参数异常");
                            }
                        } else if (yourChoice == 2) {

                            try {
                                byte[] buf = Package.getPackage_31(0xDD, getControls_2());
                                if (bc.sendData(buf)) {
                                    ;
                                } else {
                                    CommonFunctions.Msg(FunctionParamActivity.this, "发送失败");
                                }
                            } catch (Exception ex) {
                                CommonFunctions.Msg(FunctionParamActivity.this, "参数异常");
                            }
                        } else if (yourChoice == 3) {

                            try {
                                byte[] buf = Package.getPackage_35(0xCC, getControls_3());
                                if (bc.sendData(buf)) {
                                    ;
                                } else {
                                    CommonFunctions.Msg(FunctionParamActivity.this, "发送失败");
                                }
                            } catch (Exception ex) {
                                CommonFunctions.Msg(FunctionParamActivity.this, "参数异常");
                            }
                        } else if (yourChoice == 4) {

                            try {
                                byte[] buf = Package.getPackage_13(getControls_4());
                                if (bc.sendData(buf)) {
                                    CommonFunctions.Msg(FunctionParamActivity.this, "发送成功");
                                } else {
                                    CommonFunctions.Msg(FunctionParamActivity.this, "发送失败");
                                }
                            } catch (Exception ex) {
                                CommonFunctions.Msg(FunctionParamActivity.this, "参数异常");
                            }
                        } else if (yourChoice == 5) {

                            try {
                                byte[] buf = Package.getPackage_21(getControls_5());
                                if (bc.sendData(buf)) {
                                    ;
                                } else {
                                    CommonFunctions.Msg(FunctionParamActivity.this, "发送失败");
                                }
                            } catch (Exception ex) {
                                CommonFunctions.Msg(FunctionParamActivity.this, "参数异常");
                            }
                        } else if (yourChoice == 6) {

                            try {
                                byte[] buf = Package.getPackage_24(getControls_6());
                                if (bc.sendData(buf)) {
                                    ;
                                } else {
                                    CommonFunctions.Msg(FunctionParamActivity.this, "发送失败");
                                }
                            } catch (Exception ex) {
                                CommonFunctions.Msg(FunctionParamActivity.this, "参数异常");
                            }
                        } else if (yourChoice == 7) {

                            try {
                                byte[] buf = Package.getPackage_37(0xBB, getControls_7());
                                if (bc.sendData(buf)) {
                                    ;
                                } else {
                                    CommonFunctions.Msg(FunctionParamActivity.this, "发送失败");
                                }
                            } catch (Exception ex) {
                                CommonFunctions.Msg(FunctionParamActivity.this, "参数异常");
                            }
                        } else if (yourChoice == 8) {

                            try {
                                byte[] buf = Package.getPackage_25(0xC3, getControls_8());
                                if (bc.sendData(buf)) {
                                    ;
                                } else {
                                    CommonFunctions.Msg(FunctionParamActivity.this, "发送失败");
                                }
                            } catch (Exception ex) {
                                CommonFunctions.Msg(FunctionParamActivity.this, "参数异常");
                            }
                        }
                    }
                });
        singleChoiceDialog.show();
    }

    public void controlTime_set(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(FunctionParamActivity.this);
        builder.setTitle("提示");
        builder.setMessage("确定校时？");
        builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                BluetoothClient bc = BluetoothClient.getCurInstance();
                try {
                    byte[] buf = Package.getPackage_11();
                    if (bc.sendData(buf)) {
                        CommonFunctions.Msg(FunctionParamActivity.this, "发送成功");
                    } else {
                        CommonFunctions.Msg(FunctionParamActivity.this, "发送失败");
                    }
                } catch (Exception ex) {
                    CommonFunctions.Msg(FunctionParamActivity.this, "参数异常");
                }
            }
        });
        builder.setNegativeButton("否", null);
        builder.show();
    }

    public void makePackageType_set(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(FunctionParamActivity.this);
        builder.setTitle("提示");
        builder.setMessage("确定设置？");
        builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                BluetoothClient bc = BluetoothClient.getCurInstance();
                try {
                    byte[] buf = Package.getPackage_30(getControls_1());
                    if (bc.sendData(buf)) {
                        ;
                    } else {
                        CommonFunctions.Msg(FunctionParamActivity.this, "发送失败");
                    }
                } catch (Exception ex) {
                    CommonFunctions.Msg(FunctionParamActivity.this, "参数异常");
                }
            }
        });
        builder.setNegativeButton("否", null);
        builder.show();
    }

    public void baud_set(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(FunctionParamActivity.this);
        builder.setTitle("提示");
        builder.setMessage("确定设置？");
        builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                BluetoothClient bc = BluetoothClient.getCurInstance();
                try {
                    byte[] buf = Package.getPackage_31(0xDD, getControls_2());
                    if (bc.sendData(buf)) {
                        ;
                    } else {
                        CommonFunctions.Msg(FunctionParamActivity.this, "发送失败");
                    }
                } catch (Exception ex) {
                    CommonFunctions.Msg(FunctionParamActivity.this, "参数异常");
                }
            }
        });
        builder.setNegativeButton("否", null);
        builder.show();
    }

    public void com_set(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(FunctionParamActivity.this);
        builder.setTitle("提示");
        builder.setMessage("确定设置？");
        builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                BluetoothClient bc = BluetoothClient.getCurInstance();
                try {
                    byte[] buf = Package.getPackage_35(0xCC, getControls_3());
                    if (bc.sendData(buf)) {
                        ;
                    } else {
                        CommonFunctions.Msg(FunctionParamActivity.this, "发送失败");
                    }
                } catch (Exception ex) {
                    CommonFunctions.Msg(FunctionParamActivity.this, "参数异常");
                }
            }
        });
        builder.setNegativeButton("否", null);
        builder.show();
    }

    public void devInfo_set(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(FunctionParamActivity.this);
        builder.setTitle("提示");
        builder.setMessage("确定设置？");
        builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                BluetoothClient bc = BluetoothClient.getCurInstance();
                try {
                    byte[] buf = Package.getPackage_13(getControls_4());
                    if (bc.sendData(buf)) {
                        CommonFunctions.Msg(FunctionParamActivity.this, "发送成功");
                    } else {
                        CommonFunctions.Msg(FunctionParamActivity.this, "发送失败");
                    }
                } catch (Exception ex) {
                    CommonFunctions.Msg(FunctionParamActivity.this, "参数异常");
                }
            }
        });
        builder.setNegativeButton("否", null);
        builder.show();
    }

    public void uploadType_set(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(FunctionParamActivity.this);
        builder.setTitle("提示");
        builder.setMessage("确定设置？");
        builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                BluetoothClient bc = BluetoothClient.getCurInstance();
                try {
                    byte[] buf = Package.getPackage_21(getControls_5());
                    if (bc.sendData(buf)) {
                        ;
                    } else {
                        CommonFunctions.Msg(FunctionParamActivity.this, "发送失败");
                    }
                } catch (Exception ex) {
                    CommonFunctions.Msg(FunctionParamActivity.this, "参数异常");
                }
            }
        });
        builder.setNegativeButton("否", null);
        builder.show();
    }

    public void singleVehSwitch_set(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(FunctionParamActivity.this);
        builder.setTitle("提示");
        builder.setMessage("确定设置？");
        builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                BluetoothClient bc = BluetoothClient.getCurInstance();
                try {
                    byte[] buf = Package.getPackage_24(getControls_6());
                    if (bc.sendData(buf)) {
                        ;
                    } else {
                        CommonFunctions.Msg(FunctionParamActivity.this, "发送失败");
                    }
                } catch (Exception ex) {
                    CommonFunctions.Msg(FunctionParamActivity.this, "参数异常");
                }
            }
        });
        builder.setNegativeButton("否", null);
        builder.show();
    }

    public void dtu_set(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(FunctionParamActivity.this);
        builder.setTitle("提示");
        builder.setMessage("确定设置？");
        builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                BluetoothClient bc = BluetoothClient.getCurInstance();
                try {
                    byte[] buf = Package.getPackage_37(0xBB, getControls_7());
                    if (bc.sendData(buf)) {
                        ;
                    } else {
                        CommonFunctions.Msg(FunctionParamActivity.this, "发送失败");
                    }
                } catch (Exception ex) {
                    CommonFunctions.Msg(FunctionParamActivity.this, "参数异常");
                }
            }
        });
        builder.setNegativeButton("否", null);
        builder.show();
    }

    public void rule_set(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(FunctionParamActivity.this);
        builder.setTitle("提示");
        builder.setMessage("确定设置？");
        builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                BluetoothClient bc = BluetoothClient.getCurInstance();
                try {
                    byte[] buf = Package.getPackage_25(0xC3, getControls_8());
                    if (bc.sendData(buf)) {
                        ;
                    } else {
                        CommonFunctions.Msg(FunctionParamActivity.this, "发送失败");
                    }
                } catch (Exception ex) {
                    CommonFunctions.Msg(FunctionParamActivity.this, "参数异常");
                }
            }
        });
        builder.setNegativeButton("否", null);
        builder.show();
    }

    public void controlTime_query(View v) {
        BluetoothClient bc = BluetoothClient.getCurInstance();
        byte[] buf = Package.getPackage_10();
        if (bc.sendData(buf)) {
            ;
        } else {
            CommonFunctions.Msg(FunctionParamActivity.this, "发送失败");
        }
    }

    public void makePackageType_query(View v) {
        BluetoothClient bc = BluetoothClient.getCurInstance();
        byte[] buf = Package.getPackage_29();
        if (bc.sendData(buf)) {
            ;
        } else {
            CommonFunctions.Msg(FunctionParamActivity.this, "发送失败");
        }
    }

    public void baud_query(View v) {
        BluetoothClient bc = BluetoothClient.getCurInstance();
        byte[] buf = Package.getPackage_31(0xCC, null);
        if (bc.sendData(buf)) {
            ;
        } else {
            CommonFunctions.Msg(FunctionParamActivity.this, "发送失败");
        }
    }

    public void com_query(View v) {
        BluetoothClient bc = BluetoothClient.getCurInstance();
        byte[] buf = Package.getPackage_35(0xBB, null);
        if (bc.sendData(buf)) {
            ;
        } else {
            CommonFunctions.Msg(FunctionParamActivity.this, "发送失败");
        }
    }

    public void devInfo_query(View v) {
        BluetoothClient bc = BluetoothClient.getCurInstance();
        byte[] buf = Package.getPackage_12();
        if (bc.sendData(buf)) {
            ;
        } else {
            CommonFunctions.Msg(FunctionParamActivity.this, "发送失败");
        }
    }

    public void uploadType_query(View v) {
        BluetoothClient bc = BluetoothClient.getCurInstance();
        byte[] buf = Package.getPackage_20();
        if (bc.sendData(buf)) {
            ;
        } else {
            CommonFunctions.Msg(FunctionParamActivity.this, "发送失败");
        }
    }

    public void singleVehSwitch_query(View v) {
        BluetoothClient bc = BluetoothClient.getCurInstance();
        byte[] buf = Package.getPackage_24(0x02);
        if (bc.sendData(buf)) {
            ;
        } else {
            CommonFunctions.Msg(FunctionParamActivity.this, "发送失败");
        }
    }

    public void dtu_query(View v) {
        BluetoothClient bc = BluetoothClient.getCurInstance();
        byte[] buf = Package.getPackage_37(0xAA, null);
        if (bc.sendData(buf)) {
            ;
        } else {
            CommonFunctions.Msg(FunctionParamActivity.this, "发送失败");
        }
    }

    public void rule_query(View v) {
        BluetoothClient bc = BluetoothClient.getCurInstance();
        byte[] buf = Package.getPackage_25(0x3C, null);
        if (bc.sendData(buf)) {
            ;
        } else {
            CommonFunctions.Msg(FunctionParamActivity.this, "发送失败");
        }
    }

    public void adc_query(View v) {
        BluetoothClient bc = BluetoothClient.getCurInstance();
        byte[] buf = Package.getPackage_06();
        if (bc.sendData(buf)) {
            ;
        } else {
            CommonFunctions.Msg(FunctionParamActivity.this, "发送失败");
        }
    }
}
