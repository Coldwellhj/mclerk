package com.eaosoft.util;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;
/*
 这个方法只要重写onMeasure方法、需要改动的地方比起方法一少不少代码、在xml布局中和Activty中使用的ListView改成这个自定义ListView就行了、
 这个方法有一个的小毛病、就是默认显示的首项是ListView、需要手动把ScrollView滚动至最顶端、代码如下
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
	     * 重写该方法、达到使ListView适应ScrollView的效果
	     */
	    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) 
	    {
	        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,MeasureSpec.AT_MOST);
	        super.onMeasure(widthMeasureSpec, expandSpec);
	    }
}