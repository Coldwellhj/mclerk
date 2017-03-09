package com.eaosoft.mclerk;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.widget.Toast;

public class GWareHouseFillPrint extends Activity {
    private GWareHouseFillPrintMain			m_oWareHouseFillPrintMain;
    public static GWareHouseFillPrint m_oGWareHouseFillPrint = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        m_oWareHouseFillPrintMain = new GWareHouseFillPrintMain(this);
        setContentView(m_oWareHouseFillPrintMain.OnCreateView());
        m_oGWareHouseFillPrint=this;

    }
    public static void onUserMessageBox(String strTitle, String strText) {
        Toast.makeText(GWareHouseFillPrint.m_oGWareHouseFillPrint, strText, Toast.LENGTH_SHORT).show();
    }
}
