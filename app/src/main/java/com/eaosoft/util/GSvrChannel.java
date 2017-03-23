package com.eaosoft.util;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.Message;
import android.util.Log;

import com.eaosoft.mclerk.MainActivity;
import com.eaosoft.userinfo.GOperaterInfo;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.BinaryHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.ByteArrayEntity;
import cz.msebera.android.httpclient.message.BasicHeader;
import cz.msebera.android.httpclient.protocol.HTTP;

public class GSvrChannel
{
	private static final String BASE_URL = "http://120.26.96.179/mclerk/";
//    private static final String BASE_URL = "http://192.168.10.210/web_jetleex/mclerk/";
	public static String			m_strSystemToken="";
	//private static final String BASE_URL = "http://222.92.3.40:16000/htchina/";
	public static final String CALLER_NAME="Android";
	private static final int      NET_CONNECT_TIMEOUT	=60000;//设置链接超时
	private static final int      NET_RESPONSE_TIMEOUT	=60000;//设置应答超时
	//=======================================================
	public static final String	m_strURLCardRemain=BASE_URL+"api/html/cardRemain.html";//余额查询
	public static final String	m_strURLCardConsume=BASE_URL+"api/html/cardConsume.html";//消费记录
	public static final String	m_strURLOrderHistory=BASE_URL+"api/html/opOrderHistory.html";//下单记录
	//=======================================================
	public static final int	NET_RESPON_OK=0;//应答成功 
	public static final int	NET_RESPON_NG=-1;//应答失败
	//=======================================================
	private String m_strCurrentURL="";
	public  Activity m_oCurrentActivity=null;
	public GSvrChannel()
	{
		
		m_oCurrentActivity = null;
		m_strSystemToken = GOperaterInfo.m_strToken;
	}
	public void onPostEx(String strURL,JSONObject requestDatas,Activity oActivity)
	{		
		m_oCurrentActivity = oActivity;
		if(oActivity == null)
			m_oCurrentActivity = MainActivity.m_oMainActivity;
		onPost(strURL,requestDatas);
	}
	public void onPost(String strURL,JSONObject requestDatas)
	{
		 AsyncHttpClient client = new AsyncHttpClient();
         String strSVRAddr = BASE_URL+strURL;
         JSONObject jsonObject = new JSONObject();
         m_strCurrentURL = strSVRAddr;
         
         //client.setConnectTimeout(NET_CONNECT_TIMEOUT);
         client.setResponseTimeout(NET_RESPONSE_TIMEOUT);
         client.setConnectTimeout(NET_CONNECT_TIMEOUT);
         try 
         {
        	 
        	 jsonObject.put("token",GOperaterInfo.m_strToken );
             jsonObject.put("callerName",CALLER_NAME);
             if(requestDatas!=null)
            	 jsonObject.put("requestDatas", requestDatas);
         } 
         catch (JSONException e) 
         {
             e.printStackTrace();
             onNetFailure(-10000,e.getMessage());
             return;
         }
         ByteArrayEntity entity = null;
         try 
         {
             entity = new ByteArrayEntity(jsonObject.toString().getBytes("UTF-8"));
             entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
         }
         catch (UnsupportedEncodingException e) 
         {
             e.printStackTrace();
             onNetFailure(-10001,e.getMessage());
             return;
         }
         client.post(MainActivity.m_oMainActivity,strSVRAddr,entity,"application/json",new JsonHttpResponseHandler()
         {
        	 @Override
        	 public void  onSuccess(int statusCode, Header[]headers, String  jsonResponse)
        	 {

        	 }
             @Override
             public void onSuccess(int statusCode, Header[] headers, JSONObject response) 
             {
				try 
				{
					onNetSuccess(response.getInt("code"),response.getString("info"),response);
				}
				catch (JSONException e) 
				{
					e.printStackTrace();
					onNetFailure(-10003,e.getMessage());
					return;
				}
             }
             @Override
             public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) 
             {
            	 onNetFailure(-1*statusCode,responseString);
             }
             @Override
             public void onFinish()
             { 
         		Message msg = new Message(); 
        		msg.what =MainActivity.PROGRESS_NETWORK_WAIT_END;
        		msg.obj = m_oCurrentActivity;

            	 MainActivity.m_oMsgHandler.sendMessage(msg);
            	 onNetFinish(); 
            }
             public void onStart() 
             {
            	 Message msg = new Message(); 
         		 msg.what =MainActivity.PROGRESS_NETWORK_WAIT_START;
         		 msg.obj = m_oCurrentActivity;
            	 //MainActivity.m_oMsgHandler.sendEmptyMessage(MainActivity.PROGRESS_NETWORK_WAIT_START);
            	 MainActivity.m_oMsgHandler.sendMessage(msg);
            	 onNetStart();
            }
         } );
	}
	public void onNetStart()
	{
		
	}
	public void onNetFinish()
	{		
		
	}
	public void onNetFailure(int statusCode,String strInfo)
	{
		
	}
	public void onNetSuccess(int nCode,String strInfo,JSONObject oJsonData)
	{
		
	}
	public void downloadFile(String url) 
	{
		AsyncHttpClient client = new AsyncHttpClient();
		// 指定文件类型
		String[] allowedContentTypes = new String[] { "image/png", "image/jpeg" };
		// 获取二进制数据如图片和其他文件
		client.get(url, new BinaryHttpResponseHandler(allowedContentTypes) 
		{
			@Override
			public void onSuccess(int statusCode, Header[] headers,byte[] binaryData) 
			{
				String tempPath = Environment.getExternalStorageDirectory().getPath() + "/temp.jpg";
				Log.e("binaryData:", "共下载了：" + binaryData.length);

				Bitmap bmp = BitmapFactory.decodeByteArray(binaryData, 0,binaryData.length);

				File file = new File(tempPath);
				// 压缩格式
				CompressFormat format = Bitmap.CompressFormat.JPEG;
				// 压缩比例
				int quality = 100;
				try 
				{
					// 若存在则删除
					if (file.exists())
						file.delete();
					// 创建文件
					file.createNewFile();
					//
					OutputStream stream = new FileOutputStream(file);
					// 压缩输出
					bmp.compress(format, quality, stream);
					// 关闭
					stream.close();
				} catch (IOException e) 
				{
					e.printStackTrace();
				}
			}
			@Override
			public void onFailure(int statusCode, Header[] headers,byte[] binaryData, Throwable error) {}
		});
	}
	 /** 
	* @param path 
	*            要上传的文件路径 
	* @param url 
	*            服务端接收URL 
	* @throws Exception 
	*/  
	public void uploadFile(String url,String path) throws Exception 
	{  
		File file = new File(path);  
		String strSvrURL =BASE_URL+url;
		if (file.exists() && file.length() > 0) 
		{  
		    AsyncHttpClient client = new AsyncHttpClient();  
		    RequestParams params = new RequestParams();  
		    params.put("uploadedfile", file);  
		    // 上传文件  
		    client.post(strSvrURL, params, new JsonHttpResponseHandler() 
		    {  		        
		        @Override
	             public void onSuccess(int statusCode, Header[] headers, JSONObject response) 
	             {            	 
					try 
					{
						onNetSuccess(response.getInt("code"),response.getString("info"),response);
					}
					catch (JSONException e) 
					{
						e.printStackTrace();
						onNetFailure(-10003,e.getMessage());
						return;
					}
	             }
	             @Override
	             public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) 
	             {
	            	 onNetFailure(-1*statusCode,responseString);
	             }
	             @Override
	             public void onFinish(){ onNetFinish(); }
	             public void onStart() {onNetStart();};
		  
		    });  
		} 		
	}
	/*
	 *GSvrChannel svr= 	new GSvrChannel()
    	{
    		public void onNetFailure(int statusCode,String strInfo)
    		{
    			MainActivity.MessageBox("发送用户登录","statusCode:"+statusCode+",Info:"+strInfo);
    		}
    		public void onNetSuccess(int nCode,String strInfo,JSONObject oJsonData)
    		{
    			if(nCode < 0)
    			{
    				MainActivity.MessageBox("发送用户登录",strInfo);    				
    				return;
    			}
    			
    			try 
    			{
					JSONObject oData = oJsonData.getJSONObject("data");
					if(oData == null)
					{
						MainActivity.MessageBox("发送用户登录","取得登录数据失败，请检查网络后再登录");    				
	    				return;
					}
				} 
    			catch (JSONException e) 
    			{
					e.printStackTrace();
					MainActivity.MessageBox("发送用户登录",e.getMessage());    	
					return;
				}
    		}
    	};
    	 try 
         {
	    	JSONObject   requestDatas = new JSONObject();
	    	requestDatas.put("cellPhone",  "");
	    	requestDatas.put("password", "");
	    	
	    	svr.onPost("members/memLogin.do", requestDatas);
         }
    	 catch (JSONException e) 
         {
             e.printStackTrace();
             MainActivity.MessageBox("发送用户登录",e.getMessage());
         } 
	 * 
	 * 
	 * */
}