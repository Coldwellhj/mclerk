package com.eaosoft.view;

import com.eaosoft.mclerk.MainActivity;
import com.eaosoft.mclerk.R;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.JsResult;
import android.webkit.WebView;
import android.widget.AbsoluteLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import java.lang.ref.WeakReference;  
import java.lang.reflect.Field; 

public class GProgressWebView extends WebView 
{
    private final static String TAG = GProgressWebView.class.getSimpleName();

    private ProgressBar 			progressBar;
    private Context 				context;
    private Handler				m_oUserHandler=null;
    private Object					m_oUserObject  = null;
    //================================================
    private	LinearLayout 		m_oHeaderWin=null;
    private TextView				m_txtTitle=null;
    //================================================    
  
    public GProgressWebView(Context context) 
    {
        super(context);
        this.context = context;
        
        progressBar = new ProgressBar(context, null, android.R.attr.progressBarStyleHorizontal);
        progressBar.setLayoutParams(new AbsoluteLayout.LayoutParams(AbsoluteLayout.LayoutParams.MATCH_PARENT, 6, 0, 0));
        progressBar.setProgressDrawable(getResources().getDrawable(R.drawable.web_view_progress_bar));
        addView(progressBar);
        setWebChromeClient(new WebChromeClient());
    }
    public GProgressWebView(Context context,String strTitle) 
    {
        super(context);
        this.context = context;
        
        progressBar = new ProgressBar(context, null, android.R.attr.progressBarStyleHorizontal);
        progressBar.setLayoutParams(new AbsoluteLayout.LayoutParams(AbsoluteLayout.LayoutParams.MATCH_PARENT, 6, 0, 0));
        progressBar.setProgressDrawable(getResources().getDrawable(R.drawable.web_view_progress_bar));
        if(strTitle.length()>0)
        	addView(onCreateHeader(context,strTitle));

        addView(progressBar);
        setWebChromeClient(new WebChromeClient());
    }
    public GProgressWebView(Context context,Handler oUserHandler,Object oUserObject) 
    {
        super(context);
        m_oUserHandler = oUserHandler;
        m_oUserObject   = oUserObject;
        this.context = context;
        progressBar = new ProgressBar(context, null, android.R.attr.progressBarStyleHorizontal);
        progressBar.setLayoutParams(new AbsoluteLayout.LayoutParams(AbsoluteLayout.LayoutParams.MATCH_PARENT, 6, 0, 0));
        progressBar.setProgressDrawable(getResources().getDrawable(R.drawable.web_view_progress_bar));
        addView(progressBar);
        setWebChromeClient(new WebChromeClient());
    }
    public GProgressWebView(Context context, AttributeSet attrs) 
    {  
        super(context.getApplicationContext(), attrs);        
        this.context = context;
        progressBar = new ProgressBar(context, null, android.R.attr.progressBarStyleHorizontal);
        progressBar.setLayoutParams(new AbsoluteLayout.LayoutParams(AbsoluteLayout.LayoutParams.MATCH_PARENT, 6, 0, 0));
        progressBar.setProgressDrawable(getResources().getDrawable(R.drawable.web_view_progress_bar));
        addView(progressBar);
        setWebChromeClient(new WebChromeClient());
    }
    public GProgressWebView(Context context, AttributeSet attrs, int defStyle) 
    {  
        super(context.getApplicationContext(), attrs, defStyle);  
        this.context = context;
        progressBar = new ProgressBar(context, null, android.R.attr.progressBarStyleHorizontal);
        progressBar.setLayoutParams(new AbsoluteLayout.LayoutParams(AbsoluteLayout.LayoutParams.MATCH_PARENT, 6, 0, 0));
        progressBar.setProgressDrawable(getResources().getDrawable(R.drawable.web_view_progress_bar));
        addView(progressBar);
        setWebChromeClient(new WebChromeClient());
    } 
    public View onCreateHeader(Context oContext,String strTitle)
    {
    	m_oHeaderWin = new LinearLayout(oContext);  //线性布局方式  
    	m_oHeaderWin.setOrientation( LinearLayout.HORIZONTAL ); //控件对其方式为垂直排列  VERTICAL
    	m_oHeaderWin.setLayoutParams( new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, 80) );
    	//=========================================================================
    	m_txtTitle = new TextView(oContext);
    	m_txtTitle.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
    	m_txtTitle.setGravity(Gravity.CENTER);
    	m_txtTitle.setText(strTitle);
    	m_oHeaderWin.addView(m_txtTitle);
    	//=========================================================================
    	return m_oHeaderWin;
    }
   
    public class WebChromeClient extends android.webkit.WebChromeClient 
    {    	
        @Override
        public void onProgressChanged(WebView view, int newProgress) 
        {
            Log.d(TAG, "newProgress" + newProgress);
            if (newProgress == 100) 
            {
                progressBar.setVisibility(GONE);
                if(m_oUserHandler!=null)
                {
                	Message msg = new Message(); 
            		msg.what =MainActivity.PROGRESS_NETWORK_WAIT_END;
            		msg.obj = m_oUserObject;
                	m_oUserHandler.sendMessage(msg);
                }
            } 
            else 
            {
                if (progressBar.getVisibility() == GONE)
                {
                    progressBar.setVisibility(VISIBLE);
                    if(m_oUserHandler!=null)
                    {
                    	Message msg = new Message(); 
                		msg.what =MainActivity.PROGRESS_NETWORK_WAIT_START;
                		msg.obj = m_oUserObject;
                    	m_oUserHandler.sendMessage(msg);
                    }
                }
                progressBar.setProgress(newProgress);
            }
            super.onProgressChanged(view, newProgress);
        }

        // 处理javascript中的console.log
        @Override
        public boolean onConsoleMessage(ConsoleMessage cm)
        {
            android.util.Log.d(TAG, "webview console " + cm.lineNumber() + " of " + cm.sourceId() + " : " + cm.message());
            return true;
        }
        
        // 处理javascript中的alert()
        @Override
        public boolean onJsAlert(WebView view, String url, String message, JsResult result) 
        {
            //ToastUtil.showMessage(context, message, Toast.LENGTH_SHORT, Gravity.CENTER);
            result.cancel();
            return true;
        }

    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        LayoutParams lp = (LayoutParams) progressBar.getLayoutParams();
        lp.x = l;
        lp.y = t;
        progressBar.setLayoutParams(lp);
        super.onScrollChanged(l, t, oldl, oldt);
    }
}