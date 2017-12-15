package com.example.administrator.aviation.ui.cgo.domestic;

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
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.administrator.aviation.ui.base.AbPullToRefreshView;
import com.example.administrator.aviation.ui.base.SyncHorizontalScrollView;
import com.example.administrator.aviation.ui.base.TableModel;
import com.example.administrator.aviation.util.AviationCommons;
import com.example.administrator.aviation.util.ToastUtils;
import com.example.administrator.aviation.util.WeakHandler;

import org.ksoap2.serialization.SoapObject;

import java.lang.reflect.Field;
import java.sql.Ref;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.R.id.list;
import static com.example.administrator.aviation.R.id.pulltorefreshview;
import static com.example.administrator.aviation.R.id.sousuoQuxiao;

/**
 * Created by Administrator on 2017/12/6.
 */

public class DaiZhuangFragment extends Fragment {
    private View view;
    private Context mContext;

    private final String TAG = "DaiZhuangFragmentError";
    private final String page = "one";
    private int talHeight = 0;

    private AlertDialog.Builder inputDialog;
    private EditText editText;
    private AbsCommonAdapter<TableModel> mLeftAdapter, mRightAdapter;
    private WeakHandler mHandler = new WeakHandler();

    //用于存放标题的id,与textview引用
    private SparseArray<TextView> mTitleTvArray;
    private HashMap<String,String> res = new HashMap<>();
    private List<ULDLoadingCargo> DaiZhuangCargos = new ArrayList<>();
    private List<HashMap<String,String>> store = new ArrayList<>();

    @BindView(R.id.tv_table_title_left_d)
    TextView tv_table_title_left_d;
    @BindView(R.id.uldloading_proBar_d)
    ProgressBar proBar;
    @BindView(R.id.title_horsv_d)
    SyncHorizontalScrollView titleHorScv;
    @BindView(R.id.content_horsv_d)
    SyncHorizontalScrollView contentHorScv;
    @BindView(R.id.jiansuokuang_d)
    LinearLayout jiansuokuang;
    @BindView(R.id.left_container_listview_d)
    ListView leftListView;
    @BindView(R.id.right_container_listview_d)
    ListView rightListView;
    @BindView(R.id.right_title_container_d)
    LinearLayout right_title_container;
    @BindView(R.id.pulltorefreshview_d)
    AbPullToRefreshView pulltorefreshview;
    @BindView(R.id.jiajia_d)
    FloatingActionButton JiaJiafloatingButton;
    @BindView(R.id.search_d)
    FloatingActionButton JianSuofloatingButton;
    @BindView(R.id.sousuoYundan_d)
    EditText sousuoZhudan;
    @BindView(R.id.sousuoQuedin_d)
    Button sousuoQuedin;
    @BindView(R.id.sousuoQuxiao_d)
    Button sousuoQuxiao;
    @BindView(R.id.pull_refresh_scroll_d)
    ScrollView refresh_scroll;

    //region 初始化

    //region 入口函数
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.table_daizhuang, container, false);
        mContext = getActivity().getApplicationContext();
        ButterKnife.bind(this,view);
        init();
        return view;
    }
    //endregion

    //region 设置初始化
    public void init() {
        tv_table_title_left_d.setText("待装列表");
        getActivity().getLayoutInflater().inflate(R.layout.table_right_title, right_title_container);

        // 设置两个水平控件的联动
        titleHorScv.setScrollView(contentHorScv);
        contentHorScv.setScrollView(titleHorScv);

        jiansuokuang.setVisibility(View.GONE);
        inputDialog = new AlertDialog.Builder(getActivity());

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
        for (int i = 0; i < DaiZhuangCargos.size(); i++) {
            try {
                Field field = R.id.class.getField("tv_table_title_" + 0);
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

    //endregion

    //region 控件事件

    //region 设置页面上所有监听事件
    public void setListener() {

        //region 点击其他部位隐藏软键盘
        view.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                KeyBoardHide();
                return false;
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
                            CargoItem.put("PC",ta.getText1().toString().trim());
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
//                    TableModel ta = (TableModel) parent.getItemAtPosition(position);
//                    Toast.makeText(getActivity().getApplicationContext(), ta.getLeftTitle() + "_" + position , Toast.LENGTH_SHORT).show();
                }
            }
        });
        //endregion

        //region 检索悬浮按钮的点击事件
        JianSuofloatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jiansuokuang.setVisibility(View.VISIBLE);
                sousuoZhudan.requestFocus();
                KeyBoardSwitch();
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
                        ListHeigh(leftListView);
                    }
                    KeyBoardHide();
                    refresh_scroll.smoothScrollTo(0, yidongInt * talHeight);
                    leftListView.performItemClick(leftListView.getChildAt(yidongInt), yidongInt, leftListView.getItemIdAtPosition(yidongInt));
