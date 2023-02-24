import React, { Children, cloneElement, isValidElement, PropsWithChildren, ReactNode } from 'react'
import { requireNativeComponent, StyleProp, ViewProps, ViewStyle } from 'react-native'
import AppBarLayout, { AppBarLayoutProps } from '../AppBarLayout'

const CoordinatorLayoutAndroid = requireNativeComponent<any>('CoordinatorLayout')

interface CoordinatorLayoutProps {
  style?: StyleProp<ViewStyle>
}

/**
 *  若存在AppBarLayout且为Sticky布局时,
 *  则需查找其兄弟节点所设置的zIndex最大值（一次渲染仅进行一次查找）
 *  设置AppBarLayout属性zIndex大于其余兄弟节点，以确保能够优先处理手势事件
 */
function CoordinatorLayout({ style, children }: PropsWithChildren<CoordinatorLayoutProps>) {
  let siblingMaxZIndex: number | null = null
  return (
    <CoordinatorLayoutAndroid style={style}>
      {Children.map(children, element => {
        if (isValidElement(element) && element.type === AppBarLayout) {
          const { style, ...props } = element.props as AppBarLayoutProps
          if (
            typeof props.fixedHeight === 'number' ||
            typeof props.stickyHeaderBeginIndex === 'number'
          ) {
            siblingMaxZIndex = siblingMaxZIndex ?? getChildMaxZIndex(children)
            return cloneElement(element, {
              ...element.props,
              style: [{ zIndex: siblingMaxZIndex + 1 }, style ? style : undefined],
            })
          }
        }
        return element
      })}
    </CoordinatorLayoutAndroid>
  )
}

export default CoordinatorLayout

function getChildMaxZIndex(children: ReactNode) {
  let childMaxZIndex = 0
  Children.forEach(children, element => {
    if (isValidElement(element) && element.type !== AppBarLayout && element?.props.style) {
      childMaxZIndex = Math.max(getViewStyleMaxZIndex(element.props.style), childMaxZIndex)
    }
  })
  return childMaxZIndex
}

function getViewStyleMaxZIndex(styles: ViewProps['style']): number {
  if (Array.isArray(styles)) {
    return Math.max(
      ...styles.map(style => (style ? getViewStyleMaxZIndex(style as ViewProps['style']) : 0)),
    )
  }
  if (typeof styles === 'object' && typeof styles?.zIndex === 'number') {
    return styles.zIndex
  }
  return 0
}
