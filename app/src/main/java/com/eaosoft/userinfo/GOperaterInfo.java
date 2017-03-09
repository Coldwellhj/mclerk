package com.eaosoft.userinfo;

import android.content.Context;
import android.content.SharedPreferences;

import com.eaosoft.mclerk.MainActivity;
import com.eaosoft.util.GUtilBase64;
import com.eaosoft.util.GUtilFile;
import com.eaosoft.util.GUtilSDCard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GOperaterInfo
{
	//=============================================================
	public static String 		m_strUID="";
	public static String 		m_strNickName="";
	public static String 		m_strRealName="";
	public static String 		m_strLoginName="";
	public static String 		m_strSerialNo="";
	public static String 		m_strSex="";
	public static String 		m_strAmount="";//账户余额
	public static String 		m_strIntegral="";//用户积分
	public static String 		m_strHeadImage="";//用户头像
	public static String 		m_strLocalDiskImage="";//用户头像
	public static String 		m_strAuthStatus="";//认证
	public static String		m_strSignInfo="";
	public static String 		m_strPassword="";
	public static String 		m_strPhone="";
	public static String 		m_strToken="64E702545A652B7CC5BA94853E4B14BA";
	public static String 		m_str="";	
	//=============================================================
	public static String		m_strImagePath="";
	public static String		m_strIOAImagePath="";
	public static String		m_strIOAThumbnailPath="";
	//=============================================================
	public static String		m_strGroupName="";
	public static String		m_strGroupUID="";
	public static String		m_strGroupSerialNo="";
	public static String		m_strRoleID="RSale";
	public static List			m_oGroupDepts=new ArrayList();
	public static String		m_strDefaultDeptSerialNo="";
	public static String		m_strDefaultDeptUID="";
	//=============================================================
	public GOperaterInfo()
	{
		
	}
	public void onReadConfig()
	{
		SharedPreferences preferences = MainActivity.m_oMainActivity.getSharedPreferences(MainActivity.m_szMainConfigString,0);
		m_strLoginName = preferences.getString("LoginUserName", "");
		m_strPhone = preferences.getString("LoginUserName", "");
		m_strPassword = preferences.getString("LoginUserPass", "");
		m_strUID = preferences.getString("LoginUserGUID","");
		m_strToken = preferences.getString("LoginUserToken","64E702545A652B7CC5BA94853E4B14BA");
		m_strNickName = preferences.getString("LoginNickName","");
		m_strRealName = preferences.getString("LoginRealName","");
		m_strHeadImage = preferences.getString("RegUserHeadImage","");
		m_strSignInfo = preferences.getString("LoginSignInfo","");
		m_strGroupName = preferences.getString("LoginGroupName","");
		m_strGroupUID = preferences.getString("LoginGroupUID","");
		m_strGroupSerialNo = preferences.getString("LoginGroupSerialNo","");
		m_strRoleID = preferences.getString("LoginRoleID","");
		//m_strHeadImage = preferences.getString("LoginRealName","");		
		//============================================================
		String 	strKey="";
		Map 	map =null;
		for(int i=0;i<m_oGroupDepts.size();i++)
		{
			map = (Map)m_oGroupDepts.get(i);			
			strKey = "GD_UID"+i;preferences.edit().putString(strKey,map.get("uID").toString()).commit();
			strKey = "GD_Caption"+i;preferences.edit().putString(strKey,map.get("caption").toString()).commit();
			strKey = "GD_SerialNo"+i;preferences.edit().putString(strKey,map.get("serialNo").toString()).commit();
			strKey = "GD_CaptionNick"+i;preferences.edit().putString(strKey,map.get("captionNick").toString()).commit();
		}		
		m_strDefaultDeptSerialNo = preferences.getString("GroupDeptDefaultSerialNo","");
		m_strDefaultDeptUID = preferences.getString("GroupDeptDefaultUID","");
		int nGroupDeptCount = preferences.getInt("GroupDeptCount",0);
		for(int i=0;i<nGroupDeptCount;i++)
		{
			map = new HashMap();
			strKey = "GD_UID"+i;map.put("uID", preferences.getString(strKey,""));
			strKey = "GD_Caption"+i;map.put("caption", preferences.getString(strKey,""));
			strKey = "GD_SerialNo"+i;map.put("serialNo", preferences.getString(strKey,""));
			strKey = "GD_CaptionNick"+i;map.put("captionNick", preferences.getString(strKey,""));
			m_oGroupDepts.add(map);
		}
		//================================================================
        m_strImagePath=MainActivity.m_szAppBasePath+"Member/Images/"; 
        GUtilSDCard.isFolderExists(m_strImagePath);
        m_strIOAImagePath=MainActivity.m_szAppBasePath+"Member/IOAImages/"; 
        GUtilSDCard.isFolderExists(m_strIOAImagePath);
        m_strIOAThumbnailPath=MainActivity.m_szAppBasePath+"Member/IOAThumbnail/"; 
        GUtilSDCard.isFolderExists(m_strIOAThumbnailPath);
        if(GUtilFile.getFileName(m_strHeadImage).length()>0)
        	m_strLocalDiskImage =m_strImagePath+ GUtilFile.getFileName(m_strHeadImage);
	}
	public  void onConfigSave()
	{
		SharedPreferences preferences = MainActivity.m_oMainActivity.getSharedPreferences(MainActivity.m_szMainConfigString,0);
		
		preferences.edit().putString("LoginUserName", m_strLoginName).commit();
		preferences.edit().putString("LoginUserPhone", m_strPhone ).commit();
		preferences.edit().putString("LoginUserPass", m_strPassword ).commit();
		preferences.edit().putString("LoginUserGUID",m_strUID ).commit();
		preferences.edit().putString("LoginUserToken",m_strToken ).commit();
		preferences.edit().putString("RegUserHeadImage",m_strHeadImage ).commit();
		preferences.edit().putString("LoginNickName", m_strNickName).commit();
		preferences.edit().putString("LoginRealName", m_strRealName).commit();
		preferences.edit().putString("LoginSignInfo",m_strSignInfo).commit();
		preferences.edit().putString("LoginGroupName",m_strGroupName).commit();
		preferences.edit().putString("LoginGroupUID",m_strGroupUID).commit();
		preferences.edit().putString("LoginGroupSerialNo",m_strGroupSerialNo).commit();
		preferences.edit().putString("LoginRoleID",m_strRoleID).commit();
		
		String 	strKey="";
		Map 	map =null;
		for(int i=0;i<m_oGroupDepts.size();i++)
		{
			map = (Map)m_oGroupDepts.get(i);			
			strKey = "GD_UID"+i;preferences.edit().putString(strKey,map.get("uID").toString()).commit();
			strKey = "GD_Caption"+i;preferences.edit().putString(strKey,map.get("caption").toString()).commit();
			strKey = "GD_SerialNo"+i;preferences.edit().putString(strKey,map.get("serialNo").toString()).commit();
			strKey = "GD_CaptionNick"+i;preferences.edit().putString(strKey,map.get("captionNick").toString()).commit();
		}
		preferences.edit().putInt("GroupDeptCount",m_oGroupDepts.size()).commit();
		preferences.edit().putString("GroupDeptDefaultSerialNo",m_strDefaultDeptSerialNo).commit();
		preferences.edit().putString("GroupDeptDefaultUID",m_strDefaultDeptUID).commit();
	}
	public  void onUserLogout()
	{
		SharedPreferences preferences = MainActivity.m_oMainActivity.getSharedPreferences(MainActivity.m_szMainConfigString,0);
		
		preferences.edit().putString("LoginUserName", "").commit();
		preferences.edit().putString("LoginUserPhone", "" ).commit();
		preferences.edit().putString("LoginUserPass", "" ).commit();
		preferences.edit().putString("LoginUserGUID","" ).commit();
	}
	public static boolean onCheckLogin()
	{
		if(m_strLoginName.length()<1 || m_strToken.length()<1)
			return false;
		
		return true;
	}
	public static boolean setDefaultDepts(int n)
	{
		if(n<0 || n>m_oGroupDepts.size())
			return false;
		SharedPreferences preferences = MainActivity.m_oMainActivity.getSharedPreferences(MainActivity.m_szMainConfigString,0);
		Map map = (Map)m_oGroupDepts.get(n);
		m_strDefaultDeptSerialNo = map.get("serialNo").toString();
		m_strDefaultDeptUID= map.get("uID").toString();
		preferences.edit().putString("GroupDeptDefaultSerialNo",m_strDefaultDeptSerialNo).commit();
		preferences.edit().putString("GroupDeptDefaultUID",m_strDefaultDeptUID).commit();
		return true;
	}
	public static String getDeptsList()
	{
		String strDeptsInfo="";
		Map   map;
		int      i;
		
		for(i=0;i<m_oGroupDepts.size();i++)
		{
			if(i>0)
				strDeptsInfo+=",";
			map = (Map)m_oGroupDepts.get(i);
			strDeptsInfo+=map.get("serialNo").toString();
		}
		return strDeptsInfo;
	}
	public static boolean onCheckUserLoginWindow(Context oContext)
	{
		if(m_strLoginName.length()<1 || m_strToken.length()<1)
		{
			//oContext.startActivity(new Intent(oContext, ActUserLogin.class));
			return false;
		}
		return true;
	}
	public void onChangePasswordToSVR(String strNewPassword)
	{
		
	}
	public static String OnMakePassword(String strPassword)
	{
			return GUtilBase64.MD5(GUtilBase64.encode(strPassword));
	}
	public static boolean OnCheckPassword(String strPassword)
{
	if(m_strPassword.equalsIgnoreCase(OnMakePassword(strPassword)) || m_strPassword.equalsIgnoreCase(strPassword))
		return true;
	return false;
	}
	public static void OnConfirmCarrayOn(String szPrjOrderUID,String szDstUID) 
	{
		/*
		GHTChinaSVR svr= 	new GHTChinaSVR()
    	{
    		public void onNetFailure(int statusCode,String strInfo)
    		{
    			MainActivity.MessageBox("确认承接人","statusCode:"+statusCode+",Info:"+strInfo);
    		}
    		public void onNetSuccess(int nCode,String strInfo,JSONObject oJsonData)
    		{
    			if(nCode < 0)
    			{
    				MainActivity.MessageBox("确认承接人",strInfo);    				
    				return;
    			}
    			MainActivity.MessageBox("确认承接人",strInfo);    	
			}
    	};
    	 try 
         {
	    	JSONObject   requestDatas = new JSONObject();
	    	requestDatas.put("prj_order_uid", szPrjOrderUID);
	    	requestDatas.put("mem_uid", szDstUID);
	    	
	    	svr.onPost("mgr_center/mgrPrjOrderCarrayOn.do", requestDatas);
         }
    	 catch (JSONException e) 
         {
             e.printStackTrace();
             MainActivity.MessageBox("确认承接人",e.getMessage());
         }
         */ 
		}
	
}