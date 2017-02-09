package com.example.yedaye.firsttv.widgets;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.ImageButton;

/**
 * Created by wujamie on 17/1/19.
 */

public class BorderImageButton extends ImageButton {
    public BorderImageButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BorderImageButton(Context context) {
        super(context);
    }

    private boolean isFocus = false;
    private int sroke_width = 1;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint();

        if (isFocus) {
            paint.setColor((Color.parseColor("#E2E2E2")));
            paint.setDither(true);
            paint.setStrokeWidth(5);
            paint.setStyle(Paint.Style.STROKE);
            canvas.drawRect(0, 0, this.getWidth() - sroke_width, this.getHeight() - sroke_width, paint);
        }
    }

    @Override
    protected void onFocusChanged(boolean gainFocus, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
        isFocus = gainFocus;
    }
}
