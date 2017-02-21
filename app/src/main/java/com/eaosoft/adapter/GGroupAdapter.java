package com.eaosoft.adapter;

import java.util.Map;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.eaosoft.mclerk.R;
import com.eaosoft.userinfo.GOperaterInfo;
import com.eaosoft.util.GBaseAdapter;
import com.eaosoft.view.GRoundImageView;

public class GGroupAdapter extends GBaseAdapter
{
	public GGroupAdapter(Context context) 
	{
		super(context);
	}
	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2)
	{
		viewHolder holder;
		if (arg1 == null)
		{
			holder = new viewHolder();
			arg1 = inflater.inflate(R.layout.act_group_item, null);
			holder.m_imgHeader = (GRoundImageView) arg1.findViewById(R.id.imgHeader);
			holder.m_txtCaption= (TextView) arg1.findViewById(R.id.txtCaption);
			holder.m_txtSerialNo= (TextView) arg1.findViewById(R.id.txtSerialNo);
			holder.m_txtBriefing= (TextView) arg1.findViewById(R.id.txtBriefing);
			arg1.setTag(holder);			
		}
		else
		{
			holder = (viewHolder) arg1.getTag();
		}
		if(ar == null)
			return arg1;
		Map map = (Map) ar.get(arg0);

		holder.m_imgHeader.setImageResource(R.drawable.item_goods_logo);
		holder.m_strHeadPicture =map.get("imgLogo").toString();
		holder.m_txtSerialNo.setText(map.get("serialNo").toString());
		holder.m_txtCaption.setText(map.get("caption").toString());		
		holder.m_txtBriefing.setText(map.get("briefing").toString());
		holder.m_imgHeader.onSetHttpImage(holder.m_strHeadPicture,GOperaterInfo.m_strImagePath);				
		//=============================================================================
		return arg1;
	}

	private class viewHolder
	{
		private GRoundImageView 			m_imgHeader;
		private TextView 					m_txtCaption;		
		private TextView 					m_txtSerialNo;
		private TextView 					m_txtBriefing;
		private String 						m_strUID;		
        private String 						m_strHeadPicture;
	}
}