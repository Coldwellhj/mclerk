package com.eaosoft.mclerk;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Spinner;
import android.widget.TextView;

import com.eaosoft.adapter.GSpinnerAdapter;
import com.eaosoft.userinfo.GOperaterInfo;
import com.eaosoft.util.ActivityCollector;
import com.eaosoft.util.GSvrChannel;
import com.eaosoft.util.GUtilHttp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GActCardGoodsChange extends  Activity 
{
	//====================================================
	private String			m_strCardNo="";
	private String			m_strGoodsUID="";
	private String			m_strGoodsSerialNo="";
	private String			m_strGoodsCaption="";
	private String			m_strGoodsMinImage="";
	private String			m_strGoodsNorImage="";
	private String			m_strGoodsUnitName="";
	private int				m_nGoodsTotalNum=1;
	private int				m_nGoodsUserNum=0;
	private int				m_nfromNum0=0;
	private int				m_nFontSize=12;
	//====================================================
	private TextView 			m_txtHeaderCaption=null;
	private LinearLayout	m_oCardGoodsChangeList=null;
	private List					m_oCardGoodsList=new ArrayList();
	private List					m_oAdapterSpinner=new ArrayList();
	private List					m_oSpinner=new ArrayList();
	//====================================================
	protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.act_card_goods_change_list);
		ActivityCollector.addActivity(this);		
		OnReadUserParameter();
		OnInitWindow();
		
		
		setResult(RESULT_OK, null);		
		OnCreateCardChangeGoodsList(m_oCardGoodsChangeList);
		onGetGoodsChangeList();
    }
  @Override
	protected void onDestroy() 
    {
		super.onDestroy();
		ActivityCollector.removeActivity(this);
	}
  	public void onCardGoodsChangeConfirm(View v)
  	{
  		
  		Intent intent=new Intent(GActCardGoodsChange.this, GActCardCreateOrder.class);
		
		Bundle bundle=new Bundle();
		Spinner 				oSpinner=null;
		Map						map;
		String 					strKey="";
		int						m=0;
		int						nUserNum=0;
		int						nStepNum=0;
		for(int i=0;i<m_oCardGoodsList.size();i++)
		{
			
			map = (Map)m_oCardGoodsList.get(i);
			
			nUserNum = Integer.parseInt(map.get("numUser").toString());
			nStepNum = Integer.parseInt(map.get("numStep").toString());
			if(nUserNum<1)continue;
			strKey = "G"+m+"uID";bundle.putString(strKey, map.get("uID").toString());//TO GoodsUID
			strKey = "G"+m+"oID";bundle.putString(strKey, m_strGoodsUID);//FROM GoodsUID
			strKey = "G"+m+"caption";bundle.putString(strKey, map.get("caption").toString());
			strKey = "G"+m+"serialNo";bundle.putString(strKey, map.get("serialNo").toString());
			strKey = "G"+m+"unitName";bundle.putString(strKey, map.get("unitName").toString());
			strKey = "G"+m+"norImage";bundle.putString(strKey, map.get("norImage").toString());
			strKey = "G"+m+"minImage";bundle.putString(strKey, map.get("minImage").toString());
			strKey = "G"+m+"numStep";bundle.putInt(strKey, nStepNum);
			strKey = "G"+m+"numTotal";bundle.putInt(strKey, nUserNum*nStepNum);
			strKey = "G"+m+"numUser";bundle.putInt(strKey, nUserNum*Integer.parseInt(map.get("fromNum").toString()));
			m++;
		}
		bundle.putInt("GoodsCount",m);
		intent.putExtras(bundle);		    			
		setResult(RESULT_OK, intent);
		overridePendingTransition(R.anim.left, R.anim.right);
		GActCardGoodsChange.this.finish();
  	}
  	private void OnInitWindow()
  	{
  		m_txtHeaderCaption = (TextView)findViewById(R.id.txt_header_caption);
  		m_oCardGoodsChangeList= (LinearLayout)findViewById(R.id.ll_card_change_goods_list);
  		
  		String strCaption = m_strGoodsCaption+"可兑换量："+(m_nGoodsTotalNum-m_nGoodsUserNum)+m_strGoodsUnitName;
  		m_txtHeaderCaption.setText(strCaption);
  	}
   
  	private Spinner onCreateUserNumSpinner(Context	oContext,int nNumStep,int nMaxNum,String strUnitName)
  	{
  		Spinner mSpinnerA = new Spinner(oContext);
  		String[]  spinnerList = new String[nMaxNum+1];
  		
  		for(int i=0;i<=nMaxNum;i++)
  			spinnerList[i]=(""+(i*nNumStep));
  		GSpinnerAdapter myAdapter = new GSpinnerAdapter(oContext, android.R.layout.simple_spinner_item, spinnerList);  		
  		
  		m_oAdapterSpinner.add(myAdapter);
  		m_oSpinner.add(mSpinnerA);
  		mSpinnerA.setAdapter(myAdapter);
  		return mSpinnerA;
  	}
  	private void onSetUserGoodsChangeNum(String strGoodsUID,String strUserNum,int nLineNum)
  	{
  		GSpinnerAdapter	oAdapter=null;
  		Spinner 				oSpinner=null;
  		String					strVal="";
  		Map		 				map = null;
  		int 						i,m;
  		//int 						nUserNum,nNumStep;
  		//===========================================================
  		//已经兑换量
  		m_nGoodsUserNum = 0;
  		for(i=0;i<m_oSpinner.size();i++)
  		{
  			oSpinner = (Spinner)m_oSpinner.get(i);
  			m_nGoodsUserNum =m_nGoodsUserNum+oSpinner.getSelectedItemPosition();
  		}
  		if(m_nGoodsUserNum>m_nGoodsTotalNum)
  		{
  			oSpinner = (Spinner)m_oSpinner.get(nLineNum-1);
  			if(oSpinner != null)
  			{
  				int nUserNum =	oSpinner.getSelectedItemPosition();
  				m_nGoodsUserNum -= nUserNum;
  				nUserNum = m_nGoodsTotalNum - m_nGoodsUserNum;
  				if(nUserNum<0)
  					nUserNum = 0;
  				oSpinner.setSelection(nUserNum);
  				m_nGoodsUserNum = m_nGoodsTotalNum;
  			}
  		}
  		/*
  		//===========================================================
  		for(i=0;i<m_oAdapterSpinner.size();i++)
  		{
  			if(nLineNum==i)continue;
  			oSpinner = (Spinner)m_oSpinner.get(i);
  			map = (Map)oSpinner.getTag();
  			nNumStep = Integer.parseInt(map.get("numStep").toString());
  			//重新设定可以兑换量
  			oAdapter = (GSpinnerAdapter)m_oAdapterSpinner.get(i);
  			String [] oItems = new String[m_nGoodsTotalNum-nUserNum+1]; 
  			
  			for(m=0;m<=(m_nGoodsTotalNum-nUserNum);m++)  				
  				oItems[m] = (""+(m*nNumStep));
  			oAdapter.onSetDatas(oItems);
  			oAdapter.notifyDataSetChanged();
  		}
  		*/
  		//===========================================================  		
  		String strCaption = m_strGoodsCaption+"可兑换量："+(m_nGoodsTotalNum-m_nGoodsUserNum)+m_strGoodsUnitName;
  		m_txtHeaderCaption.setText(strCaption);
  	}
  	class SpinnerOnSelectedListener implements OnItemSelectedListener
  	{
        public void onItemSelected(AdapterView<?> adapterView, View view, int position,long id) 
        {
            // TODO Auto-generated method stub
            String selected = adapterView.getItemAtPosition(position).toString();
            System.out.println("selected===========>" + selected+","+adapterView.getTag().toString());
            onSetUserGoodsChangeNum(adapterView.getTag().toString(),selected,adapterView.getId()-4000);
            //m_oMsgHandler.sendEmptyMessage(1001);
        }

        public void onNothingSelected(AdapterView<?> arg0) {
            // TODO Auto-generated method stub
            System.out.println("selected===========>" + "Nothing");
        }
    }
  	private LinearLayout OnCreateCardChangeGoods(Context	oContext,Map map,int nLineNum)
  	{
  	//======================================================================================
  		LinearLayout oHead = new LinearLayout(oContext);  //线性布局方式  
  		oHead.setOrientation( LinearLayout.HORIZONTAL ); //控件对其方式为垂直排列  VERTICAL
  		//oHead.setBackgroundColor( 0xff00ffff );        //设置布局板的一个特殊颜色，这可以检验我们会话时候是否有地方颜色不正确！  
  		oHead.setLayoutParams( new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
  		
  		//======================================================================================
  		TextView txtName;
  		TextView txtUnit;
  		TextView txtNumR;
  		
  		LinearLayout.LayoutParams xCell =new  LinearLayout.LayoutParams(0, LayoutParams.WRAP_CONTENT);
  		xCell.weight=0.5f;
  		LinearLayout.LayoutParams pCell =new  LinearLayout.LayoutParams(0, LayoutParams.WRAP_CONTENT);
  		pCell.weight=1.0f;
  		pCell.setMargins(0, 5, 0,0);
  		
  		txtName = new TextView(oContext);
  		txtName.setLayoutParams(pCell);
  		if(map==null)
  			txtName.setText("名称");
  		else
  			txtName.setText(map.get("caption").toString());
  		txtName.setTextSize(m_nFontSize*MainActivity.m_nPixelsFromDP);
  		oHead.addView(txtName);
  		
  		

  		
  		if(map==null)
  		{
  			txtNumR = new TextView(oContext);
  			txtNumR.setLayoutParams(pCell);  			
  			txtNumR.setText("数量");
  			txtNumR.setTextSize(m_nFontSize*MainActivity.m_nPixelsFromDP);
  	  		oHead.addView(txtNumR);
  		}
  		else
  		{
  			/*
  			Spinner oSpinner;
  			int nMaxNum=0;
  			int nNumStep=0;
  			nMaxNum = Integer.parseInt(map.get("numTotal").toString());
  			nNumStep = Integer.parseInt(map.get("numStep").toString());
  			oSpinner = onCreateUserNumSpinner(oContext,nNumStep,m_nGoodsTotalNum,map.get("unitName").toString());
  			oSpinner.setLayoutParams(pCell);
  			oSpinner.setTag(map);
  			oSpinner.setId(4000+nLineNum);
  			oSpinner.setOnItemSelectedListener(new SpinnerOnSelectedListener());
  	  		oHead.addView(oSpinner);
  	  		*/
  			txtNumR = new TextView(oContext);
  			txtNumR.setLayoutParams(pCell);  			
  			txtNumR.setText(map.get("numUser").toString());
  			txtNumR.setTextSize(m_nFontSize*MainActivity.m_nPixelsFromDP);  	 
  			txtNumR.setId(5000+nLineNum);
  			txtNumR.setClickable(true);
  			txtNumR.setOnClickListener(m_oGoodsNumberChange);
  			txtNumR.setTag(map);
  			txtNumR.setBackgroundColor(Color.YELLOW);
  			txtNumR.setGravity(Gravity.CENTER);
  	  		oHead.addView(txtNumR);
  		}
  		
  		
  		
  		txtUnit 	= new TextView(oContext);
  		
  		txtUnit.setLayoutParams(xCell);
  		if(map==null)
  			txtUnit.setText("单位");
  		else
  			txtUnit.setText(map.get("unitName").toString());
  		txtUnit.setTextSize(m_nFontSize*MainActivity.m_nPixelsFromDP);
  		oHead.addView(txtUnit);
  		return oHead;
  	}
  	View.OnClickListener m_oGoodsNumberChange = new View.OnClickListener()
  	{
  		 public void onClick(View v) 
         {
  			 Map map = (Map)v.getTag();
  			 if(map == null)
  				 return;
  			
  			int nUserNum = Integer.parseInt(map.get("numUser").toString());//兑换量
  			int nStepNum = Integer.parseInt(map.get("numStep").toString());
  			
  			m_nGoodsUserNum -= nUserNum;
  			int nRemainNum = m_nGoodsTotalNum - m_nGoodsUserNum ;
  			map.put("numUser","0");
  			
  			final TextView txtGoodsNum = (TextView)v;
  			String strItems[] = new String[nRemainNum+1];
  			for(int i=0;i<(nRemainNum+1);i++)
  				strItems[i] = (i*nStepNum)+map.get("unitName").toString();
  			//=====================================================================
  			AlertDialog.Builder builder = new AlertDialog.Builder(GActCardGoodsChange.this);
  	        builder.setIcon(R.drawable.ic_launcher);
  	        builder.setTitle("选择兑换"+map.get("caption").toString()+"数量");
  	        builder.setItems(strItems, new DialogInterface.OnClickListener()
  	        {
  	            @Override
  	            public void onClick(DialogInterface dialog, int which)
  	            {
  	            	 Map map = (Map)txtGoodsNum.getTag();
  	            	 if(map == null)
  	    				 return;
  	            	int nStepNum = Integer.parseInt(map.get("numStep").toString());
  	            	String strUserNum = ""+(which*nStepNum);
  	            	m_nGoodsUserNum += which*Integer.parseInt(map.get("fromNum").toString());
  	            	txtGoodsNum.setText(strUserNum);  	            	
  	            	map.put("numUser",""+which);
  	            	
  	            	String strCaption = m_strGoodsCaption+"可兑换量："+(m_nGoodsTotalNum-m_nGoodsUserNum)+m_strGoodsUnitName;
  	        		m_txtHeaderCaption.setText(strCaption);
  	            }
  	        });
  	        builder.show();
         }
  	};
  	private void OnCreateCardChangeGoodsList(LinearLayout lv)
  	{
  		Context	oContext = lv.getContext();
  		Map			oMap=null;
  		lv.removeAllViews();
  		
  		LinearLayout oHead = OnCreateCardChangeGoods(oContext,null,0);
  		oHead.setBackgroundColor(Color.rgb(0,0xff, 0));
  		lv.addView(oHead);
  		//======================================================================================
  		for(int i=0;i<m_oCardGoodsList.size();i++)
  		{
  			oMap = (Map)m_oCardGoodsList.get(i);
  			oMap.put("UserNum", "0");
  			oMap.put("CardGoods", "1");
	  		LinearLayout oBody= OnCreateCardChangeGoods(oContext,oMap,i+1);
	  		lv.addView(oBody);
  		}
  		//======================================================================================
  	}
  	private void onGetGoodsChangeList()
  	{
  		GSvrChannel svr= 	new GSvrChannel()
    	{
    		public void onNetFailure(int statusCode,String strInfo)
    		{
    			MainActivity.MessageBox("兑换卡余量列表","statusCode:"+statusCode+",Info:"+strInfo);
    			MainActivity.onUserMessageBox("兑换卡余量列表", "兑换卡余量列表失败，请检查网络是否畅通或者联系管理员！");
    		}
    		public void onNetSuccess(int nCode,String strInfo,JSONObject oJsonData)
    		{
    			if(nCode < 0)
    			{
    				MainActivity.MessageBox("兑换卡余量列表",strInfo);    				
    				MainActivity.onUserMessageBox("兑换卡余量列表",strInfo);
    				return;
    			}
    			
    			try 
    			{
    				JSONArray xContent=null;
					JSONObject oData = oJsonData.getJSONObject("data");
					if(oData == null)
					{
						MainActivity.MessageBox("兑换卡余量列表","兑换卡余量列表失败，请检查网络是否畅通或者联系管理员！");
						MainActivity.onUserMessageBox("兑换卡余量列表","兑换卡余量列表失败，请检查网络是否畅通或者联系管理员！");
	    				return;
					}
					try{xContent = oData.getJSONArray("content");}catch(JSONException e){xContent=null;}
					if(xContent==null)
					{
						MainActivity.onUserMessageBox("兑换卡余量列表","兑换卡余量列表失败，请检查网络是否畅通或者联系管理员！");
						return;
					}
					if(xContent.length()<1)
					{
						MainActivity.onUserMessageBox("兑换卡余量列表",strInfo);
	    				return;
					}
					m_oCardGoodsList.clear();				
					for(int i=0;i<xContent.length();i++)
					{
						JSONObject o = xContent.getJSONObject(i);
						Map map = new HashMap();
						map.put("uID", GUtilHttp.getJSONObjectValue("uID",o));
						if(map.get("uID").toString().length()>0)
						{
							map.put("caption",GUtilHttp.getJSONObjectValue("caption",o) );						
							map.put("serialNo", GUtilHttp.getJSONObjectValue("serialNo",o) );							
							map.put("unitName", GUtilHttp.getJSONObjectValue("unitName",o) );
							map.put("numStep", GUtilHttp.getJSONObjectValue("numStep",o) );
							map.put("numTotal", GUtilHttp.getJSONObjectValue("numTotal",o) );
							map.put("norImage", GUtilHttp.getJSONObjectValue("norImage",o) );
							map.put("minImage", GUtilHttp.getJSONObjectValue("minImage",o) );
							map.put("fromNum", GUtilHttp.getJSONObjectValue("fromNum",o) );
							map.put("numUser", "0");//当前兑换量
							m_oCardGoodsList.add(map);
						}
					}
					OnCreateCardChangeGoodsList(m_oCardGoodsChangeList);
				} 
    			catch (JSONException e) 
    			{
					e.printStackTrace();
					MainActivity.MessageBox("兑换卡余量列表",e.getMessage());
					MainActivity.onUserMessageBox("兑换卡余量列表","兑换卡余量列表失败，请检查网络是否畅通或者联系管理员！");
					return;
				}
    		}
    	};
    	 try 
         {
	    	JSONObject   requestDatas = new JSONObject();
            requestDatas.put("cardUID", m_strCardNo);            
            requestDatas.put("goodsUID", m_strGoodsUID);
            requestDatas.put("coGroupUID", GOperaterInfo.m_strGroupUID);
            requestDatas.put("goodsNum", m_nGoodsTotalNum);
            svr.m_oCurrentActivity = this;
	    	svr.onPost("api/mobile/opCardGoodsChangeList.do", requestDatas);
         }
    	 catch (JSONException e) 
         {
             e.printStackTrace();
             MainActivity.MessageBox("兑换卡余量列表",e.getMessage());
             MainActivity.onUserMessageBox("兑换卡余量列表","兑换卡余量列表失败，请检查网络是否畅通或者联系管理员！");
         }
  	}
  	private void OnReadUserParameter()
	{
		Bundle bundle = this.getIntent().getExtras();
	      
		if(bundle == null)
			return;
		if(bundle.getString("cardNo")!=null)
			m_strCardNo = bundle.getString("cardNo");     
		if(bundle.getString("goodsUID")!=null)
			m_strGoodsUID = bundle.getString("goodsUID");     
		if(bundle.getString("goodsSerialNo")!=null)
			m_strGoodsSerialNo = bundle.getString("goodsSerialNo");     
		if(bundle.getString("goodsCaption")!=null)
			m_strGoodsCaption = bundle.getString("goodsCaption");     
		if(bundle.getString("goodsMinImage")!=null)
			m_strGoodsMinImage = bundle.getString("goodsMinImage");     
		if(bundle.getString("goodsNorImage")!=null)
			m_strGoodsNorImage = bundle.getString("goodsNorImage");     
		if(bundle.getString("goodsUnitName")!=null)
			m_strGoodsUnitName = bundle.getString("goodsUnitName");     
		m_nGoodsTotalNum = bundle.getInt("goodsTotalNum",1);
	}
}