package com.example.administrator.aviation.ui.cgo.domestic;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
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
import com.example.administrator.aviation.ui.base.AbPullToRefreshView;
import com.example.administrator.aviation.ui.base.SyncHorizontalScrollView;
import com.example.administrator.aviation.ui.base.TableModel;
import com.example.administrator.aviation.ui.dialog.LoadingDialog;
import com.example.administrator.aviation.util.AviationCommons;
import com.example.administrator.aviation.util.ToastUtils;
import com.example.administrator.aviation.util.WeakHandler;

import org.ksoap2.serialization.SoapObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.administrator.aviation.R.id.progressBar;
import static com.example.administrator.aviation.R.id.sousuoYundan;

/**
 * Created by 石松涛 on 2017/12/6.
 */

public class ZhuangZaiFragment extends Fragment {
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

    private View view;
    private Context mContext;

    @BindView(R.id.pulltorefreshview)
    AbPullToRefreshView pulltorefreshview;
    @BindView(R.id.tv_table_title_left)
    TextView tv_table_title_left;
    @BindView(R.id.left_container_listview)
    ListView leftListView;
    @BindView(R.id.right_container_listview)
    ListView rightListView;
    @BindView(R.id.right_title_container)
    LinearLayout right_title_container;
    @BindView(R.id.title_horsv)
    SyncHorizontalScrollView titleHorScv;
    @BindView(R.id.content_horsv)
    SyncHorizontalScrollView contentHorScv;
    @BindView(R.id.jianjian)
    FloatingActionButton JianJianfloatingButton;
    @BindView(R.id.search_2)
    FloatingActionButton JianSuofloatingButton;
    @BindView(R.id.jiansuokuang)
    LinearLayout jiansuokuang;
    @BindView(R.id.sousuoQuedin)
    Button sousuoQuedin;
    @BindView(R.id.sousuoQuxiao)
    Button sousuoQuxiao;
    @BindView(R.id.sousuoYundan)
    EditText sousuoZhudan;
    @BindView(R.id.pull_refresh_scroll)
    ScrollView refresh_scroll;

    private final String TAG = "ZhuangZaiFragmentError";
    private final String page = "one";
    private int talHeight = 0;
    // 初始化数据加载提示（即对话框）
    private LoadingDialog Ldialog;
    private TextView DaiLiRen;

    private AbsCommonAdapter<TableModel> mLeftAdapter, mRightAdapter;
    private WeakHandler mHandler = new WeakHandler();

    //用于存放标题的id,与textview引用
    private SparseArray<TextView> mTitleTvArray;
    private HashMap<String,String> res = new HashMap<>();
    private List<ULDLoadingCargo> loadingCargos = new ArrayList<>();
    private HashMap<String,String> store = new HashMap();

    //region 初始化

    //region 入口函数
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.table_zhuangzai, container, false);
        mContext = getContext();
        ButterKnife.bind(this,view);
        init();
        return view;
    }
    //endregion

    //region 设置初始化
    public void init() {
        tv_table_title_left.setText("已装列表");
        getActivity().getLayoutInflater().inflate(R.layout.table_right_title, right_title_container);
        Ldialog = new LoadingDialog(mContext);
        // 设置两个水平控件的联动
        titleHorScv.setScrollView(contentHorScv);
        contentHorScv.setScrollView(titleHorScv);

        jiansuokuang.setVisibility(View.GONE);

        res = (HashMap<String, String>) getArguments().getSerializable("zhuangzai");
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
                        store.remove(ta.getOrgCode().toString().trim());
                    } else {
                        checkBox.setChecked(true);
                        boolean flag = store.containsKey(ta.getOrgCode().toString().trim());
                        if (!flag) {
                            store.put(ta.getOrgCode().toString().trim(),ta.getText1());
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

                    loop1:for(int i = 0 ;i < loadingCargos.size();i++) {
                        String MABW = loadingCargos.get(i).getMawb().replaceAll("P", "").replaceAll("-", "");
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

        //region 卸货按钮
        JianJianfloatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() != null && store.size() > 0){
                        xiehuo();
                        store.clear();
                        pulltorefreshview.headerRefreshing();
                }
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
                    Collections.sort(loadingCargos, new Comparator<ULDLoadingCargo>() {
                        @Override
                        public int compare(ULDLoadingCargo o1, ULDLoadingCargo o2) {
                            return o1.getAgentCode().compareTo(o2.getAgentCode());
                        }
                    });

                    setDatas(loadingCargos,AviationCommons.REFRESH_DATA);
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
                        loadingCargos = ParseULDLoadingCargo.parseULDLoadingCargoXMLto(ULDLoadingCargoZhuang,0);
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

    //region 货物卸载
    private void xiehuo() {
        List<HashMap> XieHuoList = new ArrayList<>();
        for (String key : store.keySet()){
            HashMap<String, String> ll = new HashMap<>();
            ll.put("WHID", key);
            ll.put("ErrString","");
            XieHuoList.add(ll);
        }

        for (HashMap<String,String> ll: XieHuoList ) {
            HttpRoot.getInstance().requstAync(mContext, HttpCommons.CGO_DOM_Exp_unLoading_NAME, HttpCommons.CGO_DOM_Exp_unLoading_ACTION, ll,
                    new HttpRoot.CallBack() {
                        @Override
                        public void onSucess(Object result) {
                            SoapObject object = (SoapObject) result;
                            String xx = object.getProperty(0).toString();
                            if (xx.equalsIgnoreCase("true")) {
                                ToastUtils.showToast(mContext, "卸货成功", Toast.LENGTH_SHORT);
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

    //region 句柄监听
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == AviationCommons.GNC_ULDLoadingCargo) {
                if (loadingCargos.size() > 0) {
                    setDatas(loadingCargos,AviationCommons.REFRESH_DATA);

                } else {
                    ToastUtils.showToast(mContext,"数据为空",Toast.LENGTH_SHORT);
                }
                Ldialog.dismiss();
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

    //endregion

}
