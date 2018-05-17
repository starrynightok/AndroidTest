package com.wanji.jdbluetooth.tools;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

/**
 * Created by admin on 2018/2/9.
 */

public class Package {

    public static byte[] Byte2byte(List<Byte> B) {
        byte[] b = new byte[B.size()];
        for (int i = 0; i < b.length; i++) {
            b[i] = B.get(i);
        }
        return b;
    }

    public static int[] byte2int(byte[] b) {
        int[] res = new int[b.length];
        for (int i = 0; i < b.length; i++) {
            res[i] = b[i];
            res[i] = res[i] & 0xFF;
        }
        return res;
    }

    public static int bcd(int b) {
        String str = (b / 16 + "") + (b % 16 + "");
        if (str.length() > 1) {

            return (b / 16 * 10 + b % 16 * 1);
        } else {
            return (b / 16 * 1);
        }
    }

    //查询控制器参数
    public static byte[] getPackage_01() {
        List<Byte> buf = new ArrayList<Byte>();
        buf.add((byte) 0xFF);
        buf.add((byte) 0xFF);//帧头
        buf.add((byte) 0x01);//命令号
        buf.add((byte) 0x00);
        buf.add((byte) 0x0A);//帧长
        buf.add((byte) 0x00);
        buf.add((byte) 0x00);
        buf.add((byte) 0x00);//保留
        int crc = CommonFunctions.crc_16(Byte2byte(buf), buf.toArray().length);
        buf.add((byte) ((crc >> 8) & 0xFF));
        buf.add((byte) (crc & 0xFF));
        return Byte2byte(buf);
    }

    //查询控制器参数-回复 新
    public static HashMap<String, String> parsePackage_01_New(byte[] bufTmp) {
        int[] buf = byte2int(bufTmp);
        HashMap<String, String> res = new HashMap<String, String>();
        int index = 5;
        res.put("new_versionId", new String(buf, index, 11));
        index += 11;
        res.put("new_installType", buf[index++] + "");
        res.put("new_baudRate", buf[index++] + "");
        res.put("new_dog", buf[index++] + "");
        res.put("new_verticalLaserLeftHeight", ((buf[index++] << 8) + buf[index++]) + "");
        res.put("new_verticalLaserRightHeight", ((buf[index++] << 8) + buf[index++]) + "");
        res.put("new_tiltLaserLeftHeight", ((buf[index++] << 8) + buf[index++]) + "");
        res.put("new_tiltLaserRightHeight", ((buf[index++] << 8) + buf[index++]) + "");
        res.put("new_laserPosSpace", ((buf[index++] << 8) + buf[index++]) + "");
        res.put("new_tiltLaserAngle", ((buf[index++] << 8) + buf[index++]) + "");
        res.put("new_laneWidth", ((buf[index++] << 8) + buf[index++]) + "");
        res.put("new_isolationRightWidth", ((buf[index++] << 8) + buf[index++]) + "");
        res.put("new_isolationLeftWidth", ((buf[index++] << 8) + buf[index++]) + "");
        res.put("new_powerRestartCount", ((buf[index++] << 8) + buf[index++]) + "");
        res.put("new_dogRestartCount", ((buf[index++] << 8) + buf[index++]) + "");
        res.put("new_highStart", ((buf[index++] << 24) + (buf[index++] << 16) + (buf[index++] << 8) + buf[index++]) + "");
        res.put("new_laserHorizontalPos", ((buf[index++] << 8) + buf[index++]) + "");
        res.put("new_verticalCenterPos", ((buf[index++] << 8) + buf[index++]) + "");
        res.put("new_tiltCenterPos", ((buf[index++] << 8) + buf[index++]) + "");
        res.put("new_startPos", ((buf[index++] << 8) + buf[index++]) + "");
        res.put("new_endPos", ((buf[index++] << 8) + buf[index++]) + "");
        res.put("new_singleVehSwitch", buf[index++] + "");
        res.put("new_devType", buf[index++] + "");
        res.put("new_laneType", buf[index++] + "");
        res.put("new_leftLaneNum", buf[index++] + "");
        res.put("new_rightLaneNum", buf[index++] + "");
        res.put("new_netType", buf[index++] + "");
        res.put("new_sdCard", buf[index++] + "");

        return res;
    }

    //设置控制器参数 新
    public static byte[] getPackage_02_New(HashMap<String, String> hm) {
        List<Byte> buf = new ArrayList<Byte>();
        buf.add((byte) 0xFF);
        buf.add((byte) 0xFF);//帧头
        buf.add((byte) 0x02);//命令号
        buf.add((byte) 0x00);
        buf.add((byte) 0x48);//帧长
        byte[] vi = hm.get("new_versionId").getBytes();
        for (int i = 0; i < 11; i++) {
            if (i < vi.length) {
                buf.add(vi[i]);
            } else {
                buf.add((byte) 0x00);
            }
        }
        buf.add((byte) Integer.parseInt(hm.get("new_installType")));
        buf.add((byte) Integer.parseInt(hm.get("new_baudRate")));
        buf.add((byte) Integer.parseInt(hm.get("new_dog")));
        buf.add((byte) ((Integer.parseInt(hm.get("new_verticalLaserLeftHeight")) >> 8) & 0xFF));
        buf.add((byte) (Integer.parseInt(hm.get("new_verticalLaserLeftHeight")) & 0xFF));
        buf.add((byte) ((Integer.parseInt(hm.get("new_verticalLaserRightHeight")) >> 8) & 0xFF));
        buf.add((byte) (Integer.parseInt(hm.get("new_verticalLaserRightHeight")) & 0xFF));
        buf.add((byte) ((Integer.parseInt(hm.get("new_tiltLaserLeftHeight")) >> 8) & 0xFF));
        buf.add((byte) (Integer.parseInt(hm.get("new_tiltLaserLeftHeight")) & 0xFF));
        buf.add((byte) ((Integer.parseInt(hm.get("new_tiltLaserRightHeight")) >> 8) & 0xFF));
        buf.add((byte) (Integer.parseInt(hm.get("new_tiltLaserRightHeight")) & 0xFF));
        buf.add((byte) ((Integer.parseInt(hm.get("new_laserPosSpace")) >> 8) & 0xFF));
        buf.add((byte) (Integer.parseInt(hm.get("new_laserPosSpace")) & 0xFF));
        buf.add((byte) ((Integer.parseInt(hm.get("new_tiltLaserAngle")) >> 8) & 0xFF));
        buf.add((byte) (Integer.parseInt(hm.get("new_tiltLaserAngle")) & 0xFF));
        buf.add((byte) ((Integer.parseInt(hm.get("new_laneWidth")) >> 8) & 0xFF));
        buf.add((byte) (Integer.parseInt(hm.get("new_laneWidth")) & 0xFF));
        buf.add((byte) ((Integer.parseInt(hm.get("new_isolationRightWidth")) >> 8) & 0xFF));
        buf.add((byte) (Integer.parseInt(hm.get("new_isolationRightWidth")) & 0xFF));
        buf.add((byte) ((Integer.parseInt(hm.get("new_isolationLeftWidth")) >> 8) & 0xFF));
        buf.add((byte) (Integer.parseInt(hm.get("new_isolationLeftWidth")) & 0xFF));
        buf.add((byte) ((Integer.parseInt(hm.get("new_powerRestartCount")) >> 8) & 0xFF));
        buf.add((byte) (Integer.parseInt(hm.get("new_powerRestartCount")) & 0xFF));
        buf.add((byte) ((Integer.parseInt(hm.get("new_dogRestartCount")) >> 8) & 0xFF));
        buf.add((byte) (Integer.parseInt(hm.get("new_dogRestartCount")) & 0xFF));
        buf.add((byte) ((Integer.parseInt(hm.get("new_highStart")) >> 24) & 0xFF));
        buf.add((byte) ((Integer.parseInt(hm.get("new_highStart")) >> 16) & 0xFF));
        buf.add((byte) ((Integer.parseInt(hm.get("new_highStart")) >> 8) & 0xFF));
        buf.add((byte) (Integer.parseInt(hm.get("new_highStart")) & 0xFF));
        buf.add((byte) ((Integer.parseInt(hm.get("new_laserHorizontalPos")) >> 8) & 0xFF));
        buf.add((byte) (Integer.parseInt(hm.get("new_laserHorizontalPos")) & 0xFF));
        buf.add((byte) ((Integer.parseInt(hm.get("new_verticalCenterPos")) >> 8) & 0xFF));
        buf.add((byte) (Integer.parseInt(hm.get("new_verticalCenterPos")) & 0xFF));
        buf.add((byte) ((Integer.parseInt(hm.get("new_tiltCenterPos")) >> 8) & 0xFF));
        buf.add((byte) (Integer.parseInt(hm.get("new_tiltCenterPos")) & 0xFF));
        buf.add((byte) ((Integer.parseInt(hm.get("new_startPos")) >> 8) & 0xFF));
        buf.add((byte) (Integer.parseInt(hm.get("new_startPos")) & 0xFF));
        buf.add((byte) ((Integer.parseInt(hm.get("new_endPos")) >> 8) & 0xFF));
        buf.add((byte) (Integer.parseInt(hm.get("new_endPos")) & 0xFF));
        buf.add((byte) Integer.parseInt(hm.get("new_singleVehSwitch")));
        buf.add((byte) Integer.parseInt(hm.get("new_devType")));
        buf.add((byte) Integer.parseInt(hm.get("new_laneType")));
        buf.add((byte) Integer.parseInt(hm.get("new_leftLaneNum")));
        buf.add((byte) Integer.parseInt(hm.get("new_rightLaneNum")));
        buf.add((byte) Integer.parseInt(hm.get("new_netType")));
        buf.add((byte) Integer.parseInt(hm.get("new_sdCard")));
        for (int i = 0; i < 8; i++)//预留8位
        {
            buf.add((byte) 0x00);
        }

        int crc = CommonFunctions.crc_16(Byte2byte(buf), buf.toArray().length);
        buf.add((byte) ((crc >> 8) & 0xFF));
        buf.add((byte) (crc & 0xFF));
        return Byte2byte(buf);
    }

    //设置控制器参数-回复
    public static boolean parsePackage_02(byte[] buf) {
        return true;
    }

    //重启控制器
    public static byte[] getPackage_03() {
        List<Byte> buf = new ArrayList<Byte>();
        buf.add((byte) 0xFF);
        buf.add((byte) 0xFF);//帧头
        buf.add((byte) 0x03);//命令号
        buf.add((byte) 0x00);
        buf.add((byte) 0x0A);//帧长
        buf.add((byte) 0x00);
        buf.add((byte) 0x00);
        buf.add((byte) 0x00);//保留
        int crc = CommonFunctions.crc_16(Byte2byte(buf), buf.toArray().length);
        buf.add((byte) ((crc >> 8) & 0xFF));
        buf.add((byte) (crc & 0xFF));
        return Byte2byte(buf);
    }

