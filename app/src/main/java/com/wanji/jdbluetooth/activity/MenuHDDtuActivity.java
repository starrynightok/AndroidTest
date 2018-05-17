package com.wanji.jdbluetooth.activity;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.wanji.jdbluetooth.R;
import com.wanji.jdbluetooth.beans.HDDTU;
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
 * Created by Administrator on 2018/5/9.
 */

public class MenuHDDtuActivity extends AppCompatActivity {

    @Bind(R.id.serialno_activity_hddtu_menu)
    TextView mTextSerialno;
    @Bind(R.id.hardwarever_activity_hddtu_menu)
    TextView mTextHardwarever;
    @Bind(R.id.hddtu_menu_listview_parammenu)
    ListView mListHddtuMenuListview;
    @Bind(R.id.connectdtu_activity_hddtu_menu)
    Button mBtnConnectdtu;

    SimpleAdapter mListItemAdapter;
    List<String> mListMenu = new ArrayList<>();
    ArrayList<HashMap<String, Object>> mListMenuData = new ArrayList<>();

    ProgressDialog mProgress = null;

    boolean mBoolLogin = false;//登录DTU状态
    int mHdDtuMode = HdDtuMode.MODE_DATA;

    Object mSwitchATEvent = new Object(), mATTestEvent = new Object(), mLoginEvent = new Object(), mLogoutEvent = new Object();
    boolean mSwitchATMode = false, mATTestOK = false, mLoginOK = false, mLogoutOK = false;//DTU登录过程中的几种状态标识:AT模式，AT测试，AT登录
    @Bind(R.id.ResetDtu_activity_hddtu_menu)
    Button mResetDtu;

    //DTU工作状态标志
    public class HdDtuMode {
        public final static int MODE_DATA = 0;//数据通讯状态
        public final static int MODE_AT = 1;//AT模式
        /*
        public final static int STATE_AT_PREPARE = 1;//AT准备状态
        public final static int STATE_AT_OK = 2;//AT测试OK
        public final static int STATE_AT_ERROR = 3;//AT测试错误
        public final static int STATE_AT_LOGIN_OK = 4;//AT登录OK
        public final static int STATE_AT_LOGIN_ERROR = 5;//AT登录ERROR
        public final static int STATE_AT_LOGOUT_OK = 6;//AT登出OK
        public final static int STATE_AT_LOGOUT_ERROR = 7;//AT登出ERROR*/
    }

