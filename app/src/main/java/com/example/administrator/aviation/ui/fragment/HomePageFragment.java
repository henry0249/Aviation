package com.example.administrator.aviation.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.Toast;

import com.example.administrator.aviation.R;
import com.example.administrator.aviation.model.adapter.HomePageAdapter;
import com.example.administrator.aviation.model.homemessge.HomeMessage;
import com.example.administrator.aviation.model.homemessge.PrefereceHomeMessage;
import com.example.administrator.aviation.tool.DateUtils;
import com.example.administrator.aviation.ui.activity.domandintgetflight.FlightHomeDetailActivity;
import com.example.administrator.aviation.ui.activity.domexphouse.AppDomWareHouseHomeActivity;
import com.example.administrator.aviation.ui.activity.domflightcheck.DomFlightInHomeActivity;
import com.example.administrator.aviation.ui.activity.domjcgrbb.DomExportDayCarrierHomeActivity;
import com.example.administrator.aviation.ui.activity.domjcgrbb.DomImportDayCarrierHomeActivity;
import com.example.administrator.aviation.ui.activity.domjcgywl.DomExportCarrierHomeActivity;
import com.example.administrator.aviation.ui.activity.domjcgywl.DomImportCarrierHomeActivity;
import com.example.administrator.aviation.ui.activity.edeclareinfo.AppEdeclareHomeActivity;
import com.example.administrator.aviation.ui.activity.intawbofprepare.AppIntExpPrepareAWBActivity;
import com.example.administrator.aviation.ui.activity.intdeclareinfo.AppIntDeclareInfoHomeActivity;
import com.example.administrator.aviation.ui.activity.intexpawbhousemanage.AppIntExpAwbHouseHomeActivity;
import com.example.administrator.aviation.ui.activity.intexponekeydeclare.AppIntOneKeyDeclarelHomeActivity;
import com.example.administrator.aviation.ui.activity.intimpcargoinfo.AppIntimpCarGoInfoHomeActivity;
import com.example.administrator.aviation.ui.activity.intjcgrbb.IntExportDayCarrierHomeActivity;
import com.example.administrator.aviation.ui.activity.intjcgrbb.IntImportDayCarrierHomeActivity;
import com.example.administrator.aviation.ui.activity.domprepareawb.AppDomExpPrePareAWBActivity;
import com.example.administrator.aviation.ui.activity.intjcgywl.IntExportCarrierHomeActivity;
import com.example.administrator.aviation.ui.activity.intjcgywl.IntImportCarrierHomeActivity;
import com.example.administrator.aviation.ui.base.NavBar;
import com.example.administrator.aviation.ui.cgo.domestic.JinChengGuanKong;
import com.example.administrator.aviation.ui.cgo.domestic.ReWeightMain;
import com.example.administrator.aviation.ui.cgo.domestic.ZhuangJiDanMain;
import com.example.administrator.aviation.ui.cgo.domestic.expULDLoading;
import com.example.administrator.aviation.ui.cgo.gnj.KuaiJianSaoMiaoMainActivity;
import com.example.administrator.aviation.ui.cgo.gnj.gnjPickUpInfoActivity;
import com.example.administrator.aviation.ui.cgo.gnj.kjsmSaoMaQiangActivity;
import com.example.administrator.aviation.util.AviationCommons;
import com.example.administrator.aviation.util.PreferenceUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 首页fragment
 */
public class HomePageFragment extends Fragment{
    // 用户权限
    private String userPermission = "";

    // 锁屏密码
    private String lockPass = "";

    private GridView grid_home;
    private BaseAdapter mAdapter = null;

    private String xml = "";

    private View view;
    private List<HomeMessage> list;

