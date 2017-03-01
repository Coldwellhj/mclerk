package com.eaosoft.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.eaosoft.mclerk.MainActivity;
import com.eaosoft.mclerk.R;
import com.eaosoft.util.GBaseAdapter;

import java.util.Map;

public class GWareHouseAdapter extends GBaseAdapter
{
	public GWareHouseAdapter(Context context)
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
			arg1 = inflater.inflate(R.layout.warehouse_detail, null);

			holder.orderNumber = (TextView) arg1.findViewById(R.id.orderNumber);
			holder.m_otxtRoomNo= (TextView) arg1.findViewById(R.id.m_otxtRoomNo);
			holder.m_ocardNumber= (TextView) arg1.findViewById(R.id.m_ocardNumber);
			holder.m_oorderTime= (TextView) arg1.findViewById(R.id.m_oorderTime);
			holder.m_osalseman= (TextView) arg1.findViewById(R.id.m_osalseman);
			holder.m_oisPrintTask= (TextView) arg1.findViewById(R.id.m_oisPrintTask);

			holder.orderNumber.setLayoutParams(new LinearLayout.LayoutParams(MainActivity.mSreenWidth*3/4/5, ViewGroup.LayoutParams.WRAP_CONTENT));
			holder.m_otxtRoomNo.setLayoutParams(new LinearLayout.LayoutParams(MainActivity.mSreenWidth*3/4/5, ViewGroup.LayoutParams.WRAP_CONTENT));
			holder.m_ocardNumber.setLayoutParams(new LinearLayout.LayoutParams(MainActivity.mSreenWidth*3/4/5, ViewGroup.LayoutParams.WRAP_CONTENT));
			holder.m_oorderTime.setLayoutParams(new LinearLayout.LayoutParams(MainActivity.mSreenWidth*3/4/5, ViewGroup.LayoutParams.WRAP_CONTENT));
			holder.m_osalseman.setLayoutParams(new LinearLayout.LayoutParams(MainActivity.mSreenWidth*3/4/5, ViewGroup.LayoutParams.WRAP_CONTENT));

			arg1.setTag(holder);
		}
		else
		{
			holder = (viewHolder) arg1.getTag();
		}
		if(ar == null)
			return arg1;
		holder.orderNumber.setText(map.get("orderUID").toString());
		holder.m_otxtRoomNo.setText(map.get("roomSerialNo").toString());
		holder.m_ocardNumber.setText(map.get("cardUID").toString());
		holder.m_oorderTime.setText(map.get("orderTime").toString());
		holder.m_osalseman.setText(map.get("userCaption").toString());
		holder.m_oisPrintTask.setText(map.get("taskUID").toString());

		//=============================================================================
		return arg1;

	}

	private class viewHolder
	{

		public TextView 					orderNumber=null;
		public TextView 					m_otxtRoomNo=null;
		public TextView 					m_ocardNumber=null;
		public TextView 					m_oorderTime=null;
		public TextView 					m_osalseman=null;
		public TextView 					m_oisPrintTask=null;

	}
}