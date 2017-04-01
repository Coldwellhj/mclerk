package com.eaosoft.mclerk;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.eaosoft.fragment.GFragmentOne;
import com.eaosoft.fragment.GuidePageAdapter;
import com.eaosoft.userinfo.GOperaterInfo;
import com.eaosoft.util.GSvrChannel;
import com.eaosoft.util.GUtilHttp;
import com.eaosoft.util.GUtilSDCard;
import com.eaosoft.util.SingleView;
import com.eaosoft.view.RoundImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;

import static java.lang.Boolean.FALSE;

public class GShareholderMain
{
	private ScrollView				m_oUserView=null;
	private Context					m_oContext=null;
	public GFragmentOne			m_oFragmentOne=null;
	//==============================
	public RoundImageView		m_oImgHead=null;
	public LinearLayout				m_oRounddot=null;
	public ViewPager					m_oImgBanner=null;
	public TextView 					m_oShopCaption=null;
	public TextView 					m_oUserCaption=null;
	//=======================================
	// 广告数组

	private GuidePageAdapter 	m_oGuidePageAdapter;
	private AtomicInteger 			m_oAtomicInteger = new AtomicInteger();
	private List<View> 				m_arViewList=null;
	// ImageView
	private ImageView 				m_oImageViewList[];
	private ImageView 				m_oImageView;
	private int						m_nCreentViewImage;
    private int                     TodayCardCreateNum=0;
    private int                     MonthCardCreateNum=0;
    private int                     TodayMoney=10;
    private int                     MonthMoney=0;
    private List<Float>             singlelist;
    private LinearLayout            llSingle;
    private SingleView              my_single_chart_view;
    private Button	oBtnTodayMoney;
    private Button	oBtnMonthMoney;
    private  RelativeLayout rl_single;
    final Handler m_handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.arg1 == 0) {
                oBtnTodayMoney.setText(TodayMoney+"元");
                oBtnMonthMoney.setText(MonthMoney+"元");
                my_single_chart_view.setList(singlelist);
            }
        }
    };
	//========================================
	public GShareholderMain(Context oContext)
	{
		m_oContext = oContext;
		m_nCreentViewImage = 0;
		m_oFragmentOne = null;
	}
	public  View OnCreateView()
	{

		m_oUserView = new ScrollView(m_oContext);
		m_oUserView.setLayoutParams( new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT) );
		m_oUserView.setFillViewport(true);

        //============================================================================
		//主背景
		LinearLayout oMainWin = new LinearLayout(m_oContext);  //线性布局方式
		oMainWin.setOrientation( LinearLayout.VERTICAL ); //控件对其方式为垂直排列  VERTICAL
		oMainWin.setLayoutParams( new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT) );
        m_oUserView.addView(oMainWin);

        //===========================================================================
        //头
        RelativeLayout oSubHeader = new RelativeLayout(m_oContext);  //线性布局方式
        oSubHeader.setLayoutParams( new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT, 80) );
        oSubHeader.setBackgroundResource(R.color.shareholder_background);
        oMainWin.addView(oSubHeader);
        //头像
        RelativeLayout.LayoutParams pImgHead = new RelativeLayout.LayoutParams(80,80) ;
        pImgHead.addRule(RelativeLayout.ALIGN_LEFT);
        m_oImgHead = new RoundImageView(m_oContext);
        m_oImgHead.setLayoutParams( pImgHead );
        m_oImgHead.setImageResource(R.drawable.ic_launcher);
        m_oImgHead.setOnClickListener(new OnClickListener() {
	        public void onClick(View v) {
	        	Intent intent = new Intent(MainActivity.m_oMainActivity, GActUserInfo.class);
	        	MainActivity.m_oMainActivity.startActivity(intent); }});
        if(GUtilSDCard.isFileExist(GOperaterInfo.m_strLocalDiskImage ))
        {
  		  Bitmap photo = BitmapFactory.decodeFile(GOperaterInfo.m_strLocalDiskImage);
	  		if(photo !=null && m_oImgHead!=null)
	  			m_oImgHead.setImageBitmap(photo);
        }
        oSubHeader.addView(m_oImgHead);
        //门店名称
        RelativeLayout.LayoutParams pShopName = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT) ;
        pShopName.addRule(RelativeLayout.CENTER_IN_PARENT);

        m_oShopCaption=new TextView(m_oContext);
        m_oShopCaption.setLayoutParams( pShopName);
        m_oShopCaption.setTextSize(16);
        m_oShopCaption.setText("总部");
        m_oShopCaption.setTextColor(Color.WHITE);
        m_oShopCaption.setEnabled(true);
