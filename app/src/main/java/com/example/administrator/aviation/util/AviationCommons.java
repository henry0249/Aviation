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

    //解析时 错误信息的TAG
    public static final String Log_TAG = "ErrorText";

    // intent传递xml的标识
    public static final String LOGIN_XML = "loginxml";

    // bundle传递list项
    public static final String AWB_ITEM_INFO = "awbItemInfo";
    public static final String HOUSE_ITEM_INFO = "housedetail";
    public static final String INT_CHILD_INFO = "fenyeshuju";
    public static final String INT_GROUP_INFO = "zhufendanshuju";
    public static final String INT_AWB_HOUSE = "intawbhousexml";
    public static final String INT_ONEKEY_DECLARE = "decalrexml";
    public static final String DECLARE_INFO = "declareinfo";
    public static final String DECLARE_MAWB = "mawb";
    public static final String DECLARE_REARCHID = "rearchID";
    public static final String DECLARE_INFO_DEATIL = "declareinfoxml";
    public static final String DECLAREINFO_DEATIL = "declareinfo";
    public static final String IMP_CARGO_INFO = "cargoinfoxml";
    public static final String IMP_CARGO_INFO_ITEM = "cargoInfoMessage";
    public static final String IMP_CARGO_INFO_BUSINESSTYPE = "businessType";
    public static final String IMP_CARGO_INFO_HAWBID = "hawbID";
    public static final String FLIGHT_INFO = "flightinfo";
    public static final String FLIGHT_XML="xml";
    public static final String FLIGHT_DETAIL = "flightdetail";
    public static final String EDECLARE_INFO = "edeclareinfo";

    // Intent传值
    public static final String INT_GROUP_MAWB = "mawb";
    public static final String HIDE_INT_AWB_UPDATE = "hidehouse";
    public static final String MANAGE_HOUSE_MAWAB = "housemawb";
    public static final String MANAGE_HOUSE_BEGAIN_TIME = "beagintime";
    public static final String MANAGE_HOUSE_END_TIME = "endtime";
    public static final String SPLITE_REARCHID = "rearchid";
    public static final String SPLITE_PC = "pc";
    public static final String SPLITE_WEIGHT = "weight";
    public static final String SPLITE_VOLUME = "volume";
    public static final String IMP_HAWBID = "hawbid";
    public static final String IMP_MAWB = "mawb";
    public static final String IMP_TYPE = "businessType";

    // HomePageFragment内容
    public static final String APP_DOM_EXP_PREPARE_AWB = "appDomExpPrepareAWB";
    public static final String APP_DOM_EXP_WARE_HOUSE = "appDomExpWareHouse";
    public static final String APP_INT_EXP_AWB_MANAGE = "appIntExpAWBManage";
    public static final String APP_INT_EXP_PREPARE_AWB = "appIntExpPrepareAWB";
    public static final String APP_INT_EXPONEKEY_DECLARE = "appIntExpOneKeyDeclare";
    public static final String APP_INT_EXPONEKEY_DECLARE_INFO = "appIntExpDeclareInfo";
    public static final String APP_INT_IMP_CARGO_INFO = "appIntImpCargoInfo";
    public static final String APP_FLIGHT_MESSAGE = "appFlightMessage";
    public static final String APP_EDECLARE_INFO = "appExportDeclareInfoForCarrier";

    // 国际承运人
    public static final String APP_IntExportDayReportOfCarrier = "appIntExportDayReportOfCarrier";
    public static final String APP_IntExportReportOfCarrier = "appIntExportReportOfCarrier";
    public static final String APP_IntImportDayReportOfCarrier = "appIntImportDayReportOfCarrier";
    public static final String APP_IntImportReportOfCarrier = "appIntImportReportOfCarrier";

    // 国内承运人
    public static final String APP_DomExport0FlightPlanChecked = "appDomExport0FlightPlanChecked";
    public static final String APP_DomExportDayReportOfCarrier = "appDomExportDayReportOfCarrier";
    public static final String APP_DomExportReportOfCarrier = "appDomExportReportOfCarrier";
    public static final String APP_DomImportDayReportOfCarrier = "appDomImportDayReportOfCarrier";
    public static final String APP_DomImportReportOfCarrier = "appDomImportReportOfCarrier";

    //货运 国内出港
    //国内出港收运
    public static final String APP_CGO_Dom_Exp_CheckIn = "appCGODomExpCheckIn";
    //国内出港复磅
    public static final String APP_CGO_Dom_Exp_ReWeight = "appCGODomExpReWeight";
    //国内出港理货
    public static final String APP_CGO_Dom_Exp_ULDLoading = "appCGODomExpULDLoading";
    //国内出港装机单
    public static final String APP_CGO_Dom_Exp_MftLoading = "appCGODomExpMftLoading";
    //国内出港航班进程管控
    public static final String APP_CGO_Dom_Exp_FlightCtrl = "appCGODomExpFlightCtrl";
    // handler传递的值（int型）
    public static final int HOUSE_HANDLER = 0x111;
    public static final int FLIGHT_REFERCH = 0x222;
    public static final int INT_DECLARE_INFO = 0x333;
    public static final int FLIGHT_INFO_RQQUEST = 0x444;
    public static final int EDECLARE_INFO_H = 0x555;
    public static final int INT_EXPORT_DAY = 0x666;
    public static final int INT_E_DAT = 0x777;
    public static final int INT_E_DAY_MANIFEST = 0x888;
    public static final int INT_ES_DAT = 0x999;
    public static final int DOM_CHECK_INFO = 0x1111;
    public static final int GNC_expULDLoading = 0x2222;
    public static final int GNC_ULDLoadingCargo = 0x2221;
    public static final int GNC_ManifestLoading = 0x112;
    public static final int GNC_ManifestVsLoading = 0x113;
    public static final int GNC_FlightControls = 0x114;

    // intent传值需要返回
    public static final int AWB_ADD = 3;
    public static final int AWB_UPDATA = 2;
    public static final int CHANGE_PASS = 114;

    //权限申请
    public static final int REQUEST_CODE_CAMERA_PERMISSIONS  = 0x100;

    //回调参数
    public static final int GNC_ULDLOADING_CAMERA_REQUEST = 1;
    public static final int GNC_ULDinfo_CAMERA_REQUEST = 2;
    public static final int GNC_ULDLOADING_XinZenPinBan_REQUEST = 3;
    public static final int GNC_gnShouYun_REQUEST = 4;
    public static final int GNC_ZhuangJiDan_REQUEST = 5;

    public static final int GNC_ULDLOADING_CAMERA_RESULT = 11;
    public static final int GNC_ULDinfo_CAMERA_RESULT = 22;
    public static final int GNC_ULDLOADING_XinZenPinBan_RESULT = 33;
    public static final int GNC_gnShouYun_RESULT = 44;
    public static final int GNC_ZhuangJiDan_RESULT = 55;
    //上拉加载更多
    public static final int LOAD_DATA = 2;
    //下拉刷新
    public static final int REFRESH_DATA = 1;
}
