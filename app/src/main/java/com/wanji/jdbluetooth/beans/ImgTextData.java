package com.wanji.jdbluetooth.beans;

import android.widget.TextView;

/**
 * 图片与文字组成的数据类
 * Created by Administrator on 2018/5/18.
 */

public class ImgTextData {
    int imgId;
    String text;

    public ImgTextData(int id, String s){
        imgId = id;
        text = s;
    }

    public int getImgId(){
        return imgId;
    }

    public void setImgId(int id){
        imgId = id;
    }

    public String getText(){
        return text;
    }

    public void setText(String s){
        text = s;
    }
}
