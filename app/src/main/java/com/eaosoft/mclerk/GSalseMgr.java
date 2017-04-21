package com.eaosoft.mclerk;



import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.TextView;

import com.eaosoft.userinfo.GOperaterInfo;
import com.eaosoft.util.GBaseViewHolder;
import com.eaosoft.util.GSvrChannel;
import com.eaosoft.util.GUINavigationAdapter;
import com.eaosoft.util.GUINavigationView;
import com.xys.libzxing.zxing.activity.CaptureActivity;

public class GSalseMgr
{
    private ScrollView					m_oUserView=null;
    private Context						m_oContext=null;
    //============================================================
    //常用功能
    private GUINavigationAdapter	m_oNormalNavAdapter=null;
    private GUINavigationView		m_oNormalNavView=null;
    public String[] 							m_oNormalNavText = { "立即下单",   "查询酒品", "消费记录"};
    public String[] 							m_oNormalNavGUID = { "order_create","card_remain","consume_history"};
    public int[] 								m_oNormalNavImgs = { R.drawable.mgr_order_create,R.drawable.mgr_card_search, R.drawable.mgr_consume_history};
    //============================================================
    private GUINavigationAdapter	m_oStateNavAdapter=null;
    private GUINavigationView		m_oStateNavView=null;
    public String[] 							m_oStateNavText = {  "下单记录"};
    public String[] 							m_oStateNavGUID = { "card_create","order_history"};
    public int[] 								m_oStateNavImgs = { R.drawable.mgr_card_create_state,R.drawable.mgr_order_consume_state};
    //============================================================

