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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

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

public class GActCardCreateOrder extends  Activity 
{
	//===============================================
	private String					m_strCardNo="";
	private int						m_nFontSize=12;
	//===============================================
	private TextView				m_txtGroupCaption=null;
	private TextView				m_txtGroupRoomCaption=null;
	private TextView				m_txtCardNo=null;
	private LinearLayout			m_oCardOrderGoodsList=null;
	private List						m_oCardGoodsList=new ArrayList();
	private List						m_oRemainList=new ArrayList();
	//===============================================
	protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.act_card_create_order);
		ActivityCollector.addActivity(this);		
		OnReadUserParameter();
		setResult(RESULT_OK, null);
    }
  @Override
	protected void onDestroy() 
    {
		super.onDestroy();
		ActivityCollector.removeActivity(this);
	}
  	public boolean onCheckGoodsList()
  	{
  		Map map = null;
		int i,nUserNum=0,iCount=0;
		for(i=0;i<m_oCardGoodsList.size();i++)
		{
			map = (Map)m_oCardGoodsList.get(i);
			nUserNum = Integer.parseInt(map.get("numUser").toString());
			if(nUserNum < 1)
				continue;
			iCount++;
		}
		if(iCount<1)
		{
			MainActivity.onUserMessageBox("定单检查", "没有点单不需要提交定单！");
			return false;
		}	  
		return true;
  	}
	public void onCardCreateOrderConfirm(View v)
	{
        if(m_txtGroupRoomCaption.getText().equals("")){
            MainActivity.onUserMessageBox("定单检查", "包房编号不能为空");
        }else {
            if (!onCheckGoodsList())
                return;
            Intent intent = new Intent(GActCardCreateOrder.this, ConfirmOrder.class);
            Bundle bundle = new Bundle();
            bundle.putString("cardUID", m_strCardNo);
            bundle.putString("groupUID", GOperaterInfo.m_strGroupUID);
            bundle.putString("roomUID", GOperaterInfo.m_strDefaultDeptUID);
            bundle.putStringArrayList("goodsList", (ArrayList<String>) m_oCardGoodsList);
            intent.putExtras(bundle);
            startActivity(intent);
            finish();
        }
	}
  	@Override
  	protected void onActivityResult(int requestCode, int resultCode, Intent data) 
	{
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode != RESULT_OK) 
			return;
		if(requestCode == 1001  && data != null)
		{
			String 	strKey="";
			int 		i,iCount=data.getIntExtra("GoodsCount",0);
			if(iCount<1)//用户没有兑换
				return;
			for(i=0;i<iCount;i++)
			{
				Map map = new HashMap();
				strKey  ="G"+i+"uID";map.put("uID", data.getStringExtra(strKey));
				strKey  ="G"+i+"caption";map.put("caption", data.getStringExtra(strKey));
				strKey  ="G"+i+"serialNo";map.put("serialNo", data.getStringExtra(strKey));
				strKey  ="G"+i+"unitName";map.put("unitName", data.getStringExtra(strKey));
				strKey  ="G"+i+"norImage";map.put("norImage", data.getStringExtra(strKey));
				strKey  ="G"+i+"minImage";map.put("minImage", data.getStringExtra(strKey));
				strKey  ="G"+i+"numTotal";map.put("goodsNum",""+ data.getIntExtra(strKey,0));
				strKey  ="G"+i+"numTotal";map.put("numUser", ""+data.getIntExtra(strKey,0));//用量
				//
				strKey  ="G"+i+"oID";map.put("oID", data.getStringExtra(strKey));
				strKey  ="G"+i+"numStep";map.put("numStep", ""+data.getIntExtra(strKey,0));
				strKey  ="G"+i+"numUser";map.put("numUseGoods", ""+data.getIntExtra(strKey,0));//使用原兑换量
				
				m_oCardGoodsList.add(map);
			}
			OnCreateCardOrderGoodsList(m_oCardOrderGoodsList);
		}
	}
  	private int onGetGoodsNumUsed(String strGoodsUID)
  	{
  		Map map;
  		int i,nUsedNum,nUserNum;
  		nUsedNum = 0;
  		for(i=0;i<m_oCardGoodsList.size();i++)
  		{
  			map =(Map)m_oCardGoodsList.get(i);
  			if(map.get("uID").toString().equalsIgnoreCase(strGoodsUID))
  			{
  				nUserNum = Integer.parseInt(map.get("numUser").toString());
  				if(map.get("oID").toString().length()<1)//兑换量不能算是操作量
  					nUsedNum+=nUserNum;
  			}
  			if(map.get("oID").toString().equalsIgnoreCase(strGoodsUID))
  			{
  				nUserNum = Integer.parseInt(map.get("numUseGoods").toString());
  				nUsedNum+=nUserNum;
  			}
  		}
  		return nUsedNum;
  	}
  	private LinearLayout OnCreateCardOrderGoods(Context	oContext,Map map,int nLineNum)
  	{
  	//======================================================================================
  		LinearLayout oHead = new LinearLayout(oContext);  //线性布局方式  
  		oHead.setOrientation( LinearLayout.HORIZONTAL ); //控件对其方式为水平排列  VERTICAL
  		//oHead.setBackgroundColor( 0xff00ffff );        //设置布局板的一个特殊颜色，这可以检验我们会话时候是否有地方颜色不正确！  
  		oHead.setLayoutParams( new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
  		//======================================================================================
  		TextView txtName;
  		TextView txtUnit;
  		TextView txtNumR;
  		TextView txtTaste;
  		TextView txtNumX;
  		TextView txtChange;
  		
  		LinearLayout.LayoutParams xCell =new  LinearLayout.LayoutParams(0, LayoutParams.WRAP_CONTENT);
  		xCell.weight=0.5f;
  		LinearLayout.LayoutParams pCell =new  LinearLayout.LayoutParams(0, LayoutParams.WRAP_CONTENT);
  		pCell.weight=1.0f;
  		
  		txtName = new TextView(oContext);
  		txtName.setLayoutParams(pCell);
  		if(map==null)
  			txtName.setText("名称");
  		else
  			txtName.setText(map.get("caption").toString());
        txtName.setGravity(Gravity.CENTER);
  		txtName.setTextSize(m_nFontSize*MainActivity.m_nPixelsFromDP);
  		oHead.addView(txtName);
  		
  		

  		
  		if(map==null)
  		{
  			txtNumR = new TextView(oContext);
  			txtNumR.setLayoutParams(pCell);  			
  			txtNumR.setText("数量");
            txtNumR.setGravity(Gravity.CENTER);
  			txtNumR.setTextSize(m_nFontSize*MainActivity.m_nPixelsFromDP);
  	  		oHead.addView(txtNumR);
  		}
  		else
  		{
  			
  			if(map.get("oID").toString().length()<1)
  			{
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
  			else
  			{
	  			txtNumR = new TextView(oContext);
	  			txtNumR.setLayoutParams(pCell);  			
	  			txtNumR.setText(map.get("goodsNum").toString());
	  			txtNumR.setTextSize(m_nFontSize*MainActivity.m_nPixelsFromDP);  	  		
	  	  		oHead.addView(txtNumR);
  			}
  		}



  		
  		
  		txtUnit 	= new TextView(oContext);
  		
  		txtUnit.setLayoutParams(xCell);
  		if(map==null)
  			txtUnit.setText("单位");
  		else
  			txtUnit.setText(map.get("unitName").toString());
        txtUnit.setGravity(Gravity.CENTER);
  		txtUnit.setTextSize(m_nFontSize*MainActivity.m_nPixelsFromDP);
  		oHead.addView(txtUnit);

        if(map==null)
        {
            txtTaste = new TextView(oContext);
            txtTaste.setLayoutParams(pCell);
            txtTaste.setText("口味");
            txtTaste.setGravity(Gravity.CENTER);
            txtTaste.setTextSize(m_nFontSize*MainActivity.m_nPixelsFromDP);
            oHead.addView(txtTaste);
        }
        else
        {

            if(map.get("oID").toString().length()<1)
            {
                txtTaste = new TextView(oContext);
                txtTaste.setLayoutParams(pCell);
                txtTaste.setText(map.get("numUser").toString());
                txtTaste.setTextSize(m_nFontSize*MainActivity.m_nPixelsFromDP);
                txtTaste.setId(5000+nLineNum);
                txtTaste.setClickable(true);
                txtTaste.setOnClickListener(m_oGoodsNumberChange);
                txtTaste.setTag(map);
                txtTaste.setBackgroundColor(Color.YELLOW);
                txtTaste.setGravity(Gravity.CENTER);
                oHead.addView(txtTaste);
            }
            else
            {
                txtTaste = new TextView(oContext);
                txtTaste.setLayoutParams(pCell);
                txtTaste.setText(map.get("goodsNum").toString());
                txtTaste.setTextSize(m_nFontSize*MainActivity.m_nPixelsFromDP);
                oHead.addView(txtTaste);
            }
        }

  		txtNumX = new TextView(oContext);  		
  		txtNumX.setLayoutParams(xCell);
        txtNumX.setGravity(Gravity.CENTER);
  		if(map==null)
  			txtNumX.setText("余量");
  		else
  		{
  			int nUsedNum,nGoodsNum;
  			nGoodsNum = Integer.parseInt(map.get("goodsNum").toString());
  			nUsedNum = onGetGoodsNumUsed(map.get("uID").toString());
  			txtNumX.setText(""+(nGoodsNum-nUsedNum));
  			m_oRemainList.add(txtNumX);
  		}
  		txtNumX.setTextSize(m_nFontSize*MainActivity.m_nPixelsFromDP);
  		oHead.addView(txtNumX);
  		

  		if(map==null)
  		{
  	  		txtChange = new TextView(oContext);
  	  		txtChange.setLayoutParams(xCell);
  			txtChange.setText("操作");
            txtChange.setGravity(Gravity.CENTER);
  	  		txtChange.setTextSize(m_nFontSize*MainActivity.m_nPixelsFromDP);
  	  		oHead.addView(txtChange);
  		}
  		else
  		{
  			LinearLayout oUserOP = new LinearLayout(oContext);  //线性布局方式  
  			oUserOP.setOrientation( LinearLayout.HORIZONTAL ); 
  			oUserOP.setLayoutParams( xCell);
  			//=================================================================================
  			LinearLayout.LayoutParams btnCell =new  LinearLayout.LayoutParams(0, LayoutParams.FILL_PARENT);
  			btnCell.weight=1.0f;
  			if(map.get("oID").toString().length()<1)
  			{  			
	  			Button btnChange = new Button(oContext);
	  			btnChange.setLayoutParams(btnCell);
	  			btnChange.setBackgroundColor(Color.rgb(0, 0xff, 0));
	  			btnChange.setText("换");
	  			btnChange.setId(2000+nLineNum);
	  			btnChange.setBackgroundResource(R.drawable.login);
	  			btnChange.setTextSize(m_nFontSize*MainActivity.m_nPixelsFromDP);
	  			btnChange.setTag(map);  	
	  			btnChange.setOnClickListener(m_oButtonChange);
	  			
	  			oUserOP.addView(btnChange);
  			}
  			else
  			{
  				Button btnDelete = new Button(oContext);
  	  			btnDelete.setLayoutParams(btnCell);
  	  			btnDelete.setBackgroundColor(Color.rgb(0xff, 0, 0));
  	  			btnDelete.setText("删");
  	  			btnDelete.setTextSize(m_nFontSize*MainActivity.m_nPixelsFromDP);
  	  			btnDelete.setId(3000+nLineNum);
  	  			btnDelete.setBackgroundResource(R.drawable.login);
  	  			btnDelete.setTag(map.get("uID"));
  	  			btnDelete.setOnClickListener(m_oButtonDelete);
  			
  	  			oUserOP.addView(btnDelete);
  			}
  			//=================================================================================
  	  		oHead.addView(oUserOP);
  		}  		
  		return oHead;
  	}
  	private void OnCreateCardOrderGoodsList(LinearLayout lv)
  	{
  		Context	oContext = lv.getContext();
  		Map			oMap=null;
  		
  		lv.removeAllViews();  		
  		m_oRemainList.clear();
  		
  		LinearLayout oHead = OnCreateCardOrderGoods(oContext,null,0);
  		oHead.setBackgroundColor(Color.rgb(0,0xff, 0));
  		lv.addView(oHead);
  		//======================================================================================
  		for(int i=0;i<m_oCardGoodsList.size();i++)
  		{
  			oMap = (Map)m_oCardGoodsList.get(i);  			
	  		LinearLayout oBody= OnCreateCardOrderGoods(oContext,oMap,i+1);
	  		lv.addView(oBody);
  		}
  		//======================================================================================
  	}
  	public void onClickGroupRoomCaption(View v)
  	{
  		AlertDialog.Builder builder = new AlertDialog.Builder(GActCardCreateOrder.this);
        builder.setIcon(R.drawable.ic_launcher);
        builder.setTitle("选择一个房间号码");
        //    指定下拉列表的显示数据
        final String[] cities = GOperaterInfo.getDeptsList().split(",");
        //    设置一个下拉的列表选择项
        builder.setItems(cities, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
	           	 GOperaterInfo.setDefaultDepts(which);
	           	 if(m_txtGroupRoomCaption != null)
	           		m_txtGroupRoomCaption.setText(GOperaterInfo.m_strDefaultDeptSerialNo);
            }
        });
        builder.show();
  	}
  	public void onClickGroupCaption(View v)
  	{
  		 
  	}
  	
  	private void OnReadUserParameter()
	{
		Bundle bundle = this.getIntent().getExtras();
	      
		if(bundle == null)
			return;
		if(bundle.getString("cardNo")!=null)
			m_strCardNo = bundle.getString("cardNo");     
		
		//==============================================================================
		m_txtGroupCaption = (TextView)findViewById(R.id.txt_group_caption);
		m_txtGroupRoomCaption = (TextView)findViewById(R.id.txt_group_room_caption);
		m_txtCardNo = (TextView)findViewById(R.id.txt_card_no);
		m_oCardOrderGoodsList= (LinearLayout)findViewById(R.id.ll_card_order_goods_list);
		
		//==============================================================================
		m_txtCardNo.setText(m_strCardNo);
		m_txtGroupCaption.setText(GOperaterInfo.m_strGroupName);
		if(GOperaterInfo.m_strGroupName.length()<1)
			m_txtGroupCaption.setText(GOperaterInfo.m_strGroupSerialNo);
		m_txtGroupRoomCaption.setText("");
		
		OnCreateCardOrderGoodsList(m_oCardOrderGoodsList);
		onReadCardGoodsList(m_strCardNo);
	}
  	private void  onReadCardGoodsList(String strCardNo)
  	{
  		GSvrChannel svr= 	new GSvrChannel()
    	{
    		public void onNetFailure(int statusCode,String strInfo)
    		{
    			MainActivity.MessageBox("读取卡余量列表","statusCode:"+statusCode+",Info:"+strInfo);
    			MainActivity.onUserMessageBox("读取卡余量列表", "读取卡余量列表失败，请检查网络是否畅通或者联系管理员！");
    		}
    		public void onNetSuccess(int nCode,String strInfo,JSONObject oJsonData)
    		{
    			if(nCode < 0)
    			{
    				MainActivity.MessageBox("读取卡余量列表",strInfo);    				
    				MainActivity.onUserMessageBox("读取卡余量列表",strInfo);
    				return;
    			}
    			
    			try 
    			{
    				JSONArray xContent=null;
					JSONObject oData = oJsonData.getJSONObject("data");
					if(oData == null)
					{
						MainActivity.MessageBox("读取卡余量列表","读取卡余量列表失败，请检查网络是否畅通或者联系管理员！");
						MainActivity.onUserMessageBox("读取卡余量列表","读取卡余量列表失败，请检查网络是否畅通或者联系管理员！");
	    				return;
					}
					try{xContent = oData.getJSONArray("content");}catch(JSONException e){xContent=null;}
					if(xContent==null)
					{
						MainActivity.onUserMessageBox("读取卡余量列表","读取卡余量列表失败，请检查网络是否畅通或者联系管理员！");
						return;
					}
					if(xContent.length()<1)
					{
						MainActivity.onUserMessageBox("读取卡余量列表",strInfo);
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
							map.put("goodsNum", GUtilHttp.getJSONObjectValue("goodsNum",o) );
							map.put("norImage", GUtilHttp.getJSONObjectValue("norImage",o) );
							map.put("minImage", GUtilHttp.getJSONObjectValue("minImage",o) );
							map.put("oID","");
							map.put("numStep","0");
							map.put("numUser","0");//用户使用量
							map.put("numUseGoods","0");	//对于原兑换量						
							
							m_oCardGoodsList.add(map);
						}
					}
					OnCreateCardOrderGoodsList(m_oCardOrderGoodsList);
				} 
    			catch (JSONException e) 
    			{
					e.printStackTrace();
					MainActivity.MessageBox("读取卡余量列表",e.getMessage());
					MainActivity.onUserMessageBox("读取卡余量列表","读取卡余量列表失败，请检查网络是否畅通或者联系管理员！");
					return;
				}
    		}
    	};
    	 try 
         {
	    	JSONObject   requestDatas = new JSONObject();
            requestDatas.put("cardUID", strCardNo);            
            svr.m_oCurrentActivity = this;
	    	svr.onPost("api/mobile/opCardGoodsList.do", requestDatas);
         }
    	 catch (JSONException e) 
         {
             e.printStackTrace();
             MainActivity.MessageBox("读取卡余量列表",e.getMessage());
             MainActivity.onUserMessageBox("读取卡余量列表","读取卡余量列表失败，请检查网络是否畅通或者联系管理员！");
         } 	
  	}
  	View.OnClickListener m_oGoodsNumberChange = new View.OnClickListener()
  	{
  		 public void onClick(View v) 
         {
  			 int nLineNum = v.getId()-5001;
  			 if(nLineNum<0 || nLineNum >= m_oCardGoodsList.size())
  				 return;
  			 Map map = (Map)v.getTag();
  			 if(map == null)
  				 return;
  			int nMaxNum = Integer.parseInt(map.get("goodsNum").toString());
  			int nUserNum = Integer.parseInt(map.get("numUser").toString());
  			int nUsedNum = onGetGoodsNumUsed(map.get("uID").toString());
  			if(nUsedNum > nMaxNum)
  				return;
  			final TextView txtGoodsNum = (TextView)v;
  			String strItems[] = new String[nMaxNum-nUsedNum+1+nUserNum];
  			for(int i=0;i<(nMaxNum-nUsedNum+1+nUserNum);i++)
  				strItems[i] = i+map.get("unitName").toString();
  			//=====================================================================
  			AlertDialog.Builder builder = new AlertDialog.Builder(GActCardCreateOrder.this);
  	        builder.setIcon(R.drawable.ic_launcher);
  	        builder.setTitle("选择"+map.get("caption").toString()+"数量");
  	        builder.setItems(strItems, new DialogInterface.OnClickListener()
  	        {
  	            @Override
  	            public void onClick(DialogInterface dialog, int which)
  	            {
  	            	String strUserNum = ""+which;
  	            	txtGoodsNum.setText(strUserNum);
  	            	 Map map = (Map)txtGoodsNum.getTag();
  	            	 if(map == null)
  	    				 return;
  	            	map.put("numUser",strUserNum);
  	            	
  	            	TextView tv=(TextView)m_oRemainList.get(txtGoodsNum.getId()-5001);
  	        		if(tv == null)
  	        			return;  	        		
  	        		int nGoodsNum= Integer.parseInt(map.get("goodsNum").toString());//总量
  	        		int nUsedNum=onGetGoodsNumUsed(map.get("uID").toString());//用量
  	        		if(tv!=null)
  	        			tv.setText(""+(nGoodsNum-nUsedNum));
  	            }
  	        });
  	        builder.show();
         }
  	};
    View.OnClickListener m_oGoodsTasteChange = new View.OnClickListener()
    {
        public void onClick(View v)
        {
            int nLineNum = v.getId()-5001;
            if(nLineNum<0 || nLineNum >= m_oCardGoodsList.size())
                return;
            Map map = (Map)v.getTag();
            if(map == null)
                return;
            int nMaxNum = Integer.parseInt(map.get("goodsNum").toString());
            int nUserNum = Integer.parseInt(map.get("numUser").toString());
            int nUsedNum = onGetGoodsNumUsed(map.get("uID").toString());
            if(nUsedNum > nMaxNum)
                return;
            final TextView txtGoodsTaste = (TextView)v;
            String strItems[] = new String[nMaxNum-nUsedNum+1+nUserNum];
            for(int i=0;i<(nMaxNum-nUsedNum+1+nUserNum);i++)
                strItems[i] = i+map.get("unitName").toString();
            //=====================================================================
            AlertDialog.Builder builder = new AlertDialog.Builder(GActCardCreateOrder.this);
            builder.setIcon(R.drawable.ic_launcher);
            builder.setTitle("选择"+map.get("caption").toString()+"口味");
            builder.setItems(strItems, new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialog, int which)
                {
                    String strTasteUser = ""+which;
                    txtGoodsTaste.setText(strTasteUser);
                    Map map = (Map)txtGoodsTaste.getTag();
                    if(map == null)
                        return;
                    map.put("TasteUser",strTasteUser);

                }
            });
            builder.show();
        }
    };
  	View.OnClickListener m_oButtonDelete = new View.OnClickListener()
  	{
  		 public void onClick(View v) 
         {
  			 int nLineNum = v.getId()-3001;
  			 if(nLineNum<0 || nLineNum >= m_oCardGoodsList.size())
  				 return;
  			m_oCardGoodsList.remove(nLineNum);
  			/////////////////////////////////////////////////////////////////////////
  			//重新画一下
  			OnCreateCardOrderGoodsList(m_oCardOrderGoodsList);
         }
  	};
  	View.OnClickListener m_oButtonChange = new View.OnClickListener() 
  	{
        public void onClick(View v) 
        {
        	if(v.getId()<2000 || v.getId()>=3000)
        		return;
            Map  map = (Map)v.getTag();
            int nGoodsNum = 1;
            int nUserNum=0;
            int nNumTotal = 0;
            
            try{nNumTotal = Integer.parseInt(map.get("goodsNum").toString());}catch(NumberFormatException ex){nNumTotal=0;}
            nUserNum = onGetGoodsNumUsed(map.get("uID").toString());
            nGoodsNum = nNumTotal - nUserNum;
            if(nGoodsNum<1)
            {
            	String strText = "产品【"+map.get("caption").toString()+"】已经被用完不可以更换。";
            	MainActivity.onUserMessageBox("产品兑换", strText);
            	return;
            }
            
            Intent intent = new Intent(GActCardCreateOrder.this, GActCardGoodsChange.class);
            intent.putExtra("goodsUID", map.get("uID").toString());
            intent.putExtra("cardNo", m_strCardNo);
            intent.putExtra("goodsSerialNo", map.get("serialNo").toString());
            intent.putExtra("goodsCaption", map.get("caption").toString());
            intent.putExtra("goodsMinImage",map.get("minImage").toString());
            intent.putExtra("goodsNorImage",map.get("norImage").toString());
            intent.putExtra("goodsUnitName",map.get("unitName").toString());
            intent.putExtra("goodsTotalNum",nGoodsNum);
        	startActivityForResult(intent, 1001);
        }
    };
}