package com.example.yzs.customcollection.views.quadLine;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import com.example.yzs.customcollection.utils.DisplayUtils;

public class QuadTestLine extends View {
  private Paint mPaint;
  private int defaultWidth;
  private int defaultHeight;

  private int realHeight;
  private int realWidth;

  private Path testPath;

  private float quadX, quadY;

  public QuadTestLine(Context context) {
    this(context, null);
  }

  public QuadTestLine(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public QuadTestLine(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init(context, attrs);
  }

  private void init(Context context, AttributeSet attrs) {
    mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    mPaint.setColor(Color.BLACK);
    mPaint.setStyle(Paint.Style.STROKE);
    mPaint.setStrokeWidth(5);

    defaultHeight = (int) DisplayUtils.getInstance(context).dp2px(200);
    defaultWidth = (int) DisplayUtils.getInstance(context).dp2px(300);
    realHeight = defaultHeight;
    realWidth = defaultWidth;
  }

  @Override protected void onDraw(Canvas canvas) {
    testPath = new Path();
    canvas.drawCircle(getWidth() / 3, getHeight() / 2, 5, mPaint);
    canvas.drawCircle(getWidth() / 3 * 2, getHeight() / 2, 5, mPaint);
    canvas.drawCircle(quadX, quadY, 5, mPaint);
    testPath.moveTo(getWidth() / 3, getHeight() / 2);
    testPath.quadTo(quadX, quadY, getWidth() / 3 * 2, getHeight() / 2);
    canvas.drawPath(testPath, mPaint);
  }

  @Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    int width = customMeasure(defaultWidth, widthMeasureSpec);
    int height = customMeasure(defaultHeight, heightMeasureSpec);
    realHeight = Math.min(height, defaultHeight);
    realWidth = Math.min(width, defaultWidth);
    setMeasuredDimension(realWidth, realHeight);
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
        break;
      default:
        break;
    }
    return result;
  }

  @Override public boolean onTouchEvent(MotionEvent event) {
    switch (event.getAction()) {
      case MotionEvent.ACTION_MOVE:
        quadX = event.getX();
        quadY = event.getY();
        invalidate();
        break;
      default:
        break;
    }
    return true;
  }
}
