package com.eaosoft.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.eaosoft.mclerk.R;

import java.text.SimpleDateFormat;
import java.util.Date;

import static java.lang.Integer.parseInt;

public class DateAdapter extends BaseAdapter {
	private static String TAG = "ZzL";
	private boolean isLeapyear = false; // 是否为闰年
	private int daysOfMonth = 0; // 某月的天数
	private int dayOfWeek = 0; // 具体某一天是星期几
	private int nextDayOfWeek = 0;
	private int lastDayOfWeek = 0;
	private int lastDaysOfMonth = 0; // 上一个月的总天数
	private int eachDayOfWeek = 0;
	private Context context;

	private Resources res = null;
	private Drawable drawable = null;
	private String[] MonthNumber = new String[7];
    private String[] YearNumber = new String[7];
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-d");
	private int currentFlag = -1; // 用于标记当天
	// 系统当前时间
	private String sysDate = "";
	private String sys_year = "";
	private String sys_month = "";
	private String sys_day = "";
	private String currentYear = "";
	private String currentMonth = "";
	private String currentWeek = "";
	private String currentDay = "";
	private int weeksOfMonth;
	private int default_postion;
	private int clickTemp = 0;
	private int week_num = 0;
	private int week_c = 0;
	private int month = 0;
	private int jumpWeek = 0;
	private int c_month = 0;
	private int c_day_week = 0;
	private int n_day_week = 0;
	private boolean isStart;
    private int currentY;

    // 标识选择的Item
	public void setSeclection(int position) {
		clickTemp = position;
	}

	public DateAdapter() {
		Date date = new Date();
		sysDate = sdf.format(date); // 当期日期
		sys_year = sysDate.split("-")[0];
		sys_month = sysDate.split("-")[1];
		sys_day = sysDate.split("-")[2];
		month = parseInt(sys_month);
	}

	public DateAdapter(Context context, Resources rs, int year_c, int month_c,
                       int week_num, int default_postion, boolean isStart) {
		this();
		this.context = context;
		this.res = rs;
		this.default_postion = default_postion;

		this.isStart = isStart;



		currentYear = String.valueOf(year_c);
		; // 得到当前的年份
		currentMonth = String.valueOf(month_c); // 得到本月
												// （jumpMonth为滑动的次数，每滑动一次就增加一月或减一月）


		getWeek(parseInt(currentYear), parseInt(currentMonth)
				);

	}





	public void getWeek(int year, int month) {
		for (int i = 0; i < MonthNumber.length; i++) {
            if(month+i<=12){
                MonthNumber[i] = String.valueOf(month+i);
                YearNumber[i] = String.valueOf(currentYear);
            }else {
                MonthNumber[i] = String.valueOf(month+i-12);
                YearNumber[i] = String.valueOf(Integer.parseInt(currentYear)+1);
            }

		}
	}

	public String[] getMonthNumbers() {
		return MonthNumber;
	}
    public String[] getYearNumbers() {
        return YearNumber;
    }



	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return MonthNumber.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.item_calendar, null);
		}
		TextView tvMonth = (TextView) convertView.findViewById(R.id.tv_month);
		TextView tvYear = (TextView) convertView.findViewById(R.id.tv_year);
        ImageView round = (ImageView) convertView.findViewById(R.id.round);
        if(MonthNumber[position]==currentMonth&&YearNumber[position]==currentYear){
            tvMonth.setText("本月");
            tvYear.setText("");
        }else {
            tvMonth.setText(MonthNumber[position] + "月");
            tvYear.setText(YearNumber[position] + "年");
        }
		if (clickTemp == position) {
            tvMonth.setSelected(true);
            tvMonth.setTextColor(Color.BLACK);
            round.setBackgroundResource(R.drawable.round1);
		} else {
            tvMonth.setSelected(false);
            tvMonth.setTextColor(Color.BLACK);
            round.setBackgroundResource(R.drawable.round);
            tvMonth.setBackgroundColor(Color.TRANSPARENT);
		}
		return convertView;
	}


    public int getCurrentYear() {

            return currentY;

    }


}
