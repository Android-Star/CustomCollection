package com.example.yzs.customcollection.views.seekBar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.Transformation;
import com.example.yzs.customcollection.R;

public class ChengyuSeekBar extends View {
  private static final int DEFAULT_VIEW_HEIGHT = 28;
  private static final int DEFAULT_VIEW_WIDTH = 739;
  private static final int DEFAULT_BOTH_OFFSET = 2;
  private Paint mPaint;

  private int realHeight;
  private int realWidth;

  private int maxPoints;
  private float everyLength;

  private float circleX;
  private Rect textBounds;

  private int currentStep;

  private float percent;
  private float completeCircleRadius;
  private float smallDotRadius;
  private int cyTextSize;

  private int normalBgColor;
  private int completeBgColor;
  private int normalTextColor;
  private int completeTextColor;

  private SeekAnimation seekAnimation;

  private enum StepStatus {
    STATUS_ING, STEP_COMPLETE
  }

  private StepStatus stepStatus;

  private boolean isAdd;

  public ChengyuSeekBar(Context context) {
    this(context, null);
  }

  public ChengyuSeekBar(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public ChengyuSeekBar(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    initAttributes(context, attrs);
    init();
  }

  private void initAttributes(Context context, AttributeSet attrs) {
    TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ChengyuSeekBar);
    normalBgColor = typedArray.getColor(R.styleable.ChengyuSeekBar_normalBgColor,
        getResources().getColor(R.color.chengyu_seekbar_normal_bgcolor));
    completeBgColor = typedArray.getColor(R.styleable.ChengyuSeekBar_completeBgColor,
        getResources().getColor(R.color.chengyu_seekbar_complete_bgcolor));
    normalTextColor = typedArray.getColor(R.styleable.ChengyuSeekBar_normalTextColor,
        getResources().getColor(R.color.chengyu_seekbar_normal_textcolor));
    completeTextColor = typedArray.getColor(R.styleable.ChengyuSeekBar_completeTextColor,
        getResources().getColor(R.color.chengyu_seekbar_complete_textcolor));
  }

  private void init() {
    mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    mPaint.setTextSize(cyTextSize);

    realHeight = DEFAULT_VIEW_HEIGHT;
    realWidth = DEFAULT_VIEW_WIDTH;
    maxPoints = 10;
    textBounds = new Rect();
    currentStep = 6;
    stepStatus = StepStatus.STATUS_ING;
  }

  @Override protected void onDraw(Canvas canvas) {
    Log.d(ChengyuSeekBar.class.getSimpleName(), percent + "-------------");
    //绘制背景矩形
    if (isAdd) {
      mPaint.setColor(normalBgColor);
      canvas.drawRect(completeCircleRadius, getHeight() / 4f, getWidth() - DEFAULT_BOTH_OFFSET,
          getHeight() / 4 * 3f, mPaint);
    } else {
      mPaint.setColor(normalBgColor);
      canvas.drawRect(everyLength * (currentStep - 1) + getHeight() / 2f - DEFAULT_BOTH_OFFSET,
          getHeight() / 4f, getWidth() - DEFAULT_BOTH_OFFSET, getHeight() / 4 * 3f, mPaint);
    }
    //绘制大圆圈点
    for (int i = 1; i <= maxPoints; i++) {
      if (i == 1) {
        circleX = getHeight() / 2f;
      } else if (i == maxPoints) {
        circleX = everyLength * (i - 1) - getHeight() / 2f + DEFAULT_BOTH_OFFSET;
      } else {
        circleX = everyLength * (i - 1);
      }
      if (i < currentStep) {
        mPaint.setColor(completeBgColor);
        canvas.drawCircle(circleX, getHeight() / 2f, getHeight() / 2f, mPaint);
      } else if (i == currentStep) {
        mPaint.setColor(normalBgColor);
        canvas.drawCircle(circleX, getHeight() / 2f, getHeight() / 2f, mPaint);
        mPaint.setColor(completeBgColor);
        if (stepStatus == StepStatus.STATUS_ING && isAdd) {
          canvas.drawCircle(circleX, getHeight() / 2f, getHeight() / 2f, mPaint);
        }
      } else {
        mPaint.setColor(normalBgColor);
        canvas.drawCircle(circleX, getHeight() / 2f, getHeight() / 2f, mPaint);
      }
    }
    //绘制完成矩形和完成圆圈
    if (isAdd) {
      mPaint.setColor(completeBgColor);
      canvas.drawRect(completeCircleRadius, getHeight() / 4f,
          (everyLength * (currentStep - 1)) + completeCircleRadius + everyLength * percent,
          getHeight() / 4 * 3f, mPaint);
    } else {
      mPaint.setColor(completeBgColor);
      canvas.drawRect(DEFAULT_BOTH_OFFSET, getHeight() / 4f,
          everyLength * (currentStep - 1) - getHeight() / 2f + DEFAULT_BOTH_OFFSET + 5,
          getHeight() / 4 * 3f, mPaint);
    }
    //绘制数字
    for (int i = 1; i <= maxPoints; i++) {
      if (i == 1) {
        circleX = getHeight() / 2f;
      } else if (i == maxPoints) {
        circleX = everyLength * (i - 1) - getHeight() / 2f + DEFAULT_BOTH_OFFSET;
      } else {
        circleX = everyLength * (i - 1);
      }

      String text = i + "";
      mPaint.getTextBounds(text, 0, text.length(), textBounds);

      if (i < currentStep) {
        mPaint.setColor(completeTextColor);
        canvas.drawText(text, 0, text.length(), circleX - textBounds.width() / 2f,
            getHeight() / 2f + textBounds.height() / 2f, mPaint);
      } else if (i == currentStep) {
        mPaint.setColor(completeBgColor);
        if (stepStatus == StepStatus.STATUS_ING) {
          if (isAdd) {
            mPaint.setColor(completeTextColor);
            canvas.drawText(text, 0, text.length(), circleX - textBounds.width() / 2f,
                getHeight() / 2f + textBounds.height() / 2f, mPaint);
          } else {
            canvas.drawCircle(circleX, getHeight() / 2f, completeCircleRadius, mPaint);
            mPaint.setColor(completeTextColor);
            canvas.drawCircle(circleX, getHeight() / 2f, smallDotRadius, mPaint);
          }
        } else if (stepStatus == StepStatus.STEP_COMPLETE) {
          canvas.drawCircle(circleX, getHeight() / 2f, (getHeight() / 2f) * percent, mPaint);
          mPaint.setColor(completeTextColor);
          canvas.drawText(text, 0, text.length(), circleX - textBounds.width() / 2f,
              getHeight() / 2f + textBounds.height() / 2f, mPaint);
        }
      } else {
        mPaint.setColor(normalTextColor);
        canvas.drawText(text, 0, text.length(), circleX - textBounds.width() / 2f,
            getHeight() / 2f + textBounds.height() / 2f, mPaint);
      }
    }
  }