//                    Toast.makeText(getActivity(), store.size() + "", Toast.LENGTH_SHORT).show();
                }
            }
        });
        //endregion

        //region 搜索弹窗取消按钮
        sousuoQuxiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sousuoZhudan.setText("");
                KeyBoardHide();
                jiansuokuang.setVisibility(View.GONE);
            }
        });
        //endregion

        //region 装货按钮
        JiaJiafloatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() != null ){

                    if (store.size() > 1) {
                        ZhuangHuo();
                        store.clear();
                        pulltorefreshview.headerRefreshing();
                    } else if (store.size() == 1) {
                        editText = new EditText(mContext);
                        editText.setTextColor(Color.rgb(0, 0, 0));
                        editText.setText(store.get(0).get("PC") + "");
                        editText.setSelection(editText.getText().toString().trim().length());
                        inputDialog.setTitle("分批件数").setView(editText);
                        inputDialog.setPositiveButton("装货",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        int yuanshishuzi = Integer.parseInt(store.get(0).get("PC"));
                                        if (!TextUtils.isEmpty(editText.getText().toString().trim())) {
                                            if (Integer.parseInt(editText.getText().toString()) <= yuanshishuzi) {
                                                store.get(0).put("PC", editText.getText().toString().trim());
                                                ZhuangHuo();
                                                store.clear();
                                                pulltorefreshview.headerRefreshing();
                                            } else {
                                                ToastUtils.showToast(mContext,"输入值大于货物件数", Toast.LENGTH_SHORT);
                                            }
                                            KeyBoardHide();
                                        }
                                    }
                                });
                        inputDialog.show();
                    }
                }
            }
        });
        //endregion

    }
    //endregion

    //endregion

    //region 功能方法

    //region 把数据绑定到Model
    private void setDatas(List<ULDLoadingCargo> CGO, int type) {
        pulltorefreshview.setLoadMoreEnable(false);

        if (CGO.size() > 0) {
            List<TableModel> mDatas = new ArrayList<>();
            for (int i = 0; i < CGO.size(); i++) {
                ULDLoadingCargo cc = CGO.get(i);
                TableModel tableMode = new TableModel();
                tableMode.setOrgCode(cc.getWHID() + "");
                tableMode.setLeftTitle(cc.getMawb() + "");
                tableMode.setText0(cc.getAgentCode() + "");//列0内容
                tableMode.setText1(cc.getPC() + "");//列1内容
                tableMode.setText2(cc.getWeight() + "");//列2内容
                tableMode.setText3(cc.getVolume() + "");
                tableMode.setText4(cc.getSpCode() + "");
                tableMode.setText5(cc.getGoods() + "");//
                tableMode.setText6(cc.getDest() + "");//
                tableMode.setText7(cc.getBy1() + "");//

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
            pulltorefreshview.onHeaderRefreshFinish();
        } else {
            mLeftAdapter.clearData(true);
            mRightAdapter.clearData(true);
        }
    }
    //endregion

    //region 请求数据
    private void GetInfo(Map<String, String> p) {
        proBar.setVisibility(View.VISIBLE);
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
                        proBar.setVisibility(View.GONE);
                        ToastUtils.showToast(mContext,message, Toast.LENGTH_LONG);
                    }

                    @Override
                    public void onError() {
                        proBar.setVisibility(View.GONE);
                        ToastUtils.showToast(mContext,"数据获取出错",Toast.LENGTH_SHORT);
                    }
                },page);
    }
    //endregion

    //region 句柄监听
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == AviationCommons.GNC_ULDLoadingCargo) {
                if (DaiZhuangCargos.size() > 0) {
                    setDatas(DaiZhuangCargos,AviationCommons.REFRESH_DATA);
                } else {
                    ToastUtils.showToast(mContext,"数据为空",Toast.LENGTH_SHORT);
                }
                proBar.setVisibility(View.GONE);
            }
        }
    };
    //endregion

    //region 计算listview每一行高度
    private void ListHeigh(ListView listView) {
        ListAdapter mAdapter = listView.getAdapter();
        if (mAdapter == null) {
            return;
        }
        View mView = mAdapter.getView(1, null, listView);
        mView.measure(0, 0);
        talHeight = mView.getMeasuredHeight() + 4;
    }
    //endregion

    //region 软键盘状态切换
    private void KeyBoardSwitch() {
        InputMethodManager imm = (InputMethodManager)mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
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
        InputMethodManager imm = (InputMethodManager)mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        if(imm.isActive() && getActivity().getCurrentFocus()!=null){
            if (getActivity().getCurrentFocus().getWindowToken()!=null) {
                imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }
    //endregion

    //region 货物装载
    private void ZhuangHuo() {
        List<HashMap<String,String>> ZhuangHuoList = new ArrayList<>(store);

        for (HashMap<String,String> ll: ZhuangHuoList ) {
            HttpRoot.getInstance().requstAync(mContext, HttpCommons.CGO_DOM_Exp_GNCLoading_NAME, HttpCommons.CGO_DOM_Exp_GNCLoading_ACTION, ll,
                    new HttpRoot.CallBack() {
                        @Override
                        public void onSucess(Object result) {
                            SoapObject object = (SoapObject) result;
                            String xx = object.getProperty(0).toString();
                            if (xx.equalsIgnoreCase("true")) {
                                ToastUtils.showToast(mContext, "装货成功", Toast.LENGTH_SHORT);
                            } else {
                                ToastUtils.showToast(mContext, object.getProperty(1).toString(), Toast.LENGTH_SHORT);
                            }
                        }

                        @Override
                        public void onFailed(String message) {
                            ToastUtils.showToast(mContext,message, Toast.LENGTH_LONG);
                        }

                        @Override
                        public void onError() {
                            ToastUtils.showToast(mContext,"数据上传",Toast.LENGTH_SHORT);
                        }
                    },page);
        }

    }
    //endregion

    //endregion
}
