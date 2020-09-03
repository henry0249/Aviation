package com.example.administrator.aviation.ui.cgo.gnj;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageView;
import android.util.ArrayMap;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.administrator.aviation.R;
import com.example.administrator.aviation.http.HttpCommons;
import com.example.administrator.aviation.http.HttpRoot;
import com.example.administrator.aviation.model.gnj.gnjPickUpConverter;
import com.example.administrator.aviation.model.gnj.gnjPickUpModel;
import com.example.administrator.aviation.sys.PublicFun;
import com.example.administrator.aviation.ui.base.NavBar;
import com.example.administrator.aviation.ui.base.RecognizeCardActivity;
import com.example.administrator.aviation.ui.dialog.LoadingDialog;
import com.example.administrator.aviation.util.AviationCommons;
import com.example.administrator.aviation.util.ToastUtils;
import com.example.administrator.aviation.view.AutofitTextView;
import com.example.administrator.aviation.view.LinePathView;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;

import org.ksoap2.serialization.SoapObject;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.R.attr.key;
import static com.example.administrator.aviation.util.AviationCommons.PickUpSignatureActivity_REQUEST;
import static com.example.administrator.aviation.util.AviationCommons.StoragePath;

public class PickUpSignatureActivity extends AppCompatActivity {

    //region 自定义变量
    private Context mContext;
    private Activity mAct;
    private NavBar navBar;
    private final String SignerjpegFile = StoragePath + "/" + "Signer" + ".jpg";
    private final String page = "one";
    private ArrayMap<String, String> PickUpMap = new ArrayMap();
    private String ErrStr = "";
    private int CardFlag = 1;
    //endregion

    //region 未预设XML控件
    private LoadingDialog Ldialog;

    //endregion

    //region 其他控件
    @BindView(R.id.PickUpSignature_LinePathView)
    LinePathView LinePathView_HuaBu;
    @BindView(R.id.PickUpSignature_ImageView1)
    AppCompatImageView ImageView1;
    @BindView(R.id.PickUpSignature_ImageView2)
    AppCompatImageView ImageView2;
    //endregion

    //region Layout控件

    //endregion

    //region Button控件
    @BindView(R.id.PickUpSignature_Btn_qingchu)
    Button Btn_qingchu;
    @BindView(R.id.PickUpSignature_Btn_PaiZhao)
    Button Btn_PaiZhao;
    @BindView(R.id.PickUpSignature_Btn_Shangchuan)
    Button Btn_Shangchuan;
    //endregion

    //region Text控件
    @BindView(R.id.PickUpSignature_txt_shuomin)
    AutofitTextView txt_shuomin;
    //endregion

    //region 滚动View控件

    //endregion

    //region TextView控件

    //endregion

    //region ImgView控件

    //endregion

    //region 初始化

