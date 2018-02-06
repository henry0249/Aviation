package com.example.administrator.aviation.ui.cgo.domestic;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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
import com.example.administrator.aviation.ui.base.NavBar;
import com.example.administrator.aviation.util.AviationCommons;
import com.example.administrator.aviation.util.ToastUtils;

import org.ksoap2.serialization.SoapObject;

import java.security.KeyStore;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.R.id.list;
import static com.example.administrator.aviation.util.AviationCommons.GNC_ULDLOADING_CAMERA_REQUEST;
import static com.example.administrator.aviation.util.AviationCommons.GNC_ULDinfo_CAMERA_REQUEST;

public class expULDLoading extends AppCompatActivity {
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

    @BindView(R.id.uldloading_linLay_XinZenSheBei)
    LinearLayout XinZenSheBei;
    @BindView(R.id.uldloading_proBar)
    ProgressBar proBar;
    @BindView(R.id.uldloading_Img_SaoMa)
    ImageView Img_SaoMa;
    @BindView(R.id.uldloading_Scrl)
    ScrollView scrollview;

    @BindView(R.id.uldloading_Btn_ChaXun)
    Button ChaXun;
    @BindView(R.id.uldloading_Btn_XinZen)
    Button XinZen;
    @BindView(R.id.uldloading_Btn_QueDin)
    Button QueDin;
    @BindView(R.id.uldloading_Btn_QuXiao)
    Button QuXiao;
    @BindView(R.id.uldloading_Btn_QinKong)
    Button QinKong;

    @BindView(R.id.uldloading_edtTxt_PinBanHao_one)
    EditText PinBanHao_one;
    @BindView(R.id.uldloading_tv_uldBianHao)
    TextView uldBianHao;
    @BindView(R.id.uldloading_tv_ZiZhong)
    TextView ZiZhong;
    @BindView(R.id.uldloading_tv_JinZhong)
    TextView JinZhong;
    @BindView(R.id.uldloading_tv_HuoZhong)
    TextView HuoZhong;
    @BindView(R.id.uldloading_tv_TiJi)
    TextView TiJi;
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
    @BindView(R.id.uldloading_tv_BeiZhu)
    TextView BeiZhu;
    @BindView(R.id.uldloading_tv_LiuShuiHao)
    TextView LiuShuiHao;
    @BindView(R.id.uldloading_tv_BanXin)
    TextView BanXin;
    @BindView(R.id.uldloading_tv_CangWei)
    TextView CangWei;

    @BindView(R.id.uldloading_edtTxt_PinBanHao_A)
    EditText PinBanHao_A;
    @BindView(R.id.uldloading_edtTxt_PinBanZhong_A)
    EditText PinBanZhong_A;
    @BindView(R.id.uldloading_edtTxt_uldBianHao_A)
    EditText uldBianHao_A;
    @BindView(R.id.uldloading_edtTxt_uldBianZhong_A)
    EditText uldBianZhong_A;
    @BindView(R.id.uldloading_edtTxt_uldBianHao_B)
    EditText uldBianHao_B;
    @BindView(R.id.uldloading_edtTxt_uldBianZhong_B)
    EditText uldBianZhong_B;

    private NavBar navBar;
    private PopupWindow pw;

    private final String TAG = "expULDLoadingLog";
    private final String page = "one";
    private List<GNCULDLoading> gnculd = new ArrayList<>();
    private ArrayList<String> list = new ArrayList<>();

