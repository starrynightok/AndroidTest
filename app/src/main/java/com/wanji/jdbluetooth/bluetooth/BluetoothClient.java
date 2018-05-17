package com.wanji.jdbluetooth.bluetooth;

import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.icu.lang.UCharacter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.wanji.jdbluetooth.global.GlobalVariables;
import com.wanji.jdbluetooth.tools.CommonFunctions;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by admin on 2018/2/8.
 */

public class BluetoothClient {

    private static BluetoothClient bc = null;
    private Handler _handler;
    private BluetoothSocket _socket;
    private Context _context;
    private boolean _curState = false;
    private List<Byte> bufferCache = new ArrayList<Byte>();
    private Date bufRecvTime = new Date();
    private int parseType = GlobalVariables.PACKAGE_PARSE_TYPE_CONTROL;//0-控制器 1-激光 2-宏电DTU

    public static BluetoothClient getInstance(Handler handle, BluetoothSocket socket, Context context) {

        if (bc == null) {
            bc = new BluetoothClient(handle, socket, context);
        }
        return bc;
    }

    public static BluetoothClient getCurInstance() {
        return bc;
    }

    BluetoothClient(Handler handle, BluetoothSocket socket, Context context) {
        _handler = handle;
        _socket = socket;
        _context = context;
    }

    /**
     * 连接蓝牙
     */
    public void connSocket() {
        Thread t = new Thread() {
            @Override
            public void run() {
                if (_connSocket()) {
                    _sendMessage("0", "连接成功");
                } else {
                    _sendMessage("1", "连接失败");
                }
            }
        };
        t.start();
    }

    private boolean _connSocket() {
        try {
            _socket.connect();
            _curState = true;
            Thread _socketListener = new Thread() {

                @Override
                public void run() {
                    InputStream is = null;
                    byte[] buffer = new byte[10240];
                    int len = 0;
                    try {
                        is = _socket.getInputStream();
                        while (_curState) {
                            len = is.read(buffer);
                            byte[] res = new byte[len];
                            System.arraycopy(buffer, 0, res, 0, len);
                            dataProcess(res);
                        }
                    } catch (Exception ex) {
                        System.out.println(ex);
                    } finally {
                        try {
                            if (is != null) {
                                is.close();
                            }
                        } catch (Exception ex1) {
                            ;
                        }
                        if (_curState) {
                            _sendMessage("2", "蓝牙断开，正在重连...");
                            try {
                                _socket.close();
                                Thread.sleep(1000);
                                if (GlobalVariables.gCurSdk >= 10) {
                                    _socket = GlobalVariables.gCurBluetoothDevice.createInsecureRfcommSocketToServiceRecord(GlobalVariables.MY_UUID);
                                } else {
                                    _socket = GlobalVariables.gCurBluetoothDevice.createRfcommSocketToServiceRecord(GlobalVariables.MY_UUID);
                                }
                            } catch (Exception ee) {

                            }
                            while (!_connSocket() && _curState) {
                                try {
                                    _sendMessage("3", "重连失败，正在重连...");
                                    _socket.close();
                                    Thread.sleep(2000);
                                    if (GlobalVariables.gCurSdk >= 10) {
                                        _socket = GlobalVariables.gCurBluetoothDevice.createInsecureRfcommSocketToServiceRecord(GlobalVariables.MY_UUID);
                                    } else {
                                        _socket = GlobalVariables.gCurBluetoothDevice.createRfcommSocketToServiceRecord(GlobalVariables.MY_UUID);
                                    }
                                } catch (Exception eT) {

                                }
                            }
                            if (_curState) {
                                _sendMessage("3", "重连成功");
                            }
                        }
                    }
                }
            };
            _socketListener.start();
        } catch (Exception ex) {
            return false;
        }
        return true;
    }

    /**
     * 断开蓝牙连接
     */
    public void disconnSocket() {
        try {
            _curState = false;
            if (_socket != null) {
                _socket.close();
            }
        } catch (Exception ex) {

        } finally {
            bc = null;
        }
    }

