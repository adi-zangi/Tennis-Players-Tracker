/*
   A CountDownTimer that accepts a TextView to display the countdown in
 */

package com.adizangi.tennisplayerstracker.utils_ui;

import android.os.CountDownTimer;
import android.widget.TextView;

public class Timer extends CountDownTimer {

    private TextView countDownView;

    /*
       Constructs a Timer which will count down starting at millisInFuture and
       going down by intervals the length of countDownInterval
       The given countDownView will begin empty, and when start() is called it
       will be updated with the number of seconds left
     */
    public Timer(long millisInFuture, long countDownInterval, TextView countDownView) {
        super(millisInFuture, countDownInterval);
        this.countDownView = countDownView;
        countDownView.setText("");
    }

    /*
       Called each time the timer goes down by countDownInterval
       Updates the text view with the number of seconds left
     */
    @Override
    public void onTick(long millisUntilFinished) {
        long secondsRemaining = millisUntilFinished / 1000;
        String secondsShown = countDownView.getText().toString();
        if (secondsShown.isEmpty()) {
            countDownView.setText(String.valueOf(secondsRemaining));
        } else if (secondsRemaining < Integer.parseInt(secondsShown)) { // needed due to bug
            countDownView.setText(String.valueOf(secondsRemaining));
        }
    }

    /*
       Called when the time is up
       Updates the text view with "0"
     */
    @Override
    public void onFinish() {
        countDownView.setText("0");
    }

}
