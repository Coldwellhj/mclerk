package com.eaosoft.mclerk;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.eaosoft.adapter.GHttpDAO;
import com.eaosoft.adapter.GWareHouseShopHistoryRoomAdapter;
import com.eaosoft.adapter.GWareHouseShopRoomAdapter;
import com.eaosoft.adapter.GWareHouseStatisticsGoodDetailAdapter;
import com.eaosoft.userinfo.GOperaterInfo;
import com.eaosoft.util.Conts;
import com.eaosoft.util.GUtilSDCard;
import com.eaosoft.util.ListViewUtil;
import com.eaosoft.util.SwitchButton;
import com.eaosoft.view.RoundImageView;

import net.posprinter.posprinterface.ProcessData;
import net.posprinter.posprinterface.UiExecute;
import net.posprinter.utils.DataForSendToPrinterTSC;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.eaosoft.mclerk.MainActivity.binder;


public class GWareHouseMainActivity extends Activity implements View.OnClickListener {

    private TextView currentTime;
    private TextView store;
    private LinearLayout head;
    private RoundImageView personal;
    private Button chooseblu;
    private Button connect;
    private Button refresh;
    private Button ViewReport;
    private Button history;
    private LinearLayout set;
    private SwitchButton switchButton;
    private ListView lv_shopRoom = null;
    private ListView lv_shopHistoryRoom = null;
    private ListView lv_goods_statistics_detail;
    private Handler handler = new Handler();
    private GWareHouseShopRoomAdapter m_oWareHouseDetailListAdapter = null;
    private GWareHouseShopHistoryRoomAdapter m_oWareHouseShopHistoryRoomAdapter = null;
    private GWareHouseStatisticsGoodDetailAdapter m_oGWareHouseStatisticsGoodDetailAdapter = null;
    private GHttpDAO m_oWareHouseDetailListDAO = null;
    private GHttpDAO m_oWareHouseDetailHistoryListDAO = null;
    public static Handler mHandler;
    private boolean Print_two_times=true;


