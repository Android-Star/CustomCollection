package com.example.yzs.customcollection.views.scanView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import com.example.yzs.customcollection.R;

public class ScanView extends AppCompatImageView {
  private Bitmap lineBitmap;
  private float curTop;
  private boolean isRunning;

  public ScanView(Context context) {
    this(context, null);
  }

  public ScanView(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public ScanView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    lineBitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.scanline);
    setBackgroundResource(R.mipmap.book);
    setScaleType(ScaleType.CENTER_CROP);
  }

  @Override protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);
    Bitmap drawBitmap =
        Bitmap.createScaledBitmap(lineBitmap, getWidth(), lineBitmap.getHeight(), true);
    if (isRunning) {
      curTop += 3;
      if (curTop >= getMeasuredHeight() - lineBitmap.getHeight()) {
        curTop = -lineBitmap.getHeight();
      }
      canvas.drawBitmap(drawBitmap, 0, curTop, null);
      postDelayed(new Runnable() {
        @Override public void run() {
          invalidate();
        }
      }, 10);
    } else {
      canvas.drawBitmap(drawBitmap, 0, curTop, null);
    }
  }

  public void startScan() {
    isRunning = true;
    curTop = -lineBitmap.getHeight();
    invalidate();
  }

  public void stopScan() {
    isRunning = false;
    invalidate();
  }
}
