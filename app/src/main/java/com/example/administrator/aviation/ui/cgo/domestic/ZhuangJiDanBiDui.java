package com.example.administrator.aviation.ui.cgo.domestic;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
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
import com.example.administrator.aviation.ui.base.AbPullToRefreshView;
import com.example.administrator.aviation.ui.base.NavBar;
import com.example.administrator.aviation.ui.base.SyncHorizontalScrollView;
import com.example.administrator.aviation.ui.base.TableModel;
import com.example.administrator.aviation.ui.dialog.LoadingDialog;
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

import static com.example.administrator.aviation.R.id.ZhuangJiDanBiDui_tv_table_content_right_item6;
import static com.example.administrator.aviation.R.id.tv_table_content_right_item7;
import static com.example.administrator.aviation.R.id.tv_table_content_right_item8;
import static com.example.administrator.aviation.R.id.tv_table_content_right_item9;
import static com.example.administrator.aviation.util.AviationCommons.GNC_ManifestVsLoading;

public class ZhuangJiDanBiDui extends AppCompatActivity {



    //region 自定义变量
    private Context mContext;
    private Activity mAct;
    private NavBar navBar;

    private AbsCommonAdapter<TableModel> mLeftAdapter, mRightAdapter;
    private final String TAG = "ZhuangJiDanBiDui";
    private final String page = "one";
    private List<GNCManifestVSLoading> VsLoading;
    //endregion

    //region 未预设XML控件

    //endregion

    //region 其他控件
    private LoadingDialog Ldialog;
    private SparseArray<TextView> mTitleTvArray;

    @BindView(R.id.ZhuangJiDanBiDui_left_container_listview)
    ListView leftListView;
    @BindView(R.id.ZhuangJiDanBiDui_right_container_listview)
    ListView rightListView;
    //endregion

    //region Layout控件

    //endregion

    //region Button控件

    //endregion

    //region EditText控件

    //endregion

    //region 滚动View控件
    @BindView(R.id.ZhuangJiDanBiDui_pulltorefreshview)
    AbPullToRefreshView pulltorefreshview;
    @BindView(R.id.ZhuangJiDanBiDui_title_horsv)
    SyncHorizontalScrollView titleHorScv;
    @BindView(R.id.ZhuangJiDanBiDui_content_horsv)
    SyncHorizontalScrollView contentHorScv;
    //endregion

    //region TextView控件
    @BindView(R.id.ZhuangJiDanBiDui_tv_table_title_left)
    TextView txt_RightTitle;
    //endregion

    //region 初始化

