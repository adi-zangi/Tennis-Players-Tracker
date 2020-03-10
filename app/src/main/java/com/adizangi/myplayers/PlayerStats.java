/*
   Contains statistics about a tennis player, including tournament results
   in singles tennis
 */

package com.adizangi.myplayers;

import java.io.Serializable;

class PlayerStats implements Serializable {

    private String name;
    private String ranking;
    private String titles;
    private String tournamentStanding;
    private String currentTournament;
    private String latestMatchResult;
    private String upcomingMatch;
    private String resultsURL;

    /*
       Constructs a PlayerStats object
     */
    PlayerStats(String name,
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
    String getName() {
        return name;
    }

    /*
       Returns the player's ranking in the format
       'Ranking: [ranking]'
     */
    String getRanking() {
        return ranking;
    }

    /*
       Returns the number of singles titles the player won in the current year
       The format is
       '[year number] singles titles: [number of titles]'
     */
    String getTitles() {
        return titles;
    }

    /*
       Returns the player's tournament standing which may be
       'advanced to [tournament round]',
       'out', or
       'not playing'
     */
    String getTournamentStanding() {
        return tournamentStanding;
    }

    /*
       Returns the name of the tournament the player is currently in
       Returns an empty string if the player isn't in a tournament
     */
    String getCurrentTournament() {
        return currentTournament;
    }

    /*
       Returns the player's latest match result
       The format is '[tournament round]- [opponent's name] [score]'
       Returns an empty string if the player isn't in a tournament
     */
    String getLatestMatchResult() {
        return latestMatchResult;
    }

    /*
       Returns the player's upcoming match
       The format is '[player's name]  [time]'
       Returns an empty string if the player is not playing a match today
     */
    String getUpcomingMatch() {
        return upcomingMatch;
    }

    /*
       Returns the URL of an ESPN web page that has all the match results of
       the player
     */
    String getResultsURL() {
        return resultsURL;
    }
}
