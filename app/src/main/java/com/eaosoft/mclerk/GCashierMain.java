package com.eaosoft.mclerk;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.eaosoft.fragment.GFragmentOne;
import com.eaosoft.userinfo.GOperaterInfo;
import com.eaosoft.util.Conts;
import com.eaosoft.util.GSvrChannel;
import com.eaosoft.util.GUtilSDCard;
import com.eaosoft.view.GProgressWebView;
import com.eaosoft.view.RoundImageView;


import net.posprinter.posprinterface.UiExecute;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

import static com.eaosoft.mclerk.MainActivity.SCAN_CODE_CAED_CREATE;
import static com.eaosoft.mclerk.MainActivity.binder;

public class GCashierMain {
    private RelativeLayout m_oUserView = null;
    private Context m_oContext = null;
    public GFragmentOne m_oFragmentOne = null;
    //=====================================================
    public RoundImageView m_oImgHead = null;
    public LinearLayout m_oRounddot = null;
    public ViewPager m_oImgBanner = null;
    public TextView m_oShopCaption = null;
    public Button oTextBle = null;
//    public Button m_oconnectBlueTooth = null;
    private LinearLayout oMainWin_Right_one;
    private LinearLayout oMainWin_Right_two;
    private LinearLayout oMainWin_Right_;
    private LinearLayout oMainWin_Right_three;
    private LinearLayout oMainWin_Right_picture;
    private Button oBtnCardStorage;
    private Button oBtnCardKindList;
    private Button oBtnSalesReport;
    private Button oBtnCardCreate;
    private Button oBtnCardStatistics;
    private Button oBtnCardSearch;
    private Button oBtnCardSearchByScanner;
    private Button oBtnCardUse;
    private TextView   view1;
    private TextView   view2;
    private TextView   view3;
    public TextView m_oCurrentTime = null;
    String startno="";
    String startno_use="";
    String endno="";
    String endno_use="";
    private EditText et_startNo_use;
    private EditText et_endNo_use;
    private EditText et_startNo;
    private EditText et_cardNo;
    private EditText et_endNo;

    private String strURL;
    private String strURL_Statistics;
    private GProgressWebView 		m_oWebViewStock;
    private GProgressWebView 		m_oWebViewStatistics;
    private Handler handler = new Handler();
    private Runnable runnable1;


    BluetoothAdapter blueadapter;
    private View dialogView;
    private ArrayAdapter<String> adapter1, adapter2;
    private ListView lv1, lv2;
    AlertDialog dialog;
    private Button btn_scan;
    private LinearLayout ll1;
    public String mac = "";
    private ArrayList<String> deviceList_bonded = new ArrayList<String>();
    private ArrayList<String> deviceList_found = new ArrayList<String>();
    static boolean  isConnect;//������ʶ����״̬��һ��booleanֵ
    //=====================================================
    public GCashierMain(Context oContext) {
        m_oContext = oContext;
    }

    public View OnCreateView() {
        m_oUserView = new RelativeLayout(m_oContext);
        m_oUserView.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
//        m_oUserView.setFillViewport(true);
        m_oUserView.setBackgroundResource(R.color.lightgray);
        runnable1 = new Runnable() {
            public void run() {
                m_oCurrentTime.setText(MainActivity.getStringDate());
                handler.postDelayed(this,1000);
                //postDelayed(this,18000)��������һ��Runnable�������̶߳�����
            }
        };
        //============================================================================
        //������
        LinearLayout oMainWin = new LinearLayout(m_oContext);  //���Բ��ַ�ʽ
        oMainWin.setOrientation(LinearLayout.VERTICAL); //�ؼ����䷽ʽΪ��ֱ����  VERTICAL
        oMainWin.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
        m_oUserView.addView(oMainWin);

        //===========================================================================
        //ҳ��ͷ--
        oMainWin.addView(onCreatePageHead(m_oContext));
        //==========================================================================
        //�м䰴ť�Ͳ�����
        oMainWin.addView(onCreatePageBody(m_oContext));
        //==========================================================================
        //��ݰ�ť��

        return m_oUserView;
    }

