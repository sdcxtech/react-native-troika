/**
 * Copyright (c) Facebook, Inc. and its affiliates.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

#import <UIKit/UIKit.h>

#import <React/UIView+React.h>

@interface RNWheelPicker : UIPickerView

@property (nonatomic, copy) NSArray<NSString *> *items;
@property (nonatomic, assign) NSInteger selectedIndex;

@property (nonatomic, strong) UIColor *textColorCenter;
@property (nonatomic, strong) UIColor *textColorOut;
@property (nonatomic, strong) UIFont *font;
@property (nonatomic, assign) NSTextAlignment textAlign;
@property (nonatomic, assign) CGFloat itemHeight;

@property (nonatomic, copy) RCTBubblingEventBlock onItemSelected;

@end
