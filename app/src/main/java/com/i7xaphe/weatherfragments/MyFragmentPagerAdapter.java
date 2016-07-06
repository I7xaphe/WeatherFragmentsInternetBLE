package com.i7xaphe.weatherfragments;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by Kamil on 2016-07-01.
 */
public class MyFragmentPagerAdapter extends FragmentPagerAdapter {
    List<Fragment> listFragment;

    public MyFragmentPagerAdapter(FragmentManager fm, List<Fragment> listFragment) {
        super(fm);
        this.listFragment=listFragment;
    }

    @Override
    public Fragment getItem(int position) {
        return listFragment.get(position);
    }

    @Override
    public int getCount() {
        return listFragment.size();
    }
}
