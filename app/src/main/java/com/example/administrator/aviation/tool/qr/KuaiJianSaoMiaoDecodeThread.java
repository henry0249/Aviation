package com.example.administrator.aviation.tool.qr;

import android.os.Handler;
import android.os.Looper;

import com.example.administrator.aviation.ui.cgo.domestic.CaptureActivity;
import com.example.administrator.aviation.ui.cgo.gnj.KuaiJianSaoMiaoMainActivity;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.DecodeHintType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

/**
 * Created by 46296 on 2020/9/3.
 */

public class KuaiJianSaoMiaoDecodeThread extends Thread {
    public static final String BARCODE_BITMAP = "barcode_bitmap";

    public static final int BARCODE_MODE = 0X100;
    public static final int QRCODE_MODE = 0X200;
    public static final int ALL_MODE = 0X300;

    private final KuaiJianSaoMiaoMainActivity activity;
    private final Map<DecodeHintType, Object> hints;
    private Handler handler;
    private final CountDownLatch handlerInitLatch;

    public KuaiJianSaoMiaoDecodeThread(KuaiJianSaoMiaoMainActivity activity, int decodeMode) {

        this.activity = activity;
        handlerInitLatch = new CountDownLatch(1);

        hints = new EnumMap<DecodeHintType, Object>(DecodeHintType.class);

        Collection<BarcodeFormat> decodeFormats = new ArrayList<BarcodeFormat>();
        decodeFormats.addAll(EnumSet.of(BarcodeFormat.AZTEC));
        decodeFormats.addAll(EnumSet.of(BarcodeFormat.PDF_417));

        switch (decodeMode) {
            case BARCODE_MODE:
                decodeFormats.addAll(com.example.administrator.aviation.tool.qr.DecodeFormatManager.getBarCodeFormats());
                break;

            case QRCODE_MODE:
                decodeFormats.addAll(com.example.administrator.aviation.tool.qr.DecodeFormatManager.getQrCodeFormats());
                break;

            case ALL_MODE:
                decodeFormats.addAll(com.example.administrator.aviation.tool.qr.DecodeFormatManager.getBarCodeFormats());
                decodeFormats.addAll(com.example.administrator.aviation.tool.qr.DecodeFormatManager.getQrCodeFormats());
                break;

            default:
                break;
        }

        hints.put(DecodeHintType.POSSIBLE_FORMATS, decodeFormats);
    }

    public Handler getHandler() {
        try {
            handlerInitLatch.await();
        } catch (InterruptedException ie) {
            // continue?
        }
        return handler;
    }

    @Override
    public void run() {
        Looper.prepare();
        handler = new KuaiJianSaoMiaoDecodeHandler(activity, hints);
        handlerInitLatch.countDown();
        Looper.loop();
    }
}
