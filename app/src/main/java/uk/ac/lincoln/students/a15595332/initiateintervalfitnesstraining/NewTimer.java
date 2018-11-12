package uk.ac.lincoln.students.a15595332.initiateintervalfitnesstraining;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.fitness.FitnessOptions;
import com.google.android.gms.fitness.data.DataType;


public class NewTimer extends AppCompatActivity {

    private SQLiteDatabaseHandler db;


    public int sendId;
    public int sendPos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_timer);


        // Find the toolbar view inside the activity layout
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        // Sets the Toolbar to act as the ActionBar for this Activity window.
        // Make sure the toolbar exists in the activity and is not null
        setSupportActionBar(toolbar);


        // Remove default title text
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        // Get access to the custom title view
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        mTitle.setText("New Timer");

        //show back arrow
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //set back icon to back arrow icon
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);

        // create our sqlite helper class
        db = new SQLiteDatabaseHandler(this);


        Intent mIntent = getIntent();
        int id = mIntent.getIntExtra("id", 0);
        int pos = mIntent.getIntExtra("pos", 0);


        if (id != 0) {

            sendId = id;
            sendPos = pos;

            // Grab all the previous data entered.
            Timers timer = db.getTimer(id);

            EditText edit = (EditText) findViewById(R.id.workoutTitle);
            edit.setText(timer.getmTitle());

            EditText prepareEdit = (EditText)findViewById(R.id.prepareInput);
            prepareEdit.setText(timer.getmPrepare());

            EditText workoutTimeEdit = (EditText)findViewById(R.id.workoutInput);
            workoutTimeEdit.setText(timer.getmWorkout());

            EditText restEdit = (EditText)findViewById(R.id.restInput);
            restEdit.setText(timer.getmRest());

            EditText cyclesEdit = (EditText)findViewById(R.id.cyclesInput);
            cyclesEdit.setText(timer.getmCycles());

        }



        /*
        Switch sw = (Switch) findViewById(R.id.googleFitSwitch);
        sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled



                } else {
                    // The toggle is disabled
                }
            }
        });

*/

    }


    /*

    int GOOGLE_FIT_PERMISSIONS_REQUEST_CODE = 0533;

    public void googleFitEnabled(){

        //Toast.makeText(this, "Enabled ", Toast.LENGTH_SHORT).show();




            FitnessOptions fitnessOptions = FitnessOptions.builder()
                    .addDataType(DataType.TYPE_LOCATION_SAMPLE, FitnessOptions.ACCESS_WRITE)
                    .addDataType(DataType.TYPE_ACTIVITY_SEGMENT, FitnessOptions.ACCESS_WRITE)
                    .build();

            Scope scopeLocation = new Scope(Scopes.FITNESS_LOCATION_READ_WRITE);
            Scope scopesActivity = new Scope(Scopes.FITNESS_ACTIVITY_READ_WRITE);


            if (!GoogleSignIn.hasPermissions(GoogleSignIn.getLastSignedInAccount(this), fitnessOptions)) {
                GoogleSignIn.requestPermissions(
                        this, // your activity
                        GOOGLE_FIT_PERMISSIONS_REQUEST_CODE,
                        GoogleSignIn.getLastSignedInAccount(this),
                        scopesActivity, scopeLocation);
            } else {
                accessGoogleFit();
            }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == GOOGLE_FIT_PERMISSIONS_REQUEST_CODE) {

                Toast.makeText(this, "Accessing Google Fit ", Toast.LENGTH_SHORT).show();

                accessGoogleFit();

            }
        }
    }


    private void accessGoogleFit() {

        Toast.makeText(this, "Accessing Google Fit ", Toast.LENGTH_SHORT).show();


    }

    */

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add_timer_menu, menu);
        return true;
    }


    //When the back arrow is pressed.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }

    public void doneButton(MenuItem item){

        Intent intent = new Intent();

        //Get workoutTitle string value.
        EditText edit = (EditText)findViewById(R.id.workoutTitle);
        String workoutTitle = edit.getText().toString();
        intent.putExtra("workoutTitleValue", workoutTitle);

        //Get prepareTime string value.
        EditText prepareEdit = (EditText)findViewById(R.id.prepareInput);
        String prepare = prepareEdit.getText().toString();
        intent.putExtra("prepareValue", prepare);

        //Get workoutTime string value.
        EditText workoutTimeEdit = (EditText)findViewById(R.id.workoutInput);
        String workoutTime = workoutTimeEdit.getText().toString();
        intent.putExtra("workoutTimeValue", workoutTime);

        //Get restTime string value.
        EditText restEdit = (EditText)findViewById(R.id.restInput);
        String rest = restEdit.getText().toString();
        intent.putExtra("restValue", rest);

        //Get workoutTime string value.
        EditText cyclesEdit = (EditText)findViewById(R.id.cyclesInput);
        String cycles = cyclesEdit.getText().toString();
        intent.putExtra("cyclesValue", cycles);

        //Get Id.
        intent.putExtra("sendId", sendId);

        intent.putExtra("sendPos", sendPos);

        setResult(RESULT_OK, intent);
        finish();


    }





}// End of class
