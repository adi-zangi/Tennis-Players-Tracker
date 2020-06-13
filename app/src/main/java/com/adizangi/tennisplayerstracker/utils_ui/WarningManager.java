/*
   Used to display a warning on the screen that blocks the UI until it is
   dismissed
 */

package com.adizangi.tennisplayerstracker.utils_ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.ContextWrapper;

import com.adizangi.tennisplayerstracker.R;

public class WarningManager extends ContextWrapper {

    private AlertDialog currentWarning;

    /*
       Constructs a WarningManager with the given context, which is the context
       of the parent view over which the warning will appear
     */
    public WarningManager(Context base) {
        super(base);
    }

    /*
       Displays a warning with the given title and message
       The warning covers the screen with a transparent gray background,
       and contains the text at the center
     */
    public void showWarning(String title, String message) {
        currentWarning = new AlertDialog.Builder
                (this, R.style.Theme_MaterialComponents_BottomSheetDialog)
                .setTitle(title)
                .setMessage(message)
                .setCancelable(false)
                .create();
        currentWarning.show();
    }

    /*
       Displays a warning with the given message and no title
       The warning covers the screen with a transparent gray background,
       and contains the text at the center
     */
    public void showWarning(String message) {
        currentWarning = new AlertDialog.Builder
                (this, R.style.Theme_MaterialComponents_BottomSheetDialog)
                .setMessage(message)
                .setCancelable(false)
                .create();
        currentWarning.show();
    }

    /*
       If a warning is currently showing, dismisses it
     */
    public void dismissWarning() {
        if (currentWarning != null) {
            currentWarning.dismiss();
        }
        currentWarning = null;
    }

}
