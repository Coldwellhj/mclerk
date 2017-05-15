package com.eaosoft.mclerk;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.eaosoft.adapter.GHttpDAO;
import com.eaosoft.adapter.GPackage_CashierAdapter;
import com.eaosoft.userinfo.GOperaterInfo;
import com.eaosoft.util.GSvrChannel;
import com.eaosoft.util.GUtilDialog;
import com.eaosoft.util.GUtilHttp;
import com.eaosoft.util.GUtilSDCard;
import com.eaosoft.util.NewPackageListItemClickHelp;
import com.eaosoft.view.RoundImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;



public class GCashier_Package extends Activity implements View.OnClickListener ,NewPackageListItemClickHelp {

    private TextView currentTime;
    private TextView store;
    private RoundImageView personal;
    private Button package_detail;
    private View v;
    private Button package_new;
    private GHttpDAO	m_oCardKindListDAO=null;
    private ListView lv_packagelist;
    private GPackage_CashierAdapter adapter;
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
    private Handler handler = new Handler();
    private Runnable runnable1 = new Runnable() {
        public void run() {
            currentTime.setText(MainActivity.getStringDate());
            handler.postDelayed(this, 1000);
            //postDelayed(this,18000)��������һ��Runnable�������̶߳�����
        }
    };
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
        adapter =new GPackage_CashierAdapter(GCashier_Package.this,this);
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
        handler.postDelayed(runnable1, 1000); // ��ʼTimer
        store.setText(GOperaterInfo.m_strGroupName);
        personal.setImageResource(R.drawable.ic_launcher);
        if(GUtilSDCard.isFileExist(GOperaterInfo.m_strLocalDiskImage ))
        {
            Bitmap photo = BitmapFactory.decodeFile(GOperaterInfo.m_strLocalDiskImage);
            if(photo !=null && personal!=null)
                personal.setImageBitmap(photo);
        }

//        lv_packagelist.setOnItemClickListener( new AdapterView.OnItemClickListener(){
//
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                if(adapter == null)
//                    return;
//
//                if(adapter.ar==null)
//                    return;
//                Map map = (Map) adapter.ar.get(position);
//                if(map == null)
//                    return;
//                Intent intent=null;
//                if(m_szUserMgr.equalsIgnoreCase("UserMgr"))//������ϸ
//                    intent = new Intent(GCashier_Package.this, GCashier_Package_New.class);
//                else
//                    intent = new Intent(GCashier_Package.this, GActCardCreate.class);
//
//                Bundle bundle=new Bundle();
//                bundle.putString("uID", map.get("uID").toString());
//                bundle.putString("serialNo", map.get("serialNo").toString());
//                bundle.putString("caption", map.get("caption").toString());
//                bundle.putString("briefing", map.get("briefing").toString());
//                bundle.putString("totalMoney", map.get("totalMoney").toString());
//                bundle.putString("dateStart", map.get("dateStart").toString());
//                bundle.putString("dateEnd", map.get("dateEnd").toString());
//                bundle.putString("imgLogo", map.get("imgLogo").toString());
//                bundle.putString("bkColor", map.get("bkColor").toString());
//                bundle.putString("enabled", map.get("enabled").toString());
//                intent.putExtras(bundle);
//                if(m_szUserMgr.equalsIgnoreCase("UserMgr"))//������ϸ
//                {
//                    startActivity(intent);
//                }
//                else
//                {
//                    setResult(RESULT_OK, intent);
//                    overridePendingTransition(R.anim.left, R.anim.right);
//                    GCashier_Package.this.finish();
//                }
//            }
//        });



    }
    private void initView() {
        currentTime = (TextView) findViewById(R.id.currentTime);
        store = (TextView) findViewById(R.id.store);
        personal = (RoundImageView) findViewById(R.id.personal);
        package_new = (Button) findViewById(R.id.package_new);
        lv_packagelist = (ListView) findViewById(R.id.lv_packagelist);

//        View view = View.inflate(this, R.layout.act_card_kind_cashier_item, null);
//        m_oCardKindGoodsList = (LinearLayout) view.findViewById(R.id.ll_card_kind_goods_list);
        package_new.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.package_new:
                Intent intent =new Intent(GCashier_Package.this,GCashier_Package_New.class);
                startActivity(intent);
                break;
        }
    }

    private void onCardKindDeleteConfirm()
    {
        GSvrChannel svr= 	new GSvrChannel()
        {
            public void onNetFailure(int statusCode,String strInfo)
            {
                MainActivity.MessageBox("�ײ�ɾ��","statusCode:"+statusCode+",Info:"+strInfo);
                MainActivity.onUserMessageBox("�ײ�ɾ��", "�ײ�ɾ��ʧ�ܣ����������Ƿ�ͨ������ϵ����Ա��");
            }
            public void onNetSuccess(int nCode,String strInfo,JSONObject oJsonData)
            {
                if(nCode < 0)
                {
                    MainActivity.MessageBox("�ײ�ɾ��",strInfo);
                    MainActivity.onUserMessageBox("�ײ�ɾ��",strInfo);
                    return;
                }

                try
                {
                    JSONObject oData = oJsonData.getJSONObject("data");
                    if(oData == null)
                    {
                        MainActivity.MessageBox("�ײ�ɾ��","�ײ�ɾ��ʧ�ܣ����������Ƿ�ͨ������ϵ����Ա��");
                        MainActivity.onUserMessageBox("�ײ�ɾ��","�ײ�ɾ��ʧ�ܣ����������Ƿ�ͨ������ϵ����Ա��");
                        return;
                    }
                    String strUID ="";

                    strUID = "�ײ͡�"+m_oCurrentOPMap.get("caption").toString()+"("+m_oCurrentOPMap.get("serialNo").toString()+")"+"��ɾ���ɹ�";
                    MainActivity.onUserMessageBox("�ײ�ɾ��",strUID);
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
                    MainActivity.MessageBox("�ײ�ɾ��",e.getMessage());
                    MainActivity.onUserMessageBox("�ײ�ɾ��","�ײ�ɾ��ʧ�ܣ����������Ƿ�ͨ������ϵ����Ա��");
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
            MainActivity.MessageBox("�ײ�ɾ��",e.getMessage());
            MainActivity.onUserMessageBox("�ײ�ɾ��","�ײ�ɾ��ʧ�ܣ����������Ƿ�ͨ������ϵ����Ա��");
        }
    }

    private void onCardKindEnableConfirm()
    {
        GSvrChannel svr= 	new GSvrChannel()
        {
            public void onNetFailure(int statusCode,String strInfo)
            {
                MainActivity.MessageBox("�ײͽ��ã�����","statusCode:"+statusCode+",Info:"+strInfo);
                MainActivity.onUserMessageBox("�ײͽ��ã�����", "�ײͽ��ã�����ʧ�ܣ����������Ƿ�ͨ������ϵ����Ա��");
            }
            public void onNetSuccess(int nCode,String strInfo,JSONObject oJsonData)
            {
                if(nCode < 0)
                {
                    MainActivity.MessageBox("�ײͽ��ã�����",strInfo);
                    MainActivity.onUserMessageBox("�ײͽ��ã�����",strInfo);
                    return;
                }

                try
                {
                    JSONObject oData = oJsonData.getJSONObject("data");
                    if(oData == null)
                    {
                        MainActivity.MessageBox("�ײͽ��ã�����","�ײͽ��ã�����ʧ�ܣ����������Ƿ�ͨ������ϵ����Ա��");
                        MainActivity.onUserMessageBox("�ײͽ��ã�����","�ײͽ��ã�����ʧ�ܣ����������Ƿ�ͨ������ϵ����Ա��");
                        return;
                    }
                    String strUID = GUtilHttp.getJSONObjectValue("uID",oData);
                    m_oCurrentOPMap.put("enabled", GUtilHttp.getJSONObjectValue("enabled",oData));

                    if(m_oCurrentOPMap.get("enabled").toString().equalsIgnoreCase("0"))
                    {
                        strUID = "�ײ͡�"+m_oCurrentOPMap.get("caption").toString()+"("+m_oCurrentOPMap.get("serialNo").toString()+")"+"�����óɹ�";
                        m_oCurrentOPBtn.setText("����");
                        m_oCurrentOPBtn.setTextColor(Color.rgb(0, 0, 0));
                    }
                    else
                    {
                        strUID = "�ײ͡�"+m_oCurrentOPMap.get("caption").toString()+"("+m_oCurrentOPMap.get("serialNo").toString()+")"+"�����óɹ�";
                        m_oCurrentOPBtn.setText("����");
                        m_oCurrentOPBtn.setTextColor(Color.rgb(255, 0, 0));
                    }
                    if(adapter!=null)
                        adapter.notifyDataSetInvalidated();
                    MainActivity.onUserMessageBox("�ײͽ��ã�����",strUID);
                    m_oCurrentOPMap = null;

                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                    MainActivity.MessageBox("�ײͽ��ã�����",e.getMessage());
                    MainActivity.onUserMessageBox("�ײͽ��ã�����","�ײͽ��ã�����ʧ�ܣ����������Ƿ�ͨ������ϵ����Ա��");
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
            MainActivity.MessageBox("�ײͽ��ã�����",e.getMessage());
            MainActivity.onUserMessageBox("�ײͽ��ã�����","�ײͽ��ã�����ʧ�ܣ����������Ƿ�ͨ������ϵ����Ա��");
        }
    }


    @Override
    public void onEnableCardKind(View v) {

        m_oCurrentOPMap = (Map)v.getTag();
        if(m_oCurrentOPMap == null)
            return;
        m_oCurrentOPBtn = (Button)v;
        String strCaption = "";
        if(m_oCurrentOPMap.get("enabled").toString().equalsIgnoreCase("1"))
            strCaption = "��ȷ��Ҫ�����ײ�\r\n��"+m_oCurrentOPMap.get("caption").toString()+"("+m_oCurrentOPMap.get("serialNo").toString()+")"+"��";
        else
            strCaption = "��ȷ��Ҫ�����ײ�\r\n��"+m_oCurrentOPMap.get("caption").toString()+"("+m_oCurrentOPMap.get("serialNo").toString()+")"+"��";

        GUtilDialog.Confirm(GCashier_Package.this,
                "�ײͽ��ã�����ȷ��",
                strCaption,
                "�ǣ���ȷ��",new DialogInterface.OnClickListener(){public void onClick(DialogInterface dialog,int which)
                {
                    onCardKindEnableConfirm();
                }},
                "��Ҫ",new DialogInterface.OnClickListener(){public void onClick(DialogInterface dialog,int which){}}
        );
    }

    @Override
    public void onDeleteCardKind(View v) {
        m_oCurrentOPMap = (Map)v.getTag();
        if(m_oCurrentOPMap == null)
            return;
        String strCaption = "";
        strCaption = "��ȷ��Ҫɾ���ײ�\r\n��"+m_oCurrentOPMap.get("caption").toString()+"("+m_oCurrentOPMap.get("serialNo").toString()+")"+"��";

        GUtilDialog.Confirm(GCashier_Package.this,
                "�ײͽ��ã�����ȷ��",
                strCaption,
                "�ǣ���ȷ��",new DialogInterface.OnClickListener(){public void onClick(DialogInterface dialog, int which)
                {
                    onCardKindDeleteConfirm();
                }},
                "��Ҫ",new DialogInterface.OnClickListener(){public void onClick(DialogInterface dialog,int which){}}
        );
    }

    @Override
    public void onModifyCardKind(int position) {
        Intent intent =new Intent(GCashier_Package.this,GCashier_Package_New.class);
        Map map = (Map) adapter.ar.get(position);
        if(map == null)
            return;
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
        startActivity(intent);
    }
}