//       m_oShopCaption.setOnClickListener(m_oChangeGroup);
        oSubHeader.addView(m_oShopCaption);

        //操作员名称
        RelativeLayout.LayoutParams pOPName = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT) ;
        pOPName.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        pOPName.addRule(RelativeLayout.CENTER_VERTICAL);

        m_oUserCaption=new TextView(m_oContext);
        m_oUserCaption.setLayoutParams(pOPName);
        m_oUserCaption.setTextSize(16);
        m_oUserCaption.setText(GOperaterInfo.m_strRealName);
        m_oUserCaption.setTextColor(Color.WHITE);
        oSubHeader.addView(m_oUserCaption);
        //==========================================================================
        //Banner
		LinearLayout oSubBanner = new LinearLayout(m_oContext);  //线性布局方式
		oSubBanner.setOrientation( LinearLayout.VERTICAL ); //控件对其方式为垂直排列  VERTICAL
		oSubBanner.setLayoutParams( new LayoutParams(LayoutParams.MATCH_PARENT, MainActivity.mSreenHeight/4) );
		oMainWin.addView(oSubBanner);

		m_oImgBanner = new  ViewPager(m_oContext);
		m_oImgBanner.setLayoutParams( new LayoutParams(LayoutParams.MATCH_PARENT, MainActivity.mSreenHeight/4) );
		oSubBanner.addView(m_oImgBanner);


		LayoutParams pRounddot = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
		pRounddot.setMargins(0,-25,0,0);

		m_oRounddot = new LinearLayout(m_oContext);
		m_oRounddot.setLayoutParams(pRounddot);
		m_oRounddot.setOrientation( LinearLayout.HORIZONTAL ); //控件对其方式为横向排列  HORIZONTAL
		m_oRounddot.setGravity(Gravity.CENTER);
		oSubBanner.addView(m_oRounddot);

		onCreateBannerImageList();
        //==========================================================================
        //快捷按钮区
		LinearLayout oSubShortBtn = new LinearLayout(m_oContext);  //线性布局方式
		oSubShortBtn.setOrientation( LinearLayout.HORIZONTAL ); //控件对其方式为横向排列  HORIZONTAL
		oSubShortBtn.setLayoutParams( new LayoutParams(LayoutParams.FILL_PARENT, 300) );
		oMainWin.addView(oSubShortBtn);

		LayoutParams p = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT) ;
		p.setMargins(10,10,10,10);
		p.weight=1.0f;
		Button	oBtnTodayCardCreate = new Button(m_oContext);
        oBtnTodayCardCreate.setLayoutParams( p);
//        oBtnTodayCardCreate.setText("今日开卡"+TodayCardCreateNum+"张");
        oBtnTodayCardCreate.setText("今日");
        oBtnTodayCardCreate.setBackgroundResource(R.color.shareholder_background);
//        oBtnTodayCardCreate.setOnClickListener(new OnClickListener() {
//	        public void onClick(View v) {
//
//	        	        }});
		oSubShortBtn.addView(oBtnTodayCardCreate);

        oBtnTodayMoney = new Button(m_oContext);
        oBtnTodayMoney.setLayoutParams( p);
        oBtnTodayMoney.setText(TodayMoney+"元");
        oBtnTodayMoney.setBackgroundResource(R.color.shareholder_todayMoney);
		//oSubShortBtn.addView(oHLineView);
