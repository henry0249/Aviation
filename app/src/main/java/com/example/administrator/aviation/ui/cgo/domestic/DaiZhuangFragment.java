package com.example.administrator.aviation.ui.cgo.domestic;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.aviation.R;
import com.example.administrator.aviation.http.HttpCommons;
import com.example.administrator.aviation.http.HttpRoot;
import com.example.administrator.aviation.model.adapter.AbsCommonAdapter;
import com.example.administrator.aviation.model.adapter.AbsViewHolder;
import com.example.administrator.aviation.model.hygnc.ParseULDLoadingCargo;
import com.example.administrator.aviation.model.hygnc.ULDLoadingCargo;
import com.example.administrator.aviation.sys.PublicFun;
import com.example.administrator.aviation.ui.base.AbPullToRefreshView;
import com.example.administrator.aviation.ui.base.SyncHorizontalScrollView;
import com.example.administrator.aviation.ui.base.TableModel;
import com.example.administrator.aviation.ui.dialog.LoadingDialog;
import com.example.administrator.aviation.util.AviationCommons;
import com.example.administrator.aviation.util.ToastUtils;
import com.example.administrator.aviation.util.WeakHandler;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;

import org.ksoap2.serialization.SoapObject;

import java.lang.reflect.Field;
import java.sql.Ref;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.R.attr.key;
import static android.R.id.list;
import static android.R.id.message;
import static com.example.administrator.aviation.R.id.pulltorefreshview;
import static com.example.administrator.aviation.R.id.sousuoQuxiao;
import static com.example.administrator.aviation.R.id.sousuoYundan;
import static com.example.administrator.aviation.R.id.textView;

/**
 * Created by Administrator on 2017/12/6.
 */

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
public class DaiZhuangFragment extends Fragment {

    //region 全局变量
    private final String TAG = "DaiZhuangFragmentError";
    private final String page = "one";
    private int talHeight;
    //用于存放标题的id,与textview引用
    private SparseArray<TextView> mTitleTvArray;
    private HashMap<String,String> res;
    private List<ULDLoadingCargo> DaiZhuangCargos;
    private List<HashMap<String,String>> store;
    private View view;
    private Context mContext;
    private Activity mAct;
    private String ZhuangHuoTiShi="";
    private int ZhuangHuoJiShu = 0;
    private int newZhuangHuoJiShu = 0;
    //endregion

    //region 自定义和代码定义的控件
    private AlertDialog.Builder inputDialog;
    private AlertDialog ad;
    // 初始化数据加载提示（即对话框）
    private LoadingDialog Ldialog;
    private EditText diaEdit;
    private TextView DaiLiRen;
    private AbsCommonAdapter<TableModel> mLeftAdapter, mRightAdapter;
    private WeakHandler mHandler;
    //endregion

    //region Button and TextView and EditText控件
    @BindView(R.id.sousuoQuedin_d)
    Button sousuoQuedin;
    @BindView(R.id.sousuoQuxiao_d)
    Button sousuoQuxiao;
    @BindView(R.id.tv_table_title_left_d)
    TextView tv_table_title_left_d;
    @BindView(R.id.sousuoYundan_d)
    EditText sousuoZhudan;
    @BindView(R.id.jiajia_d)
    FloatingActionButton JiaJiafloatingButton;
    @BindView(R.id.search_d)
    FloatingActionButton JianSuofloatingButton;
    //endregion

    //region ScrollView and ListView控件
    @BindView(R.id.pull_refresh_scroll_d)
    ScrollView refresh_scroll;
    @BindView(R.id.title_horsv_d)
    SyncHorizontalScrollView titleHorScv;
    @BindView(R.id.content_horsv_d)
    SyncHorizontalScrollView contentHorScv;
    @BindView(R.id.pulltorefreshview_d)
    AbPullToRefreshView pulltorefreshview;
    @BindView(R.id.left_container_listview_d)
    ListView leftListView;
    @BindView(R.id.right_container_listview_d)
    ListView rightListView;
    //endregion

    //region layout控件
    @BindView(R.id.tableDaiZh)
    LinearLayout LaytableDaiZh;
    @BindView(R.id.jiansuokuang_d)
    LinearLayout jiansuokuang;
    @BindView(R.id.right_title_container_d)
    LinearLayout right_title_container;
    //endregion

    //region 初始化

