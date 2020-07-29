package com.example.administrator.aviation.ui.cgo.domestic;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.ReplacementTransformationMethod;
import android.util.ArrayMap;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.aviation.R;
import com.example.administrator.aviation.http.HttpCommons;
import com.example.administrator.aviation.http.HttpRoot;
import com.example.administrator.aviation.model.adapter.AbsCommonAdapter;
import com.example.administrator.aviation.model.adapter.AbsViewHolder;
import com.example.administrator.aviation.model.hygnc.GNCManifestVSLoading;
import com.example.administrator.aviation.model.hygnc.GNCULDLoading;
import com.example.administrator.aviation.model.hygnc.ParseGNCmessage;
import com.example.administrator.aviation.model.hygnc.ParseGncVSLoading;
import com.example.administrator.aviation.model.hygnc.ULDLoadingCargo;
import com.example.administrator.aviation.sys.PublicFun;
import com.example.administrator.aviation.tool.DateUtils;
import com.example.administrator.aviation.ui.base.AbPullToRefreshView;
import com.example.administrator.aviation.ui.base.NavBar;
import com.example.administrator.aviation.ui.base.SyncHorizontalScrollView;
import com.example.administrator.aviation.ui.base.TableModel;
import com.example.administrator.aviation.ui.dialog.LoadingDialog;
import com.example.administrator.aviation.util.AviationCommons;
import com.example.administrator.aviation.util.PreferenceUtils;
import com.example.administrator.aviation.util.ToastUtils;
import com.example.administrator.aviation.view.AutofitTextView;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;

import org.ksoap2.serialization.SoapObject;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.R.attr.x;
import static com.example.administrator.aviation.R.id.checkBox;
import static com.example.administrator.aviation.util.AviationCommons.GNC_ULDLOADING_CAMERA_REQUEST;
import static com.example.administrator.aviation.util.AviationCommons.GNC_ULDLOADING_XinZenPinBan_REQUEST;
import static com.example.administrator.aviation.util.AviationCommons.GNC_ZhuangJiDan_REQUEST;

public class ZhuangJiDanMain extends AppCompatActivity {
    //region 自定义变量
    private Context mContext;
    private Activity mAct;
    private NavBar navBar;
    //用于存放标题的id,与textview引用
    private SparseArray<TextView> mTitleTvArray;
    private AbsCommonAdapter<TableModel> mLeftAdapter, mRightAdapter;
    private final String TAG = "ZhuangJiDanMain";
    private final String page = "one";
//    private String HongSe = "";
    private String Fdate = "";
    private String Fno = "";
    private List<GNCULDLoading> gnculd;
    //endregion

    //region 未预设XML控件

    //endregion

    //region 其他控件
    @BindView(R.id.ZhuangJiDan_left_container_listview)
    ListView leftListView;
    @BindView(R.id.ZhuangJiDan_right_container_listview)
    ListView rightListView;

    private LoadingDialog Ldialog;
    //endregion

    //region Layout控件

    //endregion

    //region Button控件
    @BindView(R.id.btn_ZhuangJiDan_ChaXun)
    Button btnChaXun;
    @BindView(R.id.btn_ZhuangJiDan_QinKong)
    Button btnQinKong;
    @BindView(R.id.btn_ZhuangJiDan_BiDui)
    Button btnBiDui;
    @BindView(R.id.btn_ZhuangJiDan_JieZai)
    Button btnJieZai;
    //endregion

    //region EditText控件
    @BindView(R.id.txt_ZhuangJiDan_HangBanHao)
    EditText editHangBanHao;
    //endregion

    //region 滚动View控件
    @BindView(R.id.ZhuangJiDan_pulltorefreshview)
    AbPullToRefreshView pulltorefreshview;
    @BindView(R.id.ZhuangJiDan_title_horsv)
    SyncHorizontalScrollView titleHorScv;
    @BindView(R.id.ZhuangJiDan_content_horsv)
    SyncHorizontalScrollView contentHorScv;
    //endregion

