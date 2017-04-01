package com.eaosoft.util;


import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Calendar;
import java.util.Locale;



import com.eaosoft.mclerk.MainActivity;




import android.content.Context;
import android.os.Environment;
import android.os.StatFs;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.Toast;
/*
<!-- ��SDCardд������Ȩ�� -->
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
<!-- ��SDCard�д�����ɾ���ļ�Ȩ�� -->
<uses-permission android:name="android.permission.MOUNT_UNMOUNT_FILESYSTEMS" />
*/
public class GUtilSDCard
{
	GUtilSDCard()
	{
		
	}
	//���¶�ȡsd���ģ� 
	public long readSDCardSize() 
	{
	     String state = Environment.getExternalStorageState();
	     if(Environment.MEDIA_MOUNTED.equals(state)) 
	     {
		      File sdcardDir = Environment.getExternalStorageDirectory();
		      StatFs sf = new StatFs(sdcardDir.getPath());
		      long blockSize = sf.getBlockSize();
		      long blockCount = sf.getBlockCount();
		      long availCount = sf.getAvailableBlocks();
		      Log.d("", "block��С:"+ blockSize+",block��Ŀ:"+ blockCount+",�ܴ�С:"+blockSize*blockCount/1024+"KB");
		      Log.d("", "���õ�block��Ŀ��:"+ availCount+",ʣ��ռ�:"+ availCount*blockSize/1024+"KB");
		      
		      return blockSize*availCount;
	     }  
	     return 0;
	    } 	
//�¶�ȡϵͳ�ڲ��ռ�ģ�
	void readSystem() {
	     File root = Environment.getRootDirectory();
	  StatFs sf = new StatFs(root.getPath());
	  long blockSize = sf.getBlockSize();
	  long blockCount = sf.getBlockCount();
	  long availCount = sf.getAvailableBlocks();
	  Log.d("", "block��С:"+ blockSize+",block��Ŀ:"+ blockCount+",�ܴ�С:"+blockSize*blockCount/1024+"KB");
	  Log.d("", "���õ�block��Ŀ��:"+ availCount+",���ô�С:"+ availCount*blockSize/1024+"KB");
	    }    
	private void GetSDCardInfo()
	{
		/** ��ȡ�洢��·�� */ 
		File sdcardDir=Environment.getExternalStorageDirectory(); 
		/** StatFs ���ļ�ϵͳ�ռ�ʹ����� */ 
		StatFs statFs=new StatFs(sdcardDir.getPath()); 
		/** Block �� size*/ 
		long blockSize=statFs.getBlockSize(); 
		/** �� Block ���� */ 
		long totalBlocks=statFs.getBlockCount(); 
		/** ��ʹ�õ� Block ���� */ 
		long availableBlocks=statFs.getAvailableBlocks(); 

	}
	public static boolean isFolderExists(String strFolder) {
        File file = new File(strFolder);        
        if (!file.exists()) {
            if (file.mkdirs()) {                
                return true;
            } else {
                return false;

            }
        }
        return true;

    }
	public static String OnCreatePhotoFilePath(String szExt,Context oContext)
	{
		String szPath = "";
		
		szPath = MainActivity.m_szAppBasePath+"PG/";
		GUtilSDCard.isFolderExists(szPath);
		if(szExt.length()>0)
			szPath = szPath+ new DateFormat().format("yyyyMMdd_hhmmss_S", Calendar.getInstance(Locale.CHINA)) +"_"+(System.currentTimeMillis()%1000)+ szExt;//".jpg";
		return szPath;
	}
	private void FileRead(String FILE_PATH,String FILE_NAME)
	{
		try
		{
		//�����ļ�
		File file = new File(FILE_PATH , FILE_NAME);
        file.createNewFile();
        
       //���ļ�file��OutputStream
        OutputStream out = new FileOutputStream(file);
        String infoToWrite = "ֽ�ϵ����վ�ǳ����֪����Ҫ����";
        //���ַ���ת����byte����д���ļ�
        out.write(infoToWrite.getBytes());
        //�ر��ļ�file��OutputStream
        out.close();
        
       //���ļ�file��InputStream
        FileInputStream in = new FileInputStream(file);
        //���ļ�����ȫ�����뵽byte����
        int length = (int)file.length();
        byte[] temp = new byte[length];
        in.read(temp, 0, length);
        //��byte������UTF-8���벢����display�ַ�����
        //display =  EncodingUtils.getString(temp,TEXT_ENCODING);
        //�ر��ļ�file��InputStream
        in.close();
    } catch (IOException e) {
        //��������Ϣ��ӡ��Logcat
        Log.e("ReadFile", e.toString());
        //this.finish();
    }

    	//����Դ��ȡ

    	//InputStream is=getResources().getRawResource(R.raw.�ļ���)

	}
	public static void ConfirmAppBasePath(Context oContext,String strBasePath)
	{
		String szBasePath="";
    	if(MainActivity.m_szAppBasePath.length()<1)
    	{
    		GUtilSDCard sd= new GUtilSDCard();
    		if(sd.readSDCardSize()<1024*1024)//SD���������У�MB�Ŀռ�
    		{
    			szBasePath = oContext.getFilesDir().getAbsolutePath();
    			MainActivity.m_szAppBasePath = szBasePath+strBasePath;
    		}
    		else
    		{
    			File sdcardDir=Environment.getExternalStorageDirectory();
    			if(sdcardDir.exists())
    			{
    				szBasePath = sdcardDir.toString();
    				MainActivity.m_szAppBasePath = szBasePath + strBasePath;
    			}
    			else
    			{
    				szBasePath = oContext.getFilesDir().getAbsolutePath();
	    			MainActivity.m_szAppBasePath = szBasePath+strBasePath;
    			}
    		}
    	
    		//GUtilSDCard.isFolderExists(szBasePath);
    	}
    	if(MainActivity.m_szAppBasePath.length()>2)
        {
    		int nLen = MainActivity.m_szAppBasePath.length();
    		szBasePath = MainActivity.m_szAppBasePath.substring(nLen-1); 
	        if(szBasePath.equalsIgnoreCase("//"))
	        	MainActivity.m_szAppBasePath += "/";
        }
    	
    	GUtilSDCard.isFolderExists(MainActivity.m_szAppBasePath);
	}
	private String readFromFile(Context context){
		
		
		if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
			
			String foldername = Environment.getExternalStorageDirectory().getPath()+ "/eryaApp";
		    File folder = new File(foldername);
		    
		    if (folder == null || !folder.exists()) {
		    	folder.mkdir();
		    }
		    
		    File targetFile=new File("/sdcard/eryaApp/eryaShoppingList.txt");
		    String readedStr="";
		    
			 try{
				if(!targetFile.exists()){
					targetFile.createNewFile();
					return "No File error ";
				}else{
					 InputStream in = new BufferedInputStream(new FileInputStream(targetFile));
					 BufferedReader br= new BufferedReader(new InputStreamReader(in, "UTF-8"));
					 String tmp;
					 
					 while((tmp=br.readLine())!=null){
						 readedStr+=tmp;
					 }
					 br.close();
					 in.close();
					 
					 return readedStr;
				}
			 } catch (Exception e) {
					Toast.makeText(context,e.toString(),Toast.LENGTH_LONG).show();
					return e.toString();
			 }
		}else{
			Toast.makeText(context,"δ����SD����",Toast.LENGTH_LONG).show();
			return "SD Card error";
		}
		
	}
	/***���浽sd���µ�.ainibaichi�ļ����µ�׼ȷ���еĴ���****/
	public void saveToSDCard(String filename,String content) throws Exception{
		String dir=Environment.getExternalStorageDirectory()+"/.ainibaichi";
		
		java.io.File a=new java.io.File(dir);
       /***�ж��ļ����Ƿ���ڣ��������򴴽�***/
		if (!a.exists()){
			a.mkdir();
		}
		File file=new File(a,filename);
		FileOutputStream outStream=new FileOutputStream(file);
		outStream.write(content.getBytes());
		outStream.close();
	}
	

		public static File createSDDir(String SDPATH,String dirName) {
			File dir = new File(SDPATH + dirName);
			dir.mkdir();
			return dir;
		}

		public static boolean isFileExist(String SDPATH,String fileName) {
			File file = new File(SDPATH + fileName);
			return file.exists();
		}
		public static boolean isFileExist(String fileName) {
			File file = new File(fileName);
			return file.exists();
		}
	
	private void SavedToText(Context context,String stringToWrite){
	
	if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
		
		String foldername = Environment.getExternalStorageDirectory().getPath()+ "/eryaApp";
	    File folder = new File(foldername);
	    
	    if (folder == null || !folder.exists()) {
	    	folder.mkdir();
	    }
	    
	    String fileName="/eryaShoppingList"+".txt";
		
		File targetFile = new File(foldername + fileName);
		OutputStreamWriter osw;
        
		 try{
			if(!targetFile.exists()){
				targetFile.createNewFile();
				osw = new OutputStreamWriter(new FileOutputStream(targetFile),"utf-8");
				osw.write(stringToWrite);  
				osw.close();
			}else{
				osw = new OutputStreamWriter(new FileOutputStream(targetFile,true),"utf-8");
				osw.write("\n"+stringToWrite);
				osw.flush();
				osw.close();
			}
		 } catch (Exception e) {
			Toast.makeText(context,e.toString(),Toast.LENGTH_LONG).show();
		 }
	}else{
		Toast.makeText(context,"δ����SD����",Toast.LENGTH_LONG).show();
	}
	
	 
    
  }
	/**
	 * ����Ƿ����SD��
	 */
	public static boolean hasSdcard() {
		String state = Environment.getExternalStorageState();
		if (state.equals(Environment.MEDIA_MOUNTED)) {
			return true;
		} else {
			return false;
		}
	}
}
/*
//�����ļ�
             file = new File(FILE_PATH , FILE_NAME);
             file.createNewFile();
             
            //���ļ�file��OutputStream
             out = new FileOutputStream(file);
             String infoToWrite = "ֽ�ϵ����վ�ǳ����֪����Ҫ����";
             //���ַ���ת����byte����д���ļ�
             out.write(infoToWrite.getBytes());
             //�ر��ļ�file��OutputStream
             out.close();
             
            //���ļ�file��InputStream
             in = new FileInputStream(file);
             //���ļ�����ȫ�����뵽byte����
             int length = (int)file.length();
             byte[] temp = new byte[length];
             in.read(temp, 0, length);
             //��byte������UTF-8���벢����display�ַ�����
             display =  EncodingUtils.getString(temp,TEXT_ENCODING);
             //�ر��ļ�file��InputStream
             in.close();
         } catch (IOException e) {
             //��������Ϣ��ӡ��Logcat
             Log.e(TAG, e.toString());
             this.finish();
         }
 
//����Դ��ȡ
 
InputStream is=getResources().getRawResource(R.raw.�ļ���)
*/

