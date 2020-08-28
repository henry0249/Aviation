package com.example.administrator.aviation.tool.recognize.utils;

import android.graphics.PixelFormat;
import android.graphics.Point;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.Size;
import android.util.Log;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import static android.hardware.Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE;
import static android.hardware.Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO;

/**
 * 摄像头的一些参数设置
 *
 * @author michael
 *
 */
public class CameraPara {
	/**参数中的宽度*/
	private int paraWidth = 0;
	/**参数中设置的高度*/
	private int paraHeight = 0;
	//预览的最大宽高
	/**
	 * 获取屏幕长宽比
	 */
	private float rates = 1;

	public Point getScreenResolution() {
		return screenResolution;
	}

	public void setScreenResolution(Point screenResolution) {
		this.screenResolution = screenResolution;
	}

	private Point screenResolution;
	/**
	 * 比较器，用于排序
	 */
	private CameraSizeComparator sizeComparator=new CameraSizeComparator();
	private Parameters parameters;

	public CameraPara(Camera camera) {
		this.parameters = camera.getParameters();
	}

	/**
	 * 给摄像头参数设置值
	 */
	public void setValues(){
		parameters.setPictureFormat(PixelFormat.JPEG);//设置拍照后存储的图片格式
//		setColorEffect();
		setPictureSizes();
		setPreviewSizes();
		setFocusModes();
		parameters.setRotation(90);
	}

	public void setRates(float ra){
		this.rates=ra;
	}

	/**
	 * 获取设置参数
	 * @return
	 */
	public Parameters getParameters(){
		return parameters;
	}

	/**
	 * 设置ColorEffects
	 */
	private void setColorEffect() {
		List<String> colorEffects = parameters.getSupportedColorEffects();
		Iterator<String> iterator = colorEffects.iterator();
		while (iterator.hasNext()) {
			String currentEffect = iterator.next();
			Log.i("szm--", "surport:--" + currentEffect);
			if (currentEffect.equals(Parameters.EFFECT_SOLARIZE)) {// 曝光
				parameters.setColorEffect(currentEffect);
				break;
			}
		}
	}

	/**
	 * 判断比率是否合适
	 * @param s
	 * @param rate
	 * @return
	 */
	private  boolean equalRate(Size s, float rate){
		float r = (float)(s.width)/(float)(s.height);
		if(Math.abs(r - rate) <= 0.1) {
			return true;
		}

		if(Math.abs(r - rate) <= 0.13) {
			return true;
		}

		if(Math.abs(r - rate) <= 0.15) {
			return true;
		}

		if(Math.abs(r - rate) <= 0.18) {
			return true;
		}

		return false;
	}

	/**
	 * 设置PictureSizes
	 */
	private void setPictureSizes(){
		List<Size> list= parameters.getSupportedPictureSizes();
		Collections.sort(list, sizeComparator);
		int i = 0;
		for(Size s:list){
			if((s.height >= 800) && equalRate(s, rates)){
				break;
			}
			i++;
		}
		if(i == list.size()){
			i = 0;//如果没找到，就选最小的size
		}
		parameters.setPictureSize(list.get(i).width, list.get(i).height);
	}



	/**
	 * 设置预览尺寸大小
	 */
	private void setPreviewSizes(){
		List<Size> sizes = parameters.getSupportedPreviewSizes();
		Collections.sort(sizes, sizeComparator);
		int i = 0;
		for(Size s:sizes){
			if((s.height >= 800) && equalRate(s, rates)){
				break;
			}
			i++;
		}
		if(i == sizes.size()){
			i = 0;//如果没找到，就选最小的size
		}
		parameters.setPreviewSize(sizes.get(i).width, sizes.get(i).height);
	}

	/**
	 * 设置聚焦模式
	 */
	private void setFocusModes(){
		List<String> focusModes = parameters.getSupportedFocusModes();

		if(focusModes.contains(FOCUS_MODE_CONTINUOUS_PICTURE)){
			parameters.setFocusMode(FOCUS_MODE_CONTINUOUS_PICTURE);
		} else if(focusModes.contains(FOCUS_MODE_CONTINUOUS_VIDEO)){
			parameters.setFocusMode(FOCUS_MODE_CONTINUOUS_VIDEO);
		}
	}


	/**
	 * 自定义的比较器
	 * @author null
	 *
	 */
	public  class CameraSizeComparator implements Comparator<Size>{
		public int compare(Size lhs, Size rhs) {
			// TODO Auto-generated method stub
			if(lhs.width == rhs.width){
				return 0;
			}
			else if(lhs.width > rhs.width){
				return 1;
			}
			else{
				return -1;
			}
		}
	}
}
