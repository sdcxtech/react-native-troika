import React from 'react';
import {StyleSheet} from 'react-native';
import {PullToRefreshFooterProps, PullToRefreshHeaderProps, PullToRefreshProps} from '../types';
import {DefaultPullToRefreshFooter} from '../Footer';
import {DefaultPullToRefreshHeader} from '../Header';
import {NativePullToRefresh} from './native';

export class PullToRefresh extends React.Component<PullToRefreshProps> {
  private static DefaultHeader: React.ComponentType<PullToRefreshHeaderProps> =
    DefaultPullToRefreshHeader;
  private static DefaultFooter: React.ComponentType<PullToRefreshFooterProps> =
    DefaultPullToRefreshFooter;

  static setDefaultHeader(Header: React.ComponentType<PullToRefreshHeaderProps>) {
    PullToRefresh.DefaultHeader = Header ?? DefaultPullToRefreshHeader;
  }

  static setDefaultFooter(Footer: React.ComponentType<PullToRefreshFooterProps>) {
    PullToRefresh.DefaultFooter = Footer ?? DefaultPullToRefreshFooter;
  }

  renderHeader() {
    const {onRefresh, refreshing, header} = this.props;

    if (header) {
      return header;
    }

    if (onRefresh) {
      return <PullToRefresh.DefaultHeader onRefresh={onRefresh} refreshing={!!refreshing} />;
    }

    return null;
  }

  renderFooter() {
    const {onLoadMore, loadingMore, noMoreData, footer} = this.props;
    if (footer) {
      return footer;
    }

    if (onLoadMore) {
      return (
        <PullToRefresh.DefaultFooter
          onRefresh={onLoadMore}
          refreshing={!!loadingMore}
          noMoreData={noMoreData}
        />
      );
    }

    return null;
  }

  render() {
    const {children, style} = this.props;
    return (
      <NativePullToRefresh style={[styles.fill, style]}>
        {this.renderHeader()}
        {children}
        {this.renderFooter()}
      </NativePullToRefresh>
    );
  }
}

const styles = StyleSheet.create({
  fill: {
    flex: 1,
  },
});
