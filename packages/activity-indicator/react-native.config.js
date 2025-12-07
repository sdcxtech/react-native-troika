module.exports = {
	dependency: {
		platforms: {
			android: {
				libraryName: 'activityindicator',
				componentDescriptors: ['ActivityIndicatorComponentDescriptor'],
				cmakeListsPath: 'src/main/jni/CMakeLists.txt',
			},
		},
	},
};
