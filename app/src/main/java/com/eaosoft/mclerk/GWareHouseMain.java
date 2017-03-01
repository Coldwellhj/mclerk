package com.eaosoft.mclerk;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.eaosoft.adapter.GHttpDAO;
import com.eaosoft.adapter.GWareHouseAdapter;
import com.eaosoft.adapter.GWareHouseGoodsAdapter;
import com.eaosoft.fragment.GFragmentOne;
import com.eaosoft.userinfo.GOperaterInfo;
import com.eaosoft.util.Conts;
import com.eaosoft.util.GUtilSDCard;
import com.eaosoft.view.RoundImageView;

import net.posprinter.posprinterface.ProcessData;
import net.posprinter.posprinterface.UiExecute;
import net.posprinter.utils.DataForSendToPrinterTSC;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import static com.eaosoft.mclerk.MainActivity.binder;

public class GWareHouseMain
{
	private ScrollView				m_oUserView=null;
	private Context					m_oContext=null;
	public GFragmentOne			m_oFragmentOne=null;
	//=====================================================
	public RoundImageView		m_oImgHead=null;
	public LinearLayout				m_oRounddot=null;
	public ViewPager					m_oImgBanner=null;
	private TextView				m_txtCardNo=null;
	public TextView 					m_oShopCaption=null;
	public TextView 					m_otxtRoomNo=null;
    public TextView 					orderNumber=null;
    public TextView 					m_ocardNumber=null;
    public TextView 					m_oorderTime=null;
    public TextView                     oTextBle=null;
    public TextView 					m_osalseman=null;
    public TextView                m_oCurrentTime=null;
    private ListView							wh_listview=null;
    private ListView							wh_listview_detail=null;
    private GWareHouseAdapter  m_oWareHouseDetailListAdapter=null;
    private GWareHouseGoodsAdapter  m_oWareHouseGoodsDetailListAdapter=null;
    private GHttpDAO						m_oWareHouseDetailListDAO=null;
    BluetoothAdapter blueadapter;
    private View dialogView;
    private ArrayAdapter<String> adapter1,adapter2;
    private ListView lv1,lv2;
    AlertDialog dialog;
    private Button btn_scan;
    private LinearLayout ll1;
    public  String mac="";
    private ArrayList<String> deviceList_bonded=new ArrayList<String>();
    private ArrayList<String> deviceList_found=new ArrayList<String>();
    private Handler handler = new Handler( );
    private Runnable runnable;

    boolean isConnect;//用来标识连接状态的一个boolean值
    boolean isPrinted;
    //=====================================================


