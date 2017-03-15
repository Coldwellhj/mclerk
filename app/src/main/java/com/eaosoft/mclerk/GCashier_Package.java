package com.eaosoft.mclerk;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.eaosoft.adapter.GCardKind_CashierAdapter;
import com.eaosoft.adapter.GHttpDAO;
import com.eaosoft.userinfo.GOperaterInfo;
import com.eaosoft.util.GSvrChannel;
import com.eaosoft.util.GUtilDialog;
import com.eaosoft.util.GUtilHttp;
import com.eaosoft.util.GUtilSDCard;
import com.eaosoft.view.RoundImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;



public class GCashier_Package extends Activity implements View.OnClickListener {

    private TextView currentTime;
    private TextView store;
    private RoundImageView personal;
    private Button package_detail;
    private View v;
    private Button package_new;
    private GHttpDAO	m_oCardKindListDAO=null;
    private ListView lv_packagelist;
    private GCardKind_CashierAdapter adapter;
    private	 Map			m_oCurrentOPMap=null;
    private Button		m_oCurrentOPBtn=null;
    private LinearLayout			m_oCardKindGoodsList=null;
    private List m_oKindGoodsList=new ArrayList();
    private String 						m_strCardKindUID="";
    private String 						m_strGroupUID="";
    //==========================================
    private EditText					m_oCaption;
    private TextView					m_oGroupCaption;
    private TextView					m_oTotalMoney;
    private TextView					m_oDateEnd;
    private String						m_szUserMgr="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_gcashier__package);
        OnReadUserParameter();
        initView();
        initData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        adapter =new GCardKind_CashierAdapter(GCashier_Package.this);
        m_oCardKindListDAO = new GHttpDAO(this,adapter);
        lv_packagelist.setAdapter(adapter);
        m_oCardKindListDAO.getCardKindList("", 1,200);
    }

    private void OnReadUserParameter()
    {
        Bundle bundle = this.getIntent().getExtras();

        if(bundle == null)
            return;
        if(bundle.getString("UserMgr")!=null)
            m_szUserMgr = bundle.getString("UserMgr");
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

        lv_packagelist.setOnItemClickListener( new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(adapter == null)
                    return;

                if(adapter.ar==null)
                    return;
                Map map = (Map) adapter.ar.get(position);
                if(map == null)
                    return;
                Intent intent=null;
                if(m_szUserMgr.equalsIgnoreCase("UserMgr"))//进入明细
                    intent = new Intent(GCashier_Package.this, GCashier_Package_New.class);
                else
                    intent = new Intent(GCashier_Package.this, GActCardCreate.class);

                Bundle bundle=new Bundle();
                bundle.putString("uID", map.get("uID").toString());
                bundle.putString("serialNo", map.get("serialNo").toString());
                bundle.putString("caption", map.get("caption").toString());
                bundle.putString("briefing", map.get("briefing").toString());
                bundle.putString("totalMoney", map.get("totalMoney").toString());
                bundle.putString("dateStart", map.get("dateStart").toString());
                bundle.putString("dateEnd", map.get("dateEnd").toString());
                bundle.putString("imgLogo", map.get("imgLogo").toString());
                bundle.putString("bkColor", map.get("bkColor").toString());
                bundle.putString("enabled", map.get("enabled").toString());
                intent.putExtras(bundle);
                if(m_szUserMgr.equalsIgnoreCase("UserMgr"))//进入明细
                {
                    startActivity(intent);
                }
                else
                {
                    setResult(RESULT_OK, intent);
                    overridePendingTransition(R.anim.left, R.anim.right);
                    GCashier_Package.this.finish();
                }
            }
        });



    }
    private void initView() {
        currentTime = (TextView) findViewById(R.id.currentTime);
        store = (TextView) findViewById(R.id.store);
        personal = (RoundImageView) findViewById(R.id.personal);
        package_detail = (Button) findViewById(R.id.package_detail);
        package_new = (Button) findViewById(R.id.package_new);
        lv_packagelist = (ListView) findViewById(R.id.lv_packagelist);
        View view = View.inflate(this, R.layout.act_card_kind_cashier_item, null);
        m_oCardKindGoodsList = (LinearLayout) view.findViewById(R.id.ll_card_kind_goods_list);
        package_detail.setOnClickListener(this);
        package_new.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.package_detail:

                break;
            case R.id.package_new:
                Intent intent =new Intent(GCashier_Package.this,GCashier_Package_New.class);
                startActivity(intent);
                break;
        }
    }
    public void onDeleteCardKind(View v)
    {
        m_oCurrentOPMap = (Map)v.getTag();
        if(m_oCurrentOPMap == null)
            return;
        String strCaption = "";
        strCaption = "您确认要删除套餐\r\n【"+m_oCurrentOPMap.get("caption").toString()+"("+m_oCurrentOPMap.get("serialNo").toString()+")"+"】";

        GUtilDialog.Confirm(GCashier_Package.this,
                "套餐禁用／启用确认",
                strCaption,
                "是，我确认",new DialogInterface.OnClickListener(){public void onClick(DialogInterface dialog, int which)
                {
                    onCardKindDeleteConfirm();
                }},
                "不要",new DialogInterface.OnClickListener(){public void onClick(DialogInterface dialog,int which){}}
        );
    }
    private void onCardKindDeleteConfirm()
    {
        GSvrChannel svr= 	new GSvrChannel()
        {
            public void onNetFailure(int statusCode,String strInfo)
            {
                MainActivity.MessageBox("套餐删除","statusCode:"+statusCode+",Info:"+strInfo);
                MainActivity.onUserMessageBox("套餐删除", "套餐删除失败，请检查网络是否畅通或者联系管理员！");
            }
            public void onNetSuccess(int nCode,String strInfo,JSONObject oJsonData)
            {
                if(nCode < 0)
                {
                    MainActivity.MessageBox("套餐删除",strInfo);
                    MainActivity.onUserMessageBox("套餐删除",strInfo);
                    return;
                }

                try
                {
                    JSONObject oData = oJsonData.getJSONObject("data");
                    if(oData == null)
                    {
                        MainActivity.MessageBox("套餐删除","套餐删除失败，请检查网络是否畅通或者联系管理员！");
                        MainActivity.onUserMessageBox("套餐删除","套餐删除失败，请检查网络是否畅通或者联系管理员！");
                        return;
                    }
                    String strUID ="";

                    strUID = "套餐【"+m_oCurrentOPMap.get("caption").toString()+"("+m_oCurrentOPMap.get("serialNo").toString()+")"+"】删除成功";
                    MainActivity.onUserMessageBox("套餐删除",strUID);
                    if(adapter!=null)
                    {
                        adapter.deleteItem(m_oCurrentOPMap.get("uID").toString());
                        adapter.notifyDataSetChanged();
                        m_oCurrentOPMap = null;
                    }
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                    MainActivity.MessageBox("套餐删除",e.getMessage());
                    MainActivity.onUserMessageBox("套餐删除","套餐删除失败，请检查网络是否畅通或者联系管理员！");
                    return;
                }
            }
        };
        try
        {
            JSONObject   requestDatas = new JSONObject();
            requestDatas.put("cardKindUID",m_oCurrentOPMap.get("uID").toString());
            svr.m_oCurrentActivity = this;
            svr.onPost("api/mobile/opCardKindDelete.do", requestDatas);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
            MainActivity.MessageBox("套餐删除",e.getMessage());
            MainActivity.onUserMessageBox("套餐删除","套餐删除失败，请检查网络是否畅通或者联系管理员！");
        }
    }
    public void onEnableCardKind(View v)
    {
        m_oCurrentOPMap = (Map)v.getTag();
        if(m_oCurrentOPMap == null)
            return;
        m_oCurrentOPBtn = (Button)v;
        String strCaption = "";
        if(m_oCurrentOPMap.get("enabled").toString().equalsIgnoreCase("1"))
            strCaption = "您确认要禁用套餐\r\n【"+m_oCurrentOPMap.get("caption").toString()+"("+m_oCurrentOPMap.get("serialNo").toString()+")"+"】";
        else
            strCaption = "您确认要启用套餐\r\n【"+m_oCurrentOPMap.get("caption").toString()+"("+m_oCurrentOPMap.get("serialNo").toString()+")"+"】";

        GUtilDialog.Confirm(GCashier_Package.this,
                "套餐禁用／启用确认",
                strCaption,
                "是，我确认",new DialogInterface.OnClickListener(){public void onClick(DialogInterface dialog,int which)
                {
                    onCardKindEnableConfirm();
                }},
                "不要",new DialogInterface.OnClickListener(){public void onClick(DialogInterface dialog,int which){}}
        );
    }
    private void onCardKindEnableConfirm()
    {
        GSvrChannel svr= 	new GSvrChannel()
        {
            public void onNetFailure(int statusCode,String strInfo)
            {
                MainActivity.MessageBox("套餐禁用／启用","statusCode:"+statusCode+",Info:"+strInfo);
                MainActivity.onUserMessageBox("套餐禁用／启用", "套餐禁用／启用失败，请检查网络是否畅通或者联系管理员！");
            }
            public void onNetSuccess(int nCode,String strInfo,JSONObject oJsonData)
            {
                if(nCode < 0)
                {
                    MainActivity.MessageBox("套餐禁用／启用",strInfo);
                    MainActivity.onUserMessageBox("套餐禁用／启用",strInfo);
                    return;
                }

                try
                {
                    JSONObject oData = oJsonData.getJSONObject("data");
                    if(oData == null)
                    {
                        MainActivity.MessageBox("套餐禁用／启用","套餐禁用／启用失败，请检查网络是否畅通或者联系管理员！");
                        MainActivity.onUserMessageBox("套餐禁用／启用","套餐禁用／启用失败，请检查网络是否畅通或者联系管理员！");
                        return;
                    }
                    String strUID = GUtilHttp.getJSONObjectValue("uID",oData);
                    m_oCurrentOPMap.put("enabled", GUtilHttp.getJSONObjectValue("enabled",oData));

                    if(m_oCurrentOPMap.get("enabled").toString().equalsIgnoreCase("0"))
                    {
                        strUID = "套餐【"+m_oCurrentOPMap.get("caption").toString()+"("+m_oCurrentOPMap.get("serialNo").toString()+")"+"】禁用成功";
                        m_oCurrentOPBtn.setText("启用");
                        m_oCurrentOPBtn.setTextColor(Color.rgb(0, 0, 0));
                    }
                    else
                    {
                        strUID = "套餐【"+m_oCurrentOPMap.get("caption").toString()+"("+m_oCurrentOPMap.get("serialNo").toString()+")"+"】启用成功";
                        m_oCurrentOPBtn.setText("禁用");
                        m_oCurrentOPBtn.setTextColor(Color.rgb(255, 0, 0));
                    }
                    if(adapter!=null)
                        adapter.notifyDataSetInvalidated();
                    MainActivity.onUserMessageBox("套餐禁用／启用",strUID);
                    m_oCurrentOPMap = null;

                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                    MainActivity.MessageBox("套餐禁用／启用",e.getMessage());
                    MainActivity.onUserMessageBox("套餐禁用／启用","套餐禁用／启用失败，请检查网络是否畅通或者联系管理员！");
                    return;
                }
            }
        };
        try
        {
            JSONObject   requestDatas = new JSONObject();
            requestDatas.put("cardKindUID",m_oCurrentOPMap.get("uID").toString());
            if(m_oCurrentOPMap.get("enabled").toString().equalsIgnoreCase("1"))
                requestDatas.put("enabled","0");
            else
                requestDatas.put("enabled","1");
            svr.m_oCurrentActivity = this;
            svr.onPost("api/mobile/opCardKindEnable.do", requestDatas);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
            MainActivity.MessageBox("套餐禁用／启用",e.getMessage());
            MainActivity.onUserMessageBox("套餐禁用／启用","套餐禁用／启用失败，请检查网络是否畅通或者联系管理员！");
        }
    }
    private TextView onCreateCell(Context oContext, LinearLayout.LayoutParams pCell, Map map, int nColNum)
    {
        TextView oCell=new TextView(oContext);

        oCell.setTextSize(18);
        oCell.setLayoutParams(pCell);
        if(map == null)//这个是标题
        {
            oCell.setGravity(Gravity.CENTER_HORIZONTAL);
            oCell.setBackgroundColor(Color.rgb(0,255,0));
            switch(nColNum)
            {
                case 0:oCell.setText("名称");break;
                case 1:oCell.setText("单位");break;
                case 2:oCell.setText("数量");break;
                case 3:oCell.setText("单价");break;
                case 4:oCell.setText("操作");break;
            }
        }
        else
        {
            oCell.setTag(map);
            switch(nColNum)
            {
                case 0:oCell.setText(map.get("caption").toString());break;
                case 1:oCell.setText(map.get("unitName").toString());oCell.setGravity(Gravity.CENTER_HORIZONTAL);  break;
                case 2:oCell.setText(map.get("num").toString());oCell.setGravity(Gravity.CENTER_HORIZONTAL);  break;
                case 3:oCell.setText(map.get("price").toString());oCell.setGravity(Gravity.CENTER_HORIZONTAL);  break;
                case 4:oCell.setText("删除");oCell.setGravity(Gravity.CENTER_HORIZONTAL);  break;
            }
        }
        if(nColNum==4 && map!=null)
        {
            oCell.setText("删");
            oCell.setBackgroundResource(R.drawable.login);
            oCell.setTag(map);
            oCell.setClickable(true);
            oCell.setOnClickListener(m_oGoodsDelete);
        }
        if(nColNum==2 && map != null)
        {
            oCell.setBackgroundColor(Color.rgb(255,255,0));
            oCell.setTag(map);
            oCell.setClickable(true);
            oCell.setBackgroundResource(R.drawable.login);
            oCell.setOnClickListener(m_oGoodsNumberChange);
        }
        return oCell;
    }
    View.OnClickListener m_oGoodsDelete = new View.OnClickListener()
    {
        public void onClick(View v)
        {
            Map map=(Map)v.getTag();
            if(map == null)
                return;
            int n = getGoodsFromCardKind(map.get("uID").toString());
            if(n<0 || n>=m_oKindGoodsList.size())
                return;
            m_oKindGoodsList.remove(n);
            onReadCardKindInfo();
        }
    };
    View.OnClickListener m_oGoodsNumberChange = new View.OnClickListener()
    {
        public void onClick(View v)
        {
            final EditText inputServer = new EditText(v.getContext());
            inputServer.setTag(v);
            inputServer.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_NORMAL);
            inputServer.setFocusable(true);
            TextView txtNum = (TextView)v;
            inputServer.setText(txtNum.getText());

            AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
            builder.setTitle("请输入数量").setIcon(R.drawable.ic_launcher).setView(inputServer).setNegativeButton("取消", null);
            builder.setPositiveButton("确认",new DialogInterface.OnClickListener()
            {
                public void onClick(DialogInterface dialog, int which)
                {
                    int nNum=0;
                    String inputName = inputServer.getText().toString();
                    try{nNum = Integer.parseInt(inputName);}catch(NumberFormatException ex){nNum=0;};
                    TextView oNum = (TextView)inputServer.getTag();
                    Map map=(Map)oNum.getTag();
                    if(map == null)
                        return;
                    int n = getGoodsFromCardKind(map.get("uID").toString());
                    if(n<0 || n>=m_oKindGoodsList.size())
                        return;
                    oNum.setText(""+nNum);
                    map.put("num", ""+nNum);
                    onReadCardKindInfo();
                }
            });
            builder.show();


        }
    };
    private void onReadCardKindInfo()
    {
        float fTotalMoney=0.0f;
        float fPrice=0.0f;
        float fNum=0;
        Map map=null;
        int 	i;

        m_oCardKindGoodsList.removeAllViews();
        m_oCardKindGoodsList.addView(OnCreateCardKindGoods(this,null,-1));//标题

        for(i=0;i<m_oKindGoodsList.size();i++)
        {
            map = (Map)m_oKindGoodsList.get(i);
            m_oCardKindGoodsList.addView(OnCreateCardKindGoods(this,map,i));
            try{fPrice = Float.parseFloat(map.get("price").toString());}catch(NumberFormatException ex){fPrice=0.0f;};
            try{fNum = Float.parseFloat(map.get("num").toString());}catch(NumberFormatException ex){fNum=0.0f;};
            fTotalMoney +=(fPrice*fNum);
        }
//        m_oTotalMoney.setText(""+fTotalMoney);
    }
    private LinearLayout OnCreateCardKindGoods(Context oContext,Map map,int nLineNum)
    {
        LinearLayout oLine = new LinearLayout(oContext);  //线性布局方式
        oLine.setOrientation( LinearLayout.HORIZONTAL ); //控件对其方式为水平排列  VERTICAL
        oLine.setLayoutParams( new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        //=====================================================================================
        LinearLayout.LayoutParams pCell =new  LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT);
        pCell.weight=1.0f;

        TextView oCell = null;

        oCell = onCreateCell(oContext,pCell,map,0);oLine.addView(oCell);
        oCell = onCreateCell(oContext,pCell,map,1);oLine.addView(oCell);
        oCell = onCreateCell(oContext,pCell,map,2);oLine.addView(oCell);
        oCell = onCreateCell(oContext,pCell,map,3);oLine.addView(oCell);
        oCell = onCreateCell(oContext,pCell,map,4);oLine.addView(oCell);
        //=====================================================================================
        return oLine;
    }
    private int getGoodsFromCardKind(String strGoodsUID)
    {
        Map map;
        for(int i=0;i<m_oKindGoodsList.size();i++)
        {
            map = (Map)m_oKindGoodsList.get(i);
            if(map.get("uID").toString().equalsIgnoreCase(strGoodsUID))
                return i;
        }
        return -1;
    }

}
