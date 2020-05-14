/*
   Adapter for the list of the user's selected players in PlayersTabFragment
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
    public interface OnRemoveClickListener {
        void onRemoveClick(String player);
    }

    private List<String> myPlayers;
    private OnRemoveClickListener callback;

    /*
       Constructs a PlayersAdapter with the given list of players
     */
    public PlayersAdapter(List<String> myPlayers) {
        this.myPlayers = myPlayers;
    }

    /*
       Sets the OnRemoveClickListener callback to the given callback
     */
    public void setOnRemoveClickListener(OnRemoveClickListener callback) {
        this.callback = callback;
    }

    /*
       Creates a new ViewHolder and returns it
       Sets response to remove button clicks in the ViewHolder
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                         int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ConstraintLayout layout = (ConstraintLayout)
                inflater.inflate(R.layout.players_list_item, parent, false);
        final ViewHolder viewHolder = new ViewHolder(layout);
        viewHolder.removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View removeButton) {
                String removedPlayer = viewHolder.playerName.getText().toString();
                callback.onRemoveClick(removedPlayer);
            }
        });
        return viewHolder;
    }

    /*
       Sets the text of the given ViewHolder to the player's name that
       corresponds to the given position
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String playerName = myPlayers.get(position);
        holder.playerName.setText(playerName);
    }

    /*
       Returns the number of items held by this adapter
     */
    @Override
    public int getItemCount() {
        return myPlayers.size();
    }

}