    private View onCreatePageBody(final Context oContext) {
        //======================================================================================
        //������
        LinearLayout oMainWin = new LinearLayout(oContext);  //���Բ��ַ�ʽ
        oMainWin.setOrientation(LinearLayout.HORIZONTAL); //�ؼ����䷽ʽΪˮƽ����
        oMainWin.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        //======================================================================================
        /*
			LinearLayout oSubShortBtn = new LinearLayout(oContext);  //���Բ��ַ�ʽ  
			oSubShortBtn.setOrientation( LinearLayout.VERTICAL ); //�ؼ����䷽ʽΪ��������  VERTICAL
			oSubShortBtn.setLayoutParams( new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, 160) );
			oMainWin.addView(oSubShortBtn);
			//======================================================================================
			 */
        //���߲�����ť
        LinearLayout oMainWin_left = new LinearLayout(oContext);  //���Բ��ַ�ʽ
        oMainWin_left.setOrientation(LinearLayout.VERTICAL); //�ؼ����䷽ʽΪ��ֱ����
        oMainWin_left.setLayoutParams(new LayoutParams(MainActivity.mSreenWidth / 4, LayoutParams.MATCH_PARENT));
        oMainWin_left.setBackgroundResource(R.color.lightgray);
        oMainWin_left.setGravity(Gravity.CENTER_HORIZONTAL);
        oMainWin_left.setPadding(20,10,20,10);
        //======================================================================================
        //��ǰ�ײ�
        oBtnCardKindList = new Button(m_oContext);
        oBtnCardKindList.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

        oBtnCardKindList.setText("�ײ͹���");
        oBtnCardKindList.setBackgroundResource(R.drawable.spborde);
        oBtnCardKindList.setTextColor(oBtnCardKindList.getResources().getColor(android.R.color.white));
        oBtnCardKindList.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
//                Intent intent = new Intent(MainActivity.m_oMainActivity, GActCardKindList.class);
//                intent.putExtra("UserMgr", "UserMgr");
//                MainActivity.m_oMainActivity.startActivity(intent);
                oBtnCardKindList.setBackgroundResource(R.drawable.spborde);
                oBtnCardCreate.setBackgroundResource(R.drawable.spborde);
                oBtnCardSearch.setBackgroundResource(R.drawable.spborde);
//                oBtnSalesReport.setBackgroundResource(R.drawable.spborde);
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
        RelativeLayout.LayoutParams view_one = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, 20);
        view1 = new TextView(m_oContext);
        view1.setLayoutParams(view_one);
        view1.setBackgroundResource(R.color.lightgray);
        view1.setText("");
        view1.setTextColor(view1.getResources().getColor(android.R.color.white));
        oMainWin_left.addView(view1);
        //==========================================================================
//        //�����
//        oBtnCardStorage = new Button(m_oContext);
//        oBtnCardStorage.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
//        oBtnCardStorage.setText("�����");
//        oBtnCardStorage.setBackgroundResource(R.color.printbutton);
//        oBtnCardStorage.setTextColor(oBtnCardStorage.getResources().getColor(android.R.color.white));
//        oBtnCardStorage.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//
//                oBtnCardKindList.setBackgroundResource(R.color.printbutton);
//                oBtnCardStorage.setBackgroundResource(R.color.printbutton_light);
//                oBtnCardUse.setBackgroundResource(R.color.printbutton);
//                oBtnCardStock.setBackgroundResource(R.color.printbutton);
//                oBtnCardCreate.setBackgroundResource(R.color.printbutton);
//                oBtnCardSearch.setBackgroundResource(R.color.printbutton);
//                oBtnCardSearchByScanner.setBackgroundResource(R.color.printbutton);
//                oBtnCardStatistics.setBackgroundResource(R.color.printbutton);
//                oMainWin_Right_one.setVisibility(View.VISIBLE);
//                oMainWin_Right_two.setVisibility(View.GONE);
//                m_oWebViewStock.setVisibility(View.GONE);
//                m_oWebViewStatistics.setVisibility(View.GONE);
//                oMainWin_Right_picture.setVisibility(View.GONE);
//            }
//        });
//        oMainWin_left.addView(oBtnCardStorage);
//        //==========================================================================
//        //������
//        oBtnCardUse = new Button(m_oContext);
//        oBtnCardUse.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
//        oBtnCardUse.setText("������");
//        oBtnCardUse.setBackgroundResource(R.color.printbutton);
//        oBtnCardUse.setTextColor(oBtnCardUse.getResources().getColor(android.R.color.white));
//        oBtnCardUse.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//
//                oBtnCardKindList.setBackgroundResource(R.color.printbutton);
//                oBtnCardStorage.setBackgroundResource(R.color.printbutton);
//                oBtnCardUse.setBackgroundResource(R.color.printbutton_light);
//                oBtnCardStock.setBackgroundResource(R.color.printbutton);
//                oBtnCardCreate.setBackgroundResource(R.color.printbutton);
//                oBtnCardSearch.setBackgroundResource(R.color.printbutton);
//                oBtnCardSearchByScanner.setBackgroundResource(R.color.printbutton);
//                oBtnCardStatistics.setBackgroundResource(R.color.printbutton);
//                oMainWin_Right_one.setVisibility(View.GONE);
//                oMainWin_Right_two.setVisibility(View.VISIBLE);
//                m_oWebViewStock.setVisibility(View.GONE);
//                m_oWebViewStatistics.setVisibility(View.GONE);
//                oMainWin_Right_picture.setVisibility(View.GONE);
//            }
//        });
//        oMainWin_left.addView(oBtnCardUse);
        //==========================================================================
        //�¿�����
        oBtnCardCreate = new Button(m_oContext);
        oBtnCardCreate.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        oBtnCardCreate.setText("�ƿ�����");
        oBtnCardCreate.setBackgroundResource(R.drawable.spborde);
        oBtnCardCreate.setTextColor(oBtnCardCreate.getResources().getColor(android.R.color.white));
        oBtnCardCreate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                oBtnCardKindList.setBackgroundResource(R.drawable.spborde);
                oBtnCardCreate.setBackgroundResource(R.drawable.spborde);
                oBtnCardSearch.setBackgroundResource(R.drawable.spborde);
//                oBtnSalesReport.setBackgroundResource(R.drawable.spborde);
                oMainWin_Right_one.setVisibility(View.GONE);
                oMainWin_Right_two.setVisibility(View.GONE);
                m_oWebViewStock.setVisibility(View.GONE);
                m_oWebViewStatistics.setVisibility(View.GONE);
//                if (MainActivity.m_bDebugCardNo) {
//                    if (m_oFragmentOne != null)
//                        m_oFragmentOne.onScannerResult(MainActivity.m_strDebugCardNo, MainActivity.SCAN_CODE_CAED_CREATE);
//                } else {
//                    Intent intent = new Intent(MainActivity.m_oMainActivity, CaptureActivity.class);
////                    intent.putExtra("flag","true");
//                    MainActivity.m_oMainActivity.startActivityForResult(intent, MainActivity.SCAN_CODE_CAED_CREATE);
//                }
                final EditText inputServer = new EditText(m_oContext);
                inputServer.setFocusable(true);
                inputServer.setTextColor(inputServer.getResources().getColor(R.color.viewfinder_laser));
                inputServer.setText("");
                inputServer.setTextSize(22);
                inputServer.setTag( SCAN_CODE_CAED_CREATE);

                AlertDialog.Builder builder = new AlertDialog.Builder(m_oContext);
                builder.setTitle("��ȷ��ɨ�迨��").setView(inputServer).setNegativeButton("ȡ��", null);

                builder.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String inputName=null;
                        if(inputServer.getText().toString().contains("\n")){
                            int position=inputServer.getText().toString().indexOf("\n");
                            inputName =  inputServer.getText().toString().substring(0,position);
                        }else {
                            inputName =  inputServer.getText().toString();
                        }
                        if(inputName.length()==13){
                            int nRequestCode = 0;
                            try {
                                nRequestCode = Integer.parseInt(inputServer.getTag().toString());
                            } catch (Exception ex) {
                                return;
                            }
                            ;
                            if (m_oFragmentOne != null)
                                m_oFragmentOne.onScannerResult(inputName, nRequestCode);
                        }else {
                            MainActivity.onUserMessageBox("�������", "ɨ�費��ȷ��");
                        }

                    }
                });
                builder.show();
            }
        });
        oMainWin_left.addView(oBtnCardCreate);
        RelativeLayout.LayoutParams view_two = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, 20);
        view2 = new TextView(m_oContext);
        view2.setLayoutParams(view_two);
        view2.setBackgroundResource(R.color.lightgray);
        view2.setText("");
        view2.setTextColor(view2.getResources().getColor(android.R.color.white));
        oMainWin_left.addView(view2);
        //==========================================================================
