package com.eaosoft.mclerk;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Handler;
import android.text.InputType;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.eaosoft.adapter.GHttpDAO;
import com.eaosoft.adapter.GWareHouseAdapter;
import com.eaosoft.userinfo.GOperaterInfo;
import com.eaosoft.util.GUtilSDCard;
import com.eaosoft.view.LoadMoreListView;
import com.eaosoft.view.RoundImageView;

import java.io.Serializable;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

public class GWareHouseFillPrintMain implements View.OnTouchListener {
	private RelativeLayout m_oUserView = null;
	private Context m_oContext = null;
	//=====================================================
	public RoundImageView m_oImgHead = null;
	public TextView m_oShopCaption = null;
	public TextView m_otxtRoomNo = null;
	public EditText m_oEidtRoomNo = null;
	public EditText m_oEidtCardnumbers = null;
	public TextView m_oTextdateTime = null;
	public TextView orderNumber = null;
	public TextView m_ocardNumber = null;
	public TextView m_oorderTime = null;
	public TextView m_osalseman = null;
	public LoadMoreListView wh_fillprint_listview = null;
	public TextView m_oCurrentTime = null;
	public TextView m_ocardnumbers = null;
	public TextView m_odateTime = null;
	public String roomNo;
	public String cardNo;
	public String orderTime;
	public Integer page = 1;
	public Integer totalpage;
    private Handler handler = new Handler();
    private Runnable runnable1;
	private GWareHouseAdapter m_oWareHouseDetailListAdapter = null;

	private GHttpDAO m_oWareHouseDetailListDAO = null;

	//=====================================================
	public GWareHouseFillPrintMain(Context oContext) {
		m_oContext = oContext;
	}

