package com.wanji.jdbluetooth.beans;

/**
 * Created by Administrator on 2018/5/14.
 */
/// <summary>
/// 宏电DTU参数类
/// </summary>
public class HDDTU
{
    private DtuParam mParam = new DtuParam();

    public DtuParam getHDDTUParam(){
        return mParam;
    }
    /// <summary>
    /// 获取或者设置
    /// </summary>
    /*public enum GetOrSetType
    {
        GET_PARAM = 0,//获取
        SET_PARAM_SUCCES = 1,//设置成功
        SET_PARAM_FAIL = 2,//设置失败
        REBOOT = 3,//重启
        INIT = -1//初始状态
    }*/

    //public GetOrSetType DataBackState;//返回数据状态

    public class DtuParam{
        public String SerialNo;//序列号
        public String HardwareVer;//硬件版本号
        public String SoftwareVer;//软件版本号
        public String PhoneID;//身份识别码，手机卡号

        public String AccessPoint;//访问接入点
        public String AccessUser;//访问用户名
        public String AccessPsw;//访问密码
        public String AccessServerCode;//服务代码

        public String ComBaudRate;//波特率
        public String DebugInfo;//调试信息

        public String PackageMaxSize;//最大数据包大小
        public String RDPSwitch;//rdp协议
        public String GPRSProtocal;//GPRS下行协议

        public String ManageDscYM;//平台域名
        public String ManageDscIP;//平台IP
        public String ManageDscPort;//平台端口

        public String Dsc1YM;//DSC1
        public String Dsc1IP;
        public String Dsc1Port;
        public String Dsc1RegPack;
        public String DSC1Heart;
        public String DSC1CSMS;

        public String Dsc2YM;//DSC2
        public String Dsc2IP;
        public String Dsc2Port;
        public String Dsc2RegPack;
        public String DSC2Heart;
        public String DSC2CSMS;

        public String Dsc3YM;//DSC3
        public String Dsc3IP;
        public String Dsc3Port;
        public String Dsc3RegPack;
        public String DSC3Heart;
        public String DSC3CSMS;

        public String Dsc4YM;//DSC4
        public String Dsc4IP;
        public String Dsc4Port;
        public String Dsc4RegPack;
        public String DSC4Heart;
        public String DSC4CSMS;

        public String CSQ;//最低信号强度
    }
}