//        //�����
//        oBtnCardStock = new Button(m_oContext);
//        oBtnCardStock.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
//        oBtnCardStock.setText("�����");
//        oBtnCardStock.setBackgroundResource(R.color.printbutton);
//        oBtnCardStock.setTextColor(oBtnCardStock.getResources().getColor(android.R.color.white));
//        oBtnCardStock.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                m_oWebViewStock.loadUrl(strURL);
//                oBtnCardKindList.setBackgroundResource(R.color.printbutton);
//                oBtnCardStorage.setBackgroundResource(R.color.printbutton);
//                oBtnCardUse.setBackgroundResource(R.color.printbutton);
//                oBtnCardStock.setBackgroundResource(R.color.printbutton_light);
//                oBtnCardCreate.setBackgroundResource(R.color.printbutton);
//                oBtnCardSearch.setBackgroundResource(R.color.printbutton);
//                oBtnCardSearchByScanner.setBackgroundResource(R.color.printbutton);
//                oBtnCardStatistics.setBackgroundResource(R.color.printbutton);
//                oMainWin_Right_one.setVisibility(View.GONE);
//                oMainWin_Right_two.setVisibility(View.GONE);
//                m_oWebViewStock.setVisibility(View.VISIBLE);
//                m_oWebViewStatistics.setVisibility(View.GONE);
//                oMainWin_Right_picture.setVisibility(View.GONE);
//
//            }
//        });
//        oMainWin_left.addView(oBtnCardStock);
        //==========================================================================
//        //��ͳ��
//        oBtnCardStatistics = new Button(m_oContext);
//        oBtnCardStatistics.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
//        oBtnCardStatistics.setText("��ͳ��");
//        oBtnCardStatistics.setBackgroundResource(R.color.printbutton);
//        oBtnCardStatistics.setTextColor(oBtnCardStatistics.getResources().getColor(android.R.color.white));
//        oBtnCardStatistics.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                m_oWebViewStatistics.loadUrl(strURL_Statistics);
//                oBtnCardKindList.setBackgroundResource(R.color.printbutton);
//                oBtnCardStorage.setBackgroundResource(R.color.printbutton);
//                oBtnCardUse.setBackgroundResource(R.color.printbutton);
//                oBtnCardStock.setBackgroundResource(R.color.printbutton);
//                oBtnCardCreate.setBackgroundResource(R.color.printbutton);
//                oBtnCardSearch.setBackgroundResource(R.color.printbutton);
//                oBtnCardSearchByScanner.setBackgroundResource(R.color.printbutton);
//                oBtnCardStatistics.setBackgroundResource(R.color.printbutton_light);
//                oMainWin_Right_one.setVisibility(View.GONE);
//                oMainWin_Right_two.setVisibility(View.GONE);
//                m_oWebViewStock.setVisibility(View.GONE);
//                m_oWebViewStatistics.setVisibility(View.VISIBLE);
//                oMainWin_Right_picture.setVisibility(View.GONE);
//
//            }
//        });
//        oMainWin_left.addView(oBtnCardStatistics);
        //======================================================================================
        //����ѯ
        oBtnCardSearch = new Button(m_oContext);
        oBtnCardSearch.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        oBtnCardSearch.setText("�ƿ���ѯ");
        oBtnCardSearch.setBackgroundResource(R.drawable.spborde);
        oBtnCardSearch.setTextColor(oBtnCardSearch.getResources().getColor(android.R.color.white));
        oBtnCardSearch.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                oBtnCardKindList.setBackgroundResource(R.drawable.spborde);
                oBtnCardCreate.setBackgroundResource(R.drawable.spborde);
                oBtnCardSearch.setBackgroundResource(R.drawable.spborde);
//                oBtnSalesReport.setBackgroundResource(R.drawable.spborde);
                oMainWin_Right_one.setVisibility(View.GONE);
                oMainWin_Right_two.setVisibility(View.GONE);
                m_oWebViewStock.setVisibility(View.GONE);
                m_oWebViewStatistics.setVisibility(View.GONE);
                Intent intent =new Intent(MainActivity.m_oMainActivity,GCashier_Search.class);
                MainActivity.m_oMainActivity.startActivity(intent);
            }
        });
        oMainWin_left.addView(oBtnCardSearch);
        RelativeLayout.LayoutParams view_three = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, 20);
        view3 = new TextView(m_oContext);
        view3.setLayoutParams(view_three);
        view3.setBackgroundResource(R.color.lightgray);
        view3.setText("");
        view3.setTextColor(view3.getResources().getColor(android.R.color.white));
        oMainWin_left.addView(view3);
        //=================================
        //   ����
//        oBtnCardSearch = new Button(m_oContext);
//        oBtnCardSearch.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
//        oBtnCardSearch.setText("����");
//        oBtnCardSearch.setBackgroundResource(R.drawable.spborde);
//        oBtnCardSearch.setTextColor(oBtnCardSearch.getResources().getColor(android.R.color.white));
//        oBtnCardSearch.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                oBtnCardKindList.setBackgroundResource(R.drawable.spborde);
//                oBtnCardCreate.setBackgroundResource(R.drawable.spborde);
//                oBtnCardSearch.setBackgroundResource(R.drawable.spborde);
////                oBtnSalesReport.setBackgroundResource(R.drawable.spborde);
//                oMainWin_Right_one.setVisibility(View.GONE);
//                oMainWin_Right_two.setVisibility(View.GONE);
//                m_oWebViewStock.setVisibility(View.GONE);
//                m_oWebViewStatistics.setVisibility(View.GONE);
//
//                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.m_oMainActivity);
//                builder.setIcon(R.drawable.ic_launcher);
//                builder.setTitle("ѡ��һ���������");
//                //    ָ�������б����ʾ����
//                final String[] cities = GOperaterInfo.getDeptsList().split(",");
//                //    ����һ���������б�ѡ����
//                builder.setItems(cities, new DialogInterface.OnClickListener()
//                {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which)
//                    {
//                        GOperaterInfo.setDefaultDepts(which);
////                        if(m_txtGroupRoomCaption != null)
////                            m_txtGroupRoomCaption.setText(GOperaterInfo.m_strDefaultDeptSerialNo);
//
//                    }
//                });
////                builder.show();
//            }
//        });
//        oMainWin_left.addView(oBtnCardSearch);
//        ====================================================
        //���۱���
//        oBtnSalesReport = new Button(m_oContext);
//        oBtnSalesReport.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
//        oBtnSalesReport.setText("���۱���");
//        oBtnSalesReport.setBackgroundResource(R.drawable.spborde);
//        oBtnSalesReport.setTextColor(oBtnSalesReport.getResources().getColor(android.R.color.white));
//        oBtnSalesReport.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                oBtnCardKindList.setBackgroundResource(R.drawable.spborde);
//                oBtnCardCreate.setBackgroundResource(R.drawable.spborde);
//                oBtnCardSearch.setBackgroundResource(R.drawable.spborde);
//                oBtnSalesReport.setBackgroundResource(R.drawable.spborde_light);
//                oMainWin_Right_one.setVisibility(View.GONE);
//                oMainWin_Right_two.setVisibility(View.GONE);
//                m_oWebViewStock.setVisibility(View.GONE);
//                m_oWebViewStatistics.setVisibility(View.GONE);
//                Intent intent =new Intent(MainActivity.m_oMainActivity,GCashier_Salse_Report.class);
//                MainActivity.m_oMainActivity.startActivity(intent);
//            }
//        });
//        oMainWin_left.addView(oBtnSalesReport);
        //======================================================================================
