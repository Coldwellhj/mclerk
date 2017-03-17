package com.eaosoft.mclerk;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.eaosoft.adapter.GHttpDAO;
import com.eaosoft.adapter.GWareHouseAdapter;
import com.eaosoft.adapter.GWareHouseGoodsAdapter;
import com.eaosoft.userinfo.GOperaterInfo;

import net.posprinter.posprinterface.ProcessData;
import net.posprinter.posprinterface.UiExecute;
import net.posprinter.utils.DataForSendToPrinterTSC;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.eaosoft.mclerk.GWareHouseMain.isConnect;
import static com.eaosoft.mclerk.MainActivity.binder;

public class GWareHouseOrderDetail extends Activity implements View.OnClickListener{

    private TextView tv_package;
    private TextView orderNumber;
    private TextView orderTime;
    private TextView roomNumber;
    private TextView orderName;
    private ListView lv_order_detail;
    private String orderUID;
    private String fillPrint=null;
    private String roomSerialNo;
    private String cardUID;
    private String ordertime;
    private String userCaption;
    private String taskUID;
    private List<Map> ar1;
    private Button confirm;
    public GWareHouseGoodsAdapter adapter;
    private GWareHouseAdapter m_oWareHouseDetailListAdapter = null;
    private GHttpDAO m_oWareHouseDetailListDAO = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_order_detail);
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
        Intent intent = this.getIntent();
        orderUID = intent.getStringExtra("orderUID");
        roomSerialNo = intent.getStringExtra("roomSerialNo");
        cardUID = intent.getStringExtra("cardUID");
        ordertime = intent.getStringExtra("orderTime");
        userCaption = intent.getStringExtra("userCaption");
        taskUID = intent.getStringExtra("taskUID");
        ar1 = (List<Map>)intent.getSerializableExtra("ar1") ;
        fillPrint = intent.getStringExtra("fillPrint");
        orderNumber.setText(orderUID);
        orderTime.setText(ordertime);
        roomNumber.setText(roomSerialNo);
        orderName.setText(userCaption);
        adapter=new GWareHouseGoodsAdapter(this,ar1);
        lv_order_detail.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        if(fillPrint.equals("fillPrint")){
            confirm.setText("确认补打");
        }
    }

    private void initView() {
//        tv_package = (TextView) findViewById(R.id.tv_package);
        orderNumber = (TextView) findViewById(R.id.orderNumber);
        orderTime = (TextView) findViewById(R.id.orderTime);
        roomNumber = (TextView) findViewById(R.id.roomNumber);
        orderName = (TextView) findViewById(R.id.orderName);
        lv_order_detail = (ListView) findViewById(R.id.lv_order_detail);
        confirm = (Button) findViewById(R.id.confirm);


        confirm.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.confirm:
                if("确认打印".equals(confirm.getText().toString())){
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
                                if (orderUID != null) {

                                    m_oWareHouseDetailListDAO = new GHttpDAO(MainActivity.m_oMainActivity, m_oWareHouseDetailListAdapter);
                                    m_oWareHouseDetailListDAO.opPrnTaskComplete(taskUID);
                                  finish();
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



                                    if (orderUID != null) {


                                        list.add(("            "+GOperaterInfo.m_strGroupName + "\n").getBytes("gbk"));
                                        list.add(("单号：" + orderUID + "\n").getBytes("gbk"));
                                        list.add(("房号：" + roomSerialNo + "\n").getBytes("gbk"));
                                        list.add(("卡号：" + cardUID + "\n").getBytes("gbk"));
                                        list.add(("----------------------------"+ "\n").getBytes("gbk"));
                                        list.add(("      名称      " + "数量  " + "单位  " + "\n").getBytes("gbk"));
                                        list.add(("----------------------------"+ "\n").getBytes("gbk"));
                                        for (int i = 0; i <ar1.size(); i++) {
                                            String m_ogoodsCaption = (String) ar1.get(i).get("goodsCaption");
                                            String m_ogoodsNumber = (String) ar1.get(i).get("goodsNumber");
                                            String m_ogoodsUnitName = (String) ar1.get(i).get("goodsUnitName");
                                            if (m_ogoodsCaption.length() == 2) {
                                                list.add(("      "+m_ogoodsCaption + "        ").getBytes("gbk"));
                                                list.add((m_ogoodsNumber + "    ").getBytes("gbk"));
                                                list.add((m_ogoodsUnitName + "\n").getBytes("gbk"));
                                            } else {
                                                list.add(("    "+m_ogoodsCaption + "    ").getBytes("gbk"));
                                                list.add((m_ogoodsNumber + "    ").getBytes("gbk"));
                                                list.add((m_ogoodsUnitName + "\n").getBytes("gbk"));
                                            }
                                        }

                                        list.add(("----------------------------"+ "\n").getBytes("gbk"));
                                        list.add(("下单时间：" + ordertime + "\n").getBytes("gbk"));
                                        list.add(("销售员：" + userCaption + "\n").getBytes("gbk"));
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
                }else if("确认补打".equals(confirm.getText().toString())){
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
                                if (orderUID != null) {
                                    m_oWareHouseDetailListDAO = new GHttpDAO(GWareHouseFillPrint.m_oGWareHouseFillPrintActivity, m_oWareHouseDetailListAdapter);
                                    m_oWareHouseDetailListDAO.opPrnTaskCompleteAgain(taskUID);
                                    finish();
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



                                    if (orderUID != null) {


                                        list.add((GOperaterInfo.m_strGroupName + "\n").getBytes("gbk"));
                                        list.add(("单号：" + orderUID + "\n").getBytes("gbk"));
                                        list.add(("房号：" + roomSerialNo + "\n").getBytes("gbk"));
                                        list.add(("卡号：" + cardUID + "\n").getBytes("gbk"));
                                        list.add(("名称      " + "数量  " + "单位  " + "\n").getBytes("gbk"));
                                        for (int i = 0; i <ar1.size(); i++) {
                                            String m_ogoodsCaption = (String) ar1.get(i).get("goodsCaption");
                                            String m_ogoodsNumber = (String) ar1.get(i).get("goodsNumber");
                                            String m_ogoodsUnitName = (String) ar1.get(i).get("goodsUnitName");
                                            if (m_ogoodsCaption.length() == 2) {
                                                list.add((m_ogoodsCaption + "        ").getBytes("gbk"));
                                                list.add((m_ogoodsNumber + "    ").getBytes("gbk"));
                                                list.add((m_ogoodsUnitName + "\n").getBytes("gbk"));
                                            } else if (m_ogoodsCaption.length() == 4) {
                                                list.add((m_ogoodsCaption + "    ").getBytes("gbk"));
                                                list.add((m_ogoodsNumber + "    ").getBytes("gbk"));
                                                list.add((m_ogoodsUnitName + "\n").getBytes("gbk"));
                                            }
                                        }


                                        list.add(("下单时间：" + ordertime + "\n").getBytes("gbk"));
                                        list.add(("销售员：" + userCaption + "\n").getBytes("gbk"));
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
                }

        }
    }
}
