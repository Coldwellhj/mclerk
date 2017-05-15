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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
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

public class GCashier_Salse_Report extends Activity implements View.OnClickListener {

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
    private String dayTime1;
    private TextView dateTime_end;
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
    static boolean  isConnect;//������ʶ����״̬��һ��booleanֵ

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_gcashier_salse_report);
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
        dateTime_end.setText(dayTime);
        dateTime.setText(dayTime);
        m_oCashierSalesReportAdapter = new GCashierSalesReportAdapter(GCashier_Salse_Report.this);
        m_oWareHouseDetailListDAO = new GHttpDAO(GCashier_Salse_Report.this, m_oCashierSalesReportAdapter);
        lv_sales.setAdapter(m_oCashierSalesReportAdapter);

    }

    private void initView() {
        currentTime = (TextView) findViewById(R.id.currentTime);
        store = (TextView) findViewById(R.id.store);
        head = (LinearLayout) findViewById(R.id.head);
        personal = (RoundImageView) findViewById(R.id.personal);

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
        connectBlueTooth.setOnClickListener(this);
        Connect.setOnClickListener(this);
        statistics_search.setOnClickListener(this);
        print_sales.setOnClickListener(this);
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

                    m_oWareHouseDetailListDAO.getCashierSalesReport_Search(dayTime,dayTime1);
                }else {
                    Toast.makeText(GCashier_Salse_Report.this,"�������ڲ���Ϊ��",Toast.LENGTH_LONG);
                }

                break;

            case R.id.print_sales:
                ar=m_oCashierSalesReportAdapter.getData();

                if (isConnect) {


                    // TODO Auto-generated method stub
                    //���ӡ��������ӡָ��ʹ�ӡ���ݣ����ô˷���
                    //��һ������������UiExecute�ӿڵ�ʵ�֣��ֱ��Ƿ������ݳɹ���ʧ�ܺ���ui�̵߳Ĵ���
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
                    }, new ProcessData() {//�ڶ���������ProcessData�ӿڵ�ʵ��
                        //����ӿڵ���дprocessDataBeforeSend���������Ҫ���͵�ָ��

                        @Override
                        public List<byte[]> processDataBeforeSend() {
                            // TODO Auto-generated method stub
                            //��ʼ��һ��list
                            ArrayList<byte[]> list = new ArrayList<byte[]>();
                            //�ڴ�ӡ����������ô�ӡ���ݵ��ַ��������ͣ�Ĭ��Ϊgbk����ѡ���ӡ����ʶ������ͣ��ο�����ֲᣬ��ӡ����ҳ
                            DataForSendToPrinterTSC.setCharsetName("gbk");//�����ã�Ĭ��Ϊgbk
                            //ͨ��������õ�һ��ָ���byte[]����,���ı�Ϊ��
                            //���ȵ�����size��ǩ�ߴ�,��60mm,��30mm,Ҳ���Ե�����dot��inchΪ��λ�ķ������廻��ο�����ֲ�
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
                                        list.add(("���ţ�"+m_oserialNo + "\n").getBytes("gbk"));
                                        list.add(("ʱ�䣺"+m_obuyTime + "\n").getBytes("gbk"));
                                        list.add(("����Ա��"+m_ocaption + "\n").getBytes("gbk"));
                                        list.add(("��"+m_orealMoney + "\n").getBytes("gbk"));
                                        list.add(( "\n").getBytes("gbk"));
                                    }
                                    list.add(("----------------------------"+ "\n").getBytes("gbk"));
                                    list.add(("�ϼƣ� ���� "+ar.size() + "  �ܽ�� "+totalMoney).getBytes("gbk"));
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
                AlertDialog.Builder builder = new AlertDialog.Builder(GCashier_Salse_Report.this);
                View view = View.inflate(GCashier_Salse_Report.this, R.layout.date_time_dialog, null);
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

                builder.setTitle("��ȷ����ѯ����");
                builder.setPositiveButton("ȷ  ��", new DialogInterface.OnClickListener() {
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
                builder.setNegativeButton("ȡ  ��", new DialogInterface.OnClickListener() {
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
                AlertDialog.Builder builder1 = new AlertDialog.Builder(GCashier_Salse_Report.this);
                View view1 = View.inflate(GCashier_Salse_Report.this, R.layout.date_time_dialog, null);
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
                dateTime.setInputType(InputType.TYPE_NULL);

                dateTime.setInputType(inType1);
                //etStartTime.setSelection(etStartTime.getText().length());

                builder1.setTitle("��ȷ����ѯ����");
                builder1.setPositiveButton("ȷ  ��", new DialogInterface.OnClickListener() {
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
                        dateTime.setText(sb);
                        dialog.cancel();
                    }
                });
                builder1.setNegativeButton("ȡ  ��", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dateTime.setText("");
                        dialog.cancel();
                    }
                });


                Dialog dialog1 = builder1.create();
                dialog1.show();
                break;
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
                //���ӳɹ�����UI�߳��е�ִ��
                isConnect = true;

                Toast.makeText(GCashier_Salse_Report.this, R.string.con_success, Toast.LENGTH_SHORT).show();
                connectBlueTooth.setBackgroundResource(R.color.printbutton);




                //�˴�Ҳ���Կ�����ȡ��ӡ��������
                //����ͬ����һ��ʵ�ֵ�UiExecute�ӿڶ���
                //������Ĺ����س����쳣�������ж�����Ҳ�����쳣���Ѿ��Ͽ�
                //�����ȡ�ķ����У���һֱ��һ�����߳���ִ�ж�ȡ��ӡ�����������ݣ�
                //ֱ�����ӶϿ����쳣�Ž�������ִ��onfailed
                binder.acceptdatafromprinter(new UiExecute() {

                    @Override
                    public void onsucess() {
                        // TODO Auto-generated method stub

                    }

                    @Override
                    public void onfailed() {
                        // TODO Auto-generated method stub
                        isConnect = false;
                        Toast.makeText(GCashier_Salse_Report.this, R.string.con_has_discon, Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onfailed() {
                // TODO Auto-generated method stub
                //����ʧ�ܺ���UI�߳��е�ִ��
                isConnect = false;
                Toast.makeText(GCashier_Salse_Report.this, R.string.con_has_discon, Toast.LENGTH_SHORT).show();
                //btn0.setText("����ʧ��");
            }
        });
    }

    protected void setbluetooth() {
        // TODO Auto-generated method stub
        blueadapter = BluetoothAdapter.getDefaultAdapter();
        //ȷ�Ͽ�������
        if (!blueadapter.isEnabled()) {
            //�����û�����
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            MainActivity.m_oMainActivity.startActivityForResult(intent, Conts.ENABLE_BLUETOOTH);

        } else {
            //�����ѿ���
            showblueboothlist();
        }

    }

    private void showblueboothlist() {
        if (!blueadapter.isDiscovering()) {
            blueadapter.startDiscovery();
        }
        LayoutInflater inflater = LayoutInflater.from(GCashier_Salse_Report.this);
        dialogView = inflater.inflate(R.layout.printer_list, null);
        adapter1 = new ArrayAdapter<String>(GCashier_Salse_Report.this, android.R.layout.simple_list_item_1, deviceList_bonded);
        lv1 = (ListView) dialogView.findViewById(R.id.listView1);
        btn_scan = (Button) dialogView.findViewById(R.id.btn_scan);
        ll1 = (LinearLayout) dialogView.findViewById(R.id.ll1);
        lv2 = (ListView) dialogView.findViewById(R.id.listView2);
        adapter2 = new ArrayAdapter<String>(GCashier_Salse_Report.this, android.R.layout.simple_list_item_1, deviceList_found);
        lv1.setAdapter(adapter1);
        lv2.setAdapter(adapter2);
        dialog = new AlertDialog.Builder(GCashier_Salse_Report.this).setTitle("BLE").setView(dialogView).create();
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
        //����Ե��豸�ĵ������
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
        //δ��Ե��豸���������ԣ�������
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
        //��ȡ����������豸
        Set<BluetoothDevice> device = blueadapter.getBondedDevices();

        deviceList_bonded.clear();
        if (blueadapter != null && blueadapter.isDiscovering()) {
            adapter1.notifyDataSetChanged();
        }
        if (device.size() > 0) {
            //�����Ѿ���Թ��������豸
            for (Iterator<BluetoothDevice> it = device.iterator(); it.hasNext(); ) {
                BluetoothDevice btd = it.next();
                deviceList_bonded.add(btd.getName() + '\n' + btd.getAddress());
                adapter1.notifyDataSetChanged();
            }
        } else {  //�������Ѿ���Թ��������豸
            deviceList_bonded.add("No can be matched to use bluetooth");
            adapter1.notifyDataSetChanged();
        }

    }
}
