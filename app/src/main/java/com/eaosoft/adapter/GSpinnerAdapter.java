package com.eaosoft.adapter;

import java.util.Map;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;

public class GSpinnerAdapter extends ArrayAdapter<String> 
{  
    Context context;  
    String[] items = new String[] {};  
  
   public GSpinnerAdapter(final Context context,  final int textViewResourceId, final String[] objects) 
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
     * 
    private Spinner onCreateUserNumSpinner(Context	oContext,int nMaxNum,String strUnitName)
  	{
  		Spinner mSpinnerA = new Spinner(oContext);
  		String[]  spinnerList = new String[nMaxNum+1];
  		
  		for(int i=0;i<=nMaxNum;i++)
  			spinnerList[i]=(""+i);
  		GSpinnerAdapter myAdapter = new GSpinnerAdapter(oContext, android.R.layout.simple_spinner_item, spinnerList);  		
  		mSpinnerA.setAdapter(myAdapter);
  		return mSpinnerA;
  	}
     * 
     *建立--------------------------------------------------------------------------
  			if(map.get("oID").toString().length()<1)//这个是原生的
  			{
	  			Spinner oSpinner;
	  			int nMaxNum=0;
	  			int nUserNum=0;
	  			nMaxNum = Integer.parseInt(map.get("goodsNum").toString());
	  			nUserNum = Integer.parseInt(map.get("numUser").toString());
	  			oSpinner = onCreateUserNumSpinner(oContext,nMaxNum,map.get("unitName").toString());
	  			oSpinner.setSelection(nUserNum);
	  			oSpinner.setLayoutParams(pCell);
	  			//oSpinner(24*MainActivity.m_nPixelsFromDP);
	  			oSpinner.setTag(map);
	  			oSpinner.setId(4000+nLineNum);
	  			oSpinner.setOnItemSelectedListener(new SpinnerOnSelectedListener());
	  			m_oSpinnerList.add(oSpinner);
	  	  		oHead.addView(oSpinner);
  			}
  			else
  			{
  				txtNumR = new TextView(oContext);
  	  			txtNumR.setLayoutParams(pCell);  			
  	  			txtNumR.setText(map.get("goodsNum").toString());
  	  			txtNumR.setTextSize(m_nFontSize*MainActivity.m_nPixelsFromDP);  	  		
  	  	  		oHead.addView(txtNumR);
  			}  			
     * 建立--------------------------------------------------------------------------
     * class SpinnerOnSelectedListener implements OnItemSelectedListener
  	{
        public void onItemSelected(AdapterView<?> adapterView, View view, int position,long id) 
        {
            // TODO Auto-generated method stub
            String selected = adapterView.getItemAtPosition(position).toString();
            System.out.println("selected===========>" + selected+","+adapterView.getTag().toString());
            onSetUserGoodsNum(adapterView.getTag().toString(),selected,adapterView.getId()-4000);
        }

        public void onNothingSelected(AdapterView<?> arg0) {
            // TODO Auto-generated method stub
            System.out.println("selected===========>" + "Nothing");
        }
    }
     * 
     //只有Spinner才会调用这个
  	private void onSetUserGoodsNum(String strUID,String strUserNum,int nLineNum)
  	{
  		Spinner oSpinner=null;
  		Map		 map=null;
  		
  		if(nLineNum<1 || nLineNum>m_oSpinnerList.size())
  			return;
  		oSpinner = (Spinner)m_oSpinnerList.get(nLineNum-1);
  		map = (Map)oSpinner.getTag();  	
  		
  		TextView tv=(TextView)m_oRemainList.get(nLineNum-1);
  		if(map ==null || oSpinner ==null || tv == null)
  			return;
  		map.put("numUser", "0");
  		int nGoodsNum= Integer.parseInt(map.get("goodsNum").toString());//总量
		int nUsedNum=onGetGoodsNumUsed(map.get("uID").toString());//用量
  		int nUserNum= Integer.parseInt(strUserNum);
		//Log.e("onSetUserGoodsNum", "nGoodsNum:"+nGoodsNum+",nUsedNum:"+nUsedNum+",strUserNum:"+strUserNum);
		if(nGoodsNum<(nUsedNum+nUserNum))
		{
			nUserNum = nGoodsNum - nUsedNum;
			oSpinner.setSelection(nUserNum);
		}
		if(tv!=null)
			tv.setText(""+(nGoodsNum-nUsedNum-nUserNum));
		map.put("numUser", ""+nUserNum);
  	}
     * */
}