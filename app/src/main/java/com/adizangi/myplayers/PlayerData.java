/*
   Contains data about a tennis player
 */

package com.adizangi.myplayers;

public class PlayerData implements Comparable<PlayerData> {

    private String name;
    private String ranking;
    private String titles;
    private String currentTournament;
    private String tournamentStatus;
    private String latestMatchResult;
    private String upcomingMatch;
    private String resultsPageURL;

    /*
       Constructor is private so the only way to construct a PlayerData object
       is through the PlayerDataBuilder class
     */
    private PlayerData(PlayerDataBuilder builder) {
        this.name = builder.name;
        this.ranking = builder.ranking;
        this.titles = builder.titles;
        this.currentTournament = builder.currentTournament;
        this.tournamentStatus = builder.tournamentStatus;
        this.latestMatchResult = builder.latestMatchResult;
        this.upcomingMatch = builder.upcomingMatch;
        this.resultsPageURL = builder.resultsPageURL;
    }

    /*
       Returns the name of the tennis player
     */
    public String getName() {
        return name;
    }

    /*
       Returns the player's current ranking as a String in the form
       'Current ranking: [ranking number]'
     */
    public String getRanking() {
        return ranking;
    }

    /*
       Returns the number of singles titles the player has won this year as a
       String in the form
       '[current year number] singles titles: [number of titles]'
     */
    public String getTitles() {
        return titles;
    }

    /*
       Returns the name of the current tournament that the player is competing
       in (for singles tennis only)
       This includes any tournament that is currently happening, whether the
       player has advanced or has been eliminated from it
       Returns an empty String if the player has not entered any current
       tournament
     */
    public String getCurrentTournament() {
        return currentTournament;
    }

    /*
       Returns a String that indicates whether the player has advanced to the
       next round in their tournament, was eliminated from the tournament, or
       hasn't entered any current tournament
       The form of the String for each tournament status is:
       'advanced to [round]'
       'out'
       'not playing'
     */
    public String getTournamentStatus() {
        return tournamentStatus;
    }

    /*
       Returns a String containing the result of the latest match the player
       has played
       The String contains the tournament round, the opponent, and the score of
       the match
       Returns an empty String if the player has not entered any current
       tournament
     */
    public String getLatestMatchResult() {
        return latestMatchResult;
    }

    /*
       If the player has a match scheduled for today, returns a String
       containing the player's name and the time of the match
       Otherwise, returns an empty String
     */
    public String getUpcomingMatch() {
        return upcomingMatch;
    }

    /*
       Returns the URL of an ESPN web page that contains all the match results
       of the player
     */
    public String getResultsPageURL() {
        return resultsPageURL;
    }

    @Override
    /*
       Returns 1 if this PlayerData comes before the given PlayerData, -1 if
       this PlayerData comes after the given PlayerData, and 0 if this
       PlayerData is equal to the given PlayerData
       PlayerData's are sorted first by tournament status, with advanced before
       out before not playing
       If the tournament statuses are the same, PlayerData's are sorted
       alphabetically by the name of their latest tournament
     */
    public int compareTo(PlayerData o) {
        boolean thisAdvanced = tournamentStatus.contains("advanced");
        boolean otherAdvanced = o.tournamentStatus.contains("advanced");
        if (thisAdvanced && !otherAdvanced) {
            return 1;
        } else if (!thisAdvanced && otherAdvanced) {
            return -1;
        }
        boolean thisOut = tournamentStatus.equals("out");
        boolean otherOut = o.tournamentStatus.equals("out");
        if (thisOut && !otherOut) {
            return 1;
        } else if (!thisOut && otherOut) {
            return -1;
        }
        return currentTournament.compareTo(o.currentTournament);
    }

    /*
       Used to construct a PlayerData object
     */
    public static class PlayerDataBuilder {

        private String name;
        private String ranking;
        private String titles;
        private String currentTournament;
        private String tournamentStatus;
        private String latestMatchResult;
        private String upcomingMatch;
        private String resultsPageURL;

        public PlayerDataBuilder(String name) {
            this.name = name;
        }

        public PlayerDataBuilder setRanking(String ranking) {
            this.ranking = ranking;
            return this;
        }

        public PlayerDataBuilder setTitles(String titles) {
            this.titles = titles;
            return this;
        }

        public PlayerDataBuilder setCurrentTournament(String latestTournament) {
            this.currentTournament = latestTournament;
            return this;
        }

        public PlayerDataBuilder setTournamentStatus(String tournamentStatus) {
            this.tournamentStatus = tournamentStatus;
            return this;
        }

        public PlayerDataBuilder setLatestMatchResult(String latestMatchResult) {
            this.latestMatchResult = latestMatchResult;
            return this;
        }

        public PlayerDataBuilder setResultsPageURL(String resultsPageURL) {
            this.resultsPageURL = resultsPageURL;
            return this;
        }

        public PlayerDataBuilder setUpcomingMatch(String upcomingMatch) {
            this.upcomingMatch = upcomingMatch;
            return this;
        }

        public PlayerData build() {
            return new PlayerData(this);
        }

    }

}
