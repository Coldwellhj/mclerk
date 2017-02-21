package com.eaosoft.util;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class GBaseAdapter extends BaseAdapter
{
	protected Context context;
	public List ar=null;
	protected LayoutInflater inflater;

	public GBaseAdapter(Context context)
	{
		this.context = context;		
		inflater = LayoutInflater.from(context);
	}
	public void setData(List ar)
	{
		this.ar=ar;
	};
	@Override
	public int getCount()
	{
		if(ar==null)
			return 0;
		return ar.size();
	}

	@Override
	public Object getItem(int arg0)
	{
		if(ar==null)
			return null;
		return ar.get(arg0);
	}

	@Override
	public long getItemId(int arg0)
	{
		// TODO Auto-generated method stub
		return arg0;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		return null;
	}
	public void deleteItem(String strUID)
	{
		Map map;
		if(ar == null)
			return;
		for(int i=0;i<ar.size();i++)
		{
			map = (Map)ar.get(i);
			if(map == null)return;
			if(map.get("uID")==null)
				break;
			if(map.get("uID").toString().equalsIgnoreCase(strUID))
			{
				ar.remove(i);
				break;
			}
		}
	}
}