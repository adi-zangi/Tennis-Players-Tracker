/*
   Adapter for the list in the Stats tab
 */

package com.adizangi.myplayers.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.adizangi.myplayers.R;
import com.adizangi.myplayers.objects.PlayerStats;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

public class StatsAdapter extends RecyclerView.Adapter
        <StatsAdapter.ViewHolder> {

    private List<PlayerStats> stats;

    /*
       Holds a reference to the views of an item in the list
     */
    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView tournamentStanding;
        TextView ranking;
        TextView titles;
        TextView currentTournament;
        TextView latestMatchResult;
        Button seeResultsButton;

        /*
           Constructs a ViewHolder with the given layout of an item in the list
         */
        ViewHolder(ConstraintLayout layout) {
            super(layout);
            name = layout.findViewById(R.id.name);
            tournamentStanding = layout.findViewById(R.id.tournament_standing);
            ranking = layout.findViewById(R.id.ranking);
            titles = layout.findViewById(R.id.titles);
            currentTournament = layout.findViewById(R.id.current_tournament);
            latestMatchResult = layout.findViewById(R.id.latest_match_result);
            seeResultsButton = layout.findViewById(R.id.see_results_button);
        }
    }

    /*
       Constructs a StatsAdapter with the given list of PlayerStats objects
     */
    public StatsAdapter(List<PlayerStats> stats) {
        this.stats = stats;
    }

    /*
       Creates a new ViewHolder and returns it
       Adds a listener to the See More Results button in the ViewHolder,
       that opens an ESPN web page that has all the match results of the player
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        ConstraintLayout layout = (ConstraintLayout)
                inflater.inflate(R.layout.stats_list_item, parent, false);
        final ViewHolder viewHolder = new ViewHolder(layout);
        viewHolder.seeResultsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View seeResultsButton) {
                int position = viewHolder.getAdapterPosition();
                PlayerStats playerStats = stats.get(position);
                Uri webpage = Uri.parse(playerStats.getResultsURL());
                Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
                if (intent.resolveActivity(context.getPackageManager()) != null) {
                    context.startActivity(intent);
                }
            }
        });
        return viewHolder;
    }

    /*
       Fills the views in the given ViewHolder with information from the
       PlayerStats object that corresponds to the given position
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PlayerStats playerStats = stats.get(position);
        holder.name.setText(playerStats.getName());
        holder.ranking.setText(playerStats.getRanking());
        holder.titles.setText(playerStats.getTitles());
        holder.currentTournament.setText(playerStats.getCurrentTournament());
        holder.latestMatchResult.setText(playerStats.getLatestMatchResult());
        String tournamentStanding = playerStats.getTournamentStanding();
        holder.tournamentStanding.setText(tournamentStanding);
        if (tournamentStanding.contains("advanced")) {
            holder.tournamentStanding.setTextColor(Color.GREEN);
        } else if (tournamentStanding.equals("out")) {
            holder.tournamentStanding.setTextColor(Color.RED);
        } else {
            holder.tournamentStanding.setTextColor(Color.GRAY);
        }
    }

    /*
       Returns the number of items held by this adapter
     */
    @Override
    public int getItemCount() {
        return stats.size();
    }
}
