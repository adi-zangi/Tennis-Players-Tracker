package com.adizangi.myplayers.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.adizangi.myplayers.R;

public class PlayersAdapter extends RecyclerView.Adapter
        <PlayersAdapter.ViewHolder> {

    private List<String> myPlayers;

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView textView;

        public ViewHolder(ConstraintLayout layout) {
            super(layout);
            this.textView = layout.findViewById(R.id.player_name);
        }
    }

    public PlayersAdapter(List<String> myPlayers) {
        this.myPlayers = myPlayers;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                         int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ConstraintLayout layout = (ConstraintLayout)
                inflater.inflate(R.layout.my_players_list_item, parent, false);
        return new ViewHolder(layout);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String playerName = myPlayers.get(position);
        holder.textView.setText(playerName);
    }

    @Override
    public int getItemCount() {
        return myPlayers.size();
    }

}