    //region 入口函数
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.table_daizhuang, container, false);
        mContext = getContext();
        mAct = (Activity) mContext;
        ButterKnife.bind(this,view);
        init();
        return view;
    }
    //endregion

    //region 设置初始化
    public void init() {
        talHeight = 0;
        res = new HashMap<>();
        DaiZhuangCargos = new ArrayList<>();
        mHandler = new WeakHandler();
        store = new ArrayList<>();

        tv_table_title_left_d.setText("待装列表");
        getActivity().getLayoutInflater().inflate(R.layout.table_right_title, right_title_container);

        // 设置两个水平控件的联动
        titleHorScv.setScrollView(contentHorScv);
        contentHorScv.setScrollView(titleHorScv);

        jiansuokuang.setVisibility(View.GONE);
        inputDialog = new AlertDialog.Builder(getActivity());
        Ldialog = new LoadingDialog(mContext);

        res = (HashMap<String, String>) getArguments().getSerializable("daizhuang");
        GetInfo(res);

        findTitleTextViewIds();
        initTableView();
        setListener();
    }
    //endregion

    //region 利用反射初始化标题的TextView的item引用
    private void findTitleTextViewIds() {
        mTitleTvArray = new SparseArray<>();
        for (int i = 0; i < 13; i++) {
            try {
                Field field = R.id.class.getField("tv_table_title_" + i);
                int key = field.getInt(new R.id());
                TextView textView = (TextView) view.findViewById(key);

                mTitleTvArray.put(key, textView);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    //endregion

    //region 初始化表格的view
    public void initTableView() {
        mLeftAdapter = new AbsCommonAdapter<TableModel>(mContext, R.layout.table_left_item) {
            @Override
            public void convert(AbsViewHolder helper, TableModel item, int pos) {
                TextView tv_table_content_left = helper.getView(R.id.tv_table_content_item_left);
                CheckBox cb = helper.getView(R.id.item_cb);
                tv_table_content_left.setText(item.getLeftTitle());
                cb.setChecked(false);
            }
        };

        mRightAdapter = new AbsCommonAdapter<TableModel>(mContext, R.layout.table_right_item) {
            @Override
            public void convert(AbsViewHolder helper, TableModel item, int pos) {
                TextView tv_table_content_right_item0 = helper.getView(R.id.tv_table_content_right_item0);
                TextView tv_table_content_right_item1 = helper.getView(R.id.tv_table_content_right_item1);
                TextView tv_table_content_right_item2 = helper.getView(R.id.tv_table_content_right_item2);
                TextView tv_table_content_right_item3 = helper.getView(R.id.tv_table_content_right_item3);
                TextView tv_table_content_right_item4 = helper.getView(R.id.tv_table_content_right_item4);
                TextView tv_table_content_right_item5 = helper.getView(R.id.tv_table_content_right_item5);
                TextView tv_table_content_right_item6 = helper.getView(R.id.tv_table_content_right_item6);
                TextView tv_table_content_right_item7 = helper.getView(R.id.tv_table_content_right_item7);
                TextView tv_table_content_right_item8 = helper.getView(R.id.tv_table_content_right_item8);
                TextView tv_table_content_right_item9 = helper.getView(R.id.tv_table_content_right_item9);
                TextView tv_table_content_right_item10 = helper.getView(R.id.tv_table_content_right_item10);
                TextView tv_table_content_right_item11 = helper.getView(R.id.tv_table_content_right_item11);
                TextView tv_table_content_right_item12 = helper.getView(R.id.tv_table_content_right_item12);
                TextView tv_table_content_right_item13 = helper.getView(R.id.tv_table_content_right_item13);

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

                if (item.getText10().length() > 8){
                    tv_table_content_right_item10.setText(item.getText10().substring(0,3) + "\n" + item.getText10().substring(3));
                }else{
                    tv_table_content_right_item10.setText(item.getText10());
                }

                tv_table_content_right_item11.setText(item.getText11());
                tv_table_content_right_item12.setText(item.getText12());

                tv_table_content_right_item13.setText(item.getText13());

//                if (item.getText13().length() < 6) {
//                    tv_table_content_right_item13.setText(item.getText13());
//                } else {
//                    final ArrayMap<String,String> aa = new ArrayMap<>();
//                    aa.put("备注",item.getText13());
//                    tv_table_content_right_item13.setText(item.getText13().substring(0,5)+ "...");
//
//                    tv_table_content_right_item13.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            new QMUIDialog.MessageDialogBuilder(mAct)
//                                    .setTitle("备注详情")
//                                    .setMessage(aa.get("备注")).show();
//                        }
//                    });
//                }


                //部分行设置颜色凸显
//                item.setTextColor(tv_table_content_right_item0, item.getText0());
//                item.setTextColor(tv_table_content_right_item5, item.getText5());
//                item.setTextColor(tv_table_content_right_item10, item.getText10());
//                item.setTextColor(tv_table_content_right_item14, item.getText14());

                for (int i = 0; i < 13; i++) {
                    View view = ((LinearLayout) helper.getConvertView()).getChildAt(i);
                    view.setVisibility(View.VISIBLE);
                }
            }
        };
        leftListView.setAdapter(mLeftAdapter);
        rightListView.setAdapter(mRightAdapter);
    }
    //endregion

    //region 碎片show的时候刷新
    @Override
    public void onHiddenChanged(boolean hidd) {
        if (hidd) {
            //隐藏时所作的事情
            ToastUtils.hideToast();

        } else {
            //显示时所作的事情
            pulltorefreshview.headerRefreshing();
        }
    }
    //endregion

    //endregion

    //region 控件事件

    //region 设置页面上所有监听事件
    public void setListener() {

        //region 点击其他部位隐藏软键盘
        LaytableDaiZh.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                PublicFun.KeyBoardHide(mAct,mContext);
                return true;
            }
        });
        //endregion

        //region 下拉刷新
        pulltorefreshview.setOnHeaderRefreshListener(new AbPullToRefreshView.OnHeaderRefreshListener() {
            @Override
            public void onHeaderRefresh(AbPullToRefreshView view) {
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        GetInfo(res);
                    }
                }, 1000);
            }
        });
        //endregion

        //region 左侧标题列监听事件
        leftListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (getActivity() != null){
                    CheckBox checkBox = (CheckBox) view.findViewById(R.id.item_cb);
                    TableModel ta = (TableModel) parent.getItemAtPosition(position);
                    if (checkBox.isChecked()) {
                        checkBox.setChecked(false);
                        for (int i = store.size() - 1;i > -1; i--) {
                            if (store.get(i).get("WHID").equalsIgnoreCase(ta.getOrgCode().toString().trim())) {
                                store.remove(i);
                            }
                        }

                    } else {
                        checkBox.setChecked(true);
                        boolean flag = false;
                        for (int i = store.size() - 1;i > -1; i--) {
                            if (store.get(i).get("WHID").equalsIgnoreCase(ta.getOrgCode().toString().trim())) {
                                flag = true;
                            }
                        }

                        if (! flag) {
                            HashMap<String, String> CargoItem = new HashMap<String, String>();
                            CargoItem.put("LoadingID",res.get("ID"));
                            CargoItem.put("ULD",res.get("ULD"));
                            CargoItem.put("WHID",ta.getOrgCode().toString().trim());
                            CargoItem.put("PC",ta.getText0().toString().trim());
                            CargoItem.put("Weight","0");
                            CargoItem.put("ErrString","");
                            store.add(CargoItem);
                        }
                    }
                }
            }
        });
        //rendregion

        //region 右侧标题列监听事件
        rightListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (getActivity() != null){

                }
            }
        });
        //endregion

        //region 检索悬浮按钮的点击事件
        JianSuofloatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (jiansuokuang.getVisibility() == View.GONE) {
                    jiansuokuang.setVisibility(View.VISIBLE);
                    sousuoZhudan.requestFocus();
                    PublicFun.KeyBoardSwitch(mContext);
                }
            }
        });
        //endregion

        //region 搜索弹窗确定按钮
        sousuoQuedin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //检索运单号
                int yidongInt = 0;
                boolean aFlaf = true;
                if (!TextUtils.isEmpty(sousuoZhudan.getText())) {
                    String zhudan = sousuoZhudan.getText().toString().trim().replaceAll("P", "").replaceAll("-", "");
                    int zhudanInt = sousuoZhudan.getText().toString().trim().length();

                    if (zhudanInt < 4) {
                        Toast.makeText(getActivity(), "请输入运单后四位", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    loop1:for(int i = 0 ;i < DaiZhuangCargos.size();i++) {
                        String MABW = DaiZhuangCargos.get(i).getMawb().replaceAll("P", "").replaceAll("-", "");
                        if (zhudan.equals(MABW.substring(MABW.length()- zhudanInt))) {
                            yidongInt = i;
                            aFlaf = false;
                            break loop1;
                        }
                    }
                }

                if (yidongInt == 0 && aFlaf) {
                    Toast.makeText(getActivity(), "未找到数据，请再多输几位", Toast.LENGTH_SHORT).show();
                } else {
                    if (talHeight < 5) {
                        talHeight = PublicFun.CalcListHeigh(leftListView);
                    }
                    PublicFun.KeyBoardHide(mAct,mContext);
                    refresh_scroll.smoothScrollTo(0, yidongInt * talHeight);
                    leftListView.performItemClick(leftListView.getChildAt(yidongInt), yidongInt, leftListView.getItemIdAtPosition(yidongInt));
                }
            }
        });
        //endregion

        //region 搜索弹窗取消按钮
        sousuoQuxiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sousuoZhudan.setText("");
                PublicFun.KeyBoardHide(mAct,mContext);
                jiansuokuang.setVisibility(View.GONE);
            }
        });
        //endregion

        //region 装货按钮
        JiaJiafloatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() != null ){
                    if (store.size() > 0) {
                        ZhuangHuo();
                        pulltorefreshview.headerRefreshing();
                    }
                }
            }
        });
        //endregion

        //region
        // 按钮长按事件
        JiaJiafloatingButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (getActivity() != null ){
                    if (store.size() == 1) {
                        LayoutInflater llInflater = LayoutInflater.from(mContext);
                        View newPlanDialog = llInflater.inflate(R.layout.dialog_zhuangzai, (ViewGroup)getActivity().findViewById(R.id.dia_ZhuangZai));

                        inputDialog.setView(newPlanDialog);
                        inputDialog.setPositiveButton("装货",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        int yuanshishuzi = Integer.parseInt(store.get(0).get("PC"));
                                        if (!TextUtils.isEmpty(diaEdit.getText().toString().trim())) {
                                            if (Integer.parseInt(diaEdit.getText().toString()) <= yuanshishuzi) {
                                                store.get(0).put("PC", diaEdit.getText().toString().trim());
                                                ZhuangHuo();
                                                pulltorefreshview.headerRefreshing();
                                            } else {
                                                ToastUtils.showToast(mContext,"输入值大于货物件数", Toast.LENGTH_SHORT);
                                            }

                                            mHandler.postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    PublicFun.KeyBoardHide(mAct,mContext);
                                                }
                                            }, 100);
                                        }
                                    }
                                });

                        inputDialog.setNegativeButton("取消",new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                mHandler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        PublicFun.KeyBoardHide(mAct,mContext);
                                    }
                                }, 100);
                                ad.dismiss();

                            }
                        });
                        inputDialog.setCancelable(false);

                        ad = inputDialog.create();
                        ad.show();
                        diaEdit = (EditText)ad.findViewById(R.id.DiaAdit_ZhuangZai);
                        diaEdit.setTextColor(Color.rgb(0, 0, 0));
                        diaEdit.setText(store.get(0).get("PC") + "");
                        diaEdit.setInputType(InputType.TYPE_CLASS_NUMBER);
                        diaEdit.setSelection(diaEdit.getText().toString().trim().length());
                    }
                }
                return true;
            }
        });
        //endregion

        //region 标题栏点击事件
