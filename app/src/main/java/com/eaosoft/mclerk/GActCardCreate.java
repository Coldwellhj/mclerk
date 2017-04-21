package com.eaosoft.mclerk;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.eaosoft.userinfo.GOperaterInfo;
import com.eaosoft.util.ActivityCollector;
import com.eaosoft.util.GSvrChannel;
import com.eaosoft.view.GCouponsView;
import com.eaosoft.view.GRoundImageView;

import net.posprinter.posprinterface.ProcessData;
import net.posprinter.posprinterface.UiExecute;
import net.posprinter.utils.DataForSendToPrinterTSC;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import static com.eaosoft.mclerk.GCashierMain.isConnect;
import static com.eaosoft.mclerk.MainActivity.binder;


public class GActCardCreate extends  Activity 
{
	//===============================================
	private String					m_strCardNo="";
	private String					m_strCardKindUID="";
	//===============================================
	private GRoundImageView	m_oImgCardLogo;
	private GCouponsView			m_oCouponsView;
	private TextView					m_oCaption;
	private TextView					m_oSerialNo;
	private TextView					m_oTotalMoney;
	private TextView					m_oDateEnd;
	private String						m_strBKColor="#ff0000";
	private String						caption;
	private String						totalMoney;
	private String						s_payment_method;
	private EditText					m_oMemPhone;
	private EditText					m_oMemCaption;
    private Spinner spinner;
    private ArrayAdapter<String> adapter;
    private static final String[] payment_method={"�ֽ�","������"};
	//===============================================
	protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.act_card_create);
        spinner = (Spinner) findViewById(R.id.Spinner01);
        //����ѡ������ArrayAdapter��������
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,payment_method);

        //���������б�ķ��
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //��adapter ��ӵ�spinner��
        spinner.setAdapter(adapter);
