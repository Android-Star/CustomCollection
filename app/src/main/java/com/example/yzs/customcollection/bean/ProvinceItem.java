package com.example.yzs.customcollection.bean;


import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.util.Log;

public class ProvinceItem {
    private Path path;
    private Path dst;
    private int drawColor;
    private String title;
    private PathMeasure pathMeasure;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ProvinceItem(Path path) {
        this.path = path;
    }

    public void drawItem(Canvas canvas, Paint paint, boolean isSelect, boolean withColor, float percent) {
        if (withColor) {
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(drawColor);
            canvas.drawPath(path, paint);
            if (isSelect) {
                paint.setStyle(Paint.Style.STROKE);
                paint.setStrokeWidth(2);
                paint.setColor(0xFFD0E8F4);
                canvas.drawPath(path, paint);
            }
        } else {
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(2);
            paint.setColor(0xff000000);
            if (dst == null) {
                dst = new Path();
            }
            dst.reset();
            dst.lineTo(0, 0);
            if (pathMeasure == null) {
                pathMeasure = new PathMeasure();
            }
            pathMeasure.setPath(path, true);
            pathMeasure.getSegment(0, percent * pathMeasure.getLength(), dst, true);
            canvas.drawPath(dst, paint);
        }
    }

    public void setDrawColor(int drawColor) {
        this.drawColor = drawColor;
    }

    public boolean isTouch(float x, float y) {
        RectF rectF = new RectF();
        path.computeBounds(rectF, true);
        Region curRegin = new Region((int) rectF.left, (int) rectF.top, (int) rectF.right, (int) rectF.bottom);
        Region region = new Region();
        region.setPath(path, curRegin);
        return region.contains((int) x, (int) y);
    }
}