	public View OnCreateView() {
		m_oUserView = new RelativeLayout(m_oContext);
		m_oUserView.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));

        runnable1 = new Runnable() {
            public void run() {
                m_oCurrentTime.setText(MainActivity.getStringDate());
                handler.postDelayed(this,1000);
                //postDelayed(this,18000)方法安排一个Runnable对象到主线程队列中
            }
        };
		//============================================================================
		//主背景
		LinearLayout oMainWin = new LinearLayout(m_oContext);  //线性布局方式
		oMainWin.setOrientation(LinearLayout.VERTICAL); //控件对其方式为垂直排列  VERTICAL
		oMainWin.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		m_oUserView.addView(oMainWin);

		//===========================================================================
		//页面头--
		oMainWin.addView(onCreatePageHead(m_oContext));
		//==========================================================================
		//中间按钮和操作区
		oMainWin.addView(onCreatePageBody(m_oContext));
		//==========================================================================
		//快捷按钮区
		bindEvents();
		return m_oUserView;
	}

	public void onResume() {
		m_oWareHouseDetailListDAO.getWareHouseFillPrintDetail_Search(1, "", "", "");
	}

	AdapterView.OnItemClickListener lv_listener = new AdapterView.OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
			TextView tv = (TextView) wh_fillprint_listview.getChildAt(position).findViewById(R.id.lv_goodsTask);
			Map map = (Map) tv.getTag();
			Intent intent = new Intent(MainActivity.m_oMainActivity, GWareHouseOrderDetail.class);
			intent.putExtra("orderUID", (String) map.get("orderUID"));
			intent.putExtra("roomSerialNo", (String) map.get("roomSerialNo"));
			intent.putExtra("cardUID", (String) map.get("cardUID"));
			intent.putExtra("orderTime", (String) map.get("orderTime"));
			intent.putExtra("userCaption", (String) map.get("userCaption"));
			intent.putExtra("taskUID", (String) map.get("taskUID"));
			intent.putExtra("fillPrint", "fillPrint");
			intent.putExtra("ar1", (Serializable) (List<Map>) map.get("ar1"));


			MainActivity.m_oMainActivity.startActivity(intent);
		}
	};

	private View onCreatePageBody(Context oContext) {
		//======================================================================================
		//主背景
		LinearLayout oMainWin = new LinearLayout(oContext);  //线性布局方式
		oMainWin.setOrientation(LinearLayout.HORIZONTAL); //控件对其方式为水平排列
		oMainWin.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		//左半边操作按钮
		LinearLayout oMainWin_left = new LinearLayout(oContext);  //线性布局方式
		oMainWin_left.setOrientation(LinearLayout.VERTICAL); //控件对其方式为竖直排列
		oMainWin_left.setLayoutParams(new LayoutParams(MainActivity.mSreenWidth / 4, LayoutParams.MATCH_PARENT));
		oMainWin_left.setBackgroundResource(R.color.lightgray);
		//======================================================================================
		LinearLayout oMainWin_left_one = new LinearLayout(oContext);  //线性布局方式
		oMainWin_left_one.setOrientation(LinearLayout.HORIZONTAL); //控件对其方式为竖直排列
		oMainWin_left_one.setLayoutParams(new LayoutParams(MainActivity.mSreenWidth / 4, LayoutParams.WRAP_CONTENT));
		//======================================================================================
		RelativeLayout.LayoutParams m_txtCardNo = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		m_txtCardNo.setMargins(40, 40, 10, 10);
		m_otxtRoomNo = new TextView(oContext);
		m_otxtRoomNo.setLayoutParams(m_txtCardNo);
		m_otxtRoomNo.setTextSize(16);
		m_otxtRoomNo.setText("房号");
		m_otxtRoomNo.setTextColor(Color.BLACK);
		oMainWin_left_one.addView(m_otxtRoomNo);

		RelativeLayout.LayoutParams m_EditCardNo = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		m_EditCardNo.setMargins(40, 40, 10, 10);
		m_oEidtRoomNo = new EditText(oContext);
		m_oEidtRoomNo.setLayoutParams(m_EditCardNo);
		m_oEidtRoomNo.setTextSize(16);
//		m_oEidtCardNo.setHint("请输入房号");
		m_oEidtRoomNo.setBackgroundResource(R.drawable.tvborde);
		oMainWin_left_one.addView(m_oEidtRoomNo);

		LinearLayout oMainWin_left_two = new LinearLayout(oContext);  //线性布局方式
		oMainWin_left_two.setOrientation(LinearLayout.HORIZONTAL); //控件对其方式为竖直排列
		oMainWin_left_two.setLayoutParams(new LayoutParams(MainActivity.mSreenWidth / 4, LayoutParams.WRAP_CONTENT));

		RelativeLayout.LayoutParams m_oddnumbers = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		m_oddnumbers.setMargins(40, 40, 10, 10);
		m_ocardnumbers = new TextView(oContext);
		m_ocardnumbers.setLayoutParams(m_oddnumbers);
		m_ocardnumbers.setTextSize(16);
		m_ocardnumbers.setText("卡号");
		m_ocardnumbers.setTextColor(Color.BLACK);
		oMainWin_left_two.addView(m_ocardnumbers);
		RelativeLayout.LayoutParams m_EditOddnumbers = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		m_EditOddnumbers.setMargins(40, 40, 10, 10);
		m_oEidtCardnumbers = new EditText(oContext);
		m_oEidtCardnumbers.setLayoutParams(m_EditOddnumbers);
		m_oEidtCardnumbers.setTextSize(16);

		m_oEidtCardnumbers.setBackgroundResource(R.drawable.tvborde);
		oMainWin_left_two.addView(m_oEidtCardnumbers);

		LinearLayout oMainWin_left_three = new LinearLayout(oContext);  //线性布局方式
		oMainWin_left_three.setOrientation(LinearLayout.HORIZONTAL); //控件对其方式为水平排列
		oMainWin_left_three.setLayoutParams(new LayoutParams(MainActivity.mSreenWidth / 4, LayoutParams.WRAP_CONTENT));

		RelativeLayout.LayoutParams m_dateTime = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		m_dateTime.setMargins(40, 40, 10, 10);
		m_odateTime = new TextView(oContext);
		m_odateTime.setLayoutParams(m_dateTime);
		m_odateTime.setTextSize(16);
		m_odateTime.setText("日期");
		m_odateTime.setTextColor(Color.BLACK);
		oMainWin_left_three.addView(m_odateTime);
		RelativeLayout.LayoutParams m_EditdateTime = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		m_EditdateTime.setMargins(40, 40, 10, 10);
		m_oTextdateTime = new TextView(oContext);
		m_oTextdateTime.setLayoutParams(m_EditdateTime);
		m_oTextdateTime.setTextSize(16);
		m_oTextdateTime.setOnTouchListener(this);
		m_oTextdateTime.setBackgroundResource(R.drawable.tvborde);
		oMainWin_left_three.addView(m_oTextdateTime);

		oMainWin_left.addView(oMainWin_left_one);
		oMainWin_left.addView(oMainWin_left_two);
		oMainWin_left.addView(oMainWin_left_three);
		RelativeLayout.LayoutParams m_oBtnPrintOrder = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		m_oBtnPrintOrder.setMargins(40, 40, 40, 10);
		Button oBtnPrintOrder = new Button(m_oContext);
		oBtnPrintOrder.setLayoutParams(m_oBtnPrintOrder);
		oBtnPrintOrder.setBackgroundResource(R.color.printbutton);
		oBtnPrintOrder.setText("查询订单");
		oBtnPrintOrder.setTextColor(oBtnPrintOrder.getResources().getColor(android.R.color.white));
		oBtnPrintOrder.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				roomNo = m_oEidtRoomNo.getText().toString().trim();
				cardNo = m_oEidtCardnumbers.getText().toString().trim();
				orderTime = m_oTextdateTime.getText().toString().trim();
				wh_fillprint_listview.setAdapter(m_oWareHouseDetailListAdapter);
				m_oWareHouseDetailListDAO.getWareHouseFillPrintDetail_Search(1, roomNo, cardNo, orderTime);

			}
		});
		oMainWin_left.addView(oBtnPrintOrder);


