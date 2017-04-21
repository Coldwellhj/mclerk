package com.eaosoft.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.eaosoft.mclerk.R;
import com.eaosoft.util.GBaseAdapter;

import java.util.Map;

public class GWareHouseShopHistoryRoomAdapter extends GBaseAdapter {


    public GWareHouseShopHistoryRoomAdapter(Context context) {
        super(context);

    }

    @Override
    public View getView(int position, View arg1, ViewGroup arg2) {
        viewHolder holder;
        Map map = (Map) ar.get(position);
        if (arg1 == null) {
            holder = new viewHolder();
            arg1 = inflater.inflate(R.layout.warehouse_shophistoryroom, null);
            holder.shopHistoryRoom = (TextView) arg1.findViewById(R.id.shopHistoryRoom);

            arg1.setTag(holder);
        } else {
            holder = (viewHolder) arg1.getTag();
        }
        if (ar == null)
            return arg1;
        holder.shopHistoryRoom.setText(map.get("roomSerialNo").toString());
        holder.shopHistoryRoom.setTag(map);
//        if(position%2==0)
//            holder.orderNumber.setBackgroundColor(Color.RED);
//        else
//            holder.orderNumber.setBackgroundColor(Color.BLUE);
        //=============================================================================
        return arg1;

    }

    private class viewHolder {

        public TextView shopHistoryRoom = null;

    }
}