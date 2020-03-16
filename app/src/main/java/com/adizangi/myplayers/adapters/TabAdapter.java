/*
   Allows switching between fragments
   Used to create tabs in the MainActivity window, that can be switched between
   by swiping
 */

package com.adizangi.myplayers.adapters;

import com.adizangi.myplayers.fragments.PlayersTabFragment;
import com.adizangi.myplayers.fragments.StatsTabFragment;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class TabAdapter extends FragmentStateAdapter {

    public static final String[] TAB_TITLES = {"Stats", "Players"};

    /*
       Constructs a TabAdapter with the given FragmentActivity
       This TabAdapter will allow switching between fragments in the
       given FragmentActivity
       This TabAdapter should be set as the adapter for a ViewPager2 that
       is included in the layout of the given FragmentActivity
     */
    public TabAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    /*
       Creates a Fragment for the given position and returns it
       The StatsTabFragment will be in position 0 and the PlayersTabFragment
       will be in position 1
     */
    public Fragment createFragment(int position) {
        if (position == 0) {
            return new StatsTabFragment();
        } else {
            return new PlayersTabFragment();
        }
    }

    @Override
    /*
       Returns the total number of fragments held by this adapter, which in
       this case is the length of the tab titles list
     */
    public int getItemCount() {
        return TAB_TITLES.length;
    }
}
