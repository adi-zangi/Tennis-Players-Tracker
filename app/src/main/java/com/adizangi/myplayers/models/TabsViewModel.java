package com.adizangi.myplayers.models;

import android.app.Application;

import com.adizangi.myplayers.objects.FileManager;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

public class TabsViewModel extends AndroidViewModel {

    private List<String> myPlayers;
    private List<String> totalPlayers;
    private MutableLiveData<String> addedPlayer;
    private MutableLiveData<Integer> removedPlayerPosition;
    private FileManager fileManager;

    public TabsViewModel(@NonNull Application application) {
        super(application);
        fileManager = new FileManager(application);
        myPlayers = fileManager.readMyPlayers();
        totalPlayers = fileManager.readTotalPlayers();
        addedPlayer = new MutableLiveData<>();
        removedPlayerPosition = new MutableLiveData<>();
    }

    public List<String> getMyPlayers() {
        return myPlayers;
    }

    public List<String> getTotalPlayers() {
        return totalPlayers;
    }

    public void addPlayer(String player) {
        addedPlayer.setValue(player);
        myPlayers.add(player);
        fileManager.storeMyPlayers(myPlayers);
    }

    public void removePlayerInPosition(int position) {
        removedPlayerPosition.setValue(position);
        myPlayers.remove(position);
        fileManager.storeMyPlayers(myPlayers);
    }

}