    //重启控制器-回复
    public static boolean parsePackage_03(byte[] buf) {
        return true;
    }

    //初始化控制器
    public static byte[] getPackage_04() {
        List<Byte> buf = new ArrayList<Byte>();
        buf.add((byte) 0xFF);
        buf.add((byte) 0xFF);//帧头
        buf.add((byte) 0x04);//命令号
        buf.add((byte) 0x00);
        buf.add((byte) 0x0A);//帧长
        buf.add((byte) 0x00);
        buf.add((byte) 0x00);
        buf.add((byte) 0x00);//保留
        int crc = CommonFunctions.crc_16(Byte2byte(buf), buf.toArray().length);
        buf.add((byte) ((crc >> 8) & 0xFF));
        buf.add((byte) (crc & 0xFF));
        return Byte2byte(buf);
    }

    //初始化控制器-回复
    public static boolean parsePackage_04(byte[] buf) {
        return true;
    }

    //获取当前ADC电压值
    public static byte[] getPackage_06() {
        List<Byte> buf = new ArrayList<Byte>();
        buf.add((byte) 0xFF);
        buf.add((byte) 0xFF);//帧头
        buf.add((byte) 0x06);//命令号
        buf.add((byte) 0x00);
        buf.add((byte) 0x0A);//帧长
        buf.add((byte) 0x00);
        buf.add((byte) 0x00);
        buf.add((byte) 0x00);//保留
        int crc = CommonFunctions.crc_16(Byte2byte(buf), buf.toArray().length);
        buf.add((byte) ((crc >> 8) & 0xFF));
        buf.add((byte) (crc & 0xFF));
        return Byte2byte(buf);
    }

    //获取当前ADC电压值-回复
    public static HashMap<String, String> parsePackage_06(byte[] bufTmp) {
        int[] buf = byte2int(bufTmp);
        HashMap<String, String> res = new HashMap<String, String>();
        int index = 5;
        String adcV = "";
        int f = 0;
        if (buf[index] == 0x00) {
            f = 1;
        } else {
            f = -1;
        }
        index++;
        res.put("adcV", (f * buf[index++]) + "." + buf[index++]);
        return res;
    }

    //查询设备重启信息
    public static byte[] getPackage_07() {
        List<Byte> buf = new ArrayList<Byte>();
        buf.add((byte) 0xFF);
        buf.add((byte) 0xFF);//帧头
        buf.add((byte) 0x07);//命令号
        buf.add((byte) 0x00);
        buf.add((byte) 0x0A);//帧长
        buf.add((byte) 0x00);
        buf.add((byte) 0x00);
        buf.add((byte) 0x00);//保留
        int crc = CommonFunctions.crc_16(Byte2byte(buf), buf.toArray().length);
        buf.add((byte) ((crc >> 8) & 0xFF));
        buf.add((byte) (crc & 0xFF));
        return Byte2byte(buf);
    }

    //查询设备重启信息-回复
    public static List<String[]> parsePackage_07(byte[] bufTmp) {
        int[] buf = byte2int(bufTmp);
        int index = 7;
        int rc = (buf[index++] << 24) + (buf[index++] << 16) + (buf[index++] << 8) + buf[index++];
        int count = (buf.length - 13) / 7;
        List<String[]> restartInfo = new ArrayList<String[]>();
        for (int i = 0; i < count; i++) {
            String[] tmp = new String[2];
            tmp[0] = (bcd(buf[index]) + 2000) + "-" + String.format("%02d", bcd(buf[index + 1])) + "-" + String.format("%02d", bcd(buf[index + 2])) + " " + String.format("%02d", bcd(buf[index + 4])) + ":" + String.format("%02d", bcd(buf[index + 5])) + ":" + String.format("%02d", bcd(buf[index + 6]));
            tmp[1] = buf[index + 3] == 0x00 ? "硬件重启" : "软件重启";
            restartInfo.add(tmp);
            index += 7;
        }
        return restartInfo;
    }

    //上传的单车数据包
    public static HashMap<String, String> parsePackage_09(byte[] bufTmp) {
        int[] buf = byte2int(bufTmp);
        HashMap<String, String> res = new HashMap<String, String>();
        int index = 5;
        int len = (buf[index++] << 24) + (buf[index++] << 16) + (buf[index++] << 8) + buf[index++];
        int speed = buf[index++];
        Calendar today = Calendar.getInstance();//获取当前时间
        int h = today.get(Calendar.HOUR_OF_DAY);
        int m = today.get(Calendar.MINUTE);
        int s = today.get(Calendar.SECOND);
        String nowveh_time = String.format("%02d", h) + ":" + String.format("%02d", m) + ":" + String.format("%02d", s);
        index += 11;
        String vehType = "";
        switch (buf[index++]) {
            case 1:
                vehType = "中小客车 ";
                break;
            case 2:
                vehType = "小货车";
                break;
            case 3:
                vehType = "大客车";
                break;
            case 4:
                vehType = "中型货车";
                break;
            case 5:
                vehType = "大型货车";
                break;
            case 6:
                vehType = "特大型货车";
                break;
            case 7:
                vehType = "集装箱车";
                break;
            case 8:
                vehType = "拖拉机";
                break;
            case 9:
                vehType = "摩托车";
                break;
            default:
                vehType = "未知";
                break;
        }
        index += 16;
        int lane = buf[index++];
        index += 2;
        int height = (buf[index++] << 8) + buf[index++];
        int width = (buf[index++] << 8) + buf[index++];
        res.put("nowveh_time", nowveh_time);
        res.put("nowveh_lane_type_speed", lane + "/" + vehType + "/" + speed);
        res.put("nowveh_len_width_height", len + "/" + width + "/" + height);
        return res;
    }

    //查询控制器时间
    public static byte[] getPackage_10() {
        List<Byte> buf = new ArrayList<Byte>();
        buf.add((byte) 0xFF);
        buf.add((byte) 0xFF);//帧头
        buf.add((byte) 0x10);//命令号
        buf.add((byte) 0x00);
        buf.add((byte) 0x0A);//帧长
        buf.add((byte) 0x00);
        buf.add((byte) 0x00);
        buf.add((byte) 0x00);//保留
        int crc = CommonFunctions.crc_16(Byte2byte(buf), buf.toArray().length);
        buf.add((byte) ((crc >> 8) & 0xFF));
        buf.add((byte) (crc & 0xFF));
        return Byte2byte(buf);
    }

    //查询控制器时间-回复
    public static HashMap<String, String> parsePackage_10(byte[] bufTmp) {
        int[] buf = byte2int(bufTmp);
        HashMap<String, String> res = new HashMap<String, String>();
        int index = 5;
        String s = buf[index] > 9 ? (buf[index] + "") : ("0" + buf[index]);
        index++;
        String m = buf[index] > 9 ? (buf[index] + "") : ("0" + buf[index]);
        index++;
        String h = buf[index] > 9 ? (buf[index] + "") : ("0" + buf[index]);
        index++;
        index++;
        String D = buf[index] > 9 ? (buf[index] + "") : ("0" + buf[index]);
        index++;
        String M = buf[index] > 9 ? (buf[index] + "") : ("0" + buf[index]);
        index++;
        String Y = buf[index + 1] + "" + buf[index];
        res.put("controlTime", Y + "-" + M + "-" + D + " " + h + ":" + m + ":" + s);
        return res;
    }

    //设置控制器时间
    public static byte[] getPackage_11() {
        List<Byte> buf = new ArrayList<Byte>();

        Calendar today = Calendar.getInstance();//获取当前时间
        int Y = today.get(Calendar.YEAR);
        int M = today.get(Calendar.MONTH);
        int D = today.get(Calendar.DAY_OF_MONTH);
        int h = today.get(Calendar.HOUR_OF_DAY);
        int m = today.get(Calendar.MINUTE);
        int s = today.get(Calendar.SECOND);
        buf.add((byte) 0xFF);
        buf.add((byte) 0xFF);//帧头
        buf.add((byte) 0x11);//命令号
        buf.add((byte) 0x00);
        buf.add((byte) 0x0F);//帧长
        buf.add((byte) s);
        buf.add((byte) m);
        buf.add((byte) h);
        buf.add((byte) today.get(Calendar.DAY_OF_WEEK));
        buf.add((byte) D);
        buf.add((byte) M);
        buf.add((byte) (Y % 100));
        buf.add((byte) (Y / 100));

        int crc = CommonFunctions.crc_16(Byte2byte(buf), buf.toArray().length);
        buf.add((byte) ((crc >> 8) & 0xFF));
        buf.add((byte) (crc & 0xFF));
        return Byte2byte(buf);
    }

    //获取站点编号和设备识别码
    public static byte[] getPackage_12() {
        List<Byte> buf = new ArrayList<Byte>();
        buf.add((byte) 0xFF);
        buf.add((byte) 0xFF);//帧头
        buf.add((byte) 0x12);//命令号
        buf.add((byte) 0x00);
        buf.add((byte) 0x0A);//帧长
        buf.add((byte) 0x00);
        buf.add((byte) 0x00);
        buf.add((byte) 0x00);//保留
        int crc = CommonFunctions.crc_16(Byte2byte(buf), buf.toArray().length);
        buf.add((byte) ((crc >> 8) & 0xFF));
        buf.add((byte) (crc & 0xFF));
        return Byte2byte(buf);
    }

    //获取站点编号和设备识别码-回复
    public static HashMap<String, String> parsePackage_12(byte[] bufTmp) {
        int[] buf = byte2int(bufTmp);
        HashMap<String, String> res = new HashMap<String, String>();
        int index = 5;
        res.put("devId", new String(buf, index, 16));
        index += 16;
        res.put("stationId", new String(buf, index, 15));
        return res;
    }

    //设置站点编号和设备识别码
    public static byte[] getPackage_13(HashMap<String, String> hm) {
        List<Byte> buf = new ArrayList<Byte>();

        buf.add((byte) 0xFF);
        buf.add((byte) 0xFF);//帧头
        buf.add((byte) 0x13);//命令号
        buf.add((byte) 0x00);
        buf.add((byte) 0x26);//帧长
        byte[] tmp = hm.get("devId").getBytes();
        for (int i = 0; i < 16; i++) {
            if (i >= tmp.length) {
                buf.add((byte) 0x00);
            } else {
                buf.add(tmp[i]);
            }
        }
        tmp = hm.get("stationId").getBytes();
        for (int i = 0; i < 15; i++) {
            if (i >= tmp.length) {
                buf.add((byte) 0x00);
            } else {
                buf.add(tmp[i]);
            }
        }

        int crc = CommonFunctions.crc_16(Byte2byte(buf), buf.toArray().length);
        buf.add((byte) ((crc >> 8) & 0xFF));
        buf.add((byte) (crc & 0xFF));
        return Byte2byte(buf);
    }