	public GWareHouseMain(Context oContext)
	{
		m_oContext = oContext;

	}
	public View OnCreateView()
	{
		m_oUserView = new ScrollView(m_oContext);
        m_oUserView.setLayoutParams( new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT) );
        m_oUserView.setFillViewport(true);
        m_oUserView.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View arg0, MotionEvent arg1) {
                return true;
            }
        });
        runnable = new Runnable() {
            public void run() {
                m_oWareHouseDetailListAdapter = new GWareHouseAdapter(MainActivity.m_oMainActivity);
                m_oWareHouseDetailListDAO = new GHttpDAO(MainActivity.m_oMainActivity,m_oWareHouseDetailListAdapter);
                wh_listview.setAdapter(m_oWareHouseDetailListAdapter);
                m_oWareHouseGoodsDetailListAdapter = new GWareHouseGoodsAdapter(MainActivity.m_oMainActivity);
                wh_listview_detail.setAdapter(m_oWareHouseGoodsDetailListAdapter);
                m_oWareHouseDetailListDAO.getWareHouseDetail();
                handler.postDelayed(this, 180000);
                //postDelayed(this,18000)方法安排一个Runnable对象到主线程队列中
            }
        };

        //============================================================================
		//主背景
		LinearLayout oMainWin = new LinearLayout(m_oContext);  //线性布局方式
		oMainWin.setOrientation( LinearLayout.VERTICAL ); //控件对其方式为垂直排列  VERTICAL
		oMainWin.setLayoutParams( new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT) );
        m_oUserView.addView(oMainWin);

        //===========================================================================
        //页面头--
        oMainWin.addView(onCreatePageHead(m_oContext));
        //==========================================================================
	    //操作区
       oMainWin.addView(onCreatePageBody(m_oContext));
		//==========================================================================

		return m_oUserView;
	}
    AdapterView.OnItemClickListener lv_listener = new AdapterView.OnItemClickListener()
    {
        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,	long arg3)
        {

        }
    };
	private View onCreatePageBody(Context oContext)
	{
		//======================================================================================
		//主背景
		LinearLayout oMainWin = new LinearLayout(oContext);  //线性布局方式
		oMainWin.setOrientation( LinearLayout.HORIZONTAL ); //控件对其方式为水平排列
        oMainWin.setLayoutParams( new LayoutParams( LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        //左半边操作按钮
        LinearLayout oMainWin_left = new LinearLayout(oContext);  //线性布局方式
        oMainWin_left.setOrientation( LinearLayout.VERTICAL ); //控件对其方式为竖直排列
        oMainWin_left.setLayoutParams( new LayoutParams(MainActivity.mSreenWidth/4, LayoutParams.MATCH_PARENT));
        oMainWin_left.setBackgroundResource(R.color.lightgray);
		//======================================================================================


        RelativeLayout.LayoutParams m_oBtnPrintOrder= new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT) ;
        m_oBtnPrintOrder.setMargins(40,40,40,10);
		Button	oBtnPrintOrder = new Button(m_oContext);
        oBtnPrintOrder.setLayoutParams(m_oBtnPrintOrder);

		oBtnPrintOrder.setBackgroundResource(R.color.printbutton);
		oBtnPrintOrder.setText("历史补打");
		oBtnPrintOrder.setTextColor(oBtnPrintOrder.getResources().getColor(android.R.color.white));
        oBtnPrintOrder.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.m_oMainActivity, GWareHouseFillPrint.class);
                intent.putExtra("UserMgr", "UserMgr");
                MainActivity.m_oMainActivity.startActivity(intent);
            }});
        oMainWin_left.addView(oBtnPrintOrder);

        RelativeLayout.LayoutParams m_oBtnFillPrint= new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT) ;
        m_oBtnFillPrint.setMargins(40,40,40,10);
        Button	oBtnFillPrint = new Button(m_oContext);
        oBtnFillPrint.setLayoutParams(m_oBtnFillPrint);
        oBtnFillPrint.setBackgroundResource(R.color.printbutton);
        oBtnFillPrint.setText("暂停打印");
        oBtnFillPrint.setTextColor(oBtnPrintOrder.getResources().getColor(android.R.color.white));
        oBtnFillPrint.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.m_oMainActivity, GWareHouseStatistics.class);
                intent.putExtra("UserMgr", "UserMgr");
                MainActivity.m_oMainActivity.startActivity(intent);
            }});
        oMainWin_left.addView(oBtnFillPrint);

        RelativeLayout.LayoutParams m_oBtnViewReport= new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT) ;
        m_oBtnViewReport.setMargins(40,40,40,10);
        Button	oBtnViewReport = new Button(m_oContext);
        oBtnViewReport.setLayoutParams(m_oBtnViewReport);
        oBtnViewReport.setBackgroundResource(R.color.printbutton);
        oBtnViewReport.setText("出品汇总");
        oBtnViewReport.setTextColor(oBtnPrintOrder.getResources().getColor(android.R.color.white));
        oBtnViewReport.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

            }});
        oMainWin_left.addView(oBtnViewReport);
        RelativeLayout.LayoutParams m_oEdtBle= new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT) ;
        m_oEdtBle.setMargins(40,40,40,10);
        oTextBle = new TextView(m_oContext);
        oTextBle.setLayoutParams(m_oEdtBle);

        oTextBle.setBackgroundResource(R.color.lightgray);
        oTextBle.setText("点击选择蓝牙设备");
        oTextBle.setTextColor(oTextBle.getResources().getColor(android.R.color.white));
        oTextBle.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                connectBLE();
            }});
        oMainWin_left.addView(oTextBle);
        RelativeLayout.LayoutParams m_oConnect= new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT) ;
        m_oConnect.setMargins(40,40,40,10);
        Button	oConnect = new Button(m_oContext);
        oConnect.setLayoutParams(m_oConnect);
        oConnect.setBackgroundResource(R.color.printbutton);
        oConnect.setText("连接");
        oConnect.setTextColor(oBtnPrintOrder.getResources().getColor(android.R.color.white));
        oConnect.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                sendble();
            }});
        oMainWin_left.addView(oConnect);
        RelativeLayout.LayoutParams m_oPrint= new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT) ;
        m_oPrint.setMargins(40,40,40,10);
        Button	oPrint = new Button(m_oContext);
        oPrint.setLayoutParams(m_oPrint);
        oPrint.setBackgroundResource(R.color.printbutton);
        oPrint.setText("打印");
        oPrint.setTextColor(oBtnPrintOrder.getResources().getColor(android.R.color.white));
        oPrint.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (isConnect) {
                    // TODO Auto-generated method stub
                    //向打印机发生打印指令和打印数据，调用此方法
                    //第一个参数，还是UiExecute接口的实现，分别是发生数据成功和失败后在ui线程的处理
                    binder.writeDataByYouself(new UiExecute() {

                        @Override
                        public void onsucess() {
                            // TODO Auto-generated method stub
                            Toast.makeText(MainActivity.m_oMainActivity, R.string.send_success, Toast.LENGTH_SHORT)
                                    .show();
                            if(wh_listview.getChildAt(0)!=null) {
                                String taskUID = ((TextView) wh_listview.getChildAt(0).findViewById(R.id.m_oisPrintTask)).getText().toString();
                                m_oWareHouseDetailListDAO.opPrnTaskComplete(taskUID);
                                m_oWareHouseDetailListAdapter.notifyDataSetChanged();
                            }
                        }

                        @Override
                        public void onfailed() {
                            // TODO Auto-generated method stub
                            Toast.makeText(MainActivity.m_oMainActivity, R.string.send_failed, Toast.LENGTH_SHORT)
                                    .show();
                        }
                    }, new ProcessData() {//第二个参数是ProcessData接口的实现
                        //这个接口的重写processDataBeforeSend这个处理你要发送的指令

                        @Override
                        public List<byte[]> processDataBeforeSend() {
                            // TODO Auto-generated method stub
                            //初始化一个list
                            ArrayList<byte[]> list = new ArrayList<byte[]>();
                            //在打印请可以先设置打印内容的字符编码类型，默认为gbk，请选择打印机可识别的类型，参看编程手册，打印代码页
                            DataForSendToPrinterTSC.setCharsetName("gbk");//不设置，默认为gbk
                            //通过工具类得到一个指令的byte[]数据,以文本为例
                            //首先得设置size标签尺寸,宽60mm,高30mm,也可以调用以dot或inch为单位的方法具体换算参考编程手册
                            DataForSendToPrinterTSC
                                    .sizeBymm(60, 60);

                            try {
                                if(wh_listview.getChildAt(0)!=null) {
                                    String orderNumber=((TextView)wh_listview.getChildAt(0).findViewById(R.id.orderNumber)).getText().toString();
                                    String m_otxtRoomNo = ((TextView)wh_listview.getChildAt(0).findViewById(R.id.m_otxtRoomNo)).getText().toString();
                                    String m_ocardNumber=((TextView)wh_listview.getChildAt(0).findViewById(R.id.m_ocardNumber)).getText().toString();
                                    String m_ogoodsCaption=((TextView)wh_listview.getChildAt(0).findViewById(R.id.m_ogoodsCaption)).getText().toString();
                                    String m_ogoodsNumber=((TextView)wh_listview.getChildAt(0).findViewById(R.id.m_ogoodsNumber)).getText().toString();
                                    String m_ogoodsUnitName=((TextView)wh_listview.getChildAt(0).findViewById(R.id.m_ogoodsUnitName)).getText().toString();
                                    String m_oorderTime=((TextView)wh_listview.getChildAt(0).findViewById(R.id.m_oorderTime)).getText().toString();
                                    String m_osalseman=((TextView)wh_listview.getChildAt(0).findViewById(R.id.m_osalseman)).getText().toString();
                                    list.add((GOperaterInfo.m_strGroupName + "\n").getBytes("gbk"));
                                    list.add(("单号：" + orderNumber + "\n").getBytes("gbk"));
                                    list.add(("房号：" + m_otxtRoomNo + "\n").getBytes("gbk"));
                                    list.add(("卡号：" + m_ocardNumber + "\n").getBytes("gbk"));
                                    list.add(("名称      " + "数量  " + "单位  " + "\n").getBytes("gbk"));
                                    if(m_ogoodsCaption.length()==2){
                                        list.add((m_ogoodsCaption + "        ").getBytes("gbk"));
                                        list.add((m_ogoodsNumber + "    ").getBytes("gbk"));
                                        list.add((m_ogoodsUnitName + "\n").getBytes("gbk"));
                                    }else if (m_ogoodsCaption.length()==4) {
                                        list.add((m_ogoodsCaption + "    ").getBytes("gbk"));
                                        list.add((m_ogoodsNumber + "    ").getBytes("gbk"));
                                        list.add((m_ogoodsUnitName + "\n").getBytes("gbk"));
                                    }
                                    list.add(("下单时间：" + m_oorderTime + "\n").getBytes("gbk"));
                                    list.add(("销售员：" + m_osalseman + "\n").getBytes("gbk"));
                                }
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }

//                            byte[] data0 = DataForSendToPrinterTSC
//                                    .sizeBymm(60, 30);
//                            list.add(data0);
                            //设置Gap,同上
//                            list.add(DataForSendToPrinterTSC.gapBymm(0,
//                                    0));
//
//                            //清除缓存
//                            list.add(DataForSendToPrinterTSC.cls());
                            //条码指令，参数：int x，x方向打印起始点；int y，y方向打印起始点；
                            //string font，字体类型；int rotation，旋转角度；
                            //int x_multiplication，字体x方向放大倍数
                            //int y_multiplication,y方向放大倍数
                            //string content，打印内容
//                            byte[] data1 = DataForSendToPrinterTSC
//                                    .text(10, 10, "0", 0, 1, 1,
//                                            "abc123");
//                            list.add(data1);
                            //打印直线,int x;int y;int width,线的宽度，int height,线的高度
//                            list.add(DataForSendToPrinterTSC.bar(20,
//                                    40, 200, 3));
                            //打印条码
//                            list.add(DataForSendToPrinterTSC.barCode(
//                                    60, 50, "128", 100, 1, 0, 2, 2,
//                                    "abcdef12345"));
                            //打印

                           list.add(DataForSendToPrinterTSC.print(1));
                            return list;
                        }
                   });
                }else {
                    Toast.makeText(MainActivity.m_oMainActivity, R.string.not_con_printer,Toast.LENGTH_SHORT).show();
                }
            }
            });
        oMainWin_left.addView(oPrint);
			//======================================================================================
        //右半边打印详细
        LinearLayout oMainWin_right = new LinearLayout(oContext);  //线性布局方式
        oMainWin_right.setOrientation( LinearLayout.VERTICAL ); //控件对其方式为竖直排列
        oMainWin_right.setLayoutParams( new LayoutParams(MainActivity.mSreenWidth*3/4, LayoutParams.MATCH_PARENT));

        LinearLayout oMainWin_right_head = new LinearLayout(oContext);  //线性布局方式
        oMainWin_right_head.setOrientation( LinearLayout.HORIZONTAL ); //控件对其方式为水平排列
        oMainWin_right_head.setLayoutParams( new LayoutParams(MainActivity.mSreenWidth*3/4, LayoutParams.WRAP_CONTENT));
        RelativeLayout.LayoutParams m_orderNumber = new RelativeLayout.LayoutParams(MainActivity.mSreenWidth*3/4/5, 100) ;
