package com.example.administrator.aviation.model.prepareawb;

import java.io.Serializable;

/**
 * 获取PREPARE_AWB订单的详细信息
 */

public class MawbInfo implements Serializable{
    private String MawbID;//主运单ID号，系统提供
    private String Mawb;//主运单号，11位数字，必填
    private String PC;//件数，必填
    private String Weight;//重量，必填
    private String Volume;//体积，必填
    private String SpCode ;//特码，3位字母，数字
    private String Goods;//品名，字符，必填
    private String BusinessType;//业务类型，3位字符，可暂不填写
    private String Package;//包装类型，字符
    private String By;//承运人，2位字符，必填
    private String Dep;//启运港，3位字符，必填
    private String Dest1;//第一目的港，3位字符，必填
    private String Dest2 ;//第二目的港，3位字符，必填
    private String Remark;//备注，字符
    private String CargoType;//货物类型
    private  Mawbm mawbm;

    public MawbInfo() {
        MawbID = "";//主运单ID号，系统提供
        Mawb = "";//主运单号，11位数字，必填
        PC = "";//件数，必填
        Weight = "";//重量，必填
        Volume = "";//体积，必填
        SpCode = "";//特码，3位字母，数字
        Goods = "";//品名，字符，必填
        BusinessType = "";//业务类型，3位字符，可暂不填写
        Package = "";//包装类型，字符
        By = "";//承运人，2位字符，必填
        Dep = "";//启运港，3位字符，必填
        Dest1 = "";//第一目的港，3位字符，必填
        Dest2 = "";//第二目的港，3位字符，必填
        Remark = "";//备注，字符
        CargoType = "";//货物类型
        mawbm = new Mawbm();
    }

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
    public void setPC(String pC) {
        PC = pC;
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
    public String getBusinessType() {
        return BusinessType;
    }
    public void setBusinessType(String businessType) {
        BusinessType = businessType;
    }
    public String getPackage() {
        return Package;
    }
    public void setPackage(String package1) {
        Package = package1;
    }
    public String getBy() {
        return By;
    }
    public void setBy(String by) {
        By = by;
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


    public String getCargoType() {
        return CargoType;
    }

    public void setCargoType(String cargoType) {
        CargoType = cargoType;
    }

    @Override
    public String toString() {
        return "MawbInfo [MawbID=" + MawbID + ", Mawb=" + Mawb + ", PC=" + PC
                + ", Weight=" + Weight + ", Volume=" + Volume + ", SpCode="
                + SpCode + ", Goods=" + Goods + ", BusinessType="
                + BusinessType + ", Package=" + Package + ", By=" + By
                + ", Dep=" + Dep + ", Dest1=" + Dest1 + ", Dest2=" + Dest2
                + ", Remark=" + Remark + ", mawbm=" + mawbm.toString() + "]";
    }
}
