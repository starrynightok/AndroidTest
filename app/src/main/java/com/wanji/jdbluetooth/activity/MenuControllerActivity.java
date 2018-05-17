package com.wanji.jdbluetooth.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.wanji.jdbluetooth.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by admin on 2018/2/9.
 */

public class MenuControllerActivity extends AppCompatActivity {

    ArrayList<HashMap<String, Object>> listMenuListData = new ArrayList<HashMap<String, Object>>();
    ListView menuList;
    SimpleAdapter listItemAdapter;
    List<String> listMenu = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_controller_menu);

        listMenu.add("现场出车");
        listMenu.add("设备参数");
        listMenu.add("网络参数");
        listMenu.add("01包查询");
        listMenu.add("异常参数");
        listMenu.add("重启信息");
        listMenu.add("阈值参数");
        listMenu.add("功能参数");
        listMenu.add("激光波形");
        listMenu.add("激光状态");
        listMenu.add("激光系统");
        listMenu.add("激光心跳");


        menuList = (ListView) findViewById(R.id.menus);
        menuList.setOnItemClickListener(new MenuControllerActivity.ItemClick());
        //存储数据的数组列表
        for (int i = 0; i < listMenu.size(); i++) {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("menu_image", R.drawable.png_right);
            map.put("menu_name", listMenu.get(i));
            listMenuListData.add(map);
        }
        //适配器
        listItemAdapter = new SimpleAdapter(MenuControllerActivity.this,
                listMenuListData,
                R.layout.item_menu,
                new String[]{"menu_image", "menu_name"},
                new int[]{R.id.menu_image, R.id.menu_name});
        menuList.setAdapter(listItemAdapter);
    }

    /*
     * item上的OnClick事件
     */
    public final class ItemClick implements AdapterView.OnItemClickListener {

        public void onItemClick(AdapterView<?> parent, View arg1, int position, long id) {

            if (id == 1) {
                Intent i = new Intent(MenuControllerActivity.this, ControlParamsActivity.class);
                startActivity(i);
            } else if (id == 2) {
                Intent i = new Intent(MenuControllerActivity.this, NetParamsActivity.class);
                startActivity(i);
            } else if (id == 3) {
                Intent i = new Intent(MenuControllerActivity.this, Package01Activity.class);
                startActivity(i);
            } else if (id == 4) {
                Intent i = new Intent(MenuControllerActivity.this, ExParamActivity.class);
                startActivity(i);
            } else if (id == 5) {
                Intent i = new Intent(MenuControllerActivity.this, RestartInfoActivity.class);
                startActivity(i);
            } else if (id == 6) {
                Intent i = new Intent(MenuControllerActivity.this, ThresholdParamActivity.class);
                startActivity(i);
            } else if (id == 7) {
                Intent i = new Intent(MenuControllerActivity.this, FunctionParamActivity.class);
                startActivity(i);
            } else if (id == 8) {
                Intent i = new Intent(MenuControllerActivity.this, LaserWaveActivity.class);
                startActivity(i);
            } else if (id == 9) {
                Intent i = new Intent(MenuControllerActivity.this, LaserStateActivity.class);
                startActivity(i);
            } else if (id == 10) {
                Intent i = new Intent(MenuControllerActivity.this, LaserSysActivity.class);
                startActivity(i);
            } else if (id == 11) {
                Intent i = new Intent(MenuControllerActivity.this, LaserHeartActivity.class);
                startActivity(i);
            } else if (id == 0) {
                Intent i = new Intent(MenuControllerActivity.this, NowVehActivity.class);
                startActivity(i);
            }
        }
    }
}
