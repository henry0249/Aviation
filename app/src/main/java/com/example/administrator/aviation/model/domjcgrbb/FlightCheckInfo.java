package com.example.administrator.aviation.model.domjcgrbb;

import java.io.Serializable;

/**
 * 获取舱单详情
 */

public class FlightCheckInfo implements Serializable{
    private String Carrier; // 承运人
    private String FDate; // 航班日期
    private String Fno; // 航班号
    private String Registeration; // 机号
    private String AircraftCode; // 机型
    private String FDest; // 目的港
    private String JTZ; // 经停港
    private String EstimatedTakeOff; // 预计起飞时间
    private String MaxWeight; // 最大载量KG
    private String MaxVolume; // 最大体积MC
    private String UseableWeight; // 剩余载量KG
    private String UseableVolume; // 剩余体积MC
    private String WaitCheckInWeight; // 待批载量KG
    private String WaitCheckInVolume; // 待批体积MC
    private String DelTransWeight; // (拉货/中转)待批载量KG
    private String DelTransVolume; // (拉货/中转)待批体积MC
    private String InWeight; // 确认载量KG
    private String InVolume; // 确认体积MC

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

    public String getRegisteration() {
        return Registeration;
    }

    public void setRegisteration(String registeration) {
        Registeration = registeration;
    }

    public String getAircraftCode() {
        return AircraftCode;
    }

    public void setAircraftCode(String aircraftCode) {
        AircraftCode = aircraftCode;
    }

    public String getFDest() {
        return FDest;
    }

    public void setFDest(String FDest) {
        this.FDest = FDest;
    }

    public String getJTZ() {
        return JTZ;
    }

    public void setJTZ(String JTZ) {
        this.JTZ = JTZ;
    }

    public String getEstimatedTakeOff() {
        return EstimatedTakeOff;
    }

    public void setEstimatedTakeOff(String estimatedTakeOff) {
        EstimatedTakeOff = estimatedTakeOff;
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

    public String getUseableWeight() {
        return UseableWeight;
    }

    public void setUseableWeight(String useableWeight) {
        UseableWeight = useableWeight;
    }

    public String getUseableVolume() {
        return UseableVolume;
    }

    public void setUseableVolume(String useableVolume) {
        UseableVolume = useableVolume;
    }

    public String getWaitCheckInWeight() {
        return WaitCheckInWeight;
    }

    public void setWaitCheckInWeight(String waitCheckInWeight) {
        WaitCheckInWeight = waitCheckInWeight;
    }

    public String getWaitCheckInVolume() {
        return WaitCheckInVolume;
    }

    public void setWaitCheckInVolume(String waitCheckInVolume) {
        WaitCheckInVolume = waitCheckInVolume;
    }

    public String getDelTransWeight() {
        return DelTransWeight;
    }

    public void setDelTransWeight(String delTransWeight) {
        DelTransWeight = delTransWeight;
    }

    public String getDelTransVolume() {
        return DelTransVolume;
    }

    public void setDelTransVolume(String delTransVolume) {
        DelTransVolume = delTransVolume;
    }

    public String getInWeight() {
        return InWeight;
    }

    public void setInWeight(String inWeight) {
        InWeight = inWeight;
    }

    public String getInVolume() {
        return InVolume;
    }

    public void setInVolume(String inVolume) {
        InVolume = inVolume;
    }
}
