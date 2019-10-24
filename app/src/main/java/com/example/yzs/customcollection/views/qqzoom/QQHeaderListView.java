package com.example.yzs.customcollection.views.qqzoom;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.OvershootInterpolator;
import android.view.animation.Transformation;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.yzs.customcollection.R;

public class QQHeaderListView extends ListView {

    private int defaultHeadHeight;
    private int maxHeadHeight;
    private View headView;
    private View topView;
    private ImageView ivHead;

    public QQHeaderListView(Context context) {
        this(context, null);
    }

    public QQHeaderListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public QQHeaderListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        defaultHeadHeight = getResources().getDimensionPixelSize(R.dimen.default_header_height);
        maxHeadHeight = getResources().getDimensionPixelSize(R.dimen.max_header_height);
    }

    @Override
    public void addHeaderView(View v) {
        super.addHeaderView(v);
        this.headView = v;
    }

    public void setHeadImage(ImageView ivHead) {
        this.ivHead = ivHead;
    }

    public void setTopView(View topView) {
        this.topView = topView;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (ivHead.getHeight() > defaultHeadHeight) {
            headView.layout(0, 0, headView.getRight(), headView.getBottom());
            headView.requestLayout();
        }
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (topView != null) {
            int top = headView.getTop();
            int abs = Math.abs(top);
            if (abs > 255) {
                abs = 255;
            }
            topView.getBackground().setAlpha(abs);
        }
    }

    @Override
    protected boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY, int scrollRangeX, int scrollRangeY, int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {
        //过度滑动 deltaY为负向下滑动，为正向上滑动
        ivHead.getLayoutParams().height = Math.min(ivHead.getHeight() - deltaY, maxHeadHeight);
        ivHead.requestLayout();
        return super.overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX, scrollRangeY, maxOverScrollX, maxOverScrollY, isTouchEvent);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_UP) {
            ResetAnim resetAnim = new ResetAnim(defaultHeadHeight);
            resetAnim.setInterpolator(new OvershootInterpolator());
            resetAnim.setDuration(700);
            ivHead.startAnimation(resetAnim);
        }
        return super.onTouchEvent(ev);
    }

    class ResetAnim extends Animation {
        private int currentHeight;
        private int difHeight;

        public ResetAnim(int targetHeight) {
            this.currentHeight = ivHead.getHeight();
            this.difHeight = currentHeight - targetHeight;
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            super.applyTransformation(interpolatedTime, t);
            ivHead.getLayoutParams().height = (int) (currentHeight - difHeight * interpolatedTime);
            ivHead.requestLayout();
        }
    }
}
