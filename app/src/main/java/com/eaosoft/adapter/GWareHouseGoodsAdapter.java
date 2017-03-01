package com.eaosoft.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.eaosoft.mclerk.R;
import com.eaosoft.util.GBaseAdapter;

import java.util.Map;

public class GWareHouseGoodsAdapter extends GBaseAdapter
{
	public GWareHouseGoodsAdapter(Context context)
	{
		super(context);
	}

	@Override
	public View getView(int position, View arg1, ViewGroup arg2)
	{
		viewHolder holder;
		Map map =(Map)ar.get(position);
		if (arg1 == null)
		{
			holder = new viewHolder();
			arg1 = inflater.inflate(R.layout.warehouse_goods_detail, null);


			holder.m_ogoodsCaption= (TextView) arg1.findViewById(R.id.m_ogoodsCaption);
			holder.m_ogoodsNumber= (TextView) arg1.findViewById(R.id.m_ogoodsNumber);
			holder.m_ogoodsUnitName= (TextView) arg1.findViewById(R.id.m_ogoodsUnitName);

			arg1.setTag(holder);
		}
		else
		{
			holder = (viewHolder) arg1.getTag();
		}
		if(ar == null)
			return arg1;

		holder.m_ogoodsCaption.setText(map.get("goodsCaption").toString());
		holder.m_ogoodsNumber.setText(map.get("goodsNumber").toString());
		holder.m_ogoodsUnitName.setText(map.get("goodsUnitName").toString());
		//=============================================================================
		return arg1;

	}

	private class viewHolder
	{

		public TextView 					m_ogoodsCaption=null;
		public TextView 					m_ogoodsNumber=null;
		public TextView 					m_ogoodsUnitName=null;
	}
}