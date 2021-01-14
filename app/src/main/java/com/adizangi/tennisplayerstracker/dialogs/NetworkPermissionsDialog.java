/*
   A dialog that asks the user whether to allow the app to use mobile data when
   wifi isn't available
   It has two options which are 'Yes' and 'No'
   The host activity must implement NetworkPermissionsDialog.OnClickListener
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

public class NetworkPermissionsDialog extends DialogFragment {

    /*
       Interface used to communicate click events to the hosting activity
     */
    public interface OnClickListener {
        void onSelectYes();
        void onSelectNo();
    }

    private OnClickListener callback;

    private DialogInterface.OnClickListener onPositiveClick =
            new DialogInterface.OnClickListener() {
        /*
           Called when the user selects 'Yes'
           Invokes onSelectYes() in the host activity
         */
        @Override
        public void onClick(DialogInterface dialog, int which) {
            callback.onSelectYes();
        }
    };

    private DialogInterface.OnClickListener onNegativeClick =
            new DialogInterface.OnClickListener() {
        /*
           Called when the user selects 'No'
           Invokes onSelectNo() in the host activity
         */
        @Override
        public void onClick(DialogInterface dialog, int which) {
            callback.onSelectNo();
        }
    };

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
            callback = (OnClickListener) context;
        } catch (Exception e) {
            throw new ClassCastException(getActivity() +
                    " must implement NetworkPermissionsDialog.OnClickListener");
        }
    }

    /*
       Called after onAttach()
       Creates the dialog and sets event handlers for the buttons
       Returns the dialog
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog dialog = new AlertDialog.Builder
                (getActivity(), R.style.Theme_AppCompat_DayNight_Dialog_Alert)
                .setTitle(R.string.dialog_title_network_permissions)
                .setMessage(R.string.dialog_message_network_permissions)
                .setPositiveButton(R.string.button_yes, onPositiveClick)
                .setNegativeButton(R.string.button_no, onNegativeClick)
                .create();
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }

}
