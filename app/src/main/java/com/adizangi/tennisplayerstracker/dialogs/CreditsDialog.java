/*
   A dialog that displays a list of credits for the information and images in
   this app
 */

package com.adizangi.tennisplayerstracker.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;

import com.adizangi.tennisplayerstracker.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class CreditsDialog extends DialogFragment {

    /*
       Creates the dialog and returns it
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return new AlertDialog.Builder(getActivity())
                .setTitle(R.string.dialog_title_credits)
                .setMessage(R.string.dialog_message_credits)
                .setNegativeButton(R.string.button_close, null)
                .create();
    }
}
