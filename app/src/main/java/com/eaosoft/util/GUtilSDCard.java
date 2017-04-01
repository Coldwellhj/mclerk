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
<!-- 往SDCard写入数据权限 -->
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
<!-- 在SDCard中创建与删除文件权限 -->
<uses-permission android:name="android.permission.MOUNT_UNMOUNT_FILESYSTEMS" />
*/
public class GUtilSDCard
{
	GUtilSDCard()
	{
		
	}
	//看下读取sd卡的： 
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
		      Log.d("", "block大小:"+ blockSize+",block数目:"+ blockCount+",总大小:"+blockSize*blockCount/1024+"KB");
		      Log.d("", "可用的block数目：:"+ availCount+",剩余空间:"+ availCount*blockSize/1024+"KB");
		      
		      return blockSize*availCount;
	     }  
	     return 0;
	    } 	
//下读取系统内部空间的：
	void readSystem() {
	     File root = Environment.getRootDirectory();
	  StatFs sf = new StatFs(root.getPath());
	  long blockSize = sf.getBlockSize();
	  long blockCount = sf.getBlockCount();
	  long availCount = sf.getAvailableBlocks();
	  Log.d("", "block大小:"+ blockSize+",block数目:"+ blockCount+",总大小:"+blockSize*blockCount/1024+"KB");
	  Log.d("", "可用的block数目：:"+ availCount+",可用大小:"+ availCount*blockSize/1024+"KB");
	    }    
	private void GetSDCardInfo()
	{
		/** 获取存储卡路径 */ 
		File sdcardDir=Environment.getExternalStorageDirectory(); 
		/** StatFs 看文件系统空间使用情况 */ 
		StatFs statFs=new StatFs(sdcardDir.getPath()); 
		/** Block 的 size*/ 
		long blockSize=statFs.getBlockSize(); 
		/** 总 Block 数量 */ 
		long totalBlocks=statFs.getBlockCount(); 
		/** 已使用的 Block 数量 */ 
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
		//创建文件
		File file = new File(FILE_PATH , FILE_NAME);
        file.createNewFile();
        
       //打开文件file的OutputStream
        OutputStream out = new FileOutputStream(file);
        String infoToWrite = "纸上得来终觉浅，绝知此事要躬行";
        //将字符串转换成byte数组写入文件
        out.write(infoToWrite.getBytes());
        //关闭文件file的OutputStream
        out.close();
        
       //打开文件file的InputStream
        FileInputStream in = new FileInputStream(file);
        //将文件内容全部读入到byte数组
        int length = (int)file.length();
        byte[] temp = new byte[length];
        in.read(temp, 0, length);
        //将byte数组用UTF-8编码并存入display字符串中
        //display =  EncodingUtils.getString(temp,TEXT_ENCODING);
        //关闭文件file的InputStream
        in.close();
    } catch (IOException e) {
        //将出错信息打印到Logcat
        Log.e("ReadFile", e.toString());
        //this.finish();
    }

    	//从资源读取

    	//InputStream is=getResources().getRawResource(R.raw.文件名)

	}
	public static void ConfirmAppBasePath(Context oContext,String strBasePath)
	{
		String szBasePath="";
    	if(MainActivity.m_szAppBasePath.length()<1)
    	{
    		GUtilSDCard sd= new GUtilSDCard();
    		if(sd.readSDCardSize()<1024*1024)//SD卡上至少有１MB的空间
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
			Toast.makeText(context,"未发现SD卡！",Toast.LENGTH_LONG).show();
			return "SD Card error";
		}
		
	}
	/***保存到sd卡下的.ainibaichi文件夹下的准确可行的代码****/
	public void saveToSDCard(String filename,String content) throws Exception{
		String dir=Environment.getExternalStorageDirectory()+"/.ainibaichi";
		
		java.io.File a=new java.io.File(dir);
       /***判断文件夹是否存在，不存在则创建***/
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
		Toast.makeText(context,"未发现SD卡！",Toast.LENGTH_LONG).show();
	}
	
	 
    
  }
	/**
	 * 检查是否存在SD卡
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
//创建文件
             file = new File(FILE_PATH , FILE_NAME);
             file.createNewFile();
             
            //打开文件file的OutputStream
             out = new FileOutputStream(file);
             String infoToWrite = "纸上得来终觉浅，绝知此事要躬行";
             //将字符串转换成byte数组写入文件
             out.write(infoToWrite.getBytes());
             //关闭文件file的OutputStream
             out.close();
             
            //打开文件file的InputStream
             in = new FileInputStream(file);
             //将文件内容全部读入到byte数组
             int length = (int)file.length();
             byte[] temp = new byte[length];
             in.read(temp, 0, length);
             //将byte数组用UTF-8编码并存入display字符串中
             display =  EncodingUtils.getString(temp,TEXT_ENCODING);
             //关闭文件file的InputStream
             in.close();
         } catch (IOException e) {
             //将出错信息打印到Logcat
             Log.e(TAG, e.toString());
             this.finish();
         }
 
//从资源读取
 
InputStream is=getResources().getRawResource(R.raw.文件名)
*/

