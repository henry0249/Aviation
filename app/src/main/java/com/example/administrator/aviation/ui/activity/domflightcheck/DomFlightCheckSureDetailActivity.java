package com.example.administrator.aviation.ui.activity.domflightcheck;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.administrator.aviation.R;
import com.example.administrator.aviation.model.domjcgrbb.FlightAWBPlanInfo;
import com.example.administrator.aviation.ui.base.NavBar;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 订舱确认详情页
 */

public class DomFlightCheckSureDetailActivity extends Activity {
    @BindView(R.id.check_plan_mawb_detail_tv)
    TextView checkPlanMawbDetailTv;
    @BindView(R.id.check_plan_shenpi_detail_tv)
    TextView checkPlanShenpiDetailTv;
    @BindView(R.id.check_plan_rukuzhuangtai_detail_tv)
    TextView checkPlanRukuzhuangtaiDetailTv;
    @BindView(R.id.check_plan_dlrdm_detail_tv)
    TextView checkPlanDlrdmDetailTv;
    @BindView(R.id.check_plan_dlrmc_detail_tv)
    TextView checkPlanDlrmcDetailTv;
    @BindView(R.id.chaeck_plan_surestate_detail_tv)
    TextView chaeckPlanSurestateDetailTv;
    @BindView(R.id.check_plan_mudigang_detail_tv)
    TextView checkPlanMudigangDetailTv;
    @BindView(R.id.check_plan_pc_detail_tv)
    TextView checkPlanPcDetailTv;
    @BindView(R.id.check_plan_weight_detail_tv)
    TextView checkPlanWeightDetailTv;
    @BindView(R.id.check_plan_volume_detail_tv)
    TextView checkPlanVolumeDetailTv;
    @BindView(R.id.check_plan_goods_detail_tv)
    TextView checkPlanGoodsDetailTv;
    @BindView(R.id.check_plan_fdate_detail_tv)
    TextView checkPlanFdateDetailTv;
    @BindView(R.id.check_plan_fno_detail_tv)
    TextView checkPlanFnoDetailTv;
    @BindView(R.id.check_plan_user_detail_tv)
    TextView checkPlanUserDetailTv;
    @BindView(R.id.check_plan_detail_time_tv)
    TextView checkPlanDetailTimeTv;

    private FlightAWBPlanInfo flightAWBPlanInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dom_flight_plan_detail);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        NavBar navBar = new NavBar(this);
        navBar.setTitle("订舱确定列表详情");
        navBar.hideRight();

        // 获取上层界面传递过来的实体类
        flightAWBPlanInfo = (FlightAWBPlanInfo) getIntent().getSerializableExtra("checksure");

        setTextViewValue();
    }

    // 给控件赋值
    private void setTextViewValue() {
        String mawb = flightAWBPlanInfo.getMawb();
        if (mawb != null && !mawb.equals("")) {
            checkPlanMawbDetailTv.setText(mawb);
        } else {
            checkPlanMawbDetailTv.setText("");
        }
        String awbType = flightAWBPlanInfo.getAwbType();
        if (awbType != null && !awbType.equals("")) {
            checkPlanShenpiDetailTv.setText(awbType);
        } else {
            checkPlanShenpiDetailTv.setText("");
        }
        String cstatus = flightAWBPlanInfo.getcStatus();
        if (cstatus != null && !cstatus.equals("")) {
            checkPlanRukuzhuangtaiDetailTv.setText(cstatus);
        } else {
            checkPlanRukuzhuangtaiDetailTv.setText("");
        }
        String agentCode = flightAWBPlanInfo.getAgentCode();
        if (agentCode != null && !agentCode.equals("")) {
            checkPlanDlrdmDetailTv.setText(agentCode);
        } else {
            checkPlanDlrdmDetailTv.setText("");
        }
        String agentName = flightAWBPlanInfo.getAgentName();
        if (agentName != null && !agentName.equals("")) {
            checkPlanDlrmcDetailTv.setText(agentName);
        } else {
            checkPlanDlrmcDetailTv.setText("");
        }
        String flightChecked = flightAWBPlanInfo.getFlightChecked();
        if (flightChecked != null && !flightChecked.equals("")) {
            chaeckPlanSurestateDetailTv.setText(flightChecked);
        } else {
            chaeckPlanSurestateDetailTv.setText("");
        }
        String dest = flightAWBPlanInfo.getDest();
        if (dest != null && !dest.equals("")) {
            checkPlanMudigangDetailTv.setText(dest);
        } else {
            checkPlanMudigangDetailTv.setText("");
        }
        String pc = flightAWBPlanInfo.getPC();
        if (pc != null && !pc.equals("")) {
            checkPlanPcDetailTv.setText(pc);
        } else {
            checkPlanPcDetailTv.setText("");
        }
        String weight = flightAWBPlanInfo.getWeight();
        if (weight != null && !weight.equals("")) {
            checkPlanWeightDetailTv.setText(weight);
        } else {
            checkPlanWeightDetailTv.setText("");
        }
        String volume = flightAWBPlanInfo.getVolume();
        if (volume != null && !volume.equals("")) {
            checkPlanVolumeDetailTv.setText(volume);
        } else {
            checkPlanVolumeDetailTv.setText("");
        }
        String goods = flightAWBPlanInfo.getGoods();
        if (goods != null && !goods.equals("")) {
            checkPlanGoodsDetailTv.setText(goods);
        } else {
            checkPlanGoodsDetailTv.setText("");
        }
        String fdate = flightAWBPlanInfo.getFDate();
        if (fdate != null && !fdate.equals("")) {
            checkPlanFdateDetailTv.setText(fdate);
        } else {
            checkPlanFdateDetailTv.setText("");
        }
        String fno = flightAWBPlanInfo.getFno();
        if (fno != null && !fno.equals("")) {
            checkPlanFnoDetailTv.setText(fno);
        } else {
            checkPlanFnoDetailTv.setText("");
        }
        String checkID = flightAWBPlanInfo.getCheckID();
        if (checkID != null && !checkID.equals("")) {
            checkPlanUserDetailTv.setText(checkID);
        } else {
            checkPlanUserDetailTv.setText("");
        }
        String checkTime = flightAWBPlanInfo.getCheckTime();
        if (checkTime != null && !checkTime.equals("")) {
            checkPlanDetailTimeTv.setText(checkTime);
        } else {
            checkPlanDetailTimeTv.setText("");
        }
    }
}
