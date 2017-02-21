package com.eaosoft.util;
 
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface.OnClickListener;
 
public class GUtilDialogTool 
{
  
    /**
     * ������ͨ�Ի���
     * 
     * @param ctx ������ ����
     * @param iconId ͼ�꣬�磺R.drawable.icon ����
     * @param title ���� ����
     * @param message ��ʾ���� ����
     * @param btnName ��ť���� ����
     * @param listener ����������ʵ��android.content.DialogInterface.OnClickListener�ӿ� ����
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
  // ���öԻ����ͼ��
  builder.setIcon(iconId);
  // ���öԻ���ı���
  builder.setTitle(title);
  // ���öԻ������ʾ����
  builder.setMessage(message);
  // ��Ӱ�ť��android.content.DialogInterface.OnClickListener.OnClickListener
  builder.setPositiveButton(btnName, listener);
  // ����һ����ͨ�Ի���
  dialog = builder.create();
  return dialog;
 }
  
  
    /**
     * �����б�Ի���
     * 
     * @param ctx ������ ����
     * @param iconId ͼ�꣬�磺R.drawable.icon ����
     * @param title ���� ����
     * @param itemsId �ַ���������Դid ����
     * @param listener ����������ʵ��android.content.DialogInterface.OnClickListener�ӿ� ����
     * @return
     */
 public static Dialog createListDialog(Context ctx, 
   int iconId, 
   String title, 
   int itemsId, 
   OnClickListener listener) {
  Dialog dialog=null;
  android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(ctx);
  // ���öԻ����ͼ��
  builder.setIcon(iconId);
  // ���öԻ���ı���
  builder.setTitle(title);
  // ��Ӱ�ť��android.content.DialogInterface.OnClickListener.OnClickListener
  builder.setItems(itemsId, listener);
  // ����һ���б�Ի���
  dialog = builder.create();
  return dialog;
 }
  
