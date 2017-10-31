package com.almortah.almortah;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by ALMAHRI on 11/1/17.
 */

public class MyViewPagerAdapter extends FragmentPagerAdapter {

    public MyViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new MyChaletsListFragment();
        } else
            return new MyChaletcMapFragment();
    }

    @Override
    public int getCount() {
        return 2;
    }
}
