package com.eaosoft.mclerk;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.webkit.DownloadListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.eaosoft.util.ActivityCollector;
import com.eaosoft.view.GProgressWebView;

public class GActWebView extends Activity 
{
	private GProgressWebView 		m_oWebViewCtrl;  
	private String							m_strStartURL="http://www.eaosoft.com";
	private String							m_strTitle="";
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //m_oWebViewCtrl = new GProgressWebView(this,MainActivity.m_oMsgHandler,this);
        OnReadUserParameter();
        /*
        if(m_strTitle.length()>0)
        	m_oWebViewCtrl = new GProgressWebView(this,m_strTitle);
        else
        	m_oWebViewCtrl = new GProgressWebView(this);
		setContentView(m_oWebViewCtrl);
        */
        setContentView(onCreateView(this));
        ActivityCollector.addActivity(this);
        
    	m_oWebViewCtrl.setWebViewClient(new WebViewClient() 
    	{     
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl)     
            {                 // Handle the error         
                  
            }               
                    
            public boolean shouldOverrideUrlLoading(WebView view, String url)   
            {                
                view.loadUrl(url);     //不要跳往系统窗口
                return true;         
            }        
        });    
        m_oWebViewCtrl.setDownloadListener(new DownloadListener() 
        {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                if (url != null && url.startsWith("http://"))
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
            }
        });
        m_oWebViewCtrl.loadUrl(m_strStartURL); 
    }
    private View onCreateView(Context oContext)
    {
    	if(m_strTitle.length()<1)
    		return new GProgressWebView(oContext);

    	LinearLayout oMainWin = new LinearLayout(oContext);  //线性布局方式  
		oMainWin.setOrientation( LinearLayout.VERTICAL ); //控件对其方式为垂直排列  VERTICAL
		oMainWin.setLayoutParams( new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT) );
		//===============================================================================
		//&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&
		LinearLayout m_oHeaderWin = new LinearLayout(oContext);   
    	m_oHeaderWin.setOrientation( LinearLayout.HORIZONTAL ); 
    	m_oHeaderWin.setLayoutParams( new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, 80) );
    	m_oHeaderWin.setGravity(Gravity.CENTER_HORIZONTAL);
    	
    	
    	TextView m_txtTitle = new TextView(oContext);
    	m_txtTitle.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.FILL_PARENT));
    	m_txtTitle.setGravity(Gravity.CENTER);
    	m_txtTitle.setText(m_strTitle);
    	m_oHeaderWin.addView(m_txtTitle);    	
		//&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&
    	//===============================================================================
    	m_oWebViewCtrl = new GProgressWebView(oContext);
		//===============================================================================
    	oMainWin.addView(m_oHeaderWin);
    	oMainWin.addView(m_oWebViewCtrl);
		return oMainWin;
    }
    private View onCreateWebView(Context oContext)
    {
    	/*
    	 //============================================================================
		//主背景
		LinearLayout oMainWin = new LinearLayout(oContext);  //线性布局方式  
		oMainWin.setOrientation( LinearLayout.VERTICAL ); //控件对其方式为垂直排列  VERTICAL
		oMainWin.setLayoutParams( new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT) );
        
		 RelativeLayout oSubHeader = new RelativeLayout(oContext);  //线性布局方式          
        oSubHeader.setLayoutParams( new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, 40*MainActivity.m_nPixelsFromDP) );
        oSubHeader.setBackgroundColor(Color.rgb(0x1B,0x9A,0x16));
        
        RelativeLayout.LayoutParams pBar = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, 3);
        m_oProgressBar = new ProgressBar(oContext);
        m_oProgressBar.setLayoutParams(pBar);
        m_oProgressBar.setProgressDrawable(getResources().getDrawable(R.drawable.web_view_progress_bar));
        m_oProgressBar.setVisibility(View.GONE);
        m_oProgressBar.setId(1000);
        
        oSubHeader.addView(m_oProgressBar);
        //==========================================================================
    	m_oWebViewCtrl =new WebView(oContext);
    	 //设置WebView属性，能够执行Javascript脚本  
        m_oWebViewCtrl.getSettings().setJavaScriptEnabled(true);  
        m_oWebViewCtrl.setWebChromeClient(new WebChromeClient()
        {  
            @Override  
            public void onProgressChanged(WebView view, int newProgress) 
            {  
            	Log.e("webview", "newProgress:"+newProgress);
                if(newProgress==100)  
                	m_oProgressBar.setVisibility(View.GONE);//加载完网页进度条消失                 
                else
                {  
                	m_oProgressBar.setVisibility(View.VISIBLE);//开始加载网页时显示进度条  
                	m_oProgressBar.setProgress(newProgress);//设置进度值  
                }  
                  
            }  
        });  
        RelativeLayout.LayoutParams pWeb = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        pWeb.addRule(RelativeLayout.BELOW,1000);
        m_oWebViewCtrl.setLayoutParams(pWeb);
        
        oSubHeader.addView(m_oWebViewCtrl);
        oMainWin.addView(oSubHeader);
    	return oMainWin;
    	*/
    	return null;
    }
    private void OnReadUserParameter()
   	{
   		Bundle bundle = this.getIntent().getExtras();
           
           if(bundle == null)
           	return;
           if(bundle.getString("startURL")!=null)
        	   m_strStartURL = bundle.getString("startURL");          
           if(bundle.getString("strTitle")!=null)
        	   m_strTitle = bundle.getString("strTitle");          
       }
    @Override 
    //设置回退  
    //覆盖Activity类的onKeyDown(int keyCoder,KeyEvent event)方法  
    public boolean onKeyDown(int keyCode, KeyEvent event) 
    {  
        if ((keyCode == KeyEvent.KEYCODE_BACK) && m_oWebViewCtrl.canGoBack()) 
        {  
        	m_oWebViewCtrl.goBack(); //goBack()表示返回WebView的上一页面  
            return true;  
        }  
        finish();
        return false;  
    }
    @Override
	protected void onDestroy() 
    {
		super.onDestroy();
		ActivityCollector.removeActivity(this);
	}
}