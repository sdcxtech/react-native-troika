#import "RNBottomSheetState.h"

RNBottomSheetState RNBottomSheetStateFromString(NSString *state) {
    if ([state isEqualToString:@"collapsed"]) {
        return RNBottomSheetStateCollapsed;
    }
    if ([state isEqualToString:@"expanded"]) {
        return RNBottomSheetStateExpanded;
    }
    if ([state isEqualToString:@"settling"]) {
        return RNBottomSheetStateSettling;
    }
    if ([state isEqualToString:@"dragging"]) {
        return RNBottomSheetStateDragging;
    }
    return RNBottomSheetStateHidden;
}


NSString* RNBottomSheetStateToString(RNBottomSheetState state) {
    if (state == RNBottomSheetStateCollapsed) {
        return @"collapsed";
    }
    
    if (state == RNBottomSheetStateExpanded) {
        return @"expanded";
    }
    
    if (state == RNBottomSheetStateSettling) {
        return @"settling";
    }
    
    if (state == RNBottomSheetStateDragging) {
        return @"dragging";
    }
    
    return @"hidden";
}