    //region 入口函数
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zhuang_ji_dan_bi_dui_activity);
        mContext = ZhuangJiDanBiDui.this;
        mAct = (Activity) mContext;
        ButterKnife.bind(this);
        init();
    }
    //endregion

    //region 设置初始化
    public void init() {
        VsLoading = new ArrayList<>();
        navBar = new NavBar(this);
        Ldialog = new LoadingDialog(mContext);

        navBar.setTitle("舱单装机单比对");
        txt_RightTitle.setText("平板");
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        LinearLayout linearLayout = (LinearLayout)findViewById(R.id.ZhuangJiDanBiDui_right_title_container);
        layoutInflater.inflate(R.layout.zhuang_ji_dan_bi_dui_right_title,linearLayout,true);

        // 设置两个水平控件的联动
        titleHorScv.setScrollView(contentHorScv);
        contentHorScv.setScrollView(titleHorScv);

        findTitleTextViewIds();
        initTableView();
        QuZhiChaXun();
    }
    //endregion

    //region 取值查询
    private void QuZhiChaXun(){
        HashMap<String, String> idArrary = new HashMap<>();
        idArrary = (HashMap<String, String>) getIntent().getSerializableExtra("Info");
        BiDui(idArrary);
    }

    //endregion

    //region 利用反射初始化标题的TextView的item引用
    private void findTitleTextViewIds() {
        mTitleTvArray = new SparseArray<>();
        for (int i = 0; i < 8; i++) {
            try {
                Field field = R.id.class.getField("ZhuangJiDanBiDui_tv_table_title_" + i);
                int key = field.getInt(new R.id());
                TextView textView = (TextView) findViewById(key);

                mTitleTvArray.put(key, textView);
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
    }
    //endregion

    //region 初始化表格的view
    public void initTableView() {
        mLeftAdapter = new AbsCommonAdapter<TableModel>(mContext, R.layout.table_left_item_one) {
            @Override
            public void convert(AbsViewHolder helper, TableModel item, int pos) {
                if (Double.parseDouble(item.getText6()) > 3 || Double.parseDouble(item.getText5()) > 50){
                    helper.setTextColor(R.id.tv_table_content_item_left_one,"#FF0000");
                }else{
                    helper.setTextColor(R.id.tv_table_content_item_left_one,"#000000");
                }
                TextView tv_table_content_left = helper.getView(R.id.tv_table_content_item_left_one);
                tv_table_content_left.setText(item.getLeftTitle());
            }
        };

        mRightAdapter = new AbsCommonAdapter<TableModel>(mContext, R.layout.zhuang_ji_dan_bi_dui_right_item) {
            @Override
            public void convert(AbsViewHolder helper, TableModel item, int pos) {
                if (Double.parseDouble(item.getText6()) > 3 || Double.parseDouble(item.getText5()) > 50){
                    helper.setTextColor(R.id.ZhuangJiDanBiDui_tv_table_content_right_item6,"#FF0000");
                }else{
                    helper.setTextColor(R.id.ZhuangJiDanBiDui_tv_table_content_right_item6,"#000000");
                }

                TextView tv_table_content_right_item0 = helper.getView(R.id.ZhuangJiDanBiDui_tv_table_content_right_item0);
                TextView tv_table_content_right_item1 = helper.getView(R.id.ZhuangJiDanBiDui_tv_table_content_right_item1);
                TextView tv_table_content_right_item2 = helper.getView(R.id.ZhuangJiDanBiDui_tv_table_content_right_item2);
                TextView tv_table_content_right_item3 = helper.getView(R.id.ZhuangJiDanBiDui_tv_table_content_right_item3);
                TextView tv_table_content_right_item4 = helper.getView(R.id.ZhuangJiDanBiDui_tv_table_content_right_item4);
                TextView tv_table_content_right_item5 = helper.getView(R.id.ZhuangJiDanBiDui_tv_table_content_right_item5);
                TextView tv_table_content_right_item6 = helper.getView(R.id.ZhuangJiDanBiDui_tv_table_content_right_item6);


                tv_table_content_right_item0.setText(item.getText0());
                tv_table_content_right_item1.setText(item.getText1());
                tv_table_content_right_item2.setText(item.getText2());
                tv_table_content_right_item3.setText(item.getText3());
                tv_table_content_right_item4.setText(item.getText4());
                tv_table_content_right_item5.setText(item.getText5());
                tv_table_content_right_item6.setText(item.getText6() + "%");

                for (int i = 0; i < 7; i++) {
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

    //endregion

    //endregion

    //region 功能方法

    //region 打开编辑区

    //endregion

    //region 关闭编辑区

    //endregion

    //region 句柄监听
    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == GNC_ManifestVsLoading) {


                if (VsLoading.size() == 0) {
                    ToastUtils.showToast(mContext,"数据为空",Toast.LENGTH_SHORT);
                }else {
                    setDatas(VsLoading,AviationCommons.REFRESH_DATA);
                }

                Ldialog.dismiss();
            }
            return false;
        }
    });
    //endregion

    //region 请求数据
    private void BiDui(Map<String, String> p) {
        titleHorScv.scrollTo(0,0);
        Ldialog.show();
        HttpRoot.getInstance().requstAync(mContext, HttpCommons.GET_GNC_ManifestVSLoading_NAME, HttpCommons.GET_GNC_ManifestVSLoading_ACTION, p,
                new HttpRoot.CallBack() {
                    @Override
                    public void onSucess(Object result) {
                        SoapObject object = (SoapObject) result;
                        String vLoading = object.getProperty(0).toString();
                        VsLoading = ParseGncVSLoading.parseULDEntityXMLto(vLoading);

                        mHandler.sendEmptyMessage(GNC_ManifestVsLoading);
                        Ldialog.dismiss();
                    }

                    @Override
                    public void onFailed(String message) {
                        Ldialog.dismiss();
                        ToastUtils.showToast(mContext,message,Toast.LENGTH_SHORT);
                    }

                    @Override
                    public void onError() {
                        Ldialog.dismiss();
                        ToastUtils.showToast(mContext,"数据获取出错", Toast.LENGTH_SHORT);
                    }
                },page);
    }
    //endregion

    //region 把数据绑定到Model
    private void setDatas(List<GNCManifestVSLoading> CGO, int type) {
        pulltorefreshview.setLoadMoreEnable(false);
        pulltorefreshview.setPullRefreshEnable(false);


        if (CGO.size() > 0) {
            List<TableModel> mDatas = new ArrayList<>();
            for (int i = 0; i < CGO.size(); i++) {
                GNCManifestVSLoading cc = CGO.get(i);
                TableModel tableMode = new TableModel();
                tableMode.setOrgCode("");
                tableMode.setLeftTitle(cc.getCarID() + "");
                tableMode.setText0(cc.getULD() + "");//列0内容
                tableMode.setText1(cc.getDest() + "");//列1内容
                tableMode.setText2(cc.getNetWeight() + "");//列2内容
                tableMode.setText3(cc.getCargoWeight() + "");
                tableMode.setText4(cc.getAwbWeight() + "");
                tableMode.setText5(cc.getContrast() + "");//
                tableMode.setText6(cc.getResult() +"");//

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

    //endregion
}