    // 时间比较
    private String time;
    private String splitBeginTime;
    private  Date beginTime;
    private  Date endTime;
    private String todayTime;// 获得当前时间
    private boolean result;// 时间返回结果（true表示在使用周期，false表示不在使用周期）

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.homepage_fragment, container, false);
        initView();
        return view;
    }
    private void initView() {
        NavBar navBar = new NavBar(getActivity());
        navBar.setTitle("首页");
        navBar.hideRight();
        navBar.hideLeft();

        // 初始化gridView
        grid_home = (GridView) view.findViewById(R.id.home_gridView);

        // 得到锁屏密码
        lockPass = PreferenceUtils.getLockPass(getActivity());

        // 得到登录传递过来的xml数据
        xml = getActivity().getIntent().getStringExtra(AviationCommons.LOGIN_XML);
        list = PrefereceHomeMessage.pullXml(xml,getActivity());

        // 判断是否设置过锁屏密码
//        if (!TextUtils.isEmpty(lockPass)) {
//            // 解锁用户名
//            String userName = PreferenceUtils.getUserName(getActivity());
//            userPermission = getActivity().getIntent().getStringExtra("userPermission");
//        }

        mAdapter = new HomePageAdapter<HomeMessage>((ArrayList<HomeMessage>) list, R.layout.homepage_item) {

            @Override
            public void bindView(ViewHolder holder, HomeMessage obj) {
                if (obj.getName().equals(AviationCommons.APP_DOM_EXP_PREPARE_AWB)) {
                    holder.setImageResource(R.id.image_iv,  R.drawable.domawb);
                }else if (obj.getName().equals(AviationCommons.APP_DOM_EXP_WARE_HOUSE)){
                    holder.setImageResource(R.id.image_iv,  R.drawable.domhouse);
                } else if (obj.getName().equals(AviationCommons.APP_INT_EXP_AWB_MANAGE)) {
                    holder.setImageResource(R.id.image_iv, R.drawable.intmanage);
                } else if (obj.getName().equals(AviationCommons.APP_INT_EXP_PREPARE_AWB)) {
                    holder.setImageResource(R.id.image_iv, R.drawable.intawb);
                } else if (obj.getName().equals(AviationCommons.APP_INT_EXPONEKEY_DECLARE)) {
                    holder.setImageResource(R.id.image_iv, R.drawable.intdecleare);
                } else if (obj.getName().equals(AviationCommons.APP_INT_EXPONEKEY_DECLARE_INFO)) {
                    holder.setImageResource(R.id.image_iv, R.drawable.declareinfo);
                } else if (obj.getName().equals(AviationCommons.APP_INT_IMP_CARGO_INFO)) {
                    holder.setImageResource(R.id.image_iv, R.drawable.impcargoinfo);
                } else if (obj.getName().equals(AviationCommons.APP_FLIGHT_MESSAGE)) {
                    holder.setImageResource(R.id.image_iv, R.drawable.hangbandongtai);
                } else if (obj.getName().equals(AviationCommons.APP_EDECLARE_INFO)) {
                    holder.setImageResource(R.id.image_iv, R.drawable.gjcglj);
                } else if (obj.getName().equals(AviationCommons.APP_IntExportDayReportOfCarrier)) {
                    holder.setImageResource(R.id.image_iv, R.drawable.gjcgrbb);
                } else if (obj.getName().equals(AviationCommons.APP_IntExportReportOfCarrier)) {
                    holder.setImageResource(R.id.image_iv, R.drawable.gjcgywl);
                } else if (obj.getName().equals(AviationCommons.APP_IntImportDayReportOfCarrier)) {
                    holder.setImageResource(R.id.image_iv, R.drawable.gjjgrbb);
                } else if (obj.getName().equals(AviationCommons.APP_IntImportReportOfCarrier)) {
                    holder.setImageResource(R.id.image_iv, R.drawable.gjjgywl);
                } else if (obj.getName().equals(AviationCommons.APP_DomExport0FlightPlanChecked)) {
                    holder.setImageResource(R.id.image_iv, R.drawable.gndcjh);
                } else if (obj.getName().equals(AviationCommons.APP_DomExportDayReportOfCarrier)) {
                    holder.setImageResource(R.id.image_iv, R.drawable.gncgrbb);
                } else if (obj.getName().equals(AviationCommons.APP_DomExportReportOfCarrier)) {
                    holder.setImageResource(R.id.image_iv, R.drawable.gncgywl);
                } else if (obj.getName().equals(AviationCommons.APP_DomImportDayReportOfCarrier)) {
                    holder.setImageResource(R.id.image_iv, R.drawable.gnjgrbb);
                } else if (obj.getName().equals(AviationCommons.APP_DomImportReportOfCarrier)) {
                    holder.setImageResource(R.id.image_iv, R.drawable.gnjgywl);
                }else if (obj.getName().equals(AviationCommons.APP_CGO_Dom_Exp_CheckIn)) {
                    holder.setImageResource(R.id.image_iv, R.drawable.app_cgo_dom_exp_checkin);
                }else if (obj.getName().equals(AviationCommons.APP_CGO_Dom_Exp_ReWeight)) {
                    holder.setImageResource(R.id.image_iv, R.drawable.app_cgo_dom_exp_reweight);
                } else if (obj.getName().equals(AviationCommons.APP_CGO_Dom_Exp_ULDLoading)) {
                    holder.setImageResource(R.id.image_iv, R.drawable.app_cgo_dom_exp_uldloading);
                }else if (obj.getName().equals(AviationCommons.APP_CGO_Dom_Exp_MftLoading)) {
                    holder.setImageResource(R.id.image_iv, R.drawable.gnchugangcangdan);
                }else if (obj.getName().equals(AviationCommons.APP_CGO_Dom_Exp_FlightCtrl)){
                    holder.setImageResource(R.id.image_iv, R.drawable.hangbanjinchen);
                }else if (obj.getName().equals(AviationCommons.APP_CGO_Dom_Exp_GnjPickUp)){
                    holder.setImageResource(R.id.image_iv, R.drawable.gnjgtq);
                }else if(obj.getName().equals(AviationCommons.APP_GJC_appIntExpKJScanCommand)){
                    holder.setImageResource(R.id.image_iv, R.drawable.kuaijiansaoma);
                }
                Log.d("guoji", obj.getName());

                String title = "";
                if (obj.getNameCN().contains("国内")) {
                    title = obj.getNameCN().substring(2);
                } else {
                    title = obj.getNameCN();
                }
                holder.setText(R.id.image_tv, title);
            }
        };
        grid_home.setAdapter(mAdapter);

        grid_home.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                time = list.get(position).getActiveDate();
                splitBeginTime = time.split("T")[0];
                todayTime = DateUtils.getTodayDateTime();
                beginTime = DateUtils.convertFromStrYMD(splitBeginTime);
                endTime = DateUtils.convertFromStrYMD(todayTime);

                result = DateUtils.compareDate(beginTime, endTime);
                if (result && list.get(position).getName().equals(AviationCommons.APP_DOM_EXP_PREPARE_AWB)) {
                    Intent intentAWB = new Intent(getActivity(), AppDomExpPrePareAWBActivity.class);
                    startActivity(intentAWB);
                } else if (result && list.get(position).getName().equals(AviationCommons.APP_DOM_EXP_WARE_HOUSE)) {
                    Intent intentHouse = new Intent(getActivity(), AppDomWareHouseHomeActivity.class);
                    startActivity(intentHouse);
                }else if (result && list.get(position).getName().equals(AviationCommons.APP_INT_EXP_AWB_MANAGE)) {
                    Intent intentIntManage = new Intent(getActivity(), AppIntExpAwbHouseHomeActivity.class);
                    startActivity(intentIntManage);
                } else if (result && list.get(position).getName().equals(AviationCommons.APP_INT_EXP_PREPARE_AWB)) {
                    Intent intentIntAwb = new Intent(getActivity(), AppIntExpPrepareAWBActivity.class);
                    startActivity(intentIntAwb);
                } else if (result && list.get(position).getName().equals(AviationCommons.APP_INT_EXPONEKEY_DECLARE)) {
                    Intent intentDeclare = new Intent(getActivity(), AppIntOneKeyDeclarelHomeActivity.class);
                    startActivity(intentDeclare);
                } else if (result && list.get(position).getName().equals(AviationCommons.APP_INT_EXPONEKEY_DECLARE_INFO)) {
                    Intent intentDeclareInfo = new Intent(getActivity(), AppIntDeclareInfoHomeActivity.class);
                    startActivity(intentDeclareInfo);
                } else if (result && list.get(position).getName().equals(AviationCommons.APP_INT_IMP_CARGO_INFO)) {
                    Intent intentCargoInfo = new Intent(getActivity(), AppIntimpCarGoInfoHomeActivity.class);
                    startActivity(intentCargoInfo);
                } else if (result && list.get(position).getName().equals(AviationCommons.APP_FLIGHT_MESSAGE)) {
                    Intent intentFlight = new Intent(getActivity(), FlightHomeDetailActivity.class);
                    startActivity(intentFlight);
                } else if (result && list.get(position).getName().equals(AviationCommons.APP_EDECLARE_INFO)) {
                    Intent intentEdeclare = new Intent(getActivity(), AppEdeclareHomeActivity.class);
                    startActivity(intentEdeclare);
                } else if (result && list.get(position).getName().equals(AviationCommons.APP_IntExportReportOfCarrier)) {
                    Intent intentECA = new Intent(getActivity(), IntExportCarrierHomeActivity.class);
                    startActivity(intentECA);
                } else if (result && list.get(position).getName().equals(AviationCommons.APP_IntImportReportOfCarrier)) {
                    Intent intentIRC = new Intent(getActivity(), IntImportCarrierHomeActivity.class);
                    startActivity(intentIRC);
                } else if (result && list.get(position).getName().equals(AviationCommons.APP_IntExportDayReportOfCarrier)) {
                    Intent intentIEDO = new Intent(getActivity(), IntExportDayCarrierHomeActivity.class);
                    startActivity(intentIEDO);
                } else if (result && list.get(position).getName().equals(AviationCommons.APP_IntImportDayReportOfCarrier)) {
                    Intent intentIDRC = new Intent(getActivity(), IntImportDayCarrierHomeActivity.class);
                    startActivity(intentIDRC);
                } else if (result && list.get(position).getName().equals(AviationCommons.APP_DomExportReportOfCarrier)) {
                    Intent intentDomR = new Intent(getActivity(), DomExportCarrierHomeActivity.class);
                    startActivity(intentDomR);
                } else if (result && list.get(position).getName().equals(AviationCommons.APP_DomImportReportOfCarrier)) {
                    Intent intentDomRI = new Intent(getActivity(), DomImportCarrierHomeActivity.class);
                    startActivity(intentDomRI);
                } else if (result && list.get(position).getName().equals(AviationCommons.APP_DomExportDayReportOfCarrier)) {
                    Intent intentDomRDE = new Intent(getActivity(), DomExportDayCarrierHomeActivity.class);
                    startActivity(intentDomRDE);
                } else if (result && list.get(position).getName().equals(AviationCommons.APP_DomImportDayReportOfCarrier)) {
                    Intent intentDomRDI = new Intent(getActivity(), DomImportDayCarrierHomeActivity.class);
                    startActivity(intentDomRDI);
                }else if (result && list.get(position).getName().equals(AviationCommons.APP_DomExport0FlightPlanChecked)) {
                    Intent intentDomRDI = new Intent(getActivity(), DomFlightInHomeActivity.class);
                    startActivity(intentDomRDI);
                } else if (result && list.get(position).getName().equals(AviationCommons.APP_CGO_Dom_Exp_ULDLoading)) {
                    Intent CgoDomExpULDLoading = new Intent(getActivity(), expULDLoading.class);
                    startActivity(CgoDomExpULDLoading);
                } else if (result && list.get(position).getName().equals(AviationCommons.APP_CGO_Dom_Exp_ReWeight)) {
                        Intent CgoDomExpULDLoading = new Intent(getActivity(), ReWeightMain.class);
                        startActivity(CgoDomExpULDLoading);
                }else if (result && list.get(position).getName().equals(AviationCommons.APP_CGO_Dom_Exp_CheckIn)) {
//                    Intent CgoDomExpULDLoading = new Intent(getActivity(), gnShouYunChaXun.class);
//                    Intent CgoDomExpULDLoading = new Intent(getActivity(), KuaiJianSaoMiaoMainActivity.class);
//                    startActivity(CgoDomExpULDLoading);
                }else if (result && list.get(position).getName().equals(AviationCommons.APP_CGO_Dom_Exp_MftLoading)) {
                    Intent CgoDomExpULDLoading = new Intent(getActivity(), ZhuangJiDanMain.class);
                    startActivity(CgoDomExpULDLoading);
                } else if (result && list.get(position).getName().equals(AviationCommons.APP_CGO_Dom_Exp_FlightCtrl)) {
                    Intent CgoDomExpULDLoading = new Intent(getActivity(), JinChengGuanKong.class);
                    startActivity(CgoDomExpULDLoading);
                }else if (result && list.get(position).getName().equals(AviationCommons.APP_CGO_Dom_Exp_GnjPickUp)) {
                    Intent PickUP = new Intent(getActivity(), gnjPickUpInfoActivity.class);
                    startActivity(PickUP);
                } else if (result && list.get(position).getName().equals(AviationCommons.APP_GJC_appIntExpKJScanCommand)) {
                    Intent ScanCommand = null;

                    if (AviationCommons.AndroidModel.equals("PDA")) {
                        ScanCommand = new Intent(getActivity(), kjsmSaoMaQiangActivity.class);
                    } else {
                        ScanCommand = new Intent(getActivity(), KuaiJianSaoMiaoMainActivity.class);
                    }

                    startActivity(ScanCommand);
                }else {
                    Toast.makeText(getActivity(), "功能尚未开发", Toast.LENGTH_LONG).show();
                }
            }
        });

    }
}
