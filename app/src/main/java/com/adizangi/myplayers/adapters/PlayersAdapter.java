/*
   Adapter for the list of the user's selected players in the Players tab
 */

package com.adizangi.myplayers.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.adizangi.myplayers.R;

public class PlayersAdapter extends RecyclerView.Adapter
        <PlayersAdapter.ViewHolder> {

    private List<String> myPlayers;

    /*
       Holds a reference to the views of an item in the list
     */
    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView textView;
        Button button;

        ViewHolder(ConstraintLayout layout) {
            super(layout);
            textView = layout.findViewById(R.id.player_name);
            button = layout.findViewById(R.id.remove_button);
        }
    }

    /*
       Constructs a PlayersAdapter with the given list of players
     */
    public PlayersAdapter(List<String> myPlayers) {
        this.myPlayers = myPlayers;
    }

    @NonNull
    @Override
    /*
       Creates a new ViewHolder and returns it
     */
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                         int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ConstraintLayout layout = (ConstraintLayout)
                inflater.inflate(R.layout.players_list_item, parent, false);
        return new ViewHolder(layout);
    }

    @Override
    /*
       Sets the text of the given ViewHolder to the player's name that
       corresponds to the given position
     */
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String playerName = myPlayers.get(position);
        holder.textView.setText(playerName);
    }

    @Override
    /*
       Returns the number of items held by this adapter
     */
    public int getItemCount() {
        return myPlayers.size();
    }

}