/**
 * class name��FileService<BR>
 * class description��android�ļ���һЩ��ȡ����<BR>
 * PS�� <BR>
 * 
 * @version 1.00 2010/10/21
 * @author CODYY)peijiangping
 */
/*
public class FileService {
	private Context context;

	public FileService(Context c) {
		this.context = c;
	}

	// ��ȡsd�е��ļ�
	public String readSDCardFile(String path) throws IOException {
		File file = new File(path);
		FileInputStream fis = new FileInputStream(file);
		String result = streamRead(fis);
		return result;
	}

	// ��resĿ¼�½���һ��raw��Դ�ļ��У�������ļ�ֻ�ܶ�����д�롣����
	public String readRawFile(int fileId) throws IOException {
		// ȡ��������
		InputStream is = context.getResources().openRawResource(fileId);
		String result = streamRead(is);// ����һ���ַ���
		return result;
	}

	private String streamRead(InputStream is) throws IOException {
		int buffersize = is.available();// ȡ�����������ֽڳ���
		byte buffer[] = new byte[buffersize];
		is.read(buffer);// �����ݶ�������
		is.close();// ��ȡ��Ϻ�Ҫ�ر�����
		String result = EncodingUtils.getString(buffer, "UTF-8");// ����ȡ�õ����ݱ��룬��ֹ����
		return result;
	}

	// ��assets�ļ����µ��ļ���ͬ����ֻ�ܶ�ȡ����д��
	public String readAssetsFile(String filename) throws IOException {
		// ȡ��������
		InputStream is = context.getResources().getAssets().open(filename);
		String result = streamRead(is);// ����һ���ַ���
		return result;
	}

	// ��sd����д���ļ�
	public void writeSDCardFile(String path, byte[] buffer) throws IOException {
		File file = new File(path);
		FileOutputStream fos = new FileOutputStream(file);
		fos.write(buffer);// д��buffer���顣�����д��һЩ�򵥵��ַ������Խ�String.getBytes()��д���ļ�;
		fos.close();
	}

	// ���ļ�д��Ӧ�õ�data/data��filesĿ¼��
	public void writeDateFile(String fileName, byte[] buffer) throws Exception {
		byte[] buf = fileName.getBytes("iso8859-1");
		fileName = new String(buf, "utf-8");
		// Context.MODE_PRIVATE��ΪĬ�ϲ���ģʽ��������ļ���˽�����ݣ�ֻ�ܱ�Ӧ�ñ�����ʣ��ڸ�ģʽ�£�д������ݻḲ��ԭ�ļ������ݣ���������д�������׷�ӵ�ԭ�ļ��С�����ʹ��Context.MODE_APPEND
		// Context.MODE_APPEND��ģʽ�����ļ��Ƿ���ڣ����ھ����ļ�׷�����ݣ�����ʹ������ļ���
		// Context.MODE_WORLD_READABLE��Context.MODE_WORLD_WRITEABLE������������Ӧ���Ƿ���Ȩ�޶�д���ļ���
		// MODE_WORLD_READABLE����ʾ��ǰ�ļ����Ա�����Ӧ�ö�ȡ��MODE_WORLD_WRITEABLE����ʾ��ǰ�ļ����Ա�����Ӧ��д�롣
		// ���ϣ���ļ�������Ӧ�ö���д�����Դ��룺
		// openFileOutput("output.txt", Context.MODE_WORLD_READABLE +
		// Context.MODE_WORLD_WRITEABLE);
		FileOutputStream fos = context.openFileOutput(fileName,
				Context.MODE_APPEND);// ������ļ�����
		fos.write(buffer);
		fos.close();
	}

	// ��ȡӦ�õ�data/data��filesĿ¼���ļ�����
	public String readDateFile(String fileName) throws Exception {
		FileInputStream fis = context.openFileInput(fileName);
		String result = streamRead(fis);// ����һ���ַ���
		return result;
	}
}
*/
