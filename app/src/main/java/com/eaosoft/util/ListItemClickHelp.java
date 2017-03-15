package com.eaosoft.util;

/**
 * Created by hj on 2017/3/15 0015.
 */

public interface ListItemClickHelp {
    /**
     * 点击item条目中某个控件回调的方法

     * @param position item在ListView中所处的位置

     */
    void onClick( int position);
    void onReadTotalMoney();
}