    /**
     * 蓝牙发送数据
     *
     * @param data
     * @return
     */
    public boolean sendData(byte[] data) {
        OutputStream os = null;
        try {
            os = _socket.getOutputStream();
            int index = 0;
            while (index < data.length) {
                int len = index + GlobalVariables.SOCKET_SEND_DATA_LENGTH <= data.length ? GlobalVariables.SOCKET_SEND_DATA_LENGTH : (data.length % GlobalVariables.SOCKET_SEND_DATA_LENGTH);
                byte[] buf = new byte[len];
                for (int i = 0; i < len; i++) {
                    buf[i] = data[index + i];
                }
                index += len;
                os.write(buf);
            }
        } catch (Exception ex) {
            return false;
        } finally {
            ;
        }
        return true;
    }

    //发送广播
    private void _sendBroadcast(String action, byte[] data) {
        Intent intent = new Intent();
        intent.setAction(action);
        intent.putExtra(GlobalVariables.SOCKET_BUFFER_DATA, data);
        GlobalVariables.localBroadcastManager.sendBroadcast(intent);
    }

    /**
     * 发送广播
     * @param action
     * @param data
     */
    private void _sendBroadcast(String action, String data){
        Intent intent = new Intent();
        intent.setAction(action);
        intent.putExtra(GlobalVariables.SOCKET_BUFFER_DATA, data);
        GlobalVariables.localBroadcastManager.sendBroadcast(intent);
    }

    //发送消息
    private void _sendMessage(String msgId, String obj) {
        Message message = new Message();
        Bundle bundle = new Bundle();
        bundle.putString("msgId", msgId);
        bundle.putString("msgContent", obj);
        message.setData(bundle);
        _handler.sendMessage(message);
    }

    //设置解析方式
    public void setParseType(int type) {
        this.parseType = type;
    }