//        RelativeLayout.LayoutParams m_oBtnPrintConform= new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT) ;
//		m_oBtnPrintConform.setMargins(40,40,40,10);
//        Button	oBtnViewReport = new Button(m_oContext);
//        oBtnViewReport.setLayoutParams(m_oBtnPrintConform);
//        oBtnViewReport.setBackgroundResource(R.color.printbutton);
//        oBtnViewReport.setText("确认补打");
//        oBtnViewReport.setTextColor(oBtnPrintOrder.getResources().getColor(android.R.color.white));
//        oBtnViewReport.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//				confirmprintall();
//            }});
//		oMainWin_left.addView(oBtnViewReport);
		//======================================================================================
		//右半边打印详细
		LinearLayout oMainWin_right = new LinearLayout(oContext);  //线性布局方式
		oMainWin_right.setOrientation(LinearLayout.VERTICAL); //控件对其方式为竖直排列
		oMainWin_right.setLayoutParams(new LayoutParams(MainActivity.mSreenWidth * 3 / 4, LayoutParams.MATCH_PARENT));

		LinearLayout oMainWin_right_head = new LinearLayout(oContext);  //线性布局方式
		oMainWin_right_head.setOrientation(LinearLayout.HORIZONTAL); //控件对其方式为水平排列
		oMainWin_right_head.setLayoutParams(new LayoutParams(MainActivity.mSreenWidth * 3 / 4, LayoutParams.WRAP_CONTENT));

		RelativeLayout.LayoutParams m_orderNumber = new RelativeLayout.LayoutParams(MainActivity.mSreenWidth * 3 / 4 / 5, 50);
//        m_orderNumber.setMargins(10,40,10,10);
		orderNumber = new TextView(oContext);
		orderNumber.setLayoutParams(m_orderNumber);
		orderNumber.setTextSize(16);
		orderNumber.setGravity(Gravity.CENTER);
		orderNumber.setText("单号");
		orderNumber.setBackgroundResource(R.color.printbutton);
		orderNumber.setTextColor(Color.WHITE);
		oMainWin_right_head.addView(orderNumber);

		RelativeLayout.LayoutParams m_txtCardNum = new RelativeLayout.LayoutParams(MainActivity.mSreenWidth * 3 / 4 / 5, 50);
//        m_txtCardNo.setMargins(40,40,10,10);
		m_otxtRoomNo = new TextView(oContext);
		m_otxtRoomNo.setLayoutParams(m_txtCardNum);
		m_otxtRoomNo.setTextSize(16);
		m_otxtRoomNo.setGravity(Gravity.CENTER);
		m_otxtRoomNo.setText("房号");
		m_otxtRoomNo.setBackgroundResource(R.color.printbutton);
		m_otxtRoomNo.setTextColor(Color.WHITE);
		oMainWin_right_head.addView(m_otxtRoomNo);
		RelativeLayout.LayoutParams m_cardNumber = new RelativeLayout.LayoutParams(MainActivity.mSreenWidth * 3 / 4 / 5, 50);
//        m_txtCardNo.setMargins(40,40,10,10);
		m_ocardNumber = new TextView(oContext);
		m_ocardNumber.setLayoutParams(m_cardNumber);
		m_ocardNumber.setTextSize(16);
		m_ocardNumber.setGravity(Gravity.CENTER);
		m_ocardNumber.setText("卡号");
		m_ocardNumber.setBackgroundResource(R.color.printbutton);
		m_ocardNumber.setTextColor(Color.WHITE);
		oMainWin_right_head.addView(m_ocardNumber);
		RelativeLayout.LayoutParams m_orderTime = new RelativeLayout.LayoutParams(MainActivity.mSreenWidth * 3 / 4 / 5, 50);
//        m_txtCardNo.setMargins(40,40,10,10);
		m_oorderTime = new TextView(oContext);
		m_oorderTime.setLayoutParams(m_orderTime);
		m_oorderTime.setTextSize(16);
		m_oorderTime.setGravity(Gravity.CENTER);
		m_oorderTime.setText("下单时间");
		m_oorderTime.setBackgroundResource(R.color.printbutton);
		m_oorderTime.setTextColor(Color.WHITE);
		oMainWin_right_head.addView(m_oorderTime);
		RelativeLayout.LayoutParams m_salseMan = new RelativeLayout.LayoutParams(MainActivity.mSreenWidth * 3 / 4 / 5, 50);
