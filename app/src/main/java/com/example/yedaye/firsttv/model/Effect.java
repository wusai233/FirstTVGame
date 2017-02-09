package com.example.yedaye.firsttv.model;

import android.view.View;

/**
 * Created by wujamie on 17/1/9.
 */

public interface Effect {
    public void onFocusChanged(View target, View oldFocus, View newFocus);

    public void onScrollChanged(View target, View attachView);

    public void onLayout(View target, View attachView);

    public void onTouchModeChanged(View target, View attachView, boolean isInTouchMode);

    public void onAttach(View target, View attachView);

    public void OnDetach(View targe, View view);

    public <T> T toEffect(Class<T> t);
}
