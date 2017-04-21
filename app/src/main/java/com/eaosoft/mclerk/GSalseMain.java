package com.eaosoft.mclerk;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.hardware.Camera;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.eaosoft.fragment.GFragmentOne;
import com.eaosoft.fragment.GuidePageAdapter;
import com.eaosoft.userinfo.GOperaterInfo;
import com.eaosoft.util.GSvrChannel;
import com.eaosoft.util.GUtilSDCard;
import com.eaosoft.view.RoundImageView;
import com.xys.libzxing.zxing.activity.CaptureActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;
public class GSalseMain {
    private ScrollView m_oUserView = null;
    private Context m_oContext = null;
    public GFragmentOne m_oFragmentOne = null;
    //==============================
    public RoundImageView m_oImgHead = null;
    public LinearLayout m_oRounddot = null;
    public ViewPager m_oImgBanner = null;
    public TextView m_oShopCaption = null;
    public TextView m_oUserCaption = null;
    public Button oBtnCardOrderCreate;
    //=======================================
    // 广告数组

    private GuidePageAdapter m_oGuidePageAdapter;
    private AtomicInteger m_oAtomicInteger = new AtomicInteger();
    private List<View> m_arViewList = null;
    // ImageView
    private ImageView m_oImageViewList[];
    private ImageView m_oImageView;
    private int m_nCreentViewImage;

    private Camera camera = null;
    private Camera.Parameters parameter = null;

    //========================================
    public GSalseMain(Context oContext) {
        m_oContext = oContext;
        m_nCreentViewImage = 0;
        m_oFragmentOne = null;
    }

    public View OnCreateView() {
        m_oUserView = new ScrollView(m_oContext);
        m_oUserView.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
        m_oUserView.setFillViewport(true);

        //============================================================================
        //主背景
        LinearLayout oMainWin = new LinearLayout(m_oContext);  //线性布局方式
        oMainWin.setOrientation(LinearLayout.VERTICAL); //控件对其方式为垂直排列  VERTICAL
        oMainWin.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
        m_oUserView.addView(oMainWin);

        //===========================================================================
        //头
        RelativeLayout oSubHeader = new RelativeLayout(m_oContext);  //线性布局方式          
        oSubHeader.setLayoutParams(new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT, 100));
        oSubHeader.setBackgroundColor(MainActivity.m_oHeadColor);
        oMainWin.addView(oSubHeader);
        //头像
        RelativeLayout.LayoutParams pImgHead = new RelativeLayout.LayoutParams(80, 80);
        pImgHead.addRule(RelativeLayout.ALIGN_LEFT);
        pImgHead.setMargins(10,10,10,10);
        m_oImgHead = new RoundImageView(m_oContext);
        m_oImgHead.setLayoutParams(pImgHead);
        m_oImgHead.setImageResource(R.drawable.ic_launcher);
        m_oImgHead.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.m_oMainActivity, GActUserInfo.class);
                MainActivity.m_oMainActivity.startActivity(intent);
            }
        });
        if (GUtilSDCard.isFileExist(GOperaterInfo.m_strLocalDiskImage)) {
            Bitmap photo = BitmapFactory.decodeFile(GOperaterInfo.m_strLocalDiskImage);
            if (photo != null && m_oImgHead != null)
                m_oImgHead.setImageBitmap(photo);
        }
        oSubHeader.addView(m_oImgHead);
        //门店名称
        RelativeLayout.LayoutParams pShopName = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        pShopName.addRule(RelativeLayout.CENTER_IN_PARENT);

        m_oShopCaption = new TextView(m_oContext);
        m_oShopCaption.setLayoutParams(pShopName);
        m_oShopCaption.setTextSize(16);
        m_oShopCaption.setText(GOperaterInfo.m_strGroupName);
        m_oShopCaption.setTextColor(Color.WHITE);
        m_oShopCaption.setEnabled(true);
        m_oShopCaption.setOnClickListener(m_oChangeGroup);
        oSubHeader.addView(m_oShopCaption);

        //操作员名称
        RelativeLayout.LayoutParams pOPName = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        pOPName.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        pOPName.addRule(RelativeLayout.CENTER_VERTICAL);

        m_oUserCaption = new TextView(m_oContext);
        m_oUserCaption.setLayoutParams(pOPName);
        m_oUserCaption.setTextSize(16);
        m_oUserCaption.setText(GOperaterInfo.m_strRealName);
        m_oUserCaption.setTextColor(Color.WHITE);
        oSubHeader.addView(m_oUserCaption);
        //==========================================================================
        //Banner
        LinearLayout oSubBanner = new LinearLayout(m_oContext);  //线性布局方式
        oSubBanner.setOrientation(LinearLayout.VERTICAL); //控件对其方式为垂直排列  VERTICAL
        oSubBanner.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, MainActivity.mSreenHeight / 4));
        oMainWin.addView(oSubBanner);

        m_oImgBanner = new ViewPager(m_oContext);
        m_oImgBanner.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, MainActivity.mSreenHeight / 4));
        oSubBanner.addView(m_oImgBanner);


        LinearLayout.LayoutParams pRounddot = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
        pRounddot.setMargins(0, -25, 0, 0);

        m_oRounddot = new LinearLayout(m_oContext);
        m_oRounddot.setLayoutParams(pRounddot);
        m_oRounddot.setOrientation(LinearLayout.HORIZONTAL); //控件对其方式为横向排列  HORIZONTAL
        m_oRounddot.setGravity(Gravity.CENTER);
        oSubBanner.addView(m_oRounddot);

        onCreateBannerImageList();
        //==========================================================================
        //快捷按钮区
        RelativeLayout oSubShortBtn = new RelativeLayout(m_oContext);  //线性布局方式
