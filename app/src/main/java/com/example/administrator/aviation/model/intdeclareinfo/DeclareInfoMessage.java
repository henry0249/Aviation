package com.example.administrator.aviation.model.intdeclareinfo;

import java.io.Serializable;

/**
 * 联检状态信息存储
 */

public class DeclareInfoMessage implements Serializable{
    private String Mawb;
    private String CIQNumber;
    private String CIQStatus;
    private String CMDStatus;
    private String MftStatus;
    private String ArrivalStatus;
    private String LoadStatus;
    private String TallyStatus;
    private String PC;
    private String Weight;
    private String Dest;
    private String GPrice;
    private String Shipper;
    private String OPDate;

    public String getMawb() {
        return Mawb;
    }

    public void setMawb(String mawb) {
        Mawb = mawb;
    }

    public String getCIQNumber() {
        return CIQNumber;
    }

    public void setCIQNumber(String CIQNumber) {
        this.CIQNumber = CIQNumber;
    }

    public String getCIQStatus() {
        return CIQStatus;
    }

    public void setCIQStatus(String CIQStatus) {
        this.CIQStatus = CIQStatus;
    }

    public String getCMDStatus() {
        return CMDStatus;
    }

    public void setCMDStatus(String CMDStatus) {
        this.CMDStatus = CMDStatus;
    }

    public String getMftStatus() {
        return MftStatus;
    }

    public void setMftStatus(String mftStatus) {
        MftStatus = mftStatus;
    }

    public String getArrivalStatus() {
        return ArrivalStatus;
    }

    public void setArrivalStatus(String arrivalStatus) {
        ArrivalStatus = arrivalStatus;
    }

    public String getLoadStatus() {
        return LoadStatus;
    }

    public void setLoadStatus(String loadStatus) {
        LoadStatus = loadStatus;
    }

    public String getTallyStatus() {
        return TallyStatus;
    }

    public void setTallyStatus(String tallyStatus) {
        TallyStatus = tallyStatus;
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

    public String getDest() {
        return Dest;
    }

    public void setDest(String dest) {
        Dest = dest;
    }

    public String getGPrice() {
        return GPrice;
    }

    public void setGPrice(String GPrice) {
        this.GPrice = GPrice;
    }

    public String getShipper() {
        return Shipper;
    }

    public void setShipper(String shipper) {
        Shipper = shipper;
    }

    public String getOPDate() {
        return OPDate;
    }

    public void setOPDate(String OPDate) {
        this.OPDate = OPDate;
    }
}
