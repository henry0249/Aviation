package com.example.administrator.aviation.ui.cgo.gnj;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.ReplacementTransformationMethod;
import android.util.ArrayMap;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.administrator.aviation.R;
import com.example.administrator.aviation.model.gnj.gnjPickUpConverter;
import com.example.administrator.aviation.sys.PublicFun;
import com.example.administrator.aviation.tool.DateUtils;
import com.example.administrator.aviation.tool.TanChuPaiXu.DragSortDialog;
import com.example.administrator.aviation.ui.base.NavBar;
import com.example.administrator.aviation.ui.cgo.domestic.ActLiHuoXinZenPinBan;
import com.example.administrator.aviation.ui.cgo.domestic.JinChengGuanKong;
import com.example.administrator.aviation.ui.dialog.LoadingDialog;
import com.example.administrator.aviation.util.AviationCommons;
import com.example.administrator.aviation.util.PreferenceUtils;
import com.example.administrator.aviation.util.ToastUtils;
import com.example.administrator.aviation.view.AutofitTextView;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.administrator.aviation.R.id.pulltorefreshview;

public class gnjPickUpActivity extends AppCompatActivity {

  //region 自定义变量
  private Context mContext;
  private Activity mAct;
  private NavBar navBar;
    private List<View> views;
  //endregion

  //region 未预设XML控件

  //endregion

    //region 其他控件
  //endregion

  //region Layout控件
  @BindView(R.id.gnjPickUp_Btn_chaxun)
  Button Btn_chaxun;
  @BindView(R.id.gnjPickUpInfo_Btn_qingkong)
  Button Btn_qingkong;

  //endregion

  //region Button控件

  //endregion

    //region EditText控件
    @BindView(R.id.gnjPickUp_EdTxt_yundanhao)
    EditText EdTxt_yundanhao;
    @BindView(R.id.gnjPickUp_EdTxt_ShenFenZheng)
    EditText EdTxt_ShenFenZheng;
    @BindView(R.id.gnjPickUp_EdTxt_TiQuBiaoShi)
    EditText EdTxt_TiQuBiaoShi;
    @BindView(R.id.gnjPickUp_EdTxt_DaiLiRen)
    EditText EdTxt_DaiLiRen;
    @BindView(R.id.gnjPickUp_EdTxt_riqi)
    EditText EdTxt_riqi;
    @BindView(R.id. gnjPickUp_EdTxt_TiQuRiQi)
    EditText EdTxt_TiQuRiQi;
  //endregion

  //region 滚动View控件

  //endregion

    //region TextView控件
    @BindView(R.id.gnjPickUp_Txt_TimeTxt)
    AutofitTextView Txt_TimeTxt;
  //endregion

  //region ImgView控件

  //endregion

  //region 初始化

