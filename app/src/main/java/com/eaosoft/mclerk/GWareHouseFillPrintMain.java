package com.eaosoft.mclerk;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.Gravity;
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

public class GWareHouseFillPrintMain
{
	private ScrollView				m_oUserView=null;
	private Context					m_oContext=null;
	public GFragmentOne			m_oFragmentOne=null;
	//=====================================================
	public RoundImageView		m_oImgHead=null;
	public TextView 					m_oShopCaption=null;
	public TextView 					m_otxtCardNo=null;
	public TextView 					orderNumber=null;
	public TextView 					m_ocardNumber=null;
	public TextView 					m_oorderTime=null;
	public TextView 					m_osalseman=null;

    public TextView                m_oCurrentTime=null;
    public TextView                m_ooddnumbers=null;
	public TextView                m_odateTime=null;
    //=====================================================
	public GWareHouseFillPrintMain(Context oContext)
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
       oMainWin.addView(onCreatePageBody(m_oContext));
		//==========================================================================
        //��ݰ�ť��

		return m_oUserView;
	}
	private View onCreatePageBody(Context oContext)
	{
		//======================================================================================
		//������
		LinearLayout oMainWin = new LinearLayout(oContext);  //���Բ��ַ�ʽ
		oMainWin.setOrientation( LinearLayout.HORIZONTAL ); //�ؼ����䷽ʽΪ��ֱ����
		oMainWin.setLayoutParams( new LayoutParams( LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		//���߲�����ť
		LinearLayout oMainWin_left = new LinearLayout(oContext);  //���Բ��ַ�ʽ
		oMainWin_left.setOrientation( LinearLayout.VERTICAL ); //�ؼ����䷽ʽΪ��ֱ����
		oMainWin_left.setLayoutParams( new LayoutParams(MainActivity.mSreenWidth/4, LayoutParams.MATCH_PARENT));
		oMainWin_left.setBackgroundResource(R.color.lightgray);
		//======================================================================================
        RelativeLayout.LayoutParams m_txtCardNo = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT) ;
		m_txtCardNo.setMargins(40,40,10,10);
		m_otxtCardNo=new TextView(oContext);
		m_otxtCardNo.setLayoutParams(m_txtCardNo);
		m_otxtCardNo.setTextSize(16);
		m_otxtCardNo.setText("����");
		m_otxtCardNo.setTextColor(Color.BLACK);
		oMainWin_left.addView(m_otxtCardNo);

        RelativeLayout.LayoutParams m_oddnumbers = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT) ;
        m_oddnumbers.setMargins(40,40,10,10);
        m_ooddnumbers=new TextView(oContext);
        m_ooddnumbers.setLayoutParams(m_oddnumbers);
        m_ooddnumbers.setTextSize(16);
        m_ooddnumbers.setText("����");
        m_ooddnumbers.setTextColor(Color.BLACK);
		oMainWin_left.addView(m_ooddnumbers);

		RelativeLayout.LayoutParams m_dateTime = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT) ;
		m_dateTime.setMargins(40,40,10,10);
		m_odateTime=new TextView(oContext);
		m_odateTime.setLayoutParams(m_dateTime);
		m_odateTime.setTextSize(16);
		m_odateTime.setText("����");
		m_odateTime.setTextColor(Color.BLACK);
		oMainWin_left.addView(m_odateTime);

        RelativeLayout.LayoutParams m_oBtnPrintOrder= new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT) ;
        m_oBtnPrintOrder.setMargins(40,40,40,10);
		Button	oBtnPrintOrder = new Button(m_oContext);
        oBtnPrintOrder.setLayoutParams(m_oBtnPrintOrder);
		oBtnPrintOrder.setBackgroundResource(R.color.printbutton);
		oBtnPrintOrder.setText("��ѯ����");
		oBtnPrintOrder.setTextColor(oBtnPrintOrder.getResources().getColor(android.R.color.white));
        oBtnPrintOrder.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

            }});
		oMainWin_left.addView(oBtnPrintOrder);



        RelativeLayout.LayoutParams m_oBtnPrintConform= new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT) ;
		m_oBtnPrintConform.setMargins(40,40,40,10);
        Button	oBtnViewReport = new Button(m_oContext);
        oBtnViewReport.setLayoutParams(m_oBtnPrintConform);
        oBtnViewReport.setBackgroundResource(R.color.printbutton);
        oBtnViewReport.setText("ȷ�ϲ���");
        oBtnViewReport.setTextColor(oBtnPrintOrder.getResources().getColor(android.R.color.white));
        oBtnViewReport.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

            }});
		oMainWin_left.addView(oBtnViewReport);
			//======================================================================================
		//�Ұ�ߴ�ӡ��ϸ
		LinearLayout oMainWin_right = new LinearLayout(oContext);  //���Բ��ַ�ʽ
		oMainWin_right.setOrientation( LinearLayout.HORIZONTAL ); //�ؼ����䷽ʽΪ��ֱ����
		oMainWin_right.setLayoutParams( new LayoutParams(MainActivity.mSreenWidth*3/4, LayoutParams.MATCH_PARENT));
		RelativeLayout.LayoutParams m_orderNumber = new RelativeLayout.LayoutParams(MainActivity.mSreenWidth*3/4/5, 100) ;
