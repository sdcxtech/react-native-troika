package com.reactnative.nestedscroll;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.facebook.react.uimanager.LayoutShadowNode;
import com.facebook.react.uimanager.ReactStylesDiffMap;
import com.facebook.react.uimanager.StateWrapper;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.ViewGroupManager;
import com.facebook.react.uimanager.ViewManagerDelegate;
import com.facebook.react.uimanager.annotations.ReactProp;
import com.facebook.react.viewmanagers.NestedScrollViewManagerDelegate;
import com.facebook.react.viewmanagers.NestedScrollViewManagerInterface;

public class NestedScrollViewManager extends ViewGroupManager<NestedScrollView>
	implements NestedScrollViewManagerInterface<NestedScrollView> {

	private final NestedScrollViewManagerDelegate<NestedScrollView, NestedScrollViewManager> mDelegate
		= new NestedScrollViewManagerDelegate<>(this);

	@Override
	protected ViewManagerDelegate<NestedScrollView> getDelegate() {
		return mDelegate;
	}

	@NonNull
    @Override
    public String getName() {
        return "NestedScrollView";
    }

    @NonNull
    @Override
    protected NestedScrollView createViewInstance(@NonNull ThemedReactContext reactContext) {
        return new NestedScrollView(reactContext);
    }

    @Override
    public LayoutShadowNode createShadowNodeInstance() {
        return new NestedScrollViewShadowNode();
    }

    @Override
    public Class<? extends LayoutShadowNode> getShadowNodeClass() {
        return NestedScrollViewShadowNode.class;
    }

    @ReactProp(name = "overflow")
    public void setOverflow(NestedScrollView view, @Nullable String overflow) {
        view.setOverflow(overflow);
    }

	@Override
	public void setBounces(NestedScrollView view, boolean value) {

	}

	@Nullable
	@Override
	public Object updateState(@NonNull NestedScrollView view, ReactStylesDiffMap props, StateWrapper stateWrapper) {
		view.setStateWrapper(stateWrapper);
		return super.updateState(view, props, stateWrapper);
	}
}
