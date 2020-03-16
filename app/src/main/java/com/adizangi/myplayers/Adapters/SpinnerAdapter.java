package com.adizangi.myplayers.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import com.adizangi.myplayers.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class SpinnerAdapter extends ArrayAdapter<String> {

    private List<String> totalPlayers;

    public SpinnerAdapter(@NonNull Context context,
                          @NonNull List<String> myPlayers,
                          @NonNull List<String> totalPlayers) {
        super(context, 0, myPlayers);
        this.totalPlayers = totalPlayers;
        insert("", 0);
        insert("", 0);
    }

    @NonNull
    @Override
    public View getView(int position,
                        @Nullable View convertView,
                        @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view;
        if (position == 0) {
            view = inflater.inflate(R.layout.spinner_hint, parent, false);
        } else if (position == 1) {
            view = inflater.inflate(R.layout.spinner_search_player_item,
                    parent, false);
            AutoCompleteTextView autoComplete =
                    view.findViewById(R.id.addPlayerAutoComplete);
            ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                    android.R.layout.simple_dropdown_item_1line, totalPlayers);
            autoComplete.setAdapter(adapter);
        } else {
            view = inflater.inflate(R.layout.spinner_player_item,
                    parent, false);
            TextView playerName = view.findViewById(R.id.playerName);
            String currentItem = getItem(position);
            playerName.setText(currentItem);
        }
        return view;
    }
}
