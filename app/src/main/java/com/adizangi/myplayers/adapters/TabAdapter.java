/*
   Adapter for the tabs in MainActivity
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
       Constructs a TabAdapter for the given FragmentActivity
     */
    public TabAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    /*
       Creates a Fragment for the given position and returns it
       The StatsTabFragment is in position 0 and the PlayersTabFragment
       is in position 1
     */
    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 0) {
            return new StatsTabFragment();
        } else {
            return new PlayersTabFragment();
        }
    }

    /*
       Returns the number of tabs held by this adapter
     */
    @Override
    public int getItemCount() {
        return TAB_TITLES.length;
    }
}
