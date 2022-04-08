package com.example.myuidemo.reactpullrefreshlayout.react;

import android.content.Context;
import android.view.View;
import android.webkit.WebView;
import android.widget.ScrollView;

import com.example.myuidemo.CoordinatorLayoutView;
import com.example.myuidemo.Helper.ViewHelper;
import com.example.myuidemo.reactpullrefreshlayout.PullRefreshLayout;
import com.reactnativecommunity.webview.NestedRNCWebView;

public class ReactPullRefreshLayout extends PullRefreshLayout {
    public ReactPullRefreshLayout(Context context) {
        super(context);
        setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
    }

    @Override
    public void addView(View child, int index, LayoutParams params) {
        if (child instanceof PullRefreshLayoutPlaceholderView) {
            ViewType viewType = ((PullRefreshLayoutPlaceholderView) child).getViewType();
            if (viewType == ViewType.REFRESH) {
                addRefreshView(child, index, params);
                return;
            }
            if (viewType == ViewType.LOAD_MORE) {
                addLoadMoreView(child, index, params);
                return;
            }
        }
        super.addView(child, index, params);
    }

    @Override
    public void onViewRemoved(View child) {
        if (child instanceof PullRefreshLayoutPlaceholderView) {
            ViewType viewType = ((PullRefreshLayoutPlaceholderView) child).getViewType();
            if (viewType == ViewType.REFRESH) {
                addDefaultRefreshView();
            } else if (viewType == ViewType.LOAD_MORE) {
                addDefaultLoadMoreView();
            }
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
        if (specificScrollView instanceof NestedRNCWebView) {
            NestedRNCWebView webView = (NestedRNCWebView) specificScrollView;
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
        Class[] classes = new Class[]{WebView.class, ScrollView.class, CoordinatorLayoutView.class};
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


