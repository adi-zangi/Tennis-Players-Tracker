/*
   The Players tab, which displays the user's selected players and lets the
   user modify them
 */

package com.adizangi.myplayers.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.adizangi.myplayers.R;
import com.adizangi.myplayers.adapters.PlayersAdapter;
import com.adizangi.myplayers.models.TabsViewModel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class PlayersTabFragment extends Fragment
        implements PlayersAdapter.OnRemovePlayerListener {

    private TabsViewModel tabsViewModel;
    private AutoCompleteTextView autoCompleteSearch;
    private PlayersAdapter playersAdapter;

    @Nullable
    @Override
    /*
       Called when the Fragment should instantiate its UI
       Inflates the fragment's layout by using the given LayoutInflater, and
       returns the root View of the fragment's layout
     */
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_players_tab, container, false);
    }

    @Override
    /*
       Initializes the sub-views of the view returned by onCreateView()
       Fills the AutoCompleteTextView with a list of the total players the
       user can select
       Fills the RecyclerView with the saved list of the user's selected
       players
     */
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        tabsViewModel = new ViewModelProvider(requireActivity()).get(TabsViewModel.class);
        autoCompleteSearch =
                view.findViewById(R.id.search_player_auto_complete);
        ArrayAdapter<String> autoCompleteAdapter = new ArrayAdapter<>
                (requireContext(), android.R.layout.simple_list_item_1,
                        tabsViewModel.getTotalPlayers());
        autoCompleteSearch.setAdapter(autoCompleteAdapter);
        RecyclerView myPlayersList = view.findViewById(R.id.my_players_list);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(getContext());
        myPlayersList.setLayoutManager(manager);
        playersAdapter = new PlayersAdapter(tabsViewModel.getMyPlayers());
        myPlayersList.setAdapter(playersAdapter);
        playersAdapter.setOnRemoveClickListener(this);
        Button addButton = view.findViewById(R.id.add_button);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPlayer();
            }
        });
    }

    /*
       Adds the player whose name is in the AutoCompleteTextView, if the player
       is valid
       Saves the list of players
     */
    private void addPlayer() {
        String playerName = autoCompleteSearch.getText().toString();
        if (!tabsViewModel.getTotalPlayers().contains(playerName)) {
            Toast.makeText(getContext(), "Invalid player",
                    Toast.LENGTH_LONG).show();
        } else if (tabsViewModel.getMyPlayers().contains(playerName)) {
            Toast.makeText(getContext(), "Player already in list",
                    Toast.LENGTH_LONG).show();
        } else {
            tabsViewModel.addPlayer(playerName);
            playersAdapter.notifyDataSetChanged();
        }
        autoCompleteSearch.getText().clear();
    }

    public void onRemovePlayer(int position) {
        tabsViewModel.removePlayerInPosition(position);
        playersAdapter.notifyDataSetChanged();
    }

}
