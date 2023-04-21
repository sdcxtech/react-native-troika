package com.reactnative.keyboardinsets;

import android.util.Log;
import android.view.View;

import androidx.core.graphics.Insets;
import androidx.core.view.WindowInsetsCompat;

import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.UIManagerHelper;
import com.facebook.react.uimanager.events.Event;
import com.facebook.react.uimanager.events.EventDispatcher;

public class KeyboardManualHandler {

    private final KeyboardInsetsView view;
    private final ThemedReactContext reactContext;

    public KeyboardManualHandler(KeyboardInsetsView view, ThemedReactContext reactContext) {
        this.view = view;
        this.reactContext = reactContext;
    }

    void onStart(View focusView, int keyboardHeight) {
        sendEvent(new KeyboardStatusChangedEvent(view.getId(), keyboardHeight, SystemUI.isImeVisible(view), true));
    }

    void onEnd(View focusView, int keyboardHeight) {
        sendEvent(new KeyboardStatusChangedEvent(view.getId(), keyboardHeight, SystemUI.isImeVisible(view), false));
    }

    void onApplyWindowInsets(WindowInsetsCompat insets, View focusView, int keyboardHeight) {
        sendEvent(new KeyboardStatusChangedEvent(view.getId(), keyboardHeight, SystemUI.isImeVisible(view), true));
        handleKeyboardTransition(insets, focusView);
        sendEvent(new KeyboardStatusChangedEvent(view.getId(), keyboardHeight, SystemUI.isImeVisible(view), false));
    }

    void handleKeyboardTransition(WindowInsetsCompat insets, View focusView) {
        Insets imeInsets = insets.getInsets(WindowInsetsCompat.Type.ime());
        Log.d("KeyboardInsets", "imeInsets.bottom:" + imeInsets.bottom);
        sendEvent(new KeyboardPositionChangedEvent(view.getId(), imeInsets.bottom));
    }

    private void sendEvent(Event<?> event) {
        EventDispatcher eventDispatcher = UIManagerHelper.getEventDispatcherForReactTag(reactContext, view.getId());
        eventDispatcher.dispatchEvent(event);
    }

}