//		oSubShortBtn.setOrientation( LinearLayout.HORIZONTAL ); //控件对其方式为横向排列  HORIZONTAL
        oSubShortBtn.setLayoutParams(new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, MainActivity.mSreenHeight / 2));
        oMainWin.addView(oSubShortBtn);


        RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(MainActivity.mSreenWidth / 2, MainActivity.mSreenHeight / 4);
        p.setMargins(10, 10, 10, 10);

        oBtnCardOrderCreate = new Button(m_oContext);
        oBtnCardOrderCreate.setId(1);
        oBtnCardOrderCreate.setLayoutParams(p);
        oBtnCardOrderCreate.setText("立即下单");
        oBtnCardOrderCreate.setPadding(120, 0, 0, 0);
        oBtnCardOrderCreate.setTextSize(20);
        oBtnCardOrderCreate.setTextColor(oBtnCardOrderCreate.getResources().getColor(R.color.encode_view));
        oBtnCardOrderCreate.setBackgroundResource(R.drawable.btn_order_create1);
        oBtnCardOrderCreate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


                if (MainActivity.m_bDebugCardNo) {
                    if (m_oFragmentOne != null)
                        m_oFragmentOne.onScannerResult(MainActivity.m_strDebugCardNo, MainActivity.SCAN_CODE_ORDER_CREATE);
                } else {
                    Intent intent = new Intent(MainActivity.m_oMainActivity, CaptureActivity.class);
                    MainActivity.m_oMainActivity.startActivityForResult(intent, MainActivity.SCAN_CODE_ORDER_CREATE);

                }
            }
        });
        oSubShortBtn.addView(oBtnCardOrderCreate, p);

        RelativeLayout.LayoutParams p1 = new RelativeLayout.LayoutParams(MainActivity.mSreenWidth / 2, MainActivity.mSreenHeight / 6);
        p1.setMargins(10, 10, 10, 10);
        p1.addRule(RelativeLayout.RIGHT_OF, 1);
        Button oBtnCardSearch = new Button(m_oContext);
        oBtnCardSearch.setLayoutParams(p1);
        oBtnCardSearch.setId(2);
        oBtnCardSearch.setText("查询酒品");
        oBtnCardSearch.setPadding(140, 0, 0, 0);
        oBtnCardSearch.setTextSize(20);
        oBtnCardSearch.setTextColor(oBtnCardOrderCreate.getResources().getColor(R.color.encode_view));
        oBtnCardSearch.setBackgroundResource(R.drawable.btn_card_search1);
        //oSubShortBtn.addView(oHLineView);
        oBtnCardSearch.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (MainActivity.m_bDebugCardNo) {
                    if (m_oFragmentOne != null)
                        m_oFragmentOne.onScannerResult(MainActivity.m_strDebugCardNo, MainActivity.SCAN_CODE_CAED_INFO);
                } else {
                    Intent intent = new Intent(MainActivity.m_oMainActivity, CaptureActivity.class);
                    MainActivity.m_oMainActivity.startActivityForResult(intent, MainActivity.SCAN_CODE_CAED_INFO);
                }
            }
        });
        oSubShortBtn.addView(oBtnCardSearch, p1);
        RelativeLayout.LayoutParams p2 = new RelativeLayout.LayoutParams(MainActivity.mSreenWidth / 2, MainActivity.mSreenHeight / 4);
        p2.setMargins(10, 10, 10, 10);
        p2.addRule(RelativeLayout.BELOW, 1);
