package com.eaosoft.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.eaosoft.mclerk.R;
import com.eaosoft.util.GBaseAdapter;

import java.util.Map;

public class GCashierSalesReportAdapter extends GBaseAdapter {



    public GCashierSalesReportAdapter(Context context) {
        super(context);


    }


    @Override
    public View getView(int position, View arg1, ViewGroup arg2) {
        viewHolder holder;
        Map map =  (Map)ar.get(position);
        if (arg1 == null) {
            holder = new viewHolder();
            arg1 = inflater.inflate(R.layout.cashier_sales_report, null);
            holder.cardNum = (TextView) arg1.findViewById(R.id.cardNum);
            holder.tv_time = (TextView) arg1.findViewById(R.id.tv_time);
            holder.sales_name = (TextView) arg1.findViewById(R.id.sales_name);

            holder.money = (TextView) arg1.findViewById(R.id.money);
            arg1.setTag(holder);
        } else {
            holder = (viewHolder) arg1.getTag();
        }
        if (ar == null)
            return arg1;
        holder.cardNum.setText(map.get("serialNo").toString());
        holder.tv_time.setText(map.get("buyTime").toString());
        holder.sales_name.setText(map.get("caption").toString());
        holder.money.setText(map.get("realMoney").toString());
        holder.cardNum.setTag(map);
        //=============================================================================
        return arg1;

    }

    private class viewHolder {

        public TextView cardNum = null;
        public TextView tv_time = null;
        public TextView sales_name = null;
        public TextView money = null;

    }
}