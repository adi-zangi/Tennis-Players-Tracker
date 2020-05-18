/*
   Used to display an alert message that blocks the UI while the message is
   showing
 */

package com.adizangi.tennisplayerstracker.utils_ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.ContextWrapper;

import com.adizangi.tennisplayerstracker.R;

public class ScreenBlocker extends ContextWrapper {

    private AlertDialog screenBlock;

    /*
       Constructs a ScreenBlocker with the given context, which is the context
       of the parent view over which the blocking message will appear
     */
    public ScreenBlocker(Context base) {
        super(base);
    }

    /*
       Displays a screen-blocking alert with the given title and message
       The screen block covers the screen, has a transparent gray background,
       and contains the alert text at the center
     */
    public void blockScreen(String alertTitle, String alertMessage) {
        screenBlock = new AlertDialog.Builder
                (this, R.style.Theme_MaterialComponents_BottomSheetDialog)
                .setTitle(alertTitle)
                .setMessage(alertMessage)
                .setCancelable(false)
                .create();
        screenBlock.show();
    }

    /*
       Displays a screen-blocking alert with the given message and no title
       The screen block covers the screen, has a transparent gray background,
       and contains the alert text at the center
     */
    public void blockScreen(String alertMessage) {
        screenBlock = new AlertDialog.Builder
                (this, R.style.Theme_MaterialComponents_BottomSheetDialog)
                .setMessage(alertMessage)
                .setCancelable(false)
                .create();
        screenBlock.show();
    }

    /*
       Un-blocks the screen, if it is currently blocked
     */
    public void unBlockScreen() {
        if (screenBlock != null) {
            screenBlock.dismiss();
        }
        screenBlock = null;
    }

}
