package com.eaosoft.adapter;

import android.app.Activity;
import android.os.Message;

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

//import static com.eaosoft.mclerk.GCashier_Search.mHandler_GCashier;
import static com.eaosoft.mclerk.GCashier_Salse_Report.mHandler_GCashier;
import static com.eaosoft.mclerk.GWareHouseMainActivity.mHandler;

public class GHttpDAO
{
	private GBaseAdapter 			m_oAdapter=null;
	private Activity						m_oActivity=null;
	public int HD_page;
	public int totalPages;
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
				MainActivity.MessageBox("��ȡ�ŵ��б�","statusCode:"+statusCode+",Info:"+strInfo);
				MainActivity.onUserMessageBox("��ȡ�ŵ��б�", "��ȡ�ŵ��б�ʧ�ܣ����������Ƿ�ͨ������ϵ����Ա��");
			}
			public void onNetSuccess(int nCode,String strInfo,JSONObject oJsonData)
			{
				if(nCode < 0)
				{
					MainActivity.MessageBox("��ȡ�ŵ��б�",strInfo);
					MainActivity.onUserMessageBox("��ȡ�ŵ��б�",strInfo);
					return;
				}

				try
				{
					JSONArray xContent=null;
					JSONObject oData = oJsonData.getJSONObject("data");
					if(oData == null)
					{
						MainActivity.MessageBox("��ȡ�ŵ��б�","��ȡ�ŵ��б�ʧ�ܣ����������Ƿ�ͨ������ϵ����Ա��");
						MainActivity.onUserMessageBox("��ȡ�ŵ��б�","��ȡ�ŵ��б�ʧ�ܣ����������Ƿ�ͨ������ϵ����Ա��");
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
					MainActivity.MessageBox("��ȡ�ŵ��б�",e.getMessage());
					MainActivity.onUserMessageBox("��ȡ�ŵ��б�","��ȡ�ŵ��б�ʧ�ܣ����������Ƿ�ͨ������ϵ����Ա��");
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
			MainActivity.MessageBox("��ȡ�ŵ��б�",e.getMessage());
			MainActivity.onUserMessageBox("��ȡ�ŵ��б�","��ȡ�ŵ��б�ʧ�ܣ����������Ƿ�ͨ������ϵ����Ա��");
		}
	}
	public void getCardKindList(String strSearchText,int nPageIndex,int nPageSize)
	{
		GSvrChannel svr= 	new GSvrChannel()
		{
			public void onNetFailure(int statusCode,String strInfo)
			{
				MainActivity.MessageBox("��ȡ���ײ�","statusCode:"+statusCode+",Info:"+strInfo);
				MainActivity.onUserMessageBox("��ȡ���ײ�", "��ȡ���ײ�ʧ�ܣ����������Ƿ�ͨ������ϵ����Ա��");
			}
			public void onNetSuccess(int nCode,String strInfo,JSONObject oJsonData)
			{
				if(nCode < 0)
				{
					MainActivity.MessageBox("��ȡ���ײ�",strInfo);
					MainActivity.onUserMessageBox("��ȡ���ײ�",strInfo);
					return;
				}

				try
				{
					JSONArray xContent=null;
                    JSONArray goodsList=null;
					JSONObject oData = oJsonData.getJSONObject("data");
					if(oData == null)
					{
						MainActivity.MessageBox("��ȡ���ײ�","��ȡ���ײ�ʧ�ܣ����������Ƿ�ͨ������ϵ����Ա��");
						MainActivity.onUserMessageBox("��ȡ���ײ�","��ȡ���ײ�ʧ�ܣ����������Ƿ�ͨ������ϵ����Ա��");
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
							map.put("operaterName", GUtilHttp.getJSONObjectValue("operaterName",o) );
							map.put("orderNo", GUtilHttp.getJSONObjectValue("orderNo",o) );
                            goodsList = o.getJSONArray("goodsList");
                            List<Map> ar1 = new ArrayList<Map>();
                            for(int j=0;j<goodsList.length();j++) {
                                JSONObject oo = goodsList.getJSONObject(j);
                                Map map1 = new HashMap();
                                map1.put("caption", GUtilHttp.getJSONObjectValue("caption", oo));
                                if (map1.get("caption").toString().length() > 0) {
                                    map1.put("caption",GUtilHttp.getJSONObjectValue("caption",oo) );
                                    map1.put("serialNo", GUtilHttp.getJSONObjectValue("serialNo",oo) );
                                    map1.put("unitName", GUtilHttp.getJSONObjectValue("unitName",oo) );
                                    map1.put("norImage", GUtilHttp.getJSONObjectValue("norImage",oo) );
                                    map1.put("minImage", GUtilHttp.getJSONObjectValue("minImage",oo) );
                                    map1.put("price", GUtilHttp.getJSONObjectValue("price",oo));
                                    map1.put("num",GUtilHttp.getJSONObjectValue("goodsNum",oo) );

                                    ar1.add(map1);
                                }
                            }
                            map.put("ar1",ar1);
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
					MainActivity.MessageBox("��ȡ���ײ�",e.getMessage());
					MainActivity.onUserMessageBox("��ȡ���ײ�","��ȡ���ײ�ʧ�ܣ����������Ƿ�ͨ������ϵ����Ա��");
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
			svr.onPost("api/mobile/opHRGroupCardKindList.do", requestDatas);
		}
		catch (JSONException e)
		{
			e.printStackTrace();
			MainActivity.MessageBox("��ȡ���ײ�",e.getMessage());
			MainActivity.onUserMessageBox("��ȡ���ײ�","��ȡ���ײ�ʧ�ܣ����������Ƿ�ͨ������ϵ����Ա��");
		}
	}
    public void getCardKindPage(String strSearchText,int nPageIndex,int nPageSize,String kindMoney)
    {
        GSvrChannel svr= 	new GSvrChannel()
        {
            public void onNetFailure(int statusCode,String strInfo)
            {
                MainActivity.MessageBox("��ȡ���ײ�","statusCode:"+statusCode+",Info:"+strInfo);
                MainActivity.onUserMessageBox("��ȡ���ײ�", "��ȡ���ײ�ʧ�ܣ����������Ƿ�ͨ������ϵ����Ա��");
            }
            public void onNetSuccess(int nCode,String strInfo,JSONObject oJsonData)
            {
                if(nCode < 0)
                {
                    MainActivity.MessageBox("��ȡ���ײ�",strInfo);
                    MainActivity.onUserMessageBox("��ȡ���ײ�",strInfo);
                    return;
                }

                try
                {
                    JSONArray xContent=null;
                    JSONObject oData = oJsonData.getJSONObject("data");
                    if(oData == null)
                    {
                        MainActivity.MessageBox("��ȡ���ײ�","��ȡ���ײ�ʧ�ܣ����������Ƿ�ͨ������ϵ����Ա��");
                        MainActivity.onUserMessageBox("��ȡ���ײ�","��ȡ���ײ�ʧ�ܣ����������Ƿ�ͨ������ϵ����Ա��");
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
                    MainActivity.MessageBox("��ȡ���ײ�",e.getMessage());
                    MainActivity.onUserMessageBox("��ȡ���ײ�","��ȡ���ײ�ʧ�ܣ����������Ƿ�ͨ������ϵ����Ա��");
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
            requestDatas.put("kindMoney", kindMoney);
            svr.m_oCurrentActivity = m_oActivity;
            svr.onPost("api/mobile/opHRGroupCardKindPage.do", requestDatas);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
            MainActivity.MessageBox("��ȡ���ײ�",e.getMessage());
            MainActivity.onUserMessageBox("��ȡ���ײ�","��ȡ���ײ�ʧ�ܣ����������Ƿ�ͨ������ϵ����Ա��");
        }
    }
    public void getCardKindMoneyPage()
    {
        GSvrChannel svr= 	new GSvrChannel()
        {
            public void onNetFailure(int statusCode,String strInfo)
            {
                MainActivity.MessageBox("��ȡ���ײ�","statusCode:"+statusCode+",Info:"+strInfo);
                MainActivity.onUserMessageBox("��ȡ���ײ�", "��ȡ���ײ�ʧ�ܣ����������Ƿ�ͨ������ϵ����Ա��");
            }
            public void onNetSuccess(int nCode,String strInfo,JSONObject oJsonData)
            {
                if(nCode < 0)
                {
                    MainActivity.MessageBox("��ȡ���ײ�",strInfo);
                    MainActivity.onUserMessageBox("��ȡ���ײ�",strInfo);
                    return;
                }

                try
                {
                    JSONArray xContent=null;
                    JSONObject oData = oJsonData.getJSONObject("data");
                    if(oData == null)
                    {
                        MainActivity.MessageBox("��ȡ���ײ�","��ȡ���ײ�ʧ�ܣ����������Ƿ�ͨ������ϵ����Ա��");
                        MainActivity.onUserMessageBox("��ȡ���ײ�","��ȡ���ײ�ʧ�ܣ����������Ƿ�ͨ������ϵ����Ա��");
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
                        map.put("kindMoney", GUtilHttp.getJSONObjectValue("kindMoney",o));

                        ar.add(map);


                    }

                    //####################################################
                    m_oAdapter.setData(ar);
                    m_oAdapter.notifyDataSetChanged();

                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                    MainActivity.MessageBox("��ȡ���ײ�",e.getMessage());
                    MainActivity.onUserMessageBox("��ȡ���ײ�","��ȡ���ײ�ʧ�ܣ����������Ƿ�ͨ������ϵ����Ա��");
                    return;
                }
            }
        };
        try
        {
            JSONObject   requestDatas = new JSONObject();

            requestDatas.put("groupUID", GOperaterInfo.m_strGroupUID);

            svr.m_oCurrentActivity = m_oActivity;
            svr.onPost("api/mobile/opHRGroupCardKindMoneyList.do", requestDatas);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
            MainActivity.MessageBox("��ȡ���ײ�",e.getMessage());
            MainActivity.onUserMessageBox("��ȡ���ײ�","��ȡ���ײ�ʧ�ܣ����������Ƿ�ͨ������ϵ����Ա��");
        }
    }
	public void getWareHouseDetail()
	{

		GSvrChannel svr= 	new GSvrChannel()
		{
			public void onNetFailure(int statusCode,String strInfo)
			{

				MainActivity.MessageBox("��ȡ��ӡ�б�","statusCode:"+statusCode+",Info:"+strInfo);
				MainActivity.onUserMessageBox("��ȡ��ӡ�б�", "��ȡ��ӡ�б�ʧ�ܣ����������Ƿ�ͨ������ϵ����Ա��");
			}
			public void onNetSuccess(int nCode,String strInfo,JSONObject oJsonData)
			{

				if(nCode <=0)
				{
					MainActivity.MessageBox("��ȡ��ӡ�б�",strInfo);
					MainActivity.onUserMessageBox("��ȡ��ӡ�б�",strInfo);
					return;
				}

				try
				{
					JSONArray xContent=null;
					JSONArray content=null;
					JSONObject oData = oJsonData.getJSONObject("data");
					if(oData == null)
					{
						MainActivity.MessageBox("��ȡ��ӡ�б�","��ȡ��ӡ�б�ʧ�ܣ����������Ƿ�ͨ������ϵ����Ա��");
						MainActivity.onUserMessageBox("��ȡ��ӡ�б�","��ȡ��ӡ�б�ʧ�ܣ����������Ƿ�ͨ������ϵ����Ա��");
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
							map.put("userCaption", GUtilHttp.getJSONObjectValue("userName",o) );
							content = o.getJSONArray("content");
							List<Map> ar1 = new ArrayList<Map>();
							for(int j=0;j<content.length();j++) {
								JSONObject oo = content.getJSONObject(j);
								Map map1 = new HashMap();
								map1.put("goodsCaption", GUtilHttp.getJSONObjectValue("goodsCaption", oo));
								if (map1.get("goodsCaption").toString().length() > 0) {
									map1.put("goodsCaption", GUtilHttp.getJSONObjectValue("goodsCaption", oo));
									map1.put("goodsNumber", GUtilHttp.getJSONObjectValue("goodsNumber", oo));
									map1.put("goodsUnitName", GUtilHttp.getJSONObjectValue("goodsUnitName", oo));
									map1.put("tasteuids", GUtilHttp.getJSONObjectValue("tasteuids", oo));
									ar1.add(map1);
								}
							}
							map.put("ar1",ar1);
							map.put("taskUID", GUtilHttp.getJSONObjectValue("taskUID",o) );
							ar.add(map);
						}
					}

					//####################################################
					m_oAdapter.setData(ar);
					m_oAdapter.notifyDataSetChanged();

					Message msg =Message.obtain();
					msg.what= 1;
					mHandler.sendMessage(msg);

				}

				catch (JSONException e)
				{
					e.printStackTrace();
					MainActivity.MessageBox("��ȡ��ӡ�б�",e.getMessage());
					MainActivity.onUserMessageBox("��ȡ��ӡ�б�","��ȡ��ӡ�б�ʧ�ܣ����������Ƿ�ͨ������ϵ����Ա��");
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
			MainActivity.MessageBox("����ȡ��ӡ�б�",e.getMessage());
			MainActivity.onUserMessageBox("��ȡ��ӡ�б�","��ȡ��ӡ�б�ʧ�ܣ����������Ƿ�ͨ������ϵ����Ա��");
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

	public void getWareHouseFillPrintDetail_Search(Integer page,String roomNumber,String orderNumber,String dateTime)
	{
		HD_page=page;
		GSvrChannel svr= 	new GSvrChannel()
		{
			public void onNetFailure(int statusCode,String strInfo)
			{

				MainActivity.MessageBox("��ȡ��ӡ�б�","statusCode:"+statusCode+",Info:"+strInfo);
				MainActivity.onUserMessageBox("��ȡ��ӡ�б�", "��ȡ��ӡ�б�ʧ�ܣ����������Ƿ�ͨ������ϵ����Ա��");
			}
			public void onNetSuccess(int nCode,String strInfo,JSONObject oJsonData)
			{

				if(nCode <=0)
				{
					MainActivity.MessageBox("��ȡ��ӡ�б�",strInfo);
					MainActivity.onUserMessageBox("��ȡ��ӡ�б�","���޲�ѯ��¼��");
					return;
				}

				try
				{
					JSONArray xContent=null;
					JSONArray content=null;
					JSONObject oData = oJsonData.getJSONObject("data");
					if(oData == null)
					{
						MainActivity.MessageBox("��ȡ��ӡ�б�","��ȡ��ӡ�б�ʧ�ܣ����������Ƿ�ͨ������ϵ����Ա��");
						MainActivity.onUserMessageBox("��ȡ��ӡ�б�","��ȡ��ӡ�б�ʧ�ܣ����������Ƿ�ͨ������ϵ����Ա��");
						return;
					}
					try{xContent = oData.getJSONArray("content");
						totalPages=Integer.parseInt(oData.getString("totalPages"));
					}catch(JSONException e){xContent=null;}
					if(xContent== null)
						return;
					List ar;
					if(HD_page==1){
						ar= new ArrayList();
					}else {
						ar= m_oAdapter.getData();
					}


					for(int i=0;i<xContent.length();i++)
					{

						JSONObject o = xContent.getJSONObject(i);

						Map map = new HashMap();

						map.put("orderUID", GUtilHttp.getJSONObjectValue("orderUID",o));
						map.put("totalPages", totalPages);
						if(map.get("orderUID").toString().length()>0)
						{

							map.put("orderUID",GUtilHttp.getJSONObjectValue("orderUID",o) );
							map.put("roomSerialNo", GUtilHttp.getJSONObjectValue("roomSerialNo",o) );
							map.put("cardUID", GUtilHttp.getJSONObjectValue("cardUID",o) );
							map.put("orderTime",GUtilHttp.getJSONObjectValue("orderTime",o) );
							map.put("userCaption", GUtilHttp.getJSONObjectValue("userName",o) );
							content = o.getJSONArray("content");
							List<Map> ar1 = new ArrayList<Map>();
							for(int j=0;j<content.length();j++) {
								JSONObject oo = content.getJSONObject(j);
								Map map1 = new HashMap();
								map1.put("goodsCaption", GUtilHttp.getJSONObjectValue("goodsCaption", oo));
								if (map1.get("goodsCaption").toString().length() > 0) {
									map1.put("goodsCaption", GUtilHttp.getJSONObjectValue("goodsCaption", oo));
									map1.put("goodsNumber", GUtilHttp.getJSONObjectValue("goodsNumber", oo));
									map1.put("goodsUnitName", GUtilHttp.getJSONObjectValue("goodsUnitName", oo));
									ar1.add(map1);
								}
							}
							map.put("ar1",ar1);
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
					MainActivity.MessageBox("��ȡ��ӡ�б�",e.getMessage());
					MainActivity.onUserMessageBox("��ȡ��ӡ�б�","��ȡ��ӡ�б�ʧ�ܣ����������Ƿ�ͨ������ϵ����Ա��");
					return;
				}

			}
		};
		try
		{
			JSONObject   requestDatas = new JSONObject();
			requestDatas.put("groupUID", GOperaterInfo.m_strGroupUID);
			requestDatas.put("isPrinted", 1);
			requestDatas.put("pageIndex", HD_page);
			requestDatas.put("roomNo", roomNumber);
			requestDatas.put("cardNo", orderNumber);
			requestDatas.put("dayTime", dateTime);
			svr.m_oCurrentActivity = m_oActivity;
			svr.onPost("api/mobile/opPrnTaskPage.do", requestDatas);
		}
		catch (JSONException e)
		{
			e.printStackTrace();
			MainActivity.MessageBox("����ȡ��ӡ�б�",e.getMessage());
			MainActivity.onUserMessageBox("��ȡ��ӡ�б�","��ȡ��ӡ�б�ʧ�ܣ����������Ƿ�ͨ������ϵ����Ա��");
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	public void getWareHouseGoodsStatistics_Search(String dateTime,String dateTiem1)
	{

		GSvrChannel svr= 	new GSvrChannel()
		{
			public void onNetFailure(int statusCode,String strInfo)
			{

				MainActivity.MessageBox("��ȡ��ӡ�б�","statusCode:"+statusCode+",Info:"+strInfo);
				MainActivity.onUserMessageBox("��ȡ��ӡ�б�", "��ȡ��ӡ�б�ʧ�ܣ����������Ƿ�ͨ������ϵ����Ա��");
			}
			public void onNetSuccess(int nCode,String strInfo,JSONObject oJsonData)
			{

				if(nCode <=0)
				{
                    List ar=new ArrayList();
					MainActivity.MessageBox("��ȡ��ӡ�б�",strInfo);
					MainActivity.onUserMessageBox("��ȡ��ӡ�б�","���޲�ѯ��¼��");
                    m_oAdapter.setData(ar);
                    m_oAdapter.notifyDataSetChanged();
					return;
				}

				try
				{
					JSONArray xContent=null;
					JSONObject oData = oJsonData.getJSONObject("data");
					if(oData == null)
					{
						MainActivity.MessageBox("��ȡ��ӡ�б�","��ȡ��ӡ�б�ʧ�ܣ����������Ƿ�ͨ������ϵ����Ա��");
						MainActivity.onUserMessageBox("��ȡ��ӡ�б�","��ȡ��ӡ�б�ʧ�ܣ����������Ƿ�ͨ������ϵ����Ա��");
						return;
					}
					try{xContent = oData.getJSONArray("content");
						totalPages=Integer.parseInt(oData.getString("totalPages"));
					}catch(JSONException e){xContent=null;}
					if(xContent== null)
						return;
					List ar=new ArrayList();



					for(int i=0;i<xContent.length();i++)
					{
						JSONObject o = xContent.getJSONObject(i);
						Map map = new HashMap();
						map.put("goodsCaption", GUtilHttp.getJSONObjectValue("goodsCaption",o));
						if(map.get("goodsCaption").toString().length()>0)
						{
							map.put("goodsCaption", GUtilHttp.getJSONObjectValue("goodsCaption", o));
							map.put("goodsNumber", GUtilHttp.getJSONObjectValue("goodsNumber", o));
							map.put("goodsUnitName", GUtilHttp.getJSONObjectValue("goodsUnitName", o));
							map.put("tasteuids", GUtilHttp.getJSONObjectValue("tasteuids", o));
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
					MainActivity.MessageBox("��ȡ��ӡ�б�",e.getMessage());
					MainActivity.onUserMessageBox("��ȡ��ӡ�б�","��ȡ��ӡ�б�ʧ�ܣ����������Ƿ�ͨ������ϵ����Ա��");
					return;
				}

			}
		};
		try
		{
			JSONObject   requestDatas = new JSONObject();
			requestDatas.put("groupUID", GOperaterInfo.m_strGroupUID);
			requestDatas.put("dayTime0", dateTime);
			requestDatas.put("dayTime1", dateTiem1);
			svr.m_oCurrentActivity = m_oActivity;
			svr.onPost("api/mobile/opGoodsStatisticsPage.do", requestDatas);
		}
		catch (JSONException e)
		{
			e.printStackTrace();
			MainActivity.MessageBox("����ȡ��ӡ�б�",e.getMessage());
			MainActivity.onUserMessageBox("��ȡ��ӡ�б�","��ȡ��ӡ�б�ʧ�ܣ����������Ƿ�ͨ������ϵ����Ա��");
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
    public void getCashierSalesReport_Search(String dateTime,String dateTiem1)
    {

        GSvrChannel svr= 	new GSvrChannel()
        {
            public void onNetFailure(int statusCode,String strInfo)
            {

                MainActivity.MessageBox("��ȡ��ӡ�б�","statusCode:"+statusCode+",Info:"+strInfo);
                MainActivity.onUserMessageBox("��ȡ��ӡ�б�", "��ȡ��ӡ�б�ʧ�ܣ����������Ƿ�ͨ������ϵ����Ա��");
            }
            public void onNetSuccess(int nCode,String strInfo,JSONObject oJsonData)
            {

                if(nCode <=0)
                {
                    List ar=new ArrayList();
                    MainActivity.MessageBox("��ȡ��ӡ�б�",strInfo);
                    MainActivity.onUserMessageBox("��ȡ��ӡ�б�","���޲�ѯ��¼��");
                    m_oAdapter.setData(ar);
                    m_oAdapter.notifyDataSetChanged();
                    return;
                }

                try
                {
                    JSONArray xContent=null;
                    JSONObject oData = oJsonData.getJSONObject("data");
                    if(oData == null)
                    {
                        MainActivity.MessageBox("��ȡ��ӡ�б�","��ȡ��ӡ�б�ʧ�ܣ����������Ƿ�ͨ������ϵ����Ա��");
                        MainActivity.onUserMessageBox("��ȡ��ӡ�б�","��ȡ��ӡ�б�ʧ�ܣ����������Ƿ�ͨ������ϵ����Ա��");
                        return;
                    }
                    try{xContent = oData.getJSONArray("content");

                    }catch(JSONException e){xContent=null;}
                    if(xContent== null)
                        return;
                    List ar=new ArrayList();



                    for(int i=0;i<xContent.length();i++)
                    {
                        JSONObject o = xContent.getJSONObject(i);
                        Map map = new HashMap();
                        map.put("serialNo", GUtilHttp.getJSONObjectValue("serialNo",o));
                        if(map.get("serialNo").toString().length()>0)
                        {
                            map.put("serialNo", GUtilHttp.getJSONObjectValue("serialNo", o));
                            map.put("buyTime", GUtilHttp.getJSONObjectValue("buyTime", o));
                            map.put("caption", GUtilHttp.getJSONObjectValue("caption", o));
                            map.put("realMoney", GUtilHttp.getJSONObjectValue("realMoney", o));
                            ar.add(map);
                        }
                    }
                    //####################################################
                    m_oAdapter.setData(ar);
                    m_oAdapter.notifyDataSetChanged();
                    Message msg =Message.obtain();
                    msg.what= 2;
                    mHandler_GCashier.sendMessage(msg);


                }

                catch (JSONException e)
                {
                    e.printStackTrace();
                    MainActivity.MessageBox("��ȡ��ӡ�б�",e.getMessage());
                    MainActivity.onUserMessageBox("��ȡ��ӡ�б�","��ȡ��ӡ�б�ʧ�ܣ����������Ƿ�ͨ������ϵ����Ա��");
                    return;
                }

            }
        };
        try
        {
            JSONObject   requestDatas = new JSONObject();
            requestDatas.put("groupUID", GOperaterInfo.m_strGroupUID);
            requestDatas.put("dayTime0", dateTime);
            requestDatas.put("dayTime1", dateTiem1);
            svr.m_oCurrentActivity = m_oActivity;
            svr.onPost("api/mobile/opSalesReport.do", requestDatas);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
            MainActivity.MessageBox("����ȡ��ӡ�б�",e.getMessage());
            MainActivity.onUserMessageBox("��ȡ��ӡ�б�","��ȡ��ӡ�б�ʧ�ܣ����������Ƿ�ͨ������ϵ����Ա��");
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public void getWareHouseGoodsStatisticsDetail_Search(Integer page,String dateTime)
    {
        HD_page=page;
        GSvrChannel svr= 	new GSvrChannel()
        {
            public void onNetFailure(int statusCode,String strInfo)
            {

                MainActivity.MessageBox("��ȡ��ӡ�б�","statusCode:"+statusCode+",Info:"+strInfo);
                MainActivity.onUserMessageBox("��ȡ��ӡ�б�", "��ȡ��ӡ�б�ʧ�ܣ����������Ƿ�ͨ������ϵ����Ա��");
            }
            public void onNetSuccess(int nCode,String strInfo,JSONObject oJsonData)
            {

                if(nCode <=0)
                {
                    List ar=new ArrayList();
                    MainActivity.MessageBox("��ȡ��ӡ�б�",strInfo);
                    MainActivity.onUserMessageBox("��ȡ��ӡ�б�","���޲�ѯ��¼��");
                    m_oAdapter.setData(ar);
                    m_oAdapter.notifyDataSetChanged();

                    return;
                }

                try
                {
                    JSONArray xContent=null;
                    JSONObject oData = oJsonData.getJSONObject("data");
                    if(oData == null)
                    {
                        MainActivity.MessageBox("��ȡ��ӡ�б�","��ȡ��ӡ�б�ʧ�ܣ����������Ƿ�ͨ������ϵ����Ա��");
                        MainActivity.onUserMessageBox("��ȡ��ӡ�б�","��ȡ��ӡ�б�ʧ�ܣ����������Ƿ�ͨ������ϵ����Ա��");
                        return;
                    }
                    try{xContent = oData.getJSONArray("content");
                        totalPages=Integer.parseInt(oData.getString("totalPages"));
                    }catch(JSONException e){xContent=null;}
                    if(xContent== null)
                        return;
                    List ar;
                    if(HD_page==1){
                        ar= new ArrayList();
                    }else {
                        ar= m_oAdapter.getData();
                    }


                    for(int i=0;i<xContent.length();i++)
                    {
                        JSONObject o = xContent.getJSONObject(i);
                        Map map = new HashMap();
                        map.put("totalPages", totalPages);
                        map.put("goodsCaption", GUtilHttp.getJSONObjectValue("goodsCaption",o));
                        if(map.get("goodsCaption").toString().length()>0)
                        {
                            map.put("goodsCaption", GUtilHttp.getJSONObjectValue("goodsCaption", o));
                            map.put("goodsNumber", GUtilHttp.getJSONObjectValue("goodsNumber", o));
                            map.put("goodsUnitName", GUtilHttp.getJSONObjectValue("goodsUnitName", o));
                            map.put("roomSerialNo", GUtilHttp.getJSONObjectValue("roomSerialNo",o) );
                            map.put("cardUID", GUtilHttp.getJSONObjectValue("cardUID",o) );
                            map.put("orderTime",GUtilHttp.getJSONObjectValue("orderTime",o) );
                            map.put("userCaption", GUtilHttp.getJSONObjectValue("userCaption",o) );
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
                    MainActivity.MessageBox("��ȡ��ӡ�б�",e.getMessage());
                    MainActivity.onUserMessageBox("��ȡ��ӡ�б�","��ȡ��ӡ�б�ʧ�ܣ����������Ƿ�ͨ������ϵ����Ա��");
                    return;
                }

            }
        };
        try
        {
            JSONObject   requestDatas = new JSONObject();
            requestDatas.put("groupUID", GOperaterInfo.m_strGroupUID);
            requestDatas.put("pageIndex", HD_page);
            requestDatas.put("dayTime", dateTime);
            svr.m_oCurrentActivity = m_oActivity;
            svr.onPost("api/mobile/opGoodsStatisticsDetailPage.do", requestDatas);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
            MainActivity.MessageBox("����ȡ��ӡ�б�",e.getMessage());
            MainActivity.onUserMessageBox("��ȡ��ӡ�б�","��ȡ��ӡ�б�ʧ�ܣ����������Ƿ�ͨ������ϵ����Ա��");
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
            MainActivity.MessageBox("��ӡ����","statusCode:"+statusCode+",Info:"+strInfo);
            MainActivity.onUserMessageBox("��ӡ����", "��ӡ����ʧ�ܣ����������Ƿ�ͨ������ϵ����Ա��");
        }
        public void onNetSuccess(int nCode,String strInfo,JSONObject oJsonData)
        {
            if(nCode < 0)
            {
                MainActivity.MessageBox("��ӡ����",strInfo);
                MainActivity.onUserMessageBox("��ӡ����",strInfo);
                return;
            }

            try
            {
                JSONObject oData = oJsonData.getJSONObject("data");
                if(oData == null)
                {
                    MainActivity.MessageBox("��ӡ����","��ӡ����ʧ�ܣ����������Ƿ�ͨ������ϵ����Ա��");
                    MainActivity.onUserMessageBox("��ӡ����","��ӡ����ʧ�ܣ����������Ƿ�ͨ������ϵ����Ա��");
                    return;
                }
//						if(GUtilHttp.getJSONObjectValue("taskUID",oData)!=null){
//							m_oAdapter.deletePrintTaskItem(GUtilHttp.getJSONObjectValue("taskUID",oData));
//							//m_oAdapter.notifyDataSetChanged();
//							MainActivity.m_oMsgHandler.sendEmptyMessage(MainActivity.USER_REFRESH_MAINFACE);
//						}
                m_oAdapter.deletePrintTaskItem(GUtilHttp.getJSONObjectValue("taskUID",oData));
                m_oAdapter.notifyDataSetChanged();


                Message msg =Message.obtain();
                msg.what= 1;
                mHandler.sendMessage(msg);

            }
            catch (JSONException e)
            {
                e.printStackTrace();
                MainActivity.MessageBox("��ӡ����",e.getMessage());
                MainActivity.onUserMessageBox("��ӡ����","��ӡ����ʧ�ܣ����������Ƿ�ͨ������ϵ����Ա��");
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
        MainActivity.MessageBox("��ӡ����",e.getMessage());
        MainActivity.onUserMessageBox("��ӡ����","��ӡ����ʧ�ܣ����������Ƿ�ͨ������ϵ����Ա��");
    }
}

	public void opPrnTaskCompleteAgain(String taskUID)
	{
		GSvrChannel svr= 	new GSvrChannel()
		{
			public void onNetFailure(int statusCode,String strInfo)
			{
				MainActivity.MessageBox("��ӡ����","statusCode:"+statusCode+",Info:"+strInfo);
				MainActivity.onUserMessageBox("��ӡ����", "��ӡ����ʧ�ܣ����������Ƿ�ͨ������ϵ����Ա��");
			}
			public void onNetSuccess(int nCode,String strInfo,JSONObject oJsonData)
			{
				if(nCode < 0)
				{
					MainActivity.MessageBox("��ӡ����",strInfo);
					MainActivity.onUserMessageBox("��ӡ����",strInfo);
					return;
				}

				try
				{
					JSONObject oData = oJsonData.getJSONObject("data");
					if(oData == null)
					{
						MainActivity.MessageBox("��ӡ����","��ӡ����ʧ�ܣ����������Ƿ�ͨ������ϵ����Ա��");
						MainActivity.onUserMessageBox("��ӡ����","��ӡ����ʧ�ܣ����������Ƿ�ͨ������ϵ����Ա��");
						return;
					}






				}
				catch (JSONException e)
				{
					e.printStackTrace();
					MainActivity.MessageBox("��ӡ����",e.getMessage());
					MainActivity.onUserMessageBox("��ӡ����","��ӡ����ʧ�ܣ����������Ƿ�ͨ������ϵ����Ա��");
					return;
				}
			}
		};
		try
		{
			JSONObject   requestDatas = new JSONObject();

			requestDatas.put("taskUID", taskUID);
			svr.m_oCurrentActivity = m_oActivity;
			svr.onPost("api/mobile/opPrnTaskCompleteAgain.do", requestDatas);
		}
		catch (JSONException e)
		{
			e.printStackTrace();
			MainActivity.MessageBox("��ӡ����",e.getMessage());
			MainActivity.onUserMessageBox("��ӡ����","��ӡ����ʧ�ܣ����������Ƿ�ͨ������ϵ����Ա��");
		}
	}
}