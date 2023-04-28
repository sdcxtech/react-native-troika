import React, { useCallback, useEffect, useRef } from 'react'
import { NavigationProps, withNavigationItem } from 'hybrid-navigation'
import { StyleSheet, View } from 'react-native'
import { ObjectRect, ImageCropView, ImageCropViewRef } from '@sdcx/image-crop'
import Navigation from 'hybrid-navigation'

interface Props extends NavigationProps {
  fileUri: string
  cropStyle?: 'circular' | 'default'
  objectRect?: ObjectRect
}

function ImageCropPage({ fileUri, cropStyle, objectRect, sceneId, navigator }: Props) {
  const cropViewRef = useRef<ImageCropViewRef>(null)

  useEffect(() => {
    Navigation.setRightBarButtonItem(sceneId, {
      title: '确认',
      action: () => {
        cropViewRef.current && cropViewRef.current.crop()
      },
    })
  }, [sceneId])

  const onCropped = useCallback(
    async (uri: string) => {
      console.log('RN获取到剪切成功的uri = ', uri)
      await navigator.redirectTo('CropResultPage', {
        fileUri: uri,
      })
    },
    [navigator],
  )

  return (
    <View style={styles.container}>
      <ImageCropView
        style={styles.cropView}
        fileUri={fileUri}
        cropStyle={cropStyle}
        onCropped={onCropped}
        ref={cropViewRef}
        objectRect={objectRect}
      />
    </View>
  )
}

export default withNavigationItem({ titleItem: { title: 'CropPage' } })(ImageCropPage)

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'flex-start',
    alignItems: 'stretch',
  },
  cropView: {
    flex: 1,
  },
})
