package com.example.administrator.aviation.model.gnj;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * Created by 46296 on 2020/8/27.
 */

@Data
public class gnjPickUpModel implements Serializable {
    //流水号
    private String ID;
    //提取记录编号
    private String PKID;
    //运单号
    private String Mawb;
    //收费模式
    private String CHGMode;
    //代理人代码
    private String AgentCode;
    //运单总件数
    private String AWBPC;
    //件数
    private String PC;
    //特码
    private String SpCode;
    //品名
    private String Goods;
    //始发港
    private String Origin;
    //航班日期
    private String FDate;
    //航班号
    private String Fno;
    //缴费时间
    private String ChargeTime;
    //提取标识
    private String PickFlag;
    //提货时间
    private String DLVTime;
    //收货人名字
    private String CNEName;
    //收货人ID类型
    private String CNEIDType;
    //身份证号
    private String CNEID;
    //电话
    private String CNEPhone;
    //提货人名字
    private String DLVName;
    //提货人ID类型
    private String DLVIDType;
    //提货人ID
    private String DLVID;
    //提货人电话
    private String DLVPhone;
    //发货人ID
    private String REFID;
    //签名图
    private String Sign;
    //收货人身份证
    private String CNEIDCard;
    //提货人身份证
    private String DLVIDCard;

}
