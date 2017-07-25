package com.example.administrator.aviation.ui.activity.domflightcheck;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.administrator.aviation.R;
import com.example.administrator.aviation.model.domjcgrbb.FlightCheckInfo;
import com.example.administrator.aviation.ui.base.NavBar;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 订舱计划详情页
 */

public class DomFlightCheckInDetailActivity extends Activity {
    @BindView(R.id.check_carrier_detail_tv)
    TextView checkCarrierDetailTv;
    @BindView(R.id.check_fdate_detail_tv)
    TextView checkFdateDetailTv;
    @BindView(R.id.check_fno_detail_tv)
    TextView checkFnoDetailTv;
    @BindView(R.id.check_registeration_detail_tv)
    TextView checkRegisterationDetailTv;
    @BindView(R.id.check_aircraftCode_detail_tv)
    TextView checkAircraftCodeDetailTv;
    @BindView(R.id.check_fdest_detail_tv)
    TextView checkFdestDetailTv;
    @BindView(R.id.chaeck_jtg_detail_tv)
    TextView chaeckJtgDetailTv;
    @BindView(R.id.check_yjqf_detail_tv)
    TextView checkYjqfDetailTv;
    @BindView(R.id.check_maxweight_detail_tv)
    TextView checkMaxweightDetailTv;
    @BindView(R.id.check_maxvolume_detail_tv)
    TextView checkMaxvolumeDetailTv;
    @BindView(R.id.check_useableweight_detail_tv)
    TextView checkUseableweightDetailTv;
    @BindView(R.id.check_useablevolume_detail_tv)
    TextView checkUseablevolumeDetailTv;
    @BindView(R.id.check_waitcheckinweight_detail_tv)
    TextView checkWaitcheckinweightDetailTv;
    @BindView(R.id.check_waitcheckinvolume_detail_tv)
    TextView checkWaitcheckinvolumeDetailTv;
    @BindView(R.id.check_lahuo_weight_detail_tv)
    TextView checkLahuoWeightDetailTv;
    @BindView(R.id.check_lahuo_volume_tv)
    TextView checkLahuoVolumeTv;
    @BindView(R.id.check_sure_weight_detail_tv)
    TextView checkSureWeightDetailTv;
    @BindView(R.id.check_sure_volume_tv)
    TextView checkSureVolumeTv;

    private FlightCheckInfo flightCheckInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dom_flight_check_detail);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        NavBar navBar = new NavBar(this);
        navBar.setTitle("待确认航班计划详情");
        navBar.hideRight();
        flightCheckInfo = (FlightCheckInfo) getIntent().getSerializableExtra("domflight");
        setTextValue();
    }

    private void setTextValue() {
       String carrier = flightCheckInfo.getCarrier();
        if (carrier != null && !carrier.equals("")) {
            checkCarrierDetailTv.setText(carrier);
        } else {
            checkCarrierDetailTv.setText("");
        }

        String fdate = flightCheckInfo.getFDate();
        if (fdate != null && !fdate.equals("")) {
            checkFdateDetailTv.setText(fdate);
        } else {
            checkFdateDetailTv.setText("");
        }

        String fno = flightCheckInfo.getFno();
        if (fno != null && !fno.equals("")) {
            checkFnoDetailTv.setText(fno);
        } else {
            checkFnoDetailTv.setText("");
        }

        String registeration = flightCheckInfo.getRegisteration();
        if (registeration != null && !registeration.equals("")) {
            checkRegisterationDetailTv.setText(registeration);
        } else {
            checkRegisterationDetailTv.setText("");
        }

        String aircraftCode = flightCheckInfo.getAircraftCode();
        if (aircraftCode != null && !aircraftCode.equals("")) {
            checkAircraftCodeDetailTv.setText(aircraftCode);
        } else {
            checkAircraftCodeDetailTv.setText("");
        }

        String fDest = flightCheckInfo.getFDest();
        if (fDest != null && !fDest.equals("")) {
            checkFdestDetailTv.setText(fDest);
        } else {
            checkFdestDetailTv.setText("");
        }

        String jTg = flightCheckInfo.getJTZ();
        if (jTg != null && !jTg.equals("")) {
            chaeckJtgDetailTv.setText(jTg);
        } else {
            chaeckJtgDetailTv.setText("");
        }

        String estimatedTakeOff = flightCheckInfo.getEstimatedTakeOff();
        if (estimatedTakeOff != null && !estimatedTakeOff.equals("")) {
            checkYjqfDetailTv.setText(estimatedTakeOff);
        } else {
            checkYjqfDetailTv.setText("");
        }

        String maxWeight = flightCheckInfo.getMaxWeight();
        if (maxWeight != null && !maxWeight.equals("")) {
            checkMaxweightDetailTv.setText(maxWeight);
        } else {
            checkMaxweightDetailTv.setText("");
        }

        String maxVolume = flightCheckInfo.getMaxVolume();
        if (maxVolume != null && !maxVolume.equals("")) {
            checkMaxvolumeDetailTv.setText(maxVolume);
        } else {
            checkMaxvolumeDetailTv.setText("");
        }

        String useableWeight = flightCheckInfo.getUseableWeight();
        if (useableWeight != null && !useableWeight.equals("")) {
            checkUseableweightDetailTv.setText(useableWeight);
        } else {
            checkUseableweightDetailTv.setText("");
        }

        String useableVolume = flightCheckInfo.getUseableVolume();
        if (useableVolume != null && !useableVolume.equals("")) {
            checkUseablevolumeDetailTv.setText(useableVolume);
        } else {
            checkUseablevolumeDetailTv.setText("");
        }

        String waitCheckInWeight = flightCheckInfo.getWaitCheckInWeight();
        if (waitCheckInWeight != null && !waitCheckInWeight.equals("")) {
            checkWaitcheckinweightDetailTv.setText(waitCheckInWeight);
        } else {
            checkWaitcheckinweightDetailTv.setText("");
        }

        String waitCheckInVolume = flightCheckInfo.getWaitCheckInVolume();
        if (waitCheckInVolume != null && !waitCheckInVolume.equals("")) {
            checkWaitcheckinvolumeDetailTv.setText(waitCheckInVolume);
        } else {
            checkWaitcheckinvolumeDetailTv.setText("");
        }

        String delTransWeight = flightCheckInfo.getDelTransWeight();
        if (delTransWeight != null && !delTransWeight.equals("")) {
            checkLahuoWeightDetailTv.setText(delTransWeight);
        } else {
            checkLahuoWeightDetailTv.setText("");
        }

        String delTransVolume = flightCheckInfo.getDelTransVolume();
        if (delTransVolume != null && !delTransVolume.equals("")) {
            checkLahuoVolumeTv.setText(delTransVolume);
        } else {
            checkLahuoVolumeTv.setText("");
        }

        String inWeight = flightCheckInfo.getInWeight();
        if (inWeight != null && !inWeight.equals("")) {
            checkSureWeightDetailTv.setText(inWeight);
        } else {
            checkSureWeightDetailTv.setText("");
        }

        String inVolume = flightCheckInfo.getInVolume();
        if (inVolume != null && !inVolume.equals("")) {
            checkSureVolumeTv.setText(inVolume);
        } else {
            checkSureVolumeTv.setText("");
        }
    }
}
