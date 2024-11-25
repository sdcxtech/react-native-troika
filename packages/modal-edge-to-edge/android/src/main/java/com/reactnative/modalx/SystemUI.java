package com.reactnative.modalx;

import android.content.ContentResolver;
import android.os.Build;
import android.provider.Settings;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import androidx.annotation.NonNull;

public class SystemUI {

    public static boolean isGestureNavigationEnabled(ContentResolver contentResolver) {
        return Settings.Secure.getInt(contentResolver, "navigation_mode", 0) == 2;
    }

    public static void setNavigationBarColor(final Window window, int color) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            return;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            window.setNavigationBarContrastEnforced(false);
        }

        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setNavigationBarColor(color);
    }

    public static void setNavigationBarStyle(Window window, boolean dark) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            SystemUI30.setNavigationBarStyle(window, dark);
            return;
        }

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            return;
        }

        View decorView = window.getDecorView();
        int systemUi = decorView.getSystemUiVisibility();
        if (dark) {
            systemUi |= View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR;
        } else {
            systemUi &= ~View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR;
        }
        decorView.setSystemUiVisibility(systemUi);
    }
    
    public static void setStatusBarStyle(@NonNull Window window, boolean dark) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            SystemUI30.setStatusBarStyle(window, dark);
            return;
        }

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return;
        }

        View decorView = window.getDecorView();
        int systemUi = decorView.getSystemUiVisibility();
        if (dark) {
            systemUi |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
        } else {
            systemUi &= ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
        }
        decorView.setSystemUiVisibility(systemUi);
    }

    public static boolean isStatusBarStyleDark(@NonNull Window window) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            return SystemUI30.isStatusBarStyleDark(window);
        }

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return false;
        }

        return (window.getDecorView().getSystemUiVisibility() & View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR) != 0;
    }


}
