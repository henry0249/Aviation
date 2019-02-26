package com.example.administrator.aviation.ui.cgo.domestic;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.icu.text.DecimalFormat;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.ArrayMap;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.administrator.aviation.R;
import com.example.administrator.aviation.http.HttpCommons;
import com.example.administrator.aviation.http.HttpRoot;
import com.example.administrator.aviation.model.adapter.ListViewAdapter;
import com.example.administrator.aviation.model.hygnc.GNCULDLoading;
import com.example.administrator.aviation.model.hygnc.ParseGNCmessage;
import com.example.administrator.aviation.sys.PublicFun;
import com.example.administrator.aviation.ui.base.NavBar;
import com.example.administrator.aviation.ui.dialog.LoadingDialog;
import com.example.administrator.aviation.util.AviationCommons;
import com.example.administrator.aviation.util.ToastUtils;
import com.example.administrator.aviation.util.WeakHandler;
import com.example.administrator.aviation.view.AutofitTextView;
import com.example.administrator.aviation.view.SwitchView;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;
import com.qmuiteam.qmui.widget.popup.QMUIPopup;

import org.apache.commons.lang3.StringUtils;
import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.R.attr.padding;
import static android.media.CamcorderProfile.get;
import static com.example.administrator.aviation.R.id.ReWeight_edit_uldHao;
import static com.example.administrator.aviation.R.id.textView;
import static com.example.administrator.aviation.R.id.view;
import static com.example.administrator.aviation.R.id.wrap_content;
import static java.security.AccessController.getContext;
import static org.apache.commons.lang3.StringUtils.split;

public class ReWeightMain extends AppCompatActivity {

    //region 自定义变量和控件
    private final String TAG = "ReWeightMainLog";
    private final String page = "one";
    private String PinBan_Two = "";
    private String OriULD = "";
    private String LiuShuiHao = "";

    private NavBar navBar;
    private Context mContext;
    private Activity mAct;
    private LoadingDialog Ldialog;
    private PopupWindow pw;
    private QMUIPopup pop;

    private List<GNCULDLoading> gnculd;
    private ArrayList<String> list;

    private double chaZhi = 0;
    private double biZhi = 0;
    //endregion

    //region edittext控件
    @BindView(R.id.txt_ReWeight_DiBangZhong)
    EditText editDiBangZhong;
    @BindView(R.id.ReWeight_edit_PinBanHao)
    EditText editPinBanHao;
    @BindView(R.id.txt_ReWeight_TiJi)
    EditText editTiJi;
    @BindView(R.id.txt_ReWeight_BanXin)
    EditText editBanXin;
    @BindView(R.id.txt_ReWeight_BeiZhu)
    EditText editBeiZhu;
    @BindView(R.id.txt_ReWeight_DangQianZhongLiang)
    EditText editDangQianZhongLiang;
    //endregion

    //region Button控件和其他控件
    @BindView(R.id.ReWeight_SwitchBtn)
    SwitchView SwitchBtn;
    @BindView(R.id.ReWeight_btn_ChaXun)
    Button btnChaXun;
    @BindView(R.id.ReWeight_btn_QinKong)
    Button btnQinKong;
    @BindView(R.id.ReWeight_btn_TiJiao)
    Button btnTiJiao;
    @BindView(R.id.ReWeight_btn_XiuGai)
    Button btnXiuGai;
    @BindView(R.id.ReWeight_btn_QueDin)
    Button btnQueDin;
    @BindView(R.id.ReWeight_btn_QuXiao)
    Button btnQuXiao;
    //endregion

