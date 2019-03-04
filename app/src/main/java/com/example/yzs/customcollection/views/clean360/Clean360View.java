package com.example.yzs.customcollection.views.clean360;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import com.example.yzs.customcollection.R;

public class Clean360View extends View {
  private final int DEFAULT_WIDTH = 200;
  private final int DEFAULT_HEIGHT = 400;

  private float endX;
  private float endY;

  private float endPointY;
  private float endPointX;
  private float startPointX;
  private float startPointY;

  private BitmapFactory.Options opt;
  private Bitmap bitmapBody;
  private Bitmap bitmapBackground;

  private Paint mPaint;
  private Path path;
  private boolean drawRun = false;
  private ValueAnimator valueAnimator;
  private float currentPercent;

  public Clean360View(Context context) {
    this(context, null);
  }

  public Clean360View(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public Clean360View(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);

    mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    opt = new BitmapFactory.Options();
    opt.inScaled = false;
    opt.inPreferredConfig = Bitmap.Config.ARGB_8888;
    bitmapBody = BitmapFactory.decodeResource(getResources(), R.mipmap.mb)
        .copy(Bitmap.Config.ARGB_8888, true);
    bitmapBody.setDensity(getResources().getDisplayMetrics().densityDpi);
    bitmapBackground = BitmapFactory.decodeResource(getResources(), R.mipmap.t)
        .copy(Bitmap.Config.ARGB_8888, true);
    bitmapBackground.setDensity(getResources().getDisplayMetrics().densityDpi);

    valueAnimator = ValueAnimator.ofFloat(1, 0);
    valueAnimator.setDuration(500);
    valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
      @Override public void onAnimationUpdate(ValueAnimator animation) {
        currentPercent = (float) animation.getAnimatedValue();
        endY = endY * currentPercent;
        invalidate();
      }
    });
  }

  @Override protected void onDraw(Canvas canvas) {
    mPaint.setStyle(Paint.Style.STROKE);
    mPaint.setColor(Color.YELLOW);
    mPaint.setStrokeWidth(getWidth() / 50);
    mPaint.setStrokeCap(Paint.Cap.ROUND);
    path = new Path();
    path.moveTo(startPointX + getWidth() / 50, startPointY);
    Log.d(Clean360View.class.getSimpleName(), "endY:" + endX + ",getWidth:" + getWidth());
    if (endY >= getHeight() / 2) {
      path.quadTo(endX, endY, endPointX - getWidth() / 50, endPointY);
    } else {
      path.lineTo(endPointX - getWidth() / 50, endPointY);
    }
    canvas.drawPath(path, mPaint);
    if (drawRun) {
      if (currentPercent > 0) {
        canvas.drawBitmap(bitmapBody, endX - bitmapBody.getWidth() / 2,
            endY / 2 + getHeight() / 4 - bitmapBody.getHeight(), null);
      } else {
        endY = getHeight() / 2;
        canvas.drawBitmap(bitmapBody, endX - bitmapBody.getWidth() / 2,
            endY / 2 + getHeight() / 4 - bitmapBody.getHeight(), null);
      }
    } else {
      canvas.drawBitmap(bitmapBody, endX - bitmapBody.getWidth() / 2,
          endY / 2 + getHeight() / 4 - bitmapBody.getHeight(), null);
    }

    mPaint.setColor(Color.GRAY);
    mPaint.setStyle(Paint.Style.FILL);
    canvas.drawCircle(startPointX, startPointY, getWidth() / 50, mPaint);
    canvas.drawCircle(endPointX, endPointY, getWidth() / 50, mPaint);
  }

  @Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    int width = customMeasure(DEFAULT_WIDTH, widthMeasureSpec);
    int height = customMeasure(DEFAULT_HEIGHT, heightMeasureSpec);
    setMeasuredDimension(width, height);
  }

  @Override protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
    super.onLayout(changed, left, top, right, bottom);
    startPointX = getWidth() / 4;
    startPointY = getHeight() / 2;
    endPointX = getWidth() / 4 * 3;
    endPointY = getHeight() / 2;
    endX = getWidth() / 2;
    endY = getHeight() / 2;
  }

  private int customMeasure(int defaultSize, int measureSpec) {
    int specMode = MeasureSpec.getMode(measureSpec);
    int specSize = MeasureSpec.getSize(measureSpec);
    int result = defaultSize;
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
      case MotionEvent.ACTION_DOWN:
        drawRun = false;
        break;
      case MotionEvent.ACTION_MOVE:
        endY = event.getY();
        if (endY > getHeight() / 2) {
          invalidate();
        }
        break;
      case MotionEvent.ACTION_UP:
        if (endY > getHeight() / 2) {
          drawRun = true;
          valueAnimator.start();
        }
        break;
      default:
        break;
    }
    Log.d(Clean360View.class.getSimpleName(), "onTouchEvent:endX:" + endX + ",endY:" + endY);
    return true;
  }
}
