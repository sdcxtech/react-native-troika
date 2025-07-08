import React, {Fragment, useRef} from 'react';
import {withNavigationItem} from 'hybrid-navigation';
import {StyleSheet, TextInput, ScrollView} from 'react-native';
import {KeyboardInsetsView} from '@sdcx/keyboard-insets';
import {SafeAreaProvider, SafeAreaView} from 'react-native-safe-area-context';

function KeyboardAvoiding() {
    return (
        <SafeAreaProvider>
            <KeyboardInsetsView extraHeight={16} style={styles.flex1}>
                <ScrollView contentContainerStyle={styles.container}>
                    <SubmitToNextInputFragment />
                    <KeyboardInsetsView
                        explicitly
                        extraHeight={16}
                        style={styles.keyboard}>
                        <TextInput
                            style={styles.input}
                            placeholder={'test keyboard insets'}
                            textAlignVertical="center"
                        />
                    </KeyboardInsetsView>
                    {Array.from({length: 10}).map((item, index) => (
                        <TextInput
                            key={index + 10}
                            style={styles.input}
                            multiline
                            placeholder={`test keyboard insets ${index + 10}`}
                            textAlignVertical="top"
                        />
                    ))}
                </ScrollView>
                <KeyboardInsetsView
                    extraHeight={16}
                    style={[styles.keyboard, styles.backgroundLime]}>
                    <TextInput
                        style={styles.input}
                        placeholder={'test keyboard insets'}
                        textAlignVertical="center"
                    />
                    <SafeAreaView edges={['bottom']} />
                </KeyboardInsetsView>
            </KeyboardInsetsView>
        </SafeAreaProvider>
    );
}

const inputLength = 9;
function SubmitToNextInputFragment() {
    const inputRef = useRef<(TextInput | null)[]>([...Array(inputLength)]);
    const goNextInput = (index: number) => {
        if (index !== inputLength - 1) {
            inputRef.current[index + 1]?.focus();
        }
    };
    return (
        <Fragment>
            {Array.from({length: inputLength}).map((_, index) => (
                <TextInput
                    ref={ref => {
                        if (ref) {
                            inputRef.current[index] = ref;
                        }
                    }}
                    key={index}
                    style={styles.input}
                    placeholder={
                        index === inputLength - 1
                            ? 'submit'
                            : `current:${index} => submit and next`
                    }
                    textAlignVertical="center"
                    blurOnSubmit={index === inputLength - 1}
                    autoCorrect={false}
                    returnKeyType={index === inputLength - 1 ? 'done' : 'next'}
                    onSubmitEditing={() => goNextInput(index)}
                />
            ))}
        </Fragment>
    );
}

export default withNavigationItem({
    titleItem: {
        title: 'Keyboard Avoiding',
    },
})(KeyboardAvoiding);

const styles = StyleSheet.create({
    flex1: {
        flex: 1,
    },
    backgroundLime: {
        backgroundColor: 'lime',
    },
    container: {
        justifyContent: 'flex-start',
        alignItems: 'stretch',
        paddingBottom: 16,
    },
    input: {
        height: 40,
        marginHorizontal: 48,
        marginTop: 16,
        marginBottom: 0,
        paddingLeft: 8,
        paddingRight: 8,
        borderColor: '#cccccc',
        borderWidth: 1,
    },
    keyboard: {
        paddingBottom: 16,
        backgroundColor: 'bisque',
    },
});
