package uk.ac.lincoln.students.a15595332.initiateintervalfitnesstraining;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import net.sqlcipher.database.SQLiteDatabase;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

import static java.lang.Thread.sleep;

public class PlayingActivity extends AppCompatActivity {

    private SQLiteDatabaseHandler db;

    public String title;
    public int prepare;
    public int workout;
    public int rest;
    public int cycles;

    public int counter;
    public int id;

    public CountDownTimer countDownTimer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setContentView(R.layout.activity_playing);

        counter = 1;



        Intent mIntent = getIntent();
        id = mIntent.getIntExtra("id", 0);


        db = new SQLiteDatabaseHandler(this);

        // create loaded timer
        Timers timer = db.getTimer(id);

        title = timer.getmTitle();

        try {
            prepare = Integer.parseInt(timer.getmPrepare());
        } catch (NumberFormatException nfe) {

        }

        try {
            workout = Integer.parseInt(timer.getmWorkout());
        } catch (NumberFormatException nfe) {

        }

        try {
            rest = Integer.parseInt(timer.getmRest());
        } catch (NumberFormatException nfe) {

        }

        try {
            cycles = Integer.parseInt(timer.getmCycles());
        } catch (NumberFormatException nfe) {

        }

        prepare = prepare+1;
        workout = workout+1;
        rest = rest+1;

        //Setting the title of the workout.
        TextView titleText = (TextView) findViewById(R.id.titleText);

        titleText.setText(title);


        // Setting the section of the workout.
        TextView section = (TextView) findViewById(R.id.sectionText);


        TextView cycleText = (TextView) findViewById(R.id.cycleText);
        cycleText.setText(String.format(Locale.ENGLISH,"%d/%d", counter,cycles ));



        //*****  start the prepare *********************************

        section.setText("Prepare");
        View screen = findViewById(R.id.screen);
        screen.setBackgroundColor(getColor(R.color.prepare_colour));




            // Start the prepare countdown.
         startTimer((prepare)*1000);




    }// End of onCreate method.




    private void startTimer(final int noOfMils) {


        View screen = findViewById(R.id.screen);
        TextView section = (TextView) findViewById(R.id.sectionText);

     countDownTimer = new CountDownTimer(noOfMils, 1000) {

            //Setting the timer part of the workout.
            TextView timeText = (TextView) findViewById(R.id.timeText);

            public void onTick(long millisUntilFinished) {

                long s = (int) (millisUntilFinished / 1000) % 60 ;
                long m = (int) ((millisUntilFinished / (1000*60)) % 60);

                timeText.setText( String.format(Locale.ENGLISH,"%02d:%02d", m,s));


            }

            public void onFinish() {

                timeText.setText(getString(R.string.timer));//Change Timer text


                playWorkout();

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


        countDownTimer = new CountDownTimer((workout)*1000, 1000) {

            //Setting the timer part of the workout.
            TextView timeText = (TextView) findViewById(R.id.timeText);

            public void onTick(long millisUntilFinished) {

                long s = (int) (millisUntilFinished / 1000) % 60 ;
                long m = (int) ((millisUntilFinished / (1000*60)) % 60);

                timeText.setText( String.format(Locale.ENGLISH,"%02d:%02d", m,s));
            }

            public void onFinish() {

                timeText.setText(getString(R.string.timer));//Change Timer text

                playRest();

            }



        }.start();


    }// End of method.


    public void playRest() {


        // Setting the section of the workout.
        TextView section = (TextView) findViewById(R.id.sectionText);
        section.setText("Rest");
        View screen = findViewById(R.id.screen);
        screen.setBackgroundColor(getColor(R.color.rest_colour));
        startTimer(rest * 1000);


        countDownTimer = new CountDownTimer((rest)*1000, 1000) {

            //Setting the timer part of the workout.
            TextView timeText = (TextView) findViewById(R.id.timeText);

            public void onTick(long millisUntilFinished) {

                long s = (int) (millisUntilFinished / 1000) % 60 ;
                long m = (int) ((millisUntilFinished / (1000*60)) % 60);

                timeText.setText( String.format(Locale.ENGLISH,"%02d:%02d", m,s));
            }

            public void onFinish() {

                timeText.setText(getString(R.string.timer));//Change Timer text

                counter++;


                if (counter>cycles) {
                    countDownTimer.cancel();

                    View screen = findViewById(R.id.screen);
                    screen.setBackgroundColor(getColor(R.color.finished_colour));
                    timeText.setText("Done!");

                    end();
                }


                if (counter <= cycles) {
                    TextView cycleText = (TextView) findViewById(R.id.cycleText);

                    cycleText.setText(String.format(Locale.ENGLISH, "%d/%d", counter, cycles));

                }

            }



        }.start();




    }


    public void end(){

        new CountDownTimer(3000, 1000) {

            public void onTick(long millisUntilFinished) {

            }

            public void onFinish() {

                Intent intent = new Intent();
                intent.putExtra("sendId", id);
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


}
