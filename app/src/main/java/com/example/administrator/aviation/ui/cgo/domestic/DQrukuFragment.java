package com.example.administrator.aviation.ui.cgo.domestic;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.administrator.aviation.R;
import com.example.administrator.aviation.ui.base.NavBar;
import com.qmuiteam.qmui.widget.QMUIProgressBar;

import java.lang.ref.WeakReference;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.drm.DrmStore.Playback.STOP;
import static android.icu.text.RelativeDateTimeFormatter.Direction.NEXT;
import android.os.Message;


public class DQrukuFragment extends Fragment {

    //region 自定义变量
    private View view;
    private Context mContext;
    private Activity mAct;
    private NavBar navBar;

    int count;
    private ProgressHandler myHandler = new ProgressHandler();
    protected static final int STOP = 0x10000;
    protected static final int NEXT = 0x10001;
    //endregion

    //region 未预设XML控件

    //endregion

    //region 其他控件
    @BindView(R.id.DQruku_ZaiLiangPgBar)
    QMUIProgressBar mRectProgressBar;
    //endregion

    //region Layout控件

    //endregion

    //region Button控件
    @BindView(R.id.btn_DQruku_KaiShi)
    Button btnKaiShi;
    //endregion

    //region EditText控件

    //endregion

    //region 滚动View控件

    //endregion

    //region TextView控件

    //endregion

    //region 初始化

    //region 入口函数
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_dqruku, container, false);
        mContext = getContext();
        mAct = (Activity) mContext;
        ButterKnife.bind(this, view);
        init();
        return view;
    }
    //endregion

    //region 设置初始化
    public void init() {
        myHandler.setProgressBar(mRectProgressBar);
        setListener();

    }

    public DQrukuFragment() {
        // Required empty public constructor
    }
    //endregion

    //region 输入框置空

    //endregion

    //endregion

    //region 控件事件

    //region 页面上所有的点击事件
    public void setListener() {
        mRectProgressBar.setQMUIProgressBarTextGenerator(new QMUIProgressBar.QMUIProgressBarTextGenerator() {
            @Override
            public String generateText(QMUIProgressBar progressBar, int value, int maxValue) {
                return value + "/" + maxValue;
            }
        });

        btnKaiShi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count = 0;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i <= 20; i++) {
                            try {
                                count = (i + 1) * 5;
                                if (i == 20) {
                                    Message msg = new Message();
                                    msg.what = STOP;
                                    myHandler.sendMessage(msg);
                                } else {
                                    Message msg = new Message();
                                    msg.what = NEXT;
                                    msg.arg1 = count;
                                    myHandler.sendMessage(msg);
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }).start();
            }
        });
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

    //region 文本框赋值

    //endregion

    //region 内部类
    private static class ProgressHandler extends Handler {
        private WeakReference<QMUIProgressBar> weakRectProgressBar;

        void setProgressBar(QMUIProgressBar rectProgressBar) {
            weakRectProgressBar = new WeakReference<>(rectProgressBar);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case STOP:
                    break;
                case NEXT:
                    if (!Thread.currentThread().isInterrupted()) {
                        if (weakRectProgressBar.get() != null) {
                            weakRectProgressBar.get().setProgress(msg.arg1);
                        }
                    }
            }
        }
    }
    //endregion

    //endregion
}
