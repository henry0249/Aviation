package com.example.administrator.aviation.ui.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.aviation.R;
import com.example.administrator.aviation.model.homemessge.HomeMessage;
import com.example.administrator.aviation.model.homemessge.PrefereceHomeMessage;
import com.example.administrator.aviation.ui.base.NavBar;
import com.example.administrator.aviation.ui.fragment.HomePageFragment;
import com.example.administrator.aviation.ui.fragment.PersonFragment;
import com.example.administrator.aviation.util.AviationCommons;
import com.example.administrator.aviation.util.PreferenceUtils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.List;

import static com.example.administrator.aviation.sys.PublicFun.getSDPath;

/**
 * 所有用户首页
 * 不同的用户跳到该页面是显示是自己可选界面
 */
public class UserHomePageActivity extends FragmentActivity implements View.OnClickListener {
    private String[] permissions = {Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,

    };
    private String content[]={"拍照和录制视频权限","读写存储卡权限"};
    private StringBuilder sb_error=null;
    private StringBuilder sb_wrong=null;

    // 标题
    private NavBar navBar;

    // 底部的几个layout（例如：首页）
    private LinearLayout homePageLayout;
    private LinearLayout personLayout;

    // 底部的textView和imageView
    private TextView homePageTv;
    private TextView personTv;
    private ImageView homePageIv;
    private ImageView personIv;

    private String xml = "";

    private List<HomeMessage> list;
    private Context mContext;

    // 4个Fragment
    private HomePageFragment homePageFragment;
    private PersonFragment personFragment;

    // 定义一个标识来判断是否退出
    private static boolean isExit = false;
    Handler myHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            isExit = false;
        }
    };

    // 版本更新（版本号 提示消息 下载地址）
    private ProgressDialog pBar;
    private String version;
    private String describe;
    private String url;
    @SuppressLint("HandlerLeak")
    private Handler handler1 = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (isNeedUpdate()) {
                showUpdateDialog();
            }
