package com.example.administrator.aviation.ui.cgo.domestic;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Region;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.Log;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
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
import com.example.administrator.aviation.tool.AllCapTransformationMethod;
import com.example.administrator.aviation.ui.base.NavBar;
import com.example.administrator.aviation.ui.dialog.LoadingDialog;
import com.example.administrator.aviation.util.AviationCommons;
import com.example.administrator.aviation.util.ToastUtils;
import com.example.administrator.aviation.view.SwitchView;

import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.R.attr.data;
import static android.R.attr.tag;
import static android.R.id.message;
import static com.example.administrator.aviation.util.AviationCommons.GNC_ULDLOADING_CAMERA_REQUEST;
import static com.example.administrator.aviation.util.AviationCommons.GNC_ULDLOADING_XinZenPinBan_REQUEST;
import static com.example.administrator.aviation.util.AviationCommons.GNC_ULDinfo_CAMERA_REQUEST;

//region 佛祖保佑 永无BUG 永不修改 --by sst
////////////////////////////////////////////////////////////////////
//                          _ooOoo_                               //
//                         o8888888o                              //
//                         88" . "88                              //
//                         (| ^_^ |)                              //
//                         O\  =  /O                              //
//                      ____/`---'\____                           //
//                    .'  \\|     |//  `.                         //
//                   /  \\|||  :  |||//  \                        //
//                  /  _||||| -:- |||||-  \                       //
//                  |   | \\\  -  /// |   |                       //
//                  | \_|  ''\---/''  |   |                       //
//                  \  .-\__  `-`  ___/-. /                       //
//                ___`. .'  /--.--\  `. . ___                     //
//              ."" '<  `.___\_<|>_/___.'  >'"".                  //
//            | | :  `- \`.;`\ _ /`;.`/ - ` : | |                 //
//            \  \ `-.   \_ __\ /__ _/   .-` /  /                 //
//      ========`-.____`-.___\_____/___.-`____.-'========         //
//                           `=---='                              //
//      ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^        //
//             佛祖保佑       永无BUG     永不修改                //
//                                                                //
//       佛曰:                                                    //
//               写字楼里写字间，写字间里程序员；                 //
//               程序人员写程序，又拿程序换酒钱。                 //
//               酒醒只在网上坐，酒醉还来网下眠；                 //
//               酒醉酒醒日复日，网上网下年复年。                 //
//               但愿老死电脑间，不愿鞠躬老板前；                 //
//               奔驰宝马贵者趣，公交自行程序员。                 //
//               别人笑我太疯癫，我笑他人看不穿；                 //
//               不见满街漂亮妹，哪个归得程序员？                 //
////////////////////////////////////////////////////////////////////
//endregion
public class expULDLoading extends AppCompatActivity {
    //region Button控件
    @BindView(R.id.uldloading_Btn_ChaXun)
    Button ChaXun;
    @BindView(R.id.uldloading_Btn_XinZen)
    Button XinZen;
    @BindView(R.id.uldloading_Btn_QinKong)
    Button QinKong;
    @BindView(R.id.uldloading_Btn_XiuGai)
    Button XiuGai;
    @BindView(R.id.uldloading_Btn_Tijiao)
    Button btn_Tijiao;
    @BindView(R.id.uldloading_Btn_quXiao)
    Button btn_Quxiao;
    //endregion

    //region TextView控件
    @BindView(R.id.uldloading_tv_uldBianHao)
    TextView uldBianHao;
    @BindView(R.id.uldloading_tv_JinZhong)
    TextView JinZhong;
    @BindView(R.id.uldloading_tv_HuoZhong)
    TextView HuoZhong;
    @BindView(R.id.uldloading_tv_JianShu)
    TextView JianShu;
    @BindView(R.id.uldloading_tv_LeiXin)
    TextView LeiXin;
    @BindView(R.id.uldloading_tv_PeiZai)
    TextView PeiZai;
    @BindView(R.id.uldloading_tv_YouXianJi)
    TextView YouXianJi;
    @BindView(R.id.uldloading_tv_HangBanHao)
    TextView HangBanHao;
    @BindView(R.id.uldloading_tv_MuDinGang)
    TextView MuDinGang;
    @BindView(R.id.uldloading_tv_RiQi)
    TextView RiQi;
    @BindView(R.id.uldloading_tv_ChenYunRen)
    TextView ChenYunRen;
    @BindView(R.id.uldloading_tv_LiuShuiHao)
    TextView LiuShuiHao;
    @BindView(R.id.uldloading_tv_CangWei)
    TextView CangWei;
    //endregion

