/**
 * fork from https://github.com/Tencent/QMUI_Android
 */
package com.example.myuidemo.reactpullrefreshlayout.offsetCalculator;

public class LocateBottomRefreshOffsetCalculator implements RefreshOffsetCalculator {

    @Override
    public int calculateRefreshOffset(int refreshInitOffset, int refreshEndOffset, int refreshViewHeight, int targetCurrentOffset, int targetInitOffset, int targetRefreshOffset) {
        return targetCurrentOffset - refreshViewHeight;
    }
}
