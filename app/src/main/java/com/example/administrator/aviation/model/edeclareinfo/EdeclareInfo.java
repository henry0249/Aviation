package com.example.administrator.aviation.model.edeclareinfo;

import java.io.Serializable;

/**
 * 出港联检状态
 */

public class EdeclareInfo implements Serializable{
    private String Mawb;
    private String Carrier;
    private String BusinessName;
    private String CIQStatus;
    private String CMDStatus;
    private String AgentCode;
    private String AgentName;
    private String PaperTime; // 交单日期
    private String FDate;
    private String Fno;
    private String TotalPC;
    private String PC;
    private String Weight;
    private String Volume;
    private String Dest;
    private String GoodsCN;

    public String getMawb() {
        return Mawb;
    }

    public void setMawb(String mawb) {
        Mawb = mawb;
    }

    public String getCarrier() {
        return Carrier;
    }

    public void setCarrier(String carrier) {
        Carrier = carrier;
    }

    public String getBusinessName() {
        return BusinessName;
    }

    public void setBusinessName(String businessName) {
        BusinessName = businessName;
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

    public String getPaperTime() {
        return PaperTime;
    }

    public void setPaperTime(String paperTime) {
        PaperTime = paperTime;
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

    public String getTotalPC() {
        return TotalPC;
    }

    public void setTotalPC(String totalPC) {
        TotalPC = totalPC;
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

    public String getDest() {
        return Dest;
    }

    public void setDest(String dest) {
        Dest = dest;
    }

    public String getGoodsCN() {
        return GoodsCN;
    }

    public void setGoodsCN(String goodsCN) {
        GoodsCN = goodsCN;
    }
}
