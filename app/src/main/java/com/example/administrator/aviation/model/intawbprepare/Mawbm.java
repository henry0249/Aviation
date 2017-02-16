package com.example.administrator.aviation.model.intawbprepare;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/1/9 0009.
 */

public class Mawbm implements Serializable{
    private String FDate; // 计划航班日期，格式：yyyy-MM-dd
    private String Fno; // 计划航班号
    private String CustomsCode; // 支线离境关区
    private String TransPortMode; // 离境方式，说明：3，陆运，4，空运
    private String FreightPayment; // 支付方式
    private String CNEECity; // 收货城市（只显示，不用录入）
    private String CNEECountry; // 收货国家(只显示，不用录入)
    private String MftStatus; // 预配状态(只显示，不用录入)
    private String Shipper; // 托运人
    private String Consignee; // 收货人
    private String Gprice; // 货值
    private String CIQStatus; // 商检状态(只显示，不用录入)
    private String CIQNumber; // 商检号(只显示，不用录入)

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

    public String getCustomsCode() {
        return CustomsCode;
    }

    public void setCustomsCode(String customsCode) {
        CustomsCode = customsCode;
    }

    public String getTransPortMode() {
        return TransPortMode;
    }

    public void setTransPortMode(String transPortMode) {
        TransPortMode = transPortMode;
    }

    public String getFreightPayment() {
        return FreightPayment;
    }

    public void setFreightPayment(String freightPayment) {
        FreightPayment = freightPayment;
    }

    public String getCNEECity() {
        return CNEECity;
    }

    public void setCNEECity(String CNEECity) {
        this.CNEECity = CNEECity;
    }

    public String getCNEECountry() {
        return CNEECountry;
    }

    public void setCNEECountry(String CNEECountry) {
        this.CNEECountry = CNEECountry;
    }

    public String getMftStatus() {
        return MftStatus;
    }

    public void setMftStatus(String mftStatus) {
        MftStatus = mftStatus;
    }

    public String getShipper() {
        return Shipper;
    }

    public void setShipper(String shipper) {
        Shipper = shipper;
    }

    public String getConsignee() {
        return Consignee;
    }

    public void setConsignee(String consignee) {
        Consignee = consignee;
    }

    public String getGprice() {
        return Gprice;
    }

    public void setGprice(String gprice) {
        Gprice = gprice;
    }

    public String getCIQStatus() {
        return CIQStatus;
    }

    public void setCIQStatus(String CIQStatus) {
        this.CIQStatus = CIQStatus;
    }

    public String getCIQNumber() {
        return CIQNumber;
    }

    public void setCIQNumber(String CIQNumber) {
        this.CIQNumber = CIQNumber;
    }

    @Override
    public String toString() {
        return "Mawbm{" +
                "FDate='" + FDate + '\'' +
                ", Fno='" + Fno + '\'' +
                ", CustomsCode='" + CustomsCode + '\'' +
                ", TransPortMode='" + TransPortMode + '\'' +
                ", FreightPayment='" + FreightPayment + '\'' +
                ", CNEECity='" + CNEECity + '\'' +
                ", CNEECountry='" + CNEECountry + '\'' +
                ", MftStatus='" + MftStatus + '\'' +
                ", Shipper='" + Shipper + '\'' +
                ", Consignee='" + Consignee + '\'' +
                ", Gprice='" + Gprice + '\'' +
                ", CIQStatus='" + CIQStatus + '\'' +
                ", CIQNumber='" + CIQNumber + '\'' +
                '}';
    }
}
