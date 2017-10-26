package com.example.administrator.aviation.model.intanddomflight;

import java.io.Serializable;

/**
 * 航班动态数据
 *
 * 如果需要传递信息类，需要实现Serializable接口，并且传递的时候用putSerializable方法
 */

public class FlightMessage implements Serializable{
    private String fDate;
    private String fno;
    private String fDep;
    private String jtz;
    private String fDest;
    private String serviceType;
    private String countryType;
    private String estimatedTakeOff;
    private String actualTakeOff;
    private String estimatedArrival;
    private String actualArrival;
    private String registeration;
    private String aircraftCode;
    private String standID;
    private String flightStatus;
    private String flightTerminalID;
    private String delayFreeText;

    public String getfDate() {
        return fDate;
    }

    public void setfDate(String fDate) {
        this.fDate = fDate;
    }

    public String getFno() {
        return fno;
    }

    public void setFno(String fno) {
        this.fno = fno;
    }

    public String getfDep() {
        return fDep;
    }

    public void setfDep(String fDep) {
        this.fDep = fDep;
    }

    public String getJtz() {
        return jtz;
    }

    public void setJtz(String jtz) {
        this.jtz = jtz;
    }

    public String getfDest() {
        return fDest;
    }

    public void setfDest(String fDest) {
        this.fDest = fDest;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getCountryType() {
        return countryType;
    }

    public void setCountryType(String countryType) {
        this.countryType = countryType;
    }

    public String getEstimatedTakeOff() {
        return estimatedTakeOff;
    }

    public void setEstimatedTakeOff(String estimatedTakeOff) {
        this.estimatedTakeOff = estimatedTakeOff;
    }

    public String getActualTakeOff() {
        return actualTakeOff;
    }

    public void setActualTakeOff(String actualTakeOff) {
        this.actualTakeOff = actualTakeOff;
    }

    public String getEstimatedArrival() {
        return estimatedArrival;
    }

    public void setEstimatedArrival(String estimatedArrival) {
        this.estimatedArrival = estimatedArrival;
    }

    public String getActualArrival() {
        return actualArrival;
    }

    public void setActualArrival(String actualArrival) {
        this.actualArrival = actualArrival;
    }

    public String getRegisteration() {
        return registeration;
    }

    public void setRegisteration(String registeration) {
        this.registeration = registeration;
    }

    public String getAircraftCode() {
        return aircraftCode;
    }

    public void setAircraftCode(String aircraftCode) {
        this.aircraftCode = aircraftCode;
    }

    public String getStandID() {
        return standID;
    }

    public void setStandID(String standID) {
        this.standID = standID;
    }

    public String getFlightStatus() {
        return flightStatus;
    }

    public void setFlightStatus(String flightStatus) {
        this.flightStatus = flightStatus;
    }

    public String getFlightTerminalID() {
        return flightTerminalID;
    }

    public void setFlightTerminalID(String flightTerminalID) {
        this.flightTerminalID = flightTerminalID;
    }

    public String getDelayFreeText() {
        return delayFreeText;
    }

    public void setDelayFreeText(String delayFreeText) {
        this.delayFreeText = delayFreeText;
    }
}
