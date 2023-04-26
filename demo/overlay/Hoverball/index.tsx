import React, { useRef, useState } from 'react'
import { AppRegistry, Pressable, StyleSheet, Text } from 'react-native'
import { statusBarHeight } from 'hybrid-navigation'
import { Overlay } from '@sdcx/overlay'

import Ball from './Ball'
import Menu from './Menu'

const menus = ['菜单1', '菜单2', '菜单3']

function Hoverball() {
  const [menuVisible, setMenuVisible] = useState(false)

  const left = useRef(8)
  const top = useRef(statusBarHeight())

  const anchor = {
    x: left.current,
    y: top.current,
    size: 64,
  }

  function renderAnchor() {
    return (
      <Pressable style={styles.ball} onPress={() => setMenuVisible(true)}>
        <Text>Menu</Text>
      </Pressable>
    )
  }

  function renderMenuItem(text: string, collapse: () => void) {
    return (
      <Pressable style={styles.item} key={text} onPress={collapse}>
        <Text style={styles.text}>{text}</Text>
      </Pressable>
    )
  }

  const renderMenuContent = (collapse: () => void) => {
    return <>{menus.map(text => renderMenuItem(text, collapse))}</>
  }

  if (menuVisible) {
    return (
      <Menu
        anchor={anchor}
        renderAnchor={renderAnchor}
        menuHeight={48 * menus.length}
        renderMenuContent={renderMenuContent}
        onClose={() => setMenuVisible(false)}
      />
    )
  }

  return (
    <Ball
      anchor={anchor}
      onOffsetChanged={(x, y) => {
        left.current = x
        top.current = y
      }}>
      {renderAnchor()}
    </Ball>
  )
}

const styles = StyleSheet.create({
  ball: {
    flex: 1,
    backgroundColor: 'red',
    justifyContent: 'center',
    alignItems: 'center',
  },
  item: {
    height: 48,
    justifyContent: 'center',
    borderBottomColor: '#DDDDDD',
    borderBottomWidth: StyleSheet.hairlineWidth,
    paddingHorizontal: 16,
  },
  text: {
    color: '#222222',
    fontSize: 17,
  },
})

function registerIfNeeded() {
  if (AppRegistry.getAppKeys().includes('__overlay_hoverball__')) {
    return
  }
  AppRegistry.registerComponent('__overlay_hoverball__', () => Hoverball)
}

function show() {
  registerIfNeeded()
  Overlay.show('__overlay_hoverball__', {}, { passThroughTouches: true })
}

function hide() {
  Overlay.hide('__overlay_hoverball__')
}

const Floating = { show, hide }

export default Floating
