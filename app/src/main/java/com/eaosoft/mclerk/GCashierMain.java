package com.eaosoft.mclerk;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.eaosoft.fragment.GFragmentOne;
import com.eaosoft.userinfo.GOperaterInfo;
import com.eaosoft.util.GSvrChannel;
import com.eaosoft.util.GUtilSDCard;
import com.eaosoft.view.GProgressWebView;
import com.eaosoft.view.RoundImageView;
import com.google.zxing.client.android.CaptureActivity;

import org.json.JSONException;
import org.json.JSONObject;

public class GCashierMain {
    private ScrollView m_oUserView = null;
    private Context m_oContext = null;
    public GFragmentOne m_oFragmentOne = null;
    //=====================================================
    public RoundImageView m_oImgHead = null;
    public LinearLayout m_oRounddot = null;
    public ViewPager m_oImgBanner = null;
    public TextView m_oShopCaption = null;
    public TextView m_oUserCaption = null;
    private LinearLayout oMainWin_Right_one;
    private LinearLayout oMainWin_Right_two;
    private Button oBtnCardStorage;
    private Button oBtnCardKindList;
    private Button oBtnCardStock;
    private Button oBtnCardCreate;
    private Button oBtnCardStatistics;
    private Button oBtnCardSearch;
    private Button oBtnCardSearchByScanner;
    private Button oBtnCardUse;
    public TextView m_oCurrentTime = null;
    String startno="";
    String startno_use="";
    String endno="";
    String endno_use="";
    private String strURL;
    private GProgressWebView 		m_oWebViewStock;
    private GProgressWebView 		m_oWebViewStatistics;
    //=====================================================
    public GCashierMain(Context oContext) {
        m_oContext = oContext;
    }

    public View OnCreateView() {
        m_oUserView = new ScrollView(m_oContext);
        m_oUserView.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
        m_oUserView.setFillViewport(true);
        m_oUserView.setBackgroundResource(R.color.lightgray);

        //============================================================================
        //主背景
        LinearLayout oMainWin = new LinearLayout(m_oContext);  //线性布局方式
        oMainWin.setOrientation(LinearLayout.VERTICAL); //控件对其方式为垂直排列  VERTICAL
        oMainWin.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
        m_oUserView.addView(oMainWin);

        //===========================================================================
        //页面头--
        oMainWin.addView(onCreatePageHead(m_oContext));
        //==========================================================================
        //中间按钮和操作区
        oMainWin.addView(onCreatePageBody(m_oContext));
        //==========================================================================
        //快捷按钮区

        return m_oUserView;
    }

