package com.eaosoft.fragment;

import java.util.Calendar;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.TextView;



public class GFragmentFour extends Fragment
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
		ot.setText("GFragmentOne 4");
		m_oUserView.addView(ot);
		return m_oUserView;
	}
}
