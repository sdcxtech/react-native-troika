package com.reactnative.imagecrop;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.common.MapBuilder;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.ViewGroupManager;
import com.facebook.react.uimanager.annotations.ReactProp;

import java.util.Map;

public class RNImageCropViewManager extends ViewGroupManager<RNImageCropView> {
    private static final String REACT_CLASS = "RNImageCrop";

    private static final int COMMAND_CROP = 1;

    @NonNull
    @Override
    public String getName() {
        return REACT_CLASS;
    }

    @NonNull
    @Override
    protected RNImageCropView createViewInstance(@NonNull ThemedReactContext reactContext) {
        return new RNImageCropView(reactContext);
    }

    @ReactProp(name = "fileUri")
    public void setFileUri(RNImageCropView RNImageCropView, String fileUri) {
        RNImageCropView.setFileUri(fileUri);
    }

    @ReactProp(name = "cropStyle")
    public void setCropStyle(RNImageCropView RNImageCropView, String cropStyle) {
        RNImageCropView.setCropStyle(cropStyle);
    }

    @ReactProp(name = "objectRect")
    public void setObjectRect(RNImageCropView RNImageCropView, ReadableMap objectRect) {
        RNImageCropView.setObjectRect(objectRect);
    }

    @Override
    protected void onAfterUpdateTransaction(@NonNull RNImageCropView RNImageCropView) {
        super.onAfterUpdateTransaction(RNImageCropView);
        RNImageCropView.initProperties();
    }

    @Nullable
    @Override
    public Map<String, Integer> getCommandsMap() {
        return MapBuilder.of("crop", COMMAND_CROP);
    }

    @Override
    public void receiveCommand(@NonNull RNImageCropView root, String commandId, @Nullable ReadableArray args) {
        super.receiveCommand(root, commandId, args);
        int commandIdInt = Integer.parseInt(commandId);
        switch (commandIdInt) {
            case COMMAND_CROP:
                root.crop();
                break;
        }
    }

    public Map<String, Object> getExportedCustomBubblingEventTypeConstants() {
        String name = "phasedRegistrationNames";
        String bubbled = "bubbled";
        return MapBuilder.<String, Object>builder()
                .put("onCropped", MapBuilder.of(name, MapBuilder.of(bubbled, "onCropped")))
                .build();
    }
}
