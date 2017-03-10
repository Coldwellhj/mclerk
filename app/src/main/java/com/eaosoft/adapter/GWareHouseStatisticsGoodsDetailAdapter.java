package com.eaosoft.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.eaosoft.mclerk.R;
import com.eaosoft.util.GBaseAdapter;

import java.util.Map;

public class GWareHouseStatisticsGoodsDetailAdapter extends GBaseAdapter {



    public GWareHouseStatisticsGoodsDetailAdapter(Context context) {
        super(context);


    }


    @Override
    public View getView(int position, View arg1, ViewGroup arg2) {
        viewHolder holder;
        Map map =  (Map)ar.get(position);

        if (arg1 == null) {
            holder = new viewHolder();
            arg1 = inflater.inflate(R.layout.statisticsgoodsdetail, null);
            holder.goodsCaption = (TextView) arg1.findViewById(R.id.goodsCaption);
            holder.goodsNumber = (TextView) arg1.findViewById(R.id.goodsNumber);
            holder.goodsUnitName = (TextView) arg1.findViewById(R.id.goodsUnitName);
            holder.m_otxtRoomNo = (TextView) arg1.findViewById(R.id.m_otxtRoomNo);
            holder.m_ocardNumber = (TextView) arg1.findViewById(R.id.m_ocardNumber);
            holder.m_oorderTime = (TextView) arg1.findViewById(R.id.m_oorderTime);
            holder.m_osalseman = (TextView) arg1.findViewById(R.id.m_osalseman);
            arg1.setTag(holder);
        } else {
            holder = (viewHolder) arg1.getTag();
        }
        if (ar == null)
            return arg1;
        holder.goodsCaption.setText(map.get("goodsCaption").toString());
        holder.goodsNumber.setText(map.get("goodsNumber").toString());
        holder.goodsUnitName.setText(map.get("goodsUnitName").toString());
        holder.m_otxtRoomNo.setText(map.get("roomSerialNo").toString());
        holder.m_ocardNumber.setText(map.get("cardUID").toString());
        holder.m_oorderTime.setText(map.get("orderTime").toString());
        holder.m_osalseman.setText(map.get("userCaption").toString());
        holder.goodsCaption.setTag(map);
        //=============================================================================
        return arg1;

    }

    private class viewHolder {

        public TextView goodsCaption = null;
        public TextView goodsNumber = null;
        public TextView goodsUnitName = null;
        public TextView m_otxtRoomNo = null;
        public TextView m_ocardNumber = null;
        public TextView m_oorderTime = null;
        public TextView m_osalseman = null;
    }
}