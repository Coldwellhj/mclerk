package com.eaosoft.mclerk;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.eaosoft.adapter.DateAdapter;
import com.eaosoft.userinfo.GOperaterInfo;
import com.eaosoft.util.GSvrChannel;
import com.eaosoft.util.GUtilSDCard;
import com.eaosoft.view.RoundImageView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CardSalse extends Activity implements GestureDetector.OnGestureListener, View.OnClickListener {

    private RoundImageView personal;
    private TextView store;
    private TextView userName;
    private LinearLayout head;


    private ViewFlipper flipper1 = null;
    private GridView gridView = null;
    private GestureDetector gestureDetector = null;
    private int year_c = 0;
    private int month_c = 0;
    private String currentDate = "";

    private DateAdapter dateAdapter;

    private int selectPostion = 0;
    private String monthNumbers[] = new String[7];
    private String YearNumbers[] = new String[7];

    private int currentYear;
    private int currentMonth;
    private int currentNum;
    private int currentWeek;
    private WebView wv_record;
    private String strURL;
    private Button bt_allInformation;

    public CardSalse() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-d");
        currentDate = sdf.format(date);
        year_c = Integer.parseInt(currentDate.split("-")[0]);
        month_c = Integer.parseInt(currentDate.split("-")[1]);
        currentYear = year_c;
        currentMonth = month_c;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_card_salse);
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

        gestureDetector = new GestureDetector(this);

        dateAdapter = new DateAdapter(this, getResources(), currentYear,
                currentMonth, currentNum, selectPostion,
                currentWeek == 1 ? true : false);
        addGridView();
        monthNumbers = dateAdapter.getMonthNumbers();
        YearNumbers = dateAdapter.getYearNumbers();
        gridView.setAdapter(dateAdapter);

        gridView.setSelection(0);
        flipper1.addView(gridView, 0);
        strURL = GSvrChannel.m_strURLshopSaleStatistics + "?token=" + GOperaterInfo.m_strToken + "&callerName=" + GSvrChannel.CALLER_NAME + "&month=" + currentMonth + "&year=" + currentYear;
        wv_record.loadUrl(strURL);

    }

    private void initView() {
        personal = (RoundImageView) findViewById(R.id.personal);
        store = (TextView) findViewById(R.id.store);
        userName = (TextView) findViewById(R.id.userName);
        head = (LinearLayout) findViewById(R.id.head);
        flipper1 = (ViewFlipper) findViewById(R.id.flipper1);

        wv_record = (WebView) findViewById(R.id.wv_record);
//        wv_record.setOnClickListener(this);
        bt_allInformation = (Button) findViewById(R.id.bt_allInformation);
        bt_allInformation.setOnClickListener(this);
    }

    private void addGridView() {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                AbsListView.LayoutParams.WRAP_CONTENT, AbsListView.LayoutParams.WRAP_CONTENT);
        gridView = new GridView(this);
        gridView.setNumColumns(7);
        gridView.setGravity(Gravity.CENTER_VERTICAL);
        gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        gridView.setVerticalSpacing(1);
        gridView.setHorizontalSpacing(1);
        gridView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return CardSalse.this.gestureDetector.onTouchEvent(event);
            }
        });

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                selectPostion = position;
                strURL = GSvrChannel.m_strURLshopSaleStatistics + "?token=" + GOperaterInfo.m_strToken + "&callerName=" + GSvrChannel.CALLER_NAME + "&month=" + monthNumbers[position] + "&year=" + YearNumbers[position];
                wv_record.loadUrl(strURL);
                dateAdapter.setSeclection(position);
                dateAdapter.notifyDataSetChanged();
//                tvDate.setText(YearNumbers[position] + "年"
//                        + dayNumbers[position] + "月"
//                );
            }
        });
        gridView.setLayoutParams(params);
    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    public boolean onDown(MotionEvent e) {

        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
                            float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }


    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                           float velocityY) {
        int gvFlag = 0;
        if (e1.getX() - e2.getX() > 80) {
            // 向左滑
            addGridView();
            currentMonth = currentMonth + 7;
            if (currentMonth > 12) {
                currentMonth = currentMonth - 12;
                currentYear++;
            }
            strURL = GSvrChannel.m_strURLshopSaleStatistics + "?token=" + GOperaterInfo.m_strToken + "&callerName=" + GSvrChannel.CALLER_NAME + "&month=" + currentMonth + "&year=" + currentYear;
            wv_record.loadUrl(strURL);
            dateAdapter = new DateAdapter(this, getResources(), currentYear,
                    currentMonth, currentNum, selectPostion,
                    currentWeek == 1 ? true : false);
            monthNumbers = dateAdapter.getMonthNumbers();
            YearNumbers = dateAdapter.getYearNumbers();
            gridView.setAdapter(dateAdapter);
//            tvDate.setText(YearNumbers[selectPostion] + "年"
//                    + dayNumbers[selectPostion]+ "月"
//            );
            gvFlag++;
            flipper1.addView(gridView, gvFlag);
            dateAdapter.setSeclection(selectPostion);
            this.flipper1.setInAnimation(AnimationUtils.loadAnimation(this,
                    R.anim.push_left_in));
            this.flipper1.setOutAnimation(AnimationUtils.loadAnimation(this,
                    R.anim.push_left_out));
            this.flipper1.showNext();
            flipper1.removeViewAt(0);
            return true;

        } else if (e1.getX() - e2.getX() < -80) {
            addGridView();
            currentMonth = currentMonth - 7;
            if (currentMonth < 1) {
                currentMonth = currentMonth + 12;
                currentYear--;
            }
            strURL = GSvrChannel.m_strURLshopSaleStatistics + "?token=" + GOperaterInfo.m_strToken + "&callerName=" + GSvrChannel.CALLER_NAME + "&month=" + currentMonth + "&year=" + currentYear;
            wv_record.loadUrl(strURL);
            dateAdapter = new DateAdapter(this, getResources(), currentYear,
                    currentMonth, currentNum, selectPostion,
                    currentWeek == 1 ? true : false);
            monthNumbers = dateAdapter.getMonthNumbers();
            YearNumbers = dateAdapter.getYearNumbers();
            gridView.setAdapter(dateAdapter);
//            tvDate.setText(YearNumbers[selectPostion] + "年"
//                    +  dayNumbers[selectPostion] + "月"
//            );
            gvFlag++;
            flipper1.addView(gridView, gvFlag);
            dateAdapter.setSeclection(selectPostion);
            this.flipper1.setInAnimation(AnimationUtils.loadAnimation(this,
                    R.anim.push_right_in));
            this.flipper1.setOutAnimation(AnimationUtils.loadAnimation(this,
                    R.anim.push_right_out));
            this.flipper1.showPrevious();
            flipper1.removeViewAt(0);
            return true;
            // }
        }
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return this.gestureDetector.onTouchEvent(event);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_allInformation:
                dateAdapter.setSeclection(-1);
                dateAdapter.notifyDataSetChanged();
                strURL= GSvrChannel.m_strURLshopSaleStatistics+"?token="+GOperaterInfo.m_strToken+"&callerName="+GSvrChannel.CALLER_NAME;
                wv_record.loadUrl(strURL);
                break;
        }
    }
}
