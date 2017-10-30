package com.almortah.almortah;

/**
 * Created by ALMAHRI on 10/30/17.
 */

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new ChaletsListFragment();
        } else
            return new ChaletcMapFragment();
    }

    @Override
    public int getCount() {
        return 2;
    }
}
