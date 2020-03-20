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
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.adizangi.myplayers.R;
import com.adizangi.myplayers.adapters.StatsAdapter;
import com.adizangi.myplayers.models.TabsViewModel;

public class StatsTabFragment extends Fragment {

    private TabsViewModel tabsViewModel;
    private StatsAdapter statsAdapter;

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

    /*
       Initializes the sub-views of the view returned by onCreateView()
       Fills the RecyclerView such that each entry contains statistics and
       tournament results of one of the user's selected players
     */
    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        ViewModelProvider.AndroidViewModelFactory factory =
                ViewModelProvider.AndroidViewModelFactory
                        .getInstance(requireActivity().getApplication());
        tabsViewModel = new ViewModelProvider(requireActivity(), factory)
                .get(TabsViewModel.class);
        RecyclerView statsList = view.findViewById(R.id.stats_list);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(getContext());
        statsList.setLayoutManager(manager);
        statsAdapter = new StatsAdapter(tabsViewModel.getMyPlayersStats());
        statsList.setAdapter(statsAdapter);
    }

}
