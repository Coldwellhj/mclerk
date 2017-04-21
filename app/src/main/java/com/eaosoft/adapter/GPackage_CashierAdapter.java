package com.eaosoft.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.eaosoft.mclerk.R;
import com.eaosoft.util.GBaseAdapter;

import java.util.List;
import java.util.Map;

import static com.eaosoft.mclerk.R.id.number;

public class GPackage_CashierAdapter extends GBaseAdapter {


    public GPackage_CashierAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(int arg0, View arg1, ViewGroup arg2) {
        viewHolder holder;
        if (arg1 == null) {
            holder = new viewHolder();
            arg1 = inflater.inflate(R.layout.act_package_cashier_item, null);
            holder.orderNumber = (TextView) arg1.findViewById(R.id.orderNumber);
            holder.packageName = (TextView) arg1.findViewById(R.id.packageName);
            holder.drinksName = (TextView) arg1.findViewById(R.id.drinksName);
            holder.unit = (TextView) arg1.findViewById(R.id.unit);
            holder.number = (TextView) arg1.findViewById(number);
            holder.money = (TextView) arg1.findViewById(R.id.money);
            holder.editor = (TextView) arg1.findViewById(R.id.editor);
            holder.btn_kind_enable = (Button) arg1.findViewById(R.id.btn_kind_enable);
            holder.btn_kind_modify = (Button) arg1.findViewById(R.id.btn_kind_modify);
            holder.btn_kind_delete = (Button) arg1.findViewById(R.id.btn_kind_delete);

            arg1.setTag(holder);
        } else {
            holder = (viewHolder) arg1.getTag();
        }
        if (ar == null)
            return arg1;
        Map map = (Map) ar.get(arg0);

        holder.orderNumber.setText("");

        holder.packageName.setText(map.get("caption").toString());
        holder.drinksName.setText(((Map)((List)map.get("ar1")).get(0)).get("caption").toString());
        holder.unit.setText(((Map)((List)map.get("ar1")).get(0)).get("unitName").toString());
        holder.number.setText(((Map)((List)map.get("ar1")).get(0)).get("num").toString());
        holder.money.setText(map.get("totalMoney").toString());
        holder.editor.setText(map.get("dateEnd").toString());

//        holder.m_txtCaption.setTag(map);
//        holder.m_btnEnable.setTag(map);
//        holder.m_btnDelete.setTag(map);
        holder.btn_kind_enable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        if (map.get("enabled").toString().equalsIgnoreCase("1")) {
            holder.btn_kind_enable.setTextColor(Color.rgb(255, 255, 255));
            holder.btn_kind_enable.setText("Ω˚”√");
        } else {
            holder.btn_kind_enable.setText("∆Ù”√");
            holder.btn_kind_enable.setTextColor(Color.rgb(255, 0, 0));
        }
        //=============================================================================
        return arg1;
    }

    private class viewHolder {
        private TextView orderNumber;
        private TextView packageName;
        private TextView drinksName;
        private TextView unit;
        private TextView number;
        private TextView money;
        private TextView editor;
        private Button btn_kind_enable;
        private Button btn_kind_modify;
        private Button btn_kind_delete;
    }
}