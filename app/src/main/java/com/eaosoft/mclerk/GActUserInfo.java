package com.eaosoft.mclerk;


import java.io.File;
import java.sql.Date;
import java.text.SimpleDateFormat;

import org.json.JSONException;
import org.json.JSONObject;



import com.eaosoft.userinfo.GOperaterInfo;
import com.eaosoft.util.ActivityCollector;
import com.eaosoft.util.GSvrChannel;
import com.eaosoft.util.GUtilFile;
import com.eaosoft.util.GUtilImage;
import com.eaosoft.util.GUtilSDCard;
import com.eaosoft.view.RoundImageView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.CompoundButton.OnCheckedChangeListener;
public class GActUserInfo extends  Activity 
{
	private RoundImageView m_oHeadImage=null;
	private RoundImageView m_oHeadCamera=null;
	private String m_strMemberHeadImage="";
	private String m_strHeadImage="";
	// ����item
	private String[] items = new String[] { "�������ѡ��", "����" };

	private File tempFile = new File(Environment.getExternalStorageDirectory(),	getPhotoFileName());

	// ������
	private static final int IMAGE_REQUEST_CODE = 0;// �����������
	private static final int CAMERA_REQUEST_CODE = 1;// ����������
	private static final int RESULT_REQUEST_CODE = 2;// ���������
	
	protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.act_user_base_info);

		ActivityCollector.addActivity(this);			
		onInitUserInfo();		
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
	  m_oHeadCamera =  (RoundImageView)this.findViewById(R.id.iv_camera);
	  m_oHeadImage = (RoundImageView)this.findViewById(R.id.iv_uimage);
	  if(m_oHeadImage == null)
		  return;
	  if(GUtilSDCard.isFileExist(GOperaterInfo.m_strLocalDiskImage))
	  {
		  Bitmap photo = BitmapFactory.decodeFile(GOperaterInfo.m_strLocalDiskImage);
		  if(photo !=null && m_oHeadImage!=null)
			m_oHeadImage.setImageBitmap(photo);
		  if(m_oHeadCamera!=null)
			  m_oHeadCamera.setVisibility(View.GONE);
	  }
	  RelativeLayout 	mRlChangePass = (RelativeLayout) findViewById(R.id.rl_info);
	  RelativeLayout	mRlChangeHead = (RelativeLayout) findViewById(R.id.rl_head_info);
	  RelativeLayout	mRlChangeUser = (RelativeLayout) findViewById(R.id.rl_logout_info);
	  mRlChangePass.setOnClickListener(rl_click_listener);
	  mRlChangeHead.setOnClickListener(rl_click_listener);
	  mRlChangeUser.setOnClickListener(rl_click_listener);
  }
  private void onCameraImage()
  {
	  showDialog();
  }
  OnClickListener rl_click_listener = new OnClickListener() 
  {
		@Override
		public void onClick(View v) 
		{
			switch (v.getId()) 
			{
				case R.id.rl_head_info:onCameraImage();break;
				case R.id.rl_info:
				{
					startActivity(new Intent(GActUserInfo.this, GActUserChangePass.class));
				}break;
				case R.id.rl_logout_info:
				{
					AlertDialog.Builder builder = new Builder(GActUserInfo.this);
			        builder.setMessage("ȷ��Ҫע���˳���?");
			        builder.setTitle("��ʾ");
			        builder.setIcon(android.R.drawable.ic_dialog_alert);
			        builder.setPositiveButton("ȷ��",
			                new DialogInterface.OnClickListener() 
			        		{
			                    public void onClick(DialogInterface dialog, int which) 
			                    {
			                        dialog.dismiss();
			                        MainActivity.m_oOperaterInfo.onUserLogout();
			    					MainActivity.m_oMsgHandler.sendEmptyMessage(MainActivity.FINISH_APP);			                        
			                    }
			                });
			       
			        builder.setNegativeButton("ȡ��",
			                new android.content.DialogInterface.OnClickListener() 
			        		{
			                    public void onClick(DialogInterface dialog, int which) 
			                    {
			                        dialog.dismiss();
			                    }
			                });
			       
			        builder.create().show();
					
				}break;
			}
		}
  };
  private void showDialog() 
	{
		new AlertDialog.Builder(this)
				.setTitle("����ͷ��")
				.setItems(items, new DialogInterface.OnClickListener() 
				{
					@Override
					public void onClick(DialogInterface arg0, int which) 
					{
						switch (which) {
						case 0:
							// �������ѡ��
							Intent intentFromImage = new Intent();
							intentFromImage.setType("image/*");
							intentFromImage.setAction(Intent.ACTION_GET_CONTENT);
							startActivityForResult(intentFromImage,IMAGE_REQUEST_CODE);
							break;
						case 1:
							// ����
							Intent intentFromCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
							if (GUtilSDCard.hasSdcard()) 
							{
								// ָ������������պ���Ƭ�Ĵ���·��
								intentFromCamera.putExtra(MediaStore.EXTRA_OUTPUT,Uri.fromFile(tempFile));
							}
							startActivityForResult(intentFromCamera,CAMERA_REQUEST_CODE);
							break;
						default:
							break;
						}
					}
				})
				.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface arg0, int arg1) 
					{
						// ���ضԻ���,�ͷŶԻ�����ռ����Դ
						arg0.dismiss();
					}
				}).show();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) 
	{
		switch (requestCode) 
		{
		case IMAGE_REQUEST_CODE:
			startPhotoZoom(data.getData());
			break;
		case CAMERA_REQUEST_CODE:
			startPhotoZoom(Uri.fromFile(tempFile));
			break;
		case RESULT_REQUEST_CODE:
			if (data != null) 
			{
				sentPicToNext(data);
			}
			break;
		default:
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	/**
	 * ʹ��ϵͳ��ǰ���ڼ��Ե�����Ϊ��Ƭ������
	 * 
	 * @return
	 */
	private String getPhotoFileName() 
	{
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat dateFormat = new SimpleDateFormat("'img'_yyyyMMdd_HHmmss");

		return dateFormat.format(date) + ".jpg";
	}

	/**
	 * ����ϵͳ�ü����ܣ�
	 * 
	 * @param fromFile
	 */
	private void startPhotoZoom(Uri fromFile) 
	{
		Intent intent = new Intent("com.android.camera.action.CROP");  
      if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) 
      {  
          String url=getPath(this,fromFile);  
          intent.setDataAndType(Uri.fromFile(new File(url)), "image/*");  
      }else{  
          intent.setDataAndType(fromFile, "image/*");  
      }  
        
      // ���òü�  
      intent.putExtra("crop", "true");  
      // aspectX aspectY �ǿ�ߵı���  
      intent.putExtra("aspectX", 1);  
      intent.putExtra("aspectY", 1);  
      // outputX outputY �ǲü�ͼƬ���  
      intent.putExtra("outputX", 300);  
      intent.putExtra("outputY", 300);  
      intent.putExtra("return-data", true);  
      startActivityForResult(intent, RESULT_REQUEST_CODE);  
	}
	private void OnSetMemberHeadImage(String strHDImage)
	{
		GSvrChannel svr= 	new GSvrChannel()
	  	{
	  		public void onNetFailure(int statusCode,String strInfo)
	  		{
	  			MainActivity.MessageBox("�û�ͷ���ϴ�","statusCode:"+statusCode+",Info:"+strInfo);
	  		}
	  		public void onNetSuccess(int nCode,String strInfo,JSONObject oJsonData)
	  		{
	  			if(nCode < 0)
	  			{
	  				MainActivity.MessageBox("�û�ͷ���ϴ�",strInfo);    				
	  				return;
	  			}
	  			SharedPreferences prefere = MainActivity.m_oMainActivity.getSharedPreferences(MainActivity.m_szMainConfigString,Context.MODE_PRIVATE);
					prefere.edit().putBoolean("RegUserHeadImageUpload", true).commit();//�ϴ��ɹ�
					prefere.edit().putString("RegUserHeadImage", m_strMemberHeadImage).commit();
					GOperaterInfo.m_strHeadImage = m_strMemberHeadImage;//������ֵ
					GUtilFile.fileRename(m_strHeadImage,GOperaterInfo.m_strImagePath+m_strMemberHeadImage);
					
					MainActivity.MessageBox("�û�ͷ���ϴ�","����ͷ���ϴ��ɹ�");    			
	  		}
	  	};
	  	 try 
	       {
	  		JSONObject   requestDatas = new JSONObject();
		    	requestDatas.put("fileSaveName",  strHDImage);
		    	svr.onPost("api/mobile/opHeaderImageSet.do", requestDatas);
	       }
	  	 catch (JSONException e) 
	       {
	           e.printStackTrace();
	           MainActivity.MessageBox("�û�ͷ���ϴ�",e.getMessage());
	       }
	}
	private void OnUploadHeadImage(String strImage)
	{
		GSvrChannel svr= 	new GSvrChannel()
  	{    		
  		public void onNetFailure(int statusCode,String strInfo)
  		{
  			MainActivity.MessageBox("�û�ͷ���ϴ�","statusCode:"+statusCode+",Info:"+strInfo);
  		}
  		public void onNetSuccess(int nCode,String strInfo,JSONObject oJsonData)
  		{
  			if(nCode < 0)
  			{
  				MainActivity.MessageBox("�û�ͷ���ϴ�",strInfo);    				
  				return;
  			}
  			
  			try 
  			{
					JSONObject oData = oJsonData.getJSONObject("data");
					if(oData==null)
					{
						MainActivity.MessageBox("�û�ͷ���ϴ�","ͷ���ϴ����ݽ���ʧ�ܣ�����������ٴ���");    			
						return;
					}
					m_strMemberHeadImage = oData.getString("fileSaveName");
					OnSetMemberHeadImage(m_strMemberHeadImage);
				} 
  			catch (JSONException e) 
  			{
					e.printStackTrace();
					MainActivity.MessageBox("�û�ͷ���ϴ�","ͷ���ϴ�ʧ�ܣ�����������ٴ���");    			
					return;
				}    			
  		}
  	};
  	 try{svr.uploadFile("api/comm/binaryFileUpload.do", strImage);}
  	 catch (Exception e) 
  	 {
			e.printStackTrace();
			MainActivity.MessageBox("�û�ͷ���ϴ�",e.getMessage());
		}
	}
	/**
	 * ����ü����ͼƬ
	 * 
	 * @param data
	 */
	private void sentPicToNext(Intent data) 
	{
		Bundle extras = data.getExtras();
		if (extras != null) 
		{
			Bitmap photo = extras.getParcelable("data");
			m_oHeadImage.setImageBitmap(photo);
			//==========================================
			//���浽���ش���
			if(m_strHeadImage.length()<1)
			{
				Date date = new Date(System.currentTimeMillis());
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
				m_strHeadImage = GOperaterInfo.m_strImagePath+"HDI_"+dateFormat.format(date) + ".jpg";				
			}
			GUtilImage.writeTofiles(this, photo, m_strHeadImage);
			SharedPreferences prefere = this.getSharedPreferences("HTChinaUserRegInfo",Context.MODE_PRIVATE);
			prefere.edit().putString("RegUserHeadImageFile", m_strHeadImage).commit();
			prefere.edit().putBoolean("RegUserHeadImageUpload", false).commit();//û���ϴ�
			OnUploadHeadImage(m_strHeadImage);
		}
	}
	//�����ǹؼ���ԭ��uri���ص���file:///...���ŵģ�android4.4���ص���content:///...  
  
  @SuppressLint("NewApi")
  public static String getPath(final Context context, final Uri uri) 
  {  

      final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;  

      // DocumentProvider  
      if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {  
          // ExternalStorageProvider  
          if (isExternalStorageDocument(uri)) {  
              final String docId = DocumentsContract.getDocumentId(uri);  
              final String[] split = docId.split(":");  
              final String type = split[0];  

              if ("primary".equalsIgnoreCase(type)) {  
                  return Environment.getExternalStorageDirectory() + "/" + split[1];  
              }  

          }  
          // DownloadsProvider  
          else if (isDownloadsDocument(uri)) {  
              final String id = DocumentsContract.getDocumentId(uri);  
              final Uri contentUri = ContentUris.withAppendedId(  
                      Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));  

              return getDataColumn(context, contentUri, null, null);  
          }  
          // MediaProvider  
          else if (isMediaDocument(uri)) {  
              final String docId = DocumentsContract.getDocumentId(uri);  
              final String[] split = docId.split(":");  
              final String type = split[0];  

              Uri contentUri = null;  
              if ("image".equals(type)) {  
                  contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;  
              } else if ("video".equals(type)) {  
                  contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;  
              } else if ("audio".equals(type)) {  
                  contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;  
              }  

              final String selection = "_id=?";  
              final String[] selectionArgs = new String[] {  
                      split[1]  
              };  

              return getDataColumn(context, contentUri, selection, selectionArgs);  
          }  
      }  
      // MediaStore (and general)  
      else if ("content".equalsIgnoreCase(uri.getScheme())) {  
          // Return the remote address  
          if (isGooglePhotosUri(uri))  
              return uri.getLastPathSegment();  

          return getDataColumn(context, uri, null, null);  
      }  
      // File  
      else if ("file".equalsIgnoreCase(uri.getScheme())) {  
          return uri.getPath();  
      }  

      return null;  
  }  
  /** 
   * Get the value of the data column for this Uri. This is useful for 
   * MediaStore Uris, and other file-based ContentProviders. 
   * 
   * @param context The context. 
   * @param uri The Uri to query. 
   * @param selection (Optional) Filter used in the query. 
   * @param selectionArgs (Optional) Selection arguments used in the query. 
   * @return The value of the _data column, which is typically a file path. 
   */  
  public static String getDataColumn(Context context, Uri uri, String selection,  
          String[] selectionArgs) {  

      Cursor cursor = null;  
      final String column = "_data";  
      final String[] projection = {  
              column  
      };  

      try {  
          cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,  
                  null);  
          if (cursor != null && cursor.moveToFirst()) {  
              final int index = cursor.getColumnIndexOrThrow(column);  
              return cursor.getString(index);  
          }  
      } finally {  
          if (cursor != null)  
              cursor.close();  
      }  
      return null;  
  }  


  /** 
   * @param uri The Uri to check. 
   * @return Whether the Uri authority is ExternalStorageProvider. 
   */  
  public static boolean isExternalStorageDocument(Uri uri) {  
      return "com.android.externalstorage.documents".equals(uri.getAuthority());  
  }  

  /** 
   * @param uri The Uri to check. 
   * @return Whether the Uri authority is DownloadsProvider. 
   */  
  public static boolean isDownloadsDocument(Uri uri) {  
      return "com.android.providers.downloads.documents".equals(uri.getAuthority());  
  }  

  /** 
   * @param uri The Uri to check. 
   * @return Whether the Uri authority is MediaProvider. 
   */  
  public static boolean isMediaDocument(Uri uri) {  
      return "com.android.providers.media.documents".equals(uri.getAuthority());  
  }  

  /** 
   * @param uri The Uri to check. 
   * @return Whether the Uri authority is Google Photos. 
   */  
  public static boolean isGooglePhotosUri(Uri uri) {  
      return "com.google.android.apps.photos.content".equals(uri.getAuthority());  
  }  			
}