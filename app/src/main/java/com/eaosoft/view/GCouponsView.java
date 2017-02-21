package com.eaosoft.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * Created by �ۿ۾��� on 2016/5/17.
 * ˼·: 
�����ͼƬ��ʵ����ͨ��Linearlayout��RelativeLayoutһ����ֻ���������߶��������ڰ�Բ��ݵ���״����ôֻ��Ҫ����ͬ�ط��������������������ϻ�һ������ɫ��СԲ��ʵ������Ч����

�������������ߵİ�Բ�Լ���Բ���Բ֮��ļ���ǹ̶��ģ���ô��ͬ�ߴ����Ļ�϶��ử����ͬ�����İ�Բ����ô����ֻ��Ҫ���ݿؼ��Ŀ������ȡ�ܻ��İ�Բ����

��ҹ۲�ͼƬ�������׷��֣�Բ����������Բ���������1��Ҳ���ǣ�����Բ��������circleNum,��ôԲ������circleNum+1��

�������ǿ��Ը�����������circleNum. circleNum = (int) ((w-gap)/(2*radius+gap)); ����gap����Բ��࣬radius��Բ�뾶��w��view�Ŀ�
 */
public class GCouponsView extends LinearLayout 
{

    private Paint mPaint;
    /**
     * Բ���
     */
    private float gap = 8;
    /**
     * �뾶
     */
    private float radius = 10;
    /**
     * Բ����
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
                    android:text="�����ξ�"
                    />
                <TextView
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:textSize="12dp"
                    android:padding="5dp"
                    android:text="���:2016042304561456"
                    />
                <TextView
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:textSize="12dp"
                    android:padding="5dp"
                    android:text="���:2016042304561456"
                    />
                <TextView
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:textSize="12dp"
                    android:paddingLeft="5dp"
                    android:paddingTop="5dp"
                    android:text="��ֹ����:2016-10-05"
                    />
            </LinearLayout>
        </com.zhy.magicviewpager.sample.CouponsView>

    </FrameLayout>
     * 
     * 
     * */
}