loop1:        for(int i = 0; i < mTitleTvArray.size(); i++) {
            int key = 0;
            key = mTitleTvArray.keyAt(i);
            final TextView tx = mTitleTvArray.get(key);
            if (tx.getText().equals("代理人")) {
                DaiLiRen = tx;
                break loop1;
            }
        }

        DaiLiRen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txColor  = Integer.toHexString(DaiLiRen.getCurrentTextColor());
                if (txColor.equals("ff000000")) {
                    Collections.sort(DaiZhuangCargos, new Comparator<ULDLoadingCargo>() {
                        @Override
                        public int compare(ULDLoadingCargo o1, ULDLoadingCargo o2) {
                            return o1.getAgentCode().compareTo(o2.getAgentCode());
                        }
                    });

                    setDatas(DaiZhuangCargos,AviationCommons.REFRESH_DATA);
                    DaiLiRen.setTextColor(Color.RED);
                } else {
                    pulltorefreshview.headerRefreshing();
                }
            }
        });
        //endregion

        //region 运单号EditText监听键盘Enter事件
        sousuoZhudan.setOnEditorActionListener(new EditText.OnEditorActionListener() {
               @Override
               public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                   if (actionId == EditorInfo.IME_ACTION_DONE || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) || jiansuokuang.isEnabled())  {
                       sousuoQuedin.performClick();
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

    //region 把数据绑定到Model
    private void setDatas(List<ULDLoadingCargo> CGO, int type) {
        pulltorefreshview.setLoadMoreEnable(false);
        store.clear();
        DaiLiRen.setTextColor(Color.BLACK);

        if (CGO.size() > 0) {
            List<TableModel> mDatas = new ArrayList<>();
            for (int i = 0; i < CGO.size(); i++) {
                ULDLoadingCargo cc = CGO.get(i);
                TableModel tableMode = new TableModel();
                tableMode.setOrgCode(cc.getWHID() + "");
                tableMode.setLeftTitle(cc.getMawb() + "");
                tableMode.setText0(cc.getPC() + "");//列1内容
                tableMode.setText1(cc.getWeight() + "");//列2内容
                tableMode.setText2(cc.getVolume() + "");
                tableMode.setText3(cc.getSpCode() + "");
                tableMode.setText4(cc.getGoods() + "");//
                tableMode.setText5(cc.getDest() + "");//
                tableMode.setText6(cc.getBy1() + "");//
                tableMode.setText7(cc.getAgentCode() + "");//

                if (TextUtils.isEmpty(cc.getFDate())) {
                    tableMode.setText8(cc.getFDate() + "");
                } else {
                    tableMode.setText8(cc.getFDate().split("_")[1] + "");
                }
                //
                tableMode.setText9(cc.getFno() + "");//
                tableMode.setText10(cc.getLocation() + "");//

                if (TextUtils.isEmpty(cc.getPlanFDate())) {
                    tableMode.setText11(cc.getPlanFDate() + "");//
                } else {
                    tableMode.setText11(cc.getPlanFDate().split("_")[1] + "");//
                }

                tableMode.setText12(cc.getPlanFno() + "");//
                tableMode.setText13(cc.getRemark() + "");//
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
    }
    //endregion

    //region 请求数据
    private void GetInfo(Map<String, String> p) {
        // 显示提示框
        Ldialog.show();
        HttpRoot.getInstance().requstAync(mContext, HttpCommons.CGO_DOM_Exp_ULDLoadingCargo_NAME, HttpCommons.CGO_DOM_Exp_ULDLoadingCargo_ACTION, p,
                new HttpRoot.CallBack() {
                    @Override
                    public void onSucess(Object result) {
                        SoapObject object = (SoapObject) result;
                        String ULDLoadingCargoZhuang = object.getProperty(0).toString();
                        DaiZhuangCargos = ParseULDLoadingCargo.parseULDLoadingCargoXMLto(ULDLoadingCargoZhuang,1);
                        handler.sendEmptyMessage(AviationCommons.GNC_ULDLoadingCargo);
                    }

                    @Override
                    public void onFailed(String message) {
                        Ldialog.dismiss();
                        ToastUtils.showToast(mContext,message, Toast.LENGTH_LONG);
                    }

                    @Override
                    public void onError() {
                        Ldialog.dismiss();
                        ToastUtils.showToast(mContext,"数据获取出错",Toast.LENGTH_SHORT);
                    }
                },page);
    }
    //endregion

    //region 句柄监听
    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == AviationCommons.GNC_ULDLoadingCargo) {
                setDatas(DaiZhuangCargos,AviationCommons.REFRESH_DATA);

                if (DaiZhuangCargos.size() == 0) {
                    ToastUtils.showToast(mContext,"数据为空",Toast.LENGTH_SHORT);
                }

                Ldialog.dismiss();

            }else if(msg.what == 666){
                newZhuangHuoJiShu  += 1;
                if (newZhuangHuoJiShu == ZhuangHuoJiShu){
                    ZhuangHuoTiShi = ZhuangHuoTiShi.substring(0,ZhuangHuoTiShi.length() - 1);
                    ToastUtils.showToast(mContext,ZhuangHuoTiShi,Toast.LENGTH_LONG);
                    newZhuangHuoJiShu = 0;
                }
            }
            return false;
        }
    });
    //endregion

    //region 货物装载操作方法
    private void ZhuangHuo() {
        List<HashMap<String,String>> ZhuangHuoList = new ArrayList<>(store);
        ZhuangHuoJiShu = ZhuangHuoList.size();
        ZhuangHuoTiShi = "";

        for (HashMap<String,String> ll: ZhuangHuoList ) {
            HttpRoot.getInstance().requstAync(mContext, HttpCommons.CGO_DOM_Exp_GNCLoading_NAME, HttpCommons.CGO_DOM_Exp_GNCLoading_ACTION, ll,
                    new HttpRoot.CallBack() {
                        @Override
                        public void onSucess(Object result) {
                            ZhuangHuoTiShi += "成功" + "\n";
                            handler.sendEmptyMessage(666);
                        }

                        @Override
                        public void onFailed(String message) {
                            ZhuangHuoTiShi += message + "\n";
                            handler.sendEmptyMessage(666);
                        }

                        @Override
                        public void onError() {
                            ZhuangHuoTiShi += "上传失败" + "\n";
                            handler.sendEmptyMessage(666);
                        }
                    },page);
        }

    }
    //endregion

    //endregion
}
