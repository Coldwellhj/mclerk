package com.eaosoft.mclerk;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.eaosoft.fragment.GFragmentOne;
import com.eaosoft.userinfo.GOperaterInfo;
import com.eaosoft.util.GUtilSDCard;
import com.eaosoft.view.RoundImageView;
import com.google.zxing.client.android.CaptureActivity;

public class GWareHouseMain
{
	private ScrollView				m_oUserView=null;
	private Context					m_oContext=null;
	public GFragmentOne			m_oFragmentOne=null;
	//=====================================================
	public RoundImageView		m_oImgHead=null;
	public LinearLayout				m_oRounddot=null;
	public ViewPager					m_oImgBanner=null;
	public TextView 					m_oShopCaption=null;
	public TextView 					m_oUserCaption=null;
	//=====================================================
	public GWareHouseMain(Context oContext)
	{
		m_oContext = oContext;
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
        //页面头--
        oMainWin.addView(onCreatePageHead(m_oContext));
        //==========================================================================
	    //中间按钮和操作区
//        oMainWin.addView(onCreatePageBody(m_oContext));
		//==========================================================================
        //快捷按钮区

		return m_oUserView;
	}
	private View onCreatePageBody(Context oContext)
	{
		//======================================================================================
		//主背景
		LinearLayout oMainWin = new LinearLayout(oContext);  //线性布局方式
		oMainWin.setOrientation( LinearLayout.VERTICAL ); //控件对其方式为水平排列
		oMainWin.setLayoutParams( new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT) );
		//======================================================================================
		/*
			LinearLayout oSubShortBtn = new LinearLayout(oContext);  //线性布局方式
			oSubShortBtn.setOrientation( LinearLayout.VERTICAL ); //控件对其方式为横向排列  VERTICAL
			oSubShortBtn.setLayoutParams( new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, 160) );
			oMainWin.addView(oSubShortBtn);
			//======================================================================================
			 */
		//======================================================================================
		//当前套餐
		Button	oBtnCardKindList = new Button(m_oContext);
		oBtnCardKindList.setLayoutParams( new LayoutParams(LayoutParams.FILL_PARENT, 210) );
		oBtnCardKindList.setBackgroundResource(R.drawable.login);
		oBtnCardKindList.setText("卡套餐");
		oBtnCardKindList.setOnClickListener(new View.OnClickListener() {
	        public void onClick(View v) {
	        	Intent intent = new Intent(MainActivity.m_oMainActivity, GActCardKindList.class);
	        	intent.putExtra("UserMgr", "UserMgr");
	        	MainActivity.m_oMainActivity.startActivity(intent);
	     }});
		oMainWin.addView(oBtnCardKindList);
				//==========================================================================
				//新卡销售
				Button	oBtnCardCreate = new Button(m_oContext);
				oBtnCardCreate.setLayoutParams( new LayoutParams(LayoutParams.FILL_PARENT, 210) );
				oBtnCardCreate.setBackgroundResource(R.drawable.btn_card_create);
				oBtnCardCreate.setText("卡销售");
				oBtnCardCreate.setOnClickListener(new View.OnClickListener() {
			        public void onClick(View v) {
			        	if(MainActivity.m_bDebugCardNo)
			        	{
			        		if(m_oFragmentOne!=null)
			        			m_oFragmentOne.onScannerResult(MainActivity.m_strDebugCardNo,MainActivity.SCAN_CODE_CAED_CREATE);
			        	}
			        	else
			        	{
				        	Intent intent = new Intent(MainActivity.m_oMainActivity, CaptureActivity.class);
				        	MainActivity.m_oMainActivity.startActivityForResult(intent, MainActivity.SCAN_CODE_CAED_CREATE);
			        	}
			     }});
				oMainWin.addView(oBtnCardCreate);
			//======================================================================================
				//扫码查询
				Button	oBtnCardSearchByScanner = new Button(m_oContext);
				oBtnCardSearchByScanner.setLayoutParams( new LayoutParams(LayoutParams.FILL_PARENT, 210) );
				oBtnCardSearchByScanner.setBackgroundResource(R.drawable.login);
				oBtnCardSearchByScanner.setText("扫码查询");
				oBtnCardSearchByScanner.setOnClickListener(new View.OnClickListener() {
			        public void onClick(View v) {
			        	if(MainActivity.m_bDebugCardNo)
			        	{
			        		if(m_oFragmentOne!=null)
			        			m_oFragmentOne.onScannerResult(MainActivity.m_strDebugCardNo,MainActivity.SCAN_CODE_CAED_CONSUME);
			        	}
			        	else
			        	{
				        	Intent intent = new Intent(MainActivity.m_oMainActivity, CaptureActivity.class);
				        	MainActivity.m_oMainActivity.startActivityForResult(intent, MainActivity.SCAN_CODE_CAED_CONSUME);
			        	}
			     }});
				oMainWin.addView(oBtnCardSearchByScanner);
			//======================================================================================	
		return oMainWin;
	}
	//=============================================================================================
	//建立页面头部
	private View onCreatePageHead(Context oContext)
	{
		RelativeLayout oSubHeader = new RelativeLayout(oContext);  //线性布局方式               
        oSubHeader.setLayoutParams( new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT, 80) );
        oSubHeader.setBackgroundColor(MainActivity.m_oHeadColor);        
	        //=============================================================================
	        //头像
        RelativeLayout.LayoutParams pImgHead = new RelativeLayout.LayoutParams(80,80);
        pImgHead.addRule(RelativeLayout.ALIGN_LEFT);
        m_oImgHead = new RoundImageView(oContext);
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
        //门店名称
        RelativeLayout.LayoutParams pShopName = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT) ;
        pShopName.addRule(RelativeLayout.CENTER_IN_PARENT);
        
        m_oShopCaption=new TextView(oContext);
        m_oShopCaption.setLayoutParams( pShopName);
        m_oShopCaption.setTextSize(16);        
        m_oShopCaption.setText(GOperaterInfo.m_strGroupName);                
        m_oShopCaption.setTextColor(Color.WHITE);
        m_oShopCaption.setEnabled(true);
        m_oShopCaption.setOnClickListener(new View.OnClickListener()
      	{
     		 public void onClick(View v) 
            {
     			Intent intent = new Intent(MainActivity.m_oMainActivity, GActGroupList.class);			
     			MainActivity.m_oMainActivity.startActivityForResult(intent,MainActivity.USER_GROUP_CHANGE);	
            }
     	});
        oSubHeader.addView(m_oShopCaption);
        //报表
        RelativeLayout.LayoutParams pOPName = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT) ;
        pOPName.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        pOPName.addRule(RelativeLayout.CENTER_VERTICAL);
        
        m_oUserCaption=new TextView(oContext);
        m_oUserCaption.setLayoutParams( pOPName);
        m_oUserCaption.setTextSize(16);        
        m_oUserCaption.setText("报表");
        m_oUserCaption.setTextColor(Color.WHITE);
		m_oShopCaption.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				//跳转详细报表
			}
		});
		oSubHeader.addView(m_oUserCaption);
        return oSubHeader;
	}	
	public void onScannerResult(String strCardNo,int requestCode)
	{
		if(requestCode== MainActivity.USER_GROUP_CHANGE && m_oShopCaption != null)
				m_oShopCaption.setText(GOperaterInfo.m_strGroupName);
	}
}