package com.eaosoft.mclerk;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class GCashierCheckout extends Activity implements View.OnClickListener {

    private TextView shopName;
    private TextView cardNum;
    private TextView roomNumber;
    private TextView orderName;
    private TextView cashierName;
    private ListView lv_order_detail;
    private TextView payment_method;
    private TextView payment_money;
    private TextView Checkout;
    private Button confirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gcashier_checkout);
        initView();
    }

    private void initView() {
        shopName = (TextView) findViewById(R.id.shopName);
        cardNum = (TextView) findViewById(R.id.cardNum);
        roomNumber = (TextView) findViewById(R.id.roomNumber);
        orderName = (TextView) findViewById(R.id.orderName);
        cashierName = (TextView) findViewById(R.id.cashierName);
        lv_order_detail = (ListView) findViewById(R.id.lv_order_detail);
        payment_method = (TextView) findViewById(R.id.payment_method);
        payment_money = (TextView) findViewById(R.id.payment_money);
        Checkout = (TextView) findViewById(R.id.Checkout);
        confirm = (Button) findViewById(R.id.confirm);
        confirm.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.confirm:

                break;
        }
    }
}
