package com.eaosoft.util;
 
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface.OnClickListener;
 
public class GUtilDialogTool 
{
  
    /**
     * 创建普通对话框
     * 
     * @param ctx 上下文 必填
     * @param iconId 图标，如：R.drawable.icon 必填
     * @param title 标题 必填
     * @param message 显示内容 必填
     * @param btnName 按钮名称 必填
     * @param listener 监听器，需实现android.content.DialogInterface.OnClickListener接口 必填
     * @return
     */
 public static Dialog createNormalDialog(Context ctx, 
   int iconId, 
   String title, 
   String message, 
   String btnName, 
   OnClickListener listener) {
  Dialog dialog=null;
  android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(ctx);
  // 设置对话框的图标
  builder.setIcon(iconId);
  // 设置对话框的标题
  builder.setTitle(title);
  // 设置对话框的显示内容
  builder.setMessage(message);
  // 添加按钮，android.content.DialogInterface.OnClickListener.OnClickListener
  builder.setPositiveButton(btnName, listener);
  // 创建一个普通对话框
  dialog = builder.create();
  return dialog;
 }
  
  
    /**
     * 创建列表对话框
     * 
     * @param ctx 上下文 必填
     * @param iconId 图标，如：R.drawable.icon 必填
     * @param title 标题 必填
     * @param itemsId 字符串数组资源id 必填
     * @param listener 监听器，需实现android.content.DialogInterface.OnClickListener接口 必填
     * @return
     */
 public static Dialog createListDialog(Context ctx, 
   int iconId, 
   String title, 
   int itemsId, 
   OnClickListener listener) {
  Dialog dialog=null;
  android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(ctx);
  // 设置对话框的图标
  builder.setIcon(iconId);
  // 设置对话框的标题
  builder.setTitle(title);
  // 添加按钮，android.content.DialogInterface.OnClickListener.OnClickListener
  builder.setItems(itemsId, listener);
  // 创建一个列表对话框
  dialog = builder.create();
  return dialog;
 }
  
