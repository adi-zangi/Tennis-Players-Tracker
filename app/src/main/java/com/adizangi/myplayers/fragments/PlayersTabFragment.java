/*
   The Players tab, which displays the user's selected players and lets the
   user modify them
 */

package com.adizangi.myplayers.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.adizangi.myplayers.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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
        // Implement when add sub-views into fragment's layout
        super.onViewCreated(view, savedInstanceState);
    }

}
