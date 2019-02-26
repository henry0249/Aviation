package com.example.administrator.aviation.ui.cgo.domestic;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Region;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.aviation.R;
import com.example.administrator.aviation.model.adapter.ListViewAdapter;
import com.example.administrator.aviation.ui.base.NavBar;
import com.example.administrator.aviation.util.AviationCommons;
import com.example.administrator.aviation.util.ToastUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.administrator.aviation.R.id.pulltorefreshview;
import static com.example.administrator.aviation.util.AviationCommons.GNC_ULDinfo_CAMERA_REQUEST;
import static java.security.AccessController.getContext;

public class expULDcargoInfo extends AppCompatActivity {

    //region Button控件
    @BindView(R.id.uldInfo_btn_ZhuangZaiXinXi)
    Button ZhuangZaiXinXi;
    @BindView(R.id.uldInfo_btn_DaiZhuangXinXi)
    Button DaiZhuangXinXi;
    //endregion

    //region 自定义变量和控件
    private NavBar navBar;
    private Context Mcontext;
    private PopupWindow pw;
    private ZhuangZaiFragment zhuangzaiFragment;
    private DaiZhuangFragment daizhuangFragment;
    private AlertDialog.Builder inputDialog;
    private AlertDialog ad;
    private EditText diaEdit;
    private Fragment currentFragment;

    private final String TAG = "expULDcargoInfo";
    private int PageFlag;
    private HashMap<String, String> idArrary;
    //endregion

    //region 初始化

    //region 入口函数
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mcontext = expULDcargoInfo.this;
        setContentView(R.layout.activity_exp_uldcargo_info);
        ButterKnife.bind(this);
        initView();
        switchFragment(zhuangzaiFragment,0).commit();
    }
    //endregion

    //region 初始化变量和控件
    private void initView() {
        PageFlag = 0;
        idArrary = new HashMap<>();
        idArrary = (HashMap<String, String>) getIntent().getSerializableExtra("Info");
        navBar = new NavBar(this);
        navBar.setTitle("装载详情 " + idArrary.get("BanID").toString());
        navBar.setRight(R.drawable.ic_menu_two);
        zhuangzaiFragment = new ZhuangZaiFragment();
        daizhuangFragment = new DaiZhuangFragment();
        currentFragment = new Fragment();
        setListener();
    }
    //endregion

    //endregion

    //region 控件事件

    //region 页面上所有的点击事件
    private void setListener() {

        //region 标题栏右边图片的点击事件
        navBar.getRightImageView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                View myView = LayoutInflater.from(expULDcargoInfo.this).inflate(R.layout.pop_expuld_info, null);
//                pw = new PopupWindow(myView, 400, ViewGroup.LayoutParams.WRAP_CONTENT, true);
//                pw.showAsDropDown(navBar.getPopMenuView());
//
//                List list = new ArrayList<String>();
//                list.add(0,"扫描装卸");
//
//                uldAdapter ul = new uldAdapter(expULDcargoInfo.this, R.layout.pop_expuld_list_item, list);
//                ListView lv = (ListView) myView.findViewById(R.id.list_pop_expUld);
//                lv.setAdapter(ul);
//
//                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
//                    @Override
//                    public void onItemClick(AdapterView<?> parent, View view,
//                                            int position, long id) {
//                        pw.dismiss();
//                        if (position == 0) {
//                            useCamera();
//                        }
//                    }
//                });
            }
        });
        //endregion

        //region 装载信息按钮点击事件
        ZhuangZaiXinXi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ZhuangZaiXinXi.setBackground(getResources().getDrawable(R.drawable.button_selector));
                ZhuangZaiXinXi.setTextColor(getResources().getColor(R.color.colorGray));
                DaiZhuangXinXi.setBackground(getResources().getDrawable(R.drawable.button_noselector));
                DaiZhuangXinXi.setTextColor(getResources().getColor(R.color.colorTitle));
                PageFlag = 0;
                switchFragment(zhuangzaiFragment,0).commit();
            }
        });
        //endregion

        //region 待装信息按钮点击事件
        DaiZhuangXinXi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DaiZhuangXinXi.setBackground(getResources().getDrawable(R.drawable.button_selector));
                DaiZhuangXinXi.setTextColor(getResources().getColor(R.color.colorGray));
                ZhuangZaiXinXi.setBackground(getResources().getDrawable(R.drawable.button_noselector));
                ZhuangZaiXinXi.setTextColor(getResources().getColor(R.color.colorTitle));
                PageFlag = 1;
                switchFragment(daizhuangFragment,1).commit();
            }
        });
        //endregion

    }
    //endregion

    //endregion

    //region 功能方法

    //region 碎片切换方法
    private FragmentTransaction switchFragment(Fragment targetFragment,int index) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        Bundle bundle = new Bundle();

        if (!targetFragment.isAdded()) {
            //第一次使用switchFragment()时currentFragment为null，所以要判断一下
            if (currentFragment != null) {
                transaction.hide(currentFragment);
            }

            if (index == 0) {
                bundle.putSerializable("zhuangzai",idArrary);
            } else if (index == 1) {
                bundle.putSerializable("daizhuang",idArrary);
            }

            targetFragment.setArguments(bundle);
            transaction.add(R.id.framelayout_uld, targetFragment,targetFragment.getClass().getName());
        } else {
            transaction.hide(currentFragment).show(targetFragment);
        }

        currentFragment = targetFragment;
        return transaction;
    }
    //endregion

    //region 调用相机
    private void useCamera() {
        Intent intent = new Intent(expULDcargoInfo.this, CaptureActivity.class);
        intent.putExtra("id",GNC_ULDinfo_CAMERA_REQUEST);
        startActivityForResult(intent, GNC_ULDinfo_CAMERA_REQUEST);
    }
    //endregion

    //region activity界面回调事件
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case GNC_ULDinfo_CAMERA_REQUEST:
                if (resultCode == AviationCommons.GNC_ULDinfo_CAMERA_RESULT) {
                    Bundle bundle = data.getExtras();
                    String re = bundle.getString("result");

                    if (TextUtils.isEmpty(re)) {
                        LayoutInflater abcInflater = LayoutInflater.from(Mcontext);
                        inputDialog = new AlertDialog.Builder(Mcontext);
                        View newPlanDialog = abcInflater.inflate(R.layout.dialog_saomiao_zhuanghuo, (ViewGroup) findViewById(R.id.dia_saomiaoZhuangHuo));
                        inputDialog.setView(newPlanDialog);

                        if (PageFlag == 0) {
                            inputDialog.setPositiveButton("卸货",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            //...To-do
                                        }
                                    });
                        } else if (PageFlag == 1) {
                            inputDialog.setPositiveButton("装货",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            //...To-do
                                        }
                                    });
                        }

                        ad = inputDialog.create();
                        ad.show();
                        diaEdit = (EditText) ad.findViewById(R.id.DiaAdit_SaoMiaoYunDanHao);
                        diaEdit.setTextColor(Color.rgb(0, 0, 0));
                        diaEdit.setText(re);
                        diaEdit.setSelection(diaEdit.getText().toString().trim().length());
                    } else {
                        ToastUtils.showToast(Mcontext,"成功",Toast.LENGTH_LONG);
                    }
                }
        }
    }
    //endregion

    // region 自定义uld适配器
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

    //endregion
}