    /**
     * 创建单选按钮对话框
     * 
     * @param ctx 上下文 必填
     * @param iconId 图标，如：R.drawable.icon 必填
     * @param title 标题 必填
     * @param itemsId 字符串数组资源id 必填
     * @param listener 单选按钮项监听器，需实现android.content.DialogInterface.OnClickListener接口 必填
     * @param btnName 按钮名称 必填
     * @param listener2 按钮监听器，需实现android.content.DialogInterface.OnClickListener接口 必填
     * @return
     */
 public static Dialog createRadioDialog(Context ctx, 
   int iconId, 
   String title, 
   int itemsId, 
   OnClickListener listener,
   String btnName,
   OnClickListener listener2) 
 {
  Dialog dialog=null;
  android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(ctx);
  // 设置对话框的图标
  builder.setIcon(iconId);
  // 设置对话框的标题
  builder.setTitle(title);
  // 0: 默认第一个单选按钮被选中
  builder.setSingleChoiceItems(itemsId, 0, listener);
  // 添加一个按钮
  builder.setPositiveButton(btnName, listener2) ;
  // 创建一个单选按钮对话框
  dialog = builder.create();
  return dialog;
 }
  
  
    /**
     * 创建复选对话框
     * 
     * @param ctx 上下文 必填
     * @param iconId 图标，如：R.drawable.icon 必填
     * @param title 标题 必填
     * @param itemsId 字符串数组资源id 必填
     * @param flags 初始复选情况 必填
     * @param listener 单选按钮项监听器，需实现android.content.DialogInterface.OnMultiChoiceClickListener接口 必填
     * @param btnName 按钮名称 必填
     * @param listener2 按钮监听器，需实现android.content.DialogInterface.OnClickListener接口 必填
     * @return
     */
 public static Dialog createCheckBoxDialog(Context ctx, 
   int iconId, 
   String title, 
   int itemsId, 
   boolean[] flags,
   android.content.DialogInterface.OnMultiChoiceClickListener listener,
   String btnName,
   OnClickListener listener2) {
  Dialog dialog=null;
  android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(ctx);
  // 设置对话框的图标
  builder.setIcon(iconId);
  // 设置对话框的标题
  builder.setTitle(title);
  builder.setMultiChoiceItems(itemsId, flags, listener);
  // 添加一个按钮
  builder.setPositiveButton(btnName, listener2) ;
  // 创建一个复选对话框
  dialog = builder.create();
  return dialog;
 }
/*
 * public class DialogActivity extends Activity {
 private boolean[] flags=new boolean[]{false,true,false}; //初始复选情况
 private String[] items=null;
  
    private EditText etText=null;
    private Button btnNormal=null;
    private Button btnList=null; 
    private Button btnRadio=null;
    private Button btnCheckBox=null;
     
    private static final int DIALOG_NORMAL=0; //普通对话框常量
    private static final int DIALOG_LIST=1; //列表对话框常量
    private static final int DIALOG_RADIO=2; //单选按钮对话框常量
    private static final int DIALOG_CHECKBOX=3; //复选对话框常量
     
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
         
        items=getResources().getStringArray(R.array.hobby);
         
        etText=(EditText)findViewById(R.id.etText);
        btnNormal=(Button)findViewById(R.id.btnNormal);
        btnList=(Button)findViewById(R.id.btnList);
        btnRadio=(Button)findViewById(R.id.btnRadio);
        btnCheckBox=(Button)findViewById(R.id.btnCheckBox);
        btnNormal.setOnClickListener(l);
        btnList.setOnClickListener(l);
        btnRadio.setOnClickListener(l);
        btnCheckBox.setOnClickListener(l);
    }
     
    @Override
    protected Dialog onCreateDialog(int id) {
     Dialog dialog=null;
     switch (id) {
  case DIALOG_NORMAL: //创建普通对话框
   dialog = DialogTool.createNormalDialog(this, 
     R.drawable.icon, 
     "普通对话框", 
     "这是普通对话框中的内容！", 
     " 确 定 ", 
        new android.content.DialogInterface.OnClickListener(){
      @Override
      public void onClick(DialogInterface dialog, int which) {
       etText.setText("这是普通对话框中的内容！");
       return;
      }
              }
            );
   break;
  case DIALOG_LIST: // 创建列表对话框
   dialog = DialogTool.createListDialog(this, 
     R.drawable.icon, 
     "列表对话框", 
     R.array.hobby, 
        new android.content.DialogInterface.OnClickListener(){
      @Override
      public void onClick(DialogInterface dialog, int which) {
       String hoddy=getResources().getStringArray(R.array.hobby)[which];     
       etText.setText("您选择了： "+hoddy);
       return;
      }
              }
            );
   break;
  case DIALOG_RADIO: // 创建单选按钮对话框
   dialog=DialogTool.createRadioDialog(this, 
     R.drawable.icon,
     "单选按钮对话框",
     R.array.hobby, 
        new android.content.DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
       String hoddy = getResources().getStringArray(
         R.array.hobby)[which];
       etText.setText("您选择了： " + hoddy);
       return;
      }
     },
        " 确 定 ",
           new android.content.DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
       Toast.makeText(DialogActivity.this, 
         "您按了确定按钮！", Toast.LENGTH_LONG).show();
       return;
      }
     }
   );
   break;
  case DIALOG_CHECKBOX: // 创建复选框对话框
   dialog=DialogTool.createCheckBoxDialog(this, 
     R.drawable.icon,
     "复选对话框",
     R.array.hobby,
     flags, 
     new DialogInterface.OnMultiChoiceClickListener() {
      public void onClick(DialogInterface dialog, int which, boolean isChecked) {
       flags[which] = isChecked;
       String result = "您选择了：";
       for (int i = 0; i < flags.length; i++) {
        if (flags[i]) {
         result = result + items[i] + "、";
        }
       }
       etText.setText(result.substring(0, result.length() - 1));
      }
     },
     " 确 认 ",
           new android.content.DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
       Toast.makeText(DialogActivity.this, "您按了确定按钮！", Toast.LENGTH_LONG).show();
       return;
      }
     }
       
   );
   break;
  }
     return dialog;
    }
     
    //按钮监听
    View.OnClickListener l = new View.OnClickListener() {
  @Override
  public void onClick(View v) {
   Button btn = (Button) v;
   switch (btn.getId()) {
   case R.id.btnNormal: //普通对话框
                //显示对话框
    showDialog(DIALOG_NORMAL);
    break;
   case R.id.btnList: //列表对话框
                //显示对话框
    showDialog(DIALOG_LIST);
    break;
   case R.id.btnRadio: //单选按钮对话框
                //显示对话框
    showDialog(DIALOG_RADIO);
    break;
   case R.id.btnCheckBox: //复选对话框
                //显示对话框
    showDialog(DIALOG_CHECKBOX);
    break;
   }
  }
 };
}*/
}
