package com.example.administrator.aviation.model.domjcgrbb;

/**
 * Created by 59573 on 2017/7/25.
 */

public class FlightPlanInfo {
    private String FDate;
    private String Fno;
    private String FlightChecked;
    private String Mawb;

    public FlightPlanInfo(String FDate, String fno, String flightChecked, String mawb) {
        this.FDate = FDate;
        Fno = fno;
        FlightChecked = flightChecked;
        Mawb = mawb;
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

    public String getFlightChecked() {
        return FlightChecked;
    }

    public void setFlightChecked(String flightChecked) {
        FlightChecked = flightChecked;
    }

    public String getMawb() {
        return Mawb;
    }

    public void setMawb(String mawb) {
        Mawb = mawb;
    }
}
