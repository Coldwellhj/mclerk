package com.eaosoft.mclerk;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

import com.eaosoft.util.ActivityCollector;

public class GFramActZXingCapture extends Activity 
{
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.act_zxing_capture);
        ActivityCollector.addActivity(this);
    }
    @Override
   	protected void onDestroy() 
   	{
   		super.onDestroy();
   		ActivityCollector.removeActivity(this);
   	}
}