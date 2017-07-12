package com.example.administrator.aviation.model.intjcgrbb;

/**
 * 国际出港日报表信息
 */

public class IntExportDayInfo {
    private String Carrier;
    private String FDate;
    private String Fno;
    private String FDep;
    private String JTZ;
    private String FDest;
    private String EstimatedTakeOff; //预计起飞
    private String ActualTakeOff; // 实际起飞
    private String EstimatedArrival;//预计到达
    private String ActualArrival; // 实际到达
    private String PC;
    private String Weight;
    private String Volume;

    public String getEstimatedArrival() {
        return EstimatedArrival;
    }

    public void setEstimatedArrival(String estimatedArrival) {
        EstimatedArrival = estimatedArrival;
    }

    public String getActualArrival() {
        return ActualArrival;
    }

    public void setActualArrival(String actualArrival) {
        ActualArrival = actualArrival;
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

    public String getFno() {
        return Fno;
    }

    public void setFno(String fno) {
        Fno = fno;
    }

    public String getFDep() {
        return FDep;
    }

    public void setFDep(String FDep) {
        this.FDep = FDep;
    }

    public String getJTZ() {
        return JTZ;
    }

    public void setJTZ(String JTZ) {
        this.JTZ = JTZ;
    }

    public String getFDest() {
        return FDest;
    }

    public void setFDest(String FDest) {
        this.FDest = FDest;
    }

    public String getEstimatedTakeOff() {
        return EstimatedTakeOff;
    }

    public void setEstimatedTakeOff(String estimatedTakeOff) {
        EstimatedTakeOff = estimatedTakeOff;
    }

    public String getActualTakeOff() {
        return ActualTakeOff;
    }

    public void setActualTakeOff(String actualTakeOff) {
        ActualTakeOff = actualTakeOff;
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
}
