package com.example.administrator.aviation.tool.recognize.control;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.util.Log;
import android.view.SurfaceHolder;

import java.io.IOException;

import com.example.administrator.aviation.tool.recognize.utils.DisplayUtils;
import com.example.administrator.aviation.tool.recognize.utils.FileUtil;

import static android.R.attr.y;

/**
 * 摄像头控制管理类
 * （打开，开启预览）
 * @author michael
 *
 */
public class CameraControlManager {

	/**摄像头对象*/
	private Camera mCamera;
    private String card = "card";
    private String num = "number";
    private String ShouHuoRenCard = "ShouHuoRenCard";
    private String TiHuoRenCard = "TiHuoRenCard";


	private static CameraControlManager cameraControlManager;
	/**是否出于预览状态*/
	private boolean isPreviewing;

	private CameraControlManager(){

	}

	public static CameraControlManager getInstance(){
		if(cameraControlManager==null){
			synchronized (CameraControlManager.class) {
				if(cameraControlManager==null){
					cameraControlManager=new CameraControlManager();
				}
			}
		}
		return cameraControlManager;
	}

	/**
	 * 开启摄像头
	 */
	public void doOpenCamera(){
		mCamera=Camera.open();
	}

	/**
	 * 设置参数
	 * @param parameters
	 */
	public void setParameters(Camera.Parameters parameters){
		mCamera.setParameters(parameters);
	}

	/**
	 * 开始预览
	 */
	public void startPreView(){
		mCamera.setDisplayOrientation(90);
		mCamera.startPreview();
        mCamera.cancelAutoFocus();
		isPreviewing=true;
	}

	/**
	 * 获取android.hardware.Camera对象
	 * @return
	 */
	public Camera getCamera(){
		return mCamera;
	}

	/**
	 * 停止相机
	 */
	public void doStopCamera() {
		mCamera.stopPreview();
		isPreviewing=false;
		mCamera.release();
		mCamera=null;
	}

	/**
	 * 绑定surface到摄像头
	 * @param holder
	 */
	public void setPreviewDisplay(SurfaceHolder holder) {
		try {
			mCamera.setPreviewDisplay(holder);
		} catch (IOException e) {
			e.printStackTrace();
			mCamera.release();
		}
	}

	/**
	 * 普通拍照
	 */
	public void doTakePicture(){
		if(isPreviewing&&mCamera!=null){
			mCamera.takePicture(mShutterCallback, null, mJpegPictureCallback);
		}
	}

	int DST_RECT_WIDTH, DST_RECT_HEIGHT,SCREEN_WIDTH,SCREEN_HEIGHT;
	float DPI;
	/**
	 * 拍摄指定区域方法
	 * @param w
	 * @param h
	 */
	public void doTakePicture(int w, int h,int screenW,int screenH,float d){
		if(isPreviewing && (mCamera != null)){
			DST_RECT_WIDTH = w;
			DST_RECT_HEIGHT = h;
			SCREEN_WIDTH = screenW;
			SCREEN_HEIGHT = screenH;
            DPI = d;
            Log.i("szm--", "矩形拍照尺寸:width = " + w + " h = " + h);
			mCamera.takePicture(mShutterCallback, null, mRectJpegPictureCallback);
		}
	}

	/*为了实现拍照的快门声音及拍照保存照片需要下面三个回调变量*/
	ShutterCallback mShutterCallback = new ShutterCallback()
			//快门按下的回调，在这里我们可以设置类似播放“咔嚓”声之类的操作。默认的就是咔嚓。
	{
		public void onShutter() {
			// 设置快门声
		}
	};

	/**
	 * 常规拍照
	 */
	PictureCallback mJpegPictureCallback = new PictureCallback()
			//对jpeg图像数据的回调,最重要的一个回调
	{
		public void onPictureTaken(byte[] data, Camera camera) {
			Log.i("szm--", "myJpegCallback:onPictureTaken...");
			Bitmap b = null;
			if(null != data){
				b = BitmapFactory.decodeByteArray(data, 0, data.length);//data是字节数据，将其解析成位图
				mCamera.stopPreview();
				isPreviewing = false;
			}
			//保存图片到sdcard
			if(null != b)
			{
				FileUtil.saveBitmap(b,card);
			}
			//再次进入预览
			mCamera.startPreview();
			isPreviewing = true;
		}
	};

