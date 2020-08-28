package com.example.administrator.aviation.ui.cgo.gnj;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.aviation.R;
import com.example.administrator.aviation.http.HttpCommons;
import com.example.administrator.aviation.http.HttpRoot;
import com.example.administrator.aviation.model.adapter.AbsCommonAdapter;
import com.example.administrator.aviation.model.adapter.AbsViewHolder;
import com.example.administrator.aviation.model.gnj.gnjPickUpConverter;
import com.example.administrator.aviation.model.hygnc.GncFlightControl;
import com.example.administrator.aviation.model.hygnc.ParseGncFlightControl;
import com.example.administrator.aviation.sys.PublicFun;
import com.example.administrator.aviation.ui.base.NavBar;
import com.example.administrator.aviation.ui.base.SyncHorizontalScrollView;
import com.example.administrator.aviation.ui.base.TableModel;
import com.example.administrator.aviation.ui.cgo.domestic.JinChengGuanKong;
import com.example.administrator.aviation.ui.cgo.domestic.ZhuangJiDanMain;
import com.example.administrator.aviation.ui.dialog.LoadingDialog;
import com.example.administrator.aviation.util.AviationCommons;
import com.example.administrator.aviation.model.gnj.gnjPickUpModel;
import com.example.administrator.aviation.util.ToastUtils;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;

