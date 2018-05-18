package com.wanji.jdbluetooth.beans;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.wanji.jdbluetooth.R;
import com.wanji.jdbluetooth.tools.ConvertUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2018/5/18.
 */

public class SelectDialog extends Dialog {

    private List<HashMap<String, Object>> mDataList = new ArrayList<>();

    private Context mContext;
    private String mTitleText = null;
    private int mTitleBackgroundColor = -1;
    private int mTitleTextColor = Color.WHITE;
    private int mButtonColor = -1;
    private int mTitleTextSize;
    private int mButtonSize;
    private int mLastButtonSize;

    private ArrayList<Button> buttonList = new ArrayList<>();

    private View.OnClickListener mBtnSelectClickListener;

    public SelectDialog(Context context, Builder builder){
        super(context);
        mContext = builder.mContext;
        mTitleText = builder.mTitleText;
        mTitleBackgroundColor = builder.mTitleBackgroundColor;
        mTitleTextColor = builder.mTitleTextColor;
        mButtonColor = builder.mButtonColor;
        mDataList = builder.mDataList;
        mTitleTextSize = builder.mTitleTextSize;
        mButtonSize = builder.mButtonSize;
        mLastButtonSize = builder.mLastButtonSize;
        init();
    }

    public static class Builder {
        Context mContext;
        String mTitleText = null;
        int mTitleBackgroundColor = -1;
        int mTitleTextColor = Color.WHITE;
        int mButtonColor = -1;
        int mTitleTextSize = 18;
        int mButtonSize = 14;
        int mLastButtonSize = 17;
        List<HashMap<String, Object>> mDataList;


        public Builder(Context context) {

            mContext = context;
        }

        public SelectDialog build() {
            return new SelectDialog(mContext, this);
        }

        public Builder setTitleText(String title) {

            if (TextUtils.isEmpty(title)) {

                return null;
            }

            mTitleText = title;
            return this;
        }

        public Builder setTitleBackground(int colorResId){
            mTitleBackgroundColor = colorResId;
            return this;
        }

        public Builder setTitleTextColor(int colorResId){
            mTitleTextColor = colorResId;
            return this;
        }

        public Builder setTitleTextSize(int textSize){
            mTitleTextSize = textSize;
            return this;
        }

        public Builder setButtonColor(int buttonColor) {

            mButtonColor = buttonColor;
            return this;
        }

        public Builder setButtonSize(int buttonSize) {

            mButtonSize = buttonSize;
            return this;
        }

        public Builder setLastButtonSize(int buttonSize) {

            mLastButtonSize = buttonSize;
            return this;
        }

        public Builder setDataList(List<HashMap<String, Object>> list) {
            mDataList = list;
            return this;
        }
    }