/**
 * class name：FileService<BR>
 * class description：android文件的一些读取操作<BR>
 * PS： <BR>
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

	// 读取sd中的文件
	public String readSDCardFile(String path) throws IOException {
		File file = new File(path);
		FileInputStream fis = new FileInputStream(file);
		String result = streamRead(fis);
		return result;
	}

	// 在res目录下建立一个raw资源文件夹，这里的文件只能读不能写入。。。
	public String readRawFile(int fileId) throws IOException {
		// 取得输入流
		InputStream is = context.getResources().openRawResource(fileId);
		String result = streamRead(is);// 返回一个字符串
		return result;
	}

	private String streamRead(InputStream is) throws IOException {
		int buffersize = is.available();// 取得输入流的字节长度
		byte buffer[] = new byte[buffersize];
		is.read(buffer);// 将数据读入数组
		is.close();// 读取完毕后要关闭流。
		String result = EncodingUtils.getString(buffer, "UTF-8");// 设置取得的数据编码，防止乱码
		return result;
	}

	// 在assets文件夹下的文件，同样是只能读取不能写入
	public String readAssetsFile(String filename) throws IOException {
		// 取得输入流
		InputStream is = context.getResources().getAssets().open(filename);
		String result = streamRead(is);// 返回一个字符串
		return result;
	}

	// 往sd卡中写入文件
	public void writeSDCardFile(String path, byte[] buffer) throws IOException {
		File file = new File(path);
		FileOutputStream fos = new FileOutputStream(file);
		fos.write(buffer);// 写入buffer数组。如果想写入一些简单的字符，可以将String.getBytes()再写入文件;
		fos.close();
	}

	// 将文件写入应用的data/data的files目录下
	public void writeDateFile(String fileName, byte[] buffer) throws Exception {
		byte[] buf = fileName.getBytes("iso8859-1");
		fileName = new String(buf, "utf-8");
		// Context.MODE_PRIVATE：为默认操作模式，代表该文件是私有数据，只能被应用本身访问，在该模式下，写入的内容会覆盖原文件的内容，如果想把新写入的内容追加到原文件中。可以使用Context.MODE_APPEND
		// Context.MODE_APPEND：模式会检查文件是否存在，存在就往文件追加内容，否则就创建新文件。
		// Context.MODE_WORLD_READABLE和Context.MODE_WORLD_WRITEABLE用来控制其他应用是否有权限读写该文件。
		// MODE_WORLD_READABLE：表示当前文件可以被其他应用读取；MODE_WORLD_WRITEABLE：表示当前文件可以被其他应用写入。
		// 如果希望文件被其他应用读和写，可以传入：
		// openFileOutput("output.txt", Context.MODE_WORLD_READABLE +
		// Context.MODE_WORLD_WRITEABLE);
		FileOutputStream fos = context.openFileOutput(fileName,
				Context.MODE_APPEND);// 添加在文件后面
		fos.write(buffer);
		fos.close();
	}

	// 读取应用的data/data的files目录下文件数据
	public String readDateFile(String fileName) throws Exception {
		FileInputStream fis = context.openFileInput(fileName);
		String result = streamRead(fis);// 返回一个字符串
		return result;
	}
}
*/
