package com.wanji.jdbluetooth.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.wanji.jdbluetooth.Fragment.DeviceFragment;
import com.wanji.jdbluetooth.Fragment.ManageFragment;
import com.wanji.jdbluetooth.Fragment.MeFragment;
import com.wanji.jdbluetooth.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.studyou.navigationviewlibrary.BottomNavigationItem;
import cn.studyou.navigationviewlibrary.BottomNavigationView;

/**
 * 主界面
 * Created by Administrator on 2018/5/9.
 */

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.bottomNavigation)
    BottomNavigationView mBottomNavigation;

    private Fragment deviceFragment;//设备页
    private Fragment manageFragment;//管理页
    private Fragment meFragment;//我页

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(getWindow().FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initView();
    }

    private void initView() {
        if(mBottomNavigation != null){
            mBottomNavigation.isWithText(true);
            mBottomNavigation.isColoredBackground(false);
            //bottomNavigationView.disableShadow();
            //bottomNavigationView.setItemActiveColorWithoutColoredBackground(getResources().getColor(R.color.colorGreen));
        }

        String[] titles = getResources().getStringArray(R.array.tab_main);

        BottomNavigationItem item1 = new BottomNavigationItem(titles[0], getResources().getColor(R.color.colorGreen), R.drawable.ic_memory_black_24dp);
        BottomNavigationItem item2 = new BottomNavigationItem(titles[1], getResources().getColor(R.color.colorOrange), R.drawable.ic_event_note_black_24dp);
        BottomNavigationItem item3 = new BottomNavigationItem(titles[2], getResources().getColor(R.color.colorPrimary), R.drawable.ic_record_voice_over_black_24dp);

        selectImages(0);//默认选中第一页

        mBottomNavigation.addTab(item1);//将item加入菜单栏
        mBottomNavigation.addTab(item2);
        mBottomNavigation.addTab(item3);

        //设置点击菜单项的事件
        mBottomNavigation.setOnBottomNavigationItemClickListener(new BottomNavigationView.OnBottomNavigationItemClickListener() {
            @Override
            public void onNavigationItemClick(int index) {
                switch (index){
                    case 0:
                        selectImages(0);
                        break;
                    case 1:
                        selectImages(1);
                        break;
                    case 2:
                        selectImages(2);
                        break;
                    default:break;
                }
            }
        });
    }

    /**
     * 设置选中
     * @param i
     */
    private void selectImages(int i){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        hideAllFragment(fragmentTransaction);

        switch (i){
            case 0:
                if(deviceFragment == null){
                    deviceFragment = new DeviceFragment();
                    fragmentTransaction.add(R.id.fragment_main, deviceFragment);
                }
                fragmentTransaction.show(deviceFragment);
                break;
            case 1:
                if(manageFragment == null){
                    manageFragment = new ManageFragment();
                    fragmentTransaction.add(R.id.fragment_main, manageFragment);
                }
                fragmentTransaction.show(manageFragment);
                break;
            case 2:
                if(meFragment == null){
                    meFragment = new MeFragment();
                    fragmentTransaction.add(R.id.fragment_main, meFragment);
                }
                fragmentTransaction.show(meFragment);
                break;
            default:break;
        }
        fragmentTransaction.commit();//提交事务
    }

    /**
     * 隐藏所有Fragment
     * @param fragmentTransaction
     */
    private void hideAllFragment(FragmentTransaction fragmentTransaction){
        if(deviceFragment != null){
            fragmentTransaction.hide(deviceFragment);
        }

        if(manageFragment != null){
            fragmentTransaction.hide(manageFragment);
        }

        if(meFragment != null){
            fragmentTransaction.hide(meFragment);
        }
    }

}
