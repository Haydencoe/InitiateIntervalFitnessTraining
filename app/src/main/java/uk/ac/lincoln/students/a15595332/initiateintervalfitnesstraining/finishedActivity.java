package uk.ac.lincoln.students.a15595332.initiateintervalfitnesstraining;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.CalendarContract;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.FitnessActivities;
import com.google.android.gms.fitness.FitnessOptions;
import com.google.android.gms.fitness.data.DataPoint;
import com.google.android.gms.fitness.data.DataSet;
import com.google.android.gms.fitness.data.DataSource;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Field;
import com.google.android.gms.fitness.data.Session;
import com.google.android.gms.fitness.request.SessionInsertRequest;
import com.google.android.gms.fitness.result.DataTypeResult;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;
import com.muddzdev.styleabletoast.StyleableToast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import static android.Manifest.permission.WRITE_CALENDAR;

public class finishedActivity extends AppCompatActivity {

    private SQLiteDatabaseHandler db;
    public int id;

    public String workoutTitleText;
    public String totalTimeText;
    public String caloriesBurntText;
    public String formattedDate;

    public int calTimeStart;
    public int calTimeMinStart;


    public String jtitle;
    public String uid;
    public String uname;

    private static final String TAG = "Tag";
    private GoogleApiClient mGoogleApiClient = null;
    private Session mSession;

    public int caloriesBurntInt;

    DatabaseReference rootRef,user,journalNode,userName, jnumber;

    public Calendar beginTime;
    public Calendar endTime;

    private FirebaseAuth mAuth;


    public boolean saveDeviceFlag;

