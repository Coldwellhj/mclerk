package com.eaosoft.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import com.loopj.android.http.BinaryHttpResponseHandler;

import cz.msebera.android.httpclient.Header;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.util.Log;

/*
 http://wx1.sinaimg.cn/large/c0788b86ly1faltbcby18j20kv0rsmze0.jpg
 private static String URL_PHOTO = "http://img001.us.expono.com/100001/100001-1bc30-2d736f_m.jpg";
private void downloadTest() 
{
    String url =URL_PHOTO；
    String fileName = "" + System.currentTimeMillis() + ".jpg";
    String savePath = mContext.getCacheDir() + File.separator + fileName;
    XImageDownLoader mMyImageDownLoader = new XImageDownLoader();
        // 开始异步下载
    mMyImageDownLoader.downloadImage(url,savePath,new DownLoaderListener() 
    {
        @Override
        public void onResult(int res, String s) 
        {
            Log.i(TAG,"onResult====res===" + res);
            Log.i(TAG, "onResult====s===" + s);
            if (res == 0) 
            {
                // 下载成功
                Toast.makeText(mContext, "download success!", Toast.LENGTH_LONG).show();
                } 
                else
                 {
                    // 下载photo失败
                    Toast.makeText(mContext, "download fail!", Toast.LENGTH_LONG).show();
                }
            }
        });
    } 
 * */
public class XImageDownLoader
{
	private static final String TAG = "XImageDownLoader";

    Context mContext;

    public interface DownLoaderListener 
    {
        public void onResult(int res, String s);
    }

    public XImageDownLoader() 
    {

    }


  //download image
    public void downloadImage(String uri, String savePath, DownLoaderListener downLoaderListener){  
        // 指定文件类型  
        String[] allowedContentTypes = new String[] { "image/png", "image/jpeg" };  


        XHttpClientUtil.get(uri, new ImageResponseHandler(allowedContentTypes,savePath, downLoaderListener));  
      }  

      public class ImageResponseHandler extends BinaryHttpResponseHandler
      {  
          private String[] allowedContentTypes;  
          private String savePathString;
          DownLoaderListener mDownLoaderListener;

          public ImageResponseHandler(String[] allowedContentTypes, String path, DownLoaderListener downLoaderListener){  
              super();  
              this.allowedContentTypes = allowedContentTypes;  
              savePathString = path;
              mDownLoaderListener = downLoaderListener;
          }  

        @Override
        public void onSuccess(int statusCode, Header[] headers,
                byte[] binaryData) {
            Log.i(TAG, " statusCode=========" + statusCode);
            Log.i(TAG, " statusCode=========" + headers);
            Log.i(TAG, " statusCode====binaryData len=====" + binaryData.length);
            if (statusCode == 200 && binaryData!=null && binaryData.length > 0) {
                boolean b = saveImage(binaryData,savePathString); 
                if (b) {

                    mDownLoaderListener.onResult(0, savePathString);


                }
                else {
                    //fail
                    mDownLoaderListener.onResult(-1, savePathString);
                }
            }
        }
        @Override
        public void onFailure(int statusCode, Header[] headers,
                byte[] binaryData, Throwable error) {
            Log.i(TAG, "download failed");
        }  

        private boolean saveImage(byte[] binaryData, String savePath) {
            Bitmap bmp = BitmapFactory.decodeByteArray(binaryData, 0,  
                    binaryData.length);  

            Log.i(TAG,"saveImage==========" +  savePath);
            File file = new File(savePath);  
            // 压缩格式  
            CompressFormat format = Bitmap.CompressFormat.JPEG;  
            // 压缩比例  
            int quality = 100;  
            try {  
                if (file.createNewFile()){
                    OutputStream stream = new FileOutputStream(file);  
                    // 压缩输出  
                    bmp.compress(format, quality, stream);  
                    stream.close();  
                    return true;
                }

            } catch (IOException e) { 
                Log.i(TAG,"saveImage====003======" +  savePath);
                e.printStackTrace();  
            }  
            Log.i(TAG,"saveImage====004======" +  savePath);
            return false;
        }
      }
}