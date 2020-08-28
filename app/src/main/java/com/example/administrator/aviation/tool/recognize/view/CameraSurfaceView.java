package com.example.administrator.aviation.tool.recognize.view;

import android.content.Context;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

import com.example.administrator.aviation.tool.recognize.control.CameraControlManager;
import com.example.administrator.aviation.tool.recognize.utils.CameraPara;
import com.example.administrator.aviation.tool.recognize.utils.DisplayUtils;

/**
 * 预览界面
 * @author michael
 *
 */
public class CameraSurfaceView extends SurfaceView implements Callback{

	private CameraControlManager cameraControlManager;
	private CameraPara cameraPara;
	float ra;
	Point screenResolution;

	public CameraSurfaceView(Context context) {
		super(context);
		ra = DisplayUtils.getScreenRate(context);
		screenResolution = DisplayUtils.getScreenMetrics(context);
		getHolder().setFormat(PixelFormat.TRANSPARENT);//设置透明
		getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);//设置surface是个推送类型的
		getHolder().addCallback(this);
		cameraControlManager=CameraControlManager.getInstance();
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		cameraControlManager.doOpenCamera();
		cameraPara=new CameraPara(cameraControlManager.getCamera());
		cameraPara.setRates(ra);
		cameraPara.setScreenResolution(screenResolution);
		cameraPara.setValues();
		cameraControlManager.setParameters(cameraPara.getParameters());
		cameraControlManager.setPreviewDisplay(holder);
		cameraControlManager.startPreView();
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		cameraControlManager.doStopCamera();
	}



}