	/**
	 * 拍摄指定区域的Rect
	 */
	PictureCallback mRectJpegPictureCallback = new PictureCallback()
			//对jpeg图像数据的回调,最重要的一个回调
	{
		public void onPictureTaken(byte[] data, Camera camera) {
			Log.i("szm--", "myJpegCallback:onPictureTaken...");
			Bitmap b = null;
			if(null != data){
				b = BitmapFactory.decodeByteArray(data, 0, data.length);//data是字节数据，将其解析成位图
				mCamera.stopPreview();
				isPreviewing = false;
			}
			//保存图片到sdcard
			if(null != b)
			{
                if (b.getWidth() > b.getHeight()){
                    b = DisplayUtils.adjustPhotoRotation(b, 90);
                }

				float scalW = DST_RECT_WIDTH;
                float scalH = DST_RECT_HEIGHT;


                if (b.getWidth() / (float)SCREEN_WIDTH != 1) {
					scalW = DST_RECT_WIDTH * (b.getWidth() / (float)SCREEN_WIDTH);
				}
				if (b.getHeight() / (float)SCREEN_HEIGHT != 1) {
//					scalH = DST_RECT_HEIGHT * (b.getHeight() / (float)SCREEN_HEIGHT);
				}

				//图片宽度减去遮罩区宽度，剩余宽度除以2，得到x点
				int x = (int)((b.getWidth() - scalW)/ 2);
                int y = (int)(100 * (b.getHeight() / (float)SCREEN_HEIGHT));

                if (DST_RECT_HEIGHT < 1000) {
                    scalH += (int) (82 * (b.getHeight() / (float) SCREEN_HEIGHT));
                } else {
                    scalH += (int)(130 * (b.getHeight() / (float)SCREEN_HEIGHT));
                }

				Log.i("szm--", "---x=="+x+"---y=="+y);
				Log.i("szm--", "b.getWidth() = " + b.getWidth()
						+ " b.getHeight() = " + b.getHeight() + " scalW=" + scalW +" scalH = " + scalH);

				Bitmap rectBitmap = Bitmap.createBitmap(b, x, y, (int)scalW, (int)scalH);
//                Bitmap rectBitmap = Bitmap.createBitmap(b);
                FileUtil.saveBitmap(rectBitmap,card);


                scalW = rectBitmap.getWidth();
                scalH = rectBitmap.getHeight() / 2;
                x = 0;
                y = 0;

                Bitmap ShouHuoRenBitmap = Bitmap.createBitmap(rectBitmap, x, y, (int)scalW, (int)scalH);
                FileUtil.saveBitmap(ShouHuoRenBitmap,ShouHuoRenCard);

                scalW = rectBitmap.getWidth();
                scalH = rectBitmap.getHeight() / 2;
                x = 0;
                y = rectBitmap.getHeight() / 2;

                Bitmap TiHuoRenBitmap = Bitmap.createBitmap(rectBitmap, x, y, (int)scalW, (int)scalH);
                FileUtil.saveBitmap(TiHuoRenBitmap,TiHuoRenCard);

				if(b.isRecycled()){
					b.recycle();
					b = null;
				}

				if(rectBitmap.isRecycled()){
					rectBitmap.recycle();
					rectBitmap = null;
				}

                if(ShouHuoRenBitmap.isRecycled()){
                    ShouHuoRenBitmap.recycle();
                    ShouHuoRenBitmap = null;
                }

                if(TiHuoRenBitmap.isRecycled()){
                    TiHuoRenBitmap.recycle();
                    TiHuoRenBitmap = null;
                }

			}

			//再次进入预览
			mCamera.startPreview();
			isPreviewing = true;
			if(!b.isRecycled()){
				b.recycle();
				b = null;
			}
		}
	};
}