package com.example.administrator.aviation.model.prepareawb;

import java.io.Serializable;

/**
 * awb解析的第二段数据
 */

public class Mawbm implements Serializable{
    private String FlightChecked;//定舱确认，系统提供
    private String FDate;//计划航班日期，必填
    private String Fno;//计划航班号，必填
    private String Shipper;//托运人，字符
    private String ShipperTEL;//托运人电话
    private String Consignee;//收货人
    private String CNEETEL ;//收货人电话
    private String TransportNO;//运输证
    private String AllowTransNO;//'准运证
    private String CIQNumber ;//商检号
    private String ProTime;//预约时间

    private String Priority;//优先级
    private String CheckID;//确认人
    private String CheckTime;//确认时间

    public Mawbm() {
        FlightChecked = "";//定舱确认，系统提供
        FDate = "";//计划航班日期，必填
        Fno = "";//计划航班号，必填
        Shipper = "";//托运人，字符
        ShipperTEL = "";//托运人电话
        Consignee = "";//收货人
        CNEETEL = "";//收货人电话
        TransportNO = "";//运输证
        AllowTransNO = "";//'准运证
        CIQNumber = "";//商检号
        ProTime = "";//预约时间
        Priority = "";
        CheckID = "";
        CheckTime = "";
    }

    public String getFlightChecked() {
        return FlightChecked;
    }
    public void setFlightChecked(String flightChecked) {
        FlightChecked = flightChecked;
    }
    public String getFDate() {
        return FDate;
    }
    public void setFDate(String fDate) {
        FDate = fDate;
    }
    public String getFno() {
        return Fno;
    }
    public void setFno(String fno) {
        Fno = fno;
    }
    public String getShipper() {
        return Shipper;
    }
    public void setShipper(String shipper) {
        Shipper = shipper;
    }
    public String getShipperTEL() {
        return ShipperTEL;
    }
    public void setShipperTEL(String shipperTEL) {
        ShipperTEL = shipperTEL;
    }
    public String getConsignee() {
        return Consignee;
    }
    public void setConsignee(String consignee) {
        Consignee = consignee;
    }
    public String getCNEETEL() {
        return CNEETEL;
    }
    public void setCNEETEL(String cNEETEL) {
        CNEETEL = cNEETEL;
    }
    public String getTransportNO() {
        return TransportNO;
    }
    public void setTransportNO(String transportNO) {
        TransportNO = transportNO;
    }
    public String getAllowTransNO() {
        return AllowTransNO;
    }
    public void setAllowTransNO(String allowTransNO) {
        AllowTransNO = allowTransNO;
    }
    public String getCIQNumber() {
        return CIQNumber;
    }
    public void setCIQNumber(String cIQNumber) {
        CIQNumber = cIQNumber;
    }
    public String getProTime() {
        return ProTime;
    }

    public void setProTime(String proTime) {
        ProTime = proTime;
    }

    public String getPriority() {
        return Priority;
    }

    public void setPriority(String priority) {
        Priority = priority;
    }

    public String getCheckID() {
        return CheckID;
    }

    public void setCheckID(String checkID) {
        CheckID = checkID;
    }

    public String getCheckTime() {
        return CheckTime;
    }

    public void setCheckTime(String checkTime) {
        CheckTime = checkTime;
    }


    @Override
    public String toString() {
        return "Mawbm [FlightChecked=" + FlightChecked + ", FDate=" + FDate
                + ", Fno=" + Fno + ", Shipper=" + Shipper + ", ShipperTEL="
                + ShipperTEL + ", Consignee=" + Consignee + ", CNEETEL="
                + CNEETEL + ", TransportNO=" + TransportNO + ", AllowTransNO="
                + AllowTransNO + ", CIQNumber=" + CIQNumber + "]";
    }

}
