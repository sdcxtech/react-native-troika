package com.reactnative.keyboardinsets;

import android.view.View;
import android.view.ViewParent;

import androidx.annotation.NonNull;
import androidx.core.graphics.Insets;
import androidx.core.view.OnApplyWindowInsetsListener;
import androidx.core.view.WindowInsetsAnimationCompat;
import androidx.core.view.WindowInsetsCompat;

import com.facebook.common.logging.FLog;
import com.facebook.react.uimanager.ThemedReactContext;

import java.util.List;

public class KeyboardInsetsCallback extends WindowInsetsAnimationCompat.Callback implements OnApplyWindowInsetsListener {

    private final KeyboardInsetsView view;
    private final KeyboardAutoHandler autoHandler;
    private final KeyboardManualHandler manualHandler;

    public KeyboardInsetsCallback(KeyboardInsetsView view, ThemedReactContext reactContext) {
        super(WindowInsetsAnimationCompat.Callback.DISPATCH_MODE_CONTINUE_ON_SUBTREE);
        this.view = view;
        this.autoHandler = new KeyboardAutoHandler(view, reactContext);
        this.manualHandler = new KeyboardManualHandler(view, reactContext);
    }

    public View focusView;
    private boolean transitioning;
    private int keyboardHeight;

    @Override
    public void onPrepare(@NonNull WindowInsetsAnimationCompat animation) {
        transitioning = true;
    }

    @NonNull
    @Override
    public WindowInsetsAnimationCompat.BoundsCompat onStart(@NonNull WindowInsetsAnimationCompat animation, @NonNull WindowInsetsAnimationCompat.BoundsCompat bounds) {
        if (SystemUI.isImeVisible(view)) {
            focusView = view.findFocus();
        }

        if (!shouldHandleKeyboardTransition(focusView)) {
            return super.onStart(animation, bounds);
        }

        if (SystemUI.isImeVisible(view)) {
            keyboardHeight = SystemUI.imeHeight(view);
        }

        FLog.i("KeyboardInsets", "WindowInsetsAnimation.Callback onStart");
        if (view.isAutoMode()) {
            autoHandler.onStart(focusView, keyboardHeight);
        } else {
            manualHandler.onStart(focusView, keyboardHeight);
        }

        return super.onStart(animation, bounds);
    }

    @Override
    public void onEnd(@NonNull WindowInsetsAnimationCompat animation) {
        super.onEnd(animation);
        transitioning = false;

        if (!shouldHandleKeyboardTransition(focusView)) {
            return;
        }

        if (!SystemUI.isImeVisible(view)) {
            focusView = null;
        }

        FLog.i("KeyboardInsets", "WindowInsetsAnimation.Callback onEnd");
        if (view.isAutoMode()) {
            autoHandler.onEnd(focusView, keyboardHeight);
        } else {
            manualHandler.onEnd(focusView, keyboardHeight);
        }
    }

    @NonNull
    @Override
    public WindowInsetsCompat onProgress(@NonNull WindowInsetsCompat insets, @NonNull List<WindowInsetsAnimationCompat> runningAnimations) {
        if (!shouldHandleKeyboardTransition(focusView)) {
            return insets;
        }
        handleKeyboardTransition(insets);
        return WindowInsetsCompat.CONSUMED;
    }

    @Override
    public WindowInsetsCompat onApplyWindowInsets(View v, WindowInsetsCompat insets) {
        if (transitioning) {
            return insets;
        }

        if (focusView == null) {
            // Android 10 以下，首次弹出键盘时，不会触发 WindowInsetsAnimationCompat.Callback
            focusView = view.findFocus();
        }

        if (!shouldHandleKeyboardTransition(focusView)) {
            return insets;
        }

        Insets imeInsets = insets.getInsets(WindowInsetsCompat.Type.ime());
        FLog.i("KeyboardInsets", "onApplyWindowInsets imeInsets" + imeInsets);

        if (SystemUI.isImeVisible(view)) {
            keyboardHeight = SystemUI.imeHeight(view);
        }

        if (view.isAutoMode()) {
            View focusView = view.findFocus();
            if (focusView != null && focusView != this.focusView) {
                KeyboardInsetsView insetsView = findClosestKeyboardInsetsView(focusView);
                if (insetsView != view) {
                    focusView = null;
                }
            }
            this.focusView = focusView;
            autoHandler.onApplyWindowInsets(insets, focusView, keyboardHeight);
        } else {
            manualHandler.onApplyWindowInsets(insets, focusView, keyboardHeight);
        }

        return insets;
    }

    private void handleKeyboardTransition(WindowInsetsCompat insets) {
        if (view.isAutoMode()) {
            autoHandler.handleKeyboardTransition(insets, focusView);
        } else {
            manualHandler.handleKeyboardTransition(insets, focusView);
        }
    }

    private boolean shouldHandleKeyboardTransition(View focus) {
        if (focus != null) {
            KeyboardInsetsView insetsView = findClosestKeyboardInsetsView(focus);
            return insetsView == view;
        }
        return false;
    }

    private static KeyboardInsetsView findClosestKeyboardInsetsView(View focus) {
        ViewParent viewParent = focus.getParent();
        if (viewParent instanceof KeyboardInsetsView) {
            return (KeyboardInsetsView) viewParent;
        }

        if (viewParent instanceof View) {
            return findClosestKeyboardInsetsView((View) viewParent);
        }

        return null;
    }

}