//        oSubShortBtn.addView(oSubShortBtn_three,p2);

        Button oBtnConsume = new Button(m_oContext);
        oBtnConsume.setLayoutParams(p2);
        oBtnConsume.setId(3);
        oBtnConsume.setText("消费记录");
        oBtnConsume.setPadding(120, 0, 0, 0);
        oBtnConsume.setTextSize(20);
        oBtnConsume.setTextColor(oBtnCardOrderCreate.getResources().getColor(R.color.encode_view));
        oBtnConsume.setBackgroundResource(R.drawable.btn_consume_history1);
        //oSubShortBtn.addView(oHLineView);
        oBtnConsume.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (MainActivity.m_bDebugCardNo) {
                    if (m_oFragmentOne != null)
                        m_oFragmentOne.onScannerResult(MainActivity.m_strDebugCardNo, MainActivity.SCAN_CODE_CAED_CONSUME);
                } else {
                    Intent intent = new Intent(MainActivity.m_oMainActivity, CaptureActivity.class);
                    MainActivity.m_oMainActivity.startActivityForResult(intent, MainActivity.SCAN_CODE_CAED_CONSUME);
                }
            }
        });
        oSubShortBtn.addView(oBtnConsume, p2);

        RelativeLayout.LayoutParams p3 = new RelativeLayout.LayoutParams(MainActivity.mSreenWidth / 2, MainActivity.mSreenHeight / 3);
        p3.setMargins(10, 10, 10, 10);
        p3.addRule(RelativeLayout.RIGHT_OF, 3);
        p3.addRule(RelativeLayout.BELOW, 2);
        Button oBtnCardOrderList = new Button(m_oContext);
        oBtnCardOrderList.setText("下单记录");
        oBtnCardOrderList.setPadding(140, 0, 0, 0);
        oBtnCardOrderList.setTextSize(20);
        oBtnCardOrderList.setTextColor(oBtnCardOrderCreate.getResources().getColor(R.color.encode_view));
        oBtnCardOrderList.setLayoutParams(p3);
        oBtnCardOrderList.setBackgroundResource(R.drawable.btn_order_history1);
        oBtnCardOrderList.setId(1000);
        oBtnCardOrderList.setOnClickListener(vp_click_listener);
        oSubShortBtn.addView(oBtnCardOrderList, p3);
        //==========================================================================
//		//新卡销售
//		Button	oBtnCardCreate = new Button(m_oContext);
//		oBtnCardCreate.setLayoutParams( new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, 210) );
//		oBtnCardCreate.setBackgroundResource(R.drawable.btn_card_create);
//		oBtnCardCreate.setOnClickListener(new View.OnClickListener() {
//	        public void onClick(View v) {
//	        	if(MainActivity.m_bDebugCardNo)
//	        	{
//	        		if(m_oFragmentOne !=null)
//	        			m_oFragmentOne.onScannerResult(MainActivity.m_strDebugCardNo,MainActivity.SCAN_CODE_CAED_CREATE);
//	        	}
//	        	else
//	        	{
//	        	Intent intent = new Intent(MainActivity.m_oMainActivity, CaptureActivity.class);
//	        	MainActivity.m_oMainActivity.startActivityForResult(intent, MainActivity.SCAN_CODE_CAED_CREATE);
//	        	}
//	     }});
//		oMainWin.addView(oBtnCardCreate);

