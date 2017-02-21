package com.eaosoft.mclerk;


import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;

import com.eaosoft.fragment.GFragmentOne;
import com.eaosoft.fragment.GuidePageAdapter;
import com.eaosoft.userinfo.GOperaterInfo;
import com.eaosoft.util.GSvrChannel;
import com.eaosoft.util.GUtilSDCard;
import com.eaosoft.view.RoundImageView;
import com.google.zxing.client.android.CaptureActivity;

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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

public class GSalseMain
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
	// �������
	
	private GuidePageAdapter 	m_oGuidePageAdapter;
	private AtomicInteger 			m_oAtomicInteger = new AtomicInteger();
	private List<View> 				m_arViewList=null;
	// ImageView
	private ImageView 				m_oImageViewList[];
	private ImageView 				m_oImageView;
	private int							m_nCreentViewImage;
	//========================================
	public GSalseMain(Context oContext)
	{
		m_oContext = oContext;
		m_nCreentViewImage = 0;
		m_oFragmentOne = null;
	}
	public View OnCreateView()
	{
		m_oUserView = new ScrollView(m_oContext);
		m_oUserView.setLayoutParams( new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT) );
		m_oUserView.setFillViewport(true);
		
        //============================================================================
		//������
		LinearLayout oMainWin = new LinearLayout(m_oContext);  //���Բ��ַ�ʽ  
		oMainWin.setOrientation( LinearLayout.VERTICAL ); //�ؼ����䷽ʽΪ��ֱ����  VERTICAL
		oMainWin.setLayoutParams( new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT) );
        m_oUserView.addView(oMainWin);
		
        //===========================================================================
        //ͷ
        RelativeLayout oSubHeader = new RelativeLayout(m_oContext);  //���Բ��ַ�ʽ          
        oSubHeader.setLayoutParams( new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT, 80) );
        oSubHeader.setBackgroundColor(MainActivity.m_oHeadColor);
        oMainWin.addView(oSubHeader);
        //ͷ��
        RelativeLayout.LayoutParams pImgHead = new RelativeLayout.LayoutParams(80,80) ;
        pImgHead.addRule(RelativeLayout.ALIGN_LEFT);
        m_oImgHead = new RoundImageView(m_oContext);
        m_oImgHead.setLayoutParams( pImgHead );
        m_oImgHead.setImageResource(R.drawable.ic_launcher);
        m_oImgHead.setOnClickListener(new View.OnClickListener() {
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
        //�ŵ�����
        RelativeLayout.LayoutParams pShopName = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT) ;
        pShopName.addRule(RelativeLayout.CENTER_IN_PARENT);
        
        m_oShopCaption=new TextView(m_oContext);
        m_oShopCaption.setLayoutParams( pShopName);
        m_oShopCaption.setTextSize(16);        
        m_oShopCaption.setText(GOperaterInfo.m_strGroupName);                
        m_oShopCaption.setTextColor(Color.WHITE);
        m_oShopCaption.setEnabled(true);
        m_oShopCaption.setOnClickListener(m_oChangeGroup);
        oSubHeader.addView(m_oShopCaption);
        
        //����Ա����
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
		LinearLayout oSubBanner = new LinearLayout(m_oContext);  //���Բ��ַ�ʽ  
		oSubBanner.setOrientation( LinearLayout.VERTICAL ); //�ؼ����䷽ʽΪ��ֱ����  VERTICAL
		oSubBanner.setLayoutParams( new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, 240) );		
		oMainWin.addView(oSubBanner);
		
		m_oImgBanner = new  ViewPager(m_oContext);
		m_oImgBanner.setLayoutParams( new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, 240) );
		oSubBanner.addView(m_oImgBanner);
		
		
		LinearLayout.LayoutParams pRounddot = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT);
		pRounddot.setMargins(0,-25,0,0);
		
		m_oRounddot = new LinearLayout(m_oContext); 
		m_oRounddot.setLayoutParams(pRounddot);
		m_oRounddot.setOrientation( LinearLayout.HORIZONTAL ); //�ؼ����䷽ʽΪ��������  HORIZONTAL
		m_oRounddot.setGravity(Gravity.CENTER);
		oSubBanner.addView(m_oRounddot);
		
		onCreateBannerImageList();		
        //==========================================================================
        //��ݰ�ť��
		LinearLayout oSubShortBtn = new LinearLayout(m_oContext);  //���Բ��ַ�ʽ  
		oSubShortBtn.setOrientation( LinearLayout.HORIZONTAL ); //�ؼ����䷽ʽΪ��������  HORIZONTAL
		oSubShortBtn.setLayoutParams( new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, 160) );
		oMainWin.addView(oSubShortBtn);
		
		LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT) ;
		p.setMargins(10,10,10,10);
		p.weight=1.0f;
		Button	oBtnCardOrderCreate = new Button(m_oContext); 
		oBtnCardOrderCreate.setLayoutParams( p);
		//oBtnCardOrderCreate.setText("�µ�");
		oBtnCardOrderCreate.setBackgroundResource(R.drawable.btn_order_create);
		oBtnCardOrderCreate.setOnClickListener(new View.OnClickListener() {
	        public void onClick(View v) {
	        	if(MainActivity.m_bDebugCardNo)
	        	{
	        		if(m_oFragmentOne !=null)
	        			m_oFragmentOne.onScannerResult(MainActivity.m_strDebugCardNo,MainActivity.SCAN_CODE_ORDER_CREATE);
	        	}
	        	else
	        	{
	        	Intent intent = new Intent(MainActivity.m_oMainActivity, CaptureActivity.class);
	        	MainActivity.m_oMainActivity.startActivityForResult(intent, MainActivity.SCAN_CODE_ORDER_CREATE);
	        	}
	        	        }});
		oSubShortBtn.addView(oBtnCardOrderCreate);
		
		Button	oBtnCardSearch = new Button(m_oContext); 
		oBtnCardSearch.setLayoutParams( p);
		//oBtnCardSearch.setText("�鿨");
		oBtnCardSearch.setBackgroundResource(R.drawable.btn_card_search);
		//oSubShortBtn.addView(oHLineView);
		oBtnCardSearch.setOnClickListener(new View.OnClickListener() {
	        public void onClick(View v) {
	        	if(MainActivity.m_bDebugCardNo)
	        	{
	        		if(m_oFragmentOne !=null)
	        			m_oFragmentOne.onScannerResult(MainActivity.m_strDebugCardNo,MainActivity.SCAN_CODE_CAED_INFO);
	        	}
	        	else
	        	{
	        	Intent intent = new Intent(MainActivity.m_oMainActivity, CaptureActivity.class);
	        	MainActivity.m_oMainActivity.startActivityForResult(intent, MainActivity.SCAN_CODE_CAED_INFO);
	        	}
	        	        }});
		oSubShortBtn.addView(oBtnCardSearch);

		Button	oBtnConsume = new Button(m_oContext); 
		oBtnConsume.setLayoutParams( p);
		//oBtnConsume.setText("���Ѽ�¼");
		oBtnConsume.setBackgroundResource(R.drawable.btn_consume_history);
		//oSubShortBtn.addView(oHLineView);
		oBtnConsume.setOnClickListener(new View.OnClickListener() {
	        public void onClick(View v) {
	        	if(MainActivity.m_bDebugCardNo)
	        	{
	        		if(m_oFragmentOne !=null)
	        			m_oFragmentOne.onScannerResult(MainActivity.m_strDebugCardNo,MainActivity.SCAN_CODE_CAED_CONSUME);
	        	}
	        	else
	        	{
	        	Intent intent = new Intent(MainActivity.m_oMainActivity, CaptureActivity.class);
	        	MainActivity.m_oMainActivity.startActivityForResult(intent, MainActivity.SCAN_CODE_CAED_CONSUME);
	        	}
	        	        }});
		oSubShortBtn.addView(oBtnConsume);
		
		//==========================================================================
		//�¿�����
		Button	oBtnCardCreate = new Button(m_oContext); 
		oBtnCardCreate.setLayoutParams( new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, 210) );
		oBtnCardCreate.setBackgroundResource(R.drawable.btn_card_create);
		oBtnCardCreate.setOnClickListener(new View.OnClickListener() {
	        public void onClick(View v) {
	        	if(MainActivity.m_bDebugCardNo)
	        	{
	        		if(m_oFragmentOne !=null)
	        			m_oFragmentOne.onScannerResult(MainActivity.m_strDebugCardNo,MainActivity.SCAN_CODE_CAED_CREATE);
	        	}
	        	else
	        	{
	        	Intent intent = new Intent(MainActivity.m_oMainActivity, CaptureActivity.class);
	        	MainActivity.m_oMainActivity.startActivityForResult(intent, MainActivity.SCAN_CODE_CAED_CREATE);
	        	}
	     }});
		oMainWin.addView(oBtnCardCreate);
		
		Button	oBtnCardOrderList = new Button(m_oContext); 
		oBtnCardOrderList.setLayoutParams( new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, 180) );
		//oBtnCardOrderList.setText("�µ���¼");
		oBtnCardOrderList.setBackgroundResource(R.drawable.btn_order_history);
		oBtnCardOrderList.setId(1000);
		oBtnCardOrderList.setOnClickListener(vp_click_listener);
		oMainWin.addView(oBtnCardOrderList);
		
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
			p1.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT) );
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
			// ����ͼƬ��͸�
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

		// ������ʱ��
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
			// ��ʾ�ڼ���
			m_oImgBanner.setCurrentItem(msg.what);
			if (m_oAtomicInteger.get() == m_arViewList.size()) 
				m_oAtomicInteger.set(0);			
		};
	};
	View.OnClickListener m_oChangeGroup = new View.OnClickListener()
  	{
  		 public void onClick(View v) 
         {
  			Intent intent = new Intent(MainActivity.m_oMainActivity, GActGroupList.class);			
        	MainActivity.m_oMainActivity.startActivityForResult(intent,MainActivity.USER_GROUP_CHANGE);	
         }
  	};
	OnClickListener vp_click_listener = new OnClickListener()
	{
		public void onClick(View v) 
		{
			if(v.getId()==1000)//��ѯ�µ�����ʷ��¼
			{
				String strURL=GSvrChannel.m_strURLOrderHistory+"?token="+GOperaterInfo.m_strToken+"&callerName="+GSvrChannel.CALLER_NAME;
				String strTitle=GOperaterInfo.m_strRealName+"���µ���¼";
				Intent intent = new Intent(MainActivity.m_oMainActivity, GActWebView.class);
				
				intent.putExtra("startURL", strURL);
				intent.putExtra("strTitle", strTitle);
				
	        	MainActivity.m_oMainActivity.startActivity(intent);	
				return;
			}
			String strURL="http://www.ndtquam.com";
			if(m_nCreentViewImage==4)
				strURL = "http://www.eaosoft.com";
			/*
			Intent intent = new Intent(getActivity(), GActWebView.class);
			
			intent.putExtra("startURL", strURL);
			
			startActivity(intent);
			*/
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
	//�û�ɨ�迨��
	public void onScannerResult(String strCardNo,int requestCode)
	{
		if(requestCode== MainActivity.USER_GROUP_CHANGE && m_oShopCaption != null)
				m_oShopCaption.setText(GOperaterInfo.m_strGroupName);
	}
}