package com.eaosoft.util;

import java.io.ByteArrayOutputStream; 
import java.security.MessageDigest;

public class GUtilBase64 
{ 
	public static final char[] base64EncodeChars = new char[] { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '+', '/' }; 
	
	private static byte[] base64DecodeChars = new byte[] { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 62, -1, -1, -1, 63, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, -1, -1, -1, -1, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -1, -1, -1, -1, -1, -1, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, -1, -1, -1, -1, -1 }; 
	private static final char[] legalChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/"  
            .toCharArray();  
	GUtilBase64() 
	{
	} 
	public final static String MD5(String s) 
	{
        char hexDigits[]={'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};       
        try 
        {
            byte[] btInput = s.getBytes();
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
	public static String encodex(byte[] data) 
	{  
        int start = 0;  
        int len = data.length;  
        StringBuffer buf = new StringBuffer(data.length * 3 / 2);  
  
        int end = len - 3;  
        int i = start;  
        int n = 0;  
  
        while (i <= end) {  
            int d = ((((int) data[i]) & 0x0ff) << 16)  
                    | ((((int) data[i + 1]) & 0x0ff) << 8)  
                    | (((int) data[i + 2]) & 0x0ff);  
  
            buf.append(legalChars[(d >> 18) & 63]);  
            buf.append(legalChars[(d >> 12) & 63]);  
            buf.append(legalChars[(d >> 6) & 63]);  
            buf.append(legalChars[d & 63]);  
  
            i += 3;  
  
            if (n++ >= 14) {  
                n = 0;  
                buf.append(" ");  
            }  
        }  
  
        if (i == start + len - 2) {  
            int d = ((((int) data[i]) & 0x0ff) << 16)  
                    | ((((int) data[i + 1]) & 255) << 8);  
  
            buf.append(legalChars[(d >> 18) & 63]);  
            buf.append(legalChars[(d >> 12) & 63]);  
            buf.append(legalChars[(d >> 6) & 63]);  
            buf.append("=");  
        } else if (i == start + len - 1) {  
            int d = (((int) data[i]) & 0x0ff) << 16;  
  
            buf.append(legalChars[(d >> 18) & 63]);  
            buf.append(legalChars[(d >> 12) & 63]);  
            buf.append("==");  
        }  
  
        return buf.toString();  
    }  
  
/** 
* 将字节数组编码为字符串 
* 
* @param data 
*/ 
public static String encode(String szVal){return encode(szVal.getBytes(),szVal.length());} 
public static String encode(byte[] data,int nLength) 
{ 
	StringBuffer sb = new StringBuffer(); 
	int len = nLength;//data.length; 
	int i = 0; 
	int b1, b2, b3; 
	
	while (i < len) 
	{ 
		b1 = data[i++] & 0xff; 
		if (i == len) 
		{ 
			sb.append(base64EncodeChars[b1 >>> 2]); 
			sb.append(base64EncodeChars[(b1 & 0x3) << 4]); 
			sb.append("=="); 
			break; 
		} 
		b2 = data[i++] & 0xff; 
		if (i == len) 
		{ 
			sb.append(base64EncodeChars[b1 >>> 2]); 
			sb.append(base64EncodeChars[((b1 & 0x03) << 4) | ((b2 & 0xf0) >>> 4)]); 
			sb.append(base64EncodeChars[(b2 & 0x0f) << 2]); 
			sb.append("="); 
			break; 
		} 
		b3 = data[i++] & 0xff; 
		sb.append(base64EncodeChars[b1 >>> 2]); 
		sb.append(base64EncodeChars[((b1 & 0x03) << 4) | ((b2 & 0xf0) >>> 4)]); 
		sb.append(base64EncodeChars[((b2 & 0x0f) << 2) | ((b3 & 0xc0) >>> 6)]); 
		sb.append(base64EncodeChars[b3 & 0x3f]); 
	} 
	return sb.toString(); 
} 

	/** 
	* 将base64字符串解码为字节数组 
	* 
	* @param str 
	*/ 
	public static byte[] decode(String str) 
	{ 
		byte[] data = str.getBytes(); 
		int len = data.length; 
		ByteArrayOutputStream buf = new ByteArrayOutputStream(len); 
		int i = 0; 
		int b1, b2, b3, b4; 
		
		while (i < len) 
		{ 
		
			/* b1 */ 
			do 
			{ 
				b1 = base64DecodeChars[data[i++]]; 
			} 
			while (i < len && b1 == -1); 
			if (b1 == -1) 
			{ 
				break; 
			} 
		
			/* b2 */ 
			do 
			{ 
				b2 = base64DecodeChars[data[i++]]; 
			} 
			while (i < len && b2 == -1); 
			if (b2 == -1) 
			{ 
				break; 
			} 
			buf.write((int) ((b1 << 2) | ((b2 & 0x30) >>> 4))); 
		
			/* b3 */ 
			do
			{ 
				b3 = data[i++]; 
			if (b3 == 61) 
			{ 
				return buf.toByteArray(); 
			} 
				b3 = base64DecodeChars[b3]; 
			} 
			while (i < len && b3 == -1); 
			if (b3 == -1) 
			{ 
				break; 
			} 
			buf.write((int) (((b2 & 0x0f) << 4) | ((b3 & 0x3c) >>> 2))); 
			
			/* b4 */ 
			do 
			{ 
				b4 = data[i++]; 
				if (b4 == 61) 
				{ 
					return buf.toByteArray(); 
				} 
				b4 = base64DecodeChars[b4]; 
			}
			while (i < len && b4 == -1); 
			if (b4 == -1) 
			{ 
				break; 
			} 
			buf.write((int) (((b3 & 0x03) << 6) | b4)); 
		} 
		return buf.toByteArray(); 
	} 
}