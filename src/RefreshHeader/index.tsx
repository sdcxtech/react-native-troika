import React from 'react'
import { RefreshControlProps, requireNativeComponent } from 'react-native'

interface RefreshHeaderProps extends RefreshControlProps {}

const NativeRefreshHeader = requireNativeComponent<RefreshHeaderProps>('RefreshHeader')

function RefreshHeader(props: RefreshHeaderProps) {
  return <NativeRefreshHeader {...props} />
}

export default RefreshHeader
