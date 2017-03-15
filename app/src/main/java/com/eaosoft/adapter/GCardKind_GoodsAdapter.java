package com.eaosoft.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.eaosoft.mclerk.R;
import com.eaosoft.util.GBaseAdapter;

import java.util.List;
import java.util.Map;

public class GCardKind_GoodsAdapter extends GBaseAdapter
{
    public List list;
	public GCardKind_GoodsAdapter(Context context,List list)
	{
		super(context);
        this.list=list;
	}

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
	public View getView(int arg0, View arg1, ViewGroup arg2)
	{
		viewHolder holder;
		if (arg1 == null)
		{
			holder = new viewHolder();
			arg1 = inflater.inflate(R.layout.activity_gcashier__package_goods, null);
			holder.tv_package_goodsName = (TextView) arg1.findViewById(R.id.tv_package_goodsName);

			arg1.setTag(holder);			
		}
		else
		{
			holder = (viewHolder) arg1.getTag();
		}
		if(list == null)
			return arg1;
		Map map = (Map) list.get(arg0);

		holder.tv_package_goodsName.setText(map.get("caption").toString()+"¡¾"+map.get("unitName").toString()+"¡¿");

		//=============================================================================
		return arg1;
	}

	private class viewHolder
	{

		private TextView 					tv_package_goodsName;

	}
}