    //region Layout控件
    @BindView(R.id.uldloading_Lay_YinCan)
    LinearLayout LayYincang;
    @BindView(R.id.uldloading_Layout_Tijiao)
    LinearLayout LayTiJiao;
    @BindView(R.id.uldloading_Lay_PB)
    LinearLayout Lay_PB;
    //endregion

    //region EditText控件
    @BindView(R.id.uldloading_tv_BeiZhu)
    EditText BeiZhu;
    @BindView(R.id.uldloading_tv_ZiZhong)
    EditText ZiZhong;
    @BindView(R.id.uldloading_edtTxt_PinBanHao_one)
    EditText PinBanHao_one;
    @BindView(R.id.uldloading_tv_ULDzhong)
    EditText ULDzhong;
    @BindView(R.id.uldloading_tv_TiJi)
    EditText TiJi;
    @BindView(R.id.uldloading_tv_BanXin)
    EditText BanXin;
    @BindView(R.id.uldloading_edtTxt_MuBiaoPinBan)
    EditText MuBiaoPinBanEdt;
    @BindView(R.id.uldloading_edtTxt_MuBiaoULD)
    EditText MuBiaoULDEdt;
    //endregion

    //region 其他控件
    @BindView(R.id.uldloading_navBar)
    ViewGroup uldloading_navBar;
    @BindView(R.id.uldloading_Img_SaoMa)
    ImageView Img_SaoMa;
    @BindView(R.id.uldloading_Scrl)
    ScrollView scrollview;
    @BindView(R.id.uldloading_PinBanSwitchBtn)
    SwitchView PinBanSwitchBtn;
    @BindView(R.id.uldloading_uldSwitchBtn)
    SwitchView uldSwitchBtn;
    //endregion

    //region 未预设XML控件
    private NavBar navBar;
    private PopupWindow pw;
    private LoadingDialog Ldialog;
    //endregion

    //region 自定义全局变量
    private final String TAG = "expULDLoadingLog";
    private final String page = "one";
    private String PinBan_Two = "";
    private String OriULD = "";
    private List<GNCULDLoading> gnculd;
    private ArrayList<String> list;
    private Context mContext;
    private Activity mAct;
    //endregion

    //region 初始化

