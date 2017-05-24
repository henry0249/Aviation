package com.example.administrator.aviation.model.intimpcargoinfo;

import java.io.Serializable;

/**
 * 进港货站信息存储
 */

public class CargoInfoMessage implements Serializable{
    private String FDate;
    private String Fno;
    private String awbID;
    private String Mawb;
    private String Hno;
    private String awbPC;
    private String PC;
    private String Weight;
    private String Volume;
    private String Goods;
    private String Origin;
    private String Dep;
    private String Dest;
    private String BusinessType;
    private String awbTypeName;
    private String BusinessName;
    private String isEDIAWB;
    private String mftStatus;
    private String tallyStatus;

    public String getFDate() {
        return FDate;
    }

    public void setFDate(String FDate) {
        this.FDate = FDate;
    }

    public String getFno() {
        return Fno;
    }

    public void setFno(String fno) {
        Fno = fno;
    }

    public String getAwbID() {
        return awbID;
    }

    public void setAwbID(String awbID) {
        this.awbID = awbID;
    }

    public String getMawb() {
        return Mawb;
    }

    public void setMawb(String mawb) {
        Mawb = mawb;
    }

    public String getHno() {
        return Hno;
    }

    public void setHno(String hno) {
        Hno = hno;
    }

    public String getAwbPC() {
        return awbPC;
    }

    public void setAwbPC(String awbPC) {
        this.awbPC = awbPC;
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

    public String getGoods() {
        return Goods;
    }

    public void setGoods(String goods) {
        Goods = goods;
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

    public String getDest() {
        return Dest;
    }

    public void setDest(String dest) {
        Dest = dest;
    }

    public String getBusinessType() {
        return BusinessType;
    }

    public void setBusinessType(String businessType) {
        BusinessType = businessType;
    }

    public String getAwbTypeName() {
        return awbTypeName;
    }

    public void setAwbTypeName(String awbTypeName) {
        this.awbTypeName = awbTypeName;
    }

    public String getBusinessName() {
        return BusinessName;
    }

    public void setBusinessName(String businessName) {
        BusinessName = businessName;
    }

    public String getIsEDIAWB() {
        return isEDIAWB;
    }

    public void setIsEDIAWB(String isEDIAWB) {
        this.isEDIAWB = isEDIAWB;
    }

    public String getMftStatus() {
        return mftStatus;
    }

    public void setMftStatus(String mftStatus) {
        this.mftStatus = mftStatus;
    }

    public String getTallyStatus() {
        return tallyStatus;
    }

    public void setTallyStatus(String tallyStatus) {
        this.tallyStatus = tallyStatus;
    }
}
