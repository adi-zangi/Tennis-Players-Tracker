/*
   A dialog that prompts the user to select which network type the app is
   permitted to use, with the options 'use any network' and 'use unmetered
   network only'
 */

package com.adizangi.tennisplayerstracker.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
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
        void onSelectAnyNetwork();
        void onSelectUnmeteredOnly();
    }

    private NetworkTypeListener callback;

    private DialogInterface.OnClickListener onPositiveClick =
            new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            callback.onSelectAnyNetwork();
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
       Sets the NetworkTypeListener callback to the given callback
     */
    public void setNetworkTypeListener(NetworkTypeListener callback) {
        this.callback = callback;
    }

    /*
       Creates the dialog and returns it
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog dialog = new AlertDialog.Builder
                (getActivity(), R.style.Theme_AppCompat_DayNight_Dialog_Alert)
                .setMessage(R.string.dialog_network_type)
                .setPositiveButton(R.string.button_use_any_network, onPositiveClick)
                .setNegativeButton(R.string.button_use_unmetered_only, onNegativeClick)
                .create();
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }
}
