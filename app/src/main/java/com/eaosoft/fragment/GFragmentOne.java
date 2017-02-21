package com.eaosoft.fragment;

import com.eaosoft.mclerk.GActCardCreate;
import com.eaosoft.mclerk.GActCardCreateOrder;
import com.eaosoft.mclerk.GActWebView;
import com.eaosoft.mclerk.GCashierMain;
import com.eaosoft.mclerk.GSalseMain;
import com.eaosoft.mclerk.MainActivity;
import com.eaosoft.mclerk.GWareHouseMain;
import com.eaosoft.userinfo.GOperaterInfo;
import com.eaosoft.util.GSvrChannel;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;



public class GFragmentOne extends Fragment 
{
	private GSalseMain 				m_oSalseMain=null;
	private GCashierMain			m_oCashierMain;
	private GWareHouseMain			m_oWareHouseMain;
	private ScrollView				m_oUserView=null;
	private Context 					m_oContext=null;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) 
	{
		m_oContext = inflater.getContext();
		if(MainActivity.m_nOperaterUI==MainActivity.UI_OP_ROLE_SALSE)//���������Ա
		{
			m_oSalseMain = new GSalseMain(m_oContext);
			m_oSalseMain.m_oFragmentOne = this;
			return m_oSalseMain.OnCreateView();
		}
		if(MainActivity.m_nOperaterUI==MainActivity.UI_OP_ROLE_CASHIER)//���������Ա
		{
			m_oCashierMain = new GCashierMain(m_oContext);
			m_oCashierMain.m_oFragmentOne = this;
			return m_oCashierMain.OnCreateView();
		}
		if(MainActivity.m_nOperaterUI==MainActivity.UI_OP_ROLE_STORE)//����ǲֹ�Ա
		{
			m_oWareHouseMain = new GWareHouseMain(m_oContext);
			m_oWareHouseMain.m_oFragmentOne = this;
			return m_oWareHouseMain.OnCreateView();
		}
		m_oUserView = new ScrollView(m_oContext);
		m_oUserView.setLayoutParams( new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT) );
		m_oUserView.setFillViewport(true);
		
        
        //LinearLayout layout = new LinearLayout(m_oContext);  //���Բ��ַ�ʽ  
        //layout.setOrientation( LinearLayout.VERTICAL ); //�ؼ����䷽ʽΪ��ֱ����  VERTICAL
		TextView ot = new TextView(m_oContext);
		ot.setText("GFragmentOne 1");
		m_oUserView.addView(ot);
		return m_oUserView;
	}	
	public void onScannerResult(String strCardNo,int requestCode)
	{
		switch(requestCode)
		{
			case MainActivity.SCAN_CODE_ORDER_CREATE://	  	= 0x01;//ɨ���µ�
			{				
				Intent intent = new Intent(MainActivity.m_oMainActivity, GActCardCreateOrder.class);
				intent.putExtra("cardNo", strCardNo);
	        	MainActivity.m_oMainActivity.startActivity(intent);	
			}break;
			case MainActivity.SCAN_CODE_CAED_INFO://	  		= 0x02;//ɨ���ѯ
			{
				String strURL=GSvrChannel.m_strURLCardRemain+"?token="+GOperaterInfo.m_strToken+"&cardNo="+strCardNo+"&callerName="+GSvrChannel.CALLER_NAME;
				String strTitle ="��"+strCardNo+"�����";
				Intent intent = new Intent(MainActivity.m_oMainActivity, GActWebView.class);
				intent.putExtra("startURL", strURL);
				intent.putExtra("strTitle", strTitle);
	        	MainActivity.m_oMainActivity.startActivity(intent);
			}break;
			case MainActivity.SCAN_CODE_CAED_CONSUME://	= 0x03;//ɨ���ѯ���Ѽ�¼
			{
				String strURL=GSvrChannel.m_strURLCardConsume+"?token="+GOperaterInfo.m_strToken+"&cardNo="+strCardNo+"&callerName="+GSvrChannel.CALLER_NAME;
				String strTitle ="��"+strCardNo+"���Ѽ�¼";
				Intent intent = new Intent(MainActivity.m_oMainActivity, GActWebView.class);
				intent.putExtra("startURL", strURL);
				intent.putExtra("strTitle", strTitle);
	        	MainActivity.m_oMainActivity.startActivity(intent);				
			}break;
			case MainActivity.SCAN_CODE_CAED_CREATE://	  	= 0x04;//ɨ�뽨�¿�
			{
				Intent intent = new Intent(MainActivity.m_oMainActivity, GActCardCreate.class);
				intent.putExtra("cardNo", strCardNo);
	        	MainActivity.m_oMainActivity.startActivity(intent);								
			}break;
			
		}//end of switch
		if(MainActivity.m_nOperaterUI==MainActivity.UI_OP_ROLE_SALSE && m_oSalseMain!=null)//���������Ա
			m_oSalseMain.onScannerResult(strCardNo, requestCode);
		if(MainActivity.m_nOperaterUI==MainActivity.UI_OP_ROLE_CASHIER && m_oCashierMain!=null)//���������Ա
			m_oCashierMain.onScannerResult(strCardNo, requestCode);
		if(MainActivity.m_nOperaterUI==MainActivity.UI_OP_ROLE_STORE && m_oWareHouseMain!=null)//����ǲֹ�Ա
			m_oWareHouseMain.onScannerResult(strCardNo, requestCode);
	}
}
