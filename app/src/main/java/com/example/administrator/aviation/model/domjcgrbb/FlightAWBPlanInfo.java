package com.example.administrator.aviation.model.domjcgrbb;

import java.io.Serializable;

/**
 * 订舱确认数据
 */

public class FlightAWBPlanInfo implements Serializable{
    private String Mawb;
    private String awbType;
    private String cStatus;
    private String AgentCode;
    private String AgentName;
    private String FlightChecked;
    private String Dest;
    private String PC;
    private String Weight;
    private String Volume;
    private String Goods;
    private String FDate;
    private String Fno;
    private String CheckID;
    private String CheckTime;

    public String getMawb() {
        return Mawb;
    }

    public void setMawb(String mawb) {
        Mawb = mawb;
    }

    public String getAwbType() {
        return awbType;
    }

    public void setAwbType(String awbType) {
        this.awbType = awbType;
    }

    public String getcStatus() {
        return cStatus;
    }

    public void setcStatus(String cStatus) {
        this.cStatus = cStatus;
    }

    public String getAgentCode() {
        return AgentCode;
    }

    public void setAgentCode(String agentCode) {
        AgentCode = agentCode;
    }

    public String getAgentName() {
        return AgentName;
    }

    public void setAgentName(String agentName) {
        AgentName = agentName;
    }

    public String getFlightChecked() {
        return FlightChecked;
    }

    public void setFlightChecked(String flightChecked) {
        FlightChecked = flightChecked;
    }

    public String getDest() {
        return Dest;
    }

    public void setDest(String dest) {
        Dest = dest;
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
}
