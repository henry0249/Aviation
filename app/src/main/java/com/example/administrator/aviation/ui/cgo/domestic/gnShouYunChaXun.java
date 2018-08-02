package com.example.administrator.aviation.ui.cgo.domestic;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.aviation.R;
import com.example.administrator.aviation.model.adapter.AbsCommonAdapter;
import com.example.administrator.aviation.model.adapter.AbsViewHolder;
import com.example.administrator.aviation.model.hygnc.ULDLoadingCargo;
import com.example.administrator.aviation.sys.PublicFun;
import com.example.administrator.aviation.ui.base.AbPullToRefreshView;
import com.example.administrator.aviation.ui.base.NavBar;
import com.example.administrator.aviation.ui.base.SyncHorizontalScrollView;
import com.example.administrator.aviation.ui.base.TableModel;
import com.example.administrator.aviation.ui.dialog.LoadingDialog;
import com.example.administrator.aviation.util.AviationCommons;
import com.example.administrator.aviation.util.PreferenceUtils;
import com.example.administrator.aviation.util.ToastUtils;
import com.example.administrator.aviation.util.WeakHandler;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.R.attr.type;
import static com.example.administrator.aviation.R.id.jiansuokuang;
import static com.example.administrator.aviation.R.id.pulltorefreshview;
import static com.example.administrator.aviation.R.id.tv_table_content_right_item12;
import static com.example.administrator.aviation.util.AviationCommons.GNC_ULDinfo_CAMERA_REQUEST;
import static com.example.administrator.aviation.util.AviationCommons.GNC_gnShouYun_REQUEST;

public class gnShouYunChaXun extends AppCompatActivity {

    //region 自定义变量和未预设控件
    // 初始化数据加载提示（即对话框）
    private LoadingDialog Ldialog;
    private TextView DaiLiRen;
    private AbsCommonAdapter<TableModel> mLeftAdapter, mRightAdapter;
    private WeakHandler wHandler = new WeakHandler();
    private Context mContext;
    private Activity mAct;

    private NavBar navBar;
    private PopupWindow pw;
    private final String TAG = "gnShouYunChaXunInfo";
    private final String page = "one";
    private int talHeight = 0;
    private String TongDaoHao = "";
    //用于存放标题的id,与textview引用
    private SparseArray<TextView> mTitleTvArray;
    //endregion

    //region 其他View控件
    @BindView(R.id.gnShouYun_left_container_listview)
    ListView leftListView;
    @BindView(R.id.gnShouYun_right_container_listview)
    ListView rightListView;
    //endregion

    //region Layout控件
    @BindView(R.id.gnShouYun_jiansuokuang)
    LinearLayout Layjiansuokuang;
    //endregion

    //region Button控件
    @BindView(R.id.gnShouYun_search)
    FloatingActionButton BtnFloatSearch;
    @BindView(R.id.gnShouYun_sousuoQuxiao)
    Button BtnSSQuxiao;
    @BindView(R.id.gnShouYun_PinBanSheZhi)
    FloatingActionButton BtnFloatPinBanSheZhi;
    //endregion

    //region EditText控件
    @BindView(R.id.gnShouYun_edit_RiQi)
    EditText editRiQi;
    @BindView(R.id.gnShouYun_edit_HangBan)
    EditText editHangBan;
    @BindView(R.id.gnShouYun_edit_DaiLiRen)
    EditText editDaiLiRen;
    @BindView(R.id.gnShouYun_edit_Yundan)
    EditText editYundan;
    //endregion

    //region 滚动View控件
    @BindView(R.id.gnShouYun_pulltorefreshview)
    AbPullToRefreshView pulltorefreshview;
    @BindView(R.id.gnShouYun_title_horsv)
    SyncHorizontalScrollView titleHorScv;
    @BindView(R.id.gnShouYun_content_horsv)
    SyncHorizontalScrollView contentHorScv;
    //endregion

    //region 初始化