    //region 入口函数
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_up_signature);

        mContext = PickUpSignatureActivity.this;
        mAct = PublicFun.getActivityByContext(mContext);
        ButterKnife.bind(this);
        initView();
    }
    //endregion

    //region 设置初始化
    private void initView() {
        navBar = new NavBar(this);
        navBar.setTitle("提取签名");

        Ldialog = new LoadingDialog(mContext);

        HashMap<String,String> p = (HashMap<String,String>) getIntent().getSerializableExtra("Info");
        for (String key : p.keySet()){
            PickUpMap.put(key, p.get(key).split("/")[0]);
            String dlv = p.get(key).split("/")[3];
            String cne = p.get(key).split("/")[4];
            if (dlv.equals("*") || cne.equals("*")) {
                CardFlag = 1;
            } else if (!dlv.equals(cne)) {
                CardFlag = 2;
            }
        }

        if (CardFlag == 1) {
            txt_shuomin.setText("一张证件拍摄时，证件放置拍摄框中间区域。");
            ImageView1.setVisibility(View.GONE);
        }

        setListener();
    }
    //endregion

    //region activity界面回调事件
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case PickUpSignatureActivity_REQUEST:
                if (resultCode == AviationCommons.PickUpSignatureActivity_RESULT) {

                    if (CardFlag == 1) {
                        File f1 = new File(StoragePath + "/" + "card" +".jpg");
                        if (f1.exists()) {
                            Bitmap bitmap = PublicFun.getLoacalBitmap(StoragePath + "/" + "card" +".jpg");
                            ImageView2.setImageBitmap(bitmap);
                        }else {
                            ToastUtils.showToast(mContext, "照片拍摄未成功!", Toast.LENGTH_SHORT);
                        }
                    } else if (CardFlag == 2){
                        File f1 = new File(StoragePath + "/" + "ShouHuoRenCard" +".jpg");
                        File f2 = new File(StoragePath + "/" + "TiHuoRenCard" +".jpg");

                        if (f1.exists()) {
                            Bitmap bitmap = PublicFun.getLoacalBitmap(StoragePath + "/" + "ShouHuoRenCard" +".jpg");
                            ImageView1.setImageBitmap(bitmap);
                        } else {
                            ToastUtils.showToast(mContext, "照片拍摄未成功!", Toast.LENGTH_SHORT);
                        }

                        if (f2.exists()) {
                            Bitmap bitmap = PublicFun.getLoacalBitmap(StoragePath + "/" + "TiHuoRenCard" +".jpg");
                            ImageView2.setImageBitmap(bitmap);
                        }else {
                            ToastUtils.showToast(mContext, "照片截取未成功!", Toast.LENGTH_SHORT);
                        }

                    }
                }
                break;
            default:
                break;
        }
    }
    //endregion

    //endregion

    //region 控件事件

    //region 页面上所有的点击事件
    private void setListener() {
        //region 清除签名按钮
        Btn_qingchu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinePathView_HuaBu.clear();
            }
        });
        //endregion

        //region 拍照按钮
        Btn_PaiZhao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, RecognizeCardActivity.class);
                intent.putExtra("id", PickUpSignatureActivity_REQUEST);
                if (CardFlag == 1) {
                    intent.putExtra("Height", 600);
                } else if (CardFlag == 2) {
                    intent.putExtra("Height", 1200);
                }

                startActivityForResult(intent, PickUpSignatureActivity_REQUEST);
            }
        });
        //endregion

        //region 上传按钮
        Btn_Shangchuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinePathView_HuaBu.saveJPEG();
                File mFile= new File(SignerjpegFile);

                if (CardFlag == 1) {
                    File cFile = new File(StoragePath + "/" + "card" +".jpg");
                    if (mFile.exists() && cFile.exists()) {
                        byte[] SignerJepgByte = PublicFun.FileToByte(SignerjpegFile);
                        String SignerJepgStr = PublicFun.EncodeBase64(SignerJepgByte);

                        byte[] sJepgByte = PublicFun.FileToByte(StoragePath + "/" + "card" +".jpg");
                        String cCardJepgStr = PublicFun.EncodeBase64(sJepgByte);

                        String[] go = new String[]{SignerJepgStr,"",cCardJepgStr};

                        mFile.delete();
                        cFile.delete();

                        Map<String, String> pic = getPickUpXml(go);
                        GetInfo(pic);
                    }else {
                        ToastUtils.showToast(mContext, "未拍照或图片未找到!", Toast.LENGTH_SHORT);
                    }
                } else if((CardFlag == 2)){
                    File sFile = new File(StoragePath + "/" + "ShouHuoRenCard" +".jpg");
                    File tFile = new File(StoragePath + "/" + "TiHuoRenCard" +".jpg");

                    if (mFile.exists() && sFile.exists() && tFile.exists()) {
                        byte[] SignerJepgByte = PublicFun.FileToByte(SignerjpegFile);
                        String SignerJepgStr = PublicFun.EncodeBase64(SignerJepgByte);

                        byte[] sJepgByte = PublicFun.FileToByte(StoragePath + "/" + "ShouHuoRenCard" +".jpg");
                        String sCardJepgStr = PublicFun.EncodeBase64(sJepgByte);

                        byte[] tJepgByte = PublicFun.FileToByte(StoragePath + "/" + "TiHuoRenCard" +".jpg");
                        String tCardJepgStr = PublicFun.EncodeBase64(tJepgByte);

                        String[] go = new String[]{SignerJepgStr,sCardJepgStr,tCardJepgStr};

                        mFile.delete();
                        sFile.delete();
                        tFile.delete();

                        Map<String, String> pic = getPickUpXml(go);
                        GetInfo(pic);
                    } else {
                        ToastUtils.showToast(mContext, "未拍照或图片未找到!", Toast.LENGTH_SHORT);
                    }
                }
            }
        });
        //endregion


    }
    //endregion

    //endregion

    //region 功能方法

    //region 打开编辑区

    //endregion

    //region 封装查询信息
    private Map<String,String>  getPickUpXml(String[] pic) {
        StringBuilder sb = new StringBuilder();
        Map<String, String> pa = new HashMap<>();
        sb.append("<?xml version='1.0' encoding='UTF-8'?>");
        sb.append("<GNJPickUp>");

        for (String key : PickUpMap.keySet()){
            sb.append("  <AWBInfo>");
            sb.append("    <ID>" + key + "</ID>");
            sb.append("    <Mawb>" + PickUpMap.get(key) +"</Mawb>");
            sb.append("  </AWBInfo>");
        }

        sb.append("  <SignInfo>");
        if (CardFlag == 2) {
            sb.append("    <CNEIDCard><![CDATA[" + pic[1] + "]]></CNEIDCard>");
        } else {
            sb.append("    <CNEIDCard><![CDATA[" + "" + "]]></CNEIDCard>");
        }

        sb.append("    <DLVIDCard><![CDATA[" + pic[2] +"]]></DLVIDCard>");
        sb.append("    <Sign><![CDATA[" + pic[0] +"]]></Sign>");
        sb.append("  </SignInfo>");
        sb.append("</GNJPickUp>");

        pa.put("PickUpString", sb.toString());
        pa.put("ErrString", "");
        return pa;
    }
    //endregion

    //region 句柄监听
    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == 1) {
                Integer req = (Integer) getIntent().getSerializableExtra("id");
                Intent intent = new Intent(mContext,gnjPickUpInfoActivity.class);

                if (req == AviationCommons.PickUpSignatureActivity_REQUEST) {
                    intent.putExtra("result", "true");
                    setResult(AviationCommons.PickUpSignatureActivity_RESULT,intent);
                    mAct.finish();
                } else {
                    ToastUtils.showToast(mAct,"上层请求码错误，无法回调！",Toast.LENGTH_SHORT);
                }
            } else if (msg.what == 0) {
                Integer req = (Integer) getIntent().getSerializableExtra("id");
                Intent intent = new Intent(mContext,gnjPickUpInfoActivity.class);

                if (req == AviationCommons.PickUpSignatureActivity_REQUEST) {
                    intent.putExtra("result", "false" + ErrStr);
                    setResult(AviationCommons.PickUpSignatureActivity_RESULT,intent);
                    mAct.finish();
                } else {
                    ToastUtils.showToast(mAct,"上层请求码错误，无法回调！",Toast.LENGTH_SHORT);
                }
            }


            return false;
        }
    });
    //endregion


    //region 请求数据
    private void GetInfo(Map<String, String> p) {
        Ldialog.show();

        HttpRoot.getInstance().requstAync(mContext, HttpCommons.UPDATA_GNJ_GNJCargoPickUp_NAME, HttpCommons.UPDATA_GNJ_GNJCargoPickUp_ACTION, p,
                new HttpRoot.CallBack() {
                    @Override
                    public void onSucess(Object result) {
                        Ldialog.dismiss();

                        mHandler.sendEmptyMessage(1);
                    }

                    @Override
                    public void onFailed(String message) {
                        Ldialog.dismiss();
                        ErrStr = message;
                        mHandler.sendEmptyMessage(0);
                    }

                    @Override
                    public void onError() {
                        Ldialog.dismiss();
                        ToastUtils.showToast(mContext,"数据出错", Toast.LENGTH_SHORT);
                    }
                },page);
    }
    //endregion

    //region 文本框赋值

    //endregion

    //endregion

}
