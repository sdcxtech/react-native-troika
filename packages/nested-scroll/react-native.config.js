module.exports = {
	dependency: {
		platforms: {
			android: {
				libraryName: 'nestedscrollview',
				componentDescriptors: [
					'NestedScrollViewComponentDescriptor',
					'NestedScrollViewContentComponentDescriptor',
					'NestedScrollViewHeaderComponentDescriptor',
					'NestedScrollViewChildComponentDescriptor',
				],
				cmakeListsPath: 'src/main/jni/CMakeLists.txt',
			},
		},
	},
};
