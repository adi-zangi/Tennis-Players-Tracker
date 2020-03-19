/*
   ViewModel for the views in MainActivity's tabs
 */

package com.adizangi.myplayers.models;

import android.app.Application;

import com.adizangi.myplayers.objects.FileManager;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

public class TabsViewModel extends AndroidViewModel {

    private List<String> myPlayers;
    private final List<String> totalPlayers;
    private MutableLiveData<String> addedPlayer;
    private MutableLiveData<Integer> removedPlayerPosition;
    private FileManager fileManager;

    /*
       Constructs a TabsViewModel with the given Application reference
       Initializes the list of the user's selected players and the list of
       total players with the saved lists, and initializes the observable data
       to empty values
     */
    public TabsViewModel(@NonNull Application application) {
        super(application);
        fileManager = new FileManager(application);
        myPlayers = fileManager.readMyPlayers();
        totalPlayers = fileManager.readTotalPlayers();
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