    private Runnable runnable = new Runnable() {
        public void run() {
            Refresh();
            handler.postDelayed(this, 20000);
            //postDelayed(this,18000)��������һ��Runnable�������̶߳�����
        }
    };
    private Runnable runnable1 = new Runnable() {
        public void run() {
            currentTime.setText(MainActivity.getStringDate());
            handler.postDelayed(this, 1000);
            //postDelayed(this,18000)��������һ��Runnable�������̶߳�����
        }
    };


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
    static boolean isConnect;//������ʶ����״̬��һ��booleanֵ
    private ListView lv_history_shopRoom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_gware_house_main);
        initView();
        initData();
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 1:

                        if (lv_shopRoom.getChildAt(0) != null) {

                            TextView tv = (TextView) lv_shopRoom.getChildAt(0).findViewById(R.id.shopRoom);
                            Map map = (Map) tv.getTag();
                            List list = new ArrayList();

                            for (int i = 0; i < ((List<Map>) map.get("ar1")).size(); i++) {
                                Map map1 = new HashMap();
                                map1.put("roomSerialNo", (String) map.get("roomSerialNo"));
                                map1.put("cardUID", (String) map.get("cardUID"));
                                map1.put("orderTime", (String) map.get("orderTime"));
                                map1.put("userCaption", (String) map.get("userCaption"));
                                map1.put("goodsCaption", (String) ((List<Map>) map.get("ar1")).get(i).get("goodsCaption"));
                                map1.put("goodsNumber", (String) ((List<Map>) map.get("ar1")).get(i).get("goodsNumber"));
                                map1.put("goodsUnitName", (String) (((List<Map>) map.get("ar1")).get(i)).get("goodsUnitName"));


                                list.add(map1);
                            }
                            m_oGWareHouseStatisticsGoodDetailAdapter = new GWareHouseStatisticsGoodDetailAdapter(GWareHouseMainActivity.this, list);
                            lv_goods_statistics_detail.setAdapter(m_oGWareHouseStatisticsGoodDetailAdapter);
                            m_oGWareHouseStatisticsGoodDetailAdapter.notifyDataSetChanged();
                            //��ӡ
                            printall();
                        }else if(lv_shopHistoryRoom.getChildAt(0) != null) {
                            //�����ұߵ�listview
                            TextView tv = (TextView) lv_shopHistoryRoom.getChildAt(0).findViewById(R.id.shopHistoryRoom);
                            Map map = (Map) tv.getTag();
                            List list = new ArrayList();

                            for (int i = 0; i < ((List<Map>) map.get("ar1")).size(); i++) {
                                Map map1 = new HashMap();
                                map1.put("roomSerialNo", (String) map.get("roomSerialNo"));
                                map1.put("cardUID", (String) map.get("cardUID"));
                                map1.put("orderTime", (String) map.get("orderTime"));
                                map1.put("userCaption", (String) map.get("userCaption"));
                                map1.put("goodsCaption", (String) ((List<Map>) map.get("ar1")).get(i).get("goodsCaption"));
                                map1.put("goodsNumber", (String) ((List<Map>) map.get("ar1")).get(i).get("goodsNumber"));
                                map1.put("goodsUnitName", (String) (((List<Map>) map.get("ar1")).get(i)).get("goodsUnitName"));


                                list.add(map1);
                            }
                            m_oGWareHouseStatisticsGoodDetailAdapter = new GWareHouseStatisticsGoodDetailAdapter(GWareHouseMainActivity.this, list);
                            lv_goods_statistics_detail.setAdapter(m_oGWareHouseStatisticsGoodDetailAdapter);
                            m_oGWareHouseStatisticsGoodDetailAdapter.notifyDataSetChanged();
                        }
                        break;
                }
            }
        };

    }

    private void initData() {
        currentTime.setText(MainActivity.getStringDate());
        handler.postDelayed(runnable1, 1000); // ��ʼTimer
        handler.postDelayed(runnable, 1000); // ��ʼTimer
        store.setText(GOperaterInfo.m_strGroupName);
        personal.setImageResource(R.drawable.ic_launcher);
        if (GUtilSDCard.isFileExist(GOperaterInfo.m_strLocalDiskImage)) {
            Bitmap photo = BitmapFactory.decodeFile(GOperaterInfo.m_strLocalDiskImage);
            if (photo != null && personal != null)
                personal.setImageBitmap(photo);
        }

    }

    private void initView() {
        currentTime = (TextView) findViewById(R.id.currentTime);
        store = (TextView) findViewById(R.id.store);
        head = (LinearLayout) findViewById(R.id.head);
        personal = (RoundImageView) findViewById(R.id.personal);
        chooseblu = (Button) findViewById(R.id.chooseblu);
        connect = (Button) findViewById(R.id.connect);
        refresh = (Button) findViewById(R.id.refresh);
        ViewReport = (Button) findViewById(R.id.ViewReport);
        history = (Button) findViewById(R.id.history);
        set = (LinearLayout) findViewById(R.id.set);
        try {
            switchButton = (SwitchButton) findViewById(R.id.switchButton);
            // switchButton.setOn(false);
            switchButton.setOn(true);
            addListeners();
        } catch (Exception e) {
            e.printStackTrace();
        }
        chooseblu.setOnClickListener(this);
        personal.setOnClickListener(this);
        connect.setOnClickListener(this);
        refresh.setOnClickListener(this);
        ViewReport.setOnClickListener(this);
        history.setOnClickListener(this);
        lv_shopRoom = (ListView) findViewById(R.id.lv_shopRoom);
        lv_shopHistoryRoom = (ListView) findViewById(R.id.lv_history_shopRoom);
        lv_shopRoom.setOnItemClickListener(lv_listener);
        lv_shopHistoryRoom.setOnItemClickListener(lv_listener_history);
        lv_goods_statistics_detail = (ListView) findViewById(R.id.lv_goods_statistics_detail);
        lv_history_shopRoom = (ListView) findViewById(R.id.lv_history_shopRoom);

    }

    AdapterView.OnItemClickListener lv_listener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
            TextView tv = (TextView) lv_shopRoom.getChildAt(position).findViewById(R.id.shopRoom);
            Map map = (Map) tv.getTag();
            List list = new ArrayList();

            for (int i = 0; i < ((List<Map>) map.get("ar1")).size(); i++) {
                Map map1 = new HashMap();
                map1.put("roomSerialNo", (String) map.get("roomSerialNo"));
                map1.put("cardUID", (String) map.get("cardUID"));
                map1.put("orderTime", (String) map.get("orderTime"));
                map1.put("userCaption", (String) map.get("userCaption"));
                map1.put("goodsCaption", (String) ((List<Map>) map.get("ar1")).get(i).get("goodsCaption"));
                map1.put("goodsNumber", (String) ((List<Map>) map.get("ar1")).get(i).get("goodsNumber"));
                map1.put("goodsUnitName", (String) (((List<Map>) map.get("ar1")).get(i)).get("goodsUnitName"));


                list.add(map1);
            }
            m_oGWareHouseStatisticsGoodDetailAdapter = new GWareHouseStatisticsGoodDetailAdapter(GWareHouseMainActivity.this, list);
            lv_goods_statistics_detail.setAdapter(m_oGWareHouseStatisticsGoodDetailAdapter);
            m_oGWareHouseStatisticsGoodDetailAdapter.notifyDataSetChanged();
        }
    };
    AdapterView.OnItemClickListener lv_listener_history = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
            TextView tv = (TextView) lv_shopHistoryRoom.getChildAt(position- lv_shopHistoryRoom.getFirstVisiblePosition()).findViewById(R.id.shopHistoryRoom);
            Map map = (Map) tv.getTag();
            List list = new ArrayList();
            for (int i = 0; i < ((List<Map>) map.get("ar1")).size(); i++) {
                Map map1 = new HashMap();
                map1.put("roomSerialNo", (String) map.get("roomSerialNo"));
                map1.put("cardUID", (String) map.get("cardUID"));
                map1.put("orderTime", (String) map.get("orderTime"));
                map1.put("userCaption", (String) map.get("userCaption"));
                map1.put("goodsCaption", (String) ((List<Map>) map.get("ar1")).get(i).get("goodsCaption"));
                map1.put("goodsNumber", (String) ((List<Map>) map.get("ar1")).get(i).get("goodsNumber"));
                map1.put("goodsUnitName", (String) (((List<Map>) map.get("ar1")).get(i)).get("goodsUnitName"));
                list.add(map1);
            }
            m_oGWareHouseStatisticsGoodDetailAdapter = new GWareHouseStatisticsGoodDetailAdapter(GWareHouseMainActivity.this, list);
            lv_goods_statistics_detail.setAdapter(m_oGWareHouseStatisticsGoodDetailAdapter);
            m_oGWareHouseStatisticsGoodDetailAdapter.notifyDataSetChanged();
        }
    };
    @Override
    protected void onResume() {
        super.onResume();
        if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
   }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.personal:
                Intent intent_personal = new Intent(MainActivity.m_oMainActivity, GActUserInfo.class);
                MainActivity.m_oMainActivity.startActivity(intent_personal);
                break;
            case R.id.chooseblu:
                connectBLE();
                break;
            case R.id.connect:
                sendble();
                break;
            case R.id.refresh:
                Refresh();
                break;
            case R.id.ViewReport:
                Intent intent_viewReport = new Intent(MainActivity.m_oMainActivity, GWareHouseStatistics.class);
                MainActivity.m_oMainActivity.startActivity(intent_viewReport);
                break;
            case R.id.history:
                Intent intent = new Intent(MainActivity.m_oMainActivity, GWareHouseFillPrint.class);
                MainActivity.m_oMainActivity.startActivity(intent);
                break;

        }
    }

    private void addListeners() {
        try {
            switchButton.setOnSwitchListner(new SwitchButton.SwitchChangedListner() {
                @Override
                public void switchChanged(Integer viewId, boolean isOn) {
                    if (isOn) {
                        Print_two_times=true;
                        Toast.makeText(getApplicationContext(), "��ӡ����", Toast.LENGTH_LONG).show();
                    } else {
                        Print_two_times=false;
                        Toast.makeText(getApplicationContext(), "��ӡһ��", Toast.LENGTH_LONG).show();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void connectBLE() {
        // TODO Auto-generated method stub
        setbluetooth();
        //sendble();
    }

    public void sendble() {
        binder.connectBtPort(chooseblu.getText().toString(), new UiExecute() {

            @Override
            public void onsucess() {
                // TODO Auto-generated method stub
                //���ӳɹ�����UI�߳��е�ִ��
                isConnect = true;


                Toast.makeText(GWareHouseMainActivity.this, R.string.con_success, Toast.LENGTH_SHORT).show();
                chooseblu.setTextColor(Color.WHITE);

                printall();
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
                        Toast.makeText(GWareHouseMainActivity.this, R.string.con_has_discon, Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onfailed() {
                // TODO Auto-generated method stub
                //����ʧ�ܺ���UI�߳��е�ִ��
                isConnect = false;
                Toast.makeText(GWareHouseMainActivity.this, R.string.con_has_discon, Toast.LENGTH_SHORT).show();
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
        LayoutInflater inflater = LayoutInflater.from(GWareHouseMainActivity.this);
        dialogView = inflater.inflate(R.layout.printer_list, null);
        adapter1 = new ArrayAdapter<String>(GWareHouseMainActivity.this, android.R.layout.simple_list_item_1, deviceList_bonded);
        lv1 = (ListView) dialogView.findViewById(R.id.listView1);
        btn_scan = (Button) dialogView.findViewById(R.id.btn_scan);
        ll1 = (LinearLayout) dialogView.findViewById(R.id.ll1);
        lv2 = (ListView) dialogView.findViewById(R.id.listView2);
        adapter2 = new ArrayAdapter<String>(GWareHouseMainActivity.this, android.R.layout.simple_list_item_1, deviceList_found);
        lv1.setAdapter(adapter1);
        lv2.setAdapter(adapter2);
        dialog = new AlertDialog.Builder(GWareHouseMainActivity.this).setTitle("BLE").setView(dialogView).create();
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
                    chooseblu.setText(mac);

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
                    chooseblu.setText(mac);

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

    public void printall(){
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
                    if (lv_shopRoom.getChildAt(0) != null) {
                        String taskUID =(String) ((Map)((TextView) lv_shopRoom.getChildAt(0).findViewById(R.id.shopRoom)).getTag()).get("taskUID");
                        m_oWareHouseDetailListDAO.opPrnTaskComplete(taskUID);
                    }
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
//                    DataForSendToPrinterTSC.sizeBymm(360, 360);

                    try {



                        if (lv_shopRoom.getChildAt(0) != null) {
                            TextView tv = (TextView) lv_shopRoom.getChildAt(0).findViewById(R.id.shopRoom);
                            Map map = (Map) tv.getTag();
                            String orderNumber = (String) map.get("orderUID");
                            String m_otxtRoomNo = (String) map.get("roomSerialNo");
                            String m_ocardNumber = (String) map.get("cardUID");
                            String m_oorderTime = (String) map.get("orderTime");
                            String m_osalseman = (String) map.get("userCaption");
                            if (Print_two_times == true) {
                                for (int j = 0; j < 2; j++) {
                                    list.add(("            " + GOperaterInfo.m_strGroupName + "\n").getBytes("gbk"));
                                    list.add(("���ţ�" + orderNumber + "\n").getBytes("gbk"));
                                    list.add(("���ţ�" + m_otxtRoomNo + "\n").getBytes("gbk"));
                                    list.add(("���ţ�" + m_ocardNumber + "\n").getBytes("gbk"));

//                            DataForSendToPrinterTSC.sizeBymm(60, 60);
//                            DataForSendToPrinterTSC.bar(20, 40, 200, 3);
                                    list.add(("--------------------------------" + "\n").getBytes("gbk"));
                                    list.add(("      ����      " + "����  " + "��λ  " + "��ζ  " + "\n").getBytes("gbk"));
                                    list.add(("--------------------------------"+ "\n").getBytes("gbk"));
                                    for (int i = 0; i <((List<Map>) map.get("ar1")).size(); i++) {
                                        String m_ogoodsCaption = (String) ((List<Map>) map.get("ar1")).get(i).get("goodsCaption");
                                        String m_ogoodsNumber = (String) ((List<Map>) map.get("ar1")).get(i).get("goodsNumber");
                                        String m_ogoodsUnitName = (String) ((List<Map>) map.get("ar1")).get(i).get("goodsUnitName");
                                        String m_otasteuids = (String)  ((List<Map>)map.get("ar1")).get(i).get("tasteuids");
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
                                    list.add(("--------------------------------" + "\n").getBytes("gbk"));

                                    list.add(("�µ�ʱ�䣺" + m_oorderTime + "\n").getBytes("gbk"));
                                    list.add(("����Ա��" + m_osalseman + "\n").getBytes("gbk"));
                                    list.add(("\n").getBytes("gbk"));
                                    list.add(("\n").getBytes("gbk"));

                                }
                            } else {
                                list.add(("            " + GOperaterInfo.m_strGroupName + "\n").getBytes("gbk"));
                                list.add(("���ţ�" + orderNumber + "\n").getBytes("gbk"));
                                list.add(("���ţ�" + m_otxtRoomNo + "\n").getBytes("gbk"));
                                list.add(("���ţ�" + m_ocardNumber + "\n").getBytes("gbk"));

//                            DataForSendToPrinterTSC.sizeBymm(60, 60);
//                            DataForSendToPrinterTSC.bar(20, 40, 200, 3);
                                list.add(("--------------------------------" + "\n").getBytes("gbk"));
                                list.add(("      ����      " + "����  " + "��λ  " + "��ζ  " + "\n").getBytes("gbk"));
                                list.add(("--------------------------------"+ "\n").getBytes("gbk"));
                                for (int i = 0; i <((List<Map>) map.get("ar1")).size(); i++) {
                                    String m_ogoodsCaption = (String) ((List<Map>) map.get("ar1")).get(i).get("goodsCaption");
                                    String m_ogoodsNumber = (String) ((List<Map>) map.get("ar1")).get(i).get("goodsNumber");
                                    String m_ogoodsUnitName = (String) ((List<Map>) map.get("ar1")).get(i).get("goodsUnitName");
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
                                    }
                                }
                                list.add(("--------------------------------" + "\n").getBytes("gbk"));
                                list.add(("�µ�ʱ�䣺" + m_oorderTime + "\n").getBytes("gbk"));
                                list.add(("����Ա��" + m_osalseman + "\n").getBytes("gbk"));
                                list.add(("\n").getBytes("gbk"));
                                list.add(("\n").getBytes("gbk"));
                            }
                            list.add(("********************************" + "\n").getBytes("gbk"));

                        }
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }

//                            byte[] data0 = DataForSendToPrinterTSC
//                                    .sizeBymm(60, 30);
//                            list.add(data0);
                    //����Gap,ͬ��
//                            list.add(DataForSendToPrinterTSC.gapBymm(0,
//                                    0));
//
//                            //�������
//                            list.add(DataForSendToPrinterTSC.cls());
                    //����ָ�������int x��x�����ӡ��ʼ�㣻int y��y�����ӡ��ʼ�㣻
                    //string font���������ͣ�int rotation����ת�Ƕȣ�
                    //int x_multiplication������x����Ŵ���
                    //int y_multiplication,y����Ŵ���
                    //string content����ӡ����
//                            byte[] data1 = DataForSendToPrinterTSC
//                                    .text(10, 10, "0", 0, 1, 1,
//                                            "abc123");
//                            list.add(data1);
                    //��ӡֱ��,int x;int y;int width,�ߵĿ�ȣ�int height,�ߵĸ߶�
//                            list.add(DataForSendToPrinterTSC.bar(20,
//                                    40, 200, 3));
                    //��ӡ����
//                            list.add(DataForSendToPrinterTSC.barCode(
//                                    60, 50, "128", 100, 1, 0, 2, 2,
//                                    "abcdef12345"));
                    //��ӡ

                    DataForSendToPrinterTSC.print(1);
                    return list;
                }
            });
        } else {
            Toast.makeText(MainActivity.m_oMainActivity, R.string.not_con_printer, Toast.LENGTH_SHORT).show();
        }

    }
    public void Refresh(){
        m_oWareHouseDetailListAdapter = new GWareHouseShopRoomAdapter(GWareHouseMainActivity.this);
        m_oWareHouseDetailListDAO = new GHttpDAO(GWareHouseMainActivity.this, m_oWareHouseDetailListAdapter);
        lv_shopRoom.setAdapter(m_oWareHouseDetailListAdapter);
        m_oWareHouseDetailListDAO.getWareHouseDetail();
        m_oWareHouseShopHistoryRoomAdapter = new GWareHouseShopHistoryRoomAdapter(GWareHouseMainActivity.this);
        m_oWareHouseDetailHistoryListDAO = new GHttpDAO(GWareHouseMainActivity.this, m_oWareHouseShopHistoryRoomAdapter);
        lv_history_shopRoom.setAdapter(m_oWareHouseShopHistoryRoomAdapter);
        ListViewUtil.setListViewHeightBasedOnChildren(lv_history_shopRoom);
        m_oWareHouseDetailHistoryListDAO.getWareHouseFillPrintDetail_Search(1, "", "", "");

        if (lv_shopRoom.getChildAt(0) != null) {

            TextView tv = (TextView) lv_shopRoom.getChildAt(0).findViewById(R.id.shopRoom);
            Map map = (Map) tv.getTag();
            List list = new ArrayList();

            for (int i = 0; i < ((List<Map>) map.get("ar1")).size(); i++) {
                Map map1 = new HashMap();
                map1.put("roomSerialNo", (String) map.get("roomSerialNo"));
                map1.put("cardUID", (String) map.get("cardUID"));
                map1.put("orderTime", (String) map.get("orderTime"));
                map1.put("userCaption", (String) map.get("userCaption"));
                map1.put("goodsCaption", (String) ((List<Map>) map.get("ar1")).get(i).get("goodsCaption"));
                map1.put("goodsNumber", (String) ((List<Map>) map.get("ar1")).get(i).get("goodsNumber"));
                map1.put("goodsUnitName", (String) (((List<Map>) map.get("ar1")).get(i)).get("goodsUnitName"));


                list.add(map1);
            }
            m_oGWareHouseStatisticsGoodDetailAdapter = new GWareHouseStatisticsGoodDetailAdapter(GWareHouseMainActivity.this, list);
            lv_goods_statistics_detail.setAdapter(m_oGWareHouseStatisticsGoodDetailAdapter);
            m_oGWareHouseStatisticsGoodDetailAdapter.notifyDataSetChanged();
            //��ӡ
            printall();
        }else if(lv_shopHistoryRoom.getChildAt(0) != null) {
            //�����ұߵ�listview
            TextView tv = (TextView) lv_shopHistoryRoom.getChildAt(0).findViewById(R.id.shopHistoryRoom);
            Map map = (Map) tv.getTag();
            List list = new ArrayList();

            for (int i = 0; i < ((List<Map>) map.get("ar1")).size(); i++) {
                Map map1 = new HashMap();
                map1.put("roomSerialNo", (String) map.get("roomSerialNo"));
                map1.put("cardUID", (String) map.get("cardUID"));
                map1.put("orderTime", (String) map.get("orderTime"));
                map1.put("userCaption", (String) map.get("userCaption"));
                map1.put("goodsCaption", (String) ((List<Map>) map.get("ar1")).get(i).get("goodsCaption"));
                map1.put("goodsNumber", (String) ((List<Map>) map.get("ar1")).get(i).get("goodsNumber"));
                map1.put("goodsUnitName", (String) (((List<Map>) map.get("ar1")).get(i)).get("goodsUnitName"));


                list.add(map1);
            }
            m_oGWareHouseStatisticsGoodDetailAdapter = new GWareHouseStatisticsGoodDetailAdapter(GWareHouseMainActivity.this, list);
            lv_goods_statistics_detail.setAdapter(m_oGWareHouseStatisticsGoodDetailAdapter);
            m_oGWareHouseStatisticsGoodDetailAdapter.notifyDataSetChanged();
        }
    }
    private long exitTime = 0;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(getApplicationContext(), "�ٰ�һ���˳�����", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                MainActivity.m_oOperaterInfo.onUserBack();
                MainActivity.m_oMsgHandler.sendEmptyMessage(MainActivity.FINISH_APP);
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
