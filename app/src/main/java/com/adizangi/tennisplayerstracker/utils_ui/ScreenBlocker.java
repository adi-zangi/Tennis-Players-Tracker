/*
   Used to display an error message on the screen that blocks the screen
 */

package com.adizangi.tennisplayerstracker.utils_ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.ContextWrapper;

import com.adizangi.tennisplayerstracker.R;

public class ScreenBlocker extends ContextWrapper {

    private AlertDialog currentMessage;

    /*
       Constructs a ScreenBlocker with the given context, which is the context
       of the parent view over which the blocking message will appear
     */
    public ScreenBlocker(Context base) {
        super(base);
    }

    /*
       Shows an alert message with the given title and message
       The message shows as a transparent overlay on the screen with text in
       the center
     */
    public void blockScreen(String title, String message) {
        AlertDialog alertMessage = new AlertDialog.Builder
                (this, R.style.Theme_MaterialComponents_BottomSheetDialog)
                .setTitle(title)
                .setMessage(message)
                .setCancelable(false)
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
