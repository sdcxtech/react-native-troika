/**
 * fork from https://github.com/Tencent/QMUI_Android
 */

package com.example.myuidemo.reactpullrefreshlayout.offsetCalculator;

public interface RefreshOffsetCalculator {

    /**
     * 通过 targetView 的当前位置、targetView 的初始和刷新位置以及 refreshView 的初始与结束位置计算 RefreshView 的位置。
     *
     * @param refreshInitOffset   RefreshView 的初始 offset。
     * @param refreshEndOffset    刷新时 RefreshView 的 offset。
     * @param refreshViewHeight   RefreshView 的高度
     * @param targetCurrentOffset 下拉时 TargetView（ListView 或者 ScrollView 等）当前的位置。
     * @param targetInitOffset    TargetView（ListView 或者 ScrollView 等）的初始位置。
     * @param targetRefreshOffset 刷新时 TargetView（ListView 或者 ScrollView等）的位置。
     * @return RefreshView 当前的位置。
     */
    int calculateRefreshOffset(int refreshInitOffset, int refreshEndOffset, int refreshViewHeight,
                               int targetCurrentOffset, int targetInitOffset, int targetRefreshOffset);
}