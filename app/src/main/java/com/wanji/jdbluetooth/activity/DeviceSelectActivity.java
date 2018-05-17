package com.wanji.jdbluetooth.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.wanji.jdbluetooth.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 选择配置设备：控制器、DTU
 * Created by Administrator on 2018/5/9.
 */

public class DeviceSelectActivity extends AppCompatActivity {

    @Bind(R.id.dev_select_btn_config_controller)
    Button mBtnControllerCfg;
    @Bind(R.id.dev_select_btn_config_dtu_hd)
    Button mBtnDtuHDCfg;
    @Bind(R.id.dev_select_btn_config_dtu_cm)
    Button mBtnDtuCMCfg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dev_select);
        ButterKnife.bind(this);

    }

    @OnClick({R.id.dev_select_btn_config_controller, R.id.dev_select_btn_config_dtu_hd, R.id.dev_select_btn_config_dtu_cm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.dev_select_btn_config_controller:
                Intent intent = new Intent(DeviceSelectActivity.this, MenuControllerActivity.class);
                startActivity(intent);
                break;
            case R.id.dev_select_btn_config_dtu_hd:
                Intent intentHD = new Intent(DeviceSelectActivity.this, MenuHDDtuActivity.class);
                startActivity(intentHD);
                break;
            case R.id.dev_select_btn_config_dtu_cm:
                break;
        }
    }
}
