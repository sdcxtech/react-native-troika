# NestedScrollWebView

`NestedScrollWebView` 为 react-native-webview 提供纵向嵌套滚动支持。

因为社区的 [react-native-webview](https://github.com/react-native-webview/react-native-webview) 并没有完全遵循 Android [NestedScrolling API](https://developer.android.com/reference/androidx/core/view/NestedScrollingChild)，导致在嵌套滚动的场景下，WebView 无法正常滚动。

本库修补了这个缺陷。

## 版本兼容

| @sdcx/nested-scroll-webview | react-native-webview | RN 架构 |
| --------------------------- | -------------------- | ------- |
| 0.3                         | 11                   | 旧      |
| 0.4                         | 13                   | 旧      |
| 1.x                         | 13                   | 新      |

## Installation

```sh
yarn add react-native-webview
yarn add @sdcx/nested-scroll-webview
```

## Usage

因为是补丁，要确保 @sdcx/nested-scroll-webview 在 react-native-webview 之后装配。

1.  在 java 代码中，新建一个 `Package` 类，也可以使用项目现成的。

    在该类中，实现 `ReactPackage` 接口，返回 `RNCNestedScrollWebViewManager`。

    ```java
    package com.example.myuidemo;

    import com.reactnativecommunity.webview.RNCNestedScrollWebViewManager;
    import java.util.Arrays;

    public class MyUiPackage implements ReactPackage {
        @NonNull
        @Override
        public List<NativeModule> createNativeModules(@NonNull ReactApplicationContext reactContext) {
            return Collections.emptyList();
        }

        @NonNull
        @Override
        public List<ViewManager> createViewManagers(@NonNull ReactApplicationContext reactContext) {
            return Arrays.asList(
                    new RNCNestedScrollWebViewManager()
            );
        }
    }
    ```

2.  在 `MainApplication.java` 中，将该 `Package` 添加到 `getPackages()` 方法中。

    ```java
    @Override
    protected List<ReactPackage> getPackages() {
    	@SuppressWarnings("UnnecessaryLocalVariable")
    	List<ReactPackage> packages = new PackageList(this).getPackages();
    	packages.add(new MyUiPackage());
    	return packages;
    }
    ```

## 如果是新架构，需要额外添加一个补丁

参考 patch-package

react-native-webview+13.16.0.patch

```diff
diff --git a/node_modules/react-native-webview/android/src/newarch/com/reactnativecommunity/webview/RNCWebViewManager.java b/node_modules/react-native-webview/android/src/newarch/com/reactnativecommunity/webview/RNCWebViewManager.java
index 4709e28..3fd284f 100644
--- a/node_modules/react-native-webview/android/src/newarch/com/reactnativecommunity/webview/RNCWebViewManager.java
+++ b/node_modules/react-native-webview/android/src/newarch/com/reactnativecommunity/webview/RNCWebViewManager.java
@@ -63,6 +63,10 @@ public class RNCWebViewManager extends ViewGroupManager<RNCWebViewWrapper>
         return mRNCWebViewManagerImpl.createViewInstance(context);
     }

+	protected RNCWebViewWrapper createViewInstance(@NonNull ThemedReactContext context, RNCWebView webView) {
+		return mRNCWebViewManagerImpl.createViewInstance(context, webView);
+	}
+
     @Override
     @ReactProp(name = "allowFileAccess")
     public void setAllowFileAccess(RNCWebViewWrapper view, boolean value) {

```