    //查询车型判别阈值
    public static byte[] getPackage_14() {
        List<Byte> buf = new ArrayList<Byte>();
        buf.add((byte) 0xFF);
        buf.add((byte) 0xFF);//帧头
        buf.add((byte) 0x14);//命令号
        buf.add((byte) 0x00);
        buf.add((byte) 0x0A);//帧长
        buf.add((byte) 0x00);
        buf.add((byte) 0x00);
        buf.add((byte) 0x00);//保留
        int crc = CommonFunctions.crc_16(Byte2byte(buf), buf.toArray().length);
        buf.add((byte) ((crc >> 8) & 0xFF));
        buf.add((byte) (crc & 0xFF));
        return Byte2byte(buf);
    }

    //查询车型判别阈值-回复
    public static HashMap<String, String> parsePackage_14(byte[] bufTmp) {
        int[] buf = byte2int(bufTmp);
        HashMap<String, String> res = new HashMap<String, String>();
        int index = 5;
        res.put("xxcLenThre", ((buf[index++] << 8) + buf[index++]) + "");
        res.put("dxcLenThre", ((buf[index++] << 8) + buf[index++]) + "");
        res.put("xxcHeightThre", ((buf[index++] << 8) + buf[index++]) + "");
        res.put("dkcLenThre", ((buf[index++] << 8) + buf[index++]) + "");
        res.put("dkcHeightThre", ((buf[index++] << 8) + buf[index++]) + "");
        res.put("jzxHeightMax", ((buf[index++] << 8) + buf[index++]) + "");
        res.put("jzxHeightMin", ((buf[index++] << 8) + buf[index++]) + "");
        res.put("first", buf[index++] + "");
        index += 7;
        res.put("threType", ((buf[index++] << 8) + buf[index++]) + "");
        return res;
    }

    //设置车型判别阈值
    public static byte[] getPackage_15(HashMap<String, String> hm) {
        List<Byte> buf = new ArrayList<Byte>();

        buf.add((byte) 0xFF);
        buf.add((byte) 0xFF);//帧头
        buf.add((byte) 0x15);//命令号
        buf.add((byte) 0x00);
        buf.add((byte) 0x1F);//帧长
        buf.add((byte) ((Integer.parseInt(hm.get("xxcLenThre")) >> 8) & 0xFF));
        buf.add((byte) (Integer.parseInt(hm.get("xxcLenThre")) & 0xFF));
        buf.add((byte) ((Integer.parseInt(hm.get("dxcLenThre")) >> 8) & 0xFF));
        buf.add((byte) (Integer.parseInt(hm.get("dxcLenThre")) & 0xFF));
        buf.add((byte) ((Integer.parseInt(hm.get("xxcHeightThre")) >> 8) & 0xFF));
        buf.add((byte) (Integer.parseInt(hm.get("xxcHeightThre")) & 0xFF));
        buf.add((byte) ((Integer.parseInt(hm.get("dkcLenThre")) >> 8) & 0xFF));
        buf.add((byte) (Integer.parseInt(hm.get("dkcLenThre")) & 0xFF));
        buf.add((byte) ((Integer.parseInt(hm.get("dkcHeightThre")) >> 8) & 0xFF));
        buf.add((byte) (Integer.parseInt(hm.get("dkcHeightThre")) & 0xFF));
        buf.add((byte) ((Integer.parseInt(hm.get("jzxHeightMax")) >> 8) & 0xFF));
        buf.add((byte) (Integer.parseInt(hm.get("jzxHeightMax")) & 0xFF));
        buf.add((byte) ((Integer.parseInt(hm.get("jzxHeightMin")) >> 8) & 0xFF));
        buf.add((byte) (Integer.parseInt(hm.get("jzxHeightMin")) & 0xFF));
        buf.add((byte) Integer.parseInt(hm.get("first")));
        buf.add((byte) 0x00);
        buf.add((byte) 0x00);
        buf.add((byte) 0x00);
        buf.add((byte) 0x00);
        buf.add((byte) 0x00);
        buf.add((byte) 0x00);
        buf.add((byte) 0x00);
        buf.add((byte) ((Integer.parseInt(hm.get("threType")) >> 8) & 0xFF));
        buf.add((byte) (Integer.parseInt(hm.get("threType")) & 0xFF));

        int crc = CommonFunctions.crc_16(Byte2byte(buf), buf.toArray().length);
        buf.add((byte) ((crc >> 8) & 0xFF));
        buf.add((byte) (crc & 0xFF));
        return Byte2byte(buf);
    }

    //设置车型判别阈值-回复
    public static boolean parsePackage_15(byte[] buf) {
        return true;
    }

    //获取车道交通量上传参数
    public static byte[] getPackage_20() {
        List<Byte> buf = new ArrayList<Byte>();
        buf.add((byte) 0xFF);
        buf.add((byte) 0xFF);//帧头
        buf.add((byte) 0x20);//命令号
        buf.add((byte) 0x00);
        buf.add((byte) 0x0A);//帧长
        buf.add((byte) 0x00);
        buf.add((byte) 0x00);
        buf.add((byte) 0x00);//保留
        int crc = CommonFunctions.crc_16(Byte2byte(buf), buf.toArray().length);
        buf.add((byte) ((crc >> 8) & 0xFF));
        buf.add((byte) (crc & 0xFF));
        return Byte2byte(buf);
    }

    //获取车道交通量上传参数-回复
    public static HashMap<String, String> parsePackage_20(byte[] bufTmp) {
        int[] buf = byte2int(bufTmp);
        int index = 5;
        HashMap<String, String> res = new HashMap<String, String>();
        res.put("uploadType", buf[index] + "");
        return res;
    }

    //设置车道交通量上传参数
    public static byte[] getPackage_21(HashMap<String, String> hm) {
        List<Byte> buf = new ArrayList<Byte>();

        buf.add((byte) 0xFF);
        buf.add((byte) 0xFF);//帧头
        buf.add((byte) 0x21);//命令号
        buf.add((byte) 0x00);
        buf.add((byte) 0x08);//帧长
        buf.add((byte) Integer.parseInt(hm.get("uploadType")));

        int crc = CommonFunctions.crc_16(Byte2byte(buf), buf.toArray().length);
        buf.add((byte) ((crc >> 8) & 0xFF));
        buf.add((byte) (crc & 0xFF));
        return Byte2byte(buf);
    }

    //设置车道交通量上传参数-回复
    public static boolean parsePackage_21(byte[] buf) {
        return true;
    }

    //获取控制器异常参数
    public static byte[] getPackage_22() {
        List<Byte> buf = new ArrayList<Byte>();
        buf.add((byte) 0xFF);
        buf.add((byte) 0xFF);//帧头
        buf.add((byte) 0x22);//命令号
        buf.add((byte) 0x00);
        buf.add((byte) 0x0A);//帧长
        buf.add((byte) 0x00);
        buf.add((byte) 0x00);
        buf.add((byte) 0x00);//保留
        int crc = CommonFunctions.crc_16(Byte2byte(buf), buf.toArray().length);
        buf.add((byte) ((crc >> 8) & 0xFF));
        buf.add((byte) (crc & 0xFF));
        return Byte2byte(buf);
    }

    //获取控制器异常参数-回复
    public static HashMap<String, String> parsePackage_22(byte[] bufTmp) {
        int[] buf = byte2int(bufTmp);
        HashMap<String, String> res = new HashMap<String, String>();
        int index = 5;
        res.put("exSwitch", buf[index++] + "");
        res.put("packageVehMax", ((buf[index++] << 8) + buf[index++]) + "");
        res.put("nightVehMax", ((buf[index++] << 8) + buf[index++]) + "");
        res.put("exPackageSum", ((buf[index++] << 24) + (buf[index++] << 16) + (buf[index++] << 8) + buf[index++]) + "");
        res.put("exTime", (2000 + buf[index++]) + "-" + String.format("%02d", buf[index++]) + "-" + String.format("%02d", buf[index++]) + " " + String.format("%02d", buf[index++]) + ":" + String.format("%02d", buf[index++]) + ":" + String.format("%02d", buf[index++]));
        if (buf[index] == 0) {
            res.put("exState", "正常");
        } else if (buf[index] == 1) {
            res.put("exState", "待异常");
        } else if (buf[index] == 2) {
            res.put("exState", "异常");
        }
        index++;
        res.put("min5VehSum", ((buf[index++] << 8) + buf[index++]) + "");
        res.put("laser1Frame", ((buf[index++] << 8) + buf[index++]) + "");
        res.put("laser2Frame", ((buf[index++] << 8) + buf[index++]) + "");
        return res;
    }

    //设置控制器异常参数
    public static byte[] getPackage_23(HashMap<String, String> hm) {
        List<Byte> buf = new ArrayList<Byte>();
        buf.add((byte) 0xFF);
        buf.add((byte) 0xFF);//帧头
        buf.add((byte) 0x23);//命令号
        buf.add((byte) 0x00);
        buf.add((byte) 0x10);//帧长
        buf.add((byte) Integer.parseInt(hm.get("exSwitch")));
        buf.add((byte) ((Integer.parseInt(hm.get("packageVehMax")) >> 8) & 0xFF));
        buf.add((byte) (Integer.parseInt(hm.get("packageVehMax")) & 0xFF));
        buf.add((byte) ((Integer.parseInt(hm.get("nightVehMax")) >> 8) & 0xFF));
        buf.add((byte) (Integer.parseInt(hm.get("nightVehMax")) & 0xFF));
        buf.add((byte) ((Integer.parseInt(hm.get("exPackageSum")) >> 24) & 0xFF));
        buf.add((byte) ((Integer.parseInt(hm.get("exPackageSum")) >> 16) & 0xFF));
        buf.add((byte) ((Integer.parseInt(hm.get("exPackageSum")) >> 8) & 0xFF));
        buf.add((byte) (Integer.parseInt(hm.get("exPackageSum")) & 0xFF));

        int crc = CommonFunctions.crc_16(Byte2byte(buf), buf.toArray().length);
        buf.add((byte) ((crc >> 8) & 0xFF));
        buf.add((byte) (crc & 0xFF));
        return Byte2byte(buf);
    }

    //设置控制器异常参数-回复
    public static boolean parsePackage_23(byte[] buf) {
        return true;
    }

    //获取/设置检定单车开关状态
    public static byte[] getPackage_24(int cmd) {
        List<Byte> buf = new ArrayList<Byte>();

        buf.add((byte) 0xFF);
        buf.add((byte) 0xFF);//帧头
        buf.add((byte) 0x24);//命令号
        buf.add((byte) 0x00);
        buf.add((byte) 0x08);//帧长
        buf.add((byte) cmd);

        int crc = CommonFunctions.crc_16(Byte2byte(buf), buf.toArray().length);
        buf.add((byte) ((crc >> 8) & 0xFF));
        buf.add((byte) (crc & 0xFF));
        return Byte2byte(buf);
    }

