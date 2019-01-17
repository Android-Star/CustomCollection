package com.example.yzs.customcollection.views.dashboard;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import com.example.yzs.customcollection.utils.DisplayUtils;

public class DashBoardView extends View {
  private Paint mPaint;

  private int viewSize;
  private int defaultSize;

  private RectF bigCircleRectf;                     //最外部圆弧
  private RectF withColorRectf;                     //表示刻度的带颜色的环形
  private RectF smallCircleRectf;                   //靠近圆心小环形
  private Rect textBounds;

  private float progressMax;
  private float currentProgress;

  public DashBoardView(Context context) {
    this(context, null);
  }

  public DashBoardView(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public DashBoardView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init(context, attrs);
  }

  private void init(Context context, AttributeSet attrs) {
    mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    defaultSize = (int) DisplayUtils.getInstance(context).dp2px(200);
    viewSize = defaultSize;
    progressMax = 100;
    currentProgress = 0;
    textBounds = new Rect();
  }

  @Override protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);
    canvas.drawColor(Color.LTGRAY);
    mPaint.setColor(Color.BLUE);
    mPaint.setStyle(Paint.Style.STROKE);
    mPaint.setStrokeWidth(5);
    canvas.drawArc(bigCircleRectf, 150, 240, false, mPaint);

    canvas.save();
    canvas.rotate(120, getWidth() / 2, getHeight() / 2);
    for (int i = 0; i < 12; i++) {
      canvas.drawLine(getWidth() / 2, 0, getHeight() / 2, 20, mPaint);
      canvas.rotate(-20, getWidth() / 2, getHeight() / 2);
    }
    canvas.drawLine(getWidth() / 2, 0, getHeight() / 2, 20, mPaint);
    canvas.restore();

    mPaint.setColor(Color.BLUE);
    mPaint.setStrokeWidth(30);
    canvas.drawArc(withColorRectf, 140, currentProgress / progressMax * 260, false, mPaint);

    mPaint.setColor(Color.WHITE);
    canvas.drawArc(withColorRectf, 140 + currentProgress / progressMax * 260,
        260 - currentProgress / progressMax * 260, false, mPaint);

    mPaint.setStrokeWidth(7);
    canvas.drawArc(smallCircleRectf, 0, 360, false, mPaint);

    mPaint.setColor(Color.BLUE);
    mPaint.setStrokeWidth(2);
    canvas.drawCircle(getWidth() / 2, getHeight() / 2, 25, mPaint);

    mPaint.setColor(Color.WHITE);
    mPaint.setStrokeWidth(5);
    canvas.save();
    canvas.rotate(-130 + currentProgress / progressMax * 260, getWidth() / 2, getHeight() / 2);
    canvas.drawLine(getWidth() / 2, getHeight() / 2 - 10, getWidth() / 2, 60, mPaint);
    canvas.restore();

    mPaint.setColor(Color.BLUE);
    mPaint.setStyle(Paint.Style.FILL);
    canvas.drawRect(getWidth() / 2 - 20, getHeight() - 80, getWidth() / 2 + 20, getHeight() - 60,
        mPaint);

    String text = "已完成";
    mPaint.setColor(Color.WHITE);
    mPaint.setTextSize(18);
    mPaint.getTextBounds(text, 0, text.length(), textBounds);
    canvas.drawText(text, getWidth() / 2 - textBounds.width() / 2,
        getHeight() - 60 + textBounds.height() + 15, mPaint);
  }

  @Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    int width = customMeasure(defaultSize, widthMeasureSpec);
    int height = customMeasure(defaultSize, heightMeasureSpec);
    viewSize = Math.min(width, height);
    setMeasuredDimension(viewSize, viewSize);
  }

  @Override protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
    super.onLayout(changed, left, top, right, bottom);

    bigCircleRectf = new RectF(0, 0, getWidth(), getWidth());
    withColorRectf = new RectF(45, 45, getWidth() - 45, getHeight() - 45);
    smallCircleRectf = new RectF(getWidth() / 2 - 10, getHeight() / 2 - 10, getWidth() / 2 + 10,
        getHeight() / 2 + 10);
  }

  public void setProgress(int cur) {
    this.currentProgress = cur;
    invalidate();
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
}
