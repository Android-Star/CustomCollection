package com.example.yzs.customcollection.views.roadWheel;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.example.yzs.customcollection.R;

public class HomeRoadView extends LinearLayout implements View.OnTouchListener {
  private int count;
  private ImageView imageView;
  private float xDown;
  private float xMove;
  private float xUp;

  private ScrollListener listener;

  public HomeRoadView(Context context) {
    this(context, null);
  }

  public HomeRoadView(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public HomeRoadView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    setOrientation(HORIZONTAL);
    setOnTouchListener(this);
  }

  public void registerScrollListener(ScrollListener listener) {
    this.listener = listener;
  }

  public void setCount(int count) {
    this.count = count;
    if (count > getChildCount()) {
      int imageCount = count - getChildCount();
      for (int i = 0; i < imageCount; i++) {
        imageView = new ImageView(getContext());
        LayoutParams layoutParams = new LayoutParams(558, ViewGroup.LayoutParams.MATCH_PARENT);
        imageView.setLayoutParams(layoutParams);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        imageView.setImageResource(R.mipmap.road);
        addView(imageView);
      }
    }
  }

  @Override public boolean onTouch(View v, MotionEvent event) {
    switch (event.getAction()) {
      case MotionEvent.ACTION_DOWN:
        xDown = event.getRawX();
        break;
      case MotionEvent.ACTION_MOVE:
        xMove = event.getRawX();
        int distance = (int) Math.abs(xMove - xDown);
        if (listener != null) {
          listener.onScroll(distance);
        }
        break;
    }
    return false;
  }

  public interface ScrollListener {
    void onScroll(int distance);
  }
}
