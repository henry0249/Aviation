package com.example.administrator.aviation.ui.activity.domandintgetflight;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.administrator.aviation.R;
import com.example.administrator.aviation.model.intanddomflight.FlightMessage;
import com.example.administrator.aviation.ui.base.NavBar;
import com.example.administrator.aviation.util.AviationCommons;
import com.example.administrator.aviation.util.AviationNoteConvert;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 航班动态详情页
 */

public class FlightDetailActivity extends Activity {
    @BindView(R.id.flight_date)
    TextView flightDate;
    @BindView(R.id.flight_fno)
    TextView flightFno;
    @BindView(R.id.flight_fdep)
    TextView flightFdep;
    @BindView(R.id.flight_jtg)
    TextView flightJtg;
    @BindView(R.id.fdest)
    TextView fdest;
    @BindView(R.id.flight_type)
    TextView flightType;
    @BindView(R.id.flight_country)
    TextView flightCountry;
    @BindView(R.id.flight_etd)
    TextView flightEtd;
    @BindView(R.id.flight_atd)
    TextView flightAtd;
    @BindView(R.id.flight_eta)
    TextView flightEta;
    @BindView(R.id.flight_ata)
    TextView flightAta;
    @BindView(R.id.flight_registeration)
    TextView flightRegisteration;
    @BindView(R.id.flight_aircraftcode)
    TextView flightAircraftcode;
    @BindView(R.id.flight_standid)
    TextView flightStandid;
    @BindView(R.id.flight_status)
    TextView flightStatus;
    @BindView(R.id.flight_flightterminalid)
    TextView flightFlightterminalid;
    @BindView(R.id.flight_delayfreetext)
    TextView flightDelayfreetext;

    private FlightMessage flightMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flight_detail);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        NavBar navBar = new NavBar(this);
        navBar.setTitle("航班动态详情");
        navBar.hideRight();
        flightMessage = (FlightMessage) getIntent().getSerializableExtra(AviationCommons.FLIGHT_DETAIL);
        setText();
    }

    private void setText() {
        flightDate.setText(flightMessage.getfDate());
        flightFno.setText(flightMessage.getFno());
        flightFdep.setText(flightMessage.getfDep());
        flightJtg.setText(flightMessage.getJtz());
        fdest.setText(flightMessage.getfDest());

        // 类型
        String flightTypes = flightMessage.getServiceType();
        flightTypes = AviationNoteConvert.enTocn(flightTypes);
        flightType.setText(flightTypes);

        // 地区
        String flightdq = flightMessage.getCountryType();
        flightdq = AviationNoteConvert.enTocn(flightdq);
        flightCountry.setText(flightdq);

        flightEtd.setText(flightMessage.getEstimatedTakeOff());
        flightAtd.setText(flightMessage.getActualTakeOff());
        flightEta.setText(flightMessage.getEstimatedArrival());
        flightAta.setText(flightMessage.getActualArrival());
        flightRegisteration.setText(flightMessage.getRegisteration());
        flightAircraftcode.setText(flightMessage.getAircraftCode());
        flightStandid.setText(flightMessage.getStandID());

        String flightStau = flightMessage.getFlightStatus();
        flightStau = AviationNoteConvert.statusCntoEn(flightStau);
        flightStatus.setText(flightStau);

        flightFlightterminalid.setText(flightMessage.getFlightTerminalID());
        flightDelayfreetext.setText(flightMessage.getDelayFreeText());
    }
}
