/**
 * fork from https://github.com/Tencent/QMUI_Android
 */

package com.example.myuidemo.reactpullrefreshlayout.offsetCalculator;

public class LocateTopRefreshOffsetCalculator implements RefreshOffsetCalculator {

    @Override
    public int calculateRefreshOffset(int refreshInitOffset, int refreshEndOffset, int refreshViewHeight, int targetCurrentOffset, int targetInitOffset, int targetRefreshOffset) {
        int refreshOffset;
        if (targetCurrentOffset >= targetRefreshOffset) {
            refreshOffset = refreshEndOffset;
        } else if (targetCurrentOffset <= targetInitOffset) {
            refreshOffset = refreshInitOffset;
        } else {
            float percent = (targetCurrentOffset - targetInitOffset) * 1.0f / (targetRefreshOffset - targetInitOffset);
            refreshOffset = (int) (refreshInitOffset + percent * (refreshEndOffset - refreshInitOffset));
        }
        return refreshOffset;
    }
}
