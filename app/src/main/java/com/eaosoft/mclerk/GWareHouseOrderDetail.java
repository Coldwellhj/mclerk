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
            confirm.setText("ȷ�ϲ���");
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
                if("ȷ�ϴ�ӡ".equals(confirm.getText().toString())){
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



                                    if (orderUID != null) {


                                        list.add(("            "+GOperaterInfo.m_strGroupName + "\n").getBytes("gbk"));
                                        list.add(("���ţ�" + orderUID + "\n").getBytes("gbk"));
                                        list.add(("���ţ�" + roomSerialNo + "\n").getBytes("gbk"));
                                        list.add(("���ţ�" + cardUID + "\n").getBytes("gbk"));
                                        list.add(("----------------------------"+ "\n").getBytes("gbk"));
                                        list.add(("      ����      " + "����  " + "��λ  " + "\n").getBytes("gbk"));
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
                                        list.add(("�µ�ʱ�䣺" + ordertime + "\n").getBytes("gbk"));
                                        list.add(("����Ա��" + userCaption + "\n").getBytes("gbk"));
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
                }else if("ȷ�ϲ���".equals(confirm.getText().toString())){
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



                                    if (orderUID != null) {


                                        list.add((GOperaterInfo.m_strGroupName + "\n").getBytes("gbk"));
                                        list.add(("���ţ�" + orderUID + "\n").getBytes("gbk"));
                                        list.add(("���ţ�" + roomSerialNo + "\n").getBytes("gbk"));
                                        list.add(("���ţ�" + cardUID + "\n").getBytes("gbk"));
                                        list.add(("����      " + "����  " + "��λ  " + "\n").getBytes("gbk"));
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


                                        list.add(("�µ�ʱ�䣺" + ordertime + "\n").getBytes("gbk"));
                                        list.add(("����Ա��" + userCaption + "\n").getBytes("gbk"));
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
