package com.example.yzs.customcollection.views.bezier;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.PointFEvaluator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;
import com.example.yzs.customcollection.R;

public class QQBezierView extends View {
  /**
   * 气泡默认状态
   */
  public static final int BUBBLE_STATE_DEFAULT = 0x120;
  /**
   * 气泡连接状态
   */
  public static final int BUBBLE_STATE_CONNECT = 0x121;
  /**
   * 气泡断开状态
   */
  public static final int BUBBLE_STATE_APART = 0x122;
  /**
   * 气泡爆炸状态
   */
  public static final int BUBBLE_STATE_BURST = 0x123;
  /**
   * 固定气泡圆心
   */
  private PointF stillCenter;
  /**
   * 移动气泡圆心
   */
  private PointF moveCenter;
  /**
   * 两圆心距离
   */
  private int centerDistance;
  /**
   * 最大圆心距离（即达到断开时候的距离）
   */
  private int maxCenterDistance;
  /**
   * 保存当前气泡状态
   */
  private int currentBubbleState = BUBBLE_STATE_DEFAULT;
  /**
   * 默认气泡半径
   */
  private int bubbleRadius;
  /**
   * 静止气泡半径
   */
  private int stillBubbleRadius;
  /**
   * 气泡颜色
   */
  private int bubbleColor;
  /**
   * 气泡中的文字
   */
  private String bubbleStr;
  /**
   * 气泡文字颜色
   */
  private int bubbleTxtColor;
  /**
   * 气泡文字大小
   */
  private int bubbleTxtSize;
  /**
   * 绘制气泡文本的画笔
   */
  private Paint bubbleTxtPaint;
  /**
   * 绘制气泡爆炸图片的画笔
   */
  private Paint burstPaint;
  /**
   * 绘制气泡的画笔
   */
  private Paint bubblePaint;
  /**
   * 贝塞尔路径
   */
  private Path bubblePath;
  /**
   * 文本范围
   */
  private Rect textRect;
  /**
   * 爆炸范围
   */
  private Rect burstRect;
  /**
   * 盛放爆炸bitmap数组
   */
  private Bitmap[] burstBitmaps;
  /**
   * 手指触摸偏移量(可以被认定为拖拽)
   */
  private float MOVE_OFFSET;
  /**
   * 标记爆炸动画开始
   */
  private boolean isBurstStart = false;
  /**
   * 当前需要绘制的爆炸bitmap号
   */
  private int currentBurstPosition;
  /**
   * 爆炸资源数组
   */
  private int[] burstRes = new int[] {
      R.mipmap.burst_1, R.mipmap.burst_2, R.mipmap.burst_3, R.mipmap.burst_4, R.mipmap.burst_5
  };

  public QQBezierView(Context context) {
    this(context, null);
  }

  public QQBezierView(Context context, @Nullable AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public QQBezierView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.QQBezierView);
    bubbleRadius = (int) typedArray.getDimension(R.styleable.QQBezierView_bubbleRadius, 10);
    bubbleColor = typedArray.getColor(R.styleable.QQBezierView_bubbleColor, Color.RED);
    bubbleTxtColor = typedArray.getColor(R.styleable.QQBezierView_bubbleTxtColor, Color.GRAY);
    bubbleTxtSize = (int) typedArray.getDimension(R.styleable.QQBezierView_bubbleTxtSize, 10);
    bubbleStr = typedArray.getString(R.styleable.QQBezierView_bubbleTxt);
    typedArray.recycle();

    stillBubbleRadius = bubbleRadius;
    maxCenterDistance = 8 * bubbleRadius;

    MOVE_OFFSET = maxCenterDistance / 4;

    //抗锯齿
    bubblePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    bubblePaint.setColor(bubbleColor);
    bubblePaint.setStyle(Paint.Style.FILL);
    bubblePath = new Path();

    //文本画笔
    bubbleTxtPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    bubbleTxtPaint.setColor(bubbleTxtColor);
    bubbleTxtPaint.setTextSize(bubbleTxtSize);
    textRect = new Rect();

