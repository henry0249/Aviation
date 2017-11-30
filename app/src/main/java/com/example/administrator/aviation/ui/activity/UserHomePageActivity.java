package com.example.administrator.aviation.ui.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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

/**
 * 所有用户首页
 * 不同的用户跳到该页面是显示是自己可选界面
 */
public class UserHomePageActivity extends FragmentActivity implements View.OnClickListener {
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

    }

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
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "app-release.apk")),
                "application/vnd.android.package-archive");
        startActivity(intent);
    }

}
