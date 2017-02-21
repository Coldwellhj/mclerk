package com.eaosoft.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * Created by 折扣卷布局 on 2016/5/17.
 * 思路: 
上面的图片其实和普通的Linearlayout，RelativeLayout一样，只是上下两边多了类似于半圆锯齿的形状。那么只需要处理不同地方。可以在上下两条线上画一个个白色的小圆来实现这种效果。

假如我们上下线的半圆以及半圆与半圆之间的间距是固定的，那么不同尺寸的屏幕肯定会画出不同数量的半圆，那么我们只需要根据控件的宽度来获取能画的半圆数。

大家观察图片，很容易发现，圆的数量总是圆间距数量－1，也就是，假设圆的数量是circleNum,那么圆间距就是circleNum+1。

所以我们可以根据这个计算出circleNum. circleNum = (int) ((w-gap)/(2*radius+gap)); 这里gap就是圆间距，radius是圆半径，w是view的宽。
 */
public class GCouponsView extends LinearLayout 
{

    private Paint mPaint;
    /**
     * 圆间距
     */
    private float gap = 8;
    /**
     * 半径
     */
    private float radius = 10;
    /**
     * 圆数量
     */
    private int circleNum;

    private float remain;


    public GCouponsView(Context context) 
    {
        super(context);
    }

    public GCouponsView(Context context, AttributeSet attrs) 
    {
        super(context, attrs);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setDither(true);
        mPaint.setColor(Color.WHITE);
        mPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (remain == 0) {
            remain = (int) (w - gap) % (2 * radius + gap);
        }
        circleNum = (int) ((w - gap) / (2 * radius + gap));
    }


    public GCouponsView(Context context, AttributeSet attrs, int defStyleAttr) 
    {
        super(context, attrs, defStyleAttr);
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i=0;i<circleNum;i++){
            float x = gap+radius+remain/2+((gap+radius*2)*i);
            canvas.drawCircle(x,0,radius,mPaint);
            canvas.drawCircle(x,getHeight(),radius,mPaint);
        }
    }
    /*
     <FrameLayout
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:clipChildren="false"
        android:layout_centerInParent="true"
        android:background="@android:color/white">
        <com.zhy.magicviewpager.sample.CouponsView
            android:orientation="horizontal"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_margin="10dp"
            android:background="@color/accent_material_light"
            android:padding="20dp">
            <ImageView
                android:layout_width="120dp"
                android:layout_height="match_parent"
                android:src="@mipmap/ic_launcher"
                android:scaleType="centerCrop"/>
            <LinearLayout
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:orientation="vertical"
                android:paddingLeft="16dp">
                <TextView
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:textSize="18dp"
                    android:text="豪华游卷"
                    />
                <TextView
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:textSize="12dp"
                    android:padding="5dp"
                    android:text="编号:2016042304561456"
                    />
                <TextView
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:textSize="12dp"
                    android:padding="5dp"
                    android:text="编号:2016042304561456"
                    />
                <TextView
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:textSize="12dp"
                    android:paddingLeft="5dp"
                    android:paddingTop="5dp"
                    android:text="截止日期:2016-10-05"
                    />
            </LinearLayout>
        </com.zhy.magicviewpager.sample.CouponsView>

    </FrameLayout>
     * 
     * 
     * */
}