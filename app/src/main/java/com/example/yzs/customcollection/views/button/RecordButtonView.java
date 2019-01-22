package com.example.yzs.customcollection.views.button;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.Transformation;
import com.example.yzs.customcollection.BuildConfig;
import com.example.yzs.customcollection.R;

public class RecordButtonView extends View {
  private static final int DEFAULT_VIEW_SIZE = 120;     //默认宽高
  private static final int DEFAULT_SMALL_RADIUS = 36;   //内圈默认半径大小
  private static final float DEFAULT_START_ALPHA = 0.4f;//外圈透明度默认初始值
  private static final float DEFAULT_END_ALPHA = 0.0f;  //外圈透明度默认结束值

  private Paint mPaint;

  private int bigCircleColor;                           //外部大圈的颜色
  private int smallCircleColor;                         //内部小圈的颜色
  private int smallRadius;                              //小圈半径
  private int imgId;                                    //图片引用
  private float startAlpha;                             //外部大圈透明度变化的初始值
  private float endAlpha;                               //外部大圈透明度变化的结束值

  private float currentAddSize;
  private float currentAlphaPercent;
  private BitmapFactory.Options opt;                    //用于控制bitmap不能缩放（否则drawBitmap画出的图可能跟原图大小不一致）
  private Bitmap bitmap;                                //缓存的bitmap（防止每次onDraw时候都新建bitmap造成内存消耗）

  private WaveAnimation waveAnimation;                  //控制外圈透明度和半径变化的动画

  public RecordButtonView(Context context) {
    this(context, null);
  }

  public RecordButtonView(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public RecordButtonView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);

    init(context, attrs);
  }

  private void init(Context context, AttributeSet attrs) {
    TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RecordButtonView);
    bigCircleColor = typedArray.getColor(R.styleable.RecordButtonView_bigCircleColor,
        getResources().getColor(R.color.chengyu_record_bg));
    smallCircleColor = typedArray.getColor(R.styleable.RecordButtonView_smallCircleColor,
        getResources().getColor(R.color.chengyu_record_bg));
    smallRadius = (int) typedArray.getDimension(R.styleable.RecordButtonView_smallRadius,
        DEFAULT_SMALL_RADIUS);
    imgId = typedArray.getResourceId(R.styleable.RecordButtonView_imgId,
        R.mipmap.chengyu_record_button);
    startAlpha = typedArray.getFloat(R.styleable.RecordButtonView_startAlpha, DEFAULT_START_ALPHA);
    endAlpha = typedArray.getFloat(R.styleable.RecordButtonView_endAlpha, DEFAULT_END_ALPHA);
    typedArray.recycle();

    mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    mPaint.setColor(getResources().getColor(R.color.chengyu_record_bg));

    opt = new BitmapFactory.Options();
    opt.inScaled = false;        //设置这个属性防止因为不同的dpi文件夹导致缩放
    opt.inPreferredConfig = Bitmap.Config.ARGB_8888;
    bitmap = BitmapFactory.decodeResource(getResources(), imgId, opt)
        .copy(Bitmap.Config.ARGB_8888, true);
    bitmap.setDensity(getResources().getDisplayMetrics().densityDpi);
  }

  @Override protected void onDraw(Canvas canvas) {
    Log.d(RecordButtonView.class.getSimpleName(), (startAlpha + currentAlphaPercent) * 255 + "");
    mPaint.setColor(bigCircleColor);
    mPaint.setAlpha((int) ((startAlpha + currentAlphaPercent) * 255));
    canvas.drawCircle(getWidth() / 2, getHeight() / 2, smallRadius + currentAddSize, mPaint);

    mPaint.setColor(smallCircleColor);
    mPaint.setAlpha(255);
    canvas.drawCircle(getWidth() / 2, getHeight() / 2, smallRadius, mPaint);

    canvas.drawBitmap(bitmap, null, new Rect(24, 24, 96, 96), null);
  }

  @Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    int height = measureSize(DEFAULT_VIEW_SIZE, heightMeasureSpec);
    int width = measureSize(DEFAULT_VIEW_SIZE, widthMeasureSpec);
    int min = Math.min(height, width);
    setMeasuredDimension(min, min);
  }

  private int measureSize(int defaultSize, int measureSpec) {
    int result = defaultSize;
    int specMode = MeasureSpec.getMode(measureSpec);
    int specSize = MeasureSpec.getSize(measureSpec);

    switch (specMode) {
      case MeasureSpec.EXACTLY:
        result = specSize;
        break;
      case MeasureSpec.AT_MOST:
        Math.min(specSize, result);
        break;
      default:
        break;
    }
    return result;
  }

  public class WaveAnimation extends Animation {
    @Override protected void applyTransformation(float interpolatedTime, Transformation t) {
      super.applyTransformation(interpolatedTime, t);
      currentAddSize = interpolatedTime * (getWidth() / 2 - smallRadius);
      currentAlphaPercent = (endAlpha - startAlpha) * interpolatedTime;
      invalidate();
    }
  }

  public void startAnimation() {
    if (waveAnimation != null) {
      clearAnimation();
      waveAnimation = null;
      invalidate();
      currentAddSize = 0;
    } else {
      waveAnimation = new WaveAnimation();
      waveAnimation.setDuration(1000);
      waveAnimation.setRepeatCount(Animation.INFINITE);
      waveAnimation.setInterpolator(new LinearInterpolator());
      this.startAnimation(waveAnimation);
    }
  }

  public boolean isStart() {
    return waveAnimation != null;
  }
}
