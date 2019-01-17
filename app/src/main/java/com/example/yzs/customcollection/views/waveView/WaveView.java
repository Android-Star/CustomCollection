package com.example.yzs.customcollection.views.waveView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;
import com.example.yzs.customcollection.R;
import com.example.yzs.customcollection.utils.DisplayUtils;

public class WaveView extends View {
  private Paint mPaint;
  private int defaultHeight;
  private int defaultWidth;
  private int viewHeight;
  private int viewWidth;

  private int waveHeight;
  private int waveWidth;
  private int waveNum;

  public WaveView(Context context) {
    this(context, null);
  }

  public WaveView(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public WaveView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);

    init(context);
  }

  private void init(Context context) {
    mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    mPaint.setColor(getResources().getColor(R.color.colorAccent));

    defaultHeight = (int) DisplayUtils.getInstance(context).dp2px(200);
    defaultWidth = (int) DisplayUtils.getInstance(context).dp2px(300);
    viewWidth = defaultWidth;
    viewHeight = defaultHeight;

    waveWidth = (int) DisplayUtils.getInstance(context).dp2px(20);
    waveHeight = (int) DisplayUtils.getInstance(context).dp2px(20);
    waveNum = (int) Math.ceil(viewHeight / waveWidth);
  }

  @Override protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);
    Path path = new Path();
    path.moveTo(waveHeight, 0);
    for (int i = 0; i < waveNum; i++) {
      path.rQuadTo(-waveHeight, waveWidth / 2, 0, waveWidth);
    }
    path.lineTo(getWidth() - waveHeight, getHeight());
    for (int i = 0; i < waveNum; i++) {
      path.rLineTo(waveHeight, -waveWidth / 2);
      path.rLineTo(-waveHeight, -waveWidth);
    }
    path.lineTo(waveHeight, 0);
    path.close();
    canvas.drawPath(path, mPaint);
  }

  @Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    int height = customMeasure(defaultHeight, heightMeasureSpec);
    int width = customMeasure(defaultWidth, widthMeasureSpec);
    viewHeight = Math.min(height, defaultHeight);
    viewWidth = Math.min(width, defaultWidth);
    waveNum = (int) Math.ceil(viewHeight / waveWidth);
    setMeasuredDimension(viewWidth, viewHeight);
  }

  private int customMeasure(int defaultSize, int measureSpec) {
    int result = defaultSize;
    int specSize = MeasureSpec.getSize(measureSpec);
    int specMode = MeasureSpec.getMode(measureSpec);
    switch (specMode) {
      case MeasureSpec.EXACTLY:
        result = specSize;
        break;
      case MeasureSpec.AT_MOST:
        result = Math.min(result, specSize);
      default:
        break;
    }
    return result;
  }
}
