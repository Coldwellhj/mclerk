package com.eaosoft.mclerk;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.eaosoft.fragment.GFragmentOne;
import com.eaosoft.fragment.GuidePageAdapter;
import com.eaosoft.userinfo.GOperaterInfo;
import com.eaosoft.util.GUtilSDCard;
import com.eaosoft.view.RoundImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;

public class GShareholderMgr
{
	private ScrollView				m_oUserView=null;
	private Context					m_oContext=null;
	public GFragmentOne			m_oFragmentOne=null;
	//==============================
	public RoundImageView		m_oImgHead=null;
	public LinearLayout				m_oRounddot=null;
	public ViewPager					m_oImgBanner=null;
	public TextView 					m_oShopCaption=null;
	public TextView 					m_oUserCaption=null;
	//=======================================
	// 广告数组

	private GuidePageAdapter 	m_oGuidePageAdapter;
	private AtomicInteger 			m_oAtomicInteger = new AtomicInteger();
	private List<View> 				m_arViewList=null;
	// ImageView
	private ImageView 				m_oImageViewList[];
	private ImageView 				m_oImageView;
	private int							m_nCreentViewImage;
	//========================================
	public GShareholderMgr(Context oContext)
	{
		m_oContext = oContext;
		m_nCreentViewImage = 0;
		m_oFragmentOne = null;
	}
	public View OnCreateView()
	{
		m_oUserView = new ScrollView(m_oContext);
		m_oUserView.setLayoutParams( new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT) );
		m_oUserView.setFillViewport(true);

        //============================================================================
		//主背景
		LinearLayout oMainWin = new LinearLayout(m_oContext);  //线性布局方式
		oMainWin.setOrientation( LinearLayout.VERTICAL ); //控件对其方式为垂直排列  VERTICAL
		oMainWin.setLayoutParams( new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT) );
        m_oUserView.addView(oMainWin);

        //===========================================================================
        //头
        RelativeLayout oSubHeader = new RelativeLayout(m_oContext);  //线性布局方式
        oSubHeader.setLayoutParams( new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT, 100 ) );
        oSubHeader.setBackgroundColor(MainActivity.m_oHeadColor);
        oMainWin.addView(oSubHeader);
        //头像
        RelativeLayout.LayoutParams pImgHead = new RelativeLayout.LayoutParams(80,80) ;
        pImgHead.addRule(RelativeLayout.ALIGN_LEFT);
        pImgHead.setMargins(10,10,10,10);
        m_oImgHead = new RoundImageView(m_oContext);
        m_oImgHead.setLayoutParams( pImgHead );
        m_oImgHead.setImageResource(R.drawable.ic_launcher);
        m_oImgHead.setOnClickListener(new OnClickListener() {
	        public void onClick(View v) {
	        	Intent intent = new Intent(MainActivity.m_oMainActivity, GActUserInfo.class);
	        	MainActivity.m_oMainActivity.startActivity(intent); }});
        if(GUtilSDCard.isFileExist(GOperaterInfo.m_strLocalDiskImage ))
        {
  		  Bitmap photo = BitmapFactory.decodeFile(GOperaterInfo.m_strLocalDiskImage);
	  		if(photo !=null && m_oImgHead!=null)
	  			m_oImgHead.setImageBitmap(photo);
        }
        oSubHeader.addView(m_oImgHead);
        //门店名称
        RelativeLayout.LayoutParams pShopName = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT) ;
        pShopName.addRule(RelativeLayout.CENTER_IN_PARENT);

        m_oShopCaption=new TextView(m_oContext);
        m_oShopCaption.setLayoutParams(pShopName);
        m_oShopCaption.setTextSize(16);
        m_oShopCaption.setText("总部");
        m_oShopCaption.setTextColor(Color.WHITE);
        m_oShopCaption.setEnabled(true);
