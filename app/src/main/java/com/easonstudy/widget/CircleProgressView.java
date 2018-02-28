package com.easonstudy.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.easonstudy.R;

/**
 * Created by Administrator on 2017/6/28.
 */

public class CircleProgressView extends View {

    private int mCurrent;//当前进度
    private Paint mPaintOut;
    private Paint mPaintCurrent;
    private Paint mPaintText;
    private float mPaintWidth;//画笔宽度
    private int mPaintColor = Color.RED;//画笔颜色
    private int mTextColor = Color.BLACK;//字体颜色
    private float mTextSize;//字体大小
    private int location;//从哪个位置开始
    private float startAngle;//开始角度

    private OnLoadingCompleteListener mOnLoadingCompleteListener;

    public CircleProgressView(Context context) {
        this(context,null);
    }

    public CircleProgressView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CircleProgressView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.CircleProgressView);
        location = array.getInt(R.styleable.CircleProgressView_location,1);
        mPaintWidth = array.getDimension(R.styleable.CircleProgressView_progress_paint_width,dip2px(context,4));//默认是4dp
        mPaintColor  = array.getColor(R.styleable.CircleProgressView_progress_paint_color,mPaintColor);
        mTextColor = array.getColor(R.styleable.CircleProgressView_progress_text_color,mTextColor);
        mTextSize = array.getDimensionPixelSize(R.styleable.CircleProgressView_progress_text_size,dip2px(context,18)); // 默认是18sp
        array.recycle();

        //背景圆弧画笔
        mPaintOut = new Paint();
        mPaintOut.setAntiAlias(true);//设置画笔的锯齿效果。true代表抗锯齿，false代表不抗锯齿
        mPaintOut.setStyle(Paint.Style.STROKE);//设置画笔的样式，为FILL(实心的)，FILL_OR_STROKE，或STROKE（空心的）
        mPaintOut.setStrokeWidth(mPaintWidth);//当画笔样式为STROKE或FILL_OR_STROKE时，设置笔刷的粗细度即宽度。
        mPaintOut.setColor(Color.GRAY);
        mPaintOut.setStrokeCap(Paint.Cap.ROUND);//当画笔样式为STROKE或FILL_OR_STROKE时，设置笔刷的图形样式，如圆角形样式Cap.ROUND,或方形样式Cap.SQUARE。这个会影响画笔的始末端
        //进度圆弧画笔
        mPaintCurrent = new Paint();
        mPaintCurrent.setAntiAlias(true);
        mPaintCurrent.setStyle(Paint.Style.STROKE);
        mPaintCurrent.setStrokeWidth(mPaintWidth);
        mPaintCurrent.setColor(mPaintColor);
        mPaintCurrent.setStrokeCap(Paint.Cap.ROUND);
        //字体画笔
        mPaintText = new Paint();
        mPaintText.setAntiAlias(true);
        mPaintText.setStyle(Paint.Style.FILL);
        mPaintText.setColor(mTextColor);
        mPaintText.setTextSize(mTextSize);
        if (location == 1) {//默认从左侧开始
            startAngle = -180;
        } else if (location == 2) {
            startAngle = -90;
        } else if (location == 3) {
            startAngle = 0;
        } else if (location == 4) {
            startAngle = 90;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int with = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int size = with > height ? height:with;
        setMeasuredDimension(size,size);  //这个方法决定了当前View的大小
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //绘制背景圆弧,因为画笔有一定的宽度，所有画圆弧的范围要比View本身的大小稍微小一些，不然画笔画出来的东西会显示不完整
        RectF rectF = new RectF(mPaintWidth / 2, mPaintWidth / 2,getWidth()-mPaintWidth/2,getHeight()-mPaintWidth/2);   //分别于RectF(float left, float top, float right, float bottom)
        canvas.drawArc(rectF, 0, 360, false, mPaintOut);

        //绘制当前进度
        float sweepAngle = 360 * mCurrent / 100;
        canvas.drawArc(rectF, startAngle, sweepAngle, false, mPaintCurrent);

        String text = mCurrent+"%";
        //获取文字宽度
        float textWith = mPaintText.measureText(text,0,text.length());
        float dx = getWidth()/2 - textWith/2;
        Paint.FontMetricsInt fontMetricsInt = mPaintText.getFontMetricsInt();
        float dy = (fontMetricsInt.bottom - fontMetricsInt.top)/2 - fontMetricsInt.bottom;
        float baseline = getHeight()/2 + dy;
        canvas.drawText(text,dx,baseline,mPaintText);
        if (mOnLoadingCompleteListener!=null && mCurrent == 100){
            mOnLoadingCompleteListener.complete();
        }

    }

    private int dip2px(Context context,float dpValue){
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int)(dpValue*scale+0.5f);
    }

    public int getmCurrent() {
        return mCurrent;
    }

    public void setmCurrent(int mCurrent) {
        this.mCurrent = mCurrent;
        invalidate();
    }

    public void setOnLoadingConpleteListener(OnLoadingCompleteListener onLoadingConpleteListener){
        this.mOnLoadingCompleteListener = onLoadingConpleteListener;
    }

    public interface OnLoadingCompleteListener{
        void complete();
    }
}
