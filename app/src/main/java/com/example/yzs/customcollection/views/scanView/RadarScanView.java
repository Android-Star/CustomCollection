package com.example.yzs.customcollection.views.scanView;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.Transformation;
import com.example.yzs.customcollection.R;
import com.example.yzs.customcollection.utils.DisplayUtils;

public class RadarScanView extends View {
  private static final int DEFAULT_LINE_WIDTH = 5;
  private static final int DEFAULT_POINT_NUM = 10;
  private static final int DEFAULT_VIEW_SIZE = 200;
  private Paint bgPaint;
  private Paint pointPaint;
  private Paint defaultPaint;
  private Paint gradientPaint;

  private int radarBgColor;
  private int defaultColor;
  private int pointColor;
  private int gradientStartColor;
  private int gradientStopColor;

  private int lineWidth;
  private int pointNum;

  private int defaultSize;
  private int viewSize;

  private float currentAngle;
  private ScanAnimation scanAnimation;
  private boolean needGetPoint = true;
  private float[] currentPoints;
  private boolean cancelAnimation = false;

  public RadarScanView(Context context) {
    this(context, null);
  }

  public RadarScanView(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public RadarScanView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init(context, attrs);
  }

  private void init(Context context, AttributeSet attrs) {
    TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RadarScanView);
    radarBgColor = typedArray.getColor(R.styleable.RadarScanView_radarBgColor, Color.GRAY);
    defaultColor = typedArray.getColor(R.styleable.RadarScanView_defaultColor, Color.LTGRAY);
    pointColor = typedArray.getColor(R.styleable.RadarScanView_pointColor, Color.WHITE);
    gradientStartColor = typedArray.getColor(R.styleable.RadarScanView_gradientStartColor,
        getResources().getColor(R.color.radarStartColor));
    gradientStopColor =
        typedArray.getColor(R.styleable.RadarScanView_gradientStopColor, Color.TRANSPARENT);
    lineWidth = (int) typedArray.getDimension(R.styleable.RadarScanView_lineWidth,
        DisplayUtils.getInstance(context).dp2px(DEFAULT_LINE_WIDTH));
    pointNum = typedArray.getInteger(R.styleable.RadarScanView_pointNum, DEFAULT_POINT_NUM);
    typedArray.recycle();

    bgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    bgPaint.setColor(radarBgColor);

    pointPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    pointPaint.setStrokeWidth(lineWidth);
    pointPaint.setStyle(Paint.Style.STROKE);
    pointPaint.setColor(pointColor);

    defaultPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    defaultPaint.setColor(defaultColor);
    defaultPaint.setStrokeWidth(lineWidth);
    defaultPaint.setStyle(Paint.Style.STROKE);

    gradientPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    defaultSize = (int) DisplayUtils.getInstance(context).dp2px(DEFAULT_VIEW_SIZE);
    scanAnimation = new ScanAnimation();
  }

  @Override protected void onDraw(Canvas canvas) {
    canvas.drawCircle(getWidth() / 2, getHeight() / 2, getWidth() / 2, bgPaint);
    canvas.drawLine(0, getHeight() / 2, getWidth(), getHeight() / 2, defaultPaint);
    canvas.drawLine(getWidth() / 2, 0, getWidth() / 2, getHeight(), defaultPaint);
    canvas.drawCircle(getWidth() / 2, getHeight() / 2, getWidth() / 6, defaultPaint);
    canvas.drawCircle(getWidth() / 2, getHeight() / 2, getWidth() / 3, defaultPaint);

    if (needGetPoint) {
      currentPoints = getPoints();
    }
    canvas.drawPoints(currentPoints, pointPaint);
    canvas.rotate(-currentAngle, getWidth() / 2, getHeight() / 2);
    gradientPaint.setShader(
        new SweepGradient(getWidth() / 2, getHeight() / 2, gradientStartColor, gradientStopColor));
    canvas.drawCircle(getWidth() / 2, getHeight() / 2, getWidth() / 2, gradientPaint);
  }

  @Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    int width = measure(widthMeasureSpec);
    int height = measure(heightMeasureSpec);
    viewSize = Math.min(width, height);
    setMeasuredDimension(viewSize, viewSize);
  }

  private int measure(int measureSpec) {
    int result = defaultSize;

    int specSize = MeasureSpec.getSize(measureSpec);
    int specMode = MeasureSpec.getMode(measureSpec);
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

  private float[] getPoints() {
    float[] result = new float[pointNum];
    for (int i = 0; i < pointNum; i++) {
      result[i] = (float) (Math.random() * getWidth());
    }
    return result;
  }

  private class ScanAnimation extends Animation {
    @Override protected void applyTransformation(float interpolatedTime, Transformation t) {
      super.applyTransformation(interpolatedTime, t);

      if (cancelAnimation) {
        return;
      }
      currentAngle = interpolatedTime * 360;
      Log.d(RadarScanView.class.getSimpleName(), interpolatedTime + "");
      if (interpolatedTime == 1) {
        needGetPoint = true;
      } else {
        needGetPoint = false;
      }
      invalidate();
    }
  }

  public void start(long duration) {
    if (scanAnimation != null && duration == scanAnimation.getDuration()) {
      cancelAnimation = false;
    } else {
      scanAnimation.setDuration(duration);
      scanAnimation.setRepeatCount(Animation.INFINITE);
      scanAnimation.setInterpolator(new LinearInterpolator());
      startAnimation(scanAnimation);
    }
  }

  public void stop() {
    cancelAnimation = true;
  }
}