    //region TextView控件
    @BindView(ReWeight_edit_uldHao)
    TextView txtULDHao;
    @BindView(R.id.txt_ReWeight_PinBanZiZhong)
    AutofitTextView txtPinBanZiZhong;
    @BindView(R.id.txt_ReWeight_uldZiZhong)
    AutofitTextView txtULDZiZhong;
    @BindView(R.id.txt_ReWeight_ZhuangJiJinZhong)
    AutofitTextView txtZhuangJiJinZhong;
    @BindView(R.id.txt_ReWeight_JianShu)
    AutofitTextView txtJianShu;
    @BindView(R.id.txt_ReWeight_HuoZhong)
    AutofitTextView txtHuoZhong;
    @BindView(R.id.txt_ReWeight_HuoWuLeiXin)
    AutofitTextView txtHuoWuLeiXin;
    @BindView(R.id.txt_ReWeight_QiZhongYouJian)
    AutofitTextView txtQiZhongYouJian;
    @BindView(R.id.txt_ReWeight_HangBan)
    AutofitTextView txtHangBan;
    @BindView(R.id.txt_ReWeight_MuDiGang)
    AutofitTextView txtMuDiGang;
    @BindView(R.id.txt_ReWeight_CangWei)
    AutofitTextView txtCangWei;
    @BindView(R.id.txt_ReWeight_YouXianJi)
    AutofitTextView txtYouXianJi;
    @BindView(R.id.txt_ReWeight_DangQianHuoZhong)
    AutofitTextView txtDangQianHuoZhong;
    @BindView(R.id.txt_ReWeight_ZhuangTai)
    AutofitTextView txtZhuangTai;
    @BindView(R.id.ReWeight_txt_calc)
    AutofitTextView txtCalc;
    @BindView(R.id.txt_ReWeight_riqi)
    AutofitTextView txtHangBanRiQi;
    //endregion

    //region Layout控件
    @BindView(R.id.ReWeightBar)
    ViewGroup ReWeinavBar;
    @BindView(R.id.lay_ReWeight_relaLay)
    RelativeLayout relaLay;
    @BindView(R.id.lay_ReWeight_12)
    LinearLayout LLay_11;
    @BindView(R.id.ReWeight_Lay_ShuRuQu)
    LinearLayout layShuRuQu;
    @BindView(R.id.ReWeight_AllLayout)
    LinearLayout AllLayout;
    @BindView(R.id.ReWeight_Scrll)
    ScrollView Scrll;
    //endregion

    //region 初始化

