package com.example.administrator.aviation.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.LinearInterpolator;

import com.example.administrator.aviation.R;


/**
 * 进度条（用于apk下载，显示下载进度）
 */
public class LengthProgressBar extends View {
    private static final long DURING_TIME = 800L;
    private Paint mPaint;//画笔
    private int backColor;//圆环背景颜色
    private int progressColor;//圆环进度条的颜色
    private int center;//该控件中心点
    private float progressWidth;//进度条的宽度
    private float radius;//圆环半径
    private float tagRadius;//小圆点半径
    private float mWidth;//宽度
    private float mHeight;//高度
    private boolean isOnce;

    private double maxProgress = 100L;//总进度值，默认值100
    private float progress;
    private ValueAnimator mAnimator;
    private float mPreX;
    private float mPreY;
    private int mScaledTouchSlop;

    public void resetBackColor(int backColor) {
        this.backColor = backColor;
        invalidate();
    }

    public void setMaxProgress(double maxProgress) {
        this.maxProgress = maxProgress;
    }

    public void setBackColor(int backColor) {
        this.backColor = backColor;
    }

    public void setProgressColor(int progressColor) {
        this.progressColor = progressColor;
    }

    public LengthProgressBar(Context context) {
        super(context);
    }

    public LengthProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LengthProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        //设置画笔
        mPaint = new Paint();

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RoundProgressBar);
        backColor = typedArray.getColor(R.styleable.RoundProgressBar_backColor, Color.parseColor("#FF080808"));
        progressColor = typedArray.getColor(R.styleable.RoundProgressBar_progressColor, Color.parseColor("#FFFF4081"));
        progressWidth = typedArray.getDimension(R.styleable.RoundProgressBar_progressWidth, 40);
        progress = typedArray.getFloat(R.styleable.RoundProgressBar_progress, 0);
        tagRadius = typedArray.getDimension(R.styleable.RoundProgressBar_tagRadius, progressWidth / 3);
        typedArray.recycle();

        mScaledTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //设置描边
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(progressWidth);

        //先绘制大圆
        mPaint.setColor(backColor);
        mPaint.setDither(true);//设置抖动
        mPaint.setAntiAlias(true);//消除锯齿
//        canvas.drawCircle(center, center, radius, mPaint);
        RectF rectF = new RectF(0,0,mWidth,mHeight);
        canvas.drawRoundRect(rectF,mHeight/2,mHeight/2,mPaint);

        //绘制进度条
        mPaint.setColor(progressColor);
        RectF rectF1 = new RectF(0,0, (float) (progress/maxProgress * mWidth),mHeight);
        canvas.drawRoundRect(rectF1,mHeight/2,mHeight/2,mPaint);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (!isOnce) {
            isOnce = true;
            mWidth = w;
            mHeight = h;
            center = w / 2;
            radius = center - progressWidth / 2;
        }
    }

    /**
     * 增加同步锁，增加安全性,可以在非主线程中使用postInvalidate()进行刷新视图
     *
     * @param progress
     */
    public synchronized void setProgress(float progress, boolean animate) {
        if (animate) {
            setAnimateProgress(progress);
        } else {
            this.progress = progress;
            postInvalidate();
        }
    }

    private void setAnimateProgress(float progress) {
        if (mAnimator == null) {
            mAnimator = ValueAnimator.ofFloat(0, progress);
            mAnimator.setDuration(DURING_TIME);
            mAnimator.setInterpolator(new LinearInterpolator());
            mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float animatedValue = (float) animation.getAnimatedValue();
                    LengthProgressBar.this.progress = animatedValue;
                    postInvalidate();
                }
            });
        } else {
            mAnimator.setFloatValues(0, progress);
//            mAnimator.setDuration((long) (DURING_TIME / this.maxProgress * progress));
            mAnimator.setDuration(DURING_TIME);
        }
        mAnimator.start();
    }

    /**
     * 检验当前手指是否在圆形progressBar中
     *
     * @param x
     * @param y
     * @return
     */
    private boolean checkToutch(float x, float y) {
        boolean inToutch = false;
        inToutch = (x - center) * (x - center) + (y - center) * (y - center) <= center*center;
        return inToutch;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int minw = getPaddingLeft() + getPaddingRight() + getSuggestedMinimumWidth();
        int w = resolveSizeAndState(minw, widthMeasureSpec, 1);
        int h = resolveSizeAndState(MeasureSpec.getSize(w), heightMeasureSpec, 0);
        setMeasuredDimension(w, h);
    }

}
