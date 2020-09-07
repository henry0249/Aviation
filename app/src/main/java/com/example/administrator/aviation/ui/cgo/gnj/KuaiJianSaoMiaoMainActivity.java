package com.example.administrator.aviation.ui.cgo.gnj;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.aviation.R;
import com.example.administrator.aviation.http.HttpCommons;
import com.example.administrator.aviation.http.HttpRoot;
import com.example.administrator.aviation.model.gnj.gnjPickUpConverter;
import com.example.administrator.aviation.sys.PublicFun;
import com.example.administrator.aviation.tool.DateUtils;
import com.example.administrator.aviation.tool.camera.CameraManager;
import com.example.administrator.aviation.tool.qr.BeepManager;
import com.example.administrator.aviation.tool.qr.CaptureActivityHandler;
import com.example.administrator.aviation.tool.qr.DecodeThread;
import com.example.administrator.aviation.tool.qr.InactivityTimer;
import com.example.administrator.aviation.tool.qr.KuaiJianSaoMiaoHandler;
import com.example.administrator.aviation.ui.base.NavBar;
import com.example.administrator.aviation.ui.cgo.domestic.CaptureActivity;
import com.example.administrator.aviation.ui.dialog.LoadingDialog;
import com.example.administrator.aviation.util.AviationCommons;
import com.example.administrator.aviation.util.ToastUtils;
import com.example.administrator.aviation.view.AutofitTextView;
import com.google.zxing.Result;

import org.ksoap2.serialization.SoapObject;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;

public class KuaiJianSaoMiaoMainActivity extends AppCompatActivity implements SurfaceHolder.Callback {
    //region 自定义变量
    private Context mContext;
    private Activity mAct;
    private NavBar navBar;
    private List<View> views;
    private final String TAG = KuaiJianSaoMiaoMainActivity.class.getSimpleName();
    private Rect mCropRect = null;
    private String Numbers = "";
    private int CountFlag = 0;
    private long lastClickTime;
    private final String page = "other";
    private final String blank = "  ";
    //endregion

    //region 未预设XML控件
    private CameraManager cameraManager;
    private KuaiJianSaoMiaoHandler handler;
    private InactivityTimer inactivityTimer;
    private BeepManager beepManager;
    private SurfaceView scanPreview = null;
    private RelativeLayout scanContainer;
    private RelativeLayout scanCropView;
    private ImageView scanLine;
    private LoadingDialog Ldialog;
    //endregion

    //region 其他控件
    @BindView(R.id.KuaiJianSaoMiao_Img_chaxun)
    ImageView Img_chaxun;
    @BindView(R.id.KuaiJianSaoMiao_Img_QingKong)
    ImageView Img_QingKong;
    @BindView(R.id.KuaiJianSaoMiao_Scroll)
    ScrollView Scrll;
    //endregion

    //region Layout控件

    //endregion

    //region Button控件

    //endregion

    //region EditText控件
    @BindView(R.id.KuaiJianSaoMiao_EdTxt_BaoHao)
    EditText EdTxt_BaoHao;
    //endregion

    //region 滚动View控件

    //endregion

    //region TextView控件
    @BindView(R.id.KuaiJianSaoMiao_Txt_BaoHao)
    TextView Txt_BaoHao;
    //endregion

    //region ImgView控件

    //endregion

    //region 初始化
    public Handler getHandler() {
        return handler;
    }

    public CameraManager getCameraManager() {
        return cameraManager;
    }

    private boolean isHasSurface = false;

    private String mResult;

