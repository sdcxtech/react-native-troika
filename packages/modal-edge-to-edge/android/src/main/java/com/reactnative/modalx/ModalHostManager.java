package com.reactnative.modalx;

import com.facebook.react.module.annotations.ReactModule;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.views.modal.ReactModalHostManager;
import com.facebook.react.views.modal.ReactModalHostView;

@ReactModule(name = ReactModalHostManager.REACT_CLASS)
public class ModalHostManager extends ReactModalHostManager {
    @Override
    public boolean canOverrideExistingModule() {
        return true;
    }

    @Override
    protected ReactModalHostView createViewInstance(ThemedReactContext reactContext) {
        return new ModalHostView(reactContext);
    }
    
}
