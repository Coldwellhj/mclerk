package com.eaosoft.mclerk;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.eaosoft.userinfo.GOperaterInfo;
import com.eaosoft.util.ActivityCollector;
import com.eaosoft.util.GSvrChannel;
import com.eaosoft.util.GUtilHttp;
import com.eaosoft.view.GCouponsView;
import com.eaosoft.view.GRoundImageView;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


public class GActCardCreate extends  Activity 
{
	//===============================================
	private String					m_strCardNo="";
	private String					m_strCardKindUID="";
	//===============================================
	private GRoundImageView	m_oImgCardLogo;
	private GCouponsView			m_oCouponsView;
	private TextView					m_oCaption;
	private TextView					m_oSerialNo;
	private TextView					m_oTotalMoney;
	private TextView					m_oDateEnd;
	private String						m_strBKColor="#ff0000";
	private EditText					m_oMemPhone;
	private EditText					m_oMemCaption;
	//===============================================
	protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.act_card_create);
		ActivityCollector.addActivity(this);		
		OnReadUserParameter();
		setResult(RESULT_OK, null);
		onChangeCardKind(null);
    }
  @Override
	protected void onDestroy() 
    {
		super.onDestroy();
		ActivityCollector.removeActivity(this);
	}
  	@Override
  	protected void onActivityResult(int requestCode, int resultCode, Intent data) 
	{
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode != RESULT_OK) 
			return;
		if(requestCode == 1001  && data != null)
		{
			m_strBKColor = data.getStringExtra("bkColor");
			int nBKColor = Color.RED;
			try{nBKColor=Color.parseColor(m_strBKColor);}catch(IllegalArgumentException ex){nBKColor = Color.RED;};
			m_oCouponsView.setBackgroundColor(nBKColor);
			m_oImgCardLogo.onSetHttpImage(data.getStringExtra("imgLogo"), GOperaterInfo.m_strImagePath);
			m_oCaption.setText(data.getStringExtra("caption"));
			//m_oSerialNo.setText("卡　　号："+data.getStringExtra("serialNo"));
			m_oTotalMoney.setText("总计金额："+data.getStringExtra("totalMoney"));
			m_oDateEnd.setText("截止日期："+data.getStringExtra("dateEnd"));
			m_strCardKindUID=data.getStringExtra("uID");			
		}
	}
	public void onChangeCardKind(View v)
	{
		Intent intent = new Intent(MainActivity.m_oMainActivity, GActCardKindList.class);
		startActivityForResult(intent, 1001);		
	}
	public void onCardCreateConfirm(View v)
	{
		GSvrChannel svr= 	new GSvrChannel()
    	{
    		public void onNetFailure(int statusCode,String strInfo)
    		{
    			MainActivity.MessageBox("确认购买","statusCode:"+statusCode+",Info:"+strInfo);
    			MainActivity.onUserMessageBox("确认购买", "确认购买失败，请检查网络是否畅通或者联系管理员！");
    		}
    		public void onNetSuccess(int nCode,String strInfo,JSONObject oJsonData)
    		{
    			if(nCode < 0)
    			{
    				MainActivity.MessageBox("确认购买",strInfo);    				
    				MainActivity.onUserMessageBox("确认购买","开卡失败：\r\n\r\n"+strInfo);
    				return;
    			}
    			
    			try 
    			{
    				JSONArray xContent=null;
					JSONObject oData = oJsonData.getJSONObject("data");
					if(oData == null)
					{
						MainActivity.MessageBox("确认购买","确认购买失败，请检查网络是否畅通或者联系管理员！");
						MainActivity.onUserMessageBox("确认购买","确认购买失败，请检查网络是否畅通或者联系管理员！");
	    				return;
					}
					MainActivity.onUserMessageBox("确认开发",strInfo);
					GActCardCreate.this.finish();
				} 
    			catch (JSONException e) 
    			{
					e.printStackTrace();
					MainActivity.MessageBox("确认购买",e.getMessage());
					MainActivity.onUserMessageBox("确认购买","确认购买失败，请检查网络是否畅通或者联系管理员！");
					return;
				}
    		}
    	};
    	 try 
         {
	    	JSONObject   requestDatas = new JSONObject();
	    	
	    	requestDatas.put("cardUID", m_strCardNo);
	    	requestDatas.put("kindUID", m_strCardKindUID);
	    	requestDatas.put("groupUID",GOperaterInfo.m_strGroupUID);
	    	//============================================
	    	if(m_oMemCaption != null)
	    		requestDatas.put("memCaption",m_oMemCaption.getText().toString());
	    	if(m_oMemPhone != null)
	    	requestDatas.put("memPhone",m_oMemPhone.getText().toString());
	    	//============================================
            svr.m_oCurrentActivity = this;
	    	svr.onPost("api/mobile/opCardCreate.do", requestDatas);
         }
    	 catch (JSONException e) 
         {
             e.printStackTrace();
             MainActivity.MessageBox("确认购买",e.getMessage());
             MainActivity.onUserMessageBox("确认购买","确认购买失败，请检查网络是否畅通或者联系管理员！");
         } 
	}
	private void OnReadUserParameter()
	{
		Bundle bundle = this.getIntent().getExtras();
	      
		if(bundle == null)
			return;
		if(bundle.getString("cardNo")!=null)
			m_strCardNo = bundle.getString("cardNo");     
	  //=======================================
      	m_oCouponsView = (GCouponsView)findViewById(R.id.cv_card);
  		m_oImgCardLogo = (GRoundImageView)findViewById(R.id.iv_card_logo);
  		m_oCaption = (TextView)findViewById(R.id.txtCaption);
  		m_oSerialNo = (TextView)findViewById(R.id.txtSerialNo);
  		m_oTotalMoney = (TextView)findViewById(R.id.txtTotalMoney);
  		m_oDateEnd = (TextView)findViewById(R.id.txtDateEnd);
  		m_oMemPhone = (EditText)findViewById(R.id.edt_mem_phone);
  		m_oMemCaption = (EditText)findViewById(R.id.edt_mem_caption);
  		m_oSerialNo.setText("卡　　号："+m_strCardNo);
  		m_oDateEnd.setText("截止日期：");
  		m_oTotalMoney.setText("总计金额：0.00");
	  }
}