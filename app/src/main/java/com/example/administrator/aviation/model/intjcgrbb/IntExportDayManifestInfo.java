package com.example.administrator.aviation.model.intjcgrbb;

/**
 * 出港舱单信息
 */

public class IntExportDayManifestInfo {
    private String Mawb;
    private String AgentCode;
    private String AgentName;
    private String Dep;
    private String origin;
    private String Dest;
    private String TotalPC;
    private String PC;
    private String Weight;
    private String Volume;
    private String Goods;

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
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

    public String getAgentName() {
        return AgentName;
    }

    public void setAgentName(String agentName) {
        AgentName = agentName;
    }

    public String getDep() {
        return Dep;
    }

    public void setDep(String dep) {
        Dep = dep;
    }

    public String getDest() {
        return Dest;
    }

    public void setDest(String dest) {
        Dest = dest;
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

    public String getGoods() {
        return Goods;
    }

    public void setGoods(String goods) {
        Goods = goods;
    }
}