//        oBtnTodayMoney.setOnClickListener(new OnClickListener() {
//	        public void onClick(View v) {
//
//	        	        }});
		oSubShortBtn.addView(oBtnTodayMoney);

        LinearLayout oSubShortBtn1 = new LinearLayout(m_oContext);  //线性布局方式
        oSubShortBtn1.setOrientation( LinearLayout.HORIZONTAL ); //控件对其方式为横向排列  HORIZONTAL
        oSubShortBtn1.setLayoutParams( new LayoutParams(LayoutParams.FILL_PARENT, 300) );
        oMainWin.addView(oSubShortBtn1);
        LayoutParams p1 = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT) ;
        p1.setMargins(10,10,10,10);
        p1.weight=1.0f;
		Button	oBtnMonthCardCreate = new Button(m_oContext);
        oBtnMonthCardCreate.setLayoutParams( p1);
//        oBtnMonthCardCreate.setText("本月累计开卡"+ MonthCardCreateNum+"张");
        oBtnMonthCardCreate.setText("本月累计");
        oBtnMonthCardCreate.setBackgroundResource(R.color.shareholder_monthCardCreate);
		//oSubShortBtn.addView(oHLineView);
//        oBtnMonthCardCreate.setOnClickListener(new OnClickListener() {
//	        public void onClick(View v) {
//
//	        	        }});
        oSubShortBtn1.addView(oBtnMonthCardCreate);

       	oBtnMonthMoney = new Button(m_oContext);
        oBtnMonthMoney.setLayoutParams( p1);
        oBtnMonthMoney.setText(MonthMoney+"元");
        oBtnMonthMoney.setBackgroundResource(R.color.shareholder_monthMoney);
        //oSubShortBtn.addView(oHLineView);
//        oBtnMonthMoney.setOnClickListener(new OnClickListener() {
//            public void onClick(View v) {
//
//            }});
        oSubShortBtn1.addView(oBtnMonthMoney);

		//==========================================================================
		//柱状图
//		Button	oBtnCardCreate = new Button(m_oContext);
//		oBtnCardCreate.setLayoutParams( new LayoutParams(LayoutParams.FILL_PARENT, 210) );
//		oBtnCardCreate.setBackgroundResource(R.drawable.btn_card_create);
//		oBtnCardCreate.setOnClickListener(new OnClickListener() {
//	        public void onClick(View v) {
//
//	     }});
//		oMainWin.addView(oBtnCardCreate);
//
//		Button	oBtnCardOrderList = new Button(m_oContext);
//		oBtnCardOrderList.setLayoutParams( new LayoutParams(LayoutParams.FILL_PARENT, 180) );
//		//oBtnCardOrderList.setText("下单记录");
//		oBtnCardOrderList.setBackgroundResource(R.drawable.btn_order_history);
//		oBtnCardOrderList.setId(1000);
//		oBtnCardOrderList.setOnClickListener(vp_click_listener);
//		oMainWin.addView(oBtnCardOrderList);

            rl_single = new RelativeLayout(m_oContext);
            rl_single.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 500));

            oMainWin.addView(rl_single);
            llSingle = (LinearLayout) LayoutInflater.from(MainActivity.m_oMainActivity).inflate(R.layout.layout_pro_expense, null);
//        SingleView my_single_chart_view =new SingleView(m_oContext);
            my_single_chart_view = (SingleView) llSingle.findViewById(R.id.my_single_chart_view);
            my_single_chart_view.setLayoutParams( new LayoutParams(LayoutParams.MATCH_PARENT, 450) );
            singlelist = new ArrayList<Float>();
