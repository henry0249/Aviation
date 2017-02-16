package com.example.administrator.aviation.model.house;

import java.io.Serializable;

/**
 * house类
 */

public class WhsInfo implements Serializable{
    private String Mawb; // 主运单号(1),条件1
    private String BusinessType; // 暂不显示
    private String awbPC; // 总件数
    private String PC; // 件数
    private String Weight; // 重量
    private String Volume; // 体积(暂不显示)
    private String SpCode; // 特码
    private String Goods; // 品名
    private String Dep; // 启运港
    private String Dest; // 目的港,条件2
    private String Fdate; // 航班日期(2)
    private String Fno; // 航班号(2)
    private String PaperTime; // 接单时间[查询入库日期，止]
    private String BillsNO; // 账单号
    private String OPDate; // 入库时间(3)[查询入库日期，起]

    public String getOPDate() {
        return OPDate;
    }

    public void setOPDate(String OPDate) {
        this.OPDate = OPDate;
    }

    public String getMawb() {
        return Mawb;
    }

    public void setMawb(String mawb) {
        Mawb = mawb;
    }

    public String getBusinessType() {
        return BusinessType;
    }

    public void setBusinessType(String businessType) {
        BusinessType = businessType;
    }

    public String getAwbPC() {
        return awbPC;
    }

    public void setAwbPC(String awbPC) {
        this.awbPC = awbPC;
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

    public String getFdate() {
        return Fdate;
    }

    public void setFdate(String fdate) {
        Fdate = fdate;
    }

    public String getFno() {
        return Fno;
    }

    public void setFno(String fno) {
        Fno = fno;
    }

    public String getPaperTime() {
        return PaperTime;
    }

    public void setPaperTime(String paperTime) {
        PaperTime = paperTime;
    }

    public String getBillsNO() {
        return BillsNO;
    }

    public void setBillsNO(String billsNO) {
        BillsNO = billsNO;
    }
}
