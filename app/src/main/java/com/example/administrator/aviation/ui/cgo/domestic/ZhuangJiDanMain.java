package com.example.administrator.aviation.ui.cgo.domestic;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.aviation.R;
import com.example.administrator.aviation.model.adapter.AbsCommonAdapter;
import com.example.administrator.aviation.model.adapter.AbsViewHolder;
import com.example.administrator.aviation.sys.PublicFun;
import com.example.administrator.aviation.tool.DateUtils;
import com.example.administrator.aviation.ui.base.AbPullToRefreshView;
import com.example.administrator.aviation.ui.base.NavBar;
import com.example.administrator.aviation.ui.base.SyncHorizontalScrollView;
import com.example.administrator.aviation.ui.base.TableModel;
import com.example.administrator.aviation.util.AviationCommons;
import com.example.administrator.aviation.util.PreferenceUtils;
import com.example.administrator.aviation.util.ToastUtils;
import com.example.administrator.aviation.view.AutofitTextView;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

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
    //endregion

    //region 未预设XML控件

    //endregion

    //region 其他控件
    @BindView(R.id.ZhuangJiDan_left_container_listview)
    ListView leftListView;
    @BindView(R.id.ZhuangJiDan_right_container_listview)
    ListView rightListView;
    //endregion

    //region Layout控件

    //endregion

    //region Button控件

    //endregion

    //region EditText控件

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
    @BindView(R.id.ZhuangJiDan_tv_table_title_left)
    TextView txt_RightTitle;
    //endregion

    //region 初始化

    //region 入口函数
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zhuangjidan_main);
        mContext =ZhuangJiDanMain.this;
        mAct = (Activity) mContext;
        ButterKnife.bind(this);
        init();
    }
    //endregion

    //region 设置初始化
    public void init() {
        navBar = new NavBar(this);
        navBar.setTitle("国内出港装机单");
        txt_RightTitle.setText("平板");
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        LinearLayout linearLayout = (LinearLayout)findViewById(R.id.ZhuangJiDan_right_title_container);
        layoutInflater.inflate(R.layout.zhuangjidan_table_right_title,linearLayout,true);

        // 设置两个水平控件的联动
        titleHorScv.setScrollView(contentHorScv);
        contentHorScv.setScrollView(titleHorScv);

        findTitleTextViewIds();
        TxtViewSetEmpty();
        setListener();
        initTableView();
        setDatas();
    }
    //endregion

    //region 输入框置空
    private void TxtViewSetEmpty() {

    }
    //endregion

    //region 利用反射初始化标题的TextView的item引用
    private void findTitleTextViewIds() {
        mTitleTvArray = new SparseArray<>();
        for (int i = 0; i < 8; i++) {
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
    public void initTableView() {
        mLeftAdapter = new AbsCommonAdapter<TableModel>(mContext, R.layout.table_left_item_one) {
            @Override
            public void convert(AbsViewHolder helper, TableModel item, int pos) {
                TextView tv_table_content_left = helper.getView(R.id.tv_table_content_item_left_one);
                if (TextUtils.isEmpty(item.getText0())) {
                    item.setTextColor(tv_table_content_left,item.getLeftTitle());
                }

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
                tv_table_content_right_item12.setText(item.getText12());
                tv_table_content_right_item13.setText(item.getText13());


                //部分行设置颜色凸显
//                item.setTextColor(tv_table_content_right_item0, item.getText0());
//                item.setTextColor(tv_table_content_right_item5, item.getText5());
//                item.setTextColor(tv_table_content_right_item10, item.getText10());
//                item.setTextColor(tv_table_content_right_item14, item.getText14());

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
                    ToastUtils.showToast(mContext,"hello", Toast.LENGTH_SHORT);


                }
                break;
            default:
                break;

        }
    }
    //endregion

    //region 页面上所有的点击事件
    private void setListener() {

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

    //region 打开编辑区

    //endregion

    //region 关闭编辑区

    //endregion

    //region 句柄监听

    //endregion

    //region 请求数据

    //endregion

    //region 文本框赋值

    //endregion

    //region 把数据绑定到Model
    private void setDatas() {
        pulltorefreshview.setLoadMoreEnable(false);


        List<TableModel> mDatas = new ArrayList<>();
        TableModel tableMode = new TableModel();
        tableMode.setOrgCode("000");
        tableMode.setLeftTitle("120");
        tableMode.setText0("");//列0内容
        tableMode.setText1("002");//列1内容
        tableMode.setText2("003");//列2内容
        tableMode.setText3("004");
        tableMode.setText4("005");
        tableMode.setText5("006");//
        tableMode.setText6("007");//
        tableMode.setText7("008");//
        tableMode.setText8("009");//
        tableMode.setText9("010");//
        tableMode.setText10("011");//
        tableMode.setText11("012");//
        tableMode.setText12("013");//
        tableMode.setText13("014");//


        mDatas.add(tableMode);
        boolean isMore = false;

        mLeftAdapter.addData(mDatas, isMore);
        mRightAdapter.addData(mDatas, isMore);

        mDatas.clear();


        pulltorefreshview.onHeaderRefreshFinish();
    }
    //endregion

    //endregion

}