//        //ɨ���ѯ
//        oBtnCardSearchByScanner = new Button(m_oContext);
//        oBtnCardSearchByScanner.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
//        oBtnCardSearchByScanner.setBackgroundResource(R.drawable.login);
//        oBtnCardSearchByScanner.setBackgroundResource(R.color.printbutton);
//        oBtnCardSearchByScanner.setTextColor(oBtnCardCreate.getResources().getColor(android.R.color.white));
//        oBtnCardSearchByScanner.setText("ɨ���ѯ");
//        oBtnCardSearchByScanner.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                oBtnCardKindList.setBackgroundResource(R.color.printbutton);
//                oBtnCardStorage.setBackgroundResource(R.color.printbutton);
//                oBtnCardUse.setBackgroundResource(R.color.printbutton);
//                oBtnCardStock.setBackgroundResource(R.color.printbutton);
//                oBtnCardCreate.setBackgroundResource(R.color.printbutton);
//                oBtnCardStatistics.setBackgroundResource(R.color.printbutton);
//                oBtnCardSearch.setBackgroundResource(R.color.printbutton);
//                oBtnCardSearchByScanner.setBackgroundResource(R.color.printbutton_light);
//                oMainWin_Right_one.setVisibility(View.GONE);
//                oMainWin_Right_two.setVisibility(View.GONE);
//                m_oWebViewStock.setVisibility(View.GONE);
//                m_oWebViewStatistics.setVisibility(View.GONE);
//                oMainWin_Right_picture.setVisibility(View.GONE);
//                if (MainActivity.m_bDebugCardNo) {
//                    if (m_oFragmentOne != null)
//                        m_oFragmentOne.onScannerResult(MainActivity.m_strDebugCardNo, MainActivity.SCAN_CODE_CAED_CONSUME);
//                } else {
//                    Intent intent = new Intent(MainActivity.m_oMainActivity, CaptureActivity.class);
//                    MainActivity.m_oMainActivity.startActivityForResult(intent, MainActivity.SCAN_CODE_CAED_CONSUME);
//                }
//            }
//        });
//        oMainWin_left.addView(oBtnCardSearchByScanner);
        //======================================================================================
        FrameLayout oMainWin_Right = new FrameLayout (oContext);
        oMainWin_Right.setLayoutParams(new LayoutParams(MainActivity.mSreenWidth*3 / 4, LayoutParams.MATCH_PARENT));
        oMainWin_Right.setBackgroundResource(R.color.lightgray);

        ImageView picture=new ImageView(m_oContext);
        picture.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        picture.setImageResource(R.drawable.picture_cashier);
        oMainWin_Right.addView(picture);
