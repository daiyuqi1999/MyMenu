package net.hunau.mymenu;
import SQLite.DBAdapter;
import SQLite.Student;
import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import java.lang.reflect.Method;
import android.view.View.OnClickListener;
import SQLite.DBAdapter;

public class SubActivity extends MainActivity {
    final static int MENU_00 = Menu.FIRST;
    final static int MENU_01 = Menu.FIRST+1;
    final static int MENU_02 = Menu.FIRST+2;
    final static int MENU_03 = Menu.FIRST+3;
    final static int MENU_04 = Menu.FIRST+4;
    private DBAdapter dbAdepter ;
    private RadioButton radioButton1,radioButton,radioButton2,radioButton3;
    private EditText nameText;
    private EditText pwdText;

    private EditText idEntry;
    private Boolean ischeck,issexy;
    private TextView labelView;
    private TextView displayView;
    @Override
    protected void onDestroy() {
        dbAdepter.close();
        super.onDestroy();
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub);
        TextView label2=(TextView)findViewById(R.id.label2);
       Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String msg = bundle.getString("data");
        label2.setText("接受传到的数据的值是:"+msg);

        nameText = (EditText)findViewById(R.id.name);
        pwdText = (EditText)findViewById(R.id.pwd);

        idEntry = (EditText)findViewById(R.id.id_entry);

        labelView = (TextView)findViewById(R.id.label);
        displayView = (TextView)findViewById(R.id.display);
        radioButton = findViewById(R.id.radio);
        radioButton1 = findViewById(R.id.radio1);
        radioButton2 = findViewById(R.id.radio2);
        radioButton3 = findViewById(R.id.radio3);
        radioButton.setOnClickListener(buttonOnclickListen);
        radioButton1.setOnClickListener(buttonOnclickListen);
        radioButton2.setOnClickListener(buttonOnclickListen);
        radioButton3.setOnClickListener(buttonOnclickListen);



        Button addButton = (Button)findViewById(R.id.add);
        Button queryAllButton = (Button)findViewById(R.id.query_all);
        Button clearButton = (Button)findViewById(R.id.clear);
        Button deleteAllButton = (Button)findViewById(R.id.delete_all);

        Button queryButton = (Button)findViewById(R.id.query);
        Button deleteButton = (Button)findViewById(R.id.delete);
        Button updateButton = (Button)findViewById(R.id.update);


        addButton.setOnClickListener(buttonOnclickListen);
        queryAllButton.setOnClickListener(buttonOnclickListen);
        clearButton.setOnClickListener(buttonOnclickListen);
        deleteAllButton.setOnClickListener(buttonOnclickListen);

        queryButton.setOnClickListener(buttonOnclickListen);
        deleteButton.setOnClickListener(buttonOnclickListen);
        updateButton.setOnClickListener(buttonOnclickListen);

        dbAdepter = new DBAdapter(this);
        dbAdepter.open();
    }
    OnClickListener buttonOnclickListen = new OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.radio:
                    ischeck = true;
                    break;
                case R.id.radio1:
                    ischeck = false;
                    break;
                case R.id.radio2:
                    issexy = true;
                    break;
                case R.id.radio3:
                    issexy = false;
                    break;
                case R.id.add:
                    Student student = new Student();
                    student.setName(nameText.getText().toString());
                    student.setPwd(pwdText.getText().toString());
                    if (issexy)
                        student.setSexy(radioButton2.getText().toString());
                    else
                        student.setSexy(radioButton3.getText().toString());
                    if (ischeck)
                    {
                        student.setIsused(1);
                        Log.d("ta","判断无问题");
                    }
                    else
                        student.setIsused(0);
                    long colunm = dbAdepter.insert(student);
                    if (colunm == -1 ){
                        labelView.setText("添加过程错误！");
                    } else {
                        labelView.setText("成功添加数据，ID："+String.valueOf(colunm));

                    }
                    break;

                case R.id.query_all:
                    Student[] students = dbAdepter.queryAllData();
                    if (students == null){
                        labelView.setText("数据库中没有数据");
                        return;
                    }
                    labelView.setText("数据库：");
                    String msg = "";
                    for (int i = 0 ; i<students.length; i++){
                        msg += students[i].toString()+"\n";
                    }
                    displayView.setText(msg);
                    break;

                case R.id.clear:
                    displayView.setText("");
                    break;

                case  R.id.delete_all:
                    dbAdepter.deleteAllData();
                    String msg1 = "数据全部删除";
                    labelView.setText(msg1);
                    break;

                case R.id.query:
                    int id = Integer.parseInt(idEntry.getText().toString());
                    Student[] students1 = dbAdepter.queryOneData(id);

                    if (students1 == null){
                        labelView.setText("数据库中没有ID为"+String.valueOf(id)+"的数据");
                        return;
                    }
                    labelView.setText("数据库：");
                    displayView.setText(students1[0].toString());
                    break;

                case R.id.delete:
                    long id1 = Integer.parseInt(idEntry.getText().toString());
                    long result = dbAdepter.deleteOneData(id1);
                    String msg2 = "删除ID为"+idEntry.getText().toString()+"的数据" + (result>0?"成功":"失败");
                    labelView.setText(msg2);
                    break;

                case R.id.update:
                    Student student1 = new Student();
                    student1.setName(nameText.getText().toString());
                    student1.setPwd(pwdText.getText().toString());
                    if (issexy)
                        student1.setSexy(radioButton2.getText().toString());
                    else
                        student1.setSexy(radioButton3.getText().toString());
                    if (ischeck)
                        student1.setIsused(1) ;
                    else
                        student1.setIsused(0) ;
                    long id2 = Integer.parseInt(idEntry.getText().toString());
                    long count = dbAdepter.updateOneData(id2, student1);
                    if (count == -1 ){
                        labelView.setText("更新错误！");
                    } else {
                        labelView.setText("更新成功，更新数据"+String.valueOf(count)+"条");

                    }
                    break;
            }
        }
    };
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0,MENU_00,0,"页面切换").setIcon(R.drawable.pic0);
        menu.add(0,MENU_01,1,"新建").setIcon(R.drawable.pic1);
        menu.add(0,MENU_02,2,"邮件").setIcon(R.drawable.pic2);
        menu.add(0,MENU_03,3,"设置").setIcon(R.drawable.pic3);
        menu.add(0,MENU_04,4,"订阅").setIcon(R.drawable.pic4);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        TextView label = (TextView)findViewById(R.id.label2);
        switch (item.getItemId()) {
            case MENU_00:
                label.setText("页面切换，菜单ID：" + item.getItemId());
                Intent intent =  new Intent(SubActivity.this,MainActivity.class);
                intent.putExtra("data",label.getText());
                startActivity(intent);
                return true;
            case MENU_01:
                label.setText("新建，菜单ID：" + item.getItemId());
                return true;
            case MENU_02:
                label.setText("邮件，菜单ID：" + item.getItemId());
                return true;
            case MENU_03:
                label.setText("设置，菜单ID：" + item.getItemId());
                return true;
            case MENU_04:
                label.setText("订阅，菜单ID：" + item.getItemId());
                return true;
            default:
                return false;
        }
    }
    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        if (menu != null) {
            if (menu.getClass().getSimpleName().equalsIgnoreCase("MenuBuilder")) {
                try {
                    Method method = menu.getClass().getDeclaredMethod("setOptionalIconsVisible", Boolean.TYPE);
                    method.setAccessible(true);
                    method.invoke(menu, true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return super.onMenuOpened(featureId, menu);
    }
}
