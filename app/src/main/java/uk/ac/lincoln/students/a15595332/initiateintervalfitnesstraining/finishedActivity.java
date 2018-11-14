package uk.ac.lincoln.students.a15595332.initiateintervalfitnesstraining;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class finishedActivity extends AppCompatActivity {

    private SQLiteDatabaseHandler db;
    public int id;
    public String totalTimeText;
    public String caloriesBurntText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finished);

        Intent intent = getIntent();
        id = intent.getIntExtra("sendId",0);

            //Toast toast =  Toast.makeText(this, "Code 2", Toast.LENGTH_SHORT); toast.show();

        db = new SQLiteDatabaseHandler(this);

        // create loaded timer
        Timers timer = db.getTimer(id);

        totalTimeText = timer.getmTotalTime();

        caloriesBurntText = timer.getmCaloriesBurnt();


        TextView totalWorkout = (TextView) findViewById(R.id.totalWorkout);
        totalWorkout.setText(totalTimeText);

        TextView caloriesBurnt = (TextView) findViewById(R.id.caloriesBurnt);
        caloriesBurnt.setText(caloriesBurntText);



    }



    public void cancelButton(View v) {

        new AlertDialog.Builder(this)
                .setTitle("Quit without saving")
                .setMessage("Are you sure?")
                .setPositiveButton("Quit", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        // continue with cancel


                        finish();

                    }
                })
                .setNegativeButton("Go back", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing

                    }

                })// End of set negative button

                .setIcon(android.R.drawable.ic_dialog_alert)

                .show();

    }


}
