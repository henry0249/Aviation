package com.example.administrator.aviation.ui.cgo.gnj;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageView;
import android.text.TextUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.aviation.R;
import com.example.administrator.aviation.http.HttpCommons;
import com.example.administrator.aviation.http.HttpRoot;
import com.example.administrator.aviation.model.gnj.gnjPickUpConverter;
import com.example.administrator.aviation.model.gnj.gnjPickUpModel;
import com.example.administrator.aviation.sys.PublicFun;
import com.example.administrator.aviation.ui.base.NavBar;
import com.example.administrator.aviation.ui.cgo.domestic.JinChengGuanKong;
import com.example.administrator.aviation.ui.dialog.LoadingDialog;
import com.example.administrator.aviation.util.AviationCommons;
import com.example.administrator.aviation.util.ToastUtils;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;

import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.R.attr.bitmap;
import static android.R.attr.key;
import static com.example.administrator.aviation.R.id.pulltorefreshview;

public class gnjPicjUpDetailsActivity extends AppCompatActivity {

    //region 自定义变量
    private Context mContext;
    private Activity mAct;
    private NavBar navBar;
    private gnjPickUpModel result;
    private final String page = "one";
    private List<gnjPickUpModel> SignModelList;
    //endregion

    //region 未预设XML控件
    private LoadingDialog Ldialog;
    //endregion

    //region 其他控件
    @BindView(R.id.gnjPicjUpDetails_Img_tihuorenSign)
    AppCompatImageView Img_tihuorenSign;
    @BindView(R.id.gnjPicjUpDetails_Img_tihuorenCard)
    AppCompatImageView Img_tihuorenCard;
    @BindView(R.id.gnjPicjUpDetails_Img_shouhuorenCard)
    AppCompatImageView Img_shouhuorenCard;
    //endregion

    //region Layout控件

    //endregion

    //region Button控件

    //endregion

    //region EditText控件

    //endregion

    //region 滚动View控件

    //endregion

    //region TextView控件
    @BindView(R.id.gnjPicjUpDetails_Txt_PKID)
    TextView Txt_PKID;
    @BindView(R.id.gnjPicjUpDetails_Txt_mawb)
    TextView Txt_mawb;
    @BindView(R.id.gnjPicjUpDetails_Txt_chgmode)
    TextView Txt_chgmode;
    @BindView(R.id.gnjPicjUpDetails_Txt_agentcode)
    TextView Txt_agentcode;
    @BindView(R.id.gnjPicjUpDetails_Txt_awbpc)
    TextView Txt_awbpc;
    @BindView(R.id.gnjPicjUpDetails_Txt_pc)
    TextView Txt_pc;
    @BindView(R.id.gnjPicjUpDetails_Txt_spcode)
    TextView Txt_spcode;
    @BindView(R.id.gnjPicjUpDetails_Txt_origin)
    TextView Txt_origin;
    @BindView(R.id.gnjPicjUpDetails_Txt_fdate)
    TextView Txt_fdate;
    @BindView(R.id.gnjPicjUpDetails_Txt_fno)
    TextView Txt_fno;
    @BindView(R.id.gnjPicjUpDetails_Txt_chargetime)
    TextView Txt_chargetime;
    @BindView(R.id.gnjPicjUpDetails_Txt_pickupflag)
    TextView Txt_pickupflag;
    @BindView(R.id.gnjPicjUpDetails_Txt_dlvtime)
    TextView Txt_dlvtime;
    @BindView(R.id.gnjPicjUpDetails_Txt_cnename)
    TextView Txt_cnename;
    @BindView(R.id.gnjPicjUpDetails_Txt_cneid)
    TextView Txt_cneid;
    @BindView(R.id.gnjPicjUpDetails_Txt_cnephone)
    TextView Txt_cnephone;
    @BindView(R.id.gnjPicjUpDetails_Txt_dlvname)
    TextView Txt_dlvname;
    @BindView(R.id.gnjPicjUpDetails_Txt_dlvid)
    TextView Txt_dlvid;
    @BindView(R.id.gnjPicjUpDetails_Txt_dlvphone)
    TextView Txt_dlvphone;
    @BindView(R.id.gnjPicjUpDetails_Txt_refid)
    TextView Txt_refid;

    //endregion

    //region ImgView控件

    //endregion

    //region 初始化

