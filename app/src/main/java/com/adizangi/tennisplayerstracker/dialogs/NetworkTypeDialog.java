/*
   A dialog that prompts the user to select which type of network connection
   the app is permitted to use, with the options 'any connection' and 'unmetered
   connection only'
 */

package com.adizangi.tennisplayerstracker.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import com.adizangi.tennisplayerstracker.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class NetworkTypeDialog extends DialogFragment {

    /*
       Interface used to communicate click events to the hosting activity
     */
    public interface NetworkTypeListener {
        void onSelectAnyConnection();
        void onSelectUnmeteredOnly();
    }

    private NetworkTypeListener callback;

    private DialogInterface.OnClickListener onPositiveClick =
            new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            callback.onSelectAnyConnection();
        }
    };

    private DialogInterface.OnClickListener onNegativeClick =
            new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            callback.onSelectUnmeteredOnly();
        }
    };

    /*
       Called after onAttach()
       Creates the dialog and returns it
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog dialog = new AlertDialog.Builder
                (getActivity(), R.style.Theme_AppCompat_DayNight_Dialog_Alert)
                .setMessage(R.string.dialog_message_network_type)
                .setPositiveButton(R.string.button_any_connection, onPositiveClick)
                .setNegativeButton(R.string.button_unmetered_connection_only, onNegativeClick)
                .create();
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }

    /*
       Called when the dialog is first attached to its host activity
       If the host activity implements the callback interface, instantiates
       callback
       Otherwise, throws a ClassCastException
     */
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            callback = (NetworkTypeListener) context;
        } catch (Exception e) {
            throw new ClassCastException(context + " must implement NetworkTypeListener");
        }
    }

}
