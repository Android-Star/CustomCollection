package com.example.yzs.customcollection.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.Transformation;
import android.widget.TextView;
import com.example.yzs.customcollection.R;

public class WaveView extends View {
  private Paint wavePaint;                              //底层波浪画笔
  private Paint secondWavePaint;                        //上层透明度的波浪画笔
  private Path wavePath;                                //波浪绘制的path

  private float waveHeight;                             //波浪高度（指的是波浪总高度一半）
  private float waveWidth;                              //波浪宽度（指的是完整正弦半个周期宽度）
  private int waveNum;                                  //波浪数量（整个正弦周期的数量）
  private int defaultSize;                              //控件的默认宽高
  private int viewSize;                                 //控件真实宽高

  private float progressNum;                            //当前进度
  private float maxNum;                                 //最大进度
  private float percent;                                //进度百分比
  private WaveAnimation waveAnimation;                  //动画（控制动态改变波浪高度，文本显示，波浪上升速度）

  private float horizontalTsize;                        //绘制波浪时水平移动的距离

  private Paint circlePaint;                            //最底部圆形背景的画笔
  private Bitmap bitmap;                                //用于缓存的Bitmap
  private Canvas bitmapCanvas;                          //用于缓存的Canvas

  private int waveColor;                                //底层波浪颜色
  private int secondWaveColor;                          //上层波浪颜色
  private int bgColor;                                  //圆形背景颜色

  private OnAnimationListener onAnimationListener;      //改变文本的接口
  private TextView textView;                            //显示文本的TextView

  private boolean showSecondWave = true;               //控制是否需要绘制两层波浪

  public WaveView(Context context) {
    this(context, null);
  }

  public WaveView(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public WaveView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init(context, attrs);
  }

  private void init(Context context, AttributeSet attrs) {
    TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.WaveView);
    waveColor = typedArray.getColor(R.styleable.WaveView_wave_color, Color.GREEN);
    bgColor = typedArray.getColor(R.styleable.WaveView_bg_color, Color.GRAY);
    secondWaveColor = typedArray.getColor(R.styleable.WaveView_second_wave_color,
        getResources().getColor(R.color.light));
    waveHeight = typedArray.getDimension(R.styleable.WaveView_wave_height, dp2px(context, 15));
    waveWidth = typedArray.getDimension(R.styleable.WaveView_wave_width, dp2px(context, 50));

    wavePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    wavePaint.setColor(waveColor);
    wavePaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

    secondWavePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    secondWavePaint.setColor(secondWaveColor);
    secondWavePaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP));

    circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    circlePaint.setColor(bgColor);

    wavePath = new Path();

    defaultSize = (int) dp2px(context, 200);
    waveNum = (int) (Math.ceil(defaultSize / waveWidth / 2));

    waveAnimation = new WaveAnimation();
    percent = 0;
    progressNum = 0;
    maxNum = 100;

    horizontalTsize = 0;
  }

  @Override protected void onDraw(Canvas canvas) {
    bitmap = Bitmap.createBitmap(viewSize, viewSize, Bitmap.Config.ARGB_8888);
    bitmapCanvas = new Canvas(bitmap);
    bitmapCanvas.drawCircle(viewSize / 2, viewSize / 2, viewSize / 2, circlePaint);
    bitmapCanvas.drawPath(makePath(), wavePaint);
    if (showSecondWave) {
      bitmapCanvas.drawPath(getSecondPath(), secondWavePaint);
    }

    canvas.drawBitmap(bitmap, 0, 0, null);
  }

  private Path makePath() {
    wavePath.reset();
    wavePath.moveTo(viewSize, (1 - percent) * viewSize);
    wavePath.lineTo(viewSize, viewSize);
    wavePath.lineTo(0, viewSize);
    wavePath.lineTo(-horizontalTsize, (1 - percent) * viewSize);

    for (int i = 0; i < waveNum * 2; i++) {
      wavePath.rQuadTo(waveWidth / 2, (1 - percent) * waveHeight, waveWidth, 0);
      wavePath.rQuadTo(waveWidth / 2, -(1 - percent) * waveHeight, waveWidth, 0);
    }
    wavePath.close();
    return wavePath;
  }

  private Path getSecondPath() {
    wavePath.reset();
    wavePath.moveTo(0, (1 - percent) * viewSize);
    wavePath.lineTo(0, viewSize);
    wavePath.lineTo(viewSize, viewSize);
    wavePath.lineTo(viewSize + horizontalTsize, (1 - percent) * viewSize);
    for (int i = 0; i < waveNum * 2; i++) {
      wavePath.rQuadTo(-waveWidth / 2, (1 - percent) * waveHeight, -waveWidth, 0);
      wavePath.rQuadTo(-waveWidth / 2, -(1 - percent) * waveHeight, -waveWidth, 0);
    }
    wavePath.close();
    return wavePath;
  }

  @Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    int height = measureSize(defaultSize, heightMeasureSpec);
    int width = measureSize(defaultSize, widthMeasureSpec);
    int min = Math.min(height, width);
    setMeasuredDimension(min, min);

    viewSize = min;
    waveNum = (int) Math.ceil(viewSize / waveWidth / 2);
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

  private float dp2px(Context context, float dpValue) {
    float scale = context.getResources().getDisplayMetrics().density;
    return dpValue * scale + 0.5f;
  }

  public interface OnAnimationListener {
    String howToChangeText(float interpolatedTime, float updateNum, float maxNum);
  }

  public void setOnAnimationListener(OnAnimationListener onAnimationListener) {
    this.onAnimationListener = onAnimationListener;
  }

  public void setTextView(TextView textView) {
    this.textView = textView;
  }

  public void setShowSecondWave(boolean showSecondWave) {
    this.showSecondWave = showSecondWave;
  }

  public class WaveAnimation extends Animation {
    @Override protected void applyTransformation(float interpolatedTime, Transformation t) {
      super.applyTransformation(interpolatedTime, t);
      if (percent < progressNum / maxNum) {
        percent = interpolatedTime * progressNum / maxNum;
        Log.e(WaveView.class.getSimpleName(), interpolatedTime + "");
        if (onAnimationListener != null && textView != null) {
          textView.setText(
              onAnimationListener.howToChangeText(interpolatedTime, progressNum, maxNum));
        }
      }
      horizontalTsize = 2 * waveNum * waveWidth * interpolatedTime;
      postInvalidate();
    }
  }

  public void setProgress(float progress, int time) {
    this.progressNum = progress;

    percent = 0;
    waveAnimation.setDuration(time);
    waveAnimation.setRepeatCount(Animation.INFINITE);
    waveAnimation.setInterpolator(new LinearInterpolator());
    waveAnimation.setAnimationListener(new Animation.AnimationListener() {
      @Override public void onAnimationStart(Animation animation) {

      }

      @Override public void onAnimationEnd(Animation animation) {

      }

      @Override public void onAnimationRepeat(Animation animation) {
        if (percent == progressNum / maxNum) {
          waveAnimation.setDuration(3000);
        }
      }
    });
    this.startAnimation(waveAnimation);
  }
}
