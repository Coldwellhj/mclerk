package com.eaosoft.util;

import android.view.View;

/**
 * Created by hj on 2017/4/24
 */

public interface NewPackageListItemClickHelp {
    /**
     * ���item��Ŀ��ĳ���ؼ��ص��ķ���

     *

     */
    void onEnableCardKind(View v);//���ã�����
    void onDeleteCardKind(View v);//ɾ��
    void onModifyCardKind(int position);//�޸�
}
