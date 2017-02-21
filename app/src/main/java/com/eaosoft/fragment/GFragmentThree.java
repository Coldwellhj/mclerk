package com.eaosoft.fragment;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;


public class GFragmentThree extends Fragment 
{
	private ScrollView			m_oUserView=null;
	private Context 				m_oContext=null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) 
	{
		m_oContext = inflater.getContext();
		m_oUserView = new ScrollView(m_oContext);
		m_oUserView.setLayoutParams( new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT) );
		m_oUserView.setFillViewport(true);
        
        LinearLayout layout = new LinearLayout(m_oContext);  //线性布局方式  
        layout.setOrientation( LinearLayout.VERTICAL ); //控件对其方式为垂直排列  VERTICAL
		TextView ot = new TextView(m_oContext);
		ot.setText("GFragmentOne 3");
		m_oUserView.addView(ot);
		return m_oUserView;
	}

	
}
