package uk.ac.lincoln.students.a15595332.initiateintervalfitnesstraining;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.muddzdev.styleabletoast.StyleableToast;

import net.sqlcipher.database.SQLiteDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import static java.lang.Thread.sleep;

public class PlayingActivity extends AppCompatActivity {

    private SQLiteDatabaseHandler db;

    // Strings
    public String title;

    // ints
    public int prepare;
    public int workout;
    public int rest;
    public int cycles;
    public int sets;
    public int setRest;
    public int coolDown;
    public int cycleCounter;
    public int setCounter;
    public int id;
    public int calTime;
    public int calTimeMin;

    // booleans
    public Boolean localSound;

    public CountDownTimer countDownTimer;

    public MediaPlayer mp;
    public MediaPlayer mp3;

    public SoundPool soundPool;
    int bellSoundId;
    int bell3SoundId;

    public ImageButton soundButton;

    /**************************************************************************************
     * Title: Count Down Timer
     * Author: Google
     * Date: June 2018
     * Availability: https://developer.android.com/reference/android/os/CountDownTimer
     *
     ***************************************************************************************/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Prevent orientation layout change.
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // Sets the HH:mm format for the start time of the workout.
        Calendar c = Calendar.getInstance();
        SimpleDateFormat dfTime = new SimpleDateFormat("HH", Locale.ENGLISH);
        SimpleDateFormat dfTimeMin = new SimpleDateFormat("mm", Locale.ENGLISH);
        String calTimeS = dfTime.format(c.getTime());
        String calTimeMinS = dfTimeMin.format(c.getTime());


        mp = MediaPlayer.create(getApplicationContext(), R.raw.bell);
        mp3 = MediaPlayer.create(getApplicationContext(), R.raw.bellx3);

        // Sets the sounds to thier files for the bell noises.
        soundPool = new SoundPool.Builder()
                .setMaxStreams(10)
                .build();

        bellSoundId = soundPool.load(this, R.raw.bell, 1);
        bell3SoundId = soundPool.load(this, R.raw.bellx3, 1);


        // Start Time
        calTime = 0;
        calTimeMin = 0;

        // Convert Strings to ints with exception handling.
        try {

            calTimeMin = Integer.parseInt(calTimeMinS);
            calTime = Integer.parseInt(calTimeS);

        }
        catch (Exception e) {
            StyleableToast.makeText(PlayingActivity.this, "Error, please try again.", Toast.LENGTH_LONG, R.style.warningtoast).show();
            Log.e("APP", "exception", e);
            }


        //Stop the screen from going blank
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setContentView(R.layout.activity_playing);

        // Sets the counters for counting how many sets and cycles have lapsed.
        cycleCounter = 1;
        setCounter = 1;


        Intent mIntent = getIntent();
        id = mIntent.getIntExtra("id", 0);


        db = new SQLiteDatabaseHandler(this);

        // create loaded timer
        Timers timer = db.getTimer(id);

        title = timer.getmTitle();

        // Convert Strings to ints with exception handling.
        try {

            prepare = Integer.parseInt(timer.getmPrepare());
            workout = Integer.parseInt(timer.getmWorkout());
            rest = Integer.parseInt(timer.getmRest());
            cycles = Integer.parseInt(timer.getmCycles());
            sets = Integer.parseInt(timer.getmSets());
            setRest = Integer.parseInt(timer.getmSetRest());
            coolDown = Integer.parseInt(timer.getmCoolDown());

        }
        catch (Exception e) {
            StyleableToast.makeText(PlayingActivity.this, "Error, please try again.", Toast.LENGTH_LONG, R.style.warningtoast).show();
            Log.e("APP", "exception", e);
        }

        // This is to allow the whole time to be displayed before the counter starts.
        prepare = prepare + 1;
        workout = workout + 1;
        rest = rest + 1;
        setRest = setRest + 1;
        coolDown = coolDown + 1;

        //Setting the title of the workout.
        TextView titleText = (TextView) findViewById(R.id.titleText);
        titleText.setText(title);

        // Setting the section of the workout.
        TextView section = (TextView) findViewById(R.id.sectionText);

        TextView cycleText = (TextView) findViewById(R.id.cycleText);
        cycleText.setText(String.format(Locale.ENGLISH, "C: %d/%d", cycleCounter, cycles));

        TextView setTextNo = (TextView) findViewById(R.id.setTextNo);
        setTextNo.setText(String.format(Locale.ENGLISH, "S: %d/%d", setCounter, sets));

        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        localSound =  pref.getBoolean("key_sound", true); // getting boolean

        soundButton = (ImageButton) findViewById(R.id.soundImageButton);

        if (localSound){

            soundButton.setImageResource(R.drawable.ic_volume_up_black_24dp);
        }

        if (!localSound){

            soundButton.setImageResource(R.drawable.ic_volume_off_black_24dp);
        }


