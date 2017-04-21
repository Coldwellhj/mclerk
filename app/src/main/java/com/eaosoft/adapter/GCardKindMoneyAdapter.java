package com.eaosoft.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.eaosoft.mclerk.R;
import com.eaosoft.util.GBaseAdapter;

import java.util.Map;

public class GCardKindMoneyAdapter extends GBaseAdapter
{
	public GCardKindMoneyAdapter(Context context)
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
		
		//±àºÅºÍÃû³Æ
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
			arg1 = inflater.inflate(R.layout.act_card_kind_money_item, null);
			holder.tv_card_money = (TextView) arg1.findViewById(R.id.tv_card_money);

			arg1.setTag(holder);			
		}
		else
		{
			holder = (viewHolder) arg1.getTag();
		}
		if(ar == null)
			return arg1;
		Map map = (Map) ar.get(arg0);


		holder.tv_card_money.setText(map.get("kindMoney").toString()+"ÔªÌ×²Í");


		//=============================================================================
		return arg1;
	}

	private class viewHolder
	{
		private TextView 	tv_card_money;

	}
}