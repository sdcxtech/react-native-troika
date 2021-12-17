package com.example.myuidemo;

import android.app.Application;

import com.facebook.common.logging.FLog;
import com.facebook.react.PackageList;
import com.facebook.react.ReactApplication;
import com.facebook.react.ReactNativeHost;
import com.facebook.react.ReactPackage;
import com.facebook.soloader.SoLoader;
import com.reactnative.hybridnavigation.ReactBridgeManager;

import java.util.List;

public class MainApplication extends Application implements ReactApplication {

	private final ReactNativeHost mReactNativeHost =
		new ReactNativeHost(this) {
			@Override
			public boolean getUseDeveloperSupport() {
				return BuildConfig.DEBUG;
			}

			@Override
			protected List<ReactPackage> getPackages() {
				@SuppressWarnings("UnnecessaryLocalVariable")
				List<ReactPackage> packages = new PackageList(this).getPackages();
				packages.add(new MyUiPackage());
				return packages;
			}

			@Override
			protected String getJSMainModuleName() {
				return "index";
			}
		};

	@Override
	public ReactNativeHost getReactNativeHost() {
		return mReactNativeHost;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		SoLoader.init(this, /* native exopackage */ false);
		ReactBridgeManager bridgeManager = ReactBridgeManager.get();
		bridgeManager.install(getReactNativeHost());
		FLog.setMinimumLoggingLevel(FLog.INFO);
	}
	
}