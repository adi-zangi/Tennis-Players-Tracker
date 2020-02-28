/*
   Contains details about a tennis player, including statistics and tournament
   results in singles tennis
 */

package com.adizangi.myplayers;

import androidx.annotation.NonNull;

public class PlayerDetails {

    private String name;
    private String ranking;
    private String titles;
    private String tournamentStanding;
    private String currentTournament;
    private String latestMatchResult;
    private String upcomingMatch;
    private String resultsURL;

    /*
       Constructs a PlayerDetails object
     */
    public PlayerDetails(String name,
                         String ranking,
                         String titles,
                         String tournamentStanding,
                         String currentTournament,
                         String latestMatchResult,
                         String upcomingMatch,
                         String resultsURL) {
        this.name = name;
        this.ranking = ranking;
        this.titles = titles;
        this.tournamentStanding = tournamentStanding;
        this.currentTournament = currentTournament;
        this.latestMatchResult = latestMatchResult;
        this.upcomingMatch = upcomingMatch;
        this.resultsURL = resultsURL;
    }

    /*
       Returns the player's name
     */
    public String getName() {
        return name;
    }

    /*
       Returns the player's ranking in the format
       'Ranking: [ranking]'
     */
    public String getRanking() {
        return ranking;
    }

    /*
       Returns the number of singles titles the player won in the current year
       The format is
       '[year number] singles titles: [titles]'
     */
    public String getTitles() {
        return titles;
    }

    /*
       Returns the player's tournament standing which may be
       'advanced to [round]',
       'out', or
       'not playing'
     */
    public String getTournamentStanding() {
        return tournamentStanding;
    }

    /*
       Returns the name of the tournament that player is currently in
       Returns an empty string if the player isn't in a tournament
     */
    public String getCurrentTournament() {
        return currentTournament;
    }

    /*
       Returns the player's latest match result
       The format is '[round]- [opponent] [score]'
       Returns an empty string if the player isn't in a tournament
     */
    public String getLatestMatchResult() {
        return latestMatchResult;
    }

    /*
       Returns the player's upcoming match
       The format is '[player]  [time]'
     */
    public String getUpcomingMatch() {
        return upcomingMatch;
    }

    /*

     */
    public String getResultsURL() {
        return resultsURL;
    }
}
