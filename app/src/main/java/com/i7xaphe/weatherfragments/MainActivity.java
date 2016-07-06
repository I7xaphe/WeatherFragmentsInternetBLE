package com.i7xaphe.weatherfragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.design.widget.Snackbar;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener, TabHost.OnTabChangeListener {
    ViewPager viewPager;
    TabHost tabHost;
    List<Fragment> listFragment;
    MyFragmentPagerAdapter myFragmentPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);

        listFragment = new ArrayList<Fragment>();
        viewPager = (ViewPager)findViewById(R.id.viewPager);
        myFragmentPagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager(),listFragment);

        initViewPager();
        initTabHost();
    }
    //========================================================================================
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_connect, menu);//podmiana xmp na jave
        return super.onCreateOptionsMenu(menu);
    }

    public void onPalaczenieClick(MenuItem item) {
        startService(new Intent(MainActivity.this,FloatingWindow.class));
       // onBackPressed();

    }

    public void onProgramieClick(MenuItem item) {
        Snackbar.make(this.findViewById(android.R.id.content).getRootView(), "Created by: Kamil Kolmus\nVersion: 1.6", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }
    public void onNavigationClick(MenuItem item) {

        startActivity(new Intent(getApplicationContext(), NavigationPopup.class));
//        FragmentManager fm = getSupportFragmentManager();
//        DialogFragment newFragment = new NavigationDialog();
//        newFragment.show(fm,"Mydialog");


    }
    public void onRunInBackgroundClick(MenuItem item) {
        Animation pulseHideLong, pulseHideDownLong;
        pulseHideLong = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.hide_pulse_after_some_sec);
        pulseHideDownLong = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.hide_down_pulse_after_some_sec);
        ImageView imageView = (ImageView) findViewById(R.id.pulse_image);
        imageView.startAnimation(pulseHideDownLong);
        TextView textView = (TextView) findViewById(R.id.pulse_text);
        textView.startAnimation(pulseHideLong);
    }

    //========================================================================================

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {


    }

    @Override
    public void onPageSelected(int position) {
        tabHost.setCurrentTab(position);

    }

    @Override
    public void onPageScrollStateChanged(int state) {
        MemoryFragment.refreshAdapter();

    }

    @Override
    public void onTabChanged(String tabId) {
        int selectedItem = tabHost.getCurrentTab();
        viewPager.setCurrentItem(selectedItem);


    }

    //========================================================================================
    @Override
    public void onBackPressed() {
        System.exit(0);
    }



    public class FakeContent implements TabHost.TabContentFactory{
        Context context;
        public FakeContent(Context acontext){
            context=acontext;
        }

        @Override
        public View createTabContent(String tag) {
            View fakeView= new View (context);
            fakeView.setMinimumHeight(0);
            fakeView.setMinimumWidth(0);
            return fakeView;
        }
    }
    //========================================================================================
    private void initViewPager() {

        listFragment.add(new SensorFragment());
        listFragment.add(new MemoryFragment());
        viewPager.setAdapter(myFragmentPagerAdapter);
        viewPager.setOnPageChangeListener(this);

    }
    private void initTabHost() {
        tabHost = (TabHost) findViewById(R.id.tabHost);
        tabHost.setup();

        String[] tabNames ={"Sensors","Memory"};

        for(int i=0; i<tabNames.length;i++){
            TabHost.TabSpec  tabSpec;
            tabSpec= tabHost.newTabSpec(tabNames[i]);
            tabSpec.setIndicator(tabNames[i]);
            tabSpec.setContent(new FakeContent(getApplicationContext()));

            tabHost.addTab(tabSpec);
        }
        for(int i=0;i<tabHost.getTabWidget().getChildCount();i++)
        {
            TextView tv = (TextView) tabHost.getTabWidget().getChildAt(i).findViewById(android.R.id.title);
            tv.setTextColor(Color.parseColor("#FFFFFF"));
        }
        tabHost.setOnTabChangedListener(this);
    }


}
