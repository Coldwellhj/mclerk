package com.eaosoft.mclerk;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.eaosoft.fragment.GFragmentOne;
import com.eaosoft.userinfo.GOperaterInfo;
import com.eaosoft.util.GSvrChannel;
import com.eaosoft.util.GUtilSDCard;
import com.eaosoft.view.RoundImageView;
import com.google.zxing.client.android.CaptureActivity;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

public class GWareHouseMain
{
	private ScrollView				m_oUserView=null;
	private Context					m_oContext=null;
	public GFragmentOne			m_oFragmentOne=null;
	//=====================================================
	public RoundImageView		m_oImgHead=null;
	public LinearLayout				m_oRounddot=null;
	public ViewPager					m_oImgBanner=null;
	private TextView				m_txtCardNo=null;
	public TextView 					m_oShopCaption=null;
	public TextView 					m_otxtCardNo=null;
    public TextView                m_oCurrentTime=null;
    public TextView                m_ooddnumbers=null;

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
       oMainWin.addView(onCreatePageBody(m_oContext));
		//==========================================================================
        //快捷按钮区

		return m_oUserView;
	}
	private View onCreatePageBody(Context oContext)
	{
		//======================================================================================
		//主背景
		LinearLayout oMainWin = new LinearLayout(oContext);  //线性布局方式
		oMainWin.setOrientation( LinearLayout.VERTICAL ); //控件对其方式为竖直排列
		oMainWin.setLayoutParams( new LayoutParams(MainActivity.mSreenWidth/4, LayoutParams.MATCH_PARENT) );
        oMainWin.setBackgroundResource(R.color.lightgray);
		//======================================================================================
        RelativeLayout.LayoutParams m_txtCardNo = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT) ;
        m_txtCardNo.setMargins(40,40,10,10);
        m_otxtCardNo=new TextView(oContext);
        m_otxtCardNo.setLayoutParams(m_txtCardNo);
        m_otxtCardNo.setTextSize(16);
        m_otxtCardNo.setText("房号");
        m_otxtCardNo.setTextColor(Color.BLACK);
        oMainWin.addView(m_otxtCardNo);

        RelativeLayout.LayoutParams m_oddnumbers = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT) ;
        m_oddnumbers.setMargins(40,40,10,10);
        m_ooddnumbers=new TextView(oContext);
        m_ooddnumbers.setLayoutParams(m_oddnumbers);
        m_ooddnumbers.setTextSize(16);
        m_ooddnumbers.setText("单号");
        m_ooddnumbers.setTextColor(Color.BLACK);
        oMainWin.addView(m_ooddnumbers);

        RelativeLayout.LayoutParams m_oBtnPrintOrder= new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT) ;
        m_oBtnPrintOrder.setMargins(40,40,40,10);
		Button	oBtnPrintOrder = new Button(m_oContext);
        oBtnPrintOrder.setLayoutParams(m_oBtnPrintOrder);

		oBtnPrintOrder.setBackgroundResource(R.color.printbutton);
		oBtnPrintOrder.setText("打印订单");
		oBtnPrintOrder.setTextColor(oBtnPrintOrder.getResources().getColor(android.R.color.white));
        oBtnPrintOrder.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

            }});
		oMainWin.addView(oBtnPrintOrder);

        RelativeLayout.LayoutParams m_oBtnFillPrint= new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT) ;
        m_oBtnFillPrint.setMargins(40,40,40,10);
        Button	oBtnFillPrint = new Button(m_oContext);
        oBtnFillPrint.setLayoutParams(m_oBtnFillPrint);
        oBtnFillPrint.setBackgroundResource(R.color.printbutton);
        oBtnFillPrint.setText("历史补打");
        oBtnFillPrint.setTextColor(oBtnPrintOrder.getResources().getColor(android.R.color.white));
        oBtnFillPrint.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

            }});
        oMainWin.addView(oBtnFillPrint);

        RelativeLayout.LayoutParams m_oBtnViewReport= new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT) ;
        m_oBtnViewReport.setMargins(40,40,40,10);
        Button	oBtnViewReport = new Button(m_oContext);
        oBtnViewReport.setLayoutParams(m_oBtnViewReport);
        oBtnViewReport.setBackgroundResource(R.color.printbutton);
        oBtnViewReport.setText("查看报表");
        oBtnViewReport.setTextColor(oBtnPrintOrder.getResources().getColor(android.R.color.white));
        oBtnViewReport.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

            }});
        oMainWin.addView(oBtnViewReport);
			//======================================================================================
		return oMainWin;
	}
	//=============================================================================================
	//建立页面头部
	private View onCreatePageHead(Context oContext)
	{
		RelativeLayout oSubHeader = new RelativeLayout(oContext);  //线性布局方式               
        oSubHeader.setLayoutParams( new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT,100) );
        oSubHeader.setBackgroundColor(MainActivity.m_oHeadColor);        
	        //=============================================================================


		//实时时间
		RelativeLayout.LayoutParams pCurrentTime = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT) ;
		pCurrentTime.addRule(RelativeLayout.ALIGN_LEFT);
		pCurrentTime.addRule(RelativeLayout.CENTER_VERTICAL);
		m_oCurrentTime=new TextView(oContext);
		m_oCurrentTime.setLayoutParams( pCurrentTime);
		m_oCurrentTime.setTextSize(16);
		m_oCurrentTime.setText(getStringDate());
		m_oCurrentTime.setTextColor(Color.WHITE);

		oSubHeader.addView(m_oCurrentTime);
	        //头像
        RelativeLayout.LayoutParams pImgHead = new RelativeLayout.LayoutParams(80,80);
        pImgHead.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
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
		//操作员名称
//		RelativeLayout.LayoutParams pOPName = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT) ;
//		pOPName.addRule(RelativeLayout.ALIGN_LEFT,m_oImgHead.getId());
//
//		pOPName.addRule(RelativeLayout.CENTER_VERTICAL);
//
//		m_oUserCaption=new TextView(oContext);
//		m_oUserCaption.setLayoutParams( pOPName);
//		m_oUserCaption.setTextSize(16);
//		m_oUserCaption.setText(GOperaterInfo.m_strRealName);
//		m_oUserCaption.setTextColor(Color.WHITE);
//		m_oShopCaption.setOnClickListener(new View.OnClickListener()
//		{
//			public void onClick(View v)
//			{
//				//跳转详细报表
//			}
//		});
//		oSubHeader.addView(m_oUserCaption);
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

        return oSubHeader;
	}	
	public void onScannerResult(String strCardNo,int requestCode)
	{
		if(requestCode== MainActivity.USER_GROUP_CHANGE && m_oShopCaption != null)
				m_oShopCaption.setText(GOperaterInfo.m_strGroupName);
	}
	/**
	 * 获取现在时间
	 *
	 * @return返回字符串格式 yyyy-MM-dd HH:mm:ss
	 */
	public static String getStringDate() {
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateString = formatter.format(currentTime);
		return dateString;
	}



}