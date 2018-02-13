package com.example.administrator.aviation.ui.cgo.domestic;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
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
import com.example.administrator.aviation.ui.base.NavBar;
import com.example.administrator.aviation.util.AviationCommons;
import com.example.administrator.aviation.util.ToastUtils;

import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.administrator.aviation.util.AviationCommons.GNC_ULDLOADING_CAMERA_REQUEST;
import static com.example.administrator.aviation.util.AviationCommons.GNC_ULDLOADING_XinZenPinBan_REQUEST;

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
public class expULDLoading extends AppCompatActivity {
    @BindView(R.id.uldloading_proBar)
    ProgressBar proBar;
    @BindView(R.id.uldloading_Img_SaoMa)
    ImageView Img_SaoMa;
    @BindView(R.id.uldloading_Scrl)
    ScrollView scrollview;
    @BindView(R.id.uldloading_Layout_Tijiao)
    LinearLayout LayTiJiao;
    @BindView(R.id.uldloading_Lay_PB)
    LinearLayout Lay_PB;
    @BindView(R.id.uldloading_navBar)
    ViewGroup uldloading_navBar;

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
    @BindView(R.id.uldloading_tv_LiuShuiHao)
    TextView LiuShuiHao;
    @BindView(R.id.uldloading_tv_BanXin)
    TextView BanXin;
    @BindView(R.id.uldloading_tv_CangWei)
    TextView CangWei;

    @BindView(R.id.uldloading_Lay_YinCan)
    LinearLayout LayYincang;
    @BindView(R.id.uldloading_tv_BeiZhu)
    EditText BeiZhu;

    private NavBar navBar;
    private PopupWindow pw;
    private Context mContext;
    private Activity mAct;

    private final String TAG = "expULDLoadingLog";
    private final String page = "one";
    private static String PinBan_Two = "";
    private static List<GNCULDLoading> gnculd = new ArrayList<>();
    private static ArrayList<String> list = new ArrayList<>();

    //region 初始化

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = expULDLoading.this;
        mAct = (Activity) mContext;
        setContentView(R.layout.exp_uldloading_activity);
        ButterKnife.bind(this);
        initView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.gc();
    }

    private void initView() {
        navBar = new NavBar(this);
        navBar.setTitle("货物装载");
        navBar.setRight(R.drawable.detail_0);
        LayTiJiao.setVisibility(View.GONE);
        LayYincang.setVisibility(View.GONE);
        TxtViewSetEmpty();
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
        PinBan_Two = "";

        BeiZhu.setText("");
        BanXin.setText("");
        CangWei.setText("");
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
                        PublicFun.KeyBoardHide(mAct,mContext);
                        params.put("ID", re.split("/")[0].trim());
                        params.put("CarID", "");
                        params.put("ULD", re.split("/")[1].trim());
                        params.put("ErrString", "");
                        proBar.setVisibility(View.VISIBLE);

                        GetInfo(params);
                    }
                }
            case 3:

        }
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
                if (!TextUtils.isEmpty(pinBan)) {
                    OpenWri();

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            scrollview.fullScroll(ScrollView.FOCUS_DOWN);
                        }
                    });
                }
            }
        });
        //endregion

        //region 提交按钮点击
        btn_Tijiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        //endregion

        //region 取消按钮点击
        btn_Quxiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CloseWri();
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
                    proBar.setVisibility(View.VISIBLE);


                    GetInfo(params);
                } else {
                    TxtViewSetEmpty();
                }
            }
        });
        //endregion

        //region 下拉菜单点击事件
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
                    list.add("1");
                    list.add("2");
                    list.add("3");

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
    }
    //endregion

    //endregion

    //region 功能方法

    //region 打开编辑区
    private void OpenWri() {
        LayTiJiao.setVisibility(View.VISIBLE);

        BeiZhu.setBackgroundResource(R.drawable.edit_bg);
        CangWei.setBackgroundResource(R.drawable.selector_sinner);
        YouXianJi.setBackgroundResource(R.drawable.selector_sinner);
        uldBianHao.setBackgroundResource(0);

        BeiZhu.setFocusable(true);
        BeiZhu.setFocusableInTouchMode(true);
        BeiZhu.requestFocus();

        PublicFun.ElementSwitch(Lay_PB,false);
        PublicFun.ElementSwitch(uldloading_navBar,false);
    }
    //endregion

    //region 关闭编辑区
    private void CloseWri() {
        LayTiJiao.setVisibility(View.GONE);

        BeiZhu.setBackgroundResource(0);
        CangWei.setBackgroundResource(0);
        YouXianJi.setBackgroundResource(0);
        uldBianHao.setBackgroundResource(R.drawable.selector_sinner);

        BeiZhu.setFocusable(false);
        BeiZhu.setFocusableInTouchMode(false);

        PublicFun.ElementSwitch(Lay_PB,true);
        PublicFun.ElementSwitch(uldloading_navBar,true);
    }
    //endregion

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
                    PinBan_Two = PinBanHao_one.getText().toString().trim();
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

    //endregion
}
