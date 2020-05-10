/*
   Creates alert messages that show on the screen
 */

package com.adizangi.myplayers.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.ContextWrapper;

import com.adizangi.myplayers.R;

public class AlertMessageCreator extends ContextWrapper {

    private AlertDialog currentMessage;

    /*
       Constructs a AlertMessageCreator with the given context
     */
    public AlertMessageCreator(Context base) {
        super(base);
    }

    /*
       Shows an alert message with the given title and message
       The message shows as a transparent overlay on the screen with text in
       the center
     */
    public void showMessage(String title, String message) {
        AlertDialog alertMessage = new AlertDialog.Builder
                (this, R.style.Theme_MaterialComponents_BottomSheetDialog)
                .setTitle(title)
                .setMessage(message)
                .create();
        alertMessage.show();
        currentMessage = alertMessage;
    }

    /*
       Un-shows the message that is currently showing, if there is one
     */
    public void dismissMessage() {
        if (currentMessage != null) {
            currentMessage.dismiss();
        }
        currentMessage = null;
    }

}
