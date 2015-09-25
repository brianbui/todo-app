package com.buibros.todoapp;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class PomodoroTimerActivity extends ActionBarActivity implements View.OnClickListener {

    private PomodoroTimer pomodoroTimer;
    private boolean timerHasStarted = false;
    private Button timerButton;
    public TextView timerText;
    // Time measured in milliseconds
    // Work time is 25 minutes
    private final long startWorkTime = 1500 * 1000;
    // Rest time is 5 minutes
    private final long startRestTime = 300 * 1000;
    // Count down every second
    private final long interval = 1 * 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pomodoro_timer);

        // Initialize start button
        timerButton = (Button) this.findViewById(R.id.timer_button);
        timerButton.setOnClickListener(this);
        // Initialize count down text with starting time
        timerText = (TextView) this.findViewById(R.id.timer_text);
        timerText.setText(timerText.getText() + String.valueOf(startWorkTime / 1000));
        // Initialize count down timer with starting time
        pomodoroTimer = new PomodoroTimer(startWorkTime, interval);
    }

    @Override
    public void onClick(View v) {
        // When button pressed, start count down and change text to start
        if (!timerHasStarted) {
            pomodoroTimer.start();
            timerHasStarted = true;
            timerButton.setText("STOP");
        }
        // When button pressed, stop count down at current time and change text to restart
        else {
            pomodoroTimer.cancel();
            timerHasStarted = false;
            timerButton.setText("RESTART");
        }
    }


    // Timer class for counting down
    public class PomodoroTimer extends CountDownTimer {

        public PomodoroTimer(long startTime, long interval) {
            super(startTime, interval);
        }

        // Every count down tick displays the current second
        @Override
        public void onTick(long millisUntilFinished) {
            timerText.setText("" + millisUntilFinished / 1000);
        }

        // After count down is over, display message
        @Override
        public void onFinish() {
            timerText.setText("Time's up!");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_pomodoro_timer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
