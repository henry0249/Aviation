package com.example.administrator.aviation.tool.qr;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.example.administrator.aviation.R;
import com.example.administrator.aviation.tool.camera.CameraManager;
import com.example.administrator.aviation.ui.cgo.domestic.CaptureActivity;
import com.example.administrator.aviation.ui.cgo.gnj.KuaiJianSaoMiaoMainActivity;
import com.google.zxing.Result;

/**
 * Created by 46296 on 2020/9/3.
 */

public class KuaiJianSaoMiaoHandler  extends Handler {
    private final KuaiJianSaoMiaoMainActivity activity;
    private final KuaiJianSaoMiaoDecodeThread decodeThread;
    private final CameraManager cameraManager;
    private State state;

    private enum State {
        PREVIEW, SUCCESS, DONE
    }

    public KuaiJianSaoMiaoHandler(KuaiJianSaoMiaoMainActivity activity, CameraManager cameraManager, int decodeMode) {
        this.activity = activity;
        decodeThread = new KuaiJianSaoMiaoDecodeThread(activity, decodeMode);
        decodeThread.start();
        state = State.SUCCESS;

        // Start ourselves capturing previews and decoding.
        this.cameraManager = cameraManager;
        cameraManager.startPreview();
        restartPreviewAndDecode();
    }

    @Override
    public void handleMessage(Message message) {
        switch (message.what) {
            case R.id.restart_preview:
                restartPreviewAndDecode();
                break;
            case R.id.decode_succeeded:
                state = State.SUCCESS;
                Bundle bundle = message.getData();

                activity.handleDecode((Result) message.obj, bundle);
                break;
            case R.id.decode_failed:
                // We're decoding as fast as possible, so when one decode fails,
                // start another.
                state = State.PREVIEW;
                cameraManager.requestPreviewFrame(decodeThread.getHandler(), R.id.decode);
                break;
            case R.id.return_scan_result:
                activity.setResult(Activity.RESULT_OK, (Intent) message.obj);
                activity.finish();
                break;
        }
    }

    public void quitSynchronously() {
        state = State.DONE;
        cameraManager.stopPreview();
        Message quit = Message.obtain(decodeThread.getHandler(), R.id.quit);
        quit.sendToTarget();
        try {
            // Wait at most half a second; should be enough time, and onPause()
            // will timeout quickly
            decodeThread.join(500L);
        } catch (InterruptedException e) {
            // continue
        }

        // Be absolutely sure we don't send any queued up messages
        removeMessages(R.id.decode_succeeded);
        removeMessages(R.id.decode_failed);
    }

    public void restartPreviewAndDecode() {
        if (state == State.SUCCESS) {
            state = State.PREVIEW;
            cameraManager.requestPreviewFrame(decodeThread.getHandler(), R.id.decode);
        }
    }
}
