package com.eaosoft.util;


import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.IOException;

public class GUtilFile 
{  
    private static final String TAG = "GUtilFile";  
  
    public static File getCacheFile(String imageUri){  
        File cacheFile = null;  
        try {  
            if (Environment.getExternalStorageState().equals(  
                    Environment.MEDIA_MOUNTED)) {  
                File sdCardDir = Environment.getExternalStorageDirectory();  
                String fileName = getFileName(imageUri);  
                File dir = new File(sdCardDir.getCanonicalPath()     + AsynImageLoader.CACHE_DIR);  
                if (!dir.exists()) {  
                    dir.mkdirs();  
                }  
                cacheFile = new File(dir, fileName);  
                Log.i(TAG, "exists:" + cacheFile.exists() + ",dir:" + dir + ",file:" + fileName);  
            }    
        } catch (IOException e) {  
            e.printStackTrace();  
            Log.e(TAG, "getCacheFileError:" + e.getMessage());  
        }  
          
        return cacheFile;  
    }  
    public static boolean fileRename(String strSRC,String strDST)
    {
    	File src = new File(strSRC);
    	return src.renameTo(new File(strDST));
    
    }
    public static String   getFilePath(String path)
    {
    	int index = path.lastIndexOf("/");  
        return path.substring(0,index);  
    }
    public static String getFileName(String path) 
    {  
        int index = path.lastIndexOf("/");  
        return path.substring(index + 1);  
    }  
    /**
     * ɾ�������ļ�
     * @param   filePath    ��ɾ���ļ����ļ���
     * @return �ļ�ɾ���ɹ�����true�����򷵻�false
     */
    public static boolean deleteFile(String filePath) {
    File file = new File(filePath);
        if (file.isFile() && file.exists()) {
        return file.delete();
        }
        return false;
    }

    /**
     * ɾ���ļ����Լ�Ŀ¼�µ��ļ�
     * @param   filePath ��ɾ��Ŀ¼���ļ�·��
     * @return  Ŀ¼ɾ���ɹ�����true�����򷵻�false
     */
    public static  boolean deleteDirectory(String filePath) {
    boolean flag = false;
        //���filePath�����ļ��ָ�����β���Զ�����ļ��ָ���
        if (!filePath.endsWith(File.separator)) {
            filePath = filePath + File.separator;
        }
        File dirFile = new File(filePath);
        if (!dirFile.exists() || !dirFile.isDirectory()) {
            return false;
        }
        flag = true;
        File[] files = dirFile.listFiles();
        //����ɾ���ļ����µ������ļ�(������Ŀ¼)
        for (int i = 0; i < files.length; i++) {
            if (files[i].isFile()) {
            //ɾ�����ļ�
                flag = deleteFile(files[i].getAbsolutePath());
                if (!flag) break;
            } else {
            //ɾ����Ŀ¼
                flag = deleteDirectory(files[i].getAbsolutePath());
                if (!flag) break;
            }
        }
        if (!flag) return false;
        //ɾ����ǰ��Ŀ¼
        return dirFile.delete();
    }

    /**
     *  ����·��ɾ��ָ����Ŀ¼���ļ������۴������
     *@param filePath  Ҫɾ����Ŀ¼���ļ�
     *@return ɾ���ɹ����� true�����򷵻� false��
     */
    public static boolean DeleteFolder(String filePath) {
    File file = new File(filePath);
        if (!file.exists()) {
            return false;
        } else {
            if (file.isFile()) {
            // Ϊ�ļ�ʱ����ɾ���ļ�����
                return deleteFile(filePath);
            } else {
            // ΪĿ¼ʱ����ɾ��Ŀ¼����
                return deleteDirectory(filePath);
            }
        }
    }
}  