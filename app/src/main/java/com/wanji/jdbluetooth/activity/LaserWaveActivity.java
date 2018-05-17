package com.wanji.jdbluetooth.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.ScatterChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.ScatterData;
import com.github.mikephil.charting.data.ScatterDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IScatterDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.EntryXComparator;
import com.wanji.jdbluetooth.R;
import com.wanji.jdbluetooth.beans.SpinnerData;
import com.wanji.jdbluetooth.bluetooth.BluetoothClient;
import com.wanji.jdbluetooth.global.GlobalVariables;
import com.wanji.jdbluetooth.tools.CommonFunctions;
import com.wanji.jdbluetooth.tools.Package;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.wanji.jdbluetooth.R.id.centerPos;


/**
 * Created by admin on 2018/2/9.
 */

public class LaserWaveActivity extends AppCompatActivity implements OnChartValueSelectedListener {

    LineChart mLineChart_ji;
    //LineChart mLineChart_zj;
    ScatterChart mScatterChart;
    List<Entry> entries_zj = new ArrayList<>();
    List<Entry> entries_ji = new ArrayList<>();
    public int[] waveData = {0};/*{0x00, 0x00, 0x01, 0x22, 0x01, 0x22, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
            0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x4F, 0x60, 0x00, 0x00,
            0x00, 0x00, 0x51, 0x8C, 0x00, 0x00, 0x4B, 0x45, 0x4B, 0x77, 0x4B, 0x67, 0x48, 0x89, 0x47, 0x99,
            0x00, 0x00, 0x59, 0x64, 0x00, 0x00, 0x43, 0x7E, 0x46, 0x49, 0x38, 0x5A, 0x58, 0xD4, 0x00, 0x00,
            0x37, 0xF8, 0x3A, 0x0D, 0x39, 0x9E, 0x37, 0x93, 0x39, 0xAA, 0x39, 0x89, 0x5C, 0x6A, 0x36, 0xE7,
            0x35, 0xFC, 0x36, 0x38, 0x51, 0x14, 0x00, 0x00, 0x6E, 0x22, 0x3F, 0x88, 0x3F, 0x17, 0x5D, 0x61,
            0x40, 0x60, 0x3F, 0x36, 0x3E, 0x2C, 0x3C, 0xEB, 0x3C, 0x3B, 0x3B, 0xEC, 0x3C, 0x21, 0x3C, 0x97,
            0x35, 0xE1, 0x35, 0x2D, 0x35, 0x84, 0x39, 0x29, 0x38, 0x92, 0x37, 0x27, 0x35, 0xCC, 0x35, 0x00,
            0x34, 0x64, 0x33, 0xCD, 0x33, 0x33, 0x32, 0x84, 0x32, 0x1D, 0x31, 0x70, 0x30, 0x9D, 0x30, 0x01,
            0x2F, 0x80, 0x2E, 0xDD, 0x2E, 0x8B, 0x2D, 0xAC, 0x2D, 0x56, 0x2C, 0xC4, 0x2C, 0x28, 0x2B, 0xCA,
            0x2B, 0x3F, 0x2A, 0xED, 0x2A, 0x7F, 0x29, 0xED, 0x29, 0xC9, 0x29, 0x28, 0x28, 0xBA, 0x28, 0x61,
            0x28, 0x01, 0x27, 0xBE, 0x27, 0x57, 0x26, 0xF2, 0x26, 0xBB, 0x26, 0x56, 0x26, 0x13, 0x25, 0xCB,
            0x25, 0x69, 0x25, 0x09, 0x24, 0xE0, 0x24, 0xA4, 0x24, 0x4B, 0x23, 0xF5, 0x23, 0xB7, 0x23, 0x8E,
            0x23, 0x3C, 0x22, 0xFB, 0x22, 0xB8, 0x22, 0x8D, 0x22, 0x64, 0x22, 0x10, 0x21, 0xD2, 0x21, 0xD2,
            0x21, 0x5D, 0x21, 0x5D, 0x21, 0x09, 0x20, 0xE7, 0x20, 0xCA, 0x20, 0x87, 0x20, 0x68, 0x20, 0x36,
            0x20, 0x0F, 0x1F, 0xE4, 0x1F, 0xB4, 0x1F, 0x95, 0x1F, 0x7D, 0x1F, 0x59, 0x1F, 0x24, 0x1F, 0x16,
            0x1E, 0xE3, 0x1E, 0xBD, 0x1E, 0xA0, 0x1E, 0x75, 0x1E, 0x62, 0x1E, 0x4A, 0x1E, 0x37, 0x1E, 0x07,
            0x1D, 0xF4, 0x1D, 0xD2, 0x1D, 0x96, 0x1D, 0x98, 0x1D, 0x7E, 0x1D, 0x77, 0x1D, 0x53, 0x1D, 0x44,
            0x1D, 0x2F, 0x1D, 0x14, 0x1D, 0x0B, 0x1C, 0xF8, 0x1C, 0xDB, 0x1C, 0xD1, 0x1C, 0xC3, 0x1C, 0xAB,
            0x1C, 0x9F, 0x1C, 0x91, 0x1C, 0x7D, 0x1C, 0x6A, 0x1C, 0x63, 0x1C, 0x55, 0x1C, 0x3A, 0x1C, 0x3D,
            0x1C, 0x2C, 0x1C, 0x27, 0x1C, 0x14, 0x1C, 0x0A, 0x1B, 0xF7, 0x1B, 0xED, 0x1B, 0xE1, 0x1B, 0xE1,
            0x1B, 0xE9, 0x1B, 0xC5, 0x1B, 0x7F, 0x1A, 0xE8, 0x1B, 0x1D, 0x1B, 0x4F, 0x18, 0x90, 0x18, 0xB0,
            0x1B, 0x11, 0x1B, 0x46, 0x1B, 0x5B, 0x1B, 0x60, 0x1B, 0x4F, 0x1B, 0x5B, 0x1B, 0x56, 0x1B, 0x56,
            0x1B, 0x5B, 0x1B, 0x6A, 0x1B, 0x4F, 0x1B, 0x46, 0x1B, 0x5B, 0x1B, 0x7D, 0x1B, 0x7F, 0x1B, 0x86,
            0x1B, 0x84, 0x1B, 0x9C, 0x1B, 0xA5, 0x1B, 0x99, 0x1B, 0xAF, 0x1B, 0x8B, 0x1A, 0x0E, 0x19, 0x5F,
            0x18, 0xFA, 0x1B, 0xDA, 0x1C, 0x38, 0x1C, 0x65, 0x1C, 0x74, 0x1C, 0x8C, 0x1C, 0xAD, 0x1C, 0xC0,
            0x1C, 0xD1, 0x1C, 0xD8, 0x1C, 0xFC, 0x1D, 0x12, 0x1D, 0x1E, 0x1D, 0x34, 0x1D, 0x58, 0x1D, 0x66,
            0x1D, 0x83, 0x1D, 0xA9, 0x1D, 0xC4, 0x1D, 0xD4, 0x1D, 0xEA, 0x1E, 0x02, 0x1E, 0x39, 0x1E, 0x45,
            0x1E, 0x70, 0x1E, 0x86, 0x1E, 0x9E, 0x1E, 0xDA, 0x1E, 0xF9, 0x1F, 0x05, 0x1F, 0x2B, 0x1F, 0x54,
            0x1F, 0x6C, 0x1F, 0xAD, 0x1F, 0xC2, 0x1F, 0xF5, 0x20, 0x27, 0x20, 0x46, 0x20, 0x7D, 0x20, 0xBE,
            0x20, 0xE9, 0x21, 0x0B, 0x21, 0x31, 0x21, 0x47, 0x21, 0xA5, 0x21, 0xBF, 0x22, 0x04, 0x22, 0x2B,
            0x22, 0x7F, 0x22, 0x92, 0x22, 0xE1, 0x23, 0x1B, 0x23, 0x73, 0x23, 0xBB, 0x23, 0xDB, 0x24, 0x50,
            0x24, 0x82, 0x24, 0xCA, 0x25, 0x12, 0x25, 0x51, 0x25, 0xAC, 0x25, 0xD7, 0x26, 0x1A, 0x26, 0xAD,
            0x26, 0xDA, 0x27, 0x3F, 0x27, 0xE4, 0x27, 0xDD, 0x28, 0x1B, 0x28, 0xCF, 0x29, 0x07, 0x29, 0x70,
            0x2A, 0x0A, 0x2A, 0x62, 0x2A, 0xA8, 0x2B, 0x68, 0x2B, 0xBC, 0x00, 0x00, 0x2C, 0xEC, 0x2D, 0x67,
            0x00, 0x00, 0x00, 0x00, 0x2E, 0xB9, 0x2F, 0xBE, 0x2F, 0xD1, 0x30, 0x55, 0x31, 0x48, 0x31, 0xEB,
            0x33, 0x4E, 0x33, 0x0D, 0x33, 0xFA, 0x34, 0x99, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x37, 0x98,
            0x38, 0x4C, 0x39, 0x0E, 0x39, 0xE6, 0x3B, 0x23, 0x3C, 0x2F, 0x39, 0xA5, 0x38, 0xE3, 0x3A, 0x14,
            0x42, 0xB2, 0x5D, 0x58, 0x5E, 0x97, 0x41, 0x3C, 0x3F, 0x49, 0x47, 0x69, 0x00, 0x00, 0x46, 0x33,
            0x00, 0x00, 0x5C, 0xEE, 0x46, 0xB0, 0x44, 0x69, 0x4D, 0x6F, 0x49, 0x68, 0x43, 0xBA, 0x00, 0x00,
            0x4A, 0xC4, 0x4C, 0xD1, 0x4F, 0xA5, 0x52, 0xE3, 0x00, 0x00, 0x4C, 0xBE, 0x4C, 0x6A, 0x4D, 0x3F,
            0x3D, 0x71, 0x4C, 0x37, 0x4B, 0xA5, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
            0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
            0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
            0x00, 0x00, 0x01, 0xAA, 0x01, 0x59, 0x01, 0x16, 0x01, 0x1D, 0x01, 0x07, 0x01, 0x2E, 0x01, 0x27,
            0x00, 0x00};*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.laser_wave_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        BluetoothClient bc = BluetoothClient.getCurInstance();
        byte[] buf;
        switch (item.getItemId()) {
            case R.id.laser_wave_vertical_item:
                buf = Package.getPackage_26(0xCC);
                if (bc != null && bc.sendData(buf)) {
                    ;
                } else {
                    CommonFunctions.Msg(LaserWaveActivity.this, "发送失败");
                }
                break;
            case R.id.laser_wave_tilt_item:
                buf = Package.getPackage_26(0xDD);
                if (bc.sendData(buf)) {
                    ;
                } else {
                    CommonFunctions.Msg(LaserWaveActivity.this, "发送失败");
                }
                break;
            case R.id.center_set_item:
                if (LaserWaveActivity.this.waveData.length < 361) {
                    return true;
                }
                /*
                int[] res = new int[361];
                for (int i = 0; i < 722; i += 2) {
                    res[i / 2] = (LaserWaveActivity.this.waveData[i] << 8) + LaserWaveActivity.this.waveData[i + 1];
                }*/
                printWave(LaserWaveActivity.this.waveData);
                float minSigma = 1000000;
                Spinner sp = (Spinner) findViewById(R.id.laserInstallType);
                if ("0".equals(((SpinnerData) sp.getSelectedItem()).getValue())) {
                    List<Float> data = new ArrayList<Float>();
                    for (int i = 0; i < entries_zj.size(); i++) {
                        if (Math.abs(entries_zj.get(i).getX()) >= 3000 && Math.abs(entries_zj.get(i).getX()) <= 8000) {
                            data.add(entries_zj.get(i).getY());
                        }
                    }
                    float sigmaTmp1 = CommonFunctions.standardDeviation(data);
                    if (minSigma >= sigmaTmp1) {
                        minSigma = sigmaTmp1;
                    }
                } else {
                    List<Float> data1 = new ArrayList<Float>();
                    List<Float> data2 = new ArrayList<Float>();
                    for (int i = 0; i < entries_zj.size(); i++) {
                        if (entries_zj.get(i).getX() >= 3000 && entries_zj.get(i).getX() <= 8000) {
                            data1.add(entries_zj.get(i).getY());
                        } else if (entries_zj.get(i).getX() >= -8000 && entries_zj.get(i).getX() <= -3000) {
                            data2.add(entries_zj.get(i).getY());
                        }
                    }

                    float sigmaTmp1 = CommonFunctions.standardDeviation(data1);
                    float sigmaTmp2 = CommonFunctions.standardDeviation(data2);
                    float sigmaTmp = sigmaTmp1 < sigmaTmp2 ? sigmaTmp1 : sigmaTmp2;
                    if (minSigma >= sigmaTmp) {
                        minSigma = sigmaTmp;
                    }
                }
                EditText et = (EditText) findViewById(R.id.standardDeviation);
                et.setText((int) minSigma + "");
                CommonFunctions.Msg(LaserWaveActivity.this, "设置完成");
                break;
            case R.id.center_auto_item:
                if (LaserWaveActivity.this.waveData.length < 361) {
                    return true;
                }
                autoCenter();
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laser_wave);

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
        ArrayAdapter<SpinnerData> Adapter = new ArrayAdapter<SpinnerData>(LaserWaveActivity.this,
                android.R.layout.simple_spinner_item, lst);
        Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner sp = (Spinner) findViewById(R.id.laserInstallType);
        sp.setAdapter(Adapter);

        mLineChart_ji = (LineChart) findViewById(R.id.lineChart_ji);
        mLineChart_ji.setDrawBorders(true);//显示边界
        mLineChart_ji.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);//X轴在底部
        mLineChart_ji.setOnChartValueSelectedListener(this);
        mLineChart_ji.getDescription().setEnabled(false);

        mScatterChart = (ScatterChart) findViewById(R.id.ScatterChart);
        mScatterChart.setDrawBorders(true);//显示边界
        mScatterChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);//X轴在底部
        mScatterChart.getDescription().setEnabled(false);
        mScatterChart.setOnChartValueSelectedListener(this);

        /*
        int[] res = new int[361];
        for (int i = 0; i < 722; i += 2) {
            res[i / 2] = (waveData[i] << 8) + waveData[i + 1];
        }
        printWave(res);*/
        printWave(new int[]{0});
    }

    public byte[] int2byte(int[] i) {
        byte[] b = new byte[i.length];
        for (int index = 0; index < i.length; index++) {
            b[index] = (byte) i[index];
        }
        return b;
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
                    if (buf[2] == 0x26) {
                        waveData = Package.parsePackage_26(buf);
                        printWave(waveData);
                        CommonFunctions.Msg(LaserWaveActivity.this, "获取成功");
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    CommonFunctions.Msg(LaserWaveActivity.this, "操作失败");
                }
            }
        }
    };

    private void printWave(int[] wd) {
        mScatterChart.getDescription().setEnabled(false);
        mLineChart_ji.getDescription().setEnabled(false);

        EditText et = (EditText) findViewById(R.id.centerPos);
        int centerPos = Integer.parseInt(et.getText().toString());

        //设置数据
        entries_ji = new ArrayList<>();
        for (int i = 0; i < wd.length; i++) {
            entries_ji.add(new Entry(i, wd[i]));
        }
        Collections.sort(entries_ji, new EntryXComparator());//必须排序  不排序会报错
        LineDataSet lineDataSet_ji = new LineDataSet(entries_ji, "极坐标");
        lineDataSet_ji.setColor(Color.RED);
        lineDataSet_ji.setLineWidth(2);
        lineDataSet_ji.setDrawCircles(false);
        lineDataSet_ji.setDrawValues(false);
        LineData data_ji = new LineData(lineDataSet_ji);
        mLineChart_ji.setData(data_ji);
        mLineChart_ji.invalidate();


        entries_zj = new ArrayList<>();
        float minX = 0;
        float maxX = 0;
        //设置数据
        for (int i = 0; i < wd.length; i++) {
            float x = (float) (wd[i] * Math.cos(Math.PI * (i + 180 - centerPos) / 360) * (-1));
            float y = (float) (wd[i] * Math.sin(Math.PI * (i + 180 - centerPos) / 360));
            entries_zj.add(new Entry(x, y, i));
        }
        Collections.sort(entries_zj, new EntryXComparator());//必须排序  不排序会报错
        /*
        LineDataSet lineDataSet_zj = new LineDataSet(entries_zj, "直角坐标");
        lineDataSet_zj.setColor(Color.parseColor("#FAFAFA"));
        lineDataSet_zj.setLineWidth(2);
        lineDataSet_zj.setDrawValues(false);
        lineDataSet_zj.setCircleColor(Color.RED);
        LineData data_zj = new LineData(lineDataSet_zj);
        mLineChart_zj.setData(data_zj);*/

        ScatterDataSet sds = new ScatterDataSet(entries_zj, "直角坐标");
        sds.setScatterShape(ScatterChart.ScatterShape.CROSS);
        sds.setDrawValues(false);
        sds.setColor(Color.RED);

        ArrayList<IScatterDataSet> dataSets = new ArrayList<IScatterDataSet>();
        dataSets.add(sds);
        ScatterData data = new ScatterData(dataSets);
        mScatterChart.setData(data);
        mScatterChart.invalidate();
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {
        for (int i = 0; i < entries_zj.size(); i++) {
            Entry ent = entries_zj.get(i);
            if (ent.getX() == e.getX() && ent.getY() == e.getY() && ent.getData() != null) {
                Description desc = new Description();
                desc.setText("N:" + ((Integer)ent.getData() + 1) + " X:" + e.getX() + "mm Y:" + e.getY() + "mm");
                mScatterChart.getDescription().setEnabled(true);
                mScatterChart.setDescription(desc);
                return;
            }
        }
        Description desc = new Description();
        desc.setText("X:" + e.getX() + "mm Y:" + e.getY() + "mm");
        mLineChart_ji.getDescription().setEnabled(true);
        mLineChart_ji.setDescription(desc);
    }

    @Override
    public void onNothingSelected() {

    }

    public void autoCenter() {
        /*
        int[] res = new int[361];
        for (int i = 0; i < 722; i += 2) {
            res[i / 2] = (LaserWaveActivity.this.waveData[i] << 8) + LaserWaveActivity.this.waveData[i + 1];
        }*/
        int[] res = LaserWaveActivity.this.waveData;
        float minSigma = 1000000;
        int minCenterPos = 180;
        Spinner sp = (Spinner) findViewById(R.id.laserInstallType);
        String laserInstallType = ((SpinnerData) sp.getSelectedItem()).getValue();
        for (int centerPos = 165; centerPos <= 195; centerPos++) {
            List<Entry> el = new ArrayList<Entry>();
            for (int i = 0; i < res.length; i++) {
                int x = (int) (res[i] * Math.cos(Math.PI * (i + 180 - centerPos) / 360) * (-1));
                int y = (int) (res[i] * Math.sin(Math.PI * (i + 180 - centerPos) / 360));
                el.add(new Entry(x, y));
            }
            //正装
            if ("0".equals(laserInstallType)) {

                List<Float> data = new ArrayList<Float>();
                for (int i = 0; i < el.size(); i++) {
                    if (Math.abs(el.get(i).getX()) >= 3000 && Math.abs(el.get(i).getX()) <= 8000) {
                        data.add(el.get(i).getY());
                    }
                }
                float sigmaTmp1 = CommonFunctions.standardDeviation(data);
                if (minSigma >= sigmaTmp1) {
                    minSigma = sigmaTmp1;
                    minCenterPos = centerPos;
                }
            } else {
                List<Float> data1 = new ArrayList<Float>();
                List<Float> data2 = new ArrayList<Float>();
                for (int i = 0; i < el.size(); i++) {
                    if (el.get(i).getX() >= 3000 && el.get(i).getX() <= 8000) {
                        data1.add(el.get(i).getY());
                    } else if (el.get(i).getX() >= -8000 && el.get(i).getX() <= -3000) {
                        data2.add(el.get(i).getY());
                    }
                }

                float sigmaTmp1 = CommonFunctions.standardDeviation(data1);
                float sigmaTmp2 = CommonFunctions.standardDeviation(data2);
                float sigmaTmp = sigmaTmp1 < sigmaTmp2 ? sigmaTmp1 : sigmaTmp2;
                if (minSigma >= sigmaTmp) {
                    minSigma = sigmaTmp;
                    minCenterPos = centerPos;
                }
            }
        }
        EditText et = (EditText) findViewById(centerPos);
        et.setText(minCenterPos + "");
        et = (EditText) findViewById(R.id.standardDeviation);
        et.setText((int) minSigma + "");
        printWave(res);
        CommonFunctions.Msg(LaserWaveActivity.this, "中心点:" + minCenterPos + " 标准差:" + (int) minSigma);
    }

    public void laser_wave_vertical(View v)
    {
        BluetoothClient bc = BluetoothClient.getCurInstance();
        byte[] buf = Package.getPackage_26(0xCC);
        if (bc != null && bc.sendData(buf)) {
            ;
        } else {
            CommonFunctions.Msg(LaserWaveActivity.this, "发送失败");
        }
    }

    public void laser_wave_tilt(View v)
    {
        BluetoothClient bc = BluetoothClient.getCurInstance();
        byte[] buf = Package.getPackage_26(0xDD);
        if (bc.sendData(buf)) {
            ;
        } else {
            CommonFunctions.Msg(LaserWaveActivity.this, "发送失败");
        }
    }

    public void center_set(View v)
    {
        if (LaserWaveActivity.this.waveData.length < 361) {
            return;
        }
                /*
                int[] res = new int[361];
                for (int i = 0; i < 722; i += 2) {
                    res[i / 2] = (LaserWaveActivity.this.waveData[i] << 8) + LaserWaveActivity.this.waveData[i + 1];
                }*/
        printWave(LaserWaveActivity.this.waveData);
        float minSigma = 1000000;
        Spinner sp = (Spinner) findViewById(R.id.laserInstallType);
        if ("0".equals(((SpinnerData) sp.getSelectedItem()).getValue())) {
            List<Float> data = new ArrayList<Float>();
            for (int i = 0; i < entries_zj.size(); i++) {
                if (Math.abs(entries_zj.get(i).getX()) >= 3000 && Math.abs(entries_zj.get(i).getX()) <= 8000) {
                    data.add(entries_zj.get(i).getY());
                }
            }
            float sigmaTmp1 = CommonFunctions.standardDeviation(data);
            if (minSigma >= sigmaTmp1) {
                minSigma = sigmaTmp1;
            }
        } else {
            List<Float> data1 = new ArrayList<Float>();
            List<Float> data2 = new ArrayList<Float>();
            for (int i = 0; i < entries_zj.size(); i++) {
                if (entries_zj.get(i).getX() >= 3000 && entries_zj.get(i).getX() <= 8000) {
                    data1.add(entries_zj.get(i).getY());
                } else if (entries_zj.get(i).getX() >= -8000 && entries_zj.get(i).getX() <= -3000) {
                    data2.add(entries_zj.get(i).getY());
                }
            }

            float sigmaTmp1 = CommonFunctions.standardDeviation(data1);
            float sigmaTmp2 = CommonFunctions.standardDeviation(data2);
            float sigmaTmp = sigmaTmp1 < sigmaTmp2 ? sigmaTmp1 : sigmaTmp2;
            if (minSigma >= sigmaTmp) {
                minSigma = sigmaTmp;
            }
        }
        EditText et = (EditText) findViewById(R.id.standardDeviation);
        et.setText((int) minSigma + "");
        CommonFunctions.Msg(LaserWaveActivity.this, "设置完成");
    }

    public void center_auto(View v)
    {
        if (LaserWaveActivity.this.waveData.length < 361) {
            return;
        }
        autoCenter();
    }
}
