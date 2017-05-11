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
import com.example.tzadmin.tzsk_windows.Location.MyLocation;
import com.example.tzadmin.tzsk_windows.SendDataModule.SendChangedData;
import com.example.tzadmin.tzsk_windows.SendDataModule.SendPhoto;
import com.example.tzadmin.tzsk_windows.TabFragments.Tab1Deliveries;
import com.example.tzadmin.tzsk_windows.TabFragments.Tab2Statuses;
import java.util.Calendar;
import java.util.Date;
import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.HorizontalCalendarListener;

public class MainActivity extends AppCompatActivity  {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private Tab1Deliveries tabDeliveries;
    private Tab2Statuses tabStatuses;
    HorizontalCalendar horizontalCalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MyLocation.SetListener(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        tabDeliveries = new Tab1Deliveries();
        tabStatuses = new Tab2Statuses();

        initializeCalendar();
    }

    @Override
    public void onStart () {
        super.onStart();
        new SendPhoto(this);
        new SendChangedData(this);
        horizontalCalendar.selectDate(new Date(), false);
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
                tabDeliveries.reloadDeliveries(
                        helper.Date(horizontalCalendar.getSelectedDate()));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initializeCalendar() {
        Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.WEEK_OF_MONTH, 2);

        Calendar startDate = Calendar.getInstance();
        startDate.add(Calendar.WEEK_OF_MONTH, -1);

        horizontalCalendar = new HorizontalCalendar.Builder(this, R.id.calendarView)
                .startDate(startDate.getTime())
                .endDate(endDate.getTime())
                .build();

        horizontalCalendar.setCalendarListener(new HorizontalCalendarListener() {
            @Override
            public void onDateSelected(Date date, int position) {
                tabDeliveries.reloadDeliveries(helper.Date(date));
            }
        });
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
                    return tabStatuses;
                case 2:
                    return null;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 2;
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
