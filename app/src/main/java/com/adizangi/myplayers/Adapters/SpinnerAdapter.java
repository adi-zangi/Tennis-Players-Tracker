package com.adizangi.myplayers.Adapters;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.List;

import androidx.annotation.NonNull;

public class SpinnerAdapter extends ArrayAdapter<String> {

    public SpinnerAdapter(@NonNull Context context,
                          @NonNull List<String> myPlayers,
                          @NonNull List<String> totalPlayers) {
        myPlayers.add(0, "");
        myPlayers.add(0, "");
        super(context, 0, myPlayers);
    }
}
