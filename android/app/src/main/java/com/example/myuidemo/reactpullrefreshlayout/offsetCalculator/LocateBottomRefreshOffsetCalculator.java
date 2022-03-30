/**
 * fork from https://github.com/Tencent/QMUI_Android
 */
package com.example.myuidemo.reactpullrefreshlayout.offsetCalculator;

public class LocateBottomRefreshOffsetCalculator implements RefreshOffsetCalculator {

    @Override
    public int calculateRefreshOffset(int refreshInitOffset, int refreshEndOffset, int refreshViewHeight, int targetCurrentOffset, int targetInitOffset, int targetRefreshOffset) {
        int distance = targetRefreshOffset / 2 + refreshViewHeight / 2;
        int max = targetCurrentOffset - refreshViewHeight;
        return Math.min(max, targetCurrentOffset - distance);
    }
}
