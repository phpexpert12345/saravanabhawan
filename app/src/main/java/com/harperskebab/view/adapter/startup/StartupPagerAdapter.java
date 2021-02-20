package com.harperskebab.view.adapter.startup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.harperskebab.view.ui.fragments.startup.StartupFirstFragment;
import com.harperskebab.view.ui.fragments.startup.StartupSecondFragment;
import com.harperskebab.view.ui.fragments.startup.StartupThirdFragment;

public class StartupPagerAdapter extends FragmentPagerAdapter {

    private FragmentManager fragmentManager;

    private StartupFirstFragment startupFirstFragment;
    private StartupSecondFragment startupSecondFragment;
    private StartupThirdFragment startupThirdFragment;

    public StartupPagerAdapter(@NonNull FragmentManager fragmentManager) {
        super(fragmentManager);

        this.fragmentManager = fragmentManager;

        startupFirstFragment = new StartupFirstFragment();
        startupSecondFragment = new StartupSecondFragment();
        startupThirdFragment = new StartupThirdFragment();
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                return startupFirstFragment;
            case 1:
                return startupSecondFragment;
            case 2:
                return startupThirdFragment;
        }
        return new StartupFirstFragment();
    }

    @Override
    public int getCount() {
        return 3;
    }
}
