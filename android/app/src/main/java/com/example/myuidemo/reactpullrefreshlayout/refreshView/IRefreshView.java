/**
 * fork from https://github.com/Tencent/QMUI_Android
 */

package com.example.myuidemo.reactpullrefreshlayout.refreshView;

public interface IRefreshView {

    void onRefresh();

    void onPull(int currentRefreshViewOffset, int currentTargetViewOffset, int totalRefreshViewOffset, int totalTargetViewOffset);

    void onStop();
}
