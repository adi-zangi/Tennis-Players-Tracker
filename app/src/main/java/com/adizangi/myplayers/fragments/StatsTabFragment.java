/*
   The Stats tab, which displays statistics and tournament results for each of
   the user's players
 */

package com.adizangi.myplayers.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.adizangi.myplayers.R;

public class StatsTabFragment extends Fragment {

    /*
       Called when the Fragment should instantiate its UI
       Inflates the fragment's layout by using the given LayoutInflater, and
       returns the root View of the fragment's layout
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_stats_tab, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        // Implement when add sub-views into fragment's layout
        super.onViewCreated(view, savedInstanceState);
    }

}
