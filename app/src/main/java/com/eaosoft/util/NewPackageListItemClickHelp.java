package com.eaosoft.util;

import android.view.View;

/**
 * Created by hj on 2017/4/24
 */

public interface NewPackageListItemClickHelp {
    /**
     * 点击item条目中某个控件回调的方法

     *

     */
    void onEnableCardKind(View v);//启用，禁用
    void onDeleteCardKind(View v);//删除
    void onModifyCardKind(int position);//修改
}
