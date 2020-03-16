package com.adizangi.myplayers.adapters;

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

    @Override
    public int getViewTypeCount() {
        return 3;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return 0;
        } else if (position == 1) {
            return 1;
        } else {
            return 2;
        }
    }

    @NonNull
    @Override
    public View getView(int position,
                        @Nullable View convertView,
                        @NonNull ViewGroup parent) {
        int type = getItemViewType(position);
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            if (type == 0) {
                convertView = inflater.inflate(R.layout.spinner_hint, parent, false);
            } else if (type == 1) {
                convertView = inflater.inflate(R.layout.spinner_search_player_item,
                        parent, false);
                AutoCompleteTextView autoComplete =
                        convertView.findViewById(R.id.searchPlayerAutoComplete);
                ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                        android.R.layout.simple_dropdown_item_1line, totalPlayers);
                autoComplete.setAdapter(adapter);
            } else {
                convertView = inflater.inflate(R.layout.spinner_player_item,
                        parent, false);
                TextView playerName = convertView.findViewById(R.id.playerName);
                String currentItem = getItem(position);
                playerName.setText(currentItem);
            }
        } else {
            if (type == 2) {
                TextView playerName = convertView.findViewById(R.id.playerName);
                String currentItem = getItem(position);
                playerName.setText(currentItem);
            }
        }
        return convertView;
    }
}
