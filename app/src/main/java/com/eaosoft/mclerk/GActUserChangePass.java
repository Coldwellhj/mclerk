package com.eaosoft.mclerk;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.eaosoft.userinfo.GOperaterInfo;
import com.eaosoft.util.ActivityCollector;
import com.eaosoft.util.GSvrChannel;

import org.json.JSONException;
import org.json.JSONObject;

public class GActUserChangePass extends Activity 
{
	private EditText mEtOldPwd;
	private EditText mEtNewPwd;
	private EditText mEtAgainNewPwd;
	private String 	 m_strUserNewPassword="";
	private Button mBtnOk;
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.act_user_pwd_update);
		
		ActivityCollector.addActivity(this);
		// 初始化控件
		initView();
	}

	@Override
	protected void onDestroy() 
	{
		super.onDestroy();
		ActivityCollector.removeActivity(this);
	}

	/**
	 * 初始化控件
	 */
	private void initView() 
	{
		mEtOldPwd = (EditText) this.findViewById(R.id.edt_old_pw);
		mEtNewPwd = (EditText) this.findViewById(R.id.edt_new_pw);
		mEtAgainNewPwd = (EditText) this.findViewById(R.id.edt_again_new_pw);

		mBtnOk = (Button) this.findViewById(R.id.btn_ok);

		mBtnOk.setOnClickListener(listener);
	}
	private void OnChangeUserPassword(String strPass)
	{
		GSvrChannel svr= 	new GSvrChannel()
    	{
    		public void onNetFinish()
    		{
    			
    		}
    		public void onNetFailure(int statusCode,String strInfo)
    		{
    			MainActivity.onUserMessageBox("用户密码修改","statusCode:"+statusCode+",Info:"+strInfo);
    		}
    		public void onNetSuccess(int nCode,String strInfo,JSONObject oJsonData)
    		{
    			if(nCode < 0)
    			{
    				MainActivity.onUserMessageBox("用户密码修改",strInfo);
    				return;
    			}
    				SharedPreferences preferences = getSharedPreferences(MainActivity.m_szMainConfigString,Context.MODE_PRIVATE);
    				GOperaterInfo.m_strPassword = m_strUserNewPassword;
	    			preferences.edit().putString("LoginUserPass", m_strUserNewPassword).commit();
	    			MainActivity.onUserMessageBox("用户密码修改","密码修改成功！");
	    			finish();
    		}
    	};
    	 try 
         {
    		JSONObject   requestDatas = new JSONObject();
	    	requestDatas.put("oldPassword",  GOperaterInfo.m_strPassword);
	    	requestDatas.put("password", m_strUserNewPassword);
	    	
	    	svr.m_oCurrentActivity = this;
	    	svr.onPost("api/mobile/opChangePassword.do", requestDatas);
         }
    	 catch (JSONException e) 
         {
             e.printStackTrace();
             MainActivity.onUserMessageBox("发送注册校验码",e.getMessage());
         }
	}
	OnClickListener listener = new OnClickListener() 
	{

		@Override
		public void onClick(View v) 
		{
			String oldpwd = mEtOldPwd.getText().toString().trim();
			String newpwd = mEtNewPwd.getText().toString().trim();
			String againnewpwd = mEtAgainNewPwd.getText().toString().trim();

			if (TextUtils.isEmpty(oldpwd)) 
			{
				MainActivity.onUserMessageBox("密码修改", "请输入旧密码！");
				return;
			}
			/*
			if (ListActivityBean.OnCheckPassword(oldpwd) ==false)
			{
				MainActivity.onUserMessageBox("密码修改", "旧密码有误，请重新输入！");
				return;
			}
			*/

			if (TextUtils.isEmpty(newpwd)) 
			{
				MainActivity.onUserMessageBox("密码修改", "请输入新密码！");
				return;
			}

			if (TextUtils.isEmpty(againnewpwd)) 
			{
				MainActivity.onUserMessageBox("密码修改", "请再次输入新密码！");
				return;
			}

			if (!newpwd.equals(againnewpwd)) 
			{
				MainActivity.onUserMessageBox("密码修改","新密码不一致，请重新输入！");
				return;
			}
			m_strUserNewPassword = GOperaterInfo.OnMakePassword(againnewpwd);
			OnChangeUserPassword(m_strUserNewPassword);
		}
	};
}