//        Random random = new Random();
//        while (singlelist.size() < 12) {
//            int randomInt = random.nextInt(100);
//            singlelist.add((float) randomInt);
//        }

            my_single_chart_view.setList(singlelist);
            rl_single.removeView(llSingle);
            //原理同双柱
            my_single_chart_view.setListener(new SingleView.getNumberListener() {
                @Override
                public void getNumber(int number, int x, int y) {
                    rl_single.removeView(llSingle);
//                llSingle = (LinearLayout) LayoutInflater.from(MainActivity.m_oMainActivity).inflate(R.layout.layout_pro_expense, null);
                    TextView tvMoney = (TextView) llSingle.findViewById(R.id.tv_shouru_pro);
                    tvMoney.setText( (singlelist.get(number)+"元"));
                    llSingle.measure(0, 0);
                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                            RelativeLayout.LayoutParams.WRAP_CONTENT);
                    params.leftMargin = x - 100;
                    if (x - 100 < 0) {
                        params.leftMargin = 0;
                    } else if (x - 100 > rl_single.getWidth() - llSingle.getMeasuredWidth()) {
                        params.leftMargin = rl_single.getWidth() - llSingle.getMeasuredWidth();
                    }

                    llSingle.setLayoutParams(params);
                    rl_single.addView(llSingle);
                }
            });
            llSingle.removeView(my_single_chart_view);
            rl_single.addView(my_single_chart_view);


		return m_oUserView;
	}
	private void onCreateBannerImageList()
	{
		if(m_arViewList!=null)
			return;
		m_arViewList = new ArrayList<View>();
		for(int i=0;i<5;i++)
		{
			LinearLayout p1 = new LinearLayout(m_oContext);
			p1.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT) );
			p1.setBackgroundResource(R.drawable.main_page1+i);
			p1.setOnClickListener(vp_click_listener);
			m_arViewList.add(p1);
		}
		m_oGuidePageAdapter = new GuidePageAdapter(m_oContext, m_arViewList);
		m_oImgBanner.setAdapter(m_oGuidePageAdapter);

		m_oImageViewList = new ImageView[m_arViewList.size()];

		for (int i = 0; i < m_arViewList.size(); i++)
		{
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
		TimerTask task = new TimerTask()
		{
			@Override
			public void run()
			{
				m_oSalseMainHandler.sendEmptyMessage(m_oAtomicInteger.incrementAndGet() - 1);
			}
		};

		timer.schedule(task, 2000, 2000);
	}

	Handler m_oSalseMainHandler = new Handler()
	{
		public void handleMessage(android.os.Message msg)
		{
			// 显示第几项
			m_oImgBanner.setCurrentItem(msg.what);
			if (m_oAtomicInteger.get() == m_arViewList.size())
				m_oAtomicInteger.set(0);
		};
	};
