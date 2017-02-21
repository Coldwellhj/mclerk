package com.eaosoft.adapter;

import java.util.Map;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

import com.eaosoft.mclerk.MainActivity;
import com.eaosoft.mclerk.R;
import com.eaosoft.userinfo.GOperaterInfo;
import com.eaosoft.util.GBaseAdapter;
import com.eaosoft.util.GUtilFile;
import com.eaosoft.util.GUtilSDCard;
import com.eaosoft.util.XImageDownLoader;
import com.eaosoft.util.XImageDownLoader.DownLoaderListener;
import com.eaosoft.view.GRoundImageView;
import com.eaosoft.view.RoundImageView;

public class GCardKindAdapter extends GBaseAdapter
{
	public GCardKindAdapter(Context context) 
	{
		super(context);
	}
	/*
	private viewHolder onCreateViewHolder(Context oContext)
	{
		viewHolder holder = new viewHolder();
		//==========================================================
		LinearLayout oMainWnd = new LinearLayout(oContext);
		oMainWnd.setOrientation( LinearLayout.VERTICAL);
		oMainWnd.setBackgroundColor(Color.rgb(0,0,0));
		oMainWnd.setLayoutParams( new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT) );
		
		//编号和名称
		LinearLayout oSubHeader = new LinearLayout(oContext);
		oSubHeader.setOrientation( LinearLayout.HORIZONTAL);
		oSubHeader.setBackgroundColor(Color.rgb(0,0,0));
		oSubHeader.setLayoutParams( new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT) );
		//==========================================================
		oMainWnd.addView(oSubHeader);
		return holder;
	}
	*/	
	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2)
	{
		viewHolder holder;
		if (arg1 == null)
		{
			holder = new viewHolder();
			arg1 = inflater.inflate(R.layout.act_card_kind_item, null);
			holder.m_imgHeader = (GRoundImageView) arg1.findViewById(R.id.imgHeader);
			holder.m_txtCaption= (TextView) arg1.findViewById(R.id.txtCaption);		
			holder.m_txtSerialNo= (TextView) arg1.findViewById(R.id.txtSerialNo);
			holder.m_txtBriefing= (TextView) arg1.findViewById(R.id.txtBriefing);
			holder.m_txtTotalMoney= (TextView) arg1.findViewById(R.id.txtTotalMoney);
			holder.m_txtDateRange= (TextView) arg1.findViewById(R.id.txtDateRange);//有效期
			holder.m_btnEnable= (Button) arg1.findViewById(R.id.btn_kind_enable);
			holder.m_btnDelete= (Button) arg1.findViewById(R.id.btn_kind_delete);		
			holder.m_oUserCardKindMgr= (LinearLayout) arg1.findViewById(R.id.user_card_kind_mgr);
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
		holder.m_txtTotalMoney.setText(map.get("totalMoney").toString());
		holder.m_txtDateRange.setText(map.get("dateEnd").toString());//有效期
		holder.m_imgHeader.onSetHttpImage(holder.m_strHeadPicture,GOperaterInfo.m_strImagePath);		
		holder.m_btnEnable.setTag(map);
		holder.m_btnDelete.setTag(map);
		if(holder.m_oUserCardKindMgr != null && MainActivity.m_nOperaterUI!=MainActivity.UI_OP_ROLE_CASHIER)
			holder.m_oUserCardKindMgr.setVisibility(View.GONE);
		if(map.get("enabled").toString().equalsIgnoreCase("1"))
		{
			holder.m_btnEnable.setTextColor(Color.rgb(0, 0, 0));
			holder.m_btnEnable.setText("禁用");
		}
		else
		{
			holder.m_btnEnable.setText("启用");
			holder.m_btnEnable.setTextColor(Color.rgb(255, 0, 0));
		}
		//=============================================================================
		return arg1;
	}

	private class viewHolder
	{
		private GRoundImageView 	m_imgHeader;
		private LinearLayout			m_oUserCardKindMgr;
		private Button						m_btnEnable;
		private Button						m_btnDelete;
		private TextView 					m_txtCaption;		
		private TextView 					m_txtSerialNo;
		private TextView 					m_txtBriefing;
		private TextView 					m_txtTotalMoney;
		private TextView 					m_txtDateRange;//有效期
		private String 						m_strUID;		
        private String 						m_strHeadPicture;
	}
}