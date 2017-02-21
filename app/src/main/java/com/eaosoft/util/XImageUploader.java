package com.eaosoft.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.BinaryHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import cz.msebera.android.httpclient.Header;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.util.Log;

public class XImageUploader
{
	private static final String TAG = "XImageUploader";

    Context mContext;

    public XImageUploader() 
    {
    }

    public void uploadFile(String uri, String path) {

        File file = new File(path);
        RequestParams params = new RequestParams();

        try {
            params.put("image", file, "application/octet-stream");

            AsyncHttpClient client = new AsyncHttpClient();

            XHttpClientUtil.post(path, params, new AsyncHttpResponseHandler() 
            {
                @Override
                public void onSuccess(int statusCode, Header[] headers,byte[] responseBody) 
                {
                    Log.i(TAG, " statusCode=========" + statusCode);
                    Log.i(TAG, " statusCode=========" + headers);
                    Log.i(TAG, " statusCode====binaryData len====="+ responseBody.length);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers,byte[] responseBody, Throwable error) 
                {
                    Log.i(TAG, " statusCode=========" + statusCode);
                    Log.i(TAG, " statusCode=========" + headers);
                    Log.i(TAG, " statusCode====binaryData len====="+ responseBody.length);
                    Log.i(TAG," statusCode====error====="+ error.getLocalizedMessage());

                }

            });

        } catch (FileNotFoundException e) {

        }
    }
 }
