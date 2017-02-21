package com.eaosoft.util;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;


public class GUINavigationAdapter extends BaseAdapter 
{
	//=============================================================================
	public static final	 int				TEXT_VIEW_ID=1001;
	public static final	 int				IMAGE_VIEW_ID=1000;
	//=============================================================================
	private Context 					m_oContext;
	private String[]						m_oCellID=null; 
	private String[]						m_oCellText=null;
	private int[]							m_oCellImgID=null;
	//=============================================================================
	public GUINavigationAdapter(Context mContext,String[] oCellID,String[] oCellText,int[] oImgID) 
	{
		super();
		this.m_oContext = mContext;
		m_oCellID = oCellID;
		m_oCellText=oCellText;
		m_oCellImgID = oImgID;
	}

	@Override
	public int getCount() 
	{
		if(m_oCellText==null)
			return 0;
		return m_oCellText.length;
	}

	@Override
	public Object getItem(int position) 
	{
		return position;
	}

	@Override
	public long getItemId(int position) 
	{
		return position;
	}
	/*
	private View OnCreateCellView()
	{
		//<!-- 导航页面中的颜色 -->
	    //<color name="gui_navigation_bg">#ffe8ecef</color>
	    //<color name="gui_navigation_text_color">#ff333333</color>
	    //<color name="gui_navigation_line">#ffd3dde6</color>
		RelativeLayout oMain = new RelativeLayout(m_oContext);
		oMain.setLayoutParams( new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT) );
		oMain.setPadding(12,12, 12,12);
		
		oMain.setBackgroundColor(Color.rgb(0xe8, 0xec,0xef));
		
		ImageView oImgView= new ImageView(m_oContext);
		RelativeLayout.LayoutParams pImgView = new RelativeLayout.LayoutParams(58,58);
		pImgView.addRule(RelativeLayout.CENTER_VERTICAL);
		oImgView.setLayoutParams( pImgView);
		oImgView.setContentDescription("我的");
		oImgView.setId(IMAGE_VIEW_ID);
		
		TextView oTxtView = new TextView(m_oContext);
		RelativeLayout.LayoutParams pTxtView = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		pTxtView.addRule(RelativeLayout.CENTER_VERTICAL);
		pTxtView.addRule(RelativeLayout.BELOW,IMAGE_VIEW_ID);
		pTxtView.setMargins(0,5,0,0);
		oTxtView.setLayoutParams(pTxtView);
		oTxtView.setTextSize(14);
		oTxtView.setMaxLines(1);
		oTxtView.setTextColor(Color.rgb(0x33,0x33,0x33));
		
		
		oMain.addView(oImgView);
		oMain.addView(oTxtView);
		
		return oMain ;
	}
	*/
	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		ImageView 	oImgView=null;
		TextView 		oTxtView =null;
		
		if (convertView == null) 
		{
			ListView.LayoutParams pConvertView = new ListView.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);			
			convertView = new RelativeLayout(m_oContext);
			convertView.setLayoutParams( pConvertView);			
			convertView.setBackgroundColor(Color.rgb(0xe8, 0xec,0xef));

			RelativeLayout.LayoutParams pMain =new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.FILL_PARENT) ;
			pMain.addRule(RelativeLayout.CENTER_IN_PARENT);
			RelativeLayout oMain = new RelativeLayout(m_oContext);
			oMain.setLayoutParams( pMain);
			oMain.setPadding(12,12, 12,12);
			//oMain.setBackgroundColor(Color.rgb(0xe8, 0xec,0xef));
			oMain.setBackgroundColor(Color.WHITE);
			
			oImgView= new ImageView(m_oContext);
			RelativeLayout.LayoutParams pImgView = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
			//pImgView.addRule(RelativeLayout.CENTER_VERTICAL);
			pImgView.addRule(RelativeLayout.CENTER_IN_PARENT);
			oImgView.setLayoutParams( pImgView);
			oImgView.setContentDescription("我的");
			oImgView.setId(IMAGE_VIEW_ID);
			
			oTxtView = new TextView(m_oContext);
			RelativeLayout.LayoutParams pTxtView = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
			pTxtView.addRule(RelativeLayout.CENTER_HORIZONTAL);
			pTxtView.addRule(RelativeLayout.BELOW,IMAGE_VIEW_ID);
			pTxtView.setMargins(0,5,0,0);
			oTxtView.setLayoutParams(pTxtView);
			oTxtView.setTextSize(14);
			oTxtView.setMaxLines(1);			
			oTxtView.setTextColor(Color.rgb(0x33,0x33,0x33));
			oTxtView.setId(TEXT_VIEW_ID);
			
			((RelativeLayout)oMain).addView(oImgView);
			((RelativeLayout)oMain).addView(oTxtView);
			
			((RelativeLayout)convertView).addView(oMain);
			//convertView = oMain;
		}
		else
		{
			oTxtView = GBaseViewHolder.get(convertView, TEXT_VIEW_ID);
			oImgView= GBaseViewHolder.get(convertView, IMAGE_VIEW_ID);
		}
		if(m_oCellImgID != null)
			oImgView.setBackgroundResource(m_oCellImgID[position]);
		if(m_oCellText!=null && m_oCellID!=null)
		{
			oTxtView.setText(m_oCellText[position]);
			oTxtView.setTag(m_oCellID[position]);
			oTxtView.setTextColor(Color.rgb(0x33,0x33,0x33));
		}
		convertView.setBackgroundColor(Color.WHITE);
		return convertView;
	}
	
}