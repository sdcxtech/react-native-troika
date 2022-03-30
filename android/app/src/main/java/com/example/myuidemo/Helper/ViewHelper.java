package com.example.myuidemo.Helper;

import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

public class ViewHelper {
    public static View findSpecificView(View view, Class[] viewClasses) {
        for (Class viewClass : viewClasses) {
            if (viewClass.isInstance(view)) {
                return view;
            }
        }
        if (view instanceof ViewGroup) {
            int childCount = ((ViewGroup) view).getChildCount();
            for (int i = 0; i < childCount; i++) {
                View childView = findSpecificView(((ViewGroup) view).getChildAt(i), viewClasses);
                if (childView != null) {
                    return childView;
                }
            }
        }
        return null;
    }
    public static boolean isViewWithinMotionEventBounds(View view, MotionEvent ev) {
        int xPoint = Math.round(ev.getRawX());
        int yPoint = Math.round(ev.getRawY());
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        int x = location[0];
        int y = location[1];
        int w = view.getWidth();
        int h = view.getHeight();
        return !(xPoint < x || xPoint > x + w || yPoint < y || yPoint > y + h);
    }
}
