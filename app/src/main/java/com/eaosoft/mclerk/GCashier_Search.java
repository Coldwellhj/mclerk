package com.eaosoft.mclerk;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.eaosoft.adapter.GCashierSalesReportAdapter;
import com.eaosoft.adapter.GHttpDAO;
import com.eaosoft.userinfo.GOperaterInfo;
import com.eaosoft.util.Conts;
import com.eaosoft.util.GSvrChannel;
import com.eaosoft.util.GUtilSDCard;
import com.eaosoft.view.RoundImageView;

import net.posprinter.posprinterface.ProcessData;
import net.posprinter.posprinterface.UiExecute;
import net.posprinter.utils.DataForSendToPrinterTSC;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.eaosoft.mclerk.MainActivity.binder;

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
    public static Handler mHandler_GCashier;

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
    static boolean  isConnect;//用来标识连接状态的一个boolean值

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_gcashier__search);
        mHandler_GCashier=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what){
                    case 2:
                        ar=m_oCashierSalesReportAdapter.getData();
                        for(int i =0;i<ar.size();i++){
                            Map map=(Map)ar.get(i);
                            totalMoney+=Integer.parseInt((String)map.get("realMoney"));

                        }
                        number.setText(""+ar.size());
                        allPrice.setText(""+totalMoney);
                        break;
                }
            }
        };
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

        dateTime.setText("");
        m_oCashierSalesReportAdapter = new GCashierSalesReportAdapter(GCashier_Search.this);
        m_oWareHouseDetailListDAO = new GHttpDAO(GCashier_Search.this, m_oCashierSalesReportAdapter);
        lv_sales.setAdapter(m_oCashierSalesReportAdapter);

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
        connectBlueTooth = (Button) findViewById(R.id.connectBlueTooth);
        Connect = (Button) findViewById(R.id.Connect);
        number = (TextView) findViewById(R.id.number);
        allPrice = (TextView) findViewById(R.id.allPrice);
        print_sales = (Button) findViewById(R.id.print_sales);
        rl_sales_report = (RelativeLayout) findViewById(R.id.rl_sales_report);
        dateTime.setOnClickListener(this);
        card_search.setOnClickListener(this);
        connectBlueTooth.setOnClickListener(this);
        Connect.setOnClickListener(this);
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
                submit();
                break;
            case R.id.statistics_search:
                if(dateTime.getText().toString().trim().isEmpty()){
                    dayTime=currentTime.getText().toString().trim().split(" ")[0];
                    dateTime.setText(dayTime);
                }else {
                    dayTime=dateTime.getText().toString().trim();
                }

                m_oWareHouseDetailListDAO.getCashierSalesReport_Search(dayTime);
                break;
            case R.id.print_sales:
                ar=m_oCashierSalesReportAdapter.getData();

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

                                list.add(("----------------------------"+ "\n").getBytes("gbk"));

                                list.add(("----------------------------"+ "\n").getBytes("gbk"));
                                if (ar.size()>0) {
                                    for(int i =0;i<ar.size();i++){
                                        Map map = (Map)ar.get(i);
                                        String m_oserialNo = (String)  map.get("serialNo");
                                        String m_obuyTime = (String) map.get("buyTime");
                                        String m_ocaption = (String) map.get("caption");
                                        String m_orealMoney = (String) map.get("realMoney");
                                        list.add(("卡号："+m_oserialNo + "\n").getBytes("gbk"));
                                        list.add(("时间："+m_obuyTime + "\n").getBytes("gbk"));
                                        list.add(("销售员："+m_ocaption + "\n").getBytes("gbk"));
                                        list.add(("金额："+m_orealMoney + "\n").getBytes("gbk"));
                                        list.add(( "\n").getBytes("gbk"));
                                    }
                                    list.add(("----------------------------"+ "\n").getBytes("gbk"));
                                    list.add(("合计： 数量 "+ar.size() + "  总金额 "+totalMoney).getBytes("gbk"));
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
            case R.id.connectBlueTooth:
                connectBLE();
                break;
            case R.id.Connect:
                sendble();
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
    protected void connectBLE() {
        // TODO Auto-generated method stub
        setbluetooth();
        //sendble();
    }

    public void sendble() {
        binder.connectBtPort(connectBlueTooth.getText().toString(), new UiExecute() {

            @Override
            public void onsucess() {
                // TODO Auto-generated method stub
                //连接成功后在UI线程中的执行
                isConnect = true;

                Toast.makeText(GCashier_Search.this, R.string.con_success, Toast.LENGTH_SHORT).show();
                connectBlueTooth.setBackgroundResource(R.color.printbutton);




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
                        isConnect = false;
                        Toast.makeText(GCashier_Search.this, R.string.con_has_discon, Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onfailed() {
                // TODO Auto-generated method stub
                //连接失败后在UI线程中的执行
                isConnect = false;
                Toast.makeText(GCashier_Search.this, R.string.con_has_discon, Toast.LENGTH_SHORT).show();
                //btn0.setText("连接失败");
            }
        });
    }

    protected void setbluetooth() {
        // TODO Auto-generated method stub
        blueadapter = BluetoothAdapter.getDefaultAdapter();
        //确认开启蓝牙
        if (!blueadapter.isEnabled()) {
            //请求用户开启
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
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
        LayoutInflater inflater = LayoutInflater.from(GCashier_Search.this);
        dialogView = inflater.inflate(R.layout.printer_list, null);
        adapter1 = new ArrayAdapter<String>(GCashier_Search.this, android.R.layout.simple_list_item_1, deviceList_bonded);
        lv1 = (ListView) dialogView.findViewById(R.id.listView1);
        btn_scan = (Button) dialogView.findViewById(R.id.btn_scan);
        ll1 = (LinearLayout) dialogView.findViewById(R.id.ll1);
        lv2 = (ListView) dialogView.findViewById(R.id.listView2);
        adapter2 = new ArrayAdapter<String>(GCashier_Search.this, android.R.layout.simple_list_item_1, deviceList_found);
        lv1.setAdapter(adapter1);
        lv2.setAdapter(adapter2);
        dialog = new AlertDialog.Builder(GCashier_Search.this).setTitle("BLE").setView(dialogView).create();
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
                    if (blueadapter != null && blueadapter.isDiscovering()) {
                        blueadapter.cancelDiscovery();

                    }

                    String msg = deviceList_bonded.get(arg2);
                    mac = msg.substring(msg.length() - 17);
                    String name = msg.substring(0, msg.length() - 18);
                    //lv1.setSelection(arg2);
                    dialog.cancel();
                    connectBlueTooth.setText(mac);

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
                    if (blueadapter != null && blueadapter.isDiscovering()) {
                        blueadapter.cancelDiscovery();

                    }
                    String msg = deviceList_found.get(arg2);
                    mac = msg.substring(msg.length() - 17);
                    String name = msg.substring(0, msg.length() - 18);
                    //lv2.setSelection(arg2);
                    dialog.cancel();
                    connectBlueTooth.setText(mac);

                    Log.i("TAG", "mac=" + mac);
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
        Set<BluetoothDevice> device = blueadapter.getBondedDevices();

        deviceList_bonded.clear();
        if (blueadapter != null && blueadapter.isDiscovering()) {
            adapter1.notifyDataSetChanged();
        }
        if (device.size() > 0) {
            //存在已经配对过的蓝牙设备
            for (Iterator<BluetoothDevice> it = device.iterator(); it.hasNext(); ) {
                BluetoothDevice btd = it.next();
                deviceList_bonded.add(btd.getName() + '\n' + btd.getAddress());
                adapter1.notifyDataSetChanged();
            }
        } else {  //不存在已经配对过的蓝牙设备
            deviceList_bonded.add("No can be matched to use bluetooth");
            adapter1.notifyDataSetChanged();
        }

    }
}
