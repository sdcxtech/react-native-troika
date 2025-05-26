import type {BubblingEventHandler, Double} from 'react-native/Libraries/Types/CodegenTypes';
import codegenNativeComponent from 'react-native/Libraries/Utilities/codegenNativeComponent'
import type {HostComponent, ViewProps} from 'react-native';
type KeyboardStatusChangedEvent = {
  height: Double;
  shown: boolean;
  transitioning: boolean;
}

type KeyboardPositionChangedEvent = {
  position: Double;
}
//TODO: CodeGen不支持枚举
//'auto' | 'manual'
export interface NativeProps extends ViewProps {
  mode?: string;
  extraHeight?: Double;
  explicitly?: boolean;
  onStatusChanged?: BubblingEventHandler<KeyboardStatusChangedEvent> | null;
  onPositionChanged?:  BubblingEventHandler<KeyboardPositionChangedEvent> | null;
}
/**
 * "componentProvider": {
                "RNCKeyboardInsetsView": "RNCKeyboardInsetsView"
            }
 *这个和package.json的codegenConfig字段挂钩,componentProvider的key是ts定义的名字即RNCKeyboardInsetsView,value是原生类的class名字
 */
export default codegenNativeComponent<NativeProps>(
    'RNCKeyboardInsetsView',
) as HostComponent<NativeProps>
