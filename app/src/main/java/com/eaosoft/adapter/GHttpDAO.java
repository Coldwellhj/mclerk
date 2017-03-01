package com.eaosoft.adapter;

import android.app.Activity;

import com.eaosoft.mclerk.MainActivity;
import com.eaosoft.userinfo.GOperaterInfo;
import com.eaosoft.util.GBaseAdapter;
import com.eaosoft.util.GSvrChannel;
import com.eaosoft.util.GUtilHttp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GHttpDAO
{
	private GBaseAdapter 			m_oAdapter=null;
	private Activity						m_oActivity=null;
	//===============================================
	public GHttpDAO(Activity oActivity,GBaseAdapter oAdapter)
	{
		m_oAdapter = oAdapter;
		m_oActivity = oActivity;
	}

	public void getGroupDeptList(String strGroupUID)
	{
		
	}
	public void getGroupList()
	{
		GSvrChannel svr= 	new GSvrChannel()
    	{
    		public void onNetFailure(int statusCode,String strInfo)
    		{
    			MainActivity.MessageBox("读取门店列表","statusCode:"+statusCode+",Info:"+strInfo);
    			MainActivity.onUserMessageBox("读取门店列表", "读取门店列表失败，请检查网络是否畅通或者联系管理员！");
    		}
    		public void onNetSuccess(int nCode,String strInfo,JSONObject oJsonData)
    		{
    			if(nCode < 0)
    			{
    				MainActivity.MessageBox("读取门店列表",strInfo);    				
    				MainActivity.onUserMessageBox("读取门店列表",strInfo);
    				return;
    			}
    			
    			try 
    			{
    				JSONArray xContent=null;
					JSONObject oData = oJsonData.getJSONObject("data");
					if(oData == null)
					{
						MainActivity.MessageBox("读取门店列表","读取门店列表失败，请检查网络是否畅通或者联系管理员！");
						MainActivity.onUserMessageBox("读取门店列表","读取门店列表失败，请检查网络是否畅通或者联系管理员！");
	    				return;
					}
					try{xContent = oData.getJSONArray("content");}catch(JSONException e){xContent=null;}
					if(xContent==null)
						return;
					List ar = new ArrayList();					
					for(int i=0;i<xContent.length();i++)
					{
						JSONObject o = xContent.getJSONObject(i);
						Map map = new HashMap();
						map.put("uID", GUtilHttp.getJSONObjectValue("uID",o));
						if(map.get("uID").toString().length()>0)
						{
							map.put("caption",GUtilHttp.getJSONObjectValue("caption",o) );						
							map.put("serialNo", GUtilHttp.getJSONObjectValue("serialNo",o) );
							map.put("briefing", GUtilHttp.getJSONObjectValue("briefing",o) );
							map.put("imgLogo", GUtilHttp.getJSONObjectValue("imgLogo",o) );
							ar.add(map);
						}
					}
					
		            //####################################################
					m_oAdapter.setData(ar);
	        		m_oAdapter.notifyDataSetChanged();
				} 
    			catch (JSONException e) 
    			{
					e.printStackTrace();
					MainActivity.MessageBox("读取门店列表",e.getMessage());
					MainActivity.onUserMessageBox("读取门店列表","读取门店列表失败，请检查网络是否畅通或者联系管理员！");
					return;
				}
    		}
    	};
    	 try 
         {
	    	JSONObject   requestDatas = new JSONObject();
	    	requestDatas.put("pageIndex", 1);
            requestDatas.put("pageSize", 200);
            svr.m_oCurrentActivity = m_oActivity;
	    	svr.onPost("api/mobile/opHRGroupPage.do", requestDatas);
         }
    	 catch (JSONException e) 
         {
             e.printStackTrace();
             MainActivity.MessageBox("读取门店列表",e.getMessage());
             MainActivity.onUserMessageBox("读取门店列表","读取门店列表失败，请检查网络是否畅通或者联系管理员！");
         } 
	}
	public void getCardKindPage(String strSearchText,int nPageIndex,int nPageSize)
	{
		GSvrChannel svr= 	new GSvrChannel()
    	{
    		public void onNetFailure(int statusCode,String strInfo)
    		{
    			MainActivity.MessageBox("读取卡套餐","statusCode:"+statusCode+",Info:"+strInfo);
    			MainActivity.onUserMessageBox("读取卡套餐", "读取卡套餐失败，请检查网络是否畅通或者联系管理员！");
    		}
    		public void onNetSuccess(int nCode,String strInfo,JSONObject oJsonData)
    		{
    			if(nCode < 0)
    			{
    				MainActivity.MessageBox("读取卡套餐",strInfo);    				
    				MainActivity.onUserMessageBox("读取卡套餐",strInfo);
    				return;
    			}
    			
    			try 
    			{
    				JSONArray xContent=null;
					JSONObject oData = oJsonData.getJSONObject("data");
					if(oData == null)
					{
						MainActivity.MessageBox("读取卡套餐","读取卡套餐失败，请检查网络是否畅通或者联系管理员！");
						MainActivity.onUserMessageBox("读取卡套餐","读取卡套餐失败，请检查网络是否畅通或者联系管理员！");
	    				return;
					}
					try{xContent = oData.getJSONArray("content");}catch(JSONException e){xContent=null;}
					if(xContent==null)
						return;
					List ar = new ArrayList();					
					for(int i=0;i<xContent.length();i++)
					{
						JSONObject o = xContent.getJSONObject(i);
						Map map = new HashMap();
						map.put("uID", GUtilHttp.getJSONObjectValue("uID",o));
						if(map.get("uID").toString().length()>0)
						{
							map.put("caption",GUtilHttp.getJSONObjectValue("caption",o) );						
							map.put("serialNo", GUtilHttp.getJSONObjectValue("serialNo",o) );
							map.put("briefing", GUtilHttp.getJSONObjectValue("briefing",o) );
							map.put("totalMoney",GUtilHttp.getJSONObjectValue("totalMoney",o) );
							map.put("dateStart", GUtilHttp.getJSONObjectValue("dateStart",o) );
							map.put("imgLogo", GUtilHttp.getJSONObjectValue("imgLogo",o) );
							map.put("dateEnd", GUtilHttp.getJSONObjectValue("dateEnd",o) );
							map.put("bkColor", GUtilHttp.getJSONObjectValue("bkColor",o) );
							map.put("enabled", GUtilHttp.getJSONObjectValue("enabled",o) );
							ar.add(map);
						}
					}
					
		            //####################################################
					m_oAdapter.setData(ar);
	        		m_oAdapter.notifyDataSetChanged();
				} 
    			catch (JSONException e) 
    			{
					e.printStackTrace();
					MainActivity.MessageBox("读取卡套餐",e.getMessage());
					MainActivity.onUserMessageBox("读取卡套餐","读取卡套餐失败，请检查网络是否畅通或者联系管理员！");
					return;
				}
    		}
    	};
    	 try 
         {
	    	JSONObject   requestDatas = new JSONObject();
	    	requestDatas.put("pageIndex", nPageIndex);
            requestDatas.put("pageSize", nPageSize);
            requestDatas.put("groupUID", GOperaterInfo.m_strGroupUID);
            requestDatas.put("searchText", strSearchText);
            svr.m_oCurrentActivity = m_oActivity;
	    	svr.onPost("api/mobile/opHRGroupCardKindPage.do", requestDatas);
         }
    	 catch (JSONException e) 
         {
             e.printStackTrace();
             MainActivity.MessageBox("读取卡套餐",e.getMessage());
             MainActivity.onUserMessageBox("读取卡套餐","读取卡套餐失败，请检查网络是否畅通或者联系管理员！");
         } 
	}
	public void getWareHouseDetail()
	{

		GSvrChannel svr= 	new GSvrChannel()
		{
			public void onNetFailure(int statusCode,String strInfo)
			{

				MainActivity.MessageBox("读取打印列表","statusCode:"+statusCode+",Info:"+strInfo);
				MainActivity.onUserMessageBox("读取打印列表", "读取打印列表失败，请检查网络是否畅通或者联系管理员！");
			}
			public void onNetSuccess(int nCode,String strInfo,JSONObject oJsonData)
			{

				if(nCode <=0)
				{
					MainActivity.MessageBox("读取打印列表",strInfo);
					MainActivity.onUserMessageBox("读取打印列表",strInfo);
					return;
				}

				try
				{
					JSONArray xContent=null;
					JSONArray content=null;
					JSONObject oData = oJsonData.getJSONObject("data");
					if(oData == null)
					{
						MainActivity.MessageBox("读取打印列表","读取打印列表失败，请检查网络是否畅通或者联系管理员！");
						MainActivity.onUserMessageBox("读取打印列表","读取打印列表失败，请检查网络是否畅通或者联系管理员！");
						return;
					}
					try{xContent = oData.getJSONArray("content");
						}catch(JSONException e){xContent=null;}
					if(xContent== null)
						return;
					List ar = new ArrayList();
					for(int i=0;i<xContent.length();i++)
					{

						JSONObject o = xContent.getJSONObject(i);

						Map map = new HashMap();

						map.put("orderUID", GUtilHttp.getJSONObjectValue("orderUID",o));
						if(map.get("orderUID").toString().length()>0)
						{

							map.put("orderUID",GUtilHttp.getJSONObjectValue("orderUID",o) );
							map.put("roomSerialNo", GUtilHttp.getJSONObjectValue("roomSerialNo",o) );
							map.put("cardUID", GUtilHttp.getJSONObjectValue("cardUID",o) );
							map.put("orderTime",GUtilHttp.getJSONObjectValue("orderTime",o) );
							map.put("userCaption", GUtilHttp.getJSONObjectValue("userCaption",o) );
							content = o.getJSONArray("content");
							for(int j=0;j<content.length();j++) {
								JSONObject oo = content.getJSONObject(j);
								map.put("goodsCaption", GUtilHttp.getJSONObjectValue("goodsCaption", oo));
								if (map.get("goodsCaption").toString().length() > 0) {
									map.put("goodsCaption", GUtilHttp.getJSONObjectValue("goodsCaption", oo));
									map.put("goodsNumber", GUtilHttp.getJSONObjectValue("goodsNumber", oo));
									map.put("goodsUnitName", GUtilHttp.getJSONObjectValue("goodsUnitName", oo));
								}
							}
							map.put("taskUID", GUtilHttp.getJSONObjectValue("taskUID",o) );
							ar.add(map);
						}
					}

					//####################################################
					m_oAdapter.setData(ar);
					m_oAdapter.notifyDataSetChanged();

				}

				catch (JSONException e)
				{
					e.printStackTrace();
					MainActivity.MessageBox("读取打印列表",e.getMessage());
					MainActivity.onUserMessageBox("读取打印列表","读取打印列表失败，请检查网络是否畅通或者联系管理员！");
					return;
				}

			}
		};
		try
		{
			JSONObject   requestDatas = new JSONObject();
			requestDatas.put("groupUID", GOperaterInfo.m_strGroupUID);
			svr.m_oCurrentActivity = m_oActivity;
			svr.onPost("api/mobile/opPrnTaskPage.do", requestDatas);
		}
		catch (JSONException e)
		{
			e.printStackTrace();
			MainActivity.MessageBox("读读取打印列表",e.getMessage());
			MainActivity.onUserMessageBox("读取打印列表","读取打印列表失败，请检查网络是否畅通或者联系管理员！");
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	public void opPrnTaskComplete(String taskUID)
	{
		GSvrChannel svr= 	new GSvrChannel()
		{
			public void onNetFailure(int statusCode,String strInfo)
			{
				MainActivity.MessageBox("打印任务","statusCode:"+statusCode+",Info:"+strInfo);
				MainActivity.onUserMessageBox("打印任务", "打印任务失败，请检查网络是否畅通或者联系管理员！");
			}
			public void onNetSuccess(int nCode,String strInfo,JSONObject oJsonData)
			{
				if(nCode < 0)
				{
					MainActivity.MessageBox("打印任务",strInfo);
					MainActivity.onUserMessageBox("打印任务",strInfo);
					return;
				}

				try
				{
					JSONObject oData = oJsonData.getJSONObject("data");
					if(oData == null)
					{
						MainActivity.MessageBox("打印任务","打印任务失败，请检查网络是否畅通或者联系管理员！");
						MainActivity.onUserMessageBox("打印任务","打印任务失败，请检查网络是否畅通或者联系管理员！");
						return;
					}
						if(GUtilHttp.getJSONObjectValue("taskUID",oData)!=null){
							m_oAdapter.deletePrintTaskItem(GUtilHttp.getJSONObjectValue("taskUID",oData));
							m_oAdapter.notifyDataSetChanged();
						}


				}
				catch (JSONException e)
				{
					e.printStackTrace();
					MainActivity.MessageBox("打印任务",e.getMessage());
					MainActivity.onUserMessageBox("打印任务","打印任务失败，请检查网络是否畅通或者联系管理员！");
					return;
				}
			}
		};
		try
		{
			JSONObject   requestDatas = new JSONObject();

			requestDatas.put("taskUID", taskUID);
			svr.m_oCurrentActivity = MainActivity.m_oMainActivity;
			svr.onPost("api/mobile/opPrnTaskComplete.do", requestDatas);
		}
		catch (JSONException e)
		{
			e.printStackTrace();
			MainActivity.MessageBox("打印任务",e.getMessage());
			MainActivity.onUserMessageBox("打印任务","打印任务失败，请检查网络是否畅通或者联系管理员！");
		}
	}
}