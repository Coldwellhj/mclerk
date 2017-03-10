package com.eaosoft.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.eaosoft.mclerk.R;
import com.eaosoft.util.GBaseAdapter;

import java.util.Map;

public class GWareHouseStatisticsGoodsAdapter extends GBaseAdapter {



    public GWareHouseStatisticsGoodsAdapter(Context context) {
        super(context);


    }


    @Override
    public View getView(int position, View arg1, ViewGroup arg2) {
        viewHolder holder;
        Map map =  (Map)ar.get(position);

        if (arg1 == null) {
            holder = new viewHolder();
            arg1 = inflater.inflate(R.layout.ordergoodsdetail, null);
            holder.goodsCaption = (TextView) arg1.findViewById(R.id.goodsCaption);
            holder.goodsNumber = (TextView) arg1.findViewById(R.id.goodsNumber);
            holder.goodsUnitName = (TextView) arg1.findViewById(R.id.goodsUnitName);


            arg1.setTag(holder);
        } else {
            holder = (viewHolder) arg1.getTag();
        }
        if (ar == null)
            return arg1;
        holder.goodsCaption.setText(map.get("goodsCaption").toString());
        holder.goodsNumber.setText(map.get("goodsNumber").toString());
        holder.goodsUnitName.setText(map.get("goodsUnitName").toString());

        //=============================================================================
        return arg1;

    }

    private class viewHolder {

        public TextView goodsCaption = null;
        public TextView goodsNumber = null;
        public TextView goodsUnitName = null;

    }
}