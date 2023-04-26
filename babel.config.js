module.exports = {
  presets: ['module:metro-react-native-babel-preset'],
  plugins: [
    'react-native-reanimated/plugin',
    [
      'module-resolver',
      {
        root: './demo',
        cwd: 'babelrc',
        extensions: ['.ts', '.tsx', '.js', '.jsx'],
        alias: {
          assets: './demo/assets',
        },
      },
    ],
  ],
}
