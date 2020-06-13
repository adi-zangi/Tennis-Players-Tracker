/*
   The Players tab, which displays the user's selected players and lets the
   user modify them

   The screen has a search box (AutoCompleteTextView) that lets the user search
   for a player and filters from the list of all players as the user types
   The user can add a player by selecting one of the suggestions from the
   AutoCompleteTextView and clicking the 'Add' button
   A list of the selected players is displayed below the search box, and there
   is a 'Remove' button next to each player
 */

package com.adizangi.tennisplayerstracker.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.adizangi.tennisplayerstracker.R;
import com.adizangi.tennisplayerstracker.adapters.PlayersAdapter;
import com.adizangi.tennisplayerstracker.models.TabsViewModel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class PlayersTabFragment extends Fragment {

    private TabsViewModel tabsViewModel;
    private AutoCompleteTextView playerSearchBar;
    private PlayersAdapter playersAdapter;

    private View.OnClickListener addButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View addButton) {
            addPlayer();
        }
    };

    private PlayersAdapter.OnRemoveClickListener removeButtonListener =
            new PlayersAdapter.OnRemoveClickListener() {
        @Override
        /*
           Removes the given player from the list
         */
        public void onRemoveClick(String player) {
            tabsViewModel.removePlayer(player);
            playersAdapter.notifyDataSetChanged();
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
        return inflater.inflate(R.layout.fragment_players_tab, container, false);
    }

    /*
       Initializes the sub-views of the view returned by onCreateView()
       Fills the AutoCompleteTextView with a list of the total players the
       user can select
       Fills the RecyclerView with the saved list of the user's selected
       players
       Sets responses to button clicks
     */
    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        ViewModelProvider.AndroidViewModelFactory factory =
                ViewModelProvider.AndroidViewModelFactory
                        .getInstance(requireActivity().getApplication());
        tabsViewModel = new ViewModelProvider(requireActivity(), factory)
                .get(TabsViewModel.class);
        playerSearchBar = view.findViewById(R.id.player_search_bar);
        ArrayAdapter<String> searchBarAdapter = new ArrayAdapter<>
                (requireContext(), android.R.layout.simple_list_item_1,
                        tabsViewModel.getTotalPlayers());
        playerSearchBar.setAdapter(searchBarAdapter);
        RecyclerView playersRecyclerView = view.findViewById(R.id.players_recycler_view);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(requireContext());
        playersRecyclerView.setLayoutManager(manager);
        playersAdapter = new PlayersAdapter(tabsViewModel.getSelectedPlayers());
        playersRecyclerView.setAdapter(playersAdapter);
        playersAdapter.setOnRemoveClickListener(removeButtonListener);
        Button addButton = view.findViewById(R.id.add_button);
        addButton.setOnClickListener(addButtonListener);
    }

    /*
       If the player whose name is in the search bar is in the total players
       list and isn't already in the user's list, adds the player to the user's
       list
     */
    private void addPlayer() {
        String playerName = playerSearchBar.getText().toString();
        if (!tabsViewModel.getTotalPlayers().contains(playerName)) {
            Toast.makeText(getContext(), "Invalid player", Toast.LENGTH_LONG).show();
        } else if (tabsViewModel.getSelectedPlayers().contains(playerName)) {
            Toast.makeText(getContext(), "Player already in list", Toast.LENGTH_LONG).show();
        } else {
            tabsViewModel.addPlayer(playerName);
            playersAdapter.notifyDataSetChanged();
        }
        playerSearchBar.getText().clear();
    }

}
