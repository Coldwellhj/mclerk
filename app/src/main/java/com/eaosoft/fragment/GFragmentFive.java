package com.eaosoft.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.eaosoft.mclerk.GSalseMgr;
import com.eaosoft.mclerk.GShareholderMgr;
import com.eaosoft.mclerk.MainActivity;



public class GFragmentFive extends Fragment 
{
	private ScrollView			m_oUserView=null;
	private Context 				m_oContext=null;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) 
	{
		m_oContext = inflater.getContext();
		if(MainActivity.m_nOperaterUI==MainActivity.UI_OP_ROLE_SALSE)
		{
			GSalseMgr oSalseMgr = new GSalseMgr(m_oContext);
			return oSalseMgr.OnCreateView();
		}
        if(MainActivity.m_nOperaterUI==MainActivity.UI_OP_ROLE_MANAGER)
        {
            GShareholderMgr oShareholderMgr = new GShareholderMgr(m_oContext);
            return oShareholderMgr.OnCreateView();
        }
		m_oContext = inflater.getContext();
		m_oUserView = new ScrollView(m_oContext);
		m_oUserView.setLayoutParams( new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT) );
		m_oUserView.setFillViewport(true);
        
        LinearLayout layout = new LinearLayout(m_oContext);  //线性布局方式  
        layout.setOrientation( LinearLayout.VERTICAL ); //控件对其方式为垂直排列  VERTICAL
		TextView ot = new TextView(m_oContext);
		ot.setText("GFragmentOne 5");
		m_oUserView.addView(ot);
		return m_oUserView;		
	}
}
