package com.example.yzs.customcollection.views.progressView;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;
import com.example.yzs.customcollection.R;
import com.example.yzs.customcollection.utils.DisplayUtils;
import java.text.DecimalFormat;

public class GradientCircleProgressView extends View {

  private Paint mArcPaint;                            //环形的画笔
  private Paint arcBgPaint;                              //背景颜色的画笔
  private Paint mTextPaint;                           //文本的画笔

  private int arcColor;                               //环形颜色
  private int arcBgColor;                             //背景颜色
  private int textColor;                              //文本颜色
  private int textSize;                               //文本的字体大小
  private int startColor;
  private int endColor;

  private int radius;                                 //背景园的半径
  private int arcWidth;                               //环形的宽度
  private int defaultSize;                            //默认控件大小
  private int viewSize;                               //真实大小

  private RectF arcRect;                              //环形外接矩形（对于环形，外接矩形的各边位于环形宽度的一半处）
  private Rect textRect;                              //text文本的区域矩形
  private float currentPercent;                       //当前进度的百分比

  private int maxNum;                                 //最大进度值

  private DecimalFormat decimalFormat;                //格式化数字格式

  private ValueAnimator valueAnimator;                //控制进度增长的动画

  private Paint startPaint;                           //消除渐变开头的半圆

  public GradientCircleProgressView(Context context) {
    this(context, null);
  }

  public GradientCircleProgressView(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public GradientCircleProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);

    init(context, attrs);
  }

  private void init(Context context, AttributeSet attrs) {
    TypedArray typedArray =
        context.obtainStyledAttributes(attrs, R.styleable.GradientCircleProgressView);
    arcColor = typedArray.getColor(R.styleable.GradientCircleProgressView_arcColor1, Color.YELLOW);
    arcBgColor =
        typedArray.getColor(R.styleable.GradientCircleProgressView_arcBgColor, Color.LTGRAY);
    textColor = typedArray.getColor(R.styleable.GradientCircleProgressView_textColor1, Color.BLACK);
    radius = (int) typedArray.getDimension(R.styleable.GradientCircleProgressView_radius1,
        DisplayUtils.getInstance(context).dp2px(80));
    arcWidth = (int) typedArray.getDimension(R.styleable.GradientCircleProgressView_arcWidth1,
        DisplayUtils.getInstance(context).dp2px(10));
    textSize = (int) typedArray.getDimension(R.styleable.GradientCircleProgressView_textSize1,
        DisplayUtils.getInstance(context).dp2px(20));
    typedArray.recycle();
    startColor =
        typedArray.getColor(R.styleable.GradientCircleProgressView_startColor, Color.GREEN);
    endColor = typedArray.getColor(R.styleable.GradientCircleProgressView_endColor, Color.YELLOW);
    typedArray.recycle();

    mArcPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    mArcPaint.setColor(arcColor);
    mArcPaint.setStyle(Paint.Style.STROKE);
    mArcPaint.setStrokeWidth(arcWidth);
    mArcPaint.setStrokeCap(Paint.Cap.ROUND);

    arcBgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    arcBgPaint.setColor(arcBgColor);
    arcBgPaint.setStyle(Paint.Style.STROKE);
    arcBgPaint.setStrokeWidth(arcWidth);

    mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    mTextPaint.setColor(textColor);
    mTextPaint.setTextSize(textSize);
    mTextPaint.setTypeface(Typeface.DEFAULT_BOLD);

    startPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    startPaint.setColor(startColor);

    defaultSize = (int) DisplayUtils.getInstance(context).dp2px(160);
    textRect = new Rect();
    maxNum = 100;
    decimalFormat = new DecimalFormat("0.00");
  }

  @Override protected void onDraw(Canvas canvas) {
    arcRect =
        new RectF(getWidth() / 2 - radius + arcWidth / 2, getHeight() / 2 - radius + arcWidth / 2,
            getWidth() / 2 + radius - arcWidth / 2, getHeight() / 2 + radius - arcWidth / 2);

    //绘制背景灰色环形
    canvas.drawArc(arcRect, 0, 360, false, arcBgPaint);
    //给圆环的画笔设置渐变
    mArcPaint.setShader(new SweepGradient(getWidth() / 2, getHeight() / 2, startColor, endColor));

    //绘制环形区域
    canvas.rotate(-90, getWidth() / 2, getHeight() / 2);
    canvas.drawArc(arcRect, 0, currentPercent * 360, false, mArcPaint);
    canvas.rotate(90, getWidth() / 2, getHeight() / 2);
    //判断如果进度大于0就消除开头的半圆
    if (currentPercent > 0) {
      canvas.drawCircle(getWidth() / 2, arcWidth / 2, arcWidth / 2, startPaint);
    }

    //绘制文本
    String text = decimalFormat.format(currentPercent * 100) + "%";
    mTextPaint.getTextBounds(text, 0, text.length(), textRect);

    canvas.drawText(text, getWidth() / 2 - textRect.width() / 2,
        getHeight() / 2 + textRect.height() / 2, mTextPaint);
  }

  @Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    int height = measure(heightMeasureSpec);
    int width = measure(widthMeasureSpec);
    viewSize = Math.min(height, width);
    setMeasuredDimension(viewSize, viewSize);
  }

  private int measure(int measureSpec) {
    int result = defaultSize;
    int specMode = MeasureSpec.getMode(measureSpec);
    int specSize = MeasureSpec.getSize(measureSpec);

    switch (specMode) {
      case MeasureSpec.EXACTLY:
        result = specSize;
        break;
      case MeasureSpec.AT_MOST:
        result = Math.min(result, specSize);
        break;
      default:
        break;
    }

    return result;
  }

  public void setProgress(int targetNum) {
    if (valueAnimator != null && valueAnimator.isRunning()) {
      valueAnimator.cancel();
    }
    valueAnimator = ValueAnimator.ofFloat(0, targetNum);
    valueAnimator.setDuration(targetNum * 20);
    valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
      @Override public void onAnimationUpdate(ValueAnimator animation) {
        float haha = (float) animation.getAnimatedValue();
        currentPercent = haha / maxNum;
        invalidate();
      }
    });
    valueAnimator.start();
  }
}