    private void init(){

        if (null == mDataList) {
            return;
        }
        //头部标题TextView
        LayoutParams titleParams = new LayoutParams(
                LayoutParams.MATCH_PARENT, ConvertUtils.dip2px(mContext, 44));
        TextView titleTextView = new TextView(mContext);
        titleTextView.setLayoutParams(titleParams);
        titleTextView.setBackgroundColor(mContext.getResources().getColor(R.color.colorBlue));
        titleTextView.setTextColor(mTitleTextColor);
        titleTextView.setText(mTitleText);
        titleTextView.setTextSize(mTitleTextSize);
        titleTextView.setGravity(Gravity.CENTER);

        //用于添加button和textview的layout
        LinearLayout layout = new LinearLayout(mContext);
        LayoutParams layoutParams = new LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        layoutParams.leftMargin = ConvertUtils.dip2px(mContext, 0);
        layoutParams.rightMargin = ConvertUtils.dip2px(mContext, 0);
        layout.setLayoutParams(layoutParams);
        layout.setGravity(Gravity.CENTER);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setBackgroundColor(mButtonColor);
        layout.addView(titleTextView);


        //button的属性
        LayoutParams btnParams = new LayoutParams(
                LayoutParams.MATCH_PARENT, ConvertUtils.dip2px(mContext,44));
        btnParams.leftMargin = ConvertUtils.dip2px(mContext, 0);
        btnParams.rightMargin = ConvertUtils.dip2px(mContext, 0);

        //主Layout
        LinearLayout mainLayout = new LinearLayout(mContext);
        LayoutParams mainLayoutParams =new LayoutParams(
                layoutParams.MATCH_PARENT,  mContext.getResources().getDisplayMetrics().heightPixels);
        mainLayout.setLayoutParams(mainLayoutParams);
        mainLayout.setGravity(Gravity.CENTER);
        mainLayout.setOrientation(LinearLayout.VERTICAL);
        mainLayout.setBackgroundColor(mContext.getResources().getColor(R.color.colorWhite));

        //textView的属性
        LayoutParams textParams = new LayoutParams(
                LayoutParams.MATCH_PARENT, ConvertUtils.dip2px(mContext,1));
        textParams.leftMargin = ConvertUtils.dip2px(mContext, 5);
        textParams.rightMargin = ConvertUtils.dip2px(mContext, 5);

        int size = mDataList.size();
        for (int i = 0; i < size + 1; i++) {
            Button tDiscardBtn = new Button(mContext);
            buttonList.add(tDiscardBtn);
            tDiscardBtn.setLayoutParams(btnParams);
            tDiscardBtn.setGravity(Gravity.CENTER);
            if (i == size) {
                tDiscardBtn.setText("取消");
                tDiscardBtn.setTextSize(mLastButtonSize);
                tDiscardBtn.setBackgroundColor(mContext.getResources().getColor(R.color.colorWhite));
                tDiscardBtn.setTextColor(mContext.getResources().getColor(R.color.colorBlue));

                tDiscardBtn.setTag(-1);
            } else {
                //设置按钮图片
                tDiscardBtn.setText(mDataList.get(i).get("bluetooth_name").toString());
                tDiscardBtn.setTextSize(mButtonSize);
                tDiscardBtn.setBackgroundColor(mButtonColor);
                tDiscardBtn.setTextColor(mContext.getResources().getColor(R.color.colorBlack));

                tDiscardBtn.setTag(i);
            }
            tDiscardBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mBtnSelectClickListener != null){
                        mBtnSelectClickListener.onClick(v);
                    }
                    dismissDialog();
                }
            });
            tDiscardBtn.setGravity(Gravity.CENTER);
            TextView textView = new TextView(mContext);
            textView.setBackgroundColor(mContext.getResources().getColor(R.color.colorFrenchGrey));
            textView.setLayoutParams(textParams);

            layout.addView(tDiscardBtn);
            //去掉最后一行的线
            if (i != size) {
                layout.addView(textView);
            }
        }
        mainLayout.addView(layout);
        //设置按钮的属性
        //setAllButtonStyle();
        this.setContentView(mainLayout);
        Window window = getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.gravity = Gravity.CENTER;
        lp.width = ConvertUtils.dip2px(mContext, 280);//宽高可设置具体大小
        lp.height = LayoutParams.WRAP_CONTENT;
        lp.dimAmount =0.2f;//背景变暗程度
        getWindow().setAttributes(lp);

       /* mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dismissDialog();
            }
        });*/
    }

    @Override
    public void show() {
        super.show();
    }

    /*
        public void show(View parent) {
            if (!((Activity) mContext).isFinishing()) {
                show(parent, Gravity.CENTER, 0, 0);
            }
        }
    */
    public void setAllButtonTextColor(int colorId) {
        for (Button button : buttonList) {
            button.setTextColor(colorId);
        }
    }

    /**
     * 设置button的样式
     */
    public void setAllButtonStyle() {
        for (Button button : buttonList) {
            button.setTextSize(14);
            button.setBackgroundColor(mContext.getResources().getColor(R.color.colorWhite));
            button.setTextColor(mContext.getResources().getColor(R.color.colorBlack));
        }
    }

    private void dismissDialog() {
        if(Looper.myLooper() != Looper.getMainLooper()){
            return;
        }

        if(null == this || !isShowing() || null == getContext() || ((Activity)mContext).isFinishing()){
            return;
        }

        dismiss();
    }

    public void setButtonListener(View.OnClickListener onClickListener){
        this.mBtnSelectClickListener = onClickListener;
    }

    @Override
    public boolean onKeyDown(int keyCode, @NonNull KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            dismissDialog();
        }
        return super.onKeyDown(keyCode, event);
    }
}
