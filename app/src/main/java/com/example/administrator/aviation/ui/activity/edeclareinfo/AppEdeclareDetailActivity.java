package com.example.administrator.aviation.ui.activity.edeclareinfo;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.administrator.aviation.R;
import com.example.administrator.aviation.model.edeclareinfo.EdeclareInfo;
import com.example.administrator.aviation.ui.base.NavBar;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 国际承运人联检状态列表详情
 */

public class AppEdeclareDetailActivity extends Activity {
    @BindView(R.id.edeclareinfo_mawb_detail_tv)
    TextView edeclareinfoMawbDetailTv;
    @BindView(R.id.edeclareinfo_carrier_detail_tv)
    TextView edeclareinfoCarrierDetailTv;
    @BindView(R.id.edeclareinfo_businessName_detail_tv)
    TextView edeclareinfoBusinessNameDetailTv;
    @BindView(R.id.declareinfo_ciqstatus_detail_tv)
    TextView declareinfoCiqstatusDetailTv;
    @BindView(R.id.edeclareinfo_cmdstatus_detail_tv)
    TextView edeclareinfoCmdstatusDetailTv;
    @BindView(R.id.edeclareinfo_agentcode_detail_tv)
    TextView edeclareinfoAgentcodeDetailTv;
    @BindView(R.id.declareinfo_agentname_detail_tv)
    TextView declareinfoAgentnameDetailTv;
    @BindView(R.id.edeclareinfo_papertime_detail_tv)
    TextView edeclareinfoPapertimeDetailTv;
    @BindView(R.id.edeclareinfo_fDate_detail_tv)
    TextView edeclareinfoFDateDetailTv;
    @BindView(R.id.edeclareinfo_fno_detail_tv)
    TextView edeclareinfoFnoDetailTv;
    @BindView(R.id.declareinfo_totalpc_detail_tv)
    TextView declareinfoTotalpcDetailTv;
    @BindView(R.id.edeclareinfo_pc_detail_tv)
    TextView edeclareinfoPcDetailTv;
    @BindView(R.id.edeclareinfo_weight_detail_tv)
    TextView edeclareinfoWeightDetailTv;
    @BindView(R.id.edeclareinfo_volume_detail_tv)
    TextView edeclareinfoVolumeDetailTv;
    @BindView(R.id.edeclareinfo_dest_detail_tv)
    TextView edeclareinfoDestDetailTv;
    @BindView(R.id.edeclareinfo_goodscn_tv)
    TextView edeclareinfoGoodscnTv;

    private EdeclareInfo edeclareInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appedecalre_detail);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        NavBar navBar = new NavBar(this);
        navBar.setTitle("联检状态列表详情");
        navBar.hideRight();

        // 得到上层界面返回值
        edeclareInfo = (EdeclareInfo) getIntent().getSerializableExtra("edeclareInfo");
        setText();
    }

    // 给控件赋值
    private void setText() {
        edeclareinfoMawbDetailTv.setText(edeclareInfo.getMawb());
        edeclareinfoCarrierDetailTv.setText(edeclareInfo.getCarrier());
        edeclareinfoBusinessNameDetailTv.setText(edeclareInfo.getBusinessName());
        declareinfoCiqstatusDetailTv.setText(edeclareInfo.getCIQStatus());
        edeclareinfoCmdstatusDetailTv.setText(edeclareInfo.getCMDStatus());
        edeclareinfoAgentcodeDetailTv.setText(edeclareInfo.getAgentCode());
        declareinfoAgentnameDetailTv.setText(edeclareInfo.getAgentName());
        edeclareinfoPapertimeDetailTv.setText(edeclareInfo.getPaperTime());
        edeclareinfoFDateDetailTv.setText(edeclareInfo.getFDate());
        edeclareinfoFnoDetailTv.setText(edeclareInfo.getFno());
        declareinfoTotalpcDetailTv.setText(edeclareInfo.getTotalPC());
        edeclareinfoPcDetailTv.setText(edeclareInfo.getPC());
        edeclareinfoWeightDetailTv.setText(edeclareInfo.getWeight());
        edeclareinfoVolumeDetailTv.setText(edeclareInfo.getVolume());
        edeclareinfoDestDetailTv.setText(edeclareInfo.getDest());
        edeclareinfoGoodscnTv.setText(edeclareInfo.getGoodsCN());
    }
}
