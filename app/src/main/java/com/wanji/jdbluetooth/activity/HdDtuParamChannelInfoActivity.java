package com.wanji.jdbluetooth.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.wanji.jdbluetooth.R;
import com.wanji.jdbluetooth.beans.HDDTU;
import com.wanji.jdbluetooth.beans.SpinnerData;
import com.wanji.jdbluetooth.bluetooth.BluetoothClient;
import com.wanji.jdbluetooth.global.GlobalVariables;
import com.wanji.jdbluetooth.tools.CommonFunctions;
import com.wanji.jdbluetooth.tools.Package;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/5/11.
 */

public class HdDtuParamChannelInfoActivity extends AppCompatActivity {
    @Bind(R.id.GetChannelInfo_activity_dtuhd_param_channelinfo)
    Button mGetChannelInfo;
    @Bind(R.id.SetChannelInfo_activity_dtuhd_param_channelinfo)
    Button mSetChannelInfo;
    @Bind(R.id.ConnType1_activity_hddtu_param_netinfo)
    Spinner mConnType1;
    @Bind(R.id.DscIp1_activity_hddtu_param_systeminfo)
    EditText mDscIp1;
    @Bind(R.id.DscPort1_activity_hddtu_param_systeminfo)
    EditText mDscPort1;
    @Bind(R.id.DomainName1_activity_hddtu_param_systeminfo)
    EditText mDomainName1;
    @Bind(R.id.RegisterPack1_activity_hddtu_param_systeminfo)
    EditText mRegisterPack1;
    @Bind(R.id.HeartPack1_activity_hddtu_param_systeminfo)
    EditText mHeartPack1;
    @Bind(R.id.ConnType2_activity_hddtu_param_netinfo)
    Spinner mConnType2;
    @Bind(R.id.DscIp2_activity_hddtu_param_systeminfo)
    EditText mDscIp2;
    @Bind(R.id.DscPort2_activity_hddtu_param_systeminfo)
    EditText mDscPort2;
    @Bind(R.id.DomainName2_activity_hddtu_param_systeminfo)
    EditText mDomainName2;
    @Bind(R.id.RegisterPack2_activity_hddtu_param_systeminfo)
    EditText mRegisterPack2;
    @Bind(R.id.HeartPack2_activity_hddtu_param_systeminfo)
    EditText mHeartPack2;
    @Bind(R.id.ConnType3_activity_hddtu_param_netinfo)
    Spinner mConnType3;
    @Bind(R.id.DscIp3_activity_hddtu_param_systeminfo)
    EditText mDscIp3;
    @Bind(R.id.DscPort3_activity_hddtu_param_systeminfo)
    EditText mDscPort3;
    @Bind(R.id.DomainName3_activity_hddtu_param_systeminfo)
    EditText mDomainName3;
    @Bind(R.id.RegisterPack3_activity_hddtu_param_systeminfo)
    EditText mRegisterPack3;
    @Bind(R.id.HeartPack3_activity_hddtu_param_systeminfo)
    EditText mHeartPack3;
    @Bind(R.id.ConnType4_activity_hddtu_param_netinfo)
    Spinner mConnType4;
    @Bind(R.id.DscIp4_activity_hddtu_param_systeminfo)
    EditText mDscIp4;
    @Bind(R.id.DscPort4_activity_hddtu_param_systeminfo)
    EditText mDscPort4;
    @Bind(R.id.DomainName4_activity_hddtu_param_systeminfo)
    EditText mDomainName4;
    @Bind(R.id.RegisterPack4_activity_hddtu_param_systeminfo)
    EditText mRegisterPack4;
    @Bind(R.id.HeartPack4_activity_hddtu_param_systeminfo)
    EditText mHeartPack4;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dtuhd_param_channelinfo);
        ButterKnife.bind(this);

        initView();

        //蓝牙收数广播
        BluetoothClient bc = BluetoothClient.getCurInstance();
        if (bc != null) {
            bc.setParseType(GlobalVariables.PACKAGE_PARSE_TYPE_DTU_HD);//设置解析方式
        }
        GlobalVariables.localBroadcastManager = LocalBroadcastManager.getInstance(this);
        IntentFilter mSocketFilter = new IntentFilter();
        mSocketFilter.addAction(GlobalVariables.SOCKET_BUFFER_BROADCAST);
        //注册收数广播
        GlobalVariables.localBroadcastManager.registerReceiver(mSocketReceiver, mSocketFilter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (GlobalVariables.localBroadcastManager != null) {
            GlobalVariables.localBroadcastManager.unregisterReceiver(mSocketReceiver);//注销广播
        }
    }

    private void initView() {
        mSetChannelInfo.setEnabled(false);//禁用设置按钮

        Bundle bundle = getIntent().getExtras();

        List<SpinnerData> lst = new ArrayList<>();
        lst.add(new SpinnerData("0", "UDP"));
        lst.add(new SpinnerData("1", "TCP"));
        lst.add(new SpinnerData("2", "UDP+DDP"));
        lst.add(new SpinnerData("3", "TCP+DDP"));
        lst.add(new SpinnerData("4", "SMS"));
        lst.add(new SpinnerData("5", "TCP_SER"));
        ArrayAdapter<SpinnerData> adapter = new ArrayAdapter<SpinnerData>(HdDtuParamChannelInfoActivity.this,
                android.R.layout.simple_spinner_item, lst);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mConnType1.setAdapter(adapter);
        mConnType2.setAdapter(adapter);
        mConnType3.setAdapter(adapter);
        mConnType4.setAdapter(adapter);

        mDscIp1.setText(bundle.getString("Dsc1IP"));
        mDscPort1.setText(bundle.getString("Dsc1Port"));
        mDomainName1.setText(bundle.getString("Dsc1YM"));
        mRegisterPack1.setText(bundle.getString("Dsc1RegPack"));
        mHeartPack1.setText(bundle.getString("DSC1Heart"));

        mDscIp2.setText(bundle.getString("Dsc2IP"));
        mDscPort2.setText(bundle.getString("Dsc2Port"));
        mDomainName2.setText(bundle.getString("Dsc2YM"));
        mRegisterPack2.setText(bundle.getString("Dsc2RegPack"));
        mHeartPack2.setText(bundle.getString("DSC2Heart"));

        mDscIp3.setText(bundle.getString("Dsc3IP"));
        mDscPort3.setText(bundle.getString("Dsc3Port"));
        mDomainName3.setText(bundle.getString("Dsc3YM"));
        mRegisterPack3.setText(bundle.getString("Dsc3RegPack"));
        mHeartPack3.setText(bundle.getString("DSC3Heart"));

        mDscIp4.setText(bundle.getString("Dsc4IP"));
        mDscPort4.setText(bundle.getString("Dsc4Port"));
        mDomainName4.setText(bundle.getString("Dsc4YM"));
        mRegisterPack4.setText(bundle.getString("Dsc4RegPack"));
        mHeartPack4.setText(bundle.getString("DSC4Heart"));

    }

    @OnClick({R.id.GetChannelInfo_activity_dtuhd_param_channelinfo, R.id.SetChannelInfo_activity_dtuhd_param_channelinfo})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.GetChannelInfo_activity_dtuhd_param_channelinfo:
                BluetoothClient bc = BluetoothClient.getCurInstance();
                if (bc == null) {
                    CommonFunctions.Msg(HdDtuParamChannelInfoActivity.this, "请连接蓝牙");
                } else {
                    byte[] buf = Package.getAllParam_DtuHD();
                    if (bc.sendData(buf)) {
                        ;
                    } else {
                        CommonFunctions.Msg(HdDtuParamChannelInfoActivity.this, "发送失败");
                    }
                }
                break;
            case R.id.SetChannelInfo_activity_dtuhd_param_channelinfo:
                new AlertDialog.Builder(this)
                        .setTitle("设置")
                        .setMessage("确认设置？")
                        .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                BluetoothClient bcSet = BluetoothClient.getCurInstance();
                                if (bcSet == null) {
                                    CommonFunctions.Msg(HdDtuParamChannelInfoActivity.this, "请连接蓝牙");
                                } else {
                                    List<Package.ChannelData>[] lst = new List[4];
                                    lst[0] = new ArrayList<>();
                                    lst[0].add(new Package.ChannelData("CHPCT", mConnType1.getSelectedItem().toString()));
                                    lst[0].add(new Package.ChannelData("CHPDSCIP", mDscIp1.getText().toString()));
                                    lst[0].add(new Package.ChannelData("CHPDSCP", mDscPort1.getText().toString()));
                                    lst[0].add(new Package.ChannelData("CHPDSCD", mDomainName1.getText().toString()));
                                    lst[0].add(new Package.ChannelData("CHPCRP", mRegisterPack1.getText().toString()));
                                    lst[0].add(new Package.ChannelData("CHPCHP", mHeartPack1.getText().toString()));
                                    lst[1] = new ArrayList<>();
                                    lst[1].add(new Package.ChannelData("CHPCT", mConnType2.getSelectedItem().toString()));
                                    lst[1].add(new Package.ChannelData("CHPDSCIP", mDscIp2.getText().toString()));
                                    lst[1].add(new Package.ChannelData("CHPDSCP", mDscPort2.getText().toString()));
                                    lst[1].add(new Package.ChannelData("CHPDSCD", mDomainName2.getText().toString()));
                                    lst[1].add(new Package.ChannelData("CHPCRP", mRegisterPack2.getText().toString()));
                                    lst[1].add(new Package.ChannelData("CHPCHP", mHeartPack2.getText().toString()));
                                    lst[2] = new ArrayList<>();
                                    lst[2].add(new Package.ChannelData("CHPCT", mConnType3.getSelectedItem().toString()));
                                    lst[2].add(new Package.ChannelData("CHPDSCIP", mDscIp3.getText().toString()));
                                    lst[2].add(new Package.ChannelData("CHPDSCP", mDscPort3.getText().toString()));
                                    lst[2].add(new Package.ChannelData("CHPDSCD", mDomainName3.getText().toString()));
                                    lst[2].add(new Package.ChannelData("CHPCRP", mRegisterPack3.getText().toString()));
                                    lst[2].add(new Package.ChannelData("CHPCHP", mHeartPack3.getText().toString()));
                                    lst[3] = new ArrayList<>();
                                    lst[3].add(new Package.ChannelData("CHPCT", mConnType4.getSelectedItem().toString()));
                                    lst[3].add(new Package.ChannelData("CHPDSCIP", mDscIp4.getText().toString()));
                                    lst[3].add(new Package.ChannelData("CHPDSCP", mDscPort4.getText().toString()));
                                    lst[3].add(new Package.ChannelData("CHPDSCD", mDomainName4.getText().toString()));
                                    lst[3].add(new Package.ChannelData("CHPCRP", mRegisterPack4.getText().toString()));
                                    lst[3].add(new Package.ChannelData("CHPCHP", mHeartPack4.getText().toString()));

                                    byte[] buf = Package.setDtuChannelParam(lst);
                                    if (bcSet.sendData(buf)) {
                                        ;
                                    } else {
                                        CommonFunctions.Msg(HdDtuParamChannelInfoActivity.this, "发送失败");
                                    }
                                }
                            }
                        })
                        .setNegativeButton("取消", null)
                        .show();
                break;
        }
    }

    /**
     * 广播接收器
     */
    private BroadcastReceiver mSocketReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (GlobalVariables.SOCKET_BUFFER_BROADCAST.equals(action)) {
                String value = intent.getStringExtra(GlobalVariables.SOCKET_BUFFER_DATA);//提取数据
                if (value.contains("AT+GETPARAM?")) {
                    //返回：获取全部参数
                    Log.d("HdDtuParamChannelInfo", "获取全部参数");
                    //参数装载
                    ShowParam(value);
                }else if(value.contains("AT+SAVEPARAM\r\n\r\nOK")){
                    //返回：参数设置成功
                    Log.d("HdDtuParamChannelInfo", "设置参数成功");
                    CommonFunctions.Msg(HdDtuParamChannelInfoActivity.this, "设置成功");
                }else if(value.contains("AT+SAVEPARAM\r\n\r\nERROR")){
                    //返回：参数设置失败
                    Log.d("HdDtuParamChannelInfo", "设置参数失败");
                    CommonFunctions.Msg(HdDtuParamChannelInfoActivity.this, "设置失败");
                }
            }
        }
    };

    /**
     * 参数显示
     * @param value
     */
    private void ShowParam(String value) {
        HDDTU dtu = new HDDTU();

        try{
            //通道信息
            //通道1
            String strChannel = value.substring(value.indexOf("[CHANNEL1  PARAMETER(S)]"));
            String strTmp = strChannel.substring(strChannel.indexOf("CHPCT=") + 6);//通讯方式1
            dtu.getHDDTUParam().DSC1CSMS = strTmp.substring(0, strTmp.indexOf("\r\n"));
            strTmp = strChannel.substring(strChannel.indexOf("CHPDSCIP=") + 9);//目标IP1
            dtu.getHDDTUParam().Dsc1IP = strTmp.substring(0, strTmp.indexOf("\r\n"));
            strTmp = strChannel.substring(strChannel.indexOf("CHPDSCP=") + 8);//目标端口1
            dtu.getHDDTUParam().Dsc1Port = strTmp.substring(0, strTmp.indexOf("\r\n"));
            strTmp = strChannel.substring(strChannel.indexOf("CHPDSCD=") + 8);//目标域名1
            dtu.getHDDTUParam().Dsc1YM = strTmp.substring(0, strTmp.indexOf("\r\n"));
            strTmp = strChannel.substring(strChannel.indexOf("CHPCRP=") + 7);//注册包1
            dtu.getHDDTUParam().Dsc1RegPack = strTmp.substring(0, strTmp.indexOf("\r\n"));
            strTmp = strChannel.substring(strChannel.indexOf("CHPCHP=") + 7);//心跳包1
            dtu.getHDDTUParam().DSC1Heart = strTmp.substring(0, strTmp.indexOf("\r\n"));
            //通道2
            strChannel = value.substring(value.indexOf("[CHANNEL2  PARAMETER(S)]"));
            strTmp = strChannel.substring(strChannel.indexOf("CHPCT=") + 6);//通讯方式2
            dtu.getHDDTUParam().DSC2CSMS = strTmp.substring(0, strTmp.indexOf("\r\n"));
            strTmp = strChannel.substring(strChannel.indexOf("CHPDSCIP=") + 9);//目标IP2
            dtu.getHDDTUParam().Dsc2IP = strTmp.substring(0, strTmp.indexOf("\r\n"));
            strTmp = strChannel.substring(strChannel.indexOf("CHPDSCP=") + 8);//目标端口2
            dtu.getHDDTUParam().Dsc2Port = strTmp.substring(0, strTmp.indexOf("\r\n"));
            strTmp = strChannel.substring(strChannel.indexOf("CHPDSCD=") + 8);//目标域名2
            dtu.getHDDTUParam().Dsc2YM = strTmp.substring(0, strTmp.indexOf("\r\n"));
            strTmp = strChannel.substring(strChannel.indexOf("CHPCRP=") + 7);//注册包2
            dtu.getHDDTUParam().Dsc2RegPack = strTmp.substring(0, strTmp.indexOf("\r\n"));
            strTmp = strChannel.substring(strChannel.indexOf("CHPCHP=") + 7);//心跳包2
            dtu.getHDDTUParam().DSC2Heart = strTmp.substring(0, strTmp.indexOf("\r\n"));
            //通道3
            strChannel = value.substring(value.indexOf("[CHANNEL3  PARAMETER(S)]"));
            strTmp = strChannel.substring(strChannel.indexOf("CHPCT=") + 6);//通讯方式3
            dtu.getHDDTUParam().DSC3CSMS = strTmp.substring(0, strTmp.indexOf("\r\n"));
            strTmp = strChannel.substring(strChannel.indexOf("CHPDSCIP=") + 9);//目标IP3
            dtu.getHDDTUParam().Dsc3IP = strTmp.substring(0, strTmp.indexOf("\r\n"));
            strTmp = strChannel.substring(strChannel.indexOf("CHPDSCP=") + 8);//目标端口3
            dtu.getHDDTUParam().Dsc3Port = strTmp.substring(0, strTmp.indexOf("\r\n"));
            strTmp = strChannel.substring(strChannel.indexOf("CHPDSCD=") + 8);//目标域名3
            dtu.getHDDTUParam().Dsc3YM = strTmp.substring(0, strTmp.indexOf("\r\n"));
            strTmp = strChannel.substring(strChannel.indexOf("CHPCRP=") + 7);//注册包3
            dtu.getHDDTUParam().Dsc3RegPack = strTmp.substring(0, strTmp.indexOf("\r\n"));
            strTmp = strChannel.substring(strChannel.indexOf("CHPCHP=") + 7);//心跳包3
            dtu.getHDDTUParam().DSC3Heart = strTmp.substring(0, strTmp.indexOf("\r\n"));
            //通道4
            strChannel = value.substring(value.indexOf("[CHANNEL4  PARAMETER(S)]"));
            strTmp = strChannel.substring(strChannel.indexOf("CHPCT=") + 6);//通讯方式4
            dtu.getHDDTUParam().DSC4CSMS = strTmp.substring(0, strTmp.indexOf("\r\n"));
            strTmp = strChannel.substring(strChannel.indexOf("CHPDSCIP=") + 9);//目标IP4
            dtu.getHDDTUParam().Dsc4IP = strTmp.substring(0, strTmp.indexOf("\r\n"));
            strTmp = strChannel.substring(strChannel.indexOf("CHPDSCP=") + 8);//目标端口4
            dtu.getHDDTUParam().Dsc4Port = strTmp.substring(0, strTmp.indexOf("\r\n"));
            strTmp = strChannel.substring(strChannel.indexOf("CHPDSCD=") + 8);//目标域名4
            dtu.getHDDTUParam().Dsc4YM = strTmp.substring(0, strTmp.indexOf("\r\n"));
            strTmp = strChannel.substring(strChannel.indexOf("CHPCRP=") + 7);//注册包4
            dtu.getHDDTUParam().Dsc4RegPack = strTmp.substring(0, strTmp.indexOf("\r\n"));
            strTmp = strChannel.substring(strChannel.indexOf("CHPCHP=") + 7);//心跳包4
            dtu.getHDDTUParam().DSC4Heart = strTmp.substring(0, strTmp.indexOf("\r\n"));

            CommonFunctions.setSpinnerItemSelectedByText(mConnType1, dtu.getHDDTUParam().DSC1CSMS);
            CommonFunctions.setSpinnerItemSelectedByText(mConnType2, dtu.getHDDTUParam().DSC2CSMS);
            CommonFunctions.setSpinnerItemSelectedByText(mConnType3, dtu.getHDDTUParam().DSC3CSMS);
            CommonFunctions.setSpinnerItemSelectedByText(mConnType4, dtu.getHDDTUParam().DSC4CSMS);

            mDscIp1.setText(dtu.getHDDTUParam().Dsc1IP);
            mDscPort1.setText(dtu.getHDDTUParam().Dsc1Port);
            mDomainName1.setText(dtu.getHDDTUParam().Dsc1YM);
            mRegisterPack1.setText(dtu.getHDDTUParam().Dsc1RegPack);
            mHeartPack1.setText(dtu.getHDDTUParam().DSC1Heart);

            mDscIp2.setText(dtu.getHDDTUParam().Dsc2IP);
            mDscPort2.setText(dtu.getHDDTUParam().Dsc2Port);
            mDomainName2.setText(dtu.getHDDTUParam().Dsc2YM);
            mRegisterPack2.setText(dtu.getHDDTUParam().Dsc2RegPack);
            mHeartPack2.setText(dtu.getHDDTUParam().DSC2Heart);

            mDscIp3.setText(dtu.getHDDTUParam().Dsc3IP);
            mDscPort3.setText(dtu.getHDDTUParam().Dsc3Port);
            mDomainName3.setText(dtu.getHDDTUParam().Dsc3YM);
            mRegisterPack3.setText(dtu.getHDDTUParam().Dsc3RegPack);
            mHeartPack3.setText(dtu.getHDDTUParam().DSC3Heart);

            mDscIp4.setText(dtu.getHDDTUParam().Dsc4IP);
            mDscPort4.setText(dtu.getHDDTUParam().Dsc4Port);
            mDomainName4.setText(dtu.getHDDTUParam().Dsc4YM);
            mRegisterPack4.setText(dtu.getHDDTUParam().Dsc4RegPack);
            mHeartPack4.setText(dtu.getHDDTUParam().DSC4Heart);

            mSetChannelInfo.setEnabled(true);//启用设置按钮
            CommonFunctions.Msg(this, "查询成功");
        }catch (Exception e){
            e.printStackTrace();
            CommonFunctions.Msg(this, "查询失败");
        }

    }
}