//        m_txtCardNo.setMargins(40,40,10,10);
		m_osalseman = new TextView(oContext);
		m_osalseman.setLayoutParams(m_salseMan);
		m_osalseman.setTextSize(16);
		m_osalseman.setGravity(Gravity.CENTER);
		m_osalseman.setText("销售员");
		m_osalseman.setBackgroundResource(R.color.printbutton);
		m_osalseman.setTextColor(Color.WHITE);
		oMainWin_right_head.addView(m_osalseman);


		wh_fillprint_listview = new LoadMoreListView(oContext);
		wh_fillprint_listview.setLayoutParams(new RelativeLayout.LayoutParams(MainActivity.mSreenWidth * 3 / 4, LayoutParams.MATCH_PARENT));
		wh_fillprint_listview.setBackgroundColor(Color.WHITE);
		wh_fillprint_listview.setOnItemClickListener(lv_listener);
		m_oWareHouseDetailListAdapter = new GWareHouseAdapter(GWareHouseFillPrint.m_oGWareHouseFillPrintActivity);
		m_oWareHouseDetailListDAO = new GHttpDAO(GWareHouseFillPrint.m_oGWareHouseFillPrintActivity, m_oWareHouseDetailListAdapter);
		wh_fillprint_listview.setAdapter(m_oWareHouseDetailListAdapter);
		m_oWareHouseDetailListDAO.getWareHouseFillPrintDetail_Search(1, "", "", "");

		oMainWin_right.addView(oMainWin_right_head);
		oMainWin_right.addView(wh_fillprint_listview);
		oMainWin.addView(oMainWin_left);
		oMainWin.addView(oMainWin_right);
		return oMainWin;

	}

	//=============================================================================================
	//建立页面头部
	private View onCreatePageHead(Context oContext) {
		RelativeLayout oSubHeader = new RelativeLayout(oContext);  //线性布局方式               
		oSubHeader.setLayoutParams(new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, MainActivity.mSreenHeight/10));
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
        handler.postDelayed(runnable1, 1000); // 开始Timer
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



	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			AlertDialog.Builder builder = new AlertDialog.Builder(m_oContext);
			View view = View.inflate(MainActivity.m_oMainActivity, R.layout.date_time_dialog, null);
			final DatePicker datePicker = (DatePicker) view.findViewById(R.id.date_picker);
//			final TimePicker timePicker = (TimePicker) view.findViewById(R.id.time_picker);
			builder.setView(view);
			Calendar cal = Calendar.getInstance();
			cal.setTimeInMillis(System.currentTimeMillis());
			datePicker.init(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), null);
//			timePicker.setIs24HourView(true);
//			timePicker.setCurrentHour(cal.get(Calendar.HOUR_OF_DAY));
//			timePicker.setCurrentMinute(Calendar.MINUTE);


			final int inType = m_oTextdateTime.getInputType();
			m_oTextdateTime.setInputType(InputType.TYPE_NULL);
			m_oTextdateTime.onTouchEvent(event);
			m_oTextdateTime.setInputType(inType);
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
					m_oTextdateTime.setText(sb);
					dialog.cancel();
				}
			});
			builder.setNegativeButton("取  消", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					m_oTextdateTime.setText("");
					dialog.cancel();
				}
			});


			Dialog dialog = builder.create();
			dialog.show();
		}

		return true;
	}


	private void bindEvents() {
		wh_fillprint_listview.setOnLoadMoreListener(new LoadMoreListView.OnLoadMoreListener() {
			@Override
			public void onloadMore() {
				//System.out.println("==================1");
				loadfillPrintMore();
			}
		});
	}

	private void loadfillPrintMore() {

//		new Thread(){
//			@Override
//			public void run() {
//				super.run();
//				try {
//					Looper.prepare();
//					Thread.sleep(2000);
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
		if (wh_fillprint_listview.getChildAt(0) != null) {
			TextView tv = (TextView) wh_fillprint_listview.getChildAt(0).findViewById(R.id.lv_goodsTask);
			Map map = (Map) tv.getTag();
			if (page < (Integer) map.get("totalPages")) {
				m_oWareHouseDetailListDAO.getWareHouseFillPrintDetail_Search(++page, roomNo, cardNo, orderTime);
			} else {
				GWareHouseFillPrint.onUserMessageBox("", "已经到底了");
			}
		}
//		GWareHouseFillPrint.m_oGWareHouseFillPrintActivity.runOnUiThread(new Runnable() {
//			@Override
//			public void run() {
//
//				wh_fillprint_listview.setLoadCompleted();
//
//			}
//		});
//			}
//		}.start();
	}
}