    public boolean saveCloudFlag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finished);

        saveCloudFlag = false;
        saveDeviceFlag = false;


        Intent intent = getIntent();
        id = intent.getIntExtra("sendId",0);
        calTimeStart = intent.getIntExtra("sendCalTime",1);
        calTimeMinStart = intent.getIntExtra("sendCalTimeMin",1);


        Calendar c = Calendar.getInstance();

        //SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.ENGLISH);
        //SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
        //String  cal = df.format(c.getTime());



        //Get year
        SimpleDateFormat dfYear = new SimpleDateFormat("yyyy", Locale.ENGLISH);
        SimpleDateFormat dfMonth = new SimpleDateFormat("MM", Locale.ENGLISH);
        SimpleDateFormat dfDayofMonth = new SimpleDateFormat("dd", Locale.ENGLISH);

        SimpleDateFormat dfTime = new SimpleDateFormat("HH", Locale.ENGLISH);
        SimpleDateFormat dfTimeMin = new SimpleDateFormat("mm", Locale.ENGLISH);


        String  calYear = dfYear.format(c.getTime());
        String  calMonth = dfMonth.format(c.getTime());
        String  calDayofMonth = dfDayofMonth.format(c.getTime());

        String  calTime = dfTime.format(c.getTime());
        String  calTimeMin = dfTimeMin.format(c.getTime());





        int intCalYear = 0;

        try {
            intCalYear = Integer.parseInt(calYear);
        } catch (NumberFormatException nfe) {

        }

        int intCalMonth = 0;

        try {
            intCalMonth = Integer.parseInt(calMonth);
        } catch (NumberFormatException nfe) {

        }

        int intCalDay = 0;

        try {
            intCalDay = Integer.parseInt(calDayofMonth);
        } catch (NumberFormatException nfe) {

        }

        int intCalTime = 0;

        try {
            intCalTime = Integer.parseInt(calTime);
        } catch (NumberFormatException nfe) {

        }

        int intCalTimeMin = 0;

        try {
            intCalTimeMin = Integer.parseInt(calTimeMin);
        } catch (NumberFormatException nfe) {

        }

        // This converts the normal 1-12 month format to Java's Calendar format of 0-11.
        intCalMonth = (intCalMonth-1);


        // Check to see if workout is less than 1 min if it is then workout time becomes 1 min
        // As 1 min is the shortest amount taken by google fit.
        if (calTimeStart==intCalTime & calTimeMinStart==intCalTimeMin)
        {
            intCalTimeMin = intCalTimeMin+1;

        }


        beginTime = Calendar.getInstance();
        beginTime.set(intCalYear, intCalMonth, intCalDay, calTimeStart, calTimeMinStart);

        endTime = Calendar.getInstance();
        endTime.set(intCalYear, intCalMonth, intCalDay, intCalTime, intCalTimeMin);







            //Toast toast =  Toast.makeText(this, "Code 2", Toast.LENGTH_SHORT); toast.show();

        db = new SQLiteDatabaseHandler(this);

        // create loaded timer.
        Timers timer = db.getTimer(id);

        workoutTitleText = timer.getmTitle();

        totalTimeText = timer.getmTotalTime();

        caloriesBurntText = timer.getmCaloriesBurnt();

        // Get the int value of the calories burnt
        try {
            caloriesBurntInt = Integer.parseInt(caloriesBurntText);
        } catch (NumberFormatException nfe) {

        }

        TextView workoutTitle = (TextView) findViewById(R.id.workoutName);
        workoutTitle.setText(workoutTitleText);

        TextView totalWorkout = (TextView) findViewById(R.id.totalWorkout);
        totalWorkout.setText(totalTimeText);

        TextView caloriesBurnt = (TextView) findViewById(R.id.caloriesBurnt);
        caloriesBurnt.setText(caloriesBurntText);


        // Switch for saving to calendar.   *********************************************
        Switch sw = (Switch) findViewById(R.id.calSwitch);
        sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled

                    askForPermission();


                } else {
                    // The toggle is disabled
                }
            }
        });


        // Switch for saving to calendar.   *********************************************
        Switch swgf = (Switch) findViewById(R.id.gfSwitch);
        swgf.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled

                    googleFitEnabled();






                } else {
                    // The toggle is disabled
                }
            }
        });// End of switch



        final CheckBox saveDevice = (CheckBox) findViewById(R.id.saveDevice);

        saveDevice.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (((CheckBox) v).isChecked()) {

                    saveDeviceFlag = true;


                }

            }
        });

        CheckBox saveCloud = (CheckBox) findViewById(R.id.saveCloud);

        saveCloud.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (((CheckBox) v).isChecked()) {

                    saveCloudFlag = true;


                }

            }
        });




    }// End of onCreate.


    public void User(String dateOfBirth, String fullName, String nickname) {
        // ...
    }




    public void saveWorkoutButton(View v) {

        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.ENGLISH);

        formattedDate = df.format(c.getTime());


        jtitle = workoutTitleText + " - " + formattedDate;

        //*********SQLite************************
       if (saveDeviceFlag) {

           // create our sqlite helper class
           db = new SQLiteDatabaseHandler(this);
           // create new timer
           Journal journal = new Journal(jtitle, totalTimeText, caloriesBurntText, 0);

           // add them
           db.addJournal(journal);
       }

        //*********FIREBASE************************

        if (saveCloudFlag) {

            mAuth = FirebaseAuth.getInstance();

            FirebaseUser currentUser = mAuth.getCurrentUser();

            uid = currentUser.getUid();
            uname = currentUser.getDisplayName();

            //database reference pointing to root of database
            rootRef = FirebaseDatabase.getInstance().getReference();

            //database reference pointing to user node
            user = rootRef.child(uid);

            userName = user.child(uname);
            // user.child("Journal").setValue(new journalWorkout(jtitle, totalTimeText, caloriesBurntText));

            journalNode = userName.child("Journal");

            String journalId = userName.push().getKey();

            // journalWorkout workout = new journalWorkout(journalId, jtitle, totalTimeText, caloriesBurntText);

            jnumber = journalNode.child(journalId);

            //userName.child(journalId).setValue(workout);

            jnumber.child("journalId").setValue(journalId);
            jnumber.child("title").setValue(jtitle);
            jnumber.child("time").setValue(totalTimeText);
            jnumber.child("calories").setValue(caloriesBurntText);
        }






        finish();


    }// End of saveWorkoutButton method.




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


    private void askForPermission() {

        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_CALENDAR)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_CALENDAR)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.


                //This is called if user has denied the permission before
                //In this case I am just asking the permission again
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_CALENDAR}, 1);


            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_CALENDAR},
                        1);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }

        } else {
            // Permission has already been granted
           // Toast.makeText(this, "" + "WRITE_CALENDAR" + " is already granted.", Toast.LENGTH_SHORT).show();

            Toast.makeText(this, "Saving to calendar" , Toast.LENGTH_SHORT).show();

            saveToCalendar();

        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay!

                    Toast.makeText(this, "Saving to calendar" , Toast.LENGTH_SHORT).show();


                    saveToCalendar();

                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.


                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }


    public void saveToCalendar()
    {



        //This opens the built in calendar to save the workout to it.

        Intent calIntent = new Intent(Intent.ACTION_INSERT, CalendarContract.Events.CONTENT_URI);



        calIntent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, beginTime.getTimeInMillis());

        calIntent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime.getTimeInMillis());

        calIntent.putExtra(CalendarContract.Events.TITLE, "Initiate Interval Workout");

        calIntent.putExtra(CalendarContract.Events.DESCRIPTION, "You completed your Initiate Interval workout called: "+workoutTitleText);

        startActivity(calIntent);



    }


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
        Log.d(finishedActivity.this.getLocalClassName(), "authorized");

        if (mGoogleApiClient == null) {
           mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(Fitness.SESSIONS_API)
                    .addApi(Fitness.HISTORY_API)
                    .addScope(new Scope(Scopes.FITNESS_LOCATION_READ_WRITE))
                    .addScope(new Scope(Scopes.FITNESS_ACTIVITY_READ_WRITE))
                    .addConnectionCallbacks(
                            new GoogleApiClient.ConnectionCallbacks() {
                                @Override
                                public void onConnected(Bundle bundle) {
                                    Log.i(finishedActivity.this.getLocalClassName(), "Fitness Connected!!!");
                                    // Now you can make calls to the Fitness APIs.
                                    addUserDataAndroidHealth();
                                }

                                @Override
                                public void onConnectionSuspended(int i) {
                                    // If your connection to the sensor gets lost at some point,
                                    // you'll be able to determine the reason and react to it here.
                                    if (i == GoogleApiClient.ConnectionCallbacks.CAUSE_NETWORK_LOST) {
                                        Log.i(finishedActivity.this.getLocalClassName(), "Connection lost.  Cause: Network Lost.");
                                    } else if (i == GoogleApiClient.ConnectionCallbacks.CAUSE_SERVICE_DISCONNECTED) {
                                        Log.i(finishedActivity.this.getLocalClassName(),
                                                "Connection lost.  Reason: Service Disconnected");
                                    }
                                }
                            }
                    )
                    .enableAutoManage(this, 0, new GoogleApiClient.OnConnectionFailedListener() {
                        @Override
                        public void onConnectionFailed(ConnectionResult result) {
                            Log.i(finishedActivity.this.getLocalClassName(), "Google Play services connection failed. Cause: " + result.toString());

                            View parentLayout = findViewById(android.R.id.content);
                            Snackbar.make(parentLayout, "Exception while connecting to Google Play services: " + result.getErrorMessage(), Snackbar.LENGTH_INDEFINITE).show();
                        }
                    })
                    .build();
        }
    }

    private final String SESSION_NAME = "Initiate Interval Workout";
    SessionInsertRequest insertRequest;





    private void addUserDataAndroidHealth() {


        // Setting a start and end time.
        long startTimeLong = beginTime.getTimeInMillis();

        long endTimeLong = endTime.getTimeInMillis();




        mSession = new Session.Builder()
                .setName(SESSION_NAME)
                .setIdentifier(getString(R.string.app_name) + " " + System.currentTimeMillis())
                .setDescription("You completed the Initiate Interval workout: " + workoutTitleText)
                .setStartTime((long) startTimeLong, TimeUnit.MILLISECONDS)
                .setEndTime((long) endTimeLong, TimeUnit.MILLISECONDS)
                .setActivity(FitnessActivities.HIGH_INTENSITY_INTERVAL_TRAINING)
                .build();

        DataSource caloriesDataSource = new DataSource.Builder()
                .setAppPackageName(this.getPackageName())
                .setDataType(DataType.AGGREGATE_CALORIES_EXPENDED)
                .setName("Total Calories Burned")
                .setType(DataSource.TYPE_RAW)
                .build();
        DataSet caloriesDataSet = DataSet.create(caloriesDataSource);
        DataPoint caloriesDataPoint = caloriesDataSet.createDataPoint()
                .setTimeInterval((long) startTimeLong, (long) endTimeLong, TimeUnit.MILLISECONDS);
        caloriesDataPoint.getValue(Field.FIELD_CALORIES).setFloat((float) caloriesBurntInt);
        caloriesDataSet.add(caloriesDataPoint);
        insertRequest = new SessionInsertRequest.Builder()
                .setSession(mSession)

                .addDataSet(caloriesDataSet)
                .build();






        new InsertIUserHistoryDataGoogleFit().execute("", "", "");
    }




    private class InsertIUserHistoryDataGoogleFit extends AsyncTask<String, Integer, String> {

        PendingResult<com.google.android.gms.common.api.Status> pendingResult;

        com.google.android.gms.common.api.Status insertStatus;

        protected String doInBackground(String... urls) {

            pendingResult = Fitness.SessionsApi.insertSession(mGoogleApiClient, insertRequest);

            pendingResult.setResultCallback(new ResultCallback<com.google.android.gms.common.api.Status>() {
                @Override
                public void onResult(com.google.android.gms.common.api.Status status) {
                    insertStatus = status;
                    if (status.isSuccess()) {
                        // USER RECORD INSERTED SUCCESSFULLY.
                        StyleableToast.makeText(finishedActivity.this, "Saved to Google Fit", Toast.LENGTH_LONG, R.style.mytoast).show();

                        Log.i(finishedActivity.this.getLocalClassName(), "Successfully managed to insert workout session: " + "YESSSSS");

                    } else {
                        Log.i(finishedActivity.this.getLocalClassName(), "Failed to insert workout session: " + status.getStatusMessage());
                    }
                }
            });
            return "";
        }
        protected void onProgressUpdate(Integer... progress) {
        }
        protected void onPostExecute(String result) {
        }
    }





}// End of class.

