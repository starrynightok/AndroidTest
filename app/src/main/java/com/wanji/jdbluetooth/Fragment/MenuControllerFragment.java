package com.wanji.jdbluetooth.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.wanji.jdbluetooth.R;
import com.wanji.jdbluetooth.activity.ControlParamsActivity;
import com.wanji.jdbluetooth.activity.ExParamActivity;
import com.wanji.jdbluetooth.activity.FunctionParamActivity;
import com.wanji.jdbluetooth.activity.LaserHeartActivity;
import com.wanji.jdbluetooth.activity.LaserStateActivity;
import com.wanji.jdbluetooth.activity.LaserSysActivity;
import com.wanji.jdbluetooth.activity.LaserWaveActivity;
import com.wanji.jdbluetooth.activity.NetParamsActivity;
import com.wanji.jdbluetooth.activity.NowVehActivity;
import com.wanji.jdbluetooth.activity.Package01Activity;
import com.wanji.jdbluetooth.activity.RestartInfoActivity;
import com.wanji.jdbluetooth.activity.ThresholdParamActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/5/17.
 */

public class MenuControllerFragment extends Fragment {
    @Bind(R.id.menus)
    ListView menuList;

    ArrayList<HashMap<String, Object>> listMenuListData = new ArrayList<HashMap<String, Object>>();
    SimpleAdapter listItemAdapter;
    List<String> listMenu = new ArrayList<String>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_controller_menu, container, false);
        ButterKnife.bind(this, view);

        initView();

        return view;
    }

    private void initView() {
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

        menuList.setOnItemClickListener(new MenuControllerFragment.ItemClick());
        //存储数据的数组列表
        for (int i = 0; i < listMenu.size(); i++) {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("menu_image", R.drawable.png_right);
            map.put("menu_name", listMenu.get(i));
            listMenuListData.add(map);
        }
        //适配器
        listItemAdapter = new SimpleAdapter(getContext(),
                listMenuListData,
                R.layout.item_menu,
                new String[]{"menu_image", "menu_name"},
                new int[]{R.id.menu_image, R.id.menu_name});
        menuList.setAdapter(listItemAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    /*
     * item上的OnClick事件
     */
    public final class ItemClick implements AdapterView.OnItemClickListener {

        public void onItemClick(AdapterView<?> parent, View arg1, int position, long id) {

            if (id == 1) {
                Intent i = new Intent(getContext(), ControlParamsActivity.class);
                startActivity(i);
            } else if (id == 2) {
                Intent i = new Intent(getContext(), NetParamsActivity.class);
                startActivity(i);
            } else if (id == 3) {
                Intent i = new Intent(getContext(), Package01Activity.class);
                startActivity(i);
            } else if (id == 4) {
                Intent i = new Intent(getContext(), ExParamActivity.class);
                startActivity(i);
            } else if (id == 5) {
                Intent i = new Intent(getContext(), RestartInfoActivity.class);
                startActivity(i);
            } else if (id == 6) {
                Intent i = new Intent(getContext(), ThresholdParamActivity.class);
                startActivity(i);
            } else if (id == 7) {
                Intent i = new Intent(getContext(), FunctionParamActivity.class);
                startActivity(i);
            } else if (id == 8) {
                Intent i = new Intent(getContext(), LaserWaveActivity.class);
                startActivity(i);
            } else if (id == 9) {
                Intent i = new Intent(getContext(), LaserStateActivity.class);
                startActivity(i);
            } else if (id == 10) {
                Intent i = new Intent(getContext(), LaserSysActivity.class);
                startActivity(i);
            } else if (id == 11) {
                Intent i = new Intent(getContext(), LaserHeartActivity.class);
                startActivity(i);
            } else if (id == 0) {
                Intent i = new Intent(getContext(), NowVehActivity.class);
                startActivity(i);
            }
        }
    }
}
