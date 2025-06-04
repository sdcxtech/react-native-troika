import type {DirectEventHandler, Double} from 'react-native/Libraries/Types/CodegenTypes';
import codegenNativeComponent from 'react-native/Libraries/Utilities/codegenNativeComponent'
import type {HostComponent, ViewProps} from 'react-native';
type ScrollEvent = {
  contentOffset: {
    y: Double;
    x: Double;
  };
}

export interface NativeProps extends ViewProps {
  stickyHeight?: Double;
  stickyHeaderBeginIndex?: Double;
  onScroll?: DirectEventHandler<ScrollEvent> | null;
}

export default codegenNativeComponent<NativeProps>(
    'RNCNestedScrollHeader',
) as HostComponent<NativeProps>
