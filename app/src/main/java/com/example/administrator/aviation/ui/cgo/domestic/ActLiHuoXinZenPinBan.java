package com.example.administrator.aviation.ui.cgo.domestic;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.text.method.ReplacementTransformationMethod;
import android.util.ArrayMap;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.administrator.aviation.R;
import com.example.administrator.aviation.http.HttpCommons;
import com.example.administrator.aviation.http.HttpRoot;
import com.example.administrator.aviation.model.hygnc.ParseULDEntity;
import com.example.administrator.aviation.model.hygnc.ULDEntity;
import com.example.administrator.aviation.sys.PublicFun;
import com.example.administrator.aviation.tool.AllCapTransformationMethod;
import com.example.administrator.aviation.ui.base.NavBar;
import com.example.administrator.aviation.ui.dialog.LoadingDialog;
import com.example.administrator.aviation.util.AviationCommons;
import com.example.administrator.aviation.util.ToastUtils;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;

import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.administrator.aviation.util.AviationCommons.GNC_ULDLOADING_XinZenPinBan_REQUEST;

/**
 * Created by Administrator on 2018/2/7.
 */

public class ActLiHuoXinZenPinBan extends AppCompatActivity {

    //region EditText控件
    @BindView(R.id.uldloading_edtTxt_PinBanHao_A)
    EditText PinBanHao_A;
    @BindView(R.id.uldloading_edtTxt_PinBanZhong_A)
    EditText PinBanZhong_A;
    @BindView(R.id.uldloading_edtTxt_uldBianHao_A)
    EditText uldBianHao_A;
    @BindView(R.id.uldloading_edtTxt_uldBianZhong_A)
    EditText uldBianZhong_A;
    @BindView(R.id.uldloading_edtTxt_uldBianHao_B)
    EditText uldBianHao_B;
    @BindView(R.id.uldloading_edtTxt_uldBianZhong_B)
    EditText uldBianZhong_B;
    //endregion

    //region Button控件
    @BindView(R.id.uldloading_Btn_QueDin)
    Button btn_QueDin;
    @BindView(R.id.uldloading_Btn_QinChu)
    Button btn_QinChu;
    //endregion

    //region Layout控件
    @BindView(R.id.LiHuoXinZenA)
    LinearLayout LiHuoXinZenA;
    @BindView(R.id.LiHuoXinZenB)
    LinearLayout LiHuoXinZenB;
    //endregion

    //region 自定义控件
    private NavBar navBar;
    private QMUIDialog qmuiDialog;
    private LoadingDialog Ldialog;
    //endregion

    //region 自定义全局变量
    private final String TAG = "ActLiHuoXinZenPinBan";
    private final String page = "one";
    private Context mContext;
    private Activity mAct;
    private int uldFlag = 0;
    private List<ULDEntity> ulden;
    //endregion

    //region 初始化