    //获取/设置检定单车开关状态-回复
    public static Object parsePackage_24(byte[] bufTmp) {
        int[] buf = byte2int(bufTmp);
        int index = 5;
        if (buf[index] == 0x02) {
            return true;
        } else {
            HashMap<String, String> hm = new HashMap<String, String>();
            hm.put("singleVehSwitch", buf[index] + "");
            return hm;
        }
    }

    //查询/设置车型转换准则
    public static byte[] getPackage_25(int cmd, HashMap<String, String> hm) {
        List<Byte> buf = new ArrayList<Byte>();

        if (cmd == 0x3C) {
            buf.add((byte) 0xFF);
            buf.add((byte) 0xFF);//帧头
            buf.add((byte) 0x25);//命令号
            buf.add((byte) 0x00);
            buf.add((byte) 0x10);//帧长
            buf.add((byte) cmd);
            buf.add((byte) 0x00);
            buf.add((byte) 0x00);
            buf.add((byte) 0x00);
            buf.add((byte) 0x00);
            buf.add((byte) 0x00);
            buf.add((byte) 0x00);
            buf.add((byte) 0x00);
            buf.add((byte) 0x00);
        } else {
            buf.add((byte) 0xFF);
            buf.add((byte) 0xFF);//帧头
            buf.add((byte) 0x25);//命令号
            buf.add((byte) 0x00);
            buf.add((byte) 0x16);//帧长
            buf.add((byte) cmd);
            buf.add((byte) Integer.parseInt(hm.get("mtcRule")));
            buf.add((byte) Integer.parseInt(hm.get("slcRule")));
            buf.add((byte) Integer.parseInt(hm.get("xrRule")));
            buf.add((byte) Integer.parseInt(hm.get("jzxRule")));
            buf.add((byte) Integer.parseInt(hm.get("tljRule")));
            buf.add((byte) Integer.parseInt(hm.get("zxhSwitch")));
            buf.add((byte) 0x00);
            buf.add((byte) 0x00);
            buf.add((byte) 0x00);
            buf.add((byte) 0x00);
            buf.add((byte) 0x00);
            buf.add((byte) 0x00);
            buf.add((byte) 0x00);
            buf.add((byte) 0x00);
        }

        int crc = CommonFunctions.crc_16(Byte2byte(buf), buf.toArray().length);
        buf.add((byte) ((crc >> 8) & 0xFF));
        buf.add((byte) (crc & 0xFF));
        return Byte2byte(buf);
    }

    //查询/设置车型转换准则-回复
    public static Object parsePackage_25(byte[] bufTmp) {
        int[] buf = byte2int(bufTmp);
        int index = 5;
        if (buf[index] == 0x3C) {
            index++;
            HashMap<String, String> hm = new HashMap<String, String>();
            hm.put("mtcRule", buf[index++] + "");
            hm.put("slcRule", buf[index++] + "");
            hm.put("xrRule", buf[index++] + "");
            hm.put("jzxRule", buf[index++] + "");
            hm.put("tljRule", buf[index++] + "");
            hm.put("zxhSwitch", buf[index++] + "");
            return hm;
        } else {
            return true;
        }
    }

    //获取单帧波形
    public static byte[] getPackage_26(int laser) {
        List<Byte> buf = new ArrayList<Byte>();
        buf.add((byte) 0xFF);
        buf.add((byte) 0xFF);//帧头
        buf.add((byte) 0x26);//命令号
        buf.add((byte) 0x00);
        buf.add((byte) 0x08);//帧长
        buf.add((byte) laser);

        int crc = CommonFunctions.crc_16(Byte2byte(buf), buf.toArray().length);
        buf.add((byte) ((crc >> 8) & 0xFF));
        buf.add((byte) (crc & 0xFF));
        return Byte2byte(buf);
    }

    //获取单帧波形-回复
    public static int[] parsePackage_26(byte[] bufTmp) {
        int[] buf = byte2int(bufTmp);
        int[] res = new int[361];
        int index = 91;
        for (int i = 0; i < 722; i += 2) {
            res[i / 2] = (buf[index++] << 8) + buf[index++];
        }
        return res;
    }

    //查询01包上传状态
    public static byte[] getPackage_27() {
        List<Byte> buf = new ArrayList<Byte>();
        buf.add((byte) 0xFF);
        buf.add((byte) 0xFF);//帧头
        buf.add((byte) 0x27);//命令号
        buf.add((byte) 0x00);
        buf.add((byte) 0x08);//帧长
        buf.add((byte) 0xCC);
        int crc = CommonFunctions.crc_16(Byte2byte(buf), buf.toArray().length);
        buf.add((byte) ((crc >> 8) & 0xFF));
        buf.add((byte) (crc & 0xFF));
        return Byte2byte(buf);
    }

    //查询01包上传状态-回复
    public static HashMap<String, String> parsePackage_27(byte[] bufTmp) {
        int[] buf = byte2int(bufTmp);
        HashMap<String, String> res = new HashMap<String, String>();
        int index = 5;
        index++;
        res.put("lastPackage01Time", String.format("%04d", ((buf[index++] << 8) + buf[index++])) + "-" + String.format("%02d", buf[index++]) + "-" + String.format("%02d", buf[index++]));
        res.put("lastPackage01No", ((buf[index++] << 8) + buf[index++]) + "");
        res.put("currentPackageNo", ((buf[index++] << 8) + buf[index++]) + "");
        if (buf[index] == 0x2A) {
            res.put("package02Result", "发出");
        } else if (buf[index] == 0x2B) {
            res.put("package02Result", "收到正确返回");
        } else if (buf[index] == 0x2C) {
            res.put("package02Result", "收到反馈未建站");
        } else if (buf[index] == 0x20) {
            res.put("package02Result", "未发出");
        }
        index++;
        if (buf[index] == 0x1A) {
            res.put("package01Result", "发出");
        } else if (buf[index] == 0x1B) {
            res.put("package01Result", "收到反馈");
        } else if (buf[index] == 0x10) {
            res.put("package01Result", "未发出");
        }
        return res;
    }

    //查询/设置异常报警
    public static byte[] getPackage_28(int cmd, HashMap<String, String> hm) {
        List<Byte> buf = new ArrayList<Byte>();
        if (cmd == 0xCC) {
            buf.add((byte) 0xFF);
            buf.add((byte) 0xFF);//帧头
            buf.add((byte) 0x28);//命令号
            buf.add((byte) 0x00);
            buf.add((byte) 0x0C);//帧长
            buf.add((byte) cmd);
            buf.add((byte) 0x00);
            buf.add((byte) 0x00);
            buf.add((byte) 0x00);
            buf.add((byte) 0x00);
        } else {
            buf.add((byte) 0xFF);
            buf.add((byte) 0xFF);//帧头
            buf.add((byte) 0x28);//命令号
            buf.add((byte) 0x00);
            buf.add((byte) 0x0e);//帧长
            buf.add((byte) cmd);
            buf.add((byte) Integer.parseInt(hm.get("disconnNet")));
            buf.add((byte) Integer.parseInt(hm.get("exWave")));
            buf.add((byte) 0x00);
            buf.add((byte) 0x00);
            buf.add((byte) 0x00);
            buf.add((byte) 0x00);
        }
        int crc = CommonFunctions.crc_16(Byte2byte(buf), buf.toArray().length);
        buf.add((byte) ((crc >> 8) & 0xFF));
        buf.add((byte) (crc & 0xFF));
        return Byte2byte(buf);
    }

    //查询/设置异常报警-回复
    public static Object parsePackage_28(byte[] bufTmp) {
        int[] buf = byte2int(bufTmp);
        int index = 5;
        if (buf[index] == 0xC0) {
            index++;
            HashMap<String, String> hm = new HashMap<String, String>();
            hm.put("disconnNet", buf[index++] + "");
            hm.put("exWave", buf[index++] + "");
            return hm;
        } else if (buf[index] == 0xD0) {
            return true;
        }
        return false;
    }

    //查询组包方式协议
    public static byte[] getPackage_29() {
        List<Byte> buf = new ArrayList<Byte>();
        buf.add((byte) 0xFF);
        buf.add((byte) 0xFF);//帧头
        buf.add((byte) 0x29);//命令号
        buf.add((byte) 0x00);
        buf.add((byte) 0x0A);//帧长
        buf.add((byte) 0xAB);
        buf.add((byte) 0xBC);
        buf.add((byte) 0xCC);
        int crc = CommonFunctions.crc_16(Byte2byte(buf), buf.toArray().length);
        buf.add((byte) ((crc >> 8) & 0xFF));
        buf.add((byte) (crc & 0xFF));
        return Byte2byte(buf);
    }

    //查询组包方式协议-回复
    public static HashMap<String, String> parsePackage_29(byte[] bufTmp) {
        int[] buf = byte2int(bufTmp);
        HashMap<String, String> res = new HashMap<String, String>();
        int index = 5;
        res.put("makePackageType", buf[index] + "");
        return res;
    }

    //设置组包方式协议
    public static byte[] getPackage_30(HashMap<String, String> hm) {
        List<Byte> buf = new ArrayList<Byte>();
        buf.add((byte) 0xFF);
        buf.add((byte) 0xFF);//帧头
        buf.add((byte) 0x30);//命令号
        buf.add((byte) 0x00);
        buf.add((byte) 0x0B);//帧长
        buf.add((byte) Integer.parseInt(hm.get("makePackageType")));
        buf.add((byte) 0xAB);
        buf.add((byte) 0xBC);
        buf.add((byte) 0xCD);
        int crc = CommonFunctions.crc_16(Byte2byte(buf), buf.toArray().length);
        buf.add((byte) ((crc >> 8) & 0xFF));
        buf.add((byte) (crc & 0xFF));
        return Byte2byte(buf);
    }

    //设置组包方式协议-回复
    public static boolean parsePackage_30(byte[] buf) {
        return true;
    }

    //查询/设置串口波特率
    public static byte[] getPackage_31(int cmd, HashMap<String, String> hm) {
        List<Byte> buf = new ArrayList<Byte>();
        if (cmd == 0xCC) {
            buf.add((byte) 0xFF);
            buf.add((byte) 0xFF);//帧头
            buf.add((byte) 0x31);//命令号
            buf.add((byte) 0x00);
            buf.add((byte) 0x08);//帧长
            buf.add((byte) cmd);
        } else {
            buf.add((byte) 0xFF);
            buf.add((byte) 0xFF);//帧头
            buf.add((byte) 0x31);//命令号
            buf.add((byte) 0x00);
            buf.add((byte) 0x0A);//帧长
            buf.add((byte) cmd);
            buf.add((byte) Integer.parseInt(hm.get("com1Baud")));
            buf.add((byte) Integer.parseInt(hm.get("com2Baud")));
        }
        int crc = CommonFunctions.crc_16(Byte2byte(buf), buf.toArray().length);
        buf.add((byte) ((crc >> 8) & 0xFF));
        buf.add((byte) (crc & 0xFF));
        return Byte2byte(buf);
    }

