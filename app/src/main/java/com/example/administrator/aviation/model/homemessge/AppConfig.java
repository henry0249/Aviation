package com.example.administrator.aviation.model.homemessge;

/**
 * 用于app的更新
 */

public class AppConfig {
    private String APPVersion;
    private String APPDescribe;
    private String APPURL;

    public String getAPPVersion() {
        return APPVersion;
    }

    public void setAPPVersion(String APPVersion) {
        this.APPVersion = APPVersion;
    }

    public String getAPPDescribe() {
        return APPDescribe;
    }

    public void setAPPDescribe(String APPDescribe) {
        this.APPDescribe = APPDescribe;
    }

    public String getAPPURL() {
        return APPURL;
    }

    public void setAPPURL(String APPURL) {
        this.APPURL = APPURL;
    }
}
