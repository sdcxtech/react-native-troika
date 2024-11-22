package com.reactnative.modalx;

import android.app.Dialog;

import androidx.core.view.WindowCompat;

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
    }

}
