package com.example.administrator.aviation.model.hygnc;

/**
 * Created by Administrator on 2018/10/9.
 */

public class GNCManifestVSLoading {
    private String CarID;
    private String ULD;
    private String netWeight;
    private String CargoWeight;
    private String Dest;
    private String awbWeight;
    private String Contrast;
    private String Result;

    public String getDest() {
        return Dest;
    }

    public void setDest(String dest) {
        Dest = dest;
    }

    public GNCManifestVSLoading() {
        CarID = "";
        ULD = "";
        netWeight = "";
        CargoWeight = "";
        awbWeight = "";
        Contrast = "";

        Result = "";
        Dest = "";
    }

    public String getCarID() {
        return CarID;
    }

    public void setCarID(String carID) {
        CarID = carID;
    }

    public String getULD() {
        return ULD;
    }

    public void setULD(String ULD) {
        this.ULD = ULD;
    }

    public String getNetWeight() {
        return netWeight;
    }

    public void setNetWeight(String netWeight) {
        this.netWeight = netWeight;
    }

    public String getCargoWeight() {
        return CargoWeight;
    }

    public void setCargoWeight(String cargoWeight) {
        CargoWeight = cargoWeight;
    }

    public String getAwbWeight() {
        return awbWeight;
    }

    public void setAwbWeight(String awbWeight) {
        this.awbWeight = awbWeight;
    }

    public String getContrast() {
        return Contrast;
    }

    public void setContrast(String contrast) {
        Contrast = contrast;
    }

    public String getResult() {
        return Result;
    }

    public void setResult(String result) {
        Result = result;
    }
}