    /**
     * ������ѡ��ť�Ի���
     * 
     * @param ctx ������ ����
     * @param iconId ͼ�꣬�磺R.drawable.icon ����
     * @param title ���� ����
     * @param itemsId �ַ���������Դid ����
     * @param listener ��ѡ��ť�����������ʵ��android.content.DialogInterface.OnClickListener�ӿ� ����
     * @param btnName ��ť���� ����
     * @param listener2 ��ť����������ʵ��android.content.DialogInterface.OnClickListener�ӿ� ����
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
  // ���öԻ����ͼ��
  builder.setIcon(iconId);
  // ���öԻ���ı���
  builder.setTitle(title);
  // 0: Ĭ�ϵ�һ����ѡ��ť��ѡ��
  builder.setSingleChoiceItems(itemsId, 0, listener);
  // ���һ����ť
  builder.setPositiveButton(btnName, listener2) ;
  // ����һ����ѡ��ť�Ի���
  dialog = builder.create();
  return dialog;
 }
  
  
    /**
     * ������ѡ�Ի���
     * 
     * @param ctx ������ ����
     * @param iconId ͼ�꣬�磺R.drawable.icon ����
     * @param title ���� ����
     * @param itemsId �ַ���������Դid ����
     * @param flags ��ʼ��ѡ��� ����
     * @param listener ��ѡ��ť�����������ʵ��android.content.DialogInterface.OnMultiChoiceClickListener�ӿ� ����
     * @param btnName ��ť���� ����
     * @param listener2 ��ť����������ʵ��android.content.DialogInterface.OnClickListener�ӿ� ����
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
  // ���öԻ����ͼ��
  builder.setIcon(iconId);
  // ���öԻ���ı���
  builder.setTitle(title);
  builder.setMultiChoiceItems(itemsId, flags, listener);
  // ���һ����ť
  builder.setPositiveButton(btnName, listener2) ;
  // ����һ����ѡ�Ի���
  dialog = builder.create();
  return dialog;
 }
/*
 * public class DialogActivity extends Activity {
 private boolean[] flags=new boolean[]{false,true,false}; //��ʼ��ѡ���
 private String[] items=null;
  
    private EditText etText=null;
    private Button btnNormal=null;
    private Button btnList=null; 
    private Button btnRadio=null;
    private Button btnCheckBox=null;
     
    private static final int DIALOG_NORMAL=0; //��ͨ�Ի�����
    private static final int DIALOG_LIST=1; //�б�Ի�����
    private static final int DIALOG_RADIO=2; //��ѡ��ť�Ի�����
    private static final int DIALOG_CHECKBOX=3; //��ѡ�Ի�����
     
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
  case DIALOG_NORMAL: //������ͨ�Ի���
   dialog = DialogTool.createNormalDialog(this, 
     R.drawable.icon, 
     "��ͨ�Ի���", 
     "������ͨ�Ի����е����ݣ�", 
     " ȷ �� ", 
        new android.content.DialogInterface.OnClickListener(){
      @Override
      public void onClick(DialogInterface dialog, int which) {
       etText.setText("������ͨ�Ի����е����ݣ�");
       return;
      }
              }
            );
   break;
  case DIALOG_LIST: // �����б�Ի���
   dialog = DialogTool.createListDialog(this, 
     R.drawable.icon, 
     "�б�Ի���", 
     R.array.hobby, 
        new android.content.DialogInterface.OnClickListener(){
      @Override
      public void onClick(DialogInterface dialog, int which) {
       String hoddy=getResources().getStringArray(R.array.hobby)[which];     
       etText.setText("��ѡ���ˣ� "+hoddy);
       return;
      }
              }
            );
   break;
  case DIALOG_RADIO: // ������ѡ��ť�Ի���
   dialog=DialogTool.createRadioDialog(this, 
     R.drawable.icon,
     "��ѡ��ť�Ի���",
     R.array.hobby, 
        new android.content.DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
       String hoddy = getResources().getStringArray(
         R.array.hobby)[which];
       etText.setText("��ѡ���ˣ� " + hoddy);
       return;
      }
     },
        " ȷ �� ",
           new android.content.DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
       Toast.makeText(DialogActivity.this, 
         "������ȷ����ť��", Toast.LENGTH_LONG).show();
       return;
      }
     }
   );
   break;
  case DIALOG_CHECKBOX: // ������ѡ��Ի���
   dialog=DialogTool.createCheckBoxDialog(this, 
     R.drawable.icon,
     "��ѡ�Ի���",
     R.array.hobby,
     flags, 
     new DialogInterface.OnMultiChoiceClickListener() {
      public void onClick(DialogInterface dialog, int which, boolean isChecked) {
       flags[which] = isChecked;
       String result = "��ѡ���ˣ�";
       for (int i = 0; i < flags.length; i++) {
        if (flags[i]) {
         result = result + items[i] + "��";
        }
       }
       etText.setText(result.substring(0, result.length() - 1));
      }
     },
     " ȷ �� ",
           new android.content.DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
       Toast.makeText(DialogActivity.this, "������ȷ����ť��", Toast.LENGTH_LONG).show();
       return;
      }
     }
       
   );
   break;
  }
     return dialog;
    }
     
    //��ť����
    View.OnClickListener l = new View.OnClickListener() {
  @Override
  public void onClick(View v) {
   Button btn = (Button) v;
   switch (btn.getId()) {
   case R.id.btnNormal: //��ͨ�Ի���
                //��ʾ�Ի���
    showDialog(DIALOG_NORMAL);
    break;
   case R.id.btnList: //�б�Ի���
                //��ʾ�Ի���
    showDialog(DIALOG_LIST);
    break;
   case R.id.btnRadio: //��ѡ��ť�Ի���
                //��ʾ�Ի���
    showDialog(DIALOG_RADIO);
    break;
   case R.id.btnCheckBox: //��ѡ�Ի���
                //��ʾ�Ի���
    showDialog(DIALOG_CHECKBOX);
    break;
   }
  }
 };
}*/
}
