package com.wanji.jdbluetooth.Fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.SimpleAdapter;

import com.wanji.jdbluetooth.R;
import com.wanji.jdbluetooth.activity.SearchActivity;
import com.wanji.jdbluetooth.beans.ImgTextData;
import com.wanji.jdbluetooth.global.GlobalVariables;
import com.wanji.jdbluetooth.tools.CommonFunctions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 选择配置设备：控制器、DTU
 * Created by Administrator on 2018/5/17.
 */

public class DeviceFragment extends Fragment {
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION = 100;

    @Bind(R.id.device_tablayout)
    TabLayout mDeviceTablayout;
    @Bind(R.id.device_viewpager)
    ViewPager mDeviceViewpager;

    private ArrayList<String> mTabTitleList = new ArrayList<>();//存放标签页标题
    private ArrayList<Fragment> mFragmentList = new ArrayList<>();//存放ViewPager下的fragment
    private Fragment mFramentController, mFragmentDtuHd;
    private DeviceFragmentAdapter mAdapter;//适配器

    List<HashMap<String, Object>> listBluetoothListData = new ArrayList<HashMap<String, Object>>();
   // SimpleAdapter listItemAdapter;

    AlertDialog dia_bluetooth;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_device, container, false);

        ButterKnife.bind(this, view);

        initView();

        return view;
    }

    private void initView() {
        mTabTitleList.add("交调控制器");
        mTabTitleList.add("宏电DTU");

        mDeviceTablayout.addTab(mDeviceTablayout.newTab().setText(mTabTitleList.get(0)));
        mDeviceTablayout.addTab(mDeviceTablayout.newTab().setText(mTabTitleList.get(1)));

        mFramentController = new MenuControllerFragment();
        mFragmentDtuHd = new MenuHdDtuFragment();

        mFragmentList.add(mFramentController);
        mFragmentList.add(mFragmentDtuHd);

        mAdapter = new DeviceFragmentAdapter(getFragmentManager(), mTabTitleList, mFragmentList);
        mDeviceViewpager.setAdapter(mAdapter);//给ViewPager设置适配器
        mDeviceTablayout.setupWithViewPager(mDeviceViewpager);//将ViewPager与Tablayout联动起来
    }

    private void registerBluetoothReciever(){
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
        getActivity().registerReceiver(mBluetoothReceiver, filter);
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
                dia_bluetooth = new AlertDialog.Builder(getContext())
                                        .setTitle("提示")
                                        .setMessage("搜索中...")
                                        .create();
                dia_bluetooth.show();
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                dia_bluetooth.cancel();
                CommonFunctions.Msg(getContext(), "搜索完毕");

                //存储数据的数组列表
                for (int i = 0; i < GlobalVariables.gListBluetoothDevice.size(); i++) {
                    HashMap<String, Object> map = new HashMap<String, Object>();
                    map.put("bluetooth_image", R.drawable.png_bluetooth);
                    map.put("bluetooth_name", "设备名称：" + GlobalVariables.gListBluetoothDevice.get(i).getName());
                    map.put("bluetooth_mac", "MAC地址：" + GlobalVariables.gListBluetoothDevice.get(i).getAddress());
                    listBluetoothListData.add(map);
                }
                /*
                //适配器
                listItemAdapter = new SimpleAdapter(getContext(),
                        listBluetoothListData,
                        R.layout.item_bluetooth,
                        new String[]{"bluetooth_image", "bluetooth_name", "bluetooth_mac"},
                        new int[]{R.id.bluetooth_image, R.id.bluetooth_name, R.id.bluetooth_mac});
                bluetoothList.setAdapter(listItemAdapter);
                */
            }
        }
    };

    /*
      校验蓝牙权限
     */
    private void checkBluetoothPermission() {
        if (Build.VERSION.SDK_INT >= 23) {//android版本6.0以上需要动态申请危险权限
            //校验是否已具有模糊定位权限
            if (ContextCompat.checkSelfPermission(getContext(),
                    Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                //申请权限
                requestPermissions(
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},//模糊定位权限
                        MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION);
            } else {
                //具有权限
                bluetoothManager.bluetoothChatDeviceStart(this, handler);
            }
        } else {
            //系统不高于6.0直接执行
            BluetoothManager. .bluetoothChatDeviceStart(this, handler);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        checkBluetoothPermission();//权限检测
    }

    /**
     * 申请权限的返回值的处理，相当于StartActivityForResult
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                //权限准许
                ;
            }else{
                //权限拒绝
                ;
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
