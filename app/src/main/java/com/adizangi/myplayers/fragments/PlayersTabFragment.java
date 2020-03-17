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

import com.adizangi.myplayers.R;
import com.adizangi.myplayers.adapters.PlayersAdapter;
import com.adizangi.myplayers.objects.FileManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class PlayersTabFragment extends Fragment {

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
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        AutoCompleteTextView autoComplete =
                view.findViewById(R.id.search_player_auto_complete);
        RecyclerView myPlayersList = view.findViewById(R.id.my_players_list);
        FileManager fileManager = new FileManager(getContext());
        List<String> totalPlayers = fileManager.readTotalPlayers();
        ArrayAdapter<String> autoCompleteAdapter = new ArrayAdapter<>
                (requireContext(), android.R.layout.simple_list_item_1, totalPlayers);
        autoComplete.setAdapter(autoCompleteAdapter);
        List<String> myPlayers = new ArrayList<>(Arrays.asList
                ("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"));
        RecyclerView.LayoutManager manager =
                new LinearLayoutManager(getContext());
        myPlayersList.setLayoutManager(manager);
        PlayersAdapter playersAdapter = new PlayersAdapter(myPlayers);
        myPlayersList.setAdapter(playersAdapter);
    }

}
