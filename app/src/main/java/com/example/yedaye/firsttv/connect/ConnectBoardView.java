package com.example.yedaye.firsttv.connect;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;


import com.example.yedaye.firsttv.R;

import java.util.ArrayList;
import java.util.List;


public class ConnectBoardView extends View {

    /**
     * xCount x轴方向的图标数+1
     */
    protected static final int xCount = 20;
    /**
     * yCount y轴方向的图标数+1
     */
    protected static final int yCount = 10;
    /**
     * map 连连看游戏棋盘
     */
    protected int[][] map = new int[xCount][yCount];
    /**
     * iconSize 图标大小
     */
    protected int iconSize;
    /**
     * iconCounts 图标的数目
     */
    protected int iconCounts = 19;
    /**
     * icons 所有的图片
     */
    protected Bitmap[] icons = new Bitmap[iconCounts];

    /**
     * path 可以连通点的路径
     */
    private Point[] path = null;
    /**
     * selectedPoint 选中的图标
     */
    protected List<Point> selectedPoint = new ArrayList<Point>();
    /**
     * 当前聚焦的点
     */
    protected Point focusPoint = null;
    /**
     * 当前是否聚焦
     */
    protected boolean isFocus = false;
    /**
     * 是否刚打开游戏
     */
    protected boolean isFirstOpen = true;

    @Override
    protected void onFocusChanged(boolean gainFocus, int direction, Rect previouslyFocusedRect) {
        Log.e("jiangyou", "-------onFocusChanged--------" + gainFocus);
        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
        isFocus = gainFocus;
    }

    public ConnectBoardView(Context context, AttributeSet atts) {
        super(context, atts);

        calIconSize();

        Resources r = getResources();
        loadBitmaps(context, 1, r.getDrawable(R.drawable.fruit_01));
        loadBitmaps(context, 2, r.getDrawable(R.drawable.fruit_02));
        loadBitmaps(context, 3, r.getDrawable(R.drawable.fruit_03));
        loadBitmaps(context, 4, r.getDrawable(R.drawable.fruit_04));
        loadBitmaps(context, 5, r.getDrawable(R.drawable.fruit_05));
        loadBitmaps(context, 6, r.getDrawable(R.drawable.fruit_06));
        loadBitmaps(context, 7, r.getDrawable(R.drawable.fruit_07));
        loadBitmaps(context, 8, r.getDrawable(R.drawable.fruit_08));
        loadBitmaps(context, 9, r.getDrawable(R.drawable.fruit_09));
        loadBitmaps(context, 10, r.getDrawable(R.drawable.fruit_10));
        loadBitmaps(context, 11, r.getDrawable(R.drawable.fruit_11));
        loadBitmaps(context, 12, r.getDrawable(R.drawable.fruit_12));
        loadBitmaps(context, 13, r.getDrawable(R.drawable.fruit_13));
        loadBitmaps(context, 14, r.getDrawable(R.drawable.fruit_14));
        loadBitmaps(context, 15, r.getDrawable(R.drawable.fruit_15));
        loadBitmaps(context, 16, r.getDrawable(R.drawable.fruit_17));
        loadBitmaps(context, 17, r.getDrawable(R.drawable.fruit_18));
        loadBitmaps(context, 18, r.getDrawable(R.drawable.fruit_19));
    }

