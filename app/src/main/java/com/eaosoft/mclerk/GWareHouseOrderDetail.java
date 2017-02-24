package com.eaosoft.mclerk;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class GWareHouseOrderDetail extends Activity implements View.OnClickListener {

    private TextView tv_package;
    private TextView orderNumber;
    private TextView orderTime;
    private TextView roomNumber;
    private TextView orderName;
    private Button confirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_order_detail);
        initView();

    }

    private void initView() {
        tv_package = (TextView) findViewById(R.id.tv_package);
        orderNumber = (TextView) findViewById(R.id.orderNumber);
        orderTime = (TextView) findViewById(R.id.orderTime);
        roomNumber = (TextView) findViewById(R.id.roomNumber);
        orderName = (TextView) findViewById(R.id.orderName);
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
