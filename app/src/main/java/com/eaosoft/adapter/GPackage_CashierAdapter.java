package com.eaosoft.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.eaosoft.mclerk.R;
import com.eaosoft.util.GBaseAdapter;
import com.eaosoft.util.ListViewUtil;
import com.eaosoft.util.NewPackageListItemClickHelp;

import java.util.List;
import java.util.Map;

public class GPackage_CashierAdapter extends GBaseAdapter {

    private NewPackageListItemClickHelp newPackageListItemClickHelp;
    public GPackage_CashierAdapter(Context context, NewPackageListItemClickHelp newPackageListItemClickHelp) {
        super(context);
        this.newPackageListItemClickHelp=newPackageListItemClickHelp;
    }

    @Override
    public View getView(final int arg0, View arg1, ViewGroup arg2) {
        final viewHolder holder;
        if (arg1 == null) {
            holder = new viewHolder();
            arg1 = inflater.inflate(R.layout.act_package_cashier_item, null);
            holder.orderNumber = (TextView) arg1.findViewById(R.id.orderNumber);
            holder.packageName = (TextView) arg1.findViewById(R.id.packageName);
//            holder.drinksName = (TextView) arg1.findViewById(R.id.drinksName);
//            holder.unit = (TextView) arg1.findViewById(R.id.unit);
//            holder.number = (TextView) arg1.findViewById(number);
            holder.lv_card_kind_goods_detail = (ListView) arg1.findViewById(R.id.lv_card_kind_goods_detail);
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

        holder.orderNumber.setText(map.get("orderNo").toString());

        holder.packageName.setText(map.get("caption").toString());
//        for(int i=0;i<((List)map.get("ar1")).size();i++){
//            holder.drinksName.setText(((Map)((List)map.get("ar1")).get(i)).get("caption").toString());
//            holder.unit.setText(((Map)((List)map.get("ar1")).get(i)).get("unitName").toString());
//            holder.number.setText(((Map)((List)map.get("ar1")).get(i)).get("num").toString());
//        }
        GCardKind_Goods_detail_mainAdapter goodsDetailAdapter = new GCardKind_Goods_detail_mainAdapter(context,(List)map.get("ar1"));
        holder.lv_card_kind_goods_detail.setAdapter(goodsDetailAdapter);
        ListViewUtil.setListViewHeightBasedOnChildren(holder.lv_card_kind_goods_detail);
        goodsDetailAdapter.notifyDataSetChanged();
        holder.money.setText(map.get("totalMoney").toString());
        holder.editor.setText(map.get("operaterName").toString());

//        holder.m_txtCaption.setTag(map);
        holder.btn_kind_enable.setTag(map);
        holder.btn_kind_delete.setTag(map);
        holder.btn_kind_enable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newPackageListItemClickHelp.onEnableCardKind( holder.btn_kind_enable);
            }
        });
        holder.btn_kind_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newPackageListItemClickHelp.onDeleteCardKind( holder.btn_kind_delete);
            }
        });
        holder.btn_kind_modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newPackageListItemClickHelp.onModifyCardKind(arg0);
            }
        });
        if (map.get("enabled").toString().equalsIgnoreCase("1")) {
            holder.btn_kind_enable.setTextColor(Color.rgb(255, 255, 255));
            holder.btn_kind_enable.setText("½ûÓÃ");
        } else {
            holder.btn_kind_enable.setText("ÆôÓÃ");
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
        private ListView lv_card_kind_goods_detail;
        private TextView money;
        private TextView editor;
        private Button btn_kind_enable;
        private Button btn_kind_modify;
        private Button btn_kind_delete;
    }

}