package com.example.administrator.aviation.ui.base;

import android.graphics.Color;
import android.widget.TextView;

public class TableModel {

    private String orgCode;
    private String leftTitle;
    private String text0;
    private String text1;
    private String text2;
    private String text3;
    private String text4;
    private String text5;
    private String text6;
    private String text7;
    private String text8;
    private String text9;
    private String text10;
    private String text11;
    private String text12;
    private String text13;
    private String text14;
    private String text15;
    private String text16;
    private String text17;
    private String text18;
    private String text19;
    private String text20;
    private String text21;
    private String text22;
    private String text23;
    private String text24;
    private String text25;
    private String text26;
    private String text27;
    private String text28;
    private String text29;
    private String text30;

    public String getText20() {
        return text20;
    }

    public void setText20(String text20) {
        this.text20 = text20;
    }

    public String getText21() {
        return text21;
    }

    public void setText21(String text21) {
        this.text21 = text21;
    }

    public String getText22() {
        return text22;
    }

    public void setText22(String text22) {
        this.text22 = text22;
    }

    public String getText23() {
        return text23;
    }

    public void setText23(String text23) {
        this.text23 = text23;
    }

    public String getText24() {
        return text24;
    }

    public void setText24(String text24) {
        this.text24 = text24;
    }

    public String getText25() {
        return text25;
    }

    public void setText25(String text25) {
        this.text25 = text25;
    }

    public String getText26() {
        return text26;
    }

    public void setText26(String text26) {
        this.text26 = text26;
    }

    public String getText27() {
        return text27;
    }

    public void setText27(String text27) {
        this.text27 = text27;
    }

    public String getText28() {
        return text28;
    }

    public void setText28(String text28) {
        this.text28 = text28;
    }

    public String getText29() {
        return text29;
    }

    public void setText29(String text29) {
        this.text29 = text29;
    }

    public String getText30() {
        return text30;
    }

    public void setText30(String text30) {
        this.text30 = text30;
    }

    public TableModel(){}

    public String getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    public String getLeftTitle() {
        return leftTitle;
    }

    public void setLeftTitle(String leftTitle) {
        this.leftTitle = leftTitle;
    }

    public String getText0() {
        return text0;
    }

    public void setText0(String text0) {
        this.text0 = text0;
    }

    public String getText1() {
        return text1;
    }

    public void setText1(String text1) {
        this.text1 = text1;
    }

    public String getText2() {
        return text2;
    }

    public void setText2(String text2) {
        this.text2 = text2;
    }

    public String getText3() {
        return text3;
    }

    public void setText3(String text3) {
        this.text3 = text3;
    }

    public String getText4() {
        return text4;
    }

    public void setText4(String text4) {
        this.text4 = text4;
    }

    public String getText5() {
        return text5;
    }

    public void setText5(String text5) {
        this.text5 = text5;
    }

    public String getText6() {
        return text6;
    }

    public void setText6(String text6) {
        this.text6 = text6;
    }

    public String getText7() {
        return text7;
    }

    public void setText7(String text7) {
        this.text7 = text7;
    }


    public String getText8() {
        return text8;
    }

    public void setText8(String text8) {
        this.text8 = text8;
    }

    public String getText9() {
        return text9;
    }

    public void setText9(String text9) {
        this.text9 = text9;
    }

    public String getText10() {
        return text10;
    }

    public void setText10(String text10) {
        this.text10 = text10;
    }

    public String getText11() {
        return text11;
    }

    public void setText11(String text11) {
        this.text11 = text11;
    }

    public String getText12() {
        return text12;
    }

    public void setText12(String text12) {
        this.text12 = text12;
    }

    public String getText13() {
        return text13;
    }

    public void setText13(String text13) {
        this.text13 = text13;
    }

    public String getText14() {
        return text14;
    }

    public void setText14(String text14) {
        this.text14 = text14;
    }

    public String getText15() {
        return text15;
    }

    public void setText15(String text15) {
        this.text15 = text15;
    }

    public String getText16() {
        return text16;
    }

    public void setText16(String text16) {
        this.text16 = text16;
    }

    public String getText17() {
        return text17;
    }

    public void setText17(String text17) {
        this.text17 = text17;
    }

    public String getText18() {
        return text18;
    }

    public void setText18(String text18) {
        this.text18 = text18;
    }

    public String getText19() {
        return text19;
    }

    public void setText19(String text19) {
        this.text19 = text19;
    }



    /**
     * 是否正数
     * @return
     */
    public boolean positiveNumber1(){
        if(text1.contains("-")){
            return false;
        }else{
            return true;
        }
    }

    private String str_red_color="#ff0000";
    private String str_green_color="#009944";
    public void setTextColor(TextView tv_table_content_right_item, String text) {
        if(!text.contains("-")){
            tv_table_content_right_item.setTextColor(Color.parseColor(str_red_color));
        }else{
            if(text.trim().length()>1){
                tv_table_content_right_item.setTextColor(Color.parseColor(str_green_color));
            }else{
                tv_table_content_right_item.setTextColor(Color.BLACK);
            }
        }
    }
}
