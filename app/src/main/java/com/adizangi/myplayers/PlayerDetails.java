// this could be place to mention only contains details related to singles tennis

package com.adizangi.myplayers;

public class PlayerDetails {

    private String name;
    private String ranking;
    private String titles;
    private String tournamentStanding;
    private String currentTournament;
    private String latestMatchResult;
    private String upcomingMatch;
    private String resultsURL;

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

    public String getName() {
        return name;
    }

    public String getRanking() {
        return ranking;
    }

    public String getTitles() {
        return titles;
    }

    public String getTournamentStanding() {
        return tournamentStanding;
    }

    public String getCurrentTournament() {
        return currentTournament;
    }

    public String getLatestMatchResult() {
        return latestMatchResult;
    }

    public String getUpcomingMatch() {
        return upcomingMatch;
    }

    public String getResultsURL() {
        return resultsURL;
    }
}
