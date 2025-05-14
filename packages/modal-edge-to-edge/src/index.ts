import {NativeModules, Platform} from 'react-native';

const EdgeToEdgeModule: SystemUIType = NativeModules.EdgeToEdgeModule;

interface SystemUIType {
  setNavigationBarColor(color: string): void;
  setNavigationBarStyle(style: 'dark' | 'light'): void;
}

const SystemUI: SystemUIType = {
  setNavigationBarStyle: (style: 'dark' | 'light') => {
    if (Platform.OS === 'android') {
      EdgeToEdgeModule.setNavigationBarStyle(style);
    }
  },
  setNavigationBarColor: (color: string) => {
    if (Platform.OS === 'android') {
      EdgeToEdgeModule.setNavigationBarColor(color);
    }
  },
};

export default SystemUI;
