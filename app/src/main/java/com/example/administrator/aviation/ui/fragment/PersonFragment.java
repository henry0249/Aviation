package com.example.administrator.aviation.ui.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.administrator.aviation.LoginActivity;
import com.example.administrator.aviation.R;
import com.example.administrator.aviation.ui.activity.SettingPasswordActivity;
import com.example.administrator.aviation.ui.activity.VersionUpdateActivity;
import com.example.administrator.aviation.ui.base.NavBar;
import com.example.administrator.aviation.util.PreferenceUtils;

/**
 * 我的fragment(这里主要包括用户的一些设置)
 */

public class PersonFragment extends Fragment implements View.OnClickListener{
    private View view;

    // 设置锁屏
    private LinearLayout seetingPassLayout;

    // 版本更新
    private LinearLayout versionUpdateLayout;

    // 退出登录
    private LinearLayout exitLoginLayout;

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
        seetingPassLayout = (LinearLayout) view.findViewById(R.id.setting_pass_layout);
        seetingPassLayout.setOnClickListener(this);

        versionUpdateLayout = (LinearLayout) view.findViewById(R.id.version_update_layout);
        versionUpdateLayout.setOnClickListener(this);

        exitLoginLayout = (LinearLayout) view.findViewById(R.id.exit_login_layout);
        exitLoginLayout.setOnClickListener(this);
    }

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

            // 版本更新
            case R.id.version_update_layout:
                Intent intentUpdate = new Intent(getActivity(), VersionUpdateActivity.class);
                startActivity(intentUpdate);
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
}
