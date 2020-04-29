package edu.upc.mishu.ui.activities;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.xuexiang.xupdate.XUpdate;
import com.xuexiang.xutil.tip.ToastUtils;

import java.util.List;

import edu.upc.mishu.App;
import edu.upc.mishu.R;
import edu.upc.mishu.dto.PasswordRecord;
import edu.upc.mishu.interfaces.Transformable;
import edu.upc.mishu.ui.fragment.EctFragment;
import edu.upc.mishu.ui.fragment.PasswordFragment;
import edu.upc.mishu.ui.fragment.SettingFragment;
import edu.upc.mishu.ui.fragment.SynchronousFragment;
import edu.upc.mishu.utils.ImportAndExport;

public class MainActivity extends AppCompatActivity  {

    private static final String TAG = "MainActivity";

    private PasswordFragment passwordFragment;
    private SynchronousFragment synchronousFragment;
    private EctFragment ectFragment;
    private SettingFragment settingFragment;
    private Fragment[] fragments;
    private int lastFragment;//上一个fragment
    private Toolbar toolbar;
    private TextView title;

    private DrawerLayout drawerLayout;
    private BottomNavigationView navigation;
    private NavigationView leftnavigation;



    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void init(){

        drawerLayout = findViewById(R.id.main_drawer_layout);

//        drawerLayout.setScrimColor(Color.WHITE);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.default_personlogo);

        toolbar.inflateMenu(R.menu.toolbarmenu);
        toolbar.setNavigationOnClickListener(v -> {
            Toast.makeText(MainActivity.this,"click",Toast.LENGTH_SHORT).show();
            drawerLayout.openDrawer(GravityCompat.START);
        });

        //搜索的入口
        toolbar.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()){
                case R.id.toolbar_search:
                    Toast.makeText(MainActivity.this,"tooasdfi",Toast.LENGTH_SHORT).show();
                    return true;
            }
            return false;
        });

        title = findViewById(R.id.toolbar_title);
        title.setText("密码" );


        Transformable encoder= App.encoder;
        PasswordRecord.deleteAll(PasswordRecord.class);
        PasswordRecord.builder().type("login").name("腾讯").url("http://www.tencent.com/").username("test1").password("123123").note("test").build().encode(encoder,1).save();
        PasswordRecord.builder().type("login").name("百度").url("https://www.baidu.com/").username("test2").password("123456").note("test").build().encode(encoder,1).save();
        PasswordRecord.builder().type("login").name("阿里云").url("https://www.aliyun.com/").username("test3").password("123789").note("test").build().encode(encoder,1).save();
        PasswordRecord.builder().type("login").name("新浪").url("https://www.sina.com.cn/").username("test4").password("123321").note("test").build().encode(encoder,1).save();
        PasswordRecord.builder().type("login").name("知乎").url("https://www.zhihu.com/hot").username("test5").password("12312431").note("test").build().encode(encoder,1).save();
        PasswordRecord.builder().type("login").name("163邮箱").url("https://mail.163.com/").username("test6").password("123").note("test").build().encode(encoder,1).save();
        PasswordRecord.builder().type("login").name("3DM").url("https://www.3dmgame.com/").username("test7").password("1238892").note("test").build().encode(encoder,1).save();
        PasswordRecord.builder().type("login").name("游民星空").url("https://www.gamersky.com/").username("test8").password("2310").note("test").build().encode(encoder,1).save();
        PasswordRecord.builder().type("login").name("百度贴吧").url("https://tieba.baidu.com/").username("test9").password("1239").note("test").build().encode(encoder,1).save();
        PasswordRecord.builder().type("login").name("腾讯邮箱").url("https://mail.qq.com/").username("test10").password("0901").note("test").build().encode(encoder,1).save();

        passwordFragment = new PasswordFragment();
        synchronousFragment = new SynchronousFragment();
        ectFragment = new EctFragment();
        fragments = new Fragment[]{passwordFragment,synchronousFragment,ectFragment};
        lastFragment = 0;
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment,passwordFragment).show(passwordFragment).commit();

        navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(menuItem -> {
            switch (menuItem.getItemId()){
                case R.id.navigation_pass:
                    if (lastFragment !=0){
                        title.setText("密码");
                        switchFragment(lastFragment,0);
                        lastFragment = 0;
                    }
                    return true;
                case R.id.navigation_synchronous:
                    if(lastFragment != 1){
                        title.setText("同步");
                        switchFragment(lastFragment,1);
                        lastFragment = 1;
                    }
                    return true;
                case R.id.navigation_etc:
                    if(lastFragment != 2){
                        title.setText("密码生成");
                        switchFragment(lastFragment,2);
                        lastFragment = 2;
                    }
                    return  true;
            }
            return false;
        });

        leftnavigation = findViewById(R.id.left_navigation);
        leftnavigation.setNavigationItemSelectedListener(menuItem -> {
            switch (menuItem.getItemId()) {
                case R.id.left_navigation_setting:
                    ImportAndExport importAndExport = new ImportAndExport();
                    importAndExport.Export("test",MainActivity.this);
                    Toast.makeText(MainActivity.this,"setting",Toast.LENGTH_SHORT).show();
                    break;
                case R.id.left_navigation_update:
                    ToastUtils.toast(getString(R.string.update_checking));
                    XUpdate.newBuild(MainActivity.this)
                            .updateUrl(getString(R.string.update_url))
                            .update();
                    break;
            }
            return false;
        });


//        DisplayMetrics metric = new DisplayMetrics();
//        getWindowManager().getDefaultDisplay().getMetrics(metric);
//        int windowsWight = metric.widthPixels;
//        int windowsHeight = metric.heightPixels;
//        View leftMenu = findViewById(R.id.left_layout);
//        ViewGroup.LayoutParams leftParams = leftMenu.getLayoutParams();
//        leftParams.height = windowsHeight;
//        leftParams.width = windowsWight;
//        leftMenu.setLayoutParams(leftParams);
    }

    private void switchFragment(int lastFragment,int index)
    {
        FragmentTransaction transaction =getSupportFragmentManager().beginTransaction();
        transaction.hide(fragments[lastFragment]);//隐藏上个Fragment
        if(!fragments[index].isAdded())
        {
            transaction.add(R.id.fragment,fragments[index]);
        }
        transaction.show(fragments[index]).commitAllowingStateLoss();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch(requestCode){
            case 3:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    Toast.makeText(this, "权限被拒绝了", Toast.LENGTH_LONG).show();
                }
                break;
            default:
                break;

        }
    }
}
