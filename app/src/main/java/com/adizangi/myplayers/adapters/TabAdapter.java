package com.adizangi.myplayers.adapters;

import com.adizangi.myplayers.fragments.PlayersTabFragment;
import com.adizangi.myplayers.fragments.StatsTabFragment;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class TabAdapter extends FragmentStateAdapter {

    private static final String[] TAB_TITLES = {"Stats", "Players"};

    public TabAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 0) {
            return new StatsTabFragment();
        } else {
            return new PlayersTabFragment();
        }
    }

    @Override
    public int getItemCount() {
        return TAB_TITLES.length;
    }
}
