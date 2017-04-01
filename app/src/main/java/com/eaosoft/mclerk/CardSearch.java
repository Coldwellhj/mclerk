package com.eaosoft.mclerk;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.eaosoft.userinfo.GOperaterInfo;
import com.eaosoft.util.GSvrChannel;
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
    private WebView wv_search;
    private String strURL;
    private String cardNum;
    private String phoneNumber;
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
        store.setText("总部");
        personal.setImageResource(R.drawable.ic_launcher);
        if (GUtilSDCard.isFileExist(GOperaterInfo.m_strLocalDiskImage)) {
            Bitmap photo = BitmapFactory.decodeFile(GOperaterInfo.m_strLocalDiskImage);
            if (photo != null && personal != null)
                personal.setImageBitmap(photo);
        }
        cardNum=et_cardNum.getText().toString().trim();
        phoneNumber=et_phoneNumber.getText().toString().trim();
    }

    private void initView() {
        store = (TextView) findViewById(R.id.store);
        personal = (RoundImageView) findViewById(R.id.personal);
        userName = (TextView) findViewById(R.id.userName);
        et_cardNum = (EditText) findViewById(R.id.et_cardNum);
        et_phoneNumber = (EditText) findViewById(R.id.et_phoneNumber);
        bt_search = (Button) findViewById(R.id.bt_search);

        tv_record = (TextView) findViewById(R.id.tv_record);
        bt_search.setOnClickListener(this);
        wv_search = (WebView) findViewById(R.id.wv_search);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_search:
                submit();


                break;
        }
    }

    private void submit() {
        // validate

        String card = et_cardNum.getText().toString().trim();
        String phoneNum = et_phoneNumber.getText().toString().trim();
        if (TextUtils.isEmpty(card) && TextUtils.isEmpty(phoneNum)) {
            Toast.makeText(this, "card和phoneNum不能全为空", Toast.LENGTH_SHORT).show();
            return;
        }else {
            strURL = GSvrChannel.m_strURLcardConsume + "?token=" + GOperaterInfo.m_strToken + "&callerName=" + GSvrChannel.CALLER_NAME + "&cardNo=" + cardNum + "&cellPhone=" + phoneNumber;
            wv_search.loadUrl(strURL);
            wv_search.setWebViewClient(new WebViewClient()
            {
                public void onReceivedError(WebView view, int errorCode, String description, String failingUrl)
                {                 // Handle the error

                }

                public boolean shouldOverrideUrlLoading(WebView view, String url)
                {
                    view.loadUrl(url);     //不要跳往系统窗口
                    return true;
                }
            });
        }


    }
    @Override
    //设置回退
    //覆盖Activity类的onKeyDown(int keyCoder,KeyEvent event)方法
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && wv_search.canGoBack())
        {
            wv_search.goBack(); //goBack()表示返回WebView的上一页面
            return true;
        }
        finish();
        return false;
    }
}
