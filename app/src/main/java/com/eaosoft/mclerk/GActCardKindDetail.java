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
import com.eaosoft.util.GUtilDateTimePickDialog;
import com.eaosoft.util.GUtilHttp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

public class GActCardKindDetail extends  Activity 
{
	private String 						m_strCardKindUID="";
	private String 						m_strGroupUID="";	
	//==========================================
	private EditText					m_oCaption;
	private TextView					m_oGroupCaption;
	private TextView					m_oTotalMoney;
	private TextView					m_oDateEnd;
	//==========================================================
	private LinearLayout			m_oCardKindGoodsList=null;
	private List							m_oKindGoodsList=new ArrayList();
	private List							m_oShopGoodsList=new ArrayList();
	//==========================================================
	protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); 
        
		setContentView(R.layout.act_card_kind_info);
		ActivityCollector.addActivity(this);		
		setResult(RESULT_OK, null);
		OnReadUserParameter();
		onInitView();		
		onReadCardKindInfo();
		onGetKindInfoFromSvr(m_strCardKindUID);
    }
  @Override
	protected void onDestroy() 
    {
		super.onDestroy();
		ActivityCollector.removeActivity(this);
	}
    private TextView onCreateCell(Context oContext,LinearLayout.LayoutParams pCell,Map map,int nColNum)
    {
    	TextView oCell=new TextView(oContext);
    	
    	oCell.setTextSize(18);
    	oCell.setLayoutParams(pCell);
    	if(map == null)//����Ǳ���
    	{
    		oCell.setGravity(Gravity.CENTER_HORIZONTAL);    
    		oCell.setBackgroundColor(Color.rgb(0,255,0));
    		switch(nColNum)
    		{
    		case 0:oCell.setText("����");break;
    		case 1:oCell.setText("��λ");break;
    		case 2:oCell.setText("����");break;
    		case 3:oCell.setText("����");break;
    		case 4:oCell.setText("����");break;
    		}
    	}
    	else
    	{
    		oCell.setTag(map);
    		switch(nColNum)
    		{
    		case 0:oCell.setText(map.get("caption").toString());break;
    		case 1:oCell.setText(map.get("unitName").toString());oCell.setGravity(Gravity.CENTER_HORIZONTAL);  break;
    		case 2:oCell.setText(map.get("num").toString());oCell.setGravity(Gravity.CENTER_HORIZONTAL);  break;
    		case 3:oCell.setText(map.get("price").toString());oCell.setGravity(Gravity.CENTER_HORIZONTAL);  break;
    		case 4:oCell.setText("ɾ��");oCell.setGravity(Gravity.CENTER_HORIZONTAL);  break;
    		}
    	}
    	if(nColNum==4 && map!=null)
    	{
    		oCell.setText("ɾ");
    		oCell.setBackgroundResource(R.drawable.login);
    		oCell.setTag(map);
    		oCell.setClickable(true);
    		oCell.setOnClickListener(m_oGoodsDelete);    		
    	}
    	if(nColNum==2 && map != null)
    	{
    		oCell.setBackgroundColor(Color.rgb(255,255,0));
    		oCell.setTag(map);
    		oCell.setClickable(true);
    		oCell.setBackgroundResource(R.drawable.login);
    		oCell.setOnClickListener(m_oGoodsNumberChange);
    	}
    	return oCell;
    }
	View.OnClickListener m_oGoodsNumberChange = new View.OnClickListener()
  	{
  		 public void onClick(View v) 
         {
  			final EditText inputServer = new EditText(v.getContext());
  			inputServer.setTag(v);
  			inputServer.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_NORMAL);
  	        inputServer.setFocusable(true);
  	        TextView txtNum = (TextView)v;
  	        inputServer.setText(txtNum.getText());

  	        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
  	        builder.setTitle("����������").setIcon(R.drawable.ic_launcher).setView(inputServer).setNegativeButton("ȡ��", null);
  	        builder.setPositiveButton("ȷ��",new DialogInterface.OnClickListener() 
  	        {
  	                    public void onClick(DialogInterface dialog, int which) 
  	                    {
  	                    	int nNum=0;
  	                        String inputName = inputServer.getText().toString();  	                      
  	                    	try{nNum = Integer.parseInt(inputName);}catch(NumberFormatException ex){nNum=0;};
  	                        TextView oNum = (TextView)inputServer.getTag();
  	                        Map map=(Map)oNum.getTag();
	  	          			if(map == null)
	  	          				return;
	  	          			int n = getGoodsFromCardKind(map.get("uID").toString());
	  	          			if(n<0 || n>=m_oKindGoodsList.size())
	  	          				return;
	  	          			oNum.setText(""+nNum);
	  	          			map.put("num", ""+nNum);
  	                        onReadCardKindInfo();
  	                    }
  	        });
  	        builder.show();
  			
  			
         }
  	};
  	View.OnClickListener m_oGoodsDelete = new View.OnClickListener()
  	{
  		 public void onClick(View v) 
         {
  			Map map=(Map)v.getTag();
  			if(map == null)
  				return;
  			int n = getGoodsFromCardKind(map.get("uID").toString());
  			if(n<0 || n>=m_oKindGoodsList.size())
  				return;
  			m_oKindGoodsList.remove(n);
  			onReadCardKindInfo();
         }
  	};
    private LinearLayout OnCreateCardKindGoods(Context oContext,Map map,int nLineNum)
    {
    	LinearLayout oLine = new LinearLayout(oContext);  //���Բ��ַ�ʽ  
    	oLine.setOrientation( LinearLayout.HORIZONTAL ); //�ؼ����䷽ʽΪ��ֱ����  VERTICAL
    	oLine.setLayoutParams( new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
  		//=====================================================================================
    	LinearLayout.LayoutParams pCell =new  LinearLayout.LayoutParams(0, LayoutParams.WRAP_CONTENT);
  		pCell.weight=1.0f;
  		
    	TextView oCell = null;
    	
    	oCell = onCreateCell(oContext,pCell,map,0);oLine.addView(oCell);
    	oCell = onCreateCell(oContext,pCell,map,1);oLine.addView(oCell);
    	oCell = onCreateCell(oContext,pCell,map,2);oLine.addView(oCell);
    	oCell = onCreateCell(oContext,pCell,map,3);oLine.addView(oCell);
    	oCell = onCreateCell(oContext,pCell,map,4);oLine.addView(oCell);
  		//=====================================================================================
    	return oLine;
    }
    private void onInitView()
    {
    	m_oCaption=(EditText)findViewById(R.id.txt_card_kind_caption);
    	m_oGroupCaption=(TextView)findViewById(R.id.txt_group_caption);    	
    	m_oTotalMoney=(TextView)findViewById(R.id.txt_total_money);
    	m_oDateEnd=(TextView)findViewById(R.id.txt_date_end);
    	m_oCardKindGoodsList = (LinearLayout)findViewById(R.id.ll_card_kind_goods_list);
    	
    	m_oGroupCaption.setText(GOperaterInfo.m_strGroupName);
    	m_strGroupUID = GOperaterInfo.m_strGroupUID;
    	m_oDateEnd.setText("2030-12-31");
    	m_oTotalMoney.setText("0.00");
    	m_oCaption.setText("");
    	
    	m_oDateEnd.setClickable(true);
    	m_oDateEnd.setOnClickListener(new View.OnClickListener() 
    	{
    		    @Override
                public void onClick(View v) 
    		    {
                    new DatePickerDialog(GActCardKindDetail.this, new DatePickerDialog.OnDateSetListener() 
                    {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) 
                        {
                            //tv.setText("���ĳ��������ǣ�"+String.format("%d-%d-%d",year,monthOfYear+1,dayOfMonth));
                        	m_oDateEnd.setText(String.format("%d-%d-%d",year,monthOfYear+1,dayOfMonth));
                        }
                    },2030,11,31).show();
                }
            });

    }
    private void onReadCardKindInfo()
    {
    	float fTotalMoney=0.0f;
    	float fPrice=0.0f;
    	float fNum=0;
    	Map map=null;
    	int 	i;
    	
    	m_oCardKindGoodsList.removeAllViews();
    	m_oCardKindGoodsList.addView(OnCreateCardKindGoods(this,null,-1));//����

    	for(i=0;i<m_oKindGoodsList.size();i++)
    	{
    		map = (Map)m_oKindGoodsList.get(i);    		
    		m_oCardKindGoodsList.addView(OnCreateCardKindGoods(this,map,i));
    		try{fPrice = Float.parseFloat(map.get("price").toString());}catch(NumberFormatException ex){fPrice=0.0f;};
    		try{fNum = Float.parseFloat(map.get("num").toString());}catch(NumberFormatException ex){fNum=0.0f;};
    		fTotalMoney +=(fPrice*fNum); 
    	}
    	m_oTotalMoney.setText(""+fTotalMoney);
    }
  	private void OnReadUserParameter()
 	{
 		Bundle bundle = this.getIntent().getExtras();
         
         if(bundle == null)
         	return;
         if(bundle.getString("uID")!=null)
        	 m_strCardKindUID = bundle.getString("uID");                
     }
  	 public void onCardKindCreateConfirm(View v)
  	 {
  		 if(m_oKindGoodsList.size()<1)
  			 return;
  		 if(m_oCaption.getText().toString().length()<1)
  		 {
  			MainActivity.onUserMessageBox("�ײͱ���", "�������ײ����ƣ�");
  			return;
  		 }
  		GSvrChannel svr= 	new GSvrChannel()
    	{
    		public void onNetFailure(int statusCode,String strInfo)
    		{
    			MainActivity.MessageBox("�ײͱ���","statusCode:"+statusCode+",Info:"+strInfo);
    			MainActivity.onUserMessageBox("�ײͱ���", "�ײͱ���ʧ�ܣ����������Ƿ�ͨ������ϵ����Ա��");
    		}
    		public void onNetSuccess(int nCode,String strInfo,JSONObject oJsonData)
    		{
    			if(nCode < 0)
    			{
    				MainActivity.MessageBox("�ײͱ���",strInfo);    				
    				MainActivity.onUserMessageBox("�ײͱ���","�ײͱ���ʧ�ܣ�\r\n\r\n"+strInfo);
    				return;
    			}
    			MainActivity.onUserMessageBox("�ײͱ���","�ײͱ���ɹ���\r\n\r\n"+strInfo);
    			//finish();
    		}
    	};
	    try	//============================================
	    {   
	    	JSONObject   requestDatas = new JSONObject();
	    	JSONArray	 oGoods = new JSONArray();
	    	Map map;
            requestDatas.put("uID", m_strCardKindUID);
            requestDatas.put("caption", m_oCaption.getText().toString());
            requestDatas.put("dateEnd", m_oDateEnd.getText().toString());
            requestDatas.put("totalMoney", m_oTotalMoney.getText().toString());
            requestDatas.put("groupUID", m_strGroupUID);
            requestDatas.put("goodsCount", ""+m_oKindGoodsList.size());
            for(int i=0;i<m_oKindGoodsList.size();i++)
        	{
        		map = (Map)m_oKindGoodsList.get(i); 
        		JSONObject o = new JSONObject();
        		o.put("uID", map.get("uID").toString());
        		o.put("num", map.get("num").toString());
        		
        		oGoods.put(i,o);
        	}
            requestDatas.put("goodsList", oGoods);
            
            
	    	svr.m_oCurrentActivity = this;
	    	svr.onPost("api/mobile/opCardKindSet.do", requestDatas);
	    }
	    catch (JSONException e) 
	    {
	    	e.printStackTrace();
	    	MainActivity.MessageBox("�ײͱ���",e.getMessage());
	    	MainActivity.onUserMessageBox("�ײͱ���","��ȡ�������б�ʧ�ܣ����������Ƿ�ͨ������ϵ����Ա��");
	    }
  	 }
  	 public void onClickGroupCaption(View v)
	 {
  		Intent intent = new Intent(GActCardKindDetail.this, GActGroupList.class);			
    	startActivityForResult(intent,MainActivity.USER_GROUP_CHANGE);
	 }
  	 private void onReadShopGoodsListFromSvr()
  	 {
  		GSvrChannel svr= 	new GSvrChannel()
    	{
    		public void onNetFailure(int statusCode,String strInfo)
    		{
    			MainActivity.MessageBox("��Ʒ�б�","statusCode:"+statusCode+",Info:"+strInfo);
    			MainActivity.onUserMessageBox("��Ʒ�б�", "ȷ�Ϲ���ʧ�ܣ����������Ƿ�ͨ������ϵ����Ա��");
    		}
    		public void onNetSuccess(int nCode,String strInfo,JSONObject oJsonData)
    		{
    			if(nCode < 0)
    			{
    				MainActivity.MessageBox("��Ʒ�б�",strInfo);    				
    				MainActivity.onUserMessageBox("��Ʒ�б�","����ʧ�ܣ�\r\n\r\n"+strInfo);
    				return;
    			}
    			if(nCode == 0)
    			{
    				MainActivity.onUserMessageBox("��Ʒ�б�","û���ҵ���Ʒ���Թ�ѡ��");
    				return;
    			}
    			try 
    			{
    				JSONArray xContent=null;
					JSONObject oData = oJsonData.getJSONObject("data");
					if(oData == null)
					{
						MainActivity.MessageBox("��Ʒ�б�","ȷ�Ϲ���ʧ�ܣ����������Ƿ�ͨ������ϵ����Ա��");
						MainActivity.onUserMessageBox("��Ʒ�б�","ȷ�Ϲ���ʧ�ܣ����������Ƿ�ͨ������ϵ����Ա��");
	    				return;
					}
					try{xContent = oData.getJSONArray("content");}catch(JSONException e){xContent=null;}
					if(xContent==null)
					{
						MainActivity.onUserMessageBox("��ȡ��Ʒ�б�","��ȡ�������б�ʧ�ܣ����������Ƿ�ͨ������ϵ����Ա��");
						return;
					}
					if(xContent.length()<1)
					{
						MainActivity.onUserMessageBox("��ȡ��Ʒ�б�","û���ҵ���Ʒ���Թ�ѡ��");
	    				return;
					}
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
							map.put("norImage", GUtilHttp.getJSONObjectValue("norImage",o) );
							map.put("minImage", GUtilHttp.getJSONObjectValue("minImage",o) );		
							map.put("price", GUtilHttp.getJSONObjectValue("price",o) );
							map.put("num","0");
							map.put("userChoose","0");
							
							m_oShopGoodsList.add(map);
						}
					}
					onCardKindGoodsAddUser();
				} 
    			catch (JSONException e) 
    			{
					e.printStackTrace();
					MainActivity.MessageBox("��Ʒ�б�",e.getMessage());
					MainActivity.onUserMessageBox("��Ʒ�б�","ȷ�Ϲ���ʧ�ܣ����������Ƿ�ͨ������ϵ����Ա��");
					return;
				}
    		}
    	};
	    try	//============================================
	    {   
	    	JSONObject   requestDatas = new JSONObject();
            requestDatas.put("userID", GOperaterInfo.m_strUID);
	    	svr.m_oCurrentActivity = this;
	    	svr.onPost("api/mobile/opShopGoodsList.do", requestDatas);
	    }
	    catch (JSONException e) 
	    {
	    	e.printStackTrace();
	    	MainActivity.MessageBox("��ȡ��Ʒ�б�",e.getMessage());
	    	MainActivity.onUserMessageBox("��Ʒ�б�","��ȡ�������б�ʧ�ܣ����������Ƿ�ͨ������ϵ����Ա��");
	    } 	
  	 }
  	 private void onCardKindGoodsAddUser()
  	 {
  		 if(m_oShopGoodsList.size()<1)
  			 return;
  		AlertDialog.Builder builder = new AlertDialog.Builder(GActCardKindDetail.this);
        builder.setIcon(R.drawable.ic_launcher);
        builder.setTitle("��ѡ���ײ���Ʒ");
        final String[] hobbies = new String[m_oShopGoodsList.size()];
        for(int i=0;i<m_oShopGoodsList.size();i++)
        {
        	hobbies[i] = ((Map)m_oShopGoodsList.get(i)).get("caption").toString()+"��"+((Map)m_oShopGoodsList.get(i)).get("unitName").toString()+"��";
        	((Map)m_oShopGoodsList.get(i)).put("userChoose", "0");
        }
        //    ����һ������ѡ��������
        /**
         * ��һ������ָ������Ҫ��ʾ��һ��������ѡ������ݼ���
         * �ڶ������������ļ���ѡ�ѡ�������null�����ʾһ������ѡ�����ϣ��ָ����һ����ѡѡ���ѡ��
         * ��Ҫ����һ��boolean[]�����ȥ���䳤��Ҫ�͵�һ�������ĳ�����ͬ������ {true, false, false, true};
         * ������������ÿһ����ѡ���һ��������
         */
        builder.setMultiChoiceItems(hobbies, null, new DialogInterface.OnMultiChoiceClickListener()
        {
            //StringBuffer sb = new StringBuffer(100);
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked)
            {
            	if(which<0 || which>=m_oShopGoodsList.size())
            		return;
                if(isChecked)
                {
                    //sb.append(hobbies[which] + ", ");
                	((Map)m_oShopGoodsList.get(which)).put("userChoose", "1");
                }
                else
                {
                	((Map)m_oShopGoodsList.get(which)).put("userChoose", "0");
                }
            }
        });
        builder.setPositiveButton("ȷ��", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
            	onGoodsAddToCardKind();
            }
        });
        builder.setNegativeButton("ȡ��", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                
            }
        });
        builder.show();
  	 }
  	 private void onGoodsAddToCardKind()
  	 {
  		 Map map=null;
  		if(m_oShopGoodsList.size()<1)
  			return;
  		for(int i=0;i<m_oShopGoodsList.size();i++)
  		{
  			map = (Map)m_oShopGoodsList.get(i);
  			if(map == null)
  				return;
  			if(map.get("userChoose").toString().equalsIgnoreCase("0"))
  				continue;
  			if(getGoodsFromCardKind(map.get("uID").toString())>=0)
  				continue;
  			Map  oMap = new HashMap();
  			oMap.put("uID",map.get("uID").toString());
  			oMap.put("caption",map.get("caption").toString());						
  			oMap.put("serialNo", map.get("serialNo").toString());							
  			oMap.put("unitName", map.get("unitName").toString());
  			oMap.put("norImage", map.get("norImage").toString());
  			oMap.put("minImage", map.get("minImage").toString());
  			oMap.put("price", map.get("price").toString());	
  			oMap.put("num","1");
			
  			m_oKindGoodsList.add(oMap);
  		}
  		onReadCardKindInfo();
  	 }
	 public void onCardKindGoodsAdd(View v)
  	 {
		 if(m_oShopGoodsList.size()<1)
		 {
			 onReadShopGoodsListFromSvr();
			 return;
		 }
		 onCardKindGoodsAddUser();
  	 }
	 private int getGoodsFromCardKind(String strGoodsUID)
	 {
		 Map map;
		 for(int i=0;i<m_oKindGoodsList.size();i++)
		 {
			 map = (Map)m_oKindGoodsList.get(i);
			 if(map.get("uID").toString().equalsIgnoreCase(strGoodsUID))
				 return i;
		 }
		 return -1;
	 }
	 @Override
	 protected void onActivityResult(int requestCode, int resultCode, Intent data) 
	{
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode != RESULT_OK) 
			return;
		if(requestCode== MainActivity.USER_GROUP_CHANGE && m_oGroupCaption != null)
		{
			m_oGroupCaption.setText(GOperaterInfo.m_strGroupName);
			m_strGroupUID = GOperaterInfo.m_strGroupUID;
		}
	}
	 private void onGetKindInfoFromSvr(String strUID)
	 {
		 if(strUID.length()<1)
			 return;
		 GSvrChannel svr= 	new GSvrChannel()
    	{
    		public void onNetFailure(int statusCode,String strInfo)
    		{
    			MainActivity.MessageBox("��ȡ�ײ�����","statusCode:"+statusCode+",Info:"+strInfo);
    			MainActivity.onUserMessageBox("��ȡ�ײ�����", "��ȡ�ײ�����ʧ�ܣ����������Ƿ�ͨ������ϵ����Ա��");
    		}
    		public void onNetSuccess(int nCode,String strInfo,JSONObject oJsonData)
    		{
    			if(nCode < 0)
    			{
    				MainActivity.MessageBox("��ȡ�ײ�����",strInfo);    				
    				MainActivity.onUserMessageBox("��ȡ�ײ�����",strInfo);
    				return;
    			}
    			
    			try 
    			{
    				JSONArray xContent=null;
					JSONObject oData = oJsonData.getJSONObject("data");
					if(oData == null)
					{
						MainActivity.MessageBox("��ȡ�ײ�����","��ȡ�ײ�����ʧ�ܣ����������Ƿ�ͨ������ϵ����Ա��");
						MainActivity.onUserMessageBox("��ȡ�ײ�����","��ȡ�ײ�����ʧ�ܣ����������Ƿ�ͨ������ϵ����Ա��");
	    				return;
					}
					try{xContent = oData.getJSONArray("goodsList");}catch(JSONException e){xContent=null;}
					if(xContent==null)
						return;
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
							map.put("norImage", GUtilHttp.getJSONObjectValue("norImage",o) );
							map.put("minImage", GUtilHttp.getJSONObjectValue("minImage",o) );
							map.put("price", GUtilHttp.getJSONObjectValue("price",o));	
							map.put("num",GUtilHttp.getJSONObjectValue("goodsNum",o) );
				  			
							m_oKindGoodsList.add(map);
						}
					}
					onReadCardKindInfo();
		            //####################################################
					GUtilHttp.getJSONObjectValue("caption",oData);
					m_strCardKindUID=GUtilHttp.getJSONObjectValue("uID",oData);
					m_strGroupUID=GUtilHttp.getJSONObjectValue("groupUID",oData);	
					//==========================================
					m_oCaption.setText(GUtilHttp.getJSONObjectValue("caption",oData));
					m_oGroupCaption.setText(GUtilHttp.getJSONObjectValue("groupCaption",oData));
					m_oTotalMoney.setText(GUtilHttp.getJSONObjectValue("totalMoney",oData));
					m_oDateEnd.setText(GUtilHttp.getJSONObjectValue("dateEnd",oData));
				} 
    			catch (JSONException e) 
    			{
					e.printStackTrace();
					MainActivity.MessageBox("��ȡ�ײ�����",e.getMessage());
					MainActivity.onUserMessageBox("��ȡ�ײ�����","��ȡ�ײ�����ʧ�ܣ����������Ƿ�ͨ������ϵ����Ա��");
					return;
				}
    		}
    	};
    	 try 
         {
	    	JSONObject   requestDatas = new JSONObject();
	    	requestDatas.put("cardKindUID", strUID);            
            svr.m_oCurrentActivity = this;
	    	svr.onPost("api/mobile/opHRGroupCardKindInfo.do", requestDatas);
         }
    	 catch (JSONException e) 
         {
             e.printStackTrace();
             MainActivity.MessageBox("��ȡ�ײ�����",e.getMessage());
             MainActivity.onUserMessageBox("��ȡ�ײ�����","��ȡ�ײ�����ʧ�ܣ����������Ƿ�ͨ������ϵ����Ա��");
         } 
	 }
}