//		Button	oBtnCardOrderList = new Button(m_oContext);
//		oBtnCardOrderList.setLayoutParams( new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT,  MainActivity.mSreenHeight/8) );
//		//oBtnCardOrderList.setText("下单记录");
//		oBtnCardOrderList.setBackgroundResource(R.drawable.btn_order_history);
//		oBtnCardOrderList.setId(1000);
//		oBtnCardOrderList.setOnClickListener(vp_click_listener);
//		oMainWin.addView(oBtnCardOrderList);

        return m_oUserView;
    }

    private void onCreateBannerImageList() {
        if (m_arViewList != null)
            return;
        m_arViewList = new ArrayList<View>();
        for (int i = 0; i < 5; i++) {
            LinearLayout p1 = new LinearLayout(m_oContext);
            p1.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
            p1.setBackgroundResource(R.drawable.main_page1 + i);
            p1.setOnClickListener(vp_click_listener);
            m_arViewList.add(p1);
        }
        m_oGuidePageAdapter = new GuidePageAdapter(m_oContext, m_arViewList);
        m_oImgBanner.setAdapter(m_oGuidePageAdapter);

        m_oImageViewList = new ImageView[m_arViewList.size()];

        for (int i = 0; i < m_arViewList.size(); i++) {
            m_oImageView = new ImageView(m_oContext);
            // 设置图片宽和高
            LayoutParams layoutParams = new LayoutParams(9, 9);
            layoutParams.setMargins(10, 5, 10, 5);
            m_oImageView.setLayoutParams(layoutParams);

            m_oImageViewList[i] = m_oImageView;

            if (i == 0)
                m_oImageViewList[i].setBackgroundResource(R.drawable.small_bg);
            else
                m_oImageViewList[i].setBackgroundResource(R.drawable.small_bg1);

            m_oRounddot.addView(m_oImageViewList[i]);
        }

        m_oImgBanner.setOnPageChangeListener(vp_listener);

        // 创建定时器
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                m_oSalseMainHandler.sendEmptyMessage(m_oAtomicInteger.incrementAndGet() - 1);
            }
        };

        timer.schedule(task, 2000, 2000);
    }

    Handler m_oSalseMainHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            // 显示第几项
            m_oImgBanner.setCurrentItem(msg.what);
            if (m_oAtomicInteger.get() == m_arViewList.size())
                m_oAtomicInteger.set(0);
        }

        ;
    };
    View.OnClickListener m_oChangeGroup = new View.OnClickListener() {
        public void onClick(View v) {
            Intent intent = new Intent(MainActivity.m_oMainActivity, GActGroupList.class);
            MainActivity.m_oMainActivity.startActivityForResult(intent, MainActivity.USER_GROUP_CHANGE);
        }
    };
    OnClickListener vp_click_listener = new OnClickListener() {
        public void onClick(View v) {
            if (v.getId() == 1000)//查询下单的历史记录
            {
                String strURL = GSvrChannel.m_strURLOrderHistory + "?token=" + GOperaterInfo.m_strToken + "&callerName=" + GSvrChannel.CALLER_NAME;
                String strTitle = GOperaterInfo.m_strRealName + "的下单记录";
                Intent intent = new Intent(MainActivity.m_oMainActivity, GActWebView.class);

                intent.putExtra("startURL", strURL);
                intent.putExtra("strTitle", strTitle);

                MainActivity.m_oMainActivity.startActivity(intent);
                return;
            }
            String strURL = "http://www.ndtquam.com";
            if (m_nCreentViewImage == 4)
                strURL = "http://www.eaosoft.com";
			/*
			Intent intent = new Intent(getActivity(), GActWebView.class);
			
			intent.putExtra("startURL", strURL);
			
			startActivity(intent);
			*/
        }
    };
    OnPageChangeListener vp_listener = new OnPageChangeListener() {
        @Override
        public void onPageSelected(int arg0) {
            m_nCreentViewImage = arg0;
            m_oAtomicInteger.getAndSet(arg0);
            for (int i = 0; i < m_oImageViewList.length; i++) {
                m_oImageViewList[i].setBackgroundResource(R.drawable.small_bg1);
                if (arg0 != i)
                    m_oImageViewList[i].setBackgroundResource(R.drawable.small_bg);
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }
    };

    //用户扫描卡号
    public void onScannerResult(String strCardNo, int requestCode) {
        if (requestCode == MainActivity.USER_GROUP_CHANGE && m_oShopCaption != null)
            m_oShopCaption.setText(GOperaterInfo.m_strGroupName);
    }
}