    /**
     * 数据处理
     *
     * @param buf
     */
    public void dataProcess(byte[] buf) {
        synchronized (this) {
            Date now = new Date();
            if ((now.getTime() - bufRecvTime.getTime()) / 1000 > 5)//上一次收数超过30秒则清空缓存
            {
                bufferCache.clear();
            }
            for (int i = 0; i < buf.length; i++) {//将数据放入缓存中
                bufferCache.add(buf[i]);
            }
            bufRecvTime = new Date();//重置收数时间

            //开始解析数据包 有三种解析方式：控制器的 ，激光的， 宏电DTU的
            if (parseType == GlobalVariables.PACKAGE_PARSE_TYPE_CONTROL)//解析控制器参数
            {
                int index = 0;
                for (int i = 0; i < bufferCache.size() && i + 5 <= bufferCache.size(); i++) {
                    if (bufferCache.get(i) == (byte) 0xFF && (bufferCache.get(i + 1) == (byte) 0xFF || (bufferCache.get(i + 1) == (byte) 0x00 && bufferCache.get(i + 2) == (byte) 0x9))) {
                        int len = (bufferCache.get(i + 3) << 8) + (bufferCache.get(i + 4) & 0xFF);
                        if (i + len - 1 < bufferCache.size()) {
                            byte[] tmpBuf = new byte[len];
                            for (int j = 0; j <= len - 1; j++) {
                                tmpBuf[j] = bufferCache.get(i + j);
                            }
                            int crc = CommonFunctions.crc_16(tmpBuf, tmpBuf.length - 2);
                            if (((tmpBuf[tmpBuf.length - 2] << 8) & 0xFF00) + (tmpBuf[tmpBuf.length - 1] & 0xFF) == (crc & 0xFFFF)) {
                                _sendBroadcast(GlobalVariables.SOCKET_BUFFER_BROADCAST, tmpBuf);//获取到控制帧 将其广播
                            }
                            if (tmpBuf[0] == (byte) 0xFF && tmpBuf[1] == (byte) 0x00 && tmpBuf[2] == (byte) 0x09) {
                                _sendBroadcast(GlobalVariables.SOCKET_BUFFER_BROADCAST, tmpBuf);//获取到单车帧 将其广播
                            }
                            index = i + len;
                            i = i + len - 1;
                        }
                    }
                }
                List<Byte> listTmp = new ArrayList<Byte>();
                for (int i = index; i < bufferCache.size(); i++) {
                    listTmp.add(bufferCache.get(i));
                }
                bufferCache = listTmp;
            } else if (parseType == GlobalVariables.PACKAGE_PARSE_TYPE_LASER)//解析激光参数
            {
                int index = 0;
                for (int i = 0; i < bufferCache.size() && i + 1 < bufferCache.size(); i++) {
                    if (bufferCache.get(i) == (byte) 0xFF && bufferCache.get(i + 1) == (byte) 0xFF) {
                        int s = i;
                        for (int j = i + 2; j < bufferCache.size(); j++) {
                            if (bufferCache.get(j) == (byte) 0xEE) {
                                int e = j;
                                if (e - s + 1 > 20) {
                                    byte[] tmpBuf = new byte[e - s + 1];
                                    int tmpBufIndex = 0;
                                    for (int k = s; k <= e; k++) {
                                        tmpBuf[tmpBufIndex++] = bufferCache.get(k);
                                    }
                                    _sendBroadcast(GlobalVariables.SOCKET_BUFFER_BROADCAST, tmpBuf);//获取到激光透传帧 将其广播
                                }
                                i = e;
                                index = e + 1;
                            }
                        }
                    }
                }
                List<Byte> listTmp = new ArrayList<Byte>();
                for (int i = index; i < bufferCache.size(); i++) {
                    listTmp.add(bufferCache.get(i));
                }
                bufferCache = listTmp;
            }
            else if(parseType == GlobalVariables.PACKAGE_PARSE_TYPE_DTU_HD){//解析HD DTU返回数据
                //int leastLen = 6;//最短处理长度
                int tmpIndex = 0;
                int validLen = 0;
                while(bufferCache.size() > 0){
                    //找AT指令头
                    int i=  0 ,atHead = 0;
                    boolean res = false;
                    while(i < (bufferCache.size() - 1)){
                        if(bufferCache.get(i) == 'A' && bufferCache.get(i +1 ) == 'T'){
                            //找到AT指令头
                            res = true;
                            break;
                        }
                        i++;
                    }

                    if(!res){
                        //未找到帧头
                        List<Byte> listTmp = new ArrayList<Byte>();
                        for (int index = 0; index < bufferCache.size(); index++) {
                            listTmp.add(bufferCache.get(index));
                        }
                        bufferCache = listTmp;
                        break;
                    }

                    //找到帧头
                    atHead = i;
                    i+=2;
                    res = false;

                    //开始找帧尾OK or ERROR
                    while(i < bufferCache.size()){
                        if((i+1) < bufferCache.size() && bufferCache.get(i) == 'O' && bufferCache.get(i + 1) == 'K'){
                            //找到帧尾
                            res = true;
                            i+=2;//定位到帧尾结束
                            break;
                        }
                        else if((i + 4) < bufferCache.size() && bufferCache.get(i) == 'E' && bufferCache.get(i + 1) == 'R' && bufferCache.get(i + 2) == 'R' && bufferCache.get(i + 3) == 'O' && bufferCache.get(i + 4) == 'R'){
                            //找到帧尾
                            res = true;
                            i+=5;
                            break;
                        }
                        i++;
                    }

                    if(!res){
                        //未找到帧尾
                        List<Byte> listTmp = new ArrayList<Byte>();
                        for (int index = atHead; index < bufferCache.size(); index++) {
                            listTmp.add(bufferCache.get(index));
                        }
                        bufferCache = listTmp;
                        break;
                    }

                    //找到帧尾
                    validLen = i - atHead;//有效数据长度

                    byte[] byteData = new byte[validLen];
                    for(int j=0;j<validLen;j++){
                        byteData[j] = bufferCache.get(j+atHead);
                    }
                    //System.arraycopy(bufferCache.toArray(), atHead, byteData, 0, validLen);
                    String data = new String(byteData);

                    _sendBroadcast(GlobalVariables.SOCKET_BUFFER_BROADCAST, data);//发送宏电DTU广播

                    //处理完毕

                    List<Byte> listTmp = new ArrayList<Byte>();
                    for (int index = atHead+validLen; index < bufferCache.size(); index++) {
                        listTmp.add(bufferCache.get(index));
                    }
                    bufferCache = listTmp;
                }

            }
         }
    }
}
