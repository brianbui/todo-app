package com.buibros.todoapp;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;


public class PomodoroTimerActivity extends ActionBarActivity {
    private PomodoroTimer pomodoroTimer;
    private PomodoroTimer restTimer;
    private boolean timerHasStarted = false;
    private Button timerButton;
    private Button resetButton;
    public TextView timerText;
    private long remainingTime;
    // Formatted string for displaying time
    private static final String FORMAT = "%02d:%02d";
    // Time measured in milliseconds
    // Work time is 25 minutes
    private final long START_WORK_TIME = 1500 * 1000;
    // Rest time is 5 minutes
    private final long START_REST_TIME = 300 * 1000;
    // Count down every second
    private final long INTERVAL = 1 * 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pomodoro_timer);

        // Initialize timer button
        timerButton = (Button) findViewById(R.id.timer_button);
        timerButton.setOnClickListener(myhandler1);

        // Initialize reset button
        resetButton = (Button) findViewById(R.id.timer_reset_button);
        resetButton.setOnClickListener(myhandler2);

        // Initialize count down text view with formatted starting time
        timerText = (TextView) findViewById(R.id.timer_text);
        timerText.setText("" + String.format(FORMAT,
                TimeUnit.MILLISECONDS.toMinutes(START_WORK_TIME) - TimeUnit.HOURS.toMinutes(
                        TimeUnit.MILLISECONDS.toHours(START_WORK_TIME)),
                TimeUnit.MILLISECONDS.toSeconds(START_WORK_TIME) - TimeUnit.MINUTES.toSeconds(
                        TimeUnit.MILLISECONDS.toMinutes(START_WORK_TIME))));

        // Initialize count down timer with starting default time
        pomodoroTimer = new PomodoroTimer(START_WORK_TIME, INTERVAL);
    }

    // OnClickListener handler for start button
    View.OnClickListener myhandler1 = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (!timerHasStarted) {
                // Switch timer flag
                timerHasStarted = true;
                // Start count down
                pomodoroTimer.start();
                // Change button text
                timerButton.setText("PAUSE");
            }

            else if (timerHasStarted){
                // Switch timer flag
                timerHasStarted = false;
                // Cancel current timer
                pomodoroTimer.cancel();
                // Create new timer with remaining time
                pomodoroTimer = new PomodoroTimer(remainingTime, INTERVAL);
                // Change button text
                timerButton.setText("RESUME");
            }
        }
    };

    // OnClickListener handler for reset button
    View.OnClickListener myhandler2 = new View.OnClickListener() {
        // When pressed, cancel timer and reset everything back to default values
        @Override
        public void onClick(View view) {
            // Cancel current timer
            pomodoroTimer.cancel();
            // Reset button text
            timerButton.setText("START");
            // Reset count down text view with formatted starting time
            timerText.setText("" + String.format(FORMAT,
                    TimeUnit.MILLISECONDS.toMinutes(START_WORK_TIME) - TimeUnit.HOURS.toMinutes(
                            TimeUnit.MILLISECONDS.toHours(START_WORK_TIME)),
                    TimeUnit.MILLISECONDS.toSeconds(START_WORK_TIME) - TimeUnit.MINUTES.toSeconds(
                            TimeUnit.MILLISECONDS.toMinutes(START_WORK_TIME))));
            // Reset time remaining counter
            remainingTime = 0;
            // Reset timer flag
            timerHasStarted = false;
        }
    };

    // Timer class for counting down
    public class PomodoroTimer extends CountDownTimer {
        public PomodoroTimer(long startTime, long interval) {
            super(startTime, interval);
        }

        // Timer ticks every interval
        @Override
        public void onTick(long millisUntilFinished) {
            // Save remaining time after every tick
            remainingTime = millisUntilFinished;

            // Update count down text with formatted time remaining
            timerText.setText("" + String.format(FORMAT,
                    TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(
                            TimeUnit.MILLISECONDS.toHours(millisUntilFinished)),
                    TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(
                            TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
        }

        // Timer count down finishes
        @Override
        public void onFinish() {
            // Display message when timer finishes
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