    //region 入口函数
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gnj_picj_up_details);
        mContext = gnjPicjUpDetailsActivity.this;
        mAct = (Activity) mContext;
        ButterKnife.bind(this);
        init();
    }
    //endregion

    //region 设置初始化
    private void init() {
        navBar = new NavBar(this);
        navBar.setTitle("货物详情");

        SignModelList = new ArrayList<>();
        Ldialog = new LoadingDialog(mContext);
        result = (gnjPickUpModel) getIntent().getSerializableExtra("Info");
        setTextViewEmpty();
        setTextViewValue();
        setSignPic();
    }
    //endregion

    //region 请求图片
    private void setSignPic(){
        if (result != null && !TextUtils.isEmpty(result.getID())) {
            HashMap<String, String> quest = new HashMap<>();
            quest.put("ID", result.getID().trim());
            quest.put("ErrString", "");
            GetInfo(quest);
        } else {
            ToastUtils.showToast(mContext,"ID参数传递错误!",Toast.LENGTH_SHORT);
        }
    }
    //endregion

    //region 置空
    private void setTextViewEmpty(){
        Txt_PKID.setText("");
        Txt_mawb.setText("");
        Txt_chgmode.setText("");
        Txt_agentcode.setText("");
        Txt_awbpc.setText("");
        Txt_pc.setText("");
        Txt_spcode.setText("");
        Txt_origin.setText("");
        Txt_fdate.setText("");
        Txt_fno.setText("");
        Txt_chargetime.setText("");
        Txt_pickupflag.setText("");
        Txt_dlvtime.setText("");
        Txt_cnename.setText("");
        Txt_cneid.setText("");
        Txt_cnephone.setText("");
        Txt_dlvname.setText("");
        Txt_dlvid.setText("");
        Txt_dlvphone.setText("");
        Txt_refid.setText("");
    }
    //endregion

    //region 绑定数据
    private void setTextViewValue(){
        if (result != null && !TextUtils.isEmpty(result.getPKID())) {
            Txt_PKID.setText(result.getPKID());
            Txt_mawb.setText(result.getMawb());
            Txt_chgmode.setText(result.getCHGMode());
            Txt_agentcode.setText(result.getAgentCode());
            Txt_awbpc.setText(result.getAWBPC());
            Txt_pc.setText(result.getPC());
            Txt_spcode.setText(result.getSpCode());
            Txt_origin.setText(result.getOrigin());
            Txt_fdate.setText(result.getFDate());
            Txt_fno.setText(result.getFno());
            Txt_chargetime.setText(result.getChargeTime());
            Txt_pickupflag.setText(result.getPickFlag());
            Txt_dlvtime.setText(result.getDLVTime());
            Txt_cnename.setText(result.getCNEName());
            Txt_cneid.setText(result.getCNEID());
            Txt_cnephone.setText(result.getCNEPhone());
            Txt_dlvname.setText(result.getDLVName());
            Txt_dlvid.setText(result.getDLVID());
            Txt_dlvphone.setText(result.getDLVPhone());
            Txt_refid.setText(result.getREFID());
        } else {
            ToastUtils.showToast(mContext,"参数传递错误!",Toast.LENGTH_SHORT);
        }
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
            if (msg.what == 1) {
                if (SignModelList.size() > 0) {
                    if (!TextUtils.isEmpty(SignModelList.get(0).getSign())) {
                        Img_tihuorenSign.setImageBitmap(PublicFun.DecodeBase64ToPic(SignModelList.get(0).getSign()));
                    }

                    if (!TextUtils.isEmpty(SignModelList.get(0).getDLVIDCard())) {
                        Img_tihuorenCard.setImageBitmap(PublicFun.DecodeBase64ToPic(SignModelList.get(0).getDLVIDCard()));
                    }

                    if (!TextUtils.isEmpty(SignModelList.get(0).getCNEIDCard())) {
                        Img_shouhuorenCard.setImageBitmap(PublicFun.DecodeBase64ToPic(SignModelList.get(0).getCNEIDCard()));
                    }
                }
            }
            return false;
        }
    });
    //endregion

    //region 请求数据
    private void GetInfo(Map<String, String> p) {
        Ldialog.show();

        HttpRoot.getInstance().requstAync(mContext, HttpCommons.GET_GNJ_GetGNJPickUpSign_NAME, HttpCommons.GET_GNJ_GetGNJPickUpSign_ACTION, p,
                new HttpRoot.CallBack() {
                    @Override
                    public void onSucess(Object result) {
                        SoapObject object = (SoapObject) result;
                        String Exp_ULDLoading = object.getProperty(0).toString();
                        SignModelList = gnjPickUpConverter.gnjSignXMLtoMdoel(Exp_ULDLoading);

                        Ldialog.dismiss();
                        mHandler.sendEmptyMessage(1);
                    }

                    @Override
                    public void onFailed(String message) {
                        Ldialog.dismiss();

                        ToastUtils.showToast(mContext,message,Toast.LENGTH_SHORT);
                    }

                    @Override
                    public void onError() {
                        Ldialog.dismiss();
                        ToastUtils.showToast(mContext,"图片数据获取出错", Toast.LENGTH_SHORT);
                    }
                },page);
    }

    //endregion

    //region 文本框赋值

    //endregion

    //endregion

}