//
//        //����¼�Spinner�¼�����
        spinner.setOnItemSelectedListener(new SpinnerSelectedListener());

        //����Ĭ��ֵ
        spinner.setVisibility(View.VISIBLE);
		ActivityCollector.addActivity(this);
		OnReadUserParameter();
		setResult(RESULT_OK, null);
        onChangeCardKind(null);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(MainActivity.m_nOperaterUI == MainActivity.UI_OP_ROLE_CASHIER){
            if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            }
        }
        }


    @Override
	protected void onDestroy() 
    {
		super.onDestroy();
		ActivityCollector.removeActivity(this);
	}
  	@Override
  	protected void onActivityResult(int requestCode, int resultCode, Intent data) 
	{
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode != 1001)
			return;
		if(requestCode == 1001  && data != null)
		{
			m_strBKColor = data.getStringExtra("bkColor");
            caption=data.getStringExtra("caption");
            totalMoney=data.getStringExtra("totalMoney");
			int nBKColor = Color.RED;
			try{nBKColor=Color.RED;}
            catch(IllegalArgumentException ex){nBKColor = Color.RED;};
			m_oCouponsView.setBackgroundColor(nBKColor);
			m_oImgCardLogo.onSetHttpImage(data.getStringExtra("imgLogo"), GOperaterInfo.m_strImagePath);
			m_oCaption.setText(data.getStringExtra("caption"));
			m_oSerialNo.setText("�������ţ�"+m_strCardNo);
			m_oTotalMoney.setText("�ܼƽ�"+data.getStringExtra("totalMoney"));
			m_oDateEnd.setText("��ֹ���ڣ�"+data.getStringExtra("dateEnd"));
			m_strCardKindUID=data.getStringExtra("uID");			
		}
	}
	public void onChangeCardKind(View v)
	{
		Intent intent = new Intent(MainActivity.m_oMainActivity, GActCardKindList.class);
		startActivityForResult(intent, 1001);		
	}
	public void onCardCreateConfirm(View v)
	{
		GSvrChannel svr= 	new GSvrChannel()
    	{
    		public void onNetFailure(int statusCode,String strInfo)
    		{
    			MainActivity.MessageBox("ȷ�Ϲ���","statusCode:"+statusCode+",Info:"+strInfo);
    			MainActivity.onUserMessageBox("ȷ�Ϲ���", "ȷ�Ϲ���ʧ�ܣ����������Ƿ�ͨ������ϵ����Ա��");
    		}
    		public void onNetSuccess(int nCode,String strInfo,JSONObject oJsonData)
    		{
    			if(nCode < 0)
    			{
    				MainActivity.MessageBox("ȷ�Ϲ���",strInfo);    				
    				MainActivity.onUserMessageBox("ȷ�Ϲ���","����ʧ�ܣ�\r\n\r\n"+strInfo);
    				return;
    			}
    			
    			try 
    			{
    				JSONArray xContent=null;
					JSONObject oData = oJsonData.getJSONObject("data");
					if(oData == null)
					{
						MainActivity.MessageBox("ȷ�Ϲ���","ȷ�Ϲ���ʧ�ܣ����������Ƿ�ͨ������ϵ����Ա��");
						MainActivity.onUserMessageBox("ȷ�Ϲ���","ȷ�Ϲ���ʧ�ܣ����������Ƿ�ͨ������ϵ����Ա��");
	    				return;
					}
					MainActivity.onUserMessageBox("ȷ�Ͽ���",strInfo);
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
                                        list.add(("  ����  ��" + m_strCardNo + "\n").getBytes("gbk"));
                                        list.add(("�ײ����ƣ�" + caption + "\n").getBytes("gbk"));
                                        list.add(("֧����ʽ��" + s_payment_method + "\n").getBytes("gbk"));
                                        list.add(("֧����" + totalMoney + "\n").getBytes("gbk"));
                                        list.add(("����ʱ�䣺" + MainActivity.getStringDate() + "\n").getBytes("gbk"));
                                        list.add((" ����Ա ��" + GOperaterInfo.m_strRealName + "\n").getBytes("gbk"));
                                        list.add(( "\n").getBytes("gbk"));
                                        list.add(( "\n").getBytes("gbk"));
                                        list.add(( "\n").getBytes("gbk"));


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

					GActCardCreate.this.finish();
				} 
    			catch (JSONException e) 
    			{
					e.printStackTrace();
					MainActivity.MessageBox("ȷ�Ϲ���",e.getMessage());
					MainActivity.onUserMessageBox("ȷ�Ϲ���","ȷ�Ϲ���ʧ�ܣ����������Ƿ�ͨ������ϵ����Ա��");
					return;
				}
    		}
    	};
    	 try 
         {
	    	JSONObject   requestDatas = new JSONObject();
	    	
	    	requestDatas.put("cardUID", m_strCardNo);
	    	requestDatas.put("kindUID", m_strCardKindUID);
	    	requestDatas.put("groupUID",GOperaterInfo.m_strGroupUID);
	    	//============================================
	    	if(m_oMemCaption != null)
	    		requestDatas.put("memCaption",m_oMemCaption.getText().toString());
	    	if(m_oMemPhone != null)
	    	requestDatas.put("memPhone",m_oMemPhone.getText().toString());
	    	//============================================
            svr.m_oCurrentActivity = this;
	    	svr.onPost("api/mobile/opCardCreate.do", requestDatas);
         }
    	 catch (JSONException e) 
         {
             e.printStackTrace();
             MainActivity.MessageBox("ȷ�Ϲ���",e.getMessage());
             MainActivity.onUserMessageBox("ȷ�Ϲ���","ȷ�Ϲ���ʧ�ܣ����������Ƿ�ͨ������ϵ����Ա��");
         } 
	}
	private void OnReadUserParameter()
	{
		Bundle bundle = this.getIntent().getExtras();
	      
		if(bundle == null)
			return;
		if(bundle.getString("cardNo")!=null)
			m_strCardNo = bundle.getString("cardNo");     
	  //=======================================
      	m_oCouponsView = (GCouponsView)findViewById(R.id.cv_card);
  		m_oImgCardLogo = (GRoundImageView)findViewById(R.id.iv_card_logo);
  		m_oCaption = (TextView)findViewById(R.id.txtCaption);
  		m_oSerialNo = (TextView)findViewById(R.id.txtSerialNo);
  		m_oTotalMoney = (TextView)findViewById(R.id.txtTotalMoney);
  		m_oDateEnd = (TextView)findViewById(R.id.txtDateEnd);
  		m_oMemPhone = (EditText)findViewById(R.id.edt_mem_phone);
  		m_oMemCaption = (EditText)findViewById(R.id.edt_mem_caption);
  		m_oSerialNo.setText("�������ţ�"+m_strCardNo);
  		m_oDateEnd.setText("��ֹ���ڣ�");
  		m_oTotalMoney.setText("�ܼƽ�0.00");
	  }
    //ʹ��������ʽ����
    class SpinnerSelectedListener implements AdapterView.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
                                   long arg3) {
           s_payment_method=payment_method[arg2];
        }

        public void onNothingSelected(AdapterView<?> arg0) {
        }
    }
}