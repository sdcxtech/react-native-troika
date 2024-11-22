package com.reactnative.modalx;

import static android.view.WindowInsetsController.APPEARANCE_LIGHT_NAVIGATION_BARS;

import android.annotation.TargetApi;
import android.view.Window;
import android.view.WindowInsetsController;

@TargetApi(30)
public class SystemUI30 {
    public static void setNavigationBarStyle(Window window, boolean dark) {
        WindowInsetsController controller = window.getDecorView().getWindowInsetsController();
        assert controller != null;
        controller.setSystemBarsAppearance(dark ? APPEARANCE_LIGHT_NAVIGATION_BARS : 0, APPEARANCE_LIGHT_NAVIGATION_BARS);
    }

    public static boolean isNavigationBarStyleDark(Window window) {
        WindowInsetsController controller = window.getDecorView().getWindowInsetsController();
        assert controller != null;
        return (controller.getSystemBarsAppearance() & APPEARANCE_LIGHT_NAVIGATION_BARS) != 0;
    }
}
