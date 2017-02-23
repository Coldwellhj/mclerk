package com.eaosoft.mclerk;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.eaosoft.adapter.GCardKindAdapter;
import com.eaosoft.adapter.GGroupAdapter;
import com.eaosoft.adapter.GHttpDAO;
import com.eaosoft.userinfo.GOperaterInfo;
import com.eaosoft.util.ActivityCollector;
import com.eaosoft.util.GSvrChannel;
import com.eaosoft.util.GUtilHttp;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout.LayoutParams;

public class GActGroupList extends  Activity 
{
	private GGroupAdapter				m_oGroupListAdapter=null;
	private GHttpDAO						m_oGroupListDAO=null;
	private ListView							m_oListView=null;
	
	protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);        
		setContentView(onCreateMainWnd(this));
		ActivityCollector.addActivity(this);		
		setResult(RESULT_OK, null);
    }
	@Override
	protected void onResume() {
		/**
		 * 设置为横屏
		 */
		if(MainActivity.m_nOperaterUI==MainActivity.UI_OP_ROLE_STORE) {
			if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
				setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
			}
		}
		super.onResume();
	}
  @Override
	protected void onDestroy() 
    {
		super.onDestroy();
		ActivityCollector.removeActivity(this);
	}
  	OnItemClickListener lv_listener = new OnItemClickListener() 
	{
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,	long arg3) 
		{
			Map map = (Map) m_oGroupListAdapter.ar.get(arg2);
			if(map == null)
				return;
			GOperaterInfo.m_strGroupSerialNo = map.get("serialNo").toString();
			GOperaterInfo.m_strGroupUID= map.get("uID").toString();
			GOperaterInfo.m_strGroupName=  map.get("caption").toString();	
			GOperaterInfo.m_strDefaultDeptUID="";
			onGetGroupDeptList();
		}
	};
	private void onGetGroupDeptList()
	{
		GSvrChannel svr= 	new GSvrChannel()
    	{
    		public void onNetFailure(int statusCode,String strInfo)
    		{
    			MainActivity.MessageBox("读取门店包房列表","statusCode:"+statusCode+",Info:"+strInfo);
    			MainActivity.onUserMessageBox("读取门店包房列表", "读取门店包房列表失败，请检查网络是否畅通或者联系管理员！");
    		}
    		public void onNetSuccess(int nCode,String strInfo,JSONObject oJsonData)
    		{
    			if(nCode < 0)
    			{
    				MainActivity.MessageBox("读取门店包房列表",strInfo);    				
    				MainActivity.onUserMessageBox("读取门店包房列表",strInfo);
    				return;
    			}
    			
    			try 
    			{
    				JSONArray xContent=null;
					JSONObject oData = oJsonData.getJSONObject("data");
					if(oData == null)
					{
						MainActivity.MessageBox("读取门店包房列表","读取门店包房列表失败，请检查网络是否畅通或者联系管理员！");
						MainActivity.onUserMessageBox("读取门店包房列表","读取门店包房列表失败，请检查网络是否畅通或者联系管理员！");
	    				return;
					}
					try{xContent = oData.getJSONArray("content");}catch(JSONException e){xContent=null;}
					if(xContent==null)
						return;
					GOperaterInfo.m_oGroupDepts.clear();
					for(int i=0;i<xContent.length();i++)
					{
						JSONObject o = xContent.getJSONObject(i);
						Map map = new HashMap();
						map.put("uID", GUtilHttp.getJSONObjectValue("uID",o));
						if(map.get("uID").toString().length()<1)
							continue;
						if(GOperaterInfo.m_strDefaultDeptUID.length()<1)
						{
							GOperaterInfo.m_strDefaultDeptSerialNo = GUtilHttp.getJSONObjectValue("serialNo",o);
							GOperaterInfo.m_strDefaultDeptUID =GUtilHttp.getJSONObjectValue("uID",o) ;
						}
						map.put("caption",GUtilHttp.getJSONObjectValue("caption",o) );						
						map.put("serialNo", GUtilHttp.getJSONObjectValue("serialNo",o) );
						map.put("captionNick", GUtilHttp.getJSONObjectValue("captionNick",o) );									
						GOperaterInfo.m_oGroupDepts.add(map);
					}
					MainActivity.m_oOperaterInfo.onConfigSave();
					finish();
				} 
    			catch (JSONException e) 
    			{
					e.printStackTrace();
					MainActivity.MessageBox("读取门店包房列表",e.getMessage());
					MainActivity.onUserMessageBox("读取门店包房列表","读取门店包房列表失败，请检查网络是否畅通或者联系管理员！");
					return;
				}
    		}
    	};
    	 try
	{
		JSONObject   requestDatas = new JSONObject();
		requestDatas.put("pageIndex", 1);
		requestDatas.put("pageSize", 200);
		requestDatas.put("groupUID", GOperaterInfo.m_strGroupUID);
		svr.m_oCurrentActivity = this;
		svr.onPost("api/mobile/opHRGroupDeptPage.do", requestDatas);
	}
	catch (JSONException e)
	{
             e.printStackTrace();
             MainActivity.MessageBox("读取门店包房列表",e.getMessage());
             MainActivity.onUserMessageBox("读取门店包房列表","读取门店包房列表失败，请检查网络是否畅通或者联系管理员！");
         } 
	}
  	private View onCreateMainWnd(Context oContext)
  	{  		
  		m_oListView = new ListView(oContext);
  		m_oListView.setLayoutParams( new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT) );
  		m_oListView.setBackgroundColor(Color.BLACK);
  		m_oListView.setOnItemClickListener(lv_listener);
  		//=============================================================
  		m_oGroupListAdapter = new GGroupAdapter(oContext);
  		m_oGroupListDAO = new GHttpDAO(this,m_oGroupListAdapter);
  		m_oListView.setAdapter(m_oGroupListAdapter);
  		m_oGroupListDAO.getGroupList();
  		//=============================================================
  		return m_oListView;
  	}  	
}