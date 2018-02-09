package com.example.administrator.aviation.model.hygnc;

import java.io.Serializable;

/**
 * 平板车上货物信息
 * Created by Administrator on 2017/12/7.
 */

public class ULDLoadingCargo implements Serializable {
    private String WHID;
    private String Mawb;
    private String AgentCode;
    private String PC;
    private String Weight;
    private String Volume;
    private String SpCode;
    private String Goods;
    private String Dest1;
    private String Dest;
    private String By1;
    private String FDate;
    private String Fno;
    private String Location;
    private String LocFlag;
    private String LocID;
    private String PlanFDate;
    private String PlanFno;

    public ULDLoadingCargo() {
        WHID ="";
        Mawb = "";
        AgentCode = "";
        PC = "";
        Weight = "";
        Volume = "";
        SpCode = "";
        Goods = "";
        Dest1 = "";
        Dest = "";
        By1 = "";
        FDate = "";
        Fno = "";
        Location = "";
        LocFlag = "";
        LocID = "";
        PlanFDate = "";
        PlanFno = "";
    }

    public String getWHID() {
        return WHID;
    }

    public void setWHID(String WHID) {
        this.WHID = WHID;
    }

    public String getMawb() {
        return Mawb;
    }

    public void setMawb(String mawb) {
        Mawb = mawb;
    }

    public String getAgentCode() {
        return AgentCode;
    }

    public void setAgentCode(String agentCode) {
        AgentCode = agentCode;
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

    public String getDest1() {
        return Dest1;
    }

    public void setDest1(String dest1) {
        Dest1 = dest1;
    }

    public String getDest() {
        return Dest;
    }

    public void setDest(String dest) {
        Dest = dest;
    }

    public String getBy1() {
        return By1;
    }

    public void setBy1(String by1) {
        By1 = by1;
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

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public String getLocFlag() {
        return LocFlag;
    }

    public void setLocFlag(String locFlag) {
        LocFlag = locFlag;
    }

    public String getLocID() {
        return LocID;
    }

    public void setLocID(String locID) {
        LocID = locID;
    }

    public String getPlanFDate() {
        return PlanFDate;
    }

    public void setPlanFDate(String planFDate) {
        PlanFDate = planFDate;
    }

    public String getPlanFno() {
        return PlanFno;
    }

    public void setPlanFno(String planFno) {
        PlanFno = planFno;
    }

}