//        m_orderNumber.setMargins(10,40,10,10);
        orderNumber=new TextView(oContext);
        orderNumber.setLayoutParams(m_orderNumber);
        orderNumber.setTextSize(16);
        orderNumber.setGravity(Gravity.CENTER);
        orderNumber.setText("单号");
        orderNumber.setBackgroundResource(R.color.printbutton);
        orderNumber.setTextColor(Color.WHITE);
        oMainWin_right_head.addView(orderNumber);

        RelativeLayout.LayoutParams m_txtCardNo= new RelativeLayout.LayoutParams(MainActivity.mSreenWidth*3/4/5,100) ;
//        m_txtCardNo.setMargins(40,40,10,10);
        m_otxtRoomNo=new TextView(oContext);
        m_otxtRoomNo.setLayoutParams(m_txtCardNo);
        m_otxtRoomNo.setTextSize(16);
        m_otxtRoomNo.setGravity(Gravity.CENTER);
        m_otxtRoomNo.setText("房号");
        m_otxtRoomNo.setBackgroundResource(R.color.printbutton);
        m_otxtRoomNo.setTextColor(Color.WHITE);
        oMainWin_right_head.addView(m_otxtRoomNo);
        RelativeLayout.LayoutParams m_cardNumber = new RelativeLayout.LayoutParams(MainActivity.mSreenWidth*3/4/5, 100) ;
