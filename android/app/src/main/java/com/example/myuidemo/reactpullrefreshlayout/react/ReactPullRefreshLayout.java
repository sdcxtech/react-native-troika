package com.example.myuidemo.reactpullrefreshlayout.react;

import android.content.Context;
import android.view.View;
import android.webkit.WebView;
import android.widget.ScrollView;

import com.example.myuidemo.Helper.ViewHelper;
import com.example.myuidemo.reactpullrefreshlayout.PullRefreshLayout;
import com.example.myuidemo.reactpullrefreshlayout.react.loadmoreview.LoadMorePlaceholderView;
import com.example.myuidemo.reactpullrefreshlayout.react.refreshview.RefreshDefaultView;
import com.example.myuidemo.reactpullrefreshlayout.react.refreshview.RefreshPlaceholderView;
import com.reactnativecommunity.webview.RNCNestedScrollWebView;

public class ReactPullRefreshLayout extends PullRefreshLayout {
    public ReactPullRefreshLayout(Context context) {
        super(context);
        setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
    }

    @Override
    public void addView(View child, int index, LayoutParams params) {
        if (child instanceof RefreshPlaceholderView) {
            addRefreshView(child, index, params);
        } else if (child instanceof LoadMorePlaceholderView) {
            addLoadMoreView(child, index, params);
        } else {
            super.addView(child, index, params);
        }
    }

    @Override
    protected View createDefaultRefreshView() {
        return new RefreshDefaultView(getContext());
    }

    @Override
    public void onViewRemoved(View child) {
        if (child instanceof RefreshPlaceholderView) {
            addDefaultRefreshView();
        } else if (child instanceof LoadMorePlaceholderView) {
            addDefaultLoadMoreView();
        }
        super.onViewRemoved(child);
    }

    @Override
    protected void setTargetViewToTop(View targetView) {
        View specificScrollView = findSpecificScrollableView(targetView);
        if (specificScrollView != null) {
            specificScrollView.postDelayed(() -> specificScrollView.scrollTo(0, 0), 0);
        } else {
            super.setTargetViewToTop(targetView);
        }
    }

    @Override
    protected void setTargetViewToBottom(View targetView) {
        View specificScrollView = findSpecificScrollableView(targetView);
        if (specificScrollView instanceof RNCNestedScrollWebView) {
            RNCNestedScrollWebView webView = (RNCNestedScrollWebView) specificScrollView;
            postDelayed(webView::scrollToBottom, 0);
        } else if (specificScrollView instanceof ScrollView) {
            ScrollView scrollView = (ScrollView) specificScrollView;
            postDelayed(() -> scrollView.fullScroll(View.FOCUS_DOWN), 0);
        } else {
            super.setTargetViewToBottom(targetView);
        }
    }

    @Override
    public boolean canChildScrollUp(View view) {
        View specificScrollView = findSpecificScrollableView(view);
        if (specificScrollView != null) {
            return specificScrollView.canScrollVertically(-1);
        }
        return super.canChildScrollUp(view);
    }

    private View findSpecificScrollableView(View view) {
        Class[] classes = new Class[]{WebView.class, ScrollView.class};
        return ViewHelper.findSpecificView(view, classes);
    }

    @Override
    public void requestLayout() {
        super.requestLayout();
        post(() -> {
            measure(
                    MeasureSpec.makeMeasureSpec(getWidth(), MeasureSpec.EXACTLY),
                    MeasureSpec.makeMeasureSpec(getHeight(), MeasureSpec.EXACTLY));
            layout(getLeft(), getTop(), getRight(), getBottom());
        });
    }
}


