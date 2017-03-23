package com.eaosoft.mclerk;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.eaosoft.userinfo.GOperaterInfo;
import com.eaosoft.util.GUtilSDCard;
import com.eaosoft.view.RoundImageView;

import java.util.Calendar;

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
    private TextView total;
    private TextView number;
    private TextView allPrice;
    private Button print_sales;
    private RelativeLayout rl_sales_report;

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
        card_search = (Button) findViewById(R.id.card_search);
        sales_report = (Button) findViewById(R.id.sales_report);
        ed_card = (EditText) findViewById(R.id.ed_card);
        ed_phoneNum = (EditText) findViewById(R.id.ed_phoneNum);
        bt_search = (Button) findViewById(R.id.bt_search);
        ll_body_head = (LinearLayout) findViewById(R.id.ll_body_head);
        wv_record = (WebView) findViewById(R.id.wv_record);
        rl_card_search = (RelativeLayout) findViewById(R.id.rl_card_search);
        lv_sales = (ListView) findViewById(R.id.lv_sales);
        dateTime = (TextView) findViewById(R.id.dateTime);
        statistics_search = (Button) findViewById(R.id.statistics_search);
        total = (TextView) findViewById(R.id.total);
        number = (TextView) findViewById(R.id.number);
        allPrice = (TextView) findViewById(R.id.allPrice);
        print_sales = (Button) findViewById(R.id.print_sales);
        rl_sales_report = (RelativeLayout) findViewById(R.id.rl_sales_report);
        dateTime.setOnClickListener(this);
        card_search.setOnClickListener(this);
        sales_report.setOnClickListener(this);
        bt_search.setOnClickListener(this);
        statistics_search.setOnClickListener(this);
        print_sales.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.card_search:
                sales_report.setBackgroundResource(R.color.printbutton);
                card_search.setBackgroundResource(R.color.printbutton_light);
                rl_sales_report.setVisibility(View.GONE);
                rl_card_search.setVisibility(View.VISIBLE);
                break;
            case R.id.sales_report:
                sales_report.setBackgroundResource(R.color.printbutton_light);
                card_search.setBackgroundResource(R.color.printbutton);
                rl_sales_report.setVisibility(View.VISIBLE);
                rl_card_search.setVisibility(View.GONE);
                break;
            case R.id.bt_search:

                break;
            case R.id.statistics_search:

                break;
            case R.id.print_sales:

                break;
            case R.id.dateTime:
                AlertDialog.Builder builder = new AlertDialog.Builder(GCashier_Search.this);
                View view = View.inflate(GCashier_Search.this, R.layout.date_time_dialog, null);
                final DatePicker datePicker = (DatePicker) view.findViewById(R.id.date_picker);
//			final TimePicker timePicker = (TimePicker) view.findViewById(R.id.time_picker);
                builder.setView(view);
                Calendar cal = Calendar.getInstance();
                cal.setTimeInMillis(System.currentTimeMillis());
                datePicker.init(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), null);
//			timePicker.setIs24HourView(true);
//			timePicker.setCurrentHour(cal.get(Calendar.HOUR_OF_DAY));
//			timePicker.setCurrentMinute(Calendar.MINUTE);


                final int inType = dateTime.getInputType();
                dateTime.setInputType(InputType.TYPE_NULL);

                dateTime.setInputType(inType);
                //etStartTime.setSelection(etStartTime.getText().length());

                builder.setTitle("请确定查询日期");
                builder.setPositiveButton("确  定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        StringBuffer sb = new StringBuffer();
                        sb.append(String.format("%d-%02d-%02d",
                                datePicker.getYear(),
                                datePicker.getMonth() + 1,
                                datePicker.getDayOfMonth()
                        ));
//					sb.append(timePicker.getCurrentHour())
//							.append(":").append(timePicker.getCurrentMinute());
                        dateTime.setText(sb);
                        dialog.cancel();
                    }
                });
                builder.setNegativeButton("取  消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dateTime.setText("");
                        dialog.cancel();
                    }
                });


                Dialog dialog = builder.create();
                dialog.show();
                break;
        }
    }

    private void submit() {
        // validate
        String card = ed_card.getText().toString().trim();
        if (TextUtils.isEmpty(card)) {
            Toast.makeText(this, "card不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        String phoneNum = ed_phoneNum.getText().toString().trim();
        if (TextUtils.isEmpty(phoneNum)) {
            Toast.makeText(this, "phoneNum不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        // TODO validate success, do something


    }
}
