package com.reactnative.modalx;

import android.app.Dialog;
import android.app.Activity;

import androidx.core.view.WindowCompat;

import com.facebook.react.bridge.ReactContext;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.views.modal.ReactModalHostView;

public class ModalHostView extends ReactModalHostView {
    public ModalHostView(ThemedReactContext context) {
        super(context);
    }

    @Override
    protected void showOrUpdate() {
        super.showOrUpdate();
        Dialog dialog = getDialog();
        if (dialog != null && SystemUI.isGestureNavigationEnabled(dialog.getContext().getContentResolver())) {
            WindowCompat.setDecorFitsSystemWindows(dialog.getWindow(), false);
        }
        
        if (dialog != null) {
            SystemUI.setStatusBarStyle(dialog.getWindow(), isDark());
        }
    }
    
    private boolean isDark() {
        Activity activity = ((ReactContext) getContext()).getCurrentActivity();
        // fix activity NPE
        if (activity == null) {
            return true;
        }
        return SystemUI.isStatusBarStyleDark(activity.getWindow());
    }
    
}
