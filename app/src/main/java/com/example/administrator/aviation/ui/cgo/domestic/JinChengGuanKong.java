package com.example.administrator.aviation.ui.cgo.domestic;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
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
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.aviation.R;
import com.example.administrator.aviation.http.HttpCommons;
import com.example.administrator.aviation.http.HttpRoot;
import com.example.administrator.aviation.model.adapter.AbsCommonAdapter;
import com.example.administrator.aviation.model.adapter.AbsViewHolder;
import com.example.administrator.aviation.model.adapter.PopWindowsAdapter;
import com.example.administrator.aviation.model.hygnc.GncFlightControl;
import com.example.administrator.aviation.model.hygnc.ParseGncFlightControl;
import com.example.administrator.aviation.tool.DateUtils;
import com.example.administrator.aviation.tool.TanChuPaiXu.DragSortDialog;
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
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class JinChengGuanKong extends AppCompatActivity {

    //region 自定义变量
    private Context mContext;
    private Activity mAct;
    private NavBar navBar;
    //用于存放标题的id,与textview引用
    private SparseArray<TextView> mTitleTvArray;
    private AbsCommonAdapter<TableModel> mLeftAdapter, mRightAdapter;

    private final String TAG = "JinChengGuanKong";
    private final String page = "one";
    private HashMap<String,String> store = new HashMap();
    private List<GncFlightControl> flightControls;
    private String Fdate = "";
    private String Fno = "";
    private String LiHuoTiShi="";
    private int LiHuoJiShu = 0;
    private int newLiHuoJiShu = 0;
    private String Depoff = "true";
//    private int XiaLaJiShu = 0;
    private final String yuzhiTitle = "航班日期 航程 平板数 净重 预计起飞 实际起飞 预计到达 实际到达 航班状态 延误原因 机号 机型 机位 理货开始 理货结束 截载 交接 地服";
    //endregion

    //region 未预设XML控件

    //endregion

    //region 其他控件
    private LoadingDialog Ldialog;
    @BindView(R.id.JinChen_left_container_listview)
    ListView leftListView;
    @BindView(R.id.JinChen_right_container_listview)
    ListView rightListView;
    @BindView(R.id.checkbox_JinChen_Depoff)
    CheckBox DepoffCheckcBox;
    private PopupWindow pw;
    private  DragSortDialog dialog;
    //endregion

    //region Layout控件

    //endregion

    //region Button控件
    @BindView(R.id.btn_JinChen_KaiShi)
    Button btnLiHuoKaiShi;
    @BindView(R.id.btn_JinChen_JieShu)
    Button btnLiHuoJieShu;
    //endregion

    //region EditText控件
    @BindView(R.id.txt_JinChen_HangBanHao)
    EditText editHangBanHao;
    //endregion

    //region 滚动View控件
    @BindView(R.id.JinChen_pulltorefreshview)
    SwipeRefreshLayout pulltorefreshview;
    @BindView(R.id.JinChen_title_horsv)
    SyncHorizontalScrollView titleHorScv;
    @BindView(R.id.JinChen_content_horsv)
    SyncHorizontalScrollView contentHorScv;
    //endregion

    //region TextView控件
    @BindView(R.id.txt_JinChen_riqi)
    AutofitTextView txt_riqi;
    @BindView(R.id.JinChen_tv_table_title_left)
    TextView txt_RightTitle;
    //endregion

    //region ImgView控件
    @BindView(R.id.Img_JinChen_ChaXun)
    ImageView ImgChaXun;
    @BindView(R.id.Img_JinChen_QinKong)
    ImageView ImgQinKong;
    //endregion

    //region 初始化

    //region 入口函数
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jin_cheng_guan_kong_activity);
        mContext = JinChengGuanKong.this;
        mAct = (Activity) mContext;
        ButterKnife.bind(this);
        init();
    }
    //endregion

    //region 设置初始化
    public void init() {
        navBar = new NavBar(this);

        Ldialog = new LoadingDialog(mContext);
        dialog = new DragSortDialog(mContext);
        String  model = android.os.Build.MODEL;
        if (model.equals("HDN-L09")){dialog.setTxSize(8.0f);}
        navBar.setTitle("航班进程管控");
        navBar.setRight(R.drawable.ic_menu_two);
        txt_RightTitle.setText("航班号");
        DepoffCheckcBox.setChecked(true);
        //设置下拉的距离和动画颜色
        pulltorefreshview.setProgressViewEndTarget (true,100);
        pulltorefreshview.setDistanceToTriggerSync(150);
        pulltorefreshview.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light, android.R.color.holo_orange_light, android.R.color.holo_red_light);

        LayoutInflater layoutInflater = LayoutInflater.from(this);
        LinearLayout linearLayout = (LinearLayout)findViewById(R.id.JinChen_right_title_container);
        layoutInflater.inflate(R.layout.jinchengguankong_right_title,linearLayout,true);


        // 设置两个水平控件的联动
        titleHorScv.setScrollView(contentHorScv);
        contentHorScv.setScrollView(titleHorScv);

        findTitleTextViewIds();
        initTableView();
        TxtViewSetEmpty();
        setListener();
    }
    //endregion

    //region 输入框置空
    private void TxtViewSetEmpty() {
        editHangBanHao.setText("");

        Fdate = "";
        Fno = "";
//        XiaLaJiShu = 0;
        store.clear();

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
        for (int i = 0; i < 19; i++) {
            try {
                Field field = R.id.class.getField("JinChengGuanKong_tv_table_title_" + i);
                int key = field.getInt(new R.id());
                TextView textView = (TextView) findViewById(key);

                mTitleTvArray.put(key, textView);
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
        RightTitleSetValue();
    }
    //endregion

    //region 右侧标题动态赋值
    private void RightTitleSetValue(){
        String title = PreferenceUtils.getjcgk(mContext);

        if(!TextUtils.isEmpty(title) && mTitleTvArray.size() > 0){
            String[] ts = title.split(" ");
            for (int i=0;i<ts.length;i++){
                int key = 0;
                key = mTitleTvArray.keyAt(i);
                AppCompatTextView tx =  (AppCompatTextView)mTitleTvArray.get(key);
                tx.setText(ts[i]);
            }
        }
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
            }
        };

        mRightAdapter = new AbsCommonAdapter<TableModel>(mContext, R.layout.jinchenguankong_right_item) {
            @Override
            public void convert(AbsViewHolder helper, TableModel item, int pos) {
                TextView tv_table_content_right_item0 = helper.getView(R.id.JinChengGuanKong_tv_table_content_right_item0);
                TextView tv_table_content_right_item1 = helper.getView(R.id.JinChengGuanKong_tv_table_content_right_item1);
                TextView tv_table_content_right_item2 = helper.getView(R.id.JinChengGuanKong_tv_table_content_right_item2);
                TextView tv_table_content_right_item3 = helper.getView(R.id.JinChengGuanKong_tv_table_content_right_item3);
                TextView tv_table_content_right_item4 = helper.getView(R.id.JinChengGuanKong_tv_table_content_right_item4);
                TextView tv_table_content_right_item5 = helper.getView(R.id.JinChengGuanKong_tv_table_content_right_item5);
                TextView tv_table_content_right_item6 = helper.getView(R.id.JinChengGuanKong_tv_table_content_right_item6);
                TextView tv_table_content_right_item7 = helper.getView(R.id.JinChengGuanKong_tv_table_content_right_item7);
                TextView tv_table_content_right_item8 = helper.getView(R.id.JinChengGuanKong_tv_table_content_right_item8);
                TextView tv_table_content_right_item9 = helper.getView(R.id.JinChengGuanKong_tv_table_content_right_item9);
                TextView tv_table_content_right_item10 = helper.getView(R.id.JinChengGuanKong_tv_table_content_right_item10);
                TextView tv_table_content_right_item11 = helper.getView(R.id.JinChengGuanKong_tv_table_content_right_item11);
                TextView tv_table_content_right_item12 = helper.getView(R.id.JinChengGuanKong_tv_table_content_right_item12);
                TextView tv_table_content_right_item13 = helper.getView(R.id.JinChengGuanKong_tv_table_content_right_item13);
                TextView tv_table_content_right_item14 = helper.getView(R.id.JinChengGuanKong_tv_table_content_right_item14);
                TextView tv_table_content_right_item15 = helper.getView(R.id.JinChengGuanKong_tv_table_content_right_item15);
                TextView tv_table_content_right_item16 = helper.getView(R.id.JinChengGuanKong_tv_table_content_right_item16);
                TextView tv_table_content_right_item17 = helper.getView(R.id.JinChengGuanKong_tv_table_content_right_item17);
                TextView tv_table_content_right_item18 = helper.getView(R.id.JinChengGuanKong_tv_table_content_right_item18);

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
                tv_table_content_right_item14.setText(item.getText14());
                tv_table_content_right_item15.setText(item.getText15());
                tv_table_content_right_item16.setText(item.getText16());
                tv_table_content_right_item17.setText(item.getText17());
                tv_table_content_right_item18.setText(item.getText18());

                if (!TextUtils.isEmpty(item.getText18()) && !TextUtils.isEmpty(item.getText19())){
                    final ArrayMap<String,String> aa = new ArrayMap<>();
                    aa.put("航班日期",item.getText19());
                    aa.put("航班号",item.getText18());
                    aa.put("航程",item.getText20());
                    aa.put("过站量",item.getText21());
                    aa.put("预计起飞",item.getText22());
                    aa.put("实际起飞",item.getText23());
                    aa.put("预计到达",item.getText24());
                    aa.put("实际到达",item.getText25());

                    tv_table_content_right_item18.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            new QMUIDialog.MessageDialogBuilder(mAct)
                                    .setTitle("前置航班")
                                    .setMessage("航班日期: " + aa.get("航班日期") + "\n"
                                                 + "航班号: " + aa.get("航班号") + "\n"
                                                 + "航程: " + aa.get("航程") + "\n"
                                                 + "过站量: " + aa.get("过站量") + "\n"
                                            + "预计起飞: " + aa.get("预计起飞") + "\n"
                                            + "实际起飞: " + aa.get("实际起飞") + "\n"
                                            + "预计到达: " + aa.get("预计到达") + "\n"
                                            + "实际到达: " + aa.get("实际到达") + "\n").show();
                        }
                    });
                }

                for (int i = 0; i < 19; i++) {
                    View view = ((LinearLayout) helper.getConvertView()).getChildAt(i);
                    view.setVisibility(View.VISIBLE);
                }
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

        //region 航班号输入框监听键盘ENTER事件
        editHangBanHao.setOnEditorActionListener(new EditText.OnEditorActionListener() {
             @Override
             public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                 if (actionId == EditorInfo.IME_ACTION_DONE || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER))  {
                     ImgChaXun.performClick();
                     return true;
                 }
                 return false;
             }
         }
        );
        //endregion

        //region 过滤起飞单选框
        DepoffCheckcBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    Depoff = "true";
                } else {
                    Depoff = "false";
                }
            }
        });
        //endregion

        //region 左侧标题列监听事件
        leftListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mAct != null){
                    CheckBox checkBox = (CheckBox) view.findViewById(R.id.item_cb);
                    TableModel ta = (TableModel) parent.getItemAtPosition(position);
                    if (checkBox.isChecked()) {
                        checkBox.setChecked(false);
                        store.remove(ta.getLeftTitle().toString());
                    } else {
                        checkBox.setChecked(true);
                        boolean flag = store.containsKey(ta.getLeftTitle().toString());
                        if (!flag) {
                            store.put(ta.getLeftTitle().toString().trim(),"");
                        }

                    }
                }
            }
        });
        //rendregion

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

        //region 查询按钮
        ImgChaXun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(txt_riqi.getText().toString().toUpperCase().trim())) {
                    ToastUtils.showToast(mContext, "日期不能为空", Toast.LENGTH_SHORT);
                }else {
                    HashMap<String, String> go = new HashMap<String, String>();
                    String ri = txt_riqi.getText().toString().toUpperCase().trim() + "T00:00:00";
                    String Hangban = editHangBanHao.getText().toString().toUpperCase().trim();
                    go.put("FDate", ri);
                    go.put("Fno", Hangban);
                    go.put("DepOff",Depoff);
                    go.put("ErrString", "");
                    GetInfo(go);
                    Ldialog.show();
                }
            }
        });
        //endregion

        //region 清空按钮
        ImgQinKong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TxtViewSetEmpty();
            }
        });
        //endregion

        //region 理货开始按钮
        btnLiHuoKaiShi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAct != null && store.size() > 0) {
                        new QMUIDialog.MessageDialogBuilder(mAct)
                                .setTitle("理货开始")
                                .setMessage("确认理货开始吗？")
                                .addAction("取消", new QMUIDialogAction.ActionListener() {
                                    @Override
                                    public void onClick(QMUIDialog dialog, int index) {
                                        dialog.dismiss();
                                    }
                                })
                                .addAction("确定", new QMUIDialogAction.ActionListener() {
                                    @Override
                                    public void onClick(QMUIDialog dialog, int index) {
                                        dialog.dismiss();
                                        lihuo("start");
                                        store.clear();
                                        ImgChaXun.performClick();
                                    }
                                })
                                .show();

                } else {
                    ToastUtils.showToast(mContext, "请先选择数据", Toast.LENGTH_SHORT);
                }
            }
        });
        //endregion

        //region 理货结束按钮
        btnLiHuoJieShu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAct != null && store.size() > 0) {
                    new QMUIDialog.MessageDialogBuilder(mAct)
                            .setTitle("理货结束")
                            .setMessage("确认理货结束吗？")
                            .addAction("取消", new QMUIDialogAction.ActionListener() {
                                @Override
                                public void onClick(QMUIDialog dialog, int index) {
                                    dialog.dismiss();
                                }
                            })
                            .addAction("确定", new QMUIDialogAction.ActionListener() {
                                @Override
                                public void onClick(QMUIDialog dialog, int index) {
                                    dialog.dismiss();
                                    lihuo("end");
                                    store.clear();
                                    ImgChaXun.performClick();
                                }
                            })
                            .show();

                } else {
                    ToastUtils.showToast(mContext, "请先选择数据", Toast.LENGTH_SHORT);
                }

            }
        });
        //endregion

        //region 下拉刷新
        pulltorefreshview.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (TextUtils.isEmpty(Fdate)) {
                    pulltorefreshview.setRefreshing(false);
                    ToastUtils.showToast(mContext, "查询数据有误", Toast.LENGTH_SHORT);
                } else {
                    HashMap<String, String> go = new HashMap<String, String>();
                    go.put("FDate", Fdate);
                    go.put("Fno", Fno);
                    go.put("DepOff",Depoff);
                    go.put("ErrString", "");
                    GetInfo(go);
                }

            }
        });

        //region 标题栏右侧图片点击按钮
        navBar.getRightImageView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View myView = LayoutInflater.from(mContext).inflate(R.layout.pop_expuld_info, null);
                pw = new PopupWindow(myView, 400, ViewGroup.LayoutParams.WRAP_CONTENT, true);
                pw.showAsDropDown(navBar.getPopMenuView());

                List list = new ArrayList<String>();
                list.add(0,"数据项排序");

                PopWindowsAdapter ul = new PopWindowsAdapter(mContext, R.layout.pop_expuld_list_item, list);
                ListView lv = (ListView) myView.findViewById(R.id.list_pop_expUld);
                lv.setAdapter(ul);

                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                        pw.dismiss();
                        showDialog(view);

                    }
                });
            }
        });
        //endregion