    private View onCreatePageBody(final Context oContext) {
        //======================================================================================
        //主背景
        LinearLayout oMainWin = new LinearLayout(oContext);  //线性布局方式
        oMainWin.setOrientation(LinearLayout.HORIZONTAL); //控件对其方式为水平排列
        oMainWin.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        //======================================================================================
        /*
			LinearLayout oSubShortBtn = new LinearLayout(oContext);  //线性布局方式  
			oSubShortBtn.setOrientation( LinearLayout.VERTICAL ); //控件对其方式为横向排列  VERTICAL
			oSubShortBtn.setLayoutParams( new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, 160) );
			oMainWin.addView(oSubShortBtn);
			//======================================================================================
			 */
        //左半边操作按钮
        LinearLayout oMainWin_left = new LinearLayout(oContext);  //线性布局方式
        oMainWin_left.setOrientation(LinearLayout.VERTICAL); //控件对其方式为竖直排列
        oMainWin_left.setLayoutParams(new LayoutParams(MainActivity.mSreenWidth / 4, LayoutParams.MATCH_PARENT));
        oMainWin_left.setBackgroundResource(R.color.lightgray);
        //======================================================================================
        //当前套餐
        oBtnCardKindList = new Button(m_oContext);
        oBtnCardKindList.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        oBtnCardKindList.setText("卡套餐");
        oBtnCardKindList.setBackgroundResource(R.color.printbutton);
        oBtnCardKindList.setTextColor(oBtnCardKindList.getResources().getColor(android.R.color.white));
        oBtnCardKindList.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
//                Intent intent = new Intent(MainActivity.m_oMainActivity, GActCardKindList.class);
//                intent.putExtra("UserMgr", "UserMgr");
//                MainActivity.m_oMainActivity.startActivity(intent);
                oBtnCardKindList.setBackgroundResource(R.color.printbutton_light);
                oBtnCardStorage.setBackgroundResource(R.color.printbutton);
                oBtnCardUse.setBackgroundResource(R.color.printbutton);
                oBtnCardStock.setBackgroundResource(R.color.printbutton);
                oBtnCardCreate.setBackgroundResource(R.color.printbutton);
                oBtnCardStatistics.setBackgroundResource(R.color.printbutton);
                oBtnCardSearch.setBackgroundResource(R.color.printbutton);
                oBtnCardSearchByScanner.setBackgroundResource(R.color.printbutton);
                oMainWin_Right_one.setVisibility(View.GONE);
                oMainWin_Right_two.setVisibility(View.GONE);
                m_oWebViewStock.setVisibility(View.GONE);
                m_oWebViewStatistics.setVisibility(View.GONE);
                Intent intent = new Intent(MainActivity.m_oMainActivity, GCashier_Package.class);
                intent.putExtra("UserMgr", "UserMgr");
                MainActivity.m_oMainActivity.startActivity(intent);
            }
        });
        oMainWin_left.addView(oBtnCardKindList);
        //==========================================================================
        //卡入库
        oBtnCardStorage = new Button(m_oContext);
        oBtnCardStorage.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        oBtnCardStorage.setText("卡入库");
        oBtnCardStorage.setBackgroundResource(R.color.printbutton);
        oBtnCardStorage.setTextColor(oBtnCardStorage.getResources().getColor(android.R.color.white));
        oBtnCardStorage.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                oBtnCardKindList.setBackgroundResource(R.color.printbutton);
                oBtnCardStorage.setBackgroundResource(R.color.printbutton_light);
                oBtnCardUse.setBackgroundResource(R.color.printbutton);
                oBtnCardStock.setBackgroundResource(R.color.printbutton);
                oBtnCardCreate.setBackgroundResource(R.color.printbutton);
                oBtnCardSearch.setBackgroundResource(R.color.printbutton);
                oBtnCardSearchByScanner.setBackgroundResource(R.color.printbutton);
                oBtnCardStatistics.setBackgroundResource(R.color.printbutton);
                oMainWin_Right_one.setVisibility(View.VISIBLE);
                oMainWin_Right_two.setVisibility(View.GONE);
                m_oWebViewStock.setVisibility(View.GONE);
                m_oWebViewStatistics.setVisibility(View.GONE);
            }
        });
        oMainWin_left.addView(oBtnCardStorage);
        //==========================================================================
        //卡领用
        oBtnCardUse = new Button(m_oContext);
        oBtnCardUse.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        oBtnCardUse.setText("卡领用");
        oBtnCardUse.setBackgroundResource(R.color.printbutton);
        oBtnCardUse.setTextColor(oBtnCardUse.getResources().getColor(android.R.color.white));
        oBtnCardUse.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                oBtnCardKindList.setBackgroundResource(R.color.printbutton);
                oBtnCardStorage.setBackgroundResource(R.color.printbutton);
                oBtnCardUse.setBackgroundResource(R.color.printbutton_light);
                oBtnCardStock.setBackgroundResource(R.color.printbutton);
                oBtnCardCreate.setBackgroundResource(R.color.printbutton);
                oBtnCardSearch.setBackgroundResource(R.color.printbutton);
                oBtnCardSearchByScanner.setBackgroundResource(R.color.printbutton);
                oBtnCardStatistics.setBackgroundResource(R.color.printbutton);
                oMainWin_Right_one.setVisibility(View.GONE);
                oMainWin_Right_two.setVisibility(View.VISIBLE);
                m_oWebViewStock.setVisibility(View.GONE);
                m_oWebViewStatistics.setVisibility(View.GONE);
            }
        });
        oMainWin_left.addView(oBtnCardUse);
        //==========================================================================
        //新卡销售
        oBtnCardCreate = new Button(m_oContext);
        oBtnCardCreate.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        oBtnCardCreate.setText("卡销售");
        oBtnCardCreate.setBackgroundResource(R.color.printbutton);
        oBtnCardCreate.setTextColor(oBtnCardCreate.getResources().getColor(android.R.color.white));
        oBtnCardCreate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                oBtnCardKindList.setBackgroundResource(R.color.printbutton);
                oBtnCardStorage.setBackgroundResource(R.color.printbutton);
                oBtnCardUse.setBackgroundResource(R.color.printbutton);
                oBtnCardStock.setBackgroundResource(R.color.printbutton);
                oBtnCardCreate.setBackgroundResource(R.color.printbutton_light);
                oBtnCardStatistics.setBackgroundResource(R.color.printbutton);
                oBtnCardSearch.setBackgroundResource(R.color.printbutton);
                oBtnCardSearchByScanner.setBackgroundResource(R.color.printbutton);
                oMainWin_Right_one.setVisibility(View.GONE);
                oMainWin_Right_two.setVisibility(View.GONE);
                m_oWebViewStock.setVisibility(View.GONE);
                m_oWebViewStatistics.setVisibility(View.GONE);
                if (MainActivity.m_bDebugCardNo) {
                    if (m_oFragmentOne != null)
                        m_oFragmentOne.onScannerResult(MainActivity.m_strDebugCardNo, MainActivity.SCAN_CODE_CAED_CREATE);
                } else {
                    Intent intent = new Intent(MainActivity.m_oMainActivity, CaptureActivity.class);
                    MainActivity.m_oMainActivity.startActivityForResult(intent, MainActivity.SCAN_CODE_CAED_CREATE);
                }
            }
        });
        oMainWin_left.addView(oBtnCardCreate);
        //==========================================================================
        //卡库存
        oBtnCardStock = new Button(m_oContext);
        oBtnCardStock.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        oBtnCardStock.setText("卡库存");
        oBtnCardStock.setBackgroundResource(R.color.printbutton);
        oBtnCardStock.setTextColor(oBtnCardStock.getResources().getColor(android.R.color.white));
        oBtnCardStock.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                oBtnCardKindList.setBackgroundResource(R.color.printbutton);
                oBtnCardStorage.setBackgroundResource(R.color.printbutton);
                oBtnCardUse.setBackgroundResource(R.color.printbutton);
                oBtnCardStock.setBackgroundResource(R.color.printbutton_light);
                oBtnCardCreate.setBackgroundResource(R.color.printbutton);
                oBtnCardSearch.setBackgroundResource(R.color.printbutton);
                oBtnCardSearchByScanner.setBackgroundResource(R.color.printbutton);
                oBtnCardStatistics.setBackgroundResource(R.color.printbutton);
                oMainWin_Right_one.setVisibility(View.GONE);
                oMainWin_Right_two.setVisibility(View.GONE);
                m_oWebViewStock.setVisibility(View.VISIBLE);
                m_oWebViewStatistics.setVisibility(View.GONE);
            }
        });
        oMainWin_left.addView(oBtnCardStock);
        //==========================================================================
        //卡统计
        oBtnCardStatistics = new Button(m_oContext);
        oBtnCardStatistics.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        oBtnCardStatistics.setText("卡统计");
        oBtnCardStatistics.setBackgroundResource(R.color.printbutton);
        oBtnCardStatistics.setTextColor(oBtnCardStatistics.getResources().getColor(android.R.color.white));
        oBtnCardStatistics.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                oBtnCardKindList.setBackgroundResource(R.color.printbutton);
                oBtnCardStorage.setBackgroundResource(R.color.printbutton);
                oBtnCardUse.setBackgroundResource(R.color.printbutton);
                oBtnCardStock.setBackgroundResource(R.color.printbutton);
                oBtnCardCreate.setBackgroundResource(R.color.printbutton);
                oBtnCardSearch.setBackgroundResource(R.color.printbutton);
                oBtnCardSearchByScanner.setBackgroundResource(R.color.printbutton);
                oBtnCardStatistics.setBackgroundResource(R.color.printbutton_light);
                oMainWin_Right_one.setVisibility(View.GONE);
                oMainWin_Right_two.setVisibility(View.GONE);
                m_oWebViewStock.setVisibility(View.GONE);
                m_oWebViewStatistics.setVisibility(View.VISIBLE);
            }
        });
        oMainWin_left.addView(oBtnCardStatistics);
        //======================================================================================
        //卡查询
        oBtnCardSearch = new Button(m_oContext);
        oBtnCardSearch.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        oBtnCardSearch.setText("卡查询");
        oBtnCardSearch.setBackgroundResource(R.color.printbutton);
        oBtnCardSearch.setTextColor(oBtnCardStatistics.getResources().getColor(android.R.color.white));
        oBtnCardSearch.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                oBtnCardKindList.setBackgroundResource(R.color.printbutton);
                oBtnCardStorage.setBackgroundResource(R.color.printbutton);
                oBtnCardUse.setBackgroundResource(R.color.printbutton);
                oBtnCardStock.setBackgroundResource(R.color.printbutton);
                oBtnCardCreate.setBackgroundResource(R.color.printbutton);
                oBtnCardStatistics.setBackgroundResource(R.color.printbutton);
                oBtnCardSearch.setBackgroundResource(R.color.printbutton_light);
                oBtnCardSearchByScanner.setBackgroundResource(R.color.printbutton);
                oMainWin_Right_one.setVisibility(View.GONE);
                oMainWin_Right_two.setVisibility(View.GONE);
                m_oWebViewStock.setVisibility(View.GONE);
                m_oWebViewStatistics.setVisibility(View.GONE);
                Intent intent =new Intent(MainActivity.m_oMainActivity,GCashier_Search.class);
                MainActivity.m_oMainActivity.startActivity(intent);
            }
        });
        oMainWin_left.addView(oBtnCardSearch);
        //======================================================================================
        //扫码查询
        oBtnCardSearchByScanner = new Button(m_oContext);
        oBtnCardSearchByScanner.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        oBtnCardSearchByScanner.setBackgroundResource(R.drawable.login);
        oBtnCardSearchByScanner.setBackgroundResource(R.color.printbutton);
        oBtnCardSearchByScanner.setTextColor(oBtnCardCreate.getResources().getColor(android.R.color.white));
        oBtnCardSearchByScanner.setText("扫码查询");
        oBtnCardSearchByScanner.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                oBtnCardKindList.setBackgroundResource(R.color.printbutton);
                oBtnCardStorage.setBackgroundResource(R.color.printbutton);
                oBtnCardUse.setBackgroundResource(R.color.printbutton);
                oBtnCardStock.setBackgroundResource(R.color.printbutton);
                oBtnCardCreate.setBackgroundResource(R.color.printbutton);
                oBtnCardStatistics.setBackgroundResource(R.color.printbutton);
                oBtnCardSearch.setBackgroundResource(R.color.printbutton);
                oBtnCardSearchByScanner.setBackgroundResource(R.color.printbutton_light);
                oMainWin_Right_one.setVisibility(View.GONE);
                oMainWin_Right_two.setVisibility(View.GONE);
                m_oWebViewStock.setVisibility(View.GONE);
                m_oWebViewStatistics.setVisibility(View.GONE);
                if (MainActivity.m_bDebugCardNo) {
                    if (m_oFragmentOne != null)
                        m_oFragmentOne.onScannerResult(MainActivity.m_strDebugCardNo, MainActivity.SCAN_CODE_CAED_CONSUME);
                } else {
                    Intent intent = new Intent(MainActivity.m_oMainActivity, CaptureActivity.class);
                    MainActivity.m_oMainActivity.startActivityForResult(intent, MainActivity.SCAN_CODE_CAED_CONSUME);
                }
            }
        });
        oMainWin_left.addView(oBtnCardSearchByScanner);
        //======================================================================================
        FrameLayout oMainWin_Right = new FrameLayout(oContext);
        oMainWin_Right.setLayoutParams(new LayoutParams(MainActivity.mSreenWidth*3 / 4, LayoutParams.MATCH_PARENT));
        oMainWin_Right.setBackgroundResource(R.color.lightgray);
        oMainWin_Right_one = new LinearLayout(oContext);  //线性布局方式
        oMainWin_Right_one.setOrientation(LinearLayout.VERTICAL); //控件对其方式为竖直排列
        oMainWin_Right_one.setLayoutParams(new LayoutParams(MainActivity.mSreenWidth*3 / 4, LayoutParams.MATCH_PARENT));
        oMainWin_Right_one.setGravity(Gravity.CENTER_HORIZONTAL);
        oMainWin_Right_one.setVisibility(View.GONE);
        oMainWin_Right_one.setBackgroundResource(R.color.encode_view);
        TextView textView = new TextView(m_oContext);
        textView.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,150));
        textView.setBackgroundResource(R.color.encode_view);
        oMainWin_Right_one.addView(textView);

        LinearLayout startNum = new LinearLayout(oContext);  //线性布局方式
        startNum.setOrientation(LinearLayout.HORIZONTAL); //控件对其方式为竖直排列
        startNum.setLayoutParams(new LayoutParams(MainActivity.mSreenWidth *2/ 5, LayoutParams.WRAP_CONTENT));

        startNum.setBackgroundResource(R.color.lightgray);
        oMainWin_Right_one.addView(startNum);

        final TextView startNo = new TextView(m_oContext);
        startNo.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT,2));
        startNo.setText("起始号码");
        startNo.setGravity(Gravity.CENTER);
        startNo.setBackgroundResource(R.color.printbutton);
        startNo.setTextColor(startNo.getResources().getColor(android.R.color.white));
        startNum.addView(startNo);
        EditText et_startNo = new EditText(m_oContext);
        et_startNo.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT,1));
        et_startNo.setBackgroundResource(R.color.lightgray);
        et_startNo.setInputType(InputType.TYPE_CLASS_NUMBER);
        et_startNo.setTextColor(et_startNo.getResources().getColor(android.R.color.black));
        et_startNo.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {

                startno=s.toString();

            }
        });
        startNum.addView(et_startNo);

        TextView textView_one = new TextView(m_oContext);
        textView_one.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,20));
        textView_one.setBackgroundResource(R.color.encode_view);
        oMainWin_Right_one.addView(textView_one);

        LinearLayout endNum = new LinearLayout(oContext);  //线性布局方式
        endNum.setOrientation(LinearLayout.HORIZONTAL); //控件对其方式为竖直排列
        endNum.setLayoutParams(new LayoutParams(MainActivity.mSreenWidth *2/ 5, LayoutParams.WRAP_CONTENT));
        endNum.setBackgroundResource(R.color.lightgray);
        oMainWin_Right_one.addView(endNum);

        TextView endNo = new TextView(m_oContext);
        endNo.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT,2));
        endNo.setText("终止号码");
        endNo.setGravity(Gravity.CENTER);
        endNo.setBackgroundResource(R.color.printbutton);
        endNo.setTextColor(endNo.getResources().getColor(android.R.color.white));
        endNum.addView(endNo);
        EditText et_endNo = new EditText(m_oContext);
        et_endNo.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT,1));
        et_endNo.setBackgroundResource(R.color.lightgray);
        et_endNo.setInputType(InputType.TYPE_CLASS_NUMBER);
        et_endNo.setTextColor(et_endNo.getResources().getColor(android.R.color.black));
        endNum.addView(et_endNo);