    HDDTU mHdDtu = new HDDTU();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hddtu_menu);
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            BluetoothClient bc = BluetoothClient.getCurInstance();
            if (bc != null && mBoolLogin) {
                new AlertDialog.Builder(this)
                        .setTitle("提示")
                        .setMessage("离开前请确认：是否重启DTU？")
                        .setPositiveButton("重启", mListenerKeyDown)
                        .setNegativeButton("不重启", mListenerKeyDown)
                        .show();
                return false;
            }
            return super.onKeyDown(keyCode, event);
        }
        return  super.onKeyDown(keyCode, event);
    }

    DialogInterface.OnClickListener mListenerKeyDown = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which){
                case AlertDialog.BUTTON_POSITIVE:
                    BluetoothClient bc = BluetoothClient.getCurInstance();
                    byte[] buf = Package.resetHdDtu();
                    if (bc.sendData(buf)) {
                        ;
                    } else {
                        CommonFunctions.Msg(MenuHDDtuActivity.this, "发送失败");
                    }
                    break;
                case AlertDialog.BUTTON_NEGATIVE:
                    break;
            }
            finish();
        }
    };

    private void initView() {
        mResetDtu.setEnabled(false);//禁用重启DTU按钮

        mListMenu.add(getResources().getString(R.string.systeminfo_activity_hddtu_menu));
        mListMenu.add(getResources().getString(R.string.netinfo_activity_hddtu_menu));
        mListMenu.add(getResources().getString(R.string.serialinfo_activity_hddtu_menu));
        mListMenu.add(getResources().getString(R.string.channelinfo_activity_hddtu_menu));
        mListMenu.add(getResources().getString(R.string.serverinfo_activity_hddtu_menu));

        mListHddtuMenuListview.setOnItemClickListener(new ItemClick());

        for (int i = 0; i < mListMenu.size(); i++) {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("menu_image", R.drawable.png_right);
            map.put("menu_name", mListMenu.get(i));
            mListMenuData.add(map);
        }

        //适配器
        mListItemAdapter = new SimpleAdapter(MenuHDDtuActivity.this,
                mListMenuData,
                R.layout.item_menu,
                new String[]{"menu_image", "menu_name"},
                new int[]{R.id.menu_image, R.id.menu_name}
        );
        mListHddtuMenuListview.setAdapter(mListItemAdapter);
    }

    /**
     * 蓝牙收数接收器
     */
    private BroadcastReceiver mSocketReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (GlobalVariables.SOCKET_BUFFER_BROADCAST.equals(action)) {
                String value = intent.getStringExtra(GlobalVariables.SOCKET_BUFFER_DATA);//提取数据
                if (value.contains("AT\r\nOK")) {
                    //返回：连接(+++)成功
                    //    mHdDtuMode = HdDtuMode.STATE_AT_PREPARE;
                    mHdDtuMode = HdDtuMode.MODE_AT;
                    mSwitchATMode = true;

                    synchronized (mSwitchATEvent) {
                        mSwitchATEvent.notify();
                    }

                    if (mProgress.isShowing()) {
                        mProgress.setTitle("登录DTU");
                        mProgress.setMessage("切换AT模式成功...");
                    }

                    Log.d("MenuHDDtuActivity", "AT模式");
                }/* else if (value.contains("AT\r\n\r\nOK")) {
                    //返回：AT成功
                    //    mHdDtuMode = HdDtuSwitchState.STATE_AT_OK;
                    mATTestOK = true;

                    synchronized (mATTestEvent){
                        mATTestEvent.notify();
                    }

                    if(mProgress.isShowing()){
                        mProgress.setTitle("登录DTU");
                        mProgress.setMessage("AT测试成功...");
                    }

                    Log.d("MenuHDDtuActivity", "AT响应成功");
                } else if (value.contains("AT\r\n\r\nERROR")) {
                    //返回：失败
                    //    mHdDtuMode = HdDtuSwitchState.STATE_AT_ERROR;
                    mATTestOK = false;

                    synchronized (mATTestEvent){
                        mATTestEvent.notify();
                    }

                    if(mProgress.isShowing()){
                        mProgress.setTitle("登录DTU");
                        mProgress.setMessage("AT测试失败...");
                    }

                    Log.d("MenuHDDtuActivity", "操作错误");
                } */ else if (value.contains("AT+LOGIN=admin\r\n\r\nOK")) {
                    //返回：登录成功
                    //    mHdDtuSwitchState = HdDtuSwitchState.STATE_AT_LOGIN_OK;
                    mLoginOK = true;

                    synchronized (mLoginEvent) {
                        mLoginEvent.notify();
                    }

                    if (mProgress.isShowing()) {
                        mProgress.setTitle("登录DTU");
                        mProgress.setMessage("登录DTU成功...");
                    }

                    Log.d("MenuHDDtuActivity", "登录成功");
                } else if (value.contains("AT+LOGIN=admin\r\n\r\nERROR")) {
                    //返回：登录失败
                    //   mHdDtuSwitchState = HdDtuSwitchState.STATE_AT_LOGIN_ERROR;
                    mLoginOK = false;

                    synchronized (mLoginEvent) {
                        mLoginEvent.notify();
                    }

                    if (mProgress.isShowing()) {
                        mProgress.setTitle("登录DTU");
                        mProgress.setMessage("登录DTU失败...");
                    }

                    Log.d("MenuHDDtuActivity", "登录失败");
                } else if (value.contains("AT+LOGOUT\r\n\r\nOK")) {
                    //返回：登出成功
                    //    mHdDtuSwitchState = HdDtuSwitchState.STATE_AT_LOGOUT_OK;
                    mLogoutOK = true;

                    synchronized (mLogoutEvent) {
                        mLogoutEvent.notify();
                    }

                    if (mProgress.isShowing()) {
                        mProgress.setTitle("登出DTU");
                        mProgress.setMessage("登出DTU成功...");
                    }

                    Log.d("MenuHDDtuActivity", "登出成功");
                } else if (value.contains("AT+LOGOUT\r\n\r\nERROR")) {
                    //返回：登出失败
                    //    mHdDtuSwitchState = HdDtuSwitchState.STATE_AT_LOGOUT_ERROR;
                    mLogoutOK = false;

                    synchronized (mLogoutEvent) {
                        mLogoutEvent.notify();
                    }

                    if (mProgress.isShowing()) {
                        mProgress.setTitle("登出DTU");
                        mProgress.setMessage("登出DTU失败...");
                    }

                    Log.d("MenuHDDtuActivity", "登出失败");
                } else if (value.contains("AT+GETPARAM?")) {
                    //返回：获取全部参数
                    Log.d("MenuHDDtuActivity", "获取全部参数");
                    //参数装载
                    BasicInfoShow(LoadAllParam(value));
                }
            }
        }
    };

    private Handler mDtuConnHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case -1:
                    mBoolLogin = false;
                    CommonFunctions.Msg(MenuHDDtuActivity.this, "DTU未响应或操作失败");
                    break;
                case 0:
                    mBoolLogin = true;
                    mBtnConnectdtu.setText("退出登录");
                    mResetDtu.setEnabled(true);//启用重启DTU按钮
                    //mBtnConnectdtu.setBackground(getResources().getDrawable(R.drawable.selector_rectangle));
                    CommonFunctions.Msg(MenuHDDtuActivity.this, "登录DTU成功");
                    break;
                case 1:
                    mBoolLogin = false;
                    mBtnConnectdtu.setText("登录DTU");
                    CommonFunctions.Msg(MenuHDDtuActivity.this, "登录DTU失败");
                    break;
                case 2:
                    mBoolLogin = false;
                    mBtnConnectdtu.setText("登录DTU");
                    mBtnConnectdtu.setEnabled(false);//按钮不可点击
                    mResetDtu.setEnabled(false);//禁用重启DTU按钮
                    CommonFunctions.Msg(MenuHDDtuActivity.this, "退出登录成功");
                    //控制按钮10s后才能点击
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            int num = 20;
                            while (num >= 0) {
                                try {
                                    Message msg = new Message();
                                    msg.what = 3;
                                    msg.arg1 = num;
                                    mDtuConnHandler.sendMessage(msg);
                                    num--;
                                    Thread.sleep(1000);
                                } catch (InterruptedException ie) {
                                    ie.printStackTrace();
                                }
                            }


                        }
                    }).start();
                    break;
                case 3:
                    if (msg.arg1 > 0) {
                        mBtnConnectdtu.setText("登录DTU[" + msg.arg1 + "s]");
                        mBtnConnectdtu.setEnabled(false);//按钮不可点击
                    } else {
                        mBtnConnectdtu.setText("登录DTU");
                        mBtnConnectdtu.setEnabled(true);//按钮可点击
                    }
                    break;
                default:
                    break;
            }
            if (mProgress != null) {
                mProgress.cancel();
            }
        }
    };

    @OnClick(R.id.connectdtu_activity_hddtu_menu)
    public void onViewConnectdtuClicked() {
        mSwitchATMode = false;
        mATTestOK = false;
        mLoginOK = false;
        mLogoutOK = false;

        final BluetoothClient bc = BluetoothClient.getCurInstance();
        if (bc == null) {
            CommonFunctions.Msg(MenuHDDtuActivity.this, "请连接蓝牙");
        } else if (!mBoolLogin) {
            mProgress = ProgressDialog.show(MenuHDDtuActivity.this, "登录DTU", "正在登录DTU...");
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Message msg = new Message();

                    if (bc.sendData(Package.switchATMode_DtuHD())) {//转换AT模式
                        synchronized (mSwitchATEvent) {
                            try {
                                mSwitchATEvent.wait(3 * 1000);

                                if (!mSwitchATMode) {
                                    //超时未响应，或响应失败
                                    msg.what = -1;
                                } else {
                                    /*if(bc.sendData(Package.atTest_DtuHD())){//测试AT
                                        synchronized (mATTestEvent) {
                                            try{
                                                mATTestEvent.wait(3 * 1000);

                                                if(!mATTestOK){
                                                    //超时或者响应失败
                                                    msg.what = -1;
                                                }else{*/
                                    if (bc.sendData(Package.login_DtuHD())) {//登录DTU
                                        synchronized (mLoginEvent) {
                                            try {
                                                mLoginEvent.wait(2 * 1000);

                                                if (!mLoginOK) {
                                                    //超时或响应失败，重发

                                                    if (bc.sendData(Package.login_DtuHD())) {//登录DTU

                                                        try {
                                                            mLoginEvent.wait(2 * 1000);

                                                            if (!mLoginOK) {
                                                                //超时或响应失败
                                                                msg.what = -1;
                                                            } else {
                                                                mBoolLogin = true;
                                                                if (bc.sendData(Package.getAllParam_DtuHD())) {
                                                                    //发送获取全部参数指令
                                                                    msg.what = 0;
                                                                    //CommonFunctions.Msg(MenuHDDtuActivity.this, "[获取参数]指令发送成功");
                                                                } else {
                                                                    msg.what = -2;
                                                                    //CommonFunctions.Msg(MenuHDDtuActivity.this, "[获取参数]指令发送失败");
                                                                }
                                                            }
                                                        } catch (InterruptedException ie) {
                                                            ie.printStackTrace();
                                                        }

                                                    } else {
                                                        msg.what = -2;
                                                        //CommonFunctions.Msg(MenuHDDtuActivity.this, "[登录DTU]指令发送失败");
                                                    }
                                                } else {
                                                    mBoolLogin = true;
                                                    if (bc.sendData(Package.getAllParam_DtuHD())) {
                                                        //发送获取全部参数指令
                                                        msg.what = 0;
                                                        //CommonFunctions.Msg(MenuHDDtuActivity.this, "[获取参数]指令发送成功");
                                                    } else {
                                                        msg.what = -2;
                                                        //CommonFunctions.Msg(MenuHDDtuActivity.this, "[获取参数]指令发送失败");
                                                    }
                                                }
                                            } catch (InterruptedException ie) {
                                                ie.printStackTrace();
                                            }
                                        }
                                    } else {
                                        msg.what = -2;
                                        //CommonFunctions.Msg(MenuHDDtuActivity.this, "[登录DTU]指令发送失败");
                                    }
                                                /*}
                                            }catch (InterruptedException ie){
                                                ie.printStackTrace();
                                            }
                                        }
                                    }else{
                                        msg.what = -2;
                                        //CommonFunctions.Msg(MenuHDDtuActivity.this, "[AT测试]指令发送失败");
                                    }*/
                                }
                            } catch (InterruptedException ie) {
                                ie.printStackTrace();
                            }
                        }
                    } else {
                        msg.what = -2;
                        //CommonFunctions.Msg(MenuHDDtuActivity.this, "[转换AT模式]指令发送失败");
                    }

                    mDtuConnHandler.sendMessage(msg);//发送消息
                }
            }).start();
        } else if (mBoolLogin) {
            //退出登录DTU
            mProgress = ProgressDialog.show(MenuHDDtuActivity.this, "登出DTU", "正在登出DTU...");
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Message msg = new Message();

                    if (bc.sendData(Package.logout_DtuHD())) {//退出DTU登录
                        synchronized (mLogoutEvent) {
                            try {
                                mLogoutEvent.wait(3 * 1000);

                                if (!mLogoutOK) {
                                    //超时或操作失败
                                    msg.what = -1;
                                } else {
                                    msg.what = 2;//退出登录成功
                                    mBoolLogin = false;
                                }
                            } catch (InterruptedException ie) {
                                ie.printStackTrace();
                            }
                        }
                    }
                    mDtuConnHandler.sendMessage(msg);//发送消息
                }
            }).start();
        }
    }

    @OnClick(R.id.ResetDtu_activity_hddtu_menu)
    public void onViewResetDtuClicked() {
        final BluetoothClient bc = BluetoothClient.getCurInstance();
        if (bc == null) {
            CommonFunctions.Msg(MenuHDDtuActivity.this, "请连接蓝牙");
            return;
        } else if (!mBoolLogin) {
            CommonFunctions.Msg(MenuHDDtuActivity.this, "请先登录DTU！");
            return;
        }

        new AlertDialog.Builder(this)
                .setTitle("重启DTU")
                .setMessage("确定重启？")
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        byte[] buf = Package.resetHdDtu();
                        if (bc.sendData(buf)) {
                            Message msg = new Message();
                            msg.what = 2;
                            mDtuConnHandler.sendMessage(msg);
                        } else {
                            CommonFunctions.Msg(MenuHDDtuActivity.this, "发送失败");
                        }
                    }
                })
                .setNegativeButton("取消", null)
                .show();
    }

    public class ItemClick implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (!mBoolLogin) {
                CommonFunctions.Msg(MenuHDDtuActivity.this, "请先登录DTU！");
                return;
            }

            if (id == 0) {
                Intent intentSystemInfo = new Intent(MenuHDDtuActivity.this, HdDtuParamSystemInfoActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("HardwareVer", mHdDtu.getHDDTUParam().HardwareVer);
                bundle.putString("SoftwareVer", mHdDtu.getHDDTUParam().SoftwareVer);
                bundle.putString("SerialNo", mHdDtu.getHDDTUParam().SerialNo);
                bundle.putString("PhoneID", mHdDtu.getHDDTUParam().PhoneID);
                intentSystemInfo.putExtras(bundle);
                startActivity(intentSystemInfo);
            } else if (id == 1) {
                Intent intentNetInfo = new Intent(MenuHDDtuActivity.this, HdDtuParamNetInfoActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("AccessPoint", mHdDtu.getHDDTUParam().AccessPoint);
                bundle.putString("AccessUser", mHdDtu.getHDDTUParam().AccessUser);
                bundle.putString("AccessPsw", mHdDtu.getHDDTUParam().AccessPsw);
                bundle.putString("AccessServerCode", mHdDtu.getHDDTUParam().AccessServerCode);
                intentNetInfo.putExtras(bundle);
                startActivity(intentNetInfo);
            } else if (id == 2) {
                Intent intentSerialInfo = new Intent(MenuHDDtuActivity.this, HdDtuParamSerialInfoActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("ComBaudRate", mHdDtu.getHDDTUParam().ComBaudRate);
                bundle.putString("DebugInfo", mHdDtu.getHDDTUParam().DebugInfo);
                bundle.putString("RDPSwitch", mHdDtu.getHDDTUParam().RDPSwitch);
                bundle.putString("GPRSProtocal", mHdDtu.getHDDTUParam().GPRSProtocal);
                intentSerialInfo.putExtras(bundle);
                startActivity(intentSerialInfo);
            } else if (id == 3) {
                Intent intentChannelInfo = new Intent(MenuHDDtuActivity.this, HdDtuParamChannelInfoActivity.class);
                Bundle bundle = new Bundle();
                //通道1
                bundle.putString("DSC1CSMS", mHdDtu.getHDDTUParam().DSC1CSMS);
                bundle.putString("Dsc1IP", mHdDtu.getHDDTUParam().Dsc1IP);
                bundle.putString("Dsc1Port", mHdDtu.getHDDTUParam().Dsc1Port);
                bundle.putString("Dsc1YM", mHdDtu.getHDDTUParam().Dsc1YM);
                bundle.putString("Dsc1RegPack", mHdDtu.getHDDTUParam().Dsc1RegPack);
                bundle.putString("DSC1Heart", mHdDtu.getHDDTUParam().DSC1Heart);
                //通道2
                bundle.putString("DSC2CSMS", mHdDtu.getHDDTUParam().DSC2CSMS);
                bundle.putString("Dsc2IP", mHdDtu.getHDDTUParam().Dsc2IP);
                bundle.putString("Dsc2Port", mHdDtu.getHDDTUParam().Dsc2Port);
                bundle.putString("Dsc2YM", mHdDtu.getHDDTUParam().Dsc2YM);
                bundle.putString("Dsc2RegPack", mHdDtu.getHDDTUParam().Dsc2RegPack);
                bundle.putString("DSC2Heart", mHdDtu.getHDDTUParam().DSC2Heart);
                //通道3
                bundle.putString("DSC3CSMS", mHdDtu.getHDDTUParam().DSC3CSMS);
                bundle.putString("Dsc3IP", mHdDtu.getHDDTUParam().Dsc3IP);
                bundle.putString("Dsc3Port", mHdDtu.getHDDTUParam().Dsc3Port);
                bundle.putString("Dsc3YM", mHdDtu.getHDDTUParam().Dsc3YM);
                bundle.putString("Dsc3RegPack", mHdDtu.getHDDTUParam().Dsc3RegPack);
                bundle.putString("DSC3Heart", mHdDtu.getHDDTUParam().DSC3Heart);
                //通道4
                bundle.putString("DSC4CSMS", mHdDtu.getHDDTUParam().DSC4CSMS);
                bundle.putString("Dsc4IP", mHdDtu.getHDDTUParam().Dsc4IP);
                bundle.putString("Dsc4Port", mHdDtu.getHDDTUParam().Dsc4Port);
                bundle.putString("Dsc4YM", mHdDtu.getHDDTUParam().Dsc4YM);
                bundle.putString("Dsc4RegPack", mHdDtu.getHDDTUParam().Dsc4RegPack);
                bundle.putString("DSC4Heart", mHdDtu.getHDDTUParam().DSC4Heart);
                intentChannelInfo.putExtras(bundle);
                startActivity(intentChannelInfo);
            } else if (id == 4) {
                Intent intentServerInfo = new Intent(MenuHDDtuActivity.this, HdDtuParamServerInfoActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("ManageDscYM", mHdDtu.getHDDTUParam().ManageDscYM);
                bundle.putString("ManageDscIP", mHdDtu.getHDDTUParam().ManageDscIP);
                bundle.putString("ManageDscPort", mHdDtu.getHDDTUParam().ManageDscPort);
                intentServerInfo.putExtras(bundle);
                startActivity(intentServerInfo);
            }
        }
    }

    /**
     * 加载DTU参数
     *
     * @param value
     */
    private HDDTU LoadAllParam(String value) {
        HDDTU param = new HDDTU();

        //系统信息
        String strTmp = value.substring(value.indexOf("SPHV=") + 5);//硬件版本
        param.getHDDTUParam().HardwareVer = strTmp.substring(0, strTmp.indexOf("\r\n"));
        strTmp = value.substring(value.indexOf("SPSV=") + 5);//软件版本
        param.getHDDTUParam().SoftwareVer = strTmp.substring(0, strTmp.indexOf("\r\n"));
        strTmp = value.substring(value.indexOf("SPPID=") + 6);//序列号
        param.getHDDTUParam().SerialNo = strTmp.substring(0, strTmp.indexOf("\r\n"));
        strTmp = value.substring(value.indexOf("OPPDTUI=") + 8);//身份识别码
        param.getHDDTUParam().PhoneID = strTmp.substring(0, strTmp.indexOf("\r\n"));
/*
        //移动服务
        strTmp = value.substring(value.indexOf("MPAPN=") + 6);//访问接入点
        param.getHDDTUParam().AccessPoint = strTmp.substring(0, strTmp.indexOf("\r\n"));
        strTmp = value.substring(value.indexOf("MPUSER=") + 7);//访问用户名
        param.getHDDTUParam().AccessUser = strTmp.substring(0, strTmp.indexOf("\r\n"));
        strTmp = value.substring(value.indexOf("MPPWD=") + 6);//访问密码
        param.getHDDTUParam().AccessPsw = strTmp.substring(0, strTmp.indexOf("\r\n"));
        strTmp = value.substring(value.indexOf("MPCN=") + 5);//服务代码
        param.getHDDTUParam().AccessServerCode = strTmp.substring(0, strTmp.indexOf("\r\n"));

        //串口配置
        strTmp = value.substring(value.indexOf("RTUPBR=") + 7);//串口波特率
        param.getHDDTUParam().ComBaudRate = strTmp.substring(0, strTmp.indexOf("\r\n"));
        strTmp = value.substring(value.indexOf("OPPDIO=") + 7);//调试信息
        param.getHDDTUParam().DebugInfo = strTmp.substring(0, strTmp.indexOf("\r\n"));
        strTmp = value.substring(value.indexOf("RTUDTUP=") + 8);//启用RDP协议
        param.getHDDTUParam().RDPSwitch = strTmp.substring(0, strTmp.indexOf("\r\n"));
        strTmp = value.substring(value.indexOf("RTUDTUDOWNP=") + 12);//启用RDP下行协议
        param.getHDDTUParam().GPRSProtocal = strTmp.substring(0, strTmp.indexOf("\r\n"));

        //通道信息
        //通道1
        String strChannel = value.substring(value.indexOf("[CHANNEL1  PARAMETER(S)]"));
        strTmp = strChannel.substring(strChannel.indexOf("CHPCT=") + 6);//通讯方式1
        param.getHDDTUParam().DSC1CSMS = strTmp.substring(0, strTmp.indexOf("\r\n"));
        strTmp = strChannel.substring(strChannel.indexOf("CHPDSCIP=") + 9);//目标IP1
        param.getHDDTUParam().Dsc1IP = strTmp.substring(0, strTmp.indexOf("\r\n"));
        strTmp = strChannel.substring(strChannel.indexOf("CHPDSCP=") + 8);//目标端口1
        param.getHDDTUParam().Dsc1Port = strTmp.substring(0, strTmp.indexOf("\r\n"));
        strTmp = strChannel.substring(strChannel.indexOf("CHPDSCD=") + 8);//目标域名1
        param.getHDDTUParam().Dsc1YM = strTmp.substring(0, strTmp.indexOf("\r\n"));
        strTmp = strChannel.substring(strChannel.indexOf("CHPCRP=") + 7);//注册包1
        param.getHDDTUParam().Dsc1RegPack = strTmp.substring(0, strTmp.indexOf("\r\n"));
        strTmp = strChannel.substring(strChannel.indexOf("CHPCHP=") + 7);//心跳包1
        param.getHDDTUParam().DSC1Heart = strTmp.substring(0, strTmp.indexOf("\r\n"));
        //通道2
        strChannel = value.substring(value.indexOf("[CHANNEL2  PARAMETER(S)]"));
        strTmp = strChannel.substring(strChannel.indexOf("CHPCT=") + 6);//通讯方式2
        param.getHDDTUParam().DSC2CSMS = strTmp.substring(0, strTmp.indexOf("\r\n"));
        strTmp = strChannel.substring(strChannel.indexOf("CHPDSCIP=") + 9);//目标IP2
        param.getHDDTUParam().Dsc2IP = strTmp.substring(0, strTmp.indexOf("\r\n"));
        strTmp = strChannel.substring(strChannel.indexOf("CHPDSCP=") + 8);//目标端口2
        param.getHDDTUParam().Dsc2Port = strTmp.substring(0, strTmp.indexOf("\r\n"));
        strTmp = strChannel.substring(strChannel.indexOf("CHPDSCD=") + 8);//目标域名2
        param.getHDDTUParam().Dsc2YM = strTmp.substring(0, strTmp.indexOf("\r\n"));
        strTmp = strChannel.substring(strChannel.indexOf("CHPCRP=") + 7);//注册包2
        param.getHDDTUParam().Dsc2RegPack = strTmp.substring(0, strTmp.indexOf("\r\n"));
        strTmp = strChannel.substring(strChannel.indexOf("CHPCHP=") + 7);//心跳包2
        param.getHDDTUParam().DSC2Heart = strTmp.substring(0, strTmp.indexOf("\r\n"));
        //通道3
        strChannel = value.substring(value.indexOf("[CHANNEL3  PARAMETER(S)]"));
        strTmp = strChannel.substring(strChannel.indexOf("CHPCT=") + 6);//通讯方式3
        param.getHDDTUParam().DSC3CSMS = strTmp.substring(0, strTmp.indexOf("\r\n"));
        strTmp = strChannel.substring(strChannel.indexOf("CHPDSCIP=") + 9);//目标IP3
        param.getHDDTUParam().Dsc3IP = strTmp.substring(0, strTmp.indexOf("\r\n"));
        strTmp = strChannel.substring(strChannel.indexOf("CHPDSCP=") + 8);//目标端口3
        param.getHDDTUParam().Dsc3Port = strTmp.substring(0, strTmp.indexOf("\r\n"));
        strTmp = strChannel.substring(strChannel.indexOf("CHPDSCD=") + 8);//目标域名3
        param.getHDDTUParam().Dsc3YM = strTmp.substring(0, strTmp.indexOf("\r\n"));
        strTmp = strChannel.substring(strChannel.indexOf("CHPCRP=") + 7);//注册包3
        param.getHDDTUParam().Dsc3RegPack = strTmp.substring(0, strTmp.indexOf("\r\n"));
        strTmp = strChannel.substring(strChannel.indexOf("CHPCHP=") + 7);//心跳包3
        param.getHDDTUParam().DSC3Heart = strTmp.substring(0, strTmp.indexOf("\r\n"));
        //通道4
        strChannel = value.substring(value.indexOf("[CHANNEL4  PARAMETER(S)]"));
        strTmp = strChannel.substring(strChannel.indexOf("CHPCT=") + 6);//通讯方式4
        param.getHDDTUParam().DSC4CSMS = strTmp.substring(0, strTmp.indexOf("\r\n"));
        strTmp = strChannel.substring(strChannel.indexOf("CHPDSCIP=") + 9);//目标IP4
        param.getHDDTUParam().Dsc4IP = strTmp.substring(0, strTmp.indexOf("\r\n"));
        strTmp = strChannel.substring(strChannel.indexOf("CHPDSCP=") + 8);//目标端口4
        param.getHDDTUParam().Dsc4Port = strTmp.substring(0, strTmp.indexOf("\r\n"));
        strTmp = strChannel.substring(strChannel.indexOf("CHPDSCD=") + 8);//目标域名4
        param.getHDDTUParam().Dsc4YM = strTmp.substring(0, strTmp.indexOf("\r\n"));
        strTmp = strChannel.substring(strChannel.indexOf("CHPCRP=") + 7);//注册包4
        param.getHDDTUParam().Dsc4RegPack = strTmp.substring(0, strTmp.indexOf("\r\n"));
        strTmp = strChannel.substring(strChannel.indexOf("CHPCHP=") + 7);//心跳包4
        param.getHDDTUParam().DSC4Heart = strTmp.substring(0, strTmp.indexOf("\r\n"));

        //宏电管理平台
        strTmp = value.substring(value.indexOf("WPDOMAIN=") + 9);//平台域名
        param.getHDDTUParam().ManageDscYM = strTmp.substring(0, strTmp.indexOf("\r\n"));
        strTmp = value.substring(value.indexOf("WPIPADDR=") + 9);//平台IP
        param.getHDDTUParam().ManageDscIP = strTmp.substring(0, strTmp.indexOf("\r\n"));
        strTmp = value.substring(value.indexOf("WPDESTPORT=") + 11);//平台端口
        param.getHDDTUParam().ManageDscPort = strTmp.substring(0, strTmp.indexOf("\r\n"));
*/
        return param;
    }

    private void BasicInfoShow(HDDTU hddtu) {
        mTextHardwarever.setText(hddtu.getHDDTUParam().HardwareVer);
        mTextSerialno.setText(hddtu.getHDDTUParam().SerialNo);
    }
}
