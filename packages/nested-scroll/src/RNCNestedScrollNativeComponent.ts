import type { Double} from 'react-native/Libraries/Types/CodegenTypes';
import codegenNativeComponent from 'react-native/Libraries/Utilities/codegenNativeComponent'
import type {HostComponent, ViewProps} from 'react-native';
import { DirectEventHandler } from 'react-native/Libraries/Types/CodegenTypes';

type ScrollEvent = {
    childSize: {
        height: Double;
        width: Double;
    }
    
}
export interface NativeProps extends ViewProps {
    onScrollingChildSizeChange?:DirectEventHandler<ScrollEvent> | null
}

export default codegenNativeComponent<NativeProps>(
    'RNCNestedScroll',
) as HostComponent<NativeProps>
