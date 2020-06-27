/*
   Represents a tennis player's statistics
 */

package com.adizangi.tennisplayerstracker.utils_data;

import java.io.Serializable;

public class PlayerStats implements Comparable<PlayerStats>, Serializable {

    private static final long serialVersionUID = 5178394270442919234L;

    private String name;
    private String ranking;
    private String titles;
    private String tournamentStanding;
    private String currentTournament;
    private String latestMatchResult;
    private String upcomingMatch;

    /*
       Constructs a PlayerStats object
     */
    public PlayerStats(String name,
                String ranking,
                String titles,
                String tournamentStanding,
                String currentTournament,
                String latestMatchResult,
                String upcomingMatch) {
        this.name = name;
        this.ranking = ranking;
        this.titles = titles;
        this.tournamentStanding = tournamentStanding;
        this.currentTournament = currentTournament;
        this.latestMatchResult = latestMatchResult;
        this.upcomingMatch = upcomingMatch;
    }

    /*
       Returns the player's name
     */
    public String getName() {
        return name;
    }

    /*
       Returns the player's ranking as a string that is in the format
       'Ranking: [ranking number]'
     */
    public String getRanking() {
        return ranking;
    }

    /*
       Returns the number of singles titles the player won in the current year
       The format is
       '[year number] singles titles: [number of titles]'
     */
    public String getTitles() {
        return titles;
    }

    /*
       Returns the player's tournament standing which may be
       'advanced to [tournament round]',
       'out', or
       'not playing'
     */
    public String getTournamentStanding() {
        return tournamentStanding;
    }

    /*
       Returns the name of the tournament the player is currently in
       Returns an empty string if the player isn't in a tournament
     */
    public String getCurrentTournament() {
        return currentTournament;
    }

    /*
       Returns the player's latest match result
       The format is '[tournament round]- [opponent's name] [score]'
       Returns an empty string if the player isn't in a tournament
     */
    public String getLatestMatchResult() {
        return latestMatchResult;
    }

    /*
       Returns the player's upcoming match
       The format is '[player's name] [time of match]'
       Returns an empty string if the player does not have an upcoming match today
     */
    public String getUpcomingMatch() {
        return upcomingMatch;
    }

    /*
        Returns -1, 0, or 1 if this PlayerStats is less than, equal to, or
        greater than the given other PlayerStats, respectively
        PlayerStats objects are compared by tournament standing, such that a
        player who advanced to the next round is greater than a player who is
        out, and a player who is out is greater than a player who is not playing
        If the tournament standings are the same, the objects are compared by
        current tournament alphabetically
     */
    @Override
    public int compareTo(PlayerStats o) {
        boolean thisAdvanced = tournamentStanding.contains("advanced");
        boolean otherAdvanced = o.tournamentStanding.contains("advanced");
        if (thisAdvanced && !otherAdvanced) {
            return 1;
        } else if (!thisAdvanced && otherAdvanced) {
            return -1;
        }
        boolean thisOut = tournamentStanding.equals("out");
        boolean otherOut = o.tournamentStanding.equals("out");
        if (thisOut && !otherOut) {
            return 1;
        } else if (!thisOut && otherOut) {
            return -1;
        }
        return currentTournament.compareTo(o.currentTournament);
    }

}
