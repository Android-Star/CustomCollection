package com.example.yzs.customcollection.views.roadWheel;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.view.View;
import com.example.yzs.customcollection.R;

public class HomeWheelView extends View {

  private int defaultHeight = 60;
  private int defaultWidth = 1280;
  private int realWidth;
  private int realHeight;
  private float spaceBetweenWheel = 150f;
  private Bitmap wheelBitmap;
  private int scrollValue;

  public HomeWheelView(Context context) {
    this(context, null);
  }

  public HomeWheelView(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public HomeWheelView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    wheelBitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.wheel);
  }

  @Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    int width = customMeasure(defaultWidth, widthMeasureSpec);
    int height = customMeasure(defaultHeight, heightMeasureSpec);
    realWidth = Math.min(defaultWidth, width);
    realHeight = Math.min(defaultHeight, height);
    setMeasuredDimension(realWidth, realHeight);
  }

  @Override protected void onDraw(Canvas canvas) {
    int wheelCount = (int) Math.ceil(realWidth / (spaceBetweenWheel + wheelBitmap.getWidth()));
    float degrees = scrollValue % 558 / 558f * 360;
    for (int i = 0; i < wheelCount; i++) {
      drawRotateBitmap(canvas, wheelBitmap, -degrees,
          i * (spaceBetweenWheel + wheelBitmap.getWidth()),
          (realHeight - wheelBitmap.getHeight()) / 2);
    }
  }

  private void drawRotateBitmap(Canvas canvas, Bitmap bitmap, float rotation, float posX,
      float posY) {
    Matrix matrix = new Matrix();
    int offsetX = bitmap.getWidth() / 2;
    int offsetY = bitmap.getHeight() / 2;
    matrix.postTranslate(-offsetX, -offsetY);
    matrix.postRotate(rotation);
    matrix.postTranslate(posX + offsetX, posY + offsetY);
    canvas.drawBitmap(bitmap, matrix, null);
  }

  public void updateScrollValue(int scrollValue) {
    this.scrollValue = scrollValue;
    invalidate();
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
}