    //region 活动的创建
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gnlh_xinzenpinban);
        mContext = ActLiHuoXinZenPinBan.this;
        mAct = (Activity) mContext;
        ButterKnife.bind(this);
        initView();
    }
    //endregion

    //region 初始化变量和控件
    private void initView() {
        navBar = new NavBar(this);
        navBar.setTitle("新增平板");

        ulden = new ArrayList<>();

        uldBianHao_A.setTransformationMethod(new AllCapTransformationMethod());
        uldBianHao_B.setTransformationMethod(new AllCapTransformationMethod());
        Ldialog = new LoadingDialog(mContext);

        EditViewSetEmpty();
        setListener();

        qmuiDialog = new QMUIDialog.MessageDialogBuilder(mContext)
                .setTitle("创建新ULD")
                .setMessage("输入的ULD号不存在，是否创建？")
                .addAction("确定", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                        if (uldFlag == 1) {
                            CreatULDInfo(uldBianHao_A.getText().toString().toUpperCase().trim());
                        }else if (uldFlag == 2) {
                            CreatULDInfo(uldBianHao_B.getText().toString().toUpperCase().trim());
                        }

                    }
                })
                .addAction("取消", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                        if (uldFlag == 1) {
                            uldBianHao_A.setText("");
                            uldBianZhong_A.setText("");
                        } else if (uldFlag == 2) {
                            uldBianHao_B.setText("");
                            uldBianZhong_B.setText("");
                        }
                    }
                })
                .create();
    }
    //endregion

    //region 所有输入框置空
    private void EditViewSetEmpty() {
        PinBanHao_A.setText("");
        PinBanZhong_A.setText("");
        uldBianHao_A.setText("");
        uldBianZhong_A.setText("");
        uldBianHao_B.setText("");
        uldBianZhong_B.setText("");
    }
    //endregion

    //endregion

    //region 控件事件

    //region 页面上所有的点击事件
    private void setListener() {
        //region 确定按钮点击事件
        btn_QueDin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pinBan = PinBanHao_A.getText().toString().trim();

                if (TextUtils.isEmpty(PinBanZhong_A.getText().toString().trim()) || TextUtils.isEmpty(PinBanHao_A.getText().toString().trim())) {
                    ToastUtils.showToast(mContext," 平板信息不可为空！",Toast.LENGTH_SHORT);
                    return;
                } else if (!TextUtils.isEmpty(uldBianHao_A.getText().toString().trim()) && TextUtils.isEmpty(uldBianZhong_A.getText().toString().trim())) {
                    ToastUtils.showToast(mContext," ULD_1自重不可为空！",Toast.LENGTH_SHORT);
                    return;
                } else if (!TextUtils.isEmpty(uldBianHao_B.getText().toString().trim()) && TextUtils.isEmpty(uldBianZhong_B.getText().toString().trim())) {
                    ToastUtils.showToast(mContext," ULD_2自重不可为空！",Toast.LENGTH_SHORT);
                    return;
                }

                pinBan = PublicFun.getPinBanHao(pinBan);

                ArrayMap<String,String> re = new ArrayMap<>();
                re.put(uldBianHao_A.getText().toString().toUpperCase().trim(), uldBianZhong_A.getText().toString().toUpperCase().trim());
                re.put(uldBianHao_B.getText().toString().toUpperCase().trim(), uldBianZhong_B.getText().toString().toUpperCase().trim());

                CreatGNCULDLoading(getUpdateXml(pinBan,PinBanZhong_A.getText().toString().trim(),re));
            }
        });
        //endregion

        //region 平板编号自动变大写
        PinBanHao_A.setTransformationMethod(new ReplacementTransformationMethod() {
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

        //region 清空按钮
        btn_QinChu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditViewSetEmpty();
            }
        });
        //endregion

        //region ULD_A焦点改变—获得事件
        uldBianZhong_A.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    if (!TextUtils.isEmpty(uldBianHao_A.getText().toString().toUpperCase().trim())) {
                        uldFlag = 1;
                        ULDisExist(uldBianHao_A.getText().toString().toUpperCase().trim());
                    }
                }
            }
        });
        //endregion

        //region ULD_B焦点改变—获得事件
        uldBianZhong_B.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    if (!TextUtils.isEmpty(uldBianHao_B.getText().toString().toUpperCase().trim())) {
                        uldFlag = 2;
                        ULDisExist(uldBianHao_B.getText().toString().toUpperCase().trim());
                    }
                }
            }
        });
        //endregion
    }
    //endregion

    //endregion

    //region 功能方法

    //region finish前清空消息队列，并回收垃圾
    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
        System.gc();
    }
    //endregion

    //region 新增成功后的回调查询方法
    private void FanHui() {
        Integer req = (Integer) getIntent().getSerializableExtra("id");
        Intent intent = new Intent(ActLiHuoXinZenPinBan.this,expULDLoading.class);
        if (req == GNC_ULDLOADING_XinZenPinBan_REQUEST) {
            Bundle bundle = new Bundle();
            if (TextUtils.isEmpty(PinBanHao_A.getText().toString().trim())) {
                bundle.putString("result", "");
            } else {
                bundle.putString("result", PinBanHao_A.getText().toString().trim());
            }

            intent.putExtras(bundle);
            setResult(AviationCommons.GNC_ULDLOADING_XinZenPinBan_RESULT, intent);
            ActLiHuoXinZenPinBan.this.finish();
        } else {
            ToastUtils.showToast(ActLiHuoXinZenPinBan.this,"上层请求码错误，无法回调！",Toast.LENGTH_SHORT);
        }
    }
    //endregion

    //region 处理UI的线程
    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            View rootview = mAct.getWindow().getDecorView();
            if (rootview.findFocus() != null) {
                View FocusView = rootview.findFocus();
                FocusView.clearFocus();
                //焦点失去时的校验方法，用于更新ID
                switch (msg.what) {
                    case 6:
                        FanHui();
                        break;
                }
            }
            return false;
        }
    });
    //endregion

    //region 校验平板是否存在的方法
    private void ULDisExist (String uldID) {
        String a = "";
        ArrayMap<String, String> p = new ArrayMap<>();
        p.put("ULD",uldID);
        p.put("nowAirPort","NKG");
        p.put("ErrString","");
        ulden = new ArrayList<>();
        Ldialog.show();
        HttpRoot.getInstance().requstAync(ActLiHuoXinZenPinBan.this, HttpCommons.CGO_DOM_Exp_GetEQMULD_NAME , HttpCommons.CGO_DOM_Exp_GetEQMULD_ACTION, p,
                new HttpRoot.CallBack() {
                    @Override
                    public void onSucess(Object result) {
                        SoapObject object = (SoapObject) result;
                        String xx = object.getProperty(0).toString();
                        ulden = ParseULDEntity.parseULDEntityXMLto(xx);
                        if (ulden.size() > 0 && TextUtils.isEmpty(ulden.get(0).getULD())) {
                            if (uldFlag == 1) {
                                uldBianZhong_A.setText("");
                                uldBianHao_A.requestFocus();
                            } else if (uldFlag == 2) {
                                uldBianZhong_B.setText("");
                                uldBianHao_B.requestFocus();
                            }

                            Ldialog.dismiss();
                            qmuiDialog.show();
                        } else {
                            if (uldFlag == 1) {
                                uldBianZhong_A.setText(ulden.get(0).getULDWeight());
                                uldBianZhong_A.setSelection(uldBianZhong_A.getText().toString().trim().length());
                            }else if (uldFlag == 2) {
                                uldBianZhong_B.setText(ulden.get(0).getULDWeight());
                                uldBianZhong_B.setSelection(uldBianZhong_B.getText().toString().trim().length());
                            }

                            Ldialog.dismiss();
                        }
                    }

                    @Override
                    public void onFailed(String message) {
                        ToastUtils.showToast(ActLiHuoXinZenPinBan.this,message,Toast.LENGTH_SHORT);
                        if (uldFlag == 1) {
                            uldBianHao_A.setText("");
                            uldBianZhong_A.setText("");
                            uldBianHao_A.requestFocus();
                        } else if (uldFlag == 2) {
                            uldBianHao_B.requestFocus();
                            uldBianZhong_B.setText("");
                            uldBianHao_B.setText("");
                        }
                        Ldialog.dismiss();
                    }

                    @Override
                    public void onError() {
                        ToastUtils.showToast(ActLiHuoXinZenPinBan.this,"数据获取出错",Toast.LENGTH_SHORT);
                        Ldialog.dismiss();
                    }
                },page);
    }
    //endregion

    //region 创建新ULD
    private void CreatULDInfo (String uldID) {
        ArrayMap<String, String> p = new ArrayMap<>();
        p.put("ULD",uldID);
        p.put("nowAirPort","NKG");
        p.put("ErrString","");
        HttpRoot.getInstance().requstAync(ActLiHuoXinZenPinBan.this, HttpCommons.CGO_DOM_Exp_CreatULDInfo_NAME , HttpCommons.CGO_DOM_Exp_CreatULDInfo_ACTION, p,
                new HttpRoot.CallBack() {
                    @Override
                    public void onSucess(Object result) {
                        SoapObject object = (SoapObject) result;
                        String xx = object.getProperty(0).toString();
                        ulden = ParseULDEntity.parseULDEntityXMLto(xx);
                        if (ulden.size() > 0 && !TextUtils.isEmpty(ulden.get(0).getULD())) {
                            if (uldFlag == 1) {
                                uldBianZhong_A.setText(ulden.get(0).getULDWeight());

                                uldBianHao_A.setText(ulden.get(0).getULD());
                                uldBianHao_A.setSelection( uldBianHao_A.getText().toString().trim().length());
                            } else if (uldFlag == 2) {
                                uldBianZhong_B.setText(ulden.get(0).getULDWeight());

                                uldBianHao_B.setText(ulden.get(0).getULD());
                                uldBianHao_B.setSelection( uldBianHao_B.getText().toString().trim().length());
                            };
                        }
                    }

                    @Override
                    public void onFailed(String message) {
                        ToastUtils.showToast(ActLiHuoXinZenPinBan.this,message,Toast.LENGTH_SHORT);
                        if (uldFlag == 1) {
                            uldBianHao_A.setText("");
                            uldBianZhong_A.setText("");
                        } else if (uldFlag == 2) {
                            uldBianHao_B.setText("");
                            uldBianZhong_B.setText("");
                        }
                    }

                    @Override
                    public void onError() {
                        ToastUtils.showToast(ActLiHuoXinZenPinBan.this,"数据获取出错",Toast.LENGTH_SHORT);
                    }
                },page);
    }
    //endregion

    //region 创建新平板
    private void CreatGNCULDLoading (Map<String, String> p) {
        HttpRoot.getInstance().requstAync(ActLiHuoXinZenPinBan.this, HttpCommons.CGO_DOM_Exp_CreatGNCULDLoading_NAME, HttpCommons.CGO_DOM_Exp_CreatGNCULDLoading_ACTION, p,
                new HttpRoot.CallBack() {
                    @Override
                    public void onSucess(Object result) {
                        SoapObject object = (SoapObject) result;
                        Log.i(TAG, object.toString());
                        String res = object.getProperty(0).toString();
                        if (res.contains("true")) {
                            ToastUtils.showToast(ActLiHuoXinZenPinBan.this,"新增成功",Toast.LENGTH_SHORT);
                            handler.sendEmptyMessage(6);
                        }
                    }

                    @Override
                    public void onFailed(String message) {
                        ToastUtils.showToast(ActLiHuoXinZenPinBan.this,message,Toast.LENGTH_SHORT);
                    }

                    @Override
                    public void onError() {
                        ToastUtils.showToast(ActLiHuoXinZenPinBan.this,"数据获取出错",Toast.LENGTH_SHORT);
                    }
                },page);
    }
    //endregion

    //region 生成创建新平板用的XML
    private Map<String,String> getUpdateXml(String CarID,String CarWeight,ArrayMap<String,String> uldloading) {
        StringBuilder sb = new StringBuilder();
        Map<String, String> pa = new HashMap<>();
        sb.append("<?xml version='1.0' encoding='UTF-8'?>");
        sb.append("<GNCCarUseRecord>");
        sb.append("  <CarID>" + CarID + "</CarID>");
        sb.append("  <CarWeight>" + CarWeight + "</CarWeight>");

        Set<String> keys = uldloading.keySet();
        for (String k : keys) {
            sb.append("  <Loading>");
            sb.append("    <ULD>" + k + "</ULD>");
            sb.append("    <ULDWeight>" + uldloading.get(k) + "</ULDWeight>");
            sb.append("  </Loading>");
        }
        sb.append("</GNCCarUseRecord>");

        pa.put("CarUseXml", sb.toString());
        pa.put("ErrString", "");
        return pa;
    }
    //endregion

    //endregion
}