//        m_oShopCaption.setOnClickListener(m_oChangeGroup);
        oSubHeader.addView(m_oShopCaption);

        //操作员名称
        RelativeLayout.LayoutParams pOPName = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT) ;
        pOPName.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        pOPName.addRule(RelativeLayout.CENTER_VERTICAL);

        m_oUserCaption=new TextView(m_oContext);
        m_oUserCaption.setLayoutParams( pOPName);
        m_oUserCaption.setTextSize(16);
        m_oUserCaption.setText(GOperaterInfo.m_strRealName);
        m_oUserCaption.setTextColor(Color.WHITE);
        oSubHeader.addView(m_oUserCaption);
        //==========================================================================
        //Banner
		LinearLayout oSubBanner = new LinearLayout(m_oContext);  //线性布局方式
		oSubBanner.setOrientation( LinearLayout.VERTICAL ); //控件对其方式为垂直排列  VERTICAL
		oSubBanner.setLayoutParams( new LayoutParams(LayoutParams.FILL_PARENT, MainActivity.mSreenHeight/4) );
		oMainWin.addView(oSubBanner);

		m_oImgBanner = new  ViewPager(m_oContext);
		m_oImgBanner.setLayoutParams( new LayoutParams(LayoutParams.FILL_PARENT, MainActivity.mSreenHeight/4) );
		oSubBanner.addView(m_oImgBanner);


		LayoutParams pRounddot = new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT);
		pRounddot.setMargins(0,-25,0,0);

		m_oRounddot = new LinearLayout(m_oContext);
		m_oRounddot.setLayoutParams(pRounddot);
		m_oRounddot.setOrientation( LinearLayout.HORIZONTAL ); //控件对其方式为横向排列  HORIZONTAL
		m_oRounddot.setGravity(Gravity.CENTER);
		oSubBanner.addView(m_oRounddot);

		onCreateBannerImageList();
        //==========================================================================
        //快捷按钮区
		LinearLayout oSubShortBtn = new LinearLayout(m_oContext);  //线性布局方式
		oSubShortBtn.setOrientation( LinearLayout.HORIZONTAL ); //控件对其方式为横向排列  HORIZONTAL
		oSubShortBtn.setLayoutParams( new LayoutParams(LayoutParams.FILL_PARENT, MainActivity.mSreenHeight/8) );
		oMainWin.addView(oSubShortBtn);

		LayoutParams p = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT) ;
		p.setMargins(10,20,10,20);
		p.weight=1.0f;
		Button	oBtnCardOrderCreate = new Button(m_oContext);
		oBtnCardOrderCreate.setLayoutParams( p);
		//oBtnCardOrderCreate.setText("卡销售");
		oBtnCardOrderCreate.setBackgroundResource(R.drawable.card_salse);
		oBtnCardOrderCreate.setOnClickListener(new OnClickListener() {
	        public void onClick(View v) {
                        Intent intent =new Intent(MainActivity.m_oMainActivity,CardSalse.class);
                        MainActivity.m_oMainActivity.startActivity(intent);
	        	        }});
		oSubShortBtn.addView(oBtnCardOrderCreate);

		Button	oBtnCardSearch = new Button(m_oContext);
		oBtnCardSearch.setLayoutParams( p);
		oBtnCardSearch.setBackgroundResource(R.drawable.card_search);
		//oSubShortBtn.addView(oHLineView);
		oBtnCardSearch.setOnClickListener(new OnClickListener() {
	        public void onClick(View v) {
                        Intent intent = new Intent(MainActivity.m_oMainActivity,CardSearch.class);
                        MainActivity.m_oMainActivity.startActivity(intent);
	        	        }});
		oSubShortBtn.addView(oBtnCardSearch);

		Button	oBtnConsume = new Button(m_oContext);
		oBtnConsume.setLayoutParams( p);
		oBtnConsume.setBackgroundResource(R.drawable.consumption_summary);
		oBtnConsume.setOnClickListener(new OnClickListener() {
	        public void onClick(View v) {
                Intent intent = new Intent(MainActivity.m_oMainActivity,Consumption_summary.class);
                MainActivity.m_oMainActivity.startActivity(intent);
	        	        }});
		oSubShortBtn.addView(oBtnConsume);

		//==========================================================================
		//业绩排行
		Button	oBtnPerformanceRanking = new Button(m_oContext);
        oBtnPerformanceRanking.setLayoutParams( new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT) );
        oBtnPerformanceRanking.setBackgroundResource(R.drawable.performance_ranking);
        oBtnPerformanceRanking.setOnClickListener(new OnClickListener() {
	        public void onClick(View v) {
                Intent intent = new Intent(MainActivity.m_oMainActivity,PerformanceRanking.class);
                MainActivity.m_oMainActivity.startActivity(intent);
	     }});
		oMainWin.addView(oBtnPerformanceRanking);
        TextView textView_one = new TextView(m_oContext);
        textView_one.setLayoutParams( new LayoutParams(LayoutParams.MATCH_PARENT, 20) );
        textView_one.setBackgroundColor(Color.WHITE);
        oMainWin.addView(textView_one);

		Button	oBtnSalsePersonal = new Button(m_oContext);
        oBtnSalsePersonal.setLayoutParams( new LayoutParams(LayoutParams.FILL_PARENT,  LayoutParams.WRAP_CONTENT) );
        oBtnSalsePersonal.setBackgroundResource(R.drawable.salse_personal);
        oBtnSalsePersonal.setId(1000);
        oBtnSalsePersonal.setOnClickListener(vp_click_listener);
		oMainWin.addView(oBtnSalsePersonal);

        TextView textView_two = new TextView(m_oContext);
        textView_two.setLayoutParams( new LayoutParams(LayoutParams.MATCH_PARENT, 20) );
        textView_two.setBackgroundColor(Color.WHITE);
        oMainWin.addView(textView_two);
        //异店消费汇总
        Button	oBtnDifferentStoreSales = new Button(m_oContext);
        oBtnDifferentStoreSales.setLayoutParams( new LayoutParams(LayoutParams.FILL_PARENT,  LayoutParams.WRAP_CONTENT) );
        oBtnDifferentStoreSales.setBackgroundResource(R.drawable.different_store_sales);
        oBtnDifferentStoreSales.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                    Intent intent=new Intent(MainActivity.m_oMainActivity,DifferentStoreSales.class);
                    MainActivity.m_oMainActivity.startActivity(intent);
            }});
        oMainWin.addView(oBtnDifferentStoreSales);
		return m_oUserView;
	}
	private void onCreateBannerImageList()
	{
		if(m_arViewList!=null)
			return;
		m_arViewList = new ArrayList<View>();
		for(int i=0;i<5;i++)
		{
			LinearLayout p1 = new LinearLayout(m_oContext);
			p1.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT) );
			p1.setBackgroundResource(R.drawable.main_page1+i);
			p1.setOnClickListener(vp_click_listener);
			m_arViewList.add(p1);
		}
		m_oGuidePageAdapter = new GuidePageAdapter(m_oContext, m_arViewList);
		m_oImgBanner.setAdapter(m_oGuidePageAdapter);

		m_oImageViewList = new ImageView[m_arViewList.size()];

		for (int i = 0; i < m_arViewList.size(); i++)
		{
			m_oImageView = new ImageView(m_oContext);
			// 设置图片宽和高
			LayoutParams layoutParams = new LayoutParams(9, 9);
			layoutParams.setMargins(10, 5, 10, 5);
			m_oImageView.setLayoutParams(layoutParams);

			m_oImageViewList[i] = m_oImageView;

			if (i == 0)
				m_oImageViewList[i].setBackgroundResource(R.drawable.small_bg);
			 else
				m_oImageViewList[i].setBackgroundResource(R.drawable.small_bg1);

			m_oRounddot.addView(m_oImageViewList[i]);
		}

		m_oImgBanner.setOnPageChangeListener(vp_listener);

		// 创建定时器
		Timer timer = new Timer();
		TimerTask task = new TimerTask()
		{
			@Override
			public void run()
			{
				m_oSalseMainHandler.sendEmptyMessage(m_oAtomicInteger.incrementAndGet() - 1);
			}
		};

		timer.schedule(task, 2000, 2000);
	}

	Handler m_oSalseMainHandler = new Handler()
	{
		public void handleMessage(android.os.Message msg)
		{
			// 显示第几项
			m_oImgBanner.setCurrentItem(msg.what);
			if (m_oAtomicInteger.get() == m_arViewList.size())
				m_oAtomicInteger.set(0);
		};
	};
//	OnClickListener m_oChangeGroup = new OnClickListener()
//  	{
//  		 public void onClick(View v)
//         {
//  			Intent intent = new Intent(MainActivity.m_oMainActivity, GActGroupList.class);
//        	MainActivity.m_oMainActivity.startActivityForResult(intent,MainActivity.USER_GROUP_CHANGE);
//         }
//  	};
	OnClickListener vp_click_listener = new OnClickListener()
	{
		public void onClick(View v) 
		{
		    Intent intent =new Intent(MainActivity.m_oMainActivity,SalesPersonal.class);
            MainActivity.m_oMainActivity.startActivity(intent);
		}
	};
	OnPageChangeListener vp_listener = new OnPageChangeListener() 
	{
		@Override
		public void onPageSelected(int arg0) 
		{
			m_nCreentViewImage = arg0;
			m_oAtomicInteger.getAndSet(arg0);
			for (int i = 0; i < m_oImageViewList.length; i++) 
			{
				m_oImageViewList[i].setBackgroundResource(R.drawable.small_bg1);
				if (arg0 != i) 
					m_oImageViewList[i].setBackgroundResource(R.drawable.small_bg);
			}		
		}
		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {}
		@Override
		public void onPageScrollStateChanged(int arg0) {}
	};

}