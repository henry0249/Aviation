package com.example.administrator.aviation.model.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.example.administrator.aviation.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 自定义锁屏密码view
 * （此类有待优化）
 */
@SuppressLint({ "ClickableViewAccessibility", "DrawAllocation" })
public class LockPatternView extends View{
    //监听器
    private OnPatterChangeListner onPatterChangeListner;
    private static int POINT_SIZE = 5;
    // 画笔
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    // 定义九个点
    public Point[][] points = new Point[3][3];
    // 矩阵
    private Matrix matrix = new Matrix();
    private boolean isInit, isSelect, isFinish, movingNoPoint;
    private float width, height, offsetX, offsetY, bitmapR, movingX, movingY;
    private Bitmap pointsNomal, pointsPressed, pointsError, linePressed,
            lineError;
    private List<Point> pointList = new ArrayList<Point>();

    public LockPatternView(Context context) {
        super(context);
    }

    public LockPatternView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        // TODO Auto-generated constructor stub
    }

    public LockPatternView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
    }

    /**
     * 初始化九个点
     */
    @Override
    protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        super.onDraw(canvas);
        if (!isInit) {
            initPoints();
        }
        if (pointList.size() > 0) {
            // 绘制九宫格里面的点
            Point a = pointList.get(0);
            for (int i = 0; i < pointList.size(); i++) {
                Point b = pointList.get(i);
                line2Canvas(canvas, a, b);
                a = b;
            }
            // 绘制鼠标坐标点
            if (movingNoPoint) {
                line2Canvas(canvas, a, new Point(movingX, movingY));
            }
        }
        point2Canvas(canvas);
    }

    /**
     * 绘制划线
     *
     * @param canvas
     *            画布
     * @param a
     *            第一个点
     * @param b
     *            第二个点
     */
    private void line2Canvas(Canvas canvas, Point a, Point b) {
        // 线的长度
        float lineLength = (float) Point.distance(a, b);
        float degrees = getDegree(a, b);
        canvas.rotate(degrees, a.x, a.y);
        if (a.state == Point.STATE_PRESSED) {
            matrix.setScale(lineLength / linePressed.getWidth(), 1);
            // 矩阵处理线的平移动，从第一个点进行平移
            matrix.postTranslate(a.x - linePressed.getWidth() / 2, a.y- linePressed.getHeight() / 2);
            canvas.drawBitmap(linePressed, matrix, paint);
        } else {
            matrix.setScale(lineLength / lineError.getWidth(), 1);
            // 矩阵处理线的平移动，从第一个点进行平移
            matrix.postTranslate(a.x - lineError.getWidth() / 2, a.y- lineError.getHeight() / 2);
            canvas.drawBitmap(lineError, matrix, paint);
        }
        canvas.rotate(-degrees, a.x, a.y);
    }

    /**
     * 将点绘制到画布上
     *
     * @param canvas
     */
    private void point2Canvas(Canvas canvas) {
        for (int i = 0; i < points.length; i++) {
            for (int j = 0; j < points[i].length; j++) {
                Point point = points[i][j];
                if (point.state == Point.STATE_PRESSED) {
                    canvas.drawBitmap(pointsPressed, point.x - bitmapR, point.y
                            - bitmapR, paint);
                } else if (point.state == Point.STATE_ERROR) {
                    canvas.drawBitmap(pointsError, point.x - bitmapR, point.y
                            - bitmapR, paint);
                } else {
                    canvas.drawBitmap(pointsNomal, point.x - bitmapR, point.y
                            - bitmapR, paint);
                }
            }
        }
    }

    /**
     * 初始化点
     */
    private void initPoints() {
        // 1.获取手机的宽高
        width = getWidth();
        height = getHeight();
        // 2.计算 x,y偏移量

        // 判断手机横屏或者竖屏
        // 横屏
        if (width > height) {
            offsetX = (width - height) / 2;
            width = height;
            // 竖屏
        } else {
            offsetY = (height - width) / 2;
            height = width;
        }
        // 3.获取图片资源
        pointsNomal = BitmapFactory.decodeResource(getResources(),
                R.drawable.point_nomal);
        pointsPressed = BitmapFactory.decodeResource(getResources(),
                R.drawable.point_pressed);
        pointsError = BitmapFactory.decodeResource(getResources(),
                R.drawable.point_error);
        linePressed = BitmapFactory.decodeResource(getResources(),
                R.drawable.line_pressed);
        lineError = BitmapFactory.decodeResource(getResources(),
                R.drawable.line_error);
        // 4. 绘制点
        // 第一行
        points[0][0] = new Point(offsetX + width / 4, offsetY + width / 4);
        points[0][1] = new Point(offsetX + width / 2, offsetY + width / 4);
        points[0][2] = new Point(offsetX + width - width / 4, offsetY + width/ 4);
        // 第二行
        points[1][0] = new Point(offsetX + width / 4, offsetY + width / 2);
        points[1][1] = new Point(offsetX + width / 2, offsetY + width / 2);
        points[1][2] = new Point(offsetX + width - width / 4, offsetY + width/ 2);
        // 第三行
        points[2][0] = new Point(offsetX + width / 4, offsetY + width - width/ 4);
        points[2][1] = new Point(offsetX + width / 2, offsetY + width - width/ 4);
        points[2][2] = new Point(offsetX + width - width / 4, offsetY + width- width / 4);
        // 5.处理图片资源的半径
        bitmapR = pointsNomal.getHeight() / 2;
        //6.设置密码
        int index = 1;
        for(Point [] points:this.points){
            for(Point point:points){
                point.index = index;
                index++;
            }
        }
        // 7.初始化完成
        isInit = true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        movingNoPoint = false;
        isFinish = false;
        movingX = event.getX();
        movingY = event.getY();
        Point point = null;
        switch (event.getAction()) {
            // 按下
            case MotionEvent.ACTION_DOWN:
                //重新绘制
                if (onPatterChangeListner!=null) {
                    onPatterChangeListner.onPatterStart(true);
                }
                resets();
                point = checkSelectPoint();
                if (point != null) {
                    isSelect = true;
                }
                break;
            // 移动
            case MotionEvent.ACTION_MOVE:
                if (isSelect) {
                    point = checkSelectPoint();
                    if (point == null) {
                        movingNoPoint = true;
                    }
                }
                break;
            // 抬起
            case MotionEvent.ACTION_UP:
                isFinish = true;
                isSelect = false;
                break;

        }
        // 选中重复检查
        if (!isFinish && isSelect && point != null) {
            // 交叉点
            if (crossPoint(point)) {
                movingNoPoint = true;
                // 新点
            } else {
                point.state = Point.STATE_PRESSED;
                pointList.add(point);
            }
        }
        // 绘制结束
        if (isFinish) {
            // 绘制不成立
            if (pointList.size() == 1) {
                // 清除集合
                resets();
                // 绘制错误
            } else if (pointList.size() < POINT_SIZE && pointList.size() > 0) {
                errorPoint();
                if(onPatterChangeListner!=null){
                    onPatterChangeListner.onPatterChange(null);
                }
                //绘制成功
            }else {
                if(onPatterChangeListner!=null){
                    String passString ="";
                    for (int i = 0; i < pointList.size(); i++) {
                        passString+=pointList.get(i).index;
                    }
                    if (!TextUtils.isEmpty(passString)) {

                        onPatterChangeListner.onPatterChange(passString);
                    }
                }
            }
        }
        // 刷新View
        postInvalidate();
        return true;
    }

    /**
     * 交叉点的检查
     *
     * @param point 点
     * @return 是否交叉
     */
    private boolean crossPoint(Point point) {
        if (pointList.contains(point)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 设置绘制不成立
     */
    public void resets() {
        for (int i = 0; i < pointList.size(); i++) {
            Point point = pointList.get(i);
            point.state = Point.STATE_NOMAL;
        }
        pointList.clear();
    }

    /**
     * 设置绘制错误
     */
    public void errorPoint() {
        for (Point point : pointList) {
            point.state = Point.STATE_ERROR;
        }
    }

    /**
     * 检查鼠标的点是否和九宫格中的点重合
     */
    private Point checkSelectPoint() {
        for (int i = 0; i < points.length; i++) {
            for (int j = 0; j < points[i].length; j++) {
                Point point = points[i][j];
                if (Point.with(point.x, point.y, bitmapR, movingX, movingY)) {
                    return point;
                }
            }
        }
        return null;
    }

    /**
     * 自定义的点
     */
    public static class Point {
        public static int STATE_NOMAL = 0;
        public static int STATE_PRESSED = 1;
        public static int STATE_ERROR = 2;
        public float x, y;
        public int index = 0, state = 0;

        public Point() {
            // TODO Auto-generated constructor stub
        }

        public Point(float x, float y) {
            this.x = x;
            this.y = y;
        }

        /**
         * 两点的距离
         *
         * @param a
         *            点a
         * @param b
         *            点b
         * @return 距离
         */
        public static double distance(Point a, Point b) {
            // X轴差的平方+Y轴差的平方，对和开方
            return Math.sqrt(Math.abs(a.x - b.x) * Math.abs(a.x - b.x)
                    + Math.abs(a.y - b.y) * Math.abs(a.y - b.y));
        }

        /**
         * 是否重合
         *
         * @param pointX
         *            参考点的X
         * @param pointY
         *            参考点的Y
         * @param r
         *            圆的半径
         * @param movingX
         *            移动点的X
         * @param movingY
         *            移动点的Y
         * @return 是否重合
         */
        public static boolean with(float pointX, float pointY, float r,
                                   float movingX, float movingY) {
            // 开方
            return Math.sqrt((pointX - movingX) * (pointX - movingX)
                    + (pointY - movingY) * (pointY - movingY)) < r;
        }
    }

    /**
     * 获取角度
     *
     * @param a
     *            第一个点
     * @param b
     *            第二个点
     * @return 角度
     */
    public float getDegree(Point a, Point b) {
        float ax = a.x;
        float ay = a.y;
        float bx = b.x;
        float by = b.y;
        float degree = 0;
        if (ax == bx) {// Y轴相等 90度或者270度
            if (by > ay) {
                degree = 90;
            } else if (by < ay) {
                degree = 270;
            }
        } else if (by == ay) {// X轴相等 0度或者180度
            if (bx > ax) {
                degree = 0;
            } else if (bx < ax) {
                degree = 180;
            }
        } else if (bx > ax) {//在Y轴右边270~90
            if(by>ay){//在Y轴下面0~90
                degree = 0;
                degree = degree+switchDegree(Math.abs(by-ay),Math.abs(bx-ax));
            }else if(by<ay){//在Y轴的上边 270~0
                degree = 360;
                degree = degree-switchDegree(Math.abs(by-ay),Math.abs(bx-ax));
            }
        } else if (bx < ax) {//在Y轴左边 90~270
            if(by>ay){//在Y轴下面180~270
                degree = 90;
                degree = degree+switchDegree(Math.abs(bx-ax),Math.abs(by-ay));
            }else if(by<ay){//在Y轴的上边90~180
                degree = 270;
                degree = degree-switchDegree(Math.abs(bx-ax),Math.abs(by-ay));
            }
        }
        return degree;
    }
    private float switchDegree(float x,float y){
        //弧度转化为角度
        return (float) Math.toDegrees(Math.atan2(x, y));
    }
    /**
     * 图案监听器
     * @author Administrator
     *
     */
    public static interface OnPatterChangeListner{
        /**
         * 图案改变
         * @param passString 图案的密码
         */
        void onPatterChange(String passString);
        /**
         * 是否开始绘制图案
         * @param isStart 是否重新绘制
         */
        void onPatterStart(boolean isStart);
    }
    /**
     * 设置图案监听器
     * @param onPatterChangeListner
     */
    public void setPatterChangeListner(OnPatterChangeListner onPatterChangeListner){
        if(onPatterChangeListner!=null){
            this.onPatterChangeListner = onPatterChangeListner;
        }
    }

}