//        oMainWin_Right.addView(oMainWin_Right_one);



        oMainWin_Right_one = new LinearLayout(oContext);  //���Բ��ַ�ʽ
        oMainWin_Right_one.setOrientation(LinearLayout.VERTICAL); //�ؼ����䷽ʽΪ��ֱ����
        oMainWin_Right_one.setLayoutParams(new LayoutParams(MainActivity.mSreenWidth*3 / 4, LayoutParams.MATCH_PARENT));
        oMainWin_Right_one.setGravity(Gravity.CENTER_HORIZONTAL);
        oMainWin_Right_one.setVisibility(View.GONE);
        oMainWin_Right_one.setBackgroundResource(R.color.encode_view);
        TextView textView = new TextView(m_oContext);
        textView.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,150));
        textView.setBackgroundResource(R.color.encode_view);
        oMainWin_Right_one.addView(textView);

        LinearLayout startNum = new LinearLayout(oContext);  //���Բ��ַ�ʽ
        startNum.setOrientation(LinearLayout.HORIZONTAL); //�ؼ����䷽ʽΪ��ֱ����
        startNum.setLayoutParams(new LayoutParams(MainActivity.mSreenWidth *2/ 5, LayoutParams.WRAP_CONTENT));

        startNum.setBackgroundResource(R.color.lightgray);
        oMainWin_Right_one.addView(startNum);

        final TextView startNo = new TextView(m_oContext);
        startNo.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT,2));
        startNo.setText("��ʼ����");
        startNo.setGravity(Gravity.CENTER);
        startNo.setBackgroundResource(R.color.printbutton);
        startNo.setTextColor(startNo.getResources().getColor(android.R.color.white));
        startNum.addView(startNo);
        et_startNo = new EditText(m_oContext);
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

        LinearLayout endNum = new LinearLayout(oContext);  //���Բ��ַ�ʽ
        endNum.setOrientation(LinearLayout.HORIZONTAL); //�ؼ����䷽ʽΪ��ֱ����
        endNum.setLayoutParams(new LayoutParams(MainActivity.mSreenWidth *2/ 5, LayoutParams.WRAP_CONTENT));
        endNum.setBackgroundResource(R.color.lightgray);
        oMainWin_Right_one.addView(endNum);

        TextView endNo = new TextView(m_oContext);
        endNo.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT,2));
        endNo.setText("��ֹ����");
        endNo.setGravity(Gravity.CENTER);
        endNo.setBackgroundResource(R.color.printbutton);
        endNo.setTextColor(endNo.getResources().getColor(android.R.color.white));
        endNum.addView(endNo);
        et_endNo = new EditText(m_oContext);
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
        totalNum.setText("�ܼ�0��");
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

                double totalNo=Double.parseDouble(endno)-Double.parseDouble(startno)+1;

                totalNum.setText("�ܼ�"+totalNo+"��");

            }
        });
        oMainWin_Right_one.addView(totalNum);
        Button oBtnCardSave_Storage = new Button(m_oContext);
        oBtnCardSave_Storage.setLayoutParams(new LinearLayout.LayoutParams(MainActivity.mSreenWidth / 4, LayoutParams.WRAP_CONTENT));
        oBtnCardSave_Storage.setText("ȷ�ϱ���");
        oBtnCardSave_Storage.setGravity(Gravity.CENTER_HORIZONTAL);
        oBtnCardSave_Storage.setBackgroundResource(R.color.printbutton);
        oBtnCardSave_Storage.setTextColor(oBtnCardSave_Storage.getResources().getColor(android.R.color.white));
        oBtnCardSave_Storage.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                GSvrChannel svr= 	new GSvrChannel()
                {
                    public void onNetFailure(int statusCode,String strInfo)
                    {
                        MainActivity.MessageBox("����ʧ��","statusCode:"+statusCode+",Info:"+strInfo);
                        MainActivity.onUserMessageBox("����ʧ��", "����ʧ�ܣ����������Ƿ�ͨ������ϵ����Ա��");
                    }
                    public void onNetSuccess(int nCode,String strInfo,JSONObject oJsonData)
                    {
                        if(nCode < 0)
                        {
                            MainActivity.MessageBox("����ʧ��",strInfo);
                            MainActivity.onUserMessageBox("����ʧ��",strInfo);
                            return;
                        }
                        MainActivity.onUserMessageBox("���ɹ�",strInfo);

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
                    MainActivity.MessageBox("����ʧ��",e.getMessage());
                    MainActivity.onUserMessageBox("����ʧ��","����ʧ�ܣ����������Ƿ�ͨ������ϵ����Ա��");
                }
            }
        });
        oMainWin_Right_one.addView(oBtnCardSave_Storage);
        oMainWin_Right_two = new LinearLayout(oContext);  //���Բ��ַ�ʽ
        oMainWin_Right_two.setOrientation(LinearLayout.VERTICAL); //�ؼ����䷽ʽΪ��ֱ����
        oMainWin_Right_two.setLayoutParams(new LayoutParams(MainActivity.mSreenWidth*3 / 4, LayoutParams.MATCH_PARENT));
        oMainWin_Right_two.setGravity(Gravity.CENTER_HORIZONTAL);
        oMainWin_Right_two.setBackgroundResource(R.color.encode_view);
        oMainWin_Right_two.setVisibility(View.GONE);
        TextView textView_three = new TextView(m_oContext);
        textView_three.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,150));
        textView_three.setBackgroundResource(R.color.encode_view);
        oMainWin_Right_two.addView(textView_three);

        LinearLayout startNum_use = new LinearLayout(oContext);  //���Բ��ַ�ʽ
        startNum_use.setOrientation(LinearLayout.HORIZONTAL); //�ؼ����䷽ʽΪ��ֱ����
        startNum_use.setLayoutParams(new LayoutParams(MainActivity.mSreenWidth *2/ 5, LayoutParams.WRAP_CONTENT));

        startNum_use.setBackgroundResource(R.color.lightgray);
        oMainWin_Right_two.addView(startNum_use);

        final TextView startNo_use = new TextView(m_oContext);
        startNo_use.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT,2));
        startNo_use.setText("��ʼ����");
        startNo_use.setGravity(Gravity.CENTER);
        startNo_use.setBackgroundResource(R.color.printbutton);
        startNo_use.setTextColor(startNo_use.getResources().getColor(android.R.color.white));
        startNum_use.addView(startNo_use);
        et_startNo_use = new EditText(m_oContext);
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

        LinearLayout endNum_use = new LinearLayout(oContext);  //���Բ��ַ�ʽ
        endNum_use.setOrientation(LinearLayout.HORIZONTAL); //�ؼ����䷽ʽΪ��ֱ����
        endNum_use.setLayoutParams(new LayoutParams(MainActivity.mSreenWidth *2/ 5, LayoutParams.WRAP_CONTENT));
        endNum_use.setBackgroundResource(R.color.lightgray);
        oMainWin_Right_two.addView(endNum_use);

        TextView endNo_use = new TextView(m_oContext);
        endNo_use.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT,2));
        endNo_use.setText("��ֹ����");
        endNo_use.setGravity(Gravity.CENTER);
        endNo_use.setBackgroundResource(R.color.printbutton);
        endNo_use.setTextColor(endNo_use.getResources().getColor(android.R.color.white));
        endNum_use.addView(endNo_use);
        et_endNo_use = new EditText(m_oContext);
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
        totalNum_use.setText("�ܼ�0��");
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

                double totalNo=Double.parseDouble(endno_use)-Double.parseDouble(startno_use)+1;

                totalNum_use.setText("�ܼ�"+totalNo+"��");

            }
        });
        oMainWin_Right_two.addView(totalNum_use);
        Button oBtnCardSave_Use = new Button(m_oContext);
        oBtnCardSave_Use.setLayoutParams(new LinearLayout.LayoutParams(MainActivity.mSreenWidth / 4, LayoutParams.WRAP_CONTENT));
        oBtnCardSave_Use.setText("ȷ�ϱ���");
        oBtnCardSave_Use.setGravity(Gravity.CENTER_HORIZONTAL);
        oBtnCardSave_Use.setBackgroundResource(R.color.printbutton);
        oBtnCardSave_Use.setTextColor(oBtnCardSave_Use.getResources().getColor(android.R.color.white));
        oBtnCardSave_Use.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                GSvrChannel svr= 	new GSvrChannel()
                {
                    public void onNetFailure(int statusCode,String strInfo)
                    {
                        MainActivity.MessageBox("����ʧ��","statusCode:"+statusCode+",Info:"+strInfo);
                        MainActivity.onUserMessageBox("����ʧ��", "����ʧ�ܣ����������Ƿ�ͨ������ϵ����Ա��");
                    }
                    public void onNetSuccess(int nCode,String strInfo,JSONObject oJsonData)
                    {
                        if(nCode < 0)
                        {
                            MainActivity.MessageBox("����ʧ��",strInfo);
                            MainActivity.onUserMessageBox("����ʧ��",strInfo);
                            return;
                        }
                        MainActivity.onUserMessageBox("���óɹ�",strInfo);

                    }
                };
                try
                {
                    JSONObject   requestDatas = new JSONObject();
                    requestDatas.put("cardNoStart", startno_use);
                    requestDatas.put("cardNoEnd", endno_use);
                    requestDatas.put("groupUID", GOperaterInfo.m_strGroupUID);
                    svr.m_oCurrentActivity = MainActivity.m_oMainActivity;
                    svr.onPost("api/mobile/opCardUsed.do", requestDatas);
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                    MainActivity.MessageBox("����ʧ��",e.getMessage());
                    MainActivity.onUserMessageBox("����ʧ��","����ʧ�ܣ����������Ƿ�ͨ������ϵ����Ա��");
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
                view.loadUrl(url);     //��Ҫ����ϵͳ����
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
                view.loadUrl(url);     //��Ҫ����ϵͳ����
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
        strURL_Statistics = GSvrChannel.m_strURLcardStatistics + "?token=" + GOperaterInfo.m_strToken + "&callerName=" + GSvrChannel.CALLER_NAME + "&groupUID=" +GOperaterInfo.m_strGroupUID ;
        m_oWebViewStatistics.loadUrl(strURL_Statistics);

//        oMainWin_Right.addView(m_oWebViewStock);
//        oMainWin_Right.addView(m_oWebViewStatistics);
//        oMainWin_Right.addView(oMainWin_Right_one);
//        oMainWin_Right.addView(oMainWin_Right_two);
        oMainWin.addView(oMainWin_left);
        oMainWin.addView(oMainWin_Right);
        return oMainWin;
    }

    //=============================================================================================
    //����ҳ��ͷ��
    private View onCreatePageHead(Context oContext) {
        RelativeLayout oSubHeader = new RelativeLayout(oContext);  //���Բ��ַ�ʽ
        oSubHeader.setLayoutParams(new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));
        oSubHeader.setBackgroundColor(MainActivity.m_oHeadStoreColor);
        //=============================================================================
        //ʵʱʱ��
        RelativeLayout.LayoutParams pCurrentTime = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        pCurrentTime.addRule(RelativeLayout.ALIGN_LEFT);
        pCurrentTime.addRule(RelativeLayout.CENTER_VERTICAL);
        m_oCurrentTime = new TextView(oContext);

        m_oCurrentTime.setLayoutParams(pCurrentTime);
        m_oCurrentTime.setTextSize(16);
        m_oCurrentTime.setText(MainActivity.getStringDate());
        handler.postDelayed(runnable1, 1000); // ��ʼTimer
        m_oCurrentTime.setTextColor(Color.BLACK);
        oSubHeader.addView(m_oCurrentTime);
        //ͷ��
        RelativeLayout.LayoutParams pImgHead = new RelativeLayout.LayoutParams(60, 60);
        pImgHead.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        pImgHead.addRule(RelativeLayout.CENTER_VERTICAL);
        m_oImgHead = new RoundImageView(oContext);
        m_oImgHead.setId(1);
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

        //�ŵ�����
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
        //ѡ�������豸
        RelativeLayout.LayoutParams pconnectBlueTooth = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        pconnectBlueTooth.addRule(RelativeLayout.LEFT_OF,2);
        pconnectBlueTooth.setMargins(0,5,0,0);
        oTextBle = new Button(m_oContext);
        oTextBle.setLayoutParams(pconnectBlueTooth);

        oTextBle.setBackgroundResource(R.drawable.spborde);
        oTextBle.setTextSize(16);
        oTextBle.setGravity(Gravity.CENTER);
        oTextBle.setText("ѡ������");
        oTextBle.setTextColor(oTextBle.getResources().getColor(android.R.color.holo_red_dark));
        oTextBle.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                connectBLE();
            }
        });
        oSubHeader.addView(oTextBle);
        //���������豸
        RelativeLayout.LayoutParams m_oConnect = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        m_oConnect.addRule(RelativeLayout.LEFT_OF,1);
        m_oConnect.setMargins(0,5,0,0);
        Button oConnect = new Button(m_oContext);
        oConnect.setLayoutParams(m_oConnect);
        oConnect.setId(2);
        oConnect.setBackgroundResource(R.drawable.spborde);
        oConnect.setText("����");
        oConnect.setTextColor(oConnect.getResources().getColor(android.R.color.white));
        oConnect.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                sendble();
            }
        });
        oSubHeader.addView(oConnect);
        return oSubHeader;
    }

    public void onScannerResult(String strCardNo, int requestCode) {
        if (requestCode == MainActivity.USER_GROUP_CHANGE && m_oShopCaption != null)
            m_oShopCaption.setText(GOperaterInfo.m_strGroupName);
    }
    protected void connectBLE() {
        // TODO Auto-generated method stub
        setbluetooth();
        //sendble();
    }

    public void sendble() {
        binder.connectBtPort(oTextBle.getText().toString(), new UiExecute() {

            @Override
            public void onsucess() {
                // TODO Auto-generated method stub
                //���ӳɹ�����UI�߳��е�ִ��
                isConnect = true;

                Toast.makeText(MainActivity.m_oMainActivity, R.string.con_success, Toast.LENGTH_SHORT).show();
                oTextBle.setTextColor(oTextBle.getResources().getColor(android.R.color.white));



                //�˴�Ҳ���Կ�����ȡ��ӡ��������
                //����ͬ����һ��ʵ�ֵ�UiExecute�ӿڶ���
                //������Ĺ����س����쳣�������ж�����Ҳ�����쳣���Ѿ��Ͽ�
                //�����ȡ�ķ����У���һֱ��һ�����߳���ִ�ж�ȡ��ӡ�����������ݣ�
                //ֱ�����ӶϿ����쳣�Ž�������ִ��onfailed
                binder.acceptdatafromprinter(new UiExecute() {

                    @Override
                    public void onsucess() {
                        // TODO Auto-generated method stub

                    }

                    @Override
                    public void onfailed() {
                        // TODO Auto-generated method stub
                        isConnect = false;
                        Toast.makeText(MainActivity.m_oMainActivity, R.string.con_has_discon, Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onfailed() {
                // TODO Auto-generated method stub
                //����ʧ�ܺ���UI�߳��е�ִ��
                isConnect = false;
                Toast.makeText(MainActivity.m_oMainActivity, R.string.con_has_discon, Toast.LENGTH_SHORT).show();
                //btn0.setText("����ʧ��");
            }
        });
    }

    protected void setbluetooth() {
        // TODO Auto-generated method stub
        blueadapter = BluetoothAdapter.getDefaultAdapter();
        //ȷ�Ͽ�������
        if (!blueadapter.isEnabled()) {
            //�����û�����
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            MainActivity.m_oMainActivity.startActivityForResult(intent, Conts.ENABLE_BLUETOOTH);

        } else {
            //�����ѿ���
            showblueboothlist();
        }

    }

    private void showblueboothlist() {
        if (!blueadapter.isDiscovering()) {
            blueadapter.startDiscovery();
        }
        LayoutInflater inflater = LayoutInflater.from(MainActivity.m_oMainActivity);
        dialogView = inflater.inflate(R.layout.printer_list, null);
        adapter1 = new ArrayAdapter<String>(MainActivity.m_oMainActivity, android.R.layout.simple_list_item_1, deviceList_bonded);
        lv1 = (ListView) dialogView.findViewById(R.id.listView1);
        btn_scan = (Button) dialogView.findViewById(R.id.btn_scan);
        ll1 = (LinearLayout) dialogView.findViewById(R.id.ll1);
        lv2 = (ListView) dialogView.findViewById(R.id.listView2);
        adapter2 = new ArrayAdapter<String>(MainActivity.m_oMainActivity, android.R.layout.simple_list_item_1, deviceList_found);
        lv1.setAdapter(adapter1);
        lv2.setAdapter(adapter2);
        dialog = new AlertDialog.Builder(MainActivity.m_oMainActivity).setTitle("BLE").setView(dialogView).create();
        dialog.show();
        setlistener();
        findAvalibleDevice();
    }

    private void setlistener() {
        // TODO Auto-generated method stub
        btn_scan.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                ll1.setVisibility(View.VISIBLE);
                //btn_scan.setVisibility(View.GONE);
            }
        });
        //����Ե��豸�ĵ������
        lv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                // TODO Auto-generated method stub
                try {
                    if (blueadapter != null && blueadapter.isDiscovering()) {
                        blueadapter.cancelDiscovery();

                    }

                    String msg = deviceList_bonded.get(arg2);
                    mac = msg.substring(msg.length() - 17);
                    String name = msg.substring(0, msg.length() - 18);
                    //lv1.setSelection(arg2);
                    dialog.cancel();
                    oTextBle.setText(mac);

                    //Log.i("TAG", "mac="+mac);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });
        //δ��Ե��豸���������ԣ�������
        lv2.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                // TODO Auto-generated method stub
                try {
                    if (blueadapter != null && blueadapter.isDiscovering()) {
                        blueadapter.cancelDiscovery();

                    }
                    String msg = deviceList_found.get(arg2);
                    mac = msg.substring(msg.length() - 17);
                    String name = msg.substring(0, msg.length() - 18);
                    //lv2.setSelection(arg2);
                    dialog.cancel();
                    oTextBle.setText(mac);

                    Log.i("TAG", "mac=" + mac);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });
    }

    private void findAvalibleDevice() {
        // TODO Auto-generated method stub
        //��ȡ����������豸
        Set<BluetoothDevice> device = blueadapter.getBondedDevices();

        deviceList_bonded.clear();
        if (blueadapter != null && blueadapter.isDiscovering()) {
            adapter1.notifyDataSetChanged();
        }
        if (device.size() > 0) {
            //�����Ѿ���Թ��������豸
            for (Iterator<BluetoothDevice> it = device.iterator(); it.hasNext(); ) {
                BluetoothDevice btd = it.next();
                deviceList_bonded.add(btd.getName() + '\n' + btd.getAddress());
                adapter1.notifyDataSetChanged();
            }
        } else {  //�������Ѿ���Թ��������豸
            deviceList_bonded.add("No can be matched to use bluetooth");
            adapter1.notifyDataSetChanged();
        }

    }
//    public void printall(){
//        if (isConnect) {
//            // TODO Auto-generated method stub
//            //���ӡ��������ӡָ��ʹ�ӡ���ݣ����ô˷���
//            //��һ������������UiExecute�ӿڵ�ʵ�֣��ֱ��Ƿ������ݳɹ���ʧ�ܺ���ui�̵߳Ĵ���
//            binder.writeDataByYouself(new UiExecute() {
//
//                @Override
//                public void onsucess() {
//                    // TODO Auto-generated method stub
//                    Toast.makeText(MainActivity.m_oMainActivity, R.string.send_success, Toast.LENGTH_SHORT)
//                            .show();
////                    if (wh_listview.getChildAt(0) != null) {
////                        String taskUID = ((TextView) wh_listview.getChildAt(0).findViewById(R.id.m_oisPrintTask)).getText().toString();
////                        m_oWareHouseDetailListDAO.opPrnTaskComplete(taskUID);
////                    }
//                }
//
//                @Override
//                public void onfailed() {
//                    // TODO Auto-generated method stub
//                    Toast.makeText(MainActivity.m_oMainActivity, R.string.send_failed, Toast.LENGTH_SHORT)
//                            .show();
//                }
//            }, new ProcessData() {//�ڶ���������ProcessData�ӿڵ�ʵ��
//                //����ӿڵ���дprocessDataBeforeSend���������Ҫ���͵�ָ��
//
//                @Override
//                public List<byte[]> processDataBeforeSend() {
//                    // TODO Auto-generated method stub
//                    //��ʼ��һ��list
//                    ArrayList<byte[]> list = new ArrayList<byte[]>();
//                    //�ڴ�ӡ����������ô�ӡ���ݵ��ַ��������ͣ�Ĭ��Ϊgbk����ѡ���ӡ����ʶ������ͣ��ο�����ֲᣬ��ӡ����ҳ
//                    DataForSendToPrinterTSC.setCharsetName("gbk");//�����ã�Ĭ��Ϊgbk
//                    //ͨ��������õ�һ��ָ���byte[]����,���ı�Ϊ��
//                    //���ȵ�����size��ǩ�ߴ�,��60mm,��30mm,Ҳ���Ե�����dot��inchΪ��λ�ķ������廻��ο�����ֲ�
////                    DataForSendToPrinterTSC.sizeBymm(360, 360);
//
//                    try {
//
//
//
//                        if (wh_listview.getChildAt(0) != null) {
//                            TextView tv = (TextView) wh_listview.getChildAt(0).findViewById(R.id.lv_goodsTask);
//                            Map map = (Map) tv.getTag();
//                            String orderNumber =(String) map.get("orderUID");
//                            String m_otxtRoomNo = (String) map.get("roomSerialNo");
//                            String m_ocardNumber = (String) map.get("cardUID");
//                            String m_oorderTime = (String) map.get("orderTime");
//                            String m_osalseman = (String) map.get("userCaption");
//
//                            list.add(("            "+GOperaterInfo.m_strGroupName + "\n").getBytes("gbk"));
//                            list.add(("���ţ�" + orderNumber + "\n").getBytes("gbk"));
//                            list.add(("���ţ�" + m_otxtRoomNo + "\n").getBytes("gbk"));
//                            list.add(("���ţ�" + m_ocardNumber + "\n").getBytes("gbk"));
//
////                            DataForSendToPrinterTSC.sizeBymm(60, 60);
////                            DataForSendToPrinterTSC.bar(20, 40, 200, 3);
//                            list.add(("----------------------------"+ "\n").getBytes("gbk"));
//                            list.add(("      ����      " + "����  " + "��λ  " + "\n").getBytes("gbk"));
//                            list.add(("----------------------------"+ "\n").getBytes("gbk"));
//                            for (int i = 0; i <((List<Map>) map.get("ar1")).size(); i++) {
//                                String m_ogoodsCaption = (String) ((List<Map>) map.get("ar1")).get(i).get("goodsCaption");
//                                String m_ogoodsNumber = (String) ((List<Map>) map.get("ar1")).get(i).get("goodsNumber");
//                                String m_ogoodsUnitName = (String) ((List<Map>) map.get("ar1")).get(i).get("goodsUnitName");
//                                if (m_ogoodsCaption.getBytes("GBK").length == 1) {
//                                    list.add(("      "+m_ogoodsCaption + "           ").getBytes("gbk"));
//                                    list.add((m_ogoodsNumber + "   ").getBytes("gbk"));
//                                    list.add((m_ogoodsUnitName + "\n").getBytes("gbk"));
//                                } else if (m_ogoodsCaption.getBytes("GBK").length == 2) {
//                                    list.add(("      "+m_ogoodsCaption + "          ").getBytes("gbk"));
//                                    list.add((m_ogoodsNumber + "   ").getBytes("gbk"));
//                                    list.add((m_ogoodsUnitName + "\n").getBytes("gbk"));
//                                }else if (m_ogoodsCaption.getBytes("GBK").length == 3) {
//                                    list.add(("      "+m_ogoodsCaption + "         ").getBytes("gbk"));
//                                    list.add((m_ogoodsNumber + "   ").getBytes("gbk"));
//                                    list.add((m_ogoodsUnitName + "\n").getBytes("gbk"));
//                                } else if(m_ogoodsCaption.getBytes("GBK").length == 4) {
//                                    list.add(("      "+m_ogoodsCaption + "        ").getBytes("gbk"));
//                                    list.add((m_ogoodsNumber + "   ").getBytes("gbk"));
//                                    list.add((m_ogoodsUnitName + "\n").getBytes("gbk"));
//                                } else if(m_ogoodsCaption.getBytes("GBK").length== 5) {
//                                    list.add(("      "+m_ogoodsCaption + "       ").getBytes("gbk"));
//                                    list.add((m_ogoodsNumber + "   ").getBytes("gbk"));
//                                    list.add((m_ogoodsUnitName + "\n").getBytes("gbk"));
//                                }else if(m_ogoodsCaption.getBytes("GBK").length == 6) {
//                                    list.add(("      "+m_ogoodsCaption + "      ").getBytes("gbk"));
//                                    list.add((m_ogoodsNumber + "   ").getBytes("gbk"));
//                                    list.add((m_ogoodsUnitName + "\n").getBytes("gbk"));
//                                }else if(m_ogoodsCaption.getBytes("GBK").length== 7) {
//                                    list.add(("      "+m_ogoodsCaption + "     ").getBytes("gbk"));
//                                    list.add((m_ogoodsNumber + "   ").getBytes("gbk"));
//                                    list.add((m_ogoodsUnitName + "\n").getBytes("gbk"));
//                                }else if(m_ogoodsCaption.getBytes("GBK").length == 8) {
//                                    list.add(("    "+m_ogoodsCaption + "      ").getBytes("gbk"));
//                                    list.add((m_ogoodsNumber + "   ").getBytes("gbk"));
//                                    list.add((m_ogoodsUnitName + "\n").getBytes("gbk"));
//                                } else if(m_ogoodsCaption.getBytes("GBK").length == 9) {
//                                    list.add(("    "+m_ogoodsCaption + "     ").getBytes("gbk"));
//                                    list.add((m_ogoodsNumber + "   ").getBytes("gbk"));
//                                    list.add((m_ogoodsUnitName + "\n").getBytes("gbk"));
//                                }else if(m_ogoodsCaption.getBytes("GBK").length == 10) {
//                                    list.add(("   "+m_ogoodsCaption + "     ").getBytes("gbk"));
//                                    list.add((m_ogoodsNumber + "   ").getBytes("gbk"));
//                                    list.add((m_ogoodsUnitName + "\n").getBytes("gbk"));
//                                }else if(m_ogoodsCaption.getBytes("GBK").length == 11) {
//                                    list.add(("    "+m_ogoodsCaption + "    ").getBytes("gbk"));
//                                    list.add((m_ogoodsNumber + "   ").getBytes("gbk"));
//                                    list.add((m_ogoodsUnitName + "\n").getBytes("gbk"));
//                                }else if(m_ogoodsCaption.getBytes("GBK").length == 12) {
//                                    list.add(("  "+m_ogoodsCaption + "     ").getBytes("gbk"));
//                                    list.add((m_ogoodsNumber + "   ").getBytes("gbk"));
//                                    list.add((m_ogoodsUnitName + "\n").getBytes("gbk"));
//                                }
//                            }
//                            list.add(("----------------------------"+ "\n").getBytes("gbk"));
//
//                            list.add(("�µ�ʱ�䣺" + m_oorderTime + "\n").getBytes("gbk"));
//                            list.add(("����Ա��" + m_osalseman + "\n").getBytes("gbk"));
//                            list.add(( "\n").getBytes("gbk"));
//                            list.add(( "\n").getBytes("gbk"));
//                        }
//                    } catch (UnsupportedEncodingException e) {
//                        e.printStackTrace();
//                    }
//
////                            byte[] data0 = DataForSendToPrinterTSC
////                                    .sizeBymm(60, 30);
////                            list.add(data0);
//                    //����Gap,ͬ��
////                            list.add(DataForSendToPrinterTSC.gapBymm(0,
////                                    0));
////
////                            //�������
////                            list.add(DataForSendToPrinterTSC.cls());
//                    //����ָ�������int x��x�����ӡ��ʼ�㣻int y��y�����ӡ��ʼ�㣻
//                    //string font���������ͣ�int rotation����ת�Ƕȣ�
//                    //int x_multiplication������x����Ŵ���
//                    //int y_multiplication,y����Ŵ���
//                    //string content����ӡ����
////                            byte[] data1 = DataForSendToPrinterTSC
////                                    .text(10, 10, "0", 0, 1, 1,
////                                            "abc123");
////                            list.add(data1);
//                    //��ӡֱ��,int x;int y;int width,�ߵĿ�ȣ�int height,�ߵĸ߶�
////                            list.add(DataForSendToPrinterTSC.bar(20,
////                                    40, 200, 3));
//                    //��ӡ����
////                            list.add(DataForSendToPrinterTSC.barCode(
////                                    60, 50, "128", 100, 1, 0, 2, 2,
////                                    "abcdef12345"));
//                    //��ӡ
//
//                    DataForSendToPrinterTSC.print(1);
//                    return list;
//                }
//            });
//        } else {
//            Toast.makeText(MainActivity.m_oMainActivity, R.string.not_con_printer, Toast.LENGTH_SHORT).show();
//        }
//
//    }
}