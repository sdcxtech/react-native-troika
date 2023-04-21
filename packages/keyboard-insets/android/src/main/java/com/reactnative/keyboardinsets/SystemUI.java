package com.reactnative.keyboardinsets;

import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SystemUI {

    public static boolean isImeVisible(@NonNull View view) {
        WindowInsetsCompat insetsCompat = ViewCompat.getRootWindowInsets(view);
        assert insetsCompat != null;
        return insetsCompat.isVisible(WindowInsetsCompat.Type.ime());
    }

    public static int imeHeight(@NonNull View view) {
        WindowInsetsCompat insetsCompat = ViewCompat.getRootWindowInsets(view);
        assert insetsCompat != null;
        return insetsCompat.getInsets(WindowInsetsCompat.Type.ime()).bottom;
    }

    public static EdgeInsets getEdgeInsetsForView(@NonNull View view) {
        ViewGroup root = (ViewGroup) view.getRootView();

        int windowHeight = root.getHeight();
        int windowWidth = root.getWidth();

        Rect offset = new Rect();
        view.getDrawingRect(offset);
        root.offsetDescendantRectToMyCoords(view, offset);

        int leftMargin = 0;
        int topMargin = 0;
        int rightMargin = 0;
        int bottomMargin = 0;

        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            leftMargin = lp.leftMargin;
            topMargin = lp.topMargin;
            rightMargin = lp.rightMargin;
            bottomMargin = lp.bottomMargin;
        }

        EdgeInsets insets = new EdgeInsets();
        insets.left = Math.max(offset.left - leftMargin, 0);
        insets.top = Math.max(offset.top - topMargin, 0);
        insets.right = Math.max(windowWidth - offset.right - rightMargin, 0);
        insets.bottom = Math.max(windowHeight - offset.bottom - bottomMargin, 0);
        return insets;
    }

}
