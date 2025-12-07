module.exports = {
	dependency: {
		platforms: {
			android: {
				libraryName: 'pulltorefresh',
				componentDescriptors: [
					'PullToRefreshComponentDescriptor',
					'PullToRefreshHeaderComponentDescriptor',
					'PullToRefreshFooterComponentDescriptor',
				],
				cmakeListsPath: 'src/main/jni/CMakeLists.txt',
			},
		},
	},
};
