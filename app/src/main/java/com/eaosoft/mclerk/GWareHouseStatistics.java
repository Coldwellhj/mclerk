package com.eaosoft.mclerk;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.eaosoft.adapter.GHttpDAO;
import com.eaosoft.adapter.GWareHouseStatisticsGoodsAdapter;
import com.eaosoft.userinfo.GOperaterInfo;
import com.eaosoft.util.GUtilSDCard;
import com.eaosoft.view.LoadMoreListView;
import com.eaosoft.view.RoundImageView;

import net.posprinter.posprinterface.ProcessData;
import net.posprinter.posprinterface.UiExecute;
import net.posprinter.utils.DataForSendToPrinterTSC;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;


import static com.eaosoft.mclerk.GWareHouseMainActivity.isConnect;
import static com.eaosoft.mclerk.MainActivity.binder;

public class GWareHouseStatistics extends Activity implements View.OnClickListener {

    private TextView currentTime;
    private TextView store;
    private RoundImageView personal;
    private TextView dateTime;
    private TextView dateTime_end;
    private LinearLayout head;
    private LoadMoreListView lv_goodsStatistics;
    private Button statistics_search;
    private Button statistics_print;
//    private Button look_statistics_detail;
    private GWareHouseStatisticsGoodsAdapter m_oWareHouseStatisticsAdapter=null;
    private GHttpDAO m_oWareHouseDetailListDAO = null;
    private String dayTime;
    private String dayTime1;
    private List ar =null;
    private Handler handler = new Handler();
    private Runnable runnable1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_gware_house_statistics);
        initView();
        initData();
        runnable1 = new Runnable() {
            public void run() {
                currentTime.setText(MainActivity.getStringDate());
                handler.postDelayed(this,1000);
                //postDelayed(this,18000)方法安排一个Runnable对象到主线程队列中
            }
        };
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
        handler.postDelayed(runnable1, 1000); // 开始Timer
        store.setText(GOperaterInfo.m_strGroupName);
        personal.setImageResource(R.drawable.ic_launcher);
        if(GUtilSDCard.isFileExist(GOperaterInfo.m_strLocalDiskImage ))
        {
            Bitmap photo = BitmapFactory.decodeFile(GOperaterInfo.m_strLocalDiskImage);
            if(photo !=null && personal!=null)
                personal.setImageBitmap(photo);
        }
        dayTime=currentTime.getText().toString().trim().split(" ")[0];
        dayTime1=currentTime.getText().toString().trim().split(" ")[0];
        dateTime.setText(dayTime);
        dateTime_end.setText(dayTime);
        m_oWareHouseStatisticsAdapter = new GWareHouseStatisticsGoodsAdapter(GWareHouseStatistics.this);
        m_oWareHouseDetailListDAO = new GHttpDAO(GWareHouseStatistics.this, m_oWareHouseStatisticsAdapter);
        lv_goodsStatistics.setAdapter(m_oWareHouseStatisticsAdapter);
        m_oWareHouseDetailListDAO.getWareHouseGoodsStatistics_Search(dayTime,dayTime1);

    }

    private void initView() {
        currentTime = (TextView) findViewById(R.id.currentTime);
        store = (TextView) findViewById(R.id.store);
        head = (LinearLayout) findViewById(R.id.head);
        personal = (RoundImageView) findViewById(R.id.personal);
        dateTime = (TextView) findViewById(R.id.dateTime);
        dateTime_end = (TextView) findViewById(R.id.dateTime_end);
        lv_goodsStatistics = (LoadMoreListView)findViewById(R.id.lv_goodsStatistics);
        statistics_search = (Button) findViewById(R.id.statistics_search);
        statistics_print = (Button) findViewById(R.id.statistics_print);
//        look_statistics_detail = (Button) findViewById(R.id.look_statistics_detail);
        statistics_search.setOnClickListener(this);
        statistics_print.setOnClickListener(this);
//        look_statistics_detail.setOnClickListener(this);
        dateTime.setOnClickListener(this);
        dateTime_end.setOnClickListener(this);
        personal.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.statistics_search:
                if(dateTime.getText().toString().trim().isEmpty()){
                    dayTime=currentTime.getText().toString().trim().split(" ")[0];
                }else {
                    dayTime=dateTime.getText().toString().trim();
                }
                if(dateTime_end.getText().toString().trim().isEmpty()){
                    dayTime1=currentTime.getText().toString().trim().split(" ")[0];
                }else {
                    dayTime1=dateTime_end.getText().toString().trim();
                }
                if(dayTime.compareTo(dayTime1)<=0){
                    m_oWareHouseDetailListDAO.getWareHouseGoodsStatistics_Search(dayTime,dayTime1);
                }else {
                    Toast.makeText(GWareHouseStatistics.this,"开始日期必须小于结束日期",Toast.LENGTH_LONG);
                }

                break;
            case R.id.statistics_print:
               ar=m_oWareHouseStatisticsAdapter.getData();

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

                                list.add(("            "+GOperaterInfo.m_strGroupName + "\n").getBytes("gbk"));
                                list.add(("日期："+dayTime + "\n").getBytes("gbk"));
                                list.add(("----------------------------"+ "\n").getBytes("gbk"));
                                list.add(("      名称      " + "数量  " + "单位  " + "\n").getBytes("gbk"));
                                list.add(("----------------------------"+ "\n").getBytes("gbk"));
                                if (ar.size()>0) {
                                    for(int i =0;i<ar.size();i++){
                                        Map map = (Map)ar.get(i);
                                        String m_ogoodsCaption = (String)  map.get("goodsCaption");
                                        String m_ogoodsNumber = (String) map.get("goodsNumber");
                                        String m_ogoodsUnitName = (String) map.get("goodsUnitName");
                                        String m_otasteuids = (String) map.get("tasteuids");
                                        if (m_ogoodsCaption.getBytes("GBK").length == 1) {
                                            list.add(("      "+m_ogoodsCaption + "           ").getBytes("gbk"));
                                            list.add((m_ogoodsNumber + "   ").getBytes("gbk"));
                                            list.add((m_ogoodsUnitName + "   ").getBytes("gbk"));
                                            list.add((m_otasteuids + "\n").getBytes("gbk"));
                                        } else if (m_ogoodsCaption.getBytes("GBK").length == 2) {
                                            list.add(("      "+m_ogoodsCaption + "          ").getBytes("gbk"));
                                            list.add((m_ogoodsNumber + "   ").getBytes("gbk"));
                                            list.add((m_ogoodsUnitName + "   ").getBytes("gbk"));
                                            list.add((m_otasteuids + "\n").getBytes("gbk"));
                                        }else if (m_ogoodsCaption.getBytes("GBK").length == 3) {
                                            list.add(("      "+m_ogoodsCaption + "         ").getBytes("gbk"));
                                            list.add((m_ogoodsNumber + "   ").getBytes("gbk"));
                                            list.add((m_ogoodsUnitName + "   ").getBytes("gbk"));
                                            list.add((m_otasteuids + "\n").getBytes("gbk"));
                                        } else if(m_ogoodsCaption.getBytes("GBK").length == 4) {
                                            list.add(("      "+m_ogoodsCaption + "        ").getBytes("gbk"));
                                            list.add((m_ogoodsNumber + "   ").getBytes("gbk"));
                                            list.add((m_ogoodsUnitName + "   ").getBytes("gbk"));
                                            list.add((m_otasteuids + "\n").getBytes("gbk"));
                                        } else if(m_ogoodsCaption.getBytes("GBK").length== 5) {
                                            list.add(("      "+m_ogoodsCaption + "       ").getBytes("gbk"));
                                            list.add((m_ogoodsNumber + "   ").getBytes("gbk"));
                                            list.add((m_ogoodsUnitName + "   ").getBytes("gbk"));
                                            list.add((m_otasteuids + "\n").getBytes("gbk"));
                                        }else if(m_ogoodsCaption.getBytes("GBK").length == 6) {
                                            list.add(("      "+m_ogoodsCaption + "      ").getBytes("gbk"));
                                            list.add((m_ogoodsNumber + "   ").getBytes("gbk"));
                                            list.add((m_ogoodsUnitName + "   ").getBytes("gbk"));
                                            list.add((m_otasteuids + "\n").getBytes("gbk"));
                                        }else if(m_ogoodsCaption.getBytes("GBK").length== 7) {
                                            list.add(("      "+m_ogoodsCaption + "     ").getBytes("gbk"));
                                            list.add((m_ogoodsNumber + "   ").getBytes("gbk"));
                                            list.add((m_ogoodsUnitName + "   ").getBytes("gbk"));
                                            list.add((m_otasteuids + "\n").getBytes("gbk"));
                                        }else if(m_ogoodsCaption.getBytes("GBK").length == 8) {
                                            list.add(("    "+m_ogoodsCaption + "      ").getBytes("gbk"));
                                            list.add((m_ogoodsNumber + "   ").getBytes("gbk"));
                                            list.add((m_ogoodsUnitName + "   ").getBytes("gbk"));
                                            list.add((m_otasteuids + "\n").getBytes("gbk"));
                                        } else if(m_ogoodsCaption.getBytes("GBK").length == 9) {
                                            list.add(("    "+m_ogoodsCaption + "     ").getBytes("gbk"));
                                            list.add((m_ogoodsNumber + "   ").getBytes("gbk"));
                                            list.add((m_ogoodsUnitName + "   ").getBytes("gbk"));
                                            list.add((m_otasteuids + "\n").getBytes("gbk"));
                                        }else if(m_ogoodsCaption.getBytes("GBK").length == 10) {
                                            list.add(("   "+m_ogoodsCaption + "     ").getBytes("gbk"));
                                            list.add((m_ogoodsNumber + "   ").getBytes("gbk"));
                                            list.add((m_ogoodsUnitName + "   ").getBytes("gbk"));
                                            list.add((m_otasteuids + "\n").getBytes("gbk"));
                                        }else if(m_ogoodsCaption.getBytes("GBK").length == 11) {
                                            list.add(("    "+m_ogoodsCaption + "    ").getBytes("gbk"));
                                            list.add((m_ogoodsNumber + "   ").getBytes("gbk"));
                                            list.add((m_ogoodsUnitName + "   ").getBytes("gbk"));
                                            list.add((m_otasteuids + "\n").getBytes("gbk"));
                                        }else if(m_ogoodsCaption.getBytes("GBK").length == 12) {
                                            list.add(("  "+m_ogoodsCaption + "     ").getBytes("gbk"));
                                            list.add((m_ogoodsNumber + "   ").getBytes("gbk"));
                                            list.add((m_ogoodsUnitName + "   ").getBytes("gbk"));
                                            list.add((m_otasteuids + "\n").getBytes("gbk"));
                                        }else if(m_ogoodsCaption.getBytes("GBK").length == 13) {
                                            list.add(("  "+m_ogoodsCaption + "    ").getBytes("gbk"));
                                            list.add((m_ogoodsNumber + "   ").getBytes("gbk"));
                                            list.add((m_ogoodsUnitName + "   ").getBytes("gbk"));
                                            list.add((m_otasteuids + "\n").getBytes("gbk"));
                                        }else if(m_ogoodsCaption.getBytes("GBK").length == 14) {
                                            list.add((" "+m_ogoodsCaption + "    ").getBytes("gbk"));
                                            list.add((m_ogoodsNumber + "   ").getBytes("gbk"));
                                            list.add((m_ogoodsUnitName + "   ").getBytes("gbk"));
                                            list.add((m_otasteuids + "\n").getBytes("gbk"));
                                        }else if(m_ogoodsCaption.getBytes("GBK").length == 15) {
                                            list.add((" "+m_ogoodsCaption + "   ").getBytes("gbk"));
                                            list.add((m_ogoodsNumber + "   ").getBytes("gbk"));
                                            list.add((m_ogoodsUnitName + "   ").getBytes("gbk"));
                                            list.add((m_otasteuids + "\n").getBytes("gbk"));
                                        }else if(m_ogoodsCaption.getBytes("GBK").length == 16) {
                                            list.add((""+m_ogoodsCaption + "   ").getBytes("gbk"));
                                            list.add((m_ogoodsNumber + "   ").getBytes("gbk"));
                                            list.add((m_ogoodsUnitName + "   ").getBytes("gbk"));
                                            list.add((m_otasteuids + "\n").getBytes("gbk"));
                                        }else if(m_ogoodsCaption.getBytes("GBK").length == 17) {
                                            list.add((""+m_ogoodsCaption + "  ").getBytes("gbk"));
                                            list.add((m_ogoodsNumber + "   ").getBytes("gbk"));
                                            list.add((m_ogoodsUnitName + "   ").getBytes("gbk"));
                                            list.add((m_otasteuids + "\n").getBytes("gbk"));
                                        }else if(m_ogoodsCaption.getBytes("GBK").length == 18) {
                                            list.add((""+m_ogoodsCaption + " ").getBytes("gbk"));
                                            list.add((m_ogoodsNumber + "   ").getBytes("gbk"));
                                            list.add((m_ogoodsUnitName + "   ").getBytes("gbk"));
                                            list.add((m_otasteuids + "\n").getBytes("gbk"));
                                        }
                                    }
                                    list.add(( "\n").getBytes("gbk"));
                                    list.add(( "\n").getBytes("gbk"));





                                    }


                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }


                            DataForSendToPrinterTSC.print(1);
                            return list;
                        }
                    });
                } else {
                    Toast.makeText(MainActivity.m_oMainActivity, R.string.not_con_printer, Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.dateTime:
                AlertDialog.Builder builder = new AlertDialog.Builder(GWareHouseStatistics.this);
                View view = View.inflate(GWareHouseStatistics.this, R.layout.date_time_dialog, null);
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
            case R.id.dateTime_end:
                AlertDialog.Builder builder1 = new AlertDialog.Builder(GWareHouseStatistics.this);
                View view1 = View.inflate(GWareHouseStatistics.this, R.layout.date_time_dialog, null);
                final DatePicker datePicker1 = (DatePicker) view1.findViewById(R.id.date_picker);
//			final TimePicker timePicker = (TimePicker) view.findViewById(R.id.time_picker);
                builder1.setView(view1);
                Calendar cal1 = Calendar.getInstance();
                cal1.setTimeInMillis(System.currentTimeMillis());
                datePicker1.init(cal1.get(Calendar.YEAR), cal1.get(Calendar.MONTH), cal1.get(Calendar.DAY_OF_MONTH), null);
//			timePicker.setIs24HourView(true);
//			timePicker.setCurrentHour(cal.get(Calendar.HOUR_OF_DAY));
//			timePicker.setCurrentMinute(Calendar.MINUTE);


                final int inType1 = dateTime.getInputType();
                dateTime_end.setInputType(InputType.TYPE_NULL);

                dateTime_end.setInputType(inType1);
                //etStartTime.setSelection(etStartTime.getText().length());

                builder1.setTitle("请确定查询日期");
                builder1.setPositiveButton("确  定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        StringBuffer sb = new StringBuffer();
                        sb.append(String.format("%d-%02d-%02d",
                                datePicker1.getYear(),
                                datePicker1.getMonth() + 1,
                                datePicker1.getDayOfMonth()
                        ));
//					sb.append(timePicker.getCurrentHour())
//							.append(":").append(timePicker.getCurrentMinute());
                        dateTime_end.setText(sb);
                        dialog.cancel();
                    }
                });
                builder1.setNegativeButton("取  消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dateTime_end.setText("");
                        dialog.cancel();
                    }
                });


                Dialog dialog1 = builder1.create();
                dialog1.show();
                break;
            case R.id.personal:
                Intent intent_personal = new Intent(GWareHouseStatistics.this, GActUserInfo.class);
                startActivity(intent_personal);
                break;
        }
    }
}