    public GSalseMgr(Context oContext)
    {
        m_oContext = oContext;
    }
    public View OnCreateView()
    {
        m_oUserView = new ScrollView(m_oContext);
        m_oUserView.setLayoutParams( new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT) );
        m_oUserView.setFillViewport(true);
        m_oUserView.setBackgroundColor(Color.WHITE);
        //=====================================================================================
        //主背景
        LinearLayout oMainWin = new LinearLayout(m_oContext);  //线性布局方式
        oMainWin.setOrientation( LinearLayout.VERTICAL ); //控件对其方式为垂直排列  VERTICAL
        oMainWin.setLayoutParams( new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT) );
        m_oUserView.addView(oMainWin);
        oMainWin.setBackgroundColor(Color.rgb(0xcc,0xcc,0xcc));
        //============================================================================
        onCreateNormalNavigation(oMainWin);
        onCreateStateNavigation(oMainWin);
        //=============================================================================================
        return m_oUserView;
    }
    private void onCreateNormalNavigation(LinearLayout lay)
    {
        TextView oTxtView = new TextView(m_oContext);
        oTxtView.setLayoutParams( new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT) );
        oTxtView.setTextSize(18);
        oTxtView.setMaxLines(1);
        oTxtView.setTextColor(Color.rgb(0x33,0x33,0x33));
        oTxtView.setText("常用功能");
        lay.addView(oTxtView);

        LinearLayout.LayoutParams pNormalNavView= new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
        m_oNormalNavView=(GUINavigationView) new GUINavigationView(m_oContext);
        m_oNormalNavView.setLayoutParams(pNormalNavView);
        m_oNormalNavView.setNumColumns(3);
        m_oNormalNavView.setVerticalSpacing(2);
        m_oNormalNavView.setHorizontalSpacing(2);
        m_oNormalNavView.setGravity(Gravity.CENTER);
        m_oNormalNavView.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
        m_oNormalNavAdapter = new GUINavigationAdapter(m_oContext,m_oNormalNavGUID,m_oNormalNavText,m_oNormalNavImgs);
        m_oNormalNavView.setAdapter(m_oNormalNavAdapter);
        m_oNormalNavView.setOnItemClickListener(new GUINavigationItemClickListener());
        m_oNormalNavView.setBackgroundColor(Color.rgb(0xcc,0xcc,0xcc));
        //m_oNormalNavView.setBackgroundColor(Color.WHITE);


        lay.addView(m_oNormalNavView);
    }
    private void onCreateStateNavigation(LinearLayout lay)
    {
        //============================================================================
        TextView oTxtView = new TextView(m_oContext);
        oTxtView.setLayoutParams( new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT) );
        oTxtView.setTextSize(18);
        oTxtView.setMaxLines(1);
        oTxtView.setTextColor(Color.rgb(0x33,0x33,0x33));
        oTxtView.setText("统计");
        lay.addView(oTxtView);

        LinearLayout.LayoutParams pNormalNavView= new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
        m_oStateNavView=(GUINavigationView) new GUINavigationView(m_oContext);
        m_oStateNavView.setLayoutParams(pNormalNavView);
        m_oStateNavView.setNumColumns(3);
        m_oStateNavView.setVerticalSpacing(2);
        m_oStateNavView.setHorizontalSpacing(2);
        m_oStateNavView.setGravity(Gravity.CENTER);
        m_oStateNavView.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
        m_oStateNavAdapter = new GUINavigationAdapter(m_oContext,m_oStateNavGUID,m_oStateNavText,m_oStateNavImgs);
        m_oStateNavView.setAdapter(m_oStateNavAdapter);
        m_oStateNavView.setOnItemClickListener(new GUINavigationItemClickListener());
        m_oStateNavView.setBackgroundColor(Color.rgb(0xcc,0xcc,0xcc));
        //m_oNormalNavView.setBackgroundColor(Color.WHITE);
        lay.addView(m_oStateNavView);
    }
    private final class GUINavigationItemClickListener implements OnItemClickListener
    {
        public void onItemClick(AdapterView<?> parent, View view, int position, long id)
        {

            TextView tv = GBaseViewHolder.get(view, GUINavigationAdapter.TEXT_VIEW_ID);
            if(tv == null)
                return;
            String strOID = tv.getTag().toString();

            if(strOID.equalsIgnoreCase("order_create"))//扫码下单
            {
                Intent intent = new Intent(MainActivity.m_oMainActivity, CaptureActivity.class);
                MainActivity.m_oMainActivity.startActivityForResult(intent, MainActivity.SCAN_CODE_ORDER_CREATE);
            }
            else if(strOID.equalsIgnoreCase("card_remain"))//卡余额
            {
                Intent intent = new Intent(MainActivity.m_oMainActivity, CaptureActivity.class);
                MainActivity.m_oMainActivity.startActivityForResult(intent, MainActivity.SCAN_CODE_CAED_INFO);
            }
            else if(strOID.equalsIgnoreCase("consume_history"))//卡历史消费记录
            {
                Intent intent = new Intent(MainActivity.m_oMainActivity, CaptureActivity.class);
                MainActivity.m_oMainActivity.startActivityForResult(intent, MainActivity.SCAN_CODE_CAED_CONSUME);
            }
//            else if(strOID.equalsIgnoreCase("card_create"))//新建卡
//            {
//                Intent intent = new Intent(MainActivity.m_oMainActivity, CaptureActivity.class);
//                MainActivity.m_oMainActivity.startActivityForResult(intent, MainActivity.SCAN_CODE_CAED_CREATE);
//            }
            else if(strOID.equalsIgnoreCase("order_history"))//操作员下单记录
            {
                String strURL=GSvrChannel.m_strURLOrderHistory+"?token="+GOperaterInfo.m_strToken+"&callerName="+GSvrChannel.CALLER_NAME;
                String strTitle=GOperaterInfo.m_strRealName+"的下单记录";
                Intent intent = new Intent(MainActivity.m_oMainActivity, GActWebView.class);

                intent.putExtra("startURL", strURL);
                intent.putExtra("strTitle", strTitle);

                MainActivity.m_oMainActivity.startActivity(intent);
            }
            else
                return;
        }
    }
}