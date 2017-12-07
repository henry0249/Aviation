package com.example.administrator.aviation.ui.cgo.domestic;

import android.content.Context;
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
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.aviation.R;
import com.example.administrator.aviation.http.HttpCommons;
import com.example.administrator.aviation.http.HttpRoot;
import com.example.administrator.aviation.model.adapter.AbsCommonAdapter;
import com.example.administrator.aviation.model.adapter.AbsViewHolder;
import com.example.administrator.aviation.model.hygnc.ParseGNCmessage;
import com.example.administrator.aviation.model.hygnc.ParseULDLoadingCargo;
import com.example.administrator.aviation.model.hygnc.ULDLoadingCargo;
import com.example.administrator.aviation.ui.base.AbPullToRefreshView;
import com.example.administrator.aviation.ui.base.SyncHorizontalScrollView;
import com.example.administrator.aviation.ui.base.TableModel;
import com.example.administrator.aviation.util.AviationCommons;
import com.example.administrator.aviation.util.ToastUtils;

import org.ksoap2.serialization.SoapObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.administrator.aviation.R.id.sousuoYundan;

/**
 * Created by Administrator on 2017/12/6.
 */

public class ZhuangZaiFragment extends Fragment {
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

    private AlertDialog.Builder inputDialog;
    private AbsCommonAdapter<TableModel> mLeftAdapter, mRightAdapter;

    //用于存放标题的id,与textview引用
    private SparseArray<TextView> mTitleTvArray;
    private HashMap<String,String> res = new HashMap<>();
    private List<ULDLoadingCargo> loadingCargos = new ArrayList<>();
    private final String TAG = "ZhuangZaiFragmentError";
    private final String page = "one";
    private HashMap<String,String> store = new HashMap();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.table_zhuangzai, container, false);
        mContext = getActivity().getApplicationContext();
        ButterKnife.bind(this,view);
        init();
        return view;
    }

    public void init() {
        tv_table_title_left.setText("已装列表");
        inputDialog = new AlertDialog.Builder(getActivity());
        getActivity().getLayoutInflater().inflate(R.layout.table_right_title, right_title_container);

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


    //初始化标题的TextView的item引用
    private void findTitleTextViewIds() {
        mTitleTvArray = new SparseArray<>();
        for (int i = 0; i < loadingCargos.size(); i++) {
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

    public void setListener() {
        leftListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //跳转界面
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

        rightListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (getActivity() != null){
                    TableModel ta = (TableModel) parent.getItemAtPosition(position);
                    Toast.makeText(getActivity().getApplicationContext(), ta.getLeftTitle() + "_" + position , Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //region 请求数据
    private void GetInfo(Map<String, String> p) {
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
                        ToastUtils.showToast(mContext,"数据获取失败", Toast.LENGTH_SHORT);
                    }

                    @Override
                    public void onError() {
                        ToastUtils.showToast(mContext,"数据获取出错",Toast.LENGTH_SHORT);
                    }
                },page);
    }
    //endregion

    //region 功能方法

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
        } else {
                mLeftAdapter.clearData(true);
                mRightAdapter.clearData(true);
        }
    }

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
            }
        }
    };
    //endregion

    //endregion
}
