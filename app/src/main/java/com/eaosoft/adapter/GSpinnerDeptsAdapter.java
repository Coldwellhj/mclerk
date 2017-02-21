package com.eaosoft.adapter;

import java.util.Map;

import com.eaosoft.userinfo.GOperaterInfo;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

public class GSpinnerDeptsAdapter extends ArrayAdapter<String> 
{  
    Context context;  
    String[] items = new String[] {};  
  
   public GSpinnerDeptsAdapter(final Context context,  final int textViewResourceId, final String[] objects) 
   {  
        super(context, textViewResourceId, objects);  
        this.items = objects;  
        this.context = context;  
    }  
   public void onSetDatas(final String[] objects)
   {
	   this.items = objects;  
   }
    @Override  
    public View getDropDownView(int position, View convertView,  ViewGroup parent) 
    {  
  
        if (convertView == null) 
        {  
            LayoutInflater inflater = LayoutInflater.from(context);  
            convertView = inflater.inflate(android.R.layout.simple_spinner_dropdown_item, parent, false);  
        }  
  
        TextView tv = (TextView) convertView  .findViewById(android.R.id.text1);  
        tv.setText(items[position]); 
        tv.setGravity(Gravity.CENTER); 
        tv.setTextColor(Color.BLUE);  
        tv.setTextSize(12);  
        return convertView;  
    }  
  
    @Override  
    public View getView(int position, View convertView, ViewGroup parent) 
    {  
        if (convertView == null) 
        {  
            LayoutInflater inflater = LayoutInflater.from(context);  
            convertView = inflater.inflate(android.R.layout.simple_spinner_dropdown_item, parent, false);  
        }  
  
        // android.R.id.text1 is default text view in resource of the android.   
        // android.R.layout.simple_spinner_item is default layout in resources of android.   
  
        TextView tv = (TextView) convertView.findViewById(android.R.id.text1);  
        tv.setText(items[position]); 
        tv.setGravity(Gravity.CENTER); 
        tv.setTextColor(Color.BLUE);  
        tv.setTextSize(12);  
        return convertView;  
    }  
    /*
     *	private Spinner onCreateDeptSpinner(Context	oContext)
  	{
  		Spinner mSpinnerA = new Spinner(oContext);
  		String[]  spinnerList = new String[GOperaterInfo.m_oGroupDepts.size()];
  		
  		Map map = null;
  		int nUser=-1;
  		for(int i=0;i<GOperaterInfo.m_oGroupDepts.size();i++)
  		{
  			map = (Map)GOperaterInfo.m_oGroupDepts.get(i);
  			spinnerList[i]=map.get("serialNo").toString();
  			if(map.get("serialNo").toString().equalsIgnoreCase(GOperaterInfo.m_strDefaultDeptSerialNo))
  				nUser=i;
  		}
  		LinearLayout.LayoutParams pCell =new  LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
  		pCell.setMargins(0, 5,0,0);
  		mSpinnerA.setPadding(8, 8, 8, 8);
  		mSpinnerA.setLayoutParams(pCell);
  		GSpinnerDeptsAdapter myAdapter = new GSpinnerDeptsAdapter(oContext, android.R.layout.simple_spinner_item, spinnerList);  		
  		mSpinnerA.setAdapter(myAdapter);
  		
  		if(nUser>0)
  			mSpinnerA.setSelection(nUser);
  		return mSpinnerA;
  	}
     * 
     * */
}