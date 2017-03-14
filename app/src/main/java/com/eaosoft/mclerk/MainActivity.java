package com.eaosoft.mclerk;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.eaosoft.fragment.GFragmentFive;
import com.eaosoft.fragment.GFragmentFour;
import com.eaosoft.fragment.GFragmentOne;
import com.eaosoft.fragment.GFragmentThree;
import com.eaosoft.fragment.GFragmentTwo;
import com.eaosoft.userinfo.GOperaterInfo;
import com.eaosoft.util.ActivityCollector;
import com.eaosoft.util.GUtilSDCard;

import net.posprinter.posprinterface.IMyBinder;
import net.posprinter.service.PosprinterService;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends FragmentActivity {
    //==============================================
    public static IMyBinder binder;//IMyBinder接口，所有可供调用的连接和发送数据的方法都封装在这个接口内
    ServiceConnection conn=new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {
            // TODO Auto-generated method stub

        }
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            // TODO Auto-generated method stub
            //绑定成功
            binder=(IMyBinder) service;
        }
    };
    //系统基础参数
    public static GOperaterInfo m_oOperaterInfo = null;
    public static MainActivity m_oMainActivity = null;
    public static String m_szAppBasePath = "";
    public static String m_szMainConfigString = "";
    public static int m_nPixelsFromDP = 1;//每DP有多少像素
    //===================================================
    private static ProgressDialog m_oProgressDialog = null;
    public static final int PROGRESS_NETWORK_WAIT_START = 1;
    public static final int PROGRESS_NETWORK_WAIT_END = 2;
    public static final int JUMP_FRAME_PAGE = 3;//跳转到
    public static final int FINISH_APP = -9999;//结束进程
    public static final int USER_REFRESH_MAINFACE = 100;//刷新主界面

    //==================================================
    private GFragmentOne m_oFragmentOne = null;
    private GFragmentTwo m_oFragmentTwo = null;
    private GFragmentThree m_oFragmentThree = null;
    private GFragmentFour m_oFragmentFour = null;
    private GFragmentFive m_oFragmentFive = null;

    private LinearLayout m_oLayOne;
    private LinearLayout m_oLayTwo;
    private LinearLayout m_oLayThree;
    private LinearLayout m_oLayFour;
    private LinearLayout m_oLayFive;

    private ImageView m_oImageOne;
    private ImageView m_oImageTwo;
    private ImageView m_oImageThree;
    private ImageView m_oImageFour;
    private ImageView m_oImageFive;

    private TextView m_oTextOne;
    private TextView m_oTextTwo;
    private TextView m_oTextThree;
    private TextView m_oTextFour;
    private TextView m_oTextFive;

    private FragmentManager m_oFrmManager;
    //==============================================
    public static final int UI_OP_ROLE_SALSE = 0x01;//销售员
    public static final int UI_OP_ROLE_STORE = 0x02;//仓管员
    public static final int UI_OP_ROLE_CASHIER = 0x04;//收银员
    public static final int UI_OP_ROLE_MANAGER = 0x08;//管理者
    public static final int UI_OP_ROLE_CHIFE = 0x10;//总监
    public static final int UI_OP_ROLE_SUPPER = 0x20;//超级管理员


    //==============================================
    public static int m_nOperaterUI = UI_OP_ROLE_CASHIER;//
    public static int m_oHeadColor = Color.rgb(65, 195, 168);
    public static int m_oHeadStoreColor = Color.rgb(235, 235, 235);
    //==============================================
    public static boolean m_bUserConfirmBarcode = true;//是否需要用户确认扫描码
    public static final int USER_NETWORK_LOGIN = 0x00;//用户登录
    public static final int SCAN_CODE_ORDER_CREATE = 0x01;//扫码下单
    public static final int SCAN_CODE_CAED_INFO = 0x02;//扫码查询
    public static final int SCAN_CODE_CAED_CONSUME = 0x03;//扫码查询消费记录
    public static final int SCAN_CODE_CAED_CREATE = 0x04;//扫码建新卡
    public static final int USER_GROUP_CHANGE = 0x10;//更换门店
    //==============================================
    public static String m_strDebugCardNo = "6921734953017";
    public static boolean m_bDebugCardNo = true;
    public static int mSreenWidth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

       ActivityCollector.addActivity(this);

        m_oMainActivity = this;
        onInitApp(this);
        m_oFrmManager = getSupportFragmentManager();
        //=============================
        if (GOperaterInfo.m_strUID.length() < 1) {
            Intent intent = new Intent(this, GActUserLogin.class);
            startActivityForResult(intent, USER_NETWORK_LOGIN);
        } else {
                initView();
                getFragment(1);
        }

        //获取屏幕宽度
        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        mSreenWidth = metric.widthPixels;     // 屏幕宽度（像素）
        Intent intent=new Intent(this,PosprinterService.class);
        bindService(intent,conn,BIND_AUTO_CREATE);
    }

    @Override
    protected void onResume() {
        /**
         * 设置为横屏
         */
        if (MainActivity.m_nOperaterUI == MainActivity.UI_OP_ROLE_STORE||MainActivity.m_nOperaterUI == MainActivity.UI_OP_ROLE_CASHIER) {
            if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            }
        }
        super.onResume();
    }

    public static void onInitApp(Activity oActivity) {

        GUtilSDCard.ConfirmAppBasePath(MainActivity.m_oMainActivity, "/mclerk/");
        MainActivity.m_nPixelsFromDP = getPixelsFromDp(1, oActivity);
        MainActivity.m_oOperaterInfo = new GOperaterInfo();
        if (MainActivity.m_oOperaterInfo != null)
            MainActivity.m_oOperaterInfo.onReadConfig();

    }

    public static void MessageBox(String strTitle, String strText)//这个作为日志记录
    {

    }

    public static void onUserMessageBox(String strTitle, String strText) {
        Toast.makeText(MainActivity.m_oMainActivity, strText, Toast.LENGTH_LONG).show();
    }

    public static Handler m_oMsgHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            try {
                super.handleMessage(msg);
                switch (msg.what) {
                    case USER_REFRESH_MAINFACE:
                    {
                        if (MainActivity.m_oMainActivity.m_oFragmentOne != null)
                            MainActivity.m_oMainActivity.m_oFragmentOne.OnRefresh();
                    }break;
                    case FINISH_APP: {
                        MainActivity.m_oMainActivity.finish();
                        System.exit(0);
                    }break;
                    case PROGRESS_NETWORK_WAIT_START: {
                        //if(m_oProgressDialog==null)

                        if (msg.obj == null)
                        {
                            if(m_oProgressDialog==null)
                                m_oProgressDialog = new ProgressDialog(MainActivity.m_oMainActivity);
                        }
                        else
                        {
                            if(m_oProgressDialog==null)
                                m_oProgressDialog = new ProgressDialog((Context) msg.obj);
                        }
                        if (m_oProgressDialog != null) {
                            m_oProgressDialog.setTitle("网络链接");
                            m_oProgressDialog.setMessage("正在链接服务器，请稍候...");
                            m_oProgressDialog.setIcon(R.drawable.ic_launcher);
                            m_oProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                            m_oProgressDialog.setCancelable(false);
                            m_oProgressDialog.show();
                        }
                    }
                    break;
                    case PROGRESS_NETWORK_WAIT_END: {

                        if (m_oProgressDialog != null) {
                            m_oProgressDialog.dismiss();
                            m_oProgressDialog = null;
                        }
                    }
                    break;
                    default:
                        break;
                }
            } catch (Exception e) {
            }
        }

        ;
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK)
            return;
        if (requestCode == USER_NETWORK_LOGIN && data != null) {
            if (data.getStringExtra("login_result").equalsIgnoreCase("login_ok")) {

                    // 初始化控件
                    initView();
                    getFragment(1);
                    return;


            }
        }
        if (requestCode == USER_GROUP_CHANGE)//用户更换门店了
        {
            if (m_oFragmentOne != null)
                m_oFragmentOne.onScannerResult("", requestCode);
            return;
        }
        if (data != null && requestCode >= SCAN_CODE_ORDER_CREATE && requestCode <= SCAN_CODE_CAED_CREATE) {
            String strScannerCode = data.getStringExtra("scan_result");
            if (m_bUserConfirmBarcode)//用户确认扫描的码
            {
                final EditText inputServer = new EditText(this);
                inputServer.setFocusable(true);
                inputServer.setText(strScannerCode);
                inputServer.setTag(requestCode);

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("请确认扫描卡号").setView(inputServer).setNegativeButton("取消", null);

                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String inputName = inputServer.getText().toString();
                        int nRequestCode = 0;
                        try {
                            nRequestCode = Integer.parseInt(inputServer.getTag().toString());
                        } catch (Exception ex) {
                            return;
                        }
                        ;
                        if (m_oFragmentOne != null)
                            m_oFragmentOne.onScannerResult(inputName, nRequestCode);
                    }
                });
                builder.show();
            } else {
                if (m_oFragmentOne != null)
                    m_oFragmentOne.onScannerResult(strScannerCode, requestCode);
            }
            return;
        }
        finish();
        System.exit(0);
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();


        unbindService(conn);
        ActivityCollector.removeActivity(this);
    }

    public static int getPixelsFromDp(int size, Activity oActivity) {
        DisplayMetrics metrics = new DisplayMetrics();
        oActivity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        return (size * metrics.densityDpi) / DisplayMetrics.DENSITY_DEFAULT;
    }

    // 初始化控件
    private void initView() {
        m_oLayOne = (LinearLayout) this.findViewById(R.id.ll_tab_1);
        m_oLayTwo = (LinearLayout) this.findViewById(R.id.ll_tab_2);
        m_oLayThree = (LinearLayout) this.findViewById(R.id.ll_tab_3);
        m_oLayFour = (LinearLayout) this.findViewById(R.id.ll_tab_4);
        m_oLayFive = (LinearLayout) this.findViewById(R.id.ll_tab_5);

        m_oImageOne = (ImageView) this.findViewById(R.id.iv_tab_1);
        m_oImageTwo = (ImageView) this.findViewById(R.id.iv_tab_2);
        m_oImageThree = (ImageView) this.findViewById(R.id.iv_tab_3);
        m_oImageFour = (ImageView) this.findViewById(R.id.iv_tab_4);
        m_oImageFive = (ImageView) this.findViewById(R.id.iv_tab_5);

        m_oTextOne = (TextView) this.findViewById(R.id.tv_tab_1);
        m_oTextTwo = (TextView) this.findViewById(R.id.tv_tab_2);
        m_oTextThree = (TextView) this.findViewById(R.id.tv_tab_3);
        m_oTextFour = (TextView) this.findViewById(R.id.tv_tab_4);
        m_oTextFive = (TextView) this.findViewById(R.id.tv_tab_5);

        // Tab点击事件
        m_oLayOne.setOnClickListener(lay_listener);
        m_oLayTwo.setOnClickListener(lay_listener);
        m_oLayThree.setOnClickListener(lay_listener);
        m_oLayFour.setOnClickListener(lay_listener);
        m_oLayFive.setOnClickListener(lay_listener);

        //===============================================
        //本期系统中只有２个Frament
        if (m_oMainActivity != null) {
            if (MainActivity.m_nOperaterUI == MainActivity.UI_OP_ROLE_STORE) {
                m_oLayOne.setVisibility(View.GONE);
                m_oLayTwo.setVisibility(View.GONE);
                m_oLayThree.setVisibility(View.GONE);
                m_oLayFour.setVisibility(View.GONE);
                m_oLayFive.setVisibility(View.GONE);

                m_oImageOne.setVisibility(View.GONE);
                m_oImageTwo.setVisibility(View.GONE);
                m_oImageThree.setVisibility(View.GONE);
                m_oImageFour.setVisibility(View.GONE);
                m_oImageFive.setVisibility(View.GONE);

                m_oTextOne.setVisibility(View.GONE);
                m_oTextTwo.setVisibility(View.GONE);
                m_oTextThree.setVisibility(View.GONE);
                m_oTextFour.setVisibility(View.GONE);
                m_oTextFive.setVisibility(View.GONE);
            } else {
                m_oLayTwo.setVisibility(View.GONE);
                m_oLayThree.setVisibility(View.GONE);
                m_oLayFour.setVisibility(View.GONE);

                m_oImageTwo.setVisibility(View.GONE);
                m_oImageThree.setVisibility(View.GONE);
                m_oImageFour.setVisibility(View.GONE);

                m_oTextTwo.setVisibility(View.GONE);
                m_oTextThree.setVisibility(View.GONE);
                m_oTextFour.setVisibility(View.GONE);

                m_oTextFive.setText("管理");
                m_oLayOne.setBackgroundColor(m_oHeadColor);
                m_oLayFive.setBackgroundColor(m_oHeadColor);
            }
        }
    }

    OnClickListener lay_listener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.ll_tab_1)
                getFragment(1);
            if (v.getId() == R.id.ll_tab_2)
                getFragment(2);
            if (v.getId() == R.id.ll_tab_3)
                getFragment(3);
            if (v.getId() == R.id.ll_tab_4)
                getFragment(4);
            if (v.getId() == R.id.ll_tab_5)
                getFragment(5);
        }

    };

    private void getFragment(int i) {
        // 重置图片和字体为暗色
        resetImageAndText();
        // 开启事务
        FragmentTransaction transaction = m_oFrmManager.beginTransaction();
        // 隐藏所有Fragment
        hideFragment(transaction);

        if (i == 1) {
            m_oImageOne.setImageResource(R.drawable.main_tab1_pressed);
            m_oTextOne.setTextColor(this.getResources().getColor(R.color.orange));
            if (m_oFragmentOne == null) {
                m_oFragmentOne = new GFragmentOne();
                transaction.add(R.id.fl_content, m_oFragmentOne);
            } else {
                transaction.show(m_oFragmentOne);
            }
        }

        if (i == 2) {
            m_oImageTwo.setImageResource(R.drawable.main_tab2_pressed);
            m_oTextTwo.setTextColor(this.getResources().getColor(R.color.orange));
            if (m_oFragmentTwo == null) {
                m_oFragmentTwo = new GFragmentTwo();
                transaction.add(R.id.fl_content, m_oFragmentTwo);
            } else {
                transaction.show(m_oFragmentTwo);
            }
        }

        if (i == 3) {
            m_oImageThree.setImageResource(R.drawable.main_tab3_pressed);
            m_oTextThree.setTextColor(this.getResources().getColor(R.color.orange));
            if (m_oFragmentThree == null) {
                m_oFragmentThree = new GFragmentThree();
                transaction.add(R.id.fl_content, m_oFragmentThree);
            } else {
                transaction.show(m_oFragmentThree);
            }
        }

        if (i == 4) {
            m_oImageFour.setImageResource(R.drawable.main_tab4_pressed);
            m_oTextFour.setTextColor(this.getResources().getColor(R.color.orange));
            if (m_oFragmentFour == null) {
                m_oFragmentFour = new GFragmentFour();
                transaction.add(R.id.fl_content, m_oFragmentFour);
            } else {
                transaction.show(m_oFragmentFour);
            }
        }

        if (i == 5) {
            m_oImageFive.setImageResource(R.drawable.main_tab5_pressed);
            m_oTextFive.setTextColor(this.getResources().getColor(R.color.orange));
            if (m_oFragmentFive == null) {
                m_oFragmentFive = new GFragmentFive();
                transaction.add(R.id.fl_content, m_oFragmentFive);
            } else {
                transaction.show(m_oFragmentFive);
            }
        }

        // 提交事务！！！
        transaction.commit();
    }

    // 隐藏所有的Fragment
    private void hideFragment(FragmentTransaction transaction) {
        if (m_oFragmentOne != null)
            transaction.hide(m_oFragmentOne);
        if (m_oFragmentTwo != null)
            transaction.hide(m_oFragmentTwo);
        if (m_oFragmentThree != null)
            transaction.hide(m_oFragmentThree);
        if (m_oFragmentFour != null)
            transaction.hide(m_oFragmentFour);
        if (m_oFragmentFive != null)
            transaction.hide(m_oFragmentFive);
    }

    // 重置图片为暗色
    private void resetImageAndText() {
        m_oImageOne.setImageResource(R.drawable.main_tab1_nomal);
        m_oTextOne.setTextColor(this.getResources().getColor(R.color.black));

        m_oImageTwo.setImageResource(R.drawable.main_tab2_nomal);
        m_oTextTwo.setTextColor(this.getResources().getColor(R.color.black));

        m_oImageThree.setImageResource(R.drawable.main_tab3_nomal);
        m_oTextThree.setTextColor(this.getResources().getColor(R.color.black));

        m_oImageFour.setImageResource(R.drawable.main_tab4_nomal);
        m_oTextFour.setTextColor(this.getResources().getColor(R.color.black));

        m_oImageFive.setImageResource(R.drawable.main_tab5_nomal);
        m_oTextFive.setTextColor(this.getResources().getColor(R.color.black));
    }

    /**
     * 定义一个接口，实现两个Fragment之间的通信
     */
    public interface MyCommunication {
        public void getResultFragment(int indx);
    }

    private long exitTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 获取现在时间
     *
     * @return返回字符串格式 yyyy-MM-dd HH:mm:ss
     */
    public static String getStringDate() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = formatter.format(currentTime);
        return dateString;
    }

}
