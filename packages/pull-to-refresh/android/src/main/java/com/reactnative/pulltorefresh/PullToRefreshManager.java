package com.reactnative.pulltorefresh;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.facebook.common.logging.FLog;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.ViewGroupManager;
import com.facebook.react.uimanager.annotations.ReactProp;
import com.scwang.smart.refresh.layout.api.RefreshFooter;
import com.scwang.smart.refresh.layout.api.RefreshHeader;

public class PullToRefreshManager extends ViewGroupManager<PullToRefresh> {
	public final static String REACT_CLASS = "PullToRefresh";

	@NonNull
	@Override
	public String getName() {
		return REACT_CLASS;
	}

	@NonNull
	@Override
	protected PullToRefresh createViewInstance(@NonNull ThemedReactContext reactContext) {
		PullToRefresh pullToRefresh = new PullToRefresh(reactContext);
		pullToRefresh.setEnableOverScrollBounce(false);
		pullToRefresh.setEnableOverScrollDrag(true);
		pullToRefresh.setEnableRefresh(false);
		pullToRefresh.setEnableLoadMore(false);
		return pullToRefresh;
	}

	@Override
	public void addView(@NonNull PullToRefresh parent, @NonNull View child, int index) {
		FLog.i("PullToRefresh", "addView index:%s, tag:%s, child:%s", index, child.getId(), child);
		if (child instanceof PullToRefreshHeader) {
			parent.setEnableRefresh(true);
			parent.setRefreshHeader((RefreshHeader) child);
			if (parent.isLaidOut()) {
				((PullToRefreshHeader) child).onInitialized(parent.getRefreshKernel(), 0, 0);
			}
		} else if (child instanceof PullToRefreshFooter) {
			parent.setEnableLoadMore(true);
			parent.setRefreshFooter((RefreshFooter) child);
			if (parent.isLaidOut()) {
				((PullToRefreshFooter) child).onInitialized(parent.getRefreshKernel(), 0, 0);
			}
		} else {
			parent.setRefreshContent(child);
		}
	}

	@Override
	public void removeViewAt(PullToRefresh parent, int index) {
		View child = parent.getChildAt(index);
		FLog.i("PullToRefresh", "removeViewAt index:%s, tag:%s child:%s", index, child.getId(), child);
		if (child instanceof PullToRefreshHeader) {
			parent.setEnableRefresh(false);
			parent.setOnRefreshListener(null);
		} else if (child instanceof PullToRefreshFooter) {
			parent.setEnableLoadMore(false);
			parent.setOnLoadMoreListener(null);
		} else {
			parent.removeView(parent.getRefreshKernel().getRefreshContent().getView());
		}
	}

	@ReactProp(name = "overflow")
	public void setOverflow(PullToRefresh view, @Nullable String overflow) {
		view.setOverflow(overflow);
	}

	@Override
	public boolean needsCustomLayoutForChildren() {
		return true;
	}
}