//        m_txtCardNo.setMargins(40,40,10,10);
        m_ocardNumber=new TextView(oContext);
        m_ocardNumber.setLayoutParams(m_cardNumber);
        m_ocardNumber.setTextSize(16);
        m_ocardNumber.setGravity(Gravity.CENTER);
        m_ocardNumber.setText("卡号");
        m_ocardNumber.setBackgroundResource(R.color.printbutton);
        m_ocardNumber.setTextColor(Color.WHITE);
        oMainWin_right_head.addView(m_ocardNumber);
        RelativeLayout.LayoutParams m_orderTime = new RelativeLayout.LayoutParams(MainActivity.mSreenWidth*3/4/5, 100) ;
//        m_txtCardNo.setMargins(40,40,10,10);
        m_oorderTime=new TextView(oContext);
        m_oorderTime.setLayoutParams(m_orderTime);
        m_oorderTime.setTextSize(16);
        m_oorderTime.setGravity(Gravity.CENTER);
        m_oorderTime.setText("下单时间");
        m_oorderTime.setBackgroundResource(R.color.printbutton);
        m_oorderTime.setTextColor(Color.WHITE);
        oMainWin_right_head.addView(m_oorderTime);
        RelativeLayout.LayoutParams m_salseMan = new RelativeLayout.LayoutParams(MainActivity.mSreenWidth*3/4/5, 100) ;
