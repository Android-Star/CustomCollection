package com.example.yzs.customcollection.views.Clock;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import com.example.yzs.customcollection.R;
import com.example.yzs.customcollection.utils.DisplayUtils;
import java.util.Calendar;

public class ClockView extends View {
  private Paint mPaint;

  private int defaultSize;
  private int viewSize;
  private Rect textRect;

  private float hourAngle, minuteAngle, secondAngle;
  private boolean isRunning = false;

  public ClockView(Context context) {
    this(context, null);
  }

  public ClockView(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public ClockView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);

    init(context, attrs);
  }

  private void init(Context context, AttributeSet attrs) {
    mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    defaultSize = (int) DisplayUtils.getInstance(context).dp2px(200);
    textRect = new Rect();
  }

  @Override protected void onDraw(Canvas canvas) {
    canvas.drawColor(getResources().getColor(R.color.colorPrimary));
    mPaint.setColor(Color.WHITE);
    canvas.drawCircle(getWidth() / 2, getWidth() / 2, getWidth() / 2, mPaint);
    //绘制刻度和数字
    for (int i = 0; i < 60; i++) {
      if (i % 5 == 0) {
        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(3);
        canvas.drawLine(getWidth() / 2, 5, getWidth() / 2, 25, mPaint);

        String text = (i == 0 ? 12 : i / 5) + "";
        mPaint.setStrokeWidth(1);
        mPaint.setTextSize(20);
        mPaint.getTextBounds(text, 0, text.length(), textRect);
        canvas.drawText(text, getWidth() / 2 - textRect.width() / 2, 28 + textRect.height(),
            mPaint);
      } else {
        mPaint.setColor(Color.RED);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(1);
        canvas.drawLine(getWidth() / 2, 5, getWidth() / 2, 15, mPaint);
      }
      canvas.rotate(6, getWidth() / 2, getHeight() / 2);
    }
    //绘制指针
    mPaint.setColor(Color.BLACK);
    mPaint.setStyle(Paint.Style.STROKE);
    mPaint.setStrokeCap(Paint.Cap.ROUND);

    canvas.save();
    canvas.rotate(hourAngle, getWidth() / 2, getWidth() / 2);
    mPaint.setStrokeWidth(12);
    canvas.drawLine(getWidth() / 2, getHeight() / 2 + 20, getWidth() / 2, getHeight() / 2 - 50,
        mPaint);
    canvas.restore();

    canvas.save();
    canvas.rotate(minuteAngle, getWidth() / 2, getWidth() / 2);
    mPaint.setStrokeWidth(8);
    canvas.drawLine(getWidth() / 2, getHeight() / 2 + 20, getWidth() / 2, getHeight() / 2 - 80,
        mPaint);
    canvas.restore();

    canvas.save();
    canvas.rotate(secondAngle, getWidth() / 2, getWidth() / 2);
    mPaint.setStrokeWidth(6);
    mPaint.setColor(Color.RED);
    canvas.drawLine(getWidth() / 2, getHeight() / 2 + 20, getWidth() / 2, getHeight() / 2 - 100,
        mPaint);
    canvas.restore();

    mPaint.setStyle(Paint.Style.FILL);
    canvas.drawCircle(getWidth() / 2, getHeight() / 2, 10, mPaint);
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

  public void start() {
    if (isRunning) {
      return;
    } else {
      isRunning = true;
      Calendar calendar = Calendar.getInstance();

      int hour = calendar.get(Calendar.HOUR);// 时
      int minute = calendar.get(Calendar.MINUTE);// 分
      int second = calendar.get(Calendar.SECOND);// 秒
      // 转过的角度
      hourAngle = (hour + (float) minute / 60) * 360 / 12;
      minuteAngle = (minute + (float) second / 60) * 360 / 60;
      secondAngle = second * 360 / 60;

      invalidate();
      postDelayed(new Runnable() {
        @Override public void run() {
          if (isRunning) {
            secondAngle += 6;
            secondAngle = secondAngle % 360;
            if (secondAngle == 0) {
              minuteAngle += 6;
              minuteAngle = minuteAngle % 360;
              if (minuteAngle == 0) {
                hourAngle += 6;
              }
            }
            invalidate();
            postDelayed(this, 1000);
          }
        }
      }, 1000);
    }
  }

  public void stop() {
    isRunning = false;
  }
}
