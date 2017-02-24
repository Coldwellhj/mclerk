package com.eaosoft.mclerk;

import android.os.Bundle;
import android.app.Activity;
import android.view.Window;

public class GWareHouseFillPrint extends Activity {
    private GWareHouseFillPrintMain			m_oWareHouseFillPrintMain;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        m_oWareHouseFillPrintMain = new GWareHouseFillPrintMain(this);
        setContentView(m_oWareHouseFillPrintMain.OnCreateView());


    }

}
