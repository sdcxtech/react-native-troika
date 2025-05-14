import {PullToRefresh} from '@sdcx/pull-to-refresh';
import {LottiePullToRefreshHeader} from './LottiePullToRefreshHeader';
import {LottiePullToRefreshFooter} from './LottiePullToRefreshFooter';

PullToRefresh.setDefaultHeader(LottiePullToRefreshHeader);
PullToRefresh.setDefaultFooter(LottiePullToRefreshFooter);
