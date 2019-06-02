package net.hunau.mymenu;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.content.Intent;
import java.lang.reflect.Method;
import android.view.MenuItem;
import android.widget.TextView;
public class MainActivity extends AppCompatActivity {
    final static int MENU_00 = Menu.FIRST;
    final static int MENU_01 = Menu.FIRST+1;
    final static int MENU_02 = Menu.FIRST+2;
    final static int MENU_03 = Menu.FIRST+3;
    final static int MENU_04 = Menu.FIRST+4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0,MENU_00,0,"页面切换").setIcon(R.drawable.pic0);
        menu.add(0,MENU_01,1,"新建").setIcon(R.drawable.pic1);
        menu.add(0,MENU_02,2,"邮件").setIcon(R.drawable.pic2);
        menu.add(0,MENU_03,3,"设置").setIcon(R.drawable.pic3);
        menu.add(0,MENU_04,4,"订阅").setIcon(R.drawable.pic4);
        return true;
    }

    public boolean onMenuOpened(int featureId, Menu menu) {
        if (menu != null) {
            if (menu.getClass().getSimpleName().equalsIgnoreCase("MenuBuilder")) try {
                Method method = menu.getClass().getDeclaredMethod("setOptionalIconsVisible", Boolean.TYPE);
                method.setAccessible(true);
                method.invoke(menu, true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return super.onMenuOpened(featureId, menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        TextView label = (TextView)findViewById(R.id.label);
        switch (item.getItemId()) {
            case MENU_00:
                label.setText("页面切换，菜单ID：" + item.getItemId());
                Intent intent =  new Intent(MainActivity.this,SubActivity.class);
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
}