    //region 入口函数
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gn_shou_yun_cha_xun_activity);
        mContext = gnShouYunChaXun.this;
        mAct = (Activity) mContext;
        ButterKnife.bind(this);
        init();
    }
    //endregion

    //region 设置初始化
    public void init() {
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        LinearLayout linearLayout = (LinearLayout)findViewById(R.id.gnShouYun_right_title_container);
        layoutInflater.inflate(R.layout.shouyun_table_right_title,linearLayout,true);

        // 设置两个水平控件的联动
        titleHorScv.setScrollView(contentHorScv);
        contentHorScv.setScrollView(titleHorScv);

        navBar = new NavBar(this);
        if (TextUtils.isEmpty(PreferenceUtils.getTongDaoHao(mContext))) {
            TongDaoHao = " ?";
        } else {
            TongDaoHao = " " + PreferenceUtils.getTongDaoHao(mContext);
        }
        navBar.setTitle("国内出港收运" + TongDaoHao);
        navBar.setRight(R.drawable.ic_menu_two);
        Layjiansuokuang.setVisibility(View.GONE);

        findTitleTextViewIds();
        TxtViewSetEmpty();
        setListener();
        initTableView();
        setDatas();
    }
    //endregion

    //region 输入框置空
    private void TxtViewSetEmpty() {
        editRiQi.setText("");
        editHangBan.setText("");
        editDaiLiRen.setText("");
        editYundan.setText("");
    }
    //endregion

    //region 利用反射初始化标题的TextView的item引用
    private void findTitleTextViewIds() {
        mTitleTvArray = new SparseArray<>();
        for (int i = 0; i < 8; i++) {
            try {
                Field field = R.id.class.getField("gnShouYun_tv_table_title_" + i);
                int key = field.getInt(new R.id());
                TextView textView = (TextView) findViewById(key);

                mTitleTvArray.put(key, textView);
            } catch (Exception e) {
                e.printStackTrace();
            }
        };

        for(int i = 0; i < mTitleTvArray.size(); i++) {
            int key = 0;
            key = mTitleTvArray.keyAt(i);
            TextView tx = mTitleTvArray.get(key);
//            if (tx.getText().equals("ULD")) {
//                DaiLiRen = tx;
//                DaiLiRen.setText("hello");
//            }
        };

    }
    //endregion

    //endregion

    //region 控件事件

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

        mRightAdapter = new AbsCommonAdapter<TableModel>(mContext, R.layout.shouyun_table_right_item) {
            @Override
            public void convert(AbsViewHolder helper, TableModel item, int pos) {
                TextView tv_table_content_right_item0 = helper.getView(R.id.gnShouYun_tv_table_content_right_item0);
                TextView tv_table_content_right_item1 = helper.getView(R.id.gnShouYun_tv_table_content_right_item1);
                TextView tv_table_content_right_item2 = helper.getView(R.id.gnShouYun_tv_table_content_right_item2);
                TextView tv_table_content_right_item3 = helper.getView(R.id.gnShouYun_tv_table_content_right_item3);
                TextView tv_table_content_right_item4 = helper.getView(R.id.gnShouYun_tv_table_content_right_item4);
                TextView tv_table_content_right_item5 = helper.getView(R.id.gnShouYun_tv_table_content_right_item5);
                TextView tv_table_content_right_item6 = helper.getView(R.id.gnShouYun_tv_table_content_right_item6);
                TextView tv_table_content_right_item7 = helper.getView(R.id.gnShouYun_tv_table_content_right_item7);
                TextView tv_table_content_right_item8 = helper.getView(R.id.gnShouYun_tv_table_content_right_item8);
                TextView tv_table_content_right_item9 = helper.getView(R.id.gnShouYun_tv_table_content_right_item9);
                TextView tv_table_content_right_item10 = helper.getView(R.id.gnShouYun_tv_table_content_right_item10);
                TextView tv_table_content_right_item11 = helper.getView(R.id.gnShouYun_tv_table_content_right_item11);


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


                //部分行设置颜色凸显
//                item.setTextColor(tv_table_content_right_item0, item.getText0());
//                item.setTextColor(tv_table_content_right_item5, item.getText5());
//                item.setTextColor(tv_table_content_right_item10, item.getText10());
//                item.setTextColor(tv_table_content_right_item14, item.getText14());

                for (int i = 0; i < 12; i++) {
                    View view = ((LinearLayout) helper.getConvertView()).getChildAt(i);
                    view.setVisibility(View.VISIBLE);
                }
            }
        };
        leftListView.setAdapter(mLeftAdapter);
        rightListView.setAdapter(mRightAdapter);
    }
    //endregion

    //region 页面上所有控件的点击事件
    private void setListener() {
        //region 左侧标题列监听事件
        leftListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
        //rendregion

        //region 右侧标题列监听事件
        rightListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(gnShouYunChaXun.this, gnShouYunInfo.class);
                startActivity(intent);
//                    TableModel ta = (TableModel) parent.getItemAtPosition(position);
//                    Toast.makeText(getActivity().getApplicationContext(), ta.getLeftTitle() + "_" + position , Toast.LENGTH_SHORT).show();

            }
        });
        //endregion

        //region 浮动查询按钮点击事件
        BtnFloatSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Layjiansuokuang.getVisibility() == View.GONE) {
                    Layjiansuokuang.setVisibility(View.VISIBLE);
                    editRiQi.requestFocus();
                    PublicFun.KeyBoardSwitch(mContext);
                }
            }
        });
        //endregion

        //region 浮动设置按钮点击事件
        BtnFloatPinBanSheZhi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(gnShouYunChaXun.this, gnShouYunSheZhi.class);
                startActivity(intent);
            }
        });
        //endregion

        //region 检索框中的取消按钮
        BtnSSQuxiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TxtViewSetEmpty();
                PublicFun.KeyBoardHide(mAct,mContext);
                Layjiansuokuang.setVisibility(View.GONE);
            }
        });
        //endregion

        //region 标题栏右侧图片点击按钮
        navBar.getRightImageView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View myView = LayoutInflater.from(gnShouYunChaXun.this).inflate(R.layout.pop_expuld_info, null);
                pw = new PopupWindow(myView, 400, ViewGroup.LayoutParams.WRAP_CONTENT, true);
                pw.showAsDropDown(navBar.getPopMenuView());

                List list = new ArrayList<String>();
                list.add(0,"货物扫描");

                uldAdapter ul = new uldAdapter(gnShouYunChaXun.this, R.layout.pop_expuld_list_item, list);
                ListView lv = (ListView) myView.findViewById(R.id.list_pop_expUld);
                lv.setAdapter(ul);

                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                        pw.dismiss();
                        if (position == 0) {
                            useCamera();
                        }
                    }
                });
            }
        });
        //endregion

        navBar.getTitleTextView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final QMUIDialog.EditTextDialogBuilder builder = new QMUIDialog.EditTextDialogBuilder(mAct);
                String tdh;

                if (TextUtils.isEmpty(PreferenceUtils.getTongDaoHao(mContext))) {
                    tdh = "请输入通道号";
                } else {
                    tdh = "当前通道号: " + PreferenceUtils.getTongDaoHao(mContext);
                }
                builder.setTitle("通道号")
                        .setPlaceholder(tdh)
                        .setInputType(InputType.TYPE_CLASS_TEXT)
                        .addAction("取消", new QMUIDialogAction.ActionListener() {
                            @Override
                            public void onClick(QMUIDialog dialog, int index) {
                                mHandler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        PublicFun.KeyBoardHide(mAct,mContext);
                                    }
                                }, 100);

                                dialog.dismiss();
                            }
                        })
                        .addAction("确定", new QMUIDialogAction.ActionListener() {
                            @Override
                            public void onClick(QMUIDialog dialog, int index) {
                                CharSequence text = builder.getEditText().getText();
                                if (text != null && text.length() > 0) {
                                    if (PublicFun.isNumeric(text.toString().trim())) {
                                        PreferenceUtils.saveTDH(mContext, text.toString().trim());
                                        ToastUtils.showToast(mContext, "已保存", Toast.LENGTH_SHORT);
                                        TongDaoHao = " " + PreferenceUtils.getTongDaoHao(mContext);
                                        navBar.setTitle("国内出港收运" + TongDaoHao);
                                    }
                                } else {
                                    Toast.makeText(mAct, "请输入通道号", Toast.LENGTH_SHORT).show();
                                }
                                mHandler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        PublicFun.KeyBoardHide(mAct,mContext);
                                    }
                                }, 100);

                                dialog.dismiss();

                            }
                        })
                        .show();
            }
        });

    }
    //endregion

    //endregion

    //region 功能方法

    //region 句柄监听
    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {

            return false;
        }
    });
    //endregion

    // region 自定义弹出菜单适配器
    private class uldAdapter extends ArrayAdapter<String> {
        private int resourceID;

        public uldAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<String> objects) {
            super(context, resource, objects);
            this.resourceID = resource;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            String a = getItem(position);
            View view = LayoutInflater.from(getContext()).inflate(resourceID, parent, false);
            TextView tx = (TextView) view.findViewById(R.id.pop_listitem);
            tx.setText(a);
            return view;
        }
    }
    //endregion

    //region 调用相机
    private void useCamera() {
        Intent intent = new Intent(gnShouYunChaXun.this, CaptureActivity.class);
        intent.putExtra("id",GNC_gnShouYun_REQUEST);
        startActivityForResult(intent, GNC_gnShouYun_REQUEST);
    }
    //endregion

    //region 把数据绑定到Model
    private void setDatas() {
        pulltorefreshview.setLoadMoreEnable(false);


        List<TableModel> mDatas = new ArrayList<>();
        TableModel tableMode = new TableModel();
        tableMode.setOrgCode("000");
        tableMode.setLeftTitle("000");
        tableMode.setText0("000");//列0内容
        tableMode.setText1("000");//列1内容
        tableMode.setText2("000");//列2内容
        tableMode.setText3("000");
        tableMode.setText4("000");
        tableMode.setText5("000");//
        tableMode.setText6("000");//
        tableMode.setText7("000");//
        tableMode.setText8("000");//
        tableMode.setText9("000");//
        tableMode.setText10("000");//
        tableMode.setText11("000");//


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