//        m_orderNumber.setMargins(10,40,10,10);
		orderNumber=new TextView(oContext);
		orderNumber.setLayoutParams(m_orderNumber);
		orderNumber.setTextSize(16);
		orderNumber.setGravity(Gravity.CENTER);
		orderNumber.setText("����");
		orderNumber.setBackgroundResource(R.color.printbutton);
		orderNumber.setTextColor(Color.WHITE);
		oMainWin_right.addView(orderNumber);

		RelativeLayout.LayoutParams m_txtCardNum= new RelativeLayout.LayoutParams(MainActivity.mSreenWidth*3/4/5,100) ;
//        m_txtCardNo.setMargins(40,40,10,10);
		m_otxtCardNo=new TextView(oContext);
		m_otxtCardNo.setLayoutParams(m_txtCardNum);
		m_otxtCardNo.setTextSize(16);
		m_otxtCardNo.setGravity(Gravity.CENTER);
		m_otxtCardNo.setText("����");
		m_otxtCardNo.setBackgroundResource(R.color.printbutton);
		m_otxtCardNo.setTextColor(Color.WHITE);
		oMainWin_right.addView(m_otxtCardNo);
		RelativeLayout.LayoutParams m_cardNumber = new RelativeLayout.LayoutParams(MainActivity.mSreenWidth*3/4/5, 100) ;
//        m_txtCardNo.setMargins(40,40,10,10);
		m_ocardNumber=new TextView(oContext);
		m_ocardNumber.setLayoutParams(m_cardNumber);
		m_ocardNumber.setTextSize(16);
		m_ocardNumber.setGravity(Gravity.CENTER);
		m_ocardNumber.setText("����");
		m_ocardNumber.setBackgroundResource(R.color.printbutton);
		m_ocardNumber.setTextColor(Color.WHITE);
		oMainWin_right.addView(m_ocardNumber);
		RelativeLayout.LayoutParams m_orderTime = new RelativeLayout.LayoutParams(MainActivity.mSreenWidth*3/4/5, 100) ;
//        m_txtCardNo.setMargins(40,40,10,10);
		m_oorderTime=new TextView(oContext);
		m_oorderTime.setLayoutParams(m_orderTime);
		m_oorderTime.setTextSize(16);
		m_oorderTime.setGravity(Gravity.CENTER);
		m_oorderTime.setText("�µ�ʱ��");
		m_oorderTime.setBackgroundResource(R.color.printbutton);
		m_oorderTime.setTextColor(Color.WHITE);
		oMainWin_right.addView(m_oorderTime);
		RelativeLayout.LayoutParams m_salseMan = new RelativeLayout.LayoutParams(MainActivity.mSreenWidth*3/4/5, 100) ;
