package com.example.tzadmin.tzsk_windows;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import com.example.tzadmin.tzsk_windows.AuthModule.Auth;
import com.example.tzadmin.tzsk_windows.DatabaseModule.Database;
import com.example.tzadmin.tzsk_windows.DatabaseModule.DatabaseModels.Delivery;
import com.example.tzadmin.tzsk_windows.HttpModule.Http;
import com.example.tzadmin.tzsk_windows.HttpModule.HttpResp;
import com.example.tzadmin.tzsk_windows.JsonModule.JSON;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity  {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private Tab1Deliveries tabDeliveries;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        tabDeliveries = new Tab1Deliveries();

        //Http http = new Http();
        //HttpResp resp = http.GET(helper.HTTP_QUERY_GETORDERS, Auth.login, Auth.passwd);
        //ArrayList<Delivery> Shipments = JSON.parse(resp.body);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.btn_exit:
                Database.updateUser(Auth.id, 0);
                starActivity(LoginActivity.class);
                break;
            case R.id.btn_refresh:
                tabDeliveries.reloadDeliveries();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void starActivity (Class _class) {
        startActivity(new Intent(this, _class));
        finish();
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return tabDeliveries;
                case 1:
                    return null;
                case 2:
                    return null;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 1;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Доставки";
                case 1:
                    return "Статус";
                case 2:
                    return "Настройки";
            }
            return null;
        }
    }
}