    //查询/设置串口波特率-回复
    public static Object parsePackage_31(byte[] bufTmp) {
        int[] buf = byte2int(bufTmp);
        int index = 5;
        if (buf[index] == 0xC0) {
            index++;
            HashMap<String, String> hm = new HashMap<String, String>();
            hm.put("com1Baud", buf[index++] + "");
            hm.put("com2Baud", buf[index++] + "");
            return hm;
        } else if (buf[index] == 0xD0) {
            return true;
        }
        return false;
    }

    //查询/设置长度速度调整系数
    public static byte[] getPackage_32(int cmd, HashMap<String, String> hm) {
        List<Byte> buf = new ArrayList<Byte>();
        if (cmd == 0xCC) {
            buf.add((byte) 0xFF);
            buf.add((byte) 0xFF);//帧头
            buf.add((byte) 0x32);//命令号
            buf.add((byte) 0x00);
            buf.add((byte) 0x10);//帧长
            buf.add((byte) cmd);
            buf.add((byte) 0x00);
            buf.add((byte) 0x00);
            buf.add((byte) 0x00);
            buf.add((byte) 0x00);
            buf.add((byte) 0x00);
            buf.add((byte) 0x00);
            buf.add((byte) 0x00);
            buf.add((byte) 0x00);
        } else {
            buf.add((byte) 0xFF);
            buf.add((byte) 0xFF);//帧头
            buf.add((byte) 0x32);//命令号
            buf.add((byte) 0x00);
            buf.add((byte) 0x14);//帧长
            buf.add((byte) cmd);
            buf.add((byte) Integer.parseInt(hm.get("verticalLenRate")));
            buf.add((byte) Integer.parseInt(hm.get("tiltLenRate")));
            buf.add((byte) Integer.parseInt(hm.get("verticalSpeedRate")));
            buf.add((byte) Integer.parseInt(hm.get("tiltSpeedRate")));
            buf.add((byte) 0x00);
            buf.add((byte) 0x00);
            buf.add((byte) 0x00);
            buf.add((byte) 0x00);
            buf.add((byte) 0x00);
            buf.add((byte) 0x00);
            buf.add((byte) 0x00);
            buf.add((byte) 0x00);
        }
        int crc = CommonFunctions.crc_16(Byte2byte(buf), buf.toArray().length);
        buf.add((byte) ((crc >> 8) & 0xFF));
        buf.add((byte) (crc & 0xFF));
        return Byte2byte(buf);
    }

    //查询/设置长度速度调整系数-回复
    public static Object parsePackage_32(byte[] bufTmp) {
        int[] buf = byte2int(bufTmp);
        int index = 5;
        if (buf[index] == 0xC0) {
            index++;
            HashMap<String, String> hm = new HashMap<String, String>();
            hm.put("verticalLenRate", buf[index++] + "");
            hm.put("tiltLenRate", buf[index++] + "");
            hm.put("verticalSpeedRate", buf[index++] + "");
            hm.put("tiltSpeedRate", buf[index++] + "");
            return hm;
        } else if (buf[index] == 0xD0) {
            return true;
        }
        return false;
    }

    //查询/设置异常空白帧数
    public static byte[] getPackage_34(int cmd, HashMap<String, String> hm) {
        List<Byte> buf = new ArrayList<Byte>();
        if (cmd == 0xCC) {
            buf.add((byte) 0xFF);
            buf.add((byte) 0xFF);//帧头
            buf.add((byte) 0x34);//命令号
            buf.add((byte) 0x00);
            buf.add((byte) 0x0B);//帧长
            buf.add((byte) cmd);
            buf.add((byte) 0x00);
            buf.add((byte) 0x00);
            buf.add((byte) 0x00);
        } else if (cmd == 0xDD) {
            buf.add((byte) 0xFF);
            buf.add((byte) 0xFF);//帧头
            buf.add((byte) 0x34);//命令号
            buf.add((byte) 0x00);
            buf.add((byte) 0x0F);//帧长
            buf.add((byte) cmd);
            buf.add((byte) Integer.parseInt(hm.get("slowEmptyFrame")));
            buf.add((byte) ((Integer.parseInt(hm.get("peopleEmptyFrame")) >> 8) & 0xFF));
            buf.add((byte) (Integer.parseInt(hm.get("peopleEmptyFrame")) & 0xFF));
            buf.add((byte) Integer.parseInt(hm.get("normalEmptyFrame")));
            buf.add((byte) 0x00);
            buf.add((byte) 0x00);
            buf.add((byte) 0x00);
        }
        int crc = CommonFunctions.crc_16(Byte2byte(buf), buf.toArray().length);
        buf.add((byte) ((crc >> 8) & 0xFF));
        buf.add((byte) (crc & 0xFF));
        return Byte2byte(buf);
    }

    //查询/设置异常空白帧数-回复
    public static Object parsePackage_34(byte[] bufTmp) {
        int[] buf = byte2int(bufTmp);
        int index = 5;
        if (buf[index] == 0xC0) {
            index++;
            HashMap<String, String> hm = new HashMap<String, String>();
            hm.put("slowEmptyFrame", buf[index++] + "");
            hm.put("peopleEmptyFrame", ((buf[index++] << 8) + buf[index++]) + "");
            hm.put("normalEmptyFrame", buf[index++] + "");
            return hm;
        } else if (buf[index] == 0xD0) {
            return true;
        } else if (buf[index] == 0xD1) {
            return false;
        } else if (buf[index] == 0xD2) {
            return false;
        }
        return false;
    }

    //开启关闭双串口发数
    public static byte[] getPackage_35(int cmd, HashMap<String, String> hm) {
        List<Byte> buf = new ArrayList<Byte>();
        if (cmd == 0xBB) {
            buf.add((byte) 0xFF);
            buf.add((byte) 0xFF);//帧头
            buf.add((byte) 0x35);//命令号
            buf.add((byte) 0x00);
            buf.add((byte) 0x0C);//帧长
            buf.add((byte) cmd);
            buf.add((byte) 0x00);
            buf.add((byte) 0x00);
            buf.add((byte) 0x00);
            buf.add((byte) 0x00);
        } else if (cmd == 0xCC) {
            buf.add((byte) 0xFF);
            buf.add((byte) 0xFF);//帧头
            buf.add((byte) 0x35);//命令号
            buf.add((byte) 0x00);
            buf.add((byte) 0x0E);//帧长
            buf.add((byte) cmd);
            buf.add((byte) Integer.parseInt(hm.get("comSend")));
            buf.add((byte) Integer.parseInt(hm.get("comRep")));
            buf.add((byte) 0x00);
            buf.add((byte) 0x00);
            buf.add((byte) 0x00);
            buf.add((byte) 0x00);
        }
        int crc = CommonFunctions.crc_16(Byte2byte(buf), buf.toArray().length);
        buf.add((byte) ((crc >> 8) & 0xFF));
        buf.add((byte) (crc & 0xFF));
        return Byte2byte(buf);
    }

    //开启关闭双串口发数-回复
    public static Object parsePackage_35(byte[] bufTmp) {
        int[] buf = byte2int(bufTmp);
        int index = 5;
        if (buf[index] == 0xB0) {
            index++;
            HashMap<String, String> hm = new HashMap<String, String>();
            hm.put("comSend", buf[index++] + "");
            hm.put("comRep", buf[index++] + "");
            return hm;
        } else if (buf[index] == 0xC0) {
            return true;
        }
        return false;
    }

