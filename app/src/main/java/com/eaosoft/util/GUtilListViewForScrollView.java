package com.eaosoft.util;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;
/*
 �������ֻҪ��дonMeasure��������Ҫ�Ķ��ĵط����𷽷�һ�ٲ��ٴ��롢��xml�����к�Activty��ʹ�õ�ListView�ĳ�����Զ���ListView�����ˡ�
 ���������һ����Сë��������Ĭ����ʾ��������ListView����Ҫ�ֶ���ScrollView��������ˡ���������
sv = (ScrollView) findViewById(R.id.act_solution_4_sv);
sv.smoothScrollTo(0, 0);
 */
public class GUtilListViewForScrollView extends ListView 
{
	    public GUtilListViewForScrollView(Context context) {
	        super(context);
	    }
	    public GUtilListViewForScrollView(Context context, AttributeSet attrs) {
	        super(context, attrs);
	    }
	    public GUtilListViewForScrollView(Context context, AttributeSet attrs,
	        int defStyle) {
	        super(context, attrs, defStyle);
	    }
	    @Override
	    /**
	     * ��д�÷������ﵽʹListView��ӦScrollView��Ч��
	     */
	    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) 
	    {
	        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,MeasureSpec.AT_MOST);
	        super.onMeasure(widthMeasureSpec, expandSpec);
	    }
}