package com.example.administrator.aviation.model.intawbprepare;

import java.io.Serializable;
import java.util.List;

/**
 * 国际出港入库管理所有信息
 */

public class MawbInfo implements Serializable{
    private String MawbID; // 编号，由系统生成，用户页面不显示，但更新功能需上传。
    private String Mawb; // 主运单号
    private String PC; // 主单件数
    private String Weight; // 主单重量
    private String Volume; // 主单体积[自动计算，但可修改：Weight/220]
    private String SpCode; // 特码
    private String Goods; // 品名（英）
    private String GoodsCN; // 品名(中)
    private String BusinessType; // 业务类型，说明：ANR，普通货物运输；EMS，国际快件；KJK，D类快件；YJE，国际邮包
    private String Package; // 包装类型
    private String Origin; // 货源地，大写，长度三位
    private String Dep; // 启运港，大写，长度三位
    private String Dest1; // 目的港1，大写，长度三位
    private String Dest2; // 目的港2，大写，长度三位
    private String By1; // 承运人,目的港1，大写，长度两位
    private String TranFlag; // 报关类型，说明：0，本关；1，转关；2，大通关
    private String Remark; // 备注
    private Mawbm mawbm;
//    private Hawb hawb;

    private List<Hawb> hawb;

    public List<Hawb> getHawb() {
        return hawb;
    }

    public void setHawb(List<Hawb> hawb) {
        this.hawb = hawb;
    }
    //    public Hawb getHawb() {
//        return hawb;
//    }
//
//    public void setHawb(Hawb hawb) {
//        this.hawb = hawb;
//    }

    public String getMawbID() {
        return MawbID;
    }

    public void setMawbID(String mawbID) {
        MawbID = mawbID;
    }

    public String getMawb() {
        return Mawb;
    }

    public void setMawb(String mawb) {
        Mawb = mawb;
    }

    public String getPC() {
        return PC;
    }

    public void setPC(String PC) {
        this.PC = PC;
    }

    public String getWeight() {
        return Weight;
    }

    public void setWeight(String weight) {
        Weight = weight;
    }

    public String getVolume() {
        return Volume;
    }

    public void setVolume(String volume) {
        Volume = volume;
    }

    public String getSpCode() {
        return SpCode;
    }

    public void setSpCode(String spCode) {
        SpCode = spCode;
    }

    public String getGoods() {
        return Goods;
    }

    public void setGoods(String goods) {
        Goods = goods;
    }

    public String getGoodsCN() {
        return GoodsCN;
    }

    public void setGoodsCN(String goodsCN) {
        GoodsCN = goodsCN;
    }

    public String getBusinessType() {
        return BusinessType;
    }

    public void setBusinessType(String businessType) {
        BusinessType = businessType;
    }

    public String getPackage() {
        return Package;
    }

    public void setPackage(String aPackage) {
        Package = aPackage;
    }

    public String getOrigin() {
        return Origin;
    }

    public void setOrigin(String origin) {
        Origin = origin;
    }

    public String getDep() {
        return Dep;
    }

    public void setDep(String dep) {
        Dep = dep;
    }

    public String getDest1() {
        return Dest1;
    }

    public void setDest1(String dest1) {
        Dest1 = dest1;
    }

    public String getDest2() {
        return Dest2;
    }

    public void setDest2(String dest2) {
        Dest2 = dest2;
    }

    public String getBy1() {
        return By1;
    }

    public void setBy1(String by1) {
        By1 = by1;
    }

    public String getTranFlag() {
        return TranFlag;
    }

    public void setTranFlag(String tranFlag) {
        TranFlag = tranFlag;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String remark) {
        Remark = remark;
    }

    public Mawbm getMawbm() {
        return mawbm;
    }

    public void setMawbm(Mawbm mawbm) {
        this.mawbm = mawbm;
    }

    @Override
    public String toString() {
        return "MawbInfo{" +
                "MawbID='" + MawbID + '\'' +
                ", Mawb='" + Mawb + '\'' +
                ", PC='" + PC + '\'' +
                ", Weight='" + Weight + '\'' +
                ", Volume='" + Volume + '\'' +
                ", SpCode='" + SpCode + '\'' +
                ", Goods='" + Goods + '\'' +
                ", GoodsCN='" + GoodsCN + '\'' +
                ", BusinessType='" + BusinessType + '\'' +
                ", Package='" + Package + '\'' +
                ", Origin='" + Origin + '\'' +
                ", Dep='" + Dep + '\'' +
                ", Dest1='" + Dest1 + '\'' +
                ", Dest2='" + Dest2 + '\'' +
                ", By1='" + By1 + '\'' +
                ", TranFlag='" + TranFlag + '\'' +
                ", Remark='" + Remark + '\'' +
                ", mawbm=" + mawbm +
                ", hawb=" + hawb +
                '}';
    }
}