    //查询/设置网络参数
    public static byte[] getPackage_36(int cmd, HashMap<String, String> hm) {
        List<Byte> buf = new ArrayList<Byte>();
        if (cmd == 0xCC) {
            buf.add((byte) 0xFF);
            buf.add((byte) 0xFF);//帧头
            buf.add((byte) 0x36);//命令号
            buf.add((byte) 0x00);
            buf.add((byte) 0x18);//帧长
            buf.add((byte) cmd);
            for (int i = 0; i < 16; i++)//预留
            {
                buf.add((byte) 0x00);
            }
            int crc = CommonFunctions.crc_16(Byte2byte(buf), buf.toArray().length);
            buf.add((byte) ((crc >> 8) & 0xFF));
            buf.add((byte) (crc & 0xFF));
        } else if (cmd == 0xDD) {
            buf.add((byte) 0xFF);
            buf.add((byte) 0xFF);//帧头
            buf.add((byte) 0x36);//命令号
            buf.add((byte) 0x00);
            buf.add((byte) 0x44);//帧长
            buf.add((byte) cmd);
            buf.add((byte) Integer.parseInt(hm.get("new_verticalLaserIp").split("\\.")[0]));
            buf.add((byte) Integer.parseInt(hm.get("new_verticalLaserIp").split("\\.")[1]));
            buf.add((byte) Integer.parseInt(hm.get("new_verticalLaserIp").split("\\.")[2]));
            buf.add((byte) Integer.parseInt(hm.get("new_verticalLaserIp").split("\\.")[3]));
            buf.add((byte) ((Integer.parseInt(hm.get("new_verticalLaserPort")) >> 8) & 0xFF));
            buf.add((byte) (Integer.parseInt(hm.get("new_verticalLaserPort")) & 0xFF));
            buf.add((byte) Integer.parseInt(hm.get("new_tiltLaserIp").split("\\.")[0]));
            buf.add((byte) Integer.parseInt(hm.get("new_tiltLaserIp").split("\\.")[1]));
            buf.add((byte) Integer.parseInt(hm.get("new_tiltLaserIp").split("\\.")[2]));
            buf.add((byte) Integer.parseInt(hm.get("new_tiltLaserIp").split("\\.")[3]));
            buf.add((byte) ((Integer.parseInt(hm.get("new_tiltLaserPort")) >> 8) & 0xFF));
            buf.add((byte) (Integer.parseInt(hm.get("new_tiltLaserPort")) & 0xFF));
            buf.add((byte) Integer.parseInt(hm.get("new_controlIp").split("\\.")[0]));
            buf.add((byte) Integer.parseInt(hm.get("new_controlIp").split("\\.")[1]));
            buf.add((byte) Integer.parseInt(hm.get("new_controlIp").split("\\.")[2]));
            buf.add((byte) Integer.parseInt(hm.get("new_controlIp").split("\\.")[3]));
            buf.add((byte) Integer.parseInt(hm.get("new_controlMask").split("\\.")[0]));
            buf.add((byte) Integer.parseInt(hm.get("new_controlMask").split("\\.")[1]));
            buf.add((byte) Integer.parseInt(hm.get("new_controlMask").split("\\.")[2]));
            buf.add((byte) Integer.parseInt(hm.get("new_controlMask").split("\\.")[3]));
            buf.add((byte) ((Integer.parseInt(hm.get("new_controlPort")) >> 8) & 0xFF));
            buf.add((byte) (Integer.parseInt(hm.get("new_controlPort")) & 0xFF));
            buf.add((byte) Integer.parseInt(hm.get("new_controlNetGate").split("\\.")[0]));
            buf.add((byte) Integer.parseInt(hm.get("new_controlNetGate").split("\\.")[1]));
            buf.add((byte) Integer.parseInt(hm.get("new_controlNetGate").split("\\.")[2]));
            buf.add((byte) Integer.parseInt(hm.get("new_controlNetGate").split("\\.")[3]));
            buf.add((byte) Integer.parseInt(hm.get("new_controlMac").split("\\.")[0]));
            buf.add((byte) Integer.parseInt(hm.get("new_controlMac").split("\\.")[1]));
            buf.add((byte) Integer.parseInt(hm.get("new_controlMac").split("\\.")[2]));
            buf.add((byte) Integer.parseInt(hm.get("new_controlMac").split("\\.")[3]));
            buf.add((byte) Integer.parseInt(hm.get("new_controlMac").split("\\.")[4]));
            buf.add((byte) Integer.parseInt(hm.get("new_controlMac").split("\\.")[5]));
            buf.add((byte) ((Integer.parseInt(hm.get("new_verticalLaserDisconnCount")) >> 8) & 0xFF));
            buf.add((byte) (Integer.parseInt(hm.get("new_verticalLaserDisconnCount")) & 0xFF));
            buf.add((byte) ((Integer.parseInt(hm.get("new_tiltLaserDisconnCount")) >> 8) & 0xFF));
            buf.add((byte) (Integer.parseInt(hm.get("new_tiltLaserDisconnCount")) & 0xFF));
            buf.add((byte) ((Integer.parseInt(hm.get("new_verticalLaserValidCount")) >> 8) & 0xFF));
            buf.add((byte) (Integer.parseInt(hm.get("new_verticalLaserValidCount")) & 0xFF));
            buf.add((byte) ((Integer.parseInt(hm.get("new_tiltLaserValidCount")) >> 8) & 0xFF));
            buf.add((byte) (Integer.parseInt(hm.get("new_tiltLaserValidCount")) & 0xFF));
            buf.add((byte) ((Integer.parseInt(hm.get("new_exCode")) >> 24) & 0xFF));
            buf.add((byte) ((Integer.parseInt(hm.get("new_exCode")) >> 16) & 0xFF));
            buf.add((byte) ((Integer.parseInt(hm.get("new_exCode")) >> 8) & 0xFF));
            buf.add((byte) (Integer.parseInt(hm.get("new_exCode")) & 0xFF));
            for (int i = 0; i < 16; i++)//预留
            {
                buf.add((byte) 0x00);
            }
            int crc = CommonFunctions.crc_16(Byte2byte(buf), buf.toArray().length);
            buf.add((byte) ((crc >> 8) & 0xFF));
            buf.add((byte) (crc & 0xFF));
        }
        return Byte2byte(buf);
    }

    //查询/设置网络参数-回复
    public static Object parsePackage_36(byte[] bufTmp) {
        int[] buf = byte2int(bufTmp);
        int index = 5;
        if (buf[index++] == 0xD0) {
            return true;
        } else// if(buf[index++] == 0xD0)
        {
            HashMap<String, String> hm = new HashMap<String, String>();
            hm.put("new_verticalLaserIp", buf[index++] + "." + buf[index++] + "." + buf[index++] + "." + buf[index++]);
            hm.put("new_verticalLaserPort", ((buf[index++] << 8) + buf[index++]) + "");
            hm.put("new_tiltLaserIp", buf[index++] + "." + buf[index++] + "." + buf[index++] + "." + buf[index++]);
            hm.put("new_tiltLaserPort", ((buf[index++] << 8) + buf[index++]) + "");
            hm.put("new_controlIp", buf[index++] + "." + buf[index++] + "." + buf[index++] + "." + buf[index++]);
            hm.put("new_controlMask", buf[index++] + "." + buf[index++] + "." + buf[index++] + "." + buf[index++]);
            hm.put("new_controlPort", ((buf[index++] << 8) + buf[index++]) + "");
            hm.put("new_controlNetGate", buf[index++] + "." + buf[index++] + "." + buf[index++] + "." + buf[index++]);
            hm.put("new_controlMac", buf[index++] + "." + buf[index++] + "." + buf[index++] + "." + buf[index++] + "." + buf[index++] + "." + buf[index++]);
            hm.put("new_verticalLaserDisconnCount", ((buf[index++] << 8) + buf[index++]) + "");
            hm.put("new_tiltLaserDisconnCount", ((buf[index++] << 8) + buf[index++]) + "");
            hm.put("new_verticalLaserValidCount", ((buf[index++] << 8) + buf[index++]) + "");
            hm.put("new_tiltLaserValidCount", ((buf[index++] << 8) + buf[index++]) + "");
            hm.put("new_exCode", ((buf[index++] << 24) + (buf[index++] << 16) + (buf[index++] << 8) + buf[index++]) + "");
            return hm;
        }
    }

    //获取/设置DTU协议和中心数目
    public static byte[] getPackage_37(int cmd, HashMap<String, String> hm) {
        List<Byte> buf = new ArrayList<Byte>();

        if (cmd == 0xAA) {
            buf.add((byte) 0xFF);
            buf.add((byte) 0xFF);//帧头
            buf.add((byte) 0x37);//命令号
            buf.add((byte) 0x00);
            buf.add((byte) 0x10);//帧长
            buf.add((byte) cmd);
            buf.add((byte) 0x00);
            buf.add((byte) 0x00);
            buf.add((byte) 0x00);
            buf.add((byte) 0x00);
            buf.add((byte) 0x00);
            buf.add((byte) 0x00);
            buf.add((byte) 0x00);
            buf.add((byte) 0x00);
        } else if (cmd == 0xBB) {
            buf.add((byte) 0xFF);
            buf.add((byte) 0xFF);//帧头
            buf.add((byte) 0x37);//命令号
            buf.add((byte) 0x00);
            buf.add((byte) 0x13);//帧长
            buf.add((byte) cmd);
            buf.add((byte) Integer.parseInt(hm.get("dtuProc")));
            buf.add((byte) Integer.parseInt(hm.get("zdCenter")));
            buf.add((byte) Integer.parseInt(hm.get("bdCenter")));
            buf.add((byte) 0x00);
            buf.add((byte) 0x00);
            buf.add((byte) 0x00);
            buf.add((byte) 0x00);
            buf.add((byte) 0x00);
            buf.add((byte) 0x00);
            buf.add((byte) 0x00);
            buf.add((byte) 0x00);
        }

        int crc = CommonFunctions.crc_16(Byte2byte(buf), buf.toArray().length);
        buf.add((byte) ((crc >> 8) & 0xFF));
        buf.add((byte) (crc & 0xFF));
        return Byte2byte(buf);
    }

    //获取/设置DTU协议和中心数目-回复
    public static Object parsePackage_37(byte[] bufTmp) {
        int[] buf = byte2int(bufTmp);
        int index = 5;
        if (buf[index] == 0xA0) {
            index++;
            HashMap<String, String> hm = new HashMap<String, String>();
            hm.put("dtuProc", buf[index++] + "");
            hm.put("zdCenter", buf[index++] + "");
            hm.put("bdCenter", buf[index++] + "");
            return hm;
        } else if (buf[index] == 0xB0) {
            return true;
        }
        return false;
    }

    //************************************************************************************************

    public static byte[] transmission(byte[] data, String laserIp, int port) {
        List<Byte> buf = new ArrayList<Byte>();
        buf.add((byte) 0xFF);
        buf.add((byte) 0xFF);//帧头
        for (int i = 0; i < data.length; i++) {
            buf.add(data[i]);
        }
        buf.add((byte) Integer.parseInt(laserIp.split("\\.")[0]));
        buf.add((byte) Integer.parseInt(laserIp.split("\\.")[1]));
        buf.add((byte) Integer.parseInt(laserIp.split("\\.")[2]));
        buf.add((byte) Integer.parseInt(laserIp.split("\\.")[3]));
        buf.add((byte) ((port >> 8) & 0xFF));
        buf.add((byte) (port & 0xFF));

        buf.add((byte) 0xEE);
        return Byte2byte(buf);
    }

    public static byte[] getPackage_(byte[] data) {
        int len = data.length;
        List<Byte> buf = new ArrayList<Byte>();
        buf.add((byte) 0x02);
        buf.add((byte) 0x02);
        buf.add((byte) 0x02);
        buf.add((byte) 0x02);//帧头
        buf.add((byte) ((len >> 24) & 0xFF));
        buf.add((byte) ((len >> 16) & 0xFF));
        buf.add((byte) ((len >> 8) & 0xFF));
        buf.add((byte) (len & 0xFF));
        for (int i = 0; i < data.length; i++) {
            buf.add(data[i]);
        }
        int xor = CommonFunctions.xor_16(data);
        buf.add((byte) (xor & 0xFF));
        return Byte2byte(buf);
    }

    //激光运行状态查询指令
    public static byte[] getPackage_5F(String laserIp, int port) {
        List<Byte> buf = new ArrayList<Byte>();
        buf.add((byte) 0x73);
        buf.add((byte) 0x5F);//命令号
        buf.add((byte) 0x4E);
        buf.add((byte) 0x20);
        buf.add((byte) 0x4C);
        buf.add((byte) 0x4D);
        buf.add((byte) 0x44);
        buf.add((byte) 0x73);
        buf.add((byte) 0x63);
        buf.add((byte) 0x61);
        buf.add((byte) 0x6E);
        buf.add((byte) 0x64);
        buf.add((byte) 0x61);
        buf.add((byte) 0x74);
        buf.add((byte) 0x61);
        buf.add((byte) 0x20);
        byte[] bufTmp = getPackage_(Byte2byte(buf));
        bufTmp = transmission(bufTmp, laserIp, port);
        return bufTmp;
    }