    //region activity的创建
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exp_uldloading_activity);
        mContext = expULDLoading.this;
        mAct = (Activity) mContext;
        ButterKnife.bind(this);
        initView();
    }
    //endregion

    //region finish前清空消息队列，并回收垃圾
    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
        System.gc();
    }
    //endregion

    //region start方法，会在OnActivityResult之后调用
    @Override
    protected void onStart() {
        super.onStart();
        if (gnculd.size() > 0 && PinBan_Two.equals(PinBanHao_one.getText().toString().trim())) {
            ChaXun.performClick();
        } else {
            Intent da = getIntent();
            Bundle bundle = da.getExtras();
            if (bundle != null) {
                String re = bundle.getString("ZhuangJiDanMain","");
                if (!TextUtils.isEmpty(re)) {
                    QinKong.performClick();
                    PinBanHao_one.setText(re);
                    ChaXun.performClick();
                }
            }
        }
    }
    //endregion

    //region 变量和控件的初始化
    private void initView() {
        gnculd = new ArrayList<>();
        list = new ArrayList<>();
        MuBiaoULDEdt.setTransformationMethod(new AllCapTransformationMethod());

        navBar = new NavBar(this);
        navBar.setTitle("国内出港理货");
        navBar.setRight(R.drawable.detail_0);
        LayTiJiao.setVisibility(View.GONE);
        LayYincang.setVisibility(View.GONE);
        Ldialog = new LoadingDialog(mContext);

        TxtViewSetEmpty();
        setListener();
    }
    //endregion

    //region 输入框置空
    private void TxtViewSetEmpty() {
        PinBanHao_one.setText("");
        uldBianHao.setText("");
        ZiZhong.setText("");
        JinZhong.setText("");
        HuoZhong.setText("");
        TiJi.setText("");
        JianShu.setText("");
        LeiXin.setText("");
        PeiZai.setText("");
        YouXianJi.setText("");
        HangBanHao.setText("");
        MuDinGang.setText("");
        RiQi.setText("");
        ChenYunRen.setText("");
        LiuShuiHao.setText("");
        ULDzhong.setText("");
        PinBan_Two = "";

        BeiZhu.setText("");
        BanXin.setText("");
        CangWei.setText("");
    }
    //endregion

    //region 类的Intent
    public static Intent newIndexIntent(Context context, String name,String message) {
        Intent newIntent = new Intent(context, expULDLoading.class);
        newIntent.putExtra(name, message);
        return newIntent;
    }
    //endregion

    //endregion

    //region 控件事件

    //region activity界面回调事件
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case GNC_ULDLOADING_CAMERA_REQUEST:
                if (resultCode == AviationCommons.GNC_ULDLOADING_CAMERA_RESULT) {
                    Bundle bundle = data.getExtras();
                    String re = bundle.getString("result");

                    if (!TextUtils.isEmpty(re)) {
                        QinKong.performClick();

                        Map<String, String> params = new HashMap<>();
                        PublicFun.KeyBoardHide(mAct,mContext);
                        params.put("ID", re.split("/")[0].trim());
                        params.put("CarID", "");
                        params.put("ULD", re.split("/")[1].trim());
                        params.put("ErrString", "");
                        Ldialog.show();

                        GetInfo(params);
                    }
                }
                break;
            case GNC_ULDLOADING_XinZenPinBan_REQUEST:
                if (resultCode == AviationCommons.GNC_ULDLOADING_XinZenPinBan_RESULT) {
                    Bundle bundle = data.getExtras();
                    String re = bundle.getString("result");

                    if (!TextUtils.isEmpty(re)) {
                        QinKong.performClick();
                        PinBanHao_one.setText(re);
                        ChaXun.performClick();
                    }
                }
                break;
        }
    }
    //endregion

    //region 返回后触发父界面逻辑
    @Override
    public void finish() {
        Intent intent = new Intent();
        Bundle bundle = intent.getExtras();
        int num = 0;

        if (bundle != null) {
            String req =  bundle.getString("ZhuangJiDanMain","");
            if (!TextUtils.isEmpty(req) &&req.equals(req)) {
                intent.setClass(mContext, ZhuangJiDanMain.class);
                num = AviationCommons.GNC_ZhuangJiDan_RESULT;
            }

            setResult(num,intent);
        }

        super.finish();
    }
    //endregion

    //region 页面上所有的点击事件
    private void setListener() {

        //region navBar右侧图片的点击事件
        navBar.getRightImageView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (gnculd.size() > 0 && !TextUtils.isEmpty(LiuShuiHao.getText().toString().trim())) {
                    if (PinBan_Two.equals(PinBanHao_one.getText().toString().trim())) {
                        HashMap<String, String> go = new HashMap<String, String>();
                        go.put("ID", LiuShuiHao.getText().toString().trim());
                        go.put("ULD", uldBianHao.getText().toString().trim());
                        go.put("BanID", PinBan_Two);
                        go.put("ErrString", "");

                        Bundle mBundle = new Bundle();
                        mBundle.putSerializable("Info", go);

                        Intent intent = new Intent(expULDLoading.this, expULDcargoInfo.class);
                        intent.putExtras(mBundle);
                        startActivity(intent);
                    } else {
                        ToastUtils.showToast(mContext,"平板编号和查询信息不一致！",Toast.LENGTH_LONG);
                    }
                }
            }
        });
        //endregion

        //region 新增按钮的点击事件
        XinZen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(expULDLoading.this, ActLiHuoXinZenPinBan.class);
                intent.putExtra("id", GNC_ULDLOADING_XinZenPinBan_REQUEST);
                startActivityForResult(intent, GNC_ULDLOADING_XinZenPinBan_REQUEST);
            }
        });
        //endregion

        //region 修改按钮点击
        XiuGai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pinBan = PinBanHao_one.getText().toString().trim();
                if (!TextUtils.isEmpty(pinBan) && pinBan.equals(PinBan_Two)) {
                    OpenWri();
                }
            }
        });
        //endregion

        //region 提交按钮点击
        btn_Tijiao.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                ArrayMap<String,String> re = new ArrayMap<>();
                re.put("ID", LiuShuiHao.getText().toString().trim());
                re.put("OLDCarID", PinBanHao_one.getText().toString().trim());
                re.put("OLDULD",uldBianHao.getText().toString().toUpperCase().trim());

                String Cid = MuBiaoPinBanEdt.getText().toString().trim();
                if (TextUtils.isEmpty(Cid)) {
                    re.put("CarID", PinBanHao_one.getText().toString().trim());
                } else {
                    if (Cid.length() == 1) {
                        Cid = "00" + Cid;
                    } else if (Cid.length() == 2) {
                        Cid = "0" + Cid;
                    }
                    re.put("CarID",Cid);
                }

                re.put("ULD",TextUtils.isEmpty(MuBiaoULDEdt.getText().toString().toUpperCase().trim())
                        ? uldBianHao.getText().toString().toUpperCase().trim():MuBiaoULDEdt.getText().toString().toUpperCase().trim());
                re.put("CarWeight",TextUtils.isEmpty(ZiZhong.getText().toString().trim())
                        ? "0":ZiZhong.getText().toString().trim());
                re.put("ULDWeight",TextUtils.isEmpty(ULDzhong.getText().toString().trim())
                        ? "0":ULDzhong.getText().toString().trim());
                re.put("Volume",TextUtils.isEmpty(TiJi.getText().toString().trim())
                        ? "0":TiJi.getText().toString().trim());
                re.put("BoardType",BanXin.getText().toString().trim());
                re.put("Priority",YouXianJi.getText().toString().trim());
                re.put("Location",CangWei.getText().toString().trim());
                re.put("Remark",BeiZhu.getText().toString().trim());

                Ldialog.show();
                UpdatePinBanInfo(getUpdateXml(re));
            }
        });
        //endregion

        //region 取消按钮点击
        btn_Quxiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CloseWri();
                if (!TextUtils.isEmpty(uldBianHao.getText().toString().trim())) {
                    OriULD = uldBianHao.getText().toString().trim();
                }
                ChaXun.performClick();
            }
        });
        //endregion

        //region 查询按钮的点击事件
        ChaXun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, String> params = new HashMap<>();
                String pinBan = PinBanHao_one.getText().toString().trim();

                if (!TextUtils.isEmpty(pinBan)) {
                    if (pinBan.length() == 1) {
                        pinBan = "00" + pinBan;
                    } else if (pinBan.length() == 2) {
                        pinBan = "0" + pinBan;
                    }

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

        //region ULD编号下拉菜单点击事件
        uldBianHao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PublicFun.KeyBoardHide(mAct, mContext);
                String u = uldBianHao.getText().toString().trim();
                if (!TextUtils.isEmpty(u) && gnculd.size() > 1) {
                    if (list.size() == 0) {
                        //通过布局注入器，注入布局给View对象
                        View myView = getLayoutInflater().inflate(R.layout.pop_gnculd, null);
                        //通过view 和宽·高，构造PopopWindow
                        pw = new PopupWindow(myView, uldBianHao.getWidth(), 250, true);

                        pw.setBackgroundDrawable(getResources().getDrawable(
                                //此处为popwindow 设置背景，同事做到点击外部区域，popwindow消失
                                R.drawable.diaolog_bg));
                        //设置焦点为可点击
                        pw.setFocusable(true);//可以试试设为false的结果
                        //将window视图显示在myButton下面
                        pw.showAsDropDown(uldBianHao);

                        ListView lv = (ListView) myView.findViewById(R.id.lv_pop);
                        list = new ArrayList<String>();
                        for (int i = 0; i < gnculd.size(); i++) {
                            list.add(i, gnculd.get(i).getULD());
                        }
                        lv.setAdapter(new ListViewAdapter(expULDLoading.this, list));

                        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                            @Override
                            public void onItemClick(AdapterView<?> parent, View view,
                                                    int position, long id) {
                                uldBianHao.setText(list.get(position));
                                TextSetVaule(position);
                                pw.dismiss();
                            }
                        });
                    } else {
                        pw.showAsDropDown(uldBianHao);
                    }

                }
            }
        });
        //endregion

        //region 舱位下拉选择事件
        CangWei.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PublicFun.KeyBoardHide(mAct, mContext);
                if (LayTiJiao.getVisibility() == View.VISIBLE) {
                    //通过布局注入器，注入布局给View对象
                    View myView = getLayoutInflater().inflate(R.layout.pop_gnculd, null);
                    //通过view 和宽·高，构造PopopWindow
                    pw = new PopupWindow(myView, CangWei.getWidth(), 250, true);

                    pw.setBackgroundDrawable(getResources().getDrawable(
                            //此处为popwindow 设置背景，同事做到点击外部区域，popwindow消失
                            R.drawable.diaolog_bg));
                    //设置焦点为可点击
                    pw.setFocusable(true);//可以试试设为false的结果
                    //将window视图显示在myButton下面
                    pw.showAsDropDown(CangWei);
                    ListView lv = (ListView) myView.findViewById(R.id.lv_pop);
                    list = new ArrayList<String>();
                    list.add("");
                    list.add("1HD");
                    list.add("2HD");
                    list.add("3HD");
                    list.add("4HD");
                    list.add("5HD");

                    lv.setAdapter(new ListViewAdapter(expULDLoading.this, list));

                    lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view,
                                                int position, long id) {
                            CangWei.setText(list.get(position));
                            pw.dismiss();
                        }
                    });
                }
            }
        });
        //endregion

        //region 优先级下拉选择事件
        YouXianJi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PublicFun.KeyBoardHide(mAct, mContext);
                if (LayTiJiao.getVisibility() == View.VISIBLE) {
                    //通过布局注入器，注入布局给View对象
                    View myView = getLayoutInflater().inflate(R.layout.pop_gnculd, null);
                    //通过view 和宽·高，构造PopopWindow
                    pw = new PopupWindow(myView, YouXianJi.getWidth(), 220, true);

                    pw.setBackgroundDrawable(getResources().getDrawable(
                            //此处为popwindow 设置背景，同事做到点击外部区域，popwindow消失
                            R.drawable.diaolog_bg));
                    //设置焦点为可点击
                    pw.setFocusable(true);//可以试试设为false的结果
                    //将window视图显示在myButton下面
                    pw.showAsDropDown(YouXianJi);
                    ListView lv = (ListView) myView.findViewById(R.id.lv_pop);
                    list = new ArrayList<String>();
                    list.add("0");
                    list.add("1");
                    list.add("2");
                    list.add("3");
                    list.add("4");
                    list.add("5");
                    list.add("6");
                    list.add("7");
                    list.add("8");
                    list.add("9");

                    lv.setAdapter(new ListViewAdapter(expULDLoading.this, list));

                    lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                        @Override
                        public void onItemClick(AdapterView<?> parent, View view,
                                                int position, long id) {
                            YouXianJi.setText(list.get(position));
                            pw.dismiss();
                        }
                    });
                }
            }
        });
        //endregion

        //region 清空按钮
        QinKong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TxtViewSetEmpty();
            }
        });
        //endregion

        //region 扫描按钮点击事件
        Img_SaoMa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                useCamera();
            }
        });
        //endregion

        //region 平板号EditText监听键盘Enter事件
        PinBanHao_one.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) || ChaXun.isEnabled())  {
                    ChaXun.performClick();
                    return true;
                }
                return false;
            }
        }
        );
        //endregion

        //region 目标平板修改开关
        PinBanSwitchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (PinBanSwitchBtn.isOpened()) {
                    MuBiaoPinBanEdt.setFocusable(true);
                    MuBiaoPinBanEdt.setFocusableInTouchMode(true);
                } else {
                    MuBiaoPinBanEdt.setText("");
                    MuBiaoPinBanEdt.setFocusable(false);
                    MuBiaoPinBanEdt.setFocusableInTouchMode(false);
                }
            }
        });
        //endregion

        //region 目标平板状态切换设置
        PinBanSwitchBtn.setOnStateChangedListener(new SwitchView.OnStateChangedListener() {
            @Override
            public void toggleToOn(SwitchView view) {
                if (XiuGai.isEnabled()) {
                    view.toggleSwitch(false);
                } else {
                    view.toggleSwitch(true);
                }
            }

            @Override
            public void toggleToOff(SwitchView view) {
                view.toggleSwitch(false);
            }
        });
        //endregion

        //region 目标ULD修改开关
        uldSwitchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (uldSwitchBtn.isOpened()) {
                    MuBiaoULDEdt.setFocusable(true);
                    MuBiaoULDEdt.setFocusableInTouchMode(true);
                } else {
                    MuBiaoULDEdt.setText("");
                    MuBiaoULDEdt.setFocusable(false);
                    MuBiaoULDEdt.setFocusableInTouchMode(false);
                }
            }
        });
        //endregion

        //region 目标ULD状态切换设置
        uldSwitchBtn.setOnStateChangedListener(new SwitchView.OnStateChangedListener() {
            @Override
            public void toggleToOn(SwitchView view) {
                if (XiuGai.isEnabled()) {
                    view.toggleSwitch(false);
                } else {
                    view.toggleSwitch(true);
                }
            }

            @Override
            public void toggleToOff(SwitchView view) {
                view.toggleSwitch(false);
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
        sb.append("    <OLDCarID>" + uldloading.get("OLDCarID") + "</OLDCarID>");
        sb.append("    <OLDULD>" + uldloading.get("OLDULD") +"</OLDULD>");
        sb.append("    <CarID>" + uldloading.get("CarID") +"</CarID>");
        sb.append("    <ULD>" + uldloading.get("ULD") +"</ULD>");
        sb.append("    <CarWeight>" + uldloading.get("CarWeight") +"</CarWeight>");
        sb.append("    <ULDWeight>" + uldloading.get("ULDWeight") +"</ULDWeight>");
        sb.append("    <Volume>" + uldloading.get("Volume") +"</Volume>");
        sb.append("    <BoardType>" + uldloading.get("BoardType") +"</BoardType>");
        sb.append("    <Priority>" + uldloading.get("Priority") +"</Priority>");
        sb.append("    <Location>" + uldloading.get("Location") +"</Location>");
        sb.append("    <Remark>" + uldloading.get("Remark") +"</Remark>");
        sb.append("  </Loading>");
        sb.append("</GNCULDLoading>");


        pa.put("ULDLoadingXml", sb.toString());
        pa.put("ErrString", "");
        return pa;
    }
    //endregion

    //region 上传修改平板信息的方法
    private void UpdatePinBanInfo(Map<String, String> p) {
        HttpRoot.getInstance().requstAync(expULDLoading.this, HttpCommons.CGO_DOM_Exp_UpdateGNCLoading_NAME, HttpCommons.CGO_DOM_Exp_UpdateGNCLoading_ACTION, p,
                new HttpRoot.CallBack() {
                    @Override
                    public void onSucess(Object result) {
                        SoapObject object = (SoapObject) result;
                        Log.i(TAG, object.toString());
                        String res = object.getProperty(0).toString();
                        if (res.contains("true")) {
                            String Cid = MuBiaoPinBanEdt.getText().toString().trim();
                            if (TextUtils.isEmpty(Cid)) {
                                btn_Quxiao.performClick();

                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        Ldialog.dismiss();
                                        ToastUtils.showToast(expULDLoading.this, "修改成功", Toast.LENGTH_SHORT);
                                    }
                                }, 500);

                            } else {

                                Map<String, String> params = new HashMap<>();
                                PublicFun.KeyBoardHide(mAct, mContext);

                                if (Cid.length() == 1) {
                                    Cid = "00" + Cid;
                                } else if (Cid.length() == 2) {
                                    Cid = "0" + Cid;
                                }

                                if (!TextUtils.isEmpty(MuBiaoULDEdt.getText().toString().toUpperCase().trim())) {
                                    OriULD = MuBiaoULDEdt.getText().toString().toUpperCase().trim();
                                }

                                params.put("ID", "0");
                                params.put("CarID", Cid);
                                params.put("ULD", "");
                                params.put("ErrString", "");

                                CloseWri();

                                Ldialog.show();
                                GetInfo(params);
                                ToastUtils.showToast(expULDLoading.this, "修改成功", Toast.LENGTH_SHORT);
                            }
                        }
                    }

                    @Override
                    public void onFailed(String message) {
                        Ldialog.dismiss();
                        ToastUtils.showToast(expULDLoading.this,message,Toast.LENGTH_SHORT);
                    }

                    @Override
                    public void onError() {
                        Ldialog.dismiss();
                        ToastUtils.showToast(expULDLoading.this,"数据获取出错",Toast.LENGTH_SHORT);
                    }
                },page);
    }
    //endregion

    //region 打开编辑区
    private void OpenWri() {
        LayTiJiao.setVisibility(View.VISIBLE);

        BeiZhu.setBackgroundResource(R.drawable.edit_bg);
        BeiZhu.setFocusable(true);
        BeiZhu.setFocusableInTouchMode(true);

        ZiZhong.setBackgroundResource(R.drawable.edit_bg);
        ZiZhong.setFocusable(true);
        ZiZhong.setFocusableInTouchMode(true);

        ULDzhong.setBackgroundResource(R.drawable.edit_bg);
        ULDzhong.setFocusable(true);
        ULDzhong.setFocusableInTouchMode(true);

        TiJi.setBackgroundResource(R.drawable.edit_bg);
        TiJi.setFocusable(true);
        TiJi.setFocusableInTouchMode(true);

        BanXin.setBackgroundResource(R.drawable.edit_bg);
        BanXin.setFocusable(true);
        BanXin.setFocusableInTouchMode(true);

        CangWei.setBackgroundResource(R.drawable.selector_sinner);
        YouXianJi.setBackgroundResource(R.drawable.selector_sinner);

        uldBianHao.setTextColor(Color.parseColor("#3A000000"));

        PublicFun.ElementSwitch(Lay_PB,false);
        PublicFun.ElementSwitch(uldloading_navBar,false);

        handler.post(new Runnable() {
            @Override
            public void run() {
                scrollview.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });
    }
    //endregion

    //region 关闭编辑区
    private void CloseWri() {
        LayTiJiao.setVisibility(View.GONE);

        BeiZhu.setBackgroundResource(0);
        BeiZhu.setFocusable(false);
        BeiZhu.setFocusableInTouchMode(false);

        ZiZhong.setBackgroundResource(0);
        ZiZhong.setFocusable(false);
        ZiZhong.setFocusableInTouchMode(false);

        ULDzhong.setBackgroundResource(0);
        ULDzhong.setFocusable(false);
        ULDzhong.setFocusableInTouchMode(false);

        TiJi.setBackgroundResource(0);
        TiJi.setFocusable(false);
        TiJi.setFocusableInTouchMode(false);

        BanXin.setBackgroundResource(0);
        BanXin.setFocusable(false);
        BanXin.setFocusableInTouchMode(false);

        CangWei.setBackgroundResource(0);
        YouXianJi.setBackgroundResource(0);

        uldBianHao.setTextColor(Color.BLACK);

        PinBanSwitchBtn.setOpened(false);
        uldSwitchBtn.setOpened(false);

        MuBiaoPinBanEdt.setText("");
        MuBiaoULDEdt.setText("");

        MuBiaoPinBanEdt.setText("");
        MuBiaoPinBanEdt.setFocusable(false);
        MuBiaoPinBanEdt.setFocusableInTouchMode(false);

        MuBiaoULDEdt.setText("");
        MuBiaoULDEdt.setFocusable(false);
        MuBiaoULDEdt.setFocusableInTouchMode(false);

        PublicFun.ElementSwitch(Lay_PB,true);
        PublicFun.ElementSwitch(uldloading_navBar,true);

        handler.post(new Runnable() {
            @Override
            public void run() {
                scrollview.fullScroll(ScrollView.FOCUS_UP);
            }
        });
    }
    //endregion

    //region 句柄监听
    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == AviationCommons.GNC_expULDLoading) {
                if (gnculd.size() > 0) {
                    TextSetVaule(0);
                    list = new ArrayList<>();
                    PinBanHao_one.setSelection(PinBanHao_one.getText().toString().trim().length());
                    PinBan_Two = PinBanHao_one.getText().toString().trim();
                } else {
                    ToastUtils.showToast(expULDLoading.this,"数据为空",Toast.LENGTH_SHORT);
                    TxtViewSetEmpty();
                }
            }
            return false;
        }
    });
    //endregion

    //region 请求数据
    private void GetInfo(Map<String, String> p) {
        HttpRoot.getInstance().requstAync(expULDLoading.this, HttpCommons.CGO_DOM_Exp_ULDLoading_NAME, HttpCommons.CGO_DOM_Exp_ULDLoading_ACTION, p,
                new HttpRoot.CallBack() {
                    @Override
                    public void onSucess(Object result) {
                        SoapObject object = (SoapObject) result;
                        String Exp_ULDLoading = object.getProperty(0).toString();
                        gnculd = ParseGNCmessage.parseGNCULDLoadingXMLto(Exp_ULDLoading);

                        handler.sendEmptyMessage(AviationCommons.GNC_expULDLoading);
                        Ldialog.dismiss();
                    }

                    @Override
                    public void onFailed(String message) {
                        Ldialog.dismiss();
                        ToastUtils.showToast(expULDLoading.this,message,Toast.LENGTH_SHORT);
                    }

                    @Override
                    public void onError() {
                        Ldialog.dismiss();
                        ToastUtils.showToast(expULDLoading.this,"数据获取出错",Toast.LENGTH_SHORT);
                    }
                },page);
    }
    //endregion

    //region 调用相机
    private void useCamera() {
        Intent intent = new Intent(expULDLoading.this, CaptureActivity.class);
        intent.putExtra("id",GNC_ULDLOADING_CAMERA_REQUEST);
        startActivityForResult(intent, GNC_ULDLOADING_CAMERA_REQUEST);
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

        OriULD = "";
        PinBanHao_one.setText(gnculd.get(x).getCarID().toString());
        uldBianHao.setText(gnculd.get(x).getULD().toString());

        ZiZhong.setText(gnculd.get(x).getCarWeight().toString());
        ULDzhong.setText(gnculd.get(x).getULDWeight().toString());

        JinZhong.setText(gnculd.get(x).getNetWeight().toString());
        HuoZhong.setText(gnculd.get(x).getCargoWeight().toString());
        TiJi.setText(gnculd.get(x).getVolume().toString());
        JianShu.setText(gnculd.get(x).getPC().toString());

        String leixinTxt = gnculd.get(x).getCargoType().toString();
        if (leixinTxt.contains("-")) {
            LeiXin.setText(gnculd.get(x).getCargoType().toString().split("-")[1]);
        } else {
            LeiXin.setText(leixinTxt);
        }

        String cFlagTxt = gnculd.get(x).getcFlag().toString();
        if (cFlagTxt.contains("-")) {
            PeiZai.setText(gnculd.get(x).getcFlag().toString().split("-")[1]);
        } else {
            PeiZai.setText(cFlagTxt);
        }

        YouXianJi.setText(gnculd.get(x).getPriority().toString());
        HangBanHao.setText(gnculd.get(x).getFno().toString());
        MuDinGang.setText(gnculd.get(x).getDest().toString());

        String fda = gnculd.get(x).getFDate().toString();
        if (fda.contains("_")) {
            RiQi.setText(gnculd.get(x).getFDate().toString().split("_")[1]);
        } else {
            RiQi.setText(fda);
        }

        ChenYunRen.setText(gnculd.get(x).getCarrier().toString());
        LiuShuiHao.setText(gnculd.get(x).getID().toString());


        BeiZhu.setText(gnculd.get(x).getRemark().toString());
        BanXin.setText(gnculd.get(x).getBoardType().toString());
        CangWei.setText(gnculd.get(x).getLocation().toString());
    }
   //endregion

    //endregion
}
