package com.example.administrator.aviation.util;

/**
 * 主要存全局常用值
 */

public class AviationCommons {
    public static String TAG = "ACTAG";
    /**
     * 登录得到服务器成功返回的ID（每次注销得到的ID值不一样）
     */
    public static String LoginFlag = "";

    // intent传递xml的标识
    public static final String LOGIN_XML = "loginxml";

    // bundle传递list项
    public static final String AWB_ITEM_INFO = "awbItemInfo";
    public static final String HOUSE_ITEM_INFO = "housedetail";
    public static final String INT_CHILD_INFO = "fenyeshuju";
    public static final String INT_GROUP_INFO = "zhufendanshuju";

    // Intent传值
    public static final String INT_GROUP_MAWB = "mawb";

    // HomePageFragment内容
    public static final String APP_DOM_EXP_PREPARE_AWB = "appDomExpPrepareAWB";
    public static final String APP_DOM_EXP_WARE_HOUSE = "appDomExpWareHouse";
    public static final String APP_INT_EXP_AWB_MANAGE = "appIntExpAWBManage";
    public static final String APP_INT_EXP_PREPARE_AWB = "appIntExpPrepareAWB";

    // handler传递的值（int型）
    public static final int HOUSE_HANDLER = 0x111;
    public static final int INT_AWB_HANDLER = 0x222;
}
