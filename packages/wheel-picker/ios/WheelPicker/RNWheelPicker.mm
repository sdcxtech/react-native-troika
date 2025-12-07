#import "RNWheelPicker.h"

@interface RNWheelPicker() <UIPickerViewDataSource, UIPickerViewDelegate>
@end

@implementation RNWheelPicker

- (instancetype)initWithFrame:(CGRect)frame {
    if ((self = [super initWithFrame:frame])) {
        _font = [UIFont systemFontOfSize:14];
        _textAlign = NSTextAlignmentCenter;
        _itemHeight = 36;
        _textColorOut = [UIColor blackColor];
        _textColorCenter = [UIColor blackColor];
        self.delegate = self;
        self.dataSource = self;
    }
    return self;
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

- (void)setItems:(NSArray<NSDictionary *> *)items {
    _items = [items copy];
    [self setNeedsLayout];
}

- (void)setItemHeight:(CGFloat)itemHeight {
    _itemHeight = itemHeight;
    [self setNeedsLayout];
}

- (void)setFont:(UIFont *)font {
    _font = font;
    [self setNeedsLayout];
}

- (void)setSelectedIndex:(NSInteger)selectedIndex {
    if (_selectedIndex != selectedIndex) {
        _selectedIndex = selectedIndex;
        dispatch_async(dispatch_get_main_queue(), ^{
            [self selectRow:selectedIndex inComponent:0 animated:NO];
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
    label.textColor = _textColorOut;
    label.textAlignment = _textAlign;
    label.text = [self pickerView:pickerView titleForRow:row forComponent:component];

    dispatch_after(dispatch_time(DISPATCH_TIME_NOW, (int64_t)(0 * NSEC_PER_SEC)), dispatch_get_main_queue(), ^{
        UILabel *label = (UILabel *)[pickerView viewForRow:row forComponent:component];
        label.textColor = self->_textColorCenter;
    });

    return label;
}

- (void)pickerView:(__unused UIPickerView *)pickerView
      didSelectRow:(NSInteger)row inComponent:(__unused NSInteger)component {
    _selectedIndex = row;
    if (_onItemSelected && _items.count > (NSUInteger)row) {
		_onItemSelected(row);
    }
}

@end
