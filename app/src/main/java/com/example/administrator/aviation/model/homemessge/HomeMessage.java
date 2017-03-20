package com.example.administrator.aviation.model.homemessge;

/**
 * 用户登录获取到xml信息
 * 如可查看页面的名称和可访问的到期时间
 */

public class HomeMessage {
    // 页面的名称
    private String name;

    // 页面中文名
    private String nameCN;

    //
    private String isreadonly;

    // 到期时间
    private String activeDate;

    private AppConfig appconfig;

    public AppConfig getAppconfig() {
        return appconfig;
    }

    public void setAppconfig(AppConfig appconfig) {
        this.appconfig = appconfig;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIsreadonly() {
        return isreadonly;
    }

    public void setIsreadonly(String isreadonly) {
        this.isreadonly = isreadonly;
    }

    public String getActiveDate() {
        return activeDate;
    }

    public void setActiveDate(String activeDate) {
        this.activeDate = activeDate;
    }

    public String getNameCN() {
        return nameCN;
    }

    public void setNameCN(String nameCN) {
        this.nameCN = nameCN;
    }
}