    //爆炸画笔
    burstPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    burstPaint.setFilterBitmap(true);
    burstRect = new Rect();
    burstBitmaps = new Bitmap[burstRes.length];
    for (int i = 0; i < burstRes.length; i++) {
      //将气泡爆炸的drawable转为bitmap
      Bitmap bitmap = BitmapFactory.decodeResource(getResources(), burstRes[i]);
      burstBitmaps[i] = bitmap;
    }
  }

  @Override protected void onSizeChanged(int w, int h, int oldw, int oldh) {
    super.onSizeChanged(w, h, oldw, oldh);
    initDefaultSize(w, h);
  }

  private void initDefaultSize(int width, int height) {
    //设置两气泡圆心初始坐标
    if (stillCenter == null) {
      stillCenter = new PointF(width / 2, height / 2);
    } else {
      stillCenter.set(width / 2, height / 2);
    }

    if (moveCenter == null) {
      moveCenter = new PointF(width / 2, height / 2);
    } else {
      moveCenter.set(width / 2, height / 2);
    }
    currentBubbleState = BUBBLE_STATE_DEFAULT;
  }

  @Override protected void onDraw(Canvas canvas) {
    //1.画相连的气泡状态
    if (currentBubbleState == BUBBLE_STATE_CONNECT) {
      //a.画静止气泡
      canvas.drawCircle(stillCenter.x, stillCenter.y, bubbleRadius - centerDistance / 8,
          bubblePaint);
      //b.画曲线path
      //计算控制点坐标（两圆心中点）
      int centerX = (int) ((stillCenter.x + moveCenter.x) / 2);
      int centerY = (int) ((stillCenter.y + moveCenter.y) / 2);
      //Theta角的正余弦
      float sinTheta = (moveCenter.y - stillCenter.y) / centerDistance;
      float cosTheta = (moveCenter.x - stillCenter.x) / centerDistance;

      //A
      float iBubStillStartX = stillCenter.x - stillBubbleRadius * sinTheta;
      float iBubStillStartY = stillCenter.y + stillBubbleRadius * cosTheta;
      //B
      float iBubMoveableEndX = moveCenter.x - bubbleRadius * sinTheta;
      float iBubMoveableEndY = moveCenter.y + bubbleRadius * cosTheta;
      //C
      float iBubMoveableStartX = moveCenter.x + bubbleRadius * sinTheta;
      float iBubMoveableStartY = moveCenter.y - bubbleRadius * cosTheta;
      //D
      float iBubStillEndX = stillCenter.x + stillBubbleRadius * sinTheta;
      float iBubStillEndY = stillCenter.y - stillBubbleRadius * cosTheta;
      bubblePath.reset();
      bubblePath.moveTo(iBubStillStartX, iBubStillStartY);
      bubblePath.quadTo(centerX, centerY, iBubMoveableEndX, iBubMoveableEndY);
      bubblePath.lineTo(iBubMoveableStartX, iBubMoveableStartY);
      bubblePath.quadTo(centerX, centerY, iBubStillEndX, iBubStillEndY);
      bubblePath.close();
      canvas.drawPath(bubblePath, bubblePaint);
    }

    //2.画拖拽的气泡和文字
    if (currentBubbleState != BUBBLE_STATE_BURST) {
      canvas.drawCircle(moveCenter.x, moveCenter.y, bubbleRadius, bubblePaint);
      bubbleTxtPaint.getTextBounds(bubbleStr, 0, bubbleStr.length(), textRect);
      canvas.drawText(bubbleStr, moveCenter.x - textRect.width() / 2,
          moveCenter.y + textRect.height() / 2, bubbleTxtPaint);
    }
    //3、画消失状态---爆炸动画

    if (isBurstStart) {
      burstRect.set((int) (moveCenter.x - bubbleRadius), (int) (moveCenter.y - bubbleRadius),
          (int) (moveCenter.x + bubbleRadius), (int) (moveCenter.y + bubbleRadius));

      canvas.drawBitmap(burstBitmaps[currentBurstPosition], null, burstRect, burstPaint);
    }
  }

  @SuppressLint("NewApi") @Override public boolean onTouchEvent(MotionEvent event) {
    switch (event.getAction()) {
      case MotionEvent.ACTION_DOWN:
        moveCenter.x = event.getX();
        moveCenter.y = event.getY();
        centerDistance =
            (int) Math.hypot(moveCenter.x - stillCenter.x, moveCenter.y - stillCenter.y);
        if (centerDistance > bubbleRadius + MOVE_OFFSET) {
          currentBubbleState = BUBBLE_STATE_DEFAULT;
        } else {
          currentBubbleState = BUBBLE_STATE_CONNECT;
        }
        break;
      case MotionEvent.ACTION_MOVE:
        moveCenter.x = event.getX();
        moveCenter.y = event.getY();
        centerDistance =
            (int) Math.hypot(moveCenter.x - stillCenter.x, moveCenter.y - stillCenter.y);
        if (currentBubbleState == BUBBLE_STATE_CONNECT) {
          if (centerDistance < maxCenterDistance) {
            stillBubbleRadius = bubbleRadius - centerDistance / 8;
          } else {
            currentBubbleState = BUBBLE_STATE_APART;
          }
        }
        invalidate();
        break;
      case MotionEvent.ACTION_UP:
        if (currentBubbleState == BUBBLE_STATE_CONNECT) {
          startBubbleRestAnim();
        } else if (currentBubbleState == BUBBLE_STATE_APART) {
          if (centerDistance < 2 * bubbleRadius) {
            startBubbleRestAnim();
          } else {
            startBubbleBurstAnim();
          }
        }
        break;
      default:
        break;
    }
    return true;
  }

  private void startBubbleBurstAnim() {
    //气泡改为消失状态
    currentBubbleState = BUBBLE_STATE_BURST;
    isBurstStart = true;
    //做一个int型属性动画，从0~mBurstDrawablesArray.length结束
    ValueAnimator anim = ValueAnimator.ofInt(0, burstRes.length);
    anim.setInterpolator(new LinearInterpolator());
    anim.setDuration(500);
    anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
      @Override public void onAnimationUpdate(ValueAnimator animation) {
        //设置当前绘制的爆炸图片index
        currentBurstPosition = (int) animation.getAnimatedValue();
        invalidate();
      }
    });
    anim.addListener(new AnimatorListenerAdapter() {
      @Override public void onAnimationEnd(Animator animation) {
        //修改动画执行标志
        isBurstStart = false;
        currentBubbleState = BUBBLE_STATE_DEFAULT;
        reset();
      }
    });
    anim.start();
  }

  @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP) private void startBubbleRestAnim() {
    ValueAnimator anim =
        ValueAnimator.ofObject(new PointFEvaluator(), new PointF(moveCenter.x, moveCenter.y),
            new PointF(stillCenter.x, stillCenter.y));

    anim.setDuration(200);
    anim.setInterpolator(new OvershootInterpolator(5f));
    anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
      @Override public void onAnimationUpdate(ValueAnimator animation) {
        moveCenter = (PointF) animation.getAnimatedValue();
        invalidate();
      }
    });
    anim.addListener(new AnimatorListenerAdapter() {
      @Override public void onAnimationEnd(Animator animation) {
        currentBubbleState = BUBBLE_STATE_DEFAULT;
      }
    });
    anim.start();
  }

  public void reset() {
    initDefaultSize(getWidth(), getHeight());

    invalidate();
  }
}