    //region TextView控件
    @BindView(R.id.txt_ZhuangJiDan_riqi)
    AutofitTextView txt_riqi;
    @BindView(R.id.txt_ZhuangJiDan_MuDiGang)
    AutofitTextView txt_MuDiGang;
    @BindView(R.id.ZhuangJiDan_tv_table_title_left)
    TextView txt_RightTitle;
    //endregion

    //region 初始化

    //region 入口函数
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zhuangjidan_main);
        mContext = ZhuangJiDanMain.this;
        mAct = (Activity) mContext;
        ButterKnife.bind(this);
        init();
    }
    //endregion

    //region 设置初始化
    public void init() {
        navBar = new NavBar(this);
        gnculd = new ArrayList<>();;
        Ldialog = new LoadingDialog(mContext);

        navBar.setTitle("国内出港装机单");
        txt_RightTitle.setText("平板");
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        LinearLayout linearLayout = (LinearLayout)findViewById(R.id.ZhuangJiDan_right_title_container);
        layoutInflater.inflate(R.layout.zhuangjidan_table_right_title,linearLayout,true);

        // 设置两个水平控件的联动
        titleHorScv.setScrollView(contentHorScv);
        contentHorScv.setScrollView(titleHorScv);

        findTitleTextViewIds();
        initTableView();
        TxtViewSetEmpty();
        setListener();
        CallSetup();
    }
    //endregion

    //region 其他界面调用设置
    private void CallSetup(){
        Intent da = getIntent();
        Bundle bundle = da.getExtras();

        if (bundle != null) {
            String re = bundle.getString("JinChengGuanKong","");
            txt_riqi.setEnabled(false);
            editHangBanHao.setEnabled(false);
            btnQinKong.setEnabled(false);
            btnQinKong.setBackgroundColor(Color.parseColor("#979797"));
            editHangBanHao.setText(re.split("/")[0]);
            txt_riqi.setText(re.split("/")[1]);
            btnChaXun.performClick();
        }
    }
    //endregion

    //region 输入框置空
    private void TxtViewSetEmpty() {
        editHangBanHao.setText("");
        txt_MuDiGang.setText("");

        Fdate = "";
        Fno = "";

        mLeftAdapter.clearData(true);
        mRightAdapter.clearData(true);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date(System.currentTimeMillis());
        txt_riqi.setText(simpleDateFormat.format(date).toString());
    }
    //endregion

    //region 利用反射初始化标题的TextView的item引用
    private void findTitleTextViewIds() {
        mTitleTvArray = new SparseArray<>();
        for (int i = 0; i < 14; i++) {
            try {
                Field field = R.id.class.getField("ZhuangJiDan_tv_table_title_" + i);
                int key = field.getInt(new R.id());
                TextView textView = (TextView) findViewById(key);

                mTitleTvArray.put(key, textView);
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
    }
    //endregion

    //endregion

    //region 控件事件

    //region 初始化表格的view
    private void initTableView() {
        mLeftAdapter = new AbsCommonAdapter<TableModel>(mContext, R.layout.table_left_item_one) {
            @Override
            public void convert(AbsViewHolder helper, TableModel item, int pos) {
                if (TextUtils.isEmpty(item.getText0())){
                    helper.setTextColor(R.id.tv_table_content_item_left_one,"#FF0000");
                }else{
                    helper.setTextColor(R.id.tv_table_content_item_left_one,"#000000");
                }

                TextView tv_table_content_left = helper.getView(R.id.tv_table_content_item_left_one);
                tv_table_content_left.setText(item.getLeftTitle());
            }
        };

        mRightAdapter = new AbsCommonAdapter<TableModel>(mContext, R.layout.zhuangjidan_table_right_item) {
            @Override
            public void convert(AbsViewHolder helper, TableModel item, int pos) {
                TextView tv_table_content_right_item0 = helper.getView(R.id.ZhuangJiDan_tv_table_content_right_item0);
                TextView tv_table_content_right_item1 = helper.getView(R.id.ZhuangJiDan_tv_table_content_right_item1);
                TextView tv_table_content_right_item2 = helper.getView(R.id.ZhuangJiDan_tv_table_content_right_item2);
                TextView tv_table_content_right_item3 = helper.getView(R.id.ZhuangJiDan_tv_table_content_right_item3);
                TextView tv_table_content_right_item4 = helper.getView(R.id.ZhuangJiDan_tv_table_content_right_item4);
                TextView tv_table_content_right_item5 = helper.getView(R.id.ZhuangJiDan_tv_table_content_right_item5);
                TextView tv_table_content_right_item6 = helper.getView(R.id.ZhuangJiDan_tv_table_content_right_item6);
                TextView tv_table_content_right_item7 = helper.getView(R.id.ZhuangJiDan_tv_table_content_right_item7);
                TextView tv_table_content_right_item8 = helper.getView(R.id.ZhuangJiDan_tv_table_content_right_item8);
                TextView tv_table_content_right_item9 = helper.getView(R.id.ZhuangJiDan_tv_table_content_right_item9);
                TextView tv_table_content_right_item10 = helper.getView(R.id.ZhuangJiDan_tv_table_content_right_item10);
                TextView tv_table_content_right_item11 = helper.getView(R.id.ZhuangJiDan_tv_table_content_right_item11);
                TextView tv_table_content_right_item12 = helper.getView(R.id.ZhuangJiDan_tv_table_content_right_item12);
                TextView tv_table_content_right_item13 = helper.getView(R.id.ZhuangJiDan_tv_table_content_right_item13);

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

                if (item.getText12().length() > 6){
                    final String remark = item.getText12();
                    tv_table_content_right_item12.setText(item.getText12().substring(0,5) + "...");
                    tv_table_content_right_item12.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            new QMUIDialog.MessageDialogBuilder(mAct)
                                    .setTitle("备注")
                                    .setMessage(remark).show();
                        }
                    });
                }else{
                    tv_table_content_right_item12.setText(item.getText12());
                }

                tv_table_content_right_item13.setText(item.getText13());

                for (int i = 0; i < 14; i++) {
                    View view = ((LinearLayout) helper.getConvertView()).getChildAt(i);
                    view.setVisibility(View.VISIBLE);
                }
            }
        };
        leftListView.setAdapter(mLeftAdapter);
        rightListView.setAdapter(mRightAdapter);
    }
    //endregion

    //region activity界面回调事件
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case GNC_ZhuangJiDan_REQUEST:
                if (resultCode == AviationCommons.GNC_ZhuangJiDan_RESULT) {
                    pulltorefreshview.headerRefreshing();
                }
                break;
            default:
                break;

        }
    }
    //endregion

    //region 返回后触发父界面逻辑
    @Override
    public void finish() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        int num = 0;

        if (bundle != null) {
            String req =  bundle.getString("JinChengGuanKong","");
            if (!TextUtils.isEmpty(req)) {
                intent.putExtra(TAG,editHangBanHao.getText().toString().trim() + "/" + txt_riqi.getText().toString().trim());
                intent.setClass(mContext, JinChengGuanKong.class);
                num = AviationCommons.GNC_JinChenGuanKong_RESULT;
            }
            ToastUtils.hideToast();
            setResult(num,intent);
        }

        super.finish();
    }
    //endregion

    //region 页面上所有的点击事件
    private void setListener() {

        //region 航班号输入框监听键盘ENTER事件
        editHangBanHao.setOnEditorActionListener(new EditText.OnEditorActionListener() {
             @Override
             public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                 if (actionId == EditorInfo.IME_ACTION_DONE || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER))  {
                     btnChaXun.performClick();
                     return true;
                 }
                 return false;
             }
         }
        );
        //endregion

        //region 航班号自动变大写
        editHangBanHao.setTransformationMethod(new ReplacementTransformationMethod() {
            @Override
            protected char[] getOriginal() {
                char[] originalCharArr = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z' };
                return originalCharArr;
            }

            @Override
            protected char[] getReplacement() {
                char[] replacementCharArr = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z' };
                return replacementCharArr;
            }
        });
    //endregion

        //region 日期栏点击选择
        txt_riqi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickDlg();
            }
        });
        //endregion

        //region 左侧标题列监听事件
        leftListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mAct != null){
                    TableModel ta = (TableModel) parent.getItemAtPosition(position);

                    Intent intent = new Intent(mContext, expULDLoading.class);
                    intent.putExtra(TAG,ta.getLeftTitle());
                    startActivityForResult(intent, GNC_ZhuangJiDan_REQUEST);
                }
            }
        });
        //rendregion

        //region 查询按钮点击事件
        btnChaXun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(editHangBanHao.getText().toString().toUpperCase().trim())) {
                    ToastUtils.showToast(mContext,"航班号不能为空",Toast.LENGTH_SHORT);
                } else if (TextUtils.isEmpty(txt_riqi.getText().toString().toUpperCase().trim())) {
                    ToastUtils.showToast(mContext, "日期不能为空", Toast.LENGTH_SHORT);
                } else {
                    HashMap<String, String> go = new HashMap<String, String>();
                    String ri = txt_riqi.getText().toString().toUpperCase().trim() + "T00:00:00";
                    String Hangban = editHangBanHao.getText().toString().toUpperCase().trim();
                    go.put("FDate", ri);
                    go.put("Fno", Hangban);
                    go.put("ErrString", "");
                    GetInfo(go);
                }
            }
        });
        //endregion

        //region 清空按钮点击事件
        btnQinKong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TxtViewSetEmpty();
            }
        });
        //endregion

        //region 比对按钮
        btnBiDui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(editHangBanHao.getText().toString().toUpperCase().trim())) {
                    ToastUtils.showToast(mContext,"航班号不能为空",Toast.LENGTH_SHORT);
                } else if (TextUtils.isEmpty(txt_riqi.getText().toString().toUpperCase().trim())) {
                    ToastUtils.showToast(mContext, "日期不能为空", Toast.LENGTH_SHORT);
                } else {
                    if (TextUtils.isEmpty(Fdate) || TextUtils.isEmpty(Fno)) {
                        ToastUtils.showToast(mContext, "查询数据有误", Toast.LENGTH_SHORT);
                    } else {
                        if(Fno.equals(editHangBanHao.getText().toString().toUpperCase().trim()) && Fdate.equals(txt_riqi.getText().toString().toUpperCase().trim() + "T00:00:00")){
                            HashMap<String, String> go = new HashMap<String, String>();
                            go.put("FDate", Fdate);
                            go.put("Fno", Fno);
                            go.put("ErrString", "");

                            Bundle mBundle = new Bundle();
                            mBundle.putSerializable("Info", go);

                            Intent intent = new Intent(mContext, ZhuangJiDanBiDui.class);
                            intent.putExtras(mBundle);
                            startActivity(intent);
                        }else{
                            ToastUtils.showToast(mContext, "查询航班与输入航班不一致", Toast.LENGTH_SHORT);
                        }
                    }
                }
            }
        });
        //endregion

        //region 截载按钮
        btnJieZai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(Fdate) || TextUtils.isEmpty(Fno)) {
                    pulltorefreshview.onHeaderRefreshFinish();
                    ToastUtils.showToast(mContext, "请先查询数据", Toast.LENGTH_SHORT);
                } else {
                    if(Fno.equals(editHangBanHao.getText().toString().toUpperCase().trim()) && Fdate.equals(txt_riqi.getText().toString().toUpperCase().trim() + "T00:00:00")){
                        new QMUIDialog.MessageDialogBuilder(mAct)
                                .setTitle("截载上传")
                                .setMessage("确认截载吗？")
                                .addAction("取消", new QMUIDialogAction.ActionListener() {
                                    @Override
                                    public void onClick(QMUIDialog dialog, int index) {
                                        dialog.dismiss();
                                    }
                                })
                                .addAction("确认", new QMUIDialogAction.ActionListener() {
                                    @Override
                                    public void onClick(QMUIDialog dialog, int index) {
                                        dialog.dismiss();
                                        HashMap<String, String> go = new HashMap<String, String>();
                                        go.put("FDate", Fdate);
                                        go.put("Fno", Fno);
                                        go.put("ErrString", "");

                                        JieZai(go);
                                    }
                                })
                                .show();
                    }else {
                        ToastUtils.showToast(mContext, "查询航班与截载航班不一致", Toast.LENGTH_SHORT);
                    }
                }
            }
        });
        //endregion

        //region 下拉刷新
        pulltorefreshview.setOnHeaderRefreshListener(new AbPullToRefreshView.OnHeaderRefreshListener() {
            @Override
            public void onHeaderRefresh(AbPullToRefreshView view) {
                if (TextUtils.isEmpty(Fdate) || TextUtils.isEmpty(Fno)) {
                    pulltorefreshview.onHeaderRefreshFinish();
                    ToastUtils.showToast(mContext, "查询数据有误", Toast.LENGTH_SHORT);
                } else {
                    HashMap<String, String> go = new HashMap<String, String>();
                    go.put("FDate", Fdate);
                    go.put("Fno", Fno);
                    go.put("ErrString", "");

                    GetInfo(go);
                }
            }
        });
        //endregion

    }
    //endregion



    //endregion

    //region 功能方法

    //region 显示时间选择控件
    protected void showDatePickDlg() {
        final Calendar calendar = Calendar.getInstance();
        int yy = calendar.get(Calendar.YEAR);
        int mm = calendar.get(Calendar.MONTH);
        int dd = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dlg = new DatePickerDialog(new android.view.ContextThemeWrapper(mAct,
                android.R.style.Theme_Holo_Light_Dialog_NoActionBar), null, yy, mm, dd);
        dlg.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String ymd = year + "-" + (month + 1) + "-" + dayOfMonth;
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                txt_riqi.setText(formatter.format(DateUtils.convertFromStrYMD(ymd)));
            }
        });
        dlg.show();
    }
    //endregion

    //region 句柄监听
    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == AviationCommons.GNC_ManifestLoading) {
                if (gnculd.size() == 0) {
                    ToastUtils.showToast(mContext,"数据为空",Toast.LENGTH_SHORT);
                    clearTableView();
                }else {
                    setDatas(gnculd,AviationCommons.REFRESH_DATA);
                }

                Ldialog.dismiss();
            } else if (msg.what == 111) {
//                BianSe(HongSe,leftListView);
            }
            return false;
        }
    });
    //endregion

    //region 请求数据
    private void GetInfo(Map<String, String> p) {
        titleHorScv.scrollTo(0,0);
        Ldialog.show();
        Fdate = "";
        Fno = "";

        HttpRoot.getInstance().requstAync(mContext, HttpCommons.GET_GNC_ManifestLoading_NAME, HttpCommons.GET_GNC_ManifestLoading_ACTION, p,
                new HttpRoot.CallBack() {
                    @Override
                    public void onSucess(Object result) {
                        SoapObject object = (SoapObject) result;
                        String Exp_ULDLoading = object.getProperty(0).toString();
                        gnculd = ParseGNCmessage.parseGNCULDLoadingXMLto(Exp_ULDLoading);

                        mHandler.sendEmptyMessage(AviationCommons.GNC_ManifestLoading);
                        Ldialog.dismiss();
                    }

                    @Override
                    public void onFailed(String message) {
                        Ldialog.dismiss();
                        ToastUtils.showToast(mContext,message,Toast.LENGTH_SHORT);

                        gnculd = new ArrayList<GNCULDLoading>();
                        mHandler.sendEmptyMessage(AviationCommons.GNC_ManifestLoading);
                    }

                    @Override
                    public void onError() {
                        Ldialog.dismiss();
                        ToastUtils.showToast(mContext,"数据获取出错",Toast.LENGTH_SHORT);

                        gnculd = new ArrayList<GNCULDLoading>();
                        mHandler.sendEmptyMessage(AviationCommons.GNC_ManifestLoading);
                    }
                },page);
    }
    //endregion

    //region 截载上传
    private void JieZai(Map<String, String> p) {

        HttpRoot.getInstance().requstAync(mContext, HttpCommons.CGO_DOM_GNCLockLoading_NAME, HttpCommons.CGO_DOM_GNCLockLoading_ACTION, p,
                new HttpRoot.CallBack() {
                    @Override
                    public void onSucess(Object result) {
                        SoapObject object = (SoapObject) result;
                        String res = object.getProperty(0).toString();
                        if (res.equalsIgnoreCase("true")) {
                            ToastUtils.showToast(mContext, "截载成功", Toast.LENGTH_SHORT);
                        } else {
                            ToastUtils.showToast(mContext, object.getProperty(1).toString(), Toast.LENGTH_SHORT);
                        }

                    }

                    @Override
                    public void onFailed(String message) {
                        Ldialog.dismiss();
                        ToastUtils.showToast(mContext,message,Toast.LENGTH_SHORT);
                    }

                    @Override
                    public void onError() {
                        Ldialog.dismiss();
                        ToastUtils.showToast(mContext,"截载上传出错",Toast.LENGTH_SHORT);
                    }
                },page);
    }
    //endregion

    //region 把数据绑定到Model
    private void setDatas(List<GNCULDLoading> CGO, int type) {
        pulltorefreshview.setLoadMoreEnable(false);
//        pulltorefreshview.setPullRefreshEnable(false);
//        HongSe = "";

        if (CGO.size() > 0) {
            List<TableModel> mDatas = new ArrayList<>();
            for (int i = 0; i < CGO.size(); i++) {
                GNCULDLoading cc = CGO.get(i);
                TableModel tableMode = new TableModel();
                tableMode.setOrgCode(cc.getID() + "");


                tableMode.setLeftTitle(cc.getCarID() + "");
                tableMode.setText0(cc.getLocation() + "");//列0内容
                tableMode.setText1(cc.getULD() + "");//列1内容
                tableMode.setText2(cc.getULDWeight() + "");//列2内容
                tableMode.setText3(cc.getBoardType() + "");
                tableMode.setText4(cc.getNetWeight() + "");
                tableMode.setText5(cc.getCargoWeight() + "");//
                tableMode.setText6(cc.getVolume() + "");//
                tableMode.setText7(cc.getPC() + "");//

                String leixinTxt = cc.getCargoType().toString();
                if (leixinTxt.contains("-")) {
                    tableMode.setText8(cc.getCargoType().split("-")[1] + "");//
                }else {
                    tableMode.setText8(cc.getCargoType() + "");//
                }

                tableMode.setText9(cc.getMailWeight() + "");//
                tableMode.setText10(cc.getDest() + "");//
                tableMode.setText11(cc.getPriority() + "");
                tableMode.setText12(cc.getRemark() + "");//

                String cFlagTxt = cc.getcFlag().toString();
                if (cFlagTxt.contains("-")) {
                    tableMode.setText13(cc.getcFlag().split("-")[1] + "");//
                } else {
                    tableMode.setText13(cc.getcFlag() + "");//
                }

                mDatas.add(tableMode);
            }
            boolean isMore;
            if (type == AviationCommons.LOAD_DATA) {
                isMore = true;
            } else {
                isMore = false;
            }
            mLeftAdapter.addData(mDatas, isMore);
            mRightAdapter.addData(mDatas, isMore);

            mDatas.clear();
        } else {
            mLeftAdapter.clearData(true);
            mRightAdapter.clearData(true);
        }

        pulltorefreshview.onHeaderRefreshFinish();

        Fdate = txt_riqi.getText().toString().toUpperCase().trim() + "T00:00:00";
        Fno = editHangBanHao.getText().toString().toUpperCase().trim();
    }
    //endregion

    //region 清空tableview
    private void clearTableView(){
        mLeftAdapter.clearData(true);
        mRightAdapter.clearData(true);
        pulltorefreshview.onHeaderRefreshFinish();

        Fdate = "";
        Fno = "";
    }
    //endregion

    //endregion

}
