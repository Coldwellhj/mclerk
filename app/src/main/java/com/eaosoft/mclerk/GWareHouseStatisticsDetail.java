package com.eaosoft.mclerk;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.eaosoft.adapter.GHttpDAO;
import com.eaosoft.adapter.GWareHouseStatisticsGoodsDetailAdapter;
import com.eaosoft.userinfo.GOperaterInfo;
import com.eaosoft.util.GUtilSDCard;
import com.eaosoft.view.LoadMoreListView;

import net.posprinter.posprinterface.ProcessData;
import net.posprinter.posprinterface.UiExecute;
import net.posprinter.utils.DataForSendToPrinterTSC;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import static com.eaosoft.mclerk.GWareHouseMain.isConnect;
import static com.eaosoft.mclerk.MainActivity.binder;

public class GWareHouseStatisticsDetail extends Activity implements View.OnClickListener {

    private TextView currentTime;
    private TextView store;
    private ImageView personal;
    private TextView dateTime;
    private Button statistics_detail_print;
    private Button statistics_detail_search;
    private LoadMoreListView lv_goods_statistics_detail;
    private GWareHouseStatisticsGoodsDetailAdapter m_oWareHouseStatisticsGoodsDetailAdapter=null;
    private GHttpDAO m_oWareHouseDetailListDAO = null;
    private String dayTime;
    public Integer page = 1;
    private List ar =null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_gware_house_statistics_detail);
        initView();
        initData();
        bindEvents();
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
        if (GUtilSDCard.isFileExist(GOperaterInfo.m_strLocalDiskImage)) {
            Bitmap photo = BitmapFactory.decodeFile(GOperaterInfo.m_strLocalDiskImage);
            if (photo != null && personal != null)
                personal.setImageBitmap(photo);
        }
        dayTime=currentTime.getText().toString().trim().split(" ")[0];
        dateTime.setText(dayTime);
        m_oWareHouseStatisticsGoodsDetailAdapter = new GWareHouseStatisticsGoodsDetailAdapter(GWareHouseStatisticsDetail.this);
        m_oWareHouseDetailListDAO = new GHttpDAO(GWareHouseStatisticsDetail.this, m_oWareHouseStatisticsGoodsDetailAdapter);
        lv_goods_statistics_detail.setAdapter(m_oWareHouseStatisticsGoodsDetailAdapter);
        m_oWareHouseDetailListDAO.getWareHouseGoodsStatisticsDetail_Search(1,dayTime);
    }

    private void initView() {
        currentTime = (TextView) findViewById(R.id.currentTime);
        store = (TextView) findViewById(R.id.store);
        personal = (ImageView) findViewById(R.id.personal);
        dateTime = (TextView) findViewById(R.id.dateTime);
        statistics_detail_print = (Button) findViewById(R.id.statistics_detail_print);
        statistics_detail_search = (Button) findViewById(R.id.statistics_detail_search);
        statistics_detail_print.setOnClickListener(this);
        statistics_detail_search.setOnClickListener(this);
        dateTime.setOnClickListener(this);
        lv_goods_statistics_detail = (LoadMoreListView) findViewById(R.id.lv_goods_statistics_detail);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.statistics_detail_print:
                ar=m_oWareHouseStatisticsGoodsDetailAdapter.getData();

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

                                list.add((GOperaterInfo.m_strGroupName + "\n").getBytes("gbk"));

                                list.add(("ʱ�䣺"+dayTime + "\n").getBytes("gbk"));

                                if (ar.size()>0) {
                                    for(int i =0;i<ar.size();i++){
                                        Map map = (Map)ar.get(i);
                                        String m_ogoodsCaption = (String)  map.get("goodsCaption");
                                        String m_ogoodsNumber = (String) map.get("goodsNumber");
                                        String m_ogoodsUnitName = (String) map.get("goodsUnitName");
                                        String m_oroomSerialNo = (String)  map.get("roomSerialNo");
                                        String m_ocardUID = (String) map.get("cardUID");
                                        String m_ouserCaption = (String) map.get("userCaption");
                                        list.add(("���ƣ�"+m_ogoodsCaption +"\n").getBytes("gbk"));
                                        list.add(("��λ��"+m_ogoodsNumber +"\n").getBytes("gbk"));
                                        list.add(("������"+m_ogoodsUnitName + "\n").getBytes("gbk"));
                                        list.add(("���ţ�"+m_oroomSerialNo + "\n").getBytes("gbk"));
                                        list.add(("���ţ�"+m_ocardUID  + "\n").getBytes("gbk"));
                                        list.add(("�µ��ˣ�"+m_ouserCaption + "\n").getBytes("gbk"));

                                    }





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
            case R.id.statistics_detail_search:
                if(dateTime.getText().toString().trim().isEmpty()){
                    dayTime=currentTime.getText().toString().trim().split(" ")[0];
                }else {
                    dayTime=dateTime.getText().toString().trim();
                }
                m_oWareHouseDetailListDAO.getWareHouseGoodsStatisticsDetail_Search(1,dayTime);
                break;
            case R.id.dateTime:
                AlertDialog.Builder builder = new AlertDialog.Builder(GWareHouseStatisticsDetail.this);
                View view = View.inflate(GWareHouseStatisticsDetail.this, R.layout.date_time_dialog, null);
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
        }
    }
    private void bindEvents() {
        lv_goods_statistics_detail.setOnLoadMoreListener(new LoadMoreListView.OnLoadMoreListener() {
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
        if (lv_goods_statistics_detail.getChildAt(0) != null) {
            TextView tv = (TextView) lv_goods_statistics_detail.getChildAt(0).findViewById(R.id.goodsCaption);
            Map map = (Map) tv.getTag();
            if (page < (Integer) map.get("totalPages")) {
                m_oWareHouseDetailListDAO.getWareHouseGoodsStatisticsDetail_Search(++page,dayTime);
            } else {
                Toast.makeText(GWareHouseStatisticsDetail.this, "�Ѿ�������", Toast.LENGTH_SHORT).show();
            }
        }
//       runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//
//                lv_goods_statistics_detail.setLoadCompleted();
//
//            }
//        });
//			}
//		}.start();
    }
}