    //region 初始化

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exp_uldloading_activity);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        navBar = new NavBar(this);
        navBar.setTitle("货物装载");
        navBar.setRight(R.drawable.detail_0);
        XinZenSheBei.setVisibility(View.GONE);
        TxtViewSetEmpty();
        EditViewSetEmpty();
        setListener();
    }

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

        BeiZhu.setText("");
        BanXin.setText("");
        CangWei.setText("");
    }

    private void EditViewSetEmpty() {
        PinBanHao_A.setText("");
        PinBanZhong_A.setText("");
        uldBianHao_A.setText("");
        uldBianZhong_A.setText("");
        uldBianHao_B.setText("");
        uldBianZhong_B.setText("");
    }

    //endregion

    //region 控件事件

    //region activity界面回调事件
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
                if (resultCode == AviationCommons.GNC_ULDLOADING_CAMERA_RESULT) {
                    Bundle bundle = data.getExtras();
                    String re = bundle.getString("result");
                    QinKong.performClick();

                    if (!TextUtils.isEmpty(re)) {
                        Map<String, String> params = new HashMap<>();
                        KeyBoardHide();
                        params.put("ID", re.split("/")[0].trim());
                        params.put("CarID", "");
                        params.put("ULD", re.split("/")[1].trim());
                        params.put("ErrString", "");
                        proBar.setVisibility(View.VISIBLE);

                        GetInfo(params);
                    }
                }
        }
    }
    //endregion

    //region 页面上所有的点击事件
    private void setListener() {

        //region navBar的点击事件
        navBar.getRightImageView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (gnculd.size() > 0 && !TextUtils.isEmpty(LiuShuiHao.getText().toString().trim())) {
                    HashMap<String, String> go = new HashMap<String, String>();
                    go.put("ID",LiuShuiHao.getText().toString().trim());
                    go.put("ULD", uldBianHao.getText().toString().trim());
                    go.put("BanID", PinBanHao_one.getText().toString().trim());
                    go.put("ErrString", "");

                    Bundle mBundle = new Bundle();
                    mBundle.putSerializable("Info",go);

                    Intent intent = new Intent(expULDLoading.this,expULDcargoInfo.class);
                    intent.putExtras(mBundle);
                    startActivity(intent);
                }
            }
        });
        //endregion

        //region 新增按钮的点击事件
        XinZen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (XinZenSheBei.getVisibility() == View.VISIBLE) {
                    XinZen.setText("新增");
                    QuXiao.performClick();
                } else if (XinZenSheBei.getVisibility() == View.GONE) {
                    XinZen.setText("关闭");
                    XinZenSheBei.setVisibility(View.VISIBLE);
                    new Handler().post(new Runnable() {
                        @Override
                        public void run() {
                            scrollview.fullScroll(ScrollView.FOCUS_DOWN);
                            PinBanHao_A.setFocusable(true);
                            PinBanHao_A.setFocusableInTouchMode(true);
                            PinBanHao_A.requestFocus();
                        }
                    });
                }



            }
        });
        //endregion

        //region 取消按钮的点击事件
        QuXiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditViewSetEmpty();
                XinZenSheBei.setVisibility(View.GONE);
                if (XinZen.getText() == "关闭") {
                    XinZen.setText("新增");
                }
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

                    KeyBoardHide();
                    params.put("ID", "0");
                    params.put("CarID", pinBan);
                    params.put("ULD", "");
                    params.put("ErrString", "");
                    proBar.setVisibility(View.VISIBLE);

                    GetInfo(params);
                }
            }
        });
        //endregion

        //region 下拉菜单点击事件
        uldBianHao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KeyBoardHide();
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

    }
    //endregion

    //endregion

    //region 功能方法

    //region 句柄监听
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == AviationCommons.GNC_expULDLoading) {
                if (gnculd.size() > 0) {
                    TextSetVaule(0);
                    list = new ArrayList<>();
                    PinBanHao_one.setSelection(PinBanHao_one.getText().toString().trim().length());
                } else {
                    ToastUtils.showToast(expULDLoading.this,"数据为空",Toast.LENGTH_SHORT);
                    TxtViewSetEmpty();
                }
            }
        }
    };
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
                        proBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onFailed(String message) {
                        proBar.setVisibility(View.GONE);
                        ToastUtils.showToast(expULDLoading.this,"数据获取失败",Toast.LENGTH_SHORT);
                    }

                    @Override
                    public void onError() {
                        proBar.setVisibility(View.GONE);
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
        PinBanHao_one.setText(gnculd.get(x).getCarID().toString());

        uldBianHao.setText(gnculd.get(x).getULD().toString());

        ZiZhong.setText(gnculd.get(x).getULDWeight().toString());
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
        RiQi.setText(gnculd.get(x).getFDate().toString());
        ChenYunRen.setText(gnculd.get(x).getCarrier().toString());
        LiuShuiHao.setText(gnculd.get(x).getID().toString());

        BeiZhu.setText(gnculd.get(x).getRemark().toString());
        BanXin.setText(gnculd.get(x).getBoardType().toString());
        CangWei.setText(gnculd.get(x).getLocation().toString());
    }
   //endregion

    //region 软键盘状态切换
    private void KeyBoardSwitch() {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        // 得到InputMethodManager的实例
        if (imm.isActive()) {
            // 如果开启
            imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT,
                    InputMethodManager.HIDE_NOT_ALWAYS);

        }
    }
    //endregion

    //region 隐藏软键盘
    private void KeyBoardHide() {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        if(imm.isActive() && getCurrentFocus()!=null){
            if (getCurrentFocus().getWindowToken()!=null) {
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }
    //endregion

    // region 点击空白处隐藏软键盘
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        KeyBoardHide();
        return super.onTouchEvent(event);
    }
    //endregion

    //endregion
}