//        m_txtCardNo.setMargins(40,40,10,10);
        m_osalseman=new TextView(oContext);
        m_osalseman.setLayoutParams(m_salseMan);
        m_osalseman.setTextSize(16);
        m_osalseman.setGravity(Gravity.CENTER);
        m_osalseman.setText("销售员");
        m_osalseman.setBackgroundResource(R.color.printbutton);
        m_osalseman.setTextColor(Color.WHITE);
        oMainWin_right_head.addView(m_osalseman);

        wh_listview = new ListView(oContext);
        wh_listview.setLayoutParams( new RelativeLayout.LayoutParams(MainActivity.mSreenWidth*3/4, LayoutParams.WRAP_CONTENT)) ;
        wh_listview.setBackgroundColor(Color.WHITE);
        wh_listview.setOnItemClickListener(lv_listener);
//        m_oWareHouseDetailListAdapter = new GWareHouseAdapter(MainActivity.m_oMainActivity);
//        m_oWareHouseDetailListDAO = new GHttpDAO(MainActivity.m_oMainActivity,m_oWareHouseDetailListAdapter);
//        wh_listview.setAdapter(m_oWareHouseDetailListAdapter);
//        m_oWareHouseDetailListDAO.getWareHouseDetail();
        //=============================================================

       handler.postDelayed(runnable,1000); // 开始Timer
        wh_listview_detail = new ListView(oContext);
        wh_listview_detail.setLayoutParams( new RelativeLayout.LayoutParams(MainActivity.mSreenWidth*3/4, LayoutParams.WRAP_CONTENT)) ;
        wh_listview_detail.setVisibility(View.GONE);




        oMainWin_right.addView(oMainWin_right_head);
        oMainWin_right.addView(wh_listview);
        oMainWin_right.addView(wh_listview_detail);
        oMainWin.addView(oMainWin_left);
        oMainWin.addView(oMainWin_right);
		return oMainWin;
	}


	//=============================================================================================
	//建立页面头部
	private View onCreatePageHead(Context oContext)
	{
		RelativeLayout oSubHeader = new RelativeLayout(oContext);  //线性布局方式               
        oSubHeader.setLayoutParams( new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT,100) );
        oSubHeader.setBackgroundColor(MainActivity.m_oHeadStoreColor);
	        //=============================================================================


		//实时时间
		RelativeLayout.LayoutParams pCurrentTime = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT) ;
		pCurrentTime.addRule(RelativeLayout.ALIGN_LEFT);
		pCurrentTime.addRule(RelativeLayout.CENTER_VERTICAL);
		m_oCurrentTime=new TextView(oContext);
		m_oCurrentTime.setLayoutParams( pCurrentTime);
		m_oCurrentTime.setTextSize(16);
		m_oCurrentTime.setText(MainActivity.getStringDate());
		m_oCurrentTime.setTextColor(Color.BLACK);

		oSubHeader.addView(m_oCurrentTime);
	        //头像
        RelativeLayout.LayoutParams pImgHead = new RelativeLayout.LayoutParams(80,80);
        pImgHead.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        m_oImgHead = new RoundImageView(oContext);
        m_oImgHead.setLayoutParams( pImgHead );
        m_oImgHead.setImageResource(R.drawable.ic_launcher);
        m_oImgHead.setOnClickListener(new View.OnClickListener() {
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
        RelativeLayout.LayoutParams pShopName = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT) ;
        pShopName.addRule(RelativeLayout.CENTER_IN_PARENT);
        
        m_oShopCaption=new TextView(oContext);
        m_oShopCaption.setLayoutParams( pShopName);
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
	public void onScannerResult(String strCardNo,int requestCode)
	{
		if(requestCode== MainActivity.USER_GROUP_CHANGE && m_oShopCaption != null)
				m_oShopCaption.setText(GOperaterInfo.m_strGroupName);
	}

    protected void connectBLE() {
        // TODO Auto-generated method stub
        setbluetooth();
        //sendble();
    }
    public void sendble() {
        binder.connectBtPort(oTextBle.getText().toString(), new UiExecute() {

            @Override
            public void onsucess() {
                // TODO Auto-generated method stub
                //连接成功后在UI线程中的执行
                isConnect=true;
                Toast.makeText(MainActivity.m_oMainActivity,R.string.con_success,Toast.LENGTH_SHORT).show();
                oTextBle.setBackgroundResource(R.color.printbutton);

                //此处也可以开启读取打印机的数据
                //参数同样是一个实现的UiExecute接口对象
                //如果读的过程重出现异常，可以判断连接也发生异常，已经断开
                //这个读取的方法中，会一直在一条子线程中执行读取打印机发生的数据，
                //直到连接断开或异常才结束，并执行onfailed
                binder.acceptdatafromprinter(new UiExecute() {

                    @Override
                    public void onsucess() {
                        // TODO Auto-generated method stub

                    }

                    @Override
                    public void onfailed() {
                        // TODO Auto-generated method stub
                        isConnect=false;
                        Toast.makeText(MainActivity.m_oMainActivity, R.string.con_has_discon, Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onfailed() {
                // TODO Auto-generated method stub
                //连接失败后在UI线程中的执行
                isConnect=false;
                Toast.makeText(MainActivity.m_oMainActivity, R.string.con_has_discon, Toast.LENGTH_SHORT).show();
                //btn0.setText("连接失败");
            }
        });
    }
    protected void setbluetooth() {
        // TODO Auto-generated method stub
        blueadapter= BluetoothAdapter.getDefaultAdapter();
        //确认开启蓝牙
        if(!blueadapter.isEnabled()){
            //请求用户开启
            Intent intent=new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            MainActivity.m_oMainActivity.startActivityForResult(intent, Conts.ENABLE_BLUETOOTH);

        } else {
            //蓝牙已开启
            showblueboothlist();
        }

    }

    private void showblueboothlist() {
        if (!blueadapter.isDiscovering()) {
            blueadapter.startDiscovery();
        }
        LayoutInflater inflater=LayoutInflater.from(MainActivity.m_oMainActivity);
        dialogView=inflater.inflate(R.layout.printer_list, null);
        adapter1=new ArrayAdapter<String>(MainActivity.m_oMainActivity, android.R.layout.simple_list_item_1, deviceList_bonded);
        lv1=(ListView) dialogView.findViewById(R.id.listView1);
        btn_scan=(Button) dialogView.findViewById(R.id.btn_scan);
        ll1=(LinearLayout) dialogView.findViewById(R.id.ll1);
        lv2=(ListView) dialogView.findViewById(R.id.listView2);
        adapter2=new ArrayAdapter<String>(MainActivity.m_oMainActivity, android.R.layout.simple_list_item_1, deviceList_found);
        lv1.setAdapter(adapter1);
        lv2.setAdapter(adapter2);
        dialog=new AlertDialog.Builder(MainActivity.m_oMainActivity).setTitle("BLE").setView(dialogView).create();
        dialog.show();
        setlistener();
        findAvalibleDevice();
    }
    private void setlistener() {
        // TODO Auto-generated method stub
        btn_scan.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                ll1.setVisibility(View.VISIBLE);
                //btn_scan.setVisibility(View.GONE);
            }
        });
        //已配对的设备的点击连接
        lv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                // TODO Auto-generated method stub
                try {
                    if(blueadapter!=null&&blueadapter.isDiscovering()){
                        blueadapter.cancelDiscovery();

                    }

                    String msg=deviceList_bonded.get(arg2);
                    mac=msg.substring(msg.length()-17);
                    String name=msg.substring(0, msg.length()-18);
                    //lv1.setSelection(arg2);
                    dialog.cancel();
                    oTextBle.setText(mac);

                    //Log.i("TAG", "mac="+mac);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });
        //未配对的设备，点击，配对，再连接
        lv2.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                // TODO Auto-generated method stub
                try {
                    if(blueadapter!=null&&blueadapter.isDiscovering()){
                        blueadapter.cancelDiscovery();

                    }
                    String msg=deviceList_found.get(arg2);
                    mac=msg.substring(msg.length()-17);
                    String name=msg.substring(0, msg.length()-18);
                    //lv2.setSelection(arg2);
                    dialog.cancel();
                    oTextBle.setText(mac);

                    Log.i("TAG", "mac="+mac);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });
    }

    private void findAvalibleDevice() {
        // TODO Auto-generated method stub
        //获取可配对蓝牙设备
        Set<BluetoothDevice> device=blueadapter.getBondedDevices();

        deviceList_bonded.clear();
        if(blueadapter!=null&&blueadapter.isDiscovering()){
            adapter1.notifyDataSetChanged();
        }
        if(device.size()>0){
            //存在已经配对过的蓝牙设备
            for(Iterator<BluetoothDevice> it = device.iterator(); it.hasNext();){
                BluetoothDevice btd=it.next();
                deviceList_bonded.add(btd.getName()+'\n'+btd.getAddress());
                adapter1.notifyDataSetChanged();
            }
        }else{  //不存在已经配对过的蓝牙设备
            deviceList_bonded.add("No can be matched to use bluetooth");
            adapter1.notifyDataSetChanged();
        }

    }

}