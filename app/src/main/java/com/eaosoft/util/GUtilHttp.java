package com.eaosoft.util;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.params.CoreProtocolPNames;


public class GUtilHttp
{
	public static String httpStringGet(String url) throws Exception 
	{
		return httpStringGet(url, "utf-8");
	}
	public static Drawable loadImage(String url) 
	{
		try 
		{
			return Drawable.createFromStream((InputStream) new URL(url).getContent(), "test");
		} 
		catch (MalformedURLException e) 
		{
			Log.e("exception", e.getMessage());
		} 
		catch (IOException e) 
		{
			Log.e("exception", e.getMessage());
		}
		return null;
	}

	public static String httpStringGet(String url, String enc) throws Exception 
	{
		// This method for HttpConnection
		String page = "";
		BufferedReader bufferedReader = null;
		try 
		{
			HttpClient client = new DefaultHttpClient();
			client.getParams().setParameter(CoreProtocolPNames.USER_AGENT,"android");
			
			 HttpParams httpParams =(HttpParams) client.getParams();
			 HttpConnectionParams.setConnectionTimeout(httpParams, 3000);
			 HttpConnectionParams.setSoTimeout(httpParams, 5000);
			 
			HttpGet request = new HttpGet();
			request.setHeader("Content-Type", "text/plain; charset=utf-8");
			request.setURI(new URI(url));
			HttpResponse response = client.execute(request);
			bufferedReader = new BufferedReader(new InputStreamReader(response
					.getEntity().getContent(), enc));

			StringBuffer stringBuffer = new StringBuffer("");
			String line = "";

			String NL = System.getProperty("line.separator");
			while ((line = bufferedReader.readLine()) != null) {
				stringBuffer.append(line + NL);
			}
			bufferedReader.close();
			page = stringBuffer.toString();
			Log.i("page", page);
			System.out.println(page + "page");
			return page;
		} finally {
			if (bufferedReader != null) {
				try {
					bufferedReader.close();
				} catch (IOException e) {
					Log.d("BBB", e.toString());
				}
			}
		}
	}
	public static String getJSONObjectValue(String strName,JSONObject o)
	{
		String strValue="";
		try
		{
			strValue = o.getString(strName);
			if(strValue.equalsIgnoreCase("null"))
				strValue="";
		}
		catch (JSONException e) 
		{
			e.printStackTrace();			
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return strValue;
	}
	public static boolean checkNetWorkStatus(Context context)
	{
		boolean result;
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netinfo = cm.getActiveNetworkInfo();
		if (netinfo != null && netinfo.isConnected()) 
		{
			result = true;
			Log.i("NetStatus", "The net was connected");
		} 
		else 
		{
			result = false;
			Log.i("NetStatus", "The net was bad!");
		}
		return result;
	}
}