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

    /*
       Holds a reference to the views of an item in the list
     */
    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView playerName;
        Button removeButton;

        /*
           Constructs a ViewHolder with the given layout of an item in the list
         */
        ViewHolder(ConstraintLayout layout) {
            super(layout);
            playerName = layout.findViewById(R.id.player_name);
            removeButton = layout.findViewById(R.id.remove_button);
        }
    }

    /*
       Interface used to communicate remove button clicks to PlayersTabFragment
     */
    public interface OnRemovePlayerListener {
        void onRemovePlayer(int position);
    }

    private List<String> myPlayers;
    private OnRemovePlayerListener callback;

    /*
       Constructs a PlayersAdapter with the given list of players
     */
    public PlayersAdapter(List<String> myPlayers) {
        this.myPlayers = myPlayers;
    }

    /*
       Sets the OnRemovePlayerListener callback to the given callback
     */
    public void setOnRemoveClickListener(OnRemovePlayerListener callback) {
        this.callback = callback;
    }

    @NonNull
    @Override
    /*
       Creates a new ViewHolder and returns it
       Adds a listener to remove button clicks in the ViewHolder, that
       communicates the click to PlayersTabFragment
     */
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                         int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ConstraintLayout layout = (ConstraintLayout)
                inflater.inflate(R.layout.players_list_item, parent, false);
        final ViewHolder viewHolder = new ViewHolder(layout);
        viewHolder.removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View removeButton) {
                callback.onRemovePlayer(viewHolder.getAdapterPosition());
            }
        });
        return viewHolder;
    }

    @Override
    /*
       Sets the text of the given ViewHolder to the player's name that
       corresponds to the given position
     */
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String playerName = myPlayers.get(position);
        holder.playerName.setText(playerName);
    }

    @Override
    /*
       Returns the number of items held by this adapter
     */
    public int getItemCount() {
        return myPlayers.size();
    }

}