    //region 入口函数
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_kuai_jian_sao_miao_main);

        mContext = KuaiJianSaoMiaoMainActivity.this;
        mAct = (Activity) mContext;
        ButterKnife.bind(this);
        init();
    }
    //endregion

    //region 设置初始化
    private void init() {
        mResult = "";

        navBar = new NavBar(this);
        navBar.setTitle("相机模式");

        views = new ArrayList<View>();
        views.add(EdTxt_BaoHao);

        scanPreview = (SurfaceView) findViewById(R.id.KuaiJianSaoMiao_preview);
        scanContainer = (RelativeLayout) findViewById(R.id.KuaiJianSaoMiao_container);
        scanCropView = (RelativeLayout) findViewById(R.id.KuaiJianSaoMiao_crop_view);
        scanLine = (ImageView) findViewById(R.id.KuaiJianSaoMiao_scan_line);

        inactivityTimer = new InactivityTimer(this);
        beepManager = new BeepManager(this);

        TranslateAnimation animation = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT,
                0.9f);
        animation.setDuration(4500);
        animation.setRepeatCount(-1);
        animation.setRepeatMode(Animation.RESTART);
        scanLine.startAnimation(animation);

        Ldialog = new LoadingDialog(mContext);

        setTextEmpty();
        setListener();
    }
    //endregion

    private void SaoMaQiangM(){

    }

    //region 输入框置空
    private void setTextEmpty() {
        EdTxt_BaoHao.setText("");
    }
    //endregion

    //region 类的Intent

    //endregion

    //endregion

    //region 控件事件

    //region 页面上所有控件事件
    private void setListener() {
        Img_chaxun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(EdTxt_BaoHao.getText().toString().trim())) {
                    String re = EdTxt_BaoHao.getText().toString().trim();
                    EdTxt_BaoHao.setText("");
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
                setTextEmpty();
            }
        });
    }
    //endregion

    //endregion

    //region 功能方法

    @Override
    protected void onResume() {
        super.onResume();

        // CameraManager must be initialized here, not in onCreate(). This is
        // necessary because we don't
        // want to open the camera driver and measure the screen size if we're
        // going to show the help on
        // first launch. That led to bugs where the scanning rectangle was the
        // wrong size and partially
        // off screen.
        cameraManager = new CameraManager(getApplication());

        handler = null;

        if (isHasSurface) {
            // The activity was paused but not stopped, so the surface still
            // exists. Therefore
            // surfaceCreated() won't be called, so init the camera here.
            initCamera(scanPreview.getHolder());
        } else {
            // Install the callback and wait for surfaceCreated() to init the
            // camera.
            scanPreview.getHolder().addCallback(this);
        }

        inactivityTimer.onResume();
    }

    @Override
    protected void onPause() {
        if (handler != null) {
            handler.quitSynchronously();
            handler = null;
        }
        inactivityTimer.onPause();
        beepManager.close();
        cameraManager.closeDriver();
        if (!isHasSurface) {
            scanPreview.getHolder().removeCallback(this);
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        inactivityTimer.shutdown();
        System.gc();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (holder == null) {
            Log.e(TAG, "*** WARNING *** surfaceCreated() gave us a null surface!");
        }
        if (!isHasSurface) {
            isHasSurface = true;
            initCamera(holder);
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        isHasSurface = false;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    //region 扫描有结果时，返回结果
    public void handleDecode(Result rawResult, Bundle bundle) {
        if (Math.abs(lastClickTime - System.currentTimeMillis()) > 1000) {
            inactivityTimer.onActivity();
            beepManager.playBeepSoundAndVibrate();

            mResult = rawResult.getText();


            EdTxt_BaoHao.setText(mResult);
            EdTxt_BaoHao.setSelection(EdTxt_BaoHao.getText().length());

            if (!TextUtils.isEmpty(mResult)) {
                HashMap<String, String> go = new HashMap<String, String>();
                go.put("PackageNO", mResult);
                go.put("ErrString", "");
                GetInfo(go);
            }


            if (handler != null) {
                this.restartPreviewAfterDelay(3000);
            }

        } else {
            lastClickTime = System.currentTimeMillis();
        }
    }
    //endregion

    //region 初始化相机
    private void initCamera(SurfaceHolder surfaceHolder) {

        if (surfaceHolder == null) {
            throw new IllegalStateException("No SurfaceHolder provided");
        }
        if (cameraManager.isOpen()) {
            Log.w(TAG, "initCamera() while already open -- late SurfaceView callback?");
            return;
        }
        try {
            cameraManager.openDriver(surfaceHolder);
            // Creating the handler starts the preview, which can also throw a
            // RuntimeException.
            if (handler == null) {
                handler = new KuaiJianSaoMiaoHandler(this, cameraManager, DecodeThread.ALL_MODE);
            }

            initCrop();
        } catch (IOException ioe) {
            Log.w(TAG, ioe);
            displayFrameworkBugMessageAndExit();
        } catch (RuntimeException e) {
            // Barcode Scanner has seen crashes in the wild of this variety:
            // java.?lang.?RuntimeException: Fail to connect to camera service
            Log.w(TAG, "Unexpected error initializing camera", e);
            displayFrameworkBugMessageAndExit();
        }
    }
    //endregion

    private void displayFrameworkBugMessageAndExit() {
        // camera error
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.app_name));
        builder.setMessage("相机打开出错，请稍后重试");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }

        });
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {
                finish();
            }
        });
        builder.show();
    }

    public void restartPreviewAfterDelay(long delayMS) {
        if (handler != null) {
            handler.sendEmptyMessageDelayed(R.id.restart_preview, delayMS);
        }
    }

    public Rect getCropRect() {
        return mCropRect;
    }

    //region 初始化截取的矩形区域
    private void initCrop() {
        int cameraWidth = cameraManager.getCameraResolution().y;
        int cameraHeight = cameraManager.getCameraResolution().x;

        /** 获取布局中扫描框的位置信息 */
        int[] location = new int[2];
        scanCropView.getLocationInWindow(location);

        int cropLeft = location[0];
        int cropTop = location[1] - getStatusBarHeight();

        int cropWidth = scanCropView.getWidth();
        int cropHeight = scanCropView.getHeight();

        /** 获取布局容器的宽高 */
        int containerWidth = scanContainer.getWidth();
        int containerHeight = scanContainer.getHeight();

        /** 计算最终截取的矩形的左上角顶点x坐标 */
        int x = cropLeft * cameraWidth / containerWidth;
        /** 计算最终截取的矩形的左上角顶点y坐标 */
        int y = cropTop * cameraHeight / containerHeight;

        /** 计算最终截取的矩形的宽度 */
        int width = cropWidth * cameraWidth / containerWidth;
        /** 计算最终截取的矩形的高度 */
        int height = cropHeight * cameraHeight / containerHeight;

        /** 生成最终的截取的矩形 */
        mCropRect = new Rect(x, y, width + x, height + y);
    }
    //endregion

    //region 获得结果区域的高度
    private int getStatusBarHeight() {
        try {
            Class<?> c = Class.forName("com.android.internal.R$dimen");
            Object obj = c.newInstance();
            Field field = c.getField("status_bar_height");
            int x = Integer.parseInt(field.get(obj).toString());
            return getResources().getDimensionPixelSize(x);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
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

    //region 句柄监听
    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {

            return false;
        }
    });
    //endregion

    //endregion
}