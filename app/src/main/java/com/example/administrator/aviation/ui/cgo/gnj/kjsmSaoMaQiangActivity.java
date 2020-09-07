package com.example.administrator.aviation.ui.cgo.gnj;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.aviation.R;
import com.example.administrator.aviation.http.HttpCommons;
import com.example.administrator.aviation.http.HttpRoot;
import com.example.administrator.aviation.model.adapter.PopWindowsAdapter;
import com.example.administrator.aviation.sys.PublicFun;
import com.example.administrator.aviation.ui.base.NavBar;
import com.example.administrator.aviation.ui.dialog.LoadingDialog;
import com.example.administrator.aviation.util.PreferenceUtils;
import com.example.administrator.aviation.util.ToastUtils;
import com.example.administrator.aviation.view.AutofitTextView;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;

import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class kjsmSaoMaQiangActivity extends AppCompatActivity {

    //region 自定义变量
    private Context mContext;
    private Activity mAct;
    private NavBar navBar;
    private String Numbers = "";
    private final String page = "other";
    private final String blank = "  ";
    private List<View> views;
    private int CountFlag = 0;

    //endregion

    //region 未预设XML控件
    private LoadingDialog Ldialog;
    private PopupWindow pw;
    private int mCurrentDialogStyle = com.qmuiteam.qmui.R.style.QMUI_Dialog;
    //endregion

    //region 其他控件
    @BindView(R.id.kjsmSaoMaQiang_Img_chaxun)
    ImageView Img_chaxun;
    @BindView(R.id.kjsmSaoMaQiang_Img_QingKong)
    ImageView Img_QingKong;
    @BindView(R.id.kjsmSaoMaQiang_Scroll)
    ScrollView Scrll;
    //endregion

    //region Layout控件

    //endregion

    //region Button控件

    //endregion

    //region EditText控件
    @BindView(R.id.kjsmSaoMaQiang_EdTxt_BaoHao)
    EditText EdTxt_BaoHao;
    //endregion

    //region 滚动View控件

    //endregion

    //region TextView控件
    @BindView(R.id.kjsmSaoMaQiang_Txt_BaoHao)
    TextView Txt_BaoHao;
    @BindView(R.id.kjsmSaoMaQiang_Txt_JieGuo)
    AutofitTextView Txt_JieGuo;

    //endregion

    //region 初始化

    //region 入口函数
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kjsm_sao_ma_qiang);

        mContext = kjsmSaoMaQiangActivity.this;
        mAct = (Activity) mContext;
        ButterKnife.bind(this);
        init();
    }
    //endregion

    //region 设置初始化
    private void init() {
        navBar = new NavBar(this);
        navBar.setTitle("扫码枪模式");

        Ldialog = new LoadingDialog(mContext);
        navBar.setRight(R.drawable.ic_menu);

        views = new ArrayList<View>();
        views.add(EdTxt_BaoHao);


        setTextEmpty();
        setListener();
    }
    //endregion

    //region 输入框置空
    private void setTextEmpty() {
        EdTxt_BaoHao.setText("");
        Txt_JieGuo.setText("");
        Txt_BaoHao.setText("");

    }
    //endregion

    //endregion

    //region 类的Intent

    //endregion

    //endregion

    //region 控件事件

    //region 页面上所有的点击事件
    private void setListener() {
        Img_chaxun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(EdTxt_BaoHao.getText().toString().trim())) {
                    String re = EdTxt_BaoHao.getText().toString().trim();
                    EdTxt_BaoHao.setText("");
                    Txt_JieGuo.setText(re);
                    HashMap<String, String> go = new HashMap<String, String>();
                    go.put("PackageNO", re);
                    go.put("ErrString", "");
                    GetInfo(go);
                }
            }
        });

        Img_QingKong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EdTxt_BaoHao.setText("");
                Txt_JieGuo.setText("");
            }
        });


        EdTxt_BaoHao.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    // 监听到回车键，会执行2次该方法。按下与松开
                    if (event.getAction() == KeyEvent.ACTION_UP && !TextUtils.isEmpty(EdTxt_BaoHao.getText().toString().trim())) {
                        String re = EdTxt_BaoHao.getText().toString().trim();
                        EdTxt_BaoHao.setText("");
                        Txt_JieGuo.setText(re);
                        HashMap<String, String> go = new HashMap<String, String>();
                        go.put("PackageNO", re);
                        go.put("ErrString", "");
                        GetInfo(go);
                    }
                }
                return false;

            }
        });

        //region navBar右侧图片的点击事件
        navBar.getRightImageView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final QMUIDialog.EditTextDialogBuilder builder = new QMUIDialog.EditTextDialogBuilder(mAct);
                View myView = LayoutInflater.from(mAct).inflate(R.layout.pop_expuld_info, null);
                pw = new PopupWindow(myView, 350, ViewGroup.LayoutParams.WRAP_CONTENT, true);
                pw.showAsDropDown(navBar.getPopMenuView());

                List list = new ArrayList<String>();
                list.add(0,"相机模式");

                PopWindowsAdapter ul = new PopWindowsAdapter(mAct, R.layout.pop_expuld_list_item, list);
                ListView lv = (ListView) myView.findViewById(R.id.list_pop_expUld);
                lv.setAdapter(ul);

                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                        pw.dismiss();
                        if (position == 0) {
                            if (PublicFun.isCameraCanUse()) {
                                Intent ScanCommand = new Intent(mAct, KuaiJianSaoMiaoMainActivity.class);
                                startActivity(ScanCommand);
                            } else {
                                new QMUIDialog.MessageDialogBuilder(mAct)
                                        .setTitle("提示")
                                        .setMessage("相机模式不可用，请打开系统相机，并保持驻留在后台！")
                                        .addAction("取消", new QMUIDialogAction.ActionListener() {
                                            @Override
                                            public void onClick(QMUIDialog dialog, int index) {
                                                dialog.dismiss();
                                            }
                                        })
                                        .create(mCurrentDialogStyle).show();
                            }
                        }
                    }
                });
            }
        });
        //endregion
    }
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

            return false;
        }
    });
    //endregion

    //region 请求数据
    private void GetInfo(Map<String, String> p) {
        Ldialog.show();
        HttpRoot.getInstance().requstAync(mContext, HttpCommons.GET_CGO_IntExportKJScanCommand_NAME, HttpCommons.GET_CGO_IntExportKJScanCommand_ACTION,p,
                new HttpRoot.CallBack() {
                    @Override
                    public void onSucess(Object result) {
                        SoapObject object = (SoapObject) result;
                        String num = object.getProperty(0).toString();
                        String time = PublicFun.getDateStr("yyyy-MM-dd HH:mm:ss");

                        CountFlag += 1;


                        if (!TextUtils.isEmpty(time) && !TextUtils.isEmpty(num)) {
                            int xx = Integer.parseInt(num);
                            if (xx >= 0) {
                                Numbers +=  CountFlag + "." + blank + time.split(" ")[1] + blank + "查验票数:" + num  + "\n";
                            } else {
                                Numbers += CountFlag + "." + blank + time.split(" ")[1] + blank + object.getProperty(1).toString() + "\n";
                            }
                        } else {
                            Numbers += CountFlag + "." + blank + time.split(" ")[1] + blank + "返回值未识别!"  + "\n";
                        }

                        Txt_BaoHao.setText(Numbers);
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                int offset = Txt_BaoHao.getMeasuredHeight() - Scrll.getHeight();
                                if (offset > 0) {
                                    Scrll.fullScroll(ScrollView.FOCUS_DOWN);
                                }
                            }
                        });
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

    //region 文本框赋值

    //endregion

    //endregion
}
