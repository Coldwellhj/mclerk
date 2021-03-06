package com.eaosoft.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.eaosoft.mclerk.R;
import com.eaosoft.util.GBaseAdapter;

import java.util.List;
import java.util.Map;

public class GCardKind_Goods_detail_mainAdapter extends GBaseAdapter
{
    public List list;

    public GCardKind_Goods_detail_mainAdapter(Context context, List list)
	{
		super(context);
        this.list=list;

	}

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
	public View getView(final int arg0, View arg1,  ViewGroup arg2)
	{
		final viewHolder holder;
		if (arg1 == null)
		{
			holder = new viewHolder();
			arg1 = inflater.inflate(R.layout.activity_gcashier__package_goodsdetail_main, null);
			holder.tv_goodsName = (TextView) arg1.findViewById(R.id.tv_goodsName);
			holder.tv_goodsNum = (TextView) arg1.findViewById(R.id.tv_goodsNum);
			holder.tv_goodsUnit = (TextView) arg1.findViewById(R.id.tv_goodsUnit);

			arg1.setTag(holder);			
		}
		else
		{
			holder = (viewHolder) arg1.getTag();
		}
		if(list == null)
			return arg1;
		final Map map = (Map) list.get(arg0);

		holder.tv_goodsName.setText(map.get("caption").toString());
		holder.tv_goodsNum.setText(map.get("num").toString());
		holder.tv_goodsUnit.setText(map.get("unitName").toString());

		//=============================================================================
		return arg1;
	}

	private class viewHolder
	{

		private TextView 					tv_goodsName;
		private TextView 					tv_goodsNum;
		private TextView 					tv_goodsUnit;
		private TextView 					tv_goodsPrice;
		private ImageView 					iv_goodsDelete;

	}
}