        //*****  start the prepare *********************************

        section.setText("Prepare");
        View screen = findViewById(R.id.screen);
        screen.setBackgroundColor(getColor(R.color.prepare_colour));


        // Start the prepare countdown.
        startTimer();


    }// End of onCreate method.


    private void startTimer() {


        View screen = findViewById(R.id.screen);
        TextView section = (TextView) findViewById(R.id.sectionText);

        countDownTimer = new CountDownTimer((prepare) * 1000, 1000) {

            //Setting the timer part of the workout.
            TextView timeText = (TextView) findViewById(R.id.timeText);

            public void onTick(long millisUntilFinished) {

                long s = (int) (millisUntilFinished / 1000) % 60;
                long m = (int) ((millisUntilFinished / (1000 * 60)) % 60);

                timeText.setText(String.format(Locale.ENGLISH, "%02d:%02d", m, s));

                if (m == 0 & s <= 3) {
                    // Play bell sound for countdown warning of last 3 seconds.
                    if (localSound) {
                        soundPool.play(bellSoundId, 1, 1, 1, 0, 1);
                    }
                }
            }

            public void onFinish() {

                countDownTimer.cancel();

                // Change Timer text.
                timeText.setText(getString(R.string.timer));

                // Play 3 bell end of round sound.
                if (localSound) {
                    soundPool.play(bell3SoundId, 1, 1, 1, 0, 1);
                }

                playWorkout(); // finish current activity
            }

        }.start();

    }


    public void playWorkout() {
        //Setting the timer part of the workout.


        // Setting the section of the workout.
        TextView section = (TextView) findViewById(R.id.sectionText);

        section.setText("Workout");
        View screen = findViewById(R.id.screen);
        screen.setBackgroundColor(getColor(R.color.work_colour));


        countDownTimer = new CountDownTimer((workout) * 1000, 1000) {

            //Setting the timer part of the workout.
            TextView timeText = (TextView) findViewById(R.id.timeText);


            public void onTick(long millisUntilFinished) {

                long s = (int) (millisUntilFinished / 1000) % 60;
                long m = (int) ((millisUntilFinished / (1000 * 60)) % 60);

                timeText.setText(String.format(Locale.ENGLISH, "%02d:%02d", m, s));


                if (m == 0 & s <= 3) {
                    if (localSound) {
                        // Play bell sound for countdown warning of last 3 seconds.
                        soundPool.play(bellSoundId, 1, 1, 1, 0, 1);
                    }
                }

            }

            public void onFinish() {

                //Change Timer text
                timeText.setText(getString(R.string.timer));

                if (localSound) {
                    // Play 3 bell end of round sound.
                    soundPool.play(bell3SoundId, 1, 1, 1, 0, 1);
                }

                countDownTimer.cancel();

                playRest(); // finish current activity

            }

        }.start();

    }// End of method.


    public void playRest() {


        // Setting the section of the workout.
        final TextView section = (TextView) findViewById(R.id.sectionText);
        section.setText("Rest");
        View screen = findViewById(R.id.screen);
        screen.setBackgroundColor(getColor(R.color.rest_colour));
        //startTimer(rest * 1000);WWHHHYYYYY!!

        countDownTimer = new CountDownTimer((rest) * 1000, 1000) {

            //Setting the timer part of the workout.
            TextView timeText = (TextView) findViewById(R.id.timeText);

            public void onTick(long millisUntilFinished) {

                long s = (int) (millisUntilFinished / 1000) % 60;
                long m = (int) ((millisUntilFinished / (1000 * 60)) % 60);

                timeText.setText(String.format(Locale.ENGLISH, "%02d:%02d", m, s));

                if (m == 0 & s <= 3) {

                    if (localSound) {
                        // Play bell sound for countdown warning of last 3 seconds.
                        soundPool.play(bellSoundId, 1, 1, 1, 0, 1);
                    }

                }

            }

            public void onFinish() {

                // Change Timer text 00:00
                timeText.setText(getString(R.string.timer));

                if (localSound) {
                    // Play 3 bell end of round sound.
                    soundPool.play(bell3SoundId, 1, 1, 1, 0, 1);
                }

                cycleCounter++;
                countDownTimer.cancel();

                // Cycle count reached.
                if (cycleCounter > cycles) {


                    playSetRest();

                }

                // More cycles left to go.
                if (cycleCounter <= cycles) {

                    // Update the cycle count text view.
                    TextView cycleText = (TextView) findViewById(R.id.cycleText);

                    cycleText.setText(String.format(Locale.ENGLISH, "C: %d/%d", cycleCounter, cycles));

                    // start loop again.
                    playWorkout();

                }

            }

        }.start();

    }


    public void playSetRest() {

        final TextView section = (TextView) findViewById(R.id.sectionText);
        section.setText("Set Rest");
        View screen = findViewById(R.id.screen);
        screen.setBackgroundColor(getColor(R.color.set_rest_colour));


        new CountDownTimer(setRest * 1000, 1000) {

            //Setting the timer part of the workout.
            TextView timeText = (TextView) findViewById(R.id.timeText);

            public void onTick(long millisUntilFinished) {

                long s = (int) (millisUntilFinished / 1000) % 60;
                long m = (int) ((millisUntilFinished / (1000 * 60)) % 60);

                timeText.setText(String.format(Locale.ENGLISH, "%02d:%02d", m, s));

                if (m == 0 & s <= 3) {

                    if (localSound) {
                        // Play bell sound for countdown warning of last 3 seconds.
                        soundPool.play(bellSoundId, 1, 1, 1, 0, 1);
                    }
                }


            }

            public void onFinish() {

                // Change Timer text 00:00
                timeText.setText(getString(R.string.timer));

                if (localSound) {
                    // Play 3 bell, end of round sound.
                    soundPool.play(bell3SoundId, 1, 1, 1, 0, 1);
                }
                // Increase the set count
                setCounter++;
                // Reset the cycle counter at the end of a set.
                cycleCounter = 1;
                // Set count has been reached
                if (setCounter > sets) {

                    playCooldown();
                }


                // More sets left to go.
                if (setCounter <= sets) {
                    // Update cycle counter text view.
                    TextView cycleText = (TextView) findViewById(R.id.cycleText);
                    cycleText.setText(String.format(Locale.ENGLISH, "C: %d/%d", cycleCounter, cycles));

                    // Update set counter text view.
                    TextView setText = (TextView) findViewById(R.id.setTextNo);
                    setText.setText(String.format(Locale.ENGLISH, "S: %d/%d", setCounter, sets));

                    // Go back to start of set
                    playWorkout();

                }

            }
        }.start();


    }


    public void playCooldown() {

        final TextView section = (TextView) findViewById(R.id.sectionText);
        section.setText("Cool down");
        View screen = findViewById(R.id.screen);
        screen.setBackgroundColor(getColor(R.color.cool_colour));


        new CountDownTimer(coolDown * 1000, 1000) {

            //Setting the timer part of the workout.
            TextView timeText = (TextView) findViewById(R.id.timeText);

            public void onTick(long millisUntilFinished) {

                long s = (int) (millisUntilFinished / 1000) % 60;
                long m = (int) ((millisUntilFinished / (1000 * 60)) % 60);

                timeText.setText(String.format(Locale.ENGLISH, "%02d:%02d", m, s));

                if (m == 0 & s <= 3) {
                    if (localSound) {
                        // Play bell sound for countdown warning of last 3 seconds.
                        soundPool.play(bellSoundId, 1, 1, 1, 0, 1);
                    }
                    }

            }

            public void onFinish() {

                // Change Timer text 00:00
                timeText.setText(getString(R.string.timer));


                if (localSound) {
                    // Play 3 bell, end of round sound.
                    soundPool.play(bell3SoundId, 1, 1, 1, 0, 1);
                }

                // Start the end of the workout.
                end();

            }

        }.start();

    }

    public void end() {

        final TextView section = (TextView) findViewById(R.id.sectionText);
        section.setText("Finished!");
        View screen = findViewById(R.id.screen);
        screen.setBackgroundColor(getColor(R.color.gold_colour));

        new CountDownTimer(1000, 1000) {

            public void onTick(long millisUntilFinished) {

            }

            public void onFinish() {


                Intent intent = new Intent();
                intent.putExtra("sendId", id);
                intent.putExtra("sendCalTime", calTime);
                intent.putExtra("sendCalTimeMin", calTimeMin);

                setResult(RESULT_OK, intent);

                finish();

            }
        }.start();

    }

    public void cancelButton(View v) {

        new AlertDialog.Builder(this)
                .setTitle("Confirm Workout Quit ")
                .setMessage("Are you sure?")
                .setPositiveButton("Quit", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        // continue with cancel
                        countDownTimer.cancel();
                        finish();
                    }
                })
                .setNegativeButton("Carry On", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }

                })// End of set negative button

                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();

    }

    public void soundButton(View v) {

        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();

        ImageButton soundButton = (ImageButton) findViewById(R.id.soundImageButton);

        if (localSound) {
            soundButton.setImageResource(R.drawable.ic_volume_off_black_24dp);
            localSound = false;
            editor.putBoolean("key_sound", false); // Storing boolean - true/false
            editor.apply(); // commit changes
        }

        else {
            soundButton.setImageResource(R.drawable.ic_volume_up_black_24dp);
            localSound  = true;
            editor.putBoolean("key_sound", true); // Storing boolean - true/false
            editor.apply(); // commit changes
        }

    }

// Androi's physical back button pressed.
    @Override
    public void onBackPressed() {
        // Harware back button press stuff here

        // Cancel timer.
        countDownTimer.cancel();

        super.onBackPressed();
    }


}