  @Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    int width = customMeasure(DEFAULT_VIEW_WIDTH, widthMeasureSpec);
    int height = customMeasure(DEFAULT_VIEW_HEIGHT, heightMeasureSpec);
    realWidth = Math.min(DEFAULT_VIEW_WIDTH, width);
    realHeight = Math.min(DEFAULT_VIEW_HEIGHT, height);
    setMeasuredDimension(realWidth, realHeight);
  }

  @Override public void layout(int l, int t, int r, int b) {
    super.layout(l, t, r, b);
    everyLength = getWidth() / (maxPoints - 1);
    Log.d(ChengyuSeekBar.class.getSimpleName(), "width:" + getWidth() + ",height:" + getHeight());
    completeCircleRadius = getHeight() / 28f * 11;
    smallDotRadius = getHeight() / 14f * 2;
    cyTextSize = (int) Math.ceil(getHeight() / 14f * 9);
    mPaint.setTextSize(cyTextSize);

    Log.d(ChengyuSeekBar.class.getSimpleName(),
        "字号:" + cyTextSize + ",completeCircleRadius:" + completeCircleRadius + ",smallDotRadius:"
            + smallDotRadius);
  }

  private int customMeasure(int defaultSize, int measureSpec) {
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

  private class SeekAnimation extends Animation {
    @Override protected void applyTransformation(float interpolatedTime, Transformation t) {
      super.applyTransformation(interpolatedTime, t);
      percent = interpolatedTime;
      Log.d(ChengyuSeekBar.class.getSimpleName(), percent + "" + isAdd);
      invalidate();
    }
  }

  public void add() {
    if (currentStep < maxPoints) {
      isAdd = true;
      stepStatus = StepStatus.STATUS_ING;
      seekAnimation = new SeekAnimation();
      seekAnimation.setDuration(1000);
      seekAnimation.setInterpolator(new LinearInterpolator());
      seekAnimation.setAnimationListener(new Animation.AnimationListener() {
        @Override public void onAnimationStart(Animation animation) {

        }

        @Override public void onAnimationEnd(Animation animation) {
          currentStep++;
          stepStatus = StepStatus.STATUS_ING;
          isAdd = false;
          invalidate();
        }

        @Override public void onAnimationRepeat(Animation animation) {

        }
      });
      this.startAnimation(seekAnimation);
    }
  }

  public void less() {
    if (currentStep > 1) {
      currentStep--;
      stepStatus = StepStatus.STATUS_ING;
      invalidate();
    }
  }

  public void complete() {
    percent = 0;
    stepStatus = StepStatus.STEP_COMPLETE;
    seekAnimation = new SeekAnimation();
    seekAnimation.setInterpolator(new LinearInterpolator());
    seekAnimation.setDuration(100);
    this.startAnimation(seekAnimation);
  }
}
