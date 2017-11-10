package com.almortah.almortah;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by ALMAHRI on 11/10/17.
 */

public class AdminPager extends FragmentPagerAdapter {

    public AdminPager(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new OwnersFragment();
        } else
            return new CustomersFragments();
    }

    @Override
    public int getCount() {
        return 2;
    }
}