    //region activity的创建
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reweight_main_activity);
        mContext = ReWeightMain.this;
        mAct = (Activity) mContext;
        ButterKnife.bind(this);

        initView();
    }
    //endregion

    //region 变量和控件的初始化
    private void initView() {
        navBar = new NavBar(this);
        navBar.setTitle("国内出港复磅");
        gnculd = new ArrayList<>();

        Ldialog = new LoadingDialog(mContext);
        SwitchBtn.setOpened(true);
        LLay_11.setVisibility(View.GONE);

        TxtViewSetEmpty();
        setListener();
    }
    //endregion

    //region 输入框置空
    private void TxtViewSetEmpty() {
        PinBan_Two = "";
        LiuShuiHao = "";
        OriULD = "";
        chaZhi = 0;
        biZhi = 0;

        editPinBanHao.setText("");
        txtULDHao.setText("");
        editDiBangZhong.setText("");

        txtULDZiZhong.setText("");
        txtPinBanZiZhong.setText("");
        txtZhuangJiJinZhong.setText("");
        editDangQianZhongLiang.setText("");
        editTiJi.setText("");
        editBanXin.setText("");
        txtJianShu.setText("");
        txtHuoZhong.setText("");
        txtHuoWuLeiXin.setText("");
        txtQiZhongYouJian.setText("");
        txtHangBan.setText("");
        txtMuDiGang.setText("");
        txtCangWei.setText("");
        txtYouXianJi.setText("");
        editBeiZhu.setText("");
        txtDangQianHuoZhong.setText("");
        txtZhuangTai.setText("");
        txtCalc.setText("");
        txtHangBanRiQi.setText("");
    }
    //endregion

    //region finish前清空消息队列，并回收垃圾
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
        System.gc();
    }
    //endregion

    //endregion

    //region 控件事件

    //region 界面上所有控件的点击事件
    private void setListener() {

        //region navBar右侧图片的点击事件
//        navBar.getRightImageView().setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
        //endregion

        //region 查询按钮的点击事件
        btnChaXun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, String> params = new HashMap<>();
                String pinBan = editPinBanHao.getText().toString().trim();

                if (!TextUtils.isEmpty(pinBan)) {
                    pinBan = PublicFun.getPinBanHao(pinBan);
                    PublicFun.KeyBoardHide(mAct, mContext);
                    params.put("ID", "0");
                    params.put("CarID", pinBan);
                    params.put("ULD", "");
                    params.put("ErrString", "");
                    Ldialog.show();

                    GetInfo(params);
                } else {
                    TxtViewSetEmpty();
                }
            }
        });
        //endregion

        //region 清空按钮
        btnQinKong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TxtViewSetEmpty();
            }
        });
        //endregion

        //region ULD编号下拉菜单点击事件
        txtULDHao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PublicFun.KeyBoardHide(mAct, mContext);
                String u = txtULDHao.getText().toString().trim();
                if (!TextUtils.isEmpty(u) && gnculd.size() > 1) {
                    if (list.size() == 0) {
                        //通过布局注入器，注入布局给View对象
                        View myView = getLayoutInflater().inflate(R.layout.pop_gnculd, null);
                        //通过view 和宽·高，构造PopopWindow
                        pw = new PopupWindow(myView, txtULDHao.getWidth(), 250, true);

                        pw.setBackgroundDrawable(getResources().getDrawable(
                                //此处为popwindow 设置背景，同事做到点击外部区域，popwindow消失
                                R.drawable.diaolog_bg));
                        //设置焦点为可点击
                        pw.setFocusable(true);//可以试试设为false的结果
                        //将window视图显示在myButton下面
                        pw.showAsDropDown(txtULDHao);

                        ListView lv = (ListView) myView.findViewById(R.id.lv_pop);
                        list = new ArrayList<String>();
                        for (int i = 0; i < gnculd.size(); i++) {
                            list.add(i, gnculd.get(i).getULD());
                        }
                        lv.setAdapter(new ListViewAdapter(ReWeightMain.this, list));

                        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                            @Override
                            public void onItemClick(AdapterView<?> parent, View view,
                                                    int position, long id) {
                                txtULDHao.setText(list.get(position));
                                TextSetVaule(position);
                                pw.dismiss();
                            }
                        });
                    } else {
                        pw.showAsDropDown(txtULDHao);
                    }

                }
            }
        });
        //endregion

        //region 目标平板修改开关
        SwitchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btnXiuGai.isEnabled()) {
                    if (SwitchBtn.isOpened()) {
                        editDiBangZhong.setFocusable(true);
                        editDiBangZhong.setFocusableInTouchMode(true);
                    } else {
                        editDiBangZhong.setText("");
                        editDiBangZhong.setFocusable(false);
                        editDiBangZhong.setFocusableInTouchMode(false);
                    }
                }
            }
        });
        //endregion

        //region 地磅重量变化联动
        editDiBangZhong.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String xx = txtPinBanZiZhong.getText().toString().trim();
                int PBzhong = 0;
                int res = 0;

                if (!TextUtils.isEmpty(xx) && StringUtils.isNumeric(xx)) {
                    PBzhong = Integer.parseInt(xx);
                }

                if (!TextUtils.isEmpty(s.toString()) && StringUtils.isNumeric(s.toString())) {
                    res = Integer.parseInt(s.toString().trim()) - PBzhong;
                }

                if (res > 0) {
                    editDangQianZhongLiang.setText(String.valueOf(res));
                    CalcInt(); //偏差值计算
                    if (chaZhi != 0 && biZhi != 0) {
                        DecimalFormat df1 = new DecimalFormat("0");
                        DecimalFormat df2 = new DecimalFormat("0.0%");
                        if (biZhi > 0.03 || biZhi < -0.03) {
                            txtCalc.setTextColor(Color.RED);
                        } else {
                            txtCalc.setTextColor(Color.BLUE);
                            editDangQianZhongLiang.setText(txtZhuangJiJinZhong.getText().toString().trim());
                        }
                        txtCalc.setText("偏差值:  " + df1.format(chaZhi) + "KG" + "\n" + "偏差比:  " + df2.format(biZhi));
                    } else {
                        txtCalc.setText("");
                    }

                } else {
                    editDangQianZhongLiang.setText("");
                    txtCalc.setText("");
                }
            }
        });
        //endregion

        //region 当前货重联动变化
        editDangQianZhongLiang.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String xx = txtULDZiZhong.getText().toString().trim();
                int ULDzhong = 0;
                int res = 0;

                if (!TextUtils.isEmpty(xx) && StringUtils.isNumeric(xx)) {
                    ULDzhong = Integer.parseInt(xx);
                }

                if (!TextUtils.isEmpty(s.toString()) && StringUtils.isNumeric(s.toString())) {
                    res = Integer.parseInt(s.toString().trim()) - ULDzhong;
                }

                if (res > 0) {
                    txtDangQianHuoZhong.setText(String.valueOf(res));
                } else {
                    txtDangQianHuoZhong.setText("");
                }
            }
        });
        //endregion

        //region 修改重量时选择备注
        editDangQianZhongLiang.setOnFocusChangeListener(new android.view.View.
                OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // 此处为得到焦点时的处理内容
                } else {
                    if (!TextUtils.isEmpty(editDangQianZhongLiang.getText()) ) {
                        editBeiZhu.performLongClick();
                    }
                }
            }
        });
        //endregion

        //region 备注长按选择
        editBeiZhu.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                PublicFun.KeyBoardHide(mAct, mContext);
                final String[] items = new String[]{"取消","雨布网罩", "正常偏差" };
                new QMUIDialog.CheckableDialogBuilder(mAct)
                        .addItems(items, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (which > 0) {
                                    editBeiZhu.setText(items[which]);
                                    editBeiZhu.setSelection(items[which].length());
                                }
                                dialog.dismiss();
                            }
                        })
                        .show();
                return true;
            }
        });
        //endregion

        //region 平板号EditText监听键盘Enter事件
        editPinBanHao.setOnEditorActionListener(new EditText.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_DONE || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) || btnChaXun.isEnabled())  {
                        btnChaXun.performClick();
                        return true;
                    }
                    return false;
                }
            }
        );
        //endregion

        //region 修改按钮点击事件
        btnXiuGai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PublicFun.KeyBoardHide(mAct, mContext);
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        String pinBan = editPinBanHao.getText().toString().trim();
                        if (!TextUtils.isEmpty(pinBan) && pinBan.equals(PinBan_Two)) {
                            OpenWri();
                        }
                    }
                }, 200);
            }
        });
        //endregion

        //region 取消按钮点击
        btnQuXiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CloseWri();
                if (!TextUtils.isEmpty(txtULDHao.getText().toString().trim())) {
                    OriULD = txtULDHao.getText().toString().trim();
                }
                btnChaXun.performClick();
            }
        });
        //endregion

        //region 地磅输入切换按钮控制
        SwitchBtn.setOnStateChangedListener(new SwitchView.OnStateChangedListener() {
            @Override
            public void toggleToOn(SwitchView view) {
                if (btnXiuGai.isEnabled()) {
                    view.toggleSwitch(true);
                } else {
                    view.toggleSwitch(false);
                }
            }

            @Override
            public void toggleToOff(SwitchView view) {
                view.toggleSwitch(false);
            }
        });
        //endregion

        //region 提交按钮点击事件
        btnTiJiao.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                String Cid = editPinBanHao.getText().toString().trim();
                ArrayMap<String, String> re = getUpdateEntity(Cid);

                if (re.size() > 0 && !TextUtils.isEmpty(editDangQianZhongLiang.getText().toString().trim())) {
                    CalcInt();
                    if (biZhi > 0.03 || biZhi < -0.03) {
                        ToastUtils.showToast(mContext,"偏差值过大！",Toast.LENGTH_SHORT);
                    } else {
                        Ldialog.show();
                        UpdatePinBanInfo(getUpdateXml(re));
                    }
                }
            }
        });
        //endregion

        //region 确定按钮点击事件
        btnQueDin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Cid = editPinBanHao.getText().toString().trim();
                ArrayMap<String, String> re = getUpdateEntity(Cid);

                if (re.size() > 0 && !TextUtils.isEmpty(editDangQianZhongLiang.getText().toString().trim())) {
                    CalcInt();
                    if (biZhi > 0.03 || biZhi < -0.03) {
                        ToastUtils.showToast(mContext,"偏差值过大！",Toast.LENGTH_SHORT);
                    } else {
                        Ldialog.show();
                        UpdatePinBanInfo(getUpdateXml(re));
                    }
                }
            }
        });
        //endregion

    }
    //endregion

    //endregion

    //region 功能方法

    //region 封装平板修改的参数信息
    private Map<String,String> getUpdateXml(ArrayMap<String,String> uldloading) {
        StringBuilder sb = new StringBuilder();
        Map<String, String> pa = new HashMap<>();
        sb.append("<?xml version='1.0' encoding='UTF-8'?>");
        sb.append("<GNCULDLoading>");
        sb.append("  <Loading>");
        sb.append("    <ID>" + uldloading.get("ID") + "</ID>");
        sb.append("    <CarID>" + uldloading.get("CarID") +"</CarID>");
        sb.append("    <ULD>" + uldloading.get("ULD") +"</ULD>");
        sb.append("    <NetWeight>" + uldloading.get("NetWeight") +"</NetWeight>");
        sb.append("    <CargoWeight>" + uldloading.get("CargoWeight") +"</CargoWeight>");
        sb.append("    <Volume>" + uldloading.get("Volume") +"</Volume>");
        sb.append("    <BoardType>" + uldloading.get("BoardType") +"</BoardType>");
        sb.append("    <Remark>" + uldloading.get("Remark") +"</Remark>");
        sb.append("  </Loading>");
        sb.append("</GNCULDLoading>");

        pa.put("ULDLoadingXml", sb.toString());
        pa.put("ErrString", "");
        return pa;
    }
    //endregion

    //region 封装修改参数实体
    private ArrayMap<String, String> getUpdateEntity(String Cid) {
        ArrayMap<String, String> re = new ArrayMap<>();
        if (!TextUtils.isEmpty(PinBan_Two) && PinBan_Two.equals(Cid)) {

            re.put("ID", LiuShuiHao);

            if (Cid.length() == 1) {
                Cid = "00" + Cid;
            } else if (Cid.length() == 2) {
                Cid = "0" + Cid;
            }

            re.put("CarID", Cid);
            re.put("ULD", txtULDHao.getText().toString().toUpperCase().trim());
            if (TextUtils.isEmpty(editDangQianZhongLiang.getText().toString().trim()) || editDangQianZhongLiang.getText().toString().trim().equals("0")) {
                re.put("NetWeight", txtZhuangJiJinZhong.getText().toString().trim());
            } else {
                re.put("NetWeight", editDangQianZhongLiang.getText().toString().trim());
            }

            re.put("CargoWeight", txtHuoZhong.getText().toString().trim());

            re.put("Volume", TextUtils.isEmpty(editTiJi.getText().toString().trim())
                    ? "0" : editTiJi.getText().toString().trim());
            re.put("BoardType", editBanXin.getText().toString().trim());
            re.put("Remark", editBeiZhu.getText().toString().trim());

        }

        return re;
    }
    //endregion

    //region 上传修改平板信息的方法
    private void UpdatePinBanInfo(Map<String, String> p) {
        HttpRoot.getInstance().requstAync(ReWeightMain.this, HttpCommons.CGO_DOM_Exp_FlatUseReWeight_NAME, HttpCommons.CGO_DOM_Exp_FlatUseReWeight_ACTION, p,
                new HttpRoot.CallBack() {
                    @Override
                    public void onSucess(Object result) {
                        SoapObject object = (SoapObject) result;
                        Log.i(TAG, object.toString());
                        String res = object.getProperty(0).toString();
                        if (res.contains("true")) {
                            btnQuXiao.performClick();
                            Ldialog.dismiss();
                            ToastUtils.showToast(ReWeightMain.this, "修改成功", Toast.LENGTH_SHORT);
                        }
                    }

                    @Override
                    public void onFailed(String message) {
                        Ldialog.dismiss();
                        ToastUtils.showToast(ReWeightMain.this,message,Toast.LENGTH_SHORT);
                    }

                    @Override
                    public void onError() {
                        Ldialog.dismiss();
                        ToastUtils.showToast(ReWeightMain.this,"数据获取出错",Toast.LENGTH_SHORT);
                    }
                },page);
    }
    //endregion

    //region 请求数据
    private void GetInfo(Map<String, String> p) {
        HttpRoot.getInstance().requstAync(ReWeightMain.this, HttpCommons.CGO_DOM_Exp_ULDLoading_NAME, HttpCommons.CGO_DOM_Exp_ULDLoading_ACTION, p,
                new HttpRoot.CallBack() {
                    @Override
                    public void onSucess(Object result) {
                        SoapObject object = (SoapObject) result;
                        String Exp_ULDLoading = object.getProperty(0).toString();
                        gnculd = ParseGNCmessage.parseGNCULDLoadingXMLto(Exp_ULDLoading);

                        mHandler.sendEmptyMessage(AviationCommons.GNC_expULDLoading);
                        Ldialog.dismiss();
                    }

                    @Override
                    public void onFailed(String message) {
                        Ldialog.dismiss();
                        ToastUtils.showToast(ReWeightMain.this,message, Toast.LENGTH_SHORT);
                    }

                    @Override
                    public void onError() {
                        Ldialog.dismiss();
                        ToastUtils.showToast(ReWeightMain.this,"数据获取出错",Toast.LENGTH_SHORT);
                    }
                },page);
    }
    //endregion

    //region 文本框赋值
    private void TextSetVaule(int x) {
        for (int j = 0;j < gnculd.size();j++) {
            if (gnculd.get(j).getULD().toString().equals(OriULD)) {
                x = j;
                break;
            }
        }

        TxtViewSetEmpty();

        LiuShuiHao = gnculd.get(x).getID().toString();

        editPinBanHao.setText(gnculd.get(x).getCarID().toString());
        PinBan_Two = gnculd.get(x).getCarID().toString();
        txtULDHao.setText(gnculd.get(x).getULD().toString());

        txtPinBanZiZhong.setText(gnculd.get(x).getCarWeight().toString());
        txtULDZiZhong.setText(gnculd.get(x).getULDWeight().toString());

        txtZhuangJiJinZhong.setText(gnculd.get(x).getNetWeight().toString());
        editTiJi.setText(gnculd.get(x).getVolume().toString());
        txtJianShu.setText(gnculd.get(x).getPC().toString());

        txtHuoZhong.setText(gnculd.get(x).getCargoWeight().toString());

        String leixinTxt = gnculd.get(x).getCargoType().toString();
        if (leixinTxt.contains("-")) {
            txtHuoWuLeiXin.setText(gnculd.get(x).getCargoType().toString().split("-")[1]);
        } else {
            txtHuoWuLeiXin.setText(leixinTxt);
        }

        txtYouXianJi.setText(gnculd.get(x).getPriority().toString());
        txtHangBan.setText(gnculd.get(x).getFno().toString());
        txtMuDiGang.setText(gnculd.get(x).getDest().toString());
        txtQiZhongYouJian.setText(gnculd.get(x).getMailWeight().toString());

        editBeiZhu.setText(gnculd.get(x).getRemark().toString());
        editBanXin.setText(gnculd.get(x).getBoardType().toString());
        txtCangWei.setText(gnculd.get(x).getLocation().toString());

        String fda = gnculd.get(x).getFDate().toString();
        if (fda.contains("_")) {
            txtHangBanRiQi.setText(gnculd.get(x).getFDate().toString().split("_")[1]);
        } else {
            txtHangBanRiQi.setText(fda);
        }

        String cFlagTxt = gnculd.get(x).getcFlag().toString();
        if (cFlagTxt.contains("-")) {
            txtZhuangTai.setText(gnculd.get(x).getcFlag().toString().split("-")[1]);
        } else {
            txtZhuangTai.setText(cFlagTxt);
        }
    }
    //endregion

    //region 句柄监听
    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == AviationCommons.GNC_expULDLoading) {
                if (gnculd.size() > 0) {
                    TextSetVaule(0);
                    list = new ArrayList<>();
                    editPinBanHao.setSelection(editPinBanHao.getText().toString().trim().length());
                } else {
                    ToastUtils.showToast(ReWeightMain.this,"数据为空",Toast.LENGTH_SHORT);
                    TxtViewSetEmpty();
                }
            } else if (msg.what == 0) {

            }
            return false;
        }
    });
    //endregion

    //region 打开编辑区
    private void OpenWri() {
        LLay_11.setVisibility(View.VISIBLE);

        editDangQianZhongLiang.setBackgroundResource(R.drawable.edit_bg);
        editDangQianZhongLiang.setFocusable(true);
        editDangQianZhongLiang.setFocusableInTouchMode(true);

        editBeiZhu.setBackgroundResource(R.drawable.edit_bg_longclick);
        editBeiZhu.setFocusable(true);
        editBeiZhu.setFocusableInTouchMode(true);

        editTiJi.setBackgroundResource(R.drawable.edit_bg);
        editTiJi.setFocusable(true);
        editTiJi.setFocusableInTouchMode(true);

        editBanXin.setBackgroundResource(R.drawable.edit_bg);
        editBanXin.setFocusable(true);
        editBanXin.setFocusableInTouchMode(true);

        if (SwitchBtn.isOpened()) {
            SwitchBtn.setOpened(false);
        }

        PublicFun.ElementSwitch(layShuRuQu,false);
        PublicFun.ElementSwitch(ReWeinavBar,false);

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                int offset = AllLayout.getMeasuredHeight() - Scrll.getHeight();
                if (offset > 0) {
                    Scrll.fullScroll(ScrollView.FOCUS_DOWN);
                }
            }
        });
    }
    //endregion

    //region 关闭编辑区
    private void CloseWri() {
        LLay_11.setVisibility(View.GONE);

        editBeiZhu.setBackgroundResource(0);
        editBeiZhu.setFocusable(false);
        editBeiZhu.setFocusableInTouchMode(false);

        editTiJi.setBackgroundResource(0);
        editTiJi.setFocusable(false);
        editTiJi.setFocusableInTouchMode(false);

        editBanXin.setBackgroundResource(0);
        editBanXin.setFocusable(false);
        editBanXin.setFocusableInTouchMode(false);

        editDangQianZhongLiang.setBackgroundResource(0);
        editDangQianZhongLiang.setFocusable(false);
        editDangQianZhongLiang.setFocusableInTouchMode(false);

        PublicFun.ElementSwitch(layShuRuQu,true);
        PublicFun.ElementSwitch(ReWeinavBar,true);

        if (!SwitchBtn.isOpened()) {
            SwitchBtn.setOpened(true);
        }

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Scrll.fullScroll(ScrollView.FOCUS_UP);
            }
        },200);

    }
    //endregion

    //region 计算偏差值
    private void CalcInt() {
        String JiZhong = txtZhuangJiJinZhong.getText().toString().trim();
        String DangQian = editDangQianZhongLiang.getText().toString().trim();
        int dq = 0;
        double yl = 0;

        if (!TextUtils.isEmpty(JiZhong)) {
            if (StringUtils.isNumeric(JiZhong)) {
                yl = Double.valueOf(JiZhong);
            } else {
                if (JiZhong.contains(".")) {
                    if (Integer.valueOf(JiZhong.split("\\.")[1].charAt(0)) > 5) {
                        yl = Double.parseDouble(JiZhong.split("\\.")[0]) + 1;
                    } else {
                        yl = Double.parseDouble(JiZhong.split("\\.")[0]);
                    }
                }
            }
        }

        if (!TextUtils.isEmpty(DangQian) && StringUtils.isNumeric(DangQian)) {
            dq = Integer.parseInt(DangQian);
        }

        if (dq != 0 && yl != 0) {
            chaZhi = (dq - yl);
            biZhi = (dq - yl) / yl;
        }
    }
    //endregion

    //endregion
}