//        m_txtCardNo.setMargins(40,40,10,10);
		m_osalseman=new TextView(oContext);
		m_osalseman.setLayoutParams(m_salseMan);
		m_osalseman.setTextSize(16);
		m_osalseman.setGravity(Gravity.CENTER);
		m_osalseman.setText("����Ա");
		m_osalseman.setBackgroundResource(R.color.printbutton);
		m_osalseman.setTextColor(Color.WHITE);
		oMainWin_right.addView(m_osalseman);
		oMainWin.addView(oMainWin_left);
		oMainWin.addView(oMainWin_right);
		return oMainWin;

	}
	//=============================================================================================
	//����ҳ��ͷ��
	private View onCreatePageHead(Context oContext)
	{
		RelativeLayout oSubHeader = new RelativeLayout(oContext);  //���Բ��ַ�ʽ               
        oSubHeader.setLayoutParams( new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT,100) );
        oSubHeader.setBackgroundColor(MainActivity.m_oHeadStoreColor);
	        //=============================================================================


		//ʵʱʱ��
		RelativeLayout.LayoutParams pCurrentTime = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT) ;
		pCurrentTime.addRule(RelativeLayout.ALIGN_LEFT);
		pCurrentTime.addRule(RelativeLayout.CENTER_VERTICAL);
		m_oCurrentTime=new TextView(oContext);
		m_oCurrentTime.setLayoutParams( pCurrentTime);
		m_oCurrentTime.setTextSize(16);
		m_oCurrentTime.setText(MainActivity.getStringDate());
		m_oCurrentTime.setTextColor(Color.BLACK);

		oSubHeader.addView(m_oCurrentTime);
	        //ͷ��
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
		//����Ա����
//		RelativeLayout.LayoutParams pOPName = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT) ;
//		pOPName.addRule(RelativeLayout.ALIGN_LEFT,m_oImgHead.getId());
//
//		pOPName.addRule(RelativeLayout.CENTER_VERTICAL);
//
//		m_oUserCaption=new TextView(oContext);
//		m_oUserCaption.setLayoutParams( pOPName);
//		m_oUserCaption.setTextSize(16);
//		m_oUserCaption.setText(ListActivityBean.m_strRealName);
//		m_oUserCaption.setTextColor(Color.WHITE);
//		m_oShopCaption.setOnClickListener(new View.OnClickListener()
//		{
//			public void onClick(View v)
//			{
//				//��ת��ϸ����
//			}
//		});
//		oSubHeader.addView(m_oUserCaption);
        //�ŵ�����
        RelativeLayout.LayoutParams pShopName = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT) ;
        pShopName.addRule(RelativeLayout.CENTER_IN_PARENT);
        
        m_oShopCaption=new TextView(oContext);
        m_oShopCaption.setLayoutParams( pShopName);
        m_oShopCaption.setTextSize(16);        
        m_oShopCaption.setText(GOperaterInfo.m_strGroupName);                
        m_oShopCaption.setTextColor(Color.BLACK);
        m_oShopCaption.setEnabled(true);
//        m_oShopCaption.setOnClickListener(new View.OnClickListener()
//      	{
//     		 public void onClick(View v)
//            {
//     			Intent intent = new Intent(MainActivity.m_oMainActivity, GActGroupList.class);
//     			MainActivity.m_oMainActivity.startActivityForResult(intent,MainActivity.USER_GROUP_CHANGE);
//            }
//     	});
        oSubHeader.addView(m_oShopCaption);

        return oSubHeader;
	}	
	public void onScannerResult(String strCardNo,int requestCode)
	{
		if(requestCode== MainActivity.USER_GROUP_CHANGE && m_oShopCaption != null)
				m_oShopCaption.setText(GOperaterInfo.m_strGroupName);
	}




}