    //激光运行状态查询指令-回复
    public static HashMap<String, String> parsePackage_AF(byte[] bufTmp) {
        int[] buf = byte2int(bufTmp);
        int index = 26;
        HashMap<String, String> res = new HashMap<String, String>();

        res.put("airPress", ((buf[index + 3] << 24) + (buf[index + 2] << 16) + (buf[index + 1] << 8) + buf[index]) + "");
        index += 4;
        res.put("apdHighPress", ((buf[index + 1] << 8) + buf[index]) + "");
        index += 2;
        int apdTemp = 0;
        if ((buf[index] & 0x80) == 0x80) {
            apdTemp = (-1 - 255) + buf[index];
        } else {
            apdTemp = buf[index];
        }
        res.put("apdTemp", apdTemp + "");
        index += 1;
        int emTemp = 0;
        if ((buf[index] & 0x80) == 0x80) {
            emTemp = (-1 - 255) + buf[index];
        } else {
            emTemp = buf[index];
        }
        res.put("emTemp", emTemp + "");
        index += 1;
        res.put("zeroPlusWidth", ((buf[index + 3] << 24) + (buf[index + 2] << 16) + (buf[index + 1] << 8) + buf[index]) + "");
        index += 4;
        String heatState = "";
        if (buf[index] == 0xAA) {
            heatState = "启动";
        } else {
            heatState = "未启动";
        }
        res.put("heatState", heatState);
        index += 1;
        String emState = "";
        if (buf[index] == 0xAA) {
            emState = "启动";
        } else {
            emState = "未启动";
        }
        res.put("emState", emState);
        index += 1;
        index += 1;
        String laserVersionId = new String(buf, index, 22);
        res.put("laserVersionId", laserVersionId);
        index += 22;
        String countTimeState = "";
        if (buf[index] == 1) {
            countTimeState = "正常";
        } else {
            countTimeState = "故障";
        }
        res.put("countTimeState", countTimeState);
        index += 1;
        String laserLaunchState = "";
        if (buf[index] == 0) {
            laserLaunchState = "激光未使能";
        } else if (buf[index] == 1) {
            laserLaunchState = "激光使能";
        } else if (buf[index] == 2) {
            laserLaunchState = "激光处于正常关闭时间段";
        }
        res.put("laserLaunchState", laserLaunchState);
        index += 1;
        String humidity = buf[index + 1] + "." + buf[index];
        res.put("humidity", humidity);
        return res;
    }

    //激光器查询系统参数指令
    public static byte[] getPackage_54(String laserIp, int port) {
        List<Byte> buf = new ArrayList<Byte>();
        buf.add((byte) 0x73);
        buf.add((byte) 0x54);//命令号
        buf.add((byte) 0x4E);
        buf.add((byte) 0x20);
        buf.add((byte) 0x4C);
        buf.add((byte) 0x4D);
        buf.add((byte) 0x44);
        buf.add((byte) 0x73);
        buf.add((byte) 0x63);
        buf.add((byte) 0x61);
        buf.add((byte) 0x6E);
        buf.add((byte) 0x64);
        buf.add((byte) 0x61);
        buf.add((byte) 0x74);
        buf.add((byte) 0x61);
        buf.add((byte) 0x20);
        byte[] bufTmp = getPackage_(Byte2byte(buf));
        bufTmp = transmission(bufTmp, laserIp, port);
        return bufTmp;
    }

    //激光器查询系统参数指令-回复
    public static HashMap<String, String> parsePackage_A4(byte[] bufTmp) {
        int[] buf = byte2int(bufTmp);
        int index = 50;
        HashMap<String, String> res = new HashMap<String, String>();
        res.put("circle", ((buf[index + 1] << 8) + buf[index]) + "");
        index += 2;
        res.put("scanningPrecision", ((buf[index + 1] << 8) + buf[index]) + "");
        index += 2;
        res.put("zeroStartPos", ((buf[index + 1] << 8) + buf[index]) + "");
        index += 2;
        res.put("zeroEndPos", ((buf[index + 1] << 8) + buf[index]) + "");
        index += 2;
        res.put("zeroLength", ((buf[index + 1] << 8) + buf[index]) + "");
        index += 2;
        res.put("zeroAvgNum", ((buf[index + 1] << 8) + buf[index]) + "");
        index += 2;
        res.put("zeroMin", ((buf[index + 1] << 8) + buf[index]) + "");
        index += 2;
        res.put("scanningStartPos", ((buf[index + 1] << 8) + buf[index]) + "");
        index += 2;
        res.put("scanningEndPos", ((buf[index + 1] << 8) + buf[index]) + "");
        index += 2;
        res.put("scanningNum", ((buf[index + 1] << 8) + buf[index]) + "");
        index += 2;
        res.put("apdBreakdownVoltage", ((buf[index + 1] << 8) + buf[index]) + "");
        index += 2;
        res.put("apdBreakdownTemp", ((buf[index + 1] << 8) + buf[index]) + "");
        index += 2;
        res.put("apdDecay", ((buf[index + 1] << 8) + buf[index]) + "");
        index += 2;
        index += 14;
        res.put("mac", (buf[index++] + (buf[index++] << 8)) + "." + (buf[index++] + (buf[index++] << 8)) + "." + (buf[index++] + (buf[index++] << 8)) + "." + (buf[index++] + (buf[index++] << 8)) + "." + (buf[index++] + (buf[index++] << 8)) + "." + (buf[index++] + (buf[index++] << 8)));
        res.put("mask", (buf[index++] + (buf[index++] << 8)) + "." + (buf[index++] + (buf[index++] << 8)) + "." + (buf[index++] + (buf[index++] << 8)) + "." + (buf[index++] + (buf[index++] << 8)));
        res.put("laserIp", (buf[index++] + (buf[index++] << 8)) + "." + (buf[index++] + (buf[index++] << 8)) + "." + (buf[index++] + (buf[index++] << 8)) + "." + (buf[index++] + (buf[index++] << 8)));
        res.put("gateway", (buf[index++] + (buf[index++] << 8)) + "." + (buf[index++] + (buf[index++] << 8)) + "." + (buf[index++] + (buf[index++] << 8)) + "." + (buf[index++] + (buf[index++] << 8)));
        res.put("laserPort", ((buf[index + 1] << 8) + buf[index]) + "");
        index += 2;
        res.put("serverIp", (buf[index++] + (buf[index++] << 8)) + "." + (buf[index++] + (buf[index++] << 8)) + "." + (buf[index++] + (buf[index++] << 8)) + "." + (buf[index++] + (buf[index++] << 8)));
        res.put("serverPort", ((buf[index + 1] << 8) + buf[index]) + "");
        return res;
    }

    //设置系统参数指令
    public static byte[] getPackage_53(HashMap<String, String> hm, String laserIp, int port) {
        List<Byte> buf = new ArrayList<Byte>();
        buf.add((byte) 0x73);
        buf.add((byte) 0x53);//命令号
        buf.add((byte) 0x4E);
        buf.add((byte) 0x00);//协议上是20
        buf.add((byte) 0x4C);
        buf.add((byte) 0x4D);
        buf.add((byte) 0x44);
        buf.add((byte) 0x73);
        buf.add((byte) 0x63);
        buf.add((byte) 0x61);
        buf.add((byte) 0x6E);
        buf.add((byte) 0x64);
        buf.add((byte) 0x61);
        buf.add((byte) 0x74);
        buf.add((byte) 0x61);
        buf.add((byte) 0x20);

        buf.add((byte) 0x55);
        buf.add((byte) 0xAA);
        buf.add((byte) 0x38);
        buf.add((byte) 0x00);
        for (int i = 0; i < 20; i++) {
            buf.add((byte) 0x00);
        }
        buf.add((byte) (Integer.parseInt(hm.get("circle")) & 0xFF));
        buf.add((byte) ((Integer.parseInt(hm.get("circle")) >> 8) & 0xFF));
        buf.add((byte) (Integer.parseInt(hm.get("scanningPrecision")) & 0xFF));
        buf.add((byte) ((Integer.parseInt(hm.get("scanningPrecision")) >> 8) & 0xFF));
        buf.add((byte) (Integer.parseInt(hm.get("zeroStartPos")) & 0xFF));
        buf.add((byte) ((Integer.parseInt(hm.get("zeroStartPos")) >> 8) & 0xFF));
        buf.add((byte) (Integer.parseInt(hm.get("zeroEndPos")) & 0xFF));
        buf.add((byte) ((Integer.parseInt(hm.get("zeroEndPos")) >> 8) & 0xFF));
        buf.add((byte) (Integer.parseInt(hm.get("zeroLength")) & 0xFF));
        buf.add((byte) ((Integer.parseInt(hm.get("zeroLength")) >> 8) & 0xFF));
        buf.add((byte) (Integer.parseInt(hm.get("zeroAvgNum")) & 0xFF));
        buf.add((byte) ((Integer.parseInt(hm.get("zeroAvgNum")) >> 8) & 0xFF));
        buf.add((byte) (Integer.parseInt(hm.get("zeroMin")) & 0xFF));
        buf.add((byte) ((Integer.parseInt(hm.get("zeroMin")) >> 8) & 0xFF));
        buf.add((byte) (Integer.parseInt(hm.get("scanningStartPos")) & 0xFF));
        buf.add((byte) ((Integer.parseInt(hm.get("scanningStartPos")) >> 8) & 0xFF));
        buf.add((byte) (Integer.parseInt(hm.get("scanningEndPos")) & 0xFF));
        buf.add((byte) ((Integer.parseInt(hm.get("scanningEndPos")) >> 8) & 0xFF));
        buf.add((byte) (Integer.parseInt(hm.get("scanningNum")) & 0xFF));
        buf.add((byte) ((Integer.parseInt(hm.get("scanningNum")) >> 8) & 0xFF));
        buf.add((byte) (Integer.parseInt(hm.get("apdBreakdownVoltage")) & 0xFF));
        buf.add((byte) ((Integer.parseInt(hm.get("apdBreakdownVoltage")) >> 8) & 0xFF));
        buf.add((byte) (Integer.parseInt(hm.get("apdBreakdownTemp")) & 0xFF));
        buf.add((byte) ((Integer.parseInt(hm.get("apdBreakdownTemp")) >> 8) & 0xFF));
        buf.add((byte) (Integer.parseInt(hm.get("apdDecay")) & 0xFF));
        buf.add((byte) ((Integer.parseInt(hm.get("apdDecay")) >> 8) & 0xFF));

        for (int i = 0; i < 14; i++) {
            buf.add((byte) 0x00);
        }
        buf.add((byte) Integer.parseInt(hm.get("mac").split("\\.")[0]));
        buf.add((byte) 0x00);
        buf.add((byte) Integer.parseInt(hm.get("mac").split("\\.")[1]));
        buf.add((byte) 0x00);
        buf.add((byte) Integer.parseInt(hm.get("mac").split("\\.")[2]));
        buf.add((byte) 0x00);
        buf.add((byte) Integer.parseInt(hm.get("mac").split("\\.")[3]));
        buf.add((byte) 0x00);
        buf.add((byte) Integer.parseInt(hm.get("mac").split("\\.")[4]));
        buf.add((byte) 0x00);
        buf.add((byte) Integer.parseInt(hm.get("mac").split("\\.")[5]));
        buf.add((byte) 0x00);
        buf.add((byte) Integer.parseInt(hm.get("mask").split("\\.")[0]));
        buf.add((byte) 0x00);
        buf.add((byte) Integer.parseInt(hm.get("mask").split("\\.")[1]));
        buf.add((byte) 0x00);
        buf.add((byte) Integer.parseInt(hm.get("mask").split("\\.")[2]));
        buf.add((byte) 0x00);
        buf.add((byte) Integer.parseInt(hm.get("mask").split("\\.")[3]));
        buf.add((byte) 0x00);
        buf.add((byte) Integer.parseInt(hm.get("laserIp").split("\\.")[0]));
        buf.add((byte) 0x00);
        buf.add((byte) Integer.parseInt(hm.get("laserIp").split("\\.")[1]));
        buf.add((byte) 0x00);
        buf.add((byte) Integer.parseInt(hm.get("laserIp").split("\\.")[2]));
        buf.add((byte) 0x00);
        buf.add((byte) Integer.parseInt(hm.get("laserIp").split("\\.")[3]));
        buf.add((byte) 0x00);
        buf.add((byte) Integer.parseInt(hm.get("gateway").split("\\.")[0]));
        buf.add((byte) 0x00);
        buf.add((byte) Integer.parseInt(hm.get("gateway").split("\\.")[1]));
        buf.add((byte) 0x00);
        buf.add((byte) Integer.parseInt(hm.get("gateway").split("\\.")[2]));
        buf.add((byte) 0x00);
        buf.add((byte) Integer.parseInt(hm.get("gateway").split("\\.")[3]));
        buf.add((byte) 0x00);
        buf.add((byte) (Integer.parseInt(hm.get("laserPort")) & 0xFF));
        buf.add((byte) ((Integer.parseInt(hm.get("laserPort")) >> 8) & 0xFF));
        buf.add((byte) Integer.parseInt(hm.get("serverIp").split("\\.")[0]));
        buf.add((byte) 0x00);
        buf.add((byte) Integer.parseInt(hm.get("serverIp").split("\\.")[1]));
        buf.add((byte) 0x00);
        buf.add((byte) Integer.parseInt(hm.get("serverIp").split("\\.")[2]));
        buf.add((byte) 0x00);
        buf.add((byte) Integer.parseInt(hm.get("serverIp").split("\\.")[3]));
        buf.add((byte) 0x00);
        buf.add((byte) (Integer.parseInt(hm.get("serverPort")) & 0xFF));
        buf.add((byte) ((Integer.parseInt(hm.get("serverPort")) >> 8) & 0xFF));

        byte[] bufTmp = getPackage_(Byte2byte(buf));
        bufTmp = transmission(bufTmp, laserIp, port);
        return bufTmp;
    }

