/**
 * Copyright (c) Facebook, Inc. and its affiliates.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

#import "RNWheelPicker.h"

#import <React/RCTConvert.h>
#import <React/RCTUtils.h>

@interface RNWheelPicker() <UIPickerViewDataSource, UIPickerViewDelegate>
@end

@implementation RNWheelPicker

- (instancetype)initWithFrame:(CGRect)frame {
    if ((self = [super initWithFrame:frame])) {
        _color = [UIColor blackColor];
        _font = [UIFont systemFontOfSize:21]; // TODO: selected title default should be 23.5
        _selectedIndex = NSNotFound;
        _textAlign = NSTextAlignmentCenter;
        _itemHeight = 32;
        self.delegate = self;
        self.dataSource = self;
        // Workaround for missing selection indicator lines (see https://stackoverflow.com/questions/39564660/uipickerview-selection-indicator-not-visible-in-ios10)
        [self selectRow:0 inComponent:0 animated:YES];
    }
    return self;
}

RCT_NOT_IMPLEMENTED(- (instancetype)initWithCoder:(NSCoder *)aDecoder)

- (void)setItems:(NSArray<NSDictionary *> *)items {
    _items = [items copy];
    [self setNeedsLayout];
}

- (void)setSelectedIndex:(NSInteger)selectedIndex {
    if (_selectedIndex != selectedIndex) {
        BOOL animated = _selectedIndex != NSNotFound; // Don't animate the initial value
        _selectedIndex = selectedIndex;
        dispatch_async(dispatch_get_main_queue(), ^{
            [self selectRow:selectedIndex inComponent:0 animated:animated];
        });
    }
}

#pragma mark - UIPickerViewDataSource protocol

- (NSInteger)numberOfComponentsInPickerView:(__unused UIPickerView *)pickerView {
    return 1;
}

- (NSInteger)pickerView:(__unused UIPickerView *)pickerView
numberOfRowsInComponent:(__unused NSInteger)component {
    return _items.count;
}

#pragma mark - UIPickerViewDelegate methods

- (NSString *)pickerView:(__unused UIPickerView *)pickerView
             titleForRow:(NSInteger)row
            forComponent:(__unused NSInteger)component {
    return _items[row];
}

- (CGFloat)pickerView:(__unused UIPickerView *)pickerView rowHeightForComponent:(NSInteger)__unused component {
    return _itemHeight;
}

- (UIView *)pickerView:(UIPickerView *)pickerView
            viewForRow:(NSInteger)row
          forComponent:(NSInteger)component
           reusingView:(UILabel *)label {
    if (!label) {
        label = [[UILabel alloc] initWithFrame:(CGRect){
            CGPointZero,
            {
                [pickerView rowSizeForComponent:component].width,
                [pickerView rowSizeForComponent:component].height,
            }
        }];
    }

    label.font = _font;
    // label.textColor =
    label.textAlignment = _textAlign;
    label.text = [self pickerView:pickerView titleForRow:row forComponent:component];
    
    return label;
}

- (void)layoutSubviews {
    [super layoutSubviews];
    NSArray<UIView *> *subviews = self.subviews;
    for (NSUInteger i = 0; i < subviews.count; i++) {
        if (i != 0) {
            UIView *curtain = [subviews objectAtIndex:i];
            curtain.hidden = YES;
        }
    }
}

- (void)pickerView:(__unused UIPickerView *)pickerView
      didSelectRow:(NSInteger)row inComponent:(__unused NSInteger)component {
    _selectedIndex = row;
    if (_onItemSelected && _items.count > (NSUInteger)row) {
         _onItemSelected(@{
            @"selectedIndex": @(row),
        });
    }
}

@end
