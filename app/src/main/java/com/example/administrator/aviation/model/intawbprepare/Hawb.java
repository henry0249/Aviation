package com.example.administrator.aviation.model.intawbprepare;

import java.io.Serializable;

/**
 * 国际预录入分页信息
 */

public class Hawb implements Serializable{
    private String HawbID; // 编号，由系统生成，用户页面不显示，但更新功能需上传
    private String Hno; // 分运单号
    private String PC; // 分单件数
    private String Weight; // 分单重量
    private String Volume; // 分单体积[自动计算，但可修改：Weight/220]
    private String Goods; // 品名(英)
    private String GoodsCN; // 品名(中)
    private String MftStatus; // 预配状态(只显示，不用录入)

    public String getHawbID() {
        return HawbID;
    }

    public void setHawbID(String hawbID) {
        HawbID = hawbID;
    }

    public String getHno() {
        return Hno;
    }

    public void setHno(String hno) {
        Hno = hno;
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

    public String getGoodsCN() {
        return GoodsCN;
    }

    public void setGoodsCN(String goodsCN) {
        GoodsCN = goodsCN;
    }

    public String getMftStatus() {
        return MftStatus;
    }

    public void setMftStatus(String mftStatus) {
        MftStatus = mftStatus;
    }

    @Override
    public String toString() {
        return "Hawb{" +
                "HawbID='" + HawbID + '\'' +
                ", Hno='" + Hno + '\'' +
                ", PC='" + PC + '\'' +
                ", Weight='" + Weight + '\'' +
                ", Volume='" + Volume + '\'' +
                ", Goods='" + Goods + '\'' +
                ", GoodsCN='" + GoodsCN + '\'' +
                ", MftStatus='" + MftStatus + '\'' +
                '}';
    }
}
