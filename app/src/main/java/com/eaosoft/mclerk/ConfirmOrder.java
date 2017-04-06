package com.eaosoft.mclerk;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.eaosoft.adapter.GWareHouseGoodsAdapter;
import com.eaosoft.userinfo.GOperaterInfo;
import com.eaosoft.util.GSvrChannel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConfirmOrder extends Activity implements View.OnClickListener {

    private TextView tv_group_name;
    private LinearLayout ll_group_name;
    private TextView tv_group_room;
    private LinearLayout ll_group_room;
    private TextView tv_group_card;
    private LinearLayout ll_group_card;
    private LinearLayout ll_title;
    private Button modify;
    private Button order;
    private LinearLayout ll_bt;
    private ListView lv_confirm_order;
    private String  m_strCardNo;
    private List m_oCardGoodsList=new ArrayList();
    private List GoodsList=new ArrayList();
    private GWareHouseGoodsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_confirm_order);
        initView();
        initData();
    }

    private void initData() {

        Bundle bundle = getIntent().getExtras();
        m_strCardNo=bundle.getString("cardUID");
        m_oCardGoodsList=bundle.getStringArrayList("goodsList");
        Map map = null;

        int j = 0,nUserNum=0;

        for( int i=0;i<m_oCardGoodsList.size();i++) {
            map = (Map) m_oCardGoodsList.get(i);
            nUserNum = Integer.parseInt(map.get("numUser").toString());
            if(nUserNum >0) {
                Map<String,String> oGoods=new HashMap<String,String>();
                oGoods.put("goodsCaption",map.get("caption").toString());
                oGoods.put("goodsNumber",map.get("numUser").toString());
                oGoods.put("goodsUnitName",map.get("unitName").toString());

                GoodsList.add(j,oGoods);
                j++;
            }
        }
        adapter=new GWareHouseGoodsAdapter(this,GoodsList);
        lv_confirm_order.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        tv_group_name.setText(GOperaterInfo.m_strGroupName);
        tv_group_room.setText(GOperaterInfo.m_strDefaultDeptSerialNo);
        tv_group_card.setText(m_strCardNo);
    }

    private void initView() {
        tv_group_name = (TextView) findViewById(R.id.tv_group_name);
        ll_group_name = (LinearLayout) findViewById(R.id.ll_group_name);
        tv_group_room = (TextView) findViewById(R.id.tv_group_room);
        ll_group_room = (LinearLayout) findViewById(R.id.ll_group_room);
        tv_group_card = (TextView) findViewById(R.id.tv_group_card);
        ll_group_card = (LinearLayout) findViewById(R.id.ll_group_card);
        ll_title = (LinearLayout) findViewById(R.id.ll_title);
        modify = (Button) findViewById(R.id.modify);
        order = (Button) findViewById(R.id.order);
        ll_bt = (LinearLayout) findViewById(R.id.ll_bt);
        lv_confirm_order = (ListView) findViewById(R.id.lv_confirm_order);

        modify.setOnClickListener(this);
        order.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.modify:
                Intent intent = new Intent(MainActivity.m_oMainActivity, GActCardCreateOrder.class);
                intent.putExtra("cardNo", m_strCardNo);
                MainActivity.m_oMainActivity.startActivity(intent);
                break;
            case R.id.order:
                GSvrChannel svr= 	new GSvrChannel()
                {
                    public void onNetFailure(int statusCode,String strInfo)
                    {
                        MainActivity.MessageBox("定单提交","statusCode:"+statusCode+",Info:"+strInfo);
                        MainActivity.onUserMessageBox("定单提交", "定单提交失败，请检查网络是否畅通或者联系管理员！");
                    }
                    public void onNetSuccess(int nCode,String strInfo,JSONObject oJsonData)
                    {
                        if(nCode < 0)
                        {
                            MainActivity.MessageBox("定单提交",strInfo);
                            MainActivity.onUserMessageBox("定单提交",strInfo);
                            return;
                        }

                        MainActivity.onUserMessageBox("定单提交",strInfo);
                        finish();

                    }
                };
                try
                {
                    JSONObject   requestDatas = new JSONObject();
                    JSONArray oGoodsList = new JSONArray();
                    requestDatas.put("cardUID", m_strCardNo);
                    requestDatas.put("groupUID", GOperaterInfo.m_strGroupUID);
                    requestDatas.put("roomUID", GOperaterInfo.m_strDefaultDeptUID);
                    Map map = null;
                    int i,nUserNum=0;
                    for(i=0;i<m_oCardGoodsList.size();i++)
                    {
                        map = (Map)m_oCardGoodsList.get(i);
                        nUserNum = Integer.parseInt(map.get("numUser").toString());
                        if(nUserNum < 1)
                            continue;
                        JSONObject   oGoods = new JSONObject();
                        //=========================================

                        oGoods.put("toGoodsUID", map.get("uID").toString());
                        oGoods.put("toGoodsNum", map.get("numUser").toString());
                        if(map.get("oID").toString().length()>0)//原
                        {
                            oGoods.put("fromGoodsUID", map.get("oID").toString());
                            oGoods.put("fromGoodsNum", map.get("numUseGoods").toString());
                        }
                        //=========================================
                        oGoodsList.put(oGoods);
                    }
                    requestDatas.put("goodsList", oGoodsList);
                    svr.m_oCurrentActivity = this;
                    svr.onPost("api/mobile/opOrderCreate.do", requestDatas);
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                    MainActivity.MessageBox("定单提交",e.getMessage());
                    MainActivity.onUserMessageBox("定单提交","定单提交失败，请检查网络是否畅通或者联系管理员！");
                }
                break;
        }
    }
}
