package com.example.administrator.aviation.ui.activity.domflightcheck;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.aviation.R;
import com.example.administrator.aviation.http.HttpCommons;
import com.example.administrator.aviation.http.HttpRoot;
import com.example.administrator.aviation.model.domjcgrbb.FlightAWBPlanInfo;
import com.example.administrator.aviation.model.domjcgrbb.FlightCheckInfo;
import com.example.administrator.aviation.model.domjcgrbb.FlightPlanInfo;
import com.example.administrator.aviation.model.domjcgrbb.PrepareceFlightAWBPlanInfo;
import com.example.administrator.aviation.sys.PublicFun;
import com.example.administrator.aviation.ui.base.NavBar;
import com.example.administrator.aviation.ui.cgo.domestic.ActLiHuoXinZenPinBan;
import com.example.administrator.aviation.util.AviationCommons;
import com.qmuiteam.qmui.qqface.IQMUIQQFaceManager;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;

import org.ksoap2.serialization.SoapObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.R.attr.color;
import static android.R.attr.key;
import static android.R.attr.name;

/**
 * 订舱确认界面
 */

public class DomFlightCheckSureActivity extends Activity implements View.OnClickListener{
    @BindView(R.id.flight_check_sure_nodata_tv)
    TextView flightCheckSureNodataTv;
    @BindView(R.id.flight_check_sure_lv)
    ListView flightCheckSureLv;
    @BindView(R.id.zhixian_pb)
    ProgressBar zhixianPb;
    @BindView(R.id.flight_check_sure_btn)
    Button flightCheckSureBtn;
    @BindView(R.id.flight_check_cancel_btn)
    Button flightCheckCancelBtn;
    @BindView(R.id.flight_VolumnCheck_btn)
    Button VolumnCheck_Btn;

    private FlightCheckInfo flightCheckInfo;
    private FlightPlanInfo VolumnCheckedInfo;
    private List<FlightAWBPlanInfo> flightAWBPlanInfoList;
    private MyAWBPlanAdapter myAWBPlanAdapter;

    private Map<Integer, FlightPlanInfo> choseMap;

