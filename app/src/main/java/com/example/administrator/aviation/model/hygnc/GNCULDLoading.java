package com.example.administrator.aviation.model.hygnc;

import java.io.Serializable;

/**
 * 装载总信息
 * Created by Administrator on 2017/12/1.
 */

public class GNCULDLoading implements Serializable {
    private String ID;
    private String CarID;
    private String FID;
    private String ULD;
    private String ULDFlag;
    private String ULDWeight;
    private String MaxWeight;
    private String MaxVolume;
    private String BoardType;
    private String NetWeight;
    private String CargoWeight;
    private String Volume;
    private String PC;
    private String CargoType;
    private String MailWeight;
    private String Priority;
    private String cFlag;
    private String Carrier;
    private String FDate;
    private String Fno;
    private String Dest;
    private String Location;
    private String Remark;
    private String EmptyULD;
    private String FClose;
    private String OPDate;
    private String OPID;
    private String CarWeight;

    public GNCULDLoading() {
        ID = "";
        CarID = "";
        FID = "";
        ULD = "";
        ULDFlag = "";
        ULDWeight = "";
        CarWeight = "";
        MaxWeight = "";
        MaxVolume = "";
        BoardType = "";
        NetWeight = "";
        CargoWeight = "";
        Volume = "";
        PC = "";
        CargoType = "";
        MailWeight = "";
        Priority = "";
        cFlag = "";
        Carrier = "";
        FDate = "";
        Fno = "";
        Dest = "";
        Location = "";
        Remark = "";
        EmptyULD = "";
        FClose = "";
        OPDate = "";
        OPID = "";
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getCarID() {
        return CarID;
    }

    public void setCarID(String carID) {
        CarID = carID;
    }

    public String getFID() {
        return FID;
    }

    public void setFID(String FID) {
        this.FID = FID;
    }

    public String getULD() {
        return ULD;
    }

    public void setULD(String ULD) {
        this.ULD = ULD;
    }

    public String getULDFlag() {
        return ULDFlag;
    }

    public void setULDFlag(String ULDFlag) {
        this.ULDFlag = ULDFlag;
    }

    public String getULDWeight() {
        return ULDWeight;
    }

    public void setULDWeight(String ULDWeight) {
        this.ULDWeight = ULDWeight;
    }

    public String getMaxWeight() {
        return MaxWeight;
    }

    public void setMaxWeight(String maxWeight) {
        MaxWeight = maxWeight;
    }

    public String getMaxVolume() {
        return MaxVolume;
    }

    public void setMaxVolume(String maxVolume) {
        MaxVolume = maxVolume;
    }

    public String getBoardType() {
        return BoardType;
    }

    public void setBoardType(String boardType) {
        BoardType = boardType;
    }

    public String getNetWeight() {
        return NetWeight;
    }

    public void setNetWeight(String netWeight) {
        NetWeight = netWeight;
    }

    public String getCargoWeight() {
        return CargoWeight;
    }

    public void setCargoWeight(String cargoWeight) {
        CargoWeight = cargoWeight;
    }

    public String getVolume() {
        return Volume;
    }

    public void setVolume(String volume) {
        Volume = volume;
    }

    public String getPC() {
        return PC;
    }

    public void setPC(String PC) {
        this.PC = PC;
    }

    public String getCargoType() {
        return CargoType;
    }

    public void setCargoType(String cargoType) {
        CargoType = cargoType;
    }

    public String getMailWeight() {
        return MailWeight;
    }

    public void setMailWeight(String mailWeight) {
        MailWeight = mailWeight;
    }

    public String getPriority() {
        return Priority;
    }

    public void setPriority(String priority) {
        Priority = priority;
    }

    public String getcFlag() {
        return cFlag;
    }

    public void setcFlag(String cFlag) {
        this.cFlag = cFlag;
    }

    public String getCarrier() {
        return Carrier;
    }

    public void setCarrier(String carrier) {
        Carrier = carrier;
    }

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

    public String getDest() {
        return Dest;
    }

    public void setDest(String dest) {
        Dest = dest;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String remark) {
        Remark = remark;
    }

    public String getEmptyULD() {
        return EmptyULD;
    }

    public void setEmptyULD(String emptyULD) {
        EmptyULD = emptyULD;
    }

    public String getFClose() {
        return FClose;
    }

    public void setFClose(String FClose) {
        this.FClose = FClose;
    }

    public String getOPDate() {
        return OPDate;
    }

    public void setOPDate(String OPDate) {
        this.OPDate = OPDate;
    }

    public String getOPID() {
        return OPID;
    }

    public void setOPID(String OPID) {
        this.OPID = OPID;
    }

    public String getCarWeight() {
        return CarWeight;
    }

    public void setCarWeight(String carWeight) {
        CarWeight = carWeight;
    }
}
