package com.example.yzs.customcollection.views.banner;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import com.example.yzs.customcollection.R;
import java.util.Timer;
import java.util.TimerTask;

public class CustomBanner extends RelativeLayout implements View.OnTouchListener {
  private static final int SNAP_VELOCITY = 5;
  private int itemWidth;          //item宽度
  private int currentItemIndex;   //当前显示的item
  private int itemCount;          //所有item数量
  private int[] borders;           //保存各个itemLeftMargin的数组
  private int maxLeftMargin = 0;     //
  private int minLeftMargin = 0;
  private float xDown;
  private float xMove;
  private float xUp;
  private LinearLayout itemsLayout;
  private LinearLayout dotsLayout;
  private View firstItem;
  private MarginLayoutParams firstLayoutParams;
  private VelocityTracker velocityTracker;
  private Handler handler = new Handler(Looper.getMainLooper());

  public CustomBanner(Context context) {
    this(context, null);
  }

  public CustomBanner(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public CustomBanner(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  @Override protected void onLayout(boolean changed, int l, int t, int r, int b) {
    super.onLayout(changed, l, t, r, b);
    if (changed) {
      initItem();
      initDot();
    }
  }

  private void initDot() {
    dotsLayout = (LinearLayout) getChildAt(1);
    refreshDotLayout();
  }

  private void refreshDotLayout() {
    dotsLayout.removeAllViews();
    for (int i = 0; i < itemCount; i++) {
      LinearLayout.LayoutParams layoutParams =
          new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT);
      layoutParams.weight = 1;
      RelativeLayout relativeLayout = new RelativeLayout(getContext());
      ImageView imageView = new ImageView(getContext());
      if (i == currentItemIndex) {
        imageView.setImageResource(R.drawable.dot_selected);
      } else {
        imageView.setImageResource(R.drawable.dot_normal);
      }
      LayoutParams layoutParams1 =
          new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
      relativeLayout.addView(imageView, layoutParams1);
      dotsLayout.addView(relativeLayout, layoutParams);
    }
  }

  private void initItem() {
    itemWidth = getWidth();
    itemsLayout = (LinearLayout) getChildAt(0);
    itemCount = itemsLayout.getChildCount();
    borders = new int[itemCount];
    for (int i = 0; i < itemCount; i++) {
      borders[i] = -i * itemWidth;
      View childAt = itemsLayout.getChildAt(i);
      MarginLayoutParams layoutParams = (MarginLayoutParams) childAt.getLayoutParams();
      layoutParams.width = itemWidth;
      childAt.setLayoutParams(layoutParams);
      childAt.setOnTouchListener(this);
    }
    minLeftMargin = borders[itemCount - 1];
    firstItem = itemsLayout.getChildAt(0);
    firstLayoutParams = (MarginLayoutParams) firstItem.getLayoutParams();
  }

  @Override public boolean onTouch(View v, MotionEvent event) {
    createVelocityTracker(event);
    switch (event.getAction()) {
      case MotionEvent.ACTION_DOWN:
        xDown = event.getRawX();
        break;
      case MotionEvent.ACTION_MOVE:
        xMove = event.getRawX();
        int distance = (int) ((xMove - xDown) - (currentItemIndex * itemWidth));
        firstLayoutParams.leftMargin = distance;
        if (beAbleToScroll()) {
          firstItem.setLayoutParams(firstLayoutParams);
        }
        break;
      case MotionEvent.ACTION_UP:
        xUp = event.getRawX();
        if (beAbleToScroll()) {
          if (wantToScrollToNext()) {
            if (shouldScrollToNext()) {
              currentItemIndex++;
              scrollToNext();
              refreshDotLayout();
            } else {
              scrollToPre();
            }
          } else if (wantToScrollToPre()) {
            if (shouldScrollToPre()) {
              currentItemIndex--;
              scrollToPre();
              refreshDotLayout();
            } else {
              scrollToNext();
            }
          }
        }
        recyclerVelocityTracker();
        break;
      default:
        break;
    }
    return false;
  }

  private void scrollToNext() {
    new ScrollTask().execute(-20);
  }

  private void scrollToPre() {
    new ScrollTask().execute(20);
  }

  private void scrollToFirst() {
    new ScrollToFirstTask().execute(20 * itemCount);
  }

  private boolean shouldScrollToPre() {
    return xUp - xDown > getWidth() / 4 && getVelocity() > SNAP_VELOCITY;
  }

  private boolean shouldScrollToNext() {
    return xDown - xUp > getWidth() / 4 && getVelocity() > SNAP_VELOCITY;
  }

  private boolean wantToScrollToPre() {
    return xUp - xDown > 0;
  }

  private boolean wantToScrollToNext() {
    return xUp - xDown < 0;
  }

  private boolean beAbleToScroll() {
    return firstLayoutParams.leftMargin > minLeftMargin
        && firstLayoutParams.leftMargin < maxLeftMargin;
  }

  private void createVelocityTracker(MotionEvent event) {
    if (velocityTracker == null) {
      velocityTracker = VelocityTracker.obtain();
    }
    velocityTracker.addMovement(event);
  }

  private int getVelocity() {
    velocityTracker.computeCurrentVelocity(1000);
    int result = (int) velocityTracker.getXVelocity();
    return Math.abs(result);
  }

  private void recyclerVelocityTracker() {
    velocityTracker.recycle();
    velocityTracker = null;
  }

  private int findCrossBorder(int leftMargin) {
    int absLeftMargin = Math.abs(leftMargin);
    int closeBorder = borders[0];
    int closeMargin = Math.abs(Math.abs(closeBorder) - absLeftMargin);
    for (int border : borders) {
      int margin = Math.abs(Math.abs(border) - absLeftMargin);
      if (margin < closeMargin) {
        closeBorder = border;
        closeMargin = margin;
      }
    }
    return closeBorder;
  }

  private boolean isCrossBorder(int leftMargin, int speed) {
    for (int border : borders) {
      if (speed > 0) {
        if (leftMargin >= border && leftMargin - speed < border) {
          return true;
        }
      } else {
        if (leftMargin >= border && leftMargin + speed < border) {
          return true;
        }
      }
    }
    return false;
  }

  public void startAutoPlay() {
    new Timer().scheduleAtFixedRate(new TimerTask() {
      @Override public void run() {
        if (currentItemIndex == itemCount - 1) {
          currentItemIndex = 0;
          handler.post(new Runnable() {
            @Override public void run() {
              scrollToFirst();
              refreshDotLayout();
            }
          });
        } else {
          currentItemIndex++;
          handler.post(new Runnable() {
            @Override public void run() {
              scrollToNext();
              refreshDotLayout();
            }
          });
        }
      }
    }, 3000, 3000);
  }

  class ScrollTask extends AsyncTask<Integer, Integer, Integer> {

    @Override protected Integer doInBackground(Integer... speed) {
      int leftMargin = firstLayoutParams.leftMargin;
      while (true) {
        leftMargin += speed[0];
        if (isCrossBorder(leftMargin, speed[0])) {
          leftMargin = findCrossBorder(leftMargin);
          break;
        }
        publishProgress(leftMargin);
        try {
          Thread.sleep(5);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
      return leftMargin;
    }

    @Override protected void onProgressUpdate(Integer... values) {
      super.onProgressUpdate(values);
      firstLayoutParams.leftMargin = values[0];
      firstItem.setLayoutParams(firstLayoutParams);
    }

    @Override protected void onPostExecute(Integer integer) {
      super.onPostExecute(integer);
      firstLayoutParams.leftMargin = integer;
      firstItem.setLayoutParams(firstLayoutParams);
    }
  }

  class ScrollToFirstTask extends AsyncTask<Integer, Integer, Integer> {

    @Override protected Integer doInBackground(Integer... integers) {
      int leftMargin = firstLayoutParams.leftMargin;
      while (true) {
        leftMargin += integers[0];
        if (leftMargin > 0) {
          leftMargin = 0;
          break;
        }
        publishProgress(leftMargin);
        try {
          Thread.sleep(5);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
      return leftMargin;
    }

    @Override protected void onProgressUpdate(Integer... values) {
      super.onProgressUpdate(values);
      firstLayoutParams.leftMargin = values[0];
      firstItem.setLayoutParams(firstLayoutParams);
    }

    @Override protected void onPostExecute(Integer integer) {
      super.onPostExecute(integer);
      firstLayoutParams.leftMargin = integer;
      firstItem.setLayoutParams(firstLayoutParams);
    }
  }
}
