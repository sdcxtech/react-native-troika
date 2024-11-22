package com.reactnative.modalx;

import android.app.Dialog;

import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.views.modal.ReactModalHostView;
import androidx.core.view.WindowCompat;

public class ModalHostView extends ReactModalHostView {
    public ModalHostView(ThemedReactContext context) {
        super(context);
    }

    @Override
    protected void showOrUpdate() {
        super.showOrUpdate();
        Dialog dialog = getDialog();
        if (dialog != null) {
            WindowCompat.setDecorFitsSystemWindows(dialog.getWindow(), false);
        }
    }

}