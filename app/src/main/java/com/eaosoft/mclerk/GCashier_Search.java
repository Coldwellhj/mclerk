package com.eaosoft.mclerk;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.eaosoft.adapter.GCashierSalesReportAdapter;
import com.eaosoft.adapter.GHttpDAO;
import com.eaosoft.userinfo.GOperaterInfo;
import com.eaosoft.util.GSvrChannel;
import com.eaosoft.util.GUtilSDCard;
import com.eaosoft.view.RoundImageView;

import java.util.ArrayList;
import java.util.List;

public class GCashier_Search extends Activity implements View.OnClickListener {

    private TextView currentTime;
    private TextView store;
    private LinearLayout head;
    private RoundImageView personal;
    private Button card_search;
    private Button sales_report;
    private EditText ed_card;
    private EditText ed_phoneNum;
    private Button bt_search;
    private LinearLayout ll_body_head;
    private WebView wv_record;
    private RelativeLayout rl_card_search;
    private ListView lv_sales;
    private TextView dateTime;
    private Button statistics_search;
    private Button connectBlueTooth;
    private Button Connect;
    private TextView number;
    private List ar;
    private int totalMoney=0;
    private TextView allPrice;
    private Button print_sales;
    private RelativeLayout rl_sales_report;
    private String strURL;
    private GCashierSalesReportAdapter m_oCashierSalesReportAdapter=null;
    private GHttpDAO m_oWareHouseDetailListDAO = null;
    private String dayTime;


    BluetoothAdapter blueadapter;
    private View dialogView;
    private ArrayAdapter<String> adapter1, adapter2;
    private ListView lv1, lv2;
    AlertDialog dialog;
    private Button btn_scan;
    private LinearLayout ll1;
    public String mac = "";
    private ArrayList<String> deviceList_bonded = new ArrayList<String>();
    private ArrayList<String> deviceList_found = new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_gcashier__search);

        initView();
        initData();
    }
    @Override
    protected void onResume() {
        super.onResume();
        if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
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
        head = (LinearLayout) findViewById(R.id.head);
        personal = (RoundImageView) findViewById(R.id.personal);

        ed_card = (EditText) findViewById(R.id.ed_card);
        ed_phoneNum = (EditText) findViewById(R.id.ed_phoneNum);
        bt_search = (Button) findViewById(R.id.bt_search);
        ll_body_head = (LinearLayout) findViewById(R.id.ll_body_head);
        wv_record = (WebView) findViewById(R.id.wv_record);
        rl_card_search = (RelativeLayout) findViewById(R.id.rl_card_search);


        statistics_search = (Button) findViewById(R.id.statistics_search);
        connectBlueTooth = (Button) findViewById(R.id.connectBlueTooth);
        Connect = (Button) findViewById(R.id.Connect);
        number = (TextView) findViewById(R.id.number);
        allPrice = (TextView) findViewById(R.id.allPrice);
        print_sales = (Button) findViewById(R.id.print_sales);
        rl_sales_report = (RelativeLayout) findViewById(R.id.rl_sales_report);

        bt_search.setOnClickListener(this);

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
        String card = ed_card.getText().toString().trim();
        String phoneNum = ed_phoneNum.getText().toString().trim();
        if (TextUtils.isEmpty(card)&&TextUtils.isEmpty(phoneNum)) {
            Toast.makeText(this, "card和phoneNum不能全为空", Toast.LENGTH_SHORT).show();
            return;
        }else {
            strURL = GSvrChannel.m_strURLcardConsume + "?token=" + GOperaterInfo.m_strToken + "&callerName=" + GSvrChannel.CALLER_NAME + "&cardNo=" + card + "&cellPhone=" + phoneNum;
            wv_record.loadUrl(strURL);
            wv_record.setWebViewClient(new WebViewClient() {
                public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {                 // Handle the error

                }

                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    view.loadUrl(url);     //不要跳往系统窗口
                    return true;
                }
            });
        }
    }
}
