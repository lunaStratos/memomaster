package memomaster.lunastratos.com.memomaster;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;
import android.widget.Toolbar;

import memomaster.lunastratos.com.memomaster.Adapter.ContentFregmentAdapter;
import memomaster.lunastratos.com.memomaster.view.SettingView;

public class MainActivity extends AppCompatActivity {

    private ContentFregmentAdapter contentFregmentAdapter;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private android.support.v7.widget.Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();

        toolbar = findViewById(R.id.my_toolbar);
        viewPager = findViewById(R.id.view_pager);
        tabLayout = findViewById(R.id.tabLayout);

        //탭 아이콘/텍스트 등록
        setTabIcon();

        contentFregmentAdapter = new ContentFregmentAdapter(getSupportFragmentManager(), tabLayout.getTabCount());

        viewPager.setAdapter(contentFregmentAdapter);
        viewPager.addOnPageChangeListener(
                new TabLayout.TabLayoutOnPageChangeListener(tabLayout));


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        //타이틀에 툴바 + 텍스트 제거
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent intent = new Intent(getApplicationContext(), SettingView.class);
                intent.putExtra("seeting", "setting");
                startActivity(intent);
                break;
//
//            case R.id.action_settings2:
//                break;
//
//            case R.id.action_settings3:
//                break;

        }

        return super.onOptionsItemSelected(item);
    }

    public void setTabIcon() {
        tabLayout.addTab(tabLayout.newTab().setText("메모"));
        tabLayout.addTab(tabLayout.newTab().setText("음성메모"));
    }

}
