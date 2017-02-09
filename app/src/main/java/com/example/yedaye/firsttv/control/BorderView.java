package com.example.yedaye.firsttv.control;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import com.example.yedaye.firsttv.model.Effect;


/**
 * Created by wujamie on 17/1/9.
 */

public class BorderView<X extends View> implements ViewTreeObserver.OnGlobalFocusChangeListener, ViewTreeObserver.OnScrollChangedListener, ViewTreeObserver.OnGlobalLayoutListener, ViewTreeObserver.OnTouchModeChangeListener {
    private final String TAG = BorderView.class.getSimpleName();

    private ViewGroup mViewGroup;
    private Effect borderEffect;

    private X mView;
    private View mLastView;

    public BorderView(Context context) {
        this(context, null, 0);
    }

    public BorderView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BorderView(Context context, AttributeSet attrs, int defStyleAttr) {
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        borderEffect = new BorderEffect();
        mView = (X) new View(context, attrs, defStyleAttr);
    }

    public BorderView(X view) {
        this.mView = view;
        borderEffect = new BorderEffect();
    }

    public BorderView(X view, Effect effect) {
        this.mView = view;
        borderEffect = effect;
    }

    public BorderView(Context context, int resId) {
        this((X) LayoutInflater.from(context).inflate(resId, null, false));
    }

    public X getView() {
        return mView;
    }


    public void setBackgroundResource(int resId) {
        if (mView != null)
            mView.setBackgroundResource(resId);
    }

    @Override
    public void onScrollChanged() {
        borderEffect.onScrollChanged(mView, mViewGroup);
    }

    @Override
    public void onGlobalLayout() {
        borderEffect.onLayout(mView, mViewGroup);
    }

    @Override
    public void onTouchModeChanged(boolean isInTouchMode) {
        borderEffect.onTouchModeChanged(mView, mViewGroup, isInTouchMode);
    }

    @Override
    public void onGlobalFocusChanged(View oldFocus, View newFocus) {
        try {
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                if (oldFocus == null && mLastView != null) {
                    oldFocus = mLastView;
                }
            }
            if (borderEffect != null)
                borderEffect.onFocusChanged(mView, oldFocus, newFocus);
            mLastView = newFocus;

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }


    public <T extends BorderEffect> T getEffect() {
        return (T) borderEffect;
    }

    public void setEffect(Effect borderEffect) {
        this.borderEffect = borderEffect;
    }


    public void attachTo(ViewGroup viewGroup) {
        try {
            if (viewGroup == null) {
                if (mView.getContext() instanceof Activity) {
                    Activity activity = (Activity) mView.getContext();
                    viewGroup = (ViewGroup) activity.getWindow().getDecorView().getRootView();
                }
            }

            if (mViewGroup != viewGroup) {

                ViewTreeObserver viewTreeObserver = viewGroup.getViewTreeObserver();
                if (viewTreeObserver.isAlive() && mViewGroup == null) {
                    viewTreeObserver.addOnGlobalFocusChangeListener(this);
                    viewTreeObserver.addOnScrollChangedListener(this);
                    viewTreeObserver.addOnGlobalLayoutListener(this);
                    viewTreeObserver.addOnTouchModeChangeListener(this);
                }
                mViewGroup = viewGroup;
            }
            borderEffect.onAttach(mView, viewGroup);


        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void detach() {
        detachFrom(mViewGroup);
    }

    public void detachFrom(ViewGroup viewGroup) {
        try {
            if (viewGroup == mViewGroup) {
                ViewTreeObserver viewTreeObserver = mViewGroup.getViewTreeObserver();
                viewTreeObserver.removeOnGlobalFocusChangeListener(this);
                viewTreeObserver.removeOnScrollChangedListener(this);
                viewTreeObserver.removeOnGlobalLayoutListener(this);
                viewTreeObserver.removeOnTouchModeChangeListener(this);
                borderEffect.OnDetach(mView, viewGroup);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
