package com.eaosoft.mclerk;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.eaosoft.userinfo.GOperaterInfo;
import com.eaosoft.util.GUtilSDCard;
import com.eaosoft.view.RoundImageView;

public class CardSearch extends Activity implements View.OnClickListener {

    private TextView store;
    private RoundImageView personal;
    private TextView userName;
    private EditText et_cardNum;
    private EditText et_phoneNumber;
    private Button bt_search;
    private TextView tv_card_balance;
    private TextView card_balance;
    private TextView tv_record;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_card_search);
        initView();
        initData();
    }

    private void initData() {
        userName.setText(GOperaterInfo.m_strRealName);
        store.setText(GOperaterInfo.m_strGroupName);
        personal.setImageResource(R.drawable.ic_launcher);
        if (GUtilSDCard.isFileExist(GOperaterInfo.m_strLocalDiskImage)) {
            Bitmap photo = BitmapFactory.decodeFile(GOperaterInfo.m_strLocalDiskImage);
            if (photo != null && personal != null)
                personal.setImageBitmap(photo);
        }
    }
    private void initView() {
        store = (TextView) findViewById(R.id.store);
        personal = (RoundImageView) findViewById(R.id.personal);
        userName = (TextView) findViewById(R.id.userName);
        et_cardNum = (EditText) findViewById(R.id.et_cardNum);
        et_phoneNumber = (EditText) findViewById(R.id.et_phoneNumber);
        bt_search = (Button) findViewById(R.id.bt_search);
        tv_card_balance = (TextView) findViewById(R.id.tv_card_balance);
        card_balance = (TextView) findViewById(R.id.card_balance);
        tv_record = (TextView) findViewById(R.id.tv_record);
        bt_search.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_search:

                break;
        }
    }

    private void submit() {
        // validate
        String cardNum = et_cardNum.getText().toString().trim();
        if (TextUtils.isEmpty(cardNum)) {
            Toast.makeText(this, "cardNum不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        String phoneNumber = et_phoneNumber.getText().toString().trim();
        if (TextUtils.isEmpty(phoneNumber)) {
            Toast.makeText(this, "phoneNumber不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        // TODO validate success, do something


    }
}
