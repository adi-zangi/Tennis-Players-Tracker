/*
   ViewModel for the views in MainActivity's tabs
 */

package com.adizangi.myplayers.models;

import android.app.Application;
import android.util.Log;

import com.adizangi.myplayers.objects.FileManager;
import com.adizangi.myplayers.objects.PlayerStats;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

public class TabsViewModel extends AndroidViewModel {

    private final List<String> totalPlayers;
    private final Map<String, PlayerStats> statsMap;
    private List<String> myPlayers;
    private List<PlayerStats> myPlayersStats;
    private MutableLiveData<String> addedPlayer;
    private MutableLiveData<Integer> removedPlayerPosition;
    private FileManager fileManager;

    /*
       Constructs a TabsViewModel with the given Application reference
       Retrieves saved data
       Initializes the observable data to empty values
     */
    public TabsViewModel(@NonNull Application application) {
        super(application);
        fileManager = new FileManager(application);
        totalPlayers = fileManager.readTotalPlayers();
        statsMap = fileManager.readPlayerStats();
        myPlayers = fileManager.readMyPlayers();
        myPlayersStats = new ArrayList<>();
        for (String player : myPlayers) {
            PlayerStats playerStats = statsMap.get(player);
            myPlayersStats.add(playerStats);
        }
        Collections.sort(myPlayersStats, Collections.reverseOrder());
        addedPlayer = new MutableLiveData<>();
        removedPlayerPosition = new MutableLiveData<>();
    }

    /*
       Returns a list of the user's selected players
     */
    public List<String> getMyPlayers() {
        return myPlayers;
    }

    /*
       Returns a list of all the players the user can add
     */
    public List<String> getTotalPlayers() {
        return totalPlayers;
    }

    /*
       Returns a list of PlayerStats objects corresponding to each of the
       user's selected players
       The list is sorted in reverse order
     */
    public List<PlayerStats> getMyPlayersStats() {
        return myPlayersStats;
    }

    /*
       Adds the given player to the list of the user's players and saves the
       list
       Sets the value of addedPlayer to the player so the change will be
       observed
     */
    public void addPlayer(String player) {
        addedPlayer.setValue(player);
        myPlayers.add(player);
        fileManager.storeMyPlayers(myPlayers);
    }

    /*
       Adds a PlayerStats object that corresponds to the given player into the
       PlayerStats list
       The list remains sorted
     */
    public void addPlayerStats(String player) {
        PlayerStats playerStats = statsMap.get(player);
        myPlayersStats.add(playerStats);
        Collections.sort(myPlayersStats, Collections.reverseOrder());
    }

    /*
       Removes the player that is in the given position in the list of the
       user's players
       Sets the value of removedPlayerPosition to the player's position so the
       change will be observed
     */
    public void removePlayerInPosition(int position) {
        removedPlayerPosition.setValue(position);
        myPlayers.remove(position);
        fileManager.storeMyPlayers(myPlayers);
    }

}
