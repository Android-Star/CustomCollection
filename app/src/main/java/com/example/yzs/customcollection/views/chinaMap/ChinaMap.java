package com.example.yzs.customcollection.views.chinaMap;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.graphics.PathParser;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.example.yzs.customcollection.R;
import com.example.yzs.customcollection.bean.ProvinceItem;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class ChinaMap extends View {
    private static final String TAG = "ChinaMap";
    private Paint paint;
    private List<ProvinceItem> provinceItems;
    private ProvinceItem selectProvice;
    private float scale = 1.0f;
    private RectF pathRectF;
    private int resId;

    private Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            if (provinceItems != null) {
                postInvalidate();
            }

        }
    };

    public void setResId(int resId) {
        if (this.resId == resId) {
            return;
        }
        this.resId = resId;
        LoadThread thread = new LoadThread();
        thread.start();
    }

    public ChinaMap(Context context) {
        this(context, null);
    }

    public ChinaMap(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ChinaMap(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        this.resId = R.raw.china;
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        LoadThread thread = new LoadThread();
        thread.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        canvas.scale(scale, scale);
        if (provinceItems != null) {
            for (ProvinceItem provinceItem : provinceItems) {
                provinceItem.drawItem(canvas, paint, provinceItem == selectProvice);
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        if (pathRectF != null) {
            float pathWidth = pathRectF.width();
            float pathHeight = pathRectF.height();
//            scale = Math.min(pathWidth / width, pathHeight / height);
            scale = pathWidth / width;
            Log.d(TAG, "pathWidth:" + pathWidth + ",pathHeight:" + pathHeight + ",scale:" + scale);
        }
        setMeasuredDimension(MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY));
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        handleTouch(event.getX(), event.getY());
        return super.onTouchEvent(event);
    }

    private void handleTouch(float x, float y) {
        if (provinceItems == null) {
            return;
        }
        for (ProvinceItem item : provinceItems) {
            if (item.isTouch(x, y)) {
                selectProvice = item;
                Toast.makeText(getContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
                break;
            }
        }
        if (selectProvice != null) {
            Log.d(TAG, "selectProvince is true");
            postInvalidate();
        }
    }

    public String getRandColorCode() {
        String r, g, b;
        Random random = new Random();
        r = Integer.toHexString(random.nextInt(256)).toUpperCase();
        g = Integer.toHexString(random.nextInt(256)).toUpperCase();
        b = Integer.toHexString(random.nextInt(256)).toUpperCase();

        r = r.length() == 1 ? "0" + r : r;
        g = g.length() == 1 ? "0" + g : g;
        b = b.length() == 1 ? "0" + b : b;

        return "#" + r + g + b;
    }

    class LoadThread extends Thread {
        @Override
        public void run() {

            super.run();
            try {
                List<ProvinceItem> list = new ArrayList<>();
                DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder documentBuilder = builderFactory.newDocumentBuilder();
                Document parse = documentBuilder.parse(getResources().openRawResource(resId));
                Element documentElement = parse.getDocumentElement();
                NodeList nodeList = documentElement.getElementsByTagName("path");
                int left = 0, top = 0, right = 0, bottom = 0;
                for (int i = 0; i < nodeList.getLength(); i++) {
                    Element item = (Element) nodeList.item(i);
                    String pathData = item.getAttribute("d");
                    String title = item.getAttribute("title");
                    @SuppressLint("RestrictedApi")
                    Path path = PathParser.createPathFromPathData(pathData);
                    ProvinceItem provinceItem = new ProvinceItem(path);
                    provinceItem.setDrawColor(Color.parseColor(getRandColorCode()));
                    provinceItem.setTitle(title);
                    list.add(provinceItem);
                    RectF currentRectF = new RectF();
                    path.computeBounds(currentRectF, true);
                    left = (int) Math.min(left, currentRectF.left);
                    top = (int) Math.min(top, currentRectF.top);
                    right = (int) Math.max(right, currentRectF.right);
                    bottom = (int) Math.max(bottom, currentRectF.bottom);
                    pathRectF = new RectF(left, top, right, bottom);
                }
                provinceItems = list;
                handler.sendEmptyMessage(0);
            } catch (Exception e) {
                e.printStackTrace();
            }


        }
    }
}
