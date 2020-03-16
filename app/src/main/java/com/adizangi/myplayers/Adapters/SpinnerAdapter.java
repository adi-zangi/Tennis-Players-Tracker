package com.adizangi.myplayers.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

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
            //view = inflater.inflate(R.layout.resource, parent, false);
        } else if (position == 1) {

        } else {

        }
    }
}
