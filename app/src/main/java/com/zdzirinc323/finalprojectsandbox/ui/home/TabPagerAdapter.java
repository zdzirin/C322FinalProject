package com.zdzirinc323.finalprojectsandbox.ui.home;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.zdzirinc323.finalprojectsandbox.ui.completed.CompletedFragment;
import com.zdzirinc323.finalprojectsandbox.ui.pending.PendingFragment;

public class TabPagerAdapter extends FragmentStatePagerAdapter{

    private static final String[] TAB_TITLES = new String[]{"Pending", "Completed"};
    private final Context context;

    public TabPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        this.context = context;
    }


    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new PendingFragment();

            case 1:
                return new CompletedFragment();
        }
        return null;
    }


    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return TAB_TITLES[position];
    }

    @Override
    public int getCount() {
        //Shows 2 total pages
        return 2;
    }
}
