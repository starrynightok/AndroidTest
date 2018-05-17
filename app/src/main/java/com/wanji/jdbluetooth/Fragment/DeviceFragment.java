package com.wanji.jdbluetooth.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.wanji.jdbluetooth.R;
import com.wanji.jdbluetooth.activity.MainActivity;
import com.wanji.jdbluetooth.activity.MenuControllerActivity;
import com.wanji.jdbluetooth.activity.MenuHDDtuActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 选择配置设备：控制器、DTU
 * Created by Administrator on 2018/5/17.
 */

public class DeviceFragment extends Fragment {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.fragment_main_device, container, false);

       return view;
    }
}