//        int totalNum=Integer.parseInt(endno)-Integer.parseInt(starno)+1;
//        System.out.println("======="+totalNum);
        TextView textView_two= new TextView(m_oContext);
        textView_two.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,20));
        textView_two.setBackgroundResource(R.color.encode_view);
        oMainWin_Right_one.addView(textView_two);

        final TextView totalNum= new TextView(m_oContext);
        totalNum.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        totalNum.setBackgroundResource(R.color.encode_view);
        totalNum.setGravity(Gravity.CENTER_HORIZONTAL);
        totalNum.setText("总计0张");
        et_endNo.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                endno=s.toString();

                int totalNo=Integer.parseInt(endno)-Integer.parseInt(startno)+1;

                totalNum.setText("总计"+totalNo+"张");

            }
        });
        oMainWin_Right_one.addView(totalNum);
        Button oBtnCardSave_Storage = new Button(m_oContext);
        oBtnCardSave_Storage.setLayoutParams(new LinearLayout.LayoutParams(MainActivity.mSreenWidth / 4, LayoutParams.WRAP_CONTENT));
        oBtnCardSave_Storage.setText("确认保存");
        oBtnCardSave_Storage.setGravity(Gravity.CENTER_HORIZONTAL);
        oBtnCardSave_Storage.setBackgroundResource(R.color.printbutton);
        oBtnCardSave_Storage.setTextColor(oBtnCardStatistics.getResources().getColor(android.R.color.white));
        oBtnCardSave_Storage.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                GSvrChannel svr= 	new GSvrChannel()
                {
                    public void onNetFailure(int statusCode,String strInfo)
                    {
                        MainActivity.MessageBox("保存失败","statusCode:"+statusCode+",Info:"+strInfo);
                        MainActivity.onUserMessageBox("保存失败", "保存失败，请检查网络是否畅通或者联系管理员！");
                    }
                    public void onNetSuccess(int nCode,String strInfo,JSONObject oJsonData)
                    {
                        if(nCode < 0)
                        {
                            MainActivity.MessageBox("保存失败",strInfo);
                            MainActivity.onUserMessageBox("保存失败",strInfo);
                            return;
                        }
                        MainActivity.onUserMessageBox("入库成功",strInfo);

                    }
                };
                try
                {
                    JSONObject   requestDatas = new JSONObject();
                    requestDatas.put("cardNoStart", startno);
                    requestDatas.put("cardNoEnd", endno);
                    requestDatas.put("groupUID", GOperaterInfo.m_strGroupUID);
                    svr.m_oCurrentActivity = MainActivity.m_oMainActivity;
                    svr.onPost("api/mobile/opCardStorage.do", requestDatas);
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                    MainActivity.MessageBox("保存失败",e.getMessage());
                    MainActivity.onUserMessageBox("保存失败","保存失败，请检查网络是否畅通或者联系管理员！");
                }
            }
        });
        oMainWin_Right_one.addView(oBtnCardSave_Storage);
        oMainWin_Right_two = new LinearLayout(oContext);  //线性布局方式
        oMainWin_Right_two.setOrientation(LinearLayout.VERTICAL); //控件对其方式为竖直排列
        oMainWin_Right_two.setLayoutParams(new LayoutParams(MainActivity.mSreenWidth*3 / 4, LayoutParams.MATCH_PARENT));
        oMainWin_Right_two.setGravity(Gravity.CENTER_HORIZONTAL);
        oMainWin_Right_two.setBackgroundResource(R.color.encode_view);
        oMainWin_Right_two.setVisibility(View.GONE);
        TextView textView_three = new TextView(m_oContext);
        textView_three.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,150));
        textView_three.setBackgroundResource(R.color.encode_view);
        oMainWin_Right_two.addView(textView_three);

        LinearLayout startNum_use = new LinearLayout(oContext);  //线性布局方式
        startNum_use.setOrientation(LinearLayout.HORIZONTAL); //控件对其方式为竖直排列
        startNum_use.setLayoutParams(new LayoutParams(MainActivity.mSreenWidth *2/ 5, LayoutParams.WRAP_CONTENT));

        startNum_use.setBackgroundResource(R.color.lightgray);
        oMainWin_Right_two.addView(startNum_use);

        final TextView startNo_use = new TextView(m_oContext);
        startNo_use.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT,2));
        startNo_use.setText("起始号码");
        startNo_use.setGravity(Gravity.CENTER);
        startNo_use.setBackgroundResource(R.color.printbutton);
        startNo_use.setTextColor(startNo_use.getResources().getColor(android.R.color.white));
        startNum_use.addView(startNo_use);
        EditText et_startNo_use = new EditText(m_oContext);
        et_startNo_use.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT,1));
        et_startNo_use.setBackgroundResource(R.color.lightgray);
        et_startNo_use.setInputType(InputType.TYPE_CLASS_NUMBER);
        et_startNo_use.setTextColor(et_startNo_use.getResources().getColor(android.R.color.black));
        et_startNo_use.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {

                startno_use=s.toString();

            }
        });
        startNum_use.addView(et_startNo_use);

        TextView textView_four = new TextView(m_oContext);
        textView_four.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,20));
        textView_four.setBackgroundResource(R.color.encode_view);
        oMainWin_Right_two.addView(textView_four);

        LinearLayout endNum_use = new LinearLayout(oContext);  //线性布局方式
        endNum_use.setOrientation(LinearLayout.HORIZONTAL); //控件对其方式为竖直排列
        endNum_use.setLayoutParams(new LayoutParams(MainActivity.mSreenWidth *2/ 5, LayoutParams.WRAP_CONTENT));
        endNum_use.setBackgroundResource(R.color.lightgray);
        oMainWin_Right_two.addView(endNum_use);

        TextView endNo_use = new TextView(m_oContext);
        endNo_use.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT,2));
        endNo_use.setText("终止号码");
        endNo_use.setGravity(Gravity.CENTER);
        endNo_use.setBackgroundResource(R.color.printbutton);
        endNo_use.setTextColor(endNo_use.getResources().getColor(android.R.color.white));
        endNum_use.addView(endNo_use);
        EditText et_endNo_use = new EditText(m_oContext);
        et_endNo_use.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT,1));
        et_endNo_use.setBackgroundResource(R.color.lightgray);
        et_endNo_use.setInputType(InputType.TYPE_CLASS_NUMBER);
        et_endNo_use.setTextColor(et_endNo_use.getResources().getColor(android.R.color.black));
        endNum_use.addView(et_endNo_use);

        TextView textView_five= new TextView(m_oContext);
        textView_five.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,20));
        textView_five.setBackgroundResource(R.color.encode_view);
        oMainWin_Right_two.addView(textView_five);

        final TextView totalNum_use= new TextView(m_oContext);
        totalNum_use.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        totalNum_use.setBackgroundResource(R.color.encode_view);
        totalNum_use.setGravity(Gravity.CENTER_HORIZONTAL);
        totalNum_use.setText("总计0张");
        et_endNo_use.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                endno_use=s.toString();

                int totalNo=Integer.parseInt(endno_use)-Integer.parseInt(startno_use)+1;

                totalNum_use.setText("总计"+totalNo+"张");

            }
        });
        oMainWin_Right_two.addView(totalNum_use);
        Button oBtnCardSave_Use = new Button(m_oContext);
        oBtnCardSave_Use.setLayoutParams(new LinearLayout.LayoutParams(MainActivity.mSreenWidth / 4, LayoutParams.WRAP_CONTENT));
        oBtnCardSave_Use.setText("确认保存");
        oBtnCardSave_Use.setGravity(Gravity.CENTER_HORIZONTAL);
        oBtnCardSave_Use.setBackgroundResource(R.color.printbutton);
        oBtnCardSave_Use.setTextColor(oBtnCardSave_Use.getResources().getColor(android.R.color.white));
        oBtnCardSave_Use.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                GSvrChannel svr= 	new GSvrChannel()
                {
                    public void onNetFailure(int statusCode,String strInfo)
                    {
                        MainActivity.MessageBox("保存失败","statusCode:"+statusCode+",Info:"+strInfo);
                        MainActivity.onUserMessageBox("保存失败", "保存失败，请检查网络是否畅通或者联系管理员！");
                    }
                    public void onNetSuccess(int nCode,String strInfo,JSONObject oJsonData)
                    {
                        if(nCode < 0)
                        {
                            MainActivity.MessageBox("保存失败",strInfo);
                            MainActivity.onUserMessageBox("保存失败",strInfo);
                            return;
                        }
                        MainActivity.onUserMessageBox("领用成功",strInfo);

                    }
                };
                try
                {
                    JSONObject   requestDatas = new JSONObject();
                    requestDatas.put("cardNoStart", startno);
                    requestDatas.put("cardNoEnd", endno);
                    requestDatas.put("groupUID", GOperaterInfo.m_strGroupUID);
                    svr.m_oCurrentActivity = MainActivity.m_oMainActivity;
                    svr.onPost("api/mobile/opCardUsed.do", requestDatas);
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                    MainActivity.MessageBox("保存失败",e.getMessage());
                    MainActivity.onUserMessageBox("保存失败","保存失败，请检查网络是否畅通或者联系管理员！");
                }
            }
        });
        oMainWin_Right_two.addView(oBtnCardSave_Use);

        m_oWebViewStock = new GProgressWebView(oContext);
        m_oWebViewStock.setVisibility(View.GONE);
        m_oWebViewStock.setWebViewClient(new WebViewClient()
        {
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl)
            {                 // Handle the error

            }

            public boolean shouldOverrideUrlLoading(WebView view, String url)
            {
                view.loadUrl(url);     //不要跳往系统窗口
                return true;
            }
        });
        m_oWebViewStock.setDownloadListener(new DownloadListener()
        {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                if (url != null && url.startsWith("http://"))
                    MainActivity.m_oMainActivity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
            }
        });
        strURL = GSvrChannel.m_strURLcardStock + "?token=" + GOperaterInfo.m_strToken + "&callerName=" + GSvrChannel.CALLER_NAME + "&groupUID=" +GOperaterInfo.m_strGroupUID ;
        m_oWebViewStock.loadUrl(strURL);

        m_oWebViewStatistics = new GProgressWebView(oContext);
        m_oWebViewStatistics.setVisibility(View.GONE);
        m_oWebViewStatistics.setWebViewClient(new WebViewClient()
        {
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl)
            {                 // Handle the error

            }

            public boolean shouldOverrideUrlLoading(WebView view, String url)
            {
                view.loadUrl(url);     //不要跳往系统窗口
                return true;
            }
        });
        m_oWebViewStatistics.setDownloadListener(new DownloadListener()
        {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                if (url != null && url.startsWith("http://"))
                    MainActivity.m_oMainActivity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
            }
        });
        strURL = GSvrChannel.m_strURLcardStatistics + "?token=" + GOperaterInfo.m_strToken + "&callerName=" + GSvrChannel.CALLER_NAME + "&groupUID=" +GOperaterInfo.m_strGroupUID ;
        m_oWebViewStatistics.loadUrl(strURL);
        oMainWin_Right.addView(m_oWebViewStock);
        oMainWin_Right.addView(m_oWebViewStatistics);
        oMainWin_Right.addView(oMainWin_Right_one);
        oMainWin_Right.addView(oMainWin_Right_two);
        oMainWin.addView(oMainWin_left);
        oMainWin.addView(oMainWin_Right);
        return oMainWin;
    }

    //=============================================================================================
    //建立页面头部
    private View onCreatePageHead(Context oContext) {
        RelativeLayout oSubHeader = new RelativeLayout(oContext);  //线性布局方式
        oSubHeader.setLayoutParams(new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, 100));
        oSubHeader.setBackgroundColor(MainActivity.m_oHeadStoreColor);
        //=============================================================================


        //实时时间
        RelativeLayout.LayoutParams pCurrentTime = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        pCurrentTime.addRule(RelativeLayout.ALIGN_LEFT);
        pCurrentTime.addRule(RelativeLayout.CENTER_VERTICAL);
        m_oCurrentTime = new TextView(oContext);
        m_oCurrentTime.setLayoutParams(pCurrentTime);
        m_oCurrentTime.setTextSize(16);
        m_oCurrentTime.setText(MainActivity.getStringDate());
        m_oCurrentTime.setTextColor(Color.BLACK);
        oSubHeader.addView(m_oCurrentTime);
        //头像
        RelativeLayout.LayoutParams pImgHead = new RelativeLayout.LayoutParams(80, 80);
        pImgHead.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        m_oImgHead = new RoundImageView(oContext);
        m_oImgHead.setLayoutParams(pImgHead);
        m_oImgHead.setImageResource(R.drawable.ic_launcher);
        m_oImgHead.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.m_oMainActivity, GActUserInfo.class);
                MainActivity.m_oMainActivity.startActivity(intent);
            }
        });
        if (GUtilSDCard.isFileExist(GOperaterInfo.m_strLocalDiskImage)) {
            Bitmap photo = BitmapFactory.decodeFile(GOperaterInfo.m_strLocalDiskImage);
            if (photo != null && m_oImgHead != null)
                m_oImgHead.setImageBitmap(photo);
        }
        oSubHeader.addView(m_oImgHead);

        //门店名称
        RelativeLayout.LayoutParams pShopName = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        pShopName.addRule(RelativeLayout.CENTER_IN_PARENT);

        m_oShopCaption = new TextView(oContext);
        m_oShopCaption.setLayoutParams(pShopName);
        m_oShopCaption.setTextSize(16);
        m_oShopCaption.setText(GOperaterInfo.m_strGroupName);
        m_oShopCaption.setTextColor(Color.BLACK);
        m_oShopCaption.setEnabled(true);
        m_oShopCaption.setOnClickListener(new View.OnClickListener()
      	{
     		 public void onClick(View v)
            {
     			Intent intent = new Intent(MainActivity.m_oMainActivity, GActGroupList.class);
     			MainActivity.m_oMainActivity.startActivityForResult(intent,MainActivity.USER_GROUP_CHANGE);
            }
     	});
        oSubHeader.addView(m_oShopCaption);

        return oSubHeader;
    }

    public void onScannerResult(String strCardNo, int requestCode) {
        if (requestCode == MainActivity.USER_GROUP_CHANGE && m_oShopCaption != null)
            m_oShopCaption.setText(GOperaterInfo.m_strGroupName);
    }
}