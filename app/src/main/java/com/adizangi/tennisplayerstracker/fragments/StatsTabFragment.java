/*
   The Stats tab, which displays statistics and tournament results for each of
   the user's selected players

   The screen has a list and each section in the list shows the information
   about one of the players
   The top of each section either has green text that says the player advanced
   to the next round, red text that says the player is out of the tournament,
   or gray text that says the player is not playing
   The sections are sorted so green sections are first and gray sections are last
   Each section has a 'See More Match Results' button that opens an ESPN web
   page that has all the match results of the player
 */

package com.adizangi.tennisplayerstracker.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.adizangi.tennisplayerstracker.R;
import com.adizangi.tennisplayerstracker.adapters.StatsAdapter;
import com.adizangi.tennisplayerstracker.models.TabsViewModel;

public class StatsTabFragment extends Fragment {

    private TabsViewModel tabsViewModel;
    private StatsAdapter statsAdapter;

    private Observer<String> addedPlayerObserver = new Observer<String>() {
        @Override
        /*
           Adds the given player's statistics into the list
         */
        public void onChanged(String player) {
            tabsViewModel.addPlayerStats(player);
            statsAdapter.notifyDataSetChanged();
        }
    };

    private Observer<String> removedPlayerObserver = new Observer<String>() {
        @Override
        /*
           Removes the given player's statistics from the list
         */
        public void onChanged(String player) {
            tabsViewModel.removePlayerStats(player);
            statsAdapter.notifyDataSetChanged();
        }
    };

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
       Sets responses to change in the user's selected players
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
        RecyclerView.LayoutManager manager = new LinearLayoutManager(requireContext());
        statsList.setLayoutManager(manager);
        statsAdapter = new StatsAdapter(tabsViewModel.getMyPlayersStats());
        statsList.setAdapter(statsAdapter);
        MutableLiveData<String> addedPlayer = tabsViewModel.getAddedPlayer();
        addedPlayer.observe(getViewLifecycleOwner(), addedPlayerObserver);
        MutableLiveData<String> removedPlayer = tabsViewModel.getRemovedPlayer();
        removedPlayer.observe(getViewLifecycleOwner(), removedPlayerObserver);
    }

}