//        pulltorefreshview.setOnFooterLoadListener(new AbPullToRefreshView.OnFooterLoadListener() {
//            @Override
//            public void onFooterLoad(AbPullToRefreshView view) {
//                setDatas(flightControls,AviationCommons.LOAD_DATA);
//                pulltorefreshview.onFooterLoadFinish();
//            }
//        });
    }

    //endregion

    //endregion

    //region 功能方法

    //region 句柄监听
    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == AviationCommons.GNC_FlightControls) {
                if (flightControls.size() == 0) {
                    ToastUtils.showToast(mContext,"数据为空",Toast.LENGTH_SHORT);
                    clearTableView();
                    Ldialog.dismiss();
                }else {
                    setDatas(flightControls,AviationCommons.REFRESH_DATA);
                    mHandler.postDelayed(new Runnable(){
                        public void run() {
                            //execute the task
                            Ldialog.dismiss();
                        }
                    }, 1000);
                }
            }else if(msg.what == 666){
                newLiHuoJiShu  += 1;
                if (newLiHuoJiShu == LiHuoJiShu){
                    LiHuoTiShi = LiHuoTiShi.substring(0,LiHuoTiShi.length() - 1);
                    ToastUtils.showToast(mContext,LiHuoTiShi,Toast.LENGTH_LONG);
                    newLiHuoJiShu = 0;
                }
            }
            return false;
        }
    });
    //endregion

    //region 请求数据
    private void GetInfo(Map<String, String> p) {
        titleHorScv.scrollTo(0,0);
        Fdate = "";
        Fno = "";
        store.clear();

        HttpRoot.getInstance().requstAync(mContext, HttpCommons.GET_GNC_GetFlightProgress_NAME, HttpCommons.GET_GNC_GetFlightProgress_ACTION, p,
                new HttpRoot.CallBack() {
                    @Override
                    public void onSucess(Object result) {
                        SoapObject object = (SoapObject) result;
                        String Exp_ULDLoading = object.getProperty(0).toString();

                        flightControls = ParseGncFlightControl.parseFlightControlToEntity(Exp_ULDLoading);
                        mHandler.sendEmptyMessage(AviationCommons.GNC_FlightControls);
                    }

                    @Override
                    public void onFailed(String message) {
                        Ldialog.dismiss();
                        ToastUtils.showToast(mContext,message,Toast.LENGTH_SHORT);

                        flightControls = new ArrayList<GncFlightControl>();
                        mHandler.sendEmptyMessage(AviationCommons.GNC_FlightControls);
                    }

                    @Override
                    public void onError() {
                        Ldialog.dismiss();
                        ToastUtils.showToast(mContext,"数据获取出错", Toast.LENGTH_SHORT);

                        flightControls = new ArrayList<GncFlightControl>();
                        mHandler.sendEmptyMessage(AviationCommons.GNC_FlightControls);
                    }
                },page);
    }
    //endregion

    //region 理货（结束，开始）
    private void lihuo(String fl) {
        String name = "";
        String action = "";

        if (fl == "start"){
            name = HttpCommons.GET_GNC_TallyStart_NAME;
            action = HttpCommons.GET_GNC_TallyStart_ACTION;
        } else if (fl == "end" ){
            name = HttpCommons.GET_GNC_TallyEnd_NAME;
            action = HttpCommons.GET_GNC_TallyEnd_ACTION ;
        } else {
            ToastUtils.showToast(mContext, "理货参数错误", Toast.LENGTH_SHORT);
            return;
        }

        List<HashMap> LiHuoList = new ArrayList<>();
        for (String key : store.keySet()){
            HashMap<String, String> ll = new HashMap<>();
            ll.put("FDate", Fdate);
            ll.put("Fno", key);
            ll.put("ErrString","");
            LiHuoList.add(ll);
        }

        LiHuoJiShu = LiHuoList.size();
        LiHuoTiShi = "";

        for (HashMap<String,String> ll: LiHuoList ) {

            HttpRoot.getInstance().requstAync(mContext,name,action , ll,
                    new HttpRoot.CallBack() {
                        @Override
                        public void onSucess(Object result) {
                            LiHuoTiShi += "成功" + "\n";
                            mHandler.sendEmptyMessage(666);
                        }

                        @Override
                        public void onFailed(String message) {
                            Ldialog.dismiss();
                            LiHuoTiShi += message + "\n";
                            mHandler.sendEmptyMessage(666);
                        }

                        @Override
                        public void onError() {
                            Ldialog.dismiss();
                            LiHuoTiShi += "上传出错" + "\n";
                            mHandler.sendEmptyMessage(666);
                        }
                    },page);
        }
    }
    //endregion

    //region 把数据绑定到Model
    private void setDatas(List<GncFlightControl> CGO, int type) {
//        int x = 0;
//        int y = 0;
//        if (XiaLaJiShu == 0){
//            x = 0;
//            y = 50;
//        }else {
//            x = XiaLaJiShu * 50;
//            y = (XiaLaJiShu + 1) * 50 - 1;
//        }
//
//        if (y > CGO.size()){
//            y =  CGO.size();
//            pulltorefreshview.setLoadMoreEnable(false);
//            XiaLaJiShu = 0;
//        }
//        pulltorefreshview.setLoadMoreEnable(false);
//        pulltorefreshview.setPullRefreshEnable(true);

        if (CGO.size() > 0) {
            List<TableModel> mDatas = new ArrayList<>();
            for (int i = 0; i < CGO.size(); i++) {
                GncFlightControl cc = CGO.get(i);
                TableModel tableMode = new TableModel();
                tableMode.setOrgCode(cc.getFID() + "");
                tableMode.setLeftTitle(cc.getFno() + "");
                tableMode.setText0(getDongTai(0,cc) + "");//列0内容
                tableMode.setText1(getDongTai(1,cc) + "");//列1内容
                tableMode.setText2(getDongTai(2,cc) + "");//列2内容
                tableMode.setText3(getDongTai(3,cc) + "");
                tableMode.setText4(getDongTai(4,cc) + "");
                tableMode.setText5(getDongTai(5,cc) + "");//
                tableMode.setText6(getDongTai(6,cc) + "");//
                tableMode.setText7(getDongTai(7,cc) + "");//
                tableMode.setText8(getDongTai(8,cc) + "");//
                tableMode.setText9(getDongTai(9,cc) + "");//
                tableMode.setText10(getDongTai(10,cc) + "");//
                tableMode.setText11(getDongTai(11,cc) + "");
                tableMode.setText12(getDongTai(12,cc) + "");//
                tableMode.setText13(getDongTai(13,cc) + "");//
                tableMode.setText14(getDongTai(14,cc) + "");//
                tableMode.setText15(getDongTai(15,cc) + "");//
                tableMode.setText16(getDongTai(16,cc) + "");//
                tableMode.setText17(getDongTai(17,cc) + "");//
                tableMode.setText18(cc.getaFno() + "");//
                tableMode.setText19(cc.getaFDate() + "");//
                tableMode.setText20(cc.getaSegment() + "");//
                tableMode.setText21(cc.getPassWeight() + "");//
                tableMode.setText22(cc.getaETakeOff() + "");//
                tableMode.setText23(cc.getaATakeOff() + "");//
                tableMode.setText24(cc.getaEArrival() + "");//
                tableMode.setText25(cc.getaAArivall() + "");//

                mDatas.add(tableMode);
            }
            boolean isMore;
            if (type == AviationCommons.LOAD_DATA) {
                isMore = true;
            } else {
                isMore = false;
//                pulltorefreshview.onHeaderRefreshFinish();
                pulltorefreshview.setRefreshing(false);
            }
            mLeftAdapter.addData(mDatas, isMore);
            mRightAdapter.addData(mDatas, isMore);

            mDatas.clear();
        } else {
            mLeftAdapter.clearData(true);
            mRightAdapter.clearData(true);
//            XiaLaJiShu = 0;
        }

        Fdate = txt_riqi.getText().toString().toUpperCase().trim() + "T00:00:00";
        Fno = editHangBanHao.getText().toString().toUpperCase().trim();
//        XiaLaJiShu += 1;
    }
    //endregion

    //region 获取动态标题
    private String getDongTai(int x,GncFlightControl y){
        String result = "";
        int key = 0;
        key = mTitleTvArray.keyAt(x);
        AppCompatTextView tv = (AppCompatTextView) mTitleTvArray.get(key);
        switch (tv.getText().toString()){
            case "航班日期":
                result = y.getFDate();
                break;
            case "航程":
                result = y.getdSegment();
                break;
            case "平板数":
                result = y.getlNumber();
                break;
            case "净重":
                result = y.getNetWeight();
                break;
            case "预计起飞":
                result = y.getdETakeOff();
                break;
            case "实际起飞":
                result = y.getdATackOff();
                break;
            case "预计到达":
                result = y.getdEArrival();
                break;
            case "实际到达":
                result = y.getdAArrival();
                break;
            case "航班状态":
                if (y.getFlightStatus().contains("_")){
                    result = y.getFlightStatus().split("_")[1];
                }else{
                    result = y.getFlightStatus();
                }

                break;
            case "延误原因":
                result = y.getDelayFreeText();
                break;
            case "机号":
                result = y.getRegisteration();
                break;
            case "机型":
                result = y.getAirCraftCode();
                break;
            case "机位":
                result = y.getStandID();
                break;
            case "理货开始":
                result = y.getTallyStart();
                break;
            case "理货结束":
                result = y.getTallyEnd();
                break;
            case "截载":
                result = y.getMClose();
                break;
            case "交接":
                result = y.getOutBound();
                break;
            case "地服":
                result = y.getCheckTime();
                break;
            default:
                break;

        }
        return result;
    }
    //endregion

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

    //region 清空tableview
    private void clearTableView(){
        mLeftAdapter.clearData(true);
        mRightAdapter.clearData(true);

        Fdate = "";
        Fno = "";
    }
    //endregion

    //region 弹出排序框
    private void showDialog(View view) {
        String title = PreferenceUtils.getjcgk(mContext);
        if(title.contains("航班日期")){
            dialog.setTopItemViews(title.split(" "));
        }else {
            dialog.setTopItemViews(yuzhiTitle.split(" "));
        }
        dialog.setBottomHasDrag(false);
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                List<String> list = ((DragSortDialog) dialog).getTopDefaultItemViews();
                String li = "";
                for (int i = 0;i < list.size();i++) {
                    li += list.get(i) + " ";
                }
                PreferenceUtils.savejcgk(mContext,li);
            }
        });
        dialog.show();
    }
    //endregion
    //endregion
}
