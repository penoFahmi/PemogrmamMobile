package com.peno.learnnavigation;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class PhoneFragmentAdapter extends FragmentPagerAdapter {

    private static final int numberOfFragments = 2;

    public PhoneFragmentAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0: return MessageFragment.newInstance();
            case 1: return CallFragment.newInstance();
        }
        return null;
    }

    @Override
    public int getCount() {
        return numberOfFragments;

    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0 : return "Message";
            case 1 : return "Call History";
            default: return super.getPageTitle(position);
        }
    }
}