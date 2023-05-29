package com.reactnative.activityindicator;

import android.content.Context;
import android.graphics.Color;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.uimanager.LayoutShadowNode;
import com.facebook.react.uimanager.PixelUtil;
import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.annotations.ReactProp;
import com.facebook.yoga.YogaMeasureFunction;
import com.facebook.yoga.YogaMeasureMode;
import com.facebook.yoga.YogaMeasureOutput;
import com.facebook.yoga.YogaNode;

public class ActivityIndicatorManager extends SimpleViewManager<ActivityIndicator> {

    public static final String REACT_CLASS = "ActivityIndicatorAndroid";

    @NonNull
    @Override
    public String getName() {
        return REACT_CLASS;
    }

    @NonNull
    @Override
    protected ActivityIndicator createViewInstance(@NonNull ThemedReactContext reactContext) {
        return new ActivityIndicator(reactContext);
    }

    @Override
    public Class getShadowNodeClass() {
        return ActivityIndicatorShadowNode.class;
    }

    @Override
    public LayoutShadowNode createShadowNodeInstance() {
        return new ActivityIndicatorShadowNode();
    }

    @ReactProp(name = "animating", defaultBoolean = true)
    public void setAnimating(ActivityIndicator view, boolean animating) {
        view.setAnimating(animating);
    }

    @ReactProp(name = "color", customType = "Color")
    public void setColor(ActivityIndicator view, @Nullable Integer color) {
        if (color != null) {
            view.setColor(color);
        } else {
            view.setColor(Color.parseColor("#999999"));
        }
    }

    @ReactProp(name = "size")
    public void setSize(ActivityIndicator view, @Nullable String size) {
        if (size != null && size.equals("large")) {
            view.setSize((int) (PixelUtil.toPixelFromDIP(36) + 0.5));
        } else {
            view.setSize((int) (PixelUtil.toPixelFromDIP(20) + 0.5));
        }
    }

    @Override
    public long measure(Context context, ReadableMap localData, ReadableMap props, ReadableMap state, float width, YogaMeasureMode widthMode, float height, YogaMeasureMode heightMode, @Nullable float[] attachmentsPositions) {
        String sizeStr = props.getString("size");
        if (sizeStr == null) {
            sizeStr = "small";
        }
        int size = sizeStr.equals("small") ? 20 : 36;
        return YogaMeasureOutput.make(size, size);
    }

    static class ActivityIndicatorShadowNode extends LayoutShadowNode implements YogaMeasureFunction {
        private String mSize;

        private ActivityIndicatorShadowNode() {
            initMeasureFunction();
        }

        private void initMeasureFunction() {
            setMeasureFunction(this);
        }

        @ReactProp(name = "size")
        public void setSize(@Nullable String size) {
            if (mSize == null || !mSize.equals(size)) {
                mSize = size;
                dirty();
            }
        }

        @Override
        public long measure(YogaNode node, float width, YogaMeasureMode widthMode, float height, YogaMeasureMode heightMode) {
            if (mSize == null) {
                mSize = "small";
            }
            int size = (int) (PixelUtil.toPixelFromDIP(mSize.equals("small") ? 20 : 36) + 0.5);
            return YogaMeasureOutput.make(size, size);
        }
    }


}
