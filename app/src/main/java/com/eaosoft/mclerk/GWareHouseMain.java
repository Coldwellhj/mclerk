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
		//������
		LinearLayout oMainWin = new LinearLayout(m_oContext);  //���Բ��ַ�ʽ
		oMainWin.setOrientation( LinearLayout.VERTICAL ); //�ؼ����䷽ʽΪ��ֱ����  VERTICAL
		oMainWin.setLayoutParams( new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT) );
        m_oUserView.addView(oMainWin);

        //===========================================================================
        //ҳ��ͷ--
        oMainWin.addView(onCreatePageHead(m_oContext));
        //==========================================================================
	    //�м䰴ť�Ͳ�����
//        oMainWin.addView(onCreatePageBody(m_oContext));
		//==========================================================================
        //��ݰ�ť��

		return m_oUserView;
	}
	private View onCreatePageBody(Context oContext)
	{
		//======================================================================================
		//������
		LinearLayout oMainWin = new LinearLayout(oContext);  //���Բ��ַ�ʽ
		oMainWin.setOrientation( LinearLayout.VERTICAL ); //�ؼ����䷽ʽΪˮƽ����
		oMainWin.setLayoutParams( new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT) );
		//======================================================================================
		/*
			LinearLayout oSubShortBtn = new LinearLayout(oContext);  //���Բ��ַ�ʽ
			oSubShortBtn.setOrientation( LinearLayout.VERTICAL ); //�ؼ����䷽ʽΪ��������  VERTICAL
			oSubShortBtn.setLayoutParams( new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, 160) );
			oMainWin.addView(oSubShortBtn);
			//======================================================================================
			 */
		//======================================================================================
		//��ǰ�ײ�
		Button	oBtnCardKindList = new Button(m_oContext);
		oBtnCardKindList.setLayoutParams( new LayoutParams(LayoutParams.FILL_PARENT, 210) );
		oBtnCardKindList.setBackgroundResource(R.drawable.login);
		oBtnCardKindList.setText("���ײ�");
		oBtnCardKindList.setOnClickListener(new View.OnClickListener() {
	        public void onClick(View v) {
	        	Intent intent = new Intent(MainActivity.m_oMainActivity, GActCardKindList.class);
	        	intent.putExtra("UserMgr", "UserMgr");
	        	MainActivity.m_oMainActivity.startActivity(intent);
	     }});
		oMainWin.addView(oBtnCardKindList);
				//==========================================================================
				//�¿�����
				Button	oBtnCardCreate = new Button(m_oContext);
				oBtnCardCreate.setLayoutParams( new LayoutParams(LayoutParams.FILL_PARENT, 210) );
				oBtnCardCreate.setBackgroundResource(R.drawable.btn_card_create);
				oBtnCardCreate.setText("������");
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
				//ɨ���ѯ
				Button	oBtnCardSearchByScanner = new Button(m_oContext);
				oBtnCardSearchByScanner.setLayoutParams( new LayoutParams(LayoutParams.FILL_PARENT, 210) );
				oBtnCardSearchByScanner.setBackgroundResource(R.drawable.login);
				oBtnCardSearchByScanner.setText("ɨ���ѯ");
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
	//����ҳ��ͷ��
	private View onCreatePageHead(Context oContext)
	{
		RelativeLayout oSubHeader = new RelativeLayout(oContext);  //���Բ��ַ�ʽ               
        oSubHeader.setLayoutParams( new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT, 80) );
        oSubHeader.setBackgroundColor(MainActivity.m_oHeadColor);        
	        //=============================================================================
	        //ͷ��
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
        //�ŵ�����
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
        //����
        RelativeLayout.LayoutParams pOPName = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT) ;
        pOPName.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        pOPName.addRule(RelativeLayout.CENTER_VERTICAL);
        
        m_oUserCaption=new TextView(oContext);
        m_oUserCaption.setLayoutParams( pOPName);
        m_oUserCaption.setTextSize(16);        
        m_oUserCaption.setText("����");
        m_oUserCaption.setTextColor(Color.WHITE);
		m_oShopCaption.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				//��ת��ϸ����
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