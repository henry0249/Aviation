package com.example.administrator.aviation.model.intjcgywl;

import java.io.Serializable;

/**
 * 国际进港业务量信息
 */

public class IntImportCarrierInfo implements Serializable{
    private String Carrier;
    private String FDate;
    private String dest;
    private String origin;
    private String fno;
    private String pc;
    private String weight;
    private String volume;

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getFno() {
        return fno;
    }

    public void setFno(String fno) {
        this.fno = fno;
    }

    public String getDest() {
        return dest;
    }

    public void setDest(String dest) {
        this.dest = dest;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

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

    public String getPc() {
        return pc;
    }

    public void setPc(String pc) {
        this.pc = pc;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }
}
