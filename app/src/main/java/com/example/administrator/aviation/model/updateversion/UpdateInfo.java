package com.example.administrator.aviation.model.updateversion;

/**
 * 版本更新的一个类，用来与update.txt的被容相对应
 */

public class UpdateInfo {
    // 版本versioncode值
    private String version;

    // 升级内容描述
    private String description;

    // 下载的链接
    private String url;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
