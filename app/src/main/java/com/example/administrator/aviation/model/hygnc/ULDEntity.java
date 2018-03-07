package com.example.administrator.aviation.model.hygnc;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/3/2.
 */

public class ULDEntity implements Serializable {
    private String ULD;
    private String ULDWeight;
    private String MaxWeight;
    private String MaxVolume;
    private String ULDType;
    private String ULDFlag;
    private String Carrier;
    private String AirPort;
    private String Status;
    private String Remark;

    public ULDEntity() {
    }

    public String getULD() {
        return ULD;
    }

    public void setULD(String ULD) {
        this.ULD = ULD;
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

    public String getULDType() {
        return ULDType;
    }

    public void setULDType(String ULDType) {
        this.ULDType = ULDType;
    }

    public String getULDFlag() {
        return ULDFlag;
    }

    public void setULDFlag(String ULDFlag) {
        this.ULDFlag = ULDFlag;
    }

    public String getCarrier() {
        return Carrier;
    }

    public void setCarrier(String carrier) {
        Carrier = carrier;
    }

    public String getAirPort() {
        return AirPort;
    }

    public void setAirPort(String airPort) {
        AirPort = airPort;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String remark) {
        Remark = remark;
    }
}
