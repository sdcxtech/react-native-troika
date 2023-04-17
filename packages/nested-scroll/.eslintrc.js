module.exports = {
  root: true,
  extends: ['@react-native-community', 'plugin:prettier/recommended', 'prettier/react'],
  overrides: [
    {
      files: ['jest/*'],
      env: {
        jest: true,
      },
    },
  ],
  rules: {
    'no-shadow': 0,
    'no-bitwise': 0,
    'react-native/no-inline-styles': 0,
  },
  globals: {
    JSX: 'readonly',
  },
}