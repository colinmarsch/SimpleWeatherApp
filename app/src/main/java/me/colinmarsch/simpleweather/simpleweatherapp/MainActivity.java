package me.colinmarsch.simpleweather.simpleweatherapp;


import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;

public class MainActivity extends FragmentActivity {

    private MyAdapter mAdapter;
    private ViewPager mPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAdapter = new MyAdapter(getSupportFragmentManager());

        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(mAdapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        mPager.setCurrentItem(1);
        tabLayout.setupWithViewPager(mPager);
    }


    public class MyAdapter extends FragmentPagerAdapter {

        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public Fragment getItem(int position) {

            switch(position) {
                case 0: return new SavedCities();
                case 1: return new MainWeather();
                case 2: return new FutureWeather();
                default: return null;
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch(position) {
                case 0: return getString(R.string.saved_cities);
                case 1: return getString(R.string.current_weather);
                case 2: return getString(R.string.future_forecast);
                default: return null;
            }
        }
    }
}
