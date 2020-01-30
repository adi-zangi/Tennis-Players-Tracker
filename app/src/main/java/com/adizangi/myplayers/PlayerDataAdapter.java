/*
   Manages the views of the Player Data Recycler View, which displays
   information about each selected player
 */

package com.adizangi.myplayers;

import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PlayerDataAdapter extends RecyclerView.Adapter<PlayerDataAdapter.PlayerDataHolder> {

    /*
       Represents a view in the recycler view, and provides access to its
       child views
     */
    public static class PlayerDataHolder extends RecyclerView.ViewHolder {

        TextView tv;

        public PlayerDataHolder(TextView textView) {
            super(textView);
        }

    }

    public PlayerDataAdapter(List<PlayerData> playersData) {

    }

    @NonNull
    @Override
    public PlayerDataHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull PlayerDataHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

}