//push creates a unique id in database
// demoRef.push().setValue(jtitle);
//user.child(uid).setValue(user);



  /*
        Calendar cal = Calendar.getInstance();
        Date now = new Date();
        //cal.setTime(now);
        //cal.add(Calendar.MINUTE, -10);
        */


/*
    private void accessGoogleFit() {

        Toast.makeText(this, "Accessing Google Fit ", Toast.LENGTH_SHORT).show();

        savingGoogleFit();

    }

*/

/*
    public void savingGoogleFit(){

        // Create a session with metadata about the activity.
        Session session = new Session.Builder()
                .setName(workoutTitleText)
                .setDescription("Initiate Interval Workout")
                .setIdentifier("UniqueIdentifierHere")
                .setActivity(FitnessActivities.HIGH_INTENSITY_INTERVAL_TRAINING)
                .setStartTime(1100, TimeUnit.MILLISECONDS)
                .setEndTime(1200, TimeUnit.MILLISECONDS)
                .build();


        DataSource caloriesDataSource = new DataSource.Builder()
                .setAppPackageName(this.getPackageName())
                .setDataType(DataType.AGGREGATE_CALORIES_EXPENDED)
                .setName("Total Calories Burned")
                .setType(DataSource.TYPE_RAW)
                .build();
        DataSet caloriesDataSet = DataSet.create(caloriesDataSource);
        DataPoint caloriesDataPoint = caloriesDataSet.createDataPoint()
                .setTimeInterval((long) 1100, (long) 1200, TimeUnit.MILLISECONDS);
        caloriesDataPoint.getValue(Field.FIELD_CALORIES).setFloat((float) caloriesBurntInt);
        caloriesDataSet.add(caloriesDataPoint);



        // Build a session insert request
        SessionInsertRequest insertRequest = new SessionInsertRequest.Builder()
                .setSession(session)
                .addDataSet(caloriesDataSet)
                .build();



        // Then, invoke the Sessions API to insert the session and await the result,
        // which is possible here because of the AsyncTask. Always include a timeout when
        // calling await() to avoid hanging that can occur from the service being shutdown
        // because of low memory or other conditions.
        Log.i(TAG, "Inserting the session in the History API");
        return Fitness.getSessionsClient(this, GoogleSignIn.getLastSignedInAccount(this))
                .insertSession(insertRequest)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // At this point, the session has been inserted and can be read.
                        Log.i(TAG, "Session insert was successful!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.i(TAG, "There was a problem inserting the session: " +
                                e.getLocalizedMessage());
                    }
                });



    }
*/


 /*
        Intent calIntent = new Intent(Intent.ACTION_INSERT);
        calIntent.setType("vnd.android.cursor.item/event");

        calIntent.putExtra(CalendarContract.Events.TITLE, "Initiate Interval Workout");

        calIntent.putExtra(CalendarContract.Events.DESCRIPTION, workoutTitleText);

        GregorianCalendar calDate = new GregorianCalendar(intCalYear, intCalMonth, intCalDay);

        calIntent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, calTime);

        calIntent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, calTime);

        startActivity(calIntent);
*/

/*


 Session session = new Session.Builder()
                            .setName(workoutTitleText)
                            .setIdentifier(getString(R.string.app_name) + " " + System.currentTimeMillis())
                            .setDescription("Initiate Interval Workout")
                            .setStartTime(1100, TimeUnit.MILLISECONDS)
                            .setEndTime(1200, TimeUnit.MILLISECONDS)
                            .setActivity(FitnessActivities.HIGH_INTENSITY_INTERVAL_TRAINING)
                            .build();

                    SessionInsertRequest insertRequest = new SessionInsertRequest.Builder()
                            .setSession(session)
                           // .addDataSet(speedDataSet)
                           // .addDataSet(activityDataSet)
                            .build();

                    PendingResult<Status> pendingResult =
                            Fitness.SessionsApi.insertSession(mGoogleApiClient, insertRequest);

                    pendingResult.setResultCallback(new ResultCallback<Status>() {
                        @Override
                        public void onResult(Status status) {
                            if( status.isSuccess() ) {
                                Log.i("Tuts+", "successfully inserted running session");
                            } else {
                                Log.i("Tuts+", "Failed to insert running session: " + status.getStatusMessage());
                            }
                        }
                    });



*/