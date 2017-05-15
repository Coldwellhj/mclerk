package com.eaosoft.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.InputType;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.eaosoft.mclerk.R;
import com.eaosoft.util.GBaseAdapter;
import com.eaosoft.util.ListItemClickHelp;

import java.util.List;
import java.util.Map;

public class GCardKind_Goods_detailAdapter extends GBaseAdapter
{
    public List list;
    private ListItemClickHelp callback;
    public GCardKind_Goods_detailAdapter(Context context, List list,ListItemClickHelp callback)
	{
		super(context);
        this.list=list;
        this.callback=callback;
	}

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
	public View getView(final int arg0, View arg1,  ViewGroup arg2)
	{
		final viewHolder holder;
		if (arg1 == null)
		{
			holder = new viewHolder();
			arg1 = inflater.inflate(R.layout.activity_gcashier__package_goodsdetail, null);
			holder.tv_goodsName = (TextView) arg1.findViewById(R.id.tv_goodsName);
			holder.tv_goodsNum = (TextView) arg1.findViewById(R.id.tv_goodsNum);
			holder.tv_goodsUnit = (TextView) arg1.findViewById(R.id.tv_goodsUnit);
			holder.tv_goodsPrice = (TextView) arg1.findViewById(R.id.tv_goodsPrice);
			holder.iv_goodsDelete = (ImageView) arg1.findViewById(R.id.iv_goodsDelete);

			arg1.setTag(holder);			
		}
		else
		{
			holder = (viewHolder) arg1.getTag();
		}
		if(list == null)
			return arg1;
		final Map map = (Map) list.get(arg0);

		holder.tv_goodsName.setText(map.get("caption").toString());
		holder.tv_goodsNum.setText(map.get("num").toString());
		holder.tv_goodsUnit.setText(map.get("unitName").toString());
		holder.tv_goodsPrice.setText(map.get("price").toString());
        holder.tv_goodsNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText inputServer = new EditText(v.getContext());
                inputServer.setTag(v);
                inputServer.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_NORMAL);
                inputServer.setFocusable(true);
                inputServer.setText(holder.tv_goodsNum.getText());

                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setTitle("请输入数量").setIcon(R.drawable.ic_launcher).setView(inputServer).setNegativeButton("取消", null);
                builder.setPositiveButton("确认",new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        int nNum=0;
                        String inputName = inputServer.getText().toString();
                        try{nNum = Integer.parseInt(inputName);}catch(NumberFormatException ex){nNum=0;};

                        holder.tv_goodsNum.setText(""+nNum);
                        map.put("num", ""+nNum);
//                        callback.onReadTotalMoney();
                    }
                });
                builder.show();



            }
        });
        holder.iv_goodsDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setTitle("确认删除？").setIcon(R.drawable.ic_launcher).setNegativeButton("取消", null);
                builder.setPositiveButton("确认",new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which)
                    {

                        callback.onClick(arg0);
                    }
                });
                builder.show();




            }
        });
		//=============================================================================
		return arg1;
	}

	private class viewHolder
	{

		private TextView 					tv_goodsName;
		private TextView 					tv_goodsNum;
		private TextView 					tv_goodsUnit;
		private TextView 					tv_goodsPrice;
		private ImageView 					iv_goodsDelete;

	}
}