import org.ksoap2.serialization.SoapObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class gnjPickUpInfoActivity extends AppCompatActivity {

    //region 自定义变量
    private Context mContext;
    private Activity mAct;
    private NavBar navBar;
    private final String page = "one";
    private List<gnjPickUpModel> pickUpModelList;
    //用于存放标题的id,与textview引用
    private SparseArray<TextView> mTitleTvArray;
    private AbsCommonAdapter<TableModel> mLeftAdapter, mRightAdapter;
    private HashMap<String,String> PickUpstore = new HashMap();
    private gnjPickUpModel SearchFlag;
    //endregion

    //region 未预设XML控件
    private QMUITipDialog tipDialog;
    //endregion

    //region 其他控件
    @BindView(R.id.gnjPickUpInfo_left_container_listview)
    ListView leftListView;
    @BindView(R.id.gnjPickUpInfo_right_container_listview)
    ListView rightListView;

    private LoadingDialog Ldialog;
    //endregion

    //region Layout控件

    //endregion

    //region Button控件
    @BindView(R.id.gnjPickUpInfo_Btn_TiQu)
    Button Btn_TiQu;
    @BindView(R.id.gnjPickUpInfo_Btn_lihuoStart)
    Button Btn_lihuoStart;
    @BindView(R.id.gnjPickUpInfo_Btn_lihuoEnd)
    Button Btn_lihuoEnd;
    //endregion

    //region EditText控件

    //endregion

    //region 滚动View控件
    @BindView(R.id.gnjPickUpInfo_pulltorefreshview)
    SwipeRefreshLayout pulltorefreshview;
    @BindView(R.id.gnjPickUpInfo_title_horsv)
    SyncHorizontalScrollView titleHorScv;
    @BindView(R.id.gnjPickUpInfo_content_horsv)
    SyncHorizontalScrollView contentHorScv;
    //endregion

    //region TextView控件
    @BindView(R.id.gnjPickUpInfo_tv_table_title_left)
    TextView txt_RightTitle;
    //endregion

    //region ImgView控件

    //endregion

    //region 初始化

    //region 入口函数
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gnj_pick_up_info);
        mContext = gnjPickUpInfoActivity.this;
        mAct = (Activity) mContext;
        ButterKnife.bind(this);
        init();
    }
    //endregion

    //region 设置初始化
    public void init() {
        navBar = new NavBar(this);
        navBar.setTitle("货物提取");
        navBar.setRight(R.drawable.search_white);

        pickUpModelList = new ArrayList<gnjPickUpModel>();
        SearchFlag = new gnjPickUpModel();

        Ldialog = new LoadingDialog(mContext);
        txt_RightTitle.setText("运单号");

        //设置下拉的距离和动画颜色
        pulltorefreshview.setProgressViewEndTarget (true,100);
        pulltorefreshview.setDistanceToTriggerSync(150);
        pulltorefreshview.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light, android.R.color.holo_orange_light, android.R.color.holo_red_light);

        LayoutInflater layoutInflater = LayoutInflater.from(this);
        LinearLayout linearLayout = (LinearLayout)findViewById(R.id.gnjPickUpInfo_right_title_container);
        layoutInflater.inflate(R.layout.gnjpickup_right_title,linearLayout,true);


        // 设置两个水平控件的联动
        titleHorScv.setScrollView(contentHorScv);
        contentHorScv.setScrollView(titleHorScv);

        findTitleTextViewIds();
        initTableView();
        setListener();
        getFirstInfo();
    }
    //endregion

    //region 首次查询
    private void getFirstInfo() {
        ArrayMap<String, String> re = new ArrayMap<>();
        re.put("Mawb", "");
        re.put("PickFlag", "0");
        re.put("DLVID", "");
        re.put("AgentCode", "");
        re.put("DLVTime", PublicFun.getDateStr("yyyy-MM-dd") + "T00:00:00");
        GetInfo(getPickUpXml(re));
    }
    //endregion

    //region 清空tableview
    private void clearTableView(){
        mLeftAdapter.clearData(true);
        mRightAdapter.clearData(true);
        PickUpstore.clear();
        titleHorScv.scrollTo(0,0);
    }
    //endregion

    //region 类的Intent

    //endregion

    //region 利用反射初始化标题的TextView的item引用
    private void findTitleTextViewIds() {
        mTitleTvArray = new SparseArray<>();
        for (int i = 0; i < 14; i++) {
            try {
                Field field = R.id.class.getField("gnjPickUp_tv_table_title_" + i);
                int key = field.getInt(new R.id());
                TextView textView = (TextView) findViewById(key);

                mTitleTvArray.put(key, textView);
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
    }
    //endregion

    //region 初始化表格的view
    private void initTableView() {
        mLeftAdapter = new AbsCommonAdapter<TableModel>(mContext, R.layout.table_left_item) {
            @Override
            public void convert(AbsViewHolder helper, TableModel item, int pos) {
                TextView tv_table_content_left = helper.getView(R.id.tv_table_content_item_left);
                CheckBox cb = helper.getView(R.id.item_cb);
                tv_table_content_left.setText(item.getLeftTitle());
                cb.setChecked(false);

                if (!TextUtils.isEmpty(item.getText10()) && item.getText10() == "出库中") {
                    tv_table_content_left.setBackgroundColor(Color.parseColor("#FFBB33"));
                } else if (!TextUtils.isEmpty(item.getText10()) && item.getText10() == "待提取") {
                    tv_table_content_left.setBackgroundColor(Color.parseColor("#00FFFF"));
                } else if (!TextUtils.isEmpty(item.getText10()) && item.getText10() == "已提取") {
                    tv_table_content_left.setBackgroundColor(Color.parseColor("#00ff00"));
                } else {
                    tv_table_content_left.setBackgroundColor(Color.parseColor("#ffffff"));
                }
            }
        };

        mRightAdapter = new AbsCommonAdapter<TableModel>(mContext, R.layout.gnjpickup_right_item) {
            @Override
            public void convert(AbsViewHolder helper, final TableModel item, int pos) {
                TextView tv_table_content_right_item0 = helper.getView(R.id.gnjPickUp_tv_table_content_right_item0);
                TextView tv_table_content_right_item1 = helper.getView(R.id.gnjPickUp_tv_table_content_right_item1);
                TextView tv_table_content_right_item2 = helper.getView(R.id.gnjPickUp_tv_table_content_right_item2);
                TextView tv_table_content_right_item3 = helper.getView(R.id.gnjPickUp_tv_table_content_right_item3);
                TextView tv_table_content_right_item4 = helper.getView(R.id.gnjPickUp_tv_table_content_right_item4);
                TextView tv_table_content_right_item5 = helper.getView(R.id.gnjPickUp_tv_table_content_right_item5);
                TextView tv_table_content_right_item6 = helper.getView(R.id.gnjPickUp_tv_table_content_right_item6);
                TextView tv_table_content_right_item7 = helper.getView(R.id.gnjPickUp_tv_table_content_right_item7);
                TextView tv_table_content_right_item8 = helper.getView(R.id.gnjPickUp_tv_table_content_right_item8);
                TextView tv_table_content_right_item9 = helper.getView(R.id.gnjPickUp_tv_table_content_right_item9);
                TextView tv_table_content_right_item10 = helper.getView(R.id.gnjPickUp_tv_table_content_right_item10);
                TextView tv_table_content_right_item11 = helper.getView(R.id.gnjPickUp_tv_table_content_right_item11);
                TextView tv_table_content_right_item12 = helper.getView(R.id.gnjPickUp_tv_table_content_right_item12);
                TextView tv_table_content_right_item13 = helper.getView(R.id.gnjPickUp_tv_table_content_right_item13);

                tv_table_content_right_item0.setText(item.getText0());
                tv_table_content_right_item1.setText(item.getText1());
                tv_table_content_right_item2.setText(item.getText2());
                tv_table_content_right_item3.setText(item.getText3());
                tv_table_content_right_item4.setText(item.getText4());
                tv_table_content_right_item5.setText(item.getText5());
                tv_table_content_right_item6.setText(item.getText6());
                tv_table_content_right_item7.setText(item.getText7());
                tv_table_content_right_item8.setText(item.getText8());
                tv_table_content_right_item9.setText(item.getText9());
                tv_table_content_right_item10.setText(item.getText10());
                tv_table_content_right_item11.setText(item.getText11());
                tv_table_content_right_item12.setText(item.getText12());
                tv_table_content_right_item13.setText(item.getText13());

                for (int i = 0; i < 14; i++) {
                    View view = ((LinearLayout) helper.getConvertView()).getChildAt(i);
                    view.setVisibility(View.VISIBLE);
                }

                tv_table_content_right_item0.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        gnjPickUpModel pickupGo = new gnjPickUpModel();
                        for (gnjPickUpModel pi:pickUpModelList){
                            if (pi.getID().equals(item.getOrgCode())) {
                                pickupGo.setID(pi.getID());
                                pickupGo.setPKID(pi.getPKID());
                                pickupGo.setCHGMode(pi.getCHGMode());
                                pickupGo.setAgentCode(pi.getAgentCode());
                                pickupGo.setAWBPC(pi.getAWBPC());
                                pickupGo.setPC(pi.getPC());
                                pickupGo.setSpCode(pi.getSpCode());
                                pickupGo.setGoods(pi.getGoods());
                                pickupGo.setOrigin(pi.getOrigin());
                                pickupGo.setFDate(pi.getFDate());
                                pickupGo.setFno(pi.getFno());
                                pickupGo.setChargeTime(pi.getChargeTime());
                                pickupGo.setPickFlag(gnjPickUpConverter.SwitchPickUpFlag(pi.getPickFlag()));
                                pickupGo.setDLVTime(pi.getDLVTime());
                                pickupGo.setCNEName(pi.getCNEName());
                                pickupGo.setCNEIDType(pi.getCNEIDType());
                                pickupGo.setCNEID(pi.getCNEID());
                                pickupGo.setCNEPhone(pi.getCNEPhone());
                                pickupGo.setDLVName(pi.getDLVName());
                                pickupGo.setDLVIDType(pi.getDLVIDType());
                                pickupGo.setDLVID(pi.getDLVID());
                                pickupGo.setDLVPhone(pi.getDLVPhone());
                                pickupGo.setREFID(pi.getREFID());
                                break;
                            }
                        }

                        Bundle mBundle = new Bundle();
                        mBundle.putSerializable("Info", pickupGo);
                        Intent intent = new Intent(mContext,gnjPicjUpDetailsActivity.class);
                        intent.putExtras(mBundle);
                        startActivity(intent);
                    }
                });
            }
        };

        leftListView.setAdapter(mLeftAdapter);
        rightListView.setAdapter(mRightAdapter);
    }
    //endregion

    //endregion

    //region 控件事件

    //region 页面上所有的点击事件
    private void setListener() {
        //region 标题栏右边图片点击事件
        navBar.getRightImageView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, gnjPickUpActivity.class);
                intent.putExtra("id", AviationCommons.PickUpSearchActivity_REQUEST);
                startActivityForResult(intent, AviationCommons.PickUpSearchActivity_REQUEST);
            }
        });
        //endregion

        //region 列表左侧点击事件
        leftListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mAct != null){
                    CheckBox checkBox = (CheckBox) view.findViewById(R.id.item_cb);
                    TableModel ta = (TableModel) parent.getItemAtPosition(position);
                    String ke = ta.getOrgCode().toString().trim();
                    if (checkBox.isChecked()) {
                        checkBox.setChecked(false);
                        PickUpstore.remove(ke);
                    } else {
                        checkBox.setChecked(true);
                        boolean flag = PickUpstore.containsKey(ke);
                        if (!flag) {
                            String Mawb = ta.getLeftTitle().toString().replace("P","").trim();
                            String pkid = ta.getText0().trim();
                            String pickF = gnjPickUpConverter.SwitchPickUpFlag(ta.getText10().trim());
                            PickUpstore.put(ke,Mawb + "/" + pkid + "/" + pickF);
                        }
                    }
                }
            }
        });
        //rendregion

        //region 下拉刷新
        pulltorefreshview.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                RefreshList();
            }
        });
        //endregion

        //region 货物提取按钮
        Btn_TiQu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAct != null && PickUpstore.size() > 0) {
                    Boolean pkidFlag = true;
                    Boolean statusFlag = true;

                    int cc = 0;
                    String ori = "";
                    for (String key : PickUpstore.keySet()){
                        if (cc == 0) {
                            ori = PickUpstore.get(key).split("/")[1];
                        } else {
                            if (!ori.equals(PickUpstore.get(key).split("/")[1])) {
                                pkidFlag = false;
                                break;
                            }
                        }

                        if (!"2".equals(PickUpstore.get(key).split("/")[2])) {
                            statusFlag = false;
                            break;
                        }
                        cc += 1;
                    }

                    if (pkidFlag) {
                        if (statusFlag) {
                            Bundle mBundle = new Bundle();
                            mBundle.putSerializable("Info", PickUpstore);
                            mBundle.putInt("id", AviationCommons.PickUpSignatureActivity_REQUEST);

                            Intent intent = new Intent(mContext, PickUpSignatureActivity.class);
                            intent.putExtras(mBundle);
                            startActivityForResult(intent, AviationCommons.PickUpSignatureActivity_REQUEST);
                        }else {
                            ToastUtils.showToast(mContext, "选择的数据集提取状态非待提取!", Toast.LENGTH_SHORT);
                        }

                    } else {
                        ToastUtils.showToast(mContext, "选择的数据集提货编号不一致!", Toast.LENGTH_SHORT);
                    }
                }else {
                    ToastUtils.showToast(mContext, "请先选择数据", Toast.LENGTH_SHORT);
                }
            }
        });
        //endregion

        //region 理货开始按钮
        Btn_lihuoStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAct != null && PickUpstore.size() > 0) {
                    lihuo("1");
//                    String keys = "";
//                    for (String key : PickUpstore.keySet()){
//                        keys += key + " ";
//                    }
//                    new QMUIDialog.MessageDialogBuilder(mAct)
//                            .setTitle("理货开始")
//                            .setMessage(keys)
//                            .addAction("取消", new QMUIDialogAction.ActionListener() {
//                                @Override
//                                public void onClick(QMUIDialog dialog, int index) {
//                                    dialog.dismiss();
//                                }
//                            })
//                            .addAction("确定", new QMUIDialogAction.ActionListener() {
//                                @Override
//                                public void onClick(QMUIDialog dialog, int index) {
//                                    dialog.dismiss();
//                                    lihuo("1");
//                                }
//                            })
//                            .show();

                } else {
                    ToastUtils.showToast(mContext, "请先选择数据", Toast.LENGTH_SHORT);
                }
            }
        });
        //endregion

        //region 理货结束按钮
        Btn_lihuoEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAct != null && PickUpstore.size() > 0) {
                    lihuo("2");
//                    String keys = "";
//                    for (String key : PickUpstore.keySet()){
//                        keys += key + " ";
//                    }
//                    new QMUIDialog.MessageDialogBuilder(mAct)
//                            .setTitle("理货结束")
//                            .setMessage(keys)
//                            .addAction("取消", new QMUIDialogAction.ActionListener() {
//                                @Override
//                                public void onClick(QMUIDialog dialog, int index) {
//                                    dialog.dismiss();
//                                }
//                            })
//                            .addAction("确定", new QMUIDialogAction.ActionListener() {
//                                @Override
//                                public void onClick(QMUIDialog dialog, int index) {
//                                    dialog.dismiss();
//                                    lihuo("2");
//                                }
//                            })
//                            .show();
                } else {
                    ToastUtils.showToast(mContext, "请先选择数据", Toast.LENGTH_SHORT);
                }
            }
        });
        //endregion

    }
    //endregion

    //endregion

    //region 功能方法

    //region 理货（结束，开始）
    private void lihuo(String pickflag) {
        String name = "";
        String action = "";

        name = HttpCommons.UPDATA_GNJ_ResetPickUpStatus_NAME;
        action = HttpCommons.UPDATA_GNJ_ResetPickUpStatus_ACTION;

        List<gnjPickUpModel> LiHuoList = new ArrayList<>();

        for (String key : PickUpstore.keySet()){
            String str = PickUpstore.get(key);
            gnjPickUpModel ll = new gnjPickUpModel();
            ll.setID(key);
            ll.setMawb(str.split("/")[0]);

            if (pickflag.equals("1")) {
                if (str.split("/")[2].equals("0")) {
                    LiHuoList.add(ll);
                }
            } else if (pickflag.equals("2")) {
                if (str.split("/")[2].equals("1")) {
                    LiHuoList.add(ll);
                }
            }
        }

        if (LiHuoList.size() > 0) {
            Map<String, String> go = getPickUpStatusXml(LiHuoList, pickflag);
            PickUpstore.clear();

            HttpRoot.getInstance().requstAync(mContext, name, action, go,
                    new HttpRoot.CallBack() {
                        @Override
                        public void onSucess(Object result) {
                            Ldialog.dismiss();
                            ToastUtils.showToast(mContext, "理货成功!", Toast.LENGTH_SHORT);
                            mHandler.sendEmptyMessage(666);
                        }

                        @Override
                        public void onFailed(String message) {
                            Ldialog.dismiss();
                            ToastUtils.showToast(mContext, message, Toast.LENGTH_SHORT);
                            mHandler.sendEmptyMessage(666);
                        }

                        @Override
                        public void onError() {
                            Ldialog.dismiss();
                            ToastUtils.showToast(mContext, "置提取状态出错!", Toast.LENGTH_SHORT);
                            mHandler.sendEmptyMessage(666);
                        }
                    }, page);
        } else {
            ToastUtils.showToast(mContext, "状态校验错误!", Toast.LENGTH_SHORT);
        }
    }
    //endregion

    //region 请求数据
    private void GetInfo(Map<String, String> p) {
        clearTableView();
        Ldialog.show();

        HttpRoot.getInstance().requstAync(mContext, HttpCommons.GET_GNC_GetGNJPickUpForPAD_NAME, HttpCommons.GET_GNC_GetGNJPickUpForPAD_ACTION, p,
                new HttpRoot.CallBack() {
                    @Override
                    public void onSucess(Object result) {
                        SoapObject object = (SoapObject) result;
                        String Exp_ULDLoading = object.getProperty(0).toString();
                        pickUpModelList = gnjPickUpConverter.gnjPickUpXMLtoMdoel(Exp_ULDLoading);

                        Ldialog.dismiss();
                        mHandler.sendEmptyMessage(AviationCommons.GNC_gnjPickUpLoadList);
                    }

                    @Override
                    public void onFailed(String message) {
                        Ldialog.dismiss();
                        ToastUtils.showToast(mContext,message,Toast.LENGTH_SHORT);

                        mHandler.sendEmptyMessage(AviationCommons.GNC_gnjPickUpLoadList);
                    }

                    @Override
                    public void onError() {
                        Ldialog.dismiss();
                        ToastUtils.showToast(mContext,"数据获取出错", Toast.LENGTH_SHORT);

                        mHandler.sendEmptyMessage(AviationCommons.GNC_gnjPickUpLoadList);
                    }
                },page);
    }
    //endregion

    //region 封装查询信息
    private Map<String,String> getPickUpXml(ArrayMap<String,String> maps) {
        StringBuilder sb = new StringBuilder();
        Map<String, String> pa = new HashMap<>();
        sb.append("<?xml version='1.0' encoding='UTF-8'?>");
        sb.append("<GNJPickUp>");
        sb.append("  <AWBInfo>");
        sb.append("    <Mawb>" + maps.get("Mawb") + "</Mawb>");
        sb.append("    <PickFlag>" + maps.get("PickFlag") +"</PickFlag>");
        sb.append("    <DLVID>" + maps.get("DLVID") +"</DLVID>");
        sb.append("    <AgentCode>" + maps.get("AgentCode") +"</AgentCode>");
        sb.append("    <DLVTime>" + maps.get("DLVTime") +"</DLVTime>");
        sb.append("  </AWBInfo>");
        sb.append("</GNJPickUp>");

        pa.put("conString", sb.toString());
        pa.put("ErrString", "");
        return pa;
    }
    //endregion

    //region 封装状态信息
    private Map<String,String> getPickUpStatusXml(List<gnjPickUpModel> models,String status) {
        StringBuilder sb = new StringBuilder();
        Map<String, String> pa = new HashMap<>();
        sb.append("<?xml version='1.0' encoding='UTF-8'?>");
        sb.append("<GNJPickUp>");

        for (gnjPickUpModel mo:models){
            sb.append("  <AWBInfo>");
            sb.append("    <ID>" + mo.getID() + "</ID>");
            sb.append("    <PickFlag>" + status +"</PickFlag>");
            sb.append("    <Mawb>" + mo.getMawb() +"</Mawb>");
            sb.append("  </AWBInfo>");
        }

        sb.append("</GNJPickUp>");

        pa.put("setString", sb.toString());
        pa.put("ErrString", "");
        return pa;
    }
    //endregion

    //region  绑定数据
    private void setDatas(List<gnjPickUpModel> CGO, int type) {
        if (CGO.size() > 0) {
            List<TableModel> mDatas = new ArrayList<>();
            for (int i = 0; i < CGO.size(); i++) {
                gnjPickUpModel cc = CGO.get(i);
                TableModel tableMode = new TableModel();
                tableMode.setOrgCode(cc.getID());
                tableMode.setLeftTitle(cc.getMawb());
                tableMode.setText0(cc.getPKID());
                tableMode.setText1(cc.getGoods());//列0内容
                tableMode.setText2(cc.getPC());//列1内容
                tableMode.setText3(cc.getAWBPC());//列2内容
                tableMode.setText4(cc.getSpCode());
                tableMode.setText5(cc.getAgentCode());
                tableMode.setText6(cc.getOrigin());
                tableMode.setText7(cc.getFDate());
                tableMode.setText8(cc.getFno());
                tableMode.setText9(cc.getChargeTime());
                tableMode.setText10(gnjPickUpConverter.SwitchPickUpFlag(cc.getPickFlag()));
                tableMode.setText11(cc.getDLVTime());
                tableMode.setText12(cc.getCNEName());
                tableMode.setText13(cc.getDLVName());
                tableMode.setText14(cc.getDLVID());
                tableMode.setText15(cc.getPKID());

                mDatas.add(tableMode);
            }
            boolean isMore;
            if (type == AviationCommons.LOAD_DATA) {
                isMore = true;
            } else {
                isMore = false;
                pulltorefreshview.setRefreshing(false);
            }
            mLeftAdapter.addData(mDatas, isMore);
            mRightAdapter.addData(mDatas, isMore);

            mDatas.clear();
        } else {
            mLeftAdapter.clearData(true);
            mRightAdapter.clearData(true);
        }
    }
    //endregion

    //region 句柄监听
    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == AviationCommons.GNC_gnjPickUpLoadList) {
                if (pickUpModelList.size() == 0) {
                    pickUpModelList = new ArrayList<gnjPickUpModel>();
                    clearTableView();
                    pulltorefreshview.setRefreshing(false);
                }else {
                    setDatas(pickUpModelList,AviationCommons.REFRESH_DATA);
                    mHandler.postDelayed(new Runnable(){
                        public void run() {
                            //execute the task
                            Ldialog.dismiss();
                        }
                    }, 1000);
                }
            }else if(msg.what == 666){
                RefreshList();
            }
            return false;
        }
    });
    //endregion

    //region 其他界面返回值处理
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case AviationCommons.PickUpSearchActivity_REQUEST:
                if (resultCode == AviationCommons.PickUpSearchActivity_RESULT) {
                    String[] quest = data.getStringArrayExtra("result");
                    if (quest.length == 5 && !TextUtils.isEmpty(quest[1])) {
                        ArrayMap<String, String> go = new ArrayMap<>();
                        go.put("Mawb", quest[0]);
                        go.put("PickFlag",quest[1]);
                        go.put("DLVID", quest[2]);
                        go.put("AgentCode", quest[3]);
                        go.put("DLVTime",quest[4]);

                        SearchFlag.setMawb(quest[0]);
                        SearchFlag.setPickFlag(quest[1]);
                        SearchFlag.setDLVID(quest[2]);
                        SearchFlag.setAgentCode(quest[3]);
                        SearchFlag.setDLVTime(quest[4]);

                        GetInfo(getPickUpXml(go));
                    } else {
                        ToastUtils.showToast(mContext, "查询参数错误！", Toast.LENGTH_SHORT);
                    }
                }

                break;
            case AviationCommons.PickUpSignatureActivity_REQUEST:
                if (resultCode == AviationCommons.PickUpSignatureActivity_RESULT) {
                    String re = data.getStringExtra("result");
                    if (re.equals("true")) {
                        tipDialog = new QMUITipDialog.Builder(mContext)
                                .setIconType(QMUITipDialog.Builder.ICON_TYPE_SUCCESS)
                                .setTipWord("上传成功")
                                .create();

                        tipDialog.show();
                        mHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                tipDialog.dismiss();
                            }
                        }, 1000);
                    } else if (re.equals("false")){
                        tipDialog = new QMUITipDialog.Builder(mContext)
                                .setIconType(QMUITipDialog.Builder.ICON_TYPE_FAIL)
                                .setTipWord("上传失败")
                                .create();

                        tipDialog.show();
                        mHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                tipDialog.dismiss();
                            }
                        }, 1000);
                    }
                }
                break;
            default:
                break;
        }
    }
    //endregion

    //region 刷新数据
    private void RefreshList(){
        if (!TextUtils.isEmpty(SearchFlag.getPickFlag()) && !TextUtils.isEmpty(SearchFlag.getDLVTime())) {
            ArrayMap<String, String> go = new ArrayMap<>();

            if (!TextUtils.isEmpty(SearchFlag.getMawb())) {
                go.put("Mawb", SearchFlag.getMawb());
            } else {
                go.put("Mawb", "");
            }

            go.put("PickFlag",SearchFlag.getPickFlag());

            if (!TextUtils.isEmpty(SearchFlag.getDLVID())) {
                go.put("DLVID", SearchFlag.getDLVID());
            } else {
                go.put("DLVID", "");
            }

            if (!TextUtils.isEmpty(SearchFlag.getAgentCode())) {
                go.put("AgentCode", SearchFlag.getAgentCode());
            } else {
                go.put("AgentCode", "");
            }

            go.put("DLVTime",SearchFlag.getDLVTime());

            GetInfo(getPickUpXml(go));
        } else {
            getFirstInfo();
        }
    }
    //endregion
    //endregion
}
