package com.eaosoft.mclerk;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;

import com.eaosoft.userinfo.GOperaterInfo;
import com.eaosoft.util.ActivityCollector;
import com.eaosoft.util.GSvrChannel;
import com.eaosoft.util.GUtilFile;
import com.eaosoft.util.GUtilHttp;
import com.eaosoft.util.GUtilSDCard;
import com.eaosoft.util.XImageDownLoader;
import com.eaosoft.util.XImageDownLoader.DownLoaderListener;
import com.eaosoft.view.RoundImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class GActUserLogin extends  Activity 
{
	private RoundImageView				m_oHeadImage=null;
	private EditText 							m_oEdtUname;// 账号
	private EditText 							m_oEdtUpwd;// 密码
	private CheckBox 						m_oCbShowPwd;// 显示密码
	private Button 								m_oBtnLogin;// 登录
	
	  	protected void onCreate(Bundle savedInstanceState) 
	    {
	        super.onCreate(savedInstanceState);
	        requestWindowFeature(Window.FEATURE_NO_TITLE);
			setContentView(R.layout.act_user_login);

			ActivityCollector.addActivity(this);			
			onInitUserInfo();
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
	  private void onInitUserInfo()
	  {
		  SharedPreferences preferences = MainActivity.m_oMainActivity.getSharedPreferences(MainActivity.m_szMainConfigString,0);			
		  m_oHeadImage = (RoundImageView)this.findViewById(R.id.iv_uimage);
		  if(m_oHeadImage == null)
			  return;
		  m_oEdtUname = (EditText) this.findViewById(R.id.et_uname);
		  if(m_oEdtUname != null)
			  m_oEdtUname.setText(preferences.getString("LoginUserName", ""));
		  m_oEdtUpwd = (EditText) this.findViewById(R.id.et_upwd);
		
		  m_oCbShowPwd = (CheckBox) this.findViewById(R.id.cb_show_pwd);
		  m_oBtnLogin = (Button) this.findViewById(R.id.btn_login);
		  m_oBtnLogin.setTag(this);
		  m_oBtnLogin.setOnClickListener(user_login_listener);
		
		  Bitmap photo = BitmapFactory.decodeFile(GOperaterInfo.m_strLocalDiskImage);
		  if(photo !=null && m_oHeadImage!=null)
			m_oHeadImage.setImageBitmap(photo);
			
		m_oCbShowPwd.setOnCheckedChangeListener(new OnCheckedChangeListener() 
		  {
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) 
			{
				if (m_oCbShowPwd.isChecked()) 
					m_oEdtUpwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());					// 显示密码			
				else
					m_oEdtUpwd.setTransformationMethod(PasswordTransformationMethod.getInstance());// 不显示				
			}
		});					  
	  }
	  OnClickListener user_login_listener = new OnClickListener()
{
			@Override
			public void onClick(View v) 
			{
				GSvrChannel svr= 	new GSvrChannel()
		    	{
					public void onNetFailure(int statusCode,String strInfo)
		    		{
		    			MainActivity.MessageBox("发送用户登录","statusCode:"+statusCode+",Info:"+strInfo);
		    			MainActivity.onUserMessageBox("发送用户登录", "登录失败，请检查网络后重新登录！");
		    		}
		    		public void onNetSuccess(int nCode,String strInfo,JSONObject oJsonData)
		    		{
		    			if(nCode < 0)
		    			{
		    				MainActivity.MessageBox("发送用户登录",strInfo);    		
		    				MainActivity.onUserMessageBox("发送用户登录", strInfo);
		    				return;
		    			}
		    			
		    			try 
		    			{
							JSONObject oData = oJsonData.getJSONObject("data");
							if(oData == null)
							{
								MainActivity.onUserMessageBox("发送用户登录","取得登录数据失败，请检查网络后再登录");    				
			    				return;
							}
							GOperaterInfo.m_strUID = oData.getString("uID");
							GOperaterInfo.m_strToken = oData.getString("token");					
							GOperaterInfo.m_strNickName=oData.getString("nickName");
							GOperaterInfo.m_strRealName=oData.getString("caption");
							GOperaterInfo.m_strSerialNo=oData.getString("serialNo");
							GOperaterInfo.m_strSex=oData.getString("sex");
							GOperaterInfo.m_strHeadImage=oData.getString("headPicture");//用户头像
							GOperaterInfo.m_strSignInfo=oData.getString("signInfo");//签名
							GOperaterInfo.m_strPhone=oData.getString("loginName");
							GOperaterInfo.m_strLoginName=oData.getString("loginName");
							GOperaterInfo.m_strGroupSerialNo=oData.getString("coSerialNo");
							GOperaterInfo.m_strGroupUID=oData.getString("coGroupUID");
							GOperaterInfo.m_strGroupName=oData.getString("coCaption");
							GOperaterInfo.m_strRoleID=oData.getString("coRoleID");
							GOperaterInfo.m_strPassword =m_oEdtUpwd.getText().toString();
							//===============================================
							JSONArray oRoomList=null ;
							try{oRoomList = oData.getJSONArray("coGroupRooms");}//包房号码列表
							catch(JSONException e){oRoomList=null;}

							if(oRoomList!=null)
							{

								GOperaterInfo.m_oGroupDepts.clear();								
								for(int i=0;i<oRoomList.length();i++)
								{
									JSONObject o = oRoomList.getJSONObject(i);
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
							}
							//===============================================
			    			
							MainActivity.m_oOperaterInfo.onConfigSave();
							onDownloadHeadImage(GOperaterInfo.m_strHeadImage);
						} 
		    			catch (JSONException e) 
		    			{
							e.printStackTrace();
							return;
						}
		    			
		    			//startActivity(new Intent(GActUserLogin.this, MainActivity.class));
		    			Intent intent=new Intent();
		    			intent.setClass(GActUserLogin.this, MainActivity.class);
		    			Bundle bundle=new Bundle();		    			
		    			bundle.putString("login_result", "login_ok");
		    			intent.putExtras(bundle);		    			
		    			setResult(RESULT_OK, intent);
						overridePendingTransition(R.anim.left, R.anim.right);
						GActUserLogin.this.finish();
		    		}
		    	};
		    	 try
			{
				JSONObject   requestDatas = new JSONObject();
				requestDatas.put("cellPhone",  m_oEdtUname.getText().toString());
				requestDatas.put("password", GOperaterInfo.OnMakePassword(m_oEdtUpwd.getText().toString()));
				svr.m_oCurrentActivity=(Activity)v.getTag();
				svr.onPost("api/mobile/opLogin.do", requestDatas);
			}
		    	 catch (JSONException e) 
		         {
		             e.printStackTrace();
		             MainActivity.onUserMessageBox("发送用户登录",e.getMessage());
		         }
			}
		};
		public static void onDownloadHeadImage(String strURL)
		{
			if(strURL == null)
				return;
			if(strURL.length()<1)
				return;		
			
			GOperaterInfo.m_strLocalDiskImage  = GOperaterInfo.m_strImagePath+ GUtilFile.getFileName(strURL);
			if(GUtilSDCard.isFileExist(GOperaterInfo.m_strLocalDiskImage ))//本地磁盘已经存在
				return;
	        XImageDownLoader mMyImageDownLoader = new XImageDownLoader();
	            // 开始异步下载
	        mMyImageDownLoader.downloadImage(strURL,GOperaterInfo.m_strLocalDiskImage,new DownLoaderListener() 
	        {
	            @Override
	            public void onResult(int res, String s) 
	            {
	              
	                if (res == 0) 
	                {
	                    // 下载成功
	                    //Toast.makeText(MainActivity.m_oMainActivity, "download success!", Toast.LENGTH_LONG).show();
	                    } 
	                    else
	                     {
	                        // 下载photo失败
	                       // Toast.makeText(MainActivity.m_oMainActivity, "download fail!", Toast.LENGTH_LONG).show();
	                    }
	                }
	            });
		}
	  private View  onCreateView(Context oContext)
	  {
		  //==========================================================================================
		  LinearLayout oMainWin = new LinearLayout(oContext);  //线性布局方式  		  
		  oMainWin.setLayoutParams( new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT) );
		  oMainWin.setOrientation( LinearLayout.VERTICAL ); //控件对其方式为垂直排列  VERTICAL
		  //==========================================================================================
		  //头像区域
		  RelativeLayout oSubHeader = new RelativeLayout(oContext);  //线性布局方式          
		  oSubHeader.setLayoutParams( new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT, MainActivity.m_nPixelsFromDP*150) );
		  oSubHeader.setBackgroundColor(MainActivity.m_oHeadColor);
		  oMainWin.addView(oSubHeader);
		  
		  //RoundImageView	o
		  //==========================================================================================		  
		  return oMainWin;
	  }
}