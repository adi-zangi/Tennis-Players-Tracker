/*
   Fills the My Players Spinner with custom views
   The first entry in the Spinner is a hint that says ‘Edit my players’
   The following entry has an AutoCompleteTextView used to select a player and
   a button that says 'Add'
   The rest of the entries below it contain the user's selected players and
   a button that says 'Remove' next to each
 */

package com.adizangi.myplayers;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class MyPlayersAdapter extends ArrayAdapter<String> {

    /*
       Constructs a MyPlayersAdapter
       The given Context is used to access system services
	   myPlayers is a list of the players that are currently selected
	   allPlayers is a list of all the players the user can select
     */
    public MyPlayersAdapter(@NonNull Context context,
                            @NonNull List<String> myPlayers,
                            @NonNull List<String> allPlayers) {
        super(context, 0, myPlayers); // Invokes super constructor without a resource
                                               // file because this class handles the resource files
                                               // myPlayers is the data set for the Spinner
    }

    @NonNull
    @Override
    /*
       Returns a View for the given position in the data set
       If the given position is 0, returns the hint's View
       If the given position is 1, returns the add player View
       Otherwise, returns a View for the player in the given position
     */
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return super.getView(position, convertView, parent);
    }

    private static class HintHolder {

        HintHolder(TextView hintView) {

        }

    }

    private static class AddHolder {

        AddHolder(AutoCompleteTextView autoComplete, Button addButton) {

        }

    }

    private static class PlayerHolder {

        PlayerHolder(TextView nameView, Button removeButton) {

        }

    }

}