//            else {
//                Toast.makeText(UserHomePageActivity.this, "当前是最新版本", Toast.LENGTH_LONG).show();
//            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        // 得到登录传递过来的xml数据（有疑问此方法执行两次）
        xml = this.getIntent().getStringExtra(AviationCommons.LOGIN_XML);
        list = PrefereceHomeMessage.pullXml(xml,this);
        this.mContext = this;
        // 获取版本更新信息
        version = PreferenceUtils.getAPPVersion(UserHomePageActivity.this);
        describe = PreferenceUtils.getAPPDescribe(UserHomePageActivity.this);
        url = PreferenceUtils.getAppUrl(UserHomePageActivity.this);
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    handler1.sendEmptyMessage(0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
        initView();
        initFragment(0);
    }

    // 按两次退出程序
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    // 主要代码实现
    private void initView() {
        // 设置标题
        NavBar navBar = new NavBar(this);
        navBar.setTitle("首页");
        navBar.hideLeft();
        navBar.hideRight();

        // 初始化textView和imageView
        homePageTv = (TextView) findViewById(R.id.home_page_tv);
        homePageIv = (ImageView) findViewById(R.id.home_page_iv);
        personTv = (TextView) findViewById(R.id.person_tv);
        personIv = (ImageView) findViewById(R.id.person_iv);

        // 初始化linearLayout
        homePageLayout = (LinearLayout) findViewById(R.id.home_page_layout);
        personLayout = (LinearLayout) findViewById(R.id.person_layout);
        homePageLayout.setOnClickListener(this);
        personLayout.setOnClickListener(this);

        // 首次进入
        homePageTv.setTextColor(Color.parseColor("#3371ae"));
        homePageIv.setImageResource(R.drawable.zhuyedianji);

        requestPermission();
    }



    //region 硬件权限申请
    private void requestPermission()
    {
        //判断Android版本是否大于23
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            ActivityCompat.requestPermissions(this, permissions, 321);
        }
    }
    //endregion

    //region 权限申请允许的处理方法
    /**
     * 注册权限申请回调
     * @param requestCode 申请码
     * @param permissions 申请的权限
     * @param grantResults 结果
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 321) {
            if (Build.VERSION.SDK_INT >= 23) {
                int index = 0;
                sb_error = new StringBuilder();
                for (int i = 0; i < permissions.length; i++) {
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        if (shouldShowRequestPermissionRationale(permissions[i])) {
                            sb_error.append(content[i]);
                            sb_error.append(" ");
                            index++;
                        }
                    }
                }
                if (index != 0) {
                    setAuthority(sb_error.toString());
                    index = 0;
                }
            }
        }
    }
    //endregion

    //region 权限申请用户取消时的处理方法的回调
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 123) {
            if (Build.VERSION.SDK_INT >= 23) {
                sb_wrong = new StringBuilder();
                int index = 0;
                for (int i = 0; i < permissions.length; i++) {
            /*if (permissions[i] == "Manifest.permission.READ_EXTERNAL_STORAGE" || permissions[i] == "Manifest.permission.ACCESS_COARSE_LOCATION")
            {continue;}*/
                    int checkper = ContextCompat.checkSelfPermission(UserHomePageActivity.this, permissions[i]);
                    {
                        if (checkper != PackageManager.PERMISSION_GRANTED){
                            sb_wrong.append(content[i]);
                            sb_wrong.append(" ");
                            index++;
                        }
                    }
                }
                if (index != 0) {
                    setAuthority(sb_wrong.toString());
                    index = 0;
                } else {
                    Toast.makeText(this, "获取所有权限成功", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
    //endregion

    //region 权限申请用户取消时的处理方法
    public void setAuthority(String content) {
        new AlertDialog.Builder(this)
                .setTitle("还有权限未设置")
                .setIcon(android.R.drawable.ic_dialog_info)
                .setMessage(content)
                .setPositiveButton("立即设置", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent();
                        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        startActivityForResult(intent, 123);
                    }
                }).setCancelable(false).show();

    }
    //endregion

    // 退出方法
    private void exit() {
        if (!isExit) {
            isExit = true;
            Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_LONG).show();

            // 利用handler延迟发送更改状态信息
            myHandler.sendEmptyMessageDelayed(0, 2000);
        } else {
            finish();
            System.exit(0);
        }
    }

    // 所有点击事件
    @Override
    public void onClick(View view) {
        // 在每次点击后将所有的底部按钮(ImageView,TextView)颜色改为灰色，然后根据点击着色
        restartBottom();
        switch (view.getId()) {
            case R.id.home_page_layout:
                navBar = new NavBar(this);
                navBar.setTitle("首页");
                navBar.hideRight();
                navBar.hideLeft();

                // 设置点击效果
                homePageTv.setTextColor(Color.parseColor("#3371ae"));
                homePageIv.setImageResource(R.drawable.zhuyedianji);

                initFragment(0);
                break;

            case R.id.person_layout:
                navBar = new NavBar(this);
                navBar.setTitle("我的");
                navBar.hideRight();
                // 暂时隐藏掉（navbar目前是关闭当前activity）
                navBar.hideLeft();

                // 设置点击效果
                personTv.setTextColor(Color.parseColor("#3371ae"));
                personIv.setImageResource(R.drawable.wodedianji);

                initFragment(3);
                break;

            default:
                break;
        }
    }

    private void initFragment(int index) {
        FragmentManager fragmentManager = getSupportFragmentManager();

        // 开启事物
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        // 隐藏所有fragment
        hideFragment(transaction);
        switch (index) {
            case 0:
                if (homePageFragment == null) {
                    homePageFragment = new HomePageFragment();
                    transaction.add(R.id.content_frame, homePageFragment);
                } else {
                    transaction.show(homePageFragment);
                }
                break;
            case 1:
                break;
            case 2:
                break;
            case 3:
                if (personFragment == null) {
                    personFragment = new PersonFragment();
                    transaction.add(R.id.content_frame, personFragment);
                } else {
                    transaction.show(personFragment);
                }
                break;
            default:
                break;
        }
        // 提交事物
        transaction.commit();

    }

    // 隐藏fragment
    private void hideFragment(FragmentTransaction transaction) {
        if (homePageFragment != null) {
            transaction.hide(homePageFragment);
        }

        if(personFragment != null) {
            transaction.hide(personFragment);
        }
    }

    // 重置底部
    private void restartBottom() {
        homePageIv.setImageResource(R.drawable.zhuye2);
        homePageTv.setTextColor(0xff666666);

        personIv.setImageResource(R.drawable.wode2);
        personTv.setTextColor(0xff666666);
    }



    // 判断是否是最新版本，如果不是，跳出对话框选择是否更新
    private void showUpdateDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(UserHomePageActivity.this);
        builder.setIcon(android.R.drawable.ic_dialog_info);
        builder.setTitle("请升级APP至版本" + version);
        builder.setMessage(describe);
        builder.setCancelable(false);

        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                    downFile(url);

                    // 调用ApkDownService进行下载
