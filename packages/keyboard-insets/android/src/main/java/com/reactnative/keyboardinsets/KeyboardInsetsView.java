package com.reactnative.keyboardinsets;

import android.content.Context;
import android.view.View;

import com.facebook.react.views.view.ReactViewGroup;

public class KeyboardInsetsView extends ReactViewGroup {

    private String mode = "auto";

    private float extraHeight = 0.0f;

    public KeyboardInsetsView(Context context) {
        super(context);
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public boolean isAutoMode() {
        return this.mode.equals("auto");
    }

    public void setExtraHeight(float extraHeight) {
        this.extraHeight = extraHeight;
    }

    public float getExtraHeight() {
        return this.extraHeight;
    }


    @Override
    public void requestChildFocus(View child, View focused) {
        super.requestChildFocus(child, focused);
        requestApplyInsets();
    }
}
