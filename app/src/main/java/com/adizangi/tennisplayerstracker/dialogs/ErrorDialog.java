/*
    A dialog that alerts the user that there was an error
 */

package com.adizangi.tennisplayerstracker.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;

import com.adizangi.tennisplayerstracker.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class ErrorDialog extends DialogFragment {

    /*
       Creates the dialog and returns it
       The dialog does not have buttons and does not cancel when touched
       The only way to dismiss the dialog is to call DialogFragment.dismiss()
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog dialog = new AlertDialog.Builder
                (getActivity(), R.style.Theme_MaterialComponents_BottomSheetDialog)
                .setMessage(R.string.dialog_message_error)
                .setCancelable(false)
                .create();
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }
}
