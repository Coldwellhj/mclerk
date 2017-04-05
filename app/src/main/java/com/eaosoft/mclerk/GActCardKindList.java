package com.eaosoft.mclerk;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.eaosoft.adapter.GCardKindAdapter;
import com.eaosoft.adapter.GHttpDAO;
import com.eaosoft.util.ActivityCollector;
import com.eaosoft.util.GSvrChannel;
import com.eaosoft.util.GUtilDialog;
import com.eaosoft.util.GUtilHttp;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;
/*
 * ListView �� �����ܴ�������¼���widget�޷�һ������������ԭ���Ǽ�������widget��ListView��itemclick�¼����޷�������������widget��click�¼����Ρ�
����취��
�ڰ���button��Layout�м������� android:descendantFocusability= "blocksDescendants"
��buttion�����Լ���android:focusable="false"
������������click�¼����ٳ�ͻ�ˣ�
 * */
public class GActCardKindList extends  Activity 
{
	private GCardKindAdapter			m_oCardKindListAdapter=null;
	private GHttpDAO						m_oCardKindListDAO=null;
    private ImageView 						m_ivDeleteText;
	private EditText 							m_etSearch;
	private Button								m_oSearch=null;
	private Button								m_oCreate=null;
	private String								m_szUserText="";
	private String								m_szUserMgr="";	
	private	 Map								m_oCurrentOPMap=null;
	private Button								m_oCurrentOPBtn=null;
	protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        OnReadUserParameter();
		setContentView(R.layout.act_card_kind_list);
		ActivityCollector.addActivity(this);		
		OnCreateSearchBox();
		OnCreateReportBody();
		setResult(RESULT_OK, null);
		
    }

	private void OnReadUserParameter()
   	{
   		Bundle bundle = this.getIntent().getExtras();
           
           if(bundle == null)
           	return;
           if(bundle.getString("UserMgr")!=null)
        	   m_szUserMgr = bundle.getString("UserMgr");                
       }
    @Override
    protected void onResume() {
        /**
         * ����Ϊ����
         */
            if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        super.onResume();
    }
  @Override
	protected void onDestroy() 
    {
		super.onDestroy();
		ActivityCollector.removeActivity(this);
	}
	  private void onCardKindEnableConfirm()
	  {
		  	GSvrChannel svr= 	new GSvrChannel()
	    	{
	    		public void onNetFailure(int statusCode,String strInfo)
	    		{
	    			MainActivity.MessageBox("�ײͽ��ã�����","statusCode:"+statusCode+",Info:"+strInfo);
	    			MainActivity.onUserMessageBox("�ײͽ��ã�����", "�ײͽ��ã�����ʧ�ܣ����������Ƿ�ͨ������ϵ����Ա��");
	    		}
	    		public void onNetSuccess(int nCode,String strInfo,JSONObject oJsonData)
	    		{
	    			if(nCode < 0)
	    			{
	    				MainActivity.MessageBox("�ײͽ��ã�����",strInfo);    				
	    				MainActivity.onUserMessageBox("�ײͽ��ã�����",strInfo);
	    				return;
	    			}
	    			
	    			try 
	    			{
	    				JSONObject oData = oJsonData.getJSONObject("data");
						if(oData == null)
						{
							MainActivity.MessageBox("�ײͽ��ã�����","�ײͽ��ã�����ʧ�ܣ����������Ƿ�ͨ������ϵ����Ա��");
							MainActivity.onUserMessageBox("�ײͽ��ã�����","�ײͽ��ã�����ʧ�ܣ����������Ƿ�ͨ������ϵ����Ա��");
		    				return;
						}
						String strUID =GUtilHttp.getJSONObjectValue("uID",oData);
						m_oCurrentOPMap.put("enabled", GUtilHttp.getJSONObjectValue("enabled",oData));  
						
						if(m_oCurrentOPMap.get("enabled").toString().equalsIgnoreCase("0"))
						{
							strUID = "�ײ͡�"+m_oCurrentOPMap.get("caption").toString()+"("+m_oCurrentOPMap.get("serialNo").toString()+")"+"�����óɹ�";
							m_oCurrentOPBtn.setText("����");
							m_oCurrentOPBtn.setTextColor(Color.rgb(0, 0, 0));							
						}
						 else
						 {
						    strUID = "�ײ͡�"+m_oCurrentOPMap.get("caption").toString()+"("+m_oCurrentOPMap.get("serialNo").toString()+")"+"�����óɹ�";
						    m_oCurrentOPBtn.setText("����");
						    m_oCurrentOPBtn.setTextColor(Color.rgb(255, 0, 0));						    
						 }
						if(m_oCardKindListAdapter!=null)
							m_oCardKindListAdapter.notifyDataSetInvalidated();
						MainActivity.onUserMessageBox("�ײͽ��ã�����",strUID);
						m_oCurrentOPMap = null;
						
					} 
	    			catch (JSONException e) 
	    			{
						e.printStackTrace();
						MainActivity.MessageBox("�ײͽ��ã�����",e.getMessage());
						MainActivity.onUserMessageBox("�ײͽ��ã�����","�ײͽ��ã�����ʧ�ܣ����������Ƿ�ͨ������ϵ����Ա��");
						return;
					}
	    		}
	    	};
	    	 try 
	         {
		    	JSONObject   requestDatas = new JSONObject();
		    	requestDatas.put("cardKindUID",m_oCurrentOPMap.get("uID").toString());	 
		    	if(m_oCurrentOPMap.get("enabled").toString().equalsIgnoreCase("1"))
		    		requestDatas.put("enabled","0");
		    	else
		    		requestDatas.put("enabled","1");
		    	svr.m_oCurrentActivity = this;
		    	svr.onPost("api/mobile/opCardKindEnable.do", requestDatas);
	         }
	    	 catch (JSONException e) 
	         {
	             e.printStackTrace();
	             MainActivity.MessageBox("�ײͽ��ã�����",e.getMessage());
	             MainActivity.onUserMessageBox("�ײͽ��ã�����","�ײͽ��ã�����ʧ�ܣ����������Ƿ�ͨ������ϵ����Ա��");
	         } 
	  }
	  public void onEnableCardKind(View v)
	  {
		  m_oCurrentOPMap = (Map)v.getTag();
		  if(m_oCurrentOPMap == null)
			  return;
		  m_oCurrentOPBtn = (Button)v;
		  String strCaption = "";
		  if(m_oCurrentOPMap.get("enabled").toString().equalsIgnoreCase("1"))
			  strCaption = "��ȷ��Ҫ�����ײ�\r\n��"+m_oCurrentOPMap.get("caption").toString()+"("+m_oCurrentOPMap.get("serialNo").toString()+")"+"��";
		  else
			  strCaption = "��ȷ��Ҫ�����ײ�\r\n��"+m_oCurrentOPMap.get("caption").toString()+"("+m_oCurrentOPMap.get("serialNo").toString()+")"+"��";
		  
		  GUtilDialog.Confirm(GActCardKindList.this,
			"�ײͽ��ã�����ȷ��",
			strCaption,
			"�ǣ���ȷ��",new DialogInterface.OnClickListener(){public void onClick(DialogInterface dialog,int which)
			{	
				onCardKindEnableConfirm();
			}},
		    "��Ҫ",new DialogInterface.OnClickListener(){public void onClick(DialogInterface dialog,int which){}}
			);	
	  }
	  private void onCardKindDeleteConfirm()
	  {
		  GSvrChannel svr= 	new GSvrChannel()
	    	{
	    		public void onNetFailure(int statusCode,String strInfo)
	    		{
	    			MainActivity.MessageBox("�ײ�ɾ��","statusCode:"+statusCode+",Info:"+strInfo);
	    			MainActivity.onUserMessageBox("�ײ�ɾ��", "�ײ�ɾ��ʧ�ܣ����������Ƿ�ͨ������ϵ����Ա��");
	    		}
	    		public void onNetSuccess(int nCode,String strInfo,JSONObject oJsonData)
	    		{
	    			if(nCode < 0)
	    			{
	    				MainActivity.MessageBox("�ײ�ɾ��",strInfo);    				
	    				MainActivity.onUserMessageBox("�ײ�ɾ��",strInfo);
	    				return;
	    			}
	    			
	    			try 
	    			{
	    				JSONObject oData = oJsonData.getJSONObject("data");
						if(oData == null)
						{
							MainActivity.MessageBox("�ײ�ɾ��","�ײ�ɾ��ʧ�ܣ����������Ƿ�ͨ������ϵ����Ա��");
							MainActivity.onUserMessageBox("�ײ�ɾ��","�ײ�ɾ��ʧ�ܣ����������Ƿ�ͨ������ϵ����Ա��");
		    				return;
						}
						String strUID ="";
						
						strUID = "�ײ͡�"+m_oCurrentOPMap.get("caption").toString()+"("+m_oCurrentOPMap.get("serialNo").toString()+")"+"��ɾ���ɹ�";						
						MainActivity.onUserMessageBox("�ײ�ɾ��",strUID);
						if(m_oCardKindListAdapter!=null)
						{
							m_oCardKindListAdapter.deleteItem(m_oCurrentOPMap.get("uID").toString());
							m_oCardKindListAdapter.notifyDataSetChanged();
							m_oCurrentOPMap = null;
						}
					} 
	    			catch (JSONException e) 
	    			{
						e.printStackTrace();
						MainActivity.MessageBox("�ײ�ɾ��",e.getMessage());
						MainActivity.onUserMessageBox("�ײ�ɾ��","�ײ�ɾ��ʧ�ܣ����������Ƿ�ͨ������ϵ����Ա��");
						return;
					}
	    		}
	    	};
	    	 try 
	         {
		    	JSONObject   requestDatas = new JSONObject();		    
	            requestDatas.put("cardKindUID",m_oCurrentOPMap.get("uID").toString());
	            svr.m_oCurrentActivity = this;
		    	svr.onPost("api/mobile/opCardKindDelete.do", requestDatas);
	         }
	    	 catch (JSONException e) 
	         {
	             e.printStackTrace();
	             MainActivity.MessageBox("�ײ�ɾ��",e.getMessage());
	             MainActivity.onUserMessageBox("�ײ�ɾ��","�ײ�ɾ��ʧ�ܣ����������Ƿ�ͨ������ϵ����Ա��");
	         } 
	  }
	  public void onDeleteCardKind(View v)
	  {
		  m_oCurrentOPMap = (Map)v.getTag();
		  if(m_oCurrentOPMap == null)
			  return;
		  String strCaption = "";
		   strCaption = "��ȷ��Ҫɾ���ײ�\r\n��"+m_oCurrentOPMap.get("caption").toString()+"("+m_oCurrentOPMap.get("serialNo").toString()+")"+"��";
		  
		  GUtilDialog.Confirm(GActCardKindList.this,
			  "�ײͽ��ã�����ȷ��",
			  strCaption,
			  "�ǣ���ȷ��",new DialogInterface.OnClickListener(){public void onClick(DialogInterface dialog,int which)
			  {
				  onCardKindDeleteConfirm();
			  }},
			  "��Ҫ",new DialogInterface.OnClickListener(){public void onClick(DialogInterface dialog,int which){}}
	  );
	  }
  	private void OnSearchItem(String strText)
  	{
  		if(m_oCardKindListAdapter==null)
  			m_oCardKindListAdapter = new GCardKindAdapter(this);
		if(m_oCardKindListDAO == null)
			m_oCardKindListDAO = new GHttpDAO(this,m_oCardKindListAdapter);
		if(m_oCardKindListAdapter.ar!=null)
			m_oCardKindListAdapter.ar.clear();
		m_oCardKindListDAO.getCardKindPage(strText, 1,200); 
  	}
  	private View OnCreateReportBody()
	{
		ListView oListView = (ListView)findViewById(R.id.lv_card_kind_list);
		
		OnSearchItem("");
		if(oListView == null)
			return null;
		//===========================================================================
		oListView.setAdapter(m_oCardKindListAdapter);
		//��Ŀ����¼�
		oListView.setOnItemClickListener(lv_listener);
		//===========================================================================
		return oListView;
	}	
	private void OnCreateSearchBox()
	{
		///////////////////////////////////////////////////////////////////////////////////////////////////////////////
		//�������ؿ�
		try
		{
			View iSearch =  findViewById(R.id.GReportTop);
			m_ivDeleteText = (ImageView) iSearch.findViewById(R.id.ivDeleteText);
			m_etSearch = (EditText) iSearch.findViewById(R.id.etSearch);
			m_oSearch = (Button) iSearch.findViewById(R.id.btnSearch);
			m_oCreate = (Button) iSearch.findViewById(R.id.btnCreateCardKind);
			m_oCreate.setVisibility(View.INVISIBLE);
			if(MainActivity.m_nOperaterUI==MainActivity.UI_OP_ROLE_CASHIER)
				m_oCreate.setVisibility(View.VISIBLE);//��������Ա�ǿɼ�
			
			
			m_ivDeleteText.setOnClickListener(new OnClickListener() 
			{
				public void onClick(View v) 
				{
					String szText = m_etSearch.getText().toString();
					m_etSearch.setText("");
					if(szText.length()>0)
					{
						m_szUserText="";
	
					}
				}	
			});
			m_oCreate.setOnClickListener(new OnClickListener()
			{
				public void onClick(View v) 
				{					
					Intent intent = new Intent(GActCardKindList.this, GActCardKindDetail.class);		
					startActivity(intent);
				}
			});
			m_oSearch.setOnClickListener(new OnClickListener() 
			{
				public void onClick(View v) 
				{
					String szText = m_etSearch.getText().toString();
	
					if(!m_szUserText.equalsIgnoreCase(szText))
					{
	
						m_szUserText = szText;
	
					}
					//OnLoadReport(m_szUserText);
					OnSearchItem(m_szUserText);
				}
			});
	
			m_etSearch.addTextChangedListener(new TextWatcher() 
			{
				public void onTextChanged(CharSequence s, int start, int before, int count) 
				{
	
				}
	
				public void beforeTextChanged(CharSequence s, int start, int count,int after) 
				{
	
				}
	
				public void afterTextChanged(Editable s) 
				{
					if (s.length() == 0) 
						m_ivDeleteText.setVisibility(View.GONE);
					else 
						m_ivDeleteText.setVisibility(View.VISIBLE);
				}
			});			
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	OnItemClickListener lv_listener = new OnItemClickListener() 
	{
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,	long arg3) 
		{
			if(m_oCardKindListAdapter == null)
				return;
			if(m_oCardKindListAdapter.ar==null)
				return;
			Map map = (Map) m_oCardKindListAdapter.ar.get(arg2);
			if(map == null)
				return;
			Intent intent=null;
			if(m_szUserMgr.equalsIgnoreCase("UserMgr"))//������ϸ
				intent = new Intent(GActCardKindList.this, GActCardKindDetail.class);
			else
				intent = new Intent(GActCardKindList.this, GActCardCreate.class);
			
			Bundle bundle=new Bundle();		    			
			bundle.putString("uID", map.get("uID").toString());
			bundle.putString("serialNo", map.get("serialNo").toString());
			bundle.putString("caption", map.get("caption").toString());
			bundle.putString("briefing", map.get("briefing").toString());
			bundle.putString("totalMoney", map.get("totalMoney").toString());
			bundle.putString("dateStart", map.get("dateStart").toString());
			bundle.putString("dateEnd", map.get("dateEnd").toString());
			bundle.putString("imgLogo", map.get("imgLogo").toString());
			bundle.putString("bkColor", map.get("bkColor").toString());
			bundle.putString("enabled", map.get("enabled").toString());
			intent.putExtras(bundle);		    			
			if(m_szUserMgr.equalsIgnoreCase("UserMgr"))//������ϸ
			{
				startActivity(intent);
			}
			else
			{
				setResult(1001, intent);
				overridePendingTransition(R.anim.left, R.anim.right);
				GActCardKindList.this.finish();
			}
			/*
			Intent intent = new Intent(ActProjectInfo.this, ActImageArchiveInfo.class);
			
			intent.putExtra("uID", map.get("uID").toString());
			intent.putExtra("serialNo", map.get("serialNo").toString());
			intent.putExtra("caption", map.get("caption").toString());
			intent.putExtra("briefing", map.get("briefing").toString());
			intent.putExtra("ioaThumbnail", map.get("ioaThumbnail").toString());
			intent.putExtra("ioaMainImage", map.get("ioaMainImage").toString());
			
			startActivity(intent);
			*/
		}
	};
  }