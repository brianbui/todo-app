package com.buibros.todoapp.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.buibros.todoapp.R;

import java.util.concurrent.TimeUnit;


public class PomodoroTimerActivity extends ActionBarActivity {
    private boolean timerHasStarted = false;
    private long remainingTime;
    private Button timerButton;
    private Button resetButton;
    public TextView timerTextRemaining;
    public TextView timerTextLabel;
    private RelativeLayout layout;
    private PomodoroTimer pomodoroTimer;
    private RestTimer restTimer;
    // Formatted string for displaying time
    private static final String FORMAT = "%02d:%02d";
    // Work time is 25 minutes
    private static final long START_WORK_TIME = 5 * 1000; // in milliseconds
    // Rest time is 5 minutes
    private static final long START_REST_TIME = 3 * 1000; // in milliseconds
    // Interval time is 250 milliseconds
    private static final long INTERVAL = 250; // measured in milliseconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pomodoro_timer);

        // Initialize timer button
        timerButton = (Button) findViewById(R.id.timer_button);
        timerButton.setOnClickListener(startHandler);
        // Initialize reset button
        resetButton = (Button) findViewById(R.id.timer_reset_button);
        resetButton.setOnClickListener(resetHandler);

        // Initialize layout background color
        layout = (RelativeLayout) findViewById(R.id.timer_layout);
        layout.setBackgroundColor(Color.BLUE);

        // Initialize count down text view with formatted starting time
        timerTextRemaining = (TextView) findViewById(R.id.timer_text_remaining);
        timerTextRemaining.setText("" + String.format(FORMAT,
                TimeUnit.MILLISECONDS.toMinutes(START_WORK_TIME) - TimeUnit.HOURS.toMinutes(
                        TimeUnit.MILLISECONDS.toHours(START_WORK_TIME)),
                TimeUnit.MILLISECONDS.toSeconds(START_WORK_TIME) - TimeUnit.MINUTES.toSeconds(
                        TimeUnit.MILLISECONDS.toMinutes(START_WORK_TIME))));
        // Initialize label
        timerTextLabel = (TextView) findViewById(R.id.timer_text_label);
        timerTextLabel.setText("WORK");

        // Initialize pomodoro count down timer with initial time
        pomodoroTimer = new PomodoroTimer(START_WORK_TIME, INTERVAL);
        // Initialize rest count down timer with initial time
        restTimer = new RestTimer(START_REST_TIME, INTERVAL);
    }

    // Handler for start button click
    View.OnClickListener startHandler = new View.OnClickListener() {
        // When pressed, pause and resume timer and change button text
        @Override
        public void onClick(View view) {
            if (!timerHasStarted) {
                // Switch timer flag
                timerHasStarted = true;
                // Start count down
                pomodoroTimer.start();
                // Change background color
                layout.setBackgroundColor(Color.GREEN);
                // Change button text
                timerButton.setText("PAUSE");
            }
            else if (timerHasStarted){
                // Switch timer flag
                timerHasStarted = false;
                // Cancel current timer
                pomodoroTimer.cancel();
                // Reset background color
                layout.setBackgroundColor(Color.WHITE);
                // Create new timer with remaining time
                pomodoroTimer = new PomodoroTimer(remainingTime, INTERVAL);
                // Change button text
                timerButton.setText("RESUME");
            }
        }
    };

    // Handler for reset button click
    View.OnClickListener resetHandler = new View.OnClickListener() {
        // When pressed, cancel timer and reset everything back to default values
        @Override
        public void onClick(View view) {
            // Cancel all timers
            pomodoroTimer.cancel();
            restTimer.cancel();
            // Reset background color
            layout.setBackgroundColor(Color.WHITE);
            // Reset timer button visibility
            timerButton.setVisibility(View.VISIBLE);
            // Reset label text
            timerTextLabel.setText("WORK!");
            // Reset button text
            timerButton.setText("START");
            // Reset count down text view with formatted initial time
            timerTextRemaining.setText("" + String.format(FORMAT,
                    TimeUnit.MILLISECONDS.toMinutes(START_WORK_TIME) - TimeUnit.HOURS.toMinutes(
                            TimeUnit.MILLISECONDS.toHours(START_WORK_TIME)),
                    TimeUnit.MILLISECONDS.toSeconds(START_WORK_TIME) - TimeUnit.MINUTES.toSeconds(
                            TimeUnit.MILLISECONDS.toMinutes(START_WORK_TIME))));
            // Reset remaining time counter
            remainingTime = 0;
            // Reset timer flag
            timerHasStarted = false;
        }
    };

    // Work timer class for counting down
    public class PomodoroTimer extends CountDownTimer {
        // Default constructor
        public PomodoroTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        // Timer ticks every 250 milliseconds
        @Override
        public void onTick(long millisUntilFinished) {
            // Save remaining time after every tick
            remainingTime = millisUntilFinished;

            // Update count down text with formatted time remaining
            timerTextRemaining.setText("" + String.format(FORMAT,
                    TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(
                            TimeUnit.MILLISECONDS.toHours(millisUntilFinished)),
                    TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(
                            TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
        }

        // Pomodoro timer count down finishes
        @Override
        public void onFinish() {
            // Start rest timer count down
            restTimer.start();
            // Change background color
            layout.setBackgroundColor(Color.RED);
            // Change label
            timerTextLabel.setText("REST!");
        }
    }

    // Rest timer class for counting down
    public class RestTimer extends CountDownTimer {
        // Default constructor
        public RestTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        // Timer ticks every 250 milliseconds
        @Override
        public void onTick(long millisUntilFinished) {
            // Update count down text with formatted time remaining
            timerTextRemaining.setText("" + String.format(FORMAT,
                    TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(
                            TimeUnit.MILLISECONDS.toHours(millisUntilFinished)),
                    TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(
                            TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
        }

        // Rest timer count down finishes
        @Override
        public void onFinish() {
            // Change label
            timerTextLabel.setText("DONE!");
            // Display message when timer finishes
            timerTextRemaining.setText("Time's up!");
            // Change background color
            layout.setBackgroundColor(Color.BLUE);
            // Hide timer button
            timerButton.setVisibility(View.INVISIBLE);
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
