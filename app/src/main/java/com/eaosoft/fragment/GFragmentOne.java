package com.eaosoft.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.TextView;

import com.eaosoft.mclerk.GActCardCreate;
import com.eaosoft.mclerk.GActCardCreateOrder;
import com.eaosoft.mclerk.GActWebView;
import com.eaosoft.mclerk.GCashierMain;
import com.eaosoft.mclerk.GSalseMain;
import com.eaosoft.mclerk.GShareholderMain;
import com.eaosoft.mclerk.GWareHouseMain;
import com.eaosoft.mclerk.MainActivity;
import com.eaosoft.userinfo.GOperaterInfo;
import com.eaosoft.util.GSvrChannel;

import static com.eaosoft.mclerk.MainActivity.UI_OP_ROLE_CASHIER;
import static com.eaosoft.mclerk.MainActivity.UI_OP_ROLE_MANAGER;
import static com.eaosoft.mclerk.MainActivity.UI_OP_ROLE_SALSE;
import static com.eaosoft.mclerk.MainActivity.UI_OP_ROLE_STORE;


public class GFragmentOne extends Fragment
{
	private GSalseMain 				m_oSalseMain=null;
	private GCashierMain			m_oCashierMain=null;
	private GWareHouseMain			m_oWareHouseMain=null;
	private GShareholderMain			m_oShareholderMain=null;
	private ScrollView				m_oUserView=null;
	private Context 					m_oContext=null;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) 
	{
		m_oContext = inflater.getContext();
        if(GOperaterInfo.m_strRoleID.equals("RCashier")){
            MainActivity.m_nOperaterUI = UI_OP_ROLE_CASHIER;
        }else if(GOperaterInfo.m_strRoleID.equals("RStocker")){
            MainActivity.m_nOperaterUI = UI_OP_ROLE_STORE;
        }else if(GOperaterInfo.m_strRoleID.equals("RSale")){
            MainActivity.m_nOperaterUI = UI_OP_ROLE_SALSE;
        }else if(GOperaterInfo.m_strRoleID.equals("RAdmin")){
            MainActivity.m_nOperaterUI = UI_OP_ROLE_MANAGER;
        }
		if(MainActivity.m_nOperaterUI== UI_OP_ROLE_SALSE)//这个是销售员
		{
			m_oSalseMain = new GSalseMain(m_oContext);
			m_oSalseMain.m_oFragmentOne = this;
			return m_oSalseMain.OnCreateView();
		}
		if(MainActivity.m_nOperaterUI== UI_OP_ROLE_CASHIER)//这个是收银员
		{
			m_oCashierMain = new GCashierMain(m_oContext);
			m_oCashierMain.m_oFragmentOne = this;
			return m_oCashierMain.OnCreateView();
		}
		if(MainActivity.m_nOperaterUI== UI_OP_ROLE_STORE)//这个是仓管员
		{
			m_oWareHouseMain = new GWareHouseMain(m_oContext);
			m_oWareHouseMain.m_oFragmentOne = this;
			return m_oWareHouseMain.OnCreateView();


		}
        if(MainActivity.m_nOperaterUI== UI_OP_ROLE_MANAGER)//这个是股东
        {
            m_oShareholderMain = new GShareholderMain(m_oContext);
            m_oShareholderMain.m_oFragmentOne = this;
            m_oShareholderMain.getshopSalseStatistics();
            return m_oShareholderMain.OnCreateView();

        }
		m_oUserView = new ScrollView(m_oContext);
		m_oUserView.setLayoutParams( new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT) );
		m_oUserView.setFillViewport(true);
		
        
        //LinearLayout layout = new LinearLayout(m_oContext);  //线性布局方式  
        //layout.setOrientation( LinearLayout.VERTICAL ); //控件对其方式为垂直排列  VERTICAL
		TextView ot = new TextView(m_oContext);
		ot.setText("GFragmentOne 1");
		m_oUserView.addView(ot);
		return m_oUserView;
	}
	public void OnRefresh()
	{
		if(MainActivity.m_nOperaterUI== UI_OP_ROLE_STORE)//这个是仓管员
		{
			if(m_oWareHouseMain!=null)
				m_oWareHouseMain.OnRefresh();
		}
	}
	public void onScannerResult(String strCardNo,int requestCode)
	{
		switch(requestCode)
		{
			case MainActivity.SCAN_CODE_ORDER_CREATE://	  	= 0x01;//扫码下单
			{				
				Intent intent = new Intent(MainActivity.m_oMainActivity, GActCardCreateOrder.class);

				intent.putExtra("cardNo", strCardNo);
	        	MainActivity.m_oMainActivity.startActivity(intent);	
			}break;
			case MainActivity.SCAN_CODE_CAED_INFO://	  		= 0x02;//扫码查询
			{
				String strURL=GSvrChannel.m_strURLCardRemain+"?token="+GOperaterInfo.m_strToken+"&cardNo="+strCardNo+"&callerName="+GSvrChannel.CALLER_NAME;
				String strTitle ="卡"+strCardNo+"的余额";
				Intent intent = new Intent(MainActivity.m_oMainActivity, GActWebView.class);
				intent.putExtra("startURL", strURL);
				intent.putExtra("strTitle", strTitle);
	        	MainActivity.m_oMainActivity.startActivity(intent);
			}break;
			case MainActivity.SCAN_CODE_CAED_CONSUME://	= 0x03;//扫码查询消费记录
			{
				String strURL=GSvrChannel.m_strURLCardConsume+"?token="+GOperaterInfo.m_strToken+"&cardNo="+strCardNo+"&callerName="+GSvrChannel.CALLER_NAME;
				String strTitle ="卡"+strCardNo+"消费记录";
				Intent intent = new Intent(MainActivity.m_oMainActivity, GActWebView.class);
				intent.putExtra("startURL", strURL);
				intent.putExtra("strTitle", strTitle);
	        	MainActivity.m_oMainActivity.startActivity(intent);				
			}break;
			case MainActivity.SCAN_CODE_CAED_CREATE://	  	= 0x04;//扫码建新卡
			{
				Intent intent = new Intent(MainActivity.m_oMainActivity, GActCardCreate.class);
				intent.putExtra("cardNo", strCardNo);
	        	MainActivity.m_oMainActivity.startActivity(intent);								
			}break;
			
		}//end of switch
		if(MainActivity.m_nOperaterUI== UI_OP_ROLE_SALSE && m_oSalseMain!=null)//这个是销售员
			m_oSalseMain.onScannerResult(strCardNo, requestCode);
		if(MainActivity.m_nOperaterUI== UI_OP_ROLE_CASHIER && m_oCashierMain!=null)//这个是收银员
			m_oCashierMain.onScannerResult(strCardNo, requestCode);
		if(MainActivity.m_nOperaterUI== UI_OP_ROLE_STORE && m_oWareHouseMain!=null)//这个是仓管员
			m_oWareHouseMain.onScannerResult(strCardNo, requestCode);
        if(MainActivity.m_nOperaterUI== UI_OP_ROLE_MANAGER && m_oShareholderMain!=null)//这个是股东
            m_oShareholderMain.onScannerResult(strCardNo, requestCode);
	}
	public void printLogin(String strCardNo,int requestCode){
	}
}
