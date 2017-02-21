package com.eaosoft.util;


import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.BinaryHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class GUtilAsyncHttp
{
	private static     AsyncHttpClient client =new AsyncHttpClient();    //ʵ��������
	
	public static String		m_strSvrURL="http://58.211.102.34:9110/";
	public static String		m_strSvrKey="EAOSoft-JetLeeX";
	public static int			m_nNetTimeout=11000;
	//***********************************************************************************************************************
	
	//***********************************************************************************************************************
    static
    {
        client.setTimeout(m_nNetTimeout);   //�������ӳ�ʱ����������ã�Ĭ��Ϊ10s
    }
    public static void get(String urlString,AsyncHttpResponseHandler res)    //��һ������url��ȡһ��string����
    {
        client.get(urlString, res);
    }
    public static void get(String urlString,RequestParams params,AsyncHttpResponseHandler res)   //url���������
    {
        client.get(urlString, params,res);
    }
    public static void get(String urlString,JsonHttpResponseHandler res)   //������������ȡjson�����������
    {
        client.get(urlString, res);
    }
    public static void get(String urlString,RequestParams params,JsonHttpResponseHandler res)   //����������ȡjson�����������
    {
        client.get(urlString, params,res);
    }
    public static void get(String uString, BinaryHttpResponseHandler bHandler)   //��������ʹ�ã��᷵��byte����
    {
        client.get(uString, bHandler);
    }
    public static AsyncHttpClient getClient()
    {
        return client;
    }
   
    /*
public class MainActivity extends Activity 
{
    private TextView textView; // ����textview
    private ProgressDialog pDialog;
    private TextView textView2; // ����textview����ʾ��ȡ����������
    @Override

    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView) findViewById(R.id.text);
        textView2 = (TextView) findViewById(R.id.text2);
    }
    public void method1(View view) 
    {
        pDialog = ProgressDialog.show(this, "���Ե�", "���ݼ�����");
        String urlString = "http://client.azrj.cn/json/cook/cook_list.jsp?type=1&p=2&size=10"; // һ���@ȡ���׵�url��ַ
        
        HttpUtil.get(urlString, new AsyncHttpResponseHandler() 
        {
            public void onSuccess(String arg0) { // ��ȡ���ݳɹ����������
                pDialog.dismiss();
                textView.setText("��ȡjson���ݳɹ���������");
                textView2.setText(arg0);
                Log.i("hck", arg0);
            };
            public void onFailure(Throwable arg0) { // ʧ�ܣ�����
                Toast.makeText(MainActivity.this, "onFailure",
                        Toast.LENGTH_LONG).show();
            };
            public void onFinish() { // ��ɺ���ã�ʧ�ܣ��ɹ�����Ҫ��
            };
        });
    }
    public void method2(View view) {
        String urlString = "http://client.azrj.cn/json/cook/cook_list.jsp?";
        RequestParams params = new RequestParams(); // �󶨲���
        params.put("type", "1");
        params.put("p", "2");
        params.put("size", "10");
        HttpUtil.get(urlString, params, new JsonHttpResponseHandler() {
            public void onSuccess(JSONArray arg0) { // �ɹ��󷵻�һ��JSONArray����
                Log.i("hck", arg0.length() + "");
                try {
                    textView.setText("�������֣�"
                            + arg0.getJSONObject(2).getString("name")); //���ص���JSONArray�� ��ȡJSONArray��������ĵ�2��JSONObject����Ȼ���ȡ����Ϊname������ֵ
                } catch (Exception e) {
                    Log.e("hck", e.toString());
                }
            };
            public void onFailure(Throwable arg0) {
                Log.e("hck", " onFailure" + arg0.toString());
            };
            public void onFinish() {
                Log.i("hck", "onFinish");
            };
            public void onSuccess(JSONObject arg0) {   //���ص���JSONObject�����������
                Log.i("hck", "onSuccess ");
                try {
                    textView.setText("�������֣�"
                            + arg0.getJSONArray("list").getJSONObject(0)
                                    .getString("name"));
                } catch (Exception e) {
                    Log.e("hck", e.toString());
                }
            };
        });
    }
    public void method3(View view) {
        String urlString = "http://client.azrj.cn/json/cook/cook_list.jsp?type=1&p=2&size=10";
        HttpUtil.get(urlString, new JsonHttpResponseHandler() {
            public void onSuccess(JSONObject arg0) {
                try {
                    textView.setText("�������֣�"
                            + arg0.getJSONArray("list").getJSONObject(1)
                                    .getString("name"));
                } catch (Exception e) {
                    Log.e("hck", e.toString());
                }
            };
        });
    }
    public void method4(View view) {
        String urlString = "http://client.azrj.cn/json/cook/cook_list.jsp?";
        final RequestParams params = new RequestParams();
        params.put("type", "1");
        params.put("p", "2");
        params.put("size", "10");
        HttpUtil.get(urlString, params, new AsyncHttpResponseHandler() {
            public void onSuccess(String arg0) {
                try {
                    JSONObject jObject = new JSONObject(arg0);
                    textView.setText("�������֣�"
                            + jObject.getJSONArray("list").getJSONObject(2)
                                    .getString("name"));
                    Log.i("hck", params.getEntity().toString());
                } catch (Exception e) {
                }
            };
        });
    }
    public void method5(View view) {
        String url = "http://f.hiphotos.baidu.com/album/w%3D2048/sign=38c43ff7902397ddd6799f046dbab3b7/9c16fdfaaf51f3dee973bf7495eef01f3b2979d8.jpg";
        HttpUtil.get(url, new BinaryHttpResponseHandler() {
            @Override
            public void onSuccess(byte[] arg0) {
                super.onSuccess(arg0);
                File file = Environment.getExternalStorageDirectory();
                File file2 = new File(file, "cat");
                file2.mkdir();
                file2 = new File(file2, "cat.jpg");
                try {
                    FileOutputStream oStream = new FileOutputStream(file2);
                    oStream.write(arg0);
                    oStream.flush();
                    oStream.close();
                    textView.setText("�ɰ���è���Ѿ�������sdcard����");
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.i("hck", e.toString());
                }
            }
        });
    }
}     * */
}