//	OnClickListener m_oChangeGroup = new OnClickListener()
//  	{
//  		 public void onClick(View v)
//         {
//  			Intent intent = new Intent(MainActivity.m_oMainActivity, GActGroupList.class);
//        	MainActivity.m_oMainActivity.startActivityForResult(intent,MainActivity.USER_GROUP_CHANGE);
//         }
//  	};
	OnClickListener vp_click_listener = new OnClickListener()
	{
		public void onClick(View v) 
		{
			if(v.getId()==1000)//查询下单的历史记录
			{
				String strURL=GSvrChannel.m_strURLOrderHistory+"?token="+GOperaterInfo.m_strToken+"&callerName="+GSvrChannel.CALLER_NAME;
				String strTitle=GOperaterInfo.m_strRealName+"的下单记录";
				Intent intent = new Intent(MainActivity.m_oMainActivity, GActWebView.class);
				
				intent.putExtra("startURL", strURL);
				intent.putExtra("strTitle", strTitle);
				
	        	MainActivity.m_oMainActivity.startActivity(intent);	
				return;
			}
			String strURL="http://www.ndtquam.com";
			if(m_nCreentViewImage==4)
				strURL = "http://www.eaosoft.com";
			/*
			Intent intent = new Intent(getActivity(), GActWebView.class);
			
			intent.putExtra("startURL", strURL);
			
			startActivity(intent);
			*/
		}
	};
	OnPageChangeListener vp_listener = new OnPageChangeListener() 
	{
		@Override
		public void onPageSelected(int arg0) 
		{
			m_nCreentViewImage = arg0;
			m_oAtomicInteger.getAndSet(arg0);
			for (int i = 0; i < m_oImageViewList.length; i++) 
			{
				m_oImageViewList[i].setBackgroundResource(R.drawable.small_bg1);
				if (arg0 != i) 
					m_oImageViewList[i].setBackgroundResource(R.drawable.small_bg);
			}		
		}
		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {}
		@Override
		public void onPageScrollStateChanged(int arg0) {}
	};
	//用户扫描卡号
	public void onScannerResult(String strCardNo,int requestCode)
	{
		if(requestCode== MainActivity.USER_GROUP_CHANGE && m_oShopCaption != null)
				m_oShopCaption.setText(GOperaterInfo.m_strGroupName);
	}
    public void getshopSalseStatistics()
    {
        GSvrChannel svr= 	new GSvrChannel()
        {
            public void onNetFailure(int statusCode,String strInfo)
            {
                MainActivity.MessageBox("读取门店消费信息","statusCode:"+statusCode+",Info:"+strInfo);
                MainActivity.onUserMessageBox("读取门店消费信息", "读取门店消费信息失败，请检查网络是否畅通或者联系管理员！");
            }
            public void onNetSuccess(int nCode,String strInfo,JSONObject oJsonData)
            {
                if(nCode < 0)
                {
                    MainActivity.MessageBox("读取门店消费信息",strInfo);
                    MainActivity.onUserMessageBox("读取门店消费信息",strInfo);
                    return;
                }

                try
                {
                    JSONArray xContent=null;
                    JSONArray goodsList=null;
                    JSONObject oData = oJsonData.getJSONObject("data");
                    if(oData == null)
                    {
                        MainActivity.MessageBox("读取门店消费信息","读取门店消费信息失败，请检查网络是否畅通或者联系管理员！");
                        MainActivity.onUserMessageBox("读取门店消费信息","读取门店消费信息失败，请检查网络是否畅通或者联系管理员！");
                        return;
                    }
                    try{
                        xContent = oData.getJSONArray("content");
                        TodayMoney=Integer.parseInt(String.valueOf( GUtilHttp.getJSONObjectValue("today_money",oData)));
                        MonthMoney=Integer.parseInt(String.valueOf( GUtilHttp.getJSONObjectValue("month_money",oData)));
                    }catch(JSONException e)
                    {
                        System.out.println(e);
                        xContent=null;
                    }
                    if(xContent==null)
                        return;
//                   List ar = new ArrayList();
                    for(int i=0;i<xContent.length();i++)
                    {
                        JSONObject o = xContent.getJSONObject(i);
                        Map map = new HashMap();
                        map.put("uID", GUtilHttp.getJSONObjectValue("uID",o));
                        if(map.get("uID").toString().length()>0)
                        {
//                            map.put("caption",GUtilHttp.getJSONObjectValue("caption",o) );
//                            map.put("money", GUtilHttp.getJSONObjectValue("money",o) );
//                            ar.add(map);
                            singlelist.add(Float.parseFloat( GUtilHttp.getJSONObjectValue("money",o)));
                        }
                        Message msg = m_handler.obtainMessage();
                        msg.arg1 = 0;
                        m_handler.sendMessage(msg);
                    }



                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                    MainActivity.MessageBox("读取门店消费信息",e.getMessage());
                    MainActivity.onUserMessageBox("读取门店消费信息","读取门店消费信息失败，请检查网络是否畅通或者联系管理员！");
                    return;
                }
            }
        };
        try
        {
            JSONObject   requestDatas = new JSONObject();
            requestDatas.put("", "");
            svr.m_oCurrentActivity = MainActivity.m_oMainActivity;
            svr.onPost("api/mobile/shopSalseStatistics.do", requestDatas);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
            MainActivity.MessageBox("读取门店消费信息",e.getMessage());
            MainActivity.onUserMessageBox("读取门店消费信息","读取门店消费信息失败，请检查网络是否畅通或者联系管理员！");
        }
    }
}