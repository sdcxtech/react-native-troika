package com.reactnative.imagecrop;

import com.facebook.react.bridge.ReadableMap;

public class ObjectRect {
    private int top;
    private int left;
    private int width;
    private int height;

    public ObjectRect(int top, int left, int width, int height) {
        this.top = top;
        this.left = left;
        this.width = width;
        this.height = height;
    }

    public static ObjectRect fromReadableMap(ReadableMap map) {
        int top = -1, left = -1, width = -1, height = -1;
        if (map.hasKey("top")) {
            top = map.getInt("top");
        }
        if (map.hasKey("left")) {
            left = map.getInt("left");
        }
        if (map.hasKey("width")) {
            width = map.getInt("width");
        }
        if (map.hasKey("height")) {
            height = map.getInt("height");
        }

        if (top != -1 && left != -1 && width != -1 && height != -1) {
            return new ObjectRect(top, left, width, height);
        }
        return null;
    }

    public int getTop() {
        return top;
    }

    public void setTop(int top) {
        this.top = top;
    }

    public int getLeft() {
        return left;
    }

    public void setLeft(int left) {
        this.left = left;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
