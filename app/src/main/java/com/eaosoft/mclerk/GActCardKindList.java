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
 * ListView 和 其它能触发点击事件的widget无法一起正常工作的原因是加入其它widget后，ListView的itemclick事件将无法触发，被其它widget的click事件屏蔽。
解决办法：
在包含button的Layout中加入属性 android:descendantFocusability= "blocksDescendants"
在buttion的属性加入android:focusable="false"
问题解决，两个click事件不再冲突了！
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
         * 设置为横屏
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
	    			MainActivity.MessageBox("套餐禁用／启用","statusCode:"+statusCode+",Info:"+strInfo);
	    			MainActivity.onUserMessageBox("套餐禁用／启用", "套餐禁用／启用失败，请检查网络是否畅通或者联系管理员！");
	    		}
	    		public void onNetSuccess(int nCode,String strInfo,JSONObject oJsonData)
	    		{
	    			if(nCode < 0)
	    			{
	    				MainActivity.MessageBox("套餐禁用／启用",strInfo);    				
	    				MainActivity.onUserMessageBox("套餐禁用／启用",strInfo);
	    				return;
	    			}
	    			
	    			try 
	    			{
	    				JSONObject oData = oJsonData.getJSONObject("data");
						if(oData == null)
						{
							MainActivity.MessageBox("套餐禁用／启用","套餐禁用／启用失败，请检查网络是否畅通或者联系管理员！");
							MainActivity.onUserMessageBox("套餐禁用／启用","套餐禁用／启用失败，请检查网络是否畅通或者联系管理员！");
		    				return;
						}
						String strUID =GUtilHttp.getJSONObjectValue("uID",oData);
						m_oCurrentOPMap.put("enabled", GUtilHttp.getJSONObjectValue("enabled",oData));  
						
						if(m_oCurrentOPMap.get("enabled").toString().equalsIgnoreCase("0"))
						{
							strUID = "套餐【"+m_oCurrentOPMap.get("caption").toString()+"("+m_oCurrentOPMap.get("serialNo").toString()+")"+"】禁用成功";
							m_oCurrentOPBtn.setText("启用");
							m_oCurrentOPBtn.setTextColor(Color.rgb(0, 0, 0));							
						}
						 else
						 {
						    strUID = "套餐【"+m_oCurrentOPMap.get("caption").toString()+"("+m_oCurrentOPMap.get("serialNo").toString()+")"+"】启用成功";
						    m_oCurrentOPBtn.setText("禁用");
						    m_oCurrentOPBtn.setTextColor(Color.rgb(255, 0, 0));						    
						 }
						if(m_oCardKindListAdapter!=null)
							m_oCardKindListAdapter.notifyDataSetInvalidated();
						MainActivity.onUserMessageBox("套餐禁用／启用",strUID);
						m_oCurrentOPMap = null;
						
					} 
	    			catch (JSONException e) 
	    			{
						e.printStackTrace();
						MainActivity.MessageBox("套餐禁用／启用",e.getMessage());
						MainActivity.onUserMessageBox("套餐禁用／启用","套餐禁用／启用失败，请检查网络是否畅通或者联系管理员！");
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
	             MainActivity.MessageBox("套餐禁用／启用",e.getMessage());
	             MainActivity.onUserMessageBox("套餐禁用／启用","套餐禁用／启用失败，请检查网络是否畅通或者联系管理员！");
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
			  strCaption = "您确认要禁用套餐\r\n【"+m_oCurrentOPMap.get("caption").toString()+"("+m_oCurrentOPMap.get("serialNo").toString()+")"+"】";
		  else
			  strCaption = "您确认要启用套餐\r\n【"+m_oCurrentOPMap.get("caption").toString()+"("+m_oCurrentOPMap.get("serialNo").toString()+")"+"】";
		  
		  GUtilDialog.Confirm(GActCardKindList.this,
			"套餐禁用／启用确认",
			strCaption,
			"是，我确认",new DialogInterface.OnClickListener(){public void onClick(DialogInterface dialog,int which)
			{	
				onCardKindEnableConfirm();
			}},
		    "不要",new DialogInterface.OnClickListener(){public void onClick(DialogInterface dialog,int which){}}
			);	
	  }
	  private void onCardKindDeleteConfirm()
	  {
		  GSvrChannel svr= 	new GSvrChannel()
	    	{
	    		public void onNetFailure(int statusCode,String strInfo)
	    		{
	    			MainActivity.MessageBox("套餐删除","statusCode:"+statusCode+",Info:"+strInfo);
	    			MainActivity.onUserMessageBox("套餐删除", "套餐删除失败，请检查网络是否畅通或者联系管理员！");
	    		}
	    		public void onNetSuccess(int nCode,String strInfo,JSONObject oJsonData)
	    		{
	    			if(nCode < 0)
	    			{
	    				MainActivity.MessageBox("套餐删除",strInfo);    				
	    				MainActivity.onUserMessageBox("套餐删除",strInfo);
	    				return;
	    			}
	    			
	    			try 
	    			{
	    				JSONObject oData = oJsonData.getJSONObject("data");
						if(oData == null)
						{
							MainActivity.MessageBox("套餐删除","套餐删除失败，请检查网络是否畅通或者联系管理员！");
							MainActivity.onUserMessageBox("套餐删除","套餐删除失败，请检查网络是否畅通或者联系管理员！");
		    				return;
						}
						String strUID ="";
						
						strUID = "套餐【"+m_oCurrentOPMap.get("caption").toString()+"("+m_oCurrentOPMap.get("serialNo").toString()+")"+"】删除成功";						
						MainActivity.onUserMessageBox("套餐删除",strUID);
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
						MainActivity.MessageBox("套餐删除",e.getMessage());
						MainActivity.onUserMessageBox("套餐删除","套餐删除失败，请检查网络是否畅通或者联系管理员！");
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
	             MainActivity.MessageBox("套餐删除",e.getMessage());
	             MainActivity.onUserMessageBox("套餐删除","套餐删除失败，请检查网络是否畅通或者联系管理员！");
	         } 
	  }
	  public void onDeleteCardKind(View v)
	  {
		  m_oCurrentOPMap = (Map)v.getTag();
		  if(m_oCurrentOPMap == null)
			  return;
		  String strCaption = "";
		   strCaption = "您确认要删除套餐\r\n【"+m_oCurrentOPMap.get("caption").toString()+"("+m_oCurrentOPMap.get("serialNo").toString()+")"+"】";
		  
		  GUtilDialog.Confirm(GActCardKindList.this,
			  "套餐禁用／启用确认",
			  strCaption,
			  "是，我确认",new DialogInterface.OnClickListener(){public void onClick(DialogInterface dialog,int which)
			  {
				  onCardKindDeleteConfirm();
			  }},
			  "不要",new DialogInterface.OnClickListener(){public void onClick(DialogInterface dialog,int which){}}
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
		//条目点击事件
		oListView.setOnItemClickListener(lv_listener);
		//===========================================================================
		return oListView;
	}	
	private void OnCreateSearchBox()
	{
		///////////////////////////////////////////////////////////////////////////////////////////////////////////////
		//建立搜素框
		try
		{
			View iSearch =  findViewById(R.id.GReportTop);
			m_ivDeleteText = (ImageView) iSearch.findViewById(R.id.ivDeleteText);
			m_etSearch = (EditText) iSearch.findViewById(R.id.etSearch);
			m_oSearch = (Button) iSearch.findViewById(R.id.btnSearch);
			m_oCreate = (Button) iSearch.findViewById(R.id.btnCreateCardKind);
			m_oCreate.setVisibility(View.INVISIBLE);
			if(MainActivity.m_nOperaterUI==MainActivity.UI_OP_ROLE_CASHIER)
				m_oCreate.setVisibility(View.VISIBLE);//对于收银员是可见
			
			
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
			if(m_szUserMgr.equalsIgnoreCase("UserMgr"))//进入明细
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
			if(m_szUserMgr.equalsIgnoreCase("UserMgr"))//进入明细
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