    /**
     * 计算图标的长宽
     */
    private void calIconSize() {
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity) this.getContext()).getWindowManager()
                .getDefaultDisplay().getMetrics(dm);
        if (dm.widthPixels / (xCount) > dm.heightPixels / (yCount)) {
            iconSize = dm.heightPixels / (yCount);
        } else {
            iconSize = dm.widthPixels / (xCount);
        }
    }

    /**
     * @param key 特定图标的标识
     * @param d   drawable下的资源
     */
    public void loadBitmaps(Context context, int key, Drawable d) {
        Bitmap bitmap = Bitmap.createBitmap(iconSize, iconSize, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        d.setBounds(0, 0, iconSize, iconSize);
        d.draw(canvas);
        icons[key] = bitmap;

    }

    @Override
    protected void onDraw(Canvas canvas) {
        Log.e("jiangyou", "-------onDraw--------");
        /**
         * 绘制连通路径，然后将路径以及两个图标清除
         */
        if (path != null && path.length >= 2) {
            for (int i = 0; i < path.length - 1; i++) {
                Paint paint = new Paint();
                paint.setColor(Color.CYAN);
                paint.setStyle(Paint.Style.STROKE);
                paint.setStrokeWidth(3);
                Point p1 = indextoScreen(path[i].x, path[i].y);
                Point p2 = indextoScreen(path[i + 1].x, path[i + 1].y);
                canvas.drawLine(p1.x + iconSize / 2, p1.y + iconSize / 2,
                        p2.x + iconSize / 2, p2.y + iconSize / 2, paint);
            }
            Point p = path[0];
            map[p.x][p.y] = 0;
            p = path[path.length - 1];
            map[p.x][p.y] = 0;
            selectedPoint.clear();
            path = null;
        }

        /**
         * 绘制棋盘的所有图标 当这个坐标内的值大于0时绘制
         */
        for (int x = 0; x < map.length; x += 1) {
            for (int y = 0; y < map[x].length; y += 1) {
                if (map[x][y] > 0) {
                    Point p = indextoScreen(x, y);
                    canvas.drawBitmap(icons[map[x][y]], p.x, p.y, null);
                }
            }
        }

        if (isFirstOpen) {
            isFirstOpen = false;
            selectedPoint.clear();
            focusPoint = new Point(1, 1);
        }

        /**
         * 绘制选中图标，当选中时图标放大显示
         */
        for (Point position : selectedPoint) {
            Point p = indextoScreen(position.x, position.y);
            Log.e("jiangyou", position.x + "-------onDraw          position --------"+position.y );
            if (map[position.x][position.y] >= 1) {
                canvas.drawBitmap(icons[map[position.x][position.y]],
                        null,
                        new Rect(p.x , p.y, p.x + iconSize , p.y + iconSize), null);

                Paint paint = new Paint();
                paint.setColor(Color.GREEN);
                paint.setDither(true);
                paint.setStrokeWidth(2);
                paint.setStyle(Paint.Style.STROKE);
                canvas.drawRect(p.x , p.y, p.x + iconSize , p.y + iconSize , paint);
            }
        }

        Point selectP = indextoScreen(focusPoint.x, focusPoint.y);
        Paint selectPaint = new Paint();
        selectPaint.setColor((Color.parseColor("#E2E2E2")));
        selectPaint.setDither(true);
        selectPaint.setStrokeWidth(2);
        selectPaint.setStyle(Paint.Style.STROKE);
        canvas.drawRect(selectP.x , selectP.y, selectP.x + iconSize , selectP.y + iconSize , selectPaint);

    }

    /**
     * @param path
     */
    public void drawLine(Point[] path) {
        this.path = path;
        this.invalidate();
    }

    /**
     * 工具方法
     *
     * @param x 数组中的横坐标
     * @param y 数组中的纵坐标
     * @return 将图标在数组中的坐标转成在屏幕上的真实坐标
     */
    public Point indextoScreen(int x, int y) {
        return new Point(x * iconSize, y * iconSize);
    }

    /**
     * 工具方法
     *
     * @param x 屏幕中的横坐标
     * @param y 屏幕中的纵坐标
     * @return 将图标在屏幕中的坐标转成在数组上的虚拟坐标
     */
    public Point screenToindex(int x, int y) {
        int ix = x / iconSize;
        int iy = y / iconSize;
        if (ix < xCount && iy < yCount) {
            return new Point(ix, iy);
        } else {
            return new Point(0, 0);
        }
    }
}

