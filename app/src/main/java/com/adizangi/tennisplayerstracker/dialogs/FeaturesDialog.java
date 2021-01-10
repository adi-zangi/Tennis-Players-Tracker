/*
   A dialog that explains about the app's features
 */

package com.adizangi.tennisplayerstracker.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;

import com.adizangi.tennisplayerstracker.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class FeaturesDialog extends DialogFragment {

    /*
       Creates the dialog and returns it
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setView(View.inflate(getContext(), R.layout.dialog_features, null))
                .setPositiveButton(R.string.button_done, null)
                .create();
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }
}
