package com.example.chhotawhatsapp.UserActivity;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

class SectionPagerAdapter extends FragmentPagerAdapter {
    private static final String TAG = "PagerAdapter";

    public ChatFragment chatFragment;

    public SectionPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        switch (position){
            case 0:
                chatFragment = new ChatFragment();
                return chatFragment;

            case 1:
                return new StatusFragment();

            case 2:
                return new UsersFragment();

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    public CharSequence getPageTitle(int position){
        switch (position){
            case 0:
                return "Chats";

            case 1:
                return "Status";

            case 2:
                return "Users";

            default:
                return null;
        }
    }
}