//                    Intent intent = new Intent(UpdateActivity.this, ApkDownService.class);
//                    intent.putExtra("apkUrl", info.getUrl());
//                    startService(intent);
                } else {
                    Toast.makeText(UserHomePageActivity.this, "SD卡不可用", Toast.LENGTH_LONG).show();
                }
            }
        });

        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.create().show();
    }

    private boolean isNeedUpdate() {
        // 比较新版本和旧版本值是否相等
        if (version.equals(getVersion())) {
            return false;
        } else {
            return true;
        }
    }

    // 获取当前版本号
    private String getVersion() {
        try {
            PackageManager packageManager = this.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(this.getPackageName(), 0);
            String oldVersion = packageInfo.versionName;
            return oldVersion;
        } catch (Exception e) {
            e.printStackTrace();
            return "版本号未知";
        }
    }


    void downFile(final String url) {
        // 进度条，在下载的时候实时更新进度，提高用户友好度
        pBar = new ProgressDialog(UserHomePageActivity.this);
        pBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pBar.setTitle("正在下载");
        pBar.setMessage("请稍后......");
        pBar.setProgress(0);
        pBar.show();
        new Thread() {
            @Override
            public void run() {
                super.run();
                HttpClient client = new DefaultHttpClient();
                HttpGet get = new HttpGet(url);
                HttpResponse response;
                try {
                    response = client.execute(get);
                    HttpEntity entity = response.getEntity();
                    // 获取文件大小
                    int length = (int) entity.getContentLength();

                    // 设置进度条的总长度
                    pBar.setMax(length);

                    InputStream is = entity.getContent();
                    FileOutputStream fileOutputStream = null;
                    if (is != null) {
                        File file = new File(Environment.getExternalStorageDirectory(), "app-release.apk");
                        fileOutputStream = new FileOutputStream(file);

                        //这个是缓冲区，即一次读取10个比特，我弄的小了点，
                        // 因为在本地，所以数值太大一 下就下载完了，看不出progressbar的效果。
                        byte[] buf = new byte[1024];
                        int ch = -1;
                        int process = 0;
                        while ((ch = is.read(buf)) != -1) {
                            fileOutputStream.write(buf, 0, ch);
                            process += ch;
                            // 这里就是关键的实时更新进度
                            pBar.setProgress(process);
                        }
                    }
                    assert fileOutputStream != null;
                    fileOutputStream.flush();
                    fileOutputStream.close();
                    down();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }.start();
    }

    void down() {
        handler1.post(new Runnable() {
            @Override
            public void run() {
                pBar.cancel();
                update();
            }
        });
    }

    // 安装文件，一般固定写法
    void update() {
        if (Build.VERSION.SDK_INT >= 24) {// 判读版本是否在7.0以上
            File file = new File(getSDPath(UserHomePageActivity.this), "app-release.apk");
            // 在AndroidManifest中的android:authorities值
            Uri apkUri = FileProvider.getUriForFile(UserHomePageActivity.this, "com.example.administrator.aviation.fileprovider", file);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);//添加这一句表示对目标应用临时授权该Uri所代表的文件
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
            startActivity(intent);
        } else {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setDataAndType(Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "app-release.apk")),"application/vnd.android.package-archive");
            startActivity(intent);
        }
    }

}