    private String xml;
    private Context mContext;
    private Activity mAct;
    private int mCurrentDialogStyle = com.qmuiteam.qmui.R.style.QMUI_Dialog;
    private QMUITipDialog tipDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appintflight_check_sure);
        mContext = DomFlightCheckSureActivity.this;
        mAct = (Activity) mContext;
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        NavBar navBar = new NavBar(this);
        navBar.setTitle("订舱确认");
        navBar.hideRight();

        // 按钮点击事件
        flightCheckSureBtn.setOnClickListener(this);
        flightCheckCancelBtn.setOnClickListener(this);
        VolumnCheck_Btn.setOnClickListener(this);

        choseMap = new HashMap<>();

        // 首次进入得到数据
        showData();

        // 订舱确认列表点击到详情页
        flightCheckSureLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent intent = new Intent(DomFlightCheckSureActivity.this, DomFlightCheckSureDetailActivity.class);
                FlightAWBPlanInfo flightAWBPlanInfo = flightAWBPlanInfoList.get(position);
                Bundle bundle = new Bundle();
                bundle.putSerializable("checksure", flightAWBPlanInfo);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            // 订舱确认
            case R.id.flight_check_sure_btn:
                Set<Integer> keys = choseMap.keySet();
                for (Integer key:keys) {
                    FlightPlanInfo flightPlanInfo = choseMap.get(key);
                    String fdate = flightCheckInfo.getFDate();
                    String fno = flightCheckInfo.getFno();
                    String istrue = "1";
                    xml = getsureCancelXml(flightPlanInfo, fdate, fno, istrue);
                    if (choseMap.isEmpty()) {
                        Toast.makeText(this, "没有选择", Toast.LENGTH_SHORT).show();
                    } else {
                        Map<String, String> checkSureMap = new HashMap<>();
                        checkSureMap.put("awbXml", xml);
                        checkSureMap.put("ErrString", "");
                        HttpRoot.getInstance().requstAync(DomFlightCheckSureActivity.this, HttpCommons.CGO_DOM_EXPORT_FLIGHT_PLAN_CHECK_NAME,
                                HttpCommons.CGO_DOM_EXPORT_FLIGHT_PLAN_CHECK_ACTION, checkSureMap, new HttpRoot.CallBack() {
                                    @Override
                                    public void onSucess(Object result) {
                                        SoapObject soapObject = (SoapObject) result;
                                        String a = soapObject.getProperty(0).toString();
                                        Toast.makeText(DomFlightCheckSureActivity.this, a, Toast.LENGTH_SHORT).show();
                                        showData();
                                    }

                                    @Override
                                    public void onFailed(String message) {

                                    }

                                    @Override
                                    public void onError() {

                                    }
                                });
                    }
                }
                break;

            // 订舱取消
            case R.id.flight_check_cancel_btn:
                Set<Integer> keyes = choseMap.keySet();
                for (Integer key:keyes) {
                    FlightPlanInfo flightPlanInfo = choseMap.get(key);
                    String fdate = flightCheckInfo.getFDate();
                    String fno = flightCheckInfo.getFno();
                    String isTru = "0";
                    xml = getsureCancelXml(flightPlanInfo, fdate, fno, isTru);
                    if (choseMap.isEmpty()) {
                        Toast.makeText(this, "没有选择", Toast.LENGTH_SHORT).show();
                    } else {
                        Map<String, String> checkSureMap = new HashMap<>();
                        checkSureMap.put("awbXml", xml);
                        checkSureMap.put("ErrString", "");
                        HttpRoot.getInstance().requstAync(DomFlightCheckSureActivity.this, HttpCommons.CGO_DOM_EXPORT_FLIGHT_PLAN_CHECK_NAME,
                                HttpCommons.CGO_DOM_EXPORT_FLIGHT_PLAN_CHECK_ACTION, checkSureMap, new HttpRoot.CallBack() {
                                    @Override
                                    public void onSucess(Object result) {
                                        SoapObject soapObject = (SoapObject) result;
                                        String a = soapObject.getProperty(0).toString();
                                        Toast.makeText(DomFlightCheckSureActivity.this, a, Toast.LENGTH_SHORT).show();
                                        showData();
                                    }

                                    @Override
                                    public void onFailed(String message) {

                                    }

                                    @Override
                                    public void onError() {

                                    }
                                });
                    }
                }
                break;

            case R.id.flight_VolumnCheck_btn:

                if (choseMap.size() == 1) {
                    final QMUIDialog.EditTextDialogBuilder builder = new QMUIDialog.EditTextDialogBuilder(mAct);
                    Set<Integer> keyInts = choseMap.keySet();

                    for (Integer key:keyInts) {
                        VolumnCheckedInfo = choseMap.get(key);

                        if (VolumnCheckedInfo.getCstatus().equals("入库")) {
                            Toast.makeText(this, "已入库货物不可操作！", Toast.LENGTH_SHORT).show();
                        } else {
                            builder.setTitle("舱位体积")
                                    .setDefaultText(VolumnCheckedInfo.getVolume())
                                    .setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL)
                                    .addAction("取消", new QMUIDialogAction.ActionListener() {
                                        @Override
                                        public void onClick(QMUIDialog dialog, int index) {
//                                            mHandler.postDelayed(new Runnable() {
//                                                @Override
//                                                public void run() {
//                                                    PublicFun.KeyBoardHide(mAct, mContext);
//                                                }
//                                            }, 100);

                                            dialog.dismiss();
                                        }
                                    })
                                    .addAction("确定", new QMUIDialogAction.ActionListener() {
                                        @Override
                                        public void onClick(QMUIDialog dialog, int index) {
                                            CharSequence text = builder.getEditText().getText();
                                            if (text != null && text.length() > 0) {
                                                VolumnCheckedInfo.setVolume(text.toString());
                                                UpdateVolumn();
                                                dialog.dismiss();
                                            } else {
                                                Toast.makeText(mAct, "请填入体积", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    })
                                    .create(mCurrentDialogStyle).show();

                            builder.getEditText().setSelection(VolumnCheckedInfo.getVolume().length());
                        }
                    }
                } else {
                    Toast.makeText(this, "请单选数据批复体积！", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    //region 处理UI的线程
    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {

            return false;
        }
    });
    //endregion

    //region 适配器
    private class MyAWBPlanAdapter extends BaseAdapter {
        private Context context;
        private List<FlightAWBPlanInfo> flightAWBPlanInfoList;
        public MyAWBPlanAdapter(Context context, List<FlightAWBPlanInfo> flightAWBPlanInfoList) {
            this.context = context;
            this.flightAWBPlanInfoList = flightAWBPlanInfoList;
        }

        @Override
        public int getCount() {
            return flightAWBPlanInfoList.size();
        }

        @Override
        public FlightAWBPlanInfo getItem(int position) {
            return flightAWBPlanInfoList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup viewGroup) {
            final FlightAWBPlanInfo item = getItem(position);
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.flight_check_sure_item, viewGroup, false);
                viewHolder = new ViewHolder();
                viewHolder.planCb = (CheckBox) convertView.findViewById(R.id.flight_plan_checkbox);
                viewHolder.mawbTv = (TextView) convertView.findViewById(R.id.flight_plan_mawb_tv);
                viewHolder.nameTv = (TextView) convertView.findViewById(R.id.flight_plan_name_tv);
                viewHolder.pcTv = (TextView) convertView.findViewById(R.id.flight_plan_pc_tv);
                viewHolder.weightTv = (TextView) convertView.findViewById(R.id.flight_plan_weight_tv);
                viewHolder.volumeTv = (TextView) convertView.findViewById(R.id.flight_plan_volume_tv);
                viewHolder.userTv = (TextView) convertView.findViewById(R.id.flight_plan_user_tv);
                viewHolder.timeTv = (TextView) convertView.findViewById(R.id.flight_plan_time_tv);
                viewHolder.sureStateTv = (TextView) convertView.findViewById(R.id.flight_plan_surestate_tv);
                viewHolder.showLy = (LinearLayout) convertView.findViewById(R.id.awb_linear_layout);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            String mawb = flightAWBPlanInfoList.get(position).getMawb();
            String status = flightAWBPlanInfoList.get(position).getcStatus();

            if (mawb != null && !mawb.equals("")) {
                viewHolder.mawbTv.setText(mawb.split("-")[0] + "-" + "\n" + mawb.split("-")[1]);

                if (status.equals("入库")) {
                    viewHolder.mawbTv.setTextColor(Color.BLUE);
                }
            } else {
                viewHolder.mawbTv.setText("");
            }

            String name = flightAWBPlanInfoList.get(position).getAgentName();
            if (name != null && !name.equals("")) {
                viewHolder.nameTv.setText(name);
            } else {
                viewHolder.nameTv.setText("");
            }
            String pc = flightAWBPlanInfoList.get(position).getPC();
            if (pc != null && !pc.equals("")) {
                viewHolder.pcTv.setText(pc);
            } else {
                viewHolder.pcTv.setText("");
            }
            String weight = flightAWBPlanInfoList.get(position).getWeight();
            if (weight != null && !weight.equals("")) {
                viewHolder.weightTv.setText(weight);
            } else {
                viewHolder.weightTv.setText("");
            }
            String volume = flightAWBPlanInfoList.get(position).getVolume();
            if (volume != null && !volume.equals("")) {
                viewHolder.volumeTv.setText(volume);
            } else {
                viewHolder.volumeTv.setText("");
            }
            String user = flightAWBPlanInfoList.get(position).getCheckID();
            if (user != null && !user.equals("")) {
                viewHolder.userTv.setText(user);
            } else {
                viewHolder.userTv.setText("");
            }
            String time = flightAWBPlanInfoList.get(position).getCheckTime();
            if (time != null && !time.equals("")) {
                viewHolder.timeTv.setText(time);
            } else {
                viewHolder.timeTv.setText("");
            }
            String sureState = flightAWBPlanInfoList.get(position).getFlightChecked();
            if (sureState != null && !sureState.equals("")) {
                viewHolder.sureStateTv.setText(sureState);
                if (sureState.equals("1")) {
                    viewHolder.showLy.setBackgroundColor(Color.parseColor("#fbd0cf"));
                }
            } else {
                viewHolder.sureStateTv.setText("");
            }



            viewHolder.planCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b) {
                        FlightPlanInfo info = new FlightPlanInfo(item.getFDate(),item.getFno(),item.getFlightChecked(),item.getMawb(),item.getVolume(),item.getcStatus());
                        choseMap.put(position, info);
                    } else {
                        choseMap.remove(position);
                    }
                }
            });
            return convertView;
        }

        class ViewHolder {
            CheckBox planCb;
            TextView mawbTv;
            TextView nameTv;
            TextView pcTv;
            TextView weightTv;
            TextView volumeTv;
            TextView userTv;
            TextView timeTv;
            TextView sureStateTv;
            LinearLayout showLy;
        }
    }
    //endregion

    // 请求数据信息
    private String getXml(String fdate, String fno, String jTg, String fDest) {
        return "<GNCAWBPlan>"
                + "<FDate>" + fdate + "</FDate>"
                + "<Fno>" + fno + "</Fno>"
                + " <JTZ>" + jTg + "</JTZ>"
                + "  <FDest>" + fDest + "</FDest>"
                + "</GNCAWBPlan>";
    }

    // 订舱确认与取消订舱xml
    private String getsureCancelXml(FlightPlanInfo flightPlanInfo, String fdate, String fno, String isTrue) {
        String pre = "<GNCAWBPlan>";
        String after ="</GNCAWBPlan>";
        String result = pre;
            result += "<FDate>"+fdate+"</FDate>"
                    +"<Fno>" + fno + "</Fno>"
                    + "<FlightChecked>"+isTrue+"</FlightChecked>"
                    +"<Mawb>" + flightPlanInfo.getMawb() + "</Mawb>";
        result = result+after;
        return result;
    }

    private void MawbChecked() {
        String fdate = flightCheckInfo.getFDate();
        String fno = flightCheckInfo.getFno();
        String istrue = "1";
        xml = getsureCancelXml(VolumnCheckedInfo, fdate, fno, istrue);

        Map<String, String> checkSureMap = new HashMap<>();
        checkSureMap.put("awbXml", xml);
        checkSureMap.put("ErrString", "");
        HttpRoot.getInstance().requstAync(DomFlightCheckSureActivity.this, HttpCommons.CGO_DOM_EXPORT_FLIGHT_PLAN_CHECK_NAME,
                HttpCommons.CGO_DOM_EXPORT_FLIGHT_PLAN_CHECK_ACTION, checkSureMap, new HttpRoot.CallBack() {
                    @Override
                    public void onSucess(Object result) {
                        SoapObject soapObject = (SoapObject) result;
                        Boolean re = Boolean.parseBoolean(soapObject.getProperty(0).toString());

                        if (re) {
                            tipDialog = new QMUITipDialog.Builder(mContext)
                                    .setIconType(QMUITipDialog.Builder.ICON_TYPE_SUCCESS)
                                    .setTipWord("操作成功")
                                    .create();

                            tipDialog.show();
                            mHandler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    tipDialog.dismiss();
                                }
                            }, 1500);
                        }

                        showData();
                    }

                    @Override
                    public void onFailed(String message) {

                    }

                    @Override
                    public void onError() {

                    }
                });

    }


    private void UpdateVolumn(){
        Map<String, String> checkSureMap = new HashMap<>();
        checkSureMap.put("Mawb", VolumnCheckedInfo.getMawb().replaceAll("-",""));
        checkSureMap.put("Volume", VolumnCheckedInfo.getVolume());

        HttpRoot.getInstance().requstAync(DomFlightCheckSureActivity.this, HttpCommons.CGO_AWBVolume_Checked_NAME,
                HttpCommons.CGO_AWBVolume_Checked_ACTION, checkSureMap, new HttpRoot.CallBack() {
                    @Override
                    public void onSucess(Object result) {
                        SoapObject soapObject = (SoapObject) result;
                        Boolean re = Boolean.parseBoolean(soapObject.getProperty(0).toString());

                        if (re) {
                            MawbChecked();
                        }
                    }

                    @Override
                    public void onFailed(String message) {

                    }

                    @Override
                    public void onError() {

                    }
                });
    }
    // 展示订舱确认数据界面
    private void showData() {
        // 得到待确认航班计划列表传递过来的数据
        flightCheckInfo = (FlightCheckInfo) getIntent().getSerializableExtra("domflightsure");
        String fDate = flightCheckInfo.getFDate();
        String fno = flightCheckInfo.getFno();
        String jTg = flightCheckInfo.getJTZ();
        String fDest = flightCheckInfo.getFDest();
        String xml = getXml(fDate, fno, jTg, fDest);
        // 解析数据
        Map<String, String> params = new HashMap<>();
        params.put("awbXml", xml);
        params.put("ErrString", "");
        HttpRoot.getInstance().requstAync(DomFlightCheckSureActivity.this, HttpCommons.CGO_GET_DOM_FLIGHT_AWB_PLAN_NAME,
                HttpCommons.CGO_GET_DOM_FLIGHT_AWB_PLAN_NACTION, params, new HttpRoot.CallBack() {
                    @Override
                    public void onSucess(Object result) {
                        zhixianPb.setVisibility(View.VISIBLE);
                        SoapObject object = (SoapObject) result;
                        String awbXml = object.getProperty(0).toString();
                        flightAWBPlanInfoList = PrepareceFlightAWBPlanInfo.parseFlightAWBPlanInfoXml(awbXml);
                        assert flightAWBPlanInfoList != null;
                        if (flightAWBPlanInfoList.size() >= 1) {
                            myAWBPlanAdapter = new MyAWBPlanAdapter(DomFlightCheckSureActivity.this, flightAWBPlanInfoList);
                            flightCheckSureLv.setAdapter(myAWBPlanAdapter);
                            zhixianPb.setVisibility(View.GONE);
                        } else {
                            zhixianPb.setVisibility(View.GONE);
                            flightCheckSureNodataTv.setVisibility(View.VISIBLE);
                        }

                    }

                    @Override
                    public void onFailed(String message) {
                        zhixianPb.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError() {
                        zhixianPb.setVisibility(View.GONE);
                    }
                });
    }
}
