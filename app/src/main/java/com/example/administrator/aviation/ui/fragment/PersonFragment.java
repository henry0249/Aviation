package com.example.administrator.aviation.ui.fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.administrator.aviation.LoginActivity;
import com.example.administrator.aviation.R;
import com.example.administrator.aviation.ui.activity.ChangePassActivity;
import com.example.administrator.aviation.ui.activity.SettingPasswordActivity;
import com.example.administrator.aviation.ui.base.NavBar;
import com.example.administrator.aviation.util.PreferenceUtils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

/**
 * 我的fragment(这里主要包括用户的一些设置)
 */

public class PersonFragment extends Fragment implements View.OnClickListener{
    private View view;

    // 设置锁屏
    private RelativeLayout seetingPassLayout;

    // 修改密码
    private RelativeLayout changePassLayout;

    // 版本更新
    private RelativeLayout versionUpdateLayout;

    // 退出登录
    private RelativeLayout exitLoginLayout;

    // 版本更新（版本号 提示消息 下载地址）
    private ProgressDialog pBar;
    private String version;
    private String describe;
    private String url;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.person_fragment, container, false);
        initView();
        return view;
    }

    private void initView() {
        NavBar navBar = new NavBar(getActivity());
        navBar.setTitle("我的");
        navBar.hideRight();
        navBar.hideLeft();
        seetingPassLayout = (RelativeLayout) view.findViewById(R.id.setting_pass_layout);
        seetingPassLayout.setOnClickListener(this);

        changePassLayout = (RelativeLayout) view.findViewById(R.id.change_pass_layout);
        changePassLayout.setOnClickListener(this);

        versionUpdateLayout = (RelativeLayout) view.findViewById(R.id.version_update_layout);
        versionUpdateLayout.setOnClickListener(this);

        exitLoginLayout = (RelativeLayout) view.findViewById(R.id.exit_login_layout);
        exitLoginLayout.setOnClickListener(this);

        // 获取版本更新信息
        version = PreferenceUtils.getAPPVersion(getActivity());
        describe = PreferenceUtils.getAPPDescribe(getActivity());
        url = PreferenceUtils.getAppUrl(getActivity());

    }

    @SuppressLint("HandlerLeak")
    private Handler handler1 = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (isNeedUpdate()) {
                showUpdateDialog();
            } else {
                Toast.makeText(getActivity(), "当前是最新版本", Toast.LENGTH_LONG).show();
            }
        }
    };

    // 所有点击事件
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            // 设置锁屏
            case R.id.setting_pass_layout:
                Intent intent = new Intent(getActivity(), SettingPasswordActivity.class);
                intent.putExtra(SettingPasswordActivity.ARG_TYPE,SettingPasswordActivity.TYPE_SETTING);
                startActivity(intent);
                break;

            // 修改密码
            case R.id.change_pass_layout:
                Intent intentChangePass = new Intent(getActivity(), ChangePassActivity.class);
                startActivity(intentChangePass);
//                getActivity().finish();
                break;

            // 版本更新
            case R.id.version_update_layout:
//                Intent intentUpdate = new Intent(getActivity(), VersionUpdateActivity.class);
//                startActivity(intentUpdate);
                Toast.makeText(getActivity(), "正在检查版本更新", Toast.LENGTH_LONG).show();
                // 自动检查有没有新版本，如果有新版本就提示更新
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
                break;

            // 退出登录
            case R.id.exit_login_layout:
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("退出登录")
                        .setMessage("确定退出登录吗?")
                        .setNegativeButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intentReset = new Intent(getActivity(), LoginActivity.class);
                                PreferenceUtils.clearShare(getActivity());
                                startActivity(intentReset);
                                getActivity().finish();
                            }
                        })
                        .setPositiveButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                builder.create().show();
                break;

            default:
                break;
        }
    }

    // 判断是否是最新版本，如果不是，跳出对话框选择是否更新
    private void showUpdateDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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
                    Toast.makeText(getActivity(), "SD卡不可用", Toast.LENGTH_LONG).show();
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
            PackageManager packageManager = getActivity().getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(getActivity().getPackageName(), 0);
            String oldVersion = packageInfo.versionName;
            return oldVersion;
        } catch (Exception e) {
            e.printStackTrace();
            return "版本号未知";
        }
    }


    void downFile(final String url) {
        // 进度条，在下载的时候实时更新进度，提高用户友好度
        pBar = new ProgressDialog(getActivity());
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
