package com.eaosoft.mclerk;

import com.eaosoft.util.ActivityCollector;
import com.google.zxing.client.android.CaptureActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

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