package com.eaosoft.util;


import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.BinaryHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class GUtilAsyncHttp
{
	private static     AsyncHttpClient client =new AsyncHttpClient();    //实例话对象
	
	public static String		m_strSvrURL="http://58.211.102.34:9110/";
	public static String		m_strSvrKey="EAOSoft-JetLeeX";
	public static int			m_nNetTimeout=11000;
	//***********************************************************************************************************************
	
	//***********************************************************************************************************************
    static
    {
        client.setTimeout(m_nNetTimeout);   //设置链接超时，如果不设置，默认为10s
    }
    public static void get(String urlString,AsyncHttpResponseHandler res)    //用一个完整url获取一个string对象
    {
        client.get(urlString, res);
    }
    public static void get(String urlString,RequestParams params,AsyncHttpResponseHandler res)   //url里面带参数
    {
        client.get(urlString, params,res);
    }
    public static void get(String urlString,JsonHttpResponseHandler res)   //不带参数，获取json对象或者数组
    {
        client.get(urlString, res);
    }
    public static void get(String urlString,RequestParams params,JsonHttpResponseHandler res)   //带参数，获取json对象或者数组
    {
        client.get(urlString, params,res);
    }
    public static void get(String uString, BinaryHttpResponseHandler bHandler)   //下载数据使用，会返回byte数据
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
    private TextView textView; // 顶部textview
    private ProgressDialog pDialog;
    private TextView textView2; // 下面textview，显示获取的所有数据
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
        pDialog = ProgressDialog.show(this, "请稍等", "数据加载中");
        String urlString = "http://client.azrj.cn/json/cook/cook_list.jsp?type=1&p=2&size=10"; // 一個獲取菜谱的url地址
        
        HttpUtil.get(urlString, new AsyncHttpResponseHandler() 
        {
            public void onSuccess(String arg0) { // 获取数据成功会调用这里
                pDialog.dismiss();
                textView.setText("获取json数据成功，看下面");
                textView2.setText(arg0);
                Log.i("hck", arg0);
            };
            public void onFailure(Throwable arg0) { // 失败，调用
                Toast.makeText(MainActivity.this, "onFailure",
                        Toast.LENGTH_LONG).show();
            };
            public void onFinish() { // 完成后调用，失败，成功，都要掉
            };
        });
    }
    public void method2(View view) {
        String urlString = "http://client.azrj.cn/json/cook/cook_list.jsp?";
        RequestParams params = new RequestParams(); // 绑定参数
        params.put("type", "1");
        params.put("p", "2");
        params.put("size", "10");
        HttpUtil.get(urlString, params, new JsonHttpResponseHandler() {
            public void onSuccess(JSONArray arg0) { // 成功后返回一个JSONArray数据
                Log.i("hck", arg0.length() + "");
                try {
                    textView.setText("菜谱名字："
                            + arg0.getJSONObject(2).getString("name")); //返回的是JSONArray， 获取JSONArray数据里面的第2个JSONObject对象，然后获取名字为name的数据值
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
            public void onSuccess(JSONObject arg0) {   //返回的是JSONObject，会调用这里
                Log.i("hck", "onSuccess ");
                try {
                    textView.setText("菜谱名字："
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
                    textView.setText("菜谱名字："
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
                    textView.setText("菜谱名字："
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
                    textView.setText("可爱的猫咪已经保存在sdcard里面");
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.i("hck", e.toString());
                }
            }
        });
    }
}     * */
}