  //region 入口函数
  @Override
  protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_gnj_pick_up);

      mContext = gnjPickUpActivity.this;
      mAct = (Activity) mContext;
      ButterKnife.bind(this);
      init();
  }
  //endregion

  //region 设置初始化
  private void init() {
      navBar = new NavBar(this);
      navBar.setTitle("搜索条件");

      views = new ArrayList<View>();
      views.add(EdTxt_yundanhao);
      views.add(EdTxt_ShenFenZheng);
      views.add(EdTxt_DaiLiRen);

      EdTxt_TiQuBiaoShi.setFocusable(false);//不可编辑
      EdTxt_TiQuBiaoShi.setKeyListener(null);//不可粘贴，长按不会弹出粘贴框

      EdTxt_riqi.setFocusable(false);//不可编辑
      EdTxt_riqi.setKeyListener(null);//不可粘贴，长按不会弹出粘贴框;

      EdTxt_TiQuRiQi.setFocusable(false);
      EdTxt_TiQuRiQi.setKeyListener(null);

      setTextEmpty();
      setListener();
  }
  //endregion

  // region 输入框置空
  private void setTextEmpty(){
      EdTxt_yundanhao.setText("");
      EdTxt_ShenFenZheng.setText("");
      EdTxt_DaiLiRen.setText("");
      EdTxt_riqi.setText(PublicFun.getDateStr("yyyy-MM-dd"));
      EdTxt_TiQuRiQi.setText("");
      String flag = PreferenceUtils.getGnjPickUpFlag(mContext);
      if (TextUtils.isEmpty(flag)) {
          EdTxt_TiQuBiaoShi.setText("待出库");
      } else {
          EdTxt_TiQuBiaoShi.setText(flag);
      }

  }
  //endregion

  //region 类的Intent

  //endregion

  //endregion

  //region 控件事件

  //region 页面上所有的事件
  private void setListener() {
      //region 查询按钮点击
      Btn_chaxun.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              String[] quest = new String[]{"","","","","",""};
              String yundanhao = EdTxt_yundanhao.getText().toString().trim();
              String tiqubiaoshi = gnjPickUpConverter.SwitchPickUpFlag(EdTxt_TiQuBiaoShi.getText().toString().trim());
              String shenfenzheng = EdTxt_ShenFenZheng.getText().toString().trim().toUpperCase();
              String daliren  =EdTxt_DaiLiRen.getText().toString().trim().toUpperCase();
              String JiaoFeiRiQi = EdTxt_riqi.getText().toString().trim();
              String TiQuRiQi = EdTxt_TiQuRiQi.getText().toString().trim();

              if (TextUtils.isEmpty(yundanhao) && TextUtils.isEmpty(tiqubiaoshi) && TextUtils.isEmpty(shenfenzheng) && TextUtils.isEmpty(daliren) && TextUtils.isEmpty(JiaoFeiRiQi) && TextUtils.isEmpty(TiQuRiQi)) {
                  ToastUtils.showToast(mContext, "请填写查询条件！", Toast.LENGTH_SHORT);
              } else {
                  if (!TextUtils.isEmpty(yundanhao)) {
                      quest[0] = yundanhao;
                  }

                  if (!TextUtils.isEmpty(tiqubiaoshi)) {
                      quest[1] = tiqubiaoshi;
                  }

                  if (!TextUtils.isEmpty(shenfenzheng)) {
                      quest[2] =  shenfenzheng;
                  }

                  if (!TextUtils.isEmpty(daliren)) {
                      quest[3] = daliren;
                  }

                  if (!TextUtils.isEmpty(JiaoFeiRiQi)) {
                      quest[4] = JiaoFeiRiQi;
                  } else if (!TextUtils.isEmpty(TiQuRiQi)) {
                      quest[5] = TiQuRiQi;
                  }

                  PreferenceUtils.saveGnjPickUpFlag(mContext,EdTxt_TiQuBiaoShi.getText().toString().trim());

                  Integer req = (Integer) getIntent().getSerializableExtra("id");
                  Intent intent = new Intent(mContext,gnjPickUpInfoActivity.class);

                  if (req == AviationCommons.PickUpSearchActivity_REQUEST) {
                      intent.putExtra("result", quest);
                      setResult(AviationCommons.PickUpSearchActivity_RESULT,intent);
                      mAct.finish();
                  } else {
                      ToastUtils.showToast(mAct,"上层请求码错误，无法回调！",Toast.LENGTH_SHORT);
                  }
              }
          }
      });
      //endregion

      //region 清空按钮点击
      Btn_qingkong.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              setTextEmpty();
          }
      });
      //endregion

      //region 代理人代码自动变大写
      EdTxt_DaiLiRen.setTransformationMethod(new ReplacementTransformationMethod() {
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

      EdTxt_TiQuBiaoShi.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              PublicFun.KeyBoardHideTwo(mAct, views);

              final String[] items = new String[]{"待出库","出库中","待提取","已提取"};
              new QMUIDialog.CheckableDialogBuilder(mAct)
                      .addItems(items, new DialogInterface.OnClickListener() {
                          @Override
                          public void onClick(DialogInterface dialog, int which) {
                              EdTxt_TiQuBiaoShi.setText(items[which]);
                              EdTxt_TiQuBiaoShi.setSelection(EdTxt_TiQuBiaoShi.getText().length());
                              dialog.dismiss();
                          }
                      })
                      .show();
          }
      });

      //region 日期选择
      EdTxt_riqi.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              PublicFun.KeyBoardHideTwo(mAct, views);
              showDatePickDlg(0);
          }
      });
      //endregion

      //region 日期选择
      EdTxt_TiQuRiQi.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              PublicFun.KeyBoardHideTwo(mAct, views);
              showDatePickDlg(1);
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

  //endregion

  //region 请求数据

  //endregion

    //region 显示时间选择控件
    private void showDatePickDlg(final int flag) {
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
                if (flag == 0) {
                    EdTxt_TiQuRiQi.setText("");
                    EdTxt_riqi.setText(formatter.format(DateUtils.convertFromStrYMD(ymd)));
                    EdTxt_riqi.setSelection(EdTxt_riqi.getText().length());
                } else if (flag == 1) {
                    EdTxt_riqi.setText("");
                    EdTxt_TiQuRiQi.setText(formatter.format(DateUtils.convertFromStrYMD(ymd)));
                    EdTxt_TiQuRiQi.setSelection(EdTxt_TiQuRiQi.getText().length());
                }
            }
        });
        dlg.show();
    }
    //endregion

  //endregion
}
