package com.example.administrator.aviation.http;

/**
 * 存放后台的url地址
 */

public class HttpCommons {
    // 版本升级的url（目前是从txt文件中获取版本升级的url和版本的versionCode）
    public static final String UPDATE_VERSION_URL = "https://github.com/mxzs1314/.github.io/raw/master";
    // apk下载地址
    public static final String APK_URL = "https://github.com/mxzs1314/.github.io/raw/master/app/app-release.apk";

    // 命名空间
    public static final String NAME_SPACE = "http://58.213.128.130/";

    // EndPoint
    public static final String END_POINT = "http://58.213.128.130:888/AirLogisticsAPP/AirLogisticsService.asmx";

    // 登录
    public static final String LOGIN_METHOD_NAME = "APPUserLogin";
    public static final String LOGIN_SOAP_ACTION = "http://58.213.128.130/APPUserLogin";

    // 国内
    // 获取订单列表
    public static final String AWB_METHOD_NAME = "CGOGetDomExportAWBofPrepare";
    public static final String AWB_SOAP_ACTION = "http://58.213.128.130/CGOGetDomExportAWBofPrepare";

    // 更新订单列表
    public static final String AWB_UPDATE_METHOD_NAME = "CGOUpdateDomExportAWBofPrepare";
    public static final String AWB_UPDATE_SOAP_ACTION = "http://58.213.128.130/CGOUpdateDomExportAWBofPrepare";

    // 增加订单列表
    public static final String AWB_ADD_METHOD_NAME = "CGOAddDomExportAWBofPrepare";
    public static final String AWB_ADD_METHOD_ACTION = "http://58.213.128.130/CGOAddDomExportAWBofPrepare";

    // 删除订单
    public static final String AWB_DELETE_METHOD_NAME = "CGODeleteDomExportAWBofPrepare";
    public static final String AWB_DELETE_METHOD_ACTION = "http://58.213.128.130/CGODeleteDomExportAWBofPrepare";

    /**
     * house界面的url
     */
    // 获取house详情
    public static final String HOUSE_SEARCH_METHOD_NAME = "CGOGetDomExportWarehouse";
    public static final String HOUSE_SEARCH_METHOD_ACTION = "http://58.213.128.130/CGOGetDomExportWarehouse";

    /**
     * 国际
     */
    // 获取所有订单信息
    public static final String GET_INT_METHOD_NAME = "CGOGetIntExportAWBofPrepare";
    public static final String GET_INT_METHOD_ACTION = "http://58.213.128.130/CGOGetIntExportAWBofPrepare";

    // 主单列表的更新，增加，删除
    public static final String UPDATE_INT_MAWB_METHOD_NAME = "CGOUpdateIntExportMawbOfPrepare";
    public static final String UPDATE_INT_MAWB_METHOD_ACTION = "http://58.213.128.130/CGOUpdateIntExportMawbOfPrepare";
    public static final String DELETE_INT_MAWB_METHOD_NAME = "CGODeleteIntExportMawbOfPrepare";
    public static final String DELETE_INT_MAWB_METHOD_ACTION = "http://58.213.128.130/CGODeleteIntExportMawbOfPrepare";
    public static final String ADD_INT_MAWB_METHOD_NAME = "CGOAddIntExportMawbOfPrepare";
    public static final String ADD_INT_MAWB_METHOD_ACTION = "http://58.213.128.130/CGOAddIntExportMawbOfPrepare";

    // 分单列表的更新，增加,删除
    public static final String UPDATE_INT_HAWB_METHOD_NAME = "CGOUpdateIntExportHawb";
    public static final String UPDATE_INT_HAWB_METHOD_ACTION = "http://58.213.128.130/CGOUpdateIntExportHawb";
    public static final String ADD_INT_HAWB_METHOD_NAME = "CGOAddIntExportHawb";
    public static final String ADD_INT_HAWB_METHOD_ACTION = "http://58.213.128.130/CGOAddIntExportHawb";
    public static final String DELETE_INT_HAWB_METHOD_NAME = "CGODeleteIntExportHawb";
    public static final String DELETE_INT_HAWB_METHOD_ACTION = "http://58.213.128.130/CGODeleteIntExportHawb";
}
