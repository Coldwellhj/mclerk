package com.eaosoft.mclerk;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.eaosoft.userinfo.GOperaterInfo;
import com.eaosoft.util.GUtilSDCard;
import com.eaosoft.view.RoundImageView;

public class GWareHouseStatistics extends Activity implements View.OnClickListener {

    private TextView currentTime;
    private TextView store;
    private RoundImageView personal;
    private TextView dateTime;
    private Button statistics_search;
    private Button statistics_print;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_gware_house_statistics);
        initView();
        initData();
    }

    private void initData() {
        currentTime.setText(MainActivity.getStringDate());
        store.setText(GOperaterInfo.m_strGroupName);
        personal.setImageResource(R.drawable.ic_launcher);
        if(GUtilSDCard.isFileExist(GOperaterInfo.m_strLocalDiskImage ))
        {
            Bitmap photo = BitmapFactory.decodeFile(GOperaterInfo.m_strLocalDiskImage);
            if(photo !=null && personal!=null)
                personal.setImageBitmap(photo);
        }
    }

    private void initView() {
        currentTime = (TextView) findViewById(R.id.currentTime);
        store = (TextView) findViewById(R.id.store);
        personal = (RoundImageView) findViewById(R.id.personal);
        dateTime = (TextView) findViewById(R.id.dateTime);
        statistics_search = (Button) findViewById(R.id.statistics_search);
        statistics_print = (Button) findViewById(R.id.statistics_print);
        statistics_search.setOnClickListener(this);
        statistics_print.setOnClickListener(this);
        personal.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.statistics_search:

                break;
            case R.id.statistics_print:

                break;
            case R.id.personal:
                Intent intent = new Intent(MainActivity.m_oMainActivity, GActUserInfo.class);
                MainActivity.m_oMainActivity.startActivity(intent);
                break;
        }
    }
}
