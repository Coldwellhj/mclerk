package com.eaosoft.mclerk;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.eaosoft.adapter.GCardKind_GoodsAdapter;
import com.eaosoft.adapter.GCardKind_Goods_detailAdapter;
import com.eaosoft.userinfo.GOperaterInfo;
import com.eaosoft.util.GSvrChannel;
import com.eaosoft.util.GUtilHttp;
import com.eaosoft.util.GUtilSDCard;
import com.eaosoft.util.ListItemClickHelp;
import com.eaosoft.view.RoundImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GCashier_Package_New extends Activity implements ListItemClickHelp{
    private String 						m_strCardKindUID="";
    private String 						m_strGroupUID="";
    private List m_oShopGoodsList = new ArrayList();
    private List m_oShopGoodsDetailList = new ArrayList();
    private GCardKind_GoodsAdapter goodsAdapter;
    private GCardKind_Goods_detailAdapter goodsDetailAdapter;
    private TextView currentTime;
    private TextView store;
    private RoundImageView personal;
    private ListView lv_card_kind_goods;
    private EditText tv_package_name;
    private TextView tv_package_price;
    private TextView tv_package_editor;
    private TextView tv_package_operate;
    private ListView lv_card_kind_goods_detail;
    private TextView tv_package_all_price;
    private TextView tv_package_time;
    private float fTotalMoney = 0.0f;
    private Button btn_save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gcashier__package__new);
        OnReadUserParameter();
        initView();
        initData();
        OnClick();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        store.setText(GOperaterInfo.m_strGroupName);
    }

    private void OnReadUserParameter()
    {
        Bundle bundle = this.getIntent().getExtras();

        if(bundle == null)
            return;
        if(bundle.getString("uID")!=null)
            m_strCardKindUID = bundle.getString("uID");
    }
    private void OnClick() {
        lv_card_kind_goods.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(!m_oShopGoodsDetailList.contains(m_oShopGoodsList.get(position))){
                    m_oShopGoodsDetailList.add(m_oShopGoodsList.get(position));
                    goodsDetailAdapter.notifyDataSetChanged();
                }

            }
        });
        tv_package_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(GCashier_Package_New.this, new DatePickerDialog.OnDateSetListener()
                {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
                    {
                        tv_package_time.setText(String.format("%d-%d-%d",year,monthOfYear+1,dayOfMonth));
                    }
                },2030,11,31).show();
            }
        });
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(m_oShopGoodsDetailList.size()<1)
                    return;
                if(tv_package_name.getText().toString().length()<1)
                {
                    MainActivity.onUserMessageBox("�ײͱ���", "�������ײ����ƣ�");
                    return;
                }
                GSvrChannel svr= 	new GSvrChannel()
                {
                    public void onNetFailure(int statusCode,String strInfo)
                    {
                        MainActivity.MessageBox("�ײͱ���","statusCode:"+statusCode+",Info:"+strInfo);
                        MainActivity.onUserMessageBox("�ײͱ���", "�ײͱ���ʧ�ܣ����������Ƿ�ͨ������ϵ����Ա��");
                    }
                    public void onNetSuccess(int nCode,String strInfo,JSONObject oJsonData)
                    {
                        if(nCode < 0)
                        {
                            MainActivity.MessageBox("�ײͱ���",strInfo);
                            MainActivity.onUserMessageBox("�ײͱ���","�ײͱ���ʧ�ܣ�\r\n\r\n"+strInfo);
                            return;
                        }
                        MainActivity.onUserMessageBox("�ײͱ���","�ײͱ���ɹ���\r\n\r\n"+strInfo);
                        finish();
                    }
                };
                try	//============================================
                {
                    JSONObject   requestDatas = new JSONObject();
                    JSONArray	 oGoods = new JSONArray();
                    Map map;
                    requestDatas.put("uID", m_strCardKindUID);
                    requestDatas.put("caption", tv_package_name.getText().toString());
                    requestDatas.put("dateEnd", tv_package_time.getText().toString());
                    requestDatas.put("totalMoney", tv_package_all_price.getText().toString());
                    requestDatas.put("groupUID", m_strGroupUID);
                    requestDatas.put("goodsCount", ""+m_oShopGoodsDetailList.size());
                    for(int i=0;i<m_oShopGoodsDetailList.size();i++)
                    {
                        map = (Map)m_oShopGoodsDetailList.get(i);
                        JSONObject o = new JSONObject();
                        o.put("uID", map.get("uID").toString());
                        o.put("num", map.get("num").toString());

                        oGoods.put(i,o);
                    }
                    requestDatas.put("goodsList", oGoods);


                    svr.m_oCurrentActivity = GCashier_Package_New.this;
                    svr.onPost("api/mobile/opCardKindSet.do", requestDatas);
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                    MainActivity.MessageBox("�ײͱ���",e.getMessage());
                    MainActivity.onUserMessageBox("�ײͱ���","��ȡ�������б�ʧ�ܣ����������Ƿ�ͨ������ϵ����Ա��");
                }
            }
        });

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
        goodsDetailAdapter = new GCardKind_Goods_detailAdapter(GCashier_Package_New.this, m_oShopGoodsDetailList, this);
        lv_card_kind_goods_detail.setAdapter(goodsDetailAdapter);
        goodsAdapter = new GCardKind_GoodsAdapter(GCashier_Package_New.this, m_oShopGoodsList);
        lv_card_kind_goods.setAdapter(goodsAdapter);
        onReadShopGoodsListFromSvr();
        onGetKindInfoFromSvr(m_strCardKindUID);
        onReadTotalMoney();
    }

    private void onReadShopGoodsListFromSvr() {
        GSvrChannel svr = new GSvrChannel() {
            public void onNetFailure(int statusCode, String strInfo) {
                MainActivity.MessageBox("��Ʒ�б�", "statusCode:" + statusCode + ",Info:" + strInfo);
                MainActivity.onUserMessageBox("��Ʒ�б�", "ȷ�Ϲ���ʧ�ܣ����������Ƿ�ͨ������ϵ����Ա��");
            }

            public void onNetSuccess(int nCode, String strInfo, JSONObject oJsonData) {
                if (nCode < 0) {
                    MainActivity.MessageBox("��Ʒ�б�", strInfo);
                    MainActivity.onUserMessageBox("��Ʒ�б�", "����ʧ�ܣ�\r\n\r\n" + strInfo);
                    return;
                }
                if (nCode == 0) {
                    MainActivity.onUserMessageBox("��Ʒ�б�", "û���ҵ���Ʒ���Թ�ѡ��");
                    return;
                }
                try {
                    JSONArray xContent = null;
                    JSONObject oData = oJsonData.getJSONObject("data");
                    if (oData == null) {
                        MainActivity.MessageBox("��Ʒ�б�", "ȷ�Ϲ���ʧ�ܣ����������Ƿ�ͨ������ϵ����Ա��");
                        MainActivity.onUserMessageBox("��Ʒ�б�", "ȷ�Ϲ���ʧ�ܣ����������Ƿ�ͨ������ϵ����Ա��");
                        return;
                    }
                    try {
                        xContent = oData.getJSONArray("content");
                    } catch (JSONException e) {
                        xContent = null;
                    }
                    if (xContent == null) {
                        MainActivity.onUserMessageBox("��ȡ��Ʒ�б�", "��ȡ�������б�ʧ�ܣ����������Ƿ�ͨ������ϵ����Ա��");
                        return;
                    }
                    if (xContent.length() < 1) {
                        MainActivity.onUserMessageBox("��ȡ��Ʒ�б�", "û���ҵ���Ʒ���Թ�ѡ��");
                        return;
                    }
                    for (int i = 0; i < xContent.length(); i++) {
                        JSONObject o = xContent.getJSONObject(i);
                        Map map = new HashMap();
                        map.put("uID", GUtilHttp.getJSONObjectValue("uID", o));
                        if (map.get("uID").toString().length() > 0) {
                            map.put("caption", GUtilHttp.getJSONObjectValue("caption", o));
                            map.put("serialNo", GUtilHttp.getJSONObjectValue("serialNo", o));
                            map.put("unitName", GUtilHttp.getJSONObjectValue("unitName", o));
                            map.put("norImage", GUtilHttp.getJSONObjectValue("norImage", o));
                            map.put("minImage", GUtilHttp.getJSONObjectValue("minImage", o));
                            map.put("price", GUtilHttp.getJSONObjectValue("price", o));
                            map.put("num", "0");
                            map.put("userChoose", "0");

                            m_oShopGoodsList.add(map);
                        }
                    }
                    goodsAdapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                    MainActivity.MessageBox("��Ʒ�б�", e.getMessage());
                    MainActivity.onUserMessageBox("��Ʒ�б�", "ȷ�Ϲ���ʧ�ܣ����������Ƿ�ͨ������ϵ����Ա��");
                    return;
                }
            }
        };
        try    //============================================
        {
            JSONObject requestDatas = new JSONObject();
            requestDatas.put("userID", GOperaterInfo.m_strUID);
            svr.m_oCurrentActivity = this;
            svr.onPost("api/mobile/opShopGoodsList.do", requestDatas);
        } catch (JSONException e) {
            e.printStackTrace();
            MainActivity.MessageBox("��ȡ��Ʒ�б�", e.getMessage());
            MainActivity.onUserMessageBox("��Ʒ�б�", "��ȡ�������б�ʧ�ܣ����������Ƿ�ͨ������ϵ����Ա��");
        }
    }
    private void onGetKindInfoFromSvr(String strUID)
    {
        if(strUID.length()<1)
            return;
        GSvrChannel svr= 	new GSvrChannel()
        {
            public void onNetFailure(int statusCode,String strInfo)
            {
                MainActivity.MessageBox("��ȡ�ײ�����","statusCode:"+statusCode+",Info:"+strInfo);
                MainActivity.onUserMessageBox("��ȡ�ײ�����", "��ȡ�ײ�����ʧ�ܣ����������Ƿ�ͨ������ϵ����Ա��");
            }
            public void onNetSuccess(int nCode,String strInfo,JSONObject oJsonData)
            {
                if(nCode < 0)
                {
                    MainActivity.MessageBox("��ȡ�ײ�����",strInfo);
                    MainActivity.onUserMessageBox("��ȡ�ײ�����",strInfo);
                    return;
                }

                try
                {
                    JSONArray xContent=null;
                    JSONObject oData = oJsonData.getJSONObject("data");
                    if(oData == null)
                    {
                        MainActivity.MessageBox("��ȡ�ײ�����","��ȡ�ײ�����ʧ�ܣ����������Ƿ�ͨ������ϵ����Ա��");
                        MainActivity.onUserMessageBox("��ȡ�ײ�����","��ȡ�ײ�����ʧ�ܣ����������Ƿ�ͨ������ϵ����Ա��");
                        return;
                    }
                    try{xContent = oData.getJSONArray("goodsList");}catch(JSONException e){xContent=null;}
                    if(xContent==null)
                        return;
                    for(int i=0;i<xContent.length();i++)
                    {
                        JSONObject o = xContent.getJSONObject(i);
                        Map map = new HashMap();
                        map.put("uID", GUtilHttp.getJSONObjectValue("uID",o));
                        if(map.get("uID").toString().length()>0)
                        {
                            map.put("caption",GUtilHttp.getJSONObjectValue("caption",o) );
                            map.put("serialNo", GUtilHttp.getJSONObjectValue("serialNo",o) );
                            map.put("unitName", GUtilHttp.getJSONObjectValue("unitName",o) );
                            map.put("norImage", GUtilHttp.getJSONObjectValue("norImage",o) );
                            map.put("minImage", GUtilHttp.getJSONObjectValue("minImage",o) );
                            map.put("price", GUtilHttp.getJSONObjectValue("price",o));
                            map.put("num",GUtilHttp.getJSONObjectValue("goodsNum",o) );
                            m_oShopGoodsDetailList.add(map);
                        }
                    }
                    goodsDetailAdapter.notifyDataSetChanged();
                    //####################################################
                    GUtilHttp.getJSONObjectValue("caption",oData);
                    m_strCardKindUID=GUtilHttp.getJSONObjectValue("uID",oData);
                    m_strGroupUID=GUtilHttp.getJSONObjectValue("groupUID",oData);
                    //==========================================
                    tv_package_name.setText(GUtilHttp.getJSONObjectValue("caption",oData));
                    tv_package_all_price.setText(GUtilHttp.getJSONObjectValue("totalMoney",oData));
                    tv_package_time.setText(GUtilHttp.getJSONObjectValue("dateEnd",oData));
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                    MainActivity.MessageBox("��ȡ�ײ�����",e.getMessage());
                    MainActivity.onUserMessageBox("��ȡ�ײ�����","��ȡ�ײ�����ʧ�ܣ����������Ƿ�ͨ������ϵ����Ա��");
                    return;
                }
            }
        };
        try
        {
            JSONObject   requestDatas = new JSONObject();
            requestDatas.put("cardKindUID", strUID);
            svr.m_oCurrentActivity = this;
            svr.onPost("api/mobile/opHRGroupCardKindInfo.do", requestDatas);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
            MainActivity.MessageBox("��ȡ�ײ�����",e.getMessage());
            MainActivity.onUserMessageBox("��ȡ�ײ�����","��ȡ�ײ�����ʧ�ܣ����������Ƿ�ͨ������ϵ����Ա��");
        }
    }
    private void initView() {
        currentTime = (TextView) findViewById(R.id.currentTime);
        store = (TextView) findViewById(R.id.store);
        personal = (RoundImageView) findViewById(R.id.personal);
        lv_card_kind_goods = (ListView) findViewById(R.id.lv_card_kind_goods);
        tv_package_name = (EditText) findViewById(R.id.tv_package_name);
        tv_package_operate = (TextView) findViewById(R.id.tv_package_operate);
        lv_card_kind_goods_detail = (ListView) findViewById(R.id.lv_card_kind_goods_detail);
        tv_package_all_price = (TextView) findViewById(R.id.tv_package_all_price);
        tv_package_time = (TextView) findViewById(R.id.tv_package_time);

        btn_save = (Button) findViewById(R.id.btn_save);

    }

    @Override
    public void onClick(int position) {
        m_oShopGoodsDetailList.remove(position);
        onReadTotalMoney();
        goodsDetailAdapter.notifyDataSetChanged();
    }

    @Override
    public void onReadTotalMoney() {


        float fTotalMoney = 0.0f;
        float fPrice = 0.0f;
        float fNum = 0;
        Map map = null;
        int i;


        for (i = 0; i < m_oShopGoodsDetailList.size(); i++) {
            map = (Map) m_oShopGoodsDetailList.get(i);

            try {
                fPrice = Float.parseFloat(map.get("price").toString());
            } catch (NumberFormatException ex) {
                fPrice = 0.0f;
            }
            try {
                fNum = Float.parseFloat(map.get("num").toString());
            } catch (NumberFormatException ex) {
                fNum = 0.0f;
            }
            fTotalMoney += (fPrice * fNum);
        }
        tv_package_all_price.setText("" + fTotalMoney);

    }
}