    //复位默认系统参数指令
    public static byte[] getPackage_5B(String laserIp, int port) {
        List<Byte> buf = new ArrayList<Byte>();
        buf.add((byte) 0x73);
        buf.add((byte) 0x5B);//命令号
        buf.add((byte) 0x4E);
        buf.add((byte) 0x20);
        buf.add((byte) 0x4C);
        buf.add((byte) 0x4D);
        buf.add((byte) 0x44);
        buf.add((byte) 0x73);
        buf.add((byte) 0x63);
        buf.add((byte) 0x61);
        buf.add((byte) 0x6E);
        buf.add((byte) 0x64);
        buf.add((byte) 0x61);
        buf.add((byte) 0x74);
        buf.add((byte) 0x61);
        buf.add((byte) 0x20);
        byte[] bufTmp = getPackage_(Byte2byte(buf));
        bufTmp = transmission(bufTmp, laserIp, port);
        return bufTmp;
    }

    //应答复位默认系统参数指令
    public static boolean parsePackage_AB(byte[] buf) {
        return true;
    }

    //重启激光器
    public static byte[] getPackage_88(String laserIp, int port) {
        List<Byte> buf = new ArrayList<Byte>();
        buf.add((byte) 0x73);
        buf.add((byte) 0x88);//命令号
        buf.add((byte) 0x4E);
        buf.add((byte) 0x20);
        buf.add((byte) 0x4C);
        buf.add((byte) 0x4D);
        buf.add((byte) 0x44);
        buf.add((byte) 0x73);
        buf.add((byte) 0x63);
        buf.add((byte) 0x61);
        buf.add((byte) 0x6E);
        buf.add((byte) 0x64);
        buf.add((byte) 0x61);
        buf.add((byte) 0x74);
        buf.add((byte) 0x61);
        buf.add((byte) 0x20);
        buf.add((byte) 0x00);
        byte[] bufTmp = getPackage_(Byte2byte(buf));
        bufTmp = transmission(bufTmp, laserIp, port);
        return bufTmp;
    }

    //应答复位默认系统参数指令
    public static boolean parsePackage_89(byte[] buf) {
        return true;
    }

    //心跳指令
    public static byte[] getPackage_61(int cmd, String laserIp, int port) {
        List<Byte> buf = new ArrayList<Byte>();
        buf.add((byte) 0x73);
        buf.add((byte) 0x61);//命令号
        buf.add((byte) 0x4E);
        buf.add((byte) cmd);//子指令
        buf.add((byte) 0x4C);
        buf.add((byte) 0x4D);
        buf.add((byte) 0x44);
        buf.add((byte) (byte) 0x73);
        buf.add((byte) 0x63);
        buf.add((byte) 0x61);
        buf.add((byte) 0x6E);
        buf.add((byte) 0x64);
        buf.add((byte) 0x61);
        buf.add((byte) 0x74);
        buf.add((byte) 0x61);
        buf.add((byte) 0x20);
        byte[] bufTmp = getPackage_(Byte2byte(buf));
        bufTmp = transmission(bufTmp, laserIp, port);
        return bufTmp;
    }

    //应答心跳指令
    public static Object parsePackage_B1(byte[] bufTmp) {
        int[] buf = byte2int(bufTmp);
        int index = 13;
        if (buf[index] == 0x5F) {
            HashMap<String, String> res = new HashMap<String, String>();
            if(buf[26]==0)
            {
                res.put("heart","82");
            }
            else
            {
                res.put("heart", "81");
            }
            return res;
        } else {
            return true;
        }
    }

    //查询复位次数指令
    public static byte[] getPackage_56(String laserIp, int port) {
        List<Byte> buf = new ArrayList<Byte>();
        buf.add((byte) 0x73);
        buf.add((byte) 0x56);//命令号
        buf.add((byte) 0x4E);
        buf.add((byte) 0x20);//子指令
        buf.add((byte) 0x4C);
        buf.add((byte) 0x4D);
        buf.add((byte) 0x44);
        buf.add((byte) 0x73);
        buf.add((byte) 0x63);
        buf.add((byte) 0x61);
        buf.add((byte) 0x6E);
        buf.add((byte) 0x64);
        buf.add((byte) 0x61);
        buf.add((byte) 0x74);
        buf.add((byte) 0x61);
        buf.add((byte) 0x20);
        byte[] bufTmp = getPackage_(Byte2byte(buf));
        bufTmp = transmission(bufTmp, laserIp, port);
        return bufTmp;
    }

    //应答查询复位次数指令
    public static HashMap<String, String> parsePackage_A6(byte[] bufTmp) {
        int[] buf = byte2int(bufTmp);
        int index = 30;
        HashMap<String, String> res = new HashMap<String, String>();
        res.put("softResetCount", ((buf[index + 1] << 8) + buf[index]) + "");
        index += 2;
        res.put("independentResetCount", ((buf[index + 1] << 8) + buf[index]) + "");
        index += 2;
        res.put("windowResetCount", ((buf[index + 1] << 8) + buf[index]) + "");
        index += 2;
        res.put("powerResetCount", ((buf[index + 1] << 8) + buf[index]) + "");
        index += 2;
        res.put("nrstResetCount", ((buf[index + 1] << 8) + buf[index]) + "");
        return res;
    }

    //复位默认系统参数指令
    public static byte[] getPackage_57(String laserIp, int port) {
        List<Byte> buf = new ArrayList<Byte>();
        buf.add((byte) 0x73);
        buf.add((byte) 0x57);//命令号
        buf.add((byte) 0x4E);
        buf.add((byte) 0x20);//子指令
        buf.add((byte) 0x4C);
        buf.add((byte) 0x4D);
        buf.add((byte) 0x44);
        buf.add((byte) 0x73);
        buf.add((byte) 0x63);
        buf.add((byte) 0x61);
        buf.add((byte) 0x6E);
        buf.add((byte) 0x64);
        buf.add((byte) 0x61);
        buf.add((byte) 0x74);
        buf.add((byte) 0x61);
        buf.add((byte) 0x20);
        byte[] bufTmp = getPackage_(Byte2byte(buf));
        bufTmp = transmission(bufTmp, laserIp, port);
        return bufTmp;

    }

    //应答复位默认系统参数指令
    public static boolean parsePackage_A7(byte[] buf) {
        return true;
    }

    //宏电DTU相关指令组包
    public static byte[] switchATMode_DtuHD(){
        return "+++".getBytes();
    }

    public static byte[] atTest_DtuHD(){
        return "AT\r\n".getBytes();
    }

    public static byte[] login_DtuHD(){
        return "AT+LOGIN=admin\r\n".getBytes();
    }

    public static byte[] logout_DtuHD(){
        return "AT+LOGOUT\r\n".getBytes();
    }

    public static byte[] getAllParam_DtuHD(){
        return "AT+GETPARAM?\r\n".getBytes();
    }

    //设置DTU参数-非通道参数
    public static byte[] setDtuParam(HashMap<String, String> hm) {
        String res = "";
        for(String key:hm.keySet()){
            res += "AT+SETPARAM=" + key + "," + hm.get(key) + "\r\n";
        }
        res += "AT+SAVEPARAM\r\n";

        return res.getBytes();
    }

    /**
     * 通道参数数据
     */
    public static class ChannelData{
        String paramName;
        String value;

        public ChannelData(String name, String v){
            paramName = name;
            value = v;
        }

        public void setParamName(String name){
            paramName = name;
        }

        public String getParamName(){
            return paramName;
        }

        public void setValue(String v){
            value = v;
        }

        public String getValue(){
            return value;
        }
    }

    //设置DTU参数-通道参数
    public static byte[] setDtuChannelParam(List<ChannelData>[] data) {
        String res = "";
        int j=0,size = 0;

        if(data.length > 4){
            size = 4;
        }else{
            size = data.length;
        }

        for(int i=0;i<size;i++){
            j = i+1;
            res += "AT+SETCHL=" + j + "\r\n";
            for(ChannelData channel:data[i]){
                res += "AT+SETPARAM=" + channel.getParamName() + "," + channel.getValue() + "\r\n";
            }
        }

        res += "AT+SAVEPARAM\r\n";

        return res.getBytes();
    }

    //重启DTU
    public static byte[] resetHdDtu() {
        return "AT+RESET\r\n".getBytes();
    }
}
