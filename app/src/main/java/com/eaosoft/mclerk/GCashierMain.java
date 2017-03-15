package com.eaosoft.mclerk;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.eaosoft.fragment.GFragmentOne;
import com.eaosoft.userinfo.GOperaterInfo;
import com.eaosoft.util.GUtilSDCard;
import com.eaosoft.view.RoundImageView;
import com.google.zxing.client.android.CaptureActivity;

public class GCashierMain {
    private ScrollView m_oUserView = null;
    private Context m_oContext = null;
    public GFragmentOne m_oFragmentOne = null;
    //=====================================================
    public RoundImageView m_oImgHead = null;
    public LinearLayout m_oRounddot = null;
    public ViewPager m_oImgBanner = null;
    public TextView m_oShopCaption = null;
    public TextView m_oUserCaption = null;
    public TextView m_oCurrentTime = null;

    //=====================================================
    public GCashierMain(Context oContext) {
        m_oContext = oContext;
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
        //页面头--
        oMainWin.addView(onCreatePageHead(m_oContext));
        //==========================================================================
        //中间按钮和操作区
        oMainWin.addView(onCreatePageBody(m_oContext));
        //==========================================================================
        //快捷按钮区

        return m_oUserView;
    }

    private View onCreatePageBody(Context oContext) {
        //======================================================================================
        //主背景
        LinearLayout oMainWin = new LinearLayout(oContext);  //线性布局方式
        oMainWin.setOrientation(LinearLayout.HORIZONTAL); //控件对其方式为水平排列
        oMainWin.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        //======================================================================================
        /*
			LinearLayout oSubShortBtn = new LinearLayout(oContext);  //线性布局方式  
			oSubShortBtn.setOrientation( LinearLayout.VERTICAL ); //控件对其方式为横向排列  VERTICAL
			oSubShortBtn.setLayoutParams( new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, 160) );
			oMainWin.addView(oSubShortBtn);
			//======================================================================================
			 */
        //左半边操作按钮
        LinearLayout oMainWin_left = new LinearLayout(oContext);  //线性布局方式
        oMainWin_left.setOrientation(LinearLayout.VERTICAL); //控件对其方式为竖直排列
        oMainWin_left.setLayoutParams(new LayoutParams(MainActivity.mSreenWidth / 4, LayoutParams.MATCH_PARENT));
        oMainWin_left.setBackgroundResource(R.color.lightgray);
        //======================================================================================
        //当前套餐
        Button oBtnCardKindList = new Button(m_oContext);
        oBtnCardKindList.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        oBtnCardKindList.setText("卡套餐");
        oBtnCardKindList.setBackgroundResource(R.color.printbutton);
        oBtnCardKindList.setTextColor(oBtnCardKindList.getResources().getColor(android.R.color.white));
        oBtnCardKindList.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
//                Intent intent = new Intent(MainActivity.m_oMainActivity, GActCardKindList.class);
//                intent.putExtra("UserMgr", "UserMgr");
//                MainActivity.m_oMainActivity.startActivity(intent);
                Intent intent = new Intent(MainActivity.m_oMainActivity, GCashier_Package.class);
                intent.putExtra("UserMgr", "UserMgr");
                MainActivity.m_oMainActivity.startActivity(intent);
            }
        });
        oMainWin_left.addView(oBtnCardKindList);
        //==========================================================================
        //卡入库
        Button oBtnCardStorage = new Button(m_oContext);
        oBtnCardStorage.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        oBtnCardStorage.setText("卡入库");
        oBtnCardStorage.setBackgroundResource(R.color.printbutton);
        oBtnCardStorage.setTextColor(oBtnCardStorage.getResources().getColor(android.R.color.white));
        oBtnCardStorage.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

            }
        });
        oMainWin_left.addView(oBtnCardStorage);
        //==========================================================================
        //卡领用
        Button oBtnCardUse = new Button(m_oContext);
        oBtnCardUse.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        oBtnCardUse.setText("卡领用");
        oBtnCardUse.setBackgroundResource(R.color.printbutton);
        oBtnCardUse.setTextColor(oBtnCardUse.getResources().getColor(android.R.color.white));
        oBtnCardUse.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

            }
        });
        oMainWin_left.addView(oBtnCardUse);
        //==========================================================================
        //新卡销售
        Button oBtnCardCreate = new Button(m_oContext);
        oBtnCardCreate.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        oBtnCardCreate.setText("卡销售");
        oBtnCardCreate.setBackgroundResource(R.color.printbutton);
        oBtnCardCreate.setTextColor(oBtnCardCreate.getResources().getColor(android.R.color.white));
        oBtnCardCreate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (MainActivity.m_bDebugCardNo) {
                    if (m_oFragmentOne != null)
                        m_oFragmentOne.onScannerResult(MainActivity.m_strDebugCardNo, MainActivity.SCAN_CODE_CAED_CREATE);
                } else {
                    Intent intent = new Intent(MainActivity.m_oMainActivity, CaptureActivity.class);
                    MainActivity.m_oMainActivity.startActivityForResult(intent, MainActivity.SCAN_CODE_CAED_CREATE);
                }
            }
        });
        oMainWin_left.addView(oBtnCardCreate);
        //==========================================================================
        //卡库存
        Button oBtnCardStock = new Button(m_oContext);
        oBtnCardStock.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        oBtnCardStock.setText("卡库存");
        oBtnCardStock.setBackgroundResource(R.color.printbutton);
        oBtnCardStock.setTextColor(oBtnCardStock.getResources().getColor(android.R.color.white));
        oBtnCardStock.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

            }
        });
        oMainWin_left.addView(oBtnCardStock);
        //==========================================================================
        //卡统计
        Button oBtnCardStatistics = new Button(m_oContext);
        oBtnCardStatistics.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        oBtnCardStatistics.setText("卡统计");
        oBtnCardStatistics.setBackgroundResource(R.color.printbutton);
        oBtnCardStatistics.setTextColor(oBtnCardStatistics.getResources().getColor(android.R.color.white));
        oBtnCardStatistics.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

            }
        });
        oMainWin_left.addView(oBtnCardStatistics);
        //======================================================================================
        //扫码查询
        Button oBtnCardSearchByScanner = new Button(m_oContext);
        oBtnCardSearchByScanner.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        oBtnCardSearchByScanner.setBackgroundResource(R.drawable.login);
        oBtnCardSearchByScanner.setBackgroundResource(R.color.printbutton);
        oBtnCardSearchByScanner.setTextColor(oBtnCardCreate.getResources().getColor(android.R.color.white));
        oBtnCardSearchByScanner.setText("扫码查询");
        oBtnCardSearchByScanner.setOnClickListener(new View.OnClickListener() {
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
        oMainWin_left.addView(oBtnCardSearchByScanner);
        //======================================================================================
        oMainWin.addView(oMainWin_left);
        return oMainWin;
    }

    //=============================================================================================
    //建立页面头部
    private View onCreatePageHead(Context oContext) {
        RelativeLayout oSubHeader = new RelativeLayout(oContext);  //线性布局方式
        oSubHeader.setLayoutParams(new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, 100));
        oSubHeader.setBackgroundColor(MainActivity.m_oHeadStoreColor);
        //=============================================================================


        //实时时间
        RelativeLayout.LayoutParams pCurrentTime = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        pCurrentTime.addRule(RelativeLayout.ALIGN_LEFT);
        pCurrentTime.addRule(RelativeLayout.CENTER_VERTICAL);
        m_oCurrentTime = new TextView(oContext);
        m_oCurrentTime.setLayoutParams(pCurrentTime);
        m_oCurrentTime.setTextSize(16);
        m_oCurrentTime.setText(MainActivity.getStringDate());
        m_oCurrentTime.setTextColor(Color.BLACK);

        oSubHeader.addView(m_oCurrentTime);
        //头像
        RelativeLayout.LayoutParams pImgHead = new RelativeLayout.LayoutParams(80, 80);
        pImgHead.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        m_oImgHead = new RoundImageView(oContext);
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
        //操作员名称
//		RelativeLayout.LayoutParams pOPName = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT) ;
//		pOPName.addRule(RelativeLayout.ALIGN_LEFT,m_oImgHead.getId());
//
//		pOPName.addRule(RelativeLayout.CENTER_VERTICAL);
//
//		m_oUserCaption=new TextView(oContext);
//		m_oUserCaption.setLayoutParams( pOPName);
//		m_oUserCaption.setTextSize(16);
//		m_oUserCaption.setText(ListActivityBean.m_strRealName);
//		m_oUserCaption.setTextColor(Color.WHITE);
//		m_oShopCaption.setOnClickListener(new View.OnClickListener()
//		{
//			public void onClick(View v)
//			{
//				//跳转详细报表
//			}
//		});
//		oSubHeader.addView(m_oUserCaption);
        //门店名称
        RelativeLayout.LayoutParams pShopName = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        pShopName.addRule(RelativeLayout.CENTER_IN_PARENT);

        m_oShopCaption = new TextView(oContext);
        m_oShopCaption.setLayoutParams(pShopName);
        m_oShopCaption.setTextSize(16);
        m_oShopCaption.setText(GOperaterInfo.m_strGroupName);
        m_oShopCaption.setTextColor(Color.BLACK);
        m_oShopCaption.setEnabled(true);
//        m_oShopCaption.setOnClickListener(new View.OnClickListener()
//      	{
//     		 public void onClick(View v)
//            {
//     			Intent intent = new Intent(MainActivity.m_oMainActivity, GActGroupList.class);
//     			MainActivity.m_oMainActivity.startActivityForResult(intent,MainActivity.USER_GROUP_CHANGE);
//            }
//     	});
        oSubHeader.addView(m_oShopCaption);

        return oSubHeader;
    }

    public void onScannerResult(String strCardNo, int requestCode) {
        if (requestCode == MainActivity.USER_GROUP_CHANGE && m_oShopCaption != null)
            m_oShopCaption.setText(GOperaterInfo.m_strGroupName);
    }
}