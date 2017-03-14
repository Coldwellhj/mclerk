package com.eaosoft.mclerk;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.eaosoft.view.RoundImageView;

public class GCashier_Package extends ActionBarActivity implements View.OnClickListener {

    private TextView currentTime;
    private TextView store;
    private RoundImageView personal;
    private Button package_detail;
    private Button package_new;
    private ListView lv_packagelist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gcashier__package);
        initView();
    }

    private void initView() {
        currentTime = (TextView) findViewById(R.id.currentTime);
        store = (TextView) findViewById(R.id.store);
        personal = (RoundImageView) findViewById(R.id.personal);
        package_detail = (Button) findViewById(R.id.package_detail);
        package_new = (Button) findViewById(R.id.package_new);
        lv_packagelist = (ListView) findViewById(R.id.lv_packagelist);

        package_detail.setOnClickListener(this);
        package_new.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.package_detail:

                break;
            case R.id.package_new:

                